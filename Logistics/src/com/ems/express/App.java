package com.ems.express;

import im.fir.sdk.FIR;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.Config;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.arvin.koalapush.potter.Kpush;
import com.arvin.koalapush.potter.ReceivedListener;
import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;
import com.ems.express.bean.MessageCenterItemBean;
import com.ems.express.core.service.ChatService;
import com.ems.express.util.DBHelper;
import com.ems.express.util.DeviceUuidFactory;
import com.ems.express.util.NotificationUtil;
import com.ems.express.util.PreUtils;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

public class App extends FrontiaApplication {
	private static final String TAG = "App";
	public static ToastUtil mToastUtil;
	private static RequestQueue sQueue;
	public static DBHelper dbHelper;
	public static SQLiteDatabase db;
	public static DeviceUuidFactory factory;
	public static String loginId = "";
	public static int bindPort;
	public static String address;
	public static boolean isConnected = false;
	private static ChatService mChatService = null;
	public static List<MessageCenterItemBean> mBaiduPushData = new ArrayList<MessageCenterItemBean>();
	private String mDefaultWords = "请您上午过来取件__请您12点之后过来取件";
	public static final String DEFAULT_COMMON_WORDS_LOADED = "words_loaded";

	
	static ServiceConnection mChatServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.e("chatService", name.getClassName()+" is Connected");
			Log.e("chatService", "ChatService is DisConnected");
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.e("chatService", name.getClassName()+" is Connected");
			if(service instanceof ChatService.ChatBinder){
				mChatService = ((ChatService.ChatBinder)service).getService();
			}
		}
	};
	
	static ConcurrentLinkedQueue<ReceivedListener> listeners = new ConcurrentLinkedQueue<ReceivedListener>();

	public static ReceivedListener sReceivedListener = new ReceivedListener() {

		@Override
		public String onReceived(Object arg0) {
			String bo = "";
			Log.d(TAG, "begin onReceived:" + arg0);
			for (ReceivedListener listener : listeners) {
				bo = listener.onReceived(arg0);
			}
			return bo;
		}
	

		@Override
		public void onError(Object arg0) {
			Log.d(TAG, "onError:" + arg0);
			for (ReceivedListener listener : listeners) {
				listener.onError(arg0);
			}
		}
	};
	
	@Override
	public void onCreate() {
		//BugHD调用
		FIR.init(this);
		//友盟统计
		MobclickAgent.updateOnlineConfig(this);
		//开启默认的页面统计
		MobclickAgent.openActivityDurationTrack(true);
		super.onCreate();
		Intent chatServiceIntent = new Intent(this,ChatService.class);
		this.getApplicationContext().bindService(chatServiceIntent, mChatServiceConnection, Context.BIND_AUTO_CREATE);
		//初始化百度Push
//		PushManager.startWork(getApplicationContext(),
//                PushConstants.LOGIN_TYPE_API_KEY,
//                com.ems.express.baidupush.Utils.getMetaValue(this, "api_key"));
		FrontiaApplication.initFrontiaApplication(this);
		SDKInitializer.initialize(this);
		sQueue = Volley.newRequestQueue(this);
		sQueue.start();
		factory = new DeviceUuidFactory(this);
		dbHelper = new DBHelper(this, "ems.db");
		db = dbHelper.getWritableDatabase();
		initImageLoader(this);

        SpfsUtil.init(this);
        PreUtils.init(this);
        mToastUtil = new ToastUtil(this);
        //创建城市表
        dbHelper.createCity(db);
        dbHelper.createProvince(db);
        dbHelper.createCounty(db);
        dbHelper.createSenderInfo(db);
        dbHelper.creatOrderTable(db);
        
        
		// login();
		setUpPush();
		if (!SpfsUtil.getBoolean(DEFAULT_COMMON_WORDS_LOADED)) {
			ArrayList<String> wordsList = new ArrayList<String>();
			String[] wordsArray = mDefaultWords.split("__");
			for (String string : wordsArray) {
				wordsList.add(string);
			}
			SpfsUtil.saveCommonWords(wordsList);
			SpfsUtil.putBoolean(DEFAULT_COMMON_WORDS_LOADED, true);
		}
	}

	private void setUpPush() {
		Kpush.getInstence().create(this).showLog(true)
				.setTimeout(30).setRecoverTimes(5).close(false);
		Kpush.getInstence().setListener(sReceivedListener);
//		Log.e("IM", "本机ClientId："+Kpush.getInstence().getClientId());
	}

	private void initImageLoader(Context context) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Config.RGB_565).considerExifParams(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(15)
				.threadPoolSize(4).defaultDisplayImageOptions(options).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static RequestQueue getQueue() {
		return sQueue;
	}

	public static SQLiteDatabase getWritableDB() {
		return dbHelper.getWritableDatabase();
	}

	public static SQLiteDatabase getReadableDB() {
		return dbHelper.getReadableDatabase();
	}

	public static void addListener(ReceivedListener listener) {
		listeners.add(listener);
	}

	public static void removeListener(ReceivedListener listener) {
		listeners.remove(listener);
	}

	public static ChatService getmChatService() {
		return mChatService;
	}
	
}

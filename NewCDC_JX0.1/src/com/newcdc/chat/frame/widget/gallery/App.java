package com.newcdc.chat.frame.widget.gallery;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.newcdc.chat.net.LoginResp;
import com.newcdc.chat.net.Request;
import com.newcdc.chat.potter.MinaClientHanlder;
import com.newcdc.chat.util.Constant;
import com.newcdc.chat.util.DBHelper;
import com.newcdc.chat.util.DeviceUuidFactory;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class App extends Application {
	private static RequestQueue sQueue;
	private static DBHelper dbHelper;
	public static SQLiteDatabase db;
	public static DeviceUuidFactory factory;
	public static String loginId = "";
	public static int bindPort;
	public static String address;
	public static boolean isConnected = false;
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				new Thread(new Thread() {
					@Override
					public void run() {
						socketServer();
					}

				}).start();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		sQueue = Volley.newRequestQueue(this);
		sQueue.start();
		factory = new DeviceUuidFactory(this);
		dbHelper = new DBHelper(this, "ems.db");
		db = dbHelper.getWritableDatabase();
		initImageLoader(this);
		// login();
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

	public void login() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId", App.loginId);
		Request.todo(params, LoginResp.class, Constant.Login, null,
				new Response.Listener<LoginResp>() {

					@Override
					public void onResponse(LoginResp response) {
						bindPort = response.getServerDevice().getPort();
						address = response.getServerDevice().getIpAddress();
						System.out.println("bindPort:" + bindPort + "address:"
								+ address);
						new Thread(new Thread() {
							@Override
							public void run() {
								socketServer();
							}

						}).start();
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "get error", 1)
								.show();
					}
				});
	}

	public static void socketServer() {
		// 创建一个socket连接
		NioSocketConnector connector = new NioSocketConnector();
		// 消息核心处理器
		connector.setHandler(new MinaClientHanlder());
		// 设置链接超时时间
		connector.setConnectTimeoutCheckInterval(30);
		// 获取过滤器链
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		ProtocolCodecFilter filter = new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8")));
		// 添加编码过滤器 处理乱码、编码问题
		chain.addLast("objectFilter", filter);

		// 连接服务器，知道端口、地址
		ConnectFuture cf = connector.connect(new InetSocketAddress(address,
				bindPort));
		// 等待连接创建完成
		cf.awaitUninterruptibly();
		if (cf.isConnected()) {
			isConnected = true;
			System.out.println("等待连接断开");
			// 等待连接断开
			cf.getSession().getCloseFuture().awaitUninterruptibly();
		} else {
			System.out.println("连接断开");
			isConnected = false;
		}
		System.out.println("----dispose");
		connector.dispose();
		System.out.println("dispose-----");
	}

}

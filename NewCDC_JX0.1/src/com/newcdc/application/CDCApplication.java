package com.newcdc.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.arvin.koalapush.net.resp.OfflineMessageBean;
import com.arvin.koalapush.potter.Kpush;
import com.arvin.koalapush.potter.ReceivedListener;
import com.arvin.koalapush.util.LogUtil;
import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.activity.clcttask.CollectionActivity_JX;
import com.newcdc.asynctask.GatherAsyncTask;
import com.newcdc.asynctask.GatherAsyncTask.onPostExecuteListener_Gather;
import com.newcdc.asynctask.GatherAsyncTask.onPreExecuteListener_Gather;
import com.newcdc.db.ChatMessageDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.ChatMessageTabBean;
import com.newcdc.model.QueryTaskBean;
import com.newcdc.service.DeliverService;
import com.newcdc.service.ResetPushServise;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.XFAudio;
import com.newcdc.util.DateTimeUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/*
 * 如果您的工程中实现了Application的继承类，那么，您需要将父类改为com.baidu.frontia.FrontiaApplication。
 * 如果您没有实现Application的继承类，那么，请在AndroidManifest.xml的Application标签中增加属性： 
 * <application android:name="com.baidu.frontia.FrontiaApplication"
 * 。。。
 */
public class CDCApplication extends FrontiaApplication {
	private static RequestQueue sQueue;
	public static Kpush push = null;
	private ChatMessageDao mChatMessageDao = null;
	private SharePreferenceUtilDaoFactory spDao;
	private List<ChatMessageTabBean> list;
	private QueryTaskBean bean = new QueryTaskBean();
	private MediaPlayer mPlayer = null;// 用于语音播放

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(this);
		startService(new Intent(getApplicationContext(), ResetPushServise.class));
		// 捕获异常
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
		// 以下是您原先的代码实现，保持不变
		mChatMessageDao = DeliverDaoFactory.getInstance().getChatMessageDao(
				getApplicationContext());
		spDao = SharePreferenceUtilDaoFactory
				.getInstance(getApplicationContext());
		sQueue = Volley.newRequestQueue(this);
//		sQueue.start();
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
		if (Utils.isNetworkAvailable(getApplicationContext())) {
			Log.e("tag", "initpush");
			try {
				InitKpush();
			} catch (Exception e) {
			}
		}
		initImageLoader(this);
	}

	/**
	 * 初始化ImageLoader
	 * 
	 * @param context
	 */
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

	/**
	 * 初始化聊天推送
	 * 
	 * @param context
	 */
	private void InitKpush() {
		push = Kpush.getInstence().create(getApplicationContext())
				.showLog(true).setTimeout(30).setRecoverTimes(5);
		push.setListener(new ReceivedListener() {
			// 如果接收到的是图片和语音 你就返回 message_id 如果接收的不是图片和语音 就返回 noIsSync
			@Override
			public String onReceived(Object arg0) {
				spDao.setCLIENTID(push.getClientId());
				if ("SDK初始化成功".equals(arg0.toString())) {
					LogUtils.e("=============" + arg0.toString());
//					Toast.makeText(getApplicationContext(), "初始化成功"+arg0.toString(),
//							Toast.LENGTH_SHORT).show();
					spDao.setCLIENTID(push.getClientId());
					return "noIsSync";
				}
				List<OfflineMessageBean> list = (List<OfflineMessageBean>) arg0;
				for (OfflineMessageBean var : list) {
					int type = Integer.valueOf(var.getMessage_type());
					switch (type) {
					case 0:
						getMsgMess(var.getMessage_type(), var);
						break;
					case 1:
						getMsgMess(var.getMessage_type(), var);
						break;
					case 2:
						getMsgMess(var.getMessage_type(), var);
						break;
					case 3:
						// StringBuffer sb = new StringBuffer();
						// 消息id
						// sb.append("Pkid=" + var.getPkid()).append(",")
						// .append("MessageType=" + var.getMessage_type())
						// .append(",")
						// .append("Source=" + var.getSource())
						// .append(",")
						// .append("Target=" + var.getTarget())
						// .append(",")
						// .append("Content=" + var.getContent())
						// .append(",")
						// .append("Create_time=" + var.getCreate_time());
						// doEMSOperate(var.getContent(), sb.toString());
						doEMSOperate(var.getContent());
						break;

					default:
						break;
					}

				}
				return "";
			}

			@Override
			public void onError(Object arg0) {

			}
		});
	}

	/**
	 * 
	 */
	protected void getMsgMess(String Type, OfflineMessageBean var) {
		if (var != null) {
			ArrayList<ChatMessageTabBean> Msglist = new ArrayList<ChatMessageTabBean>();
			ChatMessageTabBean msgBean = null;
			int type = Integer.valueOf(Type);
			String urlString = "";
			msgBean = new ChatMessageTabBean();
			int MessageId = mChatMessageDao.getMesNo();
			msgBean.setMessageId(MessageId);
			switch (type) {
			case 0:
				urlString = var.getContent();
				msgBean.setMessagetype("0");
				break;
			case 1:
				urlString = var.getUrl();
				LogUtils.e("接收到的图片地址是：   " + urlString);
				msgBean.setMessagetype("1");
				downImg(urlString);
				break;
			case 2:
				urlString = var.getUrl();
				LogUtils.e("接收到的语音地址是：   " + urlString);
				msgBean.setMessagetype("2");
				dowloadSpeech(urlString, MessageId);
				break;
			default:
				break;
			}
			msgBean.setUrl(urlString);
			msgBean.setPkid("" + var.getPkid());
			String CLIENTID = var.getSource();
			list = new ArrayList<ChatMessageTabBean>();
			list = mChatMessageDao.querySourceList(CLIENTID);
			String name = "";
			String sid = "";
			if (0 == list.size()) {
				// 消息列表数量是0时 表示没有用户的名字和头像信息
				// 需要请求服务器查询
				ChatMesTask task = new ChatMesTask();
				task.execute(Global.FINDCUSTOMERMESSAGE + "channel_id="
						+ CLIENTID, CLIENTID);
			} else {
				name = list.get(0).getSourcename();
				sid = list.get(0).getSourcename();
			}
			msgBean.setSource(CLIENTID);
			msgBean.setSourcename(name);
			msgBean.setSid(sid);
			msgBean.setSndStatus("null");
			Msglist.add(msgBean);
			mChatMessageDao.insertChatMessage(Msglist);
			XFAudio audio = new XFAudio(getApplicationContext(),
					"您有新的会话消息，请及时查看");
			audio.startSay();
			// 刷新回话窗口
			Intent intent = new Intent("msg.success");
			intent.putExtra("msg", "notread");
			getApplicationContext().sendBroadcast(intent);
		}
	}

	/**
	 * EMS业务处理
	 */
	public void doEMSOperate(String content) {
		final Context context = getApplicationContext();
		try {
			Log.e("tag", "EMS业务处理=" + content);
			org.json.JSONObject obj = new org.json.JSONObject(content);
			final int messageType = obj.getInt("messageType");
			Log.e("messageType---", messageType + "");
			switch (messageType) {
			case Constant.PUSH_TYPE_DELIVERTASK:// 有新的下段信息
				SharePreferenceUtilDaoFactory shareDao = new SharePreferenceUtilDaoFactory();
				org.json.JSONObject body = obj.getJSONObject("content")
						.getJSONObject("body").getJSONObject("success");
				String taskCount = body.getString("mount");
				String serialNumber = body.getString("serialNumber");
				String taskNum = body.getString("taskNum");
				// 存储任务流水信息
				shareDao.setDeliverTaskInformation(taskNum + "," + serialNumber
						+ "," + taskCount);
				// 启动名址查询服务
				startService(new Intent(context, DeliverService.class));
				// DeliverAsyncTask deliverAsyncTask = new DeliverAsyncTask(
				// new onPostExecuteListener() {
				//
				// @Override
				// public void onPostExecute(int result) {
				// sendDownCompleteBroadCast(context, result);
				// // 重启定位服务
				// Intent locationService = new Intent(context,
				// LocationService.class);
				// context.stopService(locationService);
				// context.startService(locationService);
				// }
				// }, new onPreExecuteListener() {
				//
				// @Override
				// public void onPreExecute() {
				// Utils.sendTitleBroadcast(context,
				// "有新的下段信息，正在为您更新..");
				// }
				// }, context);
				// deliverAsyncTask.execute();
				break;
			case Constant.PUSH_TYPE_CLCTTASK:// 有新的派揽信息
				Log.e("messageType---", messageType + "");
				if (Global.isLan) {
					new GatherAsyncTask(context,
							new onPostExecuteListener_Gather() {

								@Override
								public void onPostExecute(String result) {
									sendDownCompleteBroadCast_Gather(context,
											result);
								}
							}, new onPreExecuteListener_Gather() {

								@Override
								public void onPreExecute() {
									Utils.sendTitleBroadcast(context,
											"有新的派揽信息，正在为您更新..");
								}
							}).execute();
				}
				break;
			case Constant.PUSH_TYPE_CLCTTASK2:// 智能平台新的派揽信息
				org.json.JSONObject jsonContent = obj.getJSONObject("content");
				String result = jsonContent.getString("result");
				LogUtils.e("result..........10   " + result);
				if ("1".equals(result.trim())) {
					org.json.JSONObject jsonBody = jsonContent
							.getJSONObject("body");
					org.json.JSONObject jsonSuccess = jsonBody
							.getJSONObject("success");
					org.json.JSONArray taskNums = jsonSuccess
							.getJSONArray("task");
					List<String> tmpList = new ArrayList<String>();
					if (taskNums != null && taskNums.length() != 0) {
						for (int i = 0; i < taskNums.length(); i++) {
							org.json.JSONObject task = taskNums
									.getJSONObject(i);
							String taskNum1 = task.getString("taskNum");
							// if (taskStatus.equals("5")
							// || taskStatus.equals("6")) {
							// // 该if语句是删除任务
							//
							// } else {
							tmpList.add(taskNum1);
							// }

						}
						bean.setTaskNums(tmpList);
					}
					if (bean != null && bean.getTaskNums() != null
							&& bean.getTaskNums().size() > 0) {
						final String jsonString = JSON.toJSONString(bean);
						final int count = bean.getTaskNums().size();
						// Log.e("jsonString--", jsonString);
						Log.e("conut--10", count + "");
						new Thread() {
							@Override
							public void run() {
								long startTimeMillis = System
										.currentTimeMillis();// 请求开始时间
								String operate_time = Utils.getCurrTime();// 操作时间
								long batch_no = new Date().getTime();
								String result = com.newcdc.tools.NetHelper
										.doPostJson(Global.ZHINENGCLC_URL,
												jsonString, "json");
								LogUtils.e("result......10   " + result);
								try {
									JsonUtils.parseGatherMessageJsonzhineng(
											context, result, startTimeMillis,
											operate_time, batch_no);
									sendDownCompleteBroadCast1(
											getApplicationContext(), count);
									send();
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
						}.start();
					}
				}
				break;
			case Constant.PUSH_TYPE_CLCTTASK3:// 撤单或催单
				org.json.JSONObject jsonContent1 = obj.getJSONObject("content");
				String result1 = jsonContent1.getString("result");
				Log.e("tag", "=" + jsonContent1.toString());
				Log.e("result..........11", result1);
				if ("1".equals(result1.trim())) {
					org.json.JSONObject jsonBody = jsonContent1
							.getJSONObject("body");
					org.json.JSONObject jsonSuccess = jsonBody
							.getJSONObject("success");
					final String orderStatus = jsonSuccess
							.getString("orderStatus");
					if ("9".equals(orderStatus)) {
						final String orderNo = jsonSuccess.getString("orderNo");
						DeliverDaoFactory.getInstance()
								.getGather_msgdao(getApplicationContext())
								.deletereservenum(orderNo);
						sendDownCompleteBroadCast2(getApplicationContext(), 1);
						XFAudio xfAudio = new XFAudio(getApplicationContext(),
								"派单号" + orderNo + "已删除，请注意查收");
						xfAudio.startSay();
					} else if ("10".equals(orderStatus)) {
						final String orderNo = jsonSuccess.getString("orderNo");
						Log.e("orderStatus", orderStatus);
						DeliverDaoFactory.getInstance()
								.getGather_msgdao(getApplicationContext())
								.updateRemind(orderNo, "1");
						sendDownCompleteBroadCast2(getApplicationContext(), 1);
						XFAudio xfAudio = new XFAudio(getApplicationContext(),
								"派单号" + orderNo + "客户催单");
						xfAudio.startSay();
					}
				}
				break;
			case Constant.PUSH_TYPE_CLCTTASK4:// 任务从分派
				org.json.JSONObject jsonContent4 = obj.getJSONObject("content");
				String result4 = jsonContent4.getString("result");
				Log.e("tag", "=" + jsonContent4.toString());
				Log.e("result..........12", result4);
				if ("1".equals(result4.trim())) {
					org.json.JSONObject jsonBody = jsonContent4
							.getJSONObject("body");
					org.json.JSONObject jsonSuccess = jsonBody
							.getJSONObject("success");
					org.json.JSONArray taskNums = jsonSuccess
							.getJSONArray("task");
					if (taskNums != null && taskNums.length() != 0) {
						for (int i = 0; i < taskNums.length(); i++) {
							org.json.JSONObject task = taskNums
									.getJSONObject(i);
							String orderNo = task.getString("orderNo");
							String taskStatus = task.getString("taskStatus");
							// if (taskStatus.equals("5")
							// || taskStatus.equals("6")) {
							// 该if语句是删除任务
							DeliverDaoFactory.getInstance()
									.getGather_msgdao(getApplicationContext())
									.deletereservenum(orderNo);
							sendDownCompleteBroadCast2(getApplicationContext(),
									1);
							XFAudio xfAudio = new XFAudio(
									getApplicationContext(), "派单号" + orderNo
											+ "已删除，请注意查收");
							xfAudio.startSay();
							// }
						}
					}
				}
				break;
			case Constant.PUSH_TYPE_PAYMENT:// 支付信息
				org.json.JSONObject jsonContent5 = obj.getJSONObject("content");
				String result5 = jsonContent5.getString("result");
				Log.e("gongjie", "=" + jsonContent5.toString());
				Log.e("gongjie..........7", result5);
				if ("1".equals(result5.trim())) {
					org.json.JSONObject jsonBody = jsonContent5
							.getJSONObject("body");
					org.json.JSONObject jsonSuccess = jsonBody
							.getJSONObject("success");
				String senderName =jsonSuccess.getString("senderName");
				String senderPhoneNum =jsonSuccess.getString("senderPhoneNum");
				String orderNum =jsonSuccess.getString("orderNum");
				double actFee =jsonSuccess.getDouble("actFee");
//				String payType =jsonSuccess.getString("payType");
				String messageTime = DateTimeUtil.validateDateTime(new Date());
				UserInfoUtils infoUtils = new UserInfoUtils(getApplicationContext());
				infoUtils.getUserName();
				String orgCode = infoUtils.getUserName();
//				orgCode=	jsonSuccess.getString("orgCode");
				String usercode = infoUtils.getUserDelvorgCode();
//				usercode =jsonSuccess.getString("usercode");
				DeliverDaoFactory.getInstance().getPaymentDao(getApplicationContext()).insertPaymentMessage("1", messageTime, "7", orgCode, usercode, actFee, orderNum, senderPhoneNum, senderName);
				//发送广播
				XFAudio xfAudio = new XFAudio(
						getApplicationContext(), "订单号" + orderNum
								+ "已支付，请注意查收");
				xfAudio.startSay();
				sendReceiver();
				}
				break;
			default:
				break;
			}
		} catch (org.json.JSONException e) {
			Log.e("推送-------EMS", e.getMessage());
		}
	}

	/**
	 * 更新完毕发送通知
	 */
	public void send() {
		// 标题
		String title = "派揽信息";
		// 内容
		String content = "你有新的揽收信息，请查收";
		// 1.得到NotificationManager
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 2.实例化一个通知，指定图标、概要、时间
		Notification n = new Notification(R.drawable.ic_launcher, "通知",
				System.currentTimeMillis());
		// 3.指定通知的标题、内容和intent
		Intent intent = new Intent(this, CollectionActivity_JX.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		n.setLatestEventInfo(this, title, content, pi);
		// 指定声音
		n.defaults = Notification.DEFAULT_SOUND;
		// 5.发送通知不消失
		// n.flags = Notification.FLAG_NO_CLEAR;
		// 6.发送通知消失
		n.flags = Notification.FLAG_AUTO_CANCEL;
		// 4.发送通知
		nm.notify(1, n);
	}

	/**
	 * 更新完毕发送广播
	 */
	public static void sendDownCompleteBroadCast(Context context, int result) {
		if (result > 0) {
			try {
				Toast.makeText(context, "更新完毕，共" + result + "条下段数据",
						Toast.LENGTH_SHORT).show();
				Utils.sendDownCompleteBroadcast(context, result,
						Constant.PUSH_TYPE_DELIVERTASK);
			} catch (Exception e) {
				// Utils.sendDownCompleteBroadcast(context, "请求服务器失败",
				// Constant.PUSH_TYPE_DELIVERTASK);
			}
		} else {
			// Toast.makeText(context, "请求服务器失败，请检查网络",
			// Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 智能平台新的派揽信息更新完毕发送广播
	 */
	public static void sendDownCompleteBroadCast1(Context context, int result) {
		if (result > 0) {
			try {
				// Log.e("result", "result");
				// Toast.makeText(context, "更新完毕，共" + result + "派揽信息",
				// Toast.LENGTH_SHORT).show();
				Utils.sendDownCompleteBroadcast(context, result,
						Constant.PUSH_TYPE_CLCTTASK2);
			} catch (Exception e) {
				// Utils.sendDownCompleteBroadcast(context, "请求服务器失败",
				// Constant.PUSH_TYPE_DELIVERTASK);
			}
		} else {
			// Toast.makeText(context, "请求服务器失败，请检查网络",
			// Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 撤单更新完毕发送广播
	 */
	public static void sendDownCompleteBroadCast2(Context context, int result) {
		if (result > 0) {
			try {
				// Toast.makeText(context, "删除完毕，共" + result + "派揽信息",
				// Toast.LENGTH_SHORT).show();
				Utils.sendDownCompleteBroadcast(context, result,
						Constant.PUSH_TYPE_CLCTTASK3);
			} catch (Exception e) {
				// Utils.sendDownCompleteBroadcast(context, "请求服务器失败",
				// Constant.PUSH_TYPE_DELIVERTASK);
			}
		} else {
			// Toast.makeText(context, "请求服务器失败，请检查网络",
			// Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 派揽信息更新完毕发送广播
	 */
	public void sendDownCompleteBroadCast_Gather(Context context, String result) {
		if (result != null && !"-2".equals(result) && !"-1".equals(result)) {
			try {
				int count = Integer.parseInt(result);
				Toast.makeText(context, "更新完毕，共" + count + "条派揽数据",
						Toast.LENGTH_SHORT).show();
				Utils.sendDownCompleteBroadcast(context, count,
						Constant.PUSH_TYPE_CLCTTASK);
			} catch (Exception e) {
				Utils.sendDownCompleteBroadcast(context, -1,
						Constant.PUSH_TYPE_CLCTTASK);
			}
		} else {
			Toast.makeText(context, "请求服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
		}
	}

	class ChatMesTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = com.newcdc.tools.NetHelper.doGet(params[0]);
			LogUtil.e("查询用户信息返回的结果：   " + result.toString());
			if (result == null || "请求服务器失败".equals(result)) {
				if (Utils.isNetworkAvailable(getApplicationContext())) {
				}
			} else if (BaseActivity.resState(result).equals("0")) {
			} else {
				org.json.JSONObject json;
				try {
					json = new org.json.JSONObject(result);
					JSONArray array = json.getJSONObject("body").getJSONArray(
							"success");
					// 可能是查询不到结果 会有空的情况
					if (array.length() != 0) {
						String username = array.getJSONObject(0).get("name")
								.toString();
						String sid = array.getJSONObject(0).get("sid")
								.toString();
						LogUtil.e("查询用户信息返回的结果：   " + username + "    clintID "
								+ params[1]);
						mChatMessageDao.UpdateSourcename(params[1], username,
								sid);
						// 刷新回话列表去显示客户名字
						Intent intent = new Intent("msg.username");
						intent.putExtra("Source", params[1]);
						getApplicationContext().sendBroadcast(intent);
					}
				} catch (org.json.JSONException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
	}

	/**
	 * 下载语音
	 */
	public void dowloadSpeech(String url, final int messageId) {
		final String fileName = com.newcdc.tools.Constant.MYVOICEFORDER
				+ UUID.randomUUID().toString() + ".amr";
		HttpUtils http = new HttpUtils();
		http.download(url, fileName, true, true, new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				LogUtils.e("" + arg0.result.getPath());
				mChatMessageDao.UpdateMessageUrle(messageId, fileName);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}
		});
	}

	/**
	 * 下载图片
	 */
	private void downImg(final String uRL) {
		new AsyncTask<Object, Void, String>() {
			@Override
			protected String doInBackground(Object... params) {
				NetHelper.getImgFromNet(getApplicationContext(), uRL);
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
			}

		}.execute();
	}

	public int getSpeechMessageTime(String url) {
		int speechMessageTime = 0;
		if (url != null && !"".equals(url)) {
			try {
				mPlayer.reset();
				mPlayer.setDataSource(url);
				mPlayer.prepare();
				speechMessageTime = mPlayer.getDuration() / 1000;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return speechMessageTime;
	}
	// 发送有未读消息广播
		public void sendReceiver() {
			// 新建一个Intent.ACTION_VIEW类型的intent。
			Intent intent = new Intent("NewMsgReceiver_Action");
			// 发送广播，注册了该类型的广播接收者就会接受到。
			sendBroadcast(intent);
		}
}

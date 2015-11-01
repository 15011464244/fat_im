package com.newcdc.receiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.newcdc.application.LoginActivity;
import com.newcdc.asynctask.DeliverAsyncTask;
import com.newcdc.asynctask.DeliverAsyncTask.onPostExecuteListener;
import com.newcdc.asynctask.DeliverAsyncTask.onPreExecuteListener;
import com.newcdc.asynctask.GatherAsyncTask;
import com.newcdc.asynctask.GatherAsyncTask.onPostExecuteListener_Gather;
import com.newcdc.asynctask.GatherAsyncTask.onPreExecuteListener_Gather;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.QueueDao;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.service.LocationService;
import com.newcdc.tools.BDPushUtils;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.Utils;

/**
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 
 * 返回值中的errorCode，解释如下： 0 - Success 10001 - Network Problem 30600 - Internal
 * Server Error 30601 - Method Not Allowed 30602 - Request Params Not Valid
 * 30603 - Authentication Failed 30604 - Quota Use Up Payment Required 30605 -
 * Data Required Not Found 30606 - Request Time Expires Timeout 30607 - Channel
 * Token Timeout 30608 - Bind Relation Not Found 30609 - Bind Number Too Many
 * 
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 * 
 */
public class BDPushMessageReceiver extends FrontiaPushMessageReceiver {
	/** TAG to Log */
	public static final String TAG = BDPushMessageReceiver.class
			.getSimpleName();

	/**
	 * 调用PushManager.startWork后，sdk将对push
	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *            BroadcastReceiver的执行Context
	 * @param errorCode
	 *            绑定接口返回值，0 - 成功
	 * @param appid
	 *            应用id。errorCode非0时为null
	 * @param userId
	 *            应用user id。errorCode非0时为null
	 * @param channelId
	 *            应用channel id。errorCode非0时为null
	 * @param requestId
	 *            向服务端发起的请求id。在追查问题时有用；
	 * @return none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		String deviceId = Utils.getDeviceId(context);
		final String pushUrl = Global.UPLOADPUSHINFO + "deviceId=" + deviceId
				+ "&userId=" + userId + "&channelId=" + channelId;
		Log.e("pushUrl-----------", pushUrl + "");
		new Thread() {
			public void run() {
				String doGet = NetHelper.doGet(pushUrl);
			};
		}.start();
		LoginActivity.userId = userId;
		LoginActivity.channelId = channelId;
		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		if (errorCode == 0) {
			BDPushUtils.setBind(context, true);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
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

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            推送的消息
	 * @param customContentString
	 *            自定义内容,为空或者json字符串
	 */
	@Override
	public void onMessage(final Context context, String message,
			String customContentString) {
		String messageString = "透传消息 message=\"" + message
				+ "\" customContentString=" + customContentString;
		Log.e("BD--------", messageString);
		try {
			JSONObject msgJson = new JSONObject(message);
			JSONObject custom_content = msgJson.getJSONObject("custom_content");
			final String taskNum = msgJson.getString("description");
			final int messageType = custom_content.getInt("messageType");
			// String description = msgJson.getString("description");
			switch (messageType) {
			case Constant.PUSH_TYPE_DELIVERTASK:// 有新的下段信息
				DeliverAsyncTask deliverAsyncTask = new DeliverAsyncTask(
						new onPostExecuteListener() {
							@Override
							public void onPostExecute(int result) {
								sendDownCompleteBroadCast(context, result);
								// 重启定位服务
								Intent locationService = new Intent(context,
										LocationService.class);
								context.stopService(locationService);
								context.startService(locationService);
							}
						}, new onPreExecuteListener() {

							@Override
							public void onPreExecute() {
								Utils.sendTitleBroadcast(context,
										"有新的下段信息，正在为您更新..");
							}
						}, context);
				deliverAsyncTask.execute();
				break;

			case Constant.PUSH_TYPE_CLCTTASK2:// 有新的派揽信息
//				Log.e("messageType", messageType + "");
//				Log.e("taskNum----", taskNum);
				new Thread() {
					@Override
					public void run() {
						long startTimeMillis = System.currentTimeMillis();// 请求开始时间
						String operate_time = Utils.getCurrTime();// 操作时间
						long batch_no = new Date().getTime();
						String result = com.newcdc.tools.NetHelper
								.doGet(Global.ZHINENGCLC_URL + "taskNum="
										+ taskNum);
//						Log.e("result11111", result);
						try {
							JsonUtils.parseGatherMessageJsonzhineng(context,
									result, startTimeMillis, operate_time,
									batch_no);
						} catch (Exception e) {
							e.printStackTrace();

						}
					};
				}.start();
				break;
			case Constant.PUSH_TYPE_CLCTTASK:// 有新的派揽信息
				Log.e("messageType", messageType + "");
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
				break;
			case Constant.PUSH_TYPE_USEROPERATE:// 用户端操作邮件
				String description = msgJson.getString("description");
				// description
				userOperate(description, context);

				break;
			default:
				break;

			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (!customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, messageString);
	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		// Log.e(TAG, notifyString);

		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (!customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, notifyString);
	}

	/**
	 * setTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
	 * @param successTags
	 *            设置成功的tag
	 * @param failTags
	 *            设置失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		// Log.e(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * delTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
	 * @param successTags
	 *            成功删除的tag
	 * @param failTags
	 *            删除失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		// Log.e(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * listTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示列举tag成功；非0表示失败。
	 * @param tags
	 *            当前应用设置的所有tag。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags="
				+ tags;
		// Log.e(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		// Log.e(TAG, responseString);

		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			BDPushUtils.setBind(context, false);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	private void updateContent(Context context, String content) {
		String logText = "" + BDPushUtils.logStringCache;

		if (!logText.equals("")) {
			logText += "\n";
		}

		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
		logText += sDateFormat.format(new Date()) + ": ";
		logText += content;

		BDPushUtils.logStringCache = logText;
		// Toast.makeText(context, logText, Toast.LENGTH_SHORT).show();
	}

	public String resState(String s) {
		String str = "";
		try {
			JSONObject json = new JSONObject(s);
			str = json.get("result").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 用户端操作邮件
	 */
	private void userOperate(String description, Context context) {
		QueueDao queueDao = DeliverDaoFactory.getInstance()
				.getQueueDao(context);
		DeliverDao mDeliverDao = DeliverDaoFactory.getInstance().getDeliverDao(
				context);
		ArrayList<DeliverQueueBean> queueList = new ArrayList<DeliverQueueBean>();
		if (description != null && !"".equals(description)) {
			List<MessageInfoBean> messageInfoBeans = mDeliverDao
					.queryByMailNumber(description);
			if (messageInfoBeans.size() > 0) {
				MessageInfoBean bean = messageInfoBeans.get(0);
				DeliverQueueBean queueBean = DeliverQueueBean.createBean(bean,
						"W", bean.getMoney(), "", "", "", "0", Constant.TUOTOU,
						context);
				queueBean.setPicture(bean.getPicture());
				queueBean.setCommit_result(Constant.COMMIT);
				queueList.add(queueBean);
				queueDao.insertOperate(queueList, context);

				mDeliverDao.updateListMailDealResult(
						Utils.parseBeanToIdList(messageInfoBeans),
						Constant.TUOTOU);
			}
		}
	}

}

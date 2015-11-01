package com.newcdc.asynctask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;

import com.newcdc.R;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.GroupDao;
import com.newcdc.db.PushDao;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.XFAudio;

/**
 * The deliver information download task.This class include two ways of
 * downdata: 1,execute an asynctask to start download,it's a queuetask,one in
 * server,then the ohters must wait; 2,start a thread to start,but this just can
 * do one time,you must write your own code to complete.
 * 
 * @author zhangfan
 * 
 */
public class DeliverAsyncTask extends AsyncTask<Void, Void, Integer> {
	private onPostExecuteListener onPostExecuteListener;
	private onPreExecuteListener onPreExecuteListener;
	private Context context;

	/**
	 * 下段异步任务的构造函数
	 * 
	 * @param onPostExecuteListener
	 *            数据下载完毕的接口回调
	 * @param onPreExecuteListener
	 *            数据下载前的接口回调
	 * @param context
	 *            上下文
	 */
	public DeliverAsyncTask(onPostExecuteListener onPostExecuteListener,
			onPreExecuteListener onPreExecuteListener, Context context) {
		super();
		this.onPostExecuteListener = onPostExecuteListener;
		this.onPreExecuteListener = onPreExecuteListener;
		this.context = context;
	}

	public interface onPostExecuteListener {
		void onPostExecute(int result);
	}

	public interface onPreExecuteListener {
		void onPreExecute();
	}

	@Override
	protected void onPreExecute() {
		Utils.sendTitleBroadcast(context, Constant.TITLE_LOADINGDATA);
		if (onPreExecuteListener != null) {
			onPreExecuteListener.onPreExecute();
		}
		super.onPreExecute();
	}

	public static int newMailCount = 0;

	public static boolean doRequest(Context context) {
		long startTimeMillis = System.currentTimeMillis();// 请求开始时间
		String operate_time = Utils.getCurrTime();// 操作时间
		long batch_no = new Date().getTime();
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		PushDao pushDao = daoFactory.getPushDao(context);
		String the_class_date = pushDao
				.query_class_date(Constant.PUSH_TYPE_DELIVERTASK);
		DeliverDao deliverDao = daoFactory.getDeliverDao(context);
		TelephonyManager telephonemanage = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		UserInfoUtils user = new UserInfoUtils(context);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("dlvorgcode", user
				.getUserDelvorgCode()));
		pairs.add(new BasicNameValuePair("username", user.getUserName()));
		pairs.add(new BasicNameValuePair("dlvsectionid", user.getDlvsectionid()));
		pairs.add(new BasicNameValuePair("deviceId", telephonemanage
				.getDeviceId()));
		pairs.add(new BasicNameValuePair("sid", the_class_date));
		String result = NetHelper.doPost(Global.DELIVER_URL, pairs);
		if (Utils.stringEmpty(result) || "请求服务器失败".equals(result)) {
			newMailCount = -1;
			return false;
		}
		try {
			JSONObject resultJson = new JSONObject(result);
			int resultInt = resultJson.getInt("result");
			if (resultInt == 0) {
				// 返回false表示无新数据，或者其他异常，并终止请求
				return false;
			} else {
				JSONObject body = resultJson.getJSONObject("body");
				JSONObject success = body.getJSONObject("success");
				int totalRows = success.getInt("totalRows");// 请求数据量
				if (totalRows > 50) {
					totalRows = 50;
				}
				long endTime = System.currentTimeMillis();// 请求结束时间
				// 提交请求日志
				NetHelper.saveTransferLog(operate_time, endTime
						- startTimeMillis + "", context, context.getResources()
						.getString(R.string.operate_action_deliver_down),
						totalRows + "", batch_no + "");
				if (totalRows > 0) {
					// 解析json并存储
					int count = JsonUtils.parseDeliverJson(context,
							success.getJSONArray("resultList"));
					Utils.sendChangeTitleCountBroadcast(context);
					newMailCount += count;
					// 更新sid
					// 如果有新数据，更新the_class_date,即sid
					pushDao.update_class_date(deliverDao.queryMaxSid());// 更新the_class_date
					// 更新分组表的邮件个数统计
					GroupDao groupDao = DeliverDaoFactory.getInstance()
							.getGroupDao(context);
					groupDao.updateDealMailCount(context);// 更新非地址分组
					groupDao.updateUncommitMailCount(context);// 更新未上传分组
					groupDao.updatePaymentGroup(context);// 更新收费邮件的两个分组
					groupDao.updateOtherGroups(context);// 更新地址分组
					// 返回true表示解析无异常，并继续请求下载服务
					return true;
				} else {
					return false;
				}
			}
		} catch (JSONException e) {
			// Log.e("err", e.getMessage());
		}
		return false;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		boolean request = false;
		request = doRequest(context);
		while (request) {
			request = doRequest(context);
		}
		return newMailCount;
	}

	@Override
	protected void onPostExecute(Integer result) {
		newMailCount = 0;
		// 发送广播通知标题栏，并置标题信息为空，显示定位信息
		Utils.sendTitleBroadcast(context, null);
		if (result > 0) {
			XFAudio audio = new XFAudio(context, "您有新的下段信息，请及时查看");
			audio.startSay();
		}
		SharePreferenceUtilDaoFactory.getInstance(context)
				.setDeliverTime(
						DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date())
								.toString());
		if (onPostExecuteListener != null) {
			onPostExecuteListener.onPostExecute(result);
		}
		super.onPostExecute(result);
	}
}

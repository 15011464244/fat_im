package com.newcdc.asynctask;

import java.util.Date;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.newcdc.tools.Global;
import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.XFAudio;

/**
 * @author hanrong
 * @version 创建时间：2015-1-14 下午5:57:39 类说明 派揽信息下载
 */
public class GatherAsyncTask extends AsyncTask<String, String, String> {
	private Context context;
	private onPostExecuteListener_Gather onPostExecuteListener;
	private onPreExecuteListener_Gather onPreExecuteListener;
	private String userCode = "";
	private String orgCode = "";

	public GatherAsyncTask(Context context,
			onPostExecuteListener_Gather onPostExecuteListener,
			onPreExecuteListener_Gather onPreExecuteListener) {
		super();
		this.context = context;
		this.onPostExecuteListener = onPostExecuteListener;
		this.onPreExecuteListener = onPreExecuteListener;
		UserInfoUtils userinfo = new UserInfoUtils(context);
		this.userCode = userinfo.getUserName();
		this.orgCode = userinfo.getUserDelvorgCode();
	}

	@Override
	protected void onPreExecute() {
		if (onPreExecuteListener != null) {
			onPreExecuteListener.onPreExecute();
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		long startTimeMillis = System.currentTimeMillis();// 请求开始时间
		String operate_time = Utils.getCurrTime();// 操作时间
		long batch_no = new Date().getTime();
		String urlString =Global.GATHERMESSAGE_URL + "USER_CODE=" + userCode
				+ "&ORG_CODE=" + orgCode;
		String result = com.newcdc.tools.NetHelper
				.doGet(urlString);
//		String result = com.newcdc.tools.NetHelper
//		.doGet("http://172.17.123.1:8080/message-service/PhoneClctAction/findNotifyMSG?" + "USER_CODE=" + userCode
//				+ "&ORG_CODE=" + orgCode);
		Log.e("刷新的时候的请求url", urlString);
		Log.e("result---------", result);
		String jsonResult = JsonUtils.parseGatherMessageJson(context, result,
				startTimeMillis, operate_time, batch_no);

		return jsonResult;
	}

	@Override
	protected void onPostExecute(String result) {
//		Log.e("tag", "下载派揽信息---结束");
		if ("-2".equals(result)) {
//			Log.e("tag", "下载派揽信息---失败");
			Toast.makeText(context, "派揽信息下载失败，请检查网络", Toast.LENGTH_LONG).show();
		} else if ("-1".equals(result)) {
			// Toast.makeText(context, result.toString(),
			// Toast.LENGTH_LONG).show();
//			Log.e("tag", "下载派揽信息---无数据");
		} else {
//			Log.e("tag", "下载派揽信息---成功");
			XFAudio audio = new XFAudio(context, "您有新的派揽信息，请及时查看");
			audio.startSay();
			SharePreferenceUtilDaoFactory.getInstance(context)
					.setCollectionTime(
							DateFormat
									.format("yyyy-MM-dd kk:mm:ss", new Date())
									.toString());
			// Log.e("ddddd", SharePreferenceUtilDaoFactory.getInstance(context)
			// .getCollectionTime());
			Toast.makeText(context, "派揽信息下载成功", Toast.LENGTH_SHORT).show();
		}
		if (onPostExecuteListener != null) {
			onPostExecuteListener.onPostExecute(result);
		}
		super.onPostExecute(result);
	}

	public interface onPostExecuteListener_Gather {
		void onPostExecute(String result);
	}

	public interface onPreExecuteListener_Gather {
		void onPreExecute();
	}
}

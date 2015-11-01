package com.newcdc.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;

/**
 * 发送短信异步任务
 * 
 * @author zhangfan 2015-1-22,下午4:56:31
 * 
 */
public class sendMessageTask extends AsyncTask<Void, Void, String> {
	private onPostExecuteListener onPostExecuteListener;
	private onPreExecuteListener onPreExecuteListener;
	private Context context;
	private ArrayList<Integer> list;
	private DeliverDao deliverDao;
	private String the_class_date;

	public sendMessageTask(onPostExecuteListener onPostExecuteListener,
			onPreExecuteListener onPreExecuteListener, Context context,
			ArrayList<Integer> list) {
		super();
		this.onPostExecuteListener = onPostExecuteListener;
		this.onPreExecuteListener = onPreExecuteListener;
		this.context = context;
		this.list = list;
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		deliverDao = daoFactory.getDeliverDao(context);
		the_class_date = daoFactory.getPushDao(context).query_class_date(
				Constant.PUSH_TYPE_DELIVERTASK);
	}

	public interface onPostExecuteListener {
		void onPostExecute(String result);
	}

	public interface onPreExecuteListener {
		void onPreExecute();
	}

	@Override
	protected void onPreExecute() {
		if (onPreExecuteListener != null) {
			onPreExecuteListener.onPreExecute();
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... params) {
		return upload(list, context);
	}

	@Override
	protected void onPostExecute(String result) {
		if (onPostExecuteListener != null) {
			onPostExecuteListener.onPostExecute(result);
		}
		super.onPostExecute(result);
	}

	/**
	 * 上传
	 * 
	 * @throws Exception
	 */
	public static String upload(ArrayList<Integer> list, Context context) {
		int all = list.size();
		int ok = 0;
		try {
			DeliverDao deliverDao = DeliverDaoFactory.getInstance()
					.getDeliverDao(context);
			UserInfoUtils info = new UserInfoUtils(context);
			JSONObject jsonObj = null;
			String mobiles = "";
			for (int i = 0; i < list.size(); i++) {
				MessageInfoBean bean = deliverDao.query_id(list.get(i));//
				mobiles += bean.getRcver_contact_phone1() + ",";
			}
			List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
			paramObject.add(new BasicNameValuePair("sms_type", "t"));
			paramObject.add(new BasicNameValuePair("receiver_mobiles", mobiles
					.substring(0, mobiles.length() - 1)));
			paramObject.add(new BasicNameValuePair("longitude",
					BaiduGpsContants.getInstance().getLng()));
			paramObject.add(new BasicNameValuePair("latitude", BaiduGpsContants
					.getInstance().getLat()));
			paramObject.add(new BasicNameValuePair("address", BaiduGpsContants
					.getInstance().getAddressStr()));
			paramObject.add(new BasicNameValuePair("dlvorgcode", info
					.getUserDelvorgCode()));
			paramObject.add(new BasicNameValuePair("username", info
					.getUserName()));
			paramObject.add(new BasicNameValuePair("the_class_date", ""));
			jsonObj = new JSONObject(NetHelper.doMsgPost(Global.SENDMESSAGE,
					paramObject));
			Log.e("result", jsonObj.toString());
			if (jsonObj != null && !"".equals(jsonObj)) {
				if ("1".equals(jsonObj.getString("result"))) {
					JSONArray success = jsonObj.getJSONObject("body")
							.getJSONArray("success");
					for (int i = 0; i < success.length(); i++) {
						JSONObject obj = success.getJSONObject(i);
						String childResult = obj.getString("result");
						if ("true".equals(childResult)) {
							deliverDao.addMsgTime(list.get(i));// 返回的集合的顺序和提交的顺序相等
							ok++;
						}
					}
				}
			}
		} catch (Exception e) {
			Log.e("aaa", e.getMessage());
		}
		return all + "," + ok;
	}

}

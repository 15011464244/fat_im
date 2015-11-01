package com.newcdc.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;

public class SendMessService extends IntentService {
	private String message = "发短信失败";
	private ArrayList<Integer> list = new ArrayList<Integer>();
	private DeliverDao deliverDao;
	private DeliverDaoFactory daoFactory;

	public SendMessService() {
		super("SendMessService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		daoFactory = DeliverDaoFactory.getInstance();
		deliverDao = daoFactory.getDeliverDao(getApplicationContext());
		Bundle bundle = intent.getExtras();
		Serializable tt = bundle.getSerializable("mm");
		list = (ArrayList<Integer>) tt;
	}

	/**
	 * 上传
	 * 
	 * @throws Exception
	 */
	public boolean upload() {
		boolean flag = true;
		try {
			UserInfoUtils info = new UserInfoUtils(getApplicationContext());
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
			paramObject.add(new BasicNameValuePair("the_class_date", daoFactory
					.getPushDao(getApplicationContext()).query_class_date(
							Constant.PUSH_TYPE_DELIVERTASK)));
			try {
				jsonObj = new JSONObject(NetHelper.doPost(Global.SENDMESSAGE,
						paramObject));
				int all = list.size();
				int ok = 0;
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
					message = "批量发送" + all + "条短信，成功" + ok + "条";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			flag = false;
		}
		Intent reintent = new Intent("downDeliverTask");
		reintent.putExtra("sendMessage", message);
		getApplicationContext().sendBroadcast(reintent);// 刷新列表
		stopSelf();
		return flag;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		upload();
	}

}

package com.newcdc.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.FastLanDao;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.db.JxClctDao;
import com.newcdc.model.GatherTableBean;
import com.newcdc.model.JxClctBean;
import com.newcdc.model.fastbean;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.XFAudio;

public class CollectionUploadService extends IntentService {

	private String orgCode, username, deviceId, realname, postcode, userCode;
	private DeliverDaoFactory deliverDaoFactory;
	private FastLanDao mFastLanDao;
	private Gather_MsgDao mGather_MsgDao;
	private GatherTableBean tablebean;
	private Handler handler;
	private JxClctDao jxClctDao;

	public CollectionUploadService() {
		super("CollectionUploadService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		deliverDaoFactory = DeliverDaoFactory.getInstance();
		mFastLanDao = deliverDaoFactory.getFastLanDao(getApplicationContext());
		jxClctDao = deliverDaoFactory.getJxClctDao(getApplicationContext());
		mGather_MsgDao = deliverDaoFactory
				.getGather_msgdao(getApplicationContext());
		UserInfoUtils user = new UserInfoUtils(CollectionUploadService.this);
		deviceId = Utils.getDeviceId(this);
		orgCode = user.getUserDelvorgCode();
		username = user.getUserName();
		realname = user.getRealname();
		postcode = user.getPostcode();
		userCode = user.getUserName();
		handler = new Handler();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// 一直上传，直到成功，没有次数限制 50次后不成功就不传
		int upSize = 0;
		List<JxClctBean> lan = null;
		for (int j = 0; j < Constant.REPEAT_TIME; j++) {
			if (lan != null) {
				lan.clear();
			}
			lan = jxClctDao.queryJxClctMessage(orgCode, userCode, "0");
			upSize = lan.size();
			if (upSize != 0) {// 有上传数据
				if (jx_Upload(getApplicationContext(), lan)) {
					Utils.sendGatherSCBroadcast(getApplicationContext());
					break;
				}

			} else {
				break;
			}
		}
		if (upSize != 0) {// 有上传数据且上传完成
			final int failMailSize = jxClctDao.queryJxClctMessage(orgCode,
					userCode, "0").size();
			handler.post(new Runnable() {
				@Override
				public void run() {
					// 语音提示处理结果
					if (failMailSize != 0) {
						String showResult = "揽收信息上传失败请检查网络";
						XFAudio audio = new XFAudio(getApplicationContext(),
								showResult);
						audio.startSay();
						// 发送广播更改主界面的跑马灯
						Utils.sendTitleBroadcast(getApplicationContext(),
								showResult);
					} else if (failMailSize == 0) {
						XFAudio audio = new XFAudio(getApplicationContext(),
								"揽收信息上传成功");
						audio.startSay();
					}
				}
			});
		}
		stopSelf();
	}

	/**
	 * 上传方法 返回true全部上传成功 返回false 有数据没成功
	 * 
	 * @throws Exception
	 */
	public boolean jx_Upload(Context context, List<JxClctBean> jxClctBean) {

		String urlString = Global.SERVER_URL2 + "clct";
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		for (int i = 0; i < jxClctBean.size(); i++) {
			try {
				json.put("mailNo", jxClctBean.get(i).getMailNo());
				json.put("weight", jxClctBean.get(i).getWeight());
				json.put("receiverName", jxClctBean.get(i).getReceiverName());
				json.put("receiverMobile", jxClctBean.get(i)
						.getReceiverMobile());
				json.put("receiverAddress", jxClctBean.get(i)
						.getReceiverAddress());
				json.put("fee", jxClctBean.get(i).getFee());
				json.put("senderName", jxClctBean.get(i).getSenderName());
				json.put("senderMobile", jxClctBean.get(i).getSenderMobile());
				json.put("senderAddress", jxClctBean.get(i).getSenderAddress());
				json.put("orgCode", jxClctBean.get(i).getOrgCode());
				json.put("payment", jxClctBean.get(i).getPayment());// 付款方式
				json.put("userCode", jxClctBean.get(i).getUserCode());
				json.put("userName", jxClctBean.get(i).getUserName());
				json.put("serviceType", jxClctBean.get(i).getServiceType());
				json.put("orderNo", jxClctBean.get(i).getReservenum());
				json.put("userSid", jxClctBean.get(i).getUserSid());
				json.put("useIntegral", jxClctBean.get(i).getUserIntegral());//用户欲用积分
				json.put("actUserIntegral", jxClctBean.get(i).getIntegral());//实际使用的积分
				json.put("actFee", jxClctBean.get(i).getActFee());
			} catch (JSONException e) {
				return false;
			}
			jsonArray.put(json);
		}
		String result = NetHelper.doPostJson(urlString, jsonArray.toString(),
				"json");
		Log.e("揽收地址提交参数======", urlString);
		Log.e("揽收提交参数======", jsonArray.toString());
		Log.e("揽收提交数据返回======", result);
		
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(result);
			if (jsonObj.get("result") != null
					&& "1".equals(jsonObj.getString("result"))) {
				// Toast.makeText(NoClctActivityJX.this,
				// "揽收成功", Toast.LENGTH_SHORT).show();
				for (int i = 0; i < jxClctBean.size(); i++) {
					jxClctDao.updateClctStauts(jxClctBean, "1");
					if (jxClctBean.get(i).getMailType().equals("1")) {
						getEnSureTask(jxClctBean.get(i).getMailNo(),
								jxClctBean.get(i).getCompanyid());
					}
				}
				return true;
			} else {
				// Toast.makeText(NoClctActivityJX.this,
				// "揽收失败", Toast.LENGTH_SHORT).show();
				return false;
			}
		} catch (JSONException e) {
			return false;
		}
	}

	/**
	 * 任务完成
	 */
	private void getEnSureTask(final String MailNo, final String companyid) {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				fastbean bean = new fastbean();
				bean.setTaskNum(companyid);
				bean.setMailNum(MailNo);
				bean.setExecData("4");
				bean.setOperUserCode(username);
				bean.setLat(BaiduGpsContants.getInstance().getLat());
				bean.setLon(BaiduGpsContants.getInstance().getLng());
				String urlString = Global.EnSureTask_URL;
				String jsonString = JSON.toJSONString(bean);
				JSONObject jsonObject = null;
				try {
					String str1 = NetHelper.doPostJson(urlString, jsonString,
							"json");
					jsonObject = new JSONObject(str1);
					String result = jsonObject.getString("result");
					Log.e("揽收信息上传成功之后的确认urlString", urlString );
					Log.e("揽收信息上传成功之后的确认jsonString", jsonString);
					Log.e("揽收信息上传成功之后的确认result", result );
					// if (result.equals("1")) {
					// DeliverDaoFactory.getInstance()
					// .getGather_msgdao(getApplicationContext()).updateEnSureTask(reservenum,
					// "0");
					//
					// }
				} catch (JSONException e) {
				}
				return jsonObject;
			}

			protected void onPostExecute(JSONObject result) {
			}
		}.execute();
	}
}

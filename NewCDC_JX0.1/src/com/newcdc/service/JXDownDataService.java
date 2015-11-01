package com.newcdc.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.cn.cdc.BaseData;
import com.cn.cdc.BaseDataDao;
import com.cn.cdc.DaoFactory;
import com.cn.cdc.LocCountyNanJingDao;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.activity.clcttask.DataDown;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.DeliverTaskListDao;
import com.newcdc.db.GroupDao;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.model.QueryTaskBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * 江西版 基础数据下载 更新于2015.6.24
 * 
 * @author liunannan
 */
public class JXDownDataService extends Service {
	private String orgCode = null;
	private String userName;
	private String nowdate = null;
	private String termeseqno = null;
	private SharePreferenceUtilDaoFactory share;
	private BaseDataDao baseDataDao = null;
	private String dlvSectionCode;
	private int count = 0;
	private String deviceNo;
	private DeliverTaskListDao mTaskListDao;
	private DeliverDao deliverDao;
	private GroupDao groupDao;
	private QueryTaskBean bean = new QueryTaskBean();

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(getApplicationContext(),
					"更新完毕，共" + msg.what + "条下段数据", Toast.LENGTH_SHORT).show();
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		UserInfoUtils user = new UserInfoUtils(getApplicationContext());
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		orgCode = user.getUserDelvorgCode();
		userName = user.getUserName();
		share = SharePreferenceUtilDaoFactory
				.getInstance(getApplicationContext());
		baseDataDao = DaoFactory.getInstance().getBaseDataDao(
				getApplicationContext());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		nowdate = sdf.format(new Date());
		TelephonyManager telephonemanage = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		termeseqno = telephonemanage.getDeviceId();
		deviceNo = Utils.getDeviceId(getApplicationContext());
		mTaskListDao = daoFactory
				.getDeliverTaskListDao(getApplicationContext());
		deliverDao = daoFactory.getDeliverDao(getApplicationContext());
		groupDao = daoFactory.getGroupDao(getApplicationContext());
		dlvSectionCode = user.getDlvsectionCode();

		// int stateFollowSize =
		// stateFollowDao.FindStateFllowBystateType(orgCode)
		// .size();
		// int dlvStateSize =
		// dlvStateDao.FindDlvStateBystateType(orgCode).size();
		// int followActionSize =
		// followActionDao.FindFollowAction(orgCode).size();
		// LogUtils.e("====stateFollowDao===" + stateFollowSize);
		// LogUtils.e("===dlvStateDao====" + dlvStateSize);
		// LogUtils.e("===followActionDao====" + followActionSize);
		// LogUtils.e("---------------------"
		// + stateFollowDao.FindStateFllowBystateType(orgCode).toString());
		// LogUtils.e("--------投递情况1-------------"
		// + dlvStateDao.FindDlvStateBystateType(Constant.UNDLVCODE)
		// .toString());
		// LogUtils.e("---------------------"
		// + followActionDao.FindFollowAction(orgCode).toString());
		downloadAllBaseData();
		if (Utils.isNetworkAvailable(getApplicationContext())) {
			if (Global.isLan) {
				DataDown dataDown = new DataDown();
				dataDown.oncreate(getApplicationContext(),
						telephonemanage.getDeviceId(), orgCode, userName);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 基础数据下载
	 */
	private void downloadAllBaseData() {
		new AsyncTask<Object, Void, String>() {
			@Override
			protected String doInBackground(Object... params) {
				// 下载基础数据
				downloadBaseData();
				// 下载员工任务查询
				downloadfindTaskByWorker();
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				stopSelf();
				super.onPostExecute(result);
			}
		}.execute();
	}

	/**
	 * 下载员工任务查询(派单)
	 */
	private void downloadfindTaskByWorker() {
		new Thread() {
			@Override
			public void run() {
				String url = Global.FindTaskByWorker_URL + "orgCode=" + orgCode
						+ "&userCode=" + userName;
				String result = com.newcdc.tools.NetHelper.doGet(url);
				LogUtils.e("下载员工任务查询      " + result);
				org.json.JSONObject obj;
				try {
					obj = new org.json.JSONObject(result);
					String jsonresult = obj.getString("result");
					if ("1".equals(jsonresult)) {
						org.json.JSONObject obj1 = obj.getJSONObject("content");
						org.json.JSONArray dataList = obj1
								.getJSONArray("dataList");
						List<String> tmpList = new ArrayList<String>();
						if (dataList != null && dataList.length() != 0) {
							for (int i = 0; i < dataList.length(); i++) {
								org.json.JSONObject obj2 = dataList
										.getJSONObject(i);
								String taskType = obj2.getString("taskType");
								String taskStatus = obj2
										.getString("taskStatus");
								String taskNum = obj2.getString("taskNum");
								if (taskType.equals("1")) {
									if (taskStatus.equals("2")
											|| taskStatus.equals("3")
											|| taskStatus.equals("6")) {
										tmpList.add(taskNum);
									}
								} else {
									findTaskMail(taskNum);
								}
							}
							bean.setTaskNums(tmpList);
						}
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (bean != null && bean.getTaskNums() != null
						&& bean.getTaskNums().size() > 0) {
					String jsonString = JSON.toJSONString(bean);
					long startTimeMillis = System.currentTimeMillis();// 请求开始时间
					String operate_time = Utils.getCurrTime();// 操作时间
					long batch_no = new Date().getTime();
					String result1 = com.newcdc.tools.NetHelper.doPostJson(
							Global.ZHINENGCLC_URL, jsonString, "json");
					Log.e("员工任务查询", result1);
					try {
						JsonUtils.parseGatherMessageJsonzhineng(
								getApplicationContext(), result1,
								startTimeMillis, operate_time, batch_no);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			};
		}.start();
	}

	private void findTaskMail(String taskNum) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("taskCode", taskNum));
		params.add(new BasicNameValuePair("isQueryTotal", "1"));
		params.add(new BasicNameValuePair("orgCode", orgCode));
		params.add(new BasicNameValuePair("segmentCode", dlvSectionCode));
		params.add(new BasicNameValuePair("dlvUserCode", userName));
		params.add(new BasicNameValuePair("deviceNo", deviceNo));
		params.add(new BasicNameValuePair("deviceType", "android"));
		long startTimeMillis = System.currentTimeMillis();// 请求开始时间
		String operate_time = Utils.getCurrTime();// 操作时间
		long batch_no = new Date().getTime();
		String result = NetHelper.doPost(Global.DELIVER_URL, params);
		Log.e("result", "查询名址返回参数=" + result);
		parseTaskMailJson(startTimeMillis, operate_time, batch_no, result);

	}

	/**
	 * 解析名址JSON
	 */
	private void parseTaskMailJson(Long startTimeMillis, String operate_time,
			Long batch_no, String json) {
		try {
			JSONObject reObj = new JSONObject(json);
			int result = reObj.getInt("result");
			switch (result) {
			case 0:
				break;
			case 1:
				// 清除下段流水信息
				// shareDao.setDeliverTaskInformation(null);
				// 插入到下段信息表中的邮件
				final ArrayList<MessageInfoBean> list = new ArrayList<MessageInfoBean>();

				// 全部频次
				ArrayList<Map<String, String>> frequenceList;
				int newCount = 0;
				int allCount = 0;
				// 新增邮件频次统计
				ArrayList<HashMap<Integer, Integer>> newMail = new ArrayList<HashMap<Integer, Integer>>();
				JSONArray dataList = reObj.getJSONArray("dataList");
				long endTime = System.currentTimeMillis();// 请求结束时间
				// 提交请求日志
				NetHelper.saveTransferLog(
						operate_time,
						endTime - startTimeMillis + "",
						getApplicationContext(),
						getApplicationContext().getResources().getString(
								R.string.operate_action_deliver_mingzhi),
						dataList.length() + "", batch_no + "");
				for (int i = 0; i < dataList.length(); i++) {
					frequenceList = mTaskListDao.queryGatherTaskMessage(
							orgCode, userName);
					// Log.e("tag4444444",orgCode +userName);
					JSONObject obj = dataList.getJSONObject(i);
					String the_class_date = "";
					String money = "";
					String mail_prpty_code = "";
					String sender_contact_phone1 = "";
					String sender_loc_city = "";
					String dlvorgcode = "";
					String sender_name = "";
					String rcver_addr = "";
					String rcver_name = "";
					String rcver_street_addr = "";
					String rcver_contact_phone1 = "";
					String username = "";
					String sender_addr = "";
					String sender_street_addr = "";
					String mail_num = "";
					String dlvsectionid = "";
					String sender_loc_prov = "";
					String frequence = "";
					String dlv_sts_code = "a";
					if (obj.has("the_class_date")) {
						the_class_date = Utils.getCurrTime(obj
								.getLong("the_class_date"));// ok
					}
					if (obj.has("sender_contact_phone1")) {
						sender_contact_phone1 = obj
								.getString("sender_contact_phone1");// ok
					}
					if (obj.has("dlvorgcode")) {
						dlvorgcode = obj.getString("dlvorgcode");// ok
					}
					if (obj.has("sender_loc_city")) {
						sender_loc_city = obj.getString("sender_loc_city");// ok
					}
					if (obj.has("sender_name")) {
						sender_name = obj.getString("sender_name");// ok
					}
					if (obj.has("rcver_addr")) {
						rcver_addr = obj.getString("rcver_addr");// ok
					}
					if (obj.has("rcver_name")) {
						rcver_name = obj.getString("rcver_name");// ok
					}
					if (obj.has("rcver_street_addr")) {
						rcver_street_addr = obj.getString("rcver_street_addr");// ok
					}
					if (obj.has("rcver_contact_phone1")) {
						rcver_contact_phone1 = obj
								.getString("rcver_contact_phone1");// ok
					}
					if (obj.has("username")) {
						username = obj.getString("username");// ok
					}
					if (obj.has("money")) {
						money = obj.getString("money");// ok
					}
					if (obj.has("sender_addr")) {
						sender_addr = obj.getString("sender_addr");// ok
					}
					if (obj.has("sender_street_addr")) {
						sender_street_addr = obj
								.getString("sender_street_addr");// ok
					}
					if (obj.has("mail_num")) {
						mail_num = obj.getString("mail_num");// ok
					}
					if (obj.has("dlvsectionid")) {
						dlvsectionid = obj.getString("dlvsectionid");// ok
					}
					if (obj.has("mail_prpty_code")) {
						mail_prpty_code = obj.getString("mail_prpty_code");// ok
					}
					if (obj.has("sender_loc_prov")) {
						sender_loc_prov = obj.getString("sender_loc_prov");// ok
					}

					Log.e("frequence", frequence);

					HashMap<String, String> map = new HashMap<String, String>();
					// String sender_loc_county = obj
					// .getString("sender_loc_county");
					// String dlv_pseg_code = obj.getString("dlv_pseg_code");
					// String rcver_loc_prov = obj.getString("rcver_loc_prov");
					// String rcver_loc_city = obj.getString("rcver_loc_city");
					// String rcver_loc_county =
					// obj.getString("rcver_loc_county");
					String flag = null;
					try {
						if (obj.has("flag")) {
							flag = obj.getString("flag");
						}
					} catch (Exception e) {
					}
					// int sid = obj.getInt("sid");
					// String dlv_date = obj.getString("dlv_date");
					// String dlv_time = obj.getString("dlv_time");
					// String undlv_next_actn_code = obj
					// .getString("undlv_next_actn_code");
					// String undlv_cause_code =
					// obj.getString("undlv_cause_code");
					// String sign_sts_code = obj.getString("sign_sts_code");
					// String signer_name = obj.getString("signer_name");
					if (obj.has("dlv_sts_code")) {
						dlv_sts_code = obj.getString("dlv_sts_code");// ok
					}
					if (deliverDao.queryMail(mail_num)) {
						if (obj.has("frequence")) {
							frequence = String.valueOf(obj.getInt("frequence"));// ok
							// Log.e("tag", "frequence"+frequence);
							// Log.e("tag",
							// "frequenceList"+frequenceList.size());
							// TODO 将新增邮件根据频次插入到韩荣的任务表中
							for (int j = 0; j < frequenceList.size(); j++) {

								if (frequenceList.get(j).get("delivertasknum")
										.equals(frequence)) {
									newCount = Integer.parseInt(frequenceList
											.get(j).get("delivertaskcount")) + 1;
									allCount = (Integer.parseInt(frequenceList
											.get(j).get("delivertaskallcount")) + 1);
									// String delivertasknum =
									// frequenceList.get(j).get(
									// "delivertasknum");
									mTaskListDao
											.updateDeliverOneTaskCount(orgCode,
													userName,
													String.valueOf(newCount),
													frequence);
									mTaskListDao
											.updateDeliverMoreTaskCount(
													orgCode, userName,
													String.valueOf(allCount),
													frequence);
									Log.e("tag", "allCount--denglu" + allCount);
								}
							}
						}
						MessageInfoBean bean = new MessageInfoBean(
								"rcver_loc_county", dlvorgcode,
								rcver_contact_phone1, "sender_loc_county",
								sender_street_addr, "rcver_loc_prov",
								sender_contact_phone1, rcver_name,
								the_class_date, rcver_street_addr, sender_name,
								sender_loc_prov, frequence, sender_loc_city,
								"rcver_loc_city", mail_num, username,
								sender_addr, rcver_addr, money, dlvsectionid);
						bean.setFlag(flag);// TODO 缴款
						// bean.setUndlv_cause_code(undlv_cause_code);
						// bean.setUndlv_next_actn_code(undlv_next_actn_code);
						// bean.setSign_sts_code(sign_sts_code);
						// bean.setSigner_name(signer_name);
						// bean.setDlv_date(dlv_date);
						// bean.setDlv_time(dlv_time);
						// bean.setSid(sid);
						// 根据字段该字段区分邮件处理类型
						if ("I".equals(dlv_sts_code.toUpperCase())) {
							bean.setDealResult(Constant.TUOTOU);
						} else if ("H".equals(dlv_sts_code.toUpperCase())) {
							bean.setDealResult(Constant.WEITUOTOU);
						} else {
							bean.setDealResult(Constant.DAICHULI);
						}
						list.add(bean);
					}
				}
				// 将全部邮件存入下段表
				deliverDao.insertDeliver(list);
				groupDao.updateDealMailCount(getApplicationContext());
				// 发送广播通知页面刷新
				sendBroadcast(new Intent(Constant.ACTION_DOWN_DATA_OVER));
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						int newMailCount = list.size();
						if (newMailCount != 0) {
							Toast.makeText(getApplicationContext(),
									"更新完毕，共" + newMailCount + "条下段数据",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				break;
			}
		} catch (Exception e) {
			LogUtils.e("" + e.getMessage());
		}
	}

	/**
	 * 投递情况代码dataFlags-- NEXT_ACTN_CODE 下一步动作 MAIL_NOTE_CODE 邮件备注 DLV_STS_CODE
	 * 投递备注 TURN_BACK_CAUSE_CODE 转退原因 UNDLV_CAUSE_CODE 未妥投原因
	 */
	private void downloadBaseData() {
		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... arg0) {
				JSONObject jsonObj = null;
				try {
					JSONArray jsonArray = new JSONArray();
					jsonObj = new JSONObject();
					jsonObj.put("orgCode", orgCode);
					jsonObj.put("userCode", userName);
					jsonArray.put("NEXT_ACTN_CODE");
					jsonArray.put("MAIL_NOTE_CODE");
					jsonArray.put("DLV_STS_CODE");
					jsonArray.put("TURN_BACK_CAUSE_CODE");
					jsonArray.put("UNDLV_CAUSE_CODE");
					jsonObj.put("dataFlags", jsonArray);
					String result = NetHelper
							.doPostJson(Global.BASEDATADOWNLOAD,
									jsonObj.toString(), "json");
					if (result == null || "请求服务器失败".equals(result)) {
						if (count < 5) {
							count++;
							downloadBaseData();
						}
					} else if (resState(result).equals("0")) {
					} else {
						baseDataDao.DeleteData();
						saveDaseData(result);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonObj;
			}
		}.execute();
	}

	protected void saveDaseData(String result) {
		JSONObject jsonObj;
		JSONObject jsonObjdataList;
		JSONArray jsonObj_NEXT_ACTN_CODE;
		JSONArray jsonObj_MAIL_NOTE_CODE;
		JSONArray jsonObj_DLV_STS_CODE;
		JSONArray jsonObj_UNDLV_CAUSE_CODE;
		JSONArray jsonObj_TURN_BACK_CAUSE_CODE;
		try {
			jsonObj = new JSONObject(result);
			jsonObjdataList = jsonObj.getJSONObject("dataList");
			jsonObj_NEXT_ACTN_CODE = jsonObjdataList
					.getJSONArray("NEXT_ACTN_CODE");
			jsonObj_MAIL_NOTE_CODE = jsonObjdataList
					.getJSONArray("MAIL_NOTE_CODE");
			jsonObj_DLV_STS_CODE = jsonObjdataList.getJSONArray("DLV_STS_CODE");
			jsonObj_UNDLV_CAUSE_CODE = jsonObjdataList
					.getJSONArray("UNDLV_CAUSE_CODE");
			jsonObj_TURN_BACK_CAUSE_CODE = jsonObjdataList
					.getJSONArray("TURN_BACK_CAUSE_CODE");
			int NEXT_ACTN_CODESize = jsonObj_NEXT_ACTN_CODE.length();
			int MAIL_NOTE_CODESize = jsonObj_MAIL_NOTE_CODE.length();
			int DLV_STS_CODESize = jsonObj_DLV_STS_CODE.length();
			int UNDLV_CAUSE_CODESize = jsonObj_UNDLV_CAUSE_CODE.length();
			int TURN_BACK_CAUSE_CODESize = jsonObj_TURN_BACK_CAUSE_CODE
					.length();
			List<BaseData> NEXT_ACTN_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < NEXT_ACTN_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("NEXT_ACTN_CODE");
				bean.setDataKey(jsonObj_NEXT_ACTN_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_NEXT_ACTN_CODE.getJSONObject(i)
						.getString("dataValue"));
				NEXT_ACTN_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(NEXT_ACTN_CODElist, "NEXT_ACTN_CODE");

			List<BaseData> MAIL_NOTE_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < MAIL_NOTE_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("MAIL_NOTE_CODE");
				bean.setDataKey(jsonObj_MAIL_NOTE_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_MAIL_NOTE_CODE.getJSONObject(i)
						.getString("dataValue"));
				MAIL_NOTE_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(MAIL_NOTE_CODElist, "MAIL_NOTE_CODE");

			List<BaseData> DLV_STS_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < DLV_STS_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("DLV_STS_CODE");
				bean.setDataKey(jsonObj_DLV_STS_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_DLV_STS_CODE.getJSONObject(i)
						.getString("dataValue"));
				DLV_STS_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(DLV_STS_CODElist, "DLV_STS_CODE");

			List<BaseData> UNDLV_CAUSE_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < UNDLV_CAUSE_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("UNDLV_CAUSE_CODE");
				bean.setDataKey(jsonObj_UNDLV_CAUSE_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_UNDLV_CAUSE_CODE.getJSONObject(i)
						.getString("dataValue"));
				UNDLV_CAUSE_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(UNDLV_CAUSE_CODElist, "UNDLV_CAUSE_CODE");

			List<BaseData> TURN_BACK_CAUSE_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < TURN_BACK_CAUSE_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("TURN_BACK_CAUSE_CODE");
				bean.setDataKey(jsonObj_TURN_BACK_CAUSE_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_TURN_BACK_CAUSE_CODE.getJSONObject(i)
						.getString("dataValue"));
				TURN_BACK_CAUSE_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(TURN_BACK_CAUSE_CODElist,
					"TURN_BACK_CAUSE_CODE");

			LogUtils.e(NEXT_ACTN_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("NEXT_ACTN_CODE")
							.size());
			LogUtils.e(MAIL_NOTE_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("MAIL_NOTE_CODE")
							.size());
			LogUtils.e(DLV_STS_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("DLV_STS_CODE")
							.size());
			LogUtils.e(UNDLV_CAUSE_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("UNDLV_CAUSE_CODE")
							.size());
			LogUtils.e(TURN_BACK_CAUSE_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags(
							"TURN_BACK_CAUSE_CODE").size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拆分数据
	 * 
	 * @param resource
	 * @return
	 */
	private String[] splitData8(String resource) {
		String[] result = null;
		if (resource != null && resource.trim().length() != 0) {
			if (resource.indexOf("\n") != -1) {
				result = resource.split("\n");
			} else {
				result = new String[1];
				result[0] = resource;
			}
		}
		return result;
	}

	/**
	 * 揽投站（点部）基础数据下载
	 */
	private String downloadTbUserGroupOutTrack() {
		String message = "交班基础数据下载成功";
		String result = Utils.exeRequestString8("21", orgCode,
				getApplicationContext());
		try {
			if (result != null && result.trim().length() != 0) {
				Log.e("交班基础数据下载", result);
				// 解析数据
				String[] rowsData = splitData8(result);
				List<Map<String, Object>> insertData = new ArrayList<Map<String, Object>>();
				if (rowsData != null && rowsData.length > 0) {
					for (String rowData : rowsData) {
						String[] colsData = rowData.split("\t");
						Map<String, Object> map = new HashMap<String, Object>();
						if (colsData != null && colsData.length > 0) {
							map.put("SID", colsData[0]);
							map.put("GROUP_NAME", colsData[1]);
							map.put("PDACODE", colsData[2]);
							insertData.add(map);
						}
					}
				}
			} else {
				message = "交班基础数据下载失败";
			}
		} catch (Exception e) {
			Log.e("MailDlvActivity->downloadTbUserGroupOutTrack",
					e.getMessage());
		}
		return message;
	}

	/**
	 * 市区县集散属性数据下载
	 * 
	 * @return 返回当前操作的消息通告
	 */
	private String downloadLocCountyNanJingData() {
		String message = "市区县集散属性数据下载成功";
		String result = exeRequestBackGzip34("31");
		Log.e("DownLoadService-省市区-result", result + "");
		if ("1005".equals(result)) {
			return "无数据";
		}
		try {
			if (result != null && result.trim().length() != 0) {
				Log.e("市区县集散属性数据下载", result);
				// 解析数据
				String[] rowsData = splitData34(result);
				List<Map<String, Object>> insertData = new ArrayList<Map<String, Object>>();
				if (rowsData != null && rowsData.length > 0) {
					for (String rowData : rowsData) {
						String[] colsData = rowData.split("\t");
						Map<String, Object> map = new HashMap<String, Object>();
						if (colsData != null && colsData.length > 0) {
							map.put("COUNTY_CODE", colsData[0]);
							map.put("CN_NAME", colsData[1]);
							map.put("EN_NAME", colsData[2]);
							map.put("PROV_CODE", colsData[3]);
							map.put("CITY_CODE", colsData[4]);
							map.put("POSTCODE", colsData[5]);
							map.put("EXPORTJOINHUB", colsData[6]);
							map.put("IMPORTJOINHUB", colsData[7]);
							Log.e("DownDataService-省市区下载", map.toString());
							insertData.add(map);
						}
					}
				}
				// 保存数据
				LocCountyNanJingDao locCountyNanJingDao = DaoFactory
						.getInstance().getLocCountyNanJingDao(
								getApplicationContext());
				Log.e("DownDataService-省市区下载", insertData.toString());
				if (insertData != null && insertData.size() > 0) {
					locCountyNanJingDao.saveLocCountyNanJing(insertData,
							orgCode);
				}
			} else {
				message = "市区县集散属性数据下载失败";
			}
		} catch (Exception e) {
			Log.e("CollectionDownloadActivity->downloadLocCountyNanJingData",
					e.getMessage());
			message = "市区县集散属性数据下载失败";
		}
		Log.e("DownDataService", message);
		return message;
	}

	/**
	 * 拆分数据
	 * 
	 * @param resource
	 * @return
	 */
	private String[] splitData34(String resource) {
		String[] result = null;
		if (resource != null && resource.trim().length() != 0) {
			if (resource.indexOf("|") != -1) {
				result = resource.split("\\|");
			} else {
				result = new String[1];
				result[0] = resource;
			}
		}
		return result;
	}

	/**
	 * 执行请求并返回gzip数据，对应接口编号34
	 * 
	 * @param interfaceNo
	 *            接口编号
	 * @param dataFlag
	 *            数据同步标识
	 * @param requestUrl
	 *            请求服务器地址
	 * @return
	 */
	private String exeRequestBackGzip34(String dataFlag) {
		String result = "";
		NetHelper netHelper = new NetHelper();
		try {
			result = netHelper.exeRequestBackGzipNull("", netHelper.createUrl(
					Global.WIRELESS_ALL_DATA_DOWNLOAD,
					fillDownloadData34(dataFlag)));
		} catch (Exception e) {
			Log.e("数据下载异常，同步标识：" + dataFlag, e.getMessage());
			result = null;
		}
		return result;
	}

	/**
	 * 填充下载数据对应接口编号34
	 * 
	 * @return
	 */
	private List<String> fillDownloadData34(String dataFlag) {
		List<String> listData = new ArrayList<String>();
		// 获取手机信息工具类
		TelephonyManager telephonemanage = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 获取配置数据
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"cdc_config", Context.MODE_PRIVATE);
		String orgNo = pref.getString("orgno", "");
		// 填充参数
		listData.add(telephonemanage.getDeviceId());
		listData.add(orgNo);
		listData.add(orgNo);
		// listData.add(userName);
		listData.add(dataFlag);// 数据下载标识
		listData.add("1");// 第一页
		listData.add("5000");// 5000条
		return listData;
	}

	/**
	 * 下载员工任务查询
	 */
	private void downloadDeliverTaskList() {
		new Thread() {
			@Override
			public void run() {
				String url = Global.FindTaskByWorker_URL + "orgCode=" + orgCode
						+ "&userCode=" + userName;
				String result = com.newcdc.tools.NetHelper.doGet(url);
				Log.e("下载员工任务查询", result);
				org.json.JSONObject obj;
				try {
					obj = new org.json.JSONObject(result);
					String jsonresult = obj.getString("result");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	// 获得返回值中result字段的值
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

}

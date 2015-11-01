/**
 * 
 */
package com.newcdc.service;

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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.Toast;

import com.newcdc.R;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.DeliverMailDao;
import com.newcdc.db.DeliverTaskListDao;
import com.newcdc.db.GroupDao;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * 查询名址信息服务
 * 
 * 问题： 1-下段频次的查询接口 --坐等 2-和韩荣关于任务列表新增邮件数量的显示 韩荣做 3-投递列表的图片点击启动下载问题 4-下段蓝牙实现批量扫描
 * 5-投递增加手机号 ok
 * 
 * @author zhangfan 2015-3-27,下午4:12:31
 * 
 */
public class DeliverService extends IntentService {
	private final String TAG = "DliverService";
	private Context context;
	private DeliverMailDao mailDao;
	private DeliverDao deliverDao;
	private GroupDao groupDao;
	private DeliverTaskListDao taskDao;
	private SharePreferenceUtilDaoFactory shareDao;
	private UserInfoUtils user;
	private String orgCode;
	private String dlvSectionCode;
	private String userName;
	private String dlvsectionid;
	private String deviceNo;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(getApplicationContext(),
					"更新完毕，共" + msg.what + "条下段数据", Toast.LENGTH_SHORT).show();
		};
	};
	private DeliverTaskListDao mTaskListDao;

	public DeliverService() {
		super("deliverService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		context = getApplicationContext();
		if (Utils.isNetworkAvailable(context)) {
			DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
			deliverDao = daoFactory.getDeliverDao(context);
			groupDao = daoFactory.getGroupDao(getApplicationContext());
			shareDao = new SharePreferenceUtilDaoFactory();
			taskDao = daoFactory.getDeliverTaskListDao(context);
			user = new UserInfoUtils(context);
			dlvSectionCode = user.getDlvsectionCode();
			orgCode = user.getUserDelvorgCode();
			userName = user.getUserName();
			dlvsectionid = user.getDlvsectionid();
			deviceNo = Utils.getDeviceId(context);
			mailDao = daoFactory.getDeliverMailDao(context);
			mTaskListDao = daoFactory.getDeliverTaskListDao(context);
			// queryDeliverInfo();
			findTaskMail();
		}
	}

	/**
	 * 查询下段名址信息
	 */
	private void findTaskMail() {
		HashMap<String, String> taskInfo = shareDao.getDeliverTaskInformation();
		Log.e("taskinfo", taskInfo + "");
		if (taskInfo != null) {
			// String taskCode = "T1000381420150413200001";
			// String taskSerial = "1";
			// String taskCount = taskInfo.get("taskCount");
			// orgCode = "10003814";
			// userName = "0085";
			String taskCode = taskInfo.get("taskCode");
			String taskSerial = taskInfo.get("taskSerial");
			String taskCount = taskInfo.get("taskCount");// 暂时没用到
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("taskCode", taskCode));
			params.add(new BasicNameValuePair("taskSerial", taskSerial));
			params.add(new BasicNameValuePair("segmentCode", dlvSectionCode));
			params.add(new BasicNameValuePair("orgCode", orgCode));
			params.add(new BasicNameValuePair("dlvUserCode", userName));
			params.add(new BasicNameValuePair("deviceNo", deviceNo));
			params.add(new BasicNameValuePair("deviceType", "android"));
			Log.e(TAG, "查询名址提交参数=" + params.toString());
			long startTimeMillis = System.currentTimeMillis();// 请求开始时间
			String operate_time = Utils.getCurrTime();// 操作时间
			long batch_no = new Date().getTime();
			String result = NetHelper.doPost(Global.DELIVER_URL, params);

			Log.e(TAG, "查询名址返回参数=" + result);
			parseTaskMailJson(startTimeMillis, operate_time, batch_no, result);
		} else {
			sendBroadcast(new Intent(Constant.ACTION_ONREFRESHCOMPLETE));
		}
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
				shareDao.setDeliverTaskInformation(null);
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
				NetHelper.saveTransferLog(operate_time, endTime
						- startTimeMillis + "", context, context.getResources()
						.getString(R.string.operate_action_deliver_mingzhi),
						dataList.length() + "", batch_no + "");
				for (int i = 0; i < dataList.length(); i++) {
					frequenceList = mTaskListDao.queryGatherTaskMessage(
							orgCode, userName);
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
						flag = obj.getString("flag");
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
					// String dlv_sts_code = obj.getString("dlv_sts_code");
					if (deliverDao.queryMail(mail_num)) {
						if (obj.has("frequence")) {
							frequence = String.valueOf(obj.getInt("frequence"));// ok
							// Log.e("tag", "frequence"+frequence);
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
									// Log.e("tag", "allCount--"+allCount);
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
						// if ("I".equals(dlv_sts_code.toUpperCase())) {
						// bean.setDealResult(Constant.TUOTOU);
						// } else if ("H".equals(dlv_sts_code.toUpperCase())) {
						// bean.setDealResult(Constant.WEITUOTOU);
						// } else {
						bean.setDealResult(Constant.DAICHULI);
						// }
						list.add(bean);
					}else {
						deliverDao.deleteByMailNumber(mail_num);
						if (obj.has("frequence")) {
							frequence = String.valueOf(obj.getInt("frequence"));// ok
							// Log.e("tag", "frequence"+frequence);
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
									// Log.e("tag", "allCount--"+allCount);
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
						// if ("I".equals(dlv_sts_code.toUpperCase())) {
						// bean.setDealResult(Constant.TUOTOU);
						// } else if ("H".equals(dlv_sts_code.toUpperCase())) {
						// bean.setDealResult(Constant.WEITUOTOU);
						// } else {
						bean.setDealResult(Constant.DAICHULI);
						// }
						list.add(bean);
					}
				}
				// 将全部邮件存入下段表
				deliverDao.insertDeliver(list);
				groupDao.updateDealMailCount(getApplicationContext());

				mHandler.post(new Runnable() {
					@Override
					public void run() {
						int newMailCount = list.size();
						// 发送广播通知页面刷新
						Utils.sendDownCompleteBroadcast(context, newMailCount,
								Constant.PUSH_TYPE_DELIVERTASK);
						if (newMailCount != 0) {
							Toast.makeText(context,
									"更新完毕，共" + newMailCount + "条下段数据",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 解析名址JSON （已过期）
	 */
	private void parseTaskMailJsonTest(Long startTimeMillis,
			String operate_time, Long batch_no, String json) {
		try {
			JSONObject reObj = new JSONObject(json);
			int result = reObj.getInt("result");
			switch (result) {
			case 0:
				break;
			case 1:
				// 清除下段流水信息
				shareDao.setDeliverTaskInformation(null);
				// 插入到下段信息表中的邮件
				final ArrayList<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
				// 新增邮件频次统计
				ArrayList<HashMap<Integer, Integer>> newMail = new ArrayList<HashMap<Integer, Integer>>();

				// 全部频次
				ArrayList<Map<String, String>> frequenceList = mTaskListDao
						.queryGatherTaskMessage(orgCode, userName);
				JSONObject success = reObj.getJSONObject("body").getJSONObject(
						"success");
				int endIndex = success.getInt("endIndex");
				int limit = success.getInt("limit");
				int offset = success.getInt("offset");
				int pageNo = success.getInt("pageNo");
				JSONArray resultList = success.getJSONArray("resultList");
				long endTime = System.currentTimeMillis();// 请求结束时间
				// 提交请求日志
				NetHelper.saveTransferLog(operate_time, endTime
						- startTimeMillis + "", context, context.getResources()
						.getString(R.string.operate_action_deliver_mingzhi),
						resultList.length() + "", batch_no + "");
				for (int i = 0; i < resultList.length(); i++) {
					JSONObject obj = resultList.getJSONObject(i);
					// String the_class_date = Utils.getCurrTime(obj
					// .getLong("the_class_date"));// ok
					String the_class_date = obj.getString("the_class_date");
					String sender_contact_phone1 = obj
							.getString("sender_contact_phone1");// ok
					String dlvorgcode = obj.getString("dlvorgcode");// ok
					String sender_loc_city = obj.getString("sender_loc_city");// ok
					String sender_name = obj.getString("sender_name");// ok
					String rcver_addr = obj.getString("rcver_addr");// ok
					String rcver_name = obj.getString("rcver_name");// ok
					String rcver_street_addr = obj
							.getString("rcver_street_addr");// ok
					String rcver_contact_phone1 = obj
							.getString("rcver_contact_phone1");// ok
					String username = obj.getString("username");// ok
					String money = obj.getString("money");// ok
					String sender_addr = obj.getString("sender_addr");// ok
					String sender_street_addr = obj
							.getString("sender_street_addr");// ok
					String mail_num = obj.getString("mail_num");// ok
					// String dlvsectionid = obj.getString("dlvsectionid");//
					// new
					String mail_prpty_code = obj.getString("mail_prpty_code");// new
					String sender_loc_prov = obj.getString("sender_loc_prov");// ok
					String frequence = obj.getString("frequence");// ok
					for (int j = 0; j < frequenceList.size(); j++) {
						if (frequenceList.get(j).get("delivertasknum")
								.equals(frequence)) {
							int newCount = Integer.parseInt(frequenceList
									.get(j).get("delivertaskcount")) + 1;
							int allCount = Integer.parseInt(frequenceList
									.get(j).get("delivertaskallcount")) + 1;
							String delivertasknum = frequenceList.get(j).get(
									"delivertasknum");
							mTaskListDao.updateDeliverOneTaskCount(orgCode,
									userName, newCount + "", delivertasknum);
							mTaskListDao.updateDeliverMoreTaskCount(orgCode,
									userName, allCount + "", delivertasknum);
						}
					}

					HashMap<String, String> map = new HashMap<String, String>();
					String sender_loc_county = obj
							.getString("sender_loc_county");
					String dlv_pseg_code = obj.getString("dlv_pseg_code");
					String rcver_loc_prov = obj.getString("rcver_loc_prov");
					String rcver_loc_city = obj.getString("rcver_loc_city");

					String rcver_loc_county = obj.getString("rcver_loc_county");
					String flag = obj.getString("flag");
					int sid = obj.getInt("sid");
					String dlv_date = obj.getString("dlv_date");
					String dlv_time = obj.getString("dlv_time");
					int ROWNUM_ = obj.getInt("ROWNUM_");
					String undlv_next_actn_code = obj
							.getString("undlv_next_actn_code");
					String undlv_cause_code = obj.getString("undlv_cause_code");
					String sign_sts_code = obj.getString("sign_sts_code");
					String signer_name = obj.getString("signer_name");
					String dlv_sts_code = obj.getString("dlv_sts_code");
					MessageInfoBean bean = new MessageInfoBean(
							rcver_loc_county, dlvorgcode, rcver_contact_phone1,
							sender_loc_county, sender_street_addr,
							rcver_loc_prov, sender_contact_phone1, rcver_name,
							the_class_date, rcver_street_addr, sender_name,
							sender_loc_prov, frequence, sender_loc_city,
							rcver_loc_city, mail_num, username, sender_addr,
							rcver_addr, money, dlvsectionid);
					bean.setFlag(flag);// TODO 缴款
					bean.setUndlv_cause_code(undlv_cause_code);
					bean.setUndlv_next_actn_code(undlv_next_actn_code);
					bean.setSign_sts_code(sign_sts_code);
					bean.setSigner_name(signer_name);
					bean.setDlv_date(dlv_date);
					bean.setDlv_time(dlv_time);
					bean.setSid(sid);
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
				// 将全部邮件存入下段表
				deliverDao.insertDeliver(list);
				// TODO 将新增邮件根据频次插入到韩荣的表中
				// 发送广播通知页面刷新
				sendBroadcast(new Intent(Constant.ACTION_DOWN_DATA_OVER));
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						int newMailCount = list.size();

						if (newMailCount != 0) {
							Message msg = new Message();
							msg.what = newMailCount;
							mHandler.sendMessage(msg);
						}
					}
				});
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 查询下段名址信息（已过期）
	 */
	private void queryDeliverInfo() {
		for (int i = 0; i < Constant.REPEAT_TIME; i++) {
			if (Utils.isNetworkAvailable(context)) {
				ArrayList<HashMap<String, String>> mailList = mailDao
						.findWillQueryMail();
				if (mailList.size() == 0) {
					// 未提交邮件数为0则跳出循环，结束service
					break;
				} else {
					// 扔有邮件需要提交
					request(mailList);
				}
			} else {
				// 网络不可用，跳出循环
				break;
			}
		}
	}

	/**
	 * 提交请求,查询下段（已过期）
	 */
	public void request(ArrayList<HashMap<String, String>> mails) {
		int times = (int) Math.ceil(mails.size() / 10.0);// 循环次数
		for (int i = 0; i < times; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append("mail_num1=");
			// 找子串
			List<HashMap<String, String>> subList;
			if (i == times - 1) {// 最后一次循环，取出所有的邮件
				subList = mails.subList(i * 10, mails.size());
			} else {// 其他次每次取10个
				subList = mails.subList(i * 10, (i + 1) * 10);
			}
			// 拼邮件集合参数
			for (int j = 0; j < subList.size(); j++) {
				// 请求名址信息
				if (j == subList.size() - 1) {
					sb.append(mails.get(j).get("mail_num"));
				} else {
					sb.append(mails.get(j).get("mail_num") + "&mail_num"
							+ (j + 2) + "=");
				}
			}
			// 提交查询
			String result = NetHelper.doGet(Global.MINGZHI + sb.toString());
			Log.e("查询名址", result + "");
			// 将名址信息更新到下段表中，并且从下段邮件表中置为已提交
			parseJsonInsertTb(result, subList);
		}

	}

	/**
	 * 提交请求,查询下段（已过期）
	 */
	public void testRequest() {
		StringBuffer sb = new StringBuffer();
		sb.append("mail_num1=");
		ArrayList<String> mailList = new ArrayList<String>();
		mailList.add("1000376036802");
		mailList.add("1004549109705");
		mailList.add("1050041256609");
		mailList.add("1011646848205");
		mailList.add("1025763879309");
		mailList.add("1050091247909");
		mailList.add("1085797719708");
		mailList.add("1092362564001");
		mailList.add("1092362564002");
		mailList.add("1025207982301");
		mailList.add("1025207982302");
		int times = (int) Math.ceil(mailList.size() / 10.0);// 循环次数
		for (int i = 0; i < times; i++) {
			// 找子串
			ArrayList<String> subList;
			if (i == times - 1) {// 最后一次循环，取出所有的邮件
				subList = (ArrayList<String>) mailList.subList(i * 10,
						mailList.size());
			} else {// 其他次每次取10个
				subList = (ArrayList<String>) mailList.subList(i * 10,
						(i + 1) * 10);
			}
			// 拼邮件集合参数
			for (int j = 0; j < subList.size(); j++) {
				// 请求名址信息
				if (j == subList.size() - 1) {
					sb.append(mailList.get(j));
				} else {
					sb.append(mailList.get(j) + "&mail_num" + (j + 2) + "=");
				}
			}
			// 提交查询
			String result = NetHelper.doGet(Global.MINGZHI + sb.toString());
			Log.e("查询名址", result + "");
			// 将名址信息更新到下段表中，并且从下段邮件表中置为已提交
		}

	}

	/**
	 * 解析json并插入到表中（已过期）
	 */
	public void parseJsonInsertTb(String result,
			List<HashMap<String, String>> mailList) {
		if (result != null) {
			try {
				JSONObject resultObj = new JSONObject(result);
				int resultTag = resultObj.getInt("result");
				switch (resultTag) {
				case 0:
					resultObj.getJSONObject("body").getString("error");
					// Intent intent = new Intent();
					// intent.setAction(Constant.ACTION_NOTIFY);
					// intent.putExtra("result", resultTag);
					// intent.putExtra("message", message);
					// sendBroadcast(intent);
					break;
				case 1:
					JSONArray success = resultObj.getJSONObject("body")
							.getJSONArray("success");
					ArrayList<String> arrayList = new ArrayList<String>();
					for (int j = 0; j < mailList.size(); j++) {
						arrayList.add(mailList.get(j).get("mail_num"));
					}
					// 下段信息表删除本次邮件
					// deliverDao.deleteMails(arrayList);
					mailDao.setMailQueryOk(arrayList);// 将本次邮件下段查询结果设置为成功
					ArrayList<MessageInfoBean> beans = new ArrayList<MessageInfoBean>();

					for (int i = 0; i < success.length(); i++) {
						JSONObject obj = success.getJSONObject(i);
						String mail_num = obj.getString("mail_num");// 解析出邮件号
						HashMap<String, String> mailMap = mailDao
								.findMailByMailNumber(mail_num);// 根据邮件号在邮件表中查出邮件
						String frequence = mailMap.get("frequence");// 下段邮件表获取到提交的频次
						String[] split = mailMap.get("the_class_date").split(
								",");// yyyymmddHHmmss
						String the_class_date = split[1];
						String sender_contact_phone1 = obj
								.getString("sender_contact_phone1");
						String rcver_loc_prov = obj.getString("rcver_loc_prov");
						String sender_loc_city = obj
								.getString("sender_loc_city");
						String sender_name = obj.getString("sender_name");
						String rcver_addr = obj.getString("rcver_addr");
						String rcver_street_addr = obj
								.getString("rcver_street_addr");
						String rcver_name = obj.getString("rcver_name");
						String rcver_contact_phone1 = obj
								.getString("rcver_contact_phone1");
						String rcver_loc_city = obj.getString("rcver_loc_city");
						String sender_addr = obj.getString("sender_addr");
						String sender_loc_county = obj
								.getString("sender_loc_county");
						String sender_street_addr = obj
								.getString("sender_street_addr");
						String rcver_loc_county = obj
								.getString("rcver_loc_county");
						String sender_loc_prov = obj
								.getString("sender_loc_prov");
						MessageInfoBean bean = new MessageInfoBean(
								rcver_loc_county, orgCode,
								rcver_contact_phone1, sender_loc_county,
								sender_street_addr, rcver_loc_prov,
								sender_contact_phone1, rcver_name,
								the_class_date, rcver_street_addr, sender_name,
								sender_loc_prov, frequence, sender_loc_city,
								rcver_loc_city, mail_num, userName,
								sender_addr, rcver_addr, "0", dlvsectionid);
						beans.add(bean);
					}
					// 下段信息表插入成功邮件信息
					deliverDao.updateDeliverMail(beans);
					GroupDao groupDao = DeliverDaoFactory.getInstance()
							.getGroupDao(context);
					groupDao.updateDealMailCount(context);
					// 发送广播通知下段列表页刷新界面
					Intent intent = new Intent(Constant.ACTION_NOTIFY);
					intent.putExtra("result", resultTag);
					sendBroadcast(intent);
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

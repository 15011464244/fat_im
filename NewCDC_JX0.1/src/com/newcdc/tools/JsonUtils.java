package com.newcdc.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.GroupDao;
import com.newcdc.model.GatherMessageResultBean;
import com.newcdc.model.GatherTableBean;
import com.newcdc.model.GroupBean;
import com.newcdc.model.MessageInfoBean;

/**
 * json解析公共类
 * 
 * @author zf
 * 
 */
public class JsonUtils {
	/**
	 * 解析下段信息并存储
	 * 
	 * @param context
	 * @param json
	 *            要解析的json
	 * @return 新增的邮件个数
	 * @throws JSONException
	 */
	public static int parseDeliverJson(Context context, JSONArray array)
			throws JSONException {
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		DeliverDao dao = daoFactory.getDeliverDao(context);
		ArrayList<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String the_class_date = obj.getString("the_class_date");
			String sender_contact_phone1 = obj
					.getString("sender_contact_phone1");
			String rcver_loc_prov = obj.getString("rcver_loc_prov");
			String dlvorgcode = obj.getString("dlvorgcode");
			String sender_loc_city = obj.getString("sender_loc_city");
			String sender_name = obj.getString("sender_name");
			String rcver_addr = obj.getString("rcver_addr");
			String rcver_name = obj.getString("rcver_name");
			String rcver_street_addr = obj.getString("rcver_street_addr");
			String rcver_contact_phone1 = obj.getString("rcver_contact_phone1");
			String rcver_loc_city = obj.getString("rcver_loc_city");
			String username = obj.getString("username");
			String money = obj.getString("money");
			String dlv_pseg_code = obj.getString("dlv_pseg_code");
			String sender_loc_county = obj.getString("sender_loc_county");
			String sender_addr = obj.getString("sender_addr");
			String mail_num = obj.getString("mail_num");
			String sender_street_addr = obj.getString("sender_street_addr");
			String rcver_loc_county = obj.getString("rcver_loc_county");
			String frequence = obj.getString("frequence");
			String sender_loc_prov = obj.getString("sender_loc_prov");
			String flag = obj.getString("flag");
			int sid = obj.getInt("sid");
			String dlv_date = obj.getString("dlv_date");
			String dlv_time = obj.getString("dlv_time");
			String undlv_next_actn_code = obj.getString("undlv_next_actn_code");
			String undlv_cause_code = obj.getString("undlv_cause_code");
			String sign_sts_code = obj.getString("sign_sts_code");
			String signer_name = obj.getString("signer_name");
			String dlv_sts_code = obj.getString("dlv_sts_code");
			MessageInfoBean bean = new MessageInfoBean(rcver_loc_county,
					dlvorgcode, rcver_contact_phone1, sender_loc_county,
					sender_street_addr, rcver_loc_prov, sender_contact_phone1,
					rcver_name, the_class_date, rcver_street_addr, sender_name,
					sender_loc_prov, frequence, sender_loc_city,
					rcver_loc_city, mail_num, username, sender_addr,
					rcver_addr, money, dlv_pseg_code);
			bean.setFlag(flag);
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
		dao.insertDeliver(list);
		// 将妥投、未妥投邮件存入投递表
		DealDeliverTools.insertDeliverData(list, context);
		return list.size();
	}

	/**
	 * 解析分组信息并存储
	 */
	public static void parseGroupJson(Context context, String json) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			JSONArray array = jsonObj.getJSONObject("body").getJSONArray(
					"success");
			GroupDao dao = DeliverDaoFactory.getInstance().getGroupDao(context);
			List<GroupBean> list = new ArrayList<GroupBean>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String sid = obj.getString("sid");
				// String group_type = obj.getString("group_type");
				String update_time = obj.getString("update_time");// null
				String group_content = obj.getString("group_content");
				String create_by = obj.getString("create_by");// null
				String create_time = obj.getString("create_time");
				String update_by = obj.getString("update_by");// null
				GroupBean bean = new GroupBean(sid, group_content,
						Constant.OTHERGROUP + "", create_by, create_time,
						update_by, update_time, ",", "0");
				list.add(bean);
			}
			dao.insertGroup(list, context);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取发送短信返回的message
	 * 
	 * @return 格式："msg,result"
	 */
	public static String parseMsgJson(JSONObject json) {
		String msg = null;
		try {
			JSONObject body = json.getJSONObject("body");
			JSONArray success = body.getJSONArray("success");
			for (int i = 0; i < success.length(); i++) {
				JSONObject obji = success.getJSONObject(i);
				msg = obji.getString("msg");
				String result = obji.getString("result");
				msg = msg + "," + result;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 解析派揽信息并存储
	 */

	public static String parseGatherMessageJson(Context context, String result,
			Long startTimeMillis, String operate_time, Long batch_no) {
		if (result.toString().equals("请求服务器失败")) {
			return "-2";
		} else if (resState(result.toString()).equals("0")) {
			return "-1";
		} else {
			GatherMessageResultBean beans = com.alibaba.fastjson.JSONObject
					.parseObject(result.toString().toLowerCase(),
							GatherMessageResultBean.class);
			int count = beans.getBody().getSuccess().size();
			long endTime = System.currentTimeMillis();// 请求结束时间
			// 提交请求日志
			NetHelper.saveTransferLog(
					operate_time,
					endTime - startTimeMillis + "",
					context,
					context.getResources().getString(
							R.string.operate_action_clct_down), count + "",
					batch_no + "");
			ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (int a = 0; a < count; a++) {
				Map<String, String> map = new HashMap<String, String>();
				String msg = beans.getBody().getSuccess().get(a)
						.getMsg_content();
				String msg1 = msg.substring(1, msg.length() - 1);
				String[] msgcontent = msg1.split("\t");
				map.put("phone", msgcontent[4]);
				map.put("clientname", msgcontent[1]);
				map.put("address", msgcontent[3]);
				map.put("lssx", msgcontent[5]);
				map.put("cnday", "0");
				map.put("reservenum", msgcontent[0]);
				map.put("device_number", beans.getBody().getSuccess().get(a)
						.getDevice_number());
				map.put("operationtime", beans.getBody().getSuccess().get(a)
						.getOperationtime());
				map.put("msg_id", beans.getBody().getSuccess().get(a)
						.getMsg_id());
				map.put("msg_typecode", beans.getBody().getSuccess().get(a)
						.getMsg_typecode());
				map.put("msg_code", beans.getBody().getSuccess().get(a)
						.getMsg_code());
				map.put("companyid", beans.getBody().getSuccess().get(a)
						.getCompanyid());
				list.add(map);
				// Log.e("tag", "tiao""
				// + beans.getBody().getSuccess().get(a).getMsg_content());
			}
			if (Global.isLan) {
				Log.e("baocun", "adfdaf" + count + "-------" + list.size());
				DeliverDaoFactory.getInstance().getGather_msgdao(context)
						.saveGatherMessage(list);
			}
			return "" + count;
		}
	}

	/**
	 * 解析智能派揽信息并存储
	 * 
	 * @param <V>
	 */
	public static <V> void parseGatherMessageJsonzhineng(Context context,
			String result, Long startTimeMillis, String operate_time,
			Long batch_no) throws Exception {
		if (result == null || result.toString().equals("请求服务器失败")) {
		} else {
			JSONObject obj = new JSONObject(result);
			int re = obj.getInt("result");
			switch (re) {
			case 0:
				break;
			case 1:
				long endTime = System.currentTimeMillis();// 请求结束时间
				// 提交请求日志
				NetHelper.saveTransferLog(operate_time, endTime
						- startTimeMillis + "", context, context.getResources()
						.getString(R.string.operate_action_clct_down_zn), "1",
						batch_no + "");
				ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
				// JSONObject content = obj.getJSONObject("content");
				JSONArray jsonArray = obj.getJSONArray("dataList");
				if (jsonArray != null && jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						try {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							List<GatherTableBean> beans = new ArrayList<GatherTableBean>();
							String companyid = jsonObject.getString("taskNum");
							beans = DeliverDaoFactory.getInstance()
									.getGather_msgdao(context)
									.query_companyid(companyid);
							if (beans == null || beans.size() == 0) {
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("phone",
										jsonObject.getString("senderMobile"));
								map.put("clientname",
										jsonObject.getString("senderName"));
								map.put("address",
										jsonObject.getString("senderStreet"));

								map.put("senderProvCode",
										jsonObject.getString("senderProvCode"));
								map.put("senderCityCode",
										jsonObject.getString("senderCityCode"));
								map.put("senderCountyCode", jsonObject
										.getString("senderCountyCode"));
								map.put("senderCountryCode", jsonObject
										.getString("senderCountryCode"));
								map.put("senderProv",
										jsonObject.getString("senderProv"));
								map.put("senderCity",
										jsonObject.getString("senderCity"));
								map.put("senderCounty",
										jsonObject.getString("senderCounty"));
								map.put("receiverName",
										jsonObject.getString("receiverName"));
								map.put("receiverMobile",
										jsonObject.getString("receiverMobile"));
								map.put("receiverProvCode", jsonObject
										.getString("receiverProvCode"));
								map.put("receiverCityCode", jsonObject
										.getString("receiverCityCode"));
								map.put("receiverCountyCode", jsonObject
										.getString("receiverCountyCode"));
								map.put("receiverProv",
										jsonObject.getString("receiverProv"));
								map.put("receiverCity",
										jsonObject.getString("receiverCity"));
								map.put("receiverCounty",
										jsonObject.getString("receiverCounty"));
								map.put("receiverCountryCode", jsonObject
										.getString("receiverCountryCode"));
								map.put("receiverStreet",
										jsonObject.getString("receiverStreet"));
								map.put("orderWeight",
										jsonObject.getString("orderWeight"));
								map.put("payment",
										jsonObject.getString("payment"));
								map.put("lssx",
										jsonObject.getString("orderTime"));
								if ("3".equals(jsonObject
										.getString("taskStatus"))) {
									map.put("cnday", "0");// 已确认任务
								} else {
									map.put("cnday", "1");// 智能平台
								}
								map.put("reservenum",
										jsonObject.getString("orderNum"));
								// map.put("device_number",
								// jsonObject.getString("sendType"));// 1：代表即时下单
								// 2：代表预约下单
								map.put("operationtime", "");
								map.put("msg_id",
										jsonObject.getString("userSid"));// 快递帮手注册用户的id
								map.put("msg_typecode",
										jsonObject.getString("serviceType"));
								map.put("taskAllotTime",
										jsonObject.getString("taskAllotTime"));
								if (jsonObject.has("useIntegral")) {
									map.put("msg_code",
											jsonObject.getString("useIntegral"));// 使用积分
								} else {
									map.put("msg_code", "0");// 使用积分
								}
								if (jsonObject.has("userValidIntegral")) {
									map.put("userValidIntegral", jsonObject
											.getString("userValidIntegral"));// 客户可用的积分
								} else {
									map.put("userValidIntegral", "0");// 客户可用的积分
								}
								// 下单类型
								String sendType = jsonObject
										.getString("sendType");// 1及时 2 预约
								map.put("sendType", sendType);
								if ("2".equals(sendType)) {
									map.put("startTime",
											jsonObject.getString("startTime"));//
								} else {
									map.put("startTime", "");//
								}
								map.put("companyid",
										jsonObject.getString("taskNum"));// 消息来源
								list.add(map);
							}
						} catch (Exception e) {
							LogUtils.e("" + e.getMessage());
						}
					}
					DeliverDaoFactory.getInstance().getGather_msgdao(context)
							.saveGatherMessage(list);
				}
				break;
			}
		}
	}

	public static String resState(String s) {
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

package com.newcdc.asynctask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.newcdc.R;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.QueueDao;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.Utils;

/**
 * 投递反馈
 * 
 * @author zhangfan repeatCount15-1-30,下午2:55:35
 * 
 */
public class DeliverTask {
	/**
	 * 提交请求
	 * 
	 * @param context
	 * @param repeatCount
	 *            批量上传每组的邮件个数
	 * @return 返回1表示全部上传完成
	 */
	public static int doDeliverRequest(Context context, int repeatCount) {
		QueueDao queueDao = DeliverDaoFactory.getInstance()
				.getQueueDao(context);
		for (int i = 0; i < Constant.REPEAT_TIME; i++) {
			if (Utils.isNetworkAvailable(context)) {
				// 查出所有未提交的邮件
				ArrayList<DeliverQueueBean> list = queueDao.queryAll();
				if (doRequest(list, context, queueDao, repeatCount)) {
					return 1;
				}
			} else {
				Toast.makeText(context, "您的网络存在问题哦", Toast.LENGTH_SHORT).show();
				return 0;
			}
		}
		return 0;
	}

	/**
	 * 投递反馈
	 * 
	 * @return 返回true表示全部上传完成，无失败邮件
	 */
	private static boolean doRequest(ArrayList<DeliverQueueBean> list,
			Context context, QueueDao queueDao, int repeatCount) {
		boolean requestResult = false;
		try {
			TelephonyManager telephonemanage = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String deviceId = telephonemanage.getDeviceId();// 设备号
			// 分批次循环执行
			for (int j = 0; j < list.size() / repeatCount + 1; j++) {
				List<DeliverQueueBean> subList = new ArrayList<DeliverQueueBean>();
				if (j * repeatCount + repeatCount > list.size()) {
					subList.addAll(list.subList(j * repeatCount, list.size()));
				} else {
					subList.addAll(list.subList(j * repeatCount, j
							* repeatCount + repeatCount));
				}
				String result = upload(context, deviceId, subList, queueDao);
				Log.e("投递反馈提交", result + "");
				if (!"0".equals(result)) {// 执行完每批之后，若发生失败，则将本轮doRequest设定为失败
					requestResult = true;
				}
			}
		} catch (Exception e) {
			requestResult = false;
			Log.e("投递反馈提交err", e.getMessage());
		}
		if (requestResult) {
			// 发送广播将本次提交结果通知UI刷新界面
			DeliverDaoFactory.getInstance().getGroupDao(context)
					.updateUncommitMailCount(context);
			context.sendBroadcast(new Intent(Constant.ACTION_ASYNCQUEUE));
		}
		return requestResult;
	}

	/**
	 * 网络请求
	 */
	private static String upload(Context context, String deviceId,
			List<DeliverQueueBean> subList, QueueDao queueDao)
			throws JSONException {
		long startTimeMillis = System.currentTimeMillis();// 请求开始时间
		String operate_time = Utils.getCurrTime();// 操作时间
		long batch_no = new Date().getTime();
		JSONArray array = new JSONArray();
		for (int i = 0; i < subList.size(); i++) {
			DeliverQueueBean bean = subList.get(i);
			JSONObject obj = new JSONObject();
			obj.put("operatortype", "I");
			obj.put("mail_num", bean.getMail_num());
			obj.put("dlv_bureau_org_code", bean.getOrgCode());
			obj.put("dlv_sts_code", bean.getDlv_sts_code());
			obj.put("dlv_date", bean.getDlv_date());
			obj.put("dlv_time", bean.getDlv_time());
			obj.put("rcver_phone", bean.getRcver_phone());
			obj.put("sender_phone", bean.getSender_phone());
			obj.put("data_upload_timestamp", Utils.getCurrTime());
			obj.put("dlv_pseg_code", bean.getDlv_pseg_code());
			obj.put("sign_sts_code", bean.getSign_sts_code());
			obj.put("actual_goods_fee", bean.getActual_goods_fee());
			obj.put("dlv_staff_code", bean.getUserName());// 工号
			obj.put("dlv_staff_name", bean.getRealName());// 真实姓名
			obj.put("undlv_cause_code", bean.getUndlv_cause_code());// 未妥投原因
			obj.put("undlv_next_actn_code", bean.getUndlv_next_actn_code());// 下一步动作
			obj.put("data_clct_mode_code", "2");// 数据采集方式代码
			obj.put("signer_name", bean.getSigner_name());// 签收人姓名
			obj.put("wl_term_num", deviceId);
			obj.put("data_src_sys", "5");// 数据来源系统
			obj.put("inter_flag", "0");// 国际标志
			obj.put("frequence", bean.getFrequence());
			// obj.put("frequence", bean.getFrequence());
			Log.e("收件人phone", bean.getRcver_phone() + "");
			Log.e("ji件人phone", bean.getSender_phone() + "");
			obj.put("pay_mode", bean.getPay_mode());// 付款方式
			array.put(i, obj);
		}
		Log.e("投递反馈参数", array.toString());
		String result = NetHelper.doPostJson(Global.DLVMSGRTN,
				array.toString(), "data");
		Log.e("投递返回结果", result + "");
		if (result != null) {
			JSONObject resultObj = new JSONObject(result);
			if ("1".equals(resultObj.get("result"))) {// 全部成功
				queueDao.updateCommitResult((ArrayList<DeliverQueueBean>) subList);
			} else if ("2".equals(resultObj.get("result"))) {
				// TODO 新增的返回状态为2
				queueDao.updateCommitResult((ArrayList<DeliverQueueBean>) subList);// 先把所有之前未上传的状态都改为上传成功
																					// 下面解析到未上传成功的邮件号之后再根据邮件号把成功的状态改成未成功的状态
				JSONArray errArray = new JSONArray();
				errArray = resultObj.getJSONObject("content").getJSONArray(
						"errorMail");
				for (int i = 0; i < errArray.length(); i++) {
					queueDao.updateCommitResultByMail_num(errArray
							.getJSONObject(i).getString("errorCode"));
				}
			}
			// 提交完成则向服务器发送提交日志
			int count = subList.size();// 提交条数
			long endTime = System.currentTimeMillis();// 请求结束时间
			// 提交请求日志
			NetHelper.saveTransferLog(
					operate_time,
					endTime - startTimeMillis + "",
					context,
					context.getResources().getString(
							R.string.operate_action_deliver_return),
					count + "", batch_no + "");
			return resultObj.get("result").toString();
		} else {
			return "0";
		}
	}
}

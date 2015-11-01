package com.newcdc.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newcdc.db.helper.QueueDaoHelper;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

public class QueueDao extends QueueDaoHelper {
	public QueueDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	public synchronized void insertXD(ArrayList<DeliverQueueBean> list,
			Context context) {
		SQLiteDatabase db = getWritableDatabase();
		UserInfoUtils user = new UserInfoUtils(context);
		String orgCode = user.getUserDelvorgCode();
		String userName = user.getUserName();
		String realName = user.getRealname();
		for (int i = 0; i < list.size(); i++) {
			ContentValues values = new ContentValues();
			DeliverQueueBean bean = list.get(i);
			values.put("orgCode", orgCode);
			values.put("userName", userName);
			values.put("realName", realName);
			values.put("rcver_phone", bean.getRcver_phone());
			values.put("sender_phone", bean.getSender_phone());
			values.put("mail_num", bean.getMail_num());
			values.put("flag", bean.getFlag());
			String dlv_date = "null".equals(bean.getDlv_date()) ? Utils
					.getSysDateString() : bean.getDlv_date();
			values.put("dlv_date", dlv_date);
			String dlv_time = "null".equals(bean.getDlv_time()) ? Utils
					.getSysTimeString() : bean.getDlv_time();
			values.put("dlv_time", dlv_time);
			values.put("dlv_bureau_org_code", bean.getDlv_bureau_org_code());
			values.put("dlv_pseg_code", bean.getDlv_pseg_code());
			values.put("dlv_sts_code", bean.getDlv_sts_code());
			values.put("dlv_staff_code", bean.getDlv_staff_code());
			values.put("dlv_staff_name", bean.getDlv_staff_name());
			values.put("undlv_cause_code", bean.getUndlv_cause_code());
			values.put("undlv_next_actn_code", bean.getUndlv_next_actn_code());
			values.put("signer_name", bean.getSigner_name());
			values.put("wl_term_num", bean.getWl_term_num());
			String actual_goods_fee = bean.getActual_goods_fee();
			if (actual_goods_fee == null || "0".equals(actual_goods_fee)
					|| "0.0".equals(actual_goods_fee)
					|| "".equals(actual_goods_fee)) {
				values.put("actual_goods_fee", "0");

			} else {
				values.put("actual_goods_fee", bean.getActual_goods_fee());
			}
			values.put("sign_sts_code", bean.getSign_sts_code());
			values.put("pay_mode", bean.getPay_mode());
			values.put("frequence", bean.getFrequence());
			values.put("picture", bean.getPicture());
			values.put("data_upload_timestamp", bean.getData_upload_timestamp());
			values.put("commit_result", bean.getCommit_result());
			db.insert(QUEUE_TABLE, null, values);

		}
		db.close();
	}

	public synchronized void insert(ArrayList<DeliverQueueBean> list,
			Context context) {
		SQLiteDatabase db = getWritableDatabase();
		UserInfoUtils user = new UserInfoUtils(context);
		String orgCode = user.getUserDelvorgCode();
		String userName = user.getUserName();
		String realName = user.getRealname();
		for (int i = 0; i < list.size(); i++) {
			ContentValues values = new ContentValues();
			DeliverQueueBean bean = list.get(i);
			values.put("orgCode", orgCode);
			values.put("userName", userName);
			values.put("realName", realName);
			values.put("rcver_phone", bean.getRcver_phone());
			values.put("sender_phone", bean.getSender_phone());
			values.put("mail_num", bean.getMail_num());
			values.put("flag", bean.getFlag());
			String dlv_date = Utils.stringEmpty(bean.getDlv_date()) ? Utils
					.getSysDateString() : bean.getDlv_date();
			values.put("dlv_date", dlv_date);
			String dlv_time = Utils.stringEmpty(bean.getDlv_time()) ? Utils
					.getSysTimeString() : bean.getDlv_time();
			values.put("dlv_time", dlv_time);
			values.put("dlv_bureau_org_code", bean.getDlv_bureau_org_code());
			values.put("dlv_pseg_code", bean.getDlv_pseg_code());
			values.put("dlv_sts_code", bean.getDlv_sts_code());
			values.put("dlv_staff_code", bean.getDlv_staff_code());
			values.put("dlv_staff_name", bean.getDlv_staff_name());
			values.put("undlv_cause_code", bean.getUndlv_cause_code());
			values.put("undlv_next_actn_code", bean.getUndlv_next_actn_code());
			values.put("signer_name", bean.getSigner_name());
			values.put("wl_term_num", bean.getWl_term_num());
			String actual_goods_fee = bean.getActual_goods_fee();
			if (actual_goods_fee == null || "0".equals(actual_goods_fee)
					|| "0.0".equals(actual_goods_fee)
					|| "".equals(actual_goods_fee)) {
				values.put("actual_goods_fee", "0");

			} else {
				values.put("actual_goods_fee", bean.getActual_goods_fee());
			}
			values.put("sign_sts_code", bean.getSign_sts_code());
			values.put("pay_mode", bean.getPay_mode());
			values.put("frequence", bean.getFrequence());
			values.put("picture", bean.getPicture());
			values.put("data_upload_timestamp", bean.getData_upload_timestamp());
			values.put("commit_result", bean.getCommit_result());
			db.insert(QUEUE_TABLE, null, values);
			// 同时插入缴款表 //需要支付才做缴款
			if (("1".equals(bean.getFlag()) || "2".equals(bean.getFlag()))
					&& "TT".equals(bean.getDlv_sts_code())) {
				ContentValues map = new ContentValues();
				map.put("user_code", userName);
				map.put("org_code", orgCode);
				map.put("device_num", bean.getWl_term_num());
				map.put("pay_order_num", "");
				map.put("mail_num", bean.getMail_num());
				if (actual_goods_fee == null || "0".equals(actual_goods_fee)
						|| "0.0".equals(actual_goods_fee)
						|| "".equals(actual_goods_fee)) {
					map.put("amount", "0");

				} else {
					map.put("amount", bean.getActual_goods_fee());
				}
				map.put("pay_sort",
						"1".equals(bean.getFlag()) ? "3" : ("2".equals(bean
								.getFlag()) ? "4" : ""));
				map.put("payment_mode", "1");
				map.put("operationtime", "");
				map.put("flag", bean.getFlag());
				map.put("transmission", "G");
				map.put("weight", "");
				map.put("arri_code", "");
				map.put("cust_code", "");
				map.put("mail_operation_time", dlv_date + dlv_time);
				map.put("other_payment_type_code", "");
				map.put("vip_card", "");
				map.put("resv_col1", "");
				map.put("resv_col2", "");
				map.put("resv_col3", "");
				map.put("resv_col4", "");
				map.put("resv_col5", "");
				map.put("operatortype", "I");
				map.put("way_code", "");
				map.put("resv_col10", "");
				map.put("ismoney", "0");
				map.put("isLT", "0");
				map.put("frequency", "");
				map.put("createtime", new Date().getTime());
				map.put("username", "");
				DeliverDaoFactory.getInstance().getMoneyDao(context)
						.insertDeliverMoney(map);
			}
		}
		db.close();
	}

	/**
	 * 用户操作后的插入
	 */
	public synchronized void insertOperate(ArrayList<DeliverQueueBean> list,
			Context context) {
		SQLiteDatabase db = getWritableDatabase();
		UserInfoUtils user = new UserInfoUtils(context);
		String orgCode = user.getUserDelvorgCode();
		String userName = user.getUserName();
		String realName = user.getRealname();
		for (int i = 0; i < list.size(); i++) {
			ContentValues values = new ContentValues();
			DeliverQueueBean bean = list.get(i);
			values.put("orgCode", orgCode);
			values.put("userName", userName);
			values.put("realName", realName);
			values.put("mail_num", bean.getMail_num());
			values.put("flag", bean.getFlag());
			String dlv_date = Utils.stringEmpty(bean.getDlv_date()) ? Utils
					.getSysDateString() : bean.getDlv_date();
			values.put("dlv_date", dlv_date);
			String dlv_time = Utils.stringEmpty(bean.getDlv_time()) ? Utils
					.getSysTimeString() : bean.getDlv_time();
			values.put("dlv_time", dlv_time);
			values.put("dlv_bureau_org_code", bean.getDlv_bureau_org_code());
			values.put("dlv_pseg_code", bean.getDlv_pseg_code());
			values.put("dlv_sts_code", bean.getDlv_sts_code());
			values.put("dlv_staff_code", bean.getDlv_staff_code());
			values.put("dlv_staff_name", bean.getDlv_staff_name());
			values.put("undlv_cause_code", bean.getUndlv_cause_code());
			values.put("undlv_next_actn_code", bean.getUndlv_next_actn_code());
			values.put("signer_name", bean.getSigner_name());
			values.put("wl_term_num", bean.getWl_term_num());
			String actual_goods_fee = bean.getActual_goods_fee();
			if (actual_goods_fee == null || "0".equals(actual_goods_fee)
					|| "0.0".equals(actual_goods_fee)
					|| "".equals(actual_goods_fee)) {
				values.put("actual_goods_fee", "0");

			} else {
				values.put("actual_goods_fee", bean.getActual_goods_fee());
			}
			values.put("sign_sts_code", bean.getSign_sts_code());
			values.put("pay_mode", bean.getPay_mode());
			values.put("frequence", bean.getFrequence());
			values.put("picture", bean.getPicture());
			values.put("data_upload_timestamp", bean.getData_upload_timestamp());
			values.put("commit_result", bean.getCommit_result());
			db.insert(QUEUE_TABLE, null, values);
		}
		db.close();
	}

	public synchronized ArrayList<DeliverQueueBean> queryAll() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + QUEUE_TABLE + " where commit_result!="
				+ Constant.COMMIT;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	public synchronized List<DeliverQueueBean> query_id(int _id) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + QUEUE_TABLE + " where _id=" + _id;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 根据邮件号在queue表中查询 处理结果
	 * 
	 * @param mailNum
	 * @return
	 */
	public synchronized int queryByMailNumber(String mailNum) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select commit_result from " + QUEUE_TABLE
				+ " where mail_num='" + mailNum + "'";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			return cursor.getInt(cursor.getColumnIndex("commit_result"));
		}
		return Constant.UNCOMMIT;
	}

	/**
	 * 根据邮件号查询提交信息
	 * 
	 * @param mailNum
	 * @return
	 */
	public synchronized ArrayList<DeliverQueueBean> queryCommitInfoByMailNum(
			String mailNum) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + QUEUE_TABLE + " where mail_num='"
				+ mailNum + "' order by dlv_date,dlv_time";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 提交
	 * 
	 * @param idList
	 *            要提交的集合
	 */
	public synchronized void updateCommitResult(
			ArrayList<DeliverQueueBean> idList) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < idList.size(); i++) {
			String sql = "update " + QUEUE_TABLE + " set commit_result="
					+ Constant.COMMIT + " where _id=" + idList.get(i).get_id();
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 未提交
	 * 
	 * @param mail_num
	 */
	public synchronized void updateCommitResultByMail_num(String mail_num) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + QUEUE_TABLE + " set commit_result="
				+ Constant.UNCOMMIT + " where mail_num=" + mail_num;
		db.execSQL(sql);
		db.close();
	}

	public synchronized ArrayList<DeliverQueueBean> parseCursorToList(
			Cursor cursor) {
		ArrayList<DeliverQueueBean> list = new ArrayList<DeliverQueueBean>();
		while (cursor.moveToNext()) {

			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String mail_num = cursor.getString(cursor
					.getColumnIndex("mail_num"));
			String flag = cursor.getString(cursor.getColumnIndex("flag"));
			String dlv_date = cursor.getString(cursor
					.getColumnIndex("dlv_date"));
			String dlv_time = cursor.getString(cursor
					.getColumnIndex("dlv_time"));
			String dlv_bureau_org_code = cursor.getString(cursor
					.getColumnIndex("dlv_bureau_org_code"));
			String dlv_pseg_code = cursor.getString(cursor
					.getColumnIndex("dlv_pseg_code"));
			String dlv_sts_code = cursor.getString(cursor
					.getColumnIndex("dlv_sts_code"));
			String dlv_staff_code = cursor.getString(cursor
					.getColumnIndex("dlv_staff_code"));
			String dlv_staff_name = cursor.getString(cursor
					.getColumnIndex("dlv_staff_name"));
			String undlv_cause_code = cursor.getString(cursor
					.getColumnIndex("undlv_cause_code"));
			String undlv_next_actn_code = cursor.getString(cursor
					.getColumnIndex("undlv_next_actn_code"));
			String signer_name = cursor.getString(cursor
					.getColumnIndex("signer_name"));
			String wl_term_num = cursor.getString(cursor
					.getColumnIndex("wl_term_num"));
			String pay_mode = cursor.getString(cursor
					.getColumnIndex("pay_mode"));
			String data_upload_timestamp = cursor.getString(cursor
					.getColumnIndex("data_upload_timestamp"));
			String sign_sts_code = cursor.getString(cursor
					.getColumnIndex("sign_sts_code"));
			String actual_goods_fee = cursor.getString(cursor
					.getColumnIndex("actual_goods_fee"));
			String frequence = cursor.getString(cursor
					.getColumnIndex("frequence"));
			String picture = cursor.getString(cursor.getColumnIndex("picture"));
			String rcver_phone = cursor.getString(cursor
					.getColumnIndex("rcver_phone"));
			String sender_phone = cursor.getString(cursor
					.getColumnIndex("sender_phone"));
			int commit_result = cursor.getInt(cursor
					.getColumnIndex("commit_result"));
			DeliverQueueBean bean = new DeliverQueueBean(mail_num, dlv_date,
					dlv_time, dlv_bureau_org_code, dlv_pseg_code, dlv_sts_code,
					sign_sts_code, actual_goods_fee, dlv_staff_code,
					dlv_staff_name, undlv_cause_code, undlv_next_actn_code,
					signer_name, wl_term_num, pay_mode, flag,
					data_upload_timestamp, frequence, commit_result);
			bean.setOrgCode(cursor.getString(cursor.getColumnIndex("orgCode")));
			bean.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
			bean.setRealName(cursor.getString(cursor.getColumnIndex("realName")));
			bean.set_id(_id);
			bean.setPicture(picture);
			bean.setRcver_phone(rcver_phone);
			bean.setSender_phone(sender_phone);
			list.add(bean);

		}
		return list;
	}

	/**
	 * 根据处理结果查询数据
	 * 
	 * @param commit_result
	 *            妥投-Constant.TUOTOU; 未妥投-Constant.WEITUOTOU;
	 *            已提交：Constant.COMMIT
	 * @return
	 */
	public synchronized ArrayList<DeliverQueueBean> queryByCommitResult(
			int commit_result) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + QUEUE_TABLE + " where commit_result="
				+ commit_result;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 投递缴款
	 */
	public synchronized ArrayList<DeliverQueueBean> queryByCommitResult_money() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + QUEUE_TABLE + " where commit_result="
				+ Constant.COMMIT + " and dlv_sts_code='TT'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	public synchronized void deleteCommit() {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from " + QUEUE_TABLE + " where commit_result="
				+ Constant.COMMIT;
		db.execSQL(sql);
		db.close();
	}

	public synchronized void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from " + QUEUE_TABLE;
		db.execSQL(sql);
		db.close();
	}
}

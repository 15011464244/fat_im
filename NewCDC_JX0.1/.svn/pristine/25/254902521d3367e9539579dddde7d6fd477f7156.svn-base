package com.newcdc.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newcdc.db.helper.DeliverMailDaoHelper;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.Constant;

/**
 * @author zhangfan 2015-3-27,上午10:49:15
 * 
 */
public class DeliverMailDao extends DeliverMailDaoHelper {
	private Context context;

	public DeliverMailDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
		this.context = context;
	}

	/**
	 * 将邮件插入到下段邮件表
	 * 
	 * @param mail_num
	 * @param the_class_date
	 */
	public synchronized void inserMail(ArrayList<String> mails,
			String the_class_date, int frequence) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < mails.size(); i++) {
			String mail_num = mails.get(i);
			ContentValues values = new ContentValues();
			values.put("mail_num", mail_num);
			values.put("frequence", frequence);
			values.put("the_class_date", the_class_date);
			values.put("submitType", Constant.SUBMITTYPE_FEIQIANGZHI);// 提交方式-非强制
			values.put("mailType", Constant.MAILTYPE_NORMAL);// 邮件类型-普通
			values.put("msgDesc", "正在提交");// 处理结果描述,默认为正在提交
			values.put("result", Constant.RESULT_NORMAL);// 处理结果
			values.put("deliver_info", Constant.QUERY_DELIVER_ERR);// 下段查询结果
			db.insert(DELIVER_MAIL_TABLE, null, values);
			// 同时插入下段信息表
			// DeliverDao deliverDao = DeliverDaoFactory.getInstance()
			// .getDeliverDao(context);
			// MessageInfoBean bean = new MessageInfoBean();
			// bean.setMail_num(mail_num);
			// bean.setDealResult(Constant.DAICHULI);
			// bean.setThe_class_date(the_class_date);
			// deliverDao.insertDeliver(db, bean);
		}
		db.close();
	}

	/**
	 * 更改处理结果
	 * 
	 * @param msgDesc
	 * @param mail_num
	 */
	public synchronized void updateMsgDesc(String msgDesc,
			ArrayList<String> mails) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < mails.size(); i++) {
			String sql = "update " + DELIVER_MAIL_TABLE + " set msgDesc='"
					+ msgDesc + "' where mail_num='" + mails.get(i) + "'";
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 将邮件查询下段信息结果标记为成功
	 */
	public synchronized void setMailQueryOk(ArrayList<String> mails) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < mails.size(); i++) {
			String sql = "update " + DELIVER_MAIL_TABLE + " set deliver_info="
					+ Constant.QUERY_DELIVER_OK + " where mail_num='"
					+ mails.get(i) + "'";
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 剔除异常邮件集合
	 */
	public synchronized void deleteErrMails(ArrayList<String> mails) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < mails.size(); i++) {
			String sql = "delete from " + DELIVER_MAIL_TABLE
					+ " where mail_num='" + mails.get(i) + "'";
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 剔除异常邮件
	 */
	public synchronized void deleteErrMail(String mail_num) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from " + DELIVER_MAIL_TABLE + " where mail_num='"
				+ mail_num + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 更改为强制提交
	 * 
	 * @param mail_num
	 */
	public synchronized void updateSubmitTypeToQiangZhi(String mail_num) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + DELIVER_MAIL_TABLE + " set submitType="
				+ Constant.SUBMITTYPE_QIANGZHI + " where mail_num='" + mail_num
				+ "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 更改邮件下段结果
	 * 
	 * @param result
	 *            成功：Constant.RESULT_SUCC,失败且不可上传Constant.RESULT_ERR,
	 *            可强制上传：Constant.RESULT_QIANGZHI
	 */
	public synchronized void updateResult(int result, ArrayList<String> mails) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < mails.size(); i++) {
			String sql = "update " + DELIVER_MAIL_TABLE + " set result="
					+ result + " where mail_num='" + mails.get(i) + "'";
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 更改邮件提交类型
	 * 
	 * @param commitType
	 * @param mail_num
	 */
	public synchronized void updateCommitType(int commitType, String mail_num) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + DELIVER_MAIL_TABLE + " set commitType="
				+ commitType + " where mail_num='" + mail_num + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 查询要提交下段的邮件
	 * 
	 */
	public ArrayList<HashMap<String, String>> findWillCommitMail() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_MAIL_TABLE + " where result!="
				+ Constant.RESULT_SUCC + " order by result";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursor(cursor);
	}

	/**
	 * 查询要查询下段信息的邮件
	 * 
	 */
	public ArrayList<HashMap<String, String>> findWillQueryMail() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_MAIL_TABLE + " where result="
				+ Constant.RESULT_SUCC + " and deliver_info="
				+ Constant.QUERY_DELIVER_ERR;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursor(cursor);
	}

	/**
	 * 根据邮件号查询邮件
	 */
	public HashMap<String, String> findMailByMailNumber(String mail_num) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_MAIL_TABLE
				+ " where mail_num='" + mail_num + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursor(cursor).get(0);
	}

	/**
	 * 查询所有异常邮件：包括result=0和result=2的邮件
	 */
	public ArrayList<HashMap<String, String>> findErrMail() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_MAIL_TABLE + " where result="
				+ Constant.RESULT_ERR + " or result="
				+ Constant.RESULT_QIANGZHI;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursor(cursor);
	}

	/**
	 * 查询下段成功的邮件
	 * 
	 */
	public ArrayList<HashMap<String, String>> findCommitedMail() {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "select * from " + DELIVER_MAIL_TABLE + " where result="
				+ Constant.RESULT_SUCC;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursor(cursor);
	}

	public ArrayList<HashMap<String, String>> parseCursor(Cursor cursor) {
		ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String, String> mail = new HashMap<String, String>();
			mail.put("mail_num",
					cursor.getString(cursor.getColumnIndex("mail_num")));
			mail.put("the_class_date",
					cursor.getString(cursor.getColumnIndex("the_class_date")));
			mail.put("frequence",
					cursor.getString(cursor.getColumnIndex("frequence")));
			mail.put("submitType",
					cursor.getString(cursor.getColumnIndex("submitType")));
			mail.put("mailType",
					cursor.getString(cursor.getColumnIndex("mailType")));
			mail.put("msgDesc",
					cursor.getString(cursor.getColumnIndex("msgDesc")));
			mail.put("result",
					cursor.getString(cursor.getColumnIndex("result")));
			mapList.add(mail);
		}
		cursor.close();
		return mapList;
	}

	/**
	 * 邮件是否存在
	 * 
	 * @return 存在返回true
	 */
	public boolean mailExits(String mail_num) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DELIVER_MAIL_TABLE
				+ " where mail_num='" + mail_num + "'", null);
		if (cursor.getCount() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 删除下段邮件表中所有数据
	 */
	public synchronized void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from " + DELIVER_MAIL_TABLE);
		db.close();
	}
}

package com.newcdc.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newcdc.db.helper.PushDaoHelper;

/**
 * 存储用于推送的交班时间
 * 
 * @author zhangfan
 * 
 */
public class PushDao extends PushDaoHelper {

	public PushDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	// /**
	// * 创建当日推送消息队列
	// */
	// @SuppressLint("SimpleDateFormat")
	// public void createTodayPushQueue(String the_class_date, int messageType)
	// {
	// Log.e("表-创建今日队列", "准备创建");
	// SQLiteDatabase database = getWritableDatabase();
	// ContentValues values = new ContentValues();
	// values.put("messageType", messageType);
	// values.put("the_class_date", the_class_date);
	// database.insert( PUSHTABLE, null, values);
	// database.close();
	// Log.e("表-创建今日队列", "创建成功");
	// }
	/**
	 * 创建当日推送消息队列
	 */
	@SuppressLint("SimpleDateFormat")
	public synchronized void insert(String the_class_date, int messageType) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "select * from " + PUSHTABLE + " where messageType="
				+ messageType;
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.getCount() != 1) {
			ContentValues values = new ContentValues();
			values.put("messageType", messageType);
			values.put("the_class_date", the_class_date);
			database.insert(PUSHTABLE, null, values);
		}
		cursor.close();
		database.close();
	}

	// /**
	// * 更新最近推送时间
	// *
	// * @param new_class_date
	// * 新的时间
	// * @param messageType
	// * 推送消息类型
	// * @return
	// */
	// @SuppressLint("SimpleDateFormat")
	// public boolean update_the_class_date(String new_class_date, int
	// messageType) {
	// SQLiteDatabase database = getWritableDatabase();
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// String the_system_date = sdf.format(new Date());// 系统当前日期
	// Cursor c = database.rawQuery("select * from "
	// + PUSHTABLE + " where today_date='"
	// + the_system_date + "' and messageType=" + messageType, null);//
	// 查出当日对应的推送消息类型对应的条目
	// while (c.moveToNext()) {
	// int _id = c.getInt(c.getColumnIndex("_id"));// 查到条目的_id
	// c.close();
	// String sql = "update " + PUSHTABLE
	// + " set the_class_date='" + new_class_date + "' where _id="
	// + _id;
	// try {
	// database.execSQL(sql);
	// Log.e("表-更新时间戳", "更新成功");
	// return true;
	// } catch (Exception e) {
	// Log.e("表-更新时间戳", "更新失败");
	// } finally {
	// database.close();
	// }
	// }
	// return false;
	// }

	public synchronized void update_class_date(int the_class_date) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + PUSHTABLE + " set the_class_date='"
				+ the_class_date + "'";
		database.execSQL(sql);
		database.close();
	}

	public String query_class_date(int messageType) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + PUSHTABLE + " where messageType="
				+ messageType;
		Cursor cursor = database.rawQuery(sql, null);
		String the_class_date = null;
		while (cursor.moveToNext()) {
			the_class_date = cursor.getString(cursor
					.getColumnIndex("the_class_date"));
		}
		cursor.close();
		return the_class_date;
	}

	// /**
	// * 根据推送消息类型获取当次的推送时间the_class_date字段
	// *
	// * @param messageType
	// * @return
	// */
	// @SuppressLint("SimpleDateFormat")
	// public String query_the_class_date(int messageType) {
	// SQLiteDatabase database = getReadableDatabase();
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// String the_system_date = sdf.format(new Date());// 系统当前日期
	// Cursor c = database.rawQuery("select * from "
	// + PUSHTABLE + " where today_date='"
	// + the_system_date + "' and messageType=" + messageType, null);//
	// 查出当日对应的推送消息类型对应的条目
	// String the_class_date = null;
	// while (c.moveToNext()) {
	// the_class_date = c.getString(c.getColumnIndex("the_class_date"));
	// }
	// c.close();
	// return the_class_date;
	// }

	public synchronized void deletepush() {
		SQLiteDatabase database = getWritableDatabase();
		database.delete(PUSHTABLE, null, null);
		database.close();
	}
}

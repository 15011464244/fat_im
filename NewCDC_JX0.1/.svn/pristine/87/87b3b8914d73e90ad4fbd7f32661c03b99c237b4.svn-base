package com.newcdc.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newcdc.db.helper.DeliverTaskListDaoHelper;

/**
 * @author hanrong
 * @version 创建时间：2015-4-9 下午4:28:44 类说明 投递任务dao
 */
public class DeliverTaskListDao extends DeliverTaskListDaoHelper {

	public DeliverTaskListDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	public synchronized boolean saveGatherTaskMessage(
			ArrayList<Map<String, String>> params, String Orgcode,
			String Username) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				Map<String, String> maps = params.get(i);
				values = new ContentValues();
				values.put("createtime", new Date().getTime());
				values.put("delvorgcode", Orgcode);
				values.put("username", Username);
				values.put(
						"delivertasknum",
						maps.get("delivertasknum") != null ? maps.get(
								"delivertasknum").toString() : "");
				values.put(
						"delivertaskname",
						maps.get("delivertaskname") != null ? maps.get(
								"delivertaskname").toString() : "");
				values.put(
						"delivertaskcode",
						maps.get("delivertaskcode") != null ? maps.get(
								"delivertaskcode").toString() : "");
				values.put(
						"delivertaskcount",
						maps.get("delivertaskcount") != null ? maps.get(
								"delivertaskcount").toString() : "0");
				values.put(
						"delivertaskallcount",
						maps.get("delivertaskallcount") != null ? maps.get(
								"delivertaskallcount").toString() : "0");
				values.put(
						"delivertaskstatus",
						maps.get("delivertaskstatus") != null ? maps.get(
								"delivertaskstatus").toString() : "");
				values.put(
						"delivertasktime",
						maps.get("delivertasktime") != null ? maps.get(
								"delivertasktime").toString() : "");

				db.insert(DELIVTERTASKLIST_TABLE, null, values);
			}
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}

	}

	/**
	 * 查询投递任务列表
	 */
	public ArrayList<Map<String, String>> queryGatherTaskMessage(
			String delvorgcode, String username) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVTERTASKLIST_TABLE
				+ " where delvorgcode='" + delvorgcode + "' and username='"
				+ username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 查询所有的频次集合
	 * 
	 * @param orgCode
	 * @param userName
	 */
	public ArrayList<String> queryFrequences(String orgCode, String userName) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select delivertasknum from " + DELIVTERTASKLIST_TABLE;
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String frequence = cursor.getString(cursor
					.getColumnIndex("delivertasknum"));
			list.add(frequence);
		}
		return list;
	}

	/**
	 * 将tasklist转化成mapList
	 */
	public ArrayList<Map<String, String>> parseCursorToList(Cursor cursor) {
		ArrayList<Map<String, String>> params = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("", cursor.getInt(cursor.getColumnIndex("_id")) + "");
			map.put("createtime",
					cursor.getString(cursor.getColumnIndex("createtime")));
			map.put("delvorgcode",
					cursor.getString(cursor.getColumnIndex("delvorgcode")));
			map.put("username",
					cursor.getString(cursor.getColumnIndex("username")));
			map.put("delivertasknum",
					cursor.getString(cursor.getColumnIndex("delivertasknum")));
			map.put("delivertaskname",
					cursor.getString(cursor.getColumnIndex("delivertaskname")));
			map.put("delivertaskcode",
					cursor.getString(cursor.getColumnIndex("delivertaskcode")));
			map.put("delivertaskcount",
					cursor.getString(cursor.getColumnIndex("delivertaskcount")));
			map.put("delivertaskallcount", cursor.getString(cursor
					.getColumnIndex("delivertaskallcount")));
			map.put("delivertaskstatus", cursor.getString(cursor
					.getColumnIndex("delivertaskstatus")));
			map.put("delivertasktime",
					cursor.getString(cursor.getColumnIndex("delivertasktime")));

			params.add(map);
		}
		cursor.close();
		return params;
	}

	/**
	 * 更新任务数量至0
	 */
	public synchronized void updateCountTo0(String Orgcode, String username) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + DELIVTERTASKLIST_TABLE
				+ " set delivertaskallcount=0" + " where delvorgcode='"
				+ Orgcode + "' and username='" + username + "'";
		database.execSQL(sql);
	}

	/**
	 * 修改任务数 key :delivertaskcount values:-- 条数 key :delivertasknum values:-- 频次
	 */
	public synchronized void updateDeliverTaskCount(String Orgcode,
			String Username, ArrayList<HashMap<String, String>> listCount) {
		SQLiteDatabase database = getWritableDatabase();
		int countTask = listCount.size();
		if (countTask > 0) {
			for (int i = 0; i < countTask; i++) {
				String sql = "update " + DELIVTERTASKLIST_TABLE
						+ " set delivertaskcount= '"
						+ listCount.get(i).get("delivertaskcount")
						+ "' where delvorgcode='" + Orgcode
						+ "' and username='" + Username
						+ "' and delivertasknum='"
						+ listCount.get(i).get("delivertasknum") + "'";
				database.execSQL(sql);
			}
			database.close();
		}

	}

	public synchronized void updateDeliverOneTaskCount(String Orgcode,
			String username, String delivertaskcount, String delivertasknum) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + DELIVTERTASKLIST_TABLE
				+ " set delivertaskcount= '" + delivertaskcount
				+ "' where delvorgcode='" + Orgcode + "' and username='"
				+ username + "' and delivertasknum='" + delivertasknum + "'";
		database.execSQL(sql);
		database.close();

	}

	public synchronized void updateDeliverMoreTaskCount(String Orgcode,
			String username, String delivertaskallcount, String delivertasknum) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + DELIVTERTASKLIST_TABLE
				+ " set delivertaskallcount= '" + delivertaskallcount
				+ "' where delvorgcode='" + Orgcode + "' and username='"
				+ username + "' and delivertasknum='" + delivertasknum + "'";
		database.execSQL(sql);
		database.close();

	}

	/**
	 * 更新任务总数 key :delivertaskallcount values:-- 条数 key :delivertasknum
	 * values:-- 频次
	 */
	public synchronized void updateDeliverTaskAllCount(String Orgcode,
			String Username, ArrayList<HashMap<String, String>> listCount) {
		SQLiteDatabase database = getWritableDatabase();
		int countTask = listCount.size();
		if (countTask > 0) {
			for (int i = 0; i < countTask; i++) {
				String sql = "update " + DELIVTERTASKLIST_TABLE
						+ " set delivertaskallcount= '"
						+ listCount.get(i).get("delivertaskcount")
						+ "' where delvorgcode='" + Orgcode
						+ "' and username='" + Username
						+ "' and delivertasknum='"
						+ listCount.get(i).get("delivertasknum") + "'";
				database.execSQL(sql);
			}
			database.close();
		}

	}

	/**
	 * 删除频次任务数据
	 */
	public synchronized void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(DELIVTERTASKLIST_TABLE, null, null);
		db.close();
	}
}

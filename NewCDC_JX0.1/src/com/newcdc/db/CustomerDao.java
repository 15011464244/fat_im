package com.newcdc.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newcdc.db.helper.CustomerDaoHelper;
import com.newcdc.model.CustomerBean;

/**
 * 
 * 大客户表操作
 * 
 * @author quanyi
 * 
 */
public class CustomerDao extends CustomerDaoHelper {

	public CustomerDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	// TODO 存储
	public synchronized boolean savedate(List<CustomerBean> insertData) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (insertData != null && insertData.size() > 0) {
//			db.delete(TB_CUSTOMER, null, null);
			for (CustomerBean params : insertData) {
				values = new ContentValues();
				values.put("KHDM", params.getKHDM());
				values.put("KHCDM", params.getKHCDM());
				values.put("KHMC", params.getKHMC());
				values.put("KHJC", params.getKHJC());
				values.put("KHJM", params.getKHJM());
				values.put("PIJM", params.getPIJM());
				db.insert(TB_CUSTOMER, null, values);
			}
			return true;
		}
		return false;
	}

	public synchronized String FindDaCode(String str) {
		String dataList = "";

		Cursor cursor = null;
		String[] colums = { "KHDM" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TB_CUSTOMER, colums, " KHMC=?",
					new String[] { str }, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					dataList = cursor.getString(0);
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized String FindDaName(String str) {
		String dataList = "";

		Cursor cursor = null;
		String[] colums = { "KHMC" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TB_CUSTOMER, colums, " KHDM=?",
					new String[] { str }, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					dataList = cursor.getString(0);
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized List<Map<String, String>> FindData(String str) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "KHMC", "KHCDM", "KHDM", "KHJC", "KHJM", "PIJM" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TB_CUSTOMER, colums, " KHDM" + " like '%" + str + "%'"
					+ " or (KHMC" + "  like '%" + str + "%')" 
					+ " or (KHJM" + "  like '%" + str + "%')" 
					+ " or (PIJM" + "  like '%" + str + "%')", null, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("KHMC", cursor.getString(0));
					paramsMap.put("KHCDM", cursor.getString(1));
					paramsMap.put("KHDM", cursor.getString(2));
					paramsMap.put("KHJC", cursor.getString(3));
					paramsMap.put("KHJM", cursor.getString(4));
					paramsMap.put("PIJM", cursor.getString(5));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized List<Map<String, String>> FindData2() {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "KHMC", "KHCDM", "KHDM", "KHJC", "KHJM", "PIJM" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db
					.query(TB_CUSTOMER, colums, null, null, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("KHMC", cursor.getString(0));
					paramsMap.put("KHCDM", cursor.getString(1));
					paramsMap.put("KHDM", cursor.getString(2));
					paramsMap.put("KHJC", cursor.getString(3));
					paramsMap.put("KHJM", cursor.getString(4));
					paramsMap.put("PIJM", cursor.getString(5));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized void DeleteData() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TB_CUSTOMER, null, null);
		} catch (Exception e) {
		}
	}
}

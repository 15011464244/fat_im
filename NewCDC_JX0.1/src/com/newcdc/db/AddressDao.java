package com.newcdc.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newcdc.db.helper.AddressDaoHelper;

/**
 * @author hanrong
 * @version 创建时间：2014-12-24 下午2:24:11 类说明
 */
public class AddressDao extends AddressDaoHelper {
	/**
	 * @param context
	 */
	public AddressDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	/**
	 * 查询通讯录
	 */
	public synchronized List<Map<String, Object>> Findtelephone(
			String username, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		SQLiteDatabase db =  getReadableDatabase();
		Cursor cursor = null;
		try {
			String[] colums = { "telephonenum", "bodyname", "mainid", "address" };
			cursor = db.query( ADDRESSBOOKTAB, colums,
					"username = ? and orgcode=?", new String[] { username,
							orgcode }, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("telephonenum", cursor.getString(0));
					paramsMap.put("bodyname", cursor.getString(1));
					paramsMap.put("mainid", cursor.getString(2));
					paramsMap.put("address", cursor.getString(3));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			cursor.close();
		}
		return dataList;
	}

	/**
	 * 模糊查询通讯录
	 */
	public List<Map<String, Object>> querytelreason(String reason) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		SQLiteDatabase database =  getReadableDatabase();
		String sql = "select * from " +  ADDRESSBOOKTAB
				+ " where telephonenum like '%" + reason
				+ "%' or bodyname like '%" + reason + "%' or address like '%"
				+ reason + "%'";
		Cursor cursor = database.rawQuery(sql, null);
		try {
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("telephonenum", cursor.getString(cursor
							.getColumnIndex("telephonenum")));
					paramsMap
							.put("bodyname", cursor.getString(cursor
									.getColumnIndex("bodyname")));
					paramsMap.put("mainid",
							cursor.getString(cursor.getColumnIndex("mainid")));
					paramsMap.put("address",
							cursor.getString(cursor.getColumnIndex("address")));
					dataList.add(paramsMap);
				}
			}
			return dataList;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 插入一条电话到通讯录
	 * 
	 * @param telephone
	 */
	public synchronized void inserttelephone(String telephone, String bodyname,
			String username, String orgcode, String mainid, String address) {
		SQLiteDatabase database =  getWritableDatabase();
		ContentValues values = new ContentValues();
		if (telephone != null && !"null".equals(telephone)) {
			values.put("telephonenum", telephone);
		} else {
			values.put("telephonenum", "");
		}
		if (mainid != null && !"null".equals(mainid)) {
			values.put("mainid", mainid);
		} else {
			values.put("mainid", "");
		}
		if (orgcode != null && !"null".equals(orgcode)) {
			values.put("orgcode", orgcode);
		} else {
			values.put("orgcode", "");
		}
		if (username != null && !"null".equals(username)) {
			values.put("username", username);
		} else {
			values.put("username", "");
		}
		if (bodyname != null && !"null".equals(bodyname)) {
			values.put("bodyname", bodyname);
		} else {
			values.put("bodyname", "");
		}
		if (bodyname != null && !"null".equals(bodyname)) {
			values.put("bodyname", bodyname);
		} else {
			values.put("bodyname", "");
		}
		if (address != null && !"null".equals(address)) {
			values.put("address", address);
		} else {
			values.put("address", "");
		}
		@SuppressWarnings("unused")
		long ccd = database.insert("ADDRESSBOOKTAB", null, values);
		database.close();
	}

	// 删除
	public synchronized int deletetelephone(String mainid) {
		SQLiteDatabase db =  getReadableDatabase();
		int a = 0;
		if (mainid != null) {
			a = db.delete( ADDRESSBOOKTAB, "mainid=?",
					new String[] { mainid });
		}
		return a;
	}

	/**
	 * 删除全部
	 * 
	 * @param tabName
	 */
	public synchronized void deleteAlltelphone() {
		SQLiteDatabase database =  getWritableDatabase();
		database.delete( ADDRESSBOOKTAB, null, null);
		database.close();
	}

	// 更新 通讯录
	public synchronized void updatetelephone(String name, String tel,
			String addr, String mainid) {
		SQLiteDatabase db =  getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("telephonenum", tel);
		contentValues.put("bodyname", name);
		contentValues.put("address", addr);
		long count = db.update( ADDRESSBOOKTAB,
				contentValues, "mainid = ?", new String[] { mainid });
		Log.e("connt", count + "");
	}

	// 修改通讯录地址
	public synchronized void updateaddress(String tel, String address) {
		SQLiteDatabase db =  getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("address", address);
		long count = db.update( ADDRESSBOOKTAB,
				contentValues, "telephonenum = ?", new String[] { tel });
		Log.e("connt", count + "");
	}

	/**
	 * 查询通讯录
	 */
	public synchronized List<Map<String, Object>> findistele(String tele) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		try {
			String[] colums = { "telephonenum", "bodyname", "mainid", "address" };
			cursor = db
.query(ADDRESSBOOKTAB, colums,
							"telephonenum = ?", new String[] { tele }, null,
							null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("telephonenum", cursor.getString(0));
					paramsMap.put("bodyname", cursor.getString(1));
					paramsMap.put("mainid", cursor.getString(2));
					paramsMap.put("address", cursor.getString(3));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			cursor.close();
		}
		return dataList;
	}

}

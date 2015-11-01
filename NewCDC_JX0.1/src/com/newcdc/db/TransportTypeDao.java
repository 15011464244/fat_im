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

import com.newcdc.activity.clcttask.DataDown.TransportTypeBean;
import com.newcdc.db.helper.TransportTypeDaoHelper;

/**
 * 运输方式数据操作
 * 
 * @author quanyi
 */
public class TransportTypeDao extends TransportTypeDaoHelper {

	public TransportTypeDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	// TODO 存储
	public synchronized boolean saveTransportTypeInfo(
			List<TransportTypeBean> insertData) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (insertData != null && insertData.size() > 0) {
			db.delete(TB_TRANSPORT, null, null);
			for (TransportTypeBean params : insertData) {
				values = new ContentValues();
				values.put("TransferCode", params.getTransferCode());
				values.put("TransferName", params.getTransferName());
				values.put("OldMailType", params.getOldMailType());
				values.put("ChangeMailType", params.getChangeMailType());
				db.insert(TB_TRANSPORT, null, values);
			}
			return true;
		}
		return false;
	}

	/**
	 * 查询运输方式代码
	 */
	public String queryTransferCode(String TransferName) {
		SQLiteDatabase db = getReadableDatabase();
		String code = "";
		String sql = "select TransferCode from " + TB_TRANSPORT
				+ " where TransferName='" + TransferName + "'";
		Cursor cursor = db.rawQuery(sql, null);
		try {

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					code = cursor.getString(0);
				}
			}
		} catch (Exception e) {
			Log.e("FindData", e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return code;
	}

	public List<Map<String, String>> FindTransportTypeInfo(String orgcode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;

		String[] colums = { "TransferCode", "TransferName", "OldMailType",
				"ChangeMailType" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TB_TRANSPORT, colums, null, null, null, null,
					null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("TransferCode", cursor.getString(0));
					paramsMap.put("TransferName", cursor.getString(1));
					paramsMap.put("OldMailType", cursor.getString(2));
					paramsMap.put("ChangeMailType", cursor.getString(3));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public String trimString(String str) {
		if (str != null) {
			return str.trim();
		}
		return null;
	}
	public synchronized void DeleteData() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TB_TRANSPORT, null, null);
		} catch (Exception e) {
		}
	}

}

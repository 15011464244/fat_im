package com.cn.cdc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class FollowActionDao extends FollowActionDaoHelper {
	public FollowActionDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	public FollowActionDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

//	public List<Map<String, String>> FindFollowActionByStandardCode(
//			String followAction, String orgcode) {
//		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//		Map<String, String> paramsMap = null;
//		Cursor cursor = null;
//		String[] colums = { "followAction", "actionDescCHS", "actionDescENG",
//				"actionDescTRADITIONAL" };
//		try {
//			SQLiteDatabase db = getReadableDatabase();
//			cursor = db.query(TABLE_NAME, colums,
//					"followAction=? and orgcode='" + orgcode + "'",
//					new String[] { followAction }, null, null, null);
//			if (cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					paramsMap = new LinkedHashMap<String, String>();
//					paramsMap.put("followAction", cursor.getString(0));
//					paramsMap.put("actionDescCHS", cursor.getString(1));
//					paramsMap.put("actionDescENG", cursor.getString(2));
//					paramsMap.put("actionDescTRADITIONAL", cursor.getString(3));
//					dataList.add(paramsMap);
//				}
//			}
//		} catch (Exception e) {
//		} finally {
//			closeCursor(cursor);
//		}
//		return dataList;
//	}

	public List<Map<String, String>> FindFollowAction(String orgcode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.rawQuery("select * from " + TABLE_NAME, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("followAction", cursor.getString(0));
					paramsMap.put("actionDescCHS", cursor.getString(1));
					paramsMap.put("actionDescENG", cursor.getString(2));
					paramsMap.put("actionDescTRADITIONAL", cursor.getString(3));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public String FindFollowAction(String str, String orgcode) {
		String dataList = "";
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.rawQuery("select followAction from " + TABLE_NAME
					+ " where actionDescCHS = '" + str + "'", null);
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

	public void SaveFollowAction(List<FollowAction> dataList, String orgcode)
			throws JSONException {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (dataList != null) {
			db.delete(TABLE_NAME, null, null);
			for (FollowAction params : dataList) {
				values = new ContentValues();
				values.put("followAction", params.getFollowAction());
				values.put("actionDescCHS", params.getActionDescCHS());
				values.put("orgcode", orgcode);
				db.insert(TABLE_NAME, null, values);
			}
		}
		db.close();
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

}

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

public class StateFollowDao extends StateFollowDaoHelper {

	public StateFollowDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	public StateFollowDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

//	public List<Map<String, String>> FindStateFllowBystateType(
//			String standardCode, String orgcode) {
//		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//		Map<String, String> paramsMap = null;
//		Cursor cursor = null;
//		String[] colums = { "standardCode", "followAction" };
//		try {
//			SQLiteDatabase db = getReadableDatabase();
//			cursor = db.query(TABLE_NAME, colums,
//					"standardCode=? and orgcode = '" + orgcode + "'",
//					new String[] { standardCode }, null, null, null);
//			if (cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					paramsMap = new LinkedHashMap<String, String>();
//					paramsMap.put("standardCode", cursor.getString(0));
//					paramsMap.put("followAction", cursor.getString(1));
//
//					dataList.add(paramsMap);
//				}
//			}
//		} catch (Exception e) {
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//		return dataList;
//	}

//	public List<Map<String, String>> FindStateFllowBystateType(String orgcode) {
//		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//		Map<String, String> paramsMap = null;
//		Cursor cursor = null;
//		try {
//			SQLiteDatabase db = getReadableDatabase();
//			String sql="select * from STATE_FOLLOW";
//			cursor=db.rawQuery(sql,null);
//			if (cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					paramsMap = new LinkedHashMap<String, String>();
//					paramsMap.put("standardCode", cursor.getString(0));
//					paramsMap.put("followAction", cursor.getString(1));
//					dataList.add(paramsMap);
//				}
//			}
//		} catch (Exception e) {
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//		return dataList;
//	}
//
//	public boolean SaveStateFollow(List<StandardCode> dataList, String orgcode)
//			throws JSONException {
//		SQLiteDatabase db = getWritableDatabase();
//		ContentValues values = null;
//		long cc = 0;
//		if (dataList != null) {
//			db.delete(TABLE_NAME, null, null);
//			for (StandardCode params : dataList) {
//				values = new ContentValues();
//				values.put("standardCode", params.getStandardCode());
//				values.put("followAction", params.getFollowAction());
//				values.put("orgcode", orgcode);
//				cc = db.insert(TABLE_NAME, null, values);
//			}
//		}
//		db.close();
//		if (cc > 0) {
//			return true;
//		}
//		return false;
//	}
//
//	public List<Map<String, String>> FindStateFllowActionBystateType(
//			String standardCode, String orgcode) {
//		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//		Map<String, String> paramsMap = null;
//		Cursor cursor = null;
//		// standardCode":"10", //δ��Ͷԭ�����
//		// "followAction ":"A" //��һ���������
//		try {
//			String sql = " select sf.standardCode,sf.followAction,pf.actionDescCHS ,pf.actionDescENG,pf.actionDescTRADITIONAL from STATE_FOLLOW  sf  left join FOLLOW_ACTION pf on sf.followAction=pf.followAction  where sf.standardCode='"
//					+ standardCode + "'";
//			SQLiteDatabase db = getReadableDatabase();
//			cursor = db.rawQuery(sql, null);
//			if (cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					paramsMap = new LinkedHashMap<String, String>();
//					paramsMap.put("standardCode", cursor.getString(0));
//					paramsMap.put("followAction", cursor.getString(1));
//					paramsMap.put("actionDescCHS", cursor.getString(2));
//					paramsMap.put("actionDescENG", cursor.getString(3));
//					paramsMap.put("actionDescTRADITIONAL", cursor.getString(4));
//
//					dataList.add(paramsMap);
//				}
//			}
//		} catch (Exception e) {
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//		return dataList;
//	}

}

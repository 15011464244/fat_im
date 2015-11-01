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
import com.newcdc.tools.Utils;

public class DlvStateDao extends DlvStateDaoHelper {

	public DlvStateDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}
//
	public DlvStateDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

//	public List<Map<String, String>> FindDlvStateBystateType(String stateType) {
//		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//		Map<String, String> paramsMap = null;
//		Cursor cursor = null;
//		String[] colums = { "pdaCode", "stateCode", "stateType",
//				"stateDescCHS", "followAction", "stateDescENG",
//				"stateDescTRADITIONAL" };
//		try {
//			SQLiteDatabase db = getReadableDatabase();
//			cursor = db.query(TABLE_NAME, colums, "stateType=?",
//					new String[] { stateType }, null, null, null);
//			if (cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					paramsMap = new LinkedHashMap<String, String>();
//					paramsMap.put("pdaCode", cursor.getString(0));
//					paramsMap.put("stateCode", cursor.getString(1));
//					paramsMap.put("stateType", cursor.getString(2));
//					paramsMap.put("stateDescCHS", cursor.getString(3));
//					paramsMap.put("followAction", cursor.getString(4));
//					paramsMap.put("stateDescENG", cursor.getString(5));
//					paramsMap.put("stateDescTRADITIONAL", cursor.getString(6));
//
//					dataList.add(paramsMap);
//				}
//			}
//		} catch (Exception e) {
//
//		} finally {
//			closeCursor(cursor);
//		}
//		return dataList;
//	}
//
//	public List<Map<String, String>> FindDlvStateBystateType() {
//		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//		Map<String, String> paramsMap = null;
//		Cursor cursor = null;
//		try {
//			SQLiteDatabase db = getReadableDatabase();
//			String sql = "select * from DLV_STATE";
//			cursor = db.rawQuery(sql, null);
//			if (cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					paramsMap = new LinkedHashMap<String, String>();
//					paramsMap.put("pdaCode", cursor.getString(0));
//					paramsMap.put("stateCode", cursor.getString(1));
//					paramsMap.put("stateType", cursor.getString(2));
//					paramsMap.put("stateDescCHS", cursor.getString(3));
//					paramsMap.put("followAction", cursor.getString(4));
//					paramsMap.put("stateDescENG", cursor.getString(5));
//					paramsMap.put("stateDescTRADITIONAL", cursor.getString(6));
//					dataList.add(paramsMap);
//				}
//			}
//		} catch (Exception e) {
//		} finally {
//			closeCursor(cursor);
//		}
//		return dataList;
//	}
//
//	public List<Map<String, String>> FindDlvStateBystateType(String stateType,
//			String str, String str1, String orgcode) {
//		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//		Map<String, String> paramsMap = null;
//		Cursor cursor = null;
//		String[] colums = { "pdaCode", "stateCode", "stateType",
//				"stateDescCHS", "followAction", "stateDescENG",
//				"stateDescTRADITIONAL" };
//		try {
//			SQLiteDatabase db = getReadableDatabase();
//			cursor = db
//					.query(TABLE_NAME,
//							colums,
//							"stateType=? and orgcode=? and  stateDescCHS=? or stateDescCHS=?",
//							new String[] { stateType, orgcode, str, str1 },
//							null, null, null);
//			if (cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					paramsMap = new LinkedHashMap<String, String>();
//					paramsMap.put("pdaCode", cursor.getString(0));
//					paramsMap.put("stateCode", cursor.getString(1));
//					paramsMap.put("stateType", cursor.getString(2));
//					paramsMap.put("stateDescCHS", cursor.getString(3));
//					paramsMap.put("followAction", cursor.getString(4));
//					paramsMap.put("stateDescENG", cursor.getString(5));
//					paramsMap.put("stateDescTRADITIONAL", cursor.getString(6));
//
//					dataList.add(paramsMap);
//				}
//			}
//		} catch (Exception e) {
//
//		} finally {
//			closeCursor(cursor);
//		}
//		return dataList;
//	}
//
//	public boolean SaveDlvState(List<Dlv_state> dataList, String orgcode)
//			throws JSONException {
//
//		SQLiteDatabase db = getWritableDatabase();
//		ContentValues values = null;
//		long cc = 0;
//		StringBuffer sb = new StringBuffer();
//		if (dataList != null) {
//			db.delete(TABLE_NAME, null, null);
//			for (Dlv_state params : dataList) {
//				values = new ContentValues();
//				values.put("pdaCode", params.getPdaCode());
//				values.put("stateCode", params.getStateCode());
//				values.put("stateType", params.getStateType());
//				values.put("stateDescCHS", params.getStateDescCHS());
//				values.put("followAction", params.getFollowAction());
//				values.put("orgcode", orgcode);
//				cc = db.insert(TABLE_NAME, null, values);
//				sb.append("insert into "
//						+ TABLE_NAME
//						+ "(stateType, stateCode, pdaCode, stateDescCHS, followAction) values "
//						+ "('" + params.getStateType() + "','"
//						+ params.getStateCode() + "','" + params.getPdaCode()
//						+ "','" + params.getStateDescCHS() + "','"
//						+ params.getFollowAction() + "');");
//			}
//			Utils.createFileFromString(sb.toString(), "dlv_state.txt");
//		}
//		if (cc > 0) {
//			return true;
//		}
//		db.close();
//		return false;
//	}
//
//	public String trimString(String str) {
//		if (str != null) {
//			return str.trim();
//		}
//		return null;
//	}
//
//	protected void closeCursor(Cursor cursor) {
//		if (cursor != null) {
//			cursor.close();
//		}
//	}

}

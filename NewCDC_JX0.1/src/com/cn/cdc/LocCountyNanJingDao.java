package com.cn.cdc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * 市区县集散属性数据操作类
 * 
 * @author zhaojie
 */
public class LocCountyNanJingDao extends LocCountyNanJingHelper {

	public LocCountyNanJingDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	public LocCountyNanJingDao(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public synchronized void saveLocCountyNanJing(
			List<Map<String, Object>> insertData, String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (insertData != null && insertData.size() > 0) {
			db.delete(TABLE_NAME, null, null);
			for (Map<String, Object> params : insertData) {
				values = new ContentValues();
				values.put(
						"COUNTY_CODE",
						params.get("COUNTY_CODE") != null ? params.get(
								"COUNTY_CODE").toString() : "");
				values.put("CN_NAME", params.get("CN_NAME") != null ? params
						.get("CN_NAME").toString() : "");
				values.put("EN_NAME", params.get("EN_NAME") != null ? params
						.get("EN_NAME").toString() : "");
				values.put(
						"PROV_CODE",
						params.get("PROV_CODE") != null ? params.get(
								"PROV_CODE").toString() : "");
				values.put(
						"CITY_CODE",
						params.get("CITY_CODE") != null ? params.get(
								"CITY_CODE").toString() : "");
				values.put("POSTCODE", params.get("POSTCODE") != null ? params
						.get("POSTCODE").toString() : "");
				values.put(
						"EXPORTJOINHUB",
						params.get("EXPORTJOINHUB") != null ? params.get(
								"EXPORTJOINHUB").toString() : "");
				values.put(
						"IMPORTJOINHUB",
						params.get("IMPORTJOINHUB") != null ? params.get(
								"IMPORTJOINHUB").toString() : "");
				values.put("orgcode", orgcode);
				db.insert(TABLE_NAME, null, values);
			}
		}
		db.close();
	}

	public List<Map<String, String>> FindLocCountyNanJing(String orgcode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;

		String[] colums = { "COUNTY_CODE", "CN_NAME", "EN_NAME", "PROV_CODE",
				"CITY_CODE", "POSTCODE", "EXPORTJOINHUB", "IMPORTJOINHUB" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums, "orgcode='" + orgcode + "'",
					null, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("COUNTY_CODE", cursor.getString(0));
					paramsMap.put("CN_NAME", cursor.getString(1));
					paramsMap.put("EN_NAME", cursor.getString(2));
					paramsMap.put("PROV_CODE", cursor.getString(3));
					paramsMap.put("CITY_CODE", cursor.getString(4));
					paramsMap.put("POSTCODE", cursor.getString(5));
					paramsMap.put("EXPORTJOINHUB", cursor.getString(6));
					paramsMap.put("IMPORTJOINHUB", cursor.getString(7));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			Log.e("FindData", e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public List<String> isHasCity(String v_xsdmdm, String orgcode) {

		List<String> list = new ArrayList<String>();
		String sql = "select case  when exportjoinhub is null then '0' else exportjoinhub end  from "
				+ TABLE_NAME
				+ " where  county_code=? and orgcode='"
				+ orgcode
				+ "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] { v_xsdmdm });
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		return list;
	}

	public String trimString(String str) {
		if (str != null) {
			return str.trim();
		}
		return null;
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

}

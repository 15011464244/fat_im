package com.cn.cdc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ResOrgDao extends ResOrgDaoHelper {

	public ResOrgDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	public ResOrgDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public void SaveResOrg(List<JSONObject> dataList, String orgcode)
			throws JSONException {
		// "org_code":"36300042", //�����
		// "org_sname":"���ݵ�ϼ�ٵ�", //�����
		// "prov_code":"35", //ʡ�ݴ���
		// "prov_name":"����", //ʡ�����
		// "city_code":"350600", //���д���
		// "city_name":"������", //�������
		// "county_code":"350602", //���ش���
		// "county_name":"ܼ����", //�������
		// "postcode":"363007", //�ʱ�
		// "orgTraditionalName":"363007", //�����������
		// "orgEnName":"363007" //��Ӣ�����

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (dataList != null) {
			db.delete(TABLE_NAME, null, null);
			for (JSONObject params : dataList) {
				values = new ContentValues();
				values.put("org_code", params.getString("org_code"));
				values.put("org_sname", params.getString("org_sname"));
				values.put("prov_code", params.getString("prov_code"));
				values.put("prov_name", params.getString("prov_name"));
				values.put("city_code", params.getString("city_code"));
				values.put("city_name", params.getString("city_name"));
				values.put("county_code", params.getString("county_code"));
				values.put("county_name", params.getString("county_name"));
				values.put("postcode", params.getString("postcode"));
				values.put("orgTraditionalName",
						params.getString("orgTraditionalName"));
				values.put("orgEnName", params.getString("orgEnName"));
				values.put("orgcode", orgcode);
				db.insert(TABLE_NAME, null, values);
			}
		}
		db.close();
	}

	public List<Map<String, String>> FindResOrgDaoBystateType(String orgcode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		// "org_code":"36300042", //�����
		// "org_sname":"���ݵ�ϼ�ٵ�", //�����
		// "prov_code":"35", //ʡ�ݴ���
		// "prov_name":"����", //ʡ�����
		// "city_code":"350600", //���д���
		// "city_name":"������", //�������
		// "county_code":"350602", //���ش���
		// "county_name":"ܼ����", //�������
		// "postcode":"363007", //�ʱ�
		// "orgTraditionalName":"363007", //�����������
		// "orgEnName":"363007" //��Ӣ�����
		String[] colums = { "org_code", "org_sname", "prov_code", "prov_name",
				"city_code", "city_name", "county_code", "county_name",
				"postcode", "orgTraditionalName", "orgEnName" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.rawQuery("select * from " + TABLE_NAME
					+ "where orgcode = '" + orgcode + "'", null);
			// db.query(TABLE_NAME,colums , null,null, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("org_code", cursor.getString(0));
					paramsMap.put("org_sname", cursor.getString(1));
					paramsMap.put("prov_code", cursor.getString(2));
					paramsMap.put("prov_name", cursor.getString(3));
					paramsMap.put("city_code", cursor.getString(4));
					paramsMap.put("city_name", cursor.getString(5));
					paramsMap.put("county_code", cursor.getString(6));
					paramsMap.put("county_name", cursor.getString(7));
					paramsMap.put("postcode",
							StringFormate(cursor.getString(8)));
					paramsMap.put("orgTraditionalName", cursor.getString(9));
					paramsMap.put("orgEnName", cursor.getString(10));

					dataList.add(paramsMap);
					// Log.e("yyyyyyyy:", ""+paramsMap.get("org_code"));
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public boolean tabDataIsExist(String orgcode) {
		SQLiteDatabase db = getReadableDatabase();
		boolean result = false;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from " + TABLE_NAME
					+ "where orgcode = '" + orgcode + "'", null);
			if (cursor.getCount() > 0) {
				result = true;
			}
		} catch (Exception e) {
			return result;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}
}

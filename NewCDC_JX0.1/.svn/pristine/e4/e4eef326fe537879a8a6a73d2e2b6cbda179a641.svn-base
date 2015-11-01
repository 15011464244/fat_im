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

import com.newcdc.db.helper.MailTypeDaoHelper;
import com.newcdc.model.MailTypeBean;

/**
 * 邮件分类数据操作
 * 
 * @author quanyi
 */
public class MailTypeDao extends MailTypeDaoHelper {

	public MailTypeDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	// TODO 存储
	public synchronized boolean saveMailTypeInfo(List<MailTypeBean> insertData,
			String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (insertData != null && insertData.size() > 0) {
//			db.delete(TB_TYPE, null, null);
			for (MailTypeBean params : insertData) {
				values = new ContentValues();
				values.put("OrgID", params.getOrgID());
				values.put("ClassCode", params.getClassCode());
				values.put("StartDate", params.getStartDate());
				values.put("ClassName", params.getClassName());
				values.put("EndDate", params.getEndDate());
				values.put("Sort", params.getSort());
				values.put("SJLimitTime", params.getSJLimitTime());
				values.put("SJLimitWeight", params.getSJLimitWeight());
				values.put("MailType", params.getMailType());
				values.put("Temp1", params.getTemp1());
				db.insert(TB_TYPE, null, values);
			}
			return true;
		}
		return false;
	}

	/**
	 * 查询分类方式代码
	 */
	public String queryMailCode(String ClassName) {
		SQLiteDatabase db = getReadableDatabase();
		String code = "";
		String sql = "select ClassCode from " + TB_TYPE + " where ClassName='"
				+ ClassName + "'";
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

	public List<Map<String, String>> FindMailTypeInfo(String orgcode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;

		String[] colums = { "OrgID", "ClassCode", "StartDate", "ClassName",
				"EndDate", "Sort", "SJLimitTime", "SJLimitWeight", "MailType" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TB_TYPE, colums, null, null, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("OrgID", cursor.getString(0));
					paramsMap.put("ClassCode", cursor.getString(1));
					paramsMap.put("StartDate", cursor.getString(2));
					paramsMap.put("ClassName", cursor.getString(3));
					paramsMap.put("EndDate", cursor.getString(4));
					paramsMap.put("Sort", cursor.getString(5));
					paramsMap.put("SJLimitTime", cursor.getString(6));
					paramsMap.put("SJLimitWeight", cursor.getString(7));
					paramsMap.put("MailType", cursor.getString(8));

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

	public String trimString(String str) {
		if (str != null) {
			return str.trim();
		}
		return null;
	}
	public synchronized void DeleteData() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TB_TYPE, null, null);
		} catch (Exception e) {
		}
	}

}

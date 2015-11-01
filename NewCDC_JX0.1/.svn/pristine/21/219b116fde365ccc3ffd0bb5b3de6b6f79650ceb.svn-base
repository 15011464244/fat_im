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

/**
 * 江西版 基础数据 更新于2015.6.24
 * */
public class BaseDataDao extends BaseDataDaoHelper {
	public BaseDataDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	// NEXT_ACTN_CODE 下一步动作
	// MAIL_NOTE_CODE 邮件备注
	// DLV_STS_CODE 投递备注
	// TURN_BACK_CAUSE_CODE 转退原因
	// UNDLV_CAUSE_CODE 未妥投原因
	public BaseDataDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public List<Map<String, String>> FindBaseDataByDataFlags(String dataFlags) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "dataKey", "dataValue", "dataFlags" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums, "dataFlags=?",
					new String[] { dataFlags }, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("dataKey", cursor.getString(0));
					paramsMap.put("dataValue", cursor.getString(1));
					paramsMap.put("dataFlags", cursor.getString(2));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public void SaveBaseData(List<BaseData> dataList, String dataFlags)
			throws JSONException {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (dataList != null) {
			for (BaseData params : dataList) {
				values = new ContentValues();
				values.put("dataKey", params.getDataKey());
				values.put("dataValue", params.getDataValue());
				values.put("dataFlags", dataFlags);
				db.insert(TABLE_NAME, null, values);
			}
		}
		db.close();
	}

	/**
	 * 根据dataFlags删除之前的旧数据 用于手动下载时
	 * */
	public synchronized int deleteBaseDataByDataFlags(String dataFlags) {
		SQLiteDatabase db = getWritableDatabase();
		int a = 0;
		if (dataFlags != null) {
			a = db.delete(TABLE_NAME, "dataFlags=?", new String[] { dataFlags });
		}
		db.close();
		return a;
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

	public synchronized void DeleteData() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TABLE_NAME, null, null);
		} catch (Exception e) {
		}
	}
}

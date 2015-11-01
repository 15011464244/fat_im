package com.cn.cdc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DownLoadDao extends DownloadDaoHelper {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	public DownLoadDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	public DownLoadDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public synchronized void SaveMail(JSONObject params, String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put(
					"IS_UPLOAD",
					params.isNull("IS_UPLOAD") ? "" : params
							.getString("IS_UPLOAD"));
			values.put(
					"IS_CHECK",
					params.isNull("IS_CHECK") ? "" : params
							.getString("IS_CHECK"));
			values.put("mailNo",
					params.isNull("mailNo") ? "" : params.getString("mailNo"));
			values.put("replaceSign", params.isNull("replaceSign") ? ""
					: params.getString("replaceSign"));
			values.put(
					"manyMainNo",
					params.isNull("manyMainNo") ? "" : params
							.getString("manyMainNo"));
			values.put(
					"manyCount",
					params.isNull("manyCount") ? "" : params
							.getString("manyCount"));
			values.put("lastUserCode", params.isNull("lastUserCode") ? ""
					: params.getString("lastUserCode"));
			values.put("lastUserName", params.isNull("lastUserName") ? ""
					: params.getString("lastUserName"));
			values.put(
					"lastDate",
					params.isNull("lastDate") ? "" : params
							.getString("lastDate"));
			values.put(
					"manySign",
					params.isNull("manySign") ? "" : params
							.getString("manySign"));
			values.put(
					"backSign",
					params.isNull("backSign") ? "" : params
							.getString("backSign"));
			values.put("appointTime", params.isNull("appointTime") ? ""
					: params.getString("appointTime"));
			values.put(
					"changeSign",
					params.isNull("changeSign") ? "" : params
							.getString("changeSign"));
			values.put("codSign",
					params.isNull("codSign") ? "" : params.getString("codSign"));
			values.put("weight",
					params.isNull("weight") ? "" : params.getString("weight"));
			values.put("createTime",
					params.isNull("createTime") ? sdf.format(new Date())
							: params.getString("createTime"));
			values.put("orgcode", orgcode);

			db.insert(TABLE_NAME, null, values);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		db.close();
	}

	public synchronized void updateMail(JSONObject params, String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			if (!params.isNull("IS_UPLOAD")) {
				values.put("IS_UPLOAD", params.getString("IS_UPLOAD"));
			}
			if (!params.isNull("IS_CHECK")) {
				values.put("IS_CHECK", params.getString("IS_CHECK"));
			}
			if (!params.isNull("mailNo")) {
				values.put("mailNo", params.getString("mailNo"));
			}
			if (!params.isNull("replaceSign")) {
				values.put("replaceSign", params.getString("replaceSign"));
			}
			if (!params.isNull("manyMainNo")) {
				values.put("manyMainNo", params.getString("manyMainNo"));
			}
			if (!params.isNull("manyCount")) {
				values.put("manyCount", params.getString("manyCount"));
			}
			if (!params.isNull("lastUserCode")) {
				values.put("lastUserCode", params.getString("lastUserCode"));
			}
			if (!params.isNull("lastUserName")) {
				values.put("lastUserName", params.getString("lastUserName"));
			}
			if (!params.isNull("lastDate")) {
				values.put("lastDate", params.getString("lastDate"));
			}
			if (!params.isNull("manySign")) {
				values.put("manySign", params.getString("manySign"));
			}
			if (!params.isNull("backSign")) {
				values.put("backSign", params.getString("backSign"));
			}
			if (!params.isNull("appointTime")) {
				values.put("appointTime", params.getString("appointTime"));
			}
			if (!params.isNull("changeSign")) {
				values.put("changeSign", params.getString("changeSign"));
			}
			if (!params.isNull("codSign")) {
				values.put("codSign", params.getString("codSign"));
			}
			if (!params.isNull("weight")) {
				values.put("weight", params.getString("weight"));
			}
			if (!params.isNull("createTime")) {
				values.put("createTime", params.getString("createTime"));
			}
			db.update(TABLE_NAME, values, "mailNo=? and orgcode=?",
					new String[] { params.getString("mailNo"), orgcode });
		} catch (JSONException e) {
			e.printStackTrace();
		}
		db.close();
	}

	public synchronized int deleteMail(String mailNo, String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		int a = 0;
		if (mailNo != null) {
			a = db.delete(TABLE_NAME,
					"mailNo=? and orgcode ='" + orgcode + "'",
					new String[] { mailNo });
		}
		db.close();
		return a;
	}

	public synchronized List<Map<String, Object>> FindDownLoad(
			String selection, String[] selectionArgs, String orgcode) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Cursor cursor = null;

		String[] colums = { "IS_UPLOAD", "IS_CHECK", "mailNo", "actualFee",
				"replaceSign", "manyMainNo", "manyCount", "lastUserCode",
				"lastUserName", "lastDate", "manySign", "backSign",
				"appointTime", "changeSign", "codSign", "weight", "createTime" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (selection.equals("") && selectionArgs.length == 0) {
				// cursor = db.query(TABLE_NAME, colums,
				// " orgcode=''"+orgcode+"", null,
				// null, null, "createTime  desc ", null);

				cursor = db.query(TABLE_NAME, colums, "orgcode=?",
						new String[] { orgcode }, null, null,
						"createTime  desc ", null);

			} else {
				cursor = db.query(TABLE_NAME, colums, selection
						+ " and orgcode='" + orgcode + "' ", selectionArgs,
						null, null, "createTime  desc", null);
			}

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					Map<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put("IS_UPLOAD",
							"0".equals(cursor.getString(0)) ? "否" : "是");
					paramsMap.put("IS_CHECK",
							"0".equals(cursor.getString(1)) ? "否" : "是");
					paramsMap.put("mailNo", cursor.getString(2));
					paramsMap.put("actualFee", cursor.getString(3));
					String replaceSign = cursor.getString(4);
					if ("1".equals(replaceSign)) {
						replaceSign = "代收";
					} else if ("2".equals(replaceSign)) {
						replaceSign = "收件人付费";
					}
					paramsMap.put("replaceSign", replaceSign);
					paramsMap.put("manyMainNo", cursor.getString(5));
					paramsMap.put("manyCount", cursor.getString(6));
					paramsMap.put("lastUserCode", cursor.getString(7));
					paramsMap.put("lastUserName", cursor.getString(8));
					paramsMap.put("lastDate", cursor.getString(9));

					String manySign = cursor.getString(10);

					if ("0".equals(manySign)) {
						manySign = "苹果一票多件";
					} else if ("1".equals(manySign)) {
						manySign = "经济快递一票多件";
					}

					paramsMap.put("manySign", manySign);

					String backSign = cursor.getString(11);
					if ("5".equals(backSign)) {
						backSign = "电子返单";
					} else if ("6".equals(backSign)) {
						backSign = "格式返单";
					} else if ("7".equals(backSign)) {
						backSign = "个性返单";
					}
					paramsMap.put("backSign", backSign);
					paramsMap.put("appointTime", cursor.getString(12));

					String changeSign = cursor.getString(13);
					// if("0".equals(changeSign)){
					// changeSign = "带货换货-是";
					// }else if("1".equals(changeSign)){
					// changeSign = "带货换货-否";
					// }
					if ("0".equals(changeSign)) {
						changeSign = "国内";
					} else {
						changeSign = "国际";
					}
					paramsMap.put("changeSign", changeSign);
					paramsMap.put("codSign", cursor.getString(14));
					paramsMap.put("weight", cursor.getString(15));
					paramsMap.put("createTime", cursor.getString(16));

					list.add(paramsMap);
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			closeCursor(cursor);
		}
		return list;
	}

}

package com.cn.cdc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.format.DateFormat;
import android.util.Log;

import com.newcdc.tools.Constant;

public class MailDao extends MailDaoHelper {

	public MailDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	public MailDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * ��ѯ�ʼ����Ƿ���ڣ��Ƿ���Ա���
	 * 
	 * @param mailCode
	 * @return
	 */
	public synchronized boolean FindIsSave(String mailCode, String orgcode) {
		Boolean flag = true;
		Cursor cursor = null;
		String[] colums = { "count(1) " };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					"  mailCode=?  and operationMode ='I' and dlvStsCode='I' and orgcode='"
							+ orgcode + "' ", new String[] { mailCode }, null,
					null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					if (cursor.getInt(0) > 0) {
						flag = false;
					}
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
			flag = false;
		} finally {
			closeCursor(cursor);
			flag = true;
		}
		return flag;
	}

	/**
	 * 查找是否重复
	 */

	public synchronized List<Map<String, Object>> FindIsReport(
			String creatTime, String deviceId, String mailCode, String orgcode,
			String dlvStsCode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		try {
			String[] colums = { "mailCode", "IS_UPLOAD" };

			cursor = db
					.query(TABLE_NAME,
							colums,
							"mailCode = ? and deviceNumber=? and orgCode=? and creatTime=? and dlvStsCode=? and operationMode = 'I'",
							new String[] { mailCode, deviceId, orgcode,
									creatTime, dlvStsCode }, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail_num", cursor.getString(0));
					paramsMap.put("is_upload", cursor.getString(1));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	/**
	 * ��ѯ�ϴ���
	 * 
	 * @param userCode
	 * @param dlvStsCode
	 * @return
	 */
	public synchronized List<Map<String, String>> FindMailUploadCountByUserCode(
			String userCode, String dlvStsCode, String orgcode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;

		try {
			if (dlvStsCode != null) {
				String[] colums = { "count(1) ", "IS_UPLOAD",
						"sum(actualGoodsFee) as actualGoodsFee",
						"sum(actualFee) as actualFee" };
				SQLiteDatabase db = getReadableDatabase();
				cursor = db.query(TABLE_NAME, colums,
						"  userCode=? and dlvStsCode=?  and operationMode ='I' and orgcode = '"
								+ orgcode + "'  ", new String[] { userCode,
								dlvStsCode }, "IS_UPLOAD", null, null);
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						paramsMap = new LinkedHashMap<String, String>();
						paramsMap.put("num", cursor.getString(0));
						paramsMap.put("IS_UPLOAD", cursor.getString(1));
						paramsMap.put("actualGoodsFee", cursor.getString(3));
						paramsMap.put("actualFee", cursor.getString(4));

						dataList.add(paramsMap);
					}
				}
			} else {
				String[] colums = { "count(1) ", "IS_UPLOAD", "dlvStsCode",
						"sum(actualGoodsFee) as actualGoodsFee",
						"sum(actualFee) as actualFee" };
				SQLiteDatabase db = getReadableDatabase();
				cursor = db.query(TABLE_NAME, colums,
						"  userCode=?  and operationMode ='I' and orgcode = '"
								+ orgcode + "' ", new String[] { userCode },
						"IS_UPLOAD,dlvStsCode", null, null);
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						paramsMap = new LinkedHashMap<String, String>();
						paramsMap.put("num", cursor.getString(0));
						paramsMap.put("IS_UPLOAD", cursor.getString(1));
						paramsMap.put("dlvStsCode", cursor.getString(2));
						paramsMap.put("actualGoodsFee", cursor.getString(3));
						paramsMap.put("actualFee", cursor.getString(4));

						dataList.add(paramsMap);
					}
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	// 缴款总额
	public synchronized Map<String, String> querysummoney(String orgcode,
			String pay_type) {
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "sum(actualGoodsFee) as actualGoodsFee,sum(actualFee) as actualFee" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (!"".equals(pay_type)) {
				cursor = db.query(TABLE_NAME, colums,
						"orgcode=? and pay_type='1'", new String[] { orgcode },
						null, null, null, null);
			} else {
				cursor = db.query(TABLE_NAME, colums, "orgcode=?",
						new String[] { orgcode }, null, null, null, null);
			}

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					if (cursor.getString(0) == null
							|| "null".equals(cursor.getString(0))) {
						paramsMap.put("actualGoodsFee", "0");
					} else {
						paramsMap.put("actualGoodsFee", cursor.getString(0)
								.toString());
					}
					if (cursor.getString(1) == null
							|| "null".equals(cursor.getString(1))) {
						paramsMap.put("actualFee", "0");

					} else {
						paramsMap.put("actualFee", cursor.getString(1)
								.toString());
					}
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return paramsMap;
	}

	/**
	 * ��ѯͶ�������
	 * 
	 * @param userCode
	 * @return
	 */
	public synchronized List<Map<String, String>> FindMailDlvStsCodeCountByUserCode(
			String userCode, String orgcode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;

		String[] colums = { "count(1) as num", "dlvStsCode" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					"  userCode=?  and operationMode ='I' and orgcode = '"
							+ orgcode + "' ", new String[] { userCode },
					" dlvStsCode", null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("num", cursor.getString(0));
					paramsMap.put("dlvStsCode", cursor.getString(1));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	/**
	 * ���Ͷ�������Ƿ��ϴ�4��ѯ ,����ѯȫ����dlvStsCode��null
	 * 
	 * @param userCode
	 * @param dlvStsCode
	 * @param IS_UPLOAD
	 * @return
	 */
	public synchronized List<Map<String, Object>> FindMailByUpload(
			String orgcode, String IS_UPLOAD) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.query(TABLE_NAME, colums, " IS_UPLOAD='" + IS_UPLOAD
					+ "' and orgcode = '" + orgcode
					+ "' and operationMode = 'I'", null, null, null,
					"operationTime  desc ");
			long count = cursor.getCount();
			Log.e("connt", count + "");
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	/**
	 * ���Ͷ�������Ƿ��ϴ�4��ѯ ,����ѯȫ����dlvStsCode��null
	 * 
	 * @param userCode
	 * @param dlvStsCode
	 * @param IS_UPLOAD
	 * @return
	 */
	public synchronized List<Map<String, Object>> FindMailByUpload(
			String userCode, String dlvStsCode, String IS_UPLOAD, int pageNo,
			String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Constant.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db.query(TABLE_NAME, colums,
						"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and orgcode = '"
								+ orgcode + "'   ", new String[] { userCode,
								dlvStsCode, IS_UPLOAD }, null, null,
						"operationTime  desc ", (pageNo - 1) * 10 + " , 10 ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=?  and operationMode ='I' and orgcode = '"
										+ orgcode + "' ", new String[] {
										userCode, dlvStsCode, IS_UPLOAD },
								null, null, "operationTime  desc ",
								(pageNo - 1) * 10 + " , 10 ");

			}

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized List<Map<String, Object>> FindMailFollow(
			String userCode, String dlvStsCode, String IS_UPLOAD,
			String undlvNextModeCode, int pageNo, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Constant.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db.query(TABLE_NAME, colums,
						"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and orgcode = '"
								+ orgcode + "'   ", new String[] { userCode,
								dlvStsCode, IS_UPLOAD }, null, null,
						"operationTime  desc ", (pageNo - 1) * 10 + " , 10 ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=?  and operationMode ='I' and undlvNextModeCode =? and orgcode = '"
										+ orgcode + "' ", new String[] {
										userCode, dlvStsCode, IS_UPLOAD,
										undlvNextModeCode }, null, null,
								"operationTime  desc ", (pageNo - 1) * 10
										+ " , 10 ");

			}

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	/**
	 * ���Ͷ�������Ƿ��ϴ�4��ѯ ,����ѯȫ����dlvStsCode��null
	 * 
	 * @param userCode
	 * @param dlvStsCode
	 * @param IS_UPLOAD
	 * @return
	 */
	public synchronized List<Map<String, Object>> FindMailByUpload(
			String userCode, String dlvStsCode, String IS_UPLOAD,
			String createDate, int pageNo, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Constant.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and substr(createDate,1,8) = ? and orgcode = '"
										+ orgcode + "' and operationMode='I'",
								new String[] { userCode, dlvStsCode, IS_UPLOAD,
										createDate }, null, null,
								"createDate  desc ", (pageNo - 1) * 10
										+ " , 10 ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=?  and operationMode ='I' and substr(createDate,1,8) = ? and orgcode = '"
										+ orgcode + "'", new String[] {
										userCode, dlvStsCode, IS_UPLOAD,
										createDate }, null, null,
								"createDate  desc ", (pageNo - 1) * 10
										+ " , 10 ");

			}
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized List<Map<String, Object>> FindMailFollow(
			String userCode, String dlvStsCode, String IS_UPLOAD,
			String createDate, String undlvNextModeCode, int pageNo,
			String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Constant.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and substr(createDate,1,8) = ? and undlvNextModeCode=? and orgcode = '"
										+ orgcode + "' ", new String[] {
										userCode, dlvStsCode, IS_UPLOAD,
										createDate, undlvNextModeCode }, null,
								null, "createDate  desc ", (pageNo - 1) * 10
										+ " , 10 ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=?  and operationMode ='I' and substr(createDate,1,8) = ? and undlvNextModeCode =? ",
								new String[] { userCode, dlvStsCode, IS_UPLOAD,
										createDate, undlvNextModeCode }, null,
								null, "createDate  desc ", (pageNo - 1) * 10
										+ " , 10 ");

			}
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized String Findcount(String userCode, String dlvStsCode,
			String mailCode, String orgcode) {
		String count = "";
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					" userCode=? and dlvStsCode=? and  mailCode=? and orgcode = '"
							+ orgcode + "' ", new String[] { userCode,
							dlvStsCode, mailCode }, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					count = cursor.getString(25);
				}
			} else
				count = "";
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
			count = "";
		} finally {
			closeCursor(cursor);
		}
		return count;
	}

	public synchronized String Findcount(String userCode, String mailCode,
			String orgcode) {
		String count = "";
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					" userCode=? and mailCode=? and IS_UPLOAD='0' and orgcode = '"
							+ orgcode + "'",
					new String[] { userCode, mailCode }, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					count = cursor.getString(25);
				}
			} else
				count = "";
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
			count = "";
		} finally {
			closeCursor(cursor);
		}
		return count;
	}

	public synchronized Map<String, Object> FindMail(String mailCode,
			String userCode, String IS_UPLOAD, String orgcode) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Cursor cursor = null;

		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					"mailCode=? and userCode=? and IS_UPLOAD=? and orgcode = '"
							+ orgcode + "'", new String[] { mailCode, userCode,
							IS_UPLOAD }, null, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return paramsMap;
	}

	/**
	 * 邮件交班查询是否存在
	 * 
	 * @return
	 */
	public synchronized Map<String, Object> FindMail(String mailCode,
			String userCode, String orgcode) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Cursor cursor = null;

		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark", "IS_CHANGE" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db
					.query(TABLE_NAME, colums,
							"mailCode=? and userCode=? and orgcode = '"
									+ orgcode + "'", new String[] { mailCode,
									userCode }, null, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("IS_CHANGE", cursor.getString(33));
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return paramsMap;
	}

	public synchronized Map<String, Object> FindMail(String mailCode,
			String userCode, String dlvStsCode, String IS_UPLOAD,
			String createDate, String orgcode) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Cursor cursor = null;

		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db
					.query(TABLE_NAME,
							colums,
							"mailCode=? and userCode=? and dlvStsCode=? and IS_UPLOAD=? and createDate = ? and  orgcode = '"
									+ orgcode + "'",
							new String[] { mailCode, userCode, dlvStsCode,
									IS_UPLOAD, createDate }, null, null,
							"createDate  desc ", null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return paramsMap;
	}

	public synchronized List<Map<String, Object>> FindMail(String mailCode,
			String userCode, String dlvStsCode, String IS_UPLOAD, int pageNo,
			String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;

		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {

			SQLiteDatabase db = getReadableDatabase();

			cursor = db
					.query(TABLE_NAME,
							colums,
							"mailCode=? and userCode=? and dlvStsCode=? and IS_UPLOAD=?  and operationMode ='I' and orgcode = '"
									+ orgcode + "' ", new String[] { mailCode,
									userCode, dlvStsCode, IS_UPLOAD }, null,
							null, "operationTime  desc ", (pageNo - 1) * 10
									+ ",10");
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized boolean SaveMail(JSONObject params, String orgcode)
			throws JSONException {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (params != null) {
			// �Ƿ����¼��
			if (FindIsSave(params.getString("mailCode"), orgcode)) {
				values = new ContentValues();
				values.put("IS_UPLOAD", params.getString("IS_UPLOAD"));
				values.put("deviceNumber", params.getString("deviceNumber"));
				values.put("orgCode", params.getString("orgCode"));
				values.put("userCode", params.getString("userCode"));
				values.put("role", params.getString("role"));
				values.put("operationMode", params.getString("operationMode"));
				values.put("mailCode", params.getString("mailCode"));
				values.put("dlvOrgCode", params.getString("dlvOrgCode"));
				values.put("dlvOrgName", params.getString("dlvOrgName"));
				values.put("dlvOrgPostCode", params.getString("dlvOrgPostCode"));
				values.put("dlvStsCode", params.getString("dlvStsCode"));
				values.put("signStsCode", params.getString("signStsCode"));
				values.put("actualGoodsFee", params.getDouble("actualGoodsFee"));
				values.put("actualTax", params.getDouble("actualTax"));
				values.put("actualFee", params.getDouble("actualFee"));
				values.put("otherFee", params.getDouble("otherFee"));
				values.put("dlvUserCode", params.getString("dlvUserCode"));
				values.put("dlvUserName", params.getString("dlvUserName"));
				values.put("undlvCauseCode", params.getString("undlvCauseCode"));
				values.put("undlvNextModeCode",
						params.getString("undlvNextModeCode"));
				values.put("signerName", params.getString("signerName"));
				values.put("interFlag", params.getString("interFlag"));
				values.put("operationTime",
						DateFormat.format("yyyyMMddkkmmss", new Date())
								.toString());
				values.put("dlvAddress", params.getString("dlvAddress"));
				values.put("signatureImg", params.getString("signatureImg"));// ͼƬ��basecode64ת�ɵ��ַ�
				values.put("createDate", params.getString("createDate"));
				values.put("longitude", params.isNull("longitude") ? ""
						: params.getString("longitude"));
				values.put(
						"latitude",
						params.isNull("latitude") ? "" : params
								.getString("latitude"));
				values.put(
						"province",
						params.isNull("province") ? "" : params
								.getString("province"));
				values.put("city",
						params.isNull("city") ? "" : params.getString("city"));
				values.put(
						"county",
						params.isNull("county") ? "" : params
								.getString("county"));
				values.put(
						"street",
						params.isNull("street") ? "" : params
								.getString("street"));
				values.put(
						"remark",
						params.isNull("remark") ? "" : params
								.getString("remark"));
				values.put("creatTime", params.getString("creatTime"));
				values.put("IS_CHANGE", params.isNull("IS_CHANGE") ? ""
						: params.getString("IS_CHANGE"));
				values.put("is_payup", "0");
				values.put("actioncode", "I");
				values.put(
						"pay_type",
						params.isNull("pay_type") ? "" : params
								.getString("pay_type"));
				try {
					long result = db.insert(TABLE_NAME, null, values);
					Log.e("result", result + "");
					if (result > 0) {
						return true;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				db.close();
				return false;
			}
		}
		db.close();
		return false;
	}

	/**
	 * �޸��Ƿ��ϴ�
	 * 
	 * @param mailCode
	 * @param userCode
	 * @param dlvStsCode
	 * @param IS_UPLOAD
	 */
	public synchronized long Updatetraintype(String orgcode, String mail_num,
			String userCode, String dlvStsCode) {

		long a = 0;

		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("operationMode", "D");
		a = db.update(
				TABLE_NAME,
				contentValues,
				"orgcode=? and mailCode=? and userCode=? and operationMode='I' and dlvStsCode=?",
				new String[] { orgcode, mail_num, userCode, dlvStsCode });
		db.close();
		return a;
	}

	public synchronized void updateMail(String mailCode, String IS_UPLOAD,
			String dlvStsCode, String createDate, String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		if (mailCode != null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("IS_UPLOAD", IS_UPLOAD);
			contentValues.put("operationTime",
					DateFormat.format("yyyyMMddkkmmss", new Date()).toString());
			long count = db.update(TABLE_NAME, contentValues,
					"mailCode=?  and dlvStsCode=? and createDate= ? and orgcode = '"
							+ orgcode + "'", new String[] { mailCode,
							dlvStsCode, createDate });
			Log.e("connt", count + "");
		}
		db.close();
	}

	public synchronized void deleteMail(String mailCode, String userCode,
			String dlvStsCode, String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		if (mailCode != null) {
			db.delete(TABLE_NAME,
					"mailCode=? and userCode=? and dlvStsCode=? and orgcode = '"
							+ orgcode + "'", new String[] { mailCode, userCode,
							dlvStsCode });
		}
		db.close();
	}

	public synchronized int deleteMailRe(String mailCode, String userCode,
			String IS_UPLOAD, String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		int a = 0;
		if (mailCode != null) {
			a = db.delete(TABLE_NAME,
					"mailCode=? and userCode=? and IS_UPLOAD=? and orgcode ='"
							+ orgcode + "'", new String[] { mailCode, userCode,
							IS_UPLOAD });
		}
		db.close();
		return a;
	}

	public synchronized void deleteDisableMail(Integer days, String orgcode) {
		// ɾ��10��֮ǰ�����
		Long time = new Date().getTime();
		Long wheretime = time - 1000 * 60 * 60 * 24 * days;
		String date = DateFormat.format("yyyyMMddkkmmss", wheretime).toString();
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "createDate< " + date + "and orgcode = '"
				+ orgcode + "'", null);
		db.delete("tb_mail_upload", "createDate< " + date + "and orgcode = '"
				+ orgcode + "'", null);
		db.close();
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	/**
	 * 退回妥投 邮件退回
	 * 
	 */

	public synchronized List<Map<String, Object>> FindMailByUpload(
			String userCode, String dlvStsCode, String IS_UPLOAD, int pageNo,
			String signStsCode, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Constant.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db.query(TABLE_NAME, colums,
						"  userCode=? and dlvStsCode=?and signStsCode? and IS_UPLOAD=? and orgcode = '"
								+ orgcode + "'   ", new String[] { userCode,
								dlvStsCode, signStsCode, IS_UPLOAD }, null,
						null, "operationTime  desc ", (pageNo - 1) * 10
								+ " , 10 ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and signStsCode=? and operationMode ='I' and orgcode = '"
										+ orgcode + "' ", new String[] {
										userCode, dlvStsCode, IS_UPLOAD,
										signStsCode }, null, null,
								"operationTime  desc ", (pageNo - 1) * 10
										+ " , 10 ");

			}

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized List<Map<String, Object>> FindMailByUpload(
			String userCode, String dlvStsCode, String IS_UPLOAD,
			String createDate, int pageNo, String signStsCode, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Constant.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and signStsCode=? and substr(createDate,1,8) = ? and orgcode = '"
										+ orgcode + "' and operationMode = 'I'",
								new String[] { userCode, dlvStsCode, IS_UPLOAD,
										signStsCode, createDate }, null, null,
								"createDate  desc ", (pageNo - 1) * 10
										+ " , 10 ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and signStsCode=? and operationMode ='I' and substr(createDate,1,8) = ?  and orgcode = '"
										+ orgcode + "' and operationMode = 'I'",
								new String[] { userCode, dlvStsCode, IS_UPLOAD,
										signStsCode, createDate }, null, null,
								"createDate  desc ", (pageNo - 1) * 10
										+ " , 10 ");

			}
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized List<Map<String, Object>> FindMailStatus(
			String userCode, String dlvStsCode, String IS_UPLOAD,
			String createDate, int pageNo, String signStsCode,
			String signStsCode1, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Constant.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and (signStsCode=? or signStsCode=?) and substr(createDate,1,8) = ? and orgcode = '"
										+ orgcode + "'and operationMode ='I'",
								new String[] { userCode, dlvStsCode, IS_UPLOAD,
										signStsCode, signStsCode1, createDate },
								null, null, "createDate  desc ", (pageNo - 1)
										* 10 + " , 10 ");

			} else if (Constant.UPLOAD.equals(IS_UPLOAD)) {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and (signStsCode=? or signStsCode=?) and operationMode ='I' and substr(createDate,1,8) = ?  and orgcode = '"
										+ orgcode + "'",
								new String[] { userCode, dlvStsCode, IS_UPLOAD,
										signStsCode, signStsCode1, createDate },
								null, null, "createDate  desc ", (pageNo - 1)
										* 10 + " , 10 ");

			}
			if (null == cursor) {
				return dataList;
			}
			long count = cursor.getCount();
			Log.e("connt", count + "");
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	/**
	 * 退回妥投
	 * 
	 */

	public synchronized List<Map<String, Object>> FindMailSatus1(
			String userCode, String dlvStsCode, String IS_UPLOAD, int pageNo,
			String signStsCode, String signStsCode1, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Constant.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=?and signStsCode=? or signStsCode=? and IS_UPLOAD=?  and orgcode ='"
										+ orgcode + "' and operationMode ='I' ",
								new String[] { userCode, dlvStsCode,
										signStsCode, signStsCode1, IS_UPLOAD },
								null, null, "operationTime  desc ",
								(pageNo - 1) * 10 + " , 10 ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and signStsCode=? or signStsCode=? and operationMode ='I' and orgcode = '"
										+ orgcode + "' ", new String[] {
										userCode, dlvStsCode, IS_UPLOAD,
										signStsCode, signStsCode1 }, null,
								null, "operationTime  desc ", (pageNo - 1) * 10
										+ " , 10 ");

			}

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	/**
	 * 查询所有未交班
	 */
	public synchronized List<Map<String, Object>> FindMailall(String userCode,
			String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark", "IS_CHANGE" };
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.query(TABLE_NAME, colums, "userCode='" + userCode
					+ "' and IS_CHANGE='0'  and orgcode = '" + orgcode + "'",
					null, null, null, "operationTime  desc ");
			long count = cursor.getCount();
			Log.e("connt", count + "");
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("num", cursor.getPosition() + 1);
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized List<Map<String, Object>> FindhasjiaobanMailall(
			String userCode, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark", "IS_CHANGE", "CHANGESTATION" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums, "userCode='" + userCode
					+ "' and IS_CHANGE='1'  and orgcode = '" + orgcode + "'",
					null, null, null, "operationTime  desc ");
			long count = cursor.getCount();
			Log.e("connt", count + "");
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("num", cursor.getPosition() + 1);
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("IS_CHANGE", cursor.getString(33));
					paramsMap.put("CHANGESTATION", cursor.getString(34));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	/**
	 * 查询所有未交班的
	 */
	public synchronized List<Map<String, Object>> FindMailallIschange(
			String userCode, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark", "IS_CHANGE" };
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.query(TABLE_NAME, colums, "userCode='" + userCode
					+ "' and IS_CHANGE='0'  and orgcode = '" + orgcode + "'",
					null, null, null, "operationTime  desc ");
			long count = cursor.getCount();
			Log.e("connt", count + "");
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("num", cursor.getPosition() + 1);
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("IS_CHANGE", cursor.getString(33));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	/**
	 * 查询所有可以缴费上传的
	 */
	@SuppressLint("SimpleDateFormat")
	public synchronized List<Map<String, Object>> FindAlllpay(String userCode,
			String orgcode, String is_payup) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg", "createDate",
				"longitude", "latitude", "province", "city", "county",
				"street", "remark", "IS_CHANGE", "pay_type" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (!"".equals(is_payup)) {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"is_payup='"
										+ is_payup
										+ "' and userCode='"
										+ userCode
										+ "' and (actualGoodsFee!='0.0' or actualFee!='0.0')  and orgcode = '"
										+ orgcode + "'", null, null, null,
								"operationTime  desc ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"is_payup='0' and userCode='"
										+ userCode
										+ "' and (actualGoodsFee!='0.0' or actualFee!='0.0')  and orgcode = '"
										+ orgcode + "'", null, null, null,
								"operationTime  desc ");
			}
			long count = cursor.getCount();
			Log.e("connt", count + "");
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddkkmmss");
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("num", cursor.getPosition() + 1);
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// ͼƬ��basecode64ת�ɵ��ַ�
					paramsMap.put(
							"createDate",
							DateFormat.format("yyyy-MM-dd kk:mm:ss",
									sf.parse(cursor.getString(25).toString()))
									.toString());
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("IS_CHANGE", cursor.getString(33));
					paramsMap.put("money",
							cursor.getDouble(12) + cursor.getDouble(14));
					paramsMap.put("pay_type", cursor.getString(34));

					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	// 更新缴款成功后的is_payup
	public synchronized void updatepayup(Map<String, Object> param,
			String orgcord, String username) {
		SQLiteDatabase db = getWritableDatabase();
		if (param != null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("is_payup", "1");
			long count = db.update(TABLE_NAME, contentValues,
					"mailCode=? and userCode=? and orgcode = ?",
					new String[] { param.get("mailCode").toString(), username,
							orgcord });
			Log.e("connt", count + "");
		}
		db.close();
	}

	// 更新交班成功
	public synchronized void updatechangeuploadMail(String userCode,
			String mailCode, String orgcode, String stations) {
		SQLiteDatabase db = getWritableDatabase();
		if (mailCode != null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("IS_CHANGE", "1");
			contentValues.put("CHANGESTATION", stations);
			// contentValues.put("operationTime",
			// DateFormat.format("yyyyMMddkkmmss", new Date()).toString());
			long count = db
					.update(TABLE_NAME, contentValues,
							"mailCode=? and userCode=? and orgcode = '"
									+ orgcode + "'", new String[] { mailCode,
									userCode });
			Log.e("connt", count + "");
		}
		db.close();
	}

	public synchronized List<Map<String, String>> FindMailhedui(
			String userCode, String orgcode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;

		try {
			String[] colums = { "count(1)", "IS_CHANGE" };
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums, " userCode=? and orgcode = '"
					+ orgcode + "'  ", new String[] { userCode }, "IS_CHANGE",
					null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("num", cursor.getString(0));
					paramsMap.put("IS_CHANGE", cursor.getString(1));
					dataList.add(paramsMap);
				}
			}

		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}
}

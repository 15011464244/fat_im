package com.cn.cdc;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.format.DateFormat;

import com.newcdc.tools.Constant;

public class MailHandDetailDao extends MailHandDetailHelper {
	public MailHandDetailDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	public MailHandDetailDao(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public synchronized boolean SaveMail(JSONObject params, String orgcode)
			throws JSONException {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (params != null) {
			values = new ContentValues();
			values.put("mail_num", params.getString("mail_num"));
			values.put("mail_type", params.getString("mail_type"));
			values.put("principal", params.getString("principal"));
			values.put("principal_type", "");// params.getString("principal_type"));
			values.put("abnormity_time", params.getString("abnormity_time"));
			values.put("create_time", params.getString("create_time"));

			if (!params.isNull("uploadTime")) {
				values.put("upload_time", params.getString("upload_time"));
			} else {
				values.put("upload_time", "");
			}
			values.put("sid", params.getString("sid"));
			values.put("is_out", params.getString("is_out"));
			values.put("out_time", params.getString("out_time"));
			values.put("code2d_num", params.getString("code2d_num"));
			values.put("paper_num", params.getString("paper_num"));
			values.put("operatorType", params.getString("operatorType"));
			values.put("oldSid", params.getString("oldSid"));
			values.put("signatureImg", params.getString("signatureImg"));

			values.put("orgcode", orgcode);
			db.insert(TABLE_NAME, null, values);
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	// private String formate(Object str){
	// String temp="";
	// if (str != null) {
	// temp = str.toString();
	//
	// return new
	// StringBuffer().append(temp.substring(0,3)).append("-").append(temp.substring(4,5)).append("-")
	// .append(temp.substring(6,7)).append(" ").append(temp.substring(8,9)).append(":")
	// .append(temp.substring(10,11)).append(":").append(temp.substring(12,13)).toString();
	// } else {
	// return "";
	// }
	// }
	public synchronized boolean SaveMail(JSONObject dataList, long sid,
			String oldsid, String orgcode) throws JSONException {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;

		if (dataList != null) {
			JSONArray objArray = new JSONArray(dataList.get("dataList")
					.toString());
			int tempsize = objArray.length();
			for (int i = 0; i < tempsize; i++) {
				values = new ContentValues();
				values.put("mail_num",
						objArray.getJSONObject(i).getString("mailCode"));
				values.put("mail_type",
						objArray.getJSONObject(i).getString("mailSate"));
				values.put("principal",
						objArray.getJSONObject(i).getString("principal"));
				values.put("principal_type", "");// objArray.getJSONObject(i).getString("principalType"));
				values.put("abnormity_time", objArray.getJSONObject(i)
						.getString("abnormityTime"));
				values.put("create_time",
						objArray.getJSONObject(i).getString("operationTime"));
				values.put("upload_time",
						objArray.getJSONObject(i).getString("uploadTime"));
				values.put("sid", sid);

				values.put("is_out", Constant.MAILIN);// params.getString("is_out"));
				values.put("out_time", "");// params.getString("out_time"));
				values.put("code2d_num", "");// params.getString("code2d_num"));
				values.put("paper_num", "");// params.getString("paper_num"));
				values.put("operatorType", "I");
				values.put("oldSid", oldsid);
				values.put("signatureImg", "");
				values.put("orgcode", orgcode);
				db.insert(TABLE_NAME, null, values);
			}
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	public synchronized List<Map<String, Object>> FindMail(String sid_time,
			String is_out, int pageNo, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "mail_num", "mail_type", "principal",
				"principal_type", "abnormity_time", "create_time",
				"upload_time", "sid", "is_out", "out_time", "code2d_num",
				"paper_num", "operatorType", "oldSid", "signatureImg" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (pageNo == -1) {
				if ("".equals(is_out)) {
					cursor = db.query(TABLE_NAME, colums,
							" sid=?  and orgcode = '" + orgcode + "'",
							new String[] { sid_time }, null, null, null, null);
				} else if ("".equals(sid_time)) {
					cursor = db.query(TABLE_NAME, colums, " is_out=? ",
							new String[] { is_out }, null, null, null, null);
				} else {
					cursor = db.query(TABLE_NAME, colums,
							" sid=? and is_out=? ", new String[] { sid_time,
									is_out }, null, null, null, null);
				}
			} else {
				if ("".equals(is_out)) {
					cursor = db.query(TABLE_NAME, colums, " sid=?  ",
							new String[] { sid_time }, null, null, null,
							(pageNo - 1) * 10 + " , 10 ");
				} else if ("".equals(sid_time)) {
					cursor = db.query(TABLE_NAME, colums, " is_out=? ",
							new String[] { is_out }, null, null, null,
							(pageNo - 1) * 10 + " , 10 ");
				} else {
					cursor = db.query(TABLE_NAME, colums,
							" sid=? and is_out=? ", new String[] { sid_time,
									is_out }, null, null, null, (pageNo - 1)
									* 10 + " , 10 ");
				}
			}
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail_num", cursor.getString(0));
					paramsMap.put("mail_type", cursor.getString(1));
					paramsMap.put("principal", cursor.getString(2));
					paramsMap.put("principal_type", cursor.getString(3));
					paramsMap.put("abnormity_time", cursor.getString(4));
					paramsMap.put("create_time", cursor.getString(5));
					paramsMap.put("upload_time", cursor.getString(6));
					paramsMap.put("sid", cursor.getString(7));
					paramsMap.put("is_out", cursor.getString(8));
					paramsMap.put("out_time", cursor.getString(9));
					paramsMap.put("code2d_num", cursor.getString(10));
					paramsMap.put("paper_num", cursor.getString(11));
					paramsMap.put("operatorType", cursor.getString(12));
					paramsMap.put("oldSid", cursor.getString(13));
					paramsMap.put("signatureImg", cursor.getString(14));

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

	public synchronized List<Map<String, Object>> FindChange(String sid_time,
			boolean ss, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "mail_num", "mail_type", "principal",
				"principal_type", "abnormity_time", "create_time",
				"upload_time", "sid", "is_out", "out_time", "code2d_num",
				"paper_num", "operatorType", "oldSid", "signatureImg" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (ss)
				cursor = db
						.query(TABLE_NAME, colums,
								" sid=? and is_out='0' and orgcode = '"
										+ orgcode + "'",
								new String[] { sid_time }, null, null, null,
								null);
			else
				cursor = db.query(TABLE_NAME, colums,
						" sid=? and is_out='1' and upload_time='' and orgcode = '"
								+ orgcode + "'", new String[] { sid_time },
						null, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail_num", cursor.getString(0));
					paramsMap.put("mail_type", cursor.getString(1));
					paramsMap.put("principal", cursor.getString(2));
					paramsMap.put("principal_type", cursor.getString(3));
					paramsMap.put("abnormity_time", cursor.getString(4));
					paramsMap.put("create_time", cursor.getString(5));
					paramsMap.put("upload_time", cursor.getString(6));
					paramsMap.put("sid", cursor.getString(7));
					paramsMap.put("is_out", cursor.getString(8));
					paramsMap.put("out_time", cursor.getString(9));
					paramsMap.put("code2d_num", cursor.getString(10));
					paramsMap.put("paper_num", cursor.getString(11));
					paramsMap.put("operatorType", cursor.getString(12));
					paramsMap.put("oldSid", cursor.getString(13));
					paramsMap.put("signatureImg", cursor.getString(14));

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

	public synchronized List<Map<String, Object>> FindOff(String sid_time,
			String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "mail_num", "mail_type", "principal",
				"principal_type", "abnormity_time", "create_time",
				"upload_time", "sid", "is_out", "out_time", "code2d_num",
				"paper_num", "operatorType", "oldSid", "signatureImg" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					" sid=? and upload_time=''  and orgcode = '" + orgcode
							+ "'", new String[] { sid_time }, null, null, null,
					null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail_num", cursor.getString(0));
					paramsMap.put("mail_type", cursor.getString(1));
					paramsMap.put("principal", cursor.getString(2));
					paramsMap.put("principal_type", cursor.getString(3));
					paramsMap.put("abnormity_time", cursor.getString(4));
					paramsMap.put("create_time", cursor.getString(5));
					paramsMap.put("upload_time", cursor.getString(6));
					paramsMap.put("sid", cursor.getString(7));
					paramsMap.put("is_out", cursor.getString(8));
					paramsMap.put("out_time", cursor.getString(9));
					paramsMap.put("code2d_num", cursor.getString(10));
					paramsMap.put("paper_num", cursor.getString(11));
					paramsMap.put("operatorType", cursor.getString(12));
					paramsMap.put("oldSid", cursor.getString(13));
					paramsMap.put("signatureImg", cursor.getString(14));

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

	public synchronized List<Map<String, Object>> FindMail(String sid_time,
			String is_out, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "mail_num", "mail_type", "principal",
				"principal_type", "abnormity_time", "create_time",
				"upload_time", "sid", "is_out", "out_time", "code2d_num",
				"paper_num", "operatorType", "oldSid", "signatureImg" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					" sid=? and is_out=? and upload_time='' and orgcode = '"
							+ orgcode + "'", new String[] { sid_time, is_out },
					null, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail_num", cursor.getString(0));
					paramsMap.put("mail_type", cursor.getString(1));
					paramsMap.put("principal", cursor.getString(2));
					paramsMap.put("principal_type", cursor.getString(3));
					paramsMap.put("abnormity_time", cursor.getString(4));
					paramsMap.put("create_time", cursor.getString(5));
					paramsMap.put("upload_time", cursor.getString(6));
					paramsMap.put("sid", cursor.getString(7));
					paramsMap.put("is_out", cursor.getString(8));
					paramsMap.put("out_time", cursor.getString(9));
					paramsMap.put("code2d_num", cursor.getString(10));
					paramsMap.put("paper_num", cursor.getString(11));
					paramsMap.put("operatorType", cursor.getString(12));
					paramsMap.put("oldSid", cursor.getString(13));
					paramsMap.put("signatureImg", cursor.getString(14));

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
	 * ȡ��û��ת��Ľ�������ʼ�
	 * 
	 * @return
	 */
	public synchronized List<Map<String, Object>> FindMailNos(String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Cursor cursor = null;
		Map<String, Object> paramsMap = null;
		String sql = " select mail_num,operatorType,oldSid from tb_mail_hand_detail where  is_out = '0'  and orgcode = '"
				+ orgcode
				+ "' and  sid in (select sid from tb_mail_hand  where hand_state = '2'  and orgcode = '"
				+ orgcode + "' and is_shift_stop != '0' ) ";
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.rawQuery(sql, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail_num", cursor.getString(0));
					paramsMap.put("operatorType", cursor.getString(1));
					paramsMap.put("oldSid", cursor.getString(2));
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
	 * ȡ��û��ת��Ľ�������ʼ�
	 * 
	 * @return
	 */
	public synchronized List<Map<String, String>> FindMailIn(String orgcode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Cursor cursor = null;
		Map<String, String> paramsMap = null;
		String sql = " select mail_num,sid from tb_mail_hand_detail where  is_out != '1' and orgcode = '"
				+ orgcode
				+ "' and  sid in (select sid from tb_mail_hand where hand_type = '1' and hand_state = '2'  and is_shift_stop != '0' and orgcode = '"
				+ orgcode + "') ";
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.rawQuery(sql, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("mail_num", cursor.getString(0));
					paramsMap.put("sid", cursor.getString(1));
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
	 * ȡ��Ͷ���ʼ� sid
	 * 
	 * @return
	 */
	public synchronized String FindMailInDlv(String mail, String orgcode) {
		Cursor cursor = null;
		String dataList = null;
		String sql = " select oldSid from tb_mail_hand_detail where mail_num = '"
				+ mail + "' and is_out != '1' and orgcode = '" + orgcode + "'";
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.rawQuery(sql, null);

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

	public synchronized List<Map<String, Object>> FindMailByUploadIN(
			String sid, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "mail_num", "mail_type", "principal",
				"principal_type", "abnormity_time", "create_time",
				"upload_time", "sid", "is_out", "out_time", "code2d_num",
				"paper_num", "operatorType", "oldSid", "signatureImg" };
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.query(TABLE_NAME, colums,
					" upload_time='' and sid = ? and orgcode = '" + orgcode
							+ "'", new String[] { sid }, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail_num", cursor.getString(0));
					paramsMap.put("mail_type", cursor.getString(1));
					paramsMap.put("principal", cursor.getString(2));
					paramsMap.put("principal_type", cursor.getString(3));
					paramsMap.put("abnormity_time", cursor.getString(4));
					paramsMap.put("create_time", cursor.getString(5));
					paramsMap.put("upload_time", cursor.getString(6));
					paramsMap.put("sid", cursor.getString(7));
					paramsMap.put("is_out", cursor.getString(8));
					paramsMap.put("out_time", cursor.getString(9));
					paramsMap.put("code2d_num", cursor.getString(10));
					paramsMap.put("paper_num", cursor.getString(11));
					paramsMap.put("operatorType", cursor.getString(12));
					paramsMap.put("oldSid", cursor.getString(13));
					paramsMap.put("signatureImg", cursor.getString(14));

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

	public synchronized String Findcount(String sid, String mail_type,
			String orgcode) {
		String count = "0";
		Cursor cursor = null;
		String[] colums = { "count(1) as num" };

		try {
			SQLiteDatabase db = getReadableDatabase();
			if ("".equals(mail_type))
				cursor = db
						.query(TABLE_NAME, colums, " sid=?and orgcode = '"
								+ orgcode + "'", new String[] { sid }, null,
								null, null);
			else
				cursor = db
						.query(TABLE_NAME, colums,
								" sid=? and mail_type=?and orgcode = '"
										+ orgcode + "'", new String[] { sid,
										mail_type }, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext())
					count = cursor.getString(0);
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
			count = "0";
		} finally {
			closeCursor(cursor);
		}
		return count;
	}

	public synchronized String FindcountMail(String sid, String orgcode) {
		String count = "0";
		Cursor cursor = null;
		String[] colums = { "count(1) as num" };

		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.query(TABLE_NAME, colums, " sid=?and orgcode = '"
					+ orgcode + "'", new String[] { sid }, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext())
					count = cursor.getString(0);
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
			count = "0";
		} finally {
			closeCursor(cursor);
		}
		return count;
	}

	public synchronized String FindcountUpload(String sid, String orgcode) {
		String count = "0";
		Cursor cursor = null;
		String[] colums = { "count(1) as num" };

		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db
					.query(TABLE_NAME, colums,
							" sid=? and upload_time!=''and orgcode = '"
									+ orgcode + "'", new String[] { sid },
							null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext())
					count = cursor.getString(0);
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
			count = "0";
		} finally {
			closeCursor(cursor);
		}
		return count;
	}

	public synchronized String FindcountDlv(String sid, String is_out,
			String orgcode) {
		String count = "0";
		Cursor cursor = null;
		String[] colums = { "count(1) as num" };

		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					" sid=? and is_out=?and orgcode = '" + orgcode + "'",
					new String[] { sid, is_out }, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext())
					count = cursor.getString(0);
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
			count = "0";
		} finally {
			closeCursor(cursor);
		}
		return count;
	}

	public synchronized String IsSaveMail(String is_out, String orgcode) {

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dataList1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String exits = "";

		String[] colums = { "mail_num", "mail_type", "principal",
				"principal_type", "abnormity_time", "create_time",
				"upload_time", "sid", "is_out", "out_time", "code2d_num",
				"paper_num", "operatorType", "oldSid", "signatureImg" };
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.query(TABLE_NAME, colums, " is_out=?and orgcode = '"
					+ orgcode + "'", new String[] { is_out }, null, null,
					"sid  desc ", null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail_num", cursor.getString(0));
					dataList.add(paramsMap);
					dataList1.add(paramsMap);
				}
			}

			String mail = "";
			String mail1 = "";
			int size = dataList.size();
			for (int i = 0; i < size; i++) {
				mail = dataList.get(i).get("mail_num").toString();
				for (int j = 0; j < size; j++) {
					if (i != j) {
						mail1 = dataList1.get(j).get("mail_num").toString();
						if (mail.equals(mail1)) {
							exits = mail1;
						}
					}
				}
			}
		} catch (Exception e) {
			exits = "";
		} finally {
			closeCursor(cursor);
		}
		return exits;
	}

	public synchronized boolean ExitMail(String mail_num, String is_out,
			String orgcode) {
		boolean isexit = false;
		Cursor cursor = null;
		String[] colums = { "mail_num", "mail_type", "principal",
				"principal_type", "abnormity_time", "create_time",
				"upload_time", "sid", "is_out", "out_time", "code2d_num",
				"paper_num", "operatorType", "oldSid", "signatureImg" };
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.query(TABLE_NAME, colums,
					" mail_num=? and is_out=? and orgcode = '" + orgcode + "'",
					new String[] { mail_num, is_out }, null, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					isexit = true;
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return isexit;
	}

	public synchronized boolean updateMail(String mail_num, String sid_time,
			String is_out, String out_time, String upload_time, String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		if (sid_time != null) {
			ContentValues contentValues = new ContentValues();
			if (!"".equals(is_out))
				contentValues.put("is_out", is_out);
			if (!"".equals(out_time))
				contentValues.put("out_time", out_time);
			if (!"".equals(upload_time))
				contentValues.put("upload_time", upload_time);
			db.update(TABLE_NAME, contentValues,
					" sid=? and mail_num=? and orgcode = '" + orgcode + "'",
					new String[] { sid_time, mail_num });
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * �޸�δ ���ϴ�
	 * 
	 * @param dataList
	 * @throws JSONException
	 */
	public void UpdateMail(String sid, String downsid, String orgcode)
			throws JSONException {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues
				.put("upload_time",
						DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date())
								.toString());
		contentValues.put("oldSid", downsid);
		db.update(TABLE_NAME, contentValues, "sid=? and orgcode = '" + orgcode
				+ "' ", new String[] { sid });
		db.close();
	}

	/**
	 * �޸ĳ���Ͷ��
	 * 
	 * @param dataList
	 * @param downsid
	 * @throws JSONException
	 */
	public void UpdateMailDlv(String downsid, String is_out, String mail_num,
			String orgcode) throws JSONException {

		SQLiteDatabase db = getWritableDatabase();
		if (downsid != null && !"".equals(downsid)) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("is_out", is_out);
			db.update(
					TABLE_NAME,
					contentValues,
					"oldSid=?  and mail_num =? and orgcode = '" + orgcode + "'",
					new String[] { downsid, mail_num });
		}
		db.close();
	}

	public synchronized void UpdateMail_isout(String mail, String sid,
			String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		if (sid != null && !"".equals(sid) && mail != null && !"".equals(mail)) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("is_out", "2");
			db.update(TABLE_NAME, contentValues,
					"sid=? and mail_num=? and orgcode = '" + orgcode + "'",
					new String[] { sid, mail });
		}
		db.close();
	}

	public synchronized void deleteMail(String mail_num, String create_time,
			String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		if (mail_num != null) {
			db.delete(TABLE_NAME,
					"mail_num=? and create_time=? and orgcode = '" + orgcode
							+ "'", new String[] { mail_num, create_time });
		}
		db.close();
	}

	public synchronized void deleteMail(String sid, String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		if (sid != null) {
			db.delete(TABLE_NAME, "sid=? and orgcode = '" + orgcode + "'",
					new String[] { sid });
		}
		db.close();
	}

	public synchronized void deleteMail(String orgcode) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "orgcode = '" + orgcode + "'", null);
		db.close();
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}
}

package com.newcdc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newcdc.db.helper.ExtraCastDaoHelper;
import com.newcdc.model.ExtraCast;
import com.newcdc.tools.UserInfoUtils;

public class ExtraCastDao extends ExtraCastDaoHelper {

	/**
	 * 额外费用
	 * 
	 * @author liunannan
	 * */
	private UserInfoUtils userInfo;

	public ExtraCastDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
		userInfo = new UserInfoUtils(context);
	}

	public synchronized void insertExtraCast(List<ExtraCast> list) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < list.size(); i++) {
			ExtraCast extraCast = list.get(i);
			ContentValues values = new ContentValues();
			values.put("flmc", extraCast.getFlmc());
			values.put("fldh", extraCast.getFldh());
			if (extraCast.getMoney() != null && !"".equals(extraCast.getMoney())) {
				values.put("money", extraCast.getMoney());
			} else {
				values.put("money", "0.0");
			}
			if (extraCast.getDelvorgcode() != null && !"".equals(extraCast.getDelvorgcode())) {
				values.put("delvorgcode", extraCast.getDelvorgcode());
			} else {
				values.put("delvorgcode", "");
			}
			if (extraCast.getUsername() != null && !"".equals(extraCast.getUsername())) {
				values.put("username", extraCast.getUsername());
			} else {
				values.put("username", "");
			}
			db.insert(TB_EXTRACAST, null, values);
		}
		db.close();
	}

	public List<ExtraCast> queryExtraCastall() {
		SQLiteDatabase database = getReadableDatabase();
		List<ExtraCast> list = new ArrayList<ExtraCast>();
		Cursor cursor = database.query(TB_EXTRACAST, null, null, null, null, null, null);
		try {
			list = parseExtraCastCursorToList(cursor);
		} catch (Exception e) {
		}
		return list;
	}

	public List<ExtraCast> queryExtraCastall_username(String delvorgcode, String username) {
		SQLiteDatabase database = getReadableDatabase();
		List<ExtraCast> list = new ArrayList<ExtraCast>();
		String sql = "select * from " + TB_EXTRACAST + " where delvorgcode='" + delvorgcode + "' and username='" + username + "'";
		Cursor cursor = database.rawQuery(sql, null);
		try {
			list = parseExtraCastCursorToList(cursor);
		} catch (Exception e) {
		}
		return list;
	}

	public ArrayList<ExtraCast> parseExtraCastCursorToList(Cursor cursor) {
		ArrayList<ExtraCast> extraCastList = new ArrayList<ExtraCast>();
		while (cursor.moveToNext()) {
			ExtraCast extraCast = new ExtraCast();
			if (cursor != null && cursor.getCount() != 0) {
				extraCast.set_id(cursor.getInt(cursor.getColumnIndex("_id")) + "");
				extraCast.setFlmc(cursor.getString(cursor.getColumnIndex("flmc")));
				extraCast.setFldh(cursor.getString(cursor.getColumnIndex("fldh")));
				extraCast.setMoney(cursor.getString(cursor.getColumnIndex("money")));
				extraCast.setUsername(cursor.getString(cursor.getColumnIndex("username")));
				extraCast.setDelvorgcode(cursor.getString(cursor.getColumnIndex("delvorgcode")));
				extraCastList.add(extraCast);
			}
		}
		closeCursor(cursor);
		return extraCastList;
	}

	/**
	 * @Title: deleteExtraCast
	 * @Description: TODO根据邮件号，工号删除其他缴费
	 */
	public synchronized void deleteExtraCast() {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "delete from " + MONEY_TABLE + " where org_code='" + userInfo.getUserDelvorgCode() + "' and username='" + userInfo.getUserName() + "'";
		database.execSQL(sql);
		database.close();
	}

	/**
	 * 根据_id更新其他金额缴费
	 * 
	 * @return
	 */
	public synchronized void updateOther_Money(int _id, String money) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + TB_EXTRACAST + " set money='" + money + "' where _id=" + _id;
		database.execSQL(sql);
		database.close();
	}

	/**
	 *根据机构号工号将 money置为0.0
	 * 
	 * @return
	 */
	public synchronized void updateOther_Money() {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + TB_EXTRACAST + " set money='0.0' where delvorgcode='" + userInfo.getUserDelvorgCode() + "' and username='"
				+ userInfo.getUserName() + "'";
		database.execSQL(sql);
		database.close();
	}
}

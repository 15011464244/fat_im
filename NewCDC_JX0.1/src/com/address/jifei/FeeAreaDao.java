package com.address.jifei;

import java.util.ArrayList;

import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class FeeAreaDao extends FeeAreaDaoHelper {

	public FeeAreaDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public FeeAreaDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public ArrayList<FeeArea> queryAll() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);
		return parseFeeAreaCursorToList(cursor);
	}

	/**
	 * 根据 send_area_code receive_area_code product_type查询
	 * */
	public ArrayList<FeeArea> queryFeeIDByCode(String send_area_code,
			String receive_area_code, int product_type) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TABLE_NAME + " where send_area_code= "
				+ send_area_code + " and receive_area_code= "
				+ receive_area_code + " and product_type=" + product_type;
		Cursor cursor = db.rawQuery(sql, null);
		return parseFeeAreaCursorToList(cursor);
	}

	public String queryCode(String name) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TABLE_NAME
				+ " where receive_area_name like '%" + name+"%'";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<FeeArea> arrayList=parseFeeAreaCursorToList(cursor);
		if (1<=arrayList.size()) {
			return arrayList.get(0).getReceive_area_code();
		}else {
			return null;
		}
	}
	public String querySenderCode(String name) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TABLE_NAME
				+ " where send_area_name like '%" + name+"%'";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<FeeArea> arrayList=parseFeeAreaCursorToList(cursor);
		if (1<=arrayList.size()) {
			return arrayList.get(0).getSend_area_code();
		}else {
			return null;
		}
	}

	public ArrayList<FeeArea> parseFeeAreaCursorToList(Cursor cursor) {
		ArrayList<FeeArea> feeAreaList = new ArrayList<FeeArea>();
		while (cursor.moveToNext()) {
			FeeArea feeArea = new FeeArea();
			if (cursor != null && cursor.getCount() != 0) {
				feeArea.setFee_area_id(cursor.getInt(cursor
						.getColumnIndex("fee_area_id")));
				feeArea.setSend_area_code(cursor.getString(cursor
						.getColumnIndex("send_area_code")) + "");
				feeArea.setSend_area_name(cursor.getString(cursor
						.getColumnIndex("send_area_name")) + "");
				feeArea.setReceive_area_code(cursor.getString(cursor
						.getColumnIndex("receive_area_code")) + "");
				feeArea.setReceive_area_name(cursor.getString(cursor
						.getColumnIndex("receive_area_name")));
				feeArea.setProduct_type(cursor.getInt(cursor
						.getColumnIndex("product_type")));
				feeAreaList.add(feeArea);
			}
		}
		closeCursor(cursor);
		return feeAreaList;
	}
}

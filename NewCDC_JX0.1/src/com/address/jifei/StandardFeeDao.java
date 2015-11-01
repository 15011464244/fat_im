package com.address.jifei;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class StandardFeeDao extends StandardFeeDaoHelper {

	public StandardFeeDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public StandardFeeDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public ArrayList<StandardFee> queryAll() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from  " + TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);
		return parseStandardFeeCursorToList(cursor);
	}

	/**
	 * 根据 fee_area_id weight_range_id查询
	 * */
	public ArrayList<StandardFee> queryFee(int fee_area_id, int weight_range_id) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from  " + TABLE_NAME + " where fee_area_id="
				+ fee_area_id + " and weight_range_id=" + weight_range_id;
		Cursor cursor = db.rawQuery(sql, null);
		return parseStandardFeeCursorToList(cursor);
	}

	public ArrayList<StandardFee> parseStandardFeeCursorToList(Cursor cursor) {
		ArrayList<StandardFee> standardFeeList = new ArrayList<StandardFee>();
		while (cursor.moveToNext()) {
			StandardFee standardFee = new StandardFee();
			if (cursor != null && cursor.getCount() != 0) {
				standardFee.setFee_id(cursor.getInt(cursor
						.getColumnIndex("fee_id")));
				standardFee.setFee_area_id(cursor.getInt(cursor
						.getColumnIndex("fee_area_id")));
				standardFee.setWeight_range_id(cursor.getInt(cursor
						.getColumnIndex("weight_range_id")));
				standardFee.setFirst_weight(cursor.getInt(cursor
						.getColumnIndex("first_weight")));
				standardFee.setFirst_fee(cursor.getInt(cursor
						.getColumnIndex("first_fee")));
				standardFee.setFollow_weight(cursor.getInt(cursor
						.getColumnIndex("follow_weight")));
				standardFee.setFollow_fee(cursor.getInt(cursor
						.getColumnIndex("follow_fee")));
				standardFeeList.add(standardFee);
			}
		}
		closeCursor(cursor);
		return standardFeeList;
	}
}

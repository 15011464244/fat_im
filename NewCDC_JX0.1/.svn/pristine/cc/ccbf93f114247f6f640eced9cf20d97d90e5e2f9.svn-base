package com.address.jifei;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class WeightRangeDao extends WeightRangeDaoHelper {

	public WeightRangeDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public WeightRangeDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public ArrayList<WeightRange> queryAll() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from  " + TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);
		return parseWeightRangeCursorToList(cursor);
	}
	/**
	 * 根据 weight_min weight_max product_type查询
	 * */
	public ArrayList<WeightRange> queryWeight_range_id(int weight_min,
			int weight_max, int product_type) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from  " + TABLE_NAME + " where weight_min="
				+ weight_min + " and weight_max=" + weight_max
				+ " and product_type=" + product_type;
		Cursor cursor = db.rawQuery(sql, null);
		return parseWeightRangeCursorToList(cursor);
	}

	public ArrayList<WeightRange> parseWeightRangeCursorToList(Cursor cursor) {
		ArrayList<WeightRange> weightRangeList = new ArrayList<WeightRange>();
		while (cursor.moveToNext()) {
			WeightRange weightRange = new WeightRange();
			if (cursor != null && cursor.getCount() != 0) {
				weightRange.setWeight_range_id(cursor.getInt(cursor
						.getColumnIndex("weight_range_id")));
				weightRange.setWeight_min(cursor.getInt(cursor
						.getColumnIndex("weight_min")));
				weightRange.setWeight_max(cursor.getInt(cursor
						.getColumnIndex("weight_max")));
				weightRange.setProduct_type(cursor.getInt(cursor
						.getColumnIndex("product_type")));
				weightRangeList.add(weightRange);
			}
		}
		closeCursor(cursor);
		return weightRangeList;
	}
}

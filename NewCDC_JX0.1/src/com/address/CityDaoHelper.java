package com.address;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;
import android.util.Log;

public class CityDaoHelper extends BaseSQLiteOpenHelper implements
		DatabaseConstants {

	protected static final String TABLE_NAME = "z_loc_city";
	protected static final String CREATESQL = "CREATE TABLE z_loc_city ( CITY_CODE varchar(8),CN_NAME varchar(20) , EN_NAME varchar(20) , PROV_CODE varchar(2) ,POSTCODE varchar(10) ) ";

	private AssetManager assetManager = null;

	public CityDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		assetManager = context.getAssets();
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL(CREATESQL);
			readFromAsset("z_loc_city.sql", db);
		}
	}

	public boolean tabIsExist(SQLiteDatabase db, String tabName) {
		boolean result = false;
		if (tabName == null) {
			return result;
		}
		Cursor cursor = null;
		String sql = "select count(*) as c from sqlite_master where type='table' and name = '"
				+ tabName.trim() + "'";
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			Log.e("ems", e.getMessage());
			return result;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}

	private void readFromAsset(String fileName, SQLiteDatabase database) {
		String sqlUpdate = null;
		try {
			InputStream in = assetManager.open(fileName);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, "UTF-8"));
			database.beginTransaction();
			while ((sqlUpdate = bufferedReader.readLine()) != null) {
				if (!TextUtils.isEmpty(sqlUpdate)) {
					database.execSQL(sqlUpdate);
				}
			}
			database.setTransactionSuccessful();
			database.endTransaction();
			bufferedReader.close();
			in.close();
		} catch (Exception e) {
		}

	}

}

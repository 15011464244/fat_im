package com.address;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseSQLiteOpenHelper extends SQLiteOpenHelper {

	public BaseSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS TB_PUSH_DATA ");
		db.execSQL("DROP TABLE IF EXISTS USER ");
		db.execSQL("DROP TABLE IF EXISTS tb_fee_area ");
		db.execSQL("DROP TABLE IF EXISTS tb_weight_range ");
		db.execSQL("DROP TABLE IF EXISTS tb_standard_fee ");
	}
	
	public String StringFormate(Object str) {
		if (str != null && !"null".equals(str)) {
			return str.toString();
		} else {
			return "";
		}
	}
	
	public boolean tabIsExist(SQLiteDatabase db, String tabName) {
		boolean result = false;
		if(tabName == null){
			return result;
		}
		Cursor cursor = null;
		String sql = "select count(*) as c from sqlite_master where type='table' and name = '"+tabName.trim()+"'";
		try {
			cursor = db.rawQuery(sql, null);
			if(cursor.moveToNext()){
				int count = cursor.getInt(0);
				if(count>0){
					result = true;
				}
			}
		} catch (Exception e) {
			Log.e( Global.DIALOG_NAME, e.getMessage());
			return result;
		} finally{
			if (cursor != null) {
	            cursor.close();
	        }
		}
		return result;
	}

	
	public void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}

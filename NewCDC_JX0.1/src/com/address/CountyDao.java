package com.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CountyDao extends CountyDaoHelper {
	
	public CountyDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public CountyDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public String  getCountyCode( String county){
		Cursor cursor = null;
		 String resCode = "";
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  new String[]{"COUNTY_CODE"}, "CN_NAME=?", new String[]{county}, null, null, null);
			 if(cursor.getCount()>0){
				 while (cursor.moveToNext()) { 
					 resCode = cursor.getString(0);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			closeCursor(cursor);
		}
		return resCode;
	}
	
	public List<Map<String, String>> getCountys(String cityId){
		List<Map<String, String>> list = null;
		Cursor cursor = null;
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  new String[]{"COUNTY_CODE","CN_NAME"}, "city_code=?", new String[]{cityId}, null, null, "   COUNTY_CODE ");
			 if(cursor.getCount()>0){
				 list = new ArrayList<Map<String, String>>();
				 Map<String, String> address = null;
				 while (cursor.moveToNext()) { 
					 address = new HashMap<String, String>();
					 address.put("code",cursor.getString(0));
					 address.put("value",cursor.getString(1));
					 list.add(address);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			closeCursor(cursor);
		}
		return list;
	}
	
	
	
	public String  getCountyName( String county_code){
		Cursor cursor = null;
		 String resName = "";
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  new String[]{"CN_NAME"}, "COUNTY_CODE=?", new String[]{county_code}, null, null, null);
			 if(cursor.getCount()>0){
				 while (cursor.moveToNext()) { 
				     resName = cursor.getString(0);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			closeCursor(cursor);
		}
		return resName;
	}
}

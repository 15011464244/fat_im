package com.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CityDao extends CityDaoHelper {
	
	public CityDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public CityDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public List<Map<String, String>> getCitys(String proId){
		List<Map<String, String>> list = null;
		Cursor cursor = null;
		 
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  new String[]{"CITY_CODE","CN_NAME"}, "PROV_CODE=?", new String[]{proId}, null, null, " CITY_CODE ");
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
	public String  getCityCode( String city){
		Cursor cursor = null;
		 String resCode = "";
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  new String[]{"CITY_CODE"}, "CN_NAME=?", new String[]{city}, null, null, null);
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
	
	
	public String getCitys(String code,boolean b){
		Cursor cursor = null;
		 String restr = "";
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  new String[]{"CITY_CODE","CN_NAME"}, "CITY_CODE=?", new String[]{code}, null, null, null);
			 if(cursor.getCount()>0){
				 while (cursor.moveToNext()) { 
					 restr = cursor.getString(1);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			closeCursor(cursor);
		}
		return restr;
	}

}

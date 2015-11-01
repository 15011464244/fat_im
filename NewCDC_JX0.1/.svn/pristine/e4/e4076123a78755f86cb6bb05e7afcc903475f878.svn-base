package com.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ProvinceDao  extends ProvinceDaoHelper {
	
	
	public ProvinceDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public ProvinceDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}


	
	
	public List<Map<String, String>> getProvs(){
		List<Map<String, String>> list = null;
		Cursor cursor = null;
		 
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  new String[]{"PROV_CODE","CN_NAME"}, null, null, null, null, " PROV_CODE ");
			 if(cursor.getCount()>0){
				 list = new ArrayList<Map<String, String>>();
				 Map<String, String> resultMap = null;
				 while (cursor.moveToNext()) { 
					 resultMap = new HashMap<String, String>();
					 resultMap.put("code", cursor.getString(0));
					 resultMap.put("value", cursor.getString(1));
					 list.add(resultMap);
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

	public String getProv(String prov_code){
	    String prov_name="";
		Cursor cursor = null;
		 
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  new String[]{"CN_NAME"}, "PROV_CODE=?", new String[]{prov_code}, null, null, " PROV_CODE ");
			 if(cursor.getCount()>0){
			     while (cursor.moveToNext()) { 
				 prov_name=cursor.getString(0);
			 }
			     
			 }
		}
		catch(Exception e) {
		}
		finally {
			closeCursor(cursor);
		}
		return prov_name;
	}
	
}

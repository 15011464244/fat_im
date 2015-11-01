package com.ems.express.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreUtils {
	private static SharedPreferences preferences;
	private final static String NAME ="ems"; 
	public static void init(Context context){
		preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}
	
	public static void saveString(String key,String value){
		preferences.edit().putString(key, value).commit();
	}
	
	public static String loadString(String key,String defaultString){
		return preferences.getString(key, defaultString);
	}
	
	public static void saveBoolean(String key,boolean value){
		preferences.edit().putBoolean(key, value).commit();
	}
	public static boolean loadBoolean(String key,boolean defaultBoolean){
		return preferences.getBoolean(key, defaultBoolean);
	}
	
	public static void saveInt(String key,int value){
		preferences.edit().putInt(key, value).commit();
	}
	
	public static int loadInt(String key,int deafultInt){
		return preferences.getInt(key, deafultInt);
	}
	
	public static void saveLong(String key,long value){
		preferences.edit().putLong(key, value).commit();
		
	}
	public static long loadLong(String key,long defualtLong){
		return preferences.getLong(key, defualtLong);
	}
	
}

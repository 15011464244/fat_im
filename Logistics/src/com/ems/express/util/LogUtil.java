package com.ems.express.util;

import java.util.HashMap;
import java.util.Iterator;


import android.util.Log;

public class LogUtil {

	private static HashMap<String, String> map;


	private final static Boolean DEBUG = true;
	private final static String TAG = "debug";

	public static void print(String str) {
		if (DEBUG) {
			Log.d(TAG, str);
		}
	}

	public static void print(HashMap<String, String> hashmap) {
		if (DEBUG) {
			print("{");
			Iterator<String> iterator = hashmap.keySet().iterator();
			while (iterator.hasNext()) {
				String str = iterator.next();
				print(str+" = \""+hashmap.get(str)+"\";");
			}
			print("}");
		}
	}

}

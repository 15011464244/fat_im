package com.newcdc.application;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * 
 * @author 2014-12-23 Lion base
 * 
 */
public class BaseActivity extends Activity {
	public static int height = 0;// 屏幕高度
	public static int width = 0;// 屏幕宽度
	public static float scale = 0;// 屏幕密度
	// 正则表达式匹配电话
	public static final String regEx = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
	public static final String isMEx = "^((\\+86)?|\\(\\+86\\))0?1[358]\\d{9}$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		height = metric.heightPixels;
		width = metric.widthPixels;
		scale = metric.density;
	}

	// 是否电话号
	public boolean isPhoneNum(String num) {
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(num);
		boolean rs = mat.find();
		return rs;
	}

	// 是否手机号
	public boolean isMobileNum(String num) {
		Pattern pat = Pattern.compile(isMEx);
		Matcher mat = pat.matcher(num);
		boolean rs = mat.find();
		return rs;
	}

	// 获得返回值中result字段的值
	public static String resState(String s) {
		String str = "";
		try {
			JSONObject json = new JSONObject(s);
			str = json.get("result").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 获得返回值中error
	public JSONObject resBody(String s) {
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = new JSONObject(s);
			obj = json.getJSONObject("body");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	// 获得设备号
	public String getDeviceId() {
		TelephonyManager telephonemanage = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = telephonemanage.getDeviceId();
		return deviceId;
	}

	// toast
	public void showToast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	// log
	public void showLog(String tag, String msg) {
		Log.e(tag, msg);
	}

}

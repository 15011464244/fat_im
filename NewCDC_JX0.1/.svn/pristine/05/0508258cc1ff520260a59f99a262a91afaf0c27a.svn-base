package com.newcdc.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author hanrong
 * @version 创建时间：2014-12-23 下午5:46:43 
 * 类说明：手机信息工具类
 */
public class PhoneMessageUtils {

	/**
	 * 手机设备号
	 */
	public static String deviceId(Context content) {
		if (content != null) {
			TelephonyManager telephonemanage = (TelephonyManager) content
					.getSystemService(Context.TELEPHONY_SERVICE);
			return telephonemanage.getDeviceId();
		} else {
			return "";
		}

	}

	/**
	 * 获取手机当前时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String nowTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日   HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * 将毫秒转成日期格式
	 */
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("unused")
	private String times(long times) {
		if (times > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(times);
			return sdf.format(ca.getTime());
		} else {
			return "错误时间值";
		}

	}
	public static int daysBetween(long oldDate , long nowDate){
		long between_days = (nowDate - oldDate)/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days));
	}
}

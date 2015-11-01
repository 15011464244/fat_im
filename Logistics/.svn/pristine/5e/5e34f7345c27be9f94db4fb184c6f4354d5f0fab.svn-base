package com.ems.express.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时期时间工具类
 */
public class DateTimeUtil {
	private static Date date = new Date();
	public final static SimpleDateFormat timeFormat = new SimpleDateFormat(
			"HH:mm:ss");
	public final static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat currentTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	/**
	 * 获取格式为：yyyy-MM-dd HH:mm 的当前日期
	 */
	public static String getCurrentTime() {
		return currentTimeFormat.format(new java.util.Date(date.getTime()));
	}
	
	/**
	 * 获取聊天列表的显示时间   如12月4日/昨天/早上10:43/上午 中午 下午 傍晚 晚上 凌晨
	 */
	public static String showTime(String date) {
		String temp[] = date.split(" ");
		if (temp[0].equals(getCurrentDate())) {
			String hour[] = temp[1].split(":");
			if (Integer.parseInt(hour[0]) < 7)
				return "凌晨 " + temp[1];
			else if (Integer.parseInt(hour[0]) < 9)
				return "早上 " + temp[1];
			else if (Integer.parseInt(hour[0]) < 12)
				return "上午 " + temp[1];
			else if (Integer.parseInt(hour[0]) < 13)
				return "中午 " + temp[1];
			else if (Integer.parseInt(hour[0]) < 18)
				return "下午 " + temp[1];
			else if (Integer.parseInt(hour[0]) < 20)
				return "傍晚 " + temp[1];
			else if (Integer.parseInt(hour[0]) < 24)
				return "晚上 " + temp[1];
		} else if (temp[0].equals(getLastDay()))
			return "昨天";
			return temp[0].substring(5);
	}
	
	/**
	 * @return 昨天的日期
	 */
	public static String getLastDay() {
		return dateFormat.format(new Date().getTime() - 24 * 60 * 60 * 1000);
	}
	/**
	 * @return 今天的日期
	 */
	public static String getCurrentDate() {
		return dateFormat.format(new Date());
	}
	/**
	 * @return 明天的日期
	 */
	public static String getNextDay() {
		return dateFormat.format(new java.util.Date(date.getTime() + 24 * 60 * 60 * 1000));
	}
	/**
	 * @return 明日是周几 星期一到七(1-7)
	 */
	public static int getNextDayOfweek() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date(date.getTime() + 24 * 60 * 60
				* 1000));
		if (calendar.get(Calendar.DAY_OF_WEEK) == 1)
			return 7;
		else
			return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	

	/**
	 * @return 今天的日期
	 */
	public static String getToday() {
		String operationtime = dateFormat.format(new java.util.Date(date
				.getTime()));
		return operationtime;
	}

	/**
	 * @return 现在的时间 默认的系统格式
	 */
	public static Date getNowTime() {
		return new Date();
	}

	/**
	 * 
	 * @param date
	 * @return "yyyy-MM-dd" 格式的字符串
	 */
	public static String validateDate(Date date) {
		return date == null ? null : DateTimeUtil.dateFormat.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return "HH:mm" 格式的字符串
	 */
	public static String validateTime(Date date) {
		return date == null ? null : DateTimeUtil.timeFormat.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return "yyyy-MM-dd HH:mm:ss" 格式的字符串
	 */
	public static String validateDateTime(Date date) {
		return date == null ? null : DateTimeUtil.dateTimeFormat.format(date);
	}

	/**
	 * 
	 * @param dateStr
	 *            String 格式为"yyyy-MM-dd"
	 * @return 日期
	 */
	public static Date validateDate(String dateStr) {
		try {
			return dateStr == null || dateStr.equals("") ? null
					: DateTimeUtil.dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param dateStr
	 *            String 格式为"HH:mm"
	 * @return 日期
	 */
	public static Date validateTime(String dateStr) {
		try {
			return dateStr == null || dateStr.equals("") ? null
					: DateTimeUtil.timeFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param dateStr
	 *            String 格式为"yyyy-MM-dd HH:mm:ss"
	 * @return 日期
	 */
	public static Date validateDateTime(String dateStr) {
		try {
			return dateStr == null || dateStr.equals("") ? null
					: DateTimeUtil.dateTimeFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
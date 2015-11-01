package com.newcdc.tools;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtilDaoFactory {
	private static SharedPreferences sp;
	private static SharePreferenceUtilDaoFactory spDao;
	private static SharedPreferences.Editor editor;
	private static String file = Constant.SAVE_USER;

	public synchronized static SharePreferenceUtilDaoFactory getInstance(
			Context context) {// 单线程单例
		if (spDao == null) {
			spDao = new SharePreferenceUtilDaoFactory();
		}
		sp = context.getSharedPreferences(file, context.MODE_PRIVATE);
		editor = sp.edit();
		return spDao;
	}

	// 存入头像
	public void setHead_img(String head_img) {
		editor.putString("head_img", head_img);
		editor.commit();
	}

	// 获取头像
	public String getHead_img() {
		return sp.getString("head_img", null);
	}

	// CLIENTID
	public void setCLIENTID(String CLIENTID) {
		editor.putString("CLIENTID", CLIENTID);
		editor.commit();
		Log.e("CLIENTID------", CLIENTID);
	}

	public String getCLIENTID() {
		return sp.getString("CLIENTID", "");
	}

	// 用户的工号
	public void setUSERNAME(String USERNAME) {
		editor.putString("USERNAME", USERNAME);
		editor.commit();
	}

	/**
	 * 存入上次定位地址
	 */
	public void setOldAddr(String oldAddr) {
		editor.putString("oldAddr", oldAddr);
		editor.commit();
	}

	/**
	 * 获取上次定位地址
	 */
	public String getOldAddr() {
		return sp.getString("oldAddr", null);
	}

	// -----------------------------------------start
	// 自动登陆
	public void setAutoLogin(boolean isAutoLogin) {
		editor.putBoolean("autologin", isAutoLogin);
		editor.commit();
	}

	// 记住密码
	public void setSavePass(boolean isSavePass) {
		editor.putBoolean("savepass", isSavePass);
		editor.commit();
	}

	// 密码
	public void setPass(String password) {
		editor.putString("password", password);
		editor.commit();
	}

	// 自动登陆
	public boolean getAutoLogin() {
		return sp.getBoolean("autologin", false);
	}

	// 记住密码
	public boolean getSavePass() {
		return sp.getBoolean("savepass", false);
	}

	// 密码
	public String getPass() {
		return sp.getString("password", "");
	}

	// 清除存储数据
	public void clearEditor() {
		editor.putBoolean("savepass", false);
		editor.putBoolean("autologin", false);
		editor.commit();
	}

	// 存储天气信息
	public void setWeatherInfo(String weatherInfo) {
		editor.putString("weather", weatherInfo);
		editor.commit();
	}

	// 获取天气信息
	public String getWeatherInfo() {
		return sp.getString("weather", "");
	}

	// 存储揽收信息
	public void setClc(String Clc) {
		editor.putString("Clc", Clc);
		editor.commit();
	}

	// 获取揽收信息
	public String getClc() {
		return sp.getString("Clc", "");
	}

	// 存储天气信息
	public void setException(String weatherInfo) {
		editor.putString("except", weatherInfo);
	}

	/**
	 * 存储下段流水号，任务号等信息，用于查询下段任务
	 * 
	 * @param deliverTaskInformation
	 *            参数规则：以逗号分隔三个参数，任务编码，任务流水号，投递任务数量
	 */
	public void setDeliverTaskInformation(String deliverTaskInformation) {
		editor.putString("deliverTaskInformation", deliverTaskInformation);
		editor.commit();
	}

	/**
	 * 查询下段流水号，任务号等信息
	 * 
	 * @return 返回Map，包含三个字段：taskCode：任务编码 taskSerial：任务流水号 taskCount：投递任务邮件数量
	 *         返回null：没有需要查找的投递任务
	 */
	public HashMap<String, String> getDeliverTaskInformation() {
		String info = sp.getString("deliverTaskInformation", null);
		HashMap<String, String> map = new HashMap<String, String>();
		if (info != null) {
			String[] split = info.split(",");
			map.put("taskCode", split[0]);
			map.put("taskSerial", split[1]);
			map.put("taskCount", split[2]);
			return map;
		} else {
			return null;
		}
	}

	// -----------------------------------------end
	// 存入日期
	public void setDate(String date) {
		editor.putString("format", date);
		editor.commit();
	}

	// 获取日期
	public String getDate() {
		return sp.getString("format", null);
	}

	public String getUSERNAME() {
		return sp.getString("USERNAME", "");
	}

	// 用户的机构号
	public void setDELVORGCODE(String DELVORGCODE) {
		editor.putString("DELVORGCODE", DELVORGCODE);
		editor.commit();
	}

	public String getDELVORGCODE() {
		return sp.getString("DELVORGCODE", "");
	}

	// 是否是第一次安装 默认是第一次安装
	public void setIsFirst(boolean IsFirst) {
		editor.putBoolean("IsFirst", IsFirst);
		editor.commit();
	}

	public boolean getIsFirst() {
		return sp.getBoolean("IsFirst", true);
	}

	// 下载分组完成
	public void setIsDownOver(boolean IsDownOver) {
		editor.putBoolean("IsDownOver", IsDownOver);
		editor.commit();
	}

	public boolean getIsDownOver() {
		return sp.getBoolean("IsDownOver", false);
	}

	// 定位成功
	public void setIsLocation(boolean IsLocation) {
		editor.putBoolean("IsLocation", IsLocation);
		editor.commit();
	}

	public boolean getIsLocation() {
		return sp.getBoolean("IsLocation", false);
	}

	// 是否开启了定位提醒
	public void setIsDWTX(boolean IsDWTX) {
		editor.putBoolean("IsDWTX", IsDWTX);
		editor.commit();
	}

	public boolean getIsDWTX() {
		return sp.getBoolean("IsDWTX", true);
	}

	// 是否开启了预警提醒
	public void setIsYJ(boolean IsYJ) {
		editor.putBoolean("IsYJ", IsYJ);
		editor.commit();
	}

	public boolean getIsYJ() {
		return sp.getBoolean("IsYJ", true);
	}

	// 设置中预警提醒中填入揽收的时间
	public void setYJ_LS_time(String YJ_LS_time) {
		editor.putString("YJ_LS_time", YJ_LS_time);
		editor.commit();
	}

	public String getYJ_LS_time() {
		return sp.getString("YJ_LS_time", 1 * 60 + "");
	}

	// 设置中预警提醒中填入的投递时间
	public void setYJ_TD_time(String YJ_TD_time) {
		editor.putString("YJ_TD_time", YJ_TD_time);
		editor.commit();
	}

	public String getYJ_TD_time() {
		return sp.getString("YJ_TD_time", 2 * 60 + "");
	}

	// 设置中定位提醒中填入的时间
	public void setDW_time(String DW_time) {
		editor.putString("DW_time", DW_time);
		editor.commit();
	}

	public String getDW_time() {
		return sp.getString("DW_time", "10");
	}

	// 设置中定位提醒中填入的距离
	public void setDW_distance(String DW_distance) {
		editor.putString("DW_distance", DW_distance);
		editor.commit();
	}

	public String getDW_distance() {
		return sp.getString("DW_distance", "50");
	}

	// 是否开启了蓝牙打印
	public void setisPDA(boolean isPDA) {
		editor.putBoolean("isPDA", isPDA);
		editor.commit();
	}

	public boolean getisPDA() {
		return sp.getBoolean("isPDA", false);
	}

	// ----------基础数据下载-------------------//
	/**
	 * 基础数据任务1下载完成标识
	 */
	public void setDownTask1Success(boolean task1) {
		editor.putBoolean("task1", task1);
		editor.commit();
	}

	public boolean getTask1() {
		return sp.getBoolean("task1", false);
	}

	/**
	 * 基础数据任务2下载完成标识
	 */
	public void setDownTask2Success(boolean task2) {
		editor.putBoolean("task2", task2);
		editor.commit();
	}

	public boolean getTask2() {
		return sp.getBoolean("task2", false);
	}

	/**
	 * 基础数据任务3下载完成标识
	 */
	public void setDownTask3Success(boolean task3) {
		editor.putBoolean("task3", task3);
		editor.commit();
	}

	public boolean getTask3() {
		return sp.getBoolean("task3", false);
	}

	/**
	 * 揽收更新时间
	 */
	public void setCollectionTime(String time) {
		editor.putString("collectionTime", time);
		editor.commit();
	}

	public String getCollectionTime() {
		return sp.getString("collectionTime", "");
	}

	/**
	 * 投递更新时间
	 */
	public void setDeliverTime(String time) {
		editor.putString("deliverTime", time);
		editor.commit();
	}

	public String getDeliverTime() {
		return sp.getString("deliverTime", "");
	}

	/**
	 * 签到
	 */
	public void setSignin(boolean isSignin) {
		editor.putBoolean("isSignin", isSignin);
		editor.commit();
	}

	public boolean getSignin() {
		return sp.getBoolean("isSignin", false);
	}

	/**
	 * 蓝牙地址
	 */
	public void setBlueTooth(String bluetooth_address) {
		editor.putString("bluetooth", bluetooth_address);
		editor.commit();
	}

	public String getBlueTooth() {
		return sp.getString("bluetooth", "");
	}

	/**
	 * 蓝牙地址
	 */
	public void setServiceAddress(String bluetooth_address) {
		editor.putString("SAddress", bluetooth_address);
		editor.commit();
	}

	public String getServiceAddress() {
		return sp.getString("SAddress", "");
	}
	/**
	 * 绑定机构号
	 */
	public void setOrgCode(String orgCode) {
		editor.putString("orgcode", orgCode);
		editor.commit();
	}

	public String getOrgCode() {
		return sp.getString("orgcode", "");
	}
}

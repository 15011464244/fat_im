package com.newcdc.version;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.newcdc.R;

/**
 * 
 * @author 
 * @since 2014_12_2
 *
 */
public class CurrentVersion {
	private static String tag = "Config";
	public static final String appPackageName = "com.newcdc";// 应用的包名

	/**
	 * 获取当前的版本号
	 * 
	 * @param 上下文
	 * @return 版本号
	 */
	public static int getVersionCode(Context context) {
		int versionCode = -1;
		String packageName = context.getPackageName();
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(tag, e.getMessage());
		}
		return versionCode;
	}

	/**
	 * 获取当前的版本名称
	 * 
	 * @param 上下文
	 * @return 版本名称
	 */
	public static String getVersinName(Context context) {
		String versionName = "";
		String packageName = context.getPackageName();
		try {
			versionName = context.getPackageManager().getPackageInfo(
					packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(tag, e.getMessage());
		}
		return versionName;
	}

	/**
	 * 获取应用名称
	 * 
	 * @param 上下文
	 * @return 应用名称
	 */
	public static String getAppName(Context context) {
		String appName = "";
		appName = context.getResources().getText(R.string.app_name).toString();
		return appName;
	}

}

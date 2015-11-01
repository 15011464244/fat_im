package com.ems.express.util;

import com.ems.express.App;

import android.util.Log;

public class UtilMethod {
	private static long lastClickTime;
	
	/**
	 * 设备是否读到SD卡
	 * 
	 * @return true 能读到SD卡 false无法读到SD卡
	 */
	public static boolean checkSDCard() {
		String strStorageState = android.os.Environment
				.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(strStorageState)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 防止点击时间连续点击
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
//			Log.e(TAG, "isFastDoubleClick:"+true);
			App.mToastUtil.show("请稍候长按");
			return true;
		}
		App.mToastUtil.show("请稍候长按");
//		Log.e(TAG, "isFastDoubleClick:"+false);
		lastClickTime = time;
		return false;
	}
	
}

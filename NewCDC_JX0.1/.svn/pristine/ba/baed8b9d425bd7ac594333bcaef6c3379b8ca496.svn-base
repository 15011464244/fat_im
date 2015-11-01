package com.newcdc.chat.util;

public class UtilMethod {

	
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
	
}

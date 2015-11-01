package com.newcdc.chat.util;

import android.os.Environment;

public class ConfigData {

	public static String ENV_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/";

	// 程序根目录
	public static String DATA_PATH = "/data/data/com.ems.logistics" + "/";

	// 存放在SD卡上
	public static String SDCARD_PATH = ENV_PATH + "logistics/";

	// 存放在系统根目录上
	public static String STORAGE_PATH = DATA_PATH + "logistics/";

	// 菜单图片路径
	public static String ASNAME = "logistics/";

//	public static String SOURCE = "";// 自己的 2a1c7e52894b333c82833398375b9235
//	public static String TARGET = "607d653ab5a33669b3e8838b3cb76bf2";// 对方的

}

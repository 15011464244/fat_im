package com.newcdc.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

/**
 * 
 * 作者:	
 * 业务名:获取手机相关信息
 * 功能说明: 获取手机信息
 * 编写日期:	2014-11-19
 *
 */
public class PhoneInfoUtils {
	/**
	 * 檢查網路是否有開啟
	 */
	public static boolean isNetworkOpen(Context ctx) {
		if (ctx != null) {
			ConnectivityManager connectivity = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * ----------------------------------获取ip地址--------------------------------
	 * ---
	 * 
	 * @return
	 */
	public static String getIpAddrress(Context context) {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 检查Wifi状态
		if (!wm.isWifiEnabled()) {
			return getLocalIpAddress();
		}
		WifiInfo wi = wm.getConnectionInfo();
		// 获取32位整型IP地址
		int ipAdd = wi.getIpAddress();
		// 把整型地址转换成“*.*.*.*”地址
		String ip = intToIp(ipAdd);
		return ip;
	}

	private static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
//			MLog.e("get ip", ex.toString());
		}
		return null;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	public static String callCmd(String cmd, String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);
			// 执行命令cmd，只取结果中含有filter的这一行
			while ((line = br.readLine()) != null
					&& line.contains(filter) == false) {
				// result += line;
				// MLog.i("test","line: "+line);
			}
			result = line;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {

		/*
		 * String macAddress = getWifiMacAddress(context); if (macAddress ==
		 * null) { macAddress = getWlan0Mac(); } if (macAddress == null) {
		 * macAddress = getDeviceMacAddress(context); }
		 */
		String macAddress = null;
		macAddress = getDeviceMacAddress(context);
		return macAddress;

		/*
		 * 不好使 String macAddress = "000000000000"; try { WifiManager wifiMgr =
		 * (WifiManager) context .getSystemService(Context.WIFI_SERVICE);
		 * WifiInfo info = (null == wifiMgr ? null : wifiMgr
		 * .getConnectionInfo()); if (null != info) { if
		 * (!TextUtils.isEmpty(info.getMacAddress())) macAddress =
		 * info.getMacAddress().replace(":", ""); else return macAddress; } }
		 * catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); return macAddress; } return macAddress;
		 */

		/*
		 * String macSerial = null; String str = ""; try { Process pp =
		 * Runtime.getRuntime().exec( "cat /sys/class/net/wlan0/address ");
		 * InputStreamReader ir = new InputStreamReader(pp.getInputStream());
		 * LineNumberReader input = new LineNumberReader(ir);
		 * 
		 * 
		 * for (; null != str;) { str = input.readLine(); if (str != null) {
		 * macSerial = str.trim();// 去空格 break; } } } catch (IOException ex) {
		 * // 赋予默认值 ex.printStackTrace(); } return macSerial;
		 */
	}

	/**
	 * 取得 Wifi Mac-Address
	 * 
	 * @param activity
	 * @return
	 */
	public static String getWifiMacAddress(Context context) {
		String macAddress = null;
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if (wifiInfo != null) {
				// macAddress = wifiInfo.getMacAddress();
				macAddress = wifiInfo.getBSSID();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macAddress;
	}

	/**
	 * 获取wlan0网卡的mac地址
	 * 
	 * @return
	 */
	private static String getWlan0Mac() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec(
					"cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;
	}

	private static String getDeviceMacAddress(Context context) {
		String result = "";
		String Mac = "";
		result = callCmd("busybox ifconfig", "HWaddr");
		// 如果返回的result == null，则说明网络不可取
		if (result == null) {
//			return context.getResources()
//					.getString(R.string.no_get_mac_address);
		}
		// 对该行数据进行解析
		// 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
		if (result.length() > 0 && result.contains("HWaddr") == true) {
			Mac = result.substring(result.indexOf("HWaddr") + 6,
					result.length() - 1);
			if (Mac.length() > 1) {
				Mac = Mac.replaceAll(" ", "");
			}
		}
		return Mac;
	}

	/**
	 * 获取系统类型,这里肯定是android了
	 */
	public static String getOsType() {
		return "Android";
	}

	/**
	 * 获取包名
	 * 
	 * @param context
	 * @return
	 */
	public static String getPackName(Context context) {
		return context.getPackageName();
	}

	/**
	 * 获取系统版本
	 */
	public static String getOsVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取系统SDK版本
	 */
	public static String getOsSdkVer() {
		return Build.VERSION.SDK;
	}

	/** 获取系统CPU */
	public static String getOsCPU() {
		return android.os.Build.CPU_ABI;
	}

	/**
	 * 获取设备名称,就是手机型号
	 */
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 手机生产商,就是手机品牌
	 */
	public static String getDeviceBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 获取手机语言
	 * 
	 * @return
	 */
	public static String getLanguage() {
		return Locale.getDefault().toString();
	}

	/**
	 * 获取手机时区
	 */
	public static String getPhoneTimeZone() {
		return TimeZone.getDefault().getID();
	}

	/**
	 * 获取手机时区
	 */
	public static String getPhoneTimeZoneRawOffset() {
		return TimeZone.getDefault().getRawOffset() + "";
	}

	/**
	 * 获取手机号码
	 */
	public static String getPhoneNum(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// String phoneNum;
		// return (phoneNum = tm.getLine1Number()) == null?"":phoneNum;
		return tm.getLine1Number();
	}

	/**
	 * 获取手机唯一标识符
	 * 
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId() == null ? "0" : tm.getDeviceId();
	}

	/**
	 * 获取手机SIM卡用户id
	 * 
	 * @return
	 */
	public static String getIMSI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId() == null ? "0" : tm.getSubscriberId();
	}

	/**
	 * 获取手机SIM卡串号
	 * 
	 * @return
	 */
	public static String getICCID(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimSerialNumber() == null ? "0" : tm.getSimSerialNumber();
	}

	private static int screenWidth;
	private static int screenHeight;

	/**
	 * 解析手机屏幕信息,在可以横竖屏切换的手机需要在onConfigurationChanged的方法中调用
	 */
	@SuppressWarnings("deprecation")
	public static void parseScreenInfo(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		try {
			Class<Display> cls = Display.class;
			Method method = cls.getMethod("getRotation");
			Object retobj = method.invoke(display);
			int rotation = Integer.parseInt(retobj.toString());
			// 判断横屏还是屏
			if (Surface.ROTATION_0 == rotation
					|| Surface.ROTATION_180 == rotation) {
				screenWidth = display.getWidth();
				screenHeight = display.getHeight();
			} else {
				screenWidth = display.getHeight();
				screenHeight = display.getWidth();
			}
		} catch (Exception e) {
			if (display.getOrientation() == 1) {
				screenWidth = display.getHeight();
				screenHeight = display.getWidth();
			} else {
				screenWidth = display.getWidth();
				screenHeight = display.getHeight();
			}
		}
	}

	/**
	 * 获得屏幕宽度
	 */
	public static int getWidth() {

		if (screenWidth == 0) {
			Log.e("PhoneInfoUtils", "您需要调用 parseScreenInfo()后才能获得宽度");
		}
		return screenWidth;
	}

	/**
	 * 获得屏幕高度
	 */
	public static int getHeight() {
		if (screenHeight == 0)
			Log.e("PhoneInfoUtils", "您需要调用 parseScreenInfo()后才能获得高度");
		return screenHeight;
	}

	// =======================================================

	/*	*//**
	 * 取得 有线地址
	 * 
	 * @return
	 */
	/*
	 * private static String getEthernetMacAddress() { String macAddress = null;
	 * try { macAddress = loadFileAsString("/sys/class/net/eth0/address")
	 * .toUpperCase().substring(0, 17); } catch (Exception e) {
	 * e.printStackTrace(); } return macAddress; }
	 *//**
	 * 讀取檔案
	 * 
	 * @param filePath
	 * @return
	 * @throws java.io.IOException
	 */
	/*
	 * private static String loadFileAsString(String filePath) throws
	 * java.io.IOException { StringBuffer fileData = new StringBuffer(1000);
	 * BufferedReader reader = new BufferedReader(new FileReader(filePath));
	 * char[] buf = new char[1024]; int numRead = 0; while ((numRead =
	 * reader.read(buf)) != -1) { String readData = String.valueOf(buf, 0,
	 * numRead); fileData.append(readData); } reader.close(); return
	 * fileData.toString(); }
	 */

	/**
	 * 获取当前网络连接，wifi为0，其他为大于0 ；无网络连接时state为-1 *
	 * 
	 * @param context
	 */
	public static int getNetStates(Context context) {
		// 联网方式，0 wifi, 1 3G, 2 cmnet, 3 cmwap
		// 无网络连接时为-1
		int states = 0;

		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connec.getActiveNetworkInfo();
		// 获得系统服务，从而取得sim数据
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 获得手机SIMType
		if (tm != null)
			if (info != null) {
				if (info.getType() == ConnectivityManager.TYPE_WIFI) {
					states = 0;
				} else {
					if (info.getExtraInfo() != null)
						if (info.getExtraInfo() != null
								&& info.getExtraInfo().toLowerCase()
										.indexOf("wap") != -1) {
							states = 3;
						} else if (info.getExtraInfo() != null
								&& info.getExtraInfo().toLowerCase()
										.equals("cmnet")) {
							states = 2;
						} else {
							// 其它
							states = 1;
						}
				}
			} else {
				// 无网络可用
				states = -1;
			}
		return states;
	}

}

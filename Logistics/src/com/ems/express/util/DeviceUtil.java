package com.ems.express.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class DeviceUtil {
	private static DeviceUtil deviceUtil = new DeviceUtil();
	static TelephonyManager mTelephonyMgr;
	static ConnectivityManager connectManager;
	
	private DeviceUtil(){}
	
	public static DeviceUtil getInstance(){
		return deviceUtil;
	}
	//获取手机的IMEI
	public static String getDeviceNo(Context mContext){
		mTelephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getDeviceId();
	}
	//检测手机网络
	public static boolean isNetworkAvailable(Context context) {   
		connectManager = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (connectManager == null) {  
        	
        } else {
            NetworkInfo[] info = connectManager.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                        return true;   
                    } 
                }
            }
        }
        return false;
	}
	

}

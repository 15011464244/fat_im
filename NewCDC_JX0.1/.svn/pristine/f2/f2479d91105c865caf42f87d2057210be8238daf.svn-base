package com.newcdc.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.newcdc.asynctask.TestServiceNetStateAsync;
import com.newcdc.tools.Utils;

/**
 * 监听网络状态
 * 
 * @author hanrong
 */
public class TestServiceNetStateService extends Service {
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	public static boolean serviceState = true;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (!serviceState) {
					if (info != null && info.isAvailable()) {
						// String name = info.getTypeName();
						int netState = info.getType();
						switch (netState) {
						case ConnectivityManager.TYPE_WIFI:// WIFI
							serviceState = true;
							Log.e("tag", "TYPE_WIFI");
							new TestServiceNetStateAsync(getApplicationContext(),null).execute();
							break;
						case ConnectivityManager.TYPE_MOBILE:
							serviceState = true;
							Log.e("tag", "TYPE_MOBILE");
							new TestServiceNetStateAsync(getApplicationContext(),null).execute();
							// String mobileName = info.getSubtypeName();
							// int mobileType = info.getSubtype();
							// Log.e("tag", "手机网状态-" + mobileType);
							// switch (mobileType) {
							// case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
							// case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
							// case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
							// case TelephonyManager.NETWORK_TYPE_1xRTT:
							// case TelephonyManager.NETWORK_TYPE_IDEN:
							// break;
							// case TelephonyManager.NETWORK_TYPE_EVDO_A: //
							// 电信3g
							// case TelephonyManager.NETWORK_TYPE_UMTS:
							// case TelephonyManager.NETWORK_TYPE_EVDO_0:
							// case TelephonyManager.NETWORK_TYPE_HSDPA:
							// case TelephonyManager.NETWORK_TYPE_HSUPA:
							// case TelephonyManager.NETWORK_TYPE_HSPA:
							// case TelephonyManager.NETWORK_TYPE_EVDO_B:
							// case TelephonyManager.NETWORK_TYPE_EHRPD:
							// case TelephonyManager.NETWORK_TYPE_HSPAP:
							// break;
							// case TelephonyManager.NETWORK_TYPE_LTE: // 4G
							// netStateinServer = true;
							// new TestServiceNetStateAsync().execute();
							// break;
							// }
						default:
							break;
						}

					}
				}
			} else {
				Log.e("tag", "网络连接状态--网络断开");
			}
		}
	};

}

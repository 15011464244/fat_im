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
 * @author zhangfan
 * 
 */
public class ListenNetStateService extends Service {
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	public static boolean netStateinServer = false;
	public static boolean restartLocaService = false;
	public static boolean serviceState = false;

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
				// if (!serviceState) {
				// if (info != null && info.isAvailable()) {
				// // String name = info.getTypeName();
				// int netState = info.getType();
				// switch (netState) {
				// case ConnectivityManager.TYPE_WIFI:// WIFI
				// serviceState = true;
				// new TestServiceNetStateAsync(null).execute();
				// break;
				// case ConnectivityManager.TYPE_MOBILE:
				// serviceState = true;
				// new TestServiceNetStateAsync(null).execute();
				// // String mobileName = info.getSubtypeName();
				// // int mobileType = info.getSubtype();
				// // Log.e("tag", "手机网状态-" + mobileType);
				// // switch (mobileType) {
				// // case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
				// // case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
				// // case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
				// // case TelephonyManager.NETWORK_TYPE_1xRTT:
				// // case TelephonyManager.NETWORK_TYPE_IDEN:
				// // break;
				// // case TelephonyManager.NETWORK_TYPE_EVDO_A: //
				// // 电信3g
				// // case TelephonyManager.NETWORK_TYPE_UMTS:
				// // case TelephonyManager.NETWORK_TYPE_EVDO_0:
				// // case TelephonyManager.NETWORK_TYPE_HSDPA:
				// // case TelephonyManager.NETWORK_TYPE_HSUPA:
				// // case TelephonyManager.NETWORK_TYPE_HSPA:
				// // case TelephonyManager.NETWORK_TYPE_EVDO_B:
				// // case TelephonyManager.NETWORK_TYPE_EHRPD:
				// // case TelephonyManager.NETWORK_TYPE_HSPAP:
				// // break;
				// // case TelephonyManager.NETWORK_TYPE_LTE: // 4G
				// // netStateinServer = true;
				// // new TestServiceNetStateAsync().execute();
				// // break;
				// // }
				// default:
				// break;
				// }
				//
				// }
				// }

				Utils.startIntentService(getApplicationContext());
				if (restartLocaService) {
					Intent locService = new Intent(getApplicationContext(),
							LocationService.class);
					stopService(locService);
					startService(locService);
					restartLocaService = false;
				}
				if (!netStateinServer) {
					netStateinServer = true;
					// Toast.makeText(getApplicationContext(), "网络连接成功，正在同步数据",
					// Toast.LENGTH_SHORT).show();
					Utils.startFindTaskMailService(context);
					// Intent downDCDataService = new Intent(
					// getApplicationContext(), DownDCDataService.class);
					// stopService(downDCDataService);
					// startService(downDCDataService);
				}
			} else {
				Log.e("tag", "网络连接状态--网络断开");
				if (!netStateinServer) {
					Toast.makeText(getApplicationContext(), "您的网络有问题哦",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

}

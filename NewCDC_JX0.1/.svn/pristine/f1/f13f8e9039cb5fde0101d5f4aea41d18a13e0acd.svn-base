package com.newcdc.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.gicom.device.SerialPortFactory;
import com.gicom.device.Bluetooth.BluetoothList;
import com.gicom.device.Bluetooth.BluetoothList.BluetoothSuccess;
import com.gicom.device.Bluetooth.BluetoothSerialPortInfo;
import com.gicom.device.Bluetooth.SerialPort;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.newcdc.R;
import com.newcdc.application.MainActivity;
import com.newcdc.fragment.MainFragment;
import com.newcdc.tools.BDPushUtils;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;

public class ResetPushServise extends Service {
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				MainFragment.titleInfomation = null;
				PushManager
						.startWork(ResetPushServise.this,
								PushConstants.LOGIN_TYPE_API_KEY, BDPushUtils
										.getMetaValue(ResetPushServise.this,
												"api_key"));
				SpeechUtility.createUtility(ResetPushServise.this,
						SpeechConstant.APPID + getString(R.string.app_id));
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 10, 1000 * 60 * 5);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent localIntent = new Intent();
		localIntent.setClass(this, ResetPushServise.class); // 销毁时重新启动Service
		this.startService(localIntent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

}

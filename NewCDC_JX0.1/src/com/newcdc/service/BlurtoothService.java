package com.newcdc.service;

import com.gicom.device.SerialPortFactory;
import com.gicom.device.Bluetooth.BluetoothCall;
import com.gicom.device.Bluetooth.BluetoothSerialPortInfo;
import com.gicom.device.Bluetooth.SerialPort;
import com.newcdc.activity.bluetooth.BluetoothSetActivity;
import com.newcdc.tools.Constant;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author 蓝牙枪监听初始化服务 2015-3-30,上午10:40:54
 * hanrong
 */
public class BlurtoothService extends Service {
	private SharePreferenceUtilDaoFactory shareDao;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("tag", "BlurtoothService启动");
		SerialPort serialPort = SerialPortFactory.getInstance();
		shareDao = SharePreferenceUtilDaoFactory
				.getInstance(getApplicationContext());
		if (serialPort == null || !serialPort.run) {
			Toast.makeText(getApplicationContext(), "请先设置蓝牙连接",
					Toast.LENGTH_SHORT).show();
			super.onStartCommand(intent, flags, startId);
		} else {
			serialPort.setReadCode(null);
			serialPort.setReadCode(call);
			Log.e("tag", "BlurtoothService--初始化蓝牙");
			try {
				serialPort.Start();
				Log.e("tag", "BlurtoothService--chenggong");
			} catch (Exception e) {
				Log.e("tag", "BlurtoothService-shibai "); 
				initBluetooth();
				Toast.makeText(getApplicationContext(), "启动蓝牙扫描失败",
						Toast.LENGTH_SHORT).show();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 初始化蓝牙枪监听
	 */
	private void initBluetooth() {
		SerialPort serialPort = SerialPortFactory.getInstance();
		shareDao = SharePreferenceUtilDaoFactory
				.getInstance(getApplicationContext());
		if (serialPort == null || !serialPort.run) {
			Toast.makeText(getApplicationContext(), "请先设置蓝牙连接",
					Toast.LENGTH_SHORT).show();
		} else {
			serialPort.setReadCode(null);
			serialPort.setReadCode(call);
			Log.e("tag", "--初始化蓝牙");
			try {
				serialPort.Start();
				Log.e("tag", "-kaishi");
			} catch (Exception e) {
				initBluetooth();
				Toast.makeText(getApplicationContext(), "启动蓝牙扫描失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 回调函数
	 * */
	BluetoothCall call = new BluetoothCall() {

		@Override
		public void ReadCode(String code) {
			// 此处获取的是条码的值
//			Toast.makeText(getApplicationContext(), code, Toast.LENGTH_SHORT)
//					.show();
			Intent bluetooth_Intent = new Intent(Constant.ACTION_BLUTTOOTH_MSG);
			bluetooth_Intent.putExtra("code", code);
			Log.e("tag", "蓝牙枪监听--返回值了。发送广播");
			sendBroadcast(bluetooth_Intent);
			// txtCodes.setText(txtCodes.getText().toString() + code + "\n");
		}
	};

}

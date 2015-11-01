package com.newcdc.service;

import java.util.Timer;
import java.util.TimerTask;

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
 * @author  蓝牙枪自动连接服务 2015-3-30,上午10:40:54
 * 
 */
public class AutoBlurtoothonnectionService extends Service {
	private SharePreferenceUtilDaoFactory shareDao;
	private Intent bluetoothService;// 蓝牙扫描枪监听服务
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		return super.onStartCommand(intent, flags, startId);
	}

	

	@Override
	public void onCreate() {
		super.onCreate();
		shareDao = SharePreferenceUtilDaoFactory
				.getInstance(getApplicationContext());
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				connectBluetooth();
			}
		}, 10, 1000 * 60 * 5);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 蓝牙连接
	 */
	private void connectBluetooth() {
		Log.e("tag", "AutoBlurtoothonnectionService==自动连接连蓝牙服务");
		if (!"".equals(shareDao.getBlueTooth())) {
			BluetoothSerialPortInfo serialInfo = new BluetoothSerialPortInfo();
			serialInfo.mac = shareDao.getBlueTooth();
			SerialPort serialPort = SerialPortFactory.getInstance(serialInfo);
			if (!serialPort.run) {
				try {
					serialPort.Start();
					shareDao.setBlueTooth(shareDao.getBlueTooth());
					bluetoothService = new Intent(getApplicationContext(),
							BlurtoothService.class);
					startService(bluetoothService);
					Log.e("tag", "chenggong");
				} catch (Exception e) {
					Log.e("tag", "shibai");
				}
			}

		}
	}

}

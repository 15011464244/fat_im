/**
 * 
 */
package com.newcdc.asynctask;

import com.gicom.device.SerialPortFactory;
import com.gicom.device.Bluetooth.BluetoothSerialPortInfo;
import com.gicom.device.Bluetooth.SerialPort;
import com.newcdc.application.MainActivity;
import com.newcdc.asynctask.TestServiceNetStateAsync.onPostExecuteListener_NetState;
import com.newcdc.service.BlurtoothService;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author hanrong 2015-3-30,上午10:26:16
 * 
 */
public class BluetoothAsync extends AsyncTask<String, String, String> {
	private onPostExecuteListener_connBlue onPostExecuteListener_connBlue;
	private SharePreferenceUtilDaoFactory shareDao;
	private Intent bluetoothService;// 蓝牙扫描枪监听服务
	private Context context;

	public BluetoothAsync(Context context,
			onPostExecuteListener_connBlue onPostExecuteListener_connBlue) {
		super();
		this.context = context;
		shareDao = SharePreferenceUtilDaoFactory
				.getInstance(context);
		this.onPostExecuteListener_connBlue = onPostExecuteListener_connBlue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... params) {
		if (!"".equals(shareDao.getBlueTooth())) {
			Log.e("tag", "shareDao.getBlueTooth()="+shareDao.getBlueTooth());
			BluetoothSerialPortInfo serialInfo = new BluetoothSerialPortInfo();
			serialInfo.mac = shareDao.getBlueTooth();
			SerialPort serialPort = SerialPortFactory.getInstance(serialInfo);
			// serialPort.setReadCode(call);
//			Log.e("tag", "连接前run" + serialPort.run);
			try {
				serialPort.Start();
				shareDao.setBlueTooth(shareDao.getBlueTooth());
				// Toast.makeText(MainActivity.this, "连接成功",
				// Toast.LENGTH_SHORT)
				// .show();
				// BluetoothSetActivity.this.setResult(200);
				// Intent mainIntent = new Intent("bluetooth");
				// sendBroadcast(mainIntent);
				// BluetoothSetActivity.this.finish();
				bluetoothService = new Intent(this.context, BlurtoothService.class);
				this.context.startService(bluetoothService);
//				Log.e("tag", "连接后run" + serialPort.run);
				return "1";
			} catch (Exception e) {
				// Toast.makeText(MainActivity.this, "连接失败",
				// Toast.LENGTH_SHORT)
				// .show();
				return "0";
			}
		}
		return "2";
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub.
		Log.e("tag", "BluetoothAsync="+result);
		if (onPostExecuteListener_connBlue != null) {
			onPostExecuteListener_connBlue.onPostExecute(result);
		}
		super.onPostExecute(result);
	}

	public interface onPostExecuteListener_connBlue {
		void onPostExecute(String result);
	}
}

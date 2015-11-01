package com.newcdc.activity.bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.gicom.device.SerialPortFactory;
import com.gicom.device.Bluetooth.BluetoothList;
import com.gicom.device.Bluetooth.BluetoothList.BluetoothSuccess;
import com.gicom.device.Bluetooth.BluetoothSerialPortInfo;
import com.gicom.device.Bluetooth.SerialPort;
import com.newcdc.R;
import com.newcdc.adapter.BluetoothSetAdapter;
import com.newcdc.model.BluetoothMessageBean;
import com.newcdc.service.AutoBlurtoothonnectionService;
import com.newcdc.service.BlurtoothService;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.ui.ProgressDialog;
/**
 * @author hanrong  2015-3-31,下午6:56:06
 * 蓝牙搜索连接
 */
public class BluetoothSetActivity extends Activity {
	BluetoothList bluetooths;
	ListView lstBluetooths;
	// private ArrayAdapter<String> mDevicesArrayAdapter;
	private SharePreferenceUtilDaoFactory shareDao;
	private BluetoothSetAdapter adapter;
	private List<BluetoothMessageBean> mBluetoothMessageBeans = null;
	private ImageView mbtn_back_bluetooth;
	private ProgressDialog mProgressDialog; 
	private Intent autobluetoothService;// 蓝牙枪自动连接服务
	// MyReceiver uploadReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_setting);
		// uploadReceiver = new MyReceiver();
		shareDao = SharePreferenceUtilDaoFactory
				.getInstance(getApplicationContext());
		Init();
	}

	protected void Init() {
//		dialog = new ProgressDialog(this);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
		mProgressDialog = new ProgressDialog(BluetoothSetActivity.this, "正在搜索...");
		mProgressDialog.toShow();
		lstBluetooths = (ListView) this.findViewById(R.id.lst_bluetooths);
		mbtn_back_bluetooth = (ImageView) findViewById(R.id.btn_back_bluetooth);
		mBluetoothMessageBeans = new ArrayList<BluetoothMessageBean>();
		adapter = new BluetoothSetAdapter(BluetoothSetActivity.this,
				mBluetoothMessageBeans);
		lstBluetooths.setAdapter(adapter);
		lstBluetooths.setOnItemClickListener(itemClick);
		mbtn_back_bluetooth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BluetoothSetActivity.this.finish();
			}
		});
		bluetooths = new BluetoothList(this);
		bluetooths.setBluetoothSuccess(new BluetoothSuccess() {

			@Override
			public void Success(Map<String, String> map) {
				for (String key : map.keySet()) {
					BluetoothMessageBean bean = new BluetoothMessageBean();
					bean.setBluetooth_name(map.get(key));
					bean.setBluetooth_address(key);
					mBluetoothMessageBeans.add(bean);
					adapter.notifyDataSetChanged();
					// mDevicesArrayAdapter.add(map.get(key) + "\n" + key);
					closeDialog();
				}
			}
		});
		try {
			bluetooths.StartBluetooth();
		} catch (Exception e) {
			closeDialog();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}

	}

	private void closeDialog() {
		if (mProgressDialog != null)
			mProgressDialog.toDimiss();
	}

	private OnItemClickListener itemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
//			dialog = new ProgressDialog(BluetoothSetActivity.this);
//			dialog.setCanceledOnTouchOutside(false);
//			dialog.show();
//			mProgressDialog = new ProgressDialog(BluetoothSetActivity.this, "正在连接...");
//			mProgressDialog.toShow();
			// 准备连接设备，关闭服务查找
			bluetooths.cancelDiscovery();

			// 得到mac地址
			
//			String info = ((TextView) v).getText().toString();
			String address = mBluetoothMessageBeans.get(position).getBluetooth_address();
			BluetoothSerialPortInfo serialInfo = new BluetoothSerialPortInfo();
			serialInfo.mac = address;
			SerialPort serialPort = SerialPortFactory.getInstance(serialInfo);
			// serialPort.setReadCode(call);
			try {
				serialPort.Start();
				shareDao.setBlueTooth(address);
				Toast.makeText(BluetoothSetActivity.this, "连接成功",
						Toast.LENGTH_SHORT).show();
				Intent mIntent = new Intent(BluetoothSetActivity.this,
						BlurtoothService.class);
				startService(mIntent);
				autobluetoothService = new Intent(getApplicationContext(),
						AutoBlurtoothonnectionService.class);
				startService(autobluetoothService);
			} catch (Exception e) {
				Toast.makeText(BluetoothSetActivity.this, "连接失败",
						Toast.LENGTH_SHORT).show();
			} 

		}
	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}

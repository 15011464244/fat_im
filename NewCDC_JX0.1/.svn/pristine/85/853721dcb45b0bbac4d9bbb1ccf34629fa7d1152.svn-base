package com.newcdc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.db.CustomerDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.BluetoothMessageBean;
import com.newcdc.model.MoneyBean;

/**
 * @author hanrong
 * @version 类说明 ：蓝牙设备adapter
 */
public class BluetoothSetAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private Context context;
	List<BluetoothMessageBean> mBluetoothMessageBeans = null;

	public BluetoothSetAdapter(Context context,
			List<BluetoothMessageBean> mBluetoothMessageBeans) {
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.mBluetoothMessageBeans = mBluetoothMessageBeans;
	}

	@Override
	public int getCount() {
		return mBluetoothMessageBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return mBluetoothMessageBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.blue_list_item, null);
			holder.bluetooth_name = (TextView) convertView
					.findViewById(R.id.bluetooth_name);
			holder.bluetooth_address = (TextView) convertView
					.findViewById(R.id.bluetooth_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.bluetooth_name.setText(mBluetoothMessageBeans.get(position)
				.getBluetooth_name());
		holder.bluetooth_address.setText(mBluetoothMessageBeans.get(position)
				.getBluetooth_address());
		return convertView;
	}

	public static class ViewHolder {
		TextView bluetooth_name;// 蓝牙名称
		TextView bluetooth_address;// 蓝牙地址
	}

}

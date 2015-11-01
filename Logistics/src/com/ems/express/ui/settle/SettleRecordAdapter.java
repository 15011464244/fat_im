package com.ems.express.ui.settle;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.bean.SettleRecordBean;

/**
 * 理赔记录 Adapter
 */
// TODO AdapterTemplate->Adapter name
// TODO Bean-> Adapter data
public class SettleRecordAdapter extends BaseAdapter {
	private List<SettleRecordBean> mData;
	private LayoutInflater mInflater;
	private Context mContext;

	public SettleRecordAdapter(Context context, List<SettleRecordBean> mData) {
		super();
		this.mData = mData;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
	}

	/**
	 * Update all the data
	 * 
	 * @param mData
	 *            the new data of list
	 */
	public void updateData(List<SettleRecordBean> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	/**
	 * add a list of data to adapter's data
	 * 
	 * @param mData
	 *            a list of data
	 */
	public void append(List<SettleRecordBean> mData) {
		this.mData.addAll(mData);
		notifyDataSetChanged();
	}

	/**
	 * add a single data to adapter's data
	 * 
	 * @param mData
	 *            a single data
	 */
	public void append(SettleRecordBean mData) {
		this.mData.add(mData);
		notifyDataSetChanged();
	}

	/**
	 * clear all of Adapter's data
	 */
	public void clear() {
		this.mData.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public SettleRecordBean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_settle_record,
					null);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.money = (TextView) convertView.findViewById(R.id.money);
			holder.edit = (ImageView) convertView.findViewById(R.id.edit);

			convertView.setTag(holder);
		}
		holder.number.setText(mData.get(position).getNumber());
		holder.money.setText(mData.get(position).getMoney() + "元");
		return convertView;
	}

	class ViewHolder {
		TextView number;
		TextView money;
		ImageView edit;
	}

}

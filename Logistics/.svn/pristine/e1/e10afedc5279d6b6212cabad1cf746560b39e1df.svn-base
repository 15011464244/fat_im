package com.ems.express.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @Description: Adapter template
 * @author:Arvin
 * @time:2015年1月26日 上午11:41:24
 */
// TODO AdapterTemplate->Adapter name
// TODO Bean-> Adapter data
public class AdapterTemplate extends BaseAdapter {
	private List<Bean> mData;
	private LayoutInflater mInflater;
	private Context mContext;

	public AdapterTemplate(Context context, List<Bean> mData) {
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
	public void updateData(List<Bean> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	/**
	 * add a list of data to adapter's data
	 * 
	 * @param mData
	 *            a list of data
	 */
	public void append(List<Bean> mData) {
		this.mData.addAll(mData);
		notifyDataSetChanged();
	}

	/**
	 * add a single data to adapter's data
	 * 
	 * @param mData
	 *            a single data
	 */
	public void append(Bean mData) {
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
	public Bean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// TODO convertView = mInflater.inflate(R.layout...,null);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			// TODO findViewById
			convertView.setTag(holder);
		}
		// TODO set Data

		return convertView;
	}

	class ViewHolder {
	}

	class Bean {

	}
}

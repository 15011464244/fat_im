package com.ems.express.ui.check;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.bean.ExpressRecordBean;

/**
 * 查询结果 Adapter
 */
// TODO AdapterTemplate->Adapter name
// TODO Bean-> Adapter data
public class ResultAdapter extends BaseAdapter {
	private List<ExpressRecordBean> mData = new ArrayList<ExpressRecordBean>();
	private LayoutInflater mInflater;
	private Context mContext;
	
	private Boolean isFirst = true;

	public ResultAdapter(Context context, List<ExpressRecordBean> mData) {
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
	public void updateData(List<ExpressRecordBean> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	/**
	 * add a list of data to adapter's data
	 * 
	 * @param mData
	 *            a list of data
	 */
	public void append(List<ExpressRecordBean> mData) {
		this.mData.addAll(mData);
		notifyDataSetChanged();
	}

	/**
	 * add a single data to adapter's data
	 * 
	 * @param mData
	 *            a single data
	 */
	public void append(ExpressRecordBean mData) {
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
	public ExpressRecordBean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_result, null);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.date = (TextView) convertView.findViewById(R.id.tv_date);
//			holder.target = (TextView) convertView.findViewById(R.id.source_target);
			holder.lastInfo = (ImageView) convertView.findViewById(R.id.iv_last_info);
			holder.receiver = (TextView) convertView.findViewById(R.id.receiver);
			convertView.setTag(holder);
		}
		//首行样式改变
		if(index == 0){
			holder.time.setTextColor(Color.rgb(255, 165, 0));
			holder.date.setTextColor(Color.rgb(255, 165, 0));
			Drawable drawable=mContext.getResources().getDrawable(R.drawable.img_messagetag_noread);
			holder.lastInfo.setImageDrawable(drawable);
			holder.receiver.setTextColor(Color.rgb(255, 165, 0));
		}else{
			holder.time.setTextColor(Color.rgb(51, 51, 51));
			holder.date.setTextColor(Color.rgb(51, 51, 51));
			Drawable drawable=mContext.getResources().getDrawable(R.drawable.img_messagetag_readed);
			holder.lastInfo.setImageDrawable(drawable);
			holder.receiver.setTextColor(Color.rgb(51, 51, 51));
		}
		String dateTime = mData.get(index).getTime();
		String date = dateTime.split(" ")[0];
		String time = dateTime.split(" ")[1];
		
		holder.time.setText(time);
		holder.date.setText(date);
//		holder.time.setText(mData.get(index).getTime());
//		holder.target.setText("从" + mData.get(index).getTarget() + "的派件已经签收");
		holder.receiver.setText(mData.get(index).getWho());

		return convertView;
	}

	static class ViewHolder {
		TextView time;
		TextView date;
		TextView target;
		TextView receiver;
		//最新的消息
		ImageView lastInfo;
	}

}

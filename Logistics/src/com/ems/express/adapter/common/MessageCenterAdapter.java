package com.ems.express.adapter.common;

import java.util.List;

import com.ems.express.R;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.ui.BaiduMapActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageCenterAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<DeliveryMessageBean> mList;
	
	public void setmList(List<DeliveryMessageBean> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();
	}

	public MessageCenterAdapter(Context context,List<DeliveryMessageBean> list){
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		if(mList!=null){
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView ==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_center_query, null);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			holder.location = (ImageView) convertView.findViewById(R.id.location);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(mList!=null){
//			holder.time.setText(mList.get(position).getTime());
			holder.name.setText(mList.get(position).getPeople());
			holder.phone.setText(mList.get(position).getMobile());
			holder.location.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					ToastUtil.show(mContext, "快递员位置");
					Intent intent = new Intent(mContext, BaiduMapActivity.class);
					intent.putExtra(BaiduMapActivity.KEY_TYPE,
							BaiduMapActivity.TYPE_CARRIER);
					intent.putExtra("LONGITUDE", mList.get(position).getLongitude());
					intent.putExtra("LATITUDE", mList.get(position).getLatitude());
					intent.putExtra("phoneNum", mList.get(position).getMobile());
					intent.putExtra("deliveryMessage", mList.get(position));
					intent.putExtra("activity", "messageCenter");
					mContext.startActivity(intent);
				}
			});
		}
		return convertView;
	}

	class ViewHolder{
		TextView time;
		TextView name;
		TextView phone;
		ImageView location;
	}
	
}

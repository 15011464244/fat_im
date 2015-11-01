package com.ems.express.adapter.common;

import java.util.ArrayList;
import com.ems.express.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainCommonAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<String> mList;
	
	public MainCommonAdapter(Context context,ArrayList<String> list){
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView ==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.activity_main_common_item, null);
			holder.commonName = (TextView) convertView.findViewById(R.id.common_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(mList!=null){
			holder.commonName.setText(mList.get(position));
		}
		return convertView;
	}

	class ViewHolder{
		TextView commonName;
	}
	
}

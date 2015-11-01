package com.ems.express.adapter.message;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ems.express.R;

public class MailNewsAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<String> mList;
	private List<String> myList;
	
	public MailNewsAdapter(Context context,List<String> mylist,List<String> list ) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mList = list;
		this.myList = mylist;
	}

	@Override
	public int getCount() {
		if(mList != null){
			return mList.size();
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mail_news_item, null);
			
			holder.commonName = (TextView) convertView.findViewById(R.id.tv_news_item);
			holder.messageName = (TextView) convertView.findViewById(R.id.tv_news_below_item);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(mList != null){
		holder.commonName.setText("运单号:"+myList.get(position));
		}
		
		if(myList != null){
		holder.messageName.setText(mList.get(position));
		}
		
		return convertView;
	} 
	
	class ViewHolder{
		TextView commonName;
		TextView messageName;
	}
}

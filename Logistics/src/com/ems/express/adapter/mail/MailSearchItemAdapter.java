package com.ems.express.adapter.mail;

import java.util.List;

import com.ems.express.R;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.bean.MailInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MailSearchItemAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	List<SendNoticeBean> mails;
	
	public MailSearchItemAdapter(Context context,List<SendNoticeBean> mails) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mails = mails;
	}

	@Override
	public int getCount() {
		if(mails != null){
			return mails.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mail_follow_item, null);
			holder.commonName = (TextView) convertView.findViewById(R.id.tv_follow_item);
			holder.line = (View) convertView.findViewById(R.id.v_divline);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(mails != null){
				holder.commonName.setText("邮件号："+mails.get(index).getMailNum());
//				if(index == mails.size() - 1){
////					holder.line.setVisibility(View.GONE);
//				}
		}
		return convertView;
	}  
	
	class ViewHolder{
		TextView commonName;
		View line;
	}

	
}

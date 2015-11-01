package com.ems.express.ui.chat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.ChatListItemBean;
import com.ems.express.bean.ChatMessageBean;
import com.ems.express.net.UrlUtils;
import com.ems.express.ui.view.CircleImageView;
import com.ems.express.ui.view.CustomImageView;
import com.ems.express.util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChatListAdapter extends BaseAdapter {
	private List<ChatListItemBean> mData;
	private LayoutInflater mInflater;
	private Context mContext;
	public ChatListAdapter(Context context, List<ChatListItemBean> mData) {
		super();
		this.mData = mData;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
	}

	public void updateData(List<ChatListItemBean> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	public void append(List<ChatListItemBean> mData) {
		this.mData.addAll(mData);
		notifyDataSetChanged();
	}

	public void append(ChatListItemBean mData) {
		this.mData.add(mData);
		notifyDataSetChanged();
	}

	public void clear() {
		this.mData.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public ChatListItemBean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.activity_friend_list_item,
					null);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
//			holder.image = (CircleImageView) convertView.findViewById(R.id.image);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.message = (Button) convertView.findViewById(R.id.message);
			holder.message.setTag(position);
			holder.message.setOnClickListener(onClick);
			holder.call = (Button) convertView.findViewById(R.id.call);
			holder.call.setTag(position);
			holder.call.setOnClickListener(onClick);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			holder.messageView = (LinearLayout) convertView.findViewById(R.id.message_view);
			holder.messageCount = (TextView) convertView.findViewById(R.id.message_count);
			convertView.setTag(holder);
		}
		if(mData.size()>0){
			ChatListItemBean bean = mData.get(position);
			if(!bean.getImage().equals("null")&&!bean.getImage().equals("")){
				String url = UrlUtils.URL_CARRIER_IMG+"?sid=" + bean.getImage();
				LogUtil.print("获取头像地址："+url);
//				ImageLoader.getInstance().displayImage(url, holder.image);
				URL url2 = null;
				Bitmap pngBM = null;
				try {
					url2 = new URL(url);
					pngBM = BitmapFactory.decodeStream(url2.openStream());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				holder.image.setBackground(new BitmapDrawable(pngBM));
				
			}else{
				Bitmap aa = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.defualt_header);
//				holder.image.setImageBitmap(aa);
				holder.image.setBackground(new BitmapDrawable(aa));
			}
			if(bean.getUnRedCount()>0){
				holder.messageView.setVisibility(View.VISIBLE);
				holder.messageCount.setText(bean.getUnRedCount()+"");
			}else{
				holder.messageView.setVisibility(View.GONE);
				holder.messageCount.setText("");
			}
			holder.name.setText(bean.getName());
//			holder.phone.setText(bean.getMobile());
			//获取最后回话
			String employeeNo = App.dbHelper.queryEmployeeNoIsClientId(App.db,
					bean.getClientId());
			List<ChatMessageBean> list = App.dbHelper.queryAllMessage(App.db, employeeNo,
					  bean.getImage());
			ChatMessageBean chatMessageBean = null;
			if(list != null && list.size()>0){
				 chatMessageBean = list.get(list.size()-1);
			}else{
				chatMessageBean = new ChatMessageBean();
			}
			String type = chatMessageBean.getContentType();
			String msg = chatMessageBean.getContent();
			Bitmap msg2 = chatMessageBean.getContent_image();
			File msg3 = chatMessageBean.getContent_voice();
//			switch (Integer.valueOf(type)) {
//			case 1:
//				if(msg != null){
//					holder.phone.setText(msg);
//				}
//				break;
//			case 2:
////				if(msg2 != null){
//					holder.phone.setText("[图片]");
////				}		
//				break;
//			case 3:
////				if(msg3 != null){
//					holder.phone.setText("[语音]");
////				}
//				break;
//
//			default:
////				holder.phone.setText("没有消息");
//				break;
//			}
			if(msg != null && !"".equals(msg)){
				holder.phone.setText(msg);
			}else if(msg2 != null){
				holder.phone.setText("[图片]");
			}else if(msg3 != null){
				holder.phone.setText("[语音]");
			}else{
				holder.phone.setText("");
			}
		}
		
		
		return convertView;
	}

	
	public OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.message:
				int callnumber = (Integer) v.getTag();
				mData.get(callnumber).getMobile();
				Intent intent2 = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+mData.get(callnumber).getMobile()));
				mContext.startActivity(intent2);
				break;
			case R.id.call:
				int number = (Integer) v.getTag();
				mData.get(number).getMobile();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+mData.get(number).getMobile()));
				mContext.startActivity(intent);
				break;
			default:
				break;
			}
			
		}
	};
	
	class ViewHolder {
//		CircleImageView image;
		ImageView image;
		TextView name;
		TextView time,phone,messageCount;
		Button message,call;
		LinearLayout messageView;
	}
}

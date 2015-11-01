package com.newcdc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.model.MessageInfoBean;

public class NotCountNoticeAdapter extends BaseAdapter {
	private List<MessageInfoBean> queryByType;
	private Context context;

	public NotCountNoticeAdapter(List<MessageInfoBean> queryByType, Context context) {
		this.queryByType = queryByType;
		this.context = context;
	}

	@Override
	public int getCount() {
		return queryByType.size();
	}

	@Override
	public Object getItem(int position) {
		return queryByType.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fragment_count_notice_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textViewKeHu = (TextView) convertView
					.findViewById(R.id.fragment_count_item_kehu);
			viewHolder.textViewPhone = (TextView) convertView
					.findViewById(R.id.fragment_count_item_phone);
			viewHolder.textViewNumber = (TextView) convertView
					.findViewById(R.id.fragment_count_item_number);
			viewHolder.textViewAddress = (TextView) convertView
					.findViewById(R.id.fragment_count_item_address);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textViewKeHu.setText(queryByType.get(position)
				.getRcver_name().toString());
		viewHolder.textViewPhone.setText(queryByType.get(position)
				.getRcver_contact_phone1());
		viewHolder.textViewNumber.setText(queryByType.get(position)
				.getMail_num().toString());
		viewHolder.textViewAddress.setText(queryByType.get(position)
				.getRcver_street_addr());
		return convertView;
	}

	class ViewHolder {
		private TextView textViewKeHu, textViewPhone, textViewNumber,
				textViewAddress;
	}
}

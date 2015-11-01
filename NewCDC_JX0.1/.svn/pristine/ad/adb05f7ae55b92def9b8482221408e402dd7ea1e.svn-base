package com.newcdc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.newcdc.R;

/**
 * @author hanrong
 * @version 创建时间：2014-12-29 下午7:27:01 类说明 ：其他金额明细adapter
 */
public class OtherMoneyDetailAdapter extends BaseAdapter {
	private List<String> list = null;
	private LayoutInflater layoutInflater;
	private double otherMoney = 0.0 ;
	public OtherMoneyDetailAdapter(Context context, List<String> list) {
		layoutInflater = LayoutInflater.from(context);
		this.list = new ArrayList<String>();
		this.list.add("面单");
		this.list.add("包装");
		this.list.add("实收");
		this.list.add("面单");
		this.list.add("包装");
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
			convertView = layoutInflater.inflate(R.layout.othermoneydeteil_listview_item,
					null);
			holder.item_title = (TextView) convertView
					.findViewById(R.id.item_title);
			holder.item_money = (EditText) convertView.findViewById(R.id.item_money);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item_title.setText(list.get(position));
		if(!"".equals(holder.item_money.getText().toString().trim()) && holder.item_money.getText().toString().trim()!=null){
			otherMoney += Double.parseDouble(holder.item_money.getText().toString().trim());
		}
		return convertView;
	}
	
	public static class ViewHolder {
		TextView item_title;// 站点名称
		EditText item_money;
	}
	public double getOtherMoney() {
		return otherMoney;
	}

}

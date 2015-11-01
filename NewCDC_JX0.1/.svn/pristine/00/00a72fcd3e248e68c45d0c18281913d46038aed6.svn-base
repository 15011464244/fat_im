package com.newcdc.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.db.CustomerDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.MoneyBean;

/**
 * @author hanrong
 * @version 创建时间：2015-04-11 上午10:27:01 类说明 ：投递任务adapter
 */
public class DeliverTaskListAdapter extends BaseAdapter {
	private ArrayList<Map<String, String>> mList = null;
	private LayoutInflater layoutInflater;
	private Context context;

	public DeliverTaskListAdapter(Context context,
			ArrayList<Map<String, String>> mList) {
		layoutInflater = LayoutInflater.from(context);
		this.mList = mList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mList.size();
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
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_delivertasklist,
					null);
			// holder.task_num = (TextView) convertView
			// .findViewById(R.id.task_num);
			holder.task_name = (TextView) convertView
					.findViewById(R.id.task_name);
			holder.task_count = (TextView) convertView
					.findViewById(R.id.task_count);
			holder.red_num = (TextView) convertView.findViewById(R.id.red_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (0 == Integer.parseInt(mList.get(position).get("delivertaskcount"))) {
			holder.red_num.setBackgroundResource(R.drawable.defule_reader);
		} else {
			if (Integer.parseInt(mList.get(position).get("delivertaskcount")) > 99) {
				holder.red_num.setText("99+");
			} else {
				holder.red_num.setText(mList.get(position).get(
						"delivertaskcount"));
			}
			holder.red_num.setBackgroundResource(R.drawable.red_circle_small);
		}

		holder.task_name.setText(mList.get(position).get("delivertaskname"));
		holder.task_count.setText(mList.get(position)
				.get("delivertaskallcount"));
		return convertView;
	}

	public static class ViewHolder {
		TextView task_num; // 任务号
		TextView task_name; // 任务名称
		TextView task_count;// 个数
		TextView red_num; // 有新任务
	}

}

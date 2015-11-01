package com.newcdc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.activity.delivertask.TaskShowActivity;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.GroupBean;

/**
 * @author liunannan 分组pop上的adapter
 */
public class GroupPopAdapter extends BaseAdapter {
	private List<GroupBean> groups = null;
	private LayoutInflater layoutInflater;
	private Context context;
	private int select_item = -1;
	private DeliverDao deliverDao;

	public GroupPopAdapter(Context context, List<GroupBean> groups) {
		layoutInflater = LayoutInflater.from(context);
		this.groups = groups;
		this.context = context;
		deliverDao = DeliverDaoFactory.getInstance().getDeliverDao(context);
	}

	public void setnotifyDataSetChanged(List<GroupBean> groups) {
		this.groups = groups;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return groups.size();
	}

	@Override
	public Object getItem(int position) {
		return groups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public interface setOnPopWindowDismiss {
		void onPopwindowDismiss();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_group_pop, null);
			holder.tv_group_name = (TextView) convertView
					.findViewById(R.id.tv_group_name);
			holder.tv_group_num = (TextView) convertView
					.findViewById(R.id.tv_group_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_group_name.setText(groups.get(position).getGroup_content());
		holder.tv_group_num.setText(groups.get(position).getMailCount());
		this.select_item = TaskShowActivity.select_item;
		try {
			if (this.select_item == position) {
				holder.tv_group_name.setTextSize(30); // 选中的Item字体：30px
			} else
				holder.tv_group_name.setTextSize(15); // 未选中的Item字体：15px
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return convertView;
	}

	public static class ViewHolder {
		private TextView tv_group_name, tv_group_num;

	}

}

package com.newcdc.adapter;

import java.util.List;

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
 * @version 创建时间：2014-12-22 上午10:27:01 类说明 ：缴款adapter
 */
public class MoneyAdapter extends BaseAdapter {
	private List<MoneyBean> mMoneyBeans = null;
	private LayoutInflater layoutInflater;
	private Context context;

	public MoneyAdapter(Context context, List<MoneyBean> mMoneyBean) {
		layoutInflater = LayoutInflater.from(context);
		this.mMoneyBeans = mMoneyBean;
		this.context=context;
	}

	@Override
	public int getCount() {
		return mMoneyBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return mMoneyBeans.get(position);
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
			convertView = layoutInflater.inflate(R.layout.money_listview_item,
					null);
			holder.mailId = (TextView) convertView
					.findViewById(R.id.txt_item_mailId);
			holder.mtxt_item_moneynum = (TextView) convertView
					.findViewById(R.id.txt_item_moneynum);
			holder.mtxt_item_pay_type = (TextView) convertView
					.findViewById(R.id.txt_item_pay_type);
			holder.mtxt_item_username = (TextView) convertView
					.findViewById(R.id.txt_item_username);
			holder.mtxt_item_isMoney = (TextView) convertView
					.findViewById(R.id.txt_item_isMoney);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CustomerDao customerDao = DeliverDaoFactory.getInstance()
				.getCustomerDao(context);
		holder.mailId.setText(mMoneyBeans.get(position).getMailId());
		holder.mtxt_item_moneynum.setText(mMoneyBeans.get(position)
				.getMoneyNum() + "元");
		holder.mtxt_item_username.setText(customerDao.FindDaName(mMoneyBeans.get(position)
				.getUsername()));
		if("0".equals(mMoneyBeans.get(position).getIsMoney())){
			holder.mtxt_item_isMoney.setText("未缴款");
		}else if("1".equals(mMoneyBeans.get(position).getIsMoney())){
			holder.mtxt_item_isMoney.setText("已缴款");
		}
		return convertView;
	}

	public static class ViewHolder {
		TextView mailId;// 站点名称
		TextView mtxt_item_moneynum;// 资费
		TextView mtxt_item_pay_type;// 支付方式
		TextView mtxt_item_username;// 客户
		TextView mtxt_item_isMoney ; // 是否缴款
	}

}

package com.newcdc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.cdc.DaoFactory;
import com.newcdc.R;
import com.newcdc.model.CustomDealVauleBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;

/**
 * @author hanrong
 * @version 创建时间：2014-12-22 上午10:27:01 类说明 ： 客户管理 订单记录adapter
 */
public class CustomDealAdapter extends BaseAdapter {
	private List<CustomDealVauleBean> mBeans = null;
	private LayoutInflater layoutInflater;
	private List<Map<String, String>> fllowtypeList;
	private List<Map<String, String>> typeList;
	private String orgCode;
	private Context context;
	private String mail_num;

	public CustomDealAdapter(Context context, List<CustomDealVauleBean> mBeans) {
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		UserInfoUtils user = new UserInfoUtils(context);
		orgCode = user.getUserDelvorgCode();
		typeList = DaoFactory.getInstance()
				.getBaseDataDao(context)
				.FindBaseDataByDataFlags("UNDLV_CAUSE_CODE");
		this.mBeans = mBeans;
	}

	@Override
	public int getCount() {
		return mBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return mBeans.get(position);
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
			convertView = layoutInflater.inflate(R.layout.item_list_customdeal,
					null);
			holder.mailnum = (TextView) convertView
					.findViewById(R.id.mailnum_customdeal);
			holder.sendname = (TextView) convertView
					.findViewById(R.id.sendname_customdeal);
			holder.sendphone = (TextView) convertView
					.findViewById(R.id.sendphone_customdeal);
			holder.sendaddress = (TextView) convertView
					.findViewById(R.id.sendaddress_customdeal);
			holder.rcvername = (TextView) convertView
					.findViewById(R.id.rcvername_customdeal);
			holder.rcverphone = (TextView) convertView
					.findViewById(R.id.rcverphone_customdeal);
			holder.rcveraddress = (TextView) convertView
					.findViewById(R.id.rcveraddress_customdeal);
			holder.tv_sign_state = (TextView) convertView
					.findViewById(R.id.sign_state_customdeal);
			holder.tv_cause_reason = (TextView) convertView
					.findViewById(R.id.error_reason_item_list_customdeal);
			holder.tv_next_step = (TextView) convertView
					.findViewById(R.id.error_nextstep_item_list_customdeal);
			holder.dlv_date = (TextView) convertView
					.findViewById(R.id.dlv_date_item_list_customdeal);
			holder.sign_state_layout = convertView
					.findViewById(R.id.sign_state_layout);
			holder.error_layout = convertView
					.findViewById(R.id.error_layout_item_list_customdeal);
			holder.dlv_date_layout = convertView
					.findViewById(R.id.dlv_date_layout_item_list_customdeal);
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.imageview_img_item_list_customdeal);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CustomDealVauleBean bean = mBeans.get(position);
		mail_num = bean.getMail_num();
		holder.mailnum.setText(mail_num.trim().toUpperCase());
		holder.sendname.setText(bean.getSender_name());
		holder.sendphone.setText(bean.getSender_contact_phone1());
		holder.sendaddress.setText(bean.getSender_addr());
		holder.rcvername.setText(bean.getRcver_name());
		holder.rcverphone.setText(bean.getRcver_contact_phone1());
		holder.rcveraddress.setText(bean.getRcver_addr());
		String dlv_date = bean.getDlv_date();
		String sign_sts_code = bean.getSign_sts_code();
		String undlv_cause_code = bean.getUndlv_cause_code();
		String undlv_next_actn_code = bean.getUndlv_next_actn_code();
		if (dlv_date == null || "null".equals(dlv_date)) {
			holder.dlv_date_layout.setVisibility(View.GONE);
		} else {
			holder.dlv_date_layout.setVisibility(View.VISIBLE);
			holder.dlv_date.setText(dlv_date);
		}
		if (sign_sts_code == null || "null".equals(sign_sts_code)) {
			holder.sign_state_layout.setVisibility(View.GONE);
		} else {
			holder.sign_state_layout.setVisibility(View.VISIBLE);
			if ("w".equals(sign_sts_code.trim())
					|| "W".equals(sign_sts_code.trim())) {
				holder.tv_sign_state.setText("本人收");
			} else if ("M".equals(sign_sts_code.trim())
					|| "m".equals(sign_sts_code.trim())) {
				holder.tv_sign_state.setText(bean.getSigner_name() + "");
			} else {
				holder.tv_sign_state.setText("单位发章");
			}
		}
		if (undlv_cause_code == null || "null".equals(undlv_cause_code)) {
			holder.error_layout.setVisibility(View.GONE);
		} else {
			holder.error_layout.setVisibility(View.VISIBLE);
			for (Map<String, String> map : typeList) {
				String xk = map.get("dataValue");
				String stateCode = map.get("dataKey");
				if (stateCode.equals(undlv_cause_code)) {
					holder.tv_cause_reason.setText(xk);
					break;
				}
			}
			fllowtypeList = DaoFactory.getInstance()
					.getBaseDataDao(context)
					.FindBaseDataByDataFlags("NEXT_ACTN_CODE");// 下一步动作

			for (Map<String, String> map : fllowtypeList) {
				String xk = map.get("dataValue");
				String stateCode = map.get("dataKey");
				if (stateCode.equals(undlv_next_actn_code.trim().toUpperCase())) {
					holder.tv_next_step.setText(xk);
					break;
				}
			}
		}
		new GetHeadImg().execute(holder.iv_image);
		return convertView;
	}

	class GetHeadImg extends AsyncTask<ImageView, Void, Bitmap> {
		private ImageView iv;

		@Override
		protected Bitmap doInBackground(ImageView... params) {
			iv = params[0];
			Bitmap result = null;
			result = NetHelper.doGetImg(Global.DEALIMAGE + "MAIL_NUM="
					+ mail_num.trim().toUpperCase());
			return result;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null) {
				iv.setImageBitmap(result);
			}
		}
	}

	public static class ViewHolder {
		TextView mailnum;
		TextView sendname;// 发件人姓名
		TextView sendphone;
		TextView sendaddress;
		TextView rcvername;// 收件人
		TextView rcverphone;
		TextView rcveraddress;
		TextView tv_sign_state;
		TextView tv_cause_reason;
		TextView tv_next_step;
		TextView dlv_date;
		ImageView iv_image;
		View sign_state_layout;
		View error_layout;
		View dlv_date_layout;
	}

}

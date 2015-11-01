package com.newcdc.activity.delivertask;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Utils;
import com.newcdc.ui.GetBitmap;

/**
 * @author hanrong
 * @version 创建时间：2014-12-22 上午10:27:01 类说明 ： 投递详情页
 */
@SuppressLint("NewApi")
public class DetailActivity extends Activity implements OnClickListener {
	private int _id = 0;
	private DeliverDao mDataHelper;// 数据库工具类
	private MessageInfoBean bean;

	private TextView mtxtrcver_loc_county;
	private TextView mtxtrcver_contact_phone1;
	// private TextView mtxtsender_loc_county;
	private TextView mtxtsender_street_addr;
	private TextView mtxtrcver_loc_prov;
	private TextView mtxtsender_contact_phone1;
	private TextView mtxtrcver_name;
	private TextView mtxtthe_class_date;
	// private TextView mtxtrcver_street_addr;
	private TextView mtxtsender_name;
	private TextView mtxtsender_loc_prov;
	// private TextView mtxtfrequence;
	private TextView mtxtsender_loc_city;
	// private TextView mtxtrcver_loc_city;
	private TextView mtxtmail_num;
	// private TextView mtxtsender_addr;
	private TextView mtxtrcver_addr, mtxtrcver_money;
	private Button btn_back;
	private ImageView mtxt_image;
	private Bitmap bitmap;
	String imagePath = "";
	private Dialog ivdialog;
	private int width;
	private int height;
	private ArrayList<DeliverQueueBean> commitInfo;
	private LinearLayout contentView;
	private DeliverDaoFactory daoFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail);
		DisplayMetrics metric = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		height = metric.heightPixels;
		initBundle();
		init();
		queryData();
		btn_back.setOnClickListener(this);
	}

	/**
	 * 初始化组件
	 * **/
	private void init() {
		btn_back = (Button) findViewById(R.id.deliver_back);
		mtxtrcver_loc_county = (TextView) findViewById(R.id.txtrcver_loc_county);
		mtxtrcver_contact_phone1 = (TextView) findViewById(R.id.txtrcver_contact_phone1);
		// mtxtsender_loc_county = (TextView)
		// findViewById(R.id.txtsender_loc_county);
		mtxtsender_street_addr = (TextView) findViewById(R.id.txtsender_street_addr);
		mtxtrcver_loc_prov = (TextView) findViewById(R.id.txtrcver_loc_prov);
		mtxtsender_contact_phone1 = (TextView) findViewById(R.id.txtsender_contact_phone1);
		mtxtrcver_name = (TextView) findViewById(R.id.txtrcver_name);
		mtxtthe_class_date = (TextView) findViewById(R.id.txtthe_class_date);
		// mtxtrcver_street_addr = (TextView)
		// findViewById(R.id.txtrcver_street_addr);
		mtxtsender_name = (TextView) findViewById(R.id.txtsender_name);
		mtxtsender_loc_prov = (TextView) findViewById(R.id.txtsender_loc_prov);
		// mtxtfrequence = (TextView) findViewById(R.id.txtfrequence);
		mtxtsender_loc_city = (TextView) findViewById(R.id.txtsender_loc_city);
		// mtxtrcver_loc_city = (TextView) findViewById(R.id.txtrcver_loc_city);
		mtxtmail_num = (TextView) findViewById(R.id.txtmail_num);
		// mtxtsender_addr = (TextView) findViewById(R.id.txtsender_addr);
		mtxtrcver_addr = (TextView) findViewById(R.id.txtrcver_addr);
		mtxtrcver_money = (TextView) findViewById(R.id.txtrcver_money);
		mtxt_image = (ImageView) findViewById(R.id.txt_image);
		contentView = (LinearLayout) findViewById(R.id.content_layout);
		mtxt_image.setOnClickListener(this);
	}

	/**
	 * 初始化上个页面传来的数据
	 * **/
	private void initBundle() {
		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			_id = bundle.getInt("detail_id");
		}

	}

	/**
	 * 从数据库读出信息并设置
	 * **/
	private void queryData() {
		daoFactory = DeliverDaoFactory.getInstance();
		mDataHelper = daoFactory.getDeliverDao(DetailActivity.this);
		bean = mDataHelper.query_id(_id);
		String mail_num = bean.getMail_num();
		commitInfo = daoFactory.getQueueDao(DetailActivity.this)
				.queryCommitInfoByMailNum(mail_num);
		for (int i = 0; i < commitInfo.size(); i++) {
			DeliverQueueBean queueBean = commitInfo.get(i);
			View view = LayoutInflater.from(DetailActivity.this).inflate(
					R.layout.detail_activity_view, null);
			contentView.addView(view);
			View ok_view = view.findViewById(R.id.view_ok_info);// 妥投view
			View err_view = view.findViewById(R.id.view_err_info);// 未妥投view
			TextView tv_sign_state = (TextView) view
					.findViewById(R.id.sign_state_detail_activity);// 签收情况
			TextView tv_undlv_reason = (TextView) view
					.findViewById(R.id.undlv_reason_detail_activity);// 未妥投原因
			TextView tv_undlv_nextstep = (TextView) view
					.findViewById(R.id.undlv_nextstep_detail_activity);// 下一步动作
			TextView tv_dlv_date = (TextView) view.findViewById(R.id.dlv_date);// 投递日期
			String sign_sts_code = queueBean.getSign_sts_code();
			String undlv_cause_code = queueBean.getUndlv_cause_code();
			String undlv_next_actn_code = queueBean.getUndlv_next_actn_code();
			String dlv_date = queueBean.getDlv_date();
			String dlv_time = queueBean.getDlv_time();
			if (Utils.stringEmpty(sign_sts_code)) {// 妥投为空
				if (Utils.stringEmpty(undlv_cause_code)) {// 未妥投为空
				} else {
					ok_view.setVisibility(View.GONE);
					tv_undlv_reason.setText(Utils.parseDlvCode(
							undlv_cause_code, DetailActivity.this));
					tv_undlv_nextstep.setText(Utils.parseDlvCode(
							undlv_cause_code, undlv_next_actn_code,
							DetailActivity.this));
				}
			} else {
				err_view.setVisibility(View.GONE);
				tv_sign_state.setText(Utils.parseDlvCode(queueBean));
			}
			tv_dlv_date.setText(Utils.parseDate(dlv_date, dlv_time));
		}
		TextView tv_dis = (TextView) findViewById(R.id.tv_dis_detail);
		int distance = bean.getDistance();
		if (distance == Integer.MAX_VALUE) {
			tv_dis.setText("计算中");
		} else if (distance == Integer.MAX_VALUE - 1) {
			tv_dis.setText("计算失败");
		} else {
			tv_dis.setText(bean.getDistance() + "米");
		}
		if (bean.getRcver_loc_county() != null) {
			mtxtrcver_loc_county.setText(bean.getRcver_loc_county() + "");
		}
		if (bean.getRcver_contact_phone1() != null) {
			mtxtrcver_contact_phone1.setText(bean.getRcver_contact_phone1()
					+ "");
		}
		if (bean.getSender_street_addr() != null) {
			mtxtsender_street_addr.setText(bean.getSender_street_addr() + "");
		}
		// mtxtsender_loc_county.setText(bean.getSender_loc_county()+"");
		if (bean.getRcver_loc_prov() != null) {
			mtxtrcver_loc_prov.setText(bean.getRcver_loc_prov() + "");
		}
		if (bean.getSender_contact_phone1() != null) {
			mtxtsender_contact_phone1.setText(bean.getSender_contact_phone1()
					+ "");
		}
		if (bean.getRcver_name() != null) {
			mtxtrcver_name.setText(bean.getRcver_name() + "");
		}
		if (bean.getThe_class_date() != null) {
			mtxtthe_class_date.setText(bean.getThe_class_date() + "");
		}
		if (bean.getSender_name() != null) {
			mtxtsender_name.setText(bean.getSender_name() + "");
		}
		// mtxtrcver_street_addr.setText(bean.getRcver_street_addr()+"");
		if (bean.getSender_loc_prov() != null) {
			mtxtsender_loc_prov.setText(bean.getSender_loc_prov() + "");
		}
		if (bean.getSender_loc_prov() != null) {
			mtxtsender_loc_prov.setText(bean.getSender_loc_prov() + "");
		}
		if (bean.getSender_loc_city() != null) {
			mtxtsender_loc_city.setText(bean.getSender_loc_city() + "");
		}
		// mtxtfrequence.setText(bean.getFrequence() + "");

		// mtxtrcver_loc_city.setText(bean.getRcver_loc_city()+"");
		if (bean.getMail_num() != null) {
			mtxtmail_num.setText(bean.getMail_num() + "");
		}
		if (bean.getRcver_addr() != null) {
			mtxtrcver_addr.setText(bean.getRcver_addr() + "");
		}
		if (bean.getMoney() != null && !"".equals(bean.getMoney())
				&& !"0.0".equals(bean.getMoney())) {
			mtxtrcver_money.setText(bean.getMoney() + "元");
		}
		// mtxtsender_addr.setText(bean.getSender_addr()+"");

		if (bean.getPicture() != null && !"".equals(bean.getPicture())) {
			imagePath = bean.getPicture();
			bitmap = BitmapFactory.decodeFile(Constant.DELIVER_OK + imagePath);
			mtxt_image.setImageBitmap(bitmap);
			mtxt_image.setBackground(null);
		}
		// else {
		// mtxt_image.setVisibility(View.GONE);
		// }

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.deliver_back:
			this.finish();
			break;
		case R.id.txt_image:
			if (bitmap != null) {
				reViewImage();
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	private void reViewImage() {
		if (ivdialog == null) {
			ivdialog = new Dialog(DetailActivity.this, R.style.dialogss);
		}
		View v = LayoutInflater.from(DetailActivity.this).inflate(
				R.layout.dialog_image_review, null);
		ivdialog.setContentView(v, new LayoutParams(width, height));
		ImageView review_image = (ImageView) v.findViewById(R.id.review_image);
		bitmap = BitmapFactory.decodeFile(Constant.DELIVER_OK + imagePath);
		bitmap = GetBitmap.big(bitmap);
		review_image.setImageBitmap(bitmap);
		mtxt_image.setImageBitmap(bitmap);
		mtxt_image.setBackground(null);
		review_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ivdialog.dismiss();
			}
		});
		ivdialog.show();
	}

	@Override
	protected void onResume() {
		Utils.startIntentService(DetailActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

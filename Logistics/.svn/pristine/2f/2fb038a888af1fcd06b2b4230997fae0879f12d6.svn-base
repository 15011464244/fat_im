package com.ems.express.ui.mail;

import java.util.Timer;

import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.R.id;
import com.ems.express.R.layout;
import com.ems.express.bean.OrderInfoBean;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MailDetailActivity extends Activity {
	private TextView mTvOrderNum,mTvMailNum,
					mTvSender,mTvSenderPhone,mTvSenderAddress,
					mTvReceiver,mTvReceiverPhone,mTvReveiverAddress,
					mTvWeight,mTvType,mTvPayType,mTvOrderTime;
	private TextView title;
	private OrderInfoBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_detail);
		initView();
		
		String orderNo = getIntent().getStringExtra("orderNo");
		bean = App.dbHelper.getOrderInfo(App.db, orderNo);
		if(null != bean){
			loadView();
		}else{
//			final Dialog dialog = DialogUtils.successMessage(this,"该订单记录已被删除！");
			ToastUtil.show(this, "该订单记录已被删除！");
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
//					dialog.dismiss();
					finish();
				}
			}, 3*1000);
		}
		
	}
	
	  @Override
		protected void onResume() {
			super.onResume();
			//友盟统计
			MobclickAgent.onResume(this);
		}
		@Override
		protected void onPause() {
			super.onPause();
			//友盟统计
			MobclickAgent.onPause(this);
		}
	
	void initView(){
		mTvOrderNum = (TextView) findViewById(R.id.tv_order_num);
//		mTvMailNum = (TextView) findViewById(R.id.tv_mail_num);
		mTvSender = (TextView) findViewById(R.id.tv_sender);
		mTvSenderPhone = (TextView) findViewById(R.id.tv_sender_phone);
		mTvSenderAddress = (TextView) findViewById(R.id.tv_sender_address);
		mTvReceiver = (TextView) findViewById(R.id.tv_receiver);
		mTvReceiverPhone = (TextView) findViewById(R.id.tv_receiver_phone);
		mTvReveiverAddress = (TextView) findViewById(R.id.tv_receiver_address);
		mTvWeight = (TextView) findViewById(R.id.tv_weight);
		mTvType = (TextView) findViewById(R.id.tv_type);
		mTvPayType = (TextView) findViewById(R.id.tv_pay_type);
		mTvOrderTime = (TextView) findViewById(R.id.tv_order_time);
		
		title = (TextView) findViewById(R.id.tv_title);
		title.setText("订单详情");
	}
	
	void loadView(){
		mTvOrderNum.setText(bean.getOrderNo());
		mTvSender.setText(bean.getSenderName());
		mTvSenderPhone.setText(bean.getSenderPhone());
		mTvSenderAddress.setText(bean.getSenderAddress());
		mTvReceiver.setText(bean.getReceiveName());
		mTvReceiverPhone.setText(bean.getReceivePhone());
		mTvReveiverAddress.setText(bean.getReceiveAddress());
		mTvWeight.setText(bean.getWeight()+"g");
		mTvOrderTime.setText(bean.getOrderTime());
		
		String type = "1".equals(bean.getType())? "文件":"物品"; 
		mTvType.setText(type);
		
		String payWay = "1".equals(bean.getPayWay())? "寄件现结":"到付";
		mTvPayType.setText(payWay);
	}
	
	public void back(View v) {
		finish();
	}
}

package com.ems.express.ui.message.send;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.fragment.mail.MailFollowFragment;
import com.ems.express.fragment.mail.MailHistoryFragment;
import com.ems.express.fragment.message.ArriveFragment;
import com.ems.express.fragment.message.EmbraceFragment;
import com.ems.express.fragment.message.OtherFragment;
import com.ems.express.ui.Home2Activity;
import com.ems.express.util.DialogUtils;
import com.umeng.analytics.MobclickAgent;

public class SendMessageListActivity extends Activity implements OnClickListener{
	private TextView tv;
	TextView mMainRadioButtonGame;
	TextView mMainRadioButtonHome;
	TextView mOtherMessage;
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	
	private EmbraceFragment embraceFragment;
	private ArriveFragment arriverFragment;
	private OtherFragment otherFragment;
	
	private TextView tvNewMessage1;
	private TextView tvNewMessage2;
	//其他消息
	private TextView tvNewMessage3;
	
	private ImageView font1;
	private ImageView font2;
	private ImageView font3;
	
	
	private String currentFragment = "embraceFragment";
	
	
	public BroadcastReceiver newMsgBReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			showMessageTag();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message_list);
		init();
		loadView();
		
		showMessageTag();
		
		// 注册一个自定义的广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("NewMsgReceiver_Action");
		this.registerReceiver(newMsgBReceiver, filter);
		 
		 
		 
	}
	
	public void showMessageTag() {
		// 是否有未读收件消息
		int recMsgCount = App.dbHelper.querySendNoticeMessage(App.db,"10");
		if (recMsgCount == 0) {
			tvNewMessage2.setVisibility(View.GONE);
		} else {
			tvNewMessage2.setVisibility(View.VISIBLE);
			tvNewMessage2.setText(recMsgCount + "");
		}
		// 是否有未读寄件消息
		int count1 = App.dbHelper.querySendNoticeMessage(App.db,"3");
		int count2 = App.dbHelper.querySendNoticeMessage(App.db,"4");
		int total = count1 + count2;
		
		if (total == 0) {
			tvNewMessage1.setVisibility(View.GONE);
		} else {
			tvNewMessage1.setVisibility(View.VISIBLE);
			tvNewMessage1.setText(total + "");
		}
		
		// 是否有其他消息
		int integral = App.dbHelper.queryPaymentMessage(App.db,"6");
		int payment = App.dbHelper.queryPaymentMessage(App.db,"7");
		int othersCount = integral + payment;
		if (othersCount == 0) {
			tvNewMessage3.setVisibility(View.GONE);
		} else {
			tvNewMessage3.setVisibility(View.VISIBLE);
			tvNewMessage3.setText(othersCount + "");
		}
		
	}
	
	public static void actionStart(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SendMessageListActivity.class);
		context.startActivity(intent);
	}

	private void loadView() {
		mOtherMessage =(TextView) findViewById(R.id.other_message);
		mMainRadioButtonGame=(TextView) findViewById(R.id.main_radioButton_game);
		mMainRadioButtonHome = (TextView) findViewById(R.id.main_radioButton_home);
		mMainRadioButtonGame.setOnClickListener(this);
		mMainRadioButtonHome.setOnClickListener(this);
		mOtherMessage.setOnClickListener(this);
		mFragmentManager = getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		
		
		embraceFragment = new EmbraceFragment();
		arriverFragment = new ArriveFragment();
		otherFragment = new OtherFragment();
		
		mMainRadioButtonGame.setSelected(true);
		changeStyle(font1);
		mFragmentTransaction.add(R.id.view,embraceFragment);
		
		mFragmentTransaction.commit();
	}

	private void init() {
		tv=(TextView)findViewById(R.id.tv_title);
		tv.setText("消息");
		tv.setTextSize(20);
		
		tvNewMessage1 = (TextView) this.findViewById(R.id.tv_new_message1);
		tvNewMessage2 = (TextView) this.findViewById(R.id.tv_new_message2);
		tvNewMessage3 = (TextView) this.findViewById(R.id.tv_new_message3);
		
		font1 = (ImageView) findViewById(R.id.iv_show_send_font);
		font2 = (ImageView) findViewById(R.id.iv_show_arrive_font);
		font3 = (ImageView) findViewById(R.id.iv_show_other_font);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.main_radioButton_game:
			currentFragment = "embraceFragment";
			
			mFragmentTransaction = mFragmentManager.beginTransaction();
			
			mMainRadioButtonGame.setSelected(true);
			mMainRadioButtonHome.setSelected(false);
			mOtherMessage.setSelected(false);
			changeStyle(font1);
			mFragmentTransaction.replace(R.id.view,embraceFragment);
			mFragmentTransaction.commit();
			break;
		case R.id.main_radioButton_home:
			currentFragment = "arriverFragment";
			
			mFragmentTransaction = mFragmentManager.beginTransaction();
			changeStyle(font2);
			mMainRadioButtonHome.setSelected(true);
			mMainRadioButtonGame.setSelected(false);
			mOtherMessage.setSelected(false);
			
			mFragmentTransaction.replace(R.id.view,  arriverFragment);
			mFragmentTransaction.commit();
			break;
		case R.id.other_message:
			currentFragment = "otherFragment";
			
			mFragmentTransaction = mFragmentManager.beginTransaction();
			
			mOtherMessage.setSelected(true);
			mMainRadioButtonHome.setSelected(false);
			mMainRadioButtonGame.setSelected(false);
			changeStyle(font3);
			mFragmentTransaction.replace(R.id.view,  otherFragment);
			mFragmentTransaction.commit();
			break;
		}
	}
	/**
	 * 改变按钮的style
	 * @param textV
	 * @param imgV
	 */
	void changeStyle(ImageView imgV){
		
		font1.setVisibility(View.GONE);
		font2.setVisibility(View.GONE);
		font3.setVisibility(View.GONE);
		
		imgV.setVisibility(View.VISIBLE);
		
		
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
	
	//返回
	public void back(View v) {
//		Intent intent = new Intent(this, Home2Activity.class);
//		this.startActivity(intent);
		finish();
	}
	
//	@Override  
//    public boolean onKeyDown(int keyCode, KeyEvent event) {  
//		Intent intent = new Intent(this, Home2Activity.class);
//		this.startActivity(intent);
//        return true;  
//    } 
}

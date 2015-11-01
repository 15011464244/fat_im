package com.ems.express.ui.mail;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.fragment.mail.MailFollowFragment;
import com.ems.express.fragment.mail.MailHistoryFragment;
import com.umeng.analytics.MobclickAgent;

public class MailOrderListActivity extends Activity implements OnClickListener{
	private TextView tv;
	TextView mMainRadioButtonGame;
	TextView mMainRadioButtonHome;
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private MailFollowFragment mailFollowFragment;
	private MailHistoryFragment mailHistoryFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_orderlist);
		loadView();
		 init();
	}
	
	public static void actionStart(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MailOrderListActivity.class);
		context.startActivity(intent);
	}

	private void loadView() {
		mMainRadioButtonGame=(TextView) findViewById(R.id.main_radioButton_game);
		mMainRadioButtonHome = (TextView) findViewById(R.id.main_radioButton_home);
		mMainRadioButtonGame.setOnClickListener(this);
		mMainRadioButtonHome.setOnClickListener(this);
		mFragmentManager = getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mailFollowFragment = new MailFollowFragment();
		mailHistoryFragment = new MailHistoryFragment();
		
		mMainRadioButtonGame.setSelected(true);
		
		mFragmentTransaction.add(R.id.view,mailHistoryFragment);
		mFragmentTransaction.commit();
	}

	private void init() {
		tv=(TextView)findViewById(R.id.tv_title);
		tv.setText("寄件记录");
		tv.setTextSize(20);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.main_radioButton_game:
			mFragmentTransaction = mFragmentManager.beginTransaction();
			
			mMainRadioButtonGame.setSelected(true);
			mMainRadioButtonHome.setSelected(false);
			
			mFragmentTransaction.replace(R.id.view, mailHistoryFragment);
			mFragmentTransaction.commit();
			break;
		case R.id.main_radioButton_home:
			mFragmentTransaction = mFragmentManager.beginTransaction();
			
			mMainRadioButtonHome.setSelected(true);
			mMainRadioButtonGame.setSelected(false);
			
			mFragmentTransaction.replace(R.id.view, mailFollowFragment);
			mFragmentTransaction.commit();
			break;
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
	
	//返回
	public void back(View v) {
		finish();
	}
}

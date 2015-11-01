package com.ems.express.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.fragment.mail.MailFollowFragment;
import com.ems.express.fragment.mail.MailHistoryFragment;

public class MailCenterActivity extends Activity implements OnClickListener{
	private TextView tv;
	TextView mMainRadioButtonGame;
	TextView mMainRadioButtonHome;
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private MailHistoryFragment mTrackFragment;
	private MailFollowFragment mMailFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_orderlist);
		loadView();
		 init();
	}

	private void loadView() {
		mMainRadioButtonGame=(TextView) findViewById(R.id.main_radioButton_game);
		mMainRadioButtonHome = (TextView) findViewById(R.id.main_radioButton_home);
		mMainRadioButtonGame.setOnClickListener(this);
		mMainRadioButtonHome.setOnClickListener(this);
		
		mFragmentManager = getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		
		mTrackFragment = new MailHistoryFragment(); 
		mMailFragment = new MailFollowFragment();
		
		mFragmentTransaction.add(R.id.view,mTrackFragment);
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
			mFragmentTransaction.replace(R.id.view, mTrackFragment);
			mFragmentTransaction.commit();
			break;
		case R.id.main_radioButton_home:
			mFragmentTransaction = mFragmentManager.beginTransaction();
			mFragmentTransaction.replace(R.id.view, mMailFragment);
			mFragmentTransaction.commit();
			break;
		}
	}
	
	
}

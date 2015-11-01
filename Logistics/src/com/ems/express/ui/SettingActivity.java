package com.ems.express.ui;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.ButterKnife;

import com.ems.express.R;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class SettingActivity extends Activity {

	private Context mContext;
	private TextView btnLogin;
	
	private Switch shake;
	private Switch tone;
	
	private Vibrator vibrator;//震动对象

	public static void actionStart(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SettingActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mContext = this;
		ButterKnife.inject(this);
		((TextView) findViewById(R.id.tv_title)).setText("设置");
		shake = (Switch)findViewById(R.id.sw_shake);
		tone = (Switch)findViewById(R.id.sw_tone);
		//设置shake，tone按钮的初始状态
		shake.setChecked(SpfsUtil.loadShake());
		tone.setChecked(SpfsUtil.loadTone());
		//震动对象复制
		vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
		
		btnLogin = (TextView) findViewById(R.id.btn_login);
		if (SpfsUtil.loadPhone().equals("")) {
			btnLogin.setText("登录");
		} else {
			btnLogin.setText("注销");
		}
		
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SpfsUtil.loadPhone().equals("")) {
					Intent intent = new Intent(SettingActivity.this,
							LoginActivity.class);
					startActivity(intent);
				} else {
					DialogUtils.showTwoButton(SettingActivity.this);
				}
				
			}
		});
	}

	public void back(View v) {
		finish();
	}

	public void addCommonWord(View v) {
		AddCommonActivity.actionStart(mContext);
	}

	public void clearCache(View v) {
		// ToastUtil.show(this, "hha");
		DialogUtils.showTwoButton2(SettingActivity.this);
	}
	//震动设置
	public void setShake(View v) {
		if(shake.isChecked()){
			vibrator.vibrate(new long[]{0,150,150,200}, -1);
		}
		SpfsUtil.saveShake(shake.isChecked());
	}
	//提示音设置
	public void setTone(View v) {
		SpfsUtil.saveTone(tone.isChecked());
	}

	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计
		MobclickAgent.onResume(this);
		if (SpfsUtil.loadPhone().equals("")) {
			btnLogin.setText("登录");
		} else {
			btnLogin.setText("注销");
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
	}
	
	
}


package com.ems.express.ui;

import com.ems.express.R;
import com.ems.express.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class YouhuiquanListActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youhuiquan_list);
		setHeadTitle("我的优惠券");
	}
	
	public void back(View v) {
		finish();
	}
}

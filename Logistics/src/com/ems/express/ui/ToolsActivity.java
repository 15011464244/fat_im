package com.ems.express.ui;

import com.ems.express.R;
import com.ems.express.R.id;
import com.ems.express.R.layout;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.SpfsUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToolsActivity extends Activity implements OnClickListener{
	private LinearLayout servicePoint;
	private LinearLayout rangeQuery;
	private LinearLayout timeQuery;
	private LinearLayout priceQuery;
	
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools);
		
		((TextView) findViewById(R.id.tv_title)).setText("工具");
		
		mContext = this;
		servicePoint = (LinearLayout) this.findViewById(R.id.ll_serverpoint);
		rangeQuery = (LinearLayout) this.findViewById(R.id.ll_rangequery);
		timeQuery = (LinearLayout) this.findViewById(R.id.ll_timequery);
		priceQuery = (LinearLayout) this.findViewById(R.id.ll_pricequery);
		
		servicePoint.setOnClickListener(this);
		rangeQuery.setOnClickListener(this);
		timeQuery.setOnClickListener(this);
		priceQuery.setOnClickListener(this);
		
	}
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ToolsActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.ll_serverpoint:
			if(SpfsUtil.getIsLoadingCity() == true){
				DialogUtils.successMessage(mContext,"初始化数据加载中\n请20秒后重新再试！");
				return;
			}
			intent = new Intent(this, BaiduMapActivity.class);
			intent.putExtra(BaiduMapActivity.KEY_TYPE,
					BaiduMapActivity.TYPE_SERVICE_POINT);
			startActivity(intent);
			break;
		case R.id.ll_rangequery:
			if(SpfsUtil.getIsLoadingCity() == true){
				DialogUtils.successMessage(mContext,"初始化数据加载中\n请20秒后重新再试！");
				return;
			}
			intent = new Intent(this, ServiceRangeActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_timequery:
			if(SpfsUtil.getIsLoadingCity() == true){
				DialogUtils.successMessage(mContext,"初始化数据加载中\n请20秒后重新再试！");
				return;
			}
			intent = new Intent(this, TimeQueryActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_pricequery:
			if(SpfsUtil.getIsLoadingCity() == true){
				DialogUtils.successMessage(mContext,"初始化数据加载中\n请20秒后重新再试！");
				return;
			}
			PriceActivity.actionStart(mContext);
			break;

		default:
			break;
		}
	}
	
	public void back(View v) {
		finish();
	}
}

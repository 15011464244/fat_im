package com.ems.express.ui.mail;

import java.util.List;

import com.ems.express.R;
import com.ems.express.adapter.mail.MailTransItemAdapter;
import com.ems.express.ui.check.ResultActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MailTransListActivity extends Activity{
	private TextView tv;
	private ListView listView;
	private List<String> transList;
	private Intent intent;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_translist);
		loadView();
		init();
	}
	
	//加载视图
	private void loadView(){
		listView = (ListView)this.findViewById(R.id.lv_trans_list);
		listView.setDividerHeight(0);
		mContext = this;
		//获取数据
		intent = this.getIntent();
		transList = intent.getStringArrayListExtra("mailNums");
		
		listView.setAdapter(new MailTransItemAdapter(this, transList));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//根据运单号查询订单详情
				ResultActivity.actionStart(mContext, transList.get(position),intent.getStringExtra("mailState"));
				
			}
		});
	}
	
	//初始化title
	private void init() {
		tv=(TextView)findViewById(R.id.tv_title);
		tv.setText("寄件历史");
		tv.setTextSize(20);
	}
	//返回
	public void back(View v) {
		finish();
	}
}

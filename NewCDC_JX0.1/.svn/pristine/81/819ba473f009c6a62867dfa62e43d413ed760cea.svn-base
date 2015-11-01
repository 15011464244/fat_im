package com.newcdc.activity.censcus;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.activity.delivertask.DetailActivity;
import com.newcdc.adapter.CountNoticeAdapter;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Utils;

/**
 * 妥投统计
 * 
 * @author 李朋
 * 
 */
public class CountNoticeActivity extends Activity {
	private ListView listView;
	private TextView tv_count;
	private List<MessageInfoBean> listData;
	private DeliverDao deliverDao;// 投递
	private int deliverCount = 0;// 投递个数
	private CountNoticeAdapter countNoticeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_count_notice);
		listView = (ListView) findViewById(R.id.fragment_count_notice_listView);
		tv_count = (TextView) findViewById(R.id.fragment_count_notice_textView);
		int dealType = getIntent().getIntExtra("dealType", -1);
		initData();
		findViewById(R.id.censcue_notice_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						finish();
					}
				});

		tv_count.setText(deliverCount + "");
		countNoticeAdapter = new CountNoticeAdapter(listData, this);
		listView.setAdapter(countNoticeAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Intent intent = new Intent(CountNoticeActivity.this,
						DetailActivity.class);
				MessageInfoBean bean = listData.get(position);
				int _id = bean.get_id();
				intent.putExtra("detail_id", _id);
				startActivity(intent);
			}
		});
	}

	private void initData() {
		deliverDao = DeliverDaoFactory.getInstance().getDeliverDao(
				CountNoticeActivity.this);
		listData = deliverDao.queryByDealResultAllFrequence(Constant.TUOTOU);// 已妥投邮件集合
		deliverCount = listData.size();// 妥投个数
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(CountNoticeActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

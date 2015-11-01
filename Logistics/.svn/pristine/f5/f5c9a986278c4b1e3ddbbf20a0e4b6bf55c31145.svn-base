package com.ems.express.ui;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.common.MessageCenterAdapter;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.bean.MessageCenterItemBean;

public class MessageCenterActivity extends Activity {

	private Context mContext;
	private static MessageCenterAdapter mAdapter;
	private static List<MessageCenterItemBean> mData = new ArrayList<MessageCenterItemBean>();
	private ListView mMessageCenterListview;

	 public static Handler sHandler = new Handler() {
	
	 @Override
	 public void handleMessage(Message msg) {
	 switch (msg.what) {
	 case 1:
//	  getPushMessage();
	 break;
	 }
	 super.handleMessage(msg);
	 }
	
	 };

	public static void actionStart(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MessageCenterActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_center);
		mContext = this;
		ButterKnife.inject(this);
		((TextView) findViewById(R.id.tv_title)).setText("通知中心");
		mMessageCenterListview = (ListView) findViewById(R.id.message_center_listview);
		Intent intent = getIntent();
		List<DeliveryMessageBean> dmList = App.dbHelper.queryAllDeliveryMessage(App.db);
		App.dbHelper.updateAllDeliveryMessageRed(App.db);
		mAdapter = new MessageCenterAdapter(mContext, dmList);
		mMessageCenterListview.setAdapter(mAdapter);
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			mAdapter.notifyDataSetChanged();
			handler.postDelayed(this, 1000);
		}
	};

	@Override
	protected void onPause() {
		mAdapter.notifyDataSetChanged();
		super.onPause();
	}

	public void back(View v) {
		finish();
	}

	public void addCommonWord(View v) {
		AddCommonActivity.actionStart(mContext);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mAdapter.setmList(App.dbHelper.queryAllDeliveryMessage(App.db));
	}

}

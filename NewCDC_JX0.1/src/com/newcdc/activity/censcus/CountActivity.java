package com.newcdc.activity.censcus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.newcdc.R;
import com.newcdc.activity.clcttask.CollectionActivity;
import com.newcdc.activity.clcttask.CollectionActivity_JX;
import com.newcdc.activity.delivertask.TaskShowActivity;
import com.newcdc.application.BaseActivity;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.FastLanDao;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.tools.Constant;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

public class CountActivity extends BaseActivity implements OnClickListener {
	private DeliverDao deliverDao;// 投递
	private int gatherCount, nogatherCount;// 揽收个数
	private FastLanDao fastLanDao;// 揽投
	private Gather_MsgDao gatherDao;
	private String username, delvorgcode;
	private Button btn_tuotou_count, btn_weituotou_error, btn_clct_count,
			btn_noclct_count, btn_ondeal;
	private View back;
	private int tuotouCount;
	private int ondealCount;
	private int weituotouCount;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_count);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		btn_tuotou_count = (Button) findViewById(R.id.fragment_count_button1);
		btn_weituotou_error = (Button) findViewById(R.id.fragment_count_button2);
		btn_clct_count = (Button) findViewById(R.id.fragment_count_button3);
		btn_ondeal = (Button) findViewById(R.id.fragment_count_button4);
		btn_noclct_count = (Button) findViewById(R.id.fragment_count_button5);
		back = findViewById(R.id.censcue_back);
	}

	private void initData() {
		UserInfoUtils user = new UserInfoUtils(CountActivity.this);
		delvorgcode = user.getUserDelvorgCode();
		username = user.getUserName();
		deliverDao = DeliverDaoFactory.getInstance().getDeliverDao(
				CountActivity.this);
		fastLanDao = DeliverDaoFactory.getInstance().getFastLanDao(
				getApplicationContext());
		gatherDao = DeliverDaoFactory.getInstance().getGather_msgdao(
				getApplicationContext());
		mHandler = new Handler();
		try {
			loadData();
		} catch (Exception e) {
		}
	}

	/**
	 * 加载数据
	 */
	public void loadData() {
		new Thread() {
			@Override
			public void run() {
				tuotouCount = deliverDao.queryByDealResultAllFrequence(
						Constant.TUOTOU).size();
				ondealCount = deliverDao.queryByDealResultAllFrequence(
						Constant.DAICHULI).size();
				weituotouCount = deliverDao.queryByDealResultAllFrequence(
						Constant.WEITUOTOU).size();
				gatherCount = DeliverDaoFactory.getInstance()
						.getJxClctDao(getApplicationContext())
						.queryJxClctMessage_All(delvorgcode, username).size();
				nogatherCount = gatherDao.queryGatherMessage(delvorgcode,
						username).size();
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						btn_tuotou_count.setText("妥投：" + tuotouCount);
						btn_ondeal.setText("待处理：" + ondealCount);
						btn_weituotou_error.setText("未妥投：" + weituotouCount);
						btn_clct_count.setText("已揽收：" + gatherCount);
						btn_noclct_count.setText("未揽收：" + nogatherCount);
					}
				});
			};
		}.start();
	}

	private void initListener() {
		// 妥投統計
		btn_tuotou_count.setOnClickListener(this);
		// 未妥投统计
		btn_weituotou_error.setOnClickListener(this);
		// 未处理統計
		btn_ondeal.setOnClickListener(this);
		// 揽收統計
		btn_clct_count.setOnClickListener(this);
		back.setOnClickListener(this);
		btn_noclct_count.setOnClickListener(this);
	}

	@Override
	protected void onRestart() {
		initData();
		super.onRestart();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(CountActivity.this, TaskShowActivity.class);
		switch (v.getId()) {
		case R.id.fragment_count_button1:// 妥投
			if (tuotouCount != 0) {
				intent.putExtra("dealType", Constant.TUOTOU);
				intent.putExtra("delivertasknum", "");
				startActivity(intent);
			} else {
				showToast("无相关统计信息！");
			}
			break;

		case R.id.fragment_count_button2:
			if (weituotouCount != 0) {
				intent.putExtra("dealType", Constant.WEITUOTOU);
				intent.putExtra("delivertasknum", "");
				startActivity(intent);
			} else {
				showToast("无相关统计信息！");
			}
			break;
		case R.id.fragment_count_button3:
			if (gatherCount != 0) {
				Intent intent2 = new Intent(CountActivity.this,
						CollectionActivity_JX.class);
				intent2.putExtra("dealType", Constant.COLLECTION);
				startActivity(intent2);
			} else {
				showToast("无相关统计信息");
			}
			break;
		case R.id.fragment_count_button4:
			intent.putExtra("dealType", Constant.DAICHULI);
			intent.putExtra("delivertasknum", "");
			if (ondealCount != 0) {
				startActivity(intent);
			} else {
				showToast("无相关统计信息！");
			}
			break;
		case R.id.fragment_count_button5:
			if (nogatherCount != 0) {
				Intent intent2 = new Intent(CountActivity.this,
						CollectionActivity_JX.class);
				intent2.putExtra("dealType", Constant.NOCOLLECTION);
				startActivity(intent2);
			} else {
				showToast("无相关统计信息");
			}
			break;
		case R.id.censcue_back:
			finish();
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(CountActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

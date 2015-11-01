package com.newcdc.activity.delivertask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.newcdc.R;
import com.newcdc.adapter.DeliverTaskListAdapter;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.DeliverTaskListDao;
import com.newcdc.tools.Constant;
import com.newcdc.tools.UserInfoUtils;

/**
 * 投递
 * 
 * @author hanrong 2015-4-11,上午11:20:10
 * 
 */
public class DeliverTaskListActivity extends Activity implements
		OnClickListener {
	private ListView mlistview_tasklist;
	private DeliverTaskListAdapter mDeliverTaskListAdapter;
	private ArrayList<Map<String, String>> tasklist = null;
	private DeliverTaskListDao mTaskListDao;
	private Button mbtn_back_tasklist;
	private UserInfoUtils mUserInfo = null;
	private TaskListReceiver mListReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivertasklist);
		init();
		initListAdapter();

	}

	private void init() {
		mTaskListDao = DeliverDaoFactory.getInstance().getDeliverTaskListDao(
				DeliverTaskListActivity.this);
		mUserInfo = new UserInfoUtils(DeliverTaskListActivity.this);
		// ArrayList<Map<String, String>> tasklist1 = new ArrayList<Map<String,
		// String>>();
		// for (int i = 0; i < 5; i++) {
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("delivertasknum", "" + i);
		// map.put("delivertaskcount", "" + i);
		// map.put("delivertaskname", "今日任务");
		// map.put("delivertaskallcount", "1" + i + 1);
		// tasklist1.add(map);
		// }
		// mTaskListDao.saveGatherTaskMessage(tasklist1,
		// mUserInfo.getUserDelvorgCode(), mUserInfo.getUserName());
		tasklist = mTaskListDao.queryGatherTaskMessage(
				mUserInfo.getUserDelvorgCode(), mUserInfo.getUserName());
		Log.e("tag", "tasklist+DeliverTaskListActivity" + tasklist.size());
		mlistview_tasklist = (ListView) findViewById(R.id.listview_tasklist);
		mbtn_back_tasklist = (Button) findViewById(R.id.btn_back_tasklist);
		mbtn_back_tasklist.setOnClickListener(this);
		mListReceiver = new TaskListReceiver();
	}

	private void initListAdapter() {
		mDeliverTaskListAdapter = new DeliverTaskListAdapter(
				DeliverTaskListActivity.this, tasklist);
		mlistview_tasklist.setAdapter(mDeliverTaskListAdapter);
		mlistview_tasklist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// if (btn_pushtask.isShown()) {
				// Animation anim = AnimationUtils
				// .loadAnimation(DeliverTaskListActivity.this,
				// R.anim.anim_top);
				// btn_pushtask.startAnimation(anim);
				// anim.setAnimationListener(new AnimationListener() {
				//
				// @Override
				// public void onAnimationStart(
				// Animation animation) {
				//
				// }
				//
				// @Override
				// public void onAnimationRepeat(
				// Animation animation) {
				//
				// }
				//
				// @Override
				// public void onAnimationEnd(
				// Animation animation) {
				// btn_pushtask
				// .setVisibility(View.GONE);
				// }
				// });
				// }

				ArrayList<HashMap<String, String>> listUpdata = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("delivertasknum",
						tasklist.get(position).get("delivertasknum"));
				map.put("delivertaskcount", "0");
				listUpdata.add(map);
				mTaskListDao.updateDeliverTaskCount(
						mUserInfo.getUserDelvorgCode(),
						mUserInfo.getUserName(), listUpdata);
				Intent intent = new Intent(DeliverTaskListActivity.this,
						TaskShowActivity.class);
				intent.putExtra("delivertasknum",
						tasklist.get(position).get("delivertasknum"));
				startActivity(intent);
			}

		});

	}

	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter(Constant.ACTION_DOWN_DATA_OVER);
		registerReceiver(mListReceiver, filter);

		mUserInfo = new UserInfoUtils(DeliverTaskListActivity.this);
		mTaskListDao = DeliverDaoFactory.getInstance().getDeliverTaskListDao(
				DeliverTaskListActivity.this);
		tasklist.clear();
		tasklist = mTaskListDao.queryGatherTaskMessage(
				mUserInfo.getUserDelvorgCode(), mUserInfo.getUserName());
		initListAdapter();

		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(mListReceiver);
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_tasklist:
			DeliverTaskListActivity.this.finish();
			break;

		default:
			break;
		}
	}

	class TaskListReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_DOWN_DATA_OVER.equals(intent.getAction())) {
				tasklist.clear();
				tasklist = mTaskListDao
						.queryGatherTaskMessage(mUserInfo.getUserDelvorgCode(),
								mUserInfo.getUserName());
				mDeliverTaskListAdapter.notifyDataSetChanged();
			}
		}

	}
}

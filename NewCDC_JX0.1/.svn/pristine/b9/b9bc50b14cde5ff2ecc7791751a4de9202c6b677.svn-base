package com.newcdc.activity.delivertask;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MyArrayAdapter;
import android.widget.TextView;

import com.dtr.zxing.activity.CaptureActivity;
import com.newcdc.R;
import com.newcdc.adapter.DefinitionAdapter;
import com.newcdc.adapter.DefinitionAdapter.onNotifyAdapterListener;
import com.newcdc.application.BaseActivity;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.ExpressMailUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.SwipeListView;
import com.newcdc.ui.XFAudio;

public class ManyDlvActivity extends BaseActivity implements OnClickListener {
	private Button mbtn_saoma_activity_showtask;
	private Button btn_back;
	private TextView btn_count;
	private SwipeListView mlistView;
	private DefinitionAdapter mAdapter;
	private List<MessageInfoBean> beans;
	private DeliverDao deliverDao;
	private String results = "";
	private AutoCompleteTextView input;
	private Button shure;
	// private String groupName;
	private ArrayList<MessageInfoBean> mList;
	private Dialog errorDialog;
	private ImageView iv_text = null;
	private ImageView iv_person = null;
	private Button btn_batchoper_activity_showtask = null;
	private ArrayList<String> checkedList;
	private MyReceiver manyReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.many_dlvnot);
		initView();
		initData();
		addListener();

	}

	@SuppressWarnings("static-access")
	private void initData() {
		deliverDao = DeliverDaoFactory.getInstance().getDeliverDao(
				ManyDlvActivity.this);
		checkedList = new ArrayList<String>();
		mList = deliverDao.querySelfGroupMail();
		for (int i = 0; i < mList.size(); i++) {
			checkedList.add(mList.get(i).getMail_num());
		}
		btn_count.setText("(" + checkedList.size() + ")");
		errorDialog = new Dialog(this, R.style.dialogss);
		mAdapter = new DefinitionAdapter(ManyDlvActivity.this, mList, "",
				mlistView.getRightViewWidth(),
				new DefinitionAdapter.IOnItemRightClickListener() {
					@Override
					public void onRightClick(View v, int position) {
						// Toast.makeText(ManyDlvActivity.this, "del " +
						// list.get(position),
						// Toast.LENGTH_SHORT).show();
						// Log.e("tag",
						// list.get(position)+"Position="+position);
						// list.remove(position);
						ArrayList<Integer> idList = new ArrayList<Integer>();
						idList.add(mList.get(position).get_id());
						deliverDao.updateOutSelfGroup(idList);
					}
				});
		showBackGround();
		mAdapter.setListener(new onNotifyAdapterListener() {

			@Override
			public void onNotifyAdapter() {
				btn_count.setText("(" + mAdapter.getData().size() + ")");
				showBackGround();
			}
		});
		mAdapter.visib = true;
		mlistView.setAdapter(mAdapter);
	}

	public void showBackGround() {
		if (mAdapter.getData().size() == 0) {
			iv_person.setVisibility(View.VISIBLE);
			iv_text.setVisibility(View.VISIBLE);
		} else {
			iv_person.setVisibility(View.GONE);
			iv_text.setVisibility(View.GONE);
		}
	}

	private void addListener() {
		mbtn_saoma_activity_showtask.setOnClickListener(this);
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				beans = mAdapter.getData();
				Intent intent = new Intent();
				intent.setClass(ManyDlvActivity.this, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("detail_id", beans.get(arg2).get_id());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		input.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s != null && !"".equals(s.toString())) {
					try {
						ArrayList<String> mailNumberList = deliverDao
								.queryMailNumber_m(s.toString(), checkedList);
						MyArrayAdapter<String> adapter = new MyArrayAdapter<String>(
								ManyDlvActivity.this,
								R.layout.my_autocompleteview, mailNumberList);
						input.setAdapter(adapter);
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s != null && s.length() == 13) {
					// notifyAdapter(s.toString());
					refreshSelfGroup(s.toString());
				}
			}
		});
	}

	@SuppressLint("ServiceCast")
	public void showMarkDialog() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		View v = LayoutInflater.from(ManyDlvActivity.this).inflate(
				R.layout.dialog_dlvorndlv, null);
		final Dialog delOrUpdialog = new Dialog(ManyDlvActivity.this,
				R.style.dialogss);
		delOrUpdialog.setContentView(v, new LayoutParams(
				metric.widthPixels * 15 / 20, LayoutParams.WRAP_CONTENT));
		Button update = (Button) v.findViewById(R.id.tuo);
		Button delete = (Button) v.findViewById(R.id.wtuo);
		delOrUpdialog.setCancelable(true);
		update.setOnClickListener(new OnClickListener() {// 妥投
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManyDlvActivity.this,
						DeliverOkListActivity.class);
				intent.putExtra("fromMany", true);
				startActivity(intent);
				delOrUpdialog.dismiss();
				finish();
			}
		});
		delete.setOnClickListener(new OnClickListener() {// 未妥投
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManyDlvActivity.this,
						DeliverErrorListActivity.class);
				intent.putExtra("fromMany", true);
				startActivity(intent);
				delOrUpdialog.dismiss();
				finish();
			}
		});
		delOrUpdialog.show();

	}

	/**
	 * 对13位邮件号执行操作
	 * 
	 * @param mailNum
	 */
	public void refreshSelfGroup(String mailNum) {
		if (true) {
			List<MessageInfoBean> mail = deliverDao.queryByMailNumber(mailNum);// 根据邮件号在表中查找
			if (mail.size() == 0) {
				showCommitDialog("这个邮件不是你的！！！");
			} else {
				List<MessageInfoBean> tuotouMail = deliverDao// 根据邮件号在表中查找这个邮件号是否是妥投的
						.queryByMailNumberInTuotou(mailNum);
				if (tuotouMail.size() == 1) {
					showCommitDialog("这个邮件已经妥投");
				} else {
					if (deliverDao.mailInGroup(mailNum)) {
						showCommitDialog("这个邮件已经在列表中");
					} else {
						deliverDao.updateToSelfGroup(mailNum);
					}
				}
			}
		}
//		else {
//			showCommitDialog("这不是邮件号");
//		}
		mList = deliverDao.querySelfGroupMail();
		mAdapter.setData(mList);
		mAdapter.notifyDataSetChanged();
		if (mList.size() == 0) {
			findViewById(R.id.iv_text).setVisibility(View.VISIBLE);
			findViewById(R.id.iv_person).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.iv_text).setVisibility(View.GONE);
			findViewById(R.id.iv_person).setVisibility(View.GONE);
		}
		input.setText("");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 111) {
			results = data.getStringExtra("txtResult");
			if (!"".equals(results)) {
				refreshSelfGroup(results);
				results = "";
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initView() {
		btn_batchoper_activity_showtask = (Button) findViewById(R.id.btn_batchoper_activity_showtask);
		iv_text = (ImageView) findViewById(R.id.iv_text);
		iv_person = (ImageView) findViewById(R.id.iv_person);
		btn_back = (Button) findViewById(R.id.btn_back_activity_taskshow);
		btn_count = (TextView) findViewById(R.id.tv_count_activity_taskshow);
		btn_back.setOnClickListener(this);
		mlistView = (SwipeListView) findViewById(R.id.listView_activity_taskshow);
		mbtn_saoma_activity_showtask = (Button) findViewById(R.id.btn_saoma_activity_showtask);
		input = (AutoCompleteTextView) findViewById(R.id.input);// 输入框
		shure = (Button) findViewById(R.id.shure);// 确认未妥投
		shure.setOnClickListener(this);
		XFAudio audio = new XFAudio(ManyDlvActivity.this,
				btn_batchoper_activity_showtask, input);
		audio.toSay();
		manyReceiver = new MyReceiver();
	}

	/**
	 * 邮件异常dialog
	 * 
	 * @param message
	 */
	public void showCommitDialog(String message) {
		View view = getLayoutInflater().inflate(R.layout.dialog_info_manydlv,
				null);
		errorDialog.setContentView(view, new LayoutParams(width * 18 / 20,
				LayoutParams.WRAP_CONTENT));
		errorDialog.setCancelable(true);
		TextView tv_msg = (TextView) view
				.findViewById(R.id.tv_message_dialog_info_manydlv);
		Button btn_positive = (Button) view
				.findViewById(R.id.btn_positive_dialog_info_manydlv);
		tv_msg.setText(message);
		btn_positive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				errorDialog.dismiss();
			}
		});
		errorDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_batchoper_activity_showtask:// 语音
			break;
		case R.id.btn_saoma_activity_showtask:// 扫描
			startActivityForResult(new Intent(ManyDlvActivity.this,
					CaptureActivity.class), 1);
			break;
		case R.id.btn_back_activity_taskshow:// 返回
			this.finish();
			break;
		case R.id.shure:// 确认
			if (mList.size() == 0) {
				showCommitDialog("没有需要提交的邮件");
			} else {
				showMarkDialog();
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(ManyDlvActivity.this);// 启动投递、揽收异步上传服务
		IntentFilter filter = new IntentFilter(Constant.ACTION_BLUTTOOTH_MSG);
		registerReceiver(manyReceiver, filter);
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(manyReceiver);
		super.onPause();
	}
	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			 if (Constant.ACTION_BLUTTOOTH_MSG.equals(intent.getAction())){
				String code = intent.getStringExtra("code");
				if(code != null){
					input.setText(code);
				}
			}
		}

	}

}

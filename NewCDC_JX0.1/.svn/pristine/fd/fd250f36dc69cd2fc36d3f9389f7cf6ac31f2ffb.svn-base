package com.newcdc.activity.delivertask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dtr.zxing.activity.CaptureActivity;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.adapter.DeliverAdapter;
import com.newcdc.adapter.DeliverAdapter.OnNotifySelfListener;
import com.newcdc.adapter.DeliverAdapter.onAddSelfGroupMailListener;
import com.newcdc.adapter.DeliverAdapter.onNotifyAdapterListener;
import com.newcdc.adapter.DeliverAdapter.onSendMessageListener;
import com.newcdc.adapter.GroupPopAdapter;
import com.newcdc.application.BaseActivity;
import com.newcdc.application.MainActivity;
import com.newcdc.asynctask.DeliverAsyncTask;
import com.newcdc.asynctask.sendMessageTask;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.GroupDao;
import com.newcdc.model.GroupBean;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.service.DeliverService;
import com.newcdc.service.DownDCDataService;
import com.newcdc.service.JXAsyncQueueService;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Constant;
import com.newcdc.tools.DragListView;
import com.newcdc.tools.DragListView.OnRefreshLoadingMoreListener;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.ShowToast;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.InfoDialog;
import com.newcdc.ui.ProgressDialog;
import com.newcdc.ui.XFAudio;

/**
 * 下段信息展示
 * 
 * @author zhangfan
 * 
 */
public class TaskShowActivity extends BaseActivity implements OnClickListener {
	private Button btn_sound;
	private static Button btn_group;
	private Button btn_map;
	private Button btn_self_count;
	private Button mbtn_saoma_activity_showtask;
	private Button btn_batchoper_activity_showtask;
	private AutoCompleteTextView autoTv;
	private DragListView mList;
	private View back_layout;
	private static DeliverAdapter mAdapter;
	// private MyArrayAdapter<String> autoAdapter;// 自动提示适配器
	// private ArrayList<String> autoData;
	private LinearLayout layout;
	private ListView listView;
	private Button bt_voiceadd, bt_add;
	private EditText et_group;
	public PopupWindow popupWindow;
	private GroupPopAdapter groupPopAdapter;
	private static List<GroupBean> groupList = new ArrayList<GroupBean>();
	private List<String> groupnameList = new ArrayList<String>();
	private static GroupDao groupDao;
	public static int select_item = -1;
	private boolean isL = false;
	private Dialog DelOrUpdialog, Updatedialog, Delialog;
	private ProgressDialog dialog;
	private List<MessageInfoBean> beans;
	private MyReceiver receiver;
	private static DeliverDao deliverDao;
	private UserInfoUtils user;
	private String orgCode;
	private String username;
	private String results = "";
	// private AutoCompleteTextView mautoText_tab_sl;
	private Button weituotou;
	private Button send_sms;
	private Button toudi;
	private LinearLayout task_bottom;
	private boolean isShow = false;
	private UserInfoUtils uinfo;
	public static boolean isWEI = true;
	public static boolean isSMS = true;
	public static boolean isTOU = true;
	private Dialog infoDialog;
	private LinearLayout ll_other = null;
	private TextView tv_group_num = null;
	private LinearLayout ll_group;
	private ArrayList<Integer> queueTouList;
	private int dealType;// 统计界面跳转过来
	private DeliverDaoFactory daoFactory;
	private Handler handler;
	private String curGroupName;// 当前的分组名
	private View view_bg;
	private Button btn_commit;// 未提交分组的按钮
	private String mFrequence;// 列表数据频次号

	// private View view_query;// 查询条件的view

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_show);
		// daoFactory.equals(savedInstanceState);
		initView();
		initData();
		addListener();
	}

	private void initData() {
		daoFactory = DeliverDaoFactory.getInstance();
		deliverDao = daoFactory.getDeliverDao(getActivity());
		groupDao = daoFactory.getGroupDao(getActivity());
		uinfo = new UserInfoUtils(getApplicationContext());
		mFrequence = getIntent().getStringExtra("delivertasknum") + "";
		handler = new Handler();
		receiver = new MyReceiver();
		dialog = new ProgressDialog(getActivity(), "正在处理");
		infoDialog = new Dialog(this, R.style.dialogss);
		// autoData = new ArrayList<String>();
		setRedCircle();
		DragListView.mLastUpdateTime = SharePreferenceUtilDaoFactory
				.getInstance(getApplicationContext()).getDeliverTime();
		// autoAdapter = new MyArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1, autoData);
		// autoTv.setAdapter(autoAdapter);
		user = new UserInfoUtils(TaskShowActivity.this);
		orgCode = user.getUserDelvorgCode();
		username = user.getUserName();
		dealType = getIntent().getIntExtra("dealType", Constant.DAICHULI);
		switch (dealType) {
		case Constant.TUOTOU:
			curGroupName = "妥投";
			setTitleCount();
			break;
		case Constant.WEITUOTOU:
			curGroupName = "未妥投";
			setTitleCount();
			break;
		case Constant.DAICHULI:
			curGroupName = "待处理";
			setTitleCount();
			break;
		}
		btn_group.setText(curGroupName);
		mAdapter = new DeliverAdapter(TaskShowActivity.this,
				new ArrayList<MessageInfoBean>(), curGroupName);
		setAdapterListener();
		mList.setAdapter(mAdapter);
		showMailGroup(getIntent());
	}

	/**
	 * Adapter设置自定义接口
	 */
	public void setAdapterListener() {
		// 当Adapter刷新自身时调用
		mAdapter.setOnNotifySelfListener(new OnNotifySelfListener() {

			@Override
			public void onNotifySelf() {
				setTitleCount();
				autoTv.setText("");
				notifyAutoAdapter(autoTv.getText().toString());
			}
		});
		// 当添加自定义分组时调用
		mAdapter.setAddSelfGroupMailListener(new onAddSelfGroupMailListener() {

			@Override
			public void onAddSelfGroupMail() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						setRedCircle();
					}
				});
			}
		});
		// 当发送短信后调用
		mAdapter.setSendMessageListener(new onSendMessageListener() {

			@Override
			public void onSendMessage() {
				Editable editable = autoTv.getText();
				String groupName = curGroupName;
				if (editable != null && editable.toString().length() != 0) {
					mAdapter.setData(deliverDao.queryInGroup_m(groupName,
							mFrequence, editable.toString().trim(),
							getActivity()));
					mAdapter.notifyDataSetChanged();
				} else {
					notifyAdapter(groupName);
				}
			}
		});
		// 当Adapter刷新完成之后，可执行操作
		mAdapter.setListener(new onNotifyAdapterListener() {

			@Override
			public void onNotifyAdapter() {
				if (mAdapter.getData().size() == 0) {
					if (!loading) {
						findViewById(R.id.iv_text).setVisibility(View.VISIBLE);
						findViewById(R.id.iv_person)
								.setVisibility(View.VISIBLE);
						findViewById(R.id.iv_text).setBackgroundResource(
								R.drawable.nomail_text);
						findViewById(R.id.iv_person).setBackgroundResource(
								R.drawable.person_normal);
					}
				} else {
					view_bg.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 设置自定义组的邮件个数红点提示
	 */
	public void setRedCircle() {
		int count = deliverDao.querySelfGroupMail().size();
		if (count == 0) {
			btn_self_count.setVisibility(View.GONE);
		} else {
			btn_self_count.setVisibility(View.VISIBLE);
			btn_self_count.setText(count + "");
		}
	}

	/**
	 * 设置title的数字
	 */
	public void setTitleCount() {
		try {
			if (!mFrequence.equals("")) {
				tv_group_num.setText("("
						+ groupDao.queryMailCountByName(curGroupName.trim(),
								mFrequence) + ")");
			} else {
				tv_group_num.setText("("
						+ groupDao.queryMailCountByNameAll(curGroupName.trim())
						+ ")");
			}

		} catch (Exception e) {
		}
	}

	/**
	 * 展示附近分组 用于点击通知之后的跳转
	 */
	public void showMailGroup(Intent intent) {
		if ("notifation".equals(intent.getStringExtra("notifation"))) {
			autoTv.setText("");
			curGroupName = "附近邮件";
			mAdapter.setGroupName(curGroupName);
			btn_group.setText(curGroupName);
		}
		notifyAdapter(curGroupName);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		showMailGroup(intent);
		super.onNewIntent(intent);
	}

	public static boolean loading = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 111) {
			results = data.getStringExtra("txtResult");
			if (!"".equals(results)) {
				new Thread() {
					@Override
					public void run() {
						final List<MessageInfoBean> list = deliverDao
								.queryInGroup_m(curGroupName, mFrequence,
										results, getActivity());
						handler.post(new Runnable() {

							@Override
							public void run() {
								if (list.size() == 0) {
									new InfoDialog(getActivity(),
											"未在当前列表中查找到邮件号为\n<" + results
													+ ">\n的订单").Show();
								} else {
									autoTv.setText(results);
								}
							}
						});
					};
				}.start();

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter(Constant.ACTION_DOWN_DATA_OVER);
		filter.addAction(Constant.ACTION_ASYNCQUEUE);
		filter.addAction(Constant.ACTION_TITLE_COUNT);
		filter.addAction(Constant.ACTION_BLUTTOOTH_MSG);
		filter.addAction(Constant.ACTION_NOTIFY);
		filter.addAction(Constant.ACTION_ONREFRESHCOMPLETE);
		registerReceiver(receiver, filter);
		try {
			if (!"".equals(autoTv.getText().toString().trim())
					&& autoTv.getText().toString().trim() != null) {
				notifyAutoAdapter(autoTv.getText().toString().trim());
			} else {
				notifyAdapter(curGroupName);
			}
		} catch (Exception e) {
		}
		Utils.startIntentService(TaskShowActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(receiver);
		super.onPause();
	}

	private void addListener() {
		mbtn_saoma_activity_showtask.setOnClickListener(this);
		btn_map.setOnClickListener(this);
		back_layout.setOnClickListener(this);
		btn_batchoper_activity_showtask.setOnClickListener(this);
		btn_sound.setOnClickListener(this);
		weituotou.setOnClickListener(this);
		send_sms.setOnClickListener(this);
		toudi.setOnClickListener(this);
		ll_group.setOnClickListener(this);
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utils.isNetworkAvailable(getActivity())) {
					// 点击上传按钮开启单个上传
					if (mAdapter.getData().size() == 0) {
						Toast.makeText(getActivity(), "邮件已经上传完成，无需再上传",
								Toast.LENGTH_SHORT).show();
						Utils.startIntentService(getActivity());
					} else {
						btn_commit.setVisibility(View.GONE);
						Intent deliverService = new Intent(getActivity(),
								JXAsyncQueueService.class);
						deliverService.putExtra("repeatCount", 1);
						startService(deliverService);
						Toast.makeText(getActivity(), "上传服务已开启，正在为您上传数据",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), "网络不好是没办法提交数据的哦",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		view_bg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					reloadDliverData();
				} catch (Exception e) {
					downThreadRun = false;
				}
			}
		});
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				beans = mAdapter.getData();
				MessageInfoBean bean = beans.get(arg2 - 1);
				Intent intent = new Intent();
				intent.setClass(TaskShowActivity.this, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("detail_id", bean.get_id());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mList.setOnRefreshListener(new OnRefreshLoadingMoreListener() {

			@Override
			public void onRefresh() {
				if (Utils.isNetworkAvailable(getActivity())) {
					if (curGroupName.equals("待处理")) {
						DragListView.mLastUpdateTime = SharePreferenceUtilDaoFactory
								.getInstance(getApplicationContext())
								.getDeliverTime();
						try {
							// reloadDliverData();
							refreshTaskMail();
						} catch (Exception e) {
							downThreadRun = false;
						}
					} else {
						mList.onRefreshComplete();
					}
				} else {
					Toast.makeText(getActivity(), "没有网是不是可以获取新数据的哦",
							Toast.LENGTH_SHORT).show();
					mList.onRefreshComplete();
				}
			}

			@Override
			public void onLoadMore() {

			}
		});
		autoTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (autoTv.getText().toString().trim().length() >= 12) {
					autoTv.setText("");
				}
			}
		});

		autoTv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mAdapter.setData(deliverDao.queryInGroup_m(curGroupName,
						mFrequence, autoTv.getText().toString(), getActivity()));
				mAdapter.notifyDataSetChanged();
			}
		});
		autoTv.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				final String input = s.toString();
				if (Utils.stringEmpty(input)) {
					notifyAdapter(curGroupName);
				} else {
					notifyAutoAdapter(input);
				}
			}
		});
		btn_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				select_item = -1;
				showPop();
			}
		});
	}

	public Context getContext() {
		return TaskShowActivity.this;
	}

	private boolean downThreadRun = false;

	public void refreshTaskMail() {
		Toast.makeText(getActivity(), "正在刷新数据···", Toast.LENGTH_SHORT).show();
		startService(new Intent(getContext(), DeliverService.class));
	}

	/**
	 * 起线程重新请求下载下段数据
	 */
	public void reloadDliverData() {
		if (!DownDCDataService.inServer && !downThreadRun) {
			Toast.makeText(getActivity(), "正在刷新数据···", Toast.LENGTH_SHORT)
					.show();
			new Thread() {
				@Override
				public void run() {
					downThreadRun = true;
					boolean request = false;
					request = DeliverAsyncTask.doRequest(getActivity());
					while (request) {
						request = DeliverAsyncTask.doRequest(getActivity());
					}
					handler.post(new Runnable() {

						@Override
						public void run() {
							mList.onRefreshComplete();
							setTitleCount();
							if (!Utils.stringEmpty(autoTv.getText().toString())) {
								autoTv.setText("");
							} else {
								notifyAdapter(btn_group.getText().toString());
							}
							Toast.makeText(getActivity(), "更新完毕",
									Toast.LENGTH_SHORT).show();
							downThreadRun = false;
						}
					});
				};
			}.start();
		} else {
			Toast.makeText(getActivity(), "正在全力更新中...请您耐心等待",
					Toast.LENGTH_SHORT).show();
			mList.onRefreshComplete();
		}
	}

	/**
	 * 刷新自动提示文本内容
	 */
	public void notifyAutoAdapter(final String queryInfo) {
		// autoData.clear();
		new Thread() {
			@Override
			public void run() {
				final List<MessageInfoBean> list = deliverDao.queryInGroup_m(
						curGroupName, mFrequence, queryInfo, getActivity());
				// Log.e("querylist", list.toString() + "");
				// for (int i = 0; i < list.size(); i++) {
				// if (list.get(i).getRcver_contact_phone1() != null) {
				// String s1 = list.get(i).getRcver_contact_phone1();
				// String s2 = list.get(i).getRcver_name();
				// String s3 = list.get(i).getRcver_street_addr();
				// if (s1 != null && s1.contains(queryInfo.toString())) {
				// autoData.add(s1);
				// }
				// if (s2 != null && s2.contains(queryInfo.toString())) {
				// autoData.add(s2);
				// }
				// if (s3 != null && s3.contains(queryInfo.toString())) {
				// autoData.add(s3);
				// }
				// }
				// }
				try {
					handler.post(new Runnable() {

						@Override
						public void run() {
							mAdapter.setData(list);
							mAdapter.setGroupName(queryInfo);
							mAdapter.notifyDataSetChanged();
							// autoAdapter.notifyDataSetChanged();
						}
					});
				} catch (Exception e) {
				}
			};
		}.start();
	}

	private void initView() {
		ll_group = (LinearLayout) findViewById(R.id.ll_group);
		back_layout = findViewById(R.id.back_layout);
		btn_group = (Button) findViewById(R.id.btn_group_activity_taskshow);
		btn_map = (Button) findViewById(R.id.btn_map_activity_taskshow);
		btn_sound = (Button) findViewById(R.id.btn_sound_activity_showtask);
		tv_group_num = (TextView) findViewById(R.id.tv_group_num);
		autoTv = (AutoCompleteTextView) findViewById(R.id.autoTv_taskshow_activity);
		view_bg = findViewById(R.id.bg_activity_task);
		btn_self_count = (Button) findViewById(R.id.btn_selfgroup_mailcount);
		mList = (DragListView) findViewById(R.id.listView_activity_taskshow);
		mbtn_saoma_activity_showtask = (Button) findViewById(R.id.btn_saoma_activity_showtask);
		weituotou = (Button) findViewById(R.id.weituotou);
		send_sms = (Button) findViewById(R.id.send_sms);
		toudi = (Button) findViewById(R.id.toudi);
		task_bottom = (LinearLayout) findViewById(R.id.task_bottom);
		btn_batchoper_activity_showtask = (Button) findViewById(R.id.btn_batchoper_activity_showtask);
		btn_commit = (Button) findViewById(R.id.btn_asyncservice_activity_show);
		// view_query = findViewById(R.id.view_querystyle_activity_show);
		XFAudio audio = new XFAudio(getActivity(), btn_sound, autoTv);
		audio.toSay();
	}

	private Activity getActivity() {
		return TaskShowActivity.this;
	}

	@SuppressWarnings("deprecation")
	protected void showPop() {
		layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
				R.layout.pop_group_dialog, null);
		listView = (ListView) layout.findViewById(R.id.lv_dialog);
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.pop_group_dialog_buttom, null);
		listView.addFooterView(v);
		ll_other = (LinearLayout) v.findViewById(R.id.ll_other);
		// tv_group_other_num = (TextView)
		// v.findViewById(R.id.tv_group_other_num);
		// tv_group_other_num.setText("("
		// + deliverDao.queryForGroup("其他", getActivity()).size() + ")");
		bt_voiceadd = (Button) v.findViewById(R.id.bt_voiceadd);
		bt_add = (Button) v.findViewById(R.id.bt_add);
		et_group = (EditText) v.findViewById(R.id.et_group);
		XFAudio mXFAudio = new XFAudio(getActivity(), bt_voiceadd, et_group);
		mXFAudio.toSay();
		listView.setAdapter(groupPopAdapter);
		putDate();

		popupWindow = new PopupWindow(getActivity());
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow
				.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// showAsDropDown会把里面的view作为参照物，所以要那满屏幕parent
		popupWindow.showAsDropDown(findViewById(R.id.ll_group), -25, 0);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				tv_group_num.setText("(" + groupList.get(arg2).getMailCount()
						+ ")");
				curGroupName = groupList.get(arg2).getGroup_content();
				LogUtils.e("curGroupName=========" + curGroupName);
				btn_group.setText(curGroupName);
				mAdapter.setGroupName(curGroupName);
				String auto = autoTv.getText().toString();
				if (Utils.stringEmpty(auto)) {
					notifyAdapter(curGroupName);
				} else {
					notifyAutoAdapter(auto);
				}
				isShow = false;
				task_bottom.setVisibility(View.GONE);
				// if (arg2 == 1 || arg2 == 2) {// 妥投、未妥投点击之后启动提交任务
				// startService(new Intent(getActivity(),
				// AsyncQueueService.class));
				// }
				if (arg2 == 3) {
					btn_batchoper_activity_showtask.setVisibility(View.GONE);
					btn_commit.setVisibility(View.VISIBLE);
					// view_query.setVisibility(View.GONE);
				} else {
					btn_commit.setVisibility(View.GONE);
					btn_batchoper_activity_showtask.setVisibility(View.VISIBLE);
					// view_query.setVisibility(View.VISIBLE);
				}
				isL = false;
				isLong();
			}
		});
		ll_other.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tv_group_num.setText("("
						+ deliverDao.queryForGroup("其他", mFrequence,
								getActivity()).size() + ")");
				btn_group.setText("其他");
				isL = false;
				isLong();
				mAdapter.setGroupName(curGroupName);
				notifyAdapter("其他");
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 != groupList.size()) {
					isL = true;
					isLong();
					if (!groupList.get(arg2).getGroup_type()
							.equals(Constant.OTHERGROUP + "")) {
						ShowToast.showToast(getActivity(), "点击的分组名不可修改或删除",
								1000);
					} else {
						select_item = arg2;
						groupPopAdapter.notifyDataSetChanged();
						if (DelOrUpdialog == null) {
							DelOrUpdialog = new Dialog(getActivity(),
									R.style.dialogss);
							DelOrUpGroupdialog(arg2);
						} else {
							DelOrUpGroupdialog(arg2);
						}
					}
				}
				return false;
			}
		});

		bt_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (et_group.getText().toString() == null
						|| et_group.getText().toString().trim().length() == 0) {
					ShowToast.showToast(getActivity(), "请输入要添加的分组名", 1000);
				} else {
					// 最多可添加10条分组
					if (groupDao.queryKeys().size() >= 11) {
						ShowToast.showToast(getActivity(),
								"分组数量已达上限，请先删除之后再添加", 1000);
					} else {
						// 新添加的分组名字数量要控制在10个字
						if (et_group.getText().toString().length() <= 10) {
							// 添加分组的时候要判断用户先添加的分组名是不是已经存在的 如果存在的话提示用户 新加分组已经存在
							// 不能继续添加
							clearGroupname();
							if (groupnameList.contains(et_group.getText()
									.toString())) {
								ShowToast.showToast(getActivity(), "新加分组已经存在",
										1000);
							} else {
								new AddGroud().execute(et_group.getText()
										.toString());
								ArrayList<GroupBean> list = new ArrayList<GroupBean>();
								GroupBean bean = new GroupBean("", et_group
										.getText().toString().trim(),
										Constant.OTHERGROUP + "", "", "", "",
										"", "", "0");
								list.add(bean);
								groupDao.insertGroup(list, getActivity());
								// groupDao.insertGroupInfo(et_group.getText()
								// .toString().trim(), "3",
								// Constant.OTHERGROUP + "", "", "", "",
								// "", ",", 0);
								popupWindow.dismiss();
								putDate();
							}
						}
					}
				}
			}
		});
	}

	private void clearGroupname() {
		groupnameList.clear();
		groupnameList = groupDao.queryKeys();
	}

	/**
	 * 分组列表数据刷新
	 */
	private void putDate() {
		if (!dialog.isShowing()) {
			dialog.toShow();
		}
		new Thread() {
			@Override
			public void run() {
				groupList = groupDao.queryKeysByDiatance(mFrequence);
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (null == groupPopAdapter) {
							groupPopAdapter = new GroupPopAdapter(
									getActivity(), groupList);
							listView.setAdapter(groupPopAdapter);
						} else {
							groupPopAdapter.setnotifyDataSetChanged(groupList);
						}
						if (dialog.isShowing()) {
							dialog.toDimiss();
						}
					}
				});
			};
		}.start();

	}

	/**
	 * 删除还是修改分组名的Dialog
	 * */
	protected void DelOrUpGroupdialog(final int arg2) {
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_delorup_group, null);
		DelOrUpdialog.setContentView(v, new LayoutParams(
				(metric.widthPixels) * 18 / 20, LayoutParams.WRAP_CONTENT));
		Button update = (Button) v.findViewById(R.id.info_update);
		Button delete = (Button) v.findViewById(R.id.info_del);
		DelOrUpdialog.setCancelable(true);
		update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DelOrUpdialog.dismiss();
				if (Updatedialog == null) {
					Updatedialog = new Dialog(getActivity(), R.style.dialogss);
					showUpdateGroupDialog(arg2);
				} else {
					showUpdateGroupDialog(arg2);
				}

			}
		});
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DelOrUpdialog.dismiss();
				if (Delialog == null) {
					Delialog = new Dialog(getActivity(), R.style.dialogss);
					delGroup(arg2);
				} else {
					delGroup(arg2);
				}
			}
		});
		DelOrUpdialog.show();
	}

	/**
	 * 修改分组名字的dialog
	 */
	private void showUpdateGroupDialog(final int arg2) {
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_addgroup, null);
		Updatedialog.setContentView(v, new LayoutParams(
				(metric.widthPixels) * 18 / 20, LayoutParams.WRAP_CONTENT));
		Updatedialog.setCancelable(true);
		final EditText et = (EditText) v
				.findViewById(R.id.editText_dialog_addgroup);
		et.setText(groupList.get(arg2).getGroup_content());
		v.findViewById(R.id.btn_commit_dialog_addgroup).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String trim = et.getText().toString();
						if (trim != null && trim.trim().length() != 0) {
							groupDao.updateGroupName(groupList.get(arg2)
									.getSid(), trim.trim(), getActivity());// 本地更改
							Updatedialog.dismiss();
							putDate();
							popupWindow.dismiss();
							clearGroupname();
							if (!groupnameList.contains(btn_group.getText())) {
								curGroupName = trim.trim();
								btn_group.setText(curGroupName);
							}
							new UpdateGroud().execute(groupList.get(arg2)
									.getSid(), trim);//
						} else {
							ShowToast.showToast(getActivity(), "内容不可为空！", 1000);
						}
					}
				});
		v.findViewById(R.id.btn_cancel_dialog_addgroup).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Updatedialog.dismiss();
					}
				});
		Updatedialog.show();
	}

	/**
	 * 删除分组名的Dialog
	 * */
	private void delGroup(final int arg2) {
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_deletegroup, null);
		Delialog.setContentView(v, new LayoutParams(
				(metric.widthPixels) * 18 / 20, LayoutParams.WRAP_CONTENT));
		Delialog.setCancelable(true);
		v.findViewById(R.id.info_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Delialog.dismiss();
					}
				});
		v.findViewById(R.id.info_sure).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Delialog.dismiss();
						// 要删除分组的话 要有sid这个参数
						new DelGroud().execute(groupList.get(arg2).getSid());//
						// 删除分组
						groupDao.deleteGroupBySid(groupList.get(arg2).getSid());// 删除本地数据库中的分组
						putDate();
						popupWindow.dismiss();
						clearGroupname();
						if (!groupnameList.contains(btn_group.getText())) {
							curGroupName = groupList.get(0).getGroup_content();
							btn_group.setText(curGroupName);
						}
						notifyAdapter(curGroupName);
					}
				});
		Delialog.show();
	}

	/**
	 * sid Y 记录唯一标识
	 * 
	 * */
	class DelGroud extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject result = null;
			try {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("sid", params[0]));
				result = new JSONObject(
						NetHelper.doPost(Global.DELGROUP, pairs));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (null != dialog)
				dialog.toDimiss();
			try {
				if (null != result) {
					if ("1".equals(result.get("result"))) {// 成功
						ShowToast.showToast(getActivity(), result
								.getJSONObject("body").getString("success"),
								1000);
					} else {
						ShowToast.showToast(getActivity(), "删除提交服务器未成功，已做本地更改",
								1000);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity(), "请稍后");
			dialog.toShow();
		}
	}

	/**
	 * sid Y 记录唯一标识 express_company_name 快递公司名称 org_code 机构号 employee_no 工号
	 * group_content Y 分组内容 group_type 分组类型
	 * 
	 * */
	class UpdateGroud extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject result = null;
			try {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("sid", params[0]));
				pairs.add(new BasicNameValuePair("express_company_code", ""));//
				pairs.add(new BasicNameValuePair("express_company_name", ""));//
				pairs.add(new BasicNameValuePair("org_code", ""));//
				pairs.add(new BasicNameValuePair("employee_no", ""));//
				pairs.add(new BasicNameValuePair("group_content", params[1]));//
				pairs.add(new BasicNameValuePair("group_type", ""));//
				result = new JSONObject(NetHelper.doPost(Global.UPDATEGROUP,
						pairs));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (null != dialog)
				dialog.toDimiss();
			try {
				if (null != result) {
					if ("1".equals(result.get("result"))) {// 成功
						ShowToast.showToast(getActivity(), result
								.getJSONObject("body").getString("success"),
								1000);
					} else {
						ShowToast.showToast(getActivity(), "更改提交服务器未成功，已做本地更改",
								1000);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity(), "请稍后");
			dialog.toShow();
		}
	}

	/**
	 * express_company_code Y 快递公司代码 express_company_name 快递公司名称 org_code Y 机构号
	 * employee_no Y 工号 group_content Y 分组内容 group_type Y 分组类型（默认传1）
	 * 
	 * */
	class AddGroud extends AsyncTask<String, Void, JSONObject> {
		private String groupName;

		@Override
		protected JSONObject doInBackground(String... params) {
			groupName = params[0];
			JSONObject result = null;
			try {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("express_company_code", "EMS"));//
				pairs.add(new BasicNameValuePair("express_company_name", ""));//
				pairs.add(new BasicNameValuePair("org_code", uinfo
						.getUserDelvorgCode()));//
				pairs.add(new BasicNameValuePair("employee_no", uinfo
						.getUserName()));//
				pairs.add(new BasicNameValuePair("group_content", params[0]));//
				pairs.add(new BasicNameValuePair("group_type", "1"));
				result = new JSONObject(
						NetHelper.doPost(Global.ADDGROUP, pairs));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (null != dialog)
				dialog.toDimiss();
			try {
				if (null != result) {
					if ("1".equals(result.get("result"))) {// 成功
						String sid = result.getJSONObject("body")
								.getJSONObject("success").getString("sid");
						groupDao.setSid(sid, groupName);// 将sid设置到对应的分组上
						ShowToast.showToast(getActivity(), "添加成功", 1000);
					} else {
						ShowToast
								.showToast(
										getActivity(),
										result.getJSONObject("body").getString(
												"error"), 1000);
					}
				} else {
					ShowToast.showToast(getActivity(), "上传服务器失败，已做本地添加", 1000);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity(), "请稍后");
			dialog.toShow();
		}
	}

	/**
	 * 判断是长按键还是点击按键
	 * */
	protected void isLong() {
		if (null != popupWindow) {
			if (!isL) {
				popupWindow.dismiss();
				groupPopAdapter = null;
			}
		}
	}

	ProgressDialog msgDialog;

	@Override
	public void onClick(final View v) {
		List<MessageInfoBean> data = mAdapter.getData();
		switch (v.getId()) {
		case R.id.btn_map_activity_taskshow:
			Intent intent = new Intent(TaskShowActivity.this,
					NearTaskMapActivity.class);
			startActivity(intent);
			break;
		case R.id.weituotou:// 确认未妥投/自定义分组
			// if (!Utils.isNetworkAvailable(getActivity())) {
			// Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT)
			// .show();
			// } else {
			Intent intent2 = new Intent(TaskShowActivity.this,
					ManyDlvActivity.class);
			intent2.putExtra("groupName", curGroupName);
			startActivity(intent2);
			// }
			break;
		case R.id.send_sms:// 批量发短信
			if (data.size() == 0) {
				Toast.makeText(getActivity(), "没有需要处理的邮件", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (!Utils.isNetworkAvailable(getActivity())) {
					Toast.makeText(getActivity(), "离线状态不可以发送短信哦",
							Toast.LENGTH_SHORT).show();
				} else {
					ArrayList<Integer> idList = new ArrayList<Integer>();
					for (int i = 0; i < data.size(); i++) {
						MessageInfoBean bean = data.get(i);
						if (bean.getMsg_user() == Constant.MSG_NEVER_BY_USER) {
							idList.add(bean.get_id());
						}
					}
					showInfoDialogMsg(
							"批量给" + idList.size() + "个客户发送短信，确定要发送吗?",
							v.getId(), idList);
				}
			}
			break;
		case R.id.toudi:// 批量投递
			if (data.size() == 0) {
				Toast.makeText(getActivity(), "没有需要处理的邮件", Toast.LENGTH_SHORT)
						.show();
			} else {
				showMarkDialog();
			}
			break;
		case R.id.btn_batchoper_activity_showtask:
			btn_commit.setVisibility(View.GONE);
			if ("妥投".equals(curGroupName.trim())) {
				Toast.makeText(getActivity(), "没有可执行的操作", Toast.LENGTH_SHORT)
						.show();
			} else if ("未妥投".equals(curGroupName.trim())) {
				Toast.makeText(getActivity(), "没有可执行的操作", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (DownDCDataService.inServer) {
					Toast.makeText(getActivity(), "正在更新数据，请稍等...",
							Toast.LENGTH_SHORT).show();
				} else {
					if (isShow) {
						task_bottom.setVisibility(View.GONE);
						isShow = false;
					} else {
						task_bottom.setVisibility(View.VISIBLE);
						isShow = true;
					}
				}
			}
			// showtask_do.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_saoma_activity_showtask:
			startActivityForResult(new Intent(TaskShowActivity.this,
					CaptureActivity.class), 1);
			break;
		case R.id.back_layout:
			finish();
			break;
		case R.id.ll_group:
			select_item = -1;
			showPop();
			break;
		case R.id.btn_sound_activity_showtask:

			break;
		}

	}

	// //////////////
	@SuppressLint("ServiceCast")
	public void showMarkDialog() {
		initQueueList();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		View v = LayoutInflater.from(TaskShowActivity.this).inflate(
				R.layout.dialog_dlvorndlv, null);
		final Dialog delOrUpdialog = new Dialog(TaskShowActivity.this,
				R.style.dialogss);
		delOrUpdialog.setContentView(v, new LayoutParams(
				metric.widthPixels * 15 / 20, LayoutParams.WRAP_CONTENT));
		Button update = (Button) v.findViewById(R.id.tuo);
		Button delete = (Button) v.findViewById(R.id.wtuo);
		delOrUpdialog.setCancelable(true);
		update.setOnClickListener(new OnClickListener() {// 妥投
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TaskShowActivity.this,
						DeliverOkListActivity.class);
				intent.putExtra("groupName", curGroupName);
				intent.putExtra("frequence", mFrequence);
				Editable editable = autoTv.getText();
				if (editable != null) {
					String queryInfo = editable.toString();
					boolean empty = Utils.stringEmpty(queryInfo);
					if (!empty) {
						intent.putExtra("queryInfo", queryInfo);
					}
				}
				startActivity(intent);
				delOrUpdialog.dismiss();
			}
		});
		delete.setOnClickListener(new OnClickListener() {// 未妥投
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TaskShowActivity.this,
						DeliverErrorListActivity.class);
				intent.putExtra("groupName", curGroupName);
				intent.putExtra("frequence", mFrequence);
				Editable editable = autoTv.getText();
				if (editable != null) {
					String queryInfo = editable.toString();
					boolean empty = Utils.stringEmpty(queryInfo);
					if (!empty) {
						intent.putExtra("queryInfo", queryInfo);
					}
				}
				startActivity(intent);
				delOrUpdialog.dismiss();
			}
		});
		delOrUpdialog.show();

	}

	// /**
	// * 批量投递dialog
	// *
	// * @param hanrong
	// */
	// public void showInfoDialog(String allmessage, String getmessage,
	// String commonmessage, final int viewId,
	// final ArrayList<Integer> idList) {
	// View view = getLayoutInflater().inflate(R.layout.dialog_info_taskshow,
	// null);
	// infoDialog.setContentView(view, new LayoutParams(width * 18 / 20,
	// LayoutParams.WRAP_CONTENT));
	// infoDialog.setCancelable(true);
	// TextView tv_msg = (TextView) view
	// .findViewById(R.id.tv_message_dialog_info_taskshow);
	// TextView tv_get = (TextView) view
	// .findViewById(R.id.tv_message_dialog_info_taskshow_get);
	// TextView tv_common = (TextView) view
	// .findViewById(R.id.tv_message_dialog_info_taskshow_common);
	// Button btn_positive = (Button) view
	// .findViewById(R.id.btn_positive_dialog_info_taskshow);
	// Button btn_negtive = (Button) view
	// .findViewById(R.id.btn_negtive_dialog_info_taskshow);
	// tv_msg.setText(allmessage);
	// tv_get.setText(getmessage);
	// tv_common.setText(commonmessage);
	// btn_positive.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// infoDialog.dismiss();
	// switch (viewId) {
	// case R.id.send_sms:// 批量发短信
	// break;
	//
	// case R.id.toudi:// 批量投递
	// // if (!Utils.isNetworkAvailable(getActivity())) {
	// // Toast.makeText(getActivity(), "请检查网络",
	// // Toast.LENGTH_SHORT).show();
	// // } else {
	// task_bottom.setVisibility(View.GONE);
	// isShow = false;
	// // 把选中的条目list置为妥投队列
	// Intent intent = new Intent(getActivity(),
	// DeliverOkListActivity.class);
	// Bundle bundle = new Bundle();
	// bundle.putSerializable("idList", idList);
	// intent.putExtras(bundle);
	// startActivity(intent);
	// // }
	// break;
	// }
	// }
	// });
	// btn_negtive.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// infoDialog.dismiss();
	// }
	// });
	// infoDialog.show();
	// }

	/**
	 * 批量短信dialog
	 * 
	 * @param hanrong
	 */
	public void showInfoDialogMsg(String message, final int viewId,
			final ArrayList<Integer> idList) {
		View view = getLayoutInflater().inflate(
				R.layout.dialog_info_taskshowmsg, null);
		infoDialog.setContentView(view, new LayoutParams(width * 18 / 20,
				LayoutParams.WRAP_CONTENT));
		infoDialog.setCancelable(true);
		TextView tv_msg = (TextView) view.findViewById(R.id.dialog_msg);
		Button btn_positive = (Button) view
				.findViewById(R.id.btn_positive_dialog_info_taskshow_msg);
		Button btn_negtive = (Button) view
				.findViewById(R.id.btn_negtive_dialog_info_taskshow_msg);
		tv_msg.setText(message);
		btn_positive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				infoDialog.dismiss();
				switch (viewId) {
				case R.id.send_sms:// 批量发短信
					if (!Utils.isNetworkAvailable(getActivity())) {
						Toast.makeText(getActivity(), "离线状态不可以发送短信哦",
								Toast.LENGTH_SHORT).show();
					} else {
						task_bottom.setVisibility(View.GONE);
						isShow = false;
						msgDialog = new ProgressDialog(getActivity(), "正在发送···");
						msgDialog.setCanCalcelable(false);
						msgDialog.toShow();
						new Thread() {
							@Override
							public void run() {
								String result = sendMessageTask.upload(idList,
										getActivity());
								final String[] split = result.split(",");
								handler.post(new Runnable() {

									@Override
									public void run() {
										msgDialog.toDimiss();
										notifyAdapter(curGroupName);
										showToast("共发送：" + split[0] + "条短信，成功："
												+ split[1] + "条");
									}
								});
							}
						}.start();

						// new sendMessageTask(new onPostExecuteListener() {
						//
						// @Override
						// public void onPostExecute(String result) {
						// msgDialog.toDimiss();
						// String[] split = result.split(",");
						// showToast("共发送：" + split[0] + "条短信，成功："
						// + split[1] + "条");
						// }
						// }, new onPreExecuteListener() {
						//
						// @Override
						// public void onPreExecute() {
						// msgDialog = new ProgressDialog(getActivity(),
						// "正在发送···");
						// msgDialog.toShow();
						// }
						// }, TaskShowActivity.this, idList).execute();
					}
					break;
				}
			}
		});
		btn_negtive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				infoDialog.dismiss();
			}
		});
		infoDialog.show();
	}

	/**
	 * 刷新界面
	 */
	public void notifyAdapter(final String groupName) {
		LogUtils.e("groupName====" + groupName);
		try {
			final Handler handler = new Handler();
			mAdapter.setData(new ArrayList<MessageInfoBean>());
			mAdapter.notifyDataSetChanged();
			findViewById(R.id.iv_text).setVisibility(View.GONE);
			findViewById(R.id.iv_person).setVisibility(View.VISIBLE);
			findViewById(R.id.iv_person).setBackgroundResource(
					R.drawable.onload);
			final ProgressDialog progressDialog = new ProgressDialog(
					getActivity(), "请稍等");
			if (!progressDialog.isShowing()) {
				progressDialog.toShow();
			}
			new Thread() {
				@Override
				public void run() {
					final ArrayList<MessageInfoBean> data = (ArrayList<MessageInfoBean>) deliverDao
							.queryForGroup(groupName, mFrequence,
									TaskShowActivity.this);
					LogUtils.e(groupName + "  查询出来的数量  " + data.size()
							+ "  频次：  " + mFrequence);
					try {
						handler.post(new Runnable() {

							@Override
							public void run() {
								setTitleCount();
								mAdapter.setData(data);
								mAdapter.notifyDataSetChanged();
								if (mAdapter.getData().size() == 0) {
									if (!loading) {
										findViewById(R.id.iv_text)
												.setVisibility(View.VISIBLE);
										findViewById(R.id.iv_person)
												.setVisibility(View.VISIBLE);
										findViewById(R.id.iv_text)
												.setBackgroundResource(
														R.drawable.nomail_text);
										findViewById(R.id.iv_person)
												.setBackgroundResource(
														R.drawable.person_normal);
									}
								} else {
									view_bg.setVisibility(View.GONE);
								}
								if (progressDialog.isShowing()) {
									progressDialog.toDimiss();
								}
							}
						});
					} catch (Exception e) {
					}
				};
			}.start();
		} catch (Exception e) {
		}
	}

	public void initQueueList() {
		List<MessageInfoBean> data = mAdapter.getData();
		// moneyCount = 0;
		queueTouList = new ArrayList<Integer>();
		for (int i = 0; i < data.size(); i++) {
			MessageInfoBean bean = data.get(i);
			if (bean.getDealResult() != Constant.TUOTOU) {
				// 找出不是妥投的邮件
				queueTouList.add(bean.get_id());
			}
		}
		// all = queueTouList.size();// 总数
		// for (int i = 0; i < data.size(); i++) {
		// String money = data.get(i).getMoney();
		// if (!"0.0".equals(money)) {
		// moneyCount++;
		// }
		// }
	}

	/**
	 * 广播接收:数据下载完毕刷新界面
	 * 
	 */
	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_DOWN_DATA_OVER.equals(intent.getAction())) {
				if (deliverDao.querybesidesTuoTou().size() == 0) {
					findViewById(R.id.iv_text).setVisibility(View.VISIBLE);
					findViewById(R.id.iv_person).setVisibility(View.VISIBLE);
					findViewById(R.id.iv_text).setBackgroundResource(
							R.drawable.nomail_text);
					findViewById(R.id.iv_person).setBackgroundResource(
							R.drawable.person_normal);
				} else {
					view_bg.setVisibility(View.GONE);
				}
				if (intent.getStringExtra("message") != null
						&& !"null".equals(intent.getStringExtra("message"))) {
					Toast.makeText(getActivity(),
							intent.getStringExtra("message"),
							Toast.LENGTH_SHORT).show();
				}
				if (intent.getStringExtra("sendMessage") != null
						&& !"null".equals(intent.getStringExtra("message"))) {
					Toast.makeText(getActivity(),
							intent.getStringExtra("sendMessage"),
							Toast.LENGTH_SHORT).show();
					if (msgDialog != null && msgDialog.isShowing()) {
						msgDialog.toDimiss();
					}
				}
				mList.onRefreshComplete();
				notifyAdapter(curGroupName);
			} else if (Constant.ACTION_ASYNCQUEUE.equals(intent.getAction())) {
				autoTv.setText("");
				// notifyAdapter(curGroupName);
			} else if (Constant.ACTION_ONREFRESHCOMPLETE.equals(intent
					.getAction())) {
				mList.onRefreshComplete();
			} else if (Constant.ACTION_TITLE_COUNT.equals(intent.getAction())) {
				setTitleCount();
			} else if (Constant.ACTION_BLUTTOOTH_MSG.equals(intent.getAction())) {
				String code = intent.getStringExtra("code");
				if (code != null) {
					autoTv.setText(code);
				}
			} else if (Constant.ACTION_NOTIFY.equals(intent.getAction())) {
				int result = intent.getIntExtra("result", -1);
				if (result == 1) {
					notifyAdapter(curGroupName);
				}
			}

		}
	}

	public static String mobileNum;// 通话结束后，获取的电话号码，在Adapter中给这个变量赋值
	public static int msg_id;// 要发送短信的邮件号的主键，从Adapter获取

	@Override
	protected void onRestart() {
		if (mobileNum != null) {
			// 通话结束之后向服务器发送通话信息
			ContentResolver resolver = MainActivity.resolver;
			Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, null,
					"number=?", new String[] { mobileNum }, null);
			if (cursor.moveToLast()) {
				String duration = cursor.getString(cursor
						.getColumnIndex("duration"));
				if (Integer.parseInt(duration) > 0) {
					deliverDao.addCallTime_PhoneNum(mobileNum);
				}
				boolean beSend = sendMessage(duration);
				if (beSend) {
					send();
				}
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("dlvorgcode", orgCode));
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("type", "2"));
				params.add(new BasicNameValuePair("mobile", mobileNum));
				params.add(new BasicNameValuePair("message", ""));
				params.add(new BasicNameValuePair("longitude", BaiduGpsContants
						.getInstance().getLng()));
				params.add(new BasicNameValuePair("latitude", BaiduGpsContants
						.getInstance().getLat()));
				params.add(new BasicNameValuePair("address", BaiduGpsContants
						.getInstance().getAddressStr()));
				params.add(new BasicNameValuePair("record_time", duration));
				if (Integer.parseInt(duration) == 0) {
					params.add(new BasicNameValuePair("status", "0"));
				} else {
					params.add(new BasicNameValuePair("status", "1"));
				}
				final List<NameValuePair> params2 = params;
				try {
					new Thread() {
						public void run() {
							try {
								NetHelper.doPost(Global.CALLINFO_URL, params2);
							} catch (Exception e) {
								e.printStackTrace();
							}
						};
					}.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		mobileNum = null;
		if (!"".equals(autoTv.getText().toString().trim())
				&& autoTv.getText().toString().trim() != null) {
			notifyAutoAdapter(autoTv.getText().toString().trim());
		} else {
			mAdapter.setData(deliverDao.querybesidesTuoTou());
			mAdapter.notifyDataSetChanged();
		}
		new Thread() {
			@Override
			public void run() {
				groupDao.updateDealMailCount(getContext());
				groupDao.updateUncommitMailCount(getContext());
				groupDao.updatePaymentGroup(getContext());
			};
		}.start();
		setRedCircle();
		super.onRestart();
	}

	/**
	 * 是否给当前通话对象发送短信；当通话时长为0时，发送
	 */
	private boolean sendMessage(String duration) {
		int callTime = Integer.parseInt(duration);// 通话时长
		int msgCount = deliverDao.queryMsgTime(msg_id);// 是否给该邮件发送过短信
		// 通话时长和发短信次数均为0时，自动发送短信
		if (callTime == 0 && msgCount == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 发送短信
	 */
	public void send() {
		new Thread() {
			@Override
			public void run() {
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(msg_id);
				String result = sendMessageTask.upload(list, getActivity());
				final String[] split = result.split(",");
				try {
					handler.post(new Runnable() {

						@Override
						public void run() {
							if ("1".equals(split[1])) {
								notifyAdapter(curGroupName);
							}
						}
					});
				} catch (Exception e) {
				}
			};
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isWEI = true;
		isSMS = true;
		isTOU = true;
	}

	/**
	 * 
	 * 退出隐藏 Lion 2014-12-29
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isShow) {
				isShow = false;
				task_bottom.setVisibility(View.GONE);
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

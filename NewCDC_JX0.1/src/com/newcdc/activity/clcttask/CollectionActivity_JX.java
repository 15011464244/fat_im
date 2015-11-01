package com.newcdc.activity.clcttask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.dtr.zxing.activity.CaptureActivity;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.adapter.DefinitionAdapter.IOnItemRightClickListener;
import com.newcdc.application.BaseActivity;
import com.newcdc.asynctask.GatherAsyncTask;
import com.newcdc.asynctask.GatherAsyncTask.onPostExecuteListener_Gather;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.model.GatherInfoBean;
import com.newcdc.model.GatherTableBean;
import com.newcdc.model.JxClctBean;
import com.newcdc.model.PaymentMoneyBean;
import com.newcdc.model.fastbean;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Constant;
import com.newcdc.tools.DragListView;
import com.newcdc.tools.DragListView.OnRefreshLoadingMoreListener;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.InfoDialog;
import com.newcdc.ui.ProgressDialog;
import com.newcdc.ui.SwipeListView;
import com.newcdc.ui.XFAudio;

/**
 * 江西揽收任务首页
 * 
 * @author liunannan
 * 
 */
public class CollectionActivity_JX extends BaseActivity {
	private DragListView listView = null;
	private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
	public boolean isCheckUp = true;
	/**
	 * 派单揽收 有打电话发短信揽收的按钮
	 * */
	private PaiLanAdapter mPaiLanAdapter = null;
	/**
	 * 无派单揽收 没有打电话发短信揽收的按钮
	 * */
	private WuPaiLanAdapter mWuPaiLanAdapter;
	private Intent intent;
	private Button nCollection, collection_back;
	TextView btn_group_activity_taskshow, tv_group_num;
	private Button sound;
	private Button collection_scan;
	private AutoCompleteTextView searchtext;
	private List<String> list; // 保存选中的条目的电话号码
	private Button btn_map_collection = null;
	private Gather_MsgDao gatherDao;
	private LocationReceiver receiver;
	private MyReceiver uploadReceiver;
	private String nullCollection = "danhao";
	private Dialog smsDialog;
	private View layout;
	private List<GatherTableBean> beans = new ArrayList<GatherTableBean>();
	private String delvorgcode, username;
	private ImageView iv_text, iv_person;
	private Dialog dialog;
	private View view;
	public PopupWindow popupWindow;
	private LinearLayout ll_group;
	private SwipeListView swipeListView;
	private List<JxClctBean> jxClctBeans = new ArrayList<JxClctBean>();// 江西揽收数据
	// private FastLanDao fastLanDao;// 揽投
	private String orgCode, deviceId, realname, postcode, mobile;
	private int m_posion = 0;
	private List<GatherInfoBean> gatherBeans;// 缴款实体类
	private int mRightWidth = 100;
	private IOnItemRightClickListener mListener = null;
	private int gatherCount = 0;// 揽收个数
	private int dealType;
	private Button btn_beginto_update = null;// 是否上传的按钮

	// private Timer timer = new Timer();

	// private TimerTask timerTask;

	// private boolean nolan = true;// 是否未揽收

	// private Handler mHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// if (msg.what == 1) {
	// if (dataList.size() > 0) {
	// mPaiLanAdapter.notifyDataSetChanged();
	// }
	// }
	// }
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		UserInfoUtils user = new UserInfoUtils(this);
		delvorgcode = user.getUserDelvorgCode();// 机构号
		username = user.getUserName();// 用户的工号
		realname = user.getRealname();
		mobile = user.getMobile();
		dealType = Constant.NOCOLLECTION;// 默认是待处理
		dealType = getIntent().getIntExtra("dealType", Constant.NOCOLLECTION);// 这是传过来的查看状态
		initView();// 初始化控件
		// 默认是派单列表
		// 按照分组刷新列表
		refGroup();
		addListener();
		// timerTask = new TimerTask() {
		// @Override
		// public void run() {
		// Message message = Message.obtain();
		// message.what = 1;
		// mHandler.sendMessage(message);
		// }
		// };
		// timer.schedule(timerTask, 1000, 1000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// timer.cancel();
	}

	// 刷新分组
	private void refGroup() {
		switch (dealType) {
		case Constant.COLLECTION: // 已揽收
			// swipeListView.setRightViewWidth(300);//之后如果需要删除的时候用
			swipeListView.setRightViewWidth(0);
			reflanshoudata("已揽收", gatherCount);
			showBack2();
			btn_beginto_update.setVisibility(View.GONE);
			break;
		case Constant.NOUPDATA: // 未上传
			swipeListView.setRightViewWidth(0);
			reflanshoudata("未上传", gatherCount);
			showBack2();
			btn_beginto_update.setVisibility(View.VISIBLE);// 是否上传的按钮
			break;
		case Constant.NOCOLLECTION:// 待处理
			swipeListView.setRightViewWidth(300);
			repaidliebiao();
			showBack();
			btn_beginto_update.setVisibility(View.GONE);
			btn_group_activity_taskshow.setText("待处理");
			tv_group_num.setText("(" + dataList.size() + ")" + "");
			break;
		}
	}

	// 刷新揽收列表
	private void reflanshoudata(String str, int count) {
		// 查询出数据
		if (dealType == Constant.COLLECTION) {// 已揽收
			jxClctBeans = DeliverDaoFactory.getInstance()
					.getJxClctDao(getApplicationContext())
					.queryJxClctMessage_All(delvorgcode, username);
		} else if (dealType == Constant.NOUPDATA) {// 未上传
			jxClctBeans = DeliverDaoFactory.getInstance()
					.getJxClctDao(getApplicationContext())
					.queryJxClctMessage(delvorgcode, username, "0");
		}
		count = jxClctBeans.size();
		notifyAutoCountLanAdapter("");
		btn_group_activity_taskshow.setText(str);
		tv_group_num.setText("(" + count + ")");
	}

	// 派单信息查询
	private void initData(String str) {
		beans.clear();
		// 更改推送状态
		List<GatherTableBean> IsShowbeans = new ArrayList<GatherTableBean>();
		IsShowbeans = gatherDao.orderByIsShow(delvorgcode, username);
		int size = IsShowbeans.size();
		for (int i = 0; i < size; i++) {
			gatherDao.updateIsShow(IsShowbeans.get(i).get_id());
		}
		beans = gatherDao.orderByDistance_Query(str, delvorgcode, username);
		int beansSize = beans.size();
		LogUtils.e("未揽收件的数量----" + beansSize + "");
		dataList = new ArrayList<Map<String, String>>();
		for (int a = 0; a < beansSize; a++) {
			gatherDao.updateIsShow(beans.get(a).get_id());
			Map<String, String> map = new HashMap<String, String>();
			map.put("phone", beans.get(a).getPhone());
			map.put("clientname", beans.get(a).getClientname());
			map.put("address", beans.get(a).getAddress());
			map.put("senderProv", beans.get(a).getSenderProv());
			map.put("senderCity", beans.get(a).getSenderCity());
			map.put("senderCounty", beans.get(a).getSenderCounty());
			map.put("receiverName", beans.get(a).getReceiverName());
			map.put("receiverMobile", beans.get(a).getReceiverMobile());
			map.put("receiverProv", beans.get(a).getReceiverProv());
			map.put("receiverCity", beans.get(a).getReceiverCity());
			map.put("receiverCounty", beans.get(a).getReceiverCounty());
			map.put("senderProvCode", beans.get(a).getSenderProvCode());
			map.put("senderCityCode", beans.get(a).getSenderCityCode());
			map.put("senderCountyCode", beans.get(a).getSenderCountyCode());
			map.put("receiverProvCode", beans.get(a).getReceiverProvCode());
			map.put("receiverCityCode", beans.get(a).getReceiverCityCode());
			map.put("receiverCountyCode", beans.get(a).getReceiverCountyCode());
			map.put("receiverStreet", beans.get(a).getReceiverStreet());
			map.put("orderWeight", beans.get(a).getOrderWeight());
			map.put("payment", beans.get(a).getPayment());
			map.put("taskAllotTime", beans.get(a).getTaskAllotTime());
			map.put("cnday", beans.get(a).getCnday());
			map.put("createtime", beans.get(a).getCreatetime());
			map.put("reservenum", beans.get(a).getReservenum());
			map.put("companyid", beans.get(a).getCompanyid());
			map.put("remind", beans.get(a).getRemind());
			map.put("serviceType", beans.get(a).getMsg_typecode());
			// map.put("sendType", beans.get(a).getDevice_number());
			map.put("userSid", beans.get(a).getMsg_id());
			map.put("userIntegral", beans.get(a).getMsg_code());
			map.put("userValidIntegral", beans.get(a).getUserValidIntegral());
			map.put("sendType", beans.get(a).getSendType());
			map.put("startTime", beans.get(a).getStartTime());
			// LogUtils.e("taskAllotTime--------------"
			// + beans.get(a).getTaskAllotTime());
			LogUtils.e("remind--------------" + beans.get(a).getRemind());
			dataList.add(map);
		}
		mPaiLanAdapter = new PaiLanAdapter(dataList);
		listView.setAdapter(mPaiLanAdapter);
	}

	// 揽收信息查询
	protected void notifyAutoCountLanAdapter(String str) {
		if (dealType == Constant.COLLECTION) {// 已揽收
			jxClctBeans = DeliverDaoFactory.getInstance()
					.getJxClctDao(getApplicationContext())
					.queryJxClctMessage_All_S(str, delvorgcode, username);
		} else if (dealType == Constant.NOUPDATA) {// 未上传
			jxClctBeans = DeliverDaoFactory.getInstance()
					.getJxClctDao(getApplicationContext())
					.queryJxClctMessage_S(str, delvorgcode, username, "0");
		}
		gatherCount = jxClctBeans.size();
		mWuPaiLanAdapter = new WuPaiLanAdapter(jxClctBeans);
		swipeListView.setAdapter(mWuPaiLanAdapter);

	}

	// 派单的是否显示
	public void showBack() {
		if (dataList.size() == 0) {
			iv_text.setVisibility(View.VISIBLE);
			iv_person.setVisibility(View.VISIBLE);
			swipeListView.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
		} else {
			iv_text.setVisibility(View.INVISIBLE);
			iv_person.setVisibility(View.INVISIBLE);
			swipeListView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
	}

	// 揽收的数据显示
	public void showBack2() {
		if (gatherCount == 0) {
			iv_text.setVisibility(View.VISIBLE);
			iv_person.setVisibility(View.VISIBLE);
			swipeListView.setVisibility(View.GONE);// 揽收列表
			listView.setVisibility(View.GONE);
		} else {
			iv_text.setVisibility(View.GONE);
			iv_person.setVisibility(View.GONE);
			swipeListView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}

	}

	// 刷新派单列表
	private void repaidliebiao() {
		initData("");
		mPaiLanAdapter = new PaiLanAdapter(dataList);

		listView.setAdapter(mPaiLanAdapter);	
		listView.setOnRefreshListener(new OnRefreshLoadingMoreListener() {
			@Override
			public void onRefresh() {
				DragListView.mLastUpdateTime = SharePreferenceUtilDaoFactory
						.getInstance(getApplicationContext())
						.getCollectionTime();
				new GatherAsyncTask(getApplicationContext(),
						new onPostExecuteListener_Gather() {

							@Override
							public void onPostExecute(String result) {
								listView.onRefreshComplete();
								initData("");
								if (null == mPaiLanAdapter) {
									mPaiLanAdapter = new PaiLanAdapter(dataList);
									listView.setAdapter(mPaiLanAdapter);
								} else {
									mPaiLanAdapter
											.setnotifyDataSetChanged(dataList);
								}

							}
						}, null).execute();
			}

			@Override
			public void onLoadMore() {

			}
		});
	}

	private void addListener() {
		addSound();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(CollectionActivity_JX.this,
						CollectionDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("detail_id", beans.get(arg2 - 1).get_id());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		searchtext.addTextChangedListener(new TextWatcher() {
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
				String input = s.toString();
				if (dealType == Constant.NOCOLLECTION) {
					initData(input);
				} else {
					notifyAutoCountLanAdapter(input);
				}
			}
		});
		swipeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				LogUtils.e("跳转到详情页面去");
				Intent intent = new Intent(CollectionActivity_JX.this,
						CollectionDetailActivityJX.class);
				List<JxClctBean> beans = mWuPaiLanAdapter.getJxClctBean();
				JxClctBean bean = beans.get(position);
				int _id = bean.get_id();
				intent.putExtra("detail_id", _id);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		gatherDao = DeliverDaoFactory.getInstance().getGather_msgdao(
				getApplicationContext());
		receiver = new LocationReceiver();
		listView = (DragListView) findViewById(R.id.listView);
		nCollection = (Button) findViewById(R.id.nCollection);
		nCollection.setOnClickListener(clicklistener);
		collection_scan = (Button) findViewById(R.id.collection_scan);
		collection_scan.setOnClickListener(clicklistener);
		btn_map_collection = (Button) findViewById(R.id.btn_map_collection);
		btn_map_collection.setOnClickListener(clicklistener);

		collection_back = (Button) findViewById(R.id.collection_back);
		btn_group_activity_taskshow = (TextView) findViewById(R.id.btn_group_activity_taskshow);
		tv_group_num = (TextView) findViewById(R.id.tv_group_num);
		ll_group = (LinearLayout) findViewById(R.id.ll_group);
		ll_group.setOnClickListener(clicklistener);
		uploadReceiver = new MyReceiver();
		collection_back.setOnClickListener(clicklistener);
		iv_text = (ImageView) findViewById(R.id.iv_text);
		iv_person = (ImageView) findViewById(R.id.iv_person);
		swipeListView = (SwipeListView) findViewById(R.id.fragment_count_lantou_listView);// TODO
																							// 揽收用的listview
		btn_beginto_update = (Button) findViewById(R.id.btn_beginto_update);
		btn_beginto_update.setOnClickListener(clicklistener);
	}

	private void showdiag(String info) {
		InfoDialog infoDialog = new InfoDialog(this, info);
		infoDialog.Show();

	}

	@Override
	protected void onRestart() {
		refGroup();
		super.onRestart();
	}

	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter("locAddr");
		filter.addAction(Constant.ACTION_DOWN_DATA_OVER);
		registerReceiver(receiver, filter);

		IntentFilter filter2 = new IntentFilter(Constant.ACTION_GATHER_SC);
		filter2.addAction(Constant.ACTION_BLUTTOOTH_MSG);
		registerReceiver(uploadReceiver, filter2);
		if (dealType == Constant.NOCOLLECTION) {
			initData(searchtext.getText().toString());
		} else {
			notifyAutoCountLanAdapter(searchtext.getText().toString());
		}
		Utils.startIntentService(CollectionActivity_JX.this);// 启动投递、揽收异步上传服务

		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(receiver);
		unregisterReceiver(uploadReceiver);
		super.onPause();
	}

	/**
	 * 添加语音功能
	 */
	public void addSound() {
		sound = (Button) findViewById(R.id.sound);
		searchtext = (AutoCompleteTextView) findViewById(R.id.searchtext);
		XFAudio soundAudio = new XFAudio(CollectionActivity_JX.this, sound,
				searchtext);
		soundAudio.toSay();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 111) {
			String result = data.getStringExtra("txtResult");
			if (!"".equals(result)) {
				searchtext.setText(result);
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private OnClickListener clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.collection_back: // 返回
				finish();
				break;
			case R.id.ll_group: // 展示分组
				showpop();
				refGroup();
				break;
			case R.id.nocollection_pop: // 派单信息列表
				searchtext.setText("");
				dealType = Constant.NOCOLLECTION;
				refGroup();
				popupWindow.dismiss();
				break;
			case R.id.collection_pop: // 已揽收信息列表
				searchtext.setText("");
				dealType = Constant.COLLECTION;
				refGroup();
				popupWindow.dismiss();
				break;
			case R.id.weishangchuan_pop: // 未上传信息列表
				searchtext.setText("");
				dealType = Constant.NOUPDATA;
				refGroup();
				popupWindow.dismiss();
				break;
			case R.id.collection_scan: // 扫码
				startActivityForResult(new Intent(CollectionActivity_JX.this,
						CaptureActivity.class), 1);
				break;

			case R.id.nCollection: // 无派单揽收录入
				Intent intent = new Intent(CollectionActivity_JX.this,
						ClctActivityDetails.class);
				Bundle bundle = new Bundle();
				bundle.putString("nullCollection", nullCollection);
				bundle.putString("CollectionType", "无派单揽收");
				bundle.putString("mailType", "0");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_map_collection:
				Intent ite = new Intent(CollectionActivity_JX.this,
						MapForClctTask.class);
				startActivity(ite);
				break;
			case R.id.btn_beginto_update:
				if (Utils.isNetworkAvailable(CollectionActivity_JX.this)) {
					if (DeliverDaoFactory.getInstance()
							.getJxClctDao(getApplicationContext())
							.queryJxClctMessage(delvorgcode, username, "0")
							.size() > 0) {
						Utils.startIntentService(CollectionActivity_JX.this);
						Toast.makeText(CollectionActivity_JX.this,
								"上传服务已开启，正在为您上传数据", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(CollectionActivity_JX.this, "没有未上传数据",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CollectionActivity_JX.this,
							"网络不好是没办法提交数据的哦", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	};

	/**
	 * 任务确认/任务完成
	 */
	private void getEnSureTask(final String reservenum, final String companyid,
			final String phone, final Button btn_queding) {
		Log.e("tag", "任务请求开始");
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				fastbean bean = new fastbean();
				bean.setTaskNum(companyid);
				bean.setOrderIid(reservenum);
				bean.setExecData("3");
				bean.setOperUserCode(username);
				bean.setLat(BaiduGpsContants.getInstance().getLat());
				bean.setLon(BaiduGpsContants.getInstance().getLng());
				bean.setCustomerMobile(phone);
				bean.setOrderStatus("3");
				bean.setEmployeeNo(username);
				bean.setOrgCode(delvorgcode);
				bean.setEmployeeName(realname);
				bean.setEmployeeMobile(mobile);
				String urlString = Global.EnSureTask_URL;
				String jsonString = JSON.toJSONString(bean);
				JSONObject jsonObject = null;
				try {
					String str1 = NetHelper.doPostJson(urlString, jsonString,
							"json");
					LogUtils.e("点击确认之后getEnSureTask" + jsonString);
					jsonObject = new JSONObject(str1);
					String result = jsonObject.getString("result");
					Log.e("gongjie", "点击确认之后getEnSureTask---jsonString" + jsonString);
					Log.e("gongjie", "点击确认之后getEnSureTask---result" + result);
					LogUtils.e("点击确认之后getEnSureTask---result" + result);
					if (result.equals("1")) {
						DeliverDaoFactory.getInstance()
								.getGather_msgdao(getApplicationContext())
								.updateEnSureTask(reservenum, "0");
					}
				} catch (JSONException e) {
				}
				return jsonObject;
			}

			protected void onPostExecute(JSONObject result) {
				if (progressDialog != null) {
					progressDialog.toDimiss();
				}
				mPaiLanAdapter.notifyDataSetChanged();
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(CollectionActivity_JX.this,
						"正在确认任务···");
				progressDialog.setCanCalcelable(false);
				progressDialog.toShow();
			}

		}.execute();
	}

	/**
	 * 派单揽收 有打电话发短信揽收的按钮 不可删除的listview
	 * */
	public class PaiLanAdapter extends BaseAdapter {

		List<Map<String, String>> dataList;

		public PaiLanAdapter(List<Map<String, String>> dataList) {
			this.dataList = dataList;
			int cnt = dataList.size();
			// for (int i = 0; i < cnt; i++) {
			// isSelected.put(i, false);
			// }
		}

		public void setlist() {
			int cnt = dataList.size();
			// for (int i = 0; i < cnt; i++) {
			// isSelected.put(i, true);
			// }
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		public void setnotifyDataSetChanged(List<Map<String, String>> dataList) {
			this.dataList = dataList;
			notifyDataSetChanged();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.pl_list_item_jx, null);
				holder = new ViewHolder();
				// holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);
				// holder.cBox.setChecked(isSelected.get(position));
				holder.relativeLayout_xianshi = (RelativeLayout) convertView
						.findViewById(R.id.relativeLayout_xianshi);
				holder.relativeLayout_yincang = (RelativeLayout) convertView
						.findViewById(R.id.relativeLayout_yancang);
				holder.linearLayout_Remind = (LinearLayout) convertView
						.findViewById(R.id.linearLayout_Remind);
				holder.phone = (TextView) convertView
						.findViewById(R.id.tv_phone);
				holder.btn_phone = (Button) convertView
						.findViewById(R.id.btn_phone);
				holder.btn_message = (Button) convertView
						.findViewById(R.id.btn_message);
				holder.btn_fastCollection = (Button) convertView
						.findViewById(R.id.btn_fastCollection);
				holder.btn_queding = (Button) convertView
						.findViewById(R.id.btn_queding);
				holder.clientname = (TextView) convertView
						.findViewById(R.id.clientname);
				holder.address = (TextView) convertView
						.findViewById(R.id.tv_address);
				holder.tv_mailnum = (TextView) convertView
						.findViewById(R.id.tv_mailnum);
				holder.tv_createtime = (TextView) convertView
						.findViewById(R.id.tv_createtime);

				holder.lssx = (TextView) convertView.findViewById(R.id.lssx);
				holder.cnday = (TextView) convertView.findViewById(R.id.cnday);
				holder.tv_sendType = (TextView) convertView
						.findViewById(R.id.tv_sendType);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

				// if (l4r5)
				// holder.cBox.setChecked(false);
				// else
				// holder.cBox.setChecked(true);
			}
			final String reservenum = dataList.get(position).get("reservenum");
			final String phone = dataList.get(position).get("phone");
			final String clientname = dataList.get(position).get("clientname");
			final String address = dataList.get(position).get("address");
			final String senderProv = dataList.get(position).get("senderProv");
			final String senderProvCode = dataList.get(position).get("senderProvCode");
			final String senderCity = dataList.get(position).get("senderCity");
			final String senderCityCode = dataList.get(position).get("senderCityCode");
			final String userSid = dataList.get(position).get("userSid");
			final String userIntegral = dataList.get(position).get(
					"userIntegral");
			final String userValidIntegral = dataList.get(position).get(
					"userValidIntegral");
			final String serviceType = dataList.get(position)
					.get("serviceType");
			final String senderCounty = dataList.get(position).get(
					"senderCounty");
			final String senderCountyCode = dataList.get(position).get(
					"senderCountyCode");
			final String receiverName = dataList.get(position).get(
					"receiverName");
			final String receiverMobile = dataList.get(position).get(
					"receiverMobile");
			final String receiverProv = dataList.get(position).get(
					"receiverProv");
			final String receiverProvCode = dataList.get(position).get(
					"receiverProvCode");
			final String receiverCity = dataList.get(position).get(
					"receiverCity");
			final String receiverCityCode = dataList.get(position).get(
					"receiverCityCode");
			final String receiverCounty = dataList.get(position).get(
					"receiverCounty");
			final String receiverCountyCode = dataList.get(position).get(
					"receiverCountyCode");
			final String receiverStreet = dataList.get(position).get(
					"receiverStreet");
			final String orderWeight = dataList.get(position)
					.get("orderWeight");
			final String payment = dataList.get(position).get("payment");
			final String companyid = dataList.get(position).get("companyid");
			final String cnday = dataList.get(position).get("cnday");
			final String remind = dataList.get(position).get("remind");
			final String sendType = dataList.get(position).get("sendType");
			final String startTime = dataList.get(position).get("startTime");
			final String taskAllotTime = dataList.get(position).get(
					"taskAllotTime");
			// LogUtils.e("list的   " + position + "    " + clientname
			// + "-----------" + taskAllotTime);
			LogUtils.e("list的   " + position + "    " + clientname
					+ "-----------" + taskAllotTime);
			if (cnday.equals("0")) {
				holder.relativeLayout_yincang.setVisibility(View.GONE);
				holder.relativeLayout_xianshi.setVisibility(View.VISIBLE);
			} else {
				holder.relativeLayout_xianshi.setVisibility(View.GONE);// 揽收按钮
				holder.relativeLayout_yincang.setVisibility(View.VISIBLE);
			}
			// 添加时间字段 倒计时
			if (Utils.GetMinute_Five(taskAllotTime) >= 5) {
				holder.tv_createtime.setVisibility(View.VISIBLE);
				holder.tv_createtime
						.setText(Utils.GetSecond(taskAllotTime));
				// LogUtils.e("list的  " + position + "  " + clientname
				// + "--服务器时间--" + taskAllotTime + "  要显示的时间"
				// + Utils.GetSecond(taskAllotTime));
				LogUtils.e("list的  " + position + "  " + clientname
						+ "--服务器时间--" + taskAllotTime + "  要显示的时间"
						+ Utils.GetSecond(taskAllotTime));
			} else {
				holder.tv_createtime.setVisibility(View.GONE);
			}
			// LogUtils.e("remind-----"+remind);
			if (remind != null && remind.equals("1")) {
				holder.linearLayout_Remind.setVisibility(View.VISIBLE);
			} else {
				holder.linearLayout_Remind.setVisibility(View.GONE);
			}
			// TODO 添加是否紧急
			LogUtils.e("sendType-----" + sendType);
			if (sendType != null && sendType.equals("1")) {
				holder.tv_sendType.setVisibility(View.VISIBLE);
			} else {
				holder.tv_sendType.setVisibility(View.GONE);
			}
			holder.phone.setText(dataList.get(position).get("phone"));
			holder.clientname.setText(dataList.get(position).get("clientname"));
			holder.address.setText(dataList.get(position).get("address"));
			holder.lssx.setText(dataList.get(position).get("lssx"));
			holder.cnday.setText("客户催单");
			holder.tv_mailnum.setText(reservenum);
			holder.btn_phone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Uri uri = Uri.parse("tel:" + phone);
					intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
				}
			});
			holder.btn_message.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isMobileNum(phone)) {
						msgDialogShow(phone);
					} else {
						showdiag("非手机号,不能发短信");
						// Toast.makeText(CollectionActivity.this, "非手机号,不能发短信",
						// Toast.LENGTH_SHORT).show();
					}
				}
			});
			holder.btn_fastCollection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 跳转到揽收页面
					Intent intent = new Intent(CollectionActivity_JX.this,
							ClctActivityDetails.class);
					Bundle bundle = new Bundle();
					bundle.putString("reservenum", reservenum);
					bundle.putString("phone", phone);
					bundle.putString("clientname", clientname);
					bundle.putString("address", address);
					bundle.putString("senderProv", senderProv);
					bundle.putString("senderProvCode", senderProvCode);
					bundle.putString("senderCity", senderCity);
					bundle.putString("senderCityCode", senderCityCode);
					bundle.putString("senderCounty", senderCounty);
					bundle.putString("senderCountyCode", senderCountyCode);
					bundle.putString("receiverName", receiverName);
					bundle.putString("receiverMobile", receiverMobile);
					bundle.putString("receiverProv", receiverProv);
					bundle.putString("receiverCity", receiverCity);
					bundle.putString("receiverProvCode", receiverProvCode);
					bundle.putString("receiverCityCode", receiverCityCode);
					bundle.putString("receiverCounty", receiverCounty);
					bundle.putString("receiverCountyCode", receiverCountyCode);
					bundle.putString("receiverStreet", receiverStreet);
					bundle.putString("orderWeight", orderWeight);
					bundle.putString("payment", payment);
					bundle.putString("CollectionType", "派单信息");
					bundle.putString("mailType", "1");
					bundle.putString("companyid", companyid);
					bundle.putString("serviceType", serviceType);
					bundle.putString("sendType", sendType);
					bundle.putString("startTime", startTime);
					bundle.putString("userSid", userSid);
					bundle.putString("userIntegral", userIntegral);
					bundle.putString("userValidIntegral", userValidIntegral);
					intent.putExtras(bundle);
					startActivity(intent);
					// finish();
				}
			});
			holder.btn_queding.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getEnSureTask(reservenum, companyid, phone,
							holder.btn_queding);
				}
			});

			return convertView;
		}

		@Override
		public void notifyDataSetChanged() {
			if (dealType == Constant.NOCOLLECTION) {
				beans = DeliverDaoFactory
						.getInstance()
						.getGather_msgdao(getApplicationContext())
						.orderByDistance_Query(searchtext.getText().toString(),
								delvorgcode, username);
				int beansSize = beans.size();
				dataList = new ArrayList<Map<String, String>>();
				for (int a = 0; a < beansSize; a++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("phone", beans.get(a).getPhone());
					map.put("clientname", beans.get(a).getClientname());
					map.put("address", beans.get(a).getAddress());
					map.put("senderProv", beans.get(a).getSenderProv());
					map.put("senderCity", beans.get(a).getSenderCity());
					map.put("senderCounty", beans.get(a).getSenderCounty());
					map.put("receiverName", beans.get(a).getReceiverName());
					map.put("receiverMobile", beans.get(a).getReceiverMobile());
					map.put("receiverProv", beans.get(a).getReceiverProv());
					map.put("receiverCity", beans.get(a).getReceiverCity());
					map.put("receiverCounty", beans.get(a).getReceiverCounty());
					map.put("senderProvCode", beans.get(a).getSenderProvCode());
					map.put("senderCityCode", beans.get(a).getSenderCityCode());
					map.put("senderCountyCode", beans.get(a).getSenderCountyCode());
					map.put("receiverProvCode", beans.get(a).getReceiverProvCode());
					map.put("receiverCityCode", beans.get(a).getReceiverCityCode());
					map.put("receiverCountyCode", beans.get(a).getReceiverCountyCode());
					map.put("receiverStreet", beans.get(a).getReceiverStreet());
					map.put("orderWeight", beans.get(a).getOrderWeight());
					map.put("payment", beans.get(a).getPayment());
					map.put("taskAllotTime", beans.get(a).getTaskAllotTime());
					map.put("cnday", beans.get(a).getCnday());
					map.put("reservenum", beans.get(a).getReservenum());
					map.put("companyid", beans.get(a).getCompanyid());
					map.put("remind", beans.get(a).getRemind());
					map.put("serviceType", beans.get(a).getMsg_typecode());
					// map.put("sendType", beans.get(a).getDevice_number());
					map.put("userSid", beans.get(a).getMsg_id());
					map.put("userIntegral", beans.get(a).getMsg_code());
					map.put("userValidIntegral", beans.get(a)
							.getUserValidIntegral());
					map.put("sendType", beans.get(a).getSendType());
					map.put("startTime", beans.get(a).getStartTime());
					dataList.add(map);
				}
				tv_group_num.setText("(" + beans.size() + ")" + "");
				super.notifyDataSetChanged();
			}
		}

	}

	private class ViewHolder {
		// CheckBox cBox;
		TextView phone, clientname, address, lssx, cnday, tv_mailnum,
				tv_createtime, tv_sendType;
		Button btn_fastCollection, btn_queding, btn_message, btn_phone;
		RelativeLayout relativeLayout_xianshi, relativeLayout_yincang;
		LinearLayout linearLayout_Remind;

	}

	private ProgressDialog progressDialog;

	/**
	 * 发送短信
	 * 
	 * @param number
	 */
	public void sendSms(final String number) {

		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObject = null;
				List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
				try {
					paramObject.add(new BasicNameValuePair("sms_type", "L")); // 信息类型，"L"表示揽收
					paramObject.add(new BasicNameValuePair("receiver_mobiles",
							number)); // 电话号码
					paramObject.add(new BasicNameValuePair("dlvorgcode",
							SharePreferenceUtilDaoFactory.getInstance(
									CollectionActivity_JX.this)
									.getDELVORGCODE())); // 机构号
					paramObject.add(new BasicNameValuePair("username",
							SharePreferenceUtilDaoFactory.getInstance(
									CollectionActivity_JX.this).getUSERNAME())); // 员工号
					jsonObject = new JSONObject(NetHelper.doPost(
							Global.sendSms, paramObject));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return jsonObject;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (progressDialog != null) {
					progressDialog.toDimiss();
				}

				if (result != null) {
					try {
						if ("1".equals(result.getString("result"))) {
							Toast.makeText(
									CollectionActivity_JX.this,
									result.getJSONObject("body")
											.getJSONArray("success")
											.getJSONObject(0).getString("msg"),
									1).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(CollectionActivity_JX.this, "短信发送失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(CollectionActivity_JX.this,
						"正在发送···");
				progressDialog.setCanCalcelable(false);
				progressDialog.toShow();
			}

		}.execute();
	}

	class LocationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (Constant.ACTION_LOCATION.equals(intent.getAction())) {
				// titleaddress.setText(intent.getStringExtra("addr"));
				List<GatherTableBean> beans = gatherDao.orderByDistance_Query(
						searchtext.getText().toString(), delvorgcode, username);
				int beansSize = beans.size();
				dataList.clear();
				for (int a = 0; a < beansSize; a++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("phone", beans.get(a).getPhone());
					map.put("clientname", beans.get(a).getClientname());
					map.put("address", beans.get(a).getAddress());
					map.put("senderProv", beans.get(a).getSenderProv());
					map.put("senderCity", beans.get(a).getSenderCity());
					map.put("senderCounty", beans.get(a).getSenderCounty());
					map.put("receiverName", beans.get(a).getReceiverName());
					map.put("receiverMobile", beans.get(a).getReceiverMobile());
					map.put("receiverProv", beans.get(a).getReceiverProv());
					map.put("receiverCity", beans.get(a).getReceiverCity());
					map.put("receiverCounty", beans.get(a).getReceiverCounty());
					map.put("senderProvCode", beans.get(a).getSenderProvCode());
					map.put("senderCityCode", beans.get(a).getSenderCityCode());
					map.put("senderCountyCode", beans.get(a).getSenderCountyCode());
					map.put("receiverProvCode", beans.get(a).getReceiverProvCode());
					map.put("receiverCityCode", beans.get(a).getReceiverCityCode());
					map.put("receiverCountyCode", beans.get(a).getReceiverCountyCode());
					map.put("receiverStreet", beans.get(a).getReceiverStreet());
					map.put("orderWeight", beans.get(a).getOrderWeight());
					map.put("payment", beans.get(a).getPayment());
					map.put("taskAllotTime", beans.get(a).getTaskAllotTime());
					map.put("cnday", beans.get(a).getCnday());
					map.put("createtime", beans.get(a).getCreatetime());
					map.put("reservenum", beans.get(a).getReservenum());
					map.put("companyid", beans.get(a).getCompanyid());
					map.put("remind", beans.get(a).getRemind());
					map.put("serviceType", beans.get(a).getMsg_typecode());
					// map.put("sendType", beans.get(a).getDevice_number());
					map.put("userSid", beans.get(a).getMsg_id());
					map.put("userIntegral", beans.get(a).getMsg_code());
					map.put("userValidIntegral", beans.get(a)
							.getUserValidIntegral());
					map.put("sendType", beans.get(a).getSendType());
					map.put("startTime", beans.get(a).getStartTime());
					dataList.add(map);
				}
			} else if (Constant.ACTION_DOWN_DATA_OVER
					.equals(intent.getAction())) {
				// 数据更新完毕的通知
				// Log.e("33333333333333", "33333333333333");
				String count = intent.getStringExtra("count");
				int messageType = intent.getIntExtra("messageType", -1);
				switch (messageType) {
				// TODO 根据推送消息类型做不同处理
				case Constant.PUSH_TYPE_CLCTTASK:
					try {
						int num = Integer.parseInt(count);
						List<GatherTableBean> beans = gatherDao
								.orderByDistance_Query(searchtext.getText()
										.toString(), delvorgcode, username);
						int beansSize = beans.size();
						dataList.clear();
						for (int a = 0; a < beansSize; a++) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("phone", beans.get(a).getPhone());
							map.put("clientname", beans.get(a).getClientname());
							map.put("address", beans.get(a).getAddress());
							map.put("senderProv", beans.get(a).getSenderProv());
							map.put("senderCity", beans.get(a).getSenderCity());
							map.put("senderCounty", beans.get(a)
									.getSenderCounty());
							map.put("receiverName", beans.get(a)
									.getReceiverName());
							map.put("receiverMobile", beans.get(a)
									.getReceiverMobile());
							map.put("receiverProv", beans.get(a)
									.getReceiverProv());
							map.put("receiverCity", beans.get(a)
									.getReceiverCity());
							map.put("receiverCounty", beans.get(a)
									.getReceiverCounty());
							map.put("senderProvCode", beans.get(a).getSenderProvCode());
							map.put("senderCityCode", beans.get(a).getSenderCityCode());
							map.put("senderCountyCode", beans.get(a).getSenderCountyCode());
							map.put("receiverProvCode", beans.get(a).getReceiverProvCode());
							map.put("receiverCityCode", beans.get(a).getReceiverCityCode());
							map.put("receiverCountyCode", beans.get(a).getReceiverCountyCode());
							map.put("receiverStreet", beans.get(a)
									.getReceiverStreet());
							map.put("orderWeight", beans.get(a)
									.getOrderWeight());
							map.put("payment", beans.get(a).getPayment());
							map.put("taskAllotTime", beans.get(a)
									.getTaskAllotTime());
							map.put("cnday", beans.get(a).getCnday());
							map.put("createtime", beans.get(a).getCreatetime());
							map.put("reservenum", beans.get(a).getReservenum());
							map.put("companyid", beans.get(a).getCompanyid());
							map.put("remind", beans.get(a).getRemind());
							map.put("serviceType", beans.get(a)
									.getMsg_typecode());
							// map.put("sendType",
							// beans.get(a).getDevice_number());
							map.put("userSid", beans.get(a).getMsg_id());
							map.put("userIntegral", beans.get(a).getMsg_code());
							map.put("userValidIntegral", beans.get(a)
									.getUserValidIntegral());
							map.put("sendType", beans.get(a).getSendType());
							map.put("startTime", beans.get(a).getStartTime());
							dataList.add(map);
						}
					} catch (Exception e) {
					}
					break;
				case Constant.PUSH_TYPE_CLCTTASK2:
					try {
						int num = Integer.parseInt(count);
						List<GatherTableBean> beans = gatherDao
								.orderByDistance_Query(searchtext.getText()
										.toString(), delvorgcode, username);
						int beansSize = beans.size();
						dataList.clear();
						for (int a = 0; a < beansSize; a++) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("phone", beans.get(a).getPhone());
							map.put("clientname", beans.get(a).getClientname());
							map.put("address", beans.get(a).getAddress());
							map.put("senderProv", beans.get(a).getSenderProv());
							map.put("senderCity", beans.get(a).getSenderCity());
							map.put("senderCounty", beans.get(a)
									.getSenderCounty());
							map.put("receiverName", beans.get(a)
									.getReceiverName());
							map.put("receiverMobile", beans.get(a)
									.getReceiverMobile());
							map.put("receiverProv", beans.get(a)
									.getReceiverProv());
							map.put("receiverCity", beans.get(a)
									.getReceiverCity());
							map.put("receiverCounty", beans.get(a)
									.getReceiverCounty());
							map.put("senderProvCode", beans.get(a).getSenderProvCode());
							map.put("senderCityCode", beans.get(a).getSenderCityCode());
							map.put("senderCountyCode", beans.get(a).getSenderCountyCode());
							map.put("receiverProvCode", beans.get(a).getReceiverProvCode());
							map.put("receiverCityCode", beans.get(a).getReceiverCityCode());
							map.put("receiverCountyCode", beans.get(a).getReceiverCountyCode());
							map.put("receiverStreet", beans.get(a)
									.getReceiverStreet());
							map.put("orderWeight", beans.get(a)
									.getOrderWeight());
							map.put("payment", beans.get(a).getPayment());
							map.put("taskAllotTime", beans.get(a)
									.getTaskAllotTime());
							map.put("cnday", beans.get(a).getCnday());
							map.put("createtime", beans.get(a).getCreatetime());
							map.put("reservenum", beans.get(a).getReservenum());
							map.put("companyid", beans.get(a).getCompanyid());
							map.put("remind", beans.get(a).getRemind());
							map.put("serviceType", beans.get(a)
									.getMsg_typecode());
							// map.put("sendType",
							// beans.get(a).getDevice_number());
							map.put("userSid", beans.get(a).getMsg_id());
							map.put("userIntegral", beans.get(a).getMsg_code());
							map.put("userValidIntegral", beans.get(a)
									.getUserValidIntegral());
							map.put("sendType", beans.get(a).getSendType());
							map.put("startTime", beans.get(a).getStartTime());
							dataList.add(map);
						}
					} catch (Exception e) {
					}
					refGroup();
					break;
				case Constant.PUSH_TYPE_CLCTTASK3:
					try {
						int num = Integer.parseInt(count);
						List<GatherTableBean> beans = gatherDao
								.orderByDistance_Query(searchtext.getText()
										.toString(), delvorgcode, username);
						int beansSize = beans.size();
						dataList.clear();
						for (int a = 0; a < beansSize; a++) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("phone", beans.get(a).getPhone());
							map.put("clientname", beans.get(a).getClientname());
							map.put("address", beans.get(a).getAddress());
							map.put("senderProv", beans.get(a).getSenderProv());
							map.put("senderCity", beans.get(a).getSenderCity());
							map.put("senderCounty", beans.get(a)
									.getSenderCounty());
							map.put("receiverName", beans.get(a)
									.getReceiverName());
							map.put("receiverMobile", beans.get(a)
									.getReceiverMobile());
							map.put("receiverProv", beans.get(a)
									.getReceiverProv());
							map.put("receiverCity", beans.get(a)
									.getReceiverCity());
							map.put("receiverCounty", beans.get(a)
									.getReceiverCounty());
							map.put("senderProvCode", beans.get(a).getSenderProvCode());
							map.put("senderCityCode", beans.get(a).getSenderCityCode());
							map.put("senderCountyCode", beans.get(a).getSenderCountyCode());
							map.put("receiverProvCode", beans.get(a).getReceiverProvCode());
							map.put("receiverCityCode", beans.get(a).getReceiverCityCode());
							map.put("receiverCountyCode", beans.get(a).getReceiverCountyCode());
							map.put("receiverStreet", beans.get(a)
									.getReceiverStreet());
							map.put("orderWeight", beans.get(a)
									.getOrderWeight());
							map.put("payment", beans.get(a).getPayment());
							map.put("taskAllotTime", beans.get(a)
									.getTaskAllotTime());
							map.put("cnday", beans.get(a).getCnday());
							map.put("createtime", beans.get(a).getCreatetime());
							map.put("reservenum", beans.get(a).getReservenum());
							map.put("companyid", beans.get(a).getCompanyid());
							map.put("remind", beans.get(a).getRemind());
							map.put("serviceType", beans.get(a)
									.getMsg_typecode());
							// map.put("sendType",
							// beans.get(a).getDevice_number());
							map.put("userSid", beans.get(a).getMsg_id());
							map.put("userIntegral", beans.get(a).getMsg_code());
							map.put("userValidIntegral", beans.get(a)
									.getUserValidIntegral());
							map.put("sendType", beans.get(a).getSendType());
							map.put("startTime", beans.get(a).getStartTime());
							dataList.add(map);
						}
					} catch (Exception e) {
					}
					refGroup();
					break;
				}
			}
		}
	}

	public void msgDialogShow(final String phoneNum) {
		if (dialog == null) {
			dialog = new Dialog(CollectionActivity_JX.this, R.style.dialogss);
			view = View.inflate(CollectionActivity_JX.this,
					R.layout.dialog_sms, null);
			Button cancel = (Button) view.findViewById(R.id.info_cancel);
			Button sure = (Button) view.findViewById(R.id.info_sure);
			TextView numtext = (TextView) view
					.findViewById(R.id.custom_dia_centercall);
			WindowManager manager = (WindowManager) this
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			manager.getDefaultDisplay().getMetrics(dm);
			int width = dm.widthPixels;
			int height = dm.heightPixels;
			dialog.setContentView(view, new LayoutParams(width * 15 / 20,
					android.view.WindowManager.LayoutParams.WRAP_CONTENT));
			dialog.setCancelable(false);
			numtext.setText(phoneNum);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					dialog = null;
				}
			});
			sure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sendSms(phoneNum);
					dialog.dismiss();
					dialog = null;
				}
			});
			dialog.show();
		}
	}

	/**
	 * 展示pop
	 * */
	private void showpop() {
		View v = LayoutInflater.from(this).inflate(R.layout.collection_pop_jx,
				null);
		popupWindow = new PopupWindow(this);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow
				.setWidth(getWindowManager().getDefaultDisplay().getWidth() * 3 / 7);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(v);
		// showAsDropDown会把里面的view作为参照物，所以要那满屏幕parent
		popupWindow.showAsDropDown(ll_group, 0, 0);
		LinearLayout linearLayoutnoc = (LinearLayout) v
				.findViewById(R.id.nocollection_pop);
		TextView collection_textview = (TextView) v
				.findViewById(R.id.collection_textview);// 已揽收
		TextView nocollection_textview = (TextView) v
				.findViewById(R.id.nocollection_textview);// 待处理
		LinearLayout linearLayoutc = (LinearLayout) v
				.findViewById(R.id.collection_pop);
		LinearLayout linearLayoutweiLayout = (LinearLayout) v
				.findViewById(R.id.weishangchuan_pop);
		TextView textViewwei = (TextView) v
				.findViewById(R.id.weishangchuan_textview);// 未上传
		linearLayoutnoc.setOnClickListener(clicklistener);
		linearLayoutc.setOnClickListener(clicklistener);
		linearLayoutweiLayout.setOnClickListener(clicklistener);
		int gatherCount0 = DeliverDaoFactory.getInstance()
				.getJxClctDao(getApplicationContext())
				.queryJxClctMessage(delvorgcode, username, "0").size();// 0未上传，1已揽收
		int gatherCount1 = DeliverDaoFactory.getInstance()
				.getJxClctDao(getApplicationContext())
				.queryJxClctMessage_All(delvorgcode, username).size();
		LogUtils.e("" + gatherCount1 + "          " + gatherCount0);
		collection_textview.setText("(" + gatherCount1 + ")" + "");// 已揽收
		nocollection_textview.setText("(" + dataList.size() + ")" + "");// 待处理在pop上数量的展示
		textViewwei.setText("(" + gatherCount0 + ")" + "");

	}

	/**
	 * 无派单揽收 没有打电话发短信揽收的按钮
	 * */
	public class WuPaiLanAdapter extends BaseAdapter {
		private Context context = getApplicationContext();
		List<JxClctBean> dataList;

		public WuPaiLanAdapter(List<JxClctBean> dataList) {
			this.dataList = dataList;
		}

		public WuPaiLanAdapter(Context context, int rightWidth) {
			this.context = context;
			mRightWidth = rightWidth;
		}

		public void setnotifyDataSetChanged(List<JxClctBean> dataList) {
			this.dataList = dataList;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return jxClctBeans.size();
		}

		@Override
		public Object getItem(int position) {
			return jxClctBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.wupl_list_item_jx, null);
				viewHolder = new ViewHolder();
				viewHolder.item_left = convertView.findViewById(R.id.item_left);
				viewHolder.item_right = convertView
						.findViewById(R.id.item_right);
				viewHolder.tv_delete = (TextView) convertView
						.findViewById(R.id.tv_delete);// 删除
				viewHolder.clientname = (TextView) convertView
						.findViewById(R.id.clientname);
				viewHolder.phone = (TextView) convertView
						.findViewById(R.id.tv_phone);
				viewHolder.address = (TextView) convertView
						.findViewById(R.id.tv_address);
				viewHolder.tv_mailnum = (TextView) convertView
						.findViewById(R.id.tv_mailnum);
				viewHolder.tv_status = (TextView) convertView
						.findViewById(R.id.tv_status);
				viewHolder.tv_fee = (TextView) convertView
						.findViewById(R.id.tv_fee);
				viewHolder.tv_actUserIntegral = (TextView) convertView
						.findViewById(R.id.tv_actUserIntegral);
				viewHolder.tv_actfee = (TextView) convertView
						.findViewById(R.id.tv_actfee);
				viewHolder.tv_payment = (TextView) convertView
						.findViewById(R.id.tv_payment);
				viewHolder.tv_weight = (TextView) convertView
						.findViewById(R.id.tv_weight);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			LinearLayout.LayoutParams lp1 = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			viewHolder.item_left.setLayoutParams(lp1);
			LinearLayout.LayoutParams lp2 = new LayoutParams(
					swipeListView.getRightViewWidth(),
					LayoutParams.MATCH_PARENT);
			viewHolder.item_right.setLayoutParams(lp2);
			viewHolder.item_right.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO 这里之后做删除 修改到待上传的揽收列表
					// 删除主单号，提示，会删除所有的子单
					// 删除子单，提示，不能删除子单，请删除对应的主单
					// DeliverDaoFactory
					// .getInstance()
					// .getFastLanDao(getApplicationContext())
					// .updateOpreate("D",
					// deleteLanBeans.get(temppos).getMailNum());
					// DeliverDaoFactory
					// .getInstance()
					// .getMoneyDao(context)
					// .delete_GatherMoney(
					// deleteLanBeans.get(temppos).getMainmail());
					//
					// if (Utils.isNetworkAvailable(CollectionActivity_JX.this))
					// {
					// Utils.startIntentService(CollectionActivity_JX.this);
					// Toast.makeText(CollectionActivity_JX.this,
					// "你的揽收信息已上传，未上传的请手动上传", Toast.LENGTH_SHORT)
					// .show();
					// } else {
					// Toast.makeText(CollectionActivity_JX.this,
					// "网络不好是没办法提交数据，请手动上传", Toast.LENGTH_SHORT)
					// .show();
					// }
					// notifyDataSetChanged();
				}
			});
			// 设置数据
			viewHolder.clientname.setText(""
					+ dataList.get(position).getReceiverName());
			viewHolder.tv_mailnum.setText(""
					+ dataList.get(position).getMailNo());
			viewHolder.phone.setText(""
					+ dataList.get(position).getReceiverMobile());
			viewHolder.address.setText(""
					+ dataList.get(position).getReceiverAddress());
			viewHolder.tv_fee.setText(dataList.get(position).getFee() + "  元");
			viewHolder.tv_actfee.setText(dataList.get(position).getActFee()
					+ "  元");
			viewHolder.tv_actUserIntegral.setText(dataList.get(position)
					.getIntegral() + "");
			viewHolder.tv_weight.setText(dataList.get(position).getWeight()
					+ "  g");
			LogUtils.e("状态：   " + dataList.get(position).getClct_stauts());
			if (dealType == Constant.COLLECTION) {// 只有在已揽收中才区分是否已上传的状态
				viewHolder.tv_status.setVisibility(View.VISIBLE);
				if (dataList.get(position).getClct_stauts().equals("0")) {
					viewHolder.tv_status.setText("未上传");
				} else if (dataList.get(position).getClct_stauts().equals("1")) {
					viewHolder.tv_status.setText("已揽收");
				}
			} else {
				viewHolder.tv_status.setVisibility(View.GONE);
			}
			if ("1".equals(dataList.get(position).getPayment())) {
				viewHolder.tv_payment.setText("寄件现结 ");// 1：寄件现结 2：到付
			} else if ("2".equals(dataList.get(position).getPayment())) {
				viewHolder.tv_payment.setText("到付");
			} else if ("3".equals(dataList.get(position).getPayment())) {
				viewHolder.tv_payment.setText("积分抵扣");
			}
			return convertView;
		}

		@Override
		public void notifyDataSetChanged() {
			if (dealType == Constant.COLLECTION) {
				jxClctBeans = DeliverDaoFactory.getInstance()
						.getJxClctDao(getApplicationContext())
						.queryJxClctMessage_All(delvorgcode, username);
			} else if (dealType == Constant.NOUPDATA) {
				jxClctBeans = DeliverDaoFactory.getInstance()
						.getJxClctDao(getApplicationContext())
						.queryJxClctMessage(delvorgcode, username, "0");
			}

			tv_group_num.setText("(" + jxClctBeans.size() + ")" + "");
			super.notifyDataSetChanged();
		}

		public List<JxClctBean> getJxClctBean() {
			return jxClctBeans;
		}

		class ViewHolder {
			private TextView clientname, phone, address, tv_delete, tv_mailnum,
					tv_fee, tv_actfee, tv_actUserIntegral, tv_weight,
					tv_status, tv_payment;
			View item_left;
			View item_right;
			private RelativeLayout relativeLayoutzhudan,
					relativeLayoutzhongliang, relativeLayoutmoney,
					relativeLayoutdanhao;
		}
	}

	private void delete() {
		new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				String reObjc = "";
				NetHelper.doPostJson(
						Global.PUTMONEY,
						mailPutMoneyData(new ArrayList<GatherInfoBean>(),
								gatherBeans), "json");
				return reObjc;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				// mdialog.toDimiss();
				if (result != null) {
					try {
						if ("1".equals(result)) {
							// Toast.makeText(getApplicationContext(), "上传成功",
							// Toast.LENGTH_SHORT).show();
						} else {
							// Toast.makeText(getApplicationContext(), "上传失败",
							// Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						Log.e("e", e.getMessage());
					}
				} else {
					Toast.makeText(getApplicationContext(), "上传失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			protected void onPreExecute() {
				// mdialog = new ProgressDialog(getApplicationContext(),
				// "正在上传揽收信息");
				// mdialog.setCanCalcelable(false);
				// mdialog.toShow();
				super.onPreExecute();
			}
		}.execute();

	}

	private UserInfoUtils userInfo;

	private String mailPutMoneyData(List<GatherInfoBean> touBeans,
			List<GatherInfoBean> lanBeans) {
		userInfo = new UserInfoUtils(CollectionActivity_JX.this);
		String resultURL;
		PaymentMoneyBean bean = new PaymentMoneyBean();
		if (!"".equals(userInfo.getUserName())) {
			bean.setEmployeeNo(userInfo.getUserName());
		}
		bean.setDeviceId(deviceId());
		if (!"".equals(userInfo.getUserDelvorgCode())) {
			bean.setSjvorgcode(userInfo.getUserDelvorgCode());
			bean.setDlvorgcode(userInfo.getUserDelvorgCode());
		}
		bean.setLan(lanBeans);
		bean.setTou(touBeans);
		resultURL = JSON.toJSONString(bean);
		return resultURL;

	}

	/**
	 * 手机设备号
	 */
	private String deviceId() {
		TelephonyManager telephonemanage = (TelephonyManager) CollectionActivity_JX.this
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonemanage.getDeviceId();

	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_GATHER_SC.equals(intent.getAction())) {
				if (mWuPaiLanAdapter != null) {
					mWuPaiLanAdapter.notifyDataSetChanged();
				}
			} else if (Constant.ACTION_BLUTTOOTH_MSG.equals(intent.getAction())) {
				String code = intent.getStringExtra("code");
				if (code != null) {
					searchtext.setText(code);
				}
			}
		}

	}

}

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
import com.newcdc.R;
import com.newcdc.activity.censcus.LanTouDetailActivity;
import com.newcdc.adapter.DefinitionAdapter.IOnItemRightClickListener;
import com.newcdc.application.BaseActivity;
import com.newcdc.asynctask.GatherAsyncTask;
import com.newcdc.asynctask.GatherAsyncTask.onPostExecuteListener_Gather;
import com.newcdc.db.CustomerDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.FastLanDao;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.db.MailTypeDao;
import com.newcdc.db.QsjGndmDao;
import com.newcdc.db.TransportTypeDao;
import com.newcdc.model.FastLanBean;
import com.newcdc.model.GatherInfoBean;
import com.newcdc.model.GatherTableBean;
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
 * e
 * 揽收任务首页
 * 
 * @author quanyi
 * 
 */
public class CollectionActivity extends BaseActivity {
	private DragListView listView = null;
	private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> dataList1 = new ArrayList<Map<String, String>>();
	public boolean isCheckUp = true;
	private MyAdapter adapter = null;
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
	private List<FastLanBean> deleteLanBeans = new ArrayList<FastLanBean>();
	private FastLanDao fastLanDao;// 揽投
	private String orgCode, deviceId, realname, postcode;
	private int m_posion = 0;
	private List<GatherInfoBean> gatherBeans;
	private int mRightWidth = 100;
	private IOnItemRightClickListener mListener = null;
	private CountLanTouAdapter countLanTouAdapter;
	private int gatherCount = 0;// 揽收个数
	private int dealType;
	private Button btn_beginto_update = null;

	// private boolean nolan = true;// 是否未揽收

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);

		UserInfoUtils user = new UserInfoUtils(this);
		delvorgcode = user.getUserDelvorgCode();// 机构号
		username = user.getUserName();// 用户的工号
		dealType = Constant.NOCOLLECTION;
		dealType = getIntent().getIntExtra("dealType", Constant.NOCOLLECTION);
		
		
		
		inits();
		// 默认是派单列表
		// 按照分组刷新列表
		refGroup();
		addListener();

	}

	// 刷新分组
	private void refGroup() {
		switch (dealType) {
		case Constant.COLLECTION: // 已揽收
			swipeListView.setRightViewWidth(300);
			reflanshoudata("已揽收", gatherCount);
			showBack2();
			btn_beginto_update.setVisibility(View.GONE);
			break;
		case Constant.NOUPDATA: // 未上传
			swipeListView.setRightViewWidth(0);
			reflanshoudata("未上传", gatherCount);
			showBack2();
			btn_beginto_update.setVisibility(View.VISIBLE);
			break;
		case Constant.NOCOLLECTION:
			swipeListView.setRightViewWidth(300);
			repaidliebiao();
			showBack();
			btn_beginto_update.setVisibility(View.GONE);
			btn_group_activity_taskshow.setText("未揽收");
			tv_group_num.setText("(" + dataList.size() + ")" + "");
			break;
		}
	}

	// 刷新揽收列表
	private void reflanshoudata(String str, int count) {
		// 查询出数据
		if (dealType == Constant.COLLECTION) {
			deleteLanBeans = DeliverDaoFactory.getInstance()
					.getFastLanDao(getApplicationContext())
					.queryAutoFastData("", delvorgcode, username);
		} else if (dealType == Constant.NOUPDATA) {
			deleteLanBeans = DeliverDaoFactory.getInstance()
					.getFastLanDao(getApplicationContext())
					.queryFastLan("", delvorgcode, username);

		}
		count = deleteLanBeans.size();
		notifyAutoCountLanTouAdapter("");
		btn_group_activity_taskshow.setText(str);
		tv_group_num.setText("(" + count + ")");
	}

	// 派单信息查询
	private void initData(String str) {
		Log.e("条数-------", "tiaoshu="+gatherDao.queryGatherMessage(delvorgcode, username).size());
		beans.clear();
		beans = gatherDao.orderByDistance_Query(str, delvorgcode, username);
		int beansSize = beans.size();
		Log.e("数量----", beansSize+"");
		dataList = new ArrayList<Map<String, String>>();
		for (int a = 0; a < beansSize; a++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("phone", beans.get(a).getPhone());
			map.put("clientname", beans.get(a).getClientname());
			map.put("address", beans.get(a).getAddress());
			map.put("lssx", beans.get(a).getLssx());
			map.put("cnday", beans.get(a).getCnday());
			map.put("reservenum", beans.get(a).getReservenum());
			map.put("companyid", beans.get(a).getCompanyid());
			map.put("remind", beans.get(a).getRemind());
			dataList.add(map);
		}
		adapter = new MyAdapter(dataList);
		listView.setAdapter(adapter);

	}

	// 揽收信息查询
	protected void notifyAutoCountLanTouAdapter(String str) {
		if (dealType == Constant.COLLECTION) {
			deleteLanBeans = DeliverDaoFactory.getInstance()
					.getFastLanDao(getApplicationContext())
					.queryAutoFastData(str, delvorgcode, username);
		} else if (dealType == Constant.NOUPDATA) {
			deleteLanBeans = DeliverDaoFactory.getInstance()
					.getFastLanDao(getApplicationContext())
					.queryFastLan(str, delvorgcode, username);
		}

		gatherCount = deleteLanBeans.size();
		dataList1 = new ArrayList<Map<String, String>>();
		for (int a = 0; a < gatherCount; a++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("MailNum", deleteLanBeans.get(a).getMailNum());
			map.put("RcvArea", deleteLanBeans.get(a).getRcvArea());
			map.put("Customer", deleteLanBeans.get(a).getCustomer());
			map.put("ActualWeight", deleteLanBeans.get(a).getActualWeight());
			map.put("ActualTotalFee", deleteLanBeans.get(a).getActualWeight());
			map.put("mainmail", deleteLanBeans.get(a).getMainmail());
			dataList1.add(map);
		}
		// if (null == countLanTouAdapter) {
		countLanTouAdapter = new CountLanTouAdapter(dataList1);
		swipeListView.setAdapter(countLanTouAdapter);
		// } else {
		// countLanTouAdapter.setnotifyDataSetChanged(dataList1);
		// }

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
		adapter = new MyAdapter(dataList);

		listView.setAdapter(adapter);
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
								if (null == adapter) {
									adapter = new MyAdapter(dataList);
									listView.setAdapter(adapter);
								} else {
									adapter.setnotifyDataSetChanged(dataList);
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
				intent.setClass(CollectionActivity.this,
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
					notifyAutoCountLanTouAdapter(input);
				}
			}
		});
		swipeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Intent intent = new Intent(CollectionActivity.this,
						LanTouDetailActivity.class);
				List<FastLanBean> beans = countLanTouAdapter
						.getDeleteLanBeans();
				FastLanBean bean = beans.get(position);
				int _id = bean.getId();// TODO
				intent.putExtra("detail_id", _id);
				startActivity(intent);
			}
		});
	}

	private void inits() {
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
		swipeListView = (SwipeListView) findViewById(R.id.fragment_count_lantou_listView);
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
			notifyAutoCountLanTouAdapter(searchtext.getText().toString());
		}
		Utils.startIntentService(CollectionActivity.this);// 启动投递、揽收异步上传服务

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
		XFAudio soundAudio = new XFAudio(CollectionActivity.this, sound,
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
				startActivityForResult(new Intent(CollectionActivity.this,
						CaptureActivity.class), 1);
				break;

			case R.id.nCollection: // 无派单揽收录入
				Intent intent = new Intent(CollectionActivity.this,
						ClctActivityDetails.class);
				Bundle bundle = new Bundle();
				bundle.putString("nullCollection", nullCollection);
				bundle.putString("noCollection", "无派揽信息");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_map_collection:
				Intent ite = new Intent(CollectionActivity.this,
						MapForClctTask.class);
				startActivity(ite);
				break;
			case R.id.btn_beginto_update:
				if (Utils.isNetworkAvailable(CollectionActivity.this)) {
					if(DeliverDaoFactory.getInstance().getFastLanDao(getApplicationContext()).queryFastLanMessageweishangchuan().size()>0){
						Utils.startIntentService(CollectionActivity.this);
						Toast.makeText(CollectionActivity.this, "上传服务已开启，正在为您上传数据",
								Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(CollectionActivity.this, "没有未上传数据",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CollectionActivity.this, "网络不好是没办法提交数据的哦",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	};
	
	/**
	 * 任务确认/任务完成
	 */
	private void getEnSureTask(final String reservenum,final String companyid,final String phone) {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				fastbean bean= new fastbean();
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
				String urlString = Global.EnSureTask_URL;
				String jsonString = JSON.toJSONString(bean);
				JSONObject jsonObject = null;
				try {
					String str1 = NetHelper.doPostJson(urlString, jsonString,
							"json");
					 Log.e("jsonString", jsonString + "");
					 Log.e("str1", str1 + "");
					jsonObject = new JSONObject(str1);
					String result = jsonObject.getString("result");
					Log.e("result", result + "");
					if (result.equals("1")) {
						DeliverDaoFactory.getInstance()
						.getGather_msgdao(getApplicationContext()).updateEnSureTask(reservenum, "0");
						
					}
				} catch (JSONException e) {
				}
				return jsonObject;
			}

			protected void onPostExecute(JSONObject result) {
				adapter.notifyDataSetChanged();
			}
		}.execute();
	}


	/**
	 * 
	 * adpter信息
	 * 
	 * @author quanyi
	 * 
	 */
	public class MyAdapter extends BaseAdapter {

		List<Map<String, String>> dataList;
		ViewHolder holder = null;

		public MyAdapter(List<Map<String, String>> dataList) {
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

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(getApplicationContext(),
						R.layout.clct_list_item, null);
				// holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);
				// holder.cBox.setChecked(isSelected.get(position));
				holder.relativeLayout_xianshi=(RelativeLayout) convertView.findViewById(R.id.relativeLayout_xianshi);
				holder.relativeLayout_yincang=(RelativeLayout) convertView.findViewById(R.id.relativeLayout_yancang);
				holder.linearLayout_Remind=(LinearLayout) convertView.findViewById(R.id.linearLayout_Remind);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.btn_phone = (Button) convertView
						.findViewById(R.id.btn_phone);
				holder.btn_message = (Button) convertView
						.findViewById(R.id.btn_message);
				holder.btn_fastCollection = (Button) convertView
						.findViewById(R.id.btn_fastCollection);
				holder.btn_queding=(Button) convertView.findViewById(R.id.btn_queding);
				holder.clientname = (TextView) convertView
						.findViewById(R.id.clientname);
				holder.address = (TextView) convertView
						.findViewById(R.id.address);
				holder.tv_client_reservenum = (TextView) convertView
						.findViewById(R.id.tv_client_reservenum);

				holder.lssx = (TextView) convertView.findViewById(R.id.lssx);
				holder.cnday = (TextView) convertView.findViewById(R.id.cnday);

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
			final String companyid = dataList.get(position).get("companyid");
			final String cnday = dataList.get(position).get("cnday");
			final String remind = dataList.get(position).get("remind");
			Log.e("remind", remind);
            if (cnday.equals("0")) {
            	holder.relativeLayout_xianshi.setVisibility(View.VISIBLE);
            	holder.relativeLayout_yincang.setVisibility(View.GONE);
              }else {
            	  holder.relativeLayout_yincang.setVisibility(View.VISIBLE);
            	  holder.relativeLayout_xianshi.setVisibility(View.GONE);
			}
            if (remind!=null&&remind.equals("1")) {
            	holder.linearLayout_Remind.setVisibility(View.VISIBLE);
			}else {
				holder.linearLayout_Remind.setVisibility(View.GONE);
			}
			holder.phone.setText(dataList.get(position).get("phone"));
			holder.clientname.setText(dataList.get(position).get("clientname"));
			holder.address.setText(dataList.get(position).get("address"));
			holder.lssx.setText(dataList.get(position).get("lssx"));
			holder.cnday.setText("客户催单");
			holder.tv_client_reservenum.setText(reservenum);
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
//						// TODO
//						Toast.makeText(CollectionActivity.this, "非手机号,不能发短信",
//								Toast.LENGTH_SHORT).show();
					}
				}
			});
			holder.btn_fastCollection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CollectionActivity.this,
							NoClctActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("danhao1", reservenum);
					bundle.putString("phone", phone);
					bundle.putString("clientname", clientname);
					bundle.putString("address", address);
					bundle.putString("yesCollection", "派单信息");
					intent.putExtras(bundle);
					startActivity(intent);
					// finish();
				}
			});
			holder.btn_queding.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getEnSureTask(reservenum,companyid,phone);
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
					map.put("lssx", beans.get(a).getLssx());
					map.put("cnday", beans.get(a).getCnday());
					map.put("reservenum", beans.get(a).getReservenum());
					map.put("companyid", beans.get(a).getCompanyid());
					map.put("remind", beans.get(a).getRemind());
					dataList.add(map);
				}
				tv_group_num.setText("(" + beans.size() + ")" + "");
				super.notifyDataSetChanged();
			}
		}

	}

	private class ViewHolder {
		// CheckBox cBox;
		TextView phone, clientname, address, lssx, cnday, tv_client_reservenum;
		Button btn_fastCollection,btn_queding,btn_message,btn_phone;
		RelativeLayout relativeLayout_xianshi,relativeLayout_yincang;
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
									CollectionActivity.this).getDELVORGCODE())); // 机构号
					paramObject.add(new BasicNameValuePair("username",
							SharePreferenceUtilDaoFactory.getInstance(
									CollectionActivity.this).getUSERNAME())); // 员工号
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
									CollectionActivity.this,
									result.getJSONObject("body")
											.getJSONArray("success")
											.getJSONObject(0).getString("msg"),
									1).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(CollectionActivity.this, "短信发送失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(CollectionActivity.this,
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
					map.put("lssx", beans.get(a).getLssx());
					map.put("cnday", beans.get(a).getCnday());
					map.put("reservenum", beans.get(a).getReservenum());
					map.put("companyid", beans.get(a).getCompanyid());
					map.put("remind", beans.get(a).getRemind());
					dataList.add(map);
				}
			} else if (Constant.ACTION_DOWN_DATA_OVER
					.equals(intent.getAction())) {
				// 数据更新完毕的通知
//				Log.e("33333333333333", "33333333333333");
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
							map.put("lssx", beans.get(a).getLssx());
							map.put("cnday", beans.get(a).getCnday());
							map.put("reservenum", beans.get(a).getReservenum());
							map.put("companyid", beans.get(a).getCompanyid());
							map.put("remind", beans.get(a).getRemind());
							dataList.add(map);
						}
					} catch (Exception e) {
					}
					break;
				case Constant.PUSH_TYPE_CLCTTASK2:
//					Log.e("444444444444", "444444444444444");
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
							map.put("lssx", beans.get(a).getLssx());
							map.put("cnday", beans.get(a).getCnday());
							map.put("reservenum", beans.get(a).getReservenum());
							map.put("companyid", beans.get(a).getCompanyid());
							map.put("remind", beans.get(a).getRemind());
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
							map.put("lssx", beans.get(a).getLssx());
							map.put("cnday", beans.get(a).getCnday());
							map.put("reservenum", beans.get(a).getReservenum());
							map.put("companyid", beans.get(a).getCompanyid());
							map.put("remind", beans.get(a).getRemind());
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
			dialog = new Dialog(CollectionActivity.this, R.style.dialogss);
			view = View.inflate(CollectionActivity.this, R.layout.dialog_sms,
					null);
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

	/***
	 * 执行类 异步
	 * 
	 * @author quanyi
	 * 
	 */
	class MyAsyncTask extends AsyncTask<Void, Void, Void> {
		private Context context;
		private int index;// 用于判断是下拉刷新还是点击加载更多

		public MyAsyncTask(Context context, int index) {
			this.context = context;
			this.index = index;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// if (index == DRAG_INDEX)
			listView.onRefreshComplete();
			// else if (index == LOADMORE_INDEX)
			// listView.onLoadMoreComplete(false);
		}

	}

	private void showpop() {
		View v = LayoutInflater.from(this).inflate(R.layout.collection_pop,
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
				.findViewById(R.id.collection_textview);
		TextView nocollection_textview = (TextView) v
				.findViewById(R.id.nocollection_textview);
		LinearLayout linearLayoutc = (LinearLayout) v
				.findViewById(R.id.collection_pop);
		LinearLayout linearLayoutweiLayout = (LinearLayout) v
				.findViewById(R.id.weishangchuan_pop);
		TextView textViewwei = (TextView) v
				.findViewById(R.id.weishangchuan_textview);
		linearLayoutnoc.setOnClickListener(clicklistener);
		linearLayoutc.setOnClickListener(clicklistener);
		linearLayoutweiLayout.setOnClickListener(clicklistener);
		int gatherCount2 = DeliverDaoFactory.getInstance()
				.getFastLanDao(getApplicationContext())
				.queryFastLan("", delvorgcode, username).size();
		int gatherCount1 = DeliverDaoFactory.getInstance()
				.getFastLanDao(getApplicationContext())
				.queryAutoFastData("", delvorgcode, username).size();
		collection_textview.setText("(" + gatherCount1 + ")" + "");
		nocollection_textview.setText("(" + dataList.size() + ")" + "");
		textViewwei.setText("(" + gatherCount2 + ")" + "");

	}

	public class CountLanTouAdapter extends BaseAdapter {
		private Context context = getApplicationContext();
		List<Map<String, String>> dataList;

		public CountLanTouAdapter(List<Map<String, String>> dataList) {
			this.dataList = dataList;

		}

		public CountLanTouAdapter(Context context, int rightWidth) {
			this.context = context;
			mRightWidth = rightWidth;
		}

		public void setnotifyDataSetChanged(List<Map<String, String>> dataList) {
			this.dataList = dataList;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return deleteLanBeans.size();
		}

		@Override
		public Object getItem(int position) {
			return deleteLanBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder;
			final int temppos = position;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.item_list_tongji, null);
				viewHolder = new ViewHolder();
				viewHolder.item_left = convertView
						.findViewById(R.id.item_left1);
				viewHolder.item_right = convertView
						.findViewById(R.id.item_right1);
				viewHolder.item_right_txt = (TextView) convertView
						.findViewById(R.id.item_right_txt1);
				viewHolder.textViewdanhao = (TextView) convertView
						.findViewById(R.id.fragment_countlantou_item_danhao);
				viewHolder.textViewKeHu = (TextView) convertView
						.findViewById(R.id.fragment_countlantou_item_kehu1);
				viewHolder.textViewNumber = (TextView) convertView
						.findViewById(R.id.fragment_countlantou_item_number);
				viewHolder.textViewAddress = (TextView) convertView
						.findViewById(R.id.fragment_countlantou_item_address);
				viewHolder.textViewZhongLiang = (TextView) convertView
						.findViewById(R.id.fragment_count_item_zhongliang1);
				viewHolder.textViewMoney = (TextView) convertView
						.findViewById(R.id.fragment_count_item_money1);
				viewHolder.textViewZhuDan = (TextView) convertView
						.findViewById(R.id.fragment_count_item_zhudanhao1);
				viewHolder.relativeLayoutzhudan = (RelativeLayout) convertView
						.findViewById(R.id.fragment_count_item_zhudanhao2);
				viewHolder.relativeLayoutzhongliang = (RelativeLayout) convertView
						.findViewById(R.id.fragment_count_item_zhongliang_relat);
				viewHolder.relativeLayoutmoney = (RelativeLayout) convertView
						.findViewById(R.id.fragment_count_item_money_relat);
				viewHolder.relativeLayoutdanhao = (RelativeLayout) convertView
						.findViewById(R.id.fragment_count_item_danhao_relat);
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
					// 修改到待上传的揽收列表
					// 删除主单号，提示，会删除所有的子单
					// 删除子单，提示，不能删除子单，请删除对应的主单
					DeliverDaoFactory
							.getInstance()
							.getFastLanDao(getApplicationContext())
							.updateOpreate("D",
									deleteLanBeans.get(temppos).getMailNum());
					DeliverDaoFactory
							.getInstance()
							.getMoneyDao(context)
							.delete_GatherMoney(
									deleteLanBeans.get(temppos).getMainmail());

					if (Utils.isNetworkAvailable(CollectionActivity.this)) {
						Utils.startIntentService(CollectionActivity.this);
						Toast.makeText(CollectionActivity.this,
								"你的揽收信息已上传，未上传的请手动上传", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(CollectionActivity.this,
								"网络不好是没办法提交数据，请手动上传", Toast.LENGTH_SHORT)
								.show();
					}
					notifyDataSetChanged();
				}
			});
			if (deleteLanBeans.get(position).getCustomer().toString() == null
					|| "".equals(deleteLanBeans.get(position).getCustomer()
							.toString())) {
				viewHolder.textViewKeHu.setText("");
				viewHolder.textViewKeHu.setVisibility(View.GONE);
			} else {
				viewHolder.textViewKeHu.setVisibility(View.VISIBLE);
				CustomerDao customerDao = DeliverDaoFactory.getInstance()
						.getCustomerDao(context);
				viewHolder.textViewKeHu
						.setText(customerDao.FindDaName(deleteLanBeans.get(
								position).getCustomer()));
			}
			if (deleteLanBeans.get(position).getMailNum() == null) {
				viewHolder.textViewNumber.setText("");
			} else {
				viewHolder.textViewNumber.setText(deleteLanBeans.get(position)
						.getMailNum());
			}
			if (deleteLanBeans.get(position).getRcvArea() == null) {
				viewHolder.textViewAddress.setText("");
			} else {
				QsjGndmDao qsjGndmDao = DeliverDaoFactory.getInstance()
						.getQsjGndmDao(context);
				viewHolder.textViewAddress.setText(qsjGndmDao
						.FindDaName(deleteLanBeans.get(position).getRcvArea()));
			}
			if (deleteLanBeans.get(position).getDan_num().toString() == null
					|| deleteLanBeans.get(position).getDan_num().toString()
							.equals("")) {
				viewHolder.textViewdanhao.setText("");
			} else {
				viewHolder.relativeLayoutdanhao.setVisibility(View.VISIBLE);
				viewHolder.textViewdanhao.setText(deleteLanBeans.get(position)
						.getDan_num());
			}
			if (deleteLanBeans.get(position).getActualWeight().toString() == null
					|| deleteLanBeans.get(position).getActualWeight()
							.toString().equals("")) {
				viewHolder.textViewZhongLiang.setText("");
			} else {
				viewHolder.relativeLayoutzhongliang.setVisibility(View.VISIBLE);
				viewHolder.textViewZhongLiang.setText(deleteLanBeans.get(
						position).getActualWeight()
						+ " kg");
			}
			if (deleteLanBeans.get(position).getActualTotalFee() == null
					|| deleteLanBeans.get(position).getActualWeight()
							.toString().equals("")) {
				viewHolder.textViewMoney.setText("");
			} else {
				viewHolder.relativeLayoutmoney.setVisibility(View.VISIBLE);
				viewHolder.textViewMoney.setText(deleteLanBeans.get(position)
						.getActualTotalFee() + " 元");
			}
			if (deleteLanBeans.get(position).getMainmail() == null
					|| "".equals(deleteLanBeans.get(position).getMainmail())) {
				viewHolder.textViewZhuDan.setText("");
				viewHolder.relativeLayoutzhudan.setVisibility(View.GONE);
			} else {
				viewHolder.textViewZhuDan.setText(deleteLanBeans.get(position)
						.getMainmail());
				viewHolder.relativeLayoutzhudan.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		@Override
		public void notifyDataSetChanged() {
			if (dealType == Constant.COLLECTION) {
				deleteLanBeans = DeliverDaoFactory
						.getInstance()
						.getFastLanDao(getApplicationContext())
						.queryAutoFastData(searchtext.getText().toString(),
								delvorgcode, username);
			} else if (dealType == Constant.NOUPDATA) {
				deleteLanBeans = DeliverDaoFactory
						.getInstance()
						.getFastLanDao(getApplicationContext())
						.queryFastLan(searchtext.getText().toString(),
								delvorgcode, username);
			}

			tv_group_num.setText("(" + deleteLanBeans.size() + ")" + "");
			super.notifyDataSetChanged();
		}

		public List<FastLanBean> getDeleteLanBeans() {
			return deleteLanBeans;
		}

		class ViewHolder {
			private TextView textViewKeHu, textViewNumber, textViewAddress,
					item_right_txt, textViewZhongLiang, textViewMoney,
					textViewdanhao, textViewZhuDan;
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
						// TODO: handle exception
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

	private String getTransCode(String name) {
		String code = "";
		TransportTypeDao transportTypeDao = DeliverDaoFactory.getInstance()
				.getTransportTypeDao(this);
		code = transportTypeDao.queryTransferCode(name);
		return code;
	}

	private String getMailCode(String name) {
		String code = "";
		MailTypeDao mailtypedao = DeliverDaoFactory.getInstance()
				.getMailTypeDao(this);
		code = mailtypedao.queryMailCode(name);
		return code;
	}

	private UserInfoUtils userInfo;

	private String mailPutMoneyData(List<GatherInfoBean> touBeans,
			List<GatherInfoBean> lanBeans) {
		userInfo = new UserInfoUtils(CollectionActivity.this);
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
		TelephonyManager telephonemanage = (TelephonyManager) CollectionActivity.this
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonemanage.getDeviceId();

	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_GATHER_SC.equals(intent.getAction())) {
				if (countLanTouAdapter != null) {
					countLanTouAdapter.notifyDataSetChanged();
				}
			}else if (Constant.ACTION_BLUTTOOTH_MSG.equals(intent.getAction())){
				String code = intent.getStringExtra("code");
				if(code != null){
					searchtext.setText(code);
				}
			}
		}

	}

}

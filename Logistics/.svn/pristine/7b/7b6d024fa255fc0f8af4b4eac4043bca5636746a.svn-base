package com.ems.express.fragment.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.mail.MailHistoryItemAdapter;
import com.ems.express.bean.MailInfo;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.LoginActivity;
import com.ems.express.ui.mail.MailDetailActivity;
import com.ems.express.ui.mail.MailOrderListActivity;
import com.ems.express.util.DeviceUtil;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.google.zxing.common.StringUtils;

public class MailHistoryFragment extends Fragment implements OnScrollListener {

	private boolean stayInThisFragment = true;
	Context mContext;

	private View view;
	public static ListView listView;
	private List<MailInfo> historyList;
	public static MailHistoryItemAdapter historyAdapter;

	// 总页数，当前页，最后一条记录索引
	private int toalPage;
	private int currentPage;
	private int lastIndex;

	private RelativeLayout mNotRecord;
	private RelativeLayout mNotConnection;

	private TextView mTvFlush;

	// 菊花工具
	private AnimationUtil util;
	
	//新消息广播
	public BroadcastReceiver newMsgBReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			historyList.clear();
			currentPage = 0;
			getOrderHistory();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.mail_history_fragment, null);
		mContext = this.getActivity();
		listView = (ListView) view.findViewById(R.id.lv_history);
		listView.setDividerHeight(0);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int i, long l) {
				Intent intent = new Intent(mContext, MailDetailActivity.class);
				intent.putExtra("orderNo", historyList.get(i).getSid());
				mContext.startActivity(intent);
			}
		});

		currentPage = 0;

		mNotRecord = (RelativeLayout) view.findViewById(R.id.rl_notpackage);
		mNotConnection = (RelativeLayout) view
				.findViewById(R.id.rl_notconnection);

		mTvFlush = (TextView) view.findViewById(R.id.tv_flush);
		mTvFlush.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		mTvFlush.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				getOrderHistory();

			}
		});

		util = new AnimationUtil(mContext, R.style.dialog_animation);

		historyList = new ArrayList<MailInfo>();
		// 获取数据
		getOrderHistory();
		Log.e("msggghistoryList", historyList.toString());
		
		// 注册一个自定义的广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("NewMsgReceiver_Action");
		mContext.registerReceiver(newMsgBReceiver, filter);
		
		return view;
	}

	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MailOrderListActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 计算最后可见条目的索引
		lastIndex = firstVisibleItem + visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastIndex == historyAdapter.getCount()) {
			if (currentPage == toalPage) {
				ToastUtil.show(mContext, "没有其他数据");
			} else if (currentPage < toalPage) {
				getOrderHistory();
			}

		}

	}

	@Override
	public void onResume() {
		currentPage = 0;
		stayInThisFragment = true;
		super.onResume();
	}

	@Override
	public void onPause() {
		stayInThisFragment = false;
		super.onPause();
	}

	/**
	 * 刷新
	 * 
	 * @param view
	 */
	public void flush(View view) {
		getOrderHistory();
	}

	/**
	 * 获取历史邮件
	 * 
	 * @return
	 */
	public void getOrderHistory() {
		// 判断是否登录
		String userId = SpfsUtil.loadId();
		if (userId.isEmpty()) {
			loginTips(mContext);
		}
		util.show();
		// 设置参数
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("deviceNo", DeviceUtil.getDeviceNo(mContext));
		json.put("deviceType", "android");
		json.put("customCode", SpfsUtil.loadPhone());
		// json.put("customCode","258729103277791");
		json.put("pageNo", currentPage + 1);
		json.put("limit", 30);
		String params = ParamsUtil.getUrlParamsByMap(json);
		Log.e("msgggParams", params);
		// 发送请求
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST,
				null, Constant.QueryMailHistory2,
				new Response.Listener<Object>() {
					@Override
					public void onResponse(Object result) {
						// 结束菊花
						util.dismiss();
						// result = creatDate();//造假数据
						Log.e("msgggResult", result.toString());
						if (stayInThisFragment) {
							if ("null".equals(result) || result == null
									|| result.toString().isEmpty()) {
								ToastUtil.show(mContext, "查询失败，请稍后重试");
								return;
							}
							try {
								JSONObject jso = new JSONObject(result
										.toString());
								// 寄件接口2
								if ("1".equals(jso.get("result"))) {
									// currentPage = Integer.parseInt(String
									// .valueOf(jso.get("pageNo")));
									currentPage += 1;
									toalPage = Integer.parseInt(String
											.valueOf(jso.get("totalPage")));
									// JSONArray mails =
									// jso.getJSONObject("body").getJSONObject("success").getJSONArray("resultList");
									JSONArray mails = jso
											.getJSONArray("resultList");
									// 封装mailInfo
									for (int i = 0; i < mails.length(); i++) {
										JSONObject mail = mails
												.getJSONObject(i);
										MailInfo mailInfo = new MailInfo();
										String state = mail.getString("status");
										if ("1".equals(state)
												|| "2".equals(state)
												|| "3".equals(state)
												/*|| "5".equals(state)*/) {
											mailInfo.setMailState(state);
											mailInfo.setSid(String.valueOf(mail
													.getString("orderIid")));
											historyList.add(mailInfo);
										}
									}
									Log.e("msgggmailList",
											historyList.toString());
									// 给listview添加数据
									setListView(listView, historyList);
								} else {
									ToastUtil.show(mContext, "未查询到数据");
									util.dismiss();
								}

								if (historyList.size() < 1) {
									mNotRecord.setVisibility(View.VISIBLE);
									mNotConnection.setVisibility(View.GONE);
								} else {
									mNotRecord.setVisibility(View.GONE);
									mNotConnection.setVisibility(View.GONE);
								}
							} catch (Exception e) {
								if (historyList.size() < 1) {
									mNotRecord.setVisibility(View.VISIBLE);
									mNotConnection.setVisibility(View.GONE);
								} else {
									mNotRecord.setVisibility(View.GONE);
									mNotConnection.setVisibility(View.GONE);
								}
								return;
							}
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// 结束菊花
						util.dismiss();
						arg0.printStackTrace();
						if (stayInThisFragment) {
							ToastUtil.show(mContext, "查询失败");
							if (historyList.size() < 1) {
								mNotRecord.setVisibility(View.GONE);
								mNotConnection.setVisibility(View.VISIBLE);
							} else {
								mNotRecord.setVisibility(View.GONE);
								mNotConnection.setVisibility(View.GONE);
							}
						}

					}
				}, params);
		App.getQueue().add(req);
	}

	/**
	 * 获取更多的数据
	 */
	/*
	 * public void getMoreOrderHistory(){ //判断是否登录 String userId =
	 * SpfsUtil.loadId(); if (userId.isEmpty()) { loginTips(mContext); }
	 * util.show(); //设置参数 HashMap<String, Object> json = new HashMap<String,
	 * Object>(); json.put("deviceNo",DeviceUtil.getDeviceNo(mContext));
	 * json.put("deviceType","android");
	 * json.put("customCode",SpfsUtil.loadPhone()); //
	 * json.put("customCode","258729103277791"); json.put("pageNo",currentPage +
	 * 1); json.put("limit", 20); String params =
	 * ParamsUtil.getUrlParamsByMap(json);
	 * 
	 * }
	 */

	/*
	 * 登录提示dialog
	 */
	public void loginTips(final Context context) {
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoginActivity.startAction(context);
					}
				}).create();
		dialog.setMessage("您还未登录，请先登录。");
		dialog.show();
		return;
	}

	/*
	 * 给listview赋值
	 */
	public void setListView(ListView listView, final List<MailInfo> mails) {
		if (currentPage == 1) {
			historyAdapter = new MailHistoryItemAdapter(getActivity(), mails);
			listView.setAdapter(historyAdapter);
			// 绑定监听器
			listView.setOnScrollListener(this);
		} else {
			historyAdapter.notifyDataSetChanged();
		}

		/*
		 * listView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { Intent intent = new Intent(); List<String>
		 * mailNums = mails.get(position).getMailNum(); //传运单号
		 * intent.putExtra("mailNums", (Serializable)mailNums);
		 * intent.setClass(getActivity(), MailTransListActivity.class);
		 * startActivity(intent); } });
		 */
	}

	/**
	 * 造假数据
	 * 
	 * @return
	 */

	String creatDate() {
		JSONObject json = new JSONObject();
		try {
			json.put("result", "1");
			json.put("pageNo", "10");
			json.put("totalPage", "5");

			JSONArray resultList = new JSONArray();

			JSONObject r1 = new JSONObject();
			r1.put("orderIid", 11111);

			r1.put("mailNum", "1083232779113");
			r1.put("status", "4");

			JSONObject r2 = new JSONObject();
			r2.put("orderIid", 22222);
			r2.put("mailNum", "1083232779113,1112260148611");
			r2.put("status", "4");

			JSONObject r3 = new JSONObject();
			r3.put("orderIid", 33333);
			r3.put("mailNum",
					"1083232779113,1112260148611,1083232779113,1112260148611");
			r3.put("status", "1");

			JSONObject r4 = new JSONObject();
			r4.put("orderIid", 4444);
			r4.put("mailNum", "1083232779113,1083232779113");
			r4.put("status", "2");

			JSONObject r5 = new JSONObject();
			r5.put("orderIid", 55555);
			r5.put("mailNum", "null");
			r5.put("status", "5");

			JSONObject r6 = new JSONObject();
			r6.put("orderIid", 666666);
			r6.put("mailNum", "1083232779113,1083232779113");
			r6.put("status", "3");

			JSONObject r7 = new JSONObject();
			r7.put("orderIid", 7777);

			r7.put("mailNum", "1083232779113");
			r7.put("status", "3");

			JSONObject r10 = new JSONObject();
			r10.put("orderIid", 88888);

			r10.put("mailNum", "1083232779113");
			r10.put("status", "3");

			JSONObject r8 = new JSONObject();
			r8.put("orderIid", 99999);

			r8.put("mailNum", "1083232779113");
			r8.put("status", "3");

			JSONObject r9 = new JSONObject();
			r9.put("orderIid", 000000);

			r9.put("mailNum", "1083232779113");
			r9.put("status", "3");

			resultList.put(r1);
			resultList.put(r2);
			resultList.put(r3);

			resultList.put(r4);
			resultList.put(r5);
			resultList.put(r6);

			resultList.put(r7);
			resultList.put(r8);
			resultList.put(r9);
			resultList.put(r10);
			resultList.put(r4);
			resultList.put(r5);
			resultList.put(r6);

			resultList.put(r7);
			resultList.put(r8);
			resultList.put(r9);
			resultList.put(r10);

			json.put("resultList", resultList);

			json = new JSONObject(json.toString().replaceAll("\"null\"",
					" null"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("msgggjson", json.toString());
		return json.toString();
	}

}
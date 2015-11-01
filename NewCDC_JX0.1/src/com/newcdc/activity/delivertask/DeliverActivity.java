/**
 * 
 */
package com.newcdc.activity.delivertask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dtr.zxing.activity.CaptureActivity;
import com.newcdc.R;
import com.newcdc.R.color;
import com.newcdc.activity.censcus.CountLanTouActivity;
import com.newcdc.application.BaseActivity;
import com.newcdc.application.MainActivity;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.DeliverMailDao;
import com.newcdc.tools.Constant;
import com.newcdc.tools.ExpressMailUtils;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;

/**
 * 下段
 * 
 * @author zhangfan 2015-3-26,下午2:22:52
 * 
 */
public class DeliverActivity extends Activity implements OnClickListener {
	// private View mView;
	private Button btn_back;
	private Button btn_group;
	private Button btn_edit;
	private Button btn_editor_fragment_deliver1;
	private Context context;
	private LinearLayout ll_group;
	private PopupWindow popupWindow;
	private DeliverDaoFactory daoFactory;
	private DeliverMailDao mailDao;
	private UserInfoUtils user;
	private String deviceNo;
	private String orgCode;
	private String userName;
	private String dlvsectionid;
	private String dlvSectionCode;
	private ListView mListView;
	private ArrayList<HashMap<String, String>> mails;
	private DeliverAdapter adapter;
	public String curGroup;
	private Button del_err;
	private Button commit_all;
	private View footer;
	private Handler mHandler;
	private ProgressDialog mProgerssDialog;
	private MyReceiver mDeliverReceiver;
	private Dialog dealDialog;
	private int shoudong;

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// mView = inflater.inflate(R.layout.fragment_deliver, container, false);
	// context = DeliverActivity.this;
	// initView();
	// initData();
	// addListener();
	// return mView;
	// }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_deliver);
		context = DeliverActivity.this;
		initView();
		initData();
		addListener();
	}

	/**
	 * 初始化变量
	 */
	private void initData() {
		daoFactory = DeliverDaoFactory.getInstance();
		mHandler = new Handler();
		user = new UserInfoUtils(context);
		deviceNo = Utils.getDeviceId(context);
		orgCode = user.getUserDelvorgCode();
		userName = user.getUserName();
		dlvSectionCode = user.getDlvsectionCode();
		dlvsectionid = user.getDlvsectionid();
		mailDao = daoFactory.getDeliverMailDao(context);
		mails = mailDao.findWillCommitMail();
		adapter = new DeliverAdapter();
		mListView.setAdapter(adapter);
		curGroup = "未上传";
		notifyAdapter();
	}

	/**
	 * 注册监听器
	 */
	private void addListener() {
		btn_back.setOnClickListener(this);
		ll_group.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		del_err.setOnClickListener(this);
		commit_all.setOnClickListener(this);
		btn_editor_fragment_deliver1.setOnClickListener(this);
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back_fragment_deliver);
		btn_group = (Button) findViewById(R.id.btn_group_fragment_deliver);
		ll_group = (LinearLayout) findViewById(R.id.ll_group);
		btn_edit = (Button) findViewById(R.id.btn_editor_fragment_deliver);
		mListView = (ListView) findViewById(R.id.listView_fragment_deliver);
		btn_editor_fragment_deliver1 = (Button) findViewById(R.id.btn_editor_fragment_deliver1);
		footer = findViewById(R.id.view_oper_deliverfragment);
		del_err = (Button) footer
				.findViewById(R.id.btn_footer_deliverfragment_del_err);
		commit_all = (Button) footer
				.findViewById(R.id.btn_footer_deliverfragment_commit_all);
		mProgerssDialog = new ProgressDialog(context, "正在处理");
		mProgerssDialog.setCanCalcelable(false);
		mDeliverReceiver = new MyReceiver();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_fragment_deliver:// 返回键
			// MainActivity activity = (MainActivity) context;
			// activity.switchContentFragment(0);
			finish();
			break;
		case R.id.btn_editor_fragment_deliver:
			Intent intent = new Intent(context, CaptureActivity.class);
			intent.putExtra("fromDF", true);
			startActivityForResult(intent, 111);
			break;
		case R.id.btn_editor_fragment_deliver1:
			showTianJia();
			break;
		case R.id.ll_group:
			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			} else {
				showpop();
			}
			break;
		case R.id.layout_deliver_not_commit:
			curGroup = "未上传";
			notifyAdapter();
			popupWindow.dismiss();
			break;
		case R.id.layout_deliver_commit:
			curGroup = "已上传";
			notifyAdapter();
			popupWindow.dismiss();
			break;
		case R.id.btn_footer_deliverfragment_commit_all:// 全部提交
			showCommitDialog();
			break;
		case R.id.btn_footer_deliverfragment_del_err:// 删除异常邮件
			showDelDialog();
			break;
		}
	}

	/**
	 * 全部提交
	 */
	public void commitAll(final int frequence, final int submitType) {
		mProgerssDialog.toShow();

		new Thread() {
			@Override
			public void run() {
				ArrayList<HashMap<String, String>> mails = mailDao
						.findWillCommitMail();
				JSONObject json = new JSONObject();
				try {
					json.put("deviceNo", deviceNo);
					// json.put("frequence", String.valueOf(frequence));// 频次
					json.put("frequence", frequence);// 频次
					// json.put("submitType", String.valueOf(submitType));//
					// 提交类型->是否强制
					json.put("submitType", submitType);// 提交类型->是否强制
					// 拼mailList参数，是一个arrayList
					JSONArray mailList = new JSONArray();
					for (int i = 0; i < mails.size(); i++) {
						JSONObject mail = new JSONObject();
						HashMap<String, String> map = mails.get(i);
						mail.put("mailNum", map.get("mail_num"));// 邮件号
						String[] split = map.get("the_class_date").split(",");
						mail.put("operationTime", split[0]);// 操作时间
						mailList.put(i, mail);
					}
					json.put("mailList", mailList);// 邮件集合
					json.put("segmentCode", dlvSectionCode);// 道段号dlvsectionid
					// json.put("segmentCode", dlvsectionid);// 道段号
					json.put("orgCode", orgCode);// 机构号
					json.put("userCode", userName);// 工号
					Log.e("doDeliver--提交下段参数", json.toString());
					long startTimeMillis = System.currentTimeMillis();// 请求开始时间
					String operate_time = Utils.getCurrTime();// 操作时间
					long batch_no = new Date().getTime();
					if (mailList.length() > 0) {
						String result = NetHelper.doPostJson(Global.XIADUAN,
								json.toString(), "json");
						long endTime = System.currentTimeMillis();// 请求结束时间
						// 提交请求日志
						NetHelper.saveTransferLog(
								operate_time,
								endTime - startTimeMillis + "",
								context,
								context.getResources().getString(
										R.string.operate_action_deliver),
								mails.size() + "", batch_no + "");
						Log.e("doDeliver--返回下段参数", result);
						// 解析返回结果
						parseJsonUpdateTb(result, mails);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								mProgerssDialog.toDimiss();
								if (toastMsg != null) {
									Toast.makeText(context, toastMsg,
											Toast.LENGTH_SHORT).show();
								}
								curGroup = "未上传";
								notifyAdapter();
							}
						});
					}
					
				} catch (Exception e) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							mProgerssDialog.toDimiss();
							Toast.makeText(context, "下段异常！", Toast.LENGTH_SHORT)
									.show();
							curGroup = "未上传";
							notifyAdapter();
						}
					});
				}
			};
		}.start();
	}

	/**
	 * 强制上传
	 */
	public void commitAMail(final String frequence, final String mail_num) {

		mProgerssDialog.toShow();
		new Thread() {
			@Override
			public void run() {
				HashMap<String, String> map = mailDao
						.findMailByMailNumber(mail_num);
				JSONObject json = new JSONObject();
				try {
					json.put("deviceNo", deviceNo);
					json.put("frequence", frequence);// 频次
					json.put("submitType", Constant.SUBMITTYPE_QIANGZHI);// 提交类型->是否强制
					// 拼mailList参数，是一个arrayList
					JSONArray mailList = new JSONArray();
					JSONObject mail = new JSONObject();
					mail.put("mailNum", map.get("mail_num"));// 邮件号
					String[] split = map.get("the_class_date").split(",");
					mail.put("operationTime", split[0]);// 操作时间
					mailList.put(0, mail);
					json.put("mailList", mailList);// 邮件集合
					json.put("segmentCode", dlvSectionCode);// 道段号
					// json.put("segmentCode", dlvsectionid);// 道段号
					json.put("orgCode", orgCode);// 机构号
					json.put("userCode", userName);// 工号
					Log.e("doDeliver--提交参数", json.toString());
					long startTimeMillis = System.currentTimeMillis();// 请求开始时间
					String operate_time = Utils.getCurrTime();// 操作时间
					long batch_no = new Date().getTime();
					String result = NetHelper.doPostJson(Global.XIADUAN,
							json.toString(), "json");
					long endTime = System.currentTimeMillis();// 请求结束时间
					// 提交请求日志
					NetHelper.saveTransferLog(
							operate_time,
							endTime - startTimeMillis + "",
							context,
							context.getResources().getString(
									R.string.operate_action_deliver_qz), "1",
							batch_no + "");
					ArrayList<String> list = new ArrayList<String>();
					for (int i = 0; i < mails.size(); i++) {
						list.add(mail_num);
					}
					Log.e("doDeliver--返回参数", result);
					// 解析返回结果
					ArrayList<HashMap<String, String>> submitMail = new ArrayList<HashMap<String, String>>();
					submitMail.add(map);
					parseJsonUpdateTb(result, submitMail);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							try {
								mProgerssDialog.toDimiss();
								if (toastMsg != null) {
									Toast.makeText(context, toastMsg,
											Toast.LENGTH_SHORT).show();
								}
								curGroup = "未上传";
								notifyAdapter();
							} catch (Exception e) {
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private String toastMsg = null;

	/**
	 * 解析json并更改下段邮件表
	 * 
	 * @throws Exception
	 */
	private void parseJsonUpdateTb(String result,
			ArrayList<HashMap<String, String>> mails) throws Exception {
		JSONObject obj = new JSONObject(result);
		int re = obj.getInt("result");// 以re来区分提交结果
		switch (re) {
		case 1:// 提交成功，result标记为成功
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < mails.size(); i++) {
				list.add(mails.get(i).get("mail_num"));
			}
			mailDao.updateResult(Constant.RESULT_SUCC, list);
			toastMsg = "下段成功！";
			break;
		case 0:// 失败，返回字段存在content则禁止再提交，不包含content字段则不做任何处理
			try {
				JSONObject content = obj.getJSONObject("content");// 如果不报异常，则有content字段
				parseErrContent(content, Constant.RESULT_ERR);// 解析content内容
			} catch (Exception e) {// 报异常，则不存在content字段,网络等原因导致失败，不做任何操作
				toastMsg = "提交失败，请重试";
			}
			break;
		case 2:// 警告
			try {
				JSONObject content = obj.getJSONObject("content");
				parseErrContent(content, Constant.RESULT_QIANGZHI);// 解析content内容
			} catch (Exception e) {
			}
			break;
		}
	}

	/**
	 * 返回result=2，要提示用户强制提交的邮件
	 */
	public void parseErrContent(JSONObject content, int result) {
		try {// 经济一票多件
			JSONObject warData = content.getJSONObject("warrMailData");
			JSONArray dataList = warData.getJSONArray("dataList");
			ArrayList<String> mailNumberList = new ArrayList<String>();
			for (int i = 0; i < dataList.length(); i++) {
				JSONObject data = dataList.getJSONObject(i);
				String mainMailNum = data.getString("mainMailNum");
				String mailNum = data.getString("mailNum");
				String mailCount = data.getString("mailCount");
				mailNumberList.add(mailNum);
			}
			String msgDesc = warData.getString("msgDesc");
			String msgCode = warData.getString("msgCode");
			mailDao.updateMsgDesc(msgDesc, mailNumberList);// 更改处理结果描述
			mailDao.updateResult(result, mailNumberList);// 更改处理结果
		} catch (Exception e) {
			Log.e("经济一票多件", "解析json异常：" + e.getMessage());
		}
		try {// 已经做过下段邮件
			JSONObject createdData = content.getJSONObject("createdMailData");
			JSONArray dataList = createdData.getJSONArray("dataList");
			ArrayList<String> mailNumberList = new ArrayList<String>();
			for (int i = 0; i < dataList.length(); i++) {
				JSONObject data = dataList.getJSONObject(i);
				String lastOperTime = data.getString("lastOperTime");
				String lastOperUserCode = data.getString("lastOperUserCode");
				String mailNum = data.getString("mailNum");
				String lastOperUserName = data.getString("lastOperUserName");
				mailNumberList.add(mailNum);
			}
			String msgDesc = createdData.getString("msgDesc");
			String msgCode = createdData.getString("msgCode");
			mailDao.updateMsgDesc(msgDesc, mailNumberList);// 更改处理结果描述
			mailDao.updateResult(result, mailNumberList);// 更改处理结果
		} catch (Exception e) {
			Log.e("已经做过下段邮件", "解析json异常：" + e.getMessage());
		}
		try {// 苹果一票多件
			JSONObject mainData = content.getJSONObject("mainMailData");
			JSONArray dataList = mainData.getJSONArray("dataList");
			ArrayList<String> mailNumberList = new ArrayList<String>();
			for (int i = 0; i < dataList.length(); i++) {
				JSONObject data = dataList.getJSONObject(i);
				String mainMailNum = data.getString("mainMailNum");
				String mailNum = data.getString("mailNum");
				String mailCount = data.getString("mailCount");
				mailNumberList.add(mailNum);
			}
			String msgDesc = mainData.getString("msgDesc");
			String msgCode = mainData.getString("msgCode");
			mailDao.updateMsgDesc(msgDesc, mailNumberList);// 更改处理结果描述
			mailDao.updateResult(result, mailNumberList);// 更改处理结果
		} catch (Exception e) {
			Log.e("苹果一票多件", "解析json异常：" + e.getMessage());
		}
		try {// 投递时间未到
			JSONObject dlvData = content.getJSONObject("dlvMailData");
			JSONArray dataList = dlvData.getJSONArray("dataList");
			ArrayList<String> mailNumberList = new ArrayList<String>();
			for (int i = 0; i < dataList.length(); i++) {
				JSONObject data = dataList.getJSONObject(i);
				String mailNum = data.getString("mailNum");
				String designatedDlvDate = data.getString("designatedDlvDate");
				mailNumberList.add(mailNum);
			}
			String msgDesc = dlvData.getString("msgDesc");
			String msgCode = dlvData.getString("msgCode");
			mailDao.updateMsgDesc(msgDesc, mailNumberList);// 更改处理结果描述
			mailDao.updateResult(result, mailNumberList);// 更改处理结果
		} catch (Exception e) {
			Log.e("投递时间未到", "解析json异常：" + e.getMessage());
		}
		toastMsg = "提交失败，您需要手动处理这些邮件,然后重新提交";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 111) {
			// String mail_num = data.getStringExtra("txtResult");
			Bundle bundle = data.getExtras();
			ArrayList<String> mailList = (ArrayList<String>) bundle
					.getSerializable("mailList");
			// ArrayList<String> mailList = new ArrayList<String>();
			// mailList.add("1000376036802");
			// mailList.add("1004549109705");
			// mailList.add("1050041256609");
			// mailList.add("1011646848205");
			// mailList.add("1000376036802");
			// mailList.add("1000504561302");
			// mailList.add("1025763879309");
			// mailList.add("1050091247909");
			// mailList.add("1085797719708");
			// mailList.add("1092362564001");
			// mailList.add("1092362564002");
			// mailList.add("1025207982301");
			// mailList.add("1025207982302");
			if (mailList.size() != 0) {
				showDialog(mailList);
			} else {
				Toast.makeText(context, "您好像没有扫描哦~", Toast.LENGTH_SHORT).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	String checked_frequence = "1";

	/**
	 * 手动添加
	 */
	private void showTianJia() {
		final Dialog dialog = new Dialog(context, R.style.dialogss);
		View layout = LayoutInflater.from(context).inflate(
				R.layout.dialog_shoudongxiaduan, null);
		Button cancel = (Button) layout.findViewById(R.id.info_cancel_shoudong);
		Button sure = (Button) layout.findViewById(R.id.info_sure_shoudong);
		final TextView textView = (TextView) layout.findViewById(R.id.geshu);
		final EditText editText = (EditText) layout.findViewById(R.id.mainmail);
		dialog.setCancelable(true);
		dialog.setContentView(layout, new LayoutParams(
				BaseActivity.width * 17 / 20, LayoutParams.WRAP_CONTENT));
		final ArrayList<String> mailList = new ArrayList<String>();
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mainmail = editText.getText().toString();
				if (mainmail != null && !mainmail.equals("")) {

					if (true) {
						if (mailList.size() > 0) {
							shoudong = 0;
							for (int i = 0; i < mailList.size(); i++) {
								if (mailList.get(i).equals(mainmail)) {
									shoudong++;
								}
							}
							if (shoudong == 0) {
								mailList.add(mainmail);
								textView.setText(mailList.size() + "");
								editText.setText("");
							} else {
								Toast.makeText(DeliverActivity.this,
										"不能输入重复的邮件号", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							mailList.add(mainmail);
							textView.setText(mailList.size() + "");
							editText.setText("");
						}
					}
//					else {
//						Toast.makeText(context, "邮件号不正确", Toast.LENGTH_SHORT)
//								.show();
//					}
				} else {
					Toast.makeText(context, "邮件号不能为空", Toast.LENGTH_SHORT)
							.show();

				}
			}
		});
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mailList.size() != 0) {
					showDialog(mailList);
				} else {
					Toast.makeText(context, "您好像没有添加哦", Toast.LENGTH_SHORT)
							.show();
				}
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 做投递
	 */
	private void showDialog(final ArrayList<String> mails) {
		View layout = LayoutInflater.from(context).inflate(R.layout.dialog_sms,
				null);
		Button cancel = (Button) layout.findViewById(R.id.info_cancel);
		Button sure = (Button) layout.findViewById(R.id.info_sure);
		final Spinner sp_frequence = (Spinner) layout
				.findViewById(R.id.sp_frequence);
		layout.findViewById(R.id.ll_frequence).setVisibility(View.VISIBLE);
		TextView custom_dia_centercall = (TextView) layout
				.findViewById(R.id.custom_dia_centercall);
		TextView tv_repeatMail = (TextView) layout
				.findViewById(R.id.custom_dia_centercall_repeat);
		final ArrayList<String> frequenceList = new ArrayList<String>();
		frequenceList.add("1");
		frequenceList.add("2");
		frequenceList.add("3");
		frequenceList.add("4");
		frequenceList.add("5");
		ArrayAdapter<String> sp_adapter = new ArrayAdapter<String>(context,
				R.layout.item_spinner, frequenceList);
		sp_frequence.setAdapter(sp_adapter);
		sp_frequence.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				checked_frequence = frequenceList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		TextView title = (TextView) layout.findViewById(R.id.txt_tit);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		title.setText("录入结果:");
		ArrayList<String> exitMails = new ArrayList<String>();// 下段邮件表中已有的邮件
		final ArrayList<String> notExitMails = new ArrayList<String>();//
		// 下段邮件表中没有的邮件
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mails.size(); i++) {
			String mail_num = mails.get(i);
			// sb.append("<" + mail_num + ">\n");
			if (mailDao.mailExits(mail_num)) {
				exitMails.add(mail_num);
			} else {
				notExitMails.add(mail_num);
			}
		}
		// StringBuffer sb_repeat = new StringBuffer();
		sb.append("您本次共录入了" + mails.size() + "个邮件\n");
		if (exitMails.size() != 0) {// 若存在重复邮件，则展示出来
			// sb.append("其中" + exitMails.size() + "个邮件重复扫描\n");
			// for (int i = 0; i < exitMails.size(); i++) {
			// sb_repeat.append("<" + exitMails.get(i) + ">\n");
			// }
			tv_repeatMail.setVisibility(View.VISIBLE);
			tv_repeatMail.setText("其中" + exitMails.size() + "个邮件重复录入\n");
		} else {
			tv_repeatMail.setVisibility(View.GONE);
		}
		custom_dia_centercall.setText("您本次共录入了" + mails.size() + "个邮件\n");
		manager.getDefaultDisplay().getMetrics(dm);
		if (dealDialog == null) {
			dealDialog = new Dialog(context, R.style.dialogss);
		}
		int width = dm.widthPixels;
		dealDialog.setContentView(layout, new LayoutParams(width * 15 / 20,
				LayoutParams.WRAP_CONTENT));
		dealDialog.setCancelable(true);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mailListBlue.clear();
				dealDialog.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mailListBlue.clear();
				int frequence = Integer.parseInt(checked_frequence);
				mailDao.inserMail(notExitMails, Utils.getDeliverTime() + ","
						+ Utils.getCurrTime(), frequence);// 将邮件号插入到下段邮件表
				DeliverDaoFactory.getInstance().getGroupDao(context)
						.updateDealMailCount(context);// 更新下段信息分组表信息
				commitAll(frequence, Constant.SUBMITTYPE_FEIQIANGZHI);
				dealDialog.dismiss();
			}
		});

		dealDialog.show();
	}

	/**
	 * 全部提交
	 */
	private void showCommitDialog() {
		View layout = LayoutInflater.from(context).inflate(R.layout.dialog_sms,
				null);
		Button cancel = (Button) layout.findViewById(R.id.info_cancel);
		Button sure = (Button) layout.findViewById(R.id.info_sure);
		final Spinner sp_frequence = (Spinner) layout
				.findViewById(R.id.sp_frequence);
		layout.findViewById(R.id.ll_frequence).setVisibility(View.VISIBLE);
		final ArrayList<String> frequenceList = new ArrayList<String>();
		frequenceList.add("1");
		frequenceList.add("2");
		frequenceList.add("3");
		frequenceList.add("4");
		ArrayAdapter<String> sp_adapter = new ArrayAdapter<String>(context,
				R.layout.item_spinner, frequenceList);
		sp_frequence.setAdapter(sp_adapter);
		sp_frequence.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				checked_frequence = frequenceList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		TextView custom_dia_centercall = (TextView) layout
				.findViewById(R.id.custom_dia_centercall);

		TextView title = (TextView) layout.findViewById(R.id.txt_tit);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		ArrayList<HashMap<String, String>> mails = mailDao.findWillCommitMail();
		title.setText("确认操作:");
		custom_dia_centercall.setText("共下段" + mails.size() + "个邮件");
		manager.getDefaultDisplay().getMetrics(dm);
		final Dialog dealDialog = new Dialog(context, R.style.dialogss);
		int width = dm.widthPixels;
		dealDialog.setContentView(layout, new LayoutParams(width * 15 / 20,
				LayoutParams.WRAP_CONTENT));
		dealDialog.setCancelable(true);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dealDialog.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int frequence = Integer.parseInt(checked_frequence);
				commitAll(frequence, Constant.SUBMITTYPE_FEIQIANGZHI);
				dealDialog.dismiss();
			}

		});
		dealDialog.show();
	}

	/**
	 * 剔除邮件dialog
	 */
	private void showDelDialog() {
		View layout = LayoutInflater.from(context).inflate(R.layout.dialog_sms,
				null);
		Button cancel = (Button) layout.findViewById(R.id.info_cancel);
		Button sure = (Button) layout.findViewById(R.id.info_sure);
		TextView custom_dia_centercall = (TextView) layout
				.findViewById(R.id.custom_dia_centercall);
		TextView title = (TextView) layout.findViewById(R.id.txt_tit);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		final ArrayList<HashMap<String, String>> mails = mailDao.findErrMail();
		title.setText("确认操作:");
		custom_dia_centercall.setText("共移除" + mails.size() + "个邮件");
		manager.getDefaultDisplay().getMetrics(dm);
		final Dialog dealDialog = new Dialog(context, R.style.dialogss);
		int width = dm.widthPixels;
		dealDialog.setContentView(layout, new LayoutParams(width * 15 / 20,
				LayoutParams.WRAP_CONTENT));
		dealDialog.setCancelable(true);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dealDialog.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dealDialog.dismiss();
				mProgerssDialog.toShow();
				ArrayList<String> errMaiList = new ArrayList<String>();
				for (int i = 0; i < mails.size(); i++) {
					errMaiList.add(mails.get(i).get("mail_num"));
				}
				mailDao.deleteErrMails(errMaiList);
				Toast.makeText(context, "操作成功!", Toast.LENGTH_SHORT).show();
				notifyAdapter();
				mProgerssDialog.toDimiss();
			}

		});
		dealDialog.show();
	}

	/**
	 * 刷新Listview
	 */
	private void notifyAdapter() {
		adapter.notifyDataSetChanged();
		if ("未上传".equals(curGroup)) {// 根据commitResult查询
			btn_group.setText(curGroup + "(" + mails.size() + ")");
			if (mails.size() == 0) {// 当列表为空时，移除footer
				footer.setVisibility(View.GONE);
			} else {
				footer.setVisibility(View.VISIBLE);
			}
		} else {
			footer.setVisibility(View.GONE);
			btn_group.setText(curGroup + "(" + mails.size() + ")");
		}
	}

	/**
	 * 弹出popWindow
	 */
	@SuppressWarnings("deprecation")
	private void showpop() {
		View v = LayoutInflater.from(context).inflate(R.layout.deliver_pop,
				null);
		popupWindow = new PopupWindow(context);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth() * 3 / 7);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(v);
		// showAsDropDown会把里面的view作为参照物，所以要那满屏幕parent
		popupWindow.showAsDropDown(ll_group, 0, 0);
		LinearLayout layout_not_commit = (LinearLayout) v
				.findViewById(R.id.layout_deliver_not_commit);
		TextView tv_not_commit = (TextView) v
				.findViewById(R.id.tv_deliver_not_commit);
		LinearLayout layout_commit = (LinearLayout) v
				.findViewById(R.id.layout_deliver_commit);
		TextView tv_commit = (TextView) v.findViewById(R.id.tv_deliver_commit);
		layout_not_commit.setOnClickListener(this);
		layout_commit.setOnClickListener(this);
		tv_not_commit.setText("(" + mailDao.findWillCommitMail().size() + ")");
		tv_commit.setText("(" + mailDao.findCommitedMail().size() + ")");
	}

	class DeliverAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public DeliverAdapter() {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mails.size();
		}

		@Override
		public Object getItem(int position) {
			return mails.get(position);
		}

		@Override
		public void notifyDataSetChanged() {
			if ("未上传".equals(curGroup)) {
				mails = mailDao.findWillCommitMail();
			} else {
				mails = mailDao.findCommitedMail();
			}
			super.notifyDataSetChanged();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.item_list_deliverfragment, null);
				holder = new ViewHolder();
				holder.tv_mail_num = (TextView) convertView
						.findViewById(R.id.item_list_deliverfragment_tv_mail_num);
				holder.tv_reason = (TextView) convertView
						.findViewById(R.id.item_list_deliverfragment_tv_err_reason);
				holder.view_mailnumber = convertView
						.findViewById(R.id.view_deliverfragment_mailnumber);
				holder.view_reason = convertView
						.findViewById(R.id.view_deliverfragment_reason);
				holder.btn_qz = (Button) convertView
						.findViewById(R.id.item_list_deliverfragment_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HashMap<String, String> map = mails.get(position);
			final String mail_num = map.get("mail_num");
			final String frequence = map.get("frequence");
			holder.tv_mail_num.setText(mail_num);
			int result = Integer.parseInt(map.get("result"));
			String msgDesc = map.get("msgDesc");
			if ("已上传".equals(curGroup)) {
				holder.tv_reason.setText("该邮件已完成下段");
				holder.tv_reason.setTextColor(Color.BLACK);
				holder.btn_qz.setVisibility(View.GONE);
			} else {
				switch (result) {
				case Constant.RESULT_NORMAL:// 默认情况，未提交
					holder.tv_reason.setText("提交未完成");
					holder.btn_qz.setVisibility(View.GONE);
					break;
				case Constant.RESULT_ERR:// 失败，必须剔除
					holder.tv_reason.setText(msgDesc);
					holder.tv_reason.setTextColor(Color.RED);
					holder.btn_qz.setVisibility(View.VISIBLE);
					holder.btn_qz.setText("移除邮件");
					holder.btn_qz.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mailDao.deleteErrMail(mail_num);
							notifyAdapter();
							Toast.makeText(context, "操作成功!", Toast.LENGTH_SHORT)
									.show();
						}
					});
					break;
				case Constant.RESULT_QIANGZHI:// 可强制上传,显示在未上传分组中
					holder.tv_reason.setText(msgDesc);
					holder.tv_reason.setTextColor(Color.BLACK);
					holder.btn_qz.setVisibility(View.VISIBLE);
					holder.btn_qz.setText("强制上传");
					holder.btn_qz.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mailDao.updateSubmitTypeToQiangZhi(mail_num);
							commitAMail(frequence, mail_num);
						}
					});
					break;
				}
			}
			if (result == Constant.RESULT_ERR) {
				convertView.setBackgroundColor(color.glay);
			} else {
				convertView.setBackgroundColor(Color.WHITE);
			}
			// // /999999999
			// holder.tv_reason.setText(msgDesc);
			// holder.tv_reason.setTextColor(Color.RED);
			// holder.btn_qz.setVisibility(View.VISIBLE);
			// holder.btn_qz.setText("移除邮件");
			// holder.btn_qz.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// mailDao.deleteErrMail(mail_num);
			// notifyAdapter();
			// Toast.makeText(context, "操作成功!", Toast.LENGTH_SHORT).show();
			// }
			// });
			return convertView;
		}

		class ViewHolder {
			TextView tv_mail_num, tv_reason;
			Button btn_qz;
			View view_mailnumber, view_reason;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		IntentFilter filter2 = new IntentFilter(Constant.ACTION_GATHER_SC);
		filter2.addAction(Constant.ACTION_BLUTTOOTH_MSG);
		DeliverActivity.this.registerReceiver(mDeliverReceiver, filter2);
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		DeliverActivity.this.unregisterReceiver(mDeliverReceiver);
		super.onPause();
	}

	private ArrayList<String> mailListBlue = new ArrayList<String>();
	private int codeState = 0;

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_BLUTTOOTH_MSG.equals(intent.getAction())) {
				String code = intent.getStringExtra("code");
				if (code != null) {
					if (true) {
						if (mailListBlue.size() > 0) {
							codeState = 0;
							for (int i = 0; i < mailListBlue.size(); i++) {
								if (mailListBlue.get(i).equals(code)) {
									codeState++;
								}
							}
							if (codeState == 0) {
								mailListBlue.add(code);
								showDialog(mailListBlue);
							} else {
								Toast.makeText(DeliverActivity.this,
										"不能输入重复的邮件号", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							mailListBlue.add(code);
							showDialog(mailListBlue);
						}

					}
//					else {
//						// Toast.makeText(NoClctActivity.this, "邮件号不正确,请重新扫入",
//						// Toast.LENGTH_SHORT).show();
//						// showdiag("邮件号不正确,请重新扫入");
//						Toast.makeText(DeliverActivity.this, "请输入正确的邮件号",
//								Toast.LENGTH_SHORT).show();
//					}
				}

			}
		}
	}
}
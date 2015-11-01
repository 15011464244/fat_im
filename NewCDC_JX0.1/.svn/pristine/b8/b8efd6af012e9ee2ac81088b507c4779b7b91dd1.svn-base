package com.newcdc.activity.clcttask;

/**
 * 无派单揽收
 */

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MyArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dtr.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.db.CustomerDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.FastLanDao;
import com.newcdc.db.MailTypeDao;
import com.newcdc.db.QsjGndmDao;
import com.newcdc.db.TransportTypeDao;
import com.newcdc.model.EmsItem;
import com.newcdc.model.FastLanBean;
import com.newcdc.service.CollectionUploadService;
import com.newcdc.tools.Constant;
import com.newcdc.tools.ExpressMailUtils;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.InfoDialog;
import com.newcdc.ui.ProgressDialog;
import com.newcdc.ui.XFAudio;

public class NoClctActivity extends BaseActivity {

	private WindowManager mWindowManager;
	private View content, menu;
	private SimpleSlide simpleslidel;
	protected Object qBbsReplyModel;
	private Button backmail, onetomore, jipao, back, saoyisao, takephoto;
	private Intent intent;
	private EditText mainmail, weight, shishoukuan;
	private AutoCompleteTextView autoTv_bigcustom, autoTv_endAddr;
	private Button btn_mailsay, btn_customersay, btn_sendsay,
			btn_tijiaolanshou, btn_gengduo;
	private FastLanBean fastLanBean = new FastLanBean();
	private Bundle bundle, bundle1, bundle2;
	private RadioButton cb_file, cb_res, cb_freight_yes, cb_freight_no;
	private String orgCode, username, deviceId;
	private String nullcllection = "";
	private String danhao, yesCollection, noCollection;
	private String customerkacode = "";
	private String jddkacode = "";
	private String et_itemNum = "";
	private String jdddm = "";
	private ArrayList<String> autoData, autojidadi;
	private MyArrayAdapter<String> autoAdapter;
	private TextView tv_sourthcode, tv_gekoucode, tv_gekouname, dan_num,
			tx_no_title;
	private ProgressDialog mdialog, daofuDialog;
	private ArrayList<Map<String, String>> list1;
	private LinearLayout linearLayout, linearLayout1, linearLayout2,
			linearLayout3;
	private boolean booleangengduo = true;
	private SharePreferenceUtilDaoFactory shareDao;
	private String Clc;
	private String results = "";
	private String clientname, phone, address;
	private String gekoucode, gekouname;
	private UserInfoUtils user = null;
	Message msg;
	private MyReceiver noClctReceiver;
	Handler h = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String wei = (String) msg.obj;
			weight.setText(wei);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.no_clct);
		mWindowManager = (WindowManager) NoClctActivity.this
				.getSystemService(Context.WINDOW_SERVICE);
		content = (View) findViewById(R.id.content);
		// menu = (View) findViewById(R.id.menu);

		// simpleslidel = new SimpleSlide(NoClctActivity.this, content, menu);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(clicklistener);
		cb_file = (RadioButton) findViewById(R.id.cb_file);
		cb_res = (RadioButton) findViewById(R.id.cb_res);
		cb_freight_yes = (RadioButton) findViewById(R.id.cb_freight_yes);
		cb_freight_no = (RadioButton) findViewById(R.id.cb_freight_no);
		backmail = (Button) findViewById(R.id.backmail);
		backmail.setOnClickListener(clicklistener);
		onetomore = (Button) findViewById(R.id.onetomore);
		onetomore.setOnClickListener(clicklistener);
		jipao = (Button) findViewById(R.id.jipao);
		jipao.setOnClickListener(clicklistener);
		weight = (EditText) findViewById(R.id.weight);
		saoyisao = (Button) findViewById(R.id.saoyisao);
		saoyisao.setOnClickListener(clicklistener);
		mainmail = (EditText) findViewById(R.id.mainmail);
		mainmail.setOnClickListener(clicklistener);
		takephoto = (Button) findViewById(R.id.takephoto);
		takephoto.setOnClickListener(clicklistener);
		autoTv_bigcustom = (AutoCompleteTextView) findViewById(R.id.et_bigcustomer);
		autoTv_endAddr = (AutoCompleteTextView) findViewById(R.id.et_sendAddr);
		shishoukuan = (EditText) findViewById(R.id.shishoukuan);
		dan_num = (TextView) findViewById(R.id.dan_num);
		btn_tijiaolanshou = (Button) findViewById(R.id.btn_tijiaolanshou);
		btn_tijiaolanshou.setOnClickListener(clicklistener);
		btn_mailsay = (Button) findViewById(R.id.btn_mailsay);
		btn_customersay = (Button) findViewById(R.id.btn_customersay);
		btn_sendsay = (Button) findViewById(R.id.btn_sendsay);
		sp_mailtype = (Spinner) findViewById(R.id.sp_mailtype);
		sp_transport = (Spinner) findViewById(R.id.sp_transport);
		btn_gengduo = (Button) findViewById(R.id.btn_gengduo);
		btn_gengduo.setOnClickListener(clicklistener);
		if (user == null) {
			user = new UserInfoUtils(NoClctActivity.this);
		}
		noClctReceiver = new MyReceiver();
		deviceId = Utils.getDeviceId(this);
		orgCode = user.getUserDelvorgCode();
		username = user.getUserName();

		autoData = new ArrayList<String>();
		autoAdapter = new MyArrayAdapter(this, R.layout.my_autocompleteview,
				autoData);
		autojidadi = new ArrayList<String>();
		autoTv_bigcustom.setAdapter(autoAdapter);
		autoTv_endAddr.setAdapter(autoAdapter);

		tv_sourthcode = (TextView) findViewById(R.id.tv_sourthcode);
		tv_gekoucode = (TextView) findViewById(R.id.tv_gekoucode);
		tv_gekouname = (TextView) findViewById(R.id.tv_gekouname);
		linearLayout = (LinearLayout) findViewById(R.id.gone);
		linearLayout1 = (LinearLayout) findViewById(R.id.linearlayout_type);
		linearLayout2 = (LinearLayout) findViewById(R.id.linearlayout_daofu);
		linearLayout3 = (LinearLayout) findViewById(R.id.lin_btn);
		tx_no_title = (TextView) findViewById(R.id.tx_no_title);

		// bundle = NoClctActivity.this.getIntent().getExtras();
		bundle = new Bundle();
		bundle1 = new Bundle();
		bundle2 = new Bundle();

		Global.jipaoWeight = "";
		addSound();
		// CustomerkaCode(); // 大客户的自动填充
		// addressCode();
		initData();
		addListener(); // 给邮件号，寄达地，邮件分类，运输方式添加监听，如果都不为空，则请求格口信息
		initSpinner();
		isDaoFu();
		initnullCollection();

		if (nullcllection != null && "danhao".equals(nullcllection)) {
			linearLayout.setVisibility(View.GONE);
			linearLayout1.setVisibility(View.GONE);
			linearLayout2.setVisibility(View.GONE);
		} else {
			linearLayout.setVisibility(View.VISIBLE);
			linearLayout1.setVisibility(View.VISIBLE);
			linearLayout2.setVisibility(View.VISIBLE);
		}

		
		autoTv_endAddr.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				autoTv_endAddr.setText("");
				autoData.clear();
				autojidadi.clear();
				jdddm = "";
				postcode = "";
				return false;
			}
		});

		autoTv_bigcustom.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView textview = (TextView) arg1;
				String[] split = textview.getText().toString().split("-");
				customerkacode = split[1];
			}
		});
		autoTv_bigcustom.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				autoTv_bigcustom.setText("");
				customerkacode = "";
				return false;
			}
		});

		autoTv_bigcustom.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				String input = arg0.toString();
				if (!Utils.stringEmpty(input)) {
					setAutoData();
				}
				customerkacode = "";
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.indexOf("\n") != -1) {
					str = str.replace("\n", "");
					autoTv_bigcustom.setText(str);
				}
				// if (temp.length() > 2) {
				// List<Map<String, String>> rList = null;
				// String str = autoTv_bigcustom.getText().toString().trim();
				// CustomerDao customerDao = DeliverDaoFactory.getInstance()
				// .getCustomerDao(NoClctActivity.this);
				// rList = customerDao.FindData(str);
				// int tempSize = rList.size();
				// if (tempSize > 1) {
				// if (pop.isShowing()) {
				// // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
				// pop.dismiss();
				// } else {
				// // 显示窗口 DMMC DSMC
				// arrayCode.clear();
				//
				// for (int j = 0; j < tempSize; j++) {
				// String strrList1 = rList.get(j).get("KHMC")
				// .toString()
				// + "-"
				// + rList.get(j).get("KHDM").toString();
				// HashMap<String, String> tempHashMap = new HashMap<String,
				// String>();
				//
				// tempHashMap.put("data", strrList1);
				// tempHashMap.put("KHMC", rList.get(j)
				// .get("KHMC").toString());
				// tempHashMap.put("KHDM", rList.get(j)
				// .get("KHDM").toString());
				// arrayCode.add(tempHashMap);
				// }
				// popadapter.notifyDataSetChanged();
				// }
				// } else if (tempSize == 1) {
				// arrayCode.clear();
				//
				// String strrList1 = // rList.get(0).get("KHDM").toString()+
				// // "-" +
				// rList.get(0).get("KHMC").toString() + "-"
				// + rList.get(0).get("KHDM").toString();
				// et_bigcustomer.setText(strrList1);
				// customerkacode = rList.get(0).get("KHDM").toString();
				//
				// }
				//
				// }
			}
		});

		autoTv_endAddr.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (autojidadi != null && autojidadi.get(arg2) != null) {
					String[] split = autojidadi.get(arg2).split("-");
					if (split.length > 0) {
						jdddm = split[0];
					}
					if (split.length > 1) {
						postcode = split[1];
					}
				}

			}
		});
		autoTv_endAddr.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String input = s.toString();
				if (!Utils.stringEmpty(input)) {
					setEndAddrAutoData();
				}
				jdddm = "";
				postcode = "";
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
//				if (str.indexOf("\n") != -1) {
//					str = str.replace("\n", "");
//					autoTv_bigcustom.setText(str);
//				}
			}
		});
	}
	private void initData() {
		shareDao = new SharePreferenceUtilDaoFactory();
				if (shareDao != null) {
					Clc = shareDao.getClc();
				}
				if (!"".equals(Clc)) {
					String[] clc = Clc.split(",");
					autoTv_bigcustom.setText(clc[0]);
					autoTv_endAddr.setText(clc[1]);
					weight.setText(clc[2]);
					shishoukuan.setText(clc[3]);
					try {
						if (clc[4]!=null) {
							jdddm=clc[4];
						}
						if (clc[5]!=null) {
							customerkacode=clc[5];
						}
					} catch (Exception e) {
					}
					
				}
	}


	/*
	 * 派单信息的接收
	 */
	private void initnullCollection() {
		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			nullcllection = bundle.getString("nullCollection");
			danhao = bundle.getString("danhao1");
			clientname = bundle.getString("clientname");
			phone = bundle.getString("phone");
			address = bundle.getString("address");

			yesCollection = bundle.getString("yesCollection");
			noCollection = bundle.getString("noCollection");
			if (danhao != null) {
				dan_num.setText(danhao);
//				autoTv_endAddr.setText(address);
			}
			if (noCollection != null) {
				tx_no_title.setText(noCollection);
			} else {
				tx_no_title.setText(yesCollection);
			}
		}
	}

	@Override
	protected void onResume() {
		Utils.startIntentService(NoClctActivity.this);// 启动投递、揽收异步上传服务
		IntentFilter filter = new IntentFilter(Constant.ACTION_BLUTTOOTH_MSG);
		registerReceiver(noClctReceiver, filter);
		super.onResume();
		if ("".equals(weight.getText().toString().trim())) {
			weight.setText(Global.jipaoWeight);
		} else {
			String temp = weight.getText().toString().trim();
			if (!"".equals(Global.jipaoWeight)) {
				if (new BigDecimal(temp).compareTo(new BigDecimal(Global.jipaoWeight)) < 0) {
					weight.setText(Global.jipaoWeight);
				}
			}
		}
	}

	@Override
	protected void onPause() {
		unregisterReceiver(noClctReceiver);
		super.onPause();
	}

	/*
	 * 处理回传结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			if (data != null) {
				bundle1 = data.getExtras();
				if (bundle1 != null && bundle1.getSerializable("list1") != null) {
					list1 = (ArrayList<Map<String, String>>) bundle1
							.getSerializable("list1");
				}
			}
		}
		if (resultCode == 1) {
			if (data != null) {
				bundle2 = data.getExtras();
			}
		}

		if (resultCode == 111) {
			results = data.getStringExtra("txtResult");
			if (!"".equals(results)) {
				if (true) {
					if (mailExits(results)) {
						// Toast.makeText(NoClctActivity.this, "该邮件号已使用",
						// Toast.LENGTH_SHORT).show();
						showdiag("该邮件号已使用");

					} else {
						mainmail.setText(results);
					}

				}
//				else {
//					 Toast.makeText(NoClctActivity.this, "邮件号不正确,请重新扫入",
//					 Toast.LENGTH_SHORT).show();
//					showdiag("邮件号不正确,请重新扫入");
//				}
			}

		}

	}

	/**
	 * 添加语音识别
	 */
	public void addSound() {
		XFAudio mailAudio = new XFAudio(NoClctActivity.this, btn_mailsay,
				mainmail);
		mailAudio.toSay();
		XFAudio customerAudio = new XFAudio(NoClctActivity.this,
				btn_customersay, autoTv_bigcustom);
		customerAudio.toSay();
		XFAudio sendAudio = new XFAudio(NoClctActivity.this, btn_sendsay,
				autoTv_endAddr);
		sendAudio.toSay();
	}

	/*
	 * 点击事件
	 */
	private OnClickListener clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.takephoto:
				Global.jipaoWeight = weight.getText().toString();
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 0);
				break;
			case R.id.backmail:
				if (checkedbackmail()) {
					Global.jipaoWeight = weight.getText().toString();
					intent = new Intent(NoClctActivity.this,
							BackMailActivity.class);
					Bundle bundlefan = new Bundle();
					bundlefan.putString("mainmail", mainmail.getText()
							.toString().trim());
					bundlefan.putString("adrr", autoTv_endAddr.getText()
							.toString().trim());
					bundlefan.putString("customer", autoTv_bigcustom.getText()
							.toString().trim());
					intent.putExtras(bundlefan);
					startActivityForResult(intent, 1);
				}
				break;
			case R.id.onetomore:
				if (checkedonetomore()) {
					Global.jipaoWeight = weight.getText().toString();
					intent = new Intent(NoClctActivity.this,
							OneToMoreActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("mainmail", mainmail.getText().toString()
							.trim());
					bundle.putString("customer", autoTv_bigcustom.getText()
							.toString().trim());
					bundle.putString("weight", Global.jipaoWeight);
					if (list1 != null && list1.size() > 0) {
						bundle.putSerializable("list1", list1);
					}
					intent.putExtras(bundle);
					startActivityForResult(intent, 0);
				}
				break;
			case R.id.jipao:
				Global.jipaoWeight = weight.getText().toString();
				intent = new Intent(NoClctActivity.this, JiPaoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("flag", "0");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.saoyisao:
				Global.jipaoWeight = weight.getText().toString();
				startActivityForResult(new Intent(NoClctActivity.this,
						CaptureActivity.class), 1);
				break;
			case R.id.btn_tijiaolanshou:
				QsjGndmDao qsjGndmDao = DeliverDaoFactory.getInstance().getQsjGndmDao(
						getApplicationContext());
				Editable editable = mainmail.getText();
				if (editable != null) {
					String input = editable.toString();
					if (Utils.stringEmpty(input)) {
						// Toast.makeText(NoClctActivity.this, "请输入正确的邮件号",
						// Toast.LENGTH_SHORT).show();
						showdiag("请输入正确的邮件号");
						 } else if (!Utils.stringEmpty(autoTv_bigcustom.getText().toString())&&customerkacode=="") {
								 showdiag("请输入正确大客户");
					} else if (Utils.stringEmpty(autoTv_endAddr.getText().toString())||jdddm=="") {
						
						showdiag("请输入正确的寄达地");
					} else if (weight.getText().toString() == null
							|| weight.getText().toString().trim().length() <= 0) {
						// Toast.makeText(NoClctActivity.this, "请输入正确的重量",
						// Toast.LENGTH_SHORT).show();
//						||Double.parseDouble(weight.getText().toString().trim())==0
						
						showdiag("请输入正确的重量");
					} else if (shishoukuan.getText().toString() == null
							|| shishoukuan.getText().toString().trim().length() <= 0) {
//						||Double.parseDouble(shishoukuan.getText().toString().trim())==0
						showdiag("请输入正确的实收款");
					} else {
						if (cb_file.isChecked()) {
							fastLanBean.setFoodType("1");
						} else {
							fastLanBean.setFoodType("3");
						}
						if (cb_freight_yes.isChecked()) {
							fastLanBean.setDaoFu("0");
						} else {
							fastLanBean.setDaoFu("1");
						}
						if (bundle2 != null
								&& bundle2.getString("et_itemNum") != null) {
							et_itemNum = bundle2.getString("et_itemNum");
						}
						// else {
						// et_itemNum = "";
						// }
						// uploadData();
						StringBuffer sb = new StringBuffer();
						sb.append(autoTv_bigcustom.getText()+",");
						sb.append(autoTv_endAddr.getText()+",");
						sb.append(weight.getText()+",");
						sb.append(shishoukuan.getText()+",");
						sb.append(jdddm+",");
						sb.append(customerkacode+",");
						shareDao.setClc(sb.toString());
						saveData();

					}
				}
				break;
			case R.id.btn_gengduo:
				if (booleangengduo) {
					linearLayout3.setVisibility(View.VISIBLE);
				} else {
					linearLayout3.setVisibility(View.GONE);
				}
				booleangengduo = !booleangengduo;
				break;
			}
		}

	};

	// 获取控件上的值然后将它添加到实体类中
	public void getData(String mail, String size, String flag) {
		fastLanBean.setDan_num(dan_num.getText().toString());
		fastLanBean.setMailNum(mainmail.getText().toString());
		fastLanBean.setCustomer(customerkacode);
		fastLanBean.setRcvArea(jdddm);// et_sendAddr.getText().toStrin g());
		fastLanBean.setActualWeight(weight.getText().toString());
		fastLanBean.setActualTotalFee(shishoukuan.getText().toString());
		if (sp_mailtype.getSelectedItem().toString().equals("请选择")) {
			fastLanBean.setFenlei("");
		}else {
			fastLanBean.setFenlei(sp_mailtype.getSelectedItem().toString());
			}
		String trans = "";
		if (sp_transport.getSelectedItem().toString().equals("请选择")) {
			trans="";
		} else if (sp_transport.getSelectedItem().toString()
				.equals("全程陆运")) {
			trans ="0"; // 运输方式
		} else if (sp_transport.getSelectedItem().toString()
				.equals("特殊物品")) {
			trans = "4"; // 运输方式
		}else if (sp_transport.getSelectedItem().toString()
				.equals("无")) {
			trans="A"; // 运输方式
		}
		fastLanBean.setYunshu(trans);
		fastLanBean.setReturnLiuShui(tv_gekouname.getText().toString());
		fastLanBean.setShangChuan("0");
		fastLanBean.setSourthcode(tv_sourthcode.getText().toString());
		fastLanBean.setGekoucode(gekoucode);
		fastLanBean.setGekouname(gekouname);
		fastLanBean.setReturnMailNum(et_itemNum);
		fastLanBean.setMainmail(mail);
		fastLanBean.setYpdj_flag(flag);
		fastLanBean.setYpdj_quan(size);
		fastLanBean.setPostcode(postcode);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		fastLanBean.setClct_date(time.substring(0, 8));
		fastLanBean.setClct_time(time.substring(8, 14));
		fastLanBean.setOpreate("I");
		fastLanBean.setDelvorgcode(user.getUserDelvorgCode());
		fastLanBean.setUsername(user.getUserName());
		if (clientname != null && !"".equals(clientname))
			fastLanBean.setSender_name(clientname);
		else
			fastLanBean.setSender_name("");
		if (address != null && !"".equals(address))
			fastLanBean.setSender_name(address);
		else
			fastLanBean.setSender_name("");
		if (phone != null && !"".equals(phone))
			fastLanBean.setSender_name(phone);
		else
			fastLanBean.setSender_name("");
	}

	// 获取控件上的值然后将它添加到实体类中
	public void getData1(String childmail, String weight, String size) {
		fastLanBean.setDan_num(dan_num.getText().toString());
		fastLanBean.setMailNum(childmail);
		fastLanBean.setCustomer(customerkacode);
		fastLanBean.setRcvArea(jdddm);// et_sendAddr.getText().toStrin g());
		fastLanBean.setActualWeight("");
		fastLanBean.setActualTotalFee("");
		String trans ="";
		if (sp_transport.getSelectedItem().toString().equals("请选择")) {
			trans="";
		} else if (sp_transport.getSelectedItem().toString()
				.equals("全程陆运")) {
			trans ="0"; // 运输方式
		} else if (sp_transport.getSelectedItem().toString()
				.equals("特殊物品")) {
			trans = "4"; // 运输方式
		}else if (sp_transport.getSelectedItem().toString()
				.equals("无")) {
			trans="A"; // 运输方式
		}
		fastLanBean.setYunshu(trans);
		if (sp_mailtype.getSelectedItem().toString().equals("请选择")) {
			fastLanBean.setFenlei("");
		}else {
			fastLanBean.setFenlei(sp_mailtype.getSelectedItem().toString());
			}
//		168格口名字
		fastLanBean.setReturnLiuShui(tv_gekouname.getText().toString());
		fastLanBean.setShangChuan("0");
		fastLanBean.setSourthcode(tv_sourthcode.getText().toString());
		fastLanBean.setGekoucode(gekoucode);
		fastLanBean.setGekouname(gekouname);
		fastLanBean.setReturnMailNum("");
		fastLanBean.setMainmail(mainmail.getText().toString());
		fastLanBean.setYpdj_flag("1");
		fastLanBean.setYpdj_quan(size);
		fastLanBean.setPostcode(postcode);
		String time = DateFormat.format("yyyyMMddHHmmss", new Date())
				.toString();
		fastLanBean.setClct_date(time.substring(0, 8));
		fastLanBean.setClct_time(time.substring(8));
		fastLanBean.setOpreate("I");
		fastLanBean.setDelvorgcode(user.getUserDelvorgCode());
		fastLanBean.setUsername(user.getUserName());
		if (clientname != null && !"".equals(clientname))
			fastLanBean.setSender_name(clientname);
		else
			fastLanBean.setSender_name("");
		if (address != null && !"".equals(address))
			fastLanBean.setSender_name(address);
		else
			fastLanBean.setSender_name("");
		if (phone != null && !"".equals(phone))
			fastLanBean.setSender_name(phone);
		else
			fastLanBean.setSender_name("");
	}

	/*
	 * 一票多件的条件
	 */
	private boolean checkedonetomore() {
		if (mainmail.getText().toString().trim().equals("")) {
			// Toast.makeText(NoClctActivity.this, "请填写邮件号",
			// Toast.LENGTH_LONG).show();
			showdiag("请填写邮件号");
			return false;
		} else if (weight.getText().toString().trim().equals("")) {
			// Toast.makeText(NoClctActivity.this, "请填写重量",
			// Toast.LENGTH_LONG).show();
			showdiag("请填写重量");
			return false;
		}

		return true;
	}

	/*
	 * 反单的条件
	 */
	private boolean checkedbackmail() {
		if (mainmail.getText().toString().trim().equals("")) {
			// Toast.makeText(NoClctActivity.this, "请填写邮件号",
			// Toast.LENGTH_LONG).show();
			showdiag("请填写邮件号");
			return false;
		} else if (Utils.stringEmpty(autoTv_bigcustom.getText().toString()
				.trim())) {
			// Toast.makeText(NoClctActivity.this, "请填写大客户",
			// Toast.LENGTH_LONG).show();
			showdiag("请填写大客户");
			return false;
		} else if (autoTv_endAddr.getText().toString().trim().equals("")) {
			// Toast.makeText(NoClctActivity.this, "请填写寄达地",
			// Toast.LENGTH_LONG).show();
			showdiag("请填写寄达地");
			return false;
		}
		return true;
	}

	/**
	 * 计算产品代码
	 * 
	 * @param mailNum
	 * @return
	 */
	public String jisuanCPDM(String mailNum) {
		// 如果邮件号是10,11开头，产品代码是9
		// 如果是50,51开头，产品代码是6
		if (mailNum.startsWith("11") || mailNum.startsWith("10")) {
			return "9";
		}
		if (mailNum.startsWith("50") || mailNum.startsWith("51")) {
			return "6";
		}
		return "";
	}

	/**
	 * 计算邮件种类
	 * 
	 * @param mailNum
	 * @return
	 */
	public String jisuanYJZL(String mailNum) {
		// 如果邮件号是10,11开头，邮件种类是10101
		// 如果是50,51开头，邮件种类是10201
		if (mailNum.startsWith("11") || mailNum.startsWith("10")
				|| mailNum.startsWith("EY")) {
			return "10101";
		}
		if (mailNum.startsWith("50") || mailNum.startsWith("51")) {
			return "10201";
		}
		return "";
	}

	/**
	 * 获取格口数据
	 */
	private void getGeKou() {
		//初始化格口信息
		tv_gekouname.setText("");
		tv_gekoucode.setText("");
		tv_gekoucode.setText("");
		gekoucode="";
		gekouname="";
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				// Log.e("jdddm,postcode", jdddm + " " + postcode);
				GekouBean gb = new GekouBean();
				List<EmsItem> lists = new ArrayList<EmsItem>();
				EmsItem ems = new EmsItem();
				ems.setMail_num(mainmail.getText().toString().trim()); // 邮件号
				ems.setReceiveAddressDaiMa(jdddm); // 寄达地代码
				ems.setCpDaima(jisuanCPDM(mainmail.getText().toString().trim())); // 产品代码
				ems.setReceivPost(postcode); // 收件人邮编
				if (sp_mailtype.getSelectedItem().toString().equals("请选择")) {
					ems.setFenleiDaima("");
				} else {
					ems.setFenleiDaima(getTypeCode(sp_mailtype
							.getSelectedItem().toString())); // 分类代码
				}
				ems.setEmsZhonglei(jisuanYJZL(mainmail.getText().toString()
						.trim())); // 邮件种类
				if (sp_transport.getSelectedItem().toString().equals("请选择")) {
					ems.setTransport("");
				} else if (sp_transport.getSelectedItem().toString()
						.equals("全程陆运")) {
					ems.setTransport("0"); // 运输方式
				} else if (sp_transport.getSelectedItem().toString()
						.equals("特殊物品")) {
					ems.setTransport("4"); // 运输方式
				}else if (sp_transport.getSelectedItem().toString()
						.equals("无")) {
					ems.setTransport("A"); // 运输方式
				}

				lists.add(ems);

				gb.setEms(lists);
				gb.setEmployeeNo(username);// SharePreferenceUtilDaoFactory.getInstance(NoClctActivity.this).getUSERNAME());
											// // 员工号
				gb.setDeviceId(Utils.getDeviceId(NoClctActivity.this)); // 设备号
				gb.setSjvorgcode(orgCode); // 收寄机构号
				Gson gson = new Gson();
				String str = gson.toJson(gb);
				// Log.e("Doraemon", str);
				JSONObject jsonObject = null;
				try {
					String str1 = NetHelper.doPostJson(Global.GEKOU, str,
							"json");
					if (!"请求服务器失败".equals(str1))
						jsonObject = new JSONObject(str1);
				} catch (JSONException e) {
					showdiag("你输入的信息不正确，无法请求格口信息，请从新输入");
				}
				// Log.e("Doraemon", jsonObject.toString());
				return jsonObject;
			}

			protected void onPostExecute(JSONObject result) {
				if (mdialog != null && mdialog.isShowing())
					mdialog.toDimiss();
				if (result != null) {
					try {
						if ("1".equals(result.get("result"))) {
							if ("0".equals(result.getJSONObject("body")
									.getJSONArray("success").getJSONObject(0)
									.getString("flag"))) {
								if (!"null".equals(result.getJSONObject("body")
										.getJSONArray("success")
										.getJSONObject(0)
										.getString("gekoucode"))) {
									gekoucode = result.getJSONObject("body")
											.getJSONArray("success")
											.getJSONObject(0)
											.getString("gekoucode");
									if (result
											.getJSONObject("body")
											.getJSONArray("success")
											.getJSONObject(0)
											.getString("gekouname").equals(null)) {
										tv_gekoucode.setText("");
									}else {
										tv_gekoucode.setText(result
												.getJSONObject("body")
												.getJSONArray("success")
												.getJSONObject(0)
												.getString("gekouname"));
									}
								}
								// handlecenter
								if (!"null".equals(result.getJSONObject("body")
										.getJSONArray("success")
										.getJSONObject(0)
										.getString("gekouname"))) {
									gekouname = result.getJSONObject("body")
											.getJSONArray("success")
											.getJSONObject(0)
											.getString("gekouname");
									if ("null".equals(result.getJSONObject("body")
											.getJSONArray("success")
											.getJSONObject(0)
											.getString("handlecenter"))||result
											.getJSONObject("body")
											.getJSONArray("success")
											.getJSONObject(0)
											.getString("handlecenter")==null) {
										tv_gekouname.setText("");
									}else {
										tv_gekouname.setText(result
												.getJSONObject("body")
												.getJSONArray("success")
												.getJSONObject(0)
												.getString("handlecenter"));
									}
									
								}
								if ("1".equals(result.getJSONObject("body")
										.getJSONArray("success")
										.getJSONObject(0)
										.getString("sourthcode"))) {
									tv_sourthcode.setText(" 南集");
								} else {
									tv_sourthcode.setText(" 非南集");
								}
							} else {
								// Toast.makeText(NoClctActivity.this,
								// "此邮件为直封邮件，需要单独处理", Toast.LENGTH_LONG).show();
								showdiag("此邮件为直封邮件，需要单独处理");
							}
						} else {
							showdiag("你输入的信息不正确，无法请求格口信息，请重新输入");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				// Log.e("resultco", result + "");
			}

			protected void onPreExecute() {
				mdialog = new ProgressDialog(NoClctActivity.this, "正在请求格口信息");
				mdialog.toShow();
				super.onPreExecute();
			}

		}.execute();
	}

	/*
	 * 页面dialog展示
	 */
	private void showdiag(String info) {
		// InfoDialog infoDialog = new InfoDialog(this, info);
		InfoDialog infoDialog = new InfoDialog(NoClctActivity.this, info);
		infoDialog.Dismiss();
		infoDialog.Show();
	}

	/*
	 * 判断邮件是否存在
	 */
	public boolean mailExits(String mail_num) {
		FastLanDao fastLanDao = DeliverDaoFactory.getInstance().getFastLanDao(
				getApplicationContext());
		List<FastLanBean> list = fastLanDao.queryFastLanMessageI();
		for (int i = 0; i < list.size(); i++) {
			String mailNum = list.get(i).getMailNum();
			if (mail_num.equals(mailNum)) {
				return true;
			}
		}
		return false;
	}

	boolean isReturn = false;

	// private String initUploadData(final String mainMail, final String child)
	// {
	// String reObjc = "";
	// try {
	// LanShouBean1 bean1 = new LanShouBean1();
	// LanShouBean2 bean2 = new LanShouBean2();
	// List<LanShouBean2> lists = new ArrayList<LanShouBean2>();
	// bean1.setDeviceId(deviceId);// 设备号
	// bean1.setClct_bureau_org_code(orgCode);// 机构号
	// bean1.setClct_staff_code(username);// 工号
	// bean1.setClct_staff_name(username);// 工号
	// bean2.setActual_receipt_fees(null);// 实收资费
	// bean2.setActual_total_fee(shishoukuan.getText().toString());// 实收款
	// bean2.setActual_weight(weight.getText().toString());// 重量
	// bean2.setActualconcessional_rate(null);// 实收优惠率
	// bean2.setAddi_service_code("");
	// bean2.setAgency_code("");
	// bean2.setAirport_expense(null);// 机场费
	// bean2.setApply_chk_flag("");
	// bean2.setAttach_list_quan("0");// 随附单据数
	// bean2.setBack_mail_num(et_itemNum);// 返单号
	// bean2.setBasic_fee(null);// 基本资费
	// bean2.setBilling_weight(null);// 计费重量
	// bean2.setBulgarian_price_amount(null);// 报价金额
	// bean2.setBulgarian_price_expense(null);// 保价费
	// bean2.setBulgarian_price_handling(null);// 保价手续费
	// bean2.setBusi_kind_code("");
	// bean2.setCant_dlv_deal_mode_code("");
	// bean2.setClct_bureau_post_code("1");
	// bean2.setClct_staff_code("");
	// bean2.setClct_staff_name("");
	// bean2.setData_clct_mode_code("");
	// bean2.setData_src_sys("");
	// bean2.setData_upload_timestamp("");
	// bean2.setDeclaration_expense(null);// 报关验关费
	// bean2.setDlv_area_code("");
	// bean2.setDuty_code("");
	// bean2.setDzz_num("");// 单证照件数
	// bean2.setEachfeeflag("");
	// bean2.setElectronfavorable_num("");
	// bean2.setEmbraces_charge(null);// 揽收费
	// bean2.setExtra_charge(null);// 附加费
	// bean2.setFeedback_mode_code("");
	// bean2.setFeedback_para("");
	// bean2.setFrequency("");// 频次
	// bean2.setHeight("");// 高
	// bean2.setInformation_fee(null);// 信息费
	// bean2.setInitiative_feedback_flag("");
	// bean2.setInsurance_amount(null);// 保险金额
	// bean2.setInsurance_expenses(null);// 保险费
	// bean2.setInter_flag("");
	// bean2.setIs_distributed("");
	// bean2.setLength("");// 长
	// bean2.setLoans_amount(null);// 货款金额
	// bean2.setMail_kind_code("");
	// bean2.setMail_num(child);// 邮件号
	// bean2.setMail_other_remark("");
	// bean2.setMail_prpty_code("");
	// bean2.setMail_remark_code("");
	// bean2.setMail_sort_code(getTypeCode(sp_mailtype.getSelectedItem()
	// .toString()));// 邮件分类代码
	// bean2.setMain_mailno(mainMail);// 一票多件主单号
	// bean2.setOperator_code("");
	// bean2.setOperator_name("");
	// bean2.setOperatortype("I");
	// bean2.setOrder_check_flag("");
	// bean2.setOrderno("");
	// bean2.setOther_expenses(null);// 其他费
	// bean2.setPacking_charges(null);// 包装费
	// bean2.setPart_kind_quan("0");// 内件总类数
	// bean2.setPay_side_code("");
	// bean2.setPlandlvdate("");
	// bean2.setPlandlvtime("");
	// bean2.setPreview_info_type("");
	// bean2.setProd_code("");// 产品代码
	// bean2.setRcpt_invoice_num("");
	// bean2.setRcv_area(jdddm);// et_sendAddr.getText().toStr
	// // ing());//寄达地代码
	// bean2.setRcver_addr("");
	// bean2.setRcver_contract_phone("");
	// bean2.setRcver_dept_name("");
	// bean2.setRcver_home_area_name("");
	// bean2.setRcver_name("");
	// bean2.setRcver_post_code("");
	// bean2.setReceipt_expense(null);// 回执费
	// bean2.setResv_col10("");
	// bean2.setSender_addr("");
	// bean2.setSender_contact_phone("");
	// bean2.setSender_cust_code(customerkacode);// 大客户代码
	// bean2.setSender_dept_name("");
	// bean2.setSender_home_area_name("");
	// bean2.setSender_name("");
	// bean2.setSender_post_code("");
	// bean2.setSetlmt_mode_code("");
	// bean2.setShould_pay_total_fee(null);// 应收总资费
	// bean2.setTime_limit_code("");
	// bean2.setTrans_mode_code(getTransportCode(sp_transport
	// .getSelectedItem().toString()));// 运输方式
	// bean2.setTrial_no("");
	// bean2.setUnitary_expense(null);// 单式费
	// bean2.setVip_card("");
	// bean2.setVol_weight(null);// 体积重量
	// bean2.setWay_code(tv_gekoucode.getText().toString());// 格口代码
	// bean2.setWidth("");// 宽
	// bean2.setWl_term_num("");
	// bean2.setYpdj_flag("4");
	// bean2.setYpdj_quan(list1 == null ? null : list1.size() + "");// 一票多件
	// lists.add(bean2);
	// bean1.setData(lists);
	// // 揽收数据上传接口
	// String urlString = Global.LanShouShuJu_URL;
	// // 交班接口
	// String jsonString = JSON.toJSONString(bean1);
	// // Log.e("jsonString",jsonString);
	// String result = NetHelper.doPostJson(urlString, jsonString, "json");
	//
	// JSONObject obj = new JSONObject(result);
	// reObjc = obj.get("result").toString();
	// } catch (Exception e) {
	// }
	// return reObjc;
	// }
	/*
	 * 对揽收的数据进行保存
	 */
	private void saveData() {
		if (list1 != null) {
			int tempSize = list1.size();
			if (tempSize > 0) {
				for (int i = 0; i < list1.size(); i++) {
					Map<String, String> map = list1.get(i);
					String childmail = map.get("childmail");
					String weight = map.get("weight");

					getData1(childmail, weight, (tempSize + 1) + "");
					DeliverDaoFactory.getInstance()
							.getFastLanDao(getApplicationContext())
							.insertFastLan(fastLanBean);
//					DeliverDaoFactory.getInstance()
//					.getMoneyDao(getApplicationContext()).insertGatherMoney(fastLanBean);
					DeliverDaoFactory.getInstance().getGather_msgdao(getApplicationContext()).updateStauts(fastLanBean.getDan_num(), "1");
				
				}

				getData(mainmail.getText().toString(), (tempSize + 1) + "", "1");
				DeliverDaoFactory.getInstance()
						.getFastLanDao(getApplicationContext())
						.insertFastLan(fastLanBean);
				DeliverDaoFactory.getInstance()
				.getMoneyDao(getApplicationContext()).insertGatherMoney(fastLanBean);
				DeliverDaoFactory.getInstance().getGather_msgdao(getApplicationContext()).updateStauts(fastLanBean.getDan_num(), "1");
				
			}
		} else {
			getData("", null, "");
			DeliverDaoFactory.getInstance()
					.getFastLanDao(getApplicationContext())
					.insertFastLan(fastLanBean);
			DeliverDaoFactory.getInstance()
			.getMoneyDao(getApplicationContext()).insertGatherMoney(fastLanBean);
			DeliverDaoFactory.getInstance().getGather_msgdao(getApplicationContext()).updateStauts(fastLanBean.getDan_num(), "1");
		}

		Intent intent = new Intent(NoClctActivity.this,
				CollectionUploadService.class);
		startService(intent);
		showcomplelteDiloage1();
//		Toast.makeText(NoClctActivity.this, "揽收成功，请继续揽收",
//				Toast.LENGTH_LONG).show();
	}
	/**
	 *揽收确定
	 */
		
	private void showcomplelteDiloage1() {
		final Dialog dialog = new Dialog(NoClctActivity.this,
				R.style.dialogss);
		View v = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_lantoudelete, null);
		dialog.setCancelable(false);
		Button info_cancel_lantou_delete = (Button) v
				.findViewById(R.id.info_cancel_lantou_delete);
		Button info_sure_lantou_delete = (Button) v
				.findViewById(R.id.info_sure_lantou_delete);
		dialog.setContentView(v, new LayoutParams(BaseActivity.width * 17 / 20,
				LayoutParams.WRAP_CONTENT));
		info_cancel_lantou_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				dialog.dismiss();
				
			}
		});
		info_sure_lantou_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				mainmail.setText("");
				dialog.dismiss();
			}
		});
		dialog.show();
	}


	/**
	 * 到付接口
	 */
	public void getDaofu(String code) {
		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObject = null;
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				try {
					param.add(new BasicNameValuePair("addressCode",
							(String) params[0]));
					jsonObject = new JSONObject(NetHelper.doPost(
							Global.DAOFU_URL, param));
				} catch (Exception e) {
				}
				// Log.e("Doraemon", jsonObject.toString());
				return jsonObject;

			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (daofuDialog != null) {
					daofuDialog.toDimiss();
				}
				if (result != null) {
					try {
						if ("1".equals(result.get("result"))) {
							Toast.makeText(NoClctActivity.this, "支持到付",
									Toast.LENGTH_LONG).show();
						}
						if ("0".equals(result.get("result"))) {
							Toast.makeText(NoClctActivity.this, "不支持到付",
									Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(NoClctActivity.this, "请求到付网络失败",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected void onPreExecute() {
				daofuDialog = new ProgressDialog(NoClctActivity.this,
						"查询是否支持到付");
				daofuDialog.setCanCalcelable(false);
				daofuDialog.toShow();
			}

		}.execute(code);

	}

	private String postcode; // 收件人邮编
	private Spinner sp_mailtype,sp_transport;
	private List<Map<String, String>> typeList;
	protected Context context;

	/**
	 * 初始化spinner
	 */
	public void initSpinner() {
		MailTypeDao mailtypedao = DeliverDaoFactory.getInstance()
				.getMailTypeDao(NoClctActivity.this);
		TransportTypeDao transportTypeDao = DeliverDaoFactory.getInstance()
				.getTransportTypeDao(NoClctActivity.this);
		typeList = new ArrayList<Map<String, String>>();
//		transportList = new ArrayList<Map<String, String>>();

		List<String> plist = new ArrayList<String>();
		List<String> tlist = new ArrayList<String>();
		plist.add("请选择");
		tlist.add("请选择");
		tlist.add("全程陆运");
		tlist.add("特殊物品");
		tlist.add("无");

		typeList = mailtypedao.FindMailTypeInfo(SharePreferenceUtilDaoFactory
				.getInstance(NoClctActivity.this).getDELVORGCODE()); // 分类集合
//		transportList = transportTypeDao
//				.FindTransportTypeInfo(SharePreferenceUtilDaoFactory
//						.getInstance(NoClctActivity.this).getDELVORGCODE()); // 运输集合

		for (int m = 0; m < typeList.size(); m++) {
			plist.add(typeList.get(m).get("ClassName"));
		}

		// for (int i = 0; i < transportList.size(); i++) {
		// tlist.add(transportList.get(i).get("TransferName"));
		// }
		MyArrayAdapter<String> transportAdapter = new MyArrayAdapter<String>(
				NoClctActivity.this, R.layout.my_spinner, tlist);
		transportAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_transport.setAdapter(transportAdapter);

		MyArrayAdapter<String> typeAdapter = new MyArrayAdapter<String>(
				NoClctActivity.this, R.layout.my_spinner, plist);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_mailtype.setAdapter(typeAdapter);
	}

	/**
	 * 根据运输方式名来获得运输方式代码
	 * 
	 * @param transportName
	 * @return 运输方式代码
	 */
	// public String getTransportCode(String transportName) {
	// for (int i = 0; i < transportList.size(); i++) {
	// if (transportName.equals(transportList.get(i).get("TransferName"))) {
	// return transportList.get(i).get("TransferCode");
	// }
	// }
	// return "";
	// }

	/**
	 * 根据分类名来获取分类方法
	 * 
	 * @param typename
	 * @return 邮件分类代码
	 */
	public String getTypeCode(String typename) {
		for (int i = 0; i < typeList.size(); i++) {
			if (typename.equals(typeList.get(i).get("ClassName"))) {
				return typeList.get(i).get("ClassCode");
			}
		}
		return "";
	}

	// 输入寄达地都提示是否支持到付
	public void isDaoFu() {
		autoTv_endAddr.addTextChangedListener(new TextWatcher() {
			CharSequence tempstr;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tempstr = s;
				jdddm = "";
				postcode = "";
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			/**
			 * 寄达地输入为不为空，如果是否到付点击了，则提示是否到付 是否到付点击了，如果寄达地输入不为空，则提示是否到付
			 */
			@Override
			public void afterTextChanged(Editable s) {
				if (tempstr.length() > 4) {
					if (!"".equals(autoTv_endAddr.getText().toString())) {
						if (cb_freight_yes.isChecked()) {
							getDaofu(jdddm);
						}
					}
				}
			}
		});

		cb_freight_yes
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if (!"".equals(autoTv_endAddr.getText().toString())) {
								getDaofu(jdddm);
							}
						}
					}
				});
	}

	/**
	 * 设置寄达地提示文本
	 */
	public void setEndAddrAutoData() {
		// 清空寄达地弹出的提示框
		autoData.clear();
		// 获取输入内容
		String str = autoTv_endAddr.getText().toString().trim();
		// 获取QsjGndmDao数据操作对象
		QsjGndmDao qsjGndmDao = DeliverDaoFactory.getInstance().getQsjGndmDao(
				getApplicationContext());
		// 根据输入内容获取查询结果
		List<Map<String, String>> rList = qsjGndmDao.FindData(str);
		// Log.e("工", rList.toString() + "");
		if (rList != null && rList.size() > 0) {
			autojidadi.clear();
			for (int i = 0; i < rList.size(); i++) {
				String DMMC = rList.get(i).get("DMMC").toString();
				String DSMC = "";
				String YZBM = "";
				if (rList.get(i).get("DSMC") != null
						&& rList.get(i).get("DSMC").toString().trim().length() != 0
						&& !"null".equals(rList.get(i).get("DSMC").toString()
								.trim().toLowerCase())) {
					DSMC = rList.get(i).get("DSMC").toString();
				}
				String itemString = DMMC + "(" + DSMC + ")";
				String DMDM = rList.get(i).get("DMDM").toString();
				if (rList.get(i).get("YZBM") != null
						&& rList.get(i).get("YZBM").toString().trim().length() != 0
						&& !"null".equals(rList.get(i).get("YZBM").toString()
								.trim().toLowerCase())) {
					YZBM = rList.get(i).get("YZBM").toString();
				}

				autoData.add(itemString);
				autojidadi.add(DMDM + "-" + YZBM + "-");
				// Log.e("sfldsfkdsjflds", autoData.toString() + "");
			}
		}
		// --------以下为刷新--------------
		// Log.e("工", autojidadi.toString() + "");
		autoAdapter = new MyArrayAdapter(this, R.layout.my_autocompleteview,
				autoData);
		autoTv_endAddr.setAdapter(autoAdapter);
	}

	/**
	 * 给自动提示文本赋值
	 */
	public void setAutoData() {
		autoData.clear();
		String str = autoTv_bigcustom.getText().toString().trim();
		CustomerDao customerDao = DeliverDaoFactory.getInstance()
				.getCustomerDao(NoClctActivity.this);
		List<Map<String, String>> data = customerDao.FindData(str);
		for (int i = 0; i < data.size(); i++) {
			String KHMC = data.get(i).get("KHMC").toString();
			String KHDM = data.get(i).get("KHDM").toString();
			String itemString = KHMC + "-" + KHDM;
			autoData.add(itemString);
		}
		// Log.e("autoData", autoData.toString() + "");
		autoAdapter = new MyArrayAdapter(this, R.layout.my_autocompleteview,
				autoData);
		autoTv_bigcustom.setAdapter(autoAdapter);
	}

	// // 寄达地的填充模块
	// private void addressCode() {
	//
	// LayoutInflater lay = (LayoutInflater)
	// getSystemService(this.LAYOUT_INFLATER_SERVICE);
	// View v = lay.inflate(R.layout.popupwindow, null);
	// // 创建PopupWindow对象
	// final PopupWindow pop = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT, false);
	// // 需要设置一下此参数，点击外边可消失
	// pop.setBackgroundDrawable(new BitmapDrawable());
	// // 设置点击窗口外边窗口消失
	// pop.setOutsideTouchable(true);
	// // 设置此参数获得焦点，否则无法点击
	// pop.setFocusable(true);
	//
	// ListView windowlistView = (ListView) v.findViewById(R.id.list);
	//
	// final List<HashMap<String, String>> arraylist = new
	// ArrayList<HashMap<String, String>>();
	// final SimpleAdapter popadapter = new SimpleAdapter(NoClctActivity.this,
	// arraylist, R.layout.popupwindow_item, new String[] { "data" },
	// new int[] { R.id.data });
	// windowlistView.setAdapter(popadapter);
	// windowlistView.setOnItemClickListener(new OnItemClickListener() {
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// //
	// et_sendAddr.setText(arraylist.get(arg2).get("data").toString());
	// jdddm = arraylist.get(arg2).get("DMDM").toString();
	// postcode = arraylist.get(arg2).get("XZQH").toString();
	// if (pop.isShowing()) {
	// // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
	// pop.dismiss();
	// }
	// // editor = preferences.edit();
	// // if(!jdddm.equals("")){
	// // editor.putString("jdddm",jdddm);
	// // editor.commit();
	// // }
	// }
	// });
	//
	// autoTv_endAddr.addTextChangedListener(new TextWatcher() {
	// CharSequence temp;
	//
	// @Override
	// public void onTextChanged(CharSequence arg0, int arg1, int arg2,
	// int arg3) {
	// temp = arg0;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence arg0, int arg1,
	// int arg2, int arg3) {
	//
	// }
	//
	// @Override
	// public void afterTextChanged(Editable arg0) {
	// if (temp.length() > 0) {
	// List<Map<String, String>> rList = null;
	// String str = autoTv_endAddr.getText().toString().trim();
	// QsjGndmDao qsjGndmDao = DeliverDaoFactory.getInstance()
	// .getQsjGndmDao(getApplicationContext());
	// rList = qsjGndmDao.FindData(str);
	// int tempSize = rList.size();
	// if (tempSize > 1) {
	// if (pop.isShowing()) {
	// // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
	// pop.dismiss();
	// } else {
	// // 显示窗口 DMMC DSMC
	// arraylist.clear();
	//
	// String dmbm = "";
	// String DSMC = "";
	// for (int j = 0; j < tempSize; j++) {
	// dmbm = rList.get(j).get("DMBM") == null ? ""
	// : rList.get(j).get("DMBM").toString();
	// DSMC = rList.get(j).get("DSMC") == null ? ""
	// : rList.get(j).get("DSMC").toString();
	// String strrList1 = rList.get(j).get("DMMC") == null ? ""
	// : rList.get(j).get("DMMC").toString()
	// + "(" + DSMC + ")-" + dmbm;
	// HashMap<String, String> tempHashMap = new HashMap<String, String>();
	// tempHashMap.put("data", strrList1);
	// tempHashMap.put("DMDM", rList.get(j)
	// .get("DMDM") == null ? "" : rList
	// .get(j).get("DMDM").toString());
	// tempHashMap.put("XZQH", rList.get(j)
	// .get("XZQH") == null ? "" : rList
	// .get(j).get("XZQH").toString());
	// arraylist.add(tempHashMap);
	// }
	//
	// popadapter.notifyDataSetChanged();
	// pop.showAsDropDown(et_sendAddr);
	// }
	// } else if (tempSize == 1) {
	// arraylist.clear();
	// String dmbm = rList.get(0).get("DMBM") == null ? ""
	// : rList.get(0).get("DMBM").toString();
	// String DSMC = rList.get(0).get("DSMC") == null ? ""
	// : rList.get(0).get("DSMC").toString();
	// String strrList1 = rList.get(0).get("DMMC") == null ? ""
	// : rList.get(0).get("DMMC").toString() + "("
	// + DSMC + ")-" + dmbm;
	// et_sendAddr.setText(strrList1);
	// jdddm = rList.get(0).get("DMDM") == null ? "" : rList
	// .get(0).get("DMDM").toString();
	// postcode = rList.get(0).get("XZQH") == null ? ""
	// : rList.get(0).get("XZQH").toString();
	// }
	// }
	// }
	// });
	// }

	public void addListener() {
		/*
		 * 给mainmail设置焦点的监听事件，当该空间失去焦点的时候 检查寄达地，邮件分类和运输方式是否为空，如果全部都不为空，则请求格口信息
		 */
		mainmail.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) { // 失去焦点的时候调用
					if (!"".equals(mainmail.getText().toString().trim())
							&& !"".equals(autoTv_endAddr.getText().toString()
									.trim())) {

						getGeKou();
					}
				}
			}
		});

		/**
		 * 给寄达地设置监听 如果寄达地失去焦点 检查邮件号，邮件分类和运输方式是否为空，如果不为空，则请求格口信息
		 */
		autoTv_endAddr.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (!"".equals(mainmail.getText().toString().trim())
							&& !"".equals(autoTv_endAddr.getText().toString()
									.trim())) {

						getGeKou();
					}
				}
			}
		});

		/**
		 * 如果sp有选择，则判断其余各项是否有值，如果都有则请求格口信息
		 */
		sp_mailtype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (!"".equals(mainmail.getText().toString().trim())
						&& !"".equals(autoTv_endAddr.getText().toString()
								.trim())
						&& !("请选择".equals(sp_mailtype.getSelectedItem()
								.toString()) && "请选择".equals(sp_transport
								.getSelectedItem().toString()))) {

					getGeKou();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		sp_transport.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (!"".equals(mainmail.getText().toString().trim())
						&& !"".equals(autoTv_endAddr.getText().toString()
								.trim())
						&& !("请选择".equals(sp_mailtype.getSelectedItem()
								.toString()) && "请选择".equals(sp_transport
								.getSelectedItem().toString()))) {

					getGeKou();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		/**
		 * 重量输入限制
		 */
		weight.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				try {
					if (s != null && !"".equals(s.toString())) {
						double userInput = Double.parseDouble(s.toString());
						if (userInput > 100.000) {
							// Toast.makeText(NoClctActivity.this, "不可大于100",
							// Toast.LENGTH_SHORT).show();
							showdiag("不可大于100");
							weight.setText("100");
							Editable editable = weight.getText();
							Selection.setSelection(editable, editable.length());
						} else if ((userInput + "").length()
								- (userInput + "").indexOf(".") - 1 > 3) {
							// Toast.makeText(NoClctActivity.this, "最大保留三位小数",
							// Toast.LENGTH_SHORT).show();
							showdiag("最大保留三位小数");
							NumberFormat nf = NumberFormat.getNumberInstance();
							nf.setMaximumFractionDigits(3);
							System.out.println(nf.format(userInput));
							weight.setText(nf.format(userInput));
							Editable editable = weight.getText();
							Selection.setSelection(editable, editable.length());
						}
					}
				} catch (Exception e) {
				}
			}
		});
		/**
		 * 实收款输入限制
		 */
		shishoukuan.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				try {
					if (s != null && !"".equals(s.toString())) {
						double userInput = Double.parseDouble(s.toString());
						if (userInput > 100000.000) {
							showdiag("实收款最多为100000");
							// Toast.makeText(NoClctActivity.this,
							// "实收款最多为100000",
							// Toast.LENGTH_SHORT).show();
							shishoukuan.setText("100000");
							Editable editable = shishoukuan.getText();
							Selection.setSelection(editable, editable.length());
						} else if ((userInput + "").length()
								- (userInput + "").indexOf(".") - 1 > 3) {
							showdiag("最大精确3位小数");
							// Toast.makeText(NoClctActivity.this, "最大精确3位小数",
							// Toast.LENGTH_SHORT).show();
							NumberFormat nf = NumberFormat.getNumberInstance();
							nf.setMaximumFractionDigits(3);
							shishoukuan.setText(nf.format(userInput));
							Editable editable = shishoukuan.getText();
							Selection.setSelection(editable, editable.length());
						}
					}
				} catch (Exception e) {
				}
			}
		});

	}
	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			 if (Constant.ACTION_BLUTTOOTH_MSG.equals(intent.getAction())){
				 String code = intent.getStringExtra("code");
					if (code != null) {
						if (true) {
							if (mailExits(code)) {
								// Toast.makeText(NoClctActivity.this, "该邮件号已使用",
								// Toast.LENGTH_SHORT).show();
								showdiag("该邮件号已使用");
							} else {
								mainmail.setText(code);
							}

						} 
//						else {
//							// Toast.makeText(NoClctActivity.this, "邮件号不正确,请重新扫入",
//							// Toast.LENGTH_SHORT).show();
//							showdiag("邮件号不正确,请重新扫入");
//						}

					}
			}
		}

	}

}

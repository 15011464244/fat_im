package com.newcdc.activity.payment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newcdc.R;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.ExtraCastDao;
import com.newcdc.db.MoneyDao;
//import com.newcdc.db.UserDao;
import com.newcdc.model.ExtraCast;
import com.newcdc.model.GatherInfoBean;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.model.MoneyBean;
import com.newcdc.model.PaymentMoneyBean;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;

/**
 * @author hanrong
 * @version 创建时间：2014-12-22 上午10:27:01 类说明 ： 客户管理 缴款页
 */
public class GiveMoneyActivity extends Activity implements OnClickListener {
	private List<GatherInfoBean> deliverBeans;
	private List<GatherInfoBean> gatherBeans;
	private double deliverMoney = 0.0;
	private double gatherMoney = 0.0;
	private TextView mdeliver_moynum;
	private TextView mgatherdetail_moynum;
	private TextView mall_moynum;
	private Button mbtn_money, give_btn_money_back;
	private Button mbtn_deliverdetail_moy;
	private Button mbtn_gatherdetail_moy;
	private List<MoneyBean> deliverMoneyBeans;
	private List<MoneyBean> gatherMoneyBeans;
	private MoneyBean moneyBean;
	private MoneyBean moneyBean1;
	private String mResult;// 请求返回值
	private static final int MSG_PAS = 1;
	private ProgressDialog myDialog;
	private TextView mgive_edt_other_moynum;
	private TextView mgive_edt_yawn_moynum;
	private View layout;
	private LayoutInflater inflater;
	private int width;
	private int height;
//	private UserDao mUserDao;
	private UserInfoUtils userInfo;
	private Button give_btn_otherdetail;
	private Dialog longdia;
	private Button give_btn_yawn_moynum;
	private DeliverDaoFactory daoFactory;
	private ExtraCastDao mExtraCastDao;
	private double otherMoney = 0.0;
	List<ExtraCast> otherList;
	private MoneyDao moneyDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_give_money);
		DisplayMetrics metric = new DisplayMetrics();
		GiveMoneyActivity.this.getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		width = metric.widthPixels;
		height = metric.heightPixels;
		userInfo = new UserInfoUtils(getApplicationContext());
		daoFactory = DeliverDaoFactory.getInstance();
//		mUserDao = daoFactory.getUserDao(GiveMoneyActivity.this);
		moneyDao = daoFactory.getMoneyDao(GiveMoneyActivity.this);
		mExtraCastDao = daoFactory.getExtraCastDao(GiveMoneyActivity.this);
		DeliverDao	deliverDao = daoFactory.getDeliverDao(GiveMoneyActivity.this);
		
		ArrayList<MessageInfoBean> mys = deliverDao.queryAllMails();
		for (int i = 0; i < mys.size(); i++) {
			Log.e("tag","getFrequence"+mys.get(i).getFrequence());
		}
		initView();
		addListener();

	}

	private void initView() {
		mdeliver_moynum = (TextView) findViewById(R.id.give_deliver_moynum);
		give_btn_money_back = (Button) findViewById(R.id.give_btn_money_back);
		mgatherdetail_moynum = (TextView) findViewById(R.id.give_gatherdetail_moynum);
		mall_moynum = (TextView) findViewById(R.id.give_all_moynum);
		mbtn_deliverdetail_moy = (Button) findViewById(R.id.give_btn_deliverdetail_moy);
		mbtn_gatherdetail_moy = (Button) findViewById(R.id.give_btn_gatherdetail_moy);
		mbtn_money = (Button) findViewById(R.id.give_btn_money);
		give_btn_money_back = (Button) findViewById(R.id.give_btn_money_back);
		mgive_edt_other_moynum = (TextView) findViewById(R.id.give_edt_other_moynum);
		mgive_edt_yawn_moynum = (TextView) findViewById(R.id.give_edt_yawn_moynum);
		give_btn_otherdetail = (Button) findViewById(R.id.give_btn_otherdetail);
		give_btn_yawn_moynum = (Button) findViewById(R.id.give_btn_yawn_moynum);
		otherList = mExtraCastDao.queryExtraCastall_username(
				userInfo.getUserDelvorgCode(), userInfo.getUserName());
		int othersize = otherList.size();
		if (othersize > 0) {
			otherMoney = 0.0;
			for (int i = 0; i < othersize; i++) {
				otherMoney += Double.parseDouble(otherList.get(i).getMoney());
			}
		}

		myDialog = new ProgressDialog(GiveMoneyActivity.this, "正在处理");
		deliverMoneyBeans = new ArrayList<MoneyBean>();
		gatherMoneyBeans = new ArrayList<MoneyBean>();

		deliverBeans = moneyDao.queryByMailNumber("0", "0");
		// ArrayList<DeliverQueueBean> lists =
		// mQueueDao.queryByCommitResult_money();
		// for (int i = 0; i < lists.size(); i++) {
		// double mon = Double.parseDouble(lists.get(i).getActual_goods_fee());
		// if (mon > 0.0) {
		// List<MessageInfoBean> beans =
		// mDeliverDaoDataHelper.queryByMailNumber(lists.get(i).getMail_num());
		// beans.get(0).setCharge(mon + "");
		// // Log.e("tag", mon+"");
		// // Log.e("tag", beans.get(0).getFlag());
		// // Log.e("tag", beans.get(0).getActualGoodsFee());
		// deliverBeans.add(beans.get(0));
		// }
		// }

		// gatherBeans = fastLanDao.queryFastLanPatment();
		gatherBeans = moneyDao.queryByMailNumber("1", "0");
		// for (int i = 0; i < gatherBeans.size(); i++) {
		// Log.e("tag", "gatherBeans=" + gatherBeans.size() + "==" +
		// gatherBeans.get(i).getAmount());
		// }
		// deliverBeans = new ArrayList<MessageInfoBean>();
		// gatherBeans = new ArrayList<GatherInfoBean>();
		// MessageInfoBean dbean = new MessageInfoBean();
		// dbean.setMail_num("12312312321");
		// dbean.setCharge("12");
		// dbean.setRcver_name("as");
		// dbean.setActualGoodsFee("1");
		// dbean.setPay_type("1");
		// dbean.setWeight("21");
		// dbean.setAddress("2");
		// MessageInfoBean dbean1 = new MessageInfoBean();
		// dbean1.setMail_num("12312312321");
		// dbean1.setCharge("12");
		// dbean1.setActualGoodsFee("1");
		// dbean1.setPay_type("1");
		// dbean1.setRcver_name("nihao");
		// dbean1.setWeight("21");
		// dbean1.setAddress("2");
		// deliverBeans.add(dbean);
		// deliverBeans.add(dbean1);
		//
		// GatherInfoBean gbean = new GatherInfoBean();
		// gbean.setMail_num("12312312321");
		// gbean.setCharge("12");
		// gbean.setActualGoodsFee("1");
		// gbean.setPay_type("1");
		// gbean.setWeight("21");
		// gbean.setAddress("2");
		// gbean.setUsername("asdad");
		// gatherBeans.add(gbean);

		if (deliverBeans.size() > 0) {
			for (int i = 0; i < deliverBeans.size(); i++) {
				moneyBean = new MoneyBean();
				deliverMoney += Double.parseDouble(deliverBeans.get(i)
						.getAmount());
				// Log.e("tag", "deliverBeans" +
				// deliverBeans.get(i).getMail_num());
				moneyBean.setMailId(deliverBeans.get(i).getMail_num());
				moneyBean.setMoneyNum(deliverBeans.get(i).getAmount());
				moneyBean.setUsername(deliverBeans.get(i).getUsername());
				moneyBean.setPay_type(deliverBeans.get(i).getPayment_mode());
				deliverMoneyBeans.add(moneyBean);
			}
		}
		if (Global.isLan) {
			if (gatherBeans.size() > 0) {
				for (int i = 0; i < gatherBeans.size(); i++) {
					moneyBean1 = new MoneyBean();
					if (!"".equals(gatherBeans.get(i).getAmount().trim())) {
						gatherMoney += Double.parseDouble(gatherBeans.get(i)
								.getAmount());
					}
					moneyBean1.setMailId(gatherBeans.get(i).getMail_num());
					moneyBean1.setMoneyNum(gatherBeans.get(i).getAmount());
					moneyBean1.setUsername(gatherBeans.get(i).getUsername());
					moneyBean1
							.setPay_type(gatherBeans.get(i).getPayment_mode());
					gatherMoneyBeans.add(moneyBean1);
				}
			}
		}

		mdeliver_moynum.setText(deliverMoney + "元");
		mgatherdetail_moynum.setText(gatherMoney + "元");
		mgive_edt_other_moynum.setText(otherMoney + "元");
		mall_moynum.setText((String.valueOf(deliverMoney + gatherMoney
				+ otherMoney))
				+ "元");

	}

	private void addListener() {
		give_btn_yawn_moynum.setOnClickListener(this);
		give_btn_otherdetail.setOnClickListener(this);
		mbtn_deliverdetail_moy.setOnClickListener(this);
		mbtn_gatherdetail_moy.setOnClickListener(this);
		mbtn_money.setOnClickListener(this);
		give_btn_money_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.give_btn_deliverdetail_moy:
			Intent intent = new Intent();
			intent.setClass(GiveMoneyActivity.this, MoneyActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("moneytype", "tou");
			intent.putExtras(bundle);
			startActivity(intent);

			break;
		case R.id.give_btn_gatherdetail_moy:
			Intent intent1 = new Intent();
			intent1.setClass(GiveMoneyActivity.this, MoneyActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putString("moneytype", "lan");
			intent1.putExtras(bundle1);
			startActivity(intent1);
			break;
		case R.id.give_btn_money:
			if (deliverMoney + gatherMoney + otherMoney == 0.0) {
				Toast.makeText(GiveMoneyActivity.this, "无缴费任务",
						Toast.LENGTH_SHORT).show();
			} else {
				if (longdia == null) {
					longdia = new Dialog(GiveMoneyActivity.this,
							R.style.dialogss);
					giveMoneyDialog(longdia);
				} else {
					giveMoneyDialog(longdia);
				}

			}
			break;
		case R.id.give_btn_money_back:
			GiveMoneyActivity.this.finish();
			break;
		case R.id.give_btn_otherdetail:
			if (otherList.size() > 0) {
				startActivity(new Intent(GiveMoneyActivity.this,
						OtherMoneyDetailActivity.class));
			} else {
				Toast.makeText(GiveMoneyActivity.this, "请先下载其他金额基础数据", 0)
						.show();
			}

			Toast.makeText(GiveMoneyActivity.this, "无其他金额缴费明细", 0).show();
			break;
		case R.id.give_btn_yawn_moynum:
			// startActivity(new
			// Intent(GiveMoneyActivity.this,BigCustomMoneyActivity.class));
			Toast.makeText(GiveMoneyActivity.this, "无记欠金额缴费明细", 0).show();
			break;
		default:
			break;
		}
	}

	// private String nowTime() {
	// SimpleDateFormat formatter = new
	// SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
	// Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
	// String str = formatter.format(curDate);
	// return str;
	// }

	/**
	 * 封装缴款json数据
	 * 
	 * @param touBeans
	 * @param lanBeans
	 * @return
	 */
	private String mailPutMoneyData(List<GatherInfoBean> touBeans,
			List<GatherInfoBean> lanBeans, List<ExtraCast> other) {
		userInfo = new UserInfoUtils(GiveMoneyActivity.this);
		String resultURL;
		PaymentMoneyBean bean = new PaymentMoneyBean();
		if (!"".equals(userInfo.getUserName())) {
			bean.setEmployeeNo(userInfo.getUserName());
			bean.setUser_code(userInfo.getUserName());
		}
		bean.setDeviceId(deviceId());
		bean.setDevice_num(deviceId());
		if (!"".equals(userInfo.getUserDelvorgCode())) {
			bean.setSjvorgcode(userInfo.getUserDelvorgCode());
			bean.setDlvorgcode(userInfo.getUserDelvorgCode());
			bean.setOrg_code(userInfo.getUserDelvorgCode());
		}
		for (int i = 0; i < touBeans.size(); i++) {
			touBeans.get(i).setOperation_time(nowDate(new Date()));
			touBeans.get(i).setOperatortype("I");
		}
		for (int i = 0; i < lanBeans.size(); i++) {
			lanBeans.get(i).setOperation_time(nowDate(new Date()));
			lanBeans.get(i).setOperatortype("I");
		}
		bean.setDlvorgcode(userInfo.getUserDelvorgCode());
		bean.setLan(lanBeans);
		bean.setTou(touBeans);
		resultURL = JSON.toJSONString(bean);
		return resultURL;

	}

	private String nowDate(Date mdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 手机设备号
	 */
	private String deviceId() {
		TelephonyManager telephonemanage = (TelephonyManager) GiveMoneyActivity.this
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonemanage.getDeviceId();

	}

	long startTimeMillis;// 请求开始时间
	String operate_time;// 操作时间
	long batch_no;

	/**
	 * 缴款确认框
	 * **/
	private void giveMoneyDialog(final Dialog longdia) {
		inflater = GiveMoneyActivity.this.getLayoutInflater();
		layout = inflater.inflate(R.layout.dialog_money_layout, null);
		Button cancel = (Button) layout.findViewById(R.id.info_money_cancel);
		Button sure = (Button) layout.findViewById(R.id.info_money_sure);
		longdia.setContentView(layout, new LayoutParams(width * 15 / 20,
				LayoutParams.WRAP_CONTENT));
		longdia.setCancelable(true);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				longdia.dismiss();
			}
		});

		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myDialog == null) {
					myDialog = new ProgressDialog(GiveMoneyActivity.this,
							"处理中...");
					myDialog.setCanCalcelable(false);
					myDialog.toShow();

				} else {
					myDialog.setCanCalcelable(false);
					myDialog.toShow();
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						startTimeMillis = System.currentTimeMillis();// 请求开始时间
						operate_time = Utils.getCurrTime();// 操作时间
						batch_no = new Date().getTime();
						String result = "";
						result = NetHelper.doPostJson(
								Global.PUTMONEY,
								mailPutMoneyData(deliverBeans, gatherBeans,
										otherList), "json");
						Message message = handler.obtainMessage();
						message.what = MSG_PAS;
						message.obj = result;
						handler.sendMessageDelayed(message, 0);
					}
				}).start();
				longdia.dismiss();
			}
		});
		longdia.show();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_PAS:
				if (myDialog != null) {
					myDialog.toDimiss();
				}
				mResult = (String) msg.obj;
				if (mResult.equals("请求服务器失败")) {
					Toast.makeText(GiveMoneyActivity.this, "请求服务器失败",
							Toast.LENGTH_SHORT).show();
				} else if (resState(mResult).equals("0")) {
					long endTime = System.currentTimeMillis();// 请求结束时间
					int count = deliverBeans.size() + gatherBeans.size();
					// 提交请求日志
					NetHelper.saveTransferLog(
							operate_time,
							endTime - startTimeMillis + "",
							GiveMoneyActivity.this,
							getResources().getString(
									R.string.operate_action_payment_failed),
							count + "", batch_no + "");
					Toast.makeText(GiveMoneyActivity.this, "提交失败",
							Toast.LENGTH_SHORT).show();
				} else {
					long endTime = System.currentTimeMillis();// 请求结束时间
					int count = deliverBeans.size() + gatherBeans.size();
					// 提交请求日志
					NetHelper.saveTransferLog(
							operate_time,
							endTime - startTimeMillis + "",
							GiveMoneyActivity.this,
							getResources().getString(
									R.string.operate_action_payment_success),
							count + "", batch_no + "");
					Toast.makeText(GiveMoneyActivity.this, "提交成功",
							Toast.LENGTH_SHORT).show();
					for (int i = 0; i < gatherBeans.size(); i++) {
						moneyDao.updateIsMoney(gatherBeans, "1", gatherBeans
								.get(i).getOperation_time());
					}
					for (int i = 0; i < deliverBeans.size(); i++) {
						moneyDao.updateIsMoney(deliverBeans, "1", deliverBeans
								.get(i).getOperation_time());
					}

					deliverMoney = 0.0;
					gatherMoney = 0.0;
					mExtraCastDao.updateOther_Money();
					initView();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onRestart() {
		// if (otherList.size() > 0) {
		// otherMoney = 0.0;
		// for (int i = 0; i < otherList.size(); i++) {
		// otherMoney += Double.parseDouble(otherList.get(i).getMoney());
		// }
		// }
		// mdeliver_moynum.setText(deliverMoney + "元");
		// mgatherdetail_moynum.setText(gatherMoney + "元");
		// mgive_edt_other_moynum.setText(otherMoney + "元");
		// mall_moynum.setText((Double.toString(deliverMoney + gatherMoney +
		// otherMoney)) + "元");
		// Log.e("tag", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		daoFactory = DeliverDaoFactory.getInstance();
//		mUserDao = daoFactory.getUserDao(GiveMoneyActivity.this);
		moneyDao = daoFactory.getMoneyDao(GiveMoneyActivity.this);
		mExtraCastDao = daoFactory.getExtraCastDao(GiveMoneyActivity.this);
		otherList = mExtraCastDao.queryExtraCastall_username(
				userInfo.getUserDelvorgCode(), userInfo.getUserName());
		if (otherList.size() > 0) {
			otherMoney = 0.0;
			for (int i = 0; i < otherList.size(); i++) {
				otherMoney += Double.parseDouble(otherList.get(i).getMoney());
			}
		}
		mdeliver_moynum.setText(deliverMoney + "元");
		mgatherdetail_moynum.setText(gatherMoney + "元");
		mgive_edt_other_moynum.setText(otherMoney + "元");
		mall_moynum.setText((Double.toString(deliverMoney + gatherMoney
				+ otherMoney))
				+ "元");
		Utils.startIntentService(GiveMoneyActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private String resState(String s) {
		try {
			JSONObject json = new JSONObject(s);
			String str = json.get("result").toString();
			// System.out.println("json-------" + str);
			return str;
		} catch (JSONException e) {
			e.printStackTrace();
			return "0";
		}
	}

}

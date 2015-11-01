package com.newcdc.activity.clcttask;

import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.JxClctDao;
import com.newcdc.model.JxClctBean;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

public class CollectionDetailActivityJX extends BaseActivity {

	private TextView mailNo = null;
	private TextView weight = null;
	private TextView receiverName = null;
	private TextView receiverMobile = null;
	private TextView receiverAddress = null;
	private TextView senderName = null;
	private TextView senderMobile = null;
	private TextView senderAddress = null;
	private TextView distance = null;
	private TextView tv_money = null;
	private TextView tv_actmoney = null;
	private TextView tv_Integral = null;
	private TextView tv_startTime = null;
	private TextView tv_payment = null;
	private TextView tv_serviceType = null;
	private LinearLayout ll_startTime;
	private String orgCode, username, deviceId;
	private int _id = 0;
	private JxClctDao jxClctDao;
	private JxClctBean bean;
	private List<JxClctBean> listBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_detail_jx);
		inits();
		initData();
		// initData1("1");
		// initData1("2");
	}

	private void inits() {
		jxClctDao = DeliverDaoFactory.getInstance().getJxClctDao(
				getApplicationContext());
		mailNo = (TextView) findViewById(R.id.tv_mailNo);
		weight = (TextView) findViewById(R.id.tv_weight);
		receiverName = (TextView) findViewById(R.id.tv_receiverName);
		receiverMobile = (TextView) findViewById(R.id.tv_receiverMobile);
		receiverAddress = (TextView) findViewById(R.id.tv_receiverAddress);
		tv_serviceType = (TextView) findViewById(R.id.tv_serviceType);
		tv_startTime = (TextView) findViewById(R.id.tv_startTime);
		senderName = (TextView) findViewById(R.id.tv_senderName);
		ll_startTime = (LinearLayout) findViewById(R.id.ll_startTime);
		senderMobile = (TextView) findViewById(R.id.tv_senderMobile);
		senderAddress = (TextView) findViewById(R.id.tv_senderAddress);
		distance = (TextView) findViewById(R.id.tv_distance);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_actmoney = (TextView) findViewById(R.id.tv_actmoney);
		tv_Integral = (TextView) findViewById(R.id.tv_Integral);
		tv_payment = (TextView) findViewById(R.id.tv_payment);
		tv_serviceType = (TextView) findViewById(R.id.tv_serviceType);
		UserInfoUtils user = new UserInfoUtils(CollectionDetailActivityJX.this);
		deviceId = Utils.getDeviceId(this);
		orgCode = user.getUserDelvorgCode();
		username = user.getUserName();
	}

	private void initData() {
		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			_id = bundle.getInt("detail_id");
			queryData();
		}
	}

	private void queryData() {
		listBean = jxClctDao.query_id(_id);
		if (listBean.size() > 0) {
			bean = listBean.get(0);
			mailNo.setText(bean.getMailNo());
			weight.setText(bean.getWeight() + " g");
			receiverName.setText(bean.getReceiverName());
			receiverMobile.setText(bean.getReceiverMobile());
			receiverAddress.setText(bean.getReceiverAddress());
			senderName.setText(bean.getSenderName());
			senderMobile.setText(bean.getSenderMobile());
			senderAddress.setText(bean.getSenderAddress());
			if (bean.getServiceType().equals("1")) {
				tv_serviceType.setText("快递包裹");
			} else if (bean.getServiceType().equals("2")) {
				tv_serviceType.setText("标准快件");
			} else {
				tv_serviceType.setText("");
			}
			if ("2".equals(bean.getSendType())) {
				ll_startTime.setVisibility(View.VISIBLE);
				tv_startTime.setText(bean.getStartTime() + "");
			} else {
				ll_startTime.setVisibility(View.GONE);
			}
			int distances = Integer.parseInt(bean.getDistance());
			if (distances == Integer.MAX_VALUE) {
				distance.setText("计算中");
			} else if (distances == Integer.MAX_VALUE - 1) {
				distance.setText("计算失败");
			} else {
				distance.setText(distances + "米");
			}
			tv_money.setText(bean.getFee() + "元");
			tv_actmoney.setText(bean.getActFee() + "元");
			tv_Integral.setText(bean.getIntegral() + "");
			if ("1".equals(bean.getPayment())) {
				tv_payment.setText("寄件现结 ");// 1：寄件现结 2：到付
			} else if ("2".equals(bean.getPayment())) {
				tv_payment.setText("到付");
			} else if ("3".equals(bean.getPayment())) {
				tv_payment.setText("积分抵扣");
			}
		}

	}

	public void myonClick(View v) {
		CollectionDetailActivityJX.this.finish();
	}

	/**
	 * 
	 * 查看派单信息
	 */
	// private void initData1(String type) {
	// LanShouFeedbackBean1 bean1 = new LanShouFeedbackBean1();
	// LanShouFeedbackBean2 bean2 = new LanShouFeedbackBean2();
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// String time = sdf.format(new Date());
	// List<LanShouFeedbackBean2> listfeed = new
	// ArrayList<LanShouFeedbackBean2>();
	// bean1.setDeviceId(deviceId);
	// bean1.setEmployee(username);
	// bean1.setDelvorgcode(orgCode);
	// bean1.setPostmankind("3");
	// bean1.setNumber("1");
	// bean2.setX0(bean.get(0).getMsg_id());
	// bean2.setX1("2");
	// bean2.setX2("100");
	// bean2.setX3(time);
	// bean2.setX4(bean.get(0).getCompanyid());
	// bean2.setX5(type + "\t1\t"+""+"\t1\t"+bean.get(0).getReservenum());
	// listfeed.add(bean2);
	// bean1.setDataDetail(listfeed);
	// String urlString =Global.LanShouFeedback_URL;
	// String jsonString = JSON.toJSONString(bean1);
	// bean = gatherDao.query_id(_id);
	// if (bean.get(0).getReadtype()==0) {
	// LanShouFeedbackTask async = new LanShouFeedbackTask();
	// async.setListener(new LanShouFeedbackTask.onPostExecuteListener() {
	// @Override
	// public void onPostExecute(String result) {
	// Log.e("result", result);
	// String reObjc = "";
	// if (result != null) {
	// try {
	// JSONObject obj = new JSONObject(result);
	// reObjc = obj.get("result").toString();
	// if ("1".equals(reObjc)) {
	// if ("2".equals(Global.backType)) {
	// DeliverDaoFactory.getInstance().getGather_msgdao(getApplicationContext())
	// .updateReadType(_id, 1);
	// Global.backType = "1";
	// }
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	// }
	// });
	// async.execute(urlString,jsonString,"json");
	// if ("1".equals(type)) {
	// Global.backType = "2";
	// }
	// }
	// }

	@Override
	protected void onResume() {
		Utils.startIntentService(CollectionDetailActivityJX.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

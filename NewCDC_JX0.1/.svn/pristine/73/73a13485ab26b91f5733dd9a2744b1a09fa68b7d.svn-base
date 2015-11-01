package com.newcdc.activity.clcttask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.asynctask.LanShouFeedbackTask;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.model.GatherTableBean;
import com.newcdc.model.LanShouFeedbackBean1;
import com.newcdc.model.LanShouFeedbackBean2;
import com.newcdc.tools.Global;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

public class CollectionDetailActivity extends BaseActivity {

	private TextView tv_clientname = null;
	private TextView tv_clientphone = null;
	private TextView tv_clientaddress = null;
	private TextView tv_clientlssx = null;
	private TextView tv_clientcnday = null;
	private TextView tv_client_createtime = null;
	private TextView tv_client_distance = null;
	private TextView tv_client_reservenum = null;
	private TextView tv_client_type = null;
	private String orgCode,username,deviceId;
	private int _id = 0;
	private Gather_MsgDao gatherDao;
	private List<GatherTableBean> bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_detail);
		inits();
		initData();
		initData1("1");
		initData1("2");
	}

	private void inits() {
		gatherDao = DeliverDaoFactory.getInstance().getGather_msgdao(
				getApplicationContext());
		tv_client_reservenum = (TextView) findViewById(R.id.tv_client_reservenum);
		tv_clientname = (TextView) findViewById(R.id.tv_clientname);
		tv_clientphone = (TextView) findViewById(R.id.tv_clientphone);
		tv_clientaddress = (TextView) findViewById(R.id.tv_clientaddress);
		tv_clientlssx = (TextView) findViewById(R.id.tv_clientlssx);
		tv_clientcnday = (TextView) findViewById(R.id.tv_clientcnday);
		tv_client_createtime = (TextView) findViewById(R.id.tv_client_createtime);
		tv_client_distance = (TextView) findViewById(R.id.tv_client_distance);
		tv_client_type=(TextView) findViewById(R.id.tv_client_type);
		UserInfoUtils user=new UserInfoUtils(CollectionDetailActivity.this);
		deviceId=Utils.getDeviceId(this);
		orgCode=user.getUserDelvorgCode();
		username=user.getUserName();
	}

	private void initData() {
		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			_id = bundle.getInt("detail_id");
			queryData();
		}
	}

	private void queryData() {
		bean = gatherDao.query_id(_id);
		tv_client_reservenum.setText(bean.get(0).getReservenum());
		tv_clientname.setText(bean.get(0).getClientname());
		tv_clientphone.setText(bean.get(0).getPhone());
		tv_clientaddress.setText(bean.get(0).getAddress());
		tv_clientlssx.setText(bean.get(0).getLssx());
		tv_clientcnday.setText(bean.get(0).getCnday());
		tv_client_createtime.setText(bean.get(0).getCreatetime());
		if (bean.get(0).getMsg_typecode().equals("1")) {
			tv_client_type.setText("快递包裹");
		}else {
			tv_client_type.setText("标准快件");
		}
		int distance=Integer.parseInt(bean.get(0).getDistance());
		if(distance==Integer.MAX_VALUE){
			tv_client_distance.setText("计算中");
		}else if (distance==Integer.MAX_VALUE-1) {
			tv_client_distance.setText("计算失败");
		}else {
			tv_client_distance.setText(distance+"米");
		}
	}

	public void myonClick(View v) {
		CollectionDetailActivity.this.finish();
	}
	
	/**
	 * 
	 * 查看派单信息
	 */
	private void initData1(String type) {
		LanShouFeedbackBean1 bean1 = new LanShouFeedbackBean1();
		LanShouFeedbackBean2 bean2 = new LanShouFeedbackBean2();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		List<LanShouFeedbackBean2> listfeed = new ArrayList<LanShouFeedbackBean2>();
		bean1.setDeviceId(deviceId);
		bean1.setEmployee(username);
		bean1.setDelvorgcode(orgCode);
		bean1.setPostmankind("3");
		bean1.setNumber("1");
		bean2.setX0(bean.get(0).getMsg_id());
		bean2.setX1("2");
		bean2.setX2("100");
		bean2.setX3(time);
		bean2.setX4(bean.get(0).getCompanyid());
		bean2.setX5(type + "\t1\t"+""+"\t1\t"+bean.get(0).getReservenum());
		listfeed.add(bean2);
		bean1.setDataDetail(listfeed);
		String urlString =Global.LanShouFeedback_URL;
		String jsonString = JSON.toJSONString(bean1);
		bean = gatherDao.query_id(_id);
		if (bean.get(0).getReadtype()==0) {
			LanShouFeedbackTask async = new LanShouFeedbackTask();
			async.setListener(new LanShouFeedbackTask.onPostExecuteListener() {
				@Override
				public void onPostExecute(String result) {
					Log.e("result", result);
					String reObjc = "";
					if (result != null) {
						try {
							JSONObject obj = new JSONObject(result);
							reObjc = obj.get("result").toString();
							if ("1".equals(reObjc)) {
								if ("2".equals(Global.backType)) {
									DeliverDaoFactory.getInstance().getGather_msgdao(getApplicationContext())
											.updateReadType(_id, 1);
									Global.backType = "1";
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					} 
				}
			});
			async.execute(urlString,jsonString,"json");
			if ("1".equals(type)) {
				Global.backType = "2";
			}
		}
	}

	@Override
	protected void onResume() {
		Utils.startIntentService(CollectionDetailActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

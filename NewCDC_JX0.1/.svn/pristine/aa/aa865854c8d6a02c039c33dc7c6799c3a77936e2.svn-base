package com.newcdc.activity.clcttask;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.tools.Utils;

/**
 * 揽收模块的客户详情页面
 * @author Doraemon
 *
 */
public class CustomerInfoActivity extends Activity{
	
	private TextView tv_sendName,tv_sendTel,tv_recvName,tv_recvTel;
	private Spinner sp_sendProvince,sp_sendCity,sp_recvProvince,sp_recvCity;
	private EditText et_sendAddr,et_recvAddr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_customer_info);
		
		initView();
	}

	public void initView(){
		tv_sendName = (TextView) findViewById(R.id.tv_send_name);
		tv_sendTel = (TextView) findViewById(R.id.tv_send_tel);
		tv_recvName = (TextView) findViewById(R.id.tv_recv_name);
		tv_recvTel = (TextView) findViewById(R.id.tv_recv_tel);
		
		et_sendAddr = (EditText) findViewById(R.id.et_send_addr);
		et_recvAddr = (EditText) findViewById(R.id.et_recv_addr);
		
		sp_sendProvince = (Spinner) findViewById(R.id.sp_send_province);
		sp_sendCity = (Spinner) findViewById(R.id.sp_send_city);
		sp_recvProvince = (Spinner) findViewById(R.id.sp_recv_province);
		sp_recvCity = (Spinner) findViewById(R.id.sp_recv_city);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(CustomerInfoActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}
	
}

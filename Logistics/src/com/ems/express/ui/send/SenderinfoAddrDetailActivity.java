package com.ems.express.ui.send;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.R.id;
import com.ems.express.R.layout;
import com.ems.express.bean.City;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.util.ParamsUtil;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SenderinfoAddrDetailActivity extends Activity implements OnClickListener {
	EditText mEtLocation;
	ListView mLvQuery;
	Context mContext;
	Button btSelect;
	
	TextView mTvCancle;
	
	ArrayAdapter mAdapter;
	
	Boolean flag = true;
	
	private City mProv;
	private City mCity;
	private City mCounty;
	
	private List<String> mData = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_senderinfo_addr_detail);
		
		mTvCancle = (TextView) findViewById(R.id.tv_cancle);
		
		mEtLocation = (EditText)findViewById(R.id.et_location);
		mLvQuery = (ListView) findViewById(R.id.query_address);
		btSelect = (Button) findViewById(R.id.bt_select);
		mContext = this;
		mAdapter = new ArrayAdapter<String>(mContext,
				R.layout.simple_list_item_query, mData);
		
		btSelect.setOnClickListener(this);
		mTvCancle.setOnClickListener(this);
		
		
		Intent intent = getIntent();
		
		mProv = (City) intent.getSerializableExtra("mProv");
		mCity = (City) intent.getSerializableExtra("mCity");
		mCounty = (City) intent.getSerializableExtra("mCounty");
		
		mEtLocation.setText(intent.getStringExtra("defaultAddress"));
		mEtLocation.setSelection(mEtLocation.getText().length());
		
		mLvQuery.setAdapter(mAdapter);
//		mLvQuery.setVisibility(View.GONE);
		mLvQuery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				flag = false;
				mLvQuery.setVisibility(View.GONE);
//				mEtLocation.setText(mData.get(position));
//				mEtLocation.setText(mData.get(position));
//				mLl.setVisibility(View.GONE);
				//强制隐藏键盘 
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(SenderinfoAddrDetailActivity.this.getCurrentFocus().getWindowToken(), 0); 
				
				Intent intent = new Intent();
				intent.putExtra("addressDeail", mData.get(position));
				
				setResult(RESULT_OK, intent);
				
				finish();
				finish();
			}
		});
		
		mEtLocation.addTextChangedListener(new TextWatcher() {

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
				if (flag)
					getQueryAddress(s);
				flag = true;
			}

		});
	}
	
	public void getQueryAddress(Editable s) {
		if (mProv != null && mCity != null) {
			mData.clear();
			mLvQuery.setVisibility(View.VISIBLE);
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put("provCode", mProv.getCode() + "");
			if(mProv.getCode() == 11||mProv.getCode()==12 || mProv.getCode()==31 || mProv.getCode() == 50){
				json.put("cityCode", mProv.getCode()+"");
			}else{
				json.put("cityCode", mCity.getCode()+"");
			}
			json.put("countyCode", mCounty.getCode() + "");
			json.put("addrName", s + "");
			String params = ParamsUtil.getUrlParamsByMap(json);
			System.out.println("json-pre:" + json);

			MyRequest<Object> req = new MyRequest<Object>(Method.POST, null,
					Constant.QueryAddress, new Listener<Object>() {

						@Override
						public void onResponse(Object arg0) {
							Log.e("msg", arg0.toString());

							JSONObject obj = JSONObject.parseObject(arg0
									.toString());
							if ("1".equals(obj.getString("result"))) {
								JSONObject bodyObj = obj.getJSONObject("body");
								JSONArray successObja = bodyObj
										.getJSONArray("success");
								JSONObject jsonDataObj = successObja
										.getJSONObject(0);
								String jsonData = jsonDataObj.getString("jsonData");
								if (jsonData.contains("prov_code")) {
									JSONObject addressListObj = JSONObject
											.parseObject(jsonData);
									JSONObject addressObj = addressListObj
											.getJSONObject("address_list");
									JSONArray addressObja = new JSONArray();
									try {
										addressObja = addressObj
												.getJSONArray("address");
									} catch (Exception e) {
										e.printStackTrace();
										return;
									}
									for (int i = 0; i < addressObja.size(); i++) {
										JSONObject jo = addressObja
												.getJSONObject(i);
										Log.e("msg", jo.getString("value"));
										mData.add(jo.getString("value"));
									}
									Log.e("msg", "list："+mData.toString());
									mAdapter.notifyDataSetChanged();
								}
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							arg0.printStackTrace();

						}
					}, params);
			App.getQueue().add(req);
		}
	}

	
	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.et_location2:
			mEtLocation.setFocusable(true);
			mEtLocation.setFocusableInTouchMode(true);
			mEtLocation.requestFocus();
			//弹出键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 
			imm.showSoftInput(mEtLocation,0);
			break;
			
		case R.id.bt_select:
			
            mLvQuery.setVisibility(View.GONE);
			
//			mEtLocation2.setText(mEtLocation.getText().toString());
//			mLls.setVisibility(View.GONE);
//			mLl.setVisibility(View.GONE);
//			llmain.setVisibility(View.VISIBLE);
			//强制隐藏键盘 
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0); 
			
			Intent intent = new Intent();
			intent.putExtra("addressDeail", mEtLocation.getText()+"");
			
			setResult(RESULT_OK, intent);
			
			finish();
			
			break;
		case R.id.tv_cancle:
			finish();
		default:
			break;
		}
		
	}
}

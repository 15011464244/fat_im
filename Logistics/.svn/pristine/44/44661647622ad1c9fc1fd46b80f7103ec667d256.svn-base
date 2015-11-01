package com.ems.express.ui;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.util.LoadCitysUtils;
import com.ems.express.util.LogUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.PreUtils;
import com.ems.express.util.SpfsUtil;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class Welcome extends Activity {


	boolean isFirstIn;;
	private Context mContext;
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);       //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MobclickAgent.updateOnlineConfig( this );
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		mContext = this;
		//初始化更新状态
		SpfsUtil.saveDowning(false);
		//默认未签到
		SpfsUtil.setIsSign(false);
		//加载省市县
		Boolean isLoading = SpfsUtil.getIsLoadingCity();
		if(isLoading){
			SpfsUtil.isLoadingCity(true);
			//后台进程
			LoadCitysUtils myAsyncTask = new LoadCitysUtils(mContext);
			myAsyncTask.execute("");
		}
		
		

		isFirstIn = PreUtils.loadBoolean("first", true);
		if (isFirstIn) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					goGuide();
				}
			}, 2000);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if(!"".equals(SpfsUtil.loadPhone())){
						signCheck(SpfsUtil.loadPhone());
					}else{
						goHome();
					}
					
				}
			}, 2000);
		}
	}
	
	/**
	 * 签到check
	 * @param phone
	 * @return
	 */
	public void signCheck(String phone){
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("mobile", phone);
		String params = ParamsUtil.getUrlParamsByMap(json);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.checkSign,
				new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						LogUtils.e("签到check  "+ arg0.toString());
							try {
								String result = (String) arg0;
								org.json.JSONObject object = new org.json.JSONObject(result.toString());
								if (object.getInt("result") == 1) {
									//已签到
									SpfsUtil.setIsSign(true);
								}
								goHome();
							} catch (Exception e) {
								e.printStackTrace();
								goHome();
							}
						}
				
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						LogUtils.e("签到check异常  "+ arg0.toString());
						Toast.makeText(Welcome.this, "查询是否签到失败!", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
						goHome();
					}
				}, params);
		App.getQueue().add(req);
		
	}

	private void goHome() {
//		Intent intent = new Intent(Welcome.this, HomeActivity.class);
		Intent intent = new Intent(Welcome.this, Home2Activity.class);
		Welcome.this.startActivity(intent);
		Welcome.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(Welcome.this, GuideViewPager.class);
		Welcome.this.startActivity(intent);
		Welcome.this.finish();
		PreUtils.saveBoolean("first", false);
	}

    @Override  
    public void onBackPressed() {  
        Intent intent= new Intent(Intent.ACTION_MAIN);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.addCategory(Intent.CATEGORY_HOME);  
        startActivity(intent);  
    }  
	
}

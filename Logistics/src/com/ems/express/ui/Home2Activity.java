package com.ems.express.ui;

import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.constant.Constant;
import com.ems.express.fragment.home.HomeIndexFragment;
import com.ems.express.fragment.home.HomePersonalFragment;
import com.ems.express.fragment.home.HomeSearchFragment;
import com.ems.express.net.MyRequest;
import com.ems.express.scan.activity.CaptureActivity;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.LogUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.update.UmengUpdateAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home2Activity extends BaseActivityForRequest implements OnClickListener,OnGetGeoCoderResultListener{
	private LinearLayout tosearch;
	private LinearLayout tohome;
	private LinearLayout topersonal;
	
	//map
	private GeoCoder coder;
	
	//经为度
	double lat;
	double lon;
	
//	public static Home2Activity home2;
	
	
	private ImageButton mScan;
	private TextView mSign;
	
	private RelativeLayout title;
	private LinearLayout bottom;
//	private ScrollView contant;
	private LinearLayout llContant;
	
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private HomePersonalFragment perFragment;
	private HomeSearchFragment searFragment;
	private HomeIndexFragment indexFragment;
	
	private Context mContext;
	
	private AnimationUtil util;
	
	public BroadcastReceiver msgBReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home2);
		
		mContext = this;
		//友盟更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		
		util = new AnimationUtil(this, R.style.dialog_animation);
		
		coder = GeoCoder.newInstance();
		coder.setOnGetGeoCodeResultListener(this);
		
		
		
		initView();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onRestart();
//		signStyle(SpfsUtil.isSign());
	}
	
	public void showScan(Boolean show){
		if(!show){
			mScan.setVisibility(View.GONE);
		}else {
			mScan.setVisibility(View.VISIBLE);
		}
	}
	public void showSign(Boolean show){
		if(!show){
			mSign.setVisibility(View.GONE);
		}else {
			mSign.setVisibility(View.VISIBLE);
		}
	}
	
	public void signStyle(Boolean isSign){
		if(isSign){
			mSign.setClickable(false);
			mSign.setOnClickListener(null);
			mSign.setTextColor(android.graphics.Color.GRAY);
			
		}else{
			mSign.setClickable(true);
			mSign.setOnClickListener(this);
			mSign.setTextColor(0xffffa500);
		}
	}
	
	@SuppressLint("ResourceAsColor")
	public void initView(){
		tosearch = (LinearLayout) this.findViewById(R.id.ll_tosearch);
		tohome = (LinearLayout) this.findViewById(R.id.ll_tohome);
		topersonal = (LinearLayout) this.findViewById(R.id.ll_topersonal);
		
		mScan = (ImageButton) this.findViewById(R.id.ib_scan);
		mSign = (TextView) this.findViewById(R.id.tv_sign);
		
		title = (RelativeLayout) this.findViewById(R.id.title);
		bottom = (LinearLayout) this.findViewById(R.id.bottom);
//		contant = (ScrollView) this.findViewById(R.id.sl_content);
		llContant = (LinearLayout) findViewById(R.id.ll_content);
		
		tosearch.setOnClickListener(this);
		tohome.setOnClickListener(this);
		topersonal.setOnClickListener(this);
		
		mScan.setOnClickListener(this);
		
		
		mFragmentManager = getFragmentManager();
		
		
		perFragment = new HomePersonalFragment();
		searFragment = new HomeSearchFragment();
		indexFragment = new HomeIndexFragment();
		
		
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(R.id.ll_content, searFragment);
		mFragmentTransaction.commit();
		
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(R.id.ll_content, indexFragment);
		mFragmentTransaction.commit();
		
		changeImgState(R.id.tv_b2, R.id.iv_b2);
		
	}
	
	//变更bottom的状态  查件：1 首页 ：2 我的：3 
	public void changeImgState(int tvId,int ivId){
		findViewById(R.id.iv_b1).setSelected(false);
		((TextView)findViewById(R.id.tv_b1)).setTextColor(0xff666666);
		findViewById(R.id.iv_b2).setSelected(false);
		((TextView)findViewById(R.id.tv_b2)).setTextColor(0xff666666);
		findViewById(R.id.iv_b3).setSelected(false);
		((TextView)findViewById(R.id.tv_b3)).setTextColor(0xff666666);
		findViewById(ivId).setSelected(true);
		((TextView)findViewById(tvId)).setTextColor(this.getResources().getColor(R.color.orange));
		
	}
	@Override
	public void onClick(View view) {
		mFragmentTransaction = mFragmentManager.beginTransaction();
		
		//动画效果
		mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//		mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//		mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
		
		
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()&&getCurrentFocus()!=null){
			if (getCurrentFocus().getWindowToken()!=null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}			  
		 }
		switch (view.getId()) {
		case R.id.ib_scan:
			Intent intent1 = new Intent(mContext, CaptureActivity.class);
			startActivityForResult(intent1, 999);
			break;
		case R.id.tv_sign:
			if (SpfsUtil.loadPhone().equals("")) {
				Intent toLogin = new Intent(mContext, LoginActivity.class);
				startActivity(toLogin);
				ToastUtil.show(mContext, "请先登录");
			} else {
				sign(SpfsUtil.loadPhone());
			}
			
			break;
		case R.id.ll_tosearch:
			changeImgState(R.id.tv_b1, R.id.iv_b1);
			mFragmentTransaction.replace(R.id.ll_content, searFragment);
			break;
		case R.id.ll_tohome:			
			changeImgState(R.id.tv_b2, R.id.iv_b2);
			mFragmentTransaction.replace(R.id.ll_content, indexFragment);
			break;
		case R.id.ll_topersonal:
			changeImgState(R.id.tv_b3, R.id.iv_b3);
			mFragmentTransaction.replace(R.id.ll_content, perFragment);
			break;
		default:
			break;
		}
		mFragmentTransaction.commit();
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 999:
			System.out.println("扫扫待处理");
			if(data!=null){
				Bundle bundle = data.getExtras();
				String resultStr = bundle.getString("txtResult");
				searFragment.search(resultStr);
			}
			break;

		default:
			break;
		}
	}
	/**
	 * 签到check
	 * @param phone
	 * @return
	 */
	public void signCheck(String phone){
		util.show();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("mobile", phone);
		String params = ParamsUtil.getUrlParamsByMap(json);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.checkSign,
				new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
							Log.e("gongjie", "签到查询"+arg0.toString());
							
							
							try {
								String result = (String) arg0;
								JSONObject object = new JSONObject(result.toString());
								if (object.getInt("result") == 1) {
									//已签到
									SpfsUtil.setIsSign(true);
									signStyle(true);
								} else {
									//未签到
									SpfsUtil.setIsSign(false);
									signStyle(false);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}finally{
								util.dismiss();
							}
							
						}
				
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(Home2Activity.this, "查询是否签到失败!", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
						util.dismiss();
					}
				}, params);
		App.getQueue().add(req);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int top = title.getBottom();
		int bot = bottom.getTop();
		System.out.println("---------top"+top+"-----------bot"+bot);
//		LayoutParams params = contant.getLayoutParams();
		LayoutParams params = llContant.getLayoutParams();
		params.height = bot - top + dp2px(13);
//		contant.setLayoutParams(params);
		llContant.setLayoutParams(params);
		/*if (SpfsUtil.getSignPhone() != SpfsUtil.loadPhone()) {
			
			signCheck(SpfsUtil.loadPhone());
		}*/
		signStyle(SpfsUtil.isSign());
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		int top = title.getBottom();
		int bot = bottom.getTop();
		System.out.println("---------top"+top+"-----------bot"+bot);
		LayoutParams params = llContant.getLayoutParams();
		params.height = bot - top + dp2px(13);
		llContant.setLayoutParams(params);
	}
	
	/**
	 * 签到
	 * @param phone
	 * @return
	 */
	public void sign(String phone){
		//开启菊花
		util.show();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("mobile", phone);
		String params = ParamsUtil.getUrlParamsByMap(json);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.sign,
				new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						LogUtils.e("Sign_response == "+arg0+"");
//						arg0 = "{\"content\":{\"days\":12,\"validIntegeal\":25,\"integeal\":1,\"mobile\":\"15011269225\"},\"result\":\"1\"}";
						//关闭菊花
						util.dismiss();
						//签到成功
						SpfsUtil.setIsSign(true);
						signStyle(true);
						if(((BaseActivityForRequest)mContext).stayThisPage){
							Log.e("msg", arg0.toString());
							try {
								String result = (String) arg0;
								JSONObject object = new JSONObject(result.toString());
								if (object.getInt("result") == 1) {
									JSONObject content = object.getJSONObject("content");
									int days = content.getInt("days");//签到天数
									int validIntegeal = content.getInt("validIntegeal");//当前积分
									int integeal = content.getInt("integeal");//签到获得积分
									DialogUtils.signSuccess(mContext, validIntegeal,days);
								} else {
									//签到失败
									ToastUtil.show(mContext, object.getString("errorMsg"));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						//关闭菊花
						util.dismiss();
						Toast.makeText(mContext, "签到失败!", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
					}
				}, params);
		App.getQueue().add(req);
		
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            DialogUtils.outConfirm(mContext);
        }  
        return true;  
    }

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		LatLng location = arg0.getLocation();
		lat = location.latitude;
		lon = location.longitude;
		
		
		
	}  
	
}

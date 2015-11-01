package com.ems.express.ui;

import java.util.HashMap;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.InjectView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.City;
import com.ems.express.net.MyRequest;
import com.ems.express.net.UrlUtils;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DeviceUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.RangeSelectUtil2;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class ServiceRangeActivity extends BaseActivityForRequest implements OnClickListener{
	private int intPro = 0;
	private int intCity = 0;
	private int intCount = 0;
	private TextView tv = null;
	private View reusltLayout = null;
	private TextView requestString = null;
	private TextView resutl = null;
	
	private Context mContext;
	private TextView jumpSend;
	private AnimationUtil util;
	
	private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    
    private RangeSelectUtil2 rsu2;
    private String strpro = null;
	private String strcity = null;
	private String strcounty = null;
	// 判断是否加载初始城市
			private boolean isFirstLoc = true;
		private GeoCoder coder;
		private BaiduMap mBaiduMap;
		private BDLocationListener myListener = new BDLocationListener() {

			@Override
			public void onReceivePoi(BDLocation location) {
				if (location != null) {
					System.out.println("onReceivePoi: " + location.getCity());
					System.out.println("onReceivePoi: " + location.getProvince());
					System.out.println("onReceivePoi: " + location.getDistrict());
				}
			}

			@Override
			public void onReceiveLocation(BDLocation location) {
//				if (((BaseActivityForRequest) mContext).stayThisPage) {
					if (true) {
					if (location != null) {
						if (isFirstLoc) {
							isFirstLoc = false;
							// 刷新地图
							LatLng ll = new LatLng(location.getLatitude(),
									location.getLongitude());
//							if (!"messageCenter".equals(activity)) {
//								locations = ll;
//								addMapMark2(ll);
//								setLocation(ll);
//							}
	//
//							if (!"signMessage".equals(activity)) {
//								locations = ll;
//								addMapMark2(ll);
//								setLocation(ll);
//							}

							ReverseGeoCodeOption option = new ReverseGeoCodeOption();
							option.location(ll);
							coder.reverseGeoCode(option);
						}
					}
				}
			}
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_range);
		findViewById(R.id.item_service_range).setOnClickListener(this);
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.layou_submit).setOnClickListener(this);
		tv = (TextView)findViewById(R.id.title_1);
		reusltLayout = findViewById(R.id.layout_result);
		jumpSend =(TextView)findViewById(R.id.bt_jump_send);
		reusltLayout.setVisibility(View.GONE);
		requestString = (TextView)findViewById(R.id.text_query_string);
		resutl = (TextView)findViewById(R.id.text_query_result);
		util =new AnimationUtil(this, R.style.dialog_animation);
		
		
		mContext = this;
		jumpSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SpfsUtil.loadPhone().equals("")){
					Intent intent1 = new Intent(ServiceRangeActivity.this,LoginActivity.class);
					startActivity(intent1);
					Toast.makeText(ServiceRangeActivity.this,"请先登录", 1).show();
				}else{
					Intent intent = new Intent(ServiceRangeActivity.this, SendActivity.class);
					startActivity(intent);
				}
			}
		});
		
		initSpinner();
		 if(!DeviceUtil.isNetworkAvailable(this)){
				ToastUtil.show(mContext, "没有网络，请检查网络是否开启");
			}else{
				util.show();
			}
			//新加的代码，实现本地位置的自动显示
			LocationMode mCurrentMode = LocationMode.NORMAL;
//			mBaiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
//					mCurrentMode, true, BitmapDescriptorFactory
//							.fromResource
	//
	//(R.drawable.icon_location_courier_map)));
//			mBaiduMap.setMaxAndMinZoomLevel(10, 18);
			// 开启定位图层
//			mBaiduMap.setMyLocationEnabled(true);
			// 定位初始化
			LocationClient mLocClient = new LocationClient(getApplicationContext());
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
			Log.e("ajh", "code: " + mLocClient.requestLocation());

//			LocationManager loctionManager;
//			String contextService=Context.LOCATION_SERVICE;
//			//通过系统服务，取得LocationManager对象
//			loctionManager=(LocationManager) getSystemService(contextService);
//			
//			String provider=LocationManager.GPS_PROVIDER;
//			Location location = loctionManager.getLastKnownLocation(provider);
//			Log.e("gongjie", ""+location.getLatitude());
//			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
			coder = GeoCoder.newInstance();
			coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

				@Override
				public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
					if (result != null) {
//						myLocation.setText(result.getAddress());
						Log.e("gongjie",result.getAddress());
						// 地址输入框赋初始值

						if (true) {
							if (result.getAddressDetail() != null) {
								strpro = result.getAddressDetail().province;
								if (strpro.length() > 0) {
									strpro = strpro.subSequence(0,
											strpro.length() - 1).toString();
								}
								strcity = result.getAddressDetail().city;
								if (strcity.length() > 0) {
									if (strcity.contains("北京")
											|| strcity.contains("天津")
											|| strcity.contains("上海")
											|| strcity.contains("重庆")) {
										strcity = result.getAddressDetail().city
												.substring(0, 2);
									}
								}
								strcounty = result.getAddressDetail().district;
							}
							if(strpro != null && strcity != null && strcounty != null){
								rsu2.freshByName(strpro, strcity, strcounty);
							}
							
//							// 第一次自动加载服务网点
//							submit();
							

						} else {
							// 关闭菊花
							util.dismiss();
						}
						// //关闭菊花
						 util.dismiss();
					}
				}

				@Override
				public void onGetGeoCodeResult(GeoCodeResult result) {
					if (result != null) {
						// ToastUtil.show(getApplicationContext(),
						// result.getAddress());
					}
				}
			});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		coder.destroy();
	}
	
	@Override
	public void onClick(View v) {
//		Intent intent = new Intent(this,ServiceRangeSelectActivity.class);
//		startActivityForResult(intent, 100);
		switch (v.getId()) {
//		case R.id.item_service_range:
//			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_ADDRESS,City.TYPE_COUNTY,-1,false, this);
//			break;
		case R.id.img_back:
			finish();
			break;
		case R.id.layou_submit:
			submit();
			break;
		default:
			break;
		}
		
	}
	
	private void submit(){
		intPro = ((City)provinceSpinner.getSelectedItem()).getCode();
		intCity = ((City)citySpinner.getSelectedItem()).getCode();
		intCount = ((City)countySpinner.getSelectedItem()).getCode();
		
		//收送范围统计
		MobclickAgent.onEvent(this, "rangeQuery");
		HashMap<String, Object> json = new HashMap<String, Object>();
		if(intPro > 0 ){
			json.put("PROV", intPro);
		}
		if(intCity > 0){
			json.put("CITY", intCity);
		}
		if(intCount > 0){
			json.put("COUNTY", intCount);
		}
		util.show();
		String params =  ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" +params);
		MyRequest<Object> req = new MyRequest<Object>(
				Method.POST,
				null,
				UrlUtils.URL_SERVICE_RANGE,
				new Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						if(TextUtils.isEmpty(arg0 +"")){
							ToastUtil.show(ServiceRangeActivity.this, "访问失败，请重试");
							util.dismiss();
						}else{
							JSONObject json = JSONObject.parseObject("" + arg0);
							if(json.containsKey("body")){
								reusltLayout.setVisibility(View.VISIBLE);
								String str = json.getString("body");
								if(intCity <= 0 && intCount <= 0&& intPro <= 0){
									requestString.setText("全国");
								}else{
									requestString.setText(tv.getText());
								}
								
								if(str.contains("不支持收送")){
									resutl.setText("不支持收送");
								}else if(str.contains("出错")){
									resutl.setText("揽收范围查询接口出错");
								}else{
									JSONObject obj = json.getJSONObject("body");
									StringBuffer sb = new StringBuffer();
									for(String key: obj.keySet()){
										sb.append(key).append(":").append(obj.getString(key)).append("\n");
									}
									resutl.setText(sb.toString());
								}
							}else{
								ToastUtil.show(ServiceRangeActivity.this, "数据获取失败");
								util.dismiss();
							}
						}
						util.dismiss();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(ServiceRangeActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
						util.dismiss();
					}
				}, params);
		App.getQueue().add(req);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ServiceRangeSelectActivity.REQUEST_CODE_GET_ADRRESS) {
			if (resultCode == RESULT_OK) {
				City pro = (City)data.getSerializableExtra(ServiceRangeSelectActivity.CITY_PRO);
				City city = (City)data.getSerializableExtra(ServiceRangeSelectActivity.CITY_CITY);
				City county = (City)data.getSerializableExtra(ServiceRangeSelectActivity.CITY_COUNTY);
//				ToastUtil.show(this, "数据：" + pro+":" + county+ ":" + city);
				String str = "";
				if(pro != null){
					Log.e("ajh", "pro.getName(): " + pro.getName());
					str+= pro.getName()+" ";
					intPro = pro.getCode();
				}
				if(city != null){
					Log.e("ajh", "city.getName(): " + city.getName());
					str+= city.getName()+"\t";
					intCity = city.getCode();
				}
				if(county != null){
					Log.e("ajh", "county.getName(): " + county.getName());
					str+= county.getName();
					intCount = county.getCode();
				}
				
				tv.setText(str);
				
			}else{
				tv.setText("请选择原寄地址");
//				ToastUtil.show(this, "取消了");
				intPro = 0;
				intCity = 0;
				intCity = 0;
			}
		}
	}
	
	public void initSpinner(){
		 provinceSpinner = (Spinner)findViewById(R.id.spin_province);
         citySpinner = (Spinner)findViewById(R.id.spin_city);
         countySpinner = (Spinner)findViewById(R.id.spin_county);
        
         rsu2 = new RangeSelectUtil2(mContext, provinceSpinner, citySpinner, countySpinner);
        
         rsu2.initCityList();
         rsu2.loadData();
	}

	
}

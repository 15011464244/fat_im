package com.ems.express.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MyLocationConfigeration;
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
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DeviceUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.RangeSelectUtil2;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;

public class PriceActivity extends BaseActivityForRequest implements OnClickListener, OnCheckedChangeListener{
	@InjectView(R.id.et_weight)
	EditText mEtWeight;
	@InjectView(R.id.layout_result)
	View mLayoutResult;
	@InjectView(R.id.mail_type_selection)
	RadioGroup mMailTypeSelection;
	@InjectView(R.id.tv_product)
	TextView mTvProduct;
	@InjectView(R.id.tv_weight)
	TextView mTvWeight;
	@InjectView(R.id.tv_final_price)
	TextView mTvFinalPrice;
	
	
	private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    
    private Spinner provinceSpinner2 = null;  //省级（省、直辖市）
    private Spinner citySpinner2 = null;     //地级市
    private Spinner countySpinner2 = null;    //县级（区、县、县级市）
    
    private String sendProvCode;
    private String sendCityCode;
    private String sendCountyCode;
    private String receiveProvCode;
    private String receiveCityCode;
    private String receiveCountyCode;

    
    private String strpro = null;
	private String strcity = null;
	private String strcounty = null;
	
	RangeSelectUtil2 rsu2;
	RangeSelectUtil2 rsu1;
	
	
	
	private Context mContext;
	private String mMailType = "2";
	private String mSourceProv;
	private String mSourceCity;
	private String mTargetProv;
	private ImageView imgProgress;
	private ImageView back;
	private TextView jumpSend;
	private AnimationUtil util;
	
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
			if (((BaseActivityForRequest) mContext).stayThisPage) {
				if (location != null) {
					if (isFirstLoc) {
						isFirstLoc = false;
						// 刷新地图
						LatLng ll = new LatLng(location.getLatitude(),
								location.getLongitude());
//						if (!"messageCenter".equals(activity)) {
//							locations = ll;
//							addMapMark2(ll);
//							setLocation(ll);
//						}
//
//						if (!"signMessage".equals(activity)) {
//							locations = ll;
//							addMapMark2(ll);
//							setLocation(ll);
//						}

						ReverseGeoCodeOption option = new ReverseGeoCodeOption();
						option.location(ll);
						coder.reverseGeoCode(option);
					}
				}
			}
		}
	};
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, PriceActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price);
		mContext = this;
		util = new AnimationUtil(this, R.style.dialog_animation);
		initSpinner();
		
		if(!DeviceUtil.isNetworkAvailable(this)){
			ToastUtil.show(mContext, "没有网络，请检查网络是否开启");
		}else{
			util.show();
		}
		//新加的代码，实现本地位置的自动显示
		LocationMode mCurrentMode = LocationMode.NORMAL;
//		mBaiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
//				mCurrentMode, true, BitmapDescriptorFactory
//						.fromResource
//
//(R.drawable.icon_location_courier_map)));
//		mBaiduMap.setMaxAndMinZoomLevel(10, 18);
		// 开启定位图层
//		mBaiduMap.setMyLocationEnabled(true);
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

//		LocationManager loctionManager;
//		String contextService=Context.LOCATION_SERVICE;
//		//通过系统服务，取得LocationManager对象
//		loctionManager=(LocationManager) getSystemService(contextService);
//		
//		String provider=LocationManager.GPS_PROVIDER;
//		Location location = loctionManager.getLastKnownLocation(provider);
//		Log.e("gongjie", ""+location.getLatitude());
//		LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
		coder = GeoCoder.newInstance();
		coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result != null) {
//					myLocation.setText(result.getAddress());
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
							rsu1.freshByName(strpro, strcity, strcounty);
						}
						
//						// 第一次自动加载服务网点
//						submit();
						

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
		
		
		ButterKnife.inject(this);
		((TextView) findViewById(R.id.tv_title)).setText("运费查询");
//		back  = (ImageView)findViewById(R.id.img_back);
		findViewById(R.id.layout_end_location).setOnClickListener(this);
		findViewById(R.id.layout_start_location).setOnClickListener(this);
		findViewById(R.id.layout_query).setOnClickListener(this);
		jumpSend =(TextView)findViewById(R.id.tv_info);
		jumpSend.setVisibility(View.VISIBLE);
		jumpSend.setText("寄件");
		mMailTypeSelection.setOnCheckedChangeListener(this);
		
//		back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
		
		jumpSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SpfsUtil.loadPhone().equals("")){
					Intent intent1 = new Intent(PriceActivity.this,LoginActivity.class);
					startActivity(intent1);
					Toast.makeText(PriceActivity.this,"请先登录", 1).show();
				}else{
					Intent intent = new Intent(PriceActivity.this, SendActivity.class);
					startActivity(intent);
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
	
	public void back(View v) {
		finish();
	}
	
	@Override
	public void onClick(View v) {
		
		int type;
		if (mMailTypeSelection.getCheckedRadioButtonId() == R.id.premium) {
			type = 2;
		} else {
			type = 1;
		}
		switch (v.getId()) {
		case R.id.layout_end_location:
			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_EXPRENSE, City.TYPE_CITY,
					type, false,
					this);
			break;
		case R.id.layout_start_location:
			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_EXPRENSE, City.TYPE_CITY,
					type, true,
					this);
			break;
		case R.id.layout_query:
			submit();
			break;
		default:
			break;
		}
	}
	
	public void submit(){
		//运费查询统计
		MobclickAgent.onEvent(this, "priceQuery");
		
		getAddress();
		/*if(mSourceProv == null || mSourceCity.isEmpty()){
			ToastUtil.show(mContext, "请选择寄件省份");
			return;
		}
		if(mMailType == "1"){
			if(mSourceCity == null || mSourceCity.isEmpty()){
				ToastUtil.show(mContext, "请选择寄件城市");
				return;
			}
		}
		String sendCity = mMailType == "1" ? mSourceProv + "\t" + mSourceCity : mTargetProv;
		if (sendCity == null || "".equals(sendAddress)) {
			ToastUtil.show(mContext, "请选择收件城市");
			return;
		}*/
		if(sendProvCode == null || sendProvCode.isEmpty()){
			ToastUtil.show(mContext, "请选择寄件城市");
			return;
		}
		if(receiveProvCode == null || receiveProvCode.isEmpty()){
			ToastUtil.show(mContext, "请选择收件城市");
			return;
		}
		if(mEtWeight.getText().toString().isEmpty()){
			ToastUtil.show(mContext, "请填写重量");
			return;
		}
		util.show();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("serviceType", mMailType);
//		json.put("sendCity", mMailType == "2" ? mSourceProv + "\t" + mSourceCity : mSourceProv);
//		json.put("recvCity", mTargetProv);
		Log.i("tgxx", sendCountyCode);
		Log.i("tgxx", receiveCountyCode);
		json.put("sendProvCode",sendProvCode );
		json.put("receiveProvCode", receiveProvCode);
		json.put("sendCityCode",sendCityCode );
		json.put("receiveCityCode", receiveCityCode);
		json.put("sendCountyCode",sendCountyCode );
		json.put("receiveCountyCode", receiveCountyCode);
		
		json.put("weight",Integer.parseInt(mEtWeight.getText().toString()));
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		Log.e("msg", params);
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null,Constant.Costquery, new Listener<Object>() {
					@Override
					public void onResponse(Object resp) {
						if (resp == null || resp.toString().isEmpty()) {
							ToastUtil.show(mContext, "查询失败，请稍后重试");
							util.dismiss();
							return;
						}
						/**
						 * {"model":{"message1":29,"message2":21,"message":21},"cleared":false}
						 */
						System.out.println("prices: " + resp);
						try {
							JSONObject jso = new JSONObject(resp.toString());
//							JSONObject model = jso.getJSONObject("model");
//							String price = model.getString("message");
							String price = jso.getString("fee");
							mTvProduct.setText(mMailType == "1" ? "快递包裹" : "标准快递");
							mTvWeight.setText(mEtWeight.getText().toString());
							mTvFinalPrice.setText(price);
							mLayoutResult.setVisibility(View.VISIBLE);
						} catch (Exception e) {
							e.printStackTrace();
							try {
								ToastUtil.show(mContext, new JSONObject(resp.toString()).getString("errorMsg"));
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Log.i("tgxx",resp.toString());
							util.dismiss();
						}
						util.dismiss();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(PriceActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
						util.dismiss();
					}
				}, params);
		App.getQueue().add(req);
	}

	
	private String sendAddress = "";
	private String endAddress = "";

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ServiceRangeSelectActivity.REQUEST_CODE_GET_ADRRESS) {
			if (resultCode == RESULT_OK) {
				City pro = (City) data.getSerializableExtra(ServiceRangeSelectActivity.CITY_PRO);
				City city = (City) data.getSerializableExtra(ServiceRangeSelectActivity.CITY_CITY);
				City county = (City) data.getSerializableExtra(ServiceRangeSelectActivity.CITY_COUNTY);
//				ToastUtil.show(this, "数据：" + pro + ":" + county + ":" + city);
				String str = "";
				if (pro != null) {
					Log.e("ajh", "pro.getName(): " + pro.getName());
					str += pro.getName() + " ";
				}
				if (city != null) {
					Log.e("ajh", "city.getName(): " + city.getName());
					str += city.getName() + "\t";
				}
				if (county != null) {
					Log.e("ajh", "county.getName(): " + county.getName());
					str += county.getName();
				}
				if (data.getBooleanExtra(ServiceRangeSelectActivity.KEY_IS_SEND_CITY, false)) {
					sendAddress = str;
					mSourceProv = pro.getName();
					mSourceCity = city.getName();
					TextView textView = (TextView) findViewById(R.id.text1);
					textView.setText(sendAddress);
				} else {
					endAddress = str;
					mTargetProv = pro.getName();
					TextView textView = (TextView) findViewById(R.id.text2);
					textView.setText(endAddress);
				}
			} else {
//				ToastUtil.show(this, "取消了");
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mMailType = checkedId == R.id.premium ? "2" : "1";
//		ToastUtil.show(this, mMailType);
	}
	
	//获取地址
	public void getAddress(){
		City prov = (City) provinceSpinner.getSelectedItem();
		City city = (City) citySpinner.getSelectedItem();
		City county = (City) countySpinner.getSelectedItem();
		sendProvCode = prov.getCode()+"";
		sendCityCode = city.getCode()+"";
		sendCountyCode = county.getCode()+"";
		

		
//			if(11 == prov.getCode() || 
//			12 == prov.getCode() ||
//			31 == prov.getCode() ||
//			50 == prov.getCode()){
//				sendAddress =  prov.getName()+"\t"+city.getName().substring(0,city.getName().length()-1);
//			}else{
//				sendAddress =   prov.getName()+"\t"+city.getName()endAddress = ;
//			}
//			
			City prov2 = (City) provinceSpinner2.getSelectedItem();
			City city2 = (City) citySpinner2.getSelectedItem();
			City county2 = (City) countySpinner2.getSelectedItem();
			
			receiveProvCode = prov2.getCode()+"";
			receiveCityCode = city2.getCode()+"";
			receiveCountyCode = city2.getCode()+"";

//			endAddress = prov2.getCode()+"";
//			endAddress = prov2.getName();
	}
	
	public void initSpinner(){
		 provinceSpinner = (Spinner)findViewById(R.id.spin_province);
        citySpinner = (Spinner)findViewById(R.id.spin_city);
        countySpinner = (Spinner)findViewById(R.id.spin_county);
        
        provinceSpinner2 = (Spinner)findViewById(R.id.spin_province2);
        citySpinner2 = (Spinner)findViewById(R.id.spin_city2);
        countySpinner2 = (Spinner)findViewById(R.id.spin_county2);
      
       rsu1 = new RangeSelectUtil2(mContext, provinceSpinner, citySpinner, countySpinner);
      
       rsu1.initCityList();
       rsu1.loadData();
       
       rsu2 = new RangeSelectUtil2(mContext, provinceSpinner2, citySpinner2, countySpinner2);
       
       rsu2.initCityList();
       rsu2.loadData();
	}

}

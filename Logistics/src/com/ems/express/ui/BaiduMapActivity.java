package com.ems.express.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.android.volley.toolbox.GsonPostParamsRequest;
import com.android.volley.toolbox.GsonRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
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
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.adapter.message.SignMessageBean;
import com.ems.express.bean.City;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.bean.MessageCenterBean;
import com.ems.express.constant.Constant;
import com.ems.express.net.Carrier;
import com.ems.express.net.MyRequest2;
import com.ems.express.net.ServicePoint;
import com.ems.express.net.ServicePointResp;
import com.ems.express.net.UrlUtils;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DeviceUtil;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.LogUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.RangeSelectUtil2;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class BaiduMapActivity extends BaseActivityForRequest implements
		OnClickListener, OnMarkerClickListener/*, OnWheelChangedListener*/ {
	@SuppressWarnings("unused")
	private static final String LTAG = BaiduMapActivity.class.getSimpleName();
	public final static String KEY_TYPE = "key_type";
	public final static int TYPE_SERVICE_POINT = 1;
	public final static int TYPE_CARRIER = 2;
	private boolean isFirstLoc = true;
	// 判断是否加载初始城市
	private boolean isFirstLoc2 = true;
	private String messageIsSign;
	private LatLng locations = null;
	private TextView text;
	private int type = 0;
	private TextView myLocation;
	private MapView mMapView;
	private TextView jumpSend;
	private BaiduMap mBaiduMap;
	private GeoCoder coder;
	private TextView tip = null;
	private List<ServicePoint> servicePoints;
	private List<Carrier> carriers;
	private View above = null;
	private Button btn = null;
	private String activity;
	private String phoneNum;
	private double longitude;
	private double latitude;
	private String orgcode;
	private String username;
	private LatLng ll;
	private DeliveryMessageBean mDeliveryMessage;

	private Context mContext;

	private SDKReceiver mReceiver;

	private AnimationUtil util;
	
	
	private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    
    private RangeSelectUtil2 rsu2;

	private SendNoticeBean mSendNoticeBean;

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
						if (!"messageCenter".equals(activity)) {
							locations = ll;
							addMapMark2(ll);
							setLocation(ll);
						}

						if (!"signMessage".equals(activity)) {
							locations = ll;
							addMapMark2(ll);
							setLocation(ll);
						}

						ReverseGeoCodeOption option = new ReverseGeoCodeOption();
						option.location(ll);
						coder.reverseGeoCode(option);
					}
				}
			}
		}
	};

	private void addMapMark(LatLng ll) {
		MarkerOptions markerOptions = new MarkerOptions().icon(
				BitmapDescriptorFactory
						.fromResource(R.drawable.location_other))
				.position(ll);

		if (mBaiduMap != null) {
			mBaiduMap.addOverlay(markerOptions);

			mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
		}
	}

	private void addMapMark2(LatLng ll) {
		MarkerOptions markerOptions = new MarkerOptions().icon(
				BitmapDescriptorFactory.fromResource(R.drawable.location_mine))
				.position(ll);

		if (mBaiduMap != null) {
			// System.out.println("---------mBaiduMap  "+mBaiduMap);
			// System.out.println("---------markerOptions  "+markerOptions);
			mBaiduMap.addOverlay(markerOptions);

			mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
		}
	}

	private void setLocation(LatLng ll) {
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			handler.postDelayed(this, 60000);
			Log.i("info", "1");

			mBaiduMap.clear();

			if ("1".equals(messageIsSign)) {
				queryFindEmlpoyeeMessageByPhone(orgcode, username);
			} else if ("2".equals(messageIsSign)) {
				querySignMessage(orgcode, username);
			}
			ll = new LatLng(latitude, longitude);
			addMapMark(ll);
			MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(18)
					.build();
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
					.newMapStatus(mapStatus);
			mBaiduMap.setMapStatus(mapStatusUpdate);
		}

	};

	/**
	 * 查询下段的信息
	 * 
	 * @param orgcode
	 * @param username
	 */
	public void queryFindEmlpoyeeMessageByPhone(final String orgcode,
			String username) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orgcode", orgcode);
		map.put("username", username);
		GsonPostParamsRequest<MessageCenterBean> rep = new GsonPostParamsRequest<MessageCenterBean>(
				Method.POST, Constant.QueryNextSection, null,
				new Listener<MessageCenterBean>() {
					@Override
					public void onResponse(MessageCenterBean arg0) {
						if (((BaseActivityForRequest) mContext).stayThisPage) {
							Log.e("msg", JSONObject.toJSONString(arg0));
							if (arg0 != null) {
								if ("1".equals(arg0.getResult())) {
									mDeliveryMessage = arg0.getBody()
											.getSuccess();
									longitude = mDeliveryMessage.getLongitude();
									latitude = mDeliveryMessage.getLatitude();
								} else if ("0".equals(arg0.getResult())) {
									Log.e("BaiduMapActivity", arg0.getBody()
											.getError());
								}
							}
						}
					}

				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Log.e("BaiduMapActivity",
								"查询下段信息失败" + arg0.getMessage());
					}
				}, MessageCenterBean.class, map);
		App.getQueue().add(rep);
	}

	private void querySignMessage(String orgcode, String username) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orgcode", orgcode);
		map.put("username", username);
		GsonPostParamsRequest<SignMessageBean> rep = new GsonPostParamsRequest<SignMessageBean>(
				Method.POST, Constant.QueryNextSection, null,
				new Listener<SignMessageBean>() {
					@Override
					public void onResponse(SignMessageBean arg0) {
						Log.e("msg", JSONObject.toJSONString(arg0));
						if (arg0 != null) {
							if ("1".equals(arg0.getResult())) {
								mSendNoticeBean = arg0.getBody().getSuccess();
								longitude = mSendNoticeBean.getLongitude();
								latitude = mSendNoticeBean.getLatitude();
								Log.e("msg", longitude + ";" + latitude);
							} else if ("0".equals(arg0.getResult())) {
								Log.e("BaiduMapActivity", arg0.getBody()
										.getError());
							}
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Log.e("BaiduMapActivity",
								"查询下段信息失败" + arg0.getMessage());
					}
				}, SignMessageBean.class, map);
		App.getQueue().add(rep);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		mContext = this;
		// 开启菊花
		util = new AnimationUtil(this, R.style.dialog_animation);
		
		setInitSpinner();
		
		if(!DeviceUtil.isNetworkAvailable(this)){
			ToastUtil.show(mContext, "没有网络，请检查网络是否开启");
		}else{
			util.show();
		}
		
		findViewById(R.id.layout_2).setOnClickListener(this);
		findViewById(R.id.layout_4).setOnClickListener(this);
		TextView texTitle = (TextView) findViewById(R.id.tex_title);
		texTitle.setText("附近的快递员");
		jumpSend = (TextView) findViewById(R.id.bt_jump_send);
		tip = (TextView) findViewById(R.id.item_tip);
		text = (TextView) findViewById(R.id.item_value_2);
		mMapView = (MapView) findViewById(R.id.bmapView);
		above = findViewById(R.id.layout_above);
		btn = (Button) findViewById(R.id.btn_fullscreen);
		btn.setOnClickListener(this);
		myLocation = (TextView) findViewById(R.id.item_loading);
		Intent intent = getIntent();
		type = intent.getIntExtra(KEY_TYPE, TYPE_SERVICE_POINT);
		mDeliveryMessage = (DeliveryMessageBean) intent
				.getSerializableExtra("deliveryMessage");
		activity = intent.getStringExtra("activity");
		phoneNum = intent.getStringExtra("phoneNum");
		orgcode = intent.getStringExtra("orgcode");
		username = intent.getStringExtra("username");

//		mTvZone2 = (TextView) this.findViewById(R.id.tv_zone2);
//		mIvZone2 = (ImageView) this.findViewById(R.id.iv_zone2);
//
//		mTvZone2.setOnClickListener(this);
//		mIvZone2.setOnClickListener(this);
//
//		mProvinceWheel = (WheelView) findViewById(R.id.wv_province);
//		mCityWheel = (WheelView) findViewById(R.id.wv_city);
//		mCountyWheel = (WheelView) findViewById(R.id.wv_county);
//
//		String[] defaultData = { "未获取数据" };
//		mProvinceWheel.addChangingListener(this);
//		mCityWheel.addChangingListener(this);
//		mCountyWheel.addChangingListener(this);
//
//		mProvinceWheel.setVisibleItems(3);
//		mCityWheel.setVisibleItems(3);
//		mCountyWheel.setVisibleItems(3);
//
//		mProvinceWheel.setViewAdapter(new ArrayWheelAdapter<String>(this,
//				defaultData));
//		mCityWheel.setViewAdapter(new ArrayWheelAdapter<String>(this,
//				defaultData));
//		mCountyWheel.setViewAdapter(new ArrayWheelAdapter<String>(this,
//				defaultData));

//		rsUtil = new RangeSelectUtil(mContext);
//		rsUtil.loadNetData(mContext, mProvinceWheel, mCityWheel, mCountyWheel);
//		// rsUtil.loadLocalData(mContext);
//		llAddress = (LinearLayout) findViewById(R.id.ll_address);

		messageIsSign = intent.getStringExtra("messageIsSign");

		mSendNoticeBean = (SendNoticeBean) intent
				.getSerializableExtra("sendNoticeBean");
		switch (type) {
		case TYPE_SERVICE_POINT:
			tip.setText("网点查询结果");
			setHeadTitle("服务网点");
			break;
		case TYPE_CARRIER:
			tip.setText("快递员查询结果");
			setHeadTitle("快递员位置");
			jumpSend.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		/*
		 * //判断是否有网络 if(!DeviceUtil.isNetworkAvailable(this)){
		 * ToastUtil.show(this, "请检查您的网络是否开启"); util.dismiss(); return; }
		 */

		mBaiduMap = mMapView.getMap();

		double latitude = intent.getDoubleExtra("LATITUDE", 1);
		double longitude = intent.getDoubleExtra("LONGITUDE", 1);
		ll = new LatLng(latitude, longitude);
		addMapMark(ll);
		coder = GeoCoder.newInstance();
		coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result != null) {
					myLocation.setText(result.getAddress());
					// 地址输入框赋初始值

					if (isFirstLoc2) {
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
						
						// 第一次自动加载服务网点
						submit();
						

					} else {
						// 关闭菊花
						util.dismiss();
					}
					// //关闭菊花
					// util.dismiss();
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
		mBaiduMap.setOnMarkerClickListener(this);

		LocationMode mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
				mCurrentMode, true, BitmapDescriptorFactory
						.fromResource(R.drawable.icon_location_courier_map)));
		mBaiduMap.setMaxAndMinZoomLevel(10, 18);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
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

		jumpSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SpfsUtil.loadPhone().equals("")) {
					Intent intent1 = new Intent(BaiduMapActivity.this,
							LoginActivity.class);
					startActivity(intent1);
					Toast.makeText(BaiduMapActivity.this, "请先登录", 1).show();
				} else {
					Intent intent = new Intent(BaiduMapActivity.this,
							SendActivity.class);
					startActivity(intent);
				}
			}
		});

	}

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Log.e("ajh", "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Log.e("ajh", "网络出错");
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计
		MobclickAgent.onResume(this);
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
		if ("messageCenter".equals(activity)) {
			handler.postDelayed(runnable, 5000);
		} else if ("signMessage".equals(activity)) {
			handler.postDelayed(runnable, 5000);
		}

		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		coder.destroy();
		mMapView.onDestroy();

	}

	private String strpro = null;
	private String strcity = null;
	private String strcounty = null;
	// 经纬度
	Double lat = null;
	Double lon = null;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_2:
			ServiceRangeSelectActivity.start(
					ServiceRangeSelectActivity.QUERY_TYPE_ADDRESS,
					City.TYPE_COUNTY, -1, false, this);
			break;
		case R.id.layout_4:
			//如果有网络，开启菊花
			if(!DeviceUtil.isNetworkAvailable(mContext)){
				ToastUtil.show(mContext, "没有网络，请检查网络是否连接！");
			}else{
				
				//省市县赋值
				strpro = ((City)provinceSpinner.getSelectedItem()).getName();
				strcity = ((City)citySpinner.getSelectedItem()).getName();
				if(strcity.contains("北京")|| strcity.contains("重庆") || strcity.contains("上海")|| strcity.contains("天津")){
					strcity.substring(0, 2);
				}
				strcounty = ((City)countySpinner.getSelectedItem()).getName();
				
				submit();
			}
			
			break;
		case R.id.btn_fullscreen:
			if (above.getVisibility() == View.VISIBLE) {
				above.setVisibility(View.GONE);
				btn.setText("取消全屏");
			} else {
				above.setVisibility(View.VISIBLE);
				btn.setText("切换全屏");
			}
			break;
		default:
			break;
		}
	}

	private int intPro = 0;
	private int intCity = 0;
	private int intCounty = 0;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ServiceRangeSelectActivity.REQUEST_CODE_GET_ADRRESS) {
			if (resultCode == RESULT_OK) {
				City pro = (City) data
						.getSerializableExtra(ServiceRangeSelectActivity.CITY_PRO);
				City city = (City) data
						.getSerializableExtra(ServiceRangeSelectActivity.CITY_CITY);
				City county = (City) data
						.getSerializableExtra(ServiceRangeSelectActivity.CITY_COUNTY);
//				City pro = (City) provinceSpinner.getSelectedItem();
//				City city = (City) citySpinner.getSelectedItem();
//				City county = (City) countySpinner.getSelectedItem();
				
				
				// ToastUtil.show(this, "数据：" + pro + ":" + county + ":" +
				// city);
				String str = "";
				if (pro != null) {
					Log.e("ajh", "pro.getName(): " + pro.getName());
					str += pro.getName() + "\t";
					strpro = pro.getName();
					intPro = pro.getCode();
				}
				if (city != null) {
					Log.e("ajh", "city.getName(): " + city.getName());
					str += city.getName() + "\t";
					strcity = city.getName();
					intCity = city.getCode();
				}
				if (county != null) {
					Log.e("ajh", "county.getName(): " + county.getName());
					str += county.getName();
					strcounty = county.getName();
					intCounty = county.getCode();
				}

				text.setText(str);
			} else {
				// ToastUtil.show(this, "取消了");
			}
		}
	}

	private void submit() {
		if(type == TYPE_SERVICE_POINT){
			//统计查询网点,附近邮递员次数
			MobclickAgent.onEvent(mContext,"serverPoint");
		}else if(type == TYPE_CARRIER){
			//统计发件次数
			MobclickAgent.onEvent(mContext,"nearlyCarrier");
		}
//		strpro = ((City)provinceSpinner.getSelectedItem()).getName();
//		strcity = ((City)citySpinner.getSelectedItem()).getName();
//		if(strcity.contains("北京")|| strcity.contains("重庆") || strcity.contains("上海")|| strcity.contains("天津")){
//			strcity.substring(0, 2);
//		}
//		strcounty = ((City)countySpinner.getSelectedItem()).getName();
		
		
//		// 收起联动
//		llAddress.setVisibility(View.GONE);

		HashMap<String, Object> json = new HashMap<String, Object>();
		String url = null;
		if (strpro == null || strcity == null || strcounty == null) {
			util.dismiss();
			return;
		}
		util.show();
		
		// String params = null;
		if (type == TYPE_CARRIER) {
			url = UrlUtils.URL_QUERY_CARRIER;
			if (!TextUtils.isEmpty(strpro)) {
				json.put("PROV", strpro);
			}
			if (!TextUtils.isEmpty(strcity)) {
				json.put("CITY", strcity);
			}
			if (!TextUtils.isEmpty(strcounty)) {
				json.put("COUNTY", strcounty);
			}
			// JSONObject jsonObject = new JSONObject(json);
			// params = jsonObject.toJSONString();
		} else {
			url = UrlUtils.URL_QUERY_SERVICE_POINT;
			if (!TextUtils.isEmpty(strpro)) {
				json.put("provName", strpro);
			}
			if (!TextUtils.isEmpty(strcity)) {
				json.put("cityName", strcity + "");
			}
			if (!TextUtils.isEmpty(strcounty)) {
				json.put("countyName", strcounty + "");
			}
			json.put("type", "0");
			// params = ParamsUtil.getUrlParamsByMap(json);
		}

		if (json.size() > 0) {
			System.out.println("params: " + json.toString());
			String params = ParamsUtil.getUrlParamsByMap(json);
			if (type == TYPE_SERVICE_POINT) {
				MyRequest2<ServicePointResp> req = new MyRequest2<ServicePointResp>(
						Method.POST, ServicePointResp.class, url,
						new Listener<ServicePointResp>() {

							@Override
							public void onResponse(ServicePointResp arg0) {
								if(!isFirstLoc){
									mBaiduMap.clear();
								}
								
								Log.e("msg", JSONObject.toJSONString(arg0));
								if (((BaseActivityForRequest) mContext).stayThisPage) {
									// objectToCarrier(arg0);
									if ("1".equals(arg0.getResult())) {
										// 让地图能加载自己的位置
										isFirstLoc = true;
										// 不再初始化城市
										isFirstLoc2 = false;

										if (arg0.getBody().getSuccess() != null) {
											servicePoints = arg0.getBody()
													.getSuccess();
											mBaiduMap.clear();
											util.dismiss();
										}
									}
									new Thread() {
										@Override
										public void run() {
											int count = 0;
											if(((BaseActivityForRequest) mContext).stayThisPage && servicePoints.size() < 1){
												ToastUtil.show(mContext, "该地区没有服务网点！");
											}
											for (ServicePoint servicePoint : servicePoints) {
												if (count++ >= 300) {// 最多显示所在区域300个
													return;
												}
												System.out.println("servicePoint: "
														+ servicePoint
																.getName());
												LatLng ll = new LatLng(
														servicePoint.getLat(),
														servicePoint.getLon());
												addMapMark(ll);
												if (servicePoints
														.indexOf(servicePoint) == servicePoints
														.size() - 1) {
													// 显示最后一个
													// setLocation(ll);
													util.dismiss();
												}
											}
										}
									}.start();
									util.dismiss();
								}
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
//								//网络未超时
//								flag1 = true;
								if (((BaseActivityForRequest) mContext).stayThisPage) {
									Toast.makeText(BaiduMapActivity.this,
									"网络异常！", Toast.LENGTH_LONG).show();
									// Log.e("msg", arg0.getMessage());
									util.dismiss();
								}
							}
						}, params);
				App.getQueue().add(req);
			} else {
				MyRequest2<Object> req = new MyRequest2<Object>(Method.POST,
						Object.class, url, new Listener<Object>() {

							@Override
							public void onResponse(Object arg0) {
								//网络未超时
//								flag1 = true;
								if (((BaseActivityForRequest) mContext).stayThisPage) {
									// 让地图能加载自己的位置
									isFirstLoc = true;
									// 不再初始化城市
									isFirstLoc2 = false;
									objectToCarrier(arg0);
									util.dismiss();
								}
							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError arg0) {
								//网络未超时
//								flag1 = true;
								if (((BaseActivityForRequest) mContext).stayThisPage) {
									Toast.makeText(BaiduMapActivity.this,
											"数据加载失败", Toast.LENGTH_LONG).show();
									// Log.e("msg", arg0.getMessage());
									util.dismiss();
								}
							}
						}, params);
				App.getQueue().add(req);
			}
		} else {
			GsonRequest grp = new GsonRequest<Object>(Method.GET, url, null,
					new Listener<Object>() {

						@Override
						public void onResponse(Object arg0) {
							if (((BaseActivityForRequest) mContext).stayThisPage) {
								objectToCarrier(arg0);
								util.dismiss();
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							if (((BaseActivityForRequest) mContext).stayThisPage) {
								Toast.makeText(BaiduMapActivity.this, "数据加载失败",
										Toast.LENGTH_LONG).show();
								// TODO Auto-generated method stub
								util.dismiss();
							}
						}
					}, Object.class);
			App.getQueue().add(grp);

		}
	}

	public void objectToCarrier(Object arg0) {
		if (((BaseActivityForRequest) mContext).stayThisPage) {

			if (arg0 != null) {
				if (!"[]".equals(arg0.toString())) {
					if (null != mBaiduMap) {
						mBaiduMap.clear();
					}
				}
			} else {
				// 关闭菊花
				util.dismiss();
				ToastUtil.show(this, "查询失败");
				return;
			}

			System.out.println(arg0);
			if (TextUtils.isEmpty(arg0 + "")) {
				util.dismiss();
				return;
			}
			if (type == TYPE_SERVICE_POINT) {
				servicePoints = getServices(arg0 + "");
				if (servicePoints != null) {
					new Thread() {
						@Override
						public void run() {
							int count = 0;
							
							if(((BaseActivityForRequest) mContext).stayThisPage && servicePoints.size() < 1){
								ToastUtil.show(mContext, "该地区没有服务网点！");
							}
							for (ServicePoint servicePoint : servicePoints) {
								if (count++ >= 300) {// 最多显示所在区域300个
									return;
								}
								System.out.println("servicePoint: "
										+ servicePoint.getName());
								LatLng ll = new LatLng(
										servicePoint.getLatitude(),
										servicePoint.getLongitude());
								addMapMark(ll);
								if (servicePoints.indexOf(servicePoint) == servicePoints
										.size() - 1) {
									// 显示最后一个
									// setLocation(ll);
								}
							}
						}
					}.start();
					util.dismiss();
					return;
				}
			} else {
				carriers = getCarrtier(arg0 + "");
				LogUtil.print("所有快递员信息：" + JSONObject.toJSONString(carriers));
				if (carriers != null) {
					new Thread() {
						@Override
						public void run() {
							int count = 0;
							for (Carrier carrier : carriers) {
								if (count++ >= 300) {// 最多显示所在区域300个
									return;
								}
								System.out.println("servicePoint: "
										+ carrier.getPeople());
								LatLng ll = new LatLng(carrier.getLatitude(),
										carrier.getLongitude());
								addMapMark(ll);
								if (carriers.indexOf(carrier) == 0) {
									// 显示最后一个
									setLocation(ll);
								}
							}
						}

					}.start();
					util.dismiss();
					;
					return;
				}

			}
			if(((BaseActivityForRequest) mContext).stayThisPage){
				ToastUtil.show(mContext, "该地区没有邮递员！");
			}
//			ToastUtil.show(BaiduMapActivity.this, "没有数据");
			util.dismiss();
		}
	}

	public List<ServicePoint> getServices(String str) {
		
		List<ServicePoint> list = new ArrayList<ServicePoint>();
		if (TextUtils.isEmpty(str)) {
			return null;
		}
		if (!str.contains("[[") || !str.contains("]]")) {
			return null;
		}
		// 去掉头尾框
		str = str.replace("[[", "");
		str = str.replace("]]", "");
		str = str.replace(" ", "");
		if (!str.contains("],[")) {
			encryptServicePoint(list, str);
		} else {
			String[] items = str.split("\\],\\[");
			for (String item : items) {
				encryptServicePoint(list, item);
			}
		}
		return list;
	}

	public List<Carrier> getCarrtier(String str) {
		
		List<Carrier> list = new ArrayList<Carrier>();
		if (TextUtils.isEmpty(str)) {
			return null;
		}
		if (!str.contains("[[") || !str.contains("]]")) {
			return null;
		}
		// 去掉头尾框
		str = str.replace("[[", "");
		str = str.replace("]]", "");
		str = str.replace(" ", "");
		if (!str.contains("],[")) {
			encryptCarrier(list, str);
		} else {
			String[] items = str.split("\\],\\[");
			for (String item : items) {
				encryptCarrier(list, item);
			}
		}
		return list;
	}

	public void encryptServicePoint(List<ServicePoint> list, String item) {
		/**
		 * 0 经度 1 纬度 2 服务网点名称 3 联系人 4 电话 5 开始营业时间 6 关门时间 7 详细地址
		 * */
		String temp = item.substring(1, item.length() - 1);// 去掉头尾的框
		String[] content = temp.split(",");

		ServicePoint servicePoint = new ServicePoint();
		for (int i = 0; i < content.length; i++) {
			if (i == 0)
				try {
					servicePoint.setLongitude(Double.parseDouble(content[0]));
				} catch (Exception e) {
					// TODO: handle exception
				}
			if (i == 1)
				try {
					servicePoint.setLatitude(Double.parseDouble(content[1]));
				} catch (Exception e) {
					// TODO: handle exception
				}
			if (i == 2)
				servicePoint.setName(content[2]);
			if (i == 3)
				servicePoint.setPeople(content[3]);
			if (i == 4)
				servicePoint.setMobile(content[4]);
			if (i == 5)
				servicePoint.setStartTime(content[5]);
			if (i == 6)
				servicePoint.setEndTime(content[6]);
			if (i == 7)
				servicePoint.setADDRESS(content[7]);
		}
		list.add(servicePoint);
	}

	public void encryptCarrier(List<Carrier> list, String item) {
		/*
		 * 0 经度 1 纬度 2 联系人 3 电话 4 地址 5 IM_PUSH_ID（邮递员的IM推送用户id）6 EMPLOYEE_NO
		 * 邮递员工号 7 code机构号 8 SID（如果头像为空则不查询此项）标识
		 */
		// String temp = item.substring(1, item.length() - 1);// 去掉头尾的框
		String[] content = item.split(",");
		Carrier carrier = new Carrier();
		for (int i = 0; i < content.length; i++) {
			if (i == 0)
				try {
					carrier.setLongitude(Double.parseDouble(content[0]));
				} catch (Exception e) {
					// TODO: handle exception
				}
			if (i == 1)
				try {
					carrier.setLatitude(Double.parseDouble(content[1]));
				} catch (Exception e) {
					// TODO: handle exception
				}
			if (i == 2)
				carrier.setPeople(content[2]);
			if (i == 3)
				carrier.setMobile(content[3]);
			if (i == 4)
				carrier.setAddress(content[4]);
			if (i == 5)
				carrier.setClientId(content[5]);
			if (i == 6)
				carrier.setEmployeeNo(content[6]);
			if (i == 7) {
				carrier.setCode(content[7]);
			}
			if (i == 8) {
				carrier.setSID(content[8]);
			}
		}
		list.add(carrier);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		LatLng l = marker.getPosition();
		if (type == TYPE_SERVICE_POINT) {
			if (servicePoints != null) {
				for (ServicePoint servicePoint : servicePoints) {
					String mLat = servicePoint.getLat() + "";
					if (mLat.length() > 8) {
						mLat = mLat.substring(0, 9);
					}
					// mLat = mLat.substring(0, mLat.length());
					String mLon = servicePoint.getLon() + "";
					if (mLon.length() > 9) {
						mLon = mLon.substring(0, 10);
					}
					if (Double.parseDouble(mLat) == l.latitude
							&& Double.parseDouble(mLon) == l.longitude) {
						LogUtil.print("服务点信息："
								+ JSONObject.toJSONString(servicePoint));
						DialogUtils.getServicePointDialog(servicePoint, this);
						return true;
					}
				}
			}
		} else {
			if (carriers != null) {
				for (Carrier carrier : carriers) {
					if (carrier.getLatitude() == l.latitude
							&& carrier.getLongitude() == l.longitude) {
						LogUtil.print("快递员信息："
								+ JSONObject.toJSONString(carrier));
						DialogUtils.getCarrierDialog(carrier, this, locations);
						return true;
					}
				}
			}
			if ("messageCenter".equals(activity)) {
				Carrier car = new Carrier();
				if (mDeliveryMessage.getSid() == null
						|| "".equals(mDeliveryMessage.getSid())) {
					car.setSID("null");
				} else {
					car.setSID(mDeliveryMessage.getSid());
				}
				car.setClientId(mDeliveryMessage.getChannelId());
				car.setMobile(phoneNum);
				car.setPeople(mDeliveryMessage.getPeople());
				car.setCode(mDeliveryMessage.getCode());
				car.setEmployeeNo(mDeliveryMessage.getEmployeeNo());
				car.setLatitude(mDeliveryMessage.getLatitude());
				car.setLongitude(mDeliveryMessage.getLongitude());
				DialogUtils.getCarrierDialog(car, this, ll);
			}
			if ("signMessage".equals(activity)) {
				Carrier car = new Carrier();
				if (mSendNoticeBean.getSid() == null
						|| mSendNoticeBean.getSid().equals("")) {
					car.setSID("null");
				} else {
					car.setSID(mSendNoticeBean.getSid());
				}
				car.setClientId(mSendNoticeBean.getChannelId());
				car.setMobile(phoneNum);
				car.setPeople(mSendNoticeBean.getPeople());
				car.setCode(mSendNoticeBean.getCode());
				car.setEmployeeNo(mSendNoticeBean.getEmployeeNo());
				car.setLatitude(mSendNoticeBean.getLatitude());
				car.setLongitude(mSendNoticeBean.getLongitude());
				DialogUtils.getCarrierDialog(car, this, ll);
			}

		}
		return false;
	}

	@Override
	protected void onStop() {
		super.onStop();
		handler.removeCallbacks(runnable);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// if("messageCenter".equals(activity)){
		// handler.postDelayed(runnable, 5000);
		// }
	}

	
	public void setInitSpinner(){
		 provinceSpinner = (Spinner)findViewById(R.id.spin_province);
         citySpinner = (Spinner)findViewById(R.id.spin_city);
         countySpinner = (Spinner)findViewById(R.id.spin_county);
         
         rsu2 = new RangeSelectUtil2(mContext, provinceSpinner, citySpinner, countySpinner);
         
         rsu2.initCityList();
         rsu2.loadData();
	}

}

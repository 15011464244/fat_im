package com.ems.express.ui;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
import butterknife.ButterKnife;
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
import com.ems.express.ui.mail.MailOrderListActivity;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DeviceUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.RangeSelectUtil2;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class TimeQueryActivity extends BaseActivityForRequest implements OnClickListener, OnDateChangedListener,
		OnTimeChangedListener {
	private RadioGroup group = null;
	private TextView result = null;
	private TextView resultHeader = null;
	private TextView send, recv;
	private TextView item_des;
	private View item_des_bottom_line;
	private View layoutResult = null;
	private ImageView mIvNext5;

	/**
	 * added by sweetvvck
	 */
	@InjectView(R.id.tv_selected_date)
	TextView mSelecteDate;
	@InjectView(R.id.arrow_date)
	ImageView mDateArrowView;
	@InjectView(R.id.date_selection)
	View mDateSelectionView;
	@InjectView(R.id.tv_date)
	TextView mTvDate;
	@InjectView(R.id.tv_time)
	TextView mTvTime;
	@InjectView(R.id.tv_date_line)
	TextView mTvDateLine;
	@InjectView(R.id.tv_time_line)
	TextView mTvTimeLine;
	@InjectView(R.id.date_picker)
	DatePicker mDatePicker;
	@InjectView(R.id.time_picker)
	TimePicker mTimePicker;
	@InjectView(R.id.tex_title)
	TextView mTitle;
	
	private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    
    private Spinner provinceSpinner2 = null;  //省级（省、直辖市）
    private Spinner citySpinner2 = null;     //地级市
    private Spinner countySpinner2 = null;    //县级（区、县、县级市）
	
	RangeSelectUtil2 rsu1;
	RangeSelectUtil2 rsu2;
	
	private Context mContext;
	
	private Calendar now;
	private String mDate;
	private String mTime;
	
	private TextView jumpSend;
	private AnimationUtil util;
	private int type;
	
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
				if (((BaseActivityForRequest) mContext).stayThisPage) {
//					if (true) {
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
		setContentView(R.layout.activity_time);
		ButterKnife.inject(this);
		mTitle.setText("时效查询");
		setHeadTitle("时效查询");
		group = (RadioGroup) findViewById(R.id.type);
		result = (TextView) findViewById(R.id.item_result);
		resultHeader = (TextView) findViewById(R.id.item_);
		layoutResult = findViewById(R.id.layout_result);
		jumpSend =(TextView)findViewById(R.id.bt_jump_send);
		findViewById(R.id.layout_1).setOnClickListener(this);
		findViewById(R.id.layout_2).setOnClickListener(this);
		findViewById(R.id.layout_4).setOnClickListener(this);
		findViewById(R.id.layout_5).setOnClickListener(this);
		
		mContext = this;
		
		initSpinner();

		 util =new AnimationUtil(this, R.style.dialog_animation);
		 
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
								rsu1.freshByName(strpro, strcity, strcounty);
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
		/**
		 * added by sweetvvck
		 */
		findViewById(R.id.select_date).setOnClickListener(this);
		mTvDate.setOnClickListener(this);
		mTvTime.setOnClickListener(this);
		now = Calendar.getInstance();
		mDate = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);
		mTime = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
		mSelecteDate.setText(mDate + " " + mTime);
		mDatePicker.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), this);
		mTimePicker.setOnTimeChangedListener(this);

		send = (TextView) findViewById(R.id.item_value);
		recv = (TextView) findViewById(R.id.item_value_2);
		item_des = (TextView) findViewById(R.id.item_des);
		item_des_bottom_line = (View) findViewById(R.id.item_des_bottom_line);
		mIvNext5 = (ImageView) findViewById(R.id.next_5);
		//启动默认隐藏时效说明
		item_des.setVisibility(View.GONE);
		mIvNext5.setImageResource(R.drawable.icon_down);
		item_des_bottom_line.setVisibility(View.GONE);
		
		jumpSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SpfsUtil.loadPhone().equals("")){
					Intent intent1 = new Intent(TimeQueryActivity.this,LoginActivity.class);
					startActivity(intent1);
					Toast.makeText(TimeQueryActivity.this,"请先登录", 1).show();
				}else{
					Intent intent = new Intent(TimeQueryActivity.this, SendActivity.class);
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

	private void check() {
		MobclickAgent.onEvent(this, "timeQuery");
		//获取地址参数
		getAddress();
		//隐藏时效说明
		item_des.setVisibility(View.GONE);
		mIvNext5.setImageResource(R.drawable.icon_down);
		item_des_bottom_line.setVisibility(View.GONE);
		
		if(sendAddress == null|| sendAddress.isEmpty()){
			ToastUtil.show(TimeQueryActivity.this, "请选择原寄地");
			return;
		}
		if(endAddress == null|| endAddress.isEmpty()){
				ToastUtil.show(TimeQueryActivity.this, "请选择目的地");
				return;
		}
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("queryFlag", type);
		
		String string="";
		try {
			string = new String(sendAddress.getBytes(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		json.put("sendCity", string);
		json.put("recvCity", endAddress);
		json.put("sendDate", mDate + " " + mTime);
		util.show();
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
//		ToastUtil.show(this, params,3000);
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null, UrlUtils.URL_QUERY_TIME,
				new Listener<Object>() {
					@Override
					public void onResponse(Object arg0) {
						if (TextUtils.isEmpty(arg0 + "")) {
							ToastUtil.show(TimeQueryActivity.this, "数据获取失败");
							util.dismiss();
						}  
						String aa = arg0.toString();
						if(aa.contains("traces")){
							util.dismiss();
							return;
						}
						JSONObject object = JSONObject.parseObject(aa);
						if (object.containsKey("model")) {
							JSONObject jsonObject = object.getJSONObject("model");
							if (jsonObject.containsKey("map")) {
								String days = jsonObject.getJSONObject("map").getString("limit");
								String endDate = jsonObject.getJSONObject("map").getString("endDate");
								
								resultHeader.setText(days);
								result.setText(endDate);
								
								layoutResult.setVisibility(View.VISIBLE);
								util.dismiss();
							}else {
								resultHeader.setText("暂无此类型");
								result.setText("");
								ToastUtil.show(TimeQueryActivity.this, "数据获取失败");
								util.dismiss();
								
							}
						}else {
							resultHeader.setText("暂无此类型");
							result.setText("");
							ToastUtil.show(TimeQueryActivity.this, "数据获取失败");
							util.dismiss();
						}
						
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(TimeQueryActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();
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
				
				 if(pro != null){
				 Log.e("ajh", "pro.getName(): " + pro.getName());
				 //str+= pro.getName()+"\t";
				 }
				if (city != null && !city.isZhixiashi()) {
					Log.e("ajh", "city.getName(): " + city.getName());
					str += city.getName() + "\t";
				}
				if (county != null) {
					Log.e("ajh", "county.getName(): " + county.getName());
					str += county.getName();
				}

				// ItemAdapter itemAdapter = (ItemAdapter) list.getAdapter();
				if (data.getBooleanExtra(ServiceRangeSelectActivity.KEY_IS_SEND_CITY, false)) {
					sendAddress = str;
					send.setText(str);
				} else {
					endAddress = str;
					recv.setText(str);
				}
			} else {
//				ToastUtil.show(this, "取消了");
			}
		}
	}

	@Override
	public void onClick(View v) {
		int type = -1;
		if (group.getCheckedRadioButtonId() == R.id.type_1) {
			type = ServiceRangeSelectActivity.EXPRESS_JIANJI;
		} else {
			type = ServiceRangeSelectActivity.EXPRESS_JIANJI;
		}
		
		if (group.getCheckedRadioButtonId() == R.id.type_1) {
			this.type = 2;
		} else {
			this.type = 1;
		}
		
		switch (v.getId()) {
		case R.id.layout_1:
			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_TIME, City.TYPE_COUNTY, type, true,
					this);
			break;
		case R.id.layout_2:
			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_TIME, City.TYPE_COUNTY, type, false,
					this);
			break;
		case R.id.layout_4:
			check();
			break;
		case R.id.layout_5:
			if (item_des.getVisibility() == View.VISIBLE) {
				item_des.setVisibility(View.GONE);
				mIvNext5.setImageResource(R.drawable.icon_down);
				item_des_bottom_line.setVisibility(View.GONE);
			} else {
				item_des.setVisibility(View.VISIBLE);
				mIvNext5.setImageResource(R.drawable.icon_up);
				item_des_bottom_line.setVisibility(View.VISIBLE);
			}
			break;

		/**
		 * added by sweetvvck
		 */
		case R.id.select_date:
			toggleDate();
			break;
		case R.id.tv_date:
			mTvDate.setTextColor(getResources().getColor(R.color.orange));
			mTvTime.setTextColor(getResources().getColor(R.color.black));
			mTvDateLine.setBackgroundColor(getResources().getColor(R.color.orange));
			mTvTimeLine.setBackgroundColor(getResources().getColor(R.color.white));
			
			mDatePicker.setVisibility(View.VISIBLE);
			mTimePicker.setVisibility(View.GONE);
			break;
		case R.id.tv_time:
			mTvDate.setTextColor(getResources().getColor(R.color.black));
			mTvTime.setTextColor(getResources().getColor(R.color.orange));
			mTvTimeLine.setBackgroundColor(getResources().getColor(R.color.orange));
			mTvDateLine.setBackgroundColor(getResources().getColor(R.color.white));
			
			mDatePicker.setVisibility(View.GONE);
			mTimePicker.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void toggleDate() {
		boolean selectionShown = mDateSelectionView.getVisibility() == View.VISIBLE;
		mDateArrowView.setImageResource(selectionShown ? R.drawable.icon_down : R.drawable.icon_up);
		mDateSelectionView.setVisibility(selectionShown ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		mDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
		mSelecteDate.setText(mDate + " " + mTime);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		mTime = hourOfDay + ":" + minute + ":00";
		mSelecteDate.setText(mDate + " " + mTime);
	}
	//获取收送地址
	public void getAddress(){
		City prov = ((City) provinceSpinner.getSelectedItem());
		City city =((City) citySpinner.getSelectedItem());
		City county = ((City) countySpinner.getSelectedItem());
		
		if(11 == prov.getCode() || 
		12 == prov.getCode() ||
		31 == prov.getCode() ||
		50 == prov.getCode()){
			sendAddress =  city.getName().substring(0,city.getName().length()-1);
		}else{
			sendAddress =  city.getName().substring(0,city.getName().length()-1)+"\t"+county.getName();
		}
		
		City prov2 = ((City) provinceSpinner2.getSelectedItem());
		City city2 =((City) citySpinner2.getSelectedItem());
		City county2 = ((City) countySpinner2.getSelectedItem());
		
		if(11 == prov2.getCode() || 
		12 == prov2.getCode() ||
		31 == prov2.getCode() ||
		50 == prov2.getCode()){
			endAddress =  city2.getName().substring(0,city2.getName().length()-1);
		}else{
			endAddress =  city2.getName().substring(0,city2.getName().length()-1)+"\t"+county2.getName();
		}
		
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

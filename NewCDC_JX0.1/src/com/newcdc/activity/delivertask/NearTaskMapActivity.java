package com.newcdc.activity.delivertask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.application.MainActivity;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Global;
import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * 地图页面 lion
 */
public class NearTaskMapActivity extends BaseActivity {
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	// 定位相关
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	boolean isFirstLoc = true;// 是否首次定位
	private long mLastTime = 0;
	private DeliverDao db = null;
	private BitmapDescriptor bdA = null;
	private Marker marker;
	private Dialog dialog = null;
	private TextView map_ph = null;
	private TextView map_mailnumb = null;
	private TextView map_address = null;
	private Button map_call = null;
	private Button map_sms = null;
	private TextView map_name = null;
	private Button neartask_back = null;
	private String phnum = "";
	private UserInfoUtils user = null;
	private int _id = 0;
	private String distance;
	private DeliverDaoFactory daoFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		inits();
		daoFactory = DeliverDaoFactory.getInstance();
		db = daoFactory.getDeliverDao(this);
		bdA = BitmapDescriptorFactory.fromResource(R.drawable.gcoding);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//卫星图
		mCurrentMode = LocationMode.FOLLOWING;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, null));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 初始化
	 */
	private void inits() {
		// TODO Auto-generated method stub
		user = new UserInfoUtils(getApplicationContext());
		SharePreferenceUtilDaoFactory shareDao = new SharePreferenceUtilDaoFactory();
		distance = shareDao.getDW_distance();
		neartask_back = (Button) findViewById(R.id.neartask_back);
		neartask_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
				finish();
			}
		});
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			if (isFirstLoc) {
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(90).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus mMapStatus = new MapStatus.Builder().target(ll)
						.zoom(16).build();
				MapStatusUpdate u = MapStatusUpdateFactory
						.newMapStatus(mMapStatus);
				mBaiduMap.animateMapStatus(u);
			}
			if (System.currentTimeMillis() - mLastTime > (20 * 1000)) {
				clearOverlay();
				addMarker();
				mLastTime = System.currentTimeMillis();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public static List<MessageInfoBean> mapList = new ArrayList<MessageInfoBean>();

	/**
	 * 添加marker
	 */
	private void addMarker() {
		// TODO Auto-generated method stub

		// final List<MessageInfoBean> list = db
		// .queryLocalNotifationMail(NearTaskMapActivity.this);
		// MessageInfoBean mib = new MessageInfoBean();
		// mib.setRcver_addr("北京市海学区基本原理基要要要");
		// mib.setRcver_name("李国生");
		// mib.setMail_num("ES12312312cs");
		// mib.setRcver_contact_phone1("1008611");
		// mib.setLatlng("39.903339,116.324058");
		// list.add(mib);
		// Log.e("list.size", list.size() + "");
		for (int i = 0; i < mapList.size(); i++) {
			Log.e("lng", mapList.get(i).getLatlng() + "|"
					+ mapList.get(i).get_id());
			if (mapList.get(i).getLatlng() != null
					&& !mapList.get(i).getLatlng().equals("null")
					&& !mapList.get(i).getLatlng().equals("")
					&& !mapList.get(i).getLatlng().equals(",")) {
				String[] lng = mapList.get(i).getLatlng().split(",");
				Double lat = Double.parseDouble(lng[0]);
				Double lon = Double.parseDouble(lng[1]);
				LatLng llA = new LatLng(lat, lon);
				OverlayOptions ooA = new MarkerOptions().position(llA)
						.icon(bdA).title(i + "").draggable(false);
				marker = (Marker) (mBaiduMap.addOverlay(ooA));
			}
		}

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				MessageInfoBean mib = mapList.get(Integer.parseInt(arg0
						.getTitle()));
				InfoWindow(mib);
				return true;
			}
		});
	}

	private void InfoWindow(MessageInfoBean mib) {
		if (dialog == null) {
			dialog = new Dialog(NearTaskMapActivity.this, R.style.dialogss);
		}
		View view = LayoutInflater.from(NearTaskMapActivity.this).inflate(
				R.layout.dialog_mapinfo, null);
		dialog.setContentView(view, new LayoutParams(width * 17 / 18,
				LayoutParams.WRAP_CONTENT));
		// dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		initInfoWindow(view, mib);
	}

	private void initInfoWindow(View view, final MessageInfoBean mib) {
		// TODO Auto-generated method stub
		map_name = (TextView) view.findViewById(R.id.map_name);
		map_ph = (TextView) view.findViewById(R.id.map_ph);
		map_mailnumb = (TextView) view.findViewById(R.id.map_mailnumb);
		map_address = (TextView) view.findViewById(R.id.map_address);
		map_call = (Button) view.findViewById(R.id.map_call);
		map_sms = (Button) view.findViewById(R.id.map_sms);
		map_name.setText(mib.getRcver_name());
		map_ph.setText(mib.getRcver_contact_phone1());
		map_mailnumb.setText(mib.getMail_num());
		map_address.setText(mib.getRcver_addr());
		map_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPhoneNum(mib.getRcver_contact_phone1())) {
					phnum = mib.getRcver_contact_phone1();
					db.addCallTime(mib.get_id());
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"
							+ mib.getRcver_contact_phone1()));
					startActivity(intent);
				} else {
					dialog.dismiss();
					showToast("请返回上一页补录信息");
				}
			}
		});
		map_sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				_id = mib.get_id();
				if (isMobileNum(mib.getRcver_contact_phone1())) {
					sendmessage(mib.getRcver_contact_phone1());
				} else {
					showToast("非手机号");
				}
				// sendmessage("18695709163");
			}
		});
	}

	/**
	 * 清除所有Overlay
	 * 
	 */
	public void clearOverlay() {
		mBaiduMap.clear();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		Utils.startIntentService(NearTaskMapActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (dialog != null) {
			dialog.dismiss();
		}
		// bdA.recycle();
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	// 发送短息线程
	public void sendmessage(final String mobile) {
		new AsyncTask<Object, Void, String>() {

			@Override
			protected String doInBackground(Object... params) {
				// UserDao userDao = daoFactory
				// .getUserDao(NearTaskMapActivity.this);
				UserInfoUtils userInfoUtils = new UserInfoUtils(
						getApplicationContext());
				List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
				paramObject.add(new BasicNameValuePair("sms_type", "t"));
				paramObject.add(new BasicNameValuePair("receiver_mobiles",
						mobile));
				paramObject.add(new BasicNameValuePair("longitude",
						BaiduGpsContants.getInstance().getLng()));
				paramObject.add(new BasicNameValuePair("latitude",
						BaiduGpsContants.getInstance().getLat()));
				paramObject.add(new BasicNameValuePair("address",
						BaiduGpsContants.getInstance().getAddressStr()));
				paramObject.add(new BasicNameValuePair("dlvorgcode",
						userInfoUtils.getUserDelvorgCode()));
				paramObject.add(new BasicNameValuePair("username",
						userInfoUtils.getUserName()));
				String result = com.newcdc.tools.NetHelper.doPost(
						Global.SENDMESSAGE, paramObject);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject jsonObj;
					jsonObj = new JSONObject(result);
					if ("1".equals(jsonObj.getString("result"))) {
						String message = JsonUtils.parseMsgJson(jsonObj);
						showToast(message);
						db.addMsgTime(_id);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onPostExecute(result);
			}

			@Override
			protected void onPreExecute() {

				super.onPreExecute();
			}
		}.execute();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (isPhoneNum(phnum)) {
			// UserInfoBean uib = user.getUserInfoBean();
			// showToast(uib.getLub().toString());
			// // 通话结束之后向服务器发送通话信息
			ContentResolver resolver = MainActivity.resolver;
			Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, null,
					"number=?", new String[] { phnum }, null);
			if (cursor.moveToLast()) {
				String duration = cursor.getString(cursor
						.getColumnIndex("duration"));
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("dlvorgcode", user
						.getUserDelvorgCode()));
				params.add(new BasicNameValuePair("username", user
						.getUserName()));
				params.add(new BasicNameValuePair("type", "2"));
				params.add(new BasicNameValuePair("mobile", phnum));
				params.add(new BasicNameValuePair("message", ""));
				params.add(new BasicNameValuePair("longitude", BaiduGpsContants
						.getInstance().getLng()));
				params.add(new BasicNameValuePair("latitude", BaiduGpsContants
						.getInstance().getLat()));
				params.add(new BasicNameValuePair("address", BaiduGpsContants
						.getInstance().getAddressStr()));
				params.add(new BasicNameValuePair("record_time", duration));
				if (Integer.parseInt(duration) == 0) {
					params.add(new BasicNameValuePair("status", "0"));
				} else {
					params.add(new BasicNameValuePair("status", "1"));
				}
				final List<NameValuePair> params2 = params;
				try {
					new Thread() {
						public void run() {
							try {
								NetHelper.doPost(Global.CALLINFO_URL, params2);
							} catch (Exception e) {
								e.printStackTrace();
							}
						};
					}.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}

package com.newcdc.activity.clcttask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.model.GatherTableBean;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Global;
import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * 
 * 
 * @author lion 2015-01-12 揽收地图
 * 
 */
public class CopyOfMapForClctTask extends BaseActivity implements
		OnClickListener {
	private Button neartask_back = null;

	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	// 定位相关
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	boolean isFirstLoc = true;// 是否首次定位
	private long mLastTime = 0;
	private BitmapDescriptor bdA = null;
	private Marker marker;
	private Dialog dialog = null;
	private TextView map_ph = null;
	private TextView map_mailnumb = null;
	private TextView map_address = null;
	private Button map_call = null;
	private Button map_sms = null;
	private TextView map_name = null;
	private String phnum = "";
	private UserInfoUtils user = null;
	private int _id = 0;
	private String distance;
	private Gather_MsgDao db = null;
	private String dlvcode = "";
	private String username = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		user = new UserInfoUtils(CopyOfMapForClctTask.this);
		distance = SharePreferenceUtilDaoFactory.getInstance(
				CopyOfMapForClctTask.this).getDW_distance();// 用户设置的定位范围
		db = DeliverDaoFactory.getInstance().getGather_msgdao(
				CopyOfMapForClctTask.this);
		dlvcode = user.getUserDelvorgCode();
		username = user.getUserName();
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
		initView();
		addLister();

	}

	private void initView() {
		neartask_back = (Button) findViewById(R.id.neartask_back);
	}

	private void addLister() {
		neartask_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.neartask_back:
			CopyOfMapForClctTask.this.finish();
			break;
		}
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
			if (System.currentTimeMillis() - mLastTime > (3 * 1000)) {
				clearOverlay();
				addMarker();
				mLastTime = System.currentTimeMillis();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 添加marker
	 */
	private void addMarker() {
		// TODO Auto-generated method stub
		final List<GatherTableBean> list = db.FromDistance_Query(dlvcode,
				username, distance);
		Log.e("list.size", list.size() + "");
		for (int i = 0; i < list.size(); i++) {
			Log.e("lng", list.get(i).getLatlng() + "|" + list.get(i).get_id());
			if (list.get(i).getLatlng() != null
					&& !list.get(i).getLatlng().equals("null")
					&& !list.get(i).getLatlng().equals("")
					&& !list.get(i).getLatlng().equals(",")) {
				String[] lng = list.get(i).getLatlng().split(",");
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
				GatherTableBean mib = list.get(Integer.parseInt(arg0.getTitle()));
				InfoWindow(mib);
				return true;
			}
		});
	}

	private void InfoWindow(GatherTableBean mib) {
		if (dialog == null) {
			dialog = new Dialog(CopyOfMapForClctTask.this, R.style.dialogss);
		}
		View view = LayoutInflater.from(CopyOfMapForClctTask.this).inflate(
				R.layout.dialog_mapinfo, null);
		dialog.setContentView(view, new LayoutParams(width * 17 / 18,
				LayoutParams.WRAP_CONTENT));
		// dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		initInfoWindow(view, mib);
	}

	private void initInfoWindow(View view, final GatherTableBean mib) {
		// TODO Auto-generated method stub
		map_name = (TextView) view.findViewById(R.id.map_name);
		map_ph = (TextView) view.findViewById(R.id.map_ph);
		map_mailnumb = (TextView) view.findViewById(R.id.map_mailnumb);
		map_address = (TextView) view.findViewById(R.id.map_address);
		map_call = (Button) view.findViewById(R.id.map_call);
		map_sms = (Button) view.findViewById(R.id.map_sms);
		map_name.setText(mib.getClientname());
		map_ph.setText(mib.getPhone());
		map_mailnumb.setText(mib.getReservenum());
		map_address.setText(mib.getAddress());
		map_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPhoneNum(mib.getPhone())) {
					phnum = mib.getPhone();
					// db.addCallTime(mib.get_id());
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + mib.getPhone()));
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
				if (isPhoneNum(mib.getPhone())) {
					// sendmessage(mib.getPhone());
				} else {
					showToast("请返回上一页补录信息");
				}
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
		Utils.startIntentService(CopyOfMapForClctTask.this);// 启动投递、揽收异步上传服务
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
				// UserDao userDao = DeliverDaoFactory.getInstance().getUserDao(
				// CopyOfMapForClctTask.this);
				UserInfoUtils userInfoUtils = new UserInfoUtils(
						getApplicationContext());
				List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
				paramObject.add(new BasicNameValuePair("sms_type", "L"));
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
						// db.addMsgTime(_id);
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
		// if (isPhoneNum(phnum)) {
		// // UserInfoBean uib = user.getUserInfoBean();
		// // showToast(uib.getLub().toString());
		// // // 通话结束之后向服务器发送通话信息
		// ContentResolver resolver = MainActivity.resolver;
		// Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, null,
		// "number=?", new String[] { phnum }, null);
		// if (cursor.moveToLast()) {
		// String duration = cursor.getString(cursor
		// .getColumnIndex("duration"));
		// List<NameValuePair> params = new ArrayList<NameValuePair>();
		// final NetHelper netHelper = new NetHelper();
		// netHelper.Create(Global.CALLINFO_URL);
		// params.add(new BasicNameValuePair("dlvorgcode",
		// user.getUserDelvorgCode()));
		// params.add(new BasicNameValuePair("username", user.getUserName()));
		// params.add(new BasicNameValuePair("type", "2"));
		// params.add(new BasicNameValuePair("mobile", phnum));
		// params.add(new BasicNameValuePair("message", ""));
		// params.add(new BasicNameValuePair("longitude", BaiduGpsContants
		// .getInstance().getLng()));
		// params.add(new BasicNameValuePair("latitude", BaiduGpsContants
		// .getInstance().getLat()));
		// params.add(new BasicNameValuePair("address", BaiduGpsContants
		// .getInstance().getAddressStr()));
		// params.add(new BasicNameValuePair("record_time", duration));
		// if (Integer.parseInt(duration) == 0) {
		// params.add(new BasicNameValuePair("status", "0"));
		// } else {
		// params.add(new BasicNameValuePair("status", "1"));
		// }
		// final List<NameValuePair> params2 = params;
		// try {
		// new Thread() {
		// public void run() {
		// JSONObject result;
		// try {
		// result = netHelper.execute(params2);
		// Log.e("电话记录返回结果：", result.toString() + "");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// };
		// }.start();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
	}

}

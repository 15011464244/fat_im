package com.newcdc.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.activity.delivertask.NearTaskMapActivity;
import com.newcdc.activity.delivertask.TaskShowActivity;
import com.newcdc.asynctask.sendMessageTask;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.db.GroupDao;
import com.newcdc.fragment.MainFragment;
import com.newcdc.model.GatherTableBean;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.LocationDistance;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.XFAudio;

/**
 * 定位服务
 * 
 * @author zhangfan
 */
public class LocationService extends Service {
	private LocationClient mLocationClient = null;
	// private BDLocationListener myListener = new MyLocationListener();
	private String locAddr, locLat, locLng, pro, city, district, street,
			streetNumber;
	// private long mExitTime = 0;
	private DeliverDao deliverDao;
	private GroupDao groupDao;
	private Intent addrReceiver;
	private SharePreferenceUtilDaoFactory shareDao;
	private String orgCode, username;// 机构号，工号
	private int user_distance;// 用户自定义范围
	// private String oldAddr;// 旧地址
	private int LOCTIME = 15 * 1000;// 定位间隔时间
	private Gather_MsgDao gatherDao;
	private DeliverDaoFactory daoFactory;
	private Timer timer;// 定时器重启定位
	private String oldLatlng;// 旧的经纬度
	private String oldAddrForTitle;// 即时更新主页标题地址
	public MyLocationListener myListener;// 定位监听器
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				baiduLocation();
			} else if (msg.what == 2) {
				List<GatherTableBean> beans = gatherDao.orderByIsspeech(
						orgCode, username, "1");
				boolean isSpeech = false;
				if (beans.size() > 0) {
					for (int i = 0; i < beans.size(); i++) {
						if (Utils.GetMinute_Ten(beans.get(i).getTaskAllotTime()) >= 10) {
							isSpeech = true;
							LogUtils.i("要播报超时的邮件号是----"
									+ beans.get(i).getReservenum());
							// 播报一次 播报完成之后把播报状态值改成否
							LogUtils.i("----------"
									+ gatherDao.updateIsspeech(beans.get(i)
											.get_id()));
							gatherDao.updateIsspeech(beans.get(i).get_id());
						}
					}
					if (isSpeech) {
						Utils.playVibator(getApplicationContext(), 1000);
						XFAudio audio = new XFAudio(getApplicationContext(),
								"您有未确认的派单信息，请尽快确认");
						audio.startSay();
					}
				}
			}
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public ComponentName startService(Intent service) {
		return super.startService(service);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initData();// 初始化变量
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
			}
		}, 10, 1000 * 30 * 1);
		return START_STICKY;
	}

	private void initData() {
		daoFactory = DeliverDaoFactory.getInstance();
		shareDao = SharePreferenceUtilDaoFactory
				.getInstance(getApplicationContext());
		UserInfoUtils user = new UserInfoUtils(getApplicationContext());
		orgCode = user.getUserDelvorgCode();
		username = user.getUserName();
		deliverDao = daoFactory.getDeliverDao(getApplicationContext());
		groupDao = daoFactory.getGroupDao(getApplicationContext());
		// if (Global.isLan) {
		gatherDao = DeliverDaoFactory.getInstance().getGather_msgdao(
				getApplicationContext());
		// }
		addrReceiver = new Intent(Constant.ACTION_LOCATION);
		user_distance = Integer.parseInt(shareDao.getDW_distance());

	}

	private void baiduLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		if (myListener == null) {
			myListener = new MyLocationListener();
		}
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(LOCTIME);// 设置发起定位请求的间隔时间为15000ms
		option.setIsNeedAddress(true);// 是否需要地址信息
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
		if (mLocationClient.isStarted() && mLocationClient != null) {
			mLocationClient.requestLocation();
		}
	}

	class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(final BDLocation location) {
			if (null != location) {
				locAddr = location.getAddrStr();
				locLat = String.valueOf(location.getLatitude());
				locLng = String.valueOf(location.getLongitude());
				pro = String.valueOf(location.getProvince());
				city = String.valueOf(location.getCity());
				district = String.valueOf(location.getDistrict());
				street = String.valueOf(location.getStreet());
				streetNumber = String.valueOf(location.getStreetNumber());
				BaiduGpsContants.getInstance().setAddressStr(locAddr);
				BaiduGpsContants.getInstance().setLat(locLat);
				BaiduGpsContants.getInstance().setLng(locLng);
				BaiduGpsContants.getInstance().setPro(pro);
				BaiduGpsContants.getInstance().setCity(city);
				BaiduGpsContants.getInstance().setDistrict(district);
				BaiduGpsContants.getInstance().setStreet(street);
				BaiduGpsContants.getInstance().setStreetNumber(streetNumber);
				BaiduGpsContants.getInstance().setTime(
						System.currentTimeMillis());
				if (locAddr != null && !locAddr.equals(oldAddrForTitle)) {
					sendAddrBroadCast(locAddr);// 改变主页title上的地址
					oldAddrForTitle = locAddr;
				}
				UserInfoUtils user = new UserInfoUtils(getApplicationContext());
				String orgCode = user.getUserDelvorgCode();
				if (Utils.stringEmpty(orgCode)) {
					stopSelf();
				} else {
					new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							locationServer();// 对地理信息进行操作
							return null;
						}
					}.execute();
				}
			} else {
				mLocationClient.requestLocation();
				oldAddrForTitle = null;
				ListenNetStateService.restartLocaService = true;
				// baiduLocation();
			}
		}
	};

	/**
	 * 地理信息操作
	 */
	public synchronized void locationServer() {
		String oldAddr = shareDao.getOldAddr();// 获取存储的旧地址
		if (locAddr != null && !locAddr.equals(oldAddr)) {// 新旧地址不相等，再计算
			shareDao.setOldAddr(locAddr);// 旧地址赋值
			Utils.WriteTxtFile("\n执行中locAddr=" + locAddr + ",oldAddr="
					+ oldAddr, "E速递定位信息.txt");
			shareDao.setIsLocation(true);// 定位成功
			String newLatlng;
			try {
				newLatlng = LocationDistance.getCoordinate(locAddr);
				if (oldAddr == null) {// 第一次获取位置信息，旧地址为空
					longTimeWork(user_distance + 1, newLatlng);
				} else {// 计算距离
					double distatce = LocationDistance.getDistatce(
							(Utils.stringEmpty(oldLatlng)) ? LocationDistance
									.getCoordinate(oldAddr) : oldLatlng,
							newLatlng);
					int intDistance = (int) (distatce * 1000);// 计算新旧地址的距离
					longTimeWork(intDistance, newLatlng);// 操作数据库
				}
			} catch (IOException e) {
				Utils.WriteTxtFile("err:" + e.getMessage(), "E速递定位信息.txt");
				e.printStackTrace();
			}
			uploadGPS();// 提交GPS信息
			Utils.WriteTxtFile("\nlocationServer执行完毕", "E速递定位信息.txt");
			Utils.WriteTxtFile("\n执行完毕locAddr=" + locAddr + ",oldAddr="
					+ oldAddr, "E速递定位信息.txt");
		}
		if (Utils.stringEmpty(locAddr)) {
			shareDao.setIsLocation(false);// 定位失败
			sendAddrBroadCast("定位失败");
			ListenNetStateService.restartLocaService = true;
		}
	}

	/**
	 * 执行以下操作： 1，更新投递、揽收表的距离字段 2，发送通知提醒附近邮件 3，给附近邮件发送短信
	 * 
	 * @param distance
	 *            新旧地址距离，以用户设定的距离为限，小于user_distance则不进行表更新
	 * @param newLatlng
	 *            当前的经纬度
	 */
	public void longTimeWork(int distance, final String newLatlng) {
		// TODO 上传经纬度成功
		Utils.WriteTxtFile("longTimeWork", "E速递定位信息.txt");
		if (distance >= user_distance) {// 设定范围内不发通知
			updateTable(newLatlng);// 计算并更新表
			updateGatherTable(newLatlng);// 计算更新派揽表
			oldLatlng = newLatlng;// 旧经纬度赋值
			sendNotify();// 发送通知
		}
	}

	/**
	 * 发送通知,发送短信,更新投递表字段
	 * 
	 */
	public void sendNotify() {
		int count = 0;
		List<MessageInfoBean> localMail = deliverDao
				.queryLocalNotifationMail(getApplicationContext());// 附近邮件集合
		List<MessageInfoBean> localMoneyMail = deliverDao
				.queryLocalMoney(getApplicationContext());// 附近需收款邮件集合
		// 计算收款金额
		double sumMoney = 0.0;
		for (int i = 0; i < localMoneyMail.size(); i++) {
			sumMoney += Double.parseDouble(localMoneyMail.get(i).getMoney());
		}
		count = localMail.size();
		if (count != 0) {
			initNotifation("投递任务：附近共有 " + count + " 个快件等待投递,其中需收款邮件"
					+ localMoneyMail.size() + "个,共计收款" + sumMoney + "元", 0);// 发送通知
			NearTaskMapActivity.mapList = localMail;// 地图页的list赋值
			groupDao.updateLocalMailCount(getApplicationContext());// 更新附近邮件分组邮件个数
			deliverDao.updatedlv_info(localMail, Utils.getSysDateString(),
					Utils.getSysTimeString());// 更新附近邮件的投递日期和投递时间
			deliverDao.setMailListSendNotify(localMail);// 标记已发送过通知
			// sendmessage(localMail);// 发送短信
			sendMessageTask.upload(Utils.parseBeanToIdList(localMail),
					getApplicationContext());// 发送短信
		}
	}

	/**
	 * 向服务器提交GPS信息
	 */
	public void uploadGPS() {
		Utils.WriteTxtFile("提交gps", "E速递定位信息.txt");
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		int cur_hour = Integer.parseInt(sdf.format(new Date()));
		if (cur_hour >= 8 && cur_hour <= 20 && !Utils.stringEmpty(username)
				&& !Utils.stringEmpty(orgCode)) {// 在晚上8点到早上8点之间不上传位置信息
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("userName", username));//
			pairs.add(new BasicNameValuePair("longitude", locLng));//
			pairs.add(new BasicNameValuePair("delvOrgCode", orgCode));//
			pairs.add(new BasicNameValuePair("latitude", locLat));//
			pairs.add(new BasicNameValuePair("detailAddress", locAddr));//
			pairs.add(new BasicNameValuePair("provName", pro));//
			pairs.add(new BasicNameValuePair("cityName", city));//
			pairs.add(new BasicNameValuePair("countyName", district));//
			try {
				// shareDao.setGPS("pairs--->" + pairs.toString() + "\t");
				String gpsinfo1 = "pairs--->提交参数" + pairs.toString() + "\t"
						+ "result--->" + "\t\ttime:" + Utils.getCurrTime()
						+ "\n";
				Utils.WriteTxtFile(gpsinfo1, "E速递定位信息.txt");
				String doPost = NetHelper.doPost(Global.UPDATE_GPS, pairs);
				// shareDao.setGPS("result--->" + doPost + "\t\ttime:"
				// + Utils.getCurrTime() + "\n");
				String gpsinfo = "pairs--->返回" + pairs.toString() + "\t"
						+ "result--->" + doPost + "\t\ttime:"
						+ Utils.getCurrTime() + "\n";
				Utils.WriteTxtFile(gpsinfo, "E速递定位信息.txt");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 改变主页title上的地址
	 */
	public void sendAddrBroadCast(String addr) {
		try {
			if (!Constant.TITLE_LOADINGDATA.equals(MainFragment.getTitle())) {
				MainFragment.titleInfomation = addr;
				addrReceiver.putExtra("addr", addr);
				sendBroadcast(addrReceiver);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 定义通知
	 */
	@SuppressWarnings("deprecation")
	public void initNotifation(String msgInfo, int ntfNumber) {
		NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notify = new Notification(R.drawable.ic_launcher,
				"您有一条来自揽投助手的通知,点击查看", System.currentTimeMillis());
		Intent intents = new Intent(this, TaskShowActivity.class);
		intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intents.putExtra("notifation", "notifation");
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intents, 0); // 获取PendingIntent的实例 可以理解为延迟执行的intent 即将跳转页面，还没跳转
		// 添加声音效果
		notify.defaults |= Notification.DEFAULT_SOUND;
		notify.flags = Notification.FLAG_AUTO_CANCEL;
		notify.setLatestEventInfo(this, "提醒：", msgInfo, contentIntent);
		XFAudio xfAudio = new XFAudio(getApplicationContext(), msgInfo);
		xfAudio.startSay();
		mgr.notify(ntfNumber, notify);
	}

	/**
	 * 耗时操作：计算并更新投递表
	 */
	public void updateTable(final String newLatlng) {
		List<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		list = deliverDao.querybesidesTuoTou();// 查询未妥投+待处理的邮件
		if (list.size() == 0) {
			return;
		}

		int _id = 0;
		try {
			ArrayList<Map<String, Object>> distanceInfoList = new ArrayList<Map<String, Object>>();
			// 给经纬度和距离List赋值
			for (int i = 0; i < list.size(); i++) {
				MessageInfoBean bean = list.get(i);
				_id = bean.get_id();
				try {
					String cLocation = optAddress(bean.getRcver_street_addr());
					String cLatlng = bean.getLatlng();
					if ("null".equals(cLatlng) || cLatlng == null
							|| ",".equals(cLatlng)) {
						cLatlng = LocationDistance.getCoordinate(cLocation);
					}
					HashMap<String, Object> map = new HashMap<String, Object>();
					double distance = LocationDistance.getDistatce(cLatlng,
							newLatlng);
					int intDistance = (int) (distance * 1000);
					map.put("_id", _id);
					map.put("latlng", cLatlng);
					map.put("distance", intDistance);
					distanceInfoList.add(map);
				} catch (Exception e) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("_id", _id);
					map.put("latlng", "");
					map.put("distance", Integer.MAX_VALUE - 1);
					distanceInfoList.add(map);
				}
			}
			Utils.WriteTxtFile("更新表字段!!", "E速递定位信息.txt");
			// 更新表字段
			deliverDao.updateMailLocationInfo(distanceInfoList);
		} catch (Exception e) {
		}
	}

	/**
	 * 耗时操作：计算并更新派揽表
	 */
	public void updateGatherTable(final String newLatlng) {
		List<GatherTableBean> list = new ArrayList<GatherTableBean>();
		list = gatherDao.queryGatherMessage(orgCode, username);// 查询未妥投邮件
		if (list.size() == 0) {
			return;
		}
		int _id = 0;
		try {
			ArrayList<Map<String, Object>> latlngList = new ArrayList<Map<String, Object>>();
			ArrayList<Map<String, Object>> distanceList = new ArrayList<Map<String, Object>>();
			// 给经纬度和距离List赋值
			for (int i = 0; i < list.size(); i++) {
				GatherTableBean bean = list.get(i);
				_id = bean.get_id();
				try {
					String cLocation = bean.getAddress();
					String cLatlng = bean.getLatlng();
					if ("null".equals(cLatlng) || cLatlng == null
							|| ",".equals(cLatlng)) {
						cLatlng = LocationDistance.getCoordinate(cLocation);
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("_id", _id);
						map.put("latlng", cLatlng);
						latlngList.add(map);
					}
					double distance = LocationDistance.getDistatce(cLatlng,
							newLatlng);
					int intDistance = (int) (distance * 1000);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("_id", _id);
					map.put("distance", intDistance);
					distanceList.add(map);
				} catch (Exception e) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("_id", _id);
					map.put("distance", Integer.MAX_VALUE - 1);
					distanceList.add(map);
				}
			}
			// 更新表字段
			gatherDao.updateDistance(distanceList);
			gatherDao.updateLatlng(latlngList);
		} catch (Exception e) {
		}
	}

	/**
	 * 优化收件地址，对于非空地址，添加定位省市信息，提高距离计算精准度
	 * 
	 * @return 如：小店区 --->山西省太原市小店区
	 */
	public String optAddress(String addr) {
		if (addr != null && !"".equals(addr)) {
			if (city != null && !"null".equals(city)) {
				addr = city + addr;
			}
			if (pro != null && !"null".equals(pro)) {
				addr = pro + addr;
			}
		}
		return addr;
	}

	/**
	 * 关闭定位的一切活动
	 */
	public void stopLocationListener() {
		timer.cancel();
		mLocationClient.unRegisterLocationListener(myListener);
		mLocationClient.stop();
		mLocationClient = null;
		myListener = null;
		stopSelf();
		// 设置没有上传经纬度
		shareDao.setIsLocation(false);//
	}

	@Override
	public void onDestroy() {
		try {
			stopLocationListener();
		} catch (Exception e) {
		}
		super.onDestroy();
	}

	/**
	 * 附近邮件发送短信提醒
	 * 
	 * @param list
	 */
	public void sendmessage(final List<MessageInfoBean> list) {
		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					String mobile = "";
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getRcver_contact_phone1() != null
								&& !"null".equals(list.get(i)
										.getRcver_contact_phone1())
								&& !"".equals(list.get(i)
										.getRcver_contact_phone1())) {
							mobile += list.get(i).getRcver_contact_phone1()
									+ ",";
						}
					}
					List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
					paramObject.add(new BasicNameValuePair("sms_type", "t"));
					paramObject.add(new BasicNameValuePair("receiver_mobiles",
							mobile.substring(0, mobile.length() - 1)));
					paramObject.add(new BasicNameValuePair("longitude",
							BaiduGpsContants.getInstance().getLng()));
					paramObject.add(new BasicNameValuePair("latitude",
							BaiduGpsContants.getInstance().getLat()));
					paramObject.add(new BasicNameValuePair("address",
							BaiduGpsContants.getInstance().getAddressStr()));

					paramObject.add(new BasicNameValuePair("dlvorgcode",
							orgCode));
					paramObject
							.add(new BasicNameValuePair("username", username));
					paramObject.add(new BasicNameValuePair("the_class_date",
							daoFactory.getPushDao(getApplicationContext())
									.query_class_date(
											Constant.PUSH_TYPE_DELIVERTASK)));
					String result = NetHelper.doPost(Global.SENDMESSAGE,
							paramObject);
					if (result != null) {
						jsonObj = new JSONObject(result);
					}
				} catch (Exception e) {
				}
				return jsonObj;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObj) {
				// yutuotou();
				try {
					if (jsonObj != null) {
						if ("1".equals(jsonObj.get("result"))) {// 成功
						}
					}
				} catch (Exception ex) {

				}
				super.onPostExecute(jsonObj);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
		}.execute();

	}

	/**
	 * 附近邮件执行预妥投,定位操作的最后一步
	 */
	public void yutuotou() {
		final ArrayList<MessageInfoBean> list = deliverDao
				.queryLocalNotTuoTouAllFrequence(getApplicationContext());
		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					String mails = "";
					for (int i = 0; i < list.size(); i++) {
						mails += list.get(i).getMail_num() + ",";
					}
					List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
					paramObject
							.add(new BasicNameValuePair("org_code", orgCode));
					paramObject.add(new BasicNameValuePair("employee_no",
							username));
					paramObject.add(new BasicNameValuePair("mail_num", mails
							.substring(0, mails.length() - 1)));
					String result = NetHelper.doPost(Global.YUTUOTOU,
							paramObject);
					if (result != null) {
						jsonObj = new JSONObject(result);
					}
				} catch (Exception e) {
				}
				return jsonObj;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObj) {
				try {
					if (jsonObj != null) {
						if ("1".equals(jsonObj.get("result"))) {// 成功
							// deliverDao.updateListType(
							// Utils.parseBeanToIdList(list),
							// Constant.YUTUOTOU);
						} else {
						}
					}
				} catch (Exception ex) {

				}

				super.onPostExecute(jsonObj);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
		}.execute();

	}
}

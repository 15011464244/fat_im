package com.newcdc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import com.newcdc.activity.delivertask.TaskShowActivity;
import com.newcdc.asynctask.DeliverAsyncTask;
import com.newcdc.asynctask.DeliverAsyncTask.onPostExecuteListener;
import com.newcdc.asynctask.DownGroupAsyncTask;
import com.newcdc.asynctask.GatherAsyncTask;
import com.newcdc.asynctask.GatherAsyncTask.onPostExecuteListener_Gather;
import com.newcdc.db.AddressDao;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.DeliverMailDao;
import com.newcdc.db.DeliverTaskListDao;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.db.GroupDao;
import com.newcdc.db.MoneyDao;
import com.newcdc.db.PushDao;
import com.newcdc.model.GatherTableBean;
import com.newcdc.model.GroupBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.PhoneMessageUtils;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * 下载下段信息/分组信息/派单信息
 * 
 * @author zhangfan
 * 
 */
public class DownDCDataService extends Service {
	private Context context;
	private String orgCode;
	private String username;
	private DeliverDao deliverDao;
	private DeliverMailDao mailDao;
	private DeliverTaskListDao mTaskListDao;
	private String oldFormat;
	private String newFormat;
	private SharePreferenceUtilDaoFactory shareDao;
	private GroupDao groupDao;
	private PushDao pushDao;
	private AddressDao infodao;
	private UserInfoUtils userInfo;
	private Gather_MsgDao gatherDao;
	public static boolean downDeliverFail = false;
	public static boolean inServer;
	private DeliverDaoFactory daoFactory;
	private Handler mHandler;
	private MoneyDao moneyDao;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initData();
		createTodayDeliverPushQueue();

		if ((!newFormat.equals(oldFormat))) {
			// 第二天时删除所有数据
			mTaskListDao.updateCountTo0(orgCode, username);
			deliverDao.deleteAll();
			mailDao.deleteAll();
			daoFactory.getQueueDao(context).deleteCommit();
			daoFactory.getFastLanDao(context).deleteFastLan_YiShangChuang();
			moneyDao.delete_Money();
			gatherDao.deleteAll(orgCode, username);
		}
		try {
			startService(new Intent(context, DeliverService.class));
			// downDliverTask();// 启动下载下段数据
			groupTask();// 启动下载分组
			exchangeaddress();// 启动下载客户信息
			if (Global.isLan) {
				deleGatherMessage_ThreeDays();// 删除大于三天的派揽表中的信息
				downGatherTask();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public void groupTask() {
		if (groupDao.queryKeys().size() == 0) {// 分组为空时，增加默认分组
			GroupBean bean_daichuli = new GroupBean("", "待处理",
					Constant.DAICHULI + "", "", "", "", "", "", "");
			GroupBean bean_tuotou = new GroupBean("", "妥投", Constant.TUOTOU
					+ "", "", "", "", "", "", "");
			GroupBean bean_weituotou = new GroupBean("", "未妥投",
					Constant.WEITUOTOU + "", "", "", "", "", "", "");
			GroupBean bean_err = new GroupBean("", "未上传", Constant.UNCOMMIT
					+ "", "", "", "", "", "", "");
			GroupBean bean_local = new GroupBean("", "附近邮件", Constant.LOCALMAIL
					+ "", "", "", "", "", "", "");
			GroupBean bean_daishou = new GroupBean("", "代收邮件",
					Constant.DAISHOUGROUP + "", "", "", "", "", "", "");
			GroupBean bean_sjrP = new GroupBean("", "收件人付费",
					Constant.SHOUJIANRENPAY + "", "", "", "", "", "", "");
			final List<GroupBean> list = new ArrayList<GroupBean>();
			list.add(bean_daichuli);
			list.add(bean_tuotou);
			list.add(bean_weituotou);
			list.add(bean_err);
			list.add(bean_daishou);
			list.add(bean_sjrP);
			list.add(bean_local);
			groupDao.insertGroup(list, context);
			downGroupTask();
		} else {
			new Thread() {
				@Override
				public void run() {
					groupDao.updateDealMailCount(context);
					if (!newFormat.equals(oldFormat)) {
						groupDao.updateLocalMailCount(context);
						groupDao.updateOtherGroups(context);
						groupDao.updateUncommitMailCount(context);
					}
				};
			}.start();
		}
	}

	/**
	 * 初始化成员变量
	 */
	public void initData() {
		context = getApplicationContext();
		gatherDao = DeliverDaoFactory.getInstance().getGather_msgdao(context);
		mTaskListDao = DeliverDaoFactory.getInstance().getDeliverTaskListDao(
				context);
		daoFactory = DeliverDaoFactory.getInstance();
		moneyDao = daoFactory.getMoneyDao(context);
		deliverDao = daoFactory.getDeliverDao(context);
		mailDao = daoFactory.getDeliverMailDao(context);
		groupDao = daoFactory.getGroupDao(context);
		shareDao = SharePreferenceUtilDaoFactory.getInstance(context);
		pushDao = daoFactory.getPushDao(context);
		infodao = daoFactory.getAddressDao(context);
		userInfo = new UserInfoUtils(context);
		orgCode = userInfo.getUserDelvorgCode();
		username = userInfo.getUserName();
		newFormat = Utils.getSysDate();
		oldFormat = shareDao.getDate();// 第一次登录，为空
		mHandler = new Handler();
		inServer = false;
	}

	/**
	 * 下载下段数据
	 * 
	 * @throws JSONException
	 */
	public void downDliverTask() throws JSONException {
		// 创建异步下载任务
		TaskShowActivity.loading = true;
		DeliverAsyncTask deliverAsyncTask = new DeliverAsyncTask(
				new onPostExecuteListener() {

					@Override
					public void onPostExecute(int result) {
						// UserDao userDao = DeliverDaoFactory.getInstance()
						// .getUserDao(context);
						// userDao.updateOrgcode();
						// downDeliverComplete = true;
						shareDao.setDate(newFormat);
						sendDownCompleteBroadCast(context, result);
					}
				}, null, context);
		deliverAsyncTask.execute();
	}

	public static boolean downDeliverComplete = false;

	/**
	 * 更新完毕发送广播
	 */
	public void sendDownCompleteBroadCast(Context context, int result) {
		Intent locationService = new Intent(context, LocationService.class);
		if (result > 0) {
			Toast.makeText(context, "更新完毕，共" + result + "条下段数据",
					Toast.LENGTH_SHORT).show();
			downDeliverFail = false;
		} else if (result == 0) {// 无新数据
			downDeliverFail = false;
			Utils.sendTitleBroadcast(context, Constant.TITLE_DATALATEST);
		} else {// 出现异常，下载失败
			downDeliverFail = true;
		}
		// 启动定位服务
		context.stopService(locationService);
		context.startService(locationService);
		Utils.sendDownCompleteBroadcast(context, result,
				Constant.PUSH_TYPE_DELIVERTASK);
		ListenNetStateService.netStateinServer = false;
		inServer = false;
	}

	/**
	 * 下载分组数据 sid 记录唯一标识 express_company_code 快递公司代码 express_company_name 快递公司名称
	 * org_code Y 机构号 employee_no Y 工号 group_content 分组内容 group_type 分组类型
	 * */
	private void downGroupTask() {
		String url = Global.FINDGROUP + "sid=" + "&express_company_code="
				+ "&express_company_name=" + "&org_code=" + orgCode
				+ "&employee_no=" + username + "&group_content="
				+ "&group_type=" + Constant.GROUP_BY_USER;
		DownGroupAsyncTask async = new DownGroupAsyncTask();
		async.setContext(context);
		async.setListener(new DownGroupAsyncTask.onPostExecuteListener() {

			@Override
			public void onPostExecute(String result) {
				if (result == null) {
					Toast.makeText(context, "请求服务器失败，请检查网络", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		async.execute(url);
	}

	// /**
	// * 创建今日下段信息推送队列
	// */
	// public void createTodayDeliverPushQueue() {
	// if (!Utils.getSysDate().equals(shareDao.getDate())) {// 第二天
	// shareDao.setDate(newFormat);
	// PushDao.getInstance(context).createTodayPushQueue(
	// Utils.getClassDate(context), 4);
	// }
	// }
	/**
	 * 创建今日下段信息推送队列
	 */
	public void createTodayDeliverPushQueue() {
		String the_class_date = pushDao
				.query_class_date(Constant.PUSH_TYPE_DELIVERTASK);
		if (the_class_date == null) {// 如果为空，则存入the_class_date
			shareDao.setDate(newFormat);// 第一次登入，存储日期
			pushDao.insert(Utils.getClassDate(context),
					Constant.PUSH_TYPE_DELIVERTASK);
		}
	}

	// 同步通讯录
	public void exchangeaddress() {
		if ((!newFormat.equals(oldFormat))) {
			// 第二天时删除所有数据
			infodao.deleteAlltelphone();
			findaddress();
		} else {
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			dataList = infodao.Findtelephone(username, orgCode);
			if (dataList.size() == 0) {
				infodao.deleteAlltelphone();
				findaddress();
			}
		}
	}

	/**
	 * 查询通讯录
	 */
	private void findaddress() {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
					paramObject.add(new BasicNameValuePair(
							"express_company_code", "EMS"));
					paramObject.add(new BasicNameValuePair(
							"express_company_name", "EMS"));
					paramObject.add(new BasicNameValuePair("org_code", userInfo
							.getUserDelvorgCode()));
					paramObject.add(new BasicNameValuePair("employee_no",
							userInfo.getUserName()));
					jsonObj = new JSONObject(NetHelper.doPost(
							Global.FINDUSERCONTACTS, paramObject));
					if (jsonObj != null) {
						if ("1".equals(jsonObj.get("result"))) {// 成功
							JSONArray jsoncc = jsonObj.getJSONObject("body")
									.getJSONArray("success");
							for (int i = 0; i < jsoncc.length(); i++) {
								JSONObject bbJsonObject = jsoncc
										.getJSONObject(i);
								String tel = bbJsonObject
										.getString("CONTACTS_MOBILE");
								String address = bbJsonObject
										.getString("CONTACTS_ADDRESS");
								String bodyname = bbJsonObject
										.getString("CONTACTS_NAME");//
								String mainid = bbJsonObject.getString("SID");
								String username = bbJsonObject
										.getString("EMPLOYEE_NO");
								String org_code = bbJsonObject
										.getString("ORG_CODE");
								infodao.inserttelephone(tel, bodyname,
										username, org_code, mainid, address);
							}
						}
					} else {
						Toast.makeText(context, "请求服务器失败，请检查网络",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}
				return jsonObj;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObj) {
				stopSelf();
				super.onPostExecute(jsonObj);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

		}.execute();
	}

	/**
	 * 下载派揽信息
	 */
	private void downGatherTask() {
		// Log.e("tag", "下载派揽信息---kaishi");
		new GatherAsyncTask(context, new onPostExecuteListener_Gather() {

			@Override
			public void onPostExecute(String result) {
				sendDownCompleteBroadCast_Gather(context, result);
			}
		}, null).execute();

	}

	/**
	 * 派揽信息更新完毕发送广播
	 */
	public void sendDownCompleteBroadCast_Gather(Context context, String result) {
		if (result != null && !"-2".equals(result) && !"-1".equals(result)) {
			try {
				int count = Integer.parseInt(result);
				Toast.makeText(context, "更新完毕，共" + count + "条派揽数据",
						Toast.LENGTH_SHORT).show();
				Utils.sendDownCompleteBroadcast(context, count,
						Constant.PUSH_TYPE_CLCTTASK);
			} catch (Exception e) {
				Utils.sendDownCompleteBroadcast(context, -1,
						Constant.PUSH_TYPE_CLCTTASK);
			}
		} else {
			// Toast.makeText(context, "请求服务器失败，请检查网络",
			// Toast.LENGTH_SHORT).show();
		}
	}

	public String resState(String s) {
		String str = "";
		try {
			JSONObject json = new JSONObject(s);
			str = json.get("result").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return str;
	}

	private void deleGatherMessage_ThreeDays() {
		List<GatherTableBean> gathLists = gatherDao.queryGatherMessage(orgCode,
				username);
		for (int i = 0; i < gathLists.size(); i++) {
			long oldTime = Long.parseLong(gathLists.get(i).getCreatetime());
			if (PhoneMessageUtils.daysBetween(oldTime, new Date().getTime()) > 3) {
				gatherDao.deleteGatherValue(gathLists.get(i).get_id());
			}
		}
	}
}

package com.newcdc.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cn.cdc.DaoFactory;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.activity.delivertask.TaskShowActivity;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.fragment.MainFragment;
import com.newcdc.model.ChangeStateBean;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.model.GeBean;
import com.newcdc.model.LanBean;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.model.TouBean;
import com.newcdc.service.CollectionUploadService;
import com.newcdc.service.DeliverService;
import com.newcdc.service.JXAsyncQueueService;

public class Utils {
	public static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
	public static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";

	/**
	 * 获取输入时间和系统时间的时间差,同时减去预警时间
	 * 
	 * @param mailTime
	 *            输入时间格式：HH:mm:ss
	 * @param yjTime
	 *            预警时间:单位-分
	 * @return 返回格式：HH:mm:ss
	 * @author zhangfan
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getStemp(String mailTime, String yjTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String sysTime = sdf.format(new Date());
		String[] sysSpilt = sysTime.split(":");
		String[] mailSpilt = mailTime.trim().split(":");
		int sys = Integer.parseInt(sysSpilt[0]) * 3600
				+ Integer.parseInt(sysSpilt[1]) * 60
				+ Integer.parseInt(sysSpilt[2]);// 系统时间秒数
		int mail = Integer.parseInt(mailSpilt[0]) * 3600
				+ Integer.parseInt(mailSpilt[1]) * 60
				+ Integer.parseInt(mailSpilt[2]);// 邮件时间秒数
		int temp = mail - sys + Integer.parseInt(yjTime) * 60;// 秒数差
		if (temp < 0) {
			temp = -temp;
			return "-" + parseMilsToDate(temp);
		}
		return parseMilsToDate(temp);
	}

	/**
	 * 启动异步提交任务
	 */
	public static void startIntentService(Context context) {
		if (isNetworkAvailable(context)) {
			Intent deliverService = new Intent(context,
					JXAsyncQueueService.class);
			Intent collectService = new Intent(context,
					CollectionUploadService.class);

			// Intent mailService = new Intent(context, DeliverService.class);
			context.startService(collectService);
			context.startService(deliverService);
			// context.startService(mailService);
			// context.startService(mailService);
		}
	}

	/**
	 * 启动异步提交任务
	 */
	public static void startFindTaskMailService(Context context) {
		if (isNetworkAvailable(context)) {
			Intent mailService = new Intent(context, DeliverService.class);
			context.startService(mailService);
		}
	}

	public static String readFile(File file) {
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(file);
			int c;
			while ((c = fis.read()) != -1) {
				sb.append((char) c);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 输入毫秒数，转化成HH:mm:ss
	 * 
	 * @param temp
	 * @return
	 */
	public static String parseMilsToDate(int temp) {
		int endH = temp / 3600;// end时
		int endM = (temp - endH * 3600) / 60;// end分
		int endS = (temp - endH * 3600 - endM * 60);
		if (endH < 0) {
			endM = -endM;
			endS = -endS;
		}
		return parseNumber(endH) + ":" + parseNumber(endM) + ":"
				+ parseNumber(endS);
	}

	/**
	 * 将个位数第一位添加0，如 1->01，10->10
	 */
	public static String parseNumber(int number) {
		String resultString = "";
		if (number >= 0 && number < 10) {
			resultString = "0" + number;
		} else {
			resultString = "" + number;
		}
		return resultString;
	}

	// 发送短信
	public static void sendSms(Context context, String strMessage, String phone) {
		if (strMessage.equals("")) {
			Toast.makeText(context, "请输入信息", Toast.LENGTH_SHORT).show();
		} else {
			try {
				// 建立SmsManager对象
				SmsManager smsManager = SmsManager.getDefault();
				// 建立自定义Action常数的Intent(给PendingIntent参数之用)
				Intent itSend = new Intent(SMS_SEND_ACTIOIN);
				Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);
				// sentIntent参数为传送后接受的广播信息PendingIntent
				PendingIntent mSendPI = PendingIntent.getBroadcast(context, 0,
						itSend, 0);
				// deliveryIntent参数为送达后接受的广播信息PendingIntent
				PendingIntent mDeliverPI = PendingIntent.getBroadcast(context,
						0, itDeliver, 0);
				// 发送SMS短信，注意倒数的两个PendingIntent参数
				if (strMessage.length() > 70) {
					ArrayList<String> msgs = smsManager
							.divideMessage(strMessage);
					for (String string : msgs) {
						smsManager.sendTextMessage(phone, null, string,
								mSendPI, mDeliverPI);
					}
				} else {
					smsManager.sendTextMessage(phone, null, strMessage,
							mSendPI, mDeliverPI);
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}

	public static String parseMsgDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 将长时间格式字符串转换为字符串 yyyy-MM-dd HH:mm:ss
	 */
	public static String longToStrng(String time) {
		Date date = new Date(Long.parseLong(time.trim()));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * @return HH:mm:ss
	 * */
	public static String getTime(String time) {
		SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = oldFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("---" + newFormat.format(date));
		return newFormat.format(date).split(" ")[1];
	}

	/**
	 * 计算两个时间差 ？天数
	 * */
	public static long GetDay(String time) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin;
		long day = 0;
		try {
			begin = dfs.parse(time);
			java.util.Date end = dfs.parse(Utils.getCurrTime());// 现在的系统时间
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			day = between / (24 * 3600);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 计算两个时间差 ？分钟数 以十分钟为单位
	 * */
	public static long GetMinute_Ten(String time) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin;
		long minute = 0;
		try {
			begin = dfs.parse(time);
			java.util.Date end = dfs.parse(Utils.getCurrTime());// 现在的系统时间
			long Second = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			minute = Second / (1 * 60) % 60;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return minute;
	}

	/**
	 * 计算两个时间差 ？分钟数 以五分钟为单位
	 * */
	public static long GetMinute_Five(String time) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin;
		long minute = 0;
		try {
			begin = dfs.parse(time);
			java.util.Date end = dfs.parse(Utils.getCurrTime());// 现在的系统时间
			long Second = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			minute = Second / (1 * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return minute;
	}

	/**
	 * 计算两个时间差 ?分和秒
	 * */
	public static String GetSecond(String time) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin;
		String timeString = "";
		long day = 0;
		long hour = 0;
		long minute = 0;
		long second = 0;
		String Hour = "";
		String Minute = "";
		String SSecond = "";
		try {
			begin = dfs.parse(time);
			java.util.Date end = dfs.parse(Utils.getCurrTime());// 现在的系统时间
			long Second = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			day = Second / (1 * 60 * 60 * 24);
			hour = Second / (1 * 60 * 60) % 24;
			minute = Second / (1 * 60) % 60;
			second = Second / (1) % 60;
			if (hour > 9) {
				Hour = String.valueOf(hour);
			} else {
				Hour = "0" + String.valueOf(hour);
			}
			if (minute > 9) {
				Minute = String.valueOf(minute);
			} else {
				Minute = "0" + String.valueOf(minute);
			}
			if (second > 9) {
				SSecond = String.valueOf(second);
			} else {
				SSecond = "0" + String.valueOf(second);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		LogUtils.e("--------------"+day + "天  " + Hour + ":" + Minute + ":" + SSecond);
		if (day > 0) {
			timeString = day + "天  " + Hour + ":" + Minute + ":" + SSecond;
		} else {
			timeString = Hour + ":" + Minute + ":" + SSecond;
		}

		return timeString;
	}

	/**
	 * 获取系统日期 格式：2014-09-01
	 */
	public static String getSysDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());// 系统当前日期
	}

	/**
	 * 获取无字符分隔的系统日期
	 * 
	 * @return
	 */
	public static String getSysDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	/**
	 * 获取无字符分隔的系统时间
	 * 
	 * @return
	 */
	public static String getSysTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 获取系统日期 格式：2014-09-01 10:01:02
	 */
	public static String getCurrTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());// 系统当前日期
	}

	/**
	 * 获取系统日期 格式：2014-09-01 10:01:02
	 */
	public static String getCurrTime(Long timeMills) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(timeMills));// 系统当前日期
	}

	/**
	 * 获取系统日期 格式：20140901100102
	 */
	public static String getDeliverTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());// 系统当前日期
	}

	/**
	 * 判断输入的string是否为空
	 * 
	 * @param inputString
	 * @return
	 */
	public static boolean stringEmpty(String inputString) {
		if (inputString == null || "null".equals(inputString)
				|| "".equals(inputString)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 执行请求并返回string数据，对应接口编号8
	 * 
	 * @param interfaceNo
	 *            接口编号
	 * @param dataFlag
	 *            数据同步标识
	 * @param requestUrl
	 *            请求服务器地址
	 * @return
	 */
	public static String exeRequestString8(String dataFlag, String orgCode,
			Context context) {
		String result = "";
		NetHelper netHelper = new NetHelper();
		try {
			result = netHelper.exeRequestPost(netHelper.createUrl(
					Global.WIRELESS_DATA_DOWNLOAD,
					fillDownloadData8(dataFlag, orgCode, context)));
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	/**
	 * 填充下载数据对应接口编号8
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static List<String> fillDownloadData8(String dataFlag,
			String orgCode, Context context) {
		List<String> listData = new ArrayList<String>();
		// 获取手机信息工具类
		TelephonyManager telephonemanage = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 获取配置数据
		// 填充参数
		listData.add(telephonemanage.getDeviceId());
		listData.add(orgCode);
		listData.add(dataFlag);// 数据下载标识
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		listData.add(sdf.format(new Date()));
		return listData;
	}

	/**
	 * 拆分数据
	 * 
	 * @param resource
	 * @return
	 */
	public static String[] splitData8(String resource) {
		String[] result = null;
		if (resource != null && resource.trim().length() != 0) {
			if (resource.indexOf("\n") != -1) {
				result = resource.split("\n");
			} else {
				result = new String[1];
				result[0] = resource;
			}
		}
		return result;
	}

	/**
	 * 解析投递情况代码，获得未妥投原因文字
	 */
	public static String parseDlvCode(String reason_code, Context context) {
		UserInfoUtils user = new UserInfoUtils(context);
		String orgCode = user.getUserDelvorgCode();
		List<Map<String, String>> list = DaoFactory.getInstance()
				.getBaseDataDao(context)
				.FindBaseDataByDataFlags("UNDLV_CAUSE_CODE");
		for (Map<String, String> map : list) {
			String xk = map.get("dataValue");
			String stateCode = map.get("dataKey");
			if (stateCode.equals(reason_code)) {
				return xk;
			}
		}
		return null;
	}

	/**
	 * 格式化投递时间
	 * 
	 * @param dlv_date
	 *            eg:dlv_date:20141012
	 * @param dlv_time
	 *            eg:082509
	 * @return 2014-10-12 08:25:09
	 */
	public static String parseDate(String dlv_date, String dlv_time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
		String dlv_info = dlv_date + " " + dlv_time;
		Date date;
		try {
			date = sdf.parse(dlv_info);
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedTime = sdf.format(date);
			return formattedTime;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dlv_info;
	}

	/**
	 * 查询妥投情况文字
	 * 
	 * @param bean
	 * @return
	 */
	public static String parseDlvCode(DeliverQueueBean bean) {
		String sign_sts_code = bean.getSign_sts_code();
		if ("w".equals(sign_sts_code.trim())
				|| "W".equals(sign_sts_code.trim())) {
			return "本人收";
		} else if ("M".equals(sign_sts_code.trim())
				|| "m".equals(sign_sts_code.trim())) {
			return bean.getSigner_name();
		} else {
			return "单位发章";
		}
	}

	/**
	 * 解析投递情况代码,获得下一步动作文字
	 */
	public static String parseDlvCode(String reason_code, String nextstep_code,
			Context context) {
		LogUtils.e("reason_code:   " + reason_code + "    nextstep_code:  "
				+ nextstep_code);
		UserInfoUtils user = new UserInfoUtils(context);
		String orgCode = user.getUserDelvorgCode();
		List<Map<String, String>> followList = DaoFactory.getInstance()
				.getBaseDataDao(context)
				.FindBaseDataByDataFlags("NEXT_ACTN_CODE");
		for (Map<String, String> map : followList) {
			String xk = map.get("dataValue");
			String stateCode = map.get("dataKey");
			if (stateCode.equals(nextstep_code.trim())) {
				return xk;
			}
			// if (stateCode.equals(nextstep_code.trim().toUpperCase())) {
			// return xk;
			// }
		}
		return null;
	}

	/**
	 * 根据输入的经纬度计算出距离
	 * 
	 * @param cLatlng
	 *            格式："lat,lng"
	 * @param newLatlngvf
	 *            格式："lat,lng"
	 * @return 单位km
	 */
	public static double getDistatce(String cLatlng, String newLatlng) {
		double lat1 = Double.parseDouble(cLatlng.split(",")[0]);
		double lat2 = Double.parseDouble(newLatlng.split(",")[0]);
		double lon1 = Double.parseDouble(cLatlng.split(",")[1]);
		double lon2 = Double.parseDouble(newLatlng.split(",")[1]);
		double R = 6371;
		double distance = 0.0;
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
		return distance;
	}

	/**
	 * @param addr
	 *            查询的地址
	 * @return
	 * @throws IOException
	 */
	public static String getCoordinate(String addr) throws IOException {
		String address = "";
		StringBuffer sb;
		if (addr != null) {
			try {
				address = java.net.URLEncoder.encode(addr, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String key = "fKI8sgeeLTfFhV46L7w8yHyh";
			String url = String
					.format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s",
							address, key);
			URL myURL = null;
			URLConnection httpsConn = null;
			try {
				myURL = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputStreamReader insr = null;
			BufferedReader br = null;
			try {
				httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
				if (httpsConn != null) {
					insr = new InputStreamReader(httpsConn.getInputStream(),
							"UTF-8");
					br = new BufferedReader(insr);
					String data = null;
					sb = new StringBuffer();
					while ((data = br.readLine()) != null) {
						sb.append(data);
					}
					String latlng = parseJson(sb.toString());
					return latlng;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (insr != null) {
					insr.close();
				}
				if (br != null) {
					br.close();
				}
			}
		}
		return null;
	}

	public static String parseJson(String data) {
		String lat = "";
		String lng = "";
		try {
			JSONObject object = new JSONObject(data);
			try {
				JSONObject result = object.getJSONObject("result");
				JSONObject location = result.getJSONObject("location");
				lng = location.getString("lng");
				lat = location.getString("lat");
			} catch (Exception e) {
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lat + "," + lng;
	}

	/**
	 * 查询投递任务表中，最新的数据对应的the_class_date
	 */
	public static String getClassDate(Context context) {
		DeliverDao deliverDao = DeliverDaoFactory.getInstance().getDeliverDao(
				context);
		List<MessageInfoBean> deliverBeans = deliverDao.querybesidesTuoTou();
		long max = 0;
		int max_id = 1;// 最近的日期对应的条目的_id
		for (int i = 0; i < deliverBeans.size(); i++) {
			String the_class_date = deliverBeans.get(i).getThe_class_date();
			long cur = Long.valueOf(the_class_date.replaceAll("[-\\s:]", ""));// 装运日期
			if (cur > max) {
				max = cur;
				max_id = deliverBeans.get(i).get_id();
			}
		}
		Cursor c = deliverDao.queryBy_id_Detail(max_id);
		String the_class_date = null;
		while (c.moveToNext()) {
			the_class_date = c.getString(c.getColumnIndex("sid"));
		}
		c.close();
		return the_class_date;
	}

	// 请求参数
	public static String initJsonStr(int state, List<TouBean> ltb,
			Context context) {
		UserInfoUtils user = new UserInfoUtils(context);
		ChangeStateBean csb = new ChangeStateBean();
		csb.setDeviceId(getDeviceId(context));
		csb.setDlvorgcode(user.getUserDelvorgCode());
		csb.setEmployeeNo(user.getUserDelvorgCode());
		csb.setSjvorgcode(user.getUserDelvorgCode());
		csb.setUsername(user.getUserName());
		if (state == 1 || state == 3) {
			List<LanBean> llb = new LinkedList<LanBean>();
			LanBean lb = new LanBean();
			lb.setFrequency("2");
			lb.setFrequencyDate("2");
			lb.setGekoudaima("2");
			lb.setMail_num("2");
			lb.setPay_type("2");
			lb.setTotailCharge("2");
			llb.add(lb);
			csb.setLan(llb);
		}
		if (state == 2 || state == 3) {
			TouBean tb = new TouBean();
			tb.setCharge(tb.getCharge());
			tb.setDeliveryConditions(tb.getDeliveryConditions());
			tb.setFrequency(tb.getFrequency());
			tb.setFrequencyDate(tb.getFrequencyDate());
			tb.setMail_num(tb.getMail_num());
			tb.setNextAction(tb.getNextAction());
			tb.setPay_type(tb.getPay_type());
			tb.setSign(tb.getSign());
			tb.setTotailCharge(tb.getTotailCharge());
			// tb.setPdacode(JiaoBanDao.getInstance(this).queryPdaCode(this));
			tb.setPdacode("");
			ltb.add(tb);
			csb.setTou(ltb);
		}
		List<GeBean> lgb = new LinkedList<GeBean>();
		GeBean gb = new GeBean();
		gb.setCpDaima("4");
		gb.setEmsZhonglei("4");
		gb.setFenleiDaima("4");
		gb.setMail_num("4");
		gb.setReceiveAddressDaiMa("4");
		gb.setReceivPost("4");
		gb.setTransport("4");
		lgb.add(gb);
		csb.setGekoudaima(lgb);
		String jsonstr = JSON.toJSONString(csb);
		return jsonstr;
	}

	// 获得设备号
	public static String getDeviceId(Context context) {
		TelephonyManager telephonemanage = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = telephonemanage.getDeviceId();
		return deviceId;
	}

	/**
	 * 下段信息，派揽信息下载完成发送的广播
	 * 
	 * @param context
	 * @param count
	 *            本次新增的数据数量 ，如果无新数据，则传入一个非数字类型即可
	 * @param messageType
	 *            数据类型：下段:Constant.PUSH_TYPE_DELIVERTASK
	 *            派揽：Constant.PUSH_TYPE_CLCTTASK
	 */
	public static void sendDownCompleteBroadcast(Context context, int count,
			int messageType) {
		TaskShowActivity.loading = false;
		Intent mainIntent = new Intent(Constant.ACTION_DOWN_DATA_OVER);
		mainIntent.putExtra("messageType", messageType);
		mainIntent.putExtra("count", count);
		context.sendBroadcast(mainIntent);
		// Log.e("2222222222", "22222222222222");

	}

	/**
	 * 更改TaskshowActivity头上的邮件个数
	 */
	public static void sendChangeTitleCountBroadcast(Context context) {
		Intent intent = new Intent(Constant.ACTION_TITLE_COUNT);
		context.sendBroadcast(intent);
	}

	/**
	 * 给MainFragment发送广播，更改title
	 * 
	 * @param context
	 * @param information
	 */
	public static void sendTitleBroadcast(Context context, String information) {
		Intent mainIntent = new Intent(Constant.ACTION_LOADING);
		MainFragment.titleInfomation = information;// 给主页面的title赋值
		context.sendBroadcast(mainIntent);
	}

	/**
	 * 给揽收上传发送广播，更改列表
	 * 
	 * @param context
	 * @param information
	 */
	public static void sendGatherSCBroadcast(Context context) {
		Intent mainIntent = new Intent(Constant.ACTION_GATHER_SC);
		context.sendBroadcast(mainIntent);
	}

	/**
	 * 将MessageInfoBean转化为_id集合
	 * 
	 * @param localMail
	 * @return
	 */
	public static ArrayList<Integer> parseBeanToIdList(
			List<MessageInfoBean> localMail) {
		ArrayList<Integer> idList = new ArrayList<Integer>();
		for (int i = 0; i < localMail.size(); i++) {
			idList.add(localMail.get(i).get_id());
		}
		return idList;
	}

	/**
	 * 将string转化为file
	 * 
	 */
	@SuppressWarnings("resource")
	public static void createFileFromString(String sqlString, String name) {
		File file = new File(Constant.SD, name);
		try {
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(file));
			osw.write(sqlString);
			osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void WriteTxtFile(String strcontent, String strFilePath) {
		// 每次写入时，都换行写
		String strContent = strcontent + "\n";
		try {
			File file = new File(Constant.SD);
			if (!file.exists()) {
				file.mkdirs();
			}
			File file2 = new File(file, strFilePath);
			if (!file2.exists()) {
				file2.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file2, "rw");
			raf.seek(file2.length());
			raf.write(strContent.getBytes());
			raf.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 将id集合转化为bean集合
	 * 
	 * @param context
	 * @param idList
	 * @return
	 */
	public static ArrayList<MessageInfoBean> parseIdListToBeans(
			Context context, ArrayList<Integer> idList) {
		DeliverDao dao = DeliverDaoFactory.getInstance().getDeliverDao(context);
		ArrayList<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		for (int i = 0; i < idList.size(); i++) {
			MessageInfoBean bean = dao.query_id(idList.get(i));
			list.add(bean);
		}
		return list;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return 是否可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni != null && ni.isAvailable());
	}

	/**
	 * 验证输入的手机号格式是否正确
	 * */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证输入的格式是否全是数字
	 * */
	public static boolean isAllNum(String mString) {
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(mString);
		return m.matches();
	}

	/**
	 * 震动
	 * */
	public static void playVibator(Context context, long timelong) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(timelong);
	}

}

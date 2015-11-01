package com.newcdc.tools;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;

public class NetHelper {
	/**
	 * GET请求方式 2014-12-23 Lion
	 * 
	 * @param urlStr
	 * @return result
	 */
	public static String doGet(String urlStr) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parms, 15000);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 15000);// 设置网络超时
			HttpGet httpget = new HttpGet(urlStr);
			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println("访问连接成功！");
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity);
				}
			} else {
				result = "请求服务器失败";
			}
		} catch (MalformedURLException e) {
			result = "请求服务器失败";
		} catch (IOException e) {
			result = "请求服务器失败";
		}
		return result;
	}

	public static Bitmap doGetImg2(String urlStr) {
		Bitmap bitmap = null;
		byte[] result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parms, 15000);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 15000);// 设置网络超时
			HttpGet httpget = new HttpGet(urlStr);
			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toByteArray(entity);
					result = Base64.decode(result, 0, result.length,
							Base64.DEFAULT);
					byte[] decodedString = Base64
							.decode(result, Base64.DEFAULT);
					bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
							decodedString.length);
				}
			} else {
				return null;
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return bitmap;
	}

	public static Bitmap doGetImg(String urlStr) {
		byte[] result = null;
		Bitmap bitmap = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parms, 15000);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 15000);// 设置网络超时
			HttpGet httpget = new HttpGet(urlStr);
			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				Log.e("doGetImg", "访问连接成功！");
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toByteArray(entity);
					result = Base64.decode(result, 0, result.length,
							Base64.DEFAULT);
					BitmapFactory.Options opt = new BitmapFactory.Options();
					opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图
																	// 565代表对应三原色占的位数
					opt.inInputShareable = true;
					opt.inPurgeable = true;// 设置图片可以被回收
					opt.inSampleSize = 1;// 表示缩小到1/2
					InputStream stream = new ByteArrayInputStream(result);
					bitmap = BitmapFactory.decodeStream(stream, null, opt);
				}
			} else {
				return null;
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return bitmap;
	}

	/**
	 * post请求
	 * 
	 * @param urlStr
	 * @param list
	 * @param context
	 * @param operate_action
	 *            操作动作：本次操作的汉字描述
	 * @param data_count
	 *            数据量（条数）：数据条数
	 * @return
	 */
	public static String doPost(String urlStr, List<NameValuePair> list) {
		HttpEntity entity1 = null;
		String result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parms, 10000);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 10000);// 设置网络超时
			entity1 = new UrlEncodedFormEntity(list, HTTP.UTF_8);
			HttpPost httppost = new HttpPost(urlStr);
			httppost.setEntity(entity1);
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			} else {
				result = "请求服务器失败";
			}
		} catch (MalformedURLException e) {
			result = "请求服务器失败";
		} catch (IOException e) {
			result = "请求服务器失败";
		}
		return result;
	}

	/**
	 * 提交请求日志
	 * 
	 * @param operate_time
	 *            操作时间，请求提交前获取的系统时间 Utils.getCurrTime();
	 * @param cost_time
	 *            消耗时间，请求提交前到服务器返回数据后的时间差，毫秒数
	 * @param context
	 *            上下文
	 * @param operate_action
	 *            操作动作 ，对本次操作的表述
	 * @param data_count
	 *            数据量，本次下载或提交的数据条数
	 * @param batch_no
	 *            请求提交前日期格式的时间
	 */
	public static void saveTransferLog(String operate_time, String cost_time,
			Context context, String operate_action, String data_count,
			String batch_no) {
		UserInfoUtils user = new UserInfoUtils(context);
		String org_code = user.getUserDelvorgCode();
		String employee_no = user.getUserName();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org_code", org_code));
		params.add(new BasicNameValuePair("employee_no", employee_no));
		params.add(new BasicNameValuePair("operate_action", operate_action));
		params.add(new BasicNameValuePair("operate_time", operate_time));
		params.add(new BasicNameValuePair("cost_time", cost_time));
		params.add(new BasicNameValuePair("data_count", data_count));
		params.add(new BasicNameValuePair("batch_no", batch_no));
		doPost(Global.SAVETRANSFERLOG, params);
	}

	/**
	 * POST请求方式 2014-12-23 Lionvis
	 * 
	 * @param urlStr
	 * @param requestJson
	 * @return result
	 */
	public static String doMsgPost(String urlStr, List<NameValuePair> list) {
		HttpEntity entity1 = null;
		String result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parms, 1000 * 60);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 1000 * 60);// 设置网络超时
			entity1 = new UrlEncodedFormEntity(list, HTTP.UTF_8);
			HttpPost httppost = new HttpPost(urlStr);
			httppost.setEntity(entity1);
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("status line :"
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println("访问连接成功！");
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			} else {
				result = "请求服务器失败";
			}
		} catch (MalformedURLException e) {
			result = "请求服务器失败";
		} catch (IOException e) {
			result = "请求服务器失败";
		}
		return result;
	}

	/**
	 * 
	 * post请求 key:value-->>json:jsonstr
	 */
	public static String doPostJson(String urlStr, String request, String name) {
		LogUtils.e("访问请求===doPostJson");
		HttpEntity entity1 = null;
		String result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parms, 15000);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 15000);// 设置网络超时
			BasicNameValuePair nvp = new BasicNameValuePair(name, request);
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(nvp);
			entity1 = new UrlEncodedFormEntity(list, HTTP.UTF_8);
			HttpPost httppost = new HttpPost(urlStr);
			httppost.setEntity(entity1);
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			} else {
				result = "请求服务器失败";
			}
		} catch (MalformedURLException e) {
			result = "请求服务器失败";
		} catch (IOException e) {
			result = "请求服务器失败";
		}
		return result;
	}

	/**
	 * 
	 * post请求 key:value-->>json:jsonstr
	 */
	public static String doErrorPostJson(String urlStr, String request,
			String name) {
		HttpEntity entity1 = null;
		String result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();

			HttpConnectionParams.setConnectionTimeout(parms, 60 * 1000);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 60 * 1000);// 设置网络超时
			BasicNameValuePair nvp = new BasicNameValuePair(name, request);
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(nvp);
			entity1 = new UrlEncodedFormEntity(list, HTTP.UTF_8);
			HttpPost httppost = new HttpPost(urlStr);
			httppost.setEntity(entity1);
			HttpResponse response = httpclient.execute(httppost);
			Log.e("NetHelper->doPostJson", response.getStatusLine()
					.getStatusCode() + "");
			if (response.getStatusLine().getStatusCode() == 200) {
				Log.e("NetHelper->doPostJson", "访问连接成功！");
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);

			} else {
				result = "请求服务器失败";
			}
		} catch (MalformedURLException e) {
			result = "请求服务器失败";
		} catch (IOException e) {
			result = "请求服务器失败";
		}
		return result;
	}

	public static String doGetFile(String urlStr) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parms, 15000);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 15000);// 设置网络超时
			HttpGet httpget = new HttpGet(urlStr);
			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println("访问连接成功！");
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity);
					byte[] bytes = EntityUtils.toByteArray(entity);
				}
			} else {
				result = "请求服务器失败";
			}
		} catch (MalformedURLException e) {
			result = "请求服务器失败";
		} catch (IOException e) {
			result = "请求服务器失败";
		}
		return result;
	}

	public String exeRequestPost(String urlStr) throws Exception {
		try {
			URL url = new URL(urlStr.toString());
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setConnectTimeout(1000 * 4);
			httpConn.setReadTimeout(1000 * 6);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			// httpConn.setRequestProperty("Content-Type",
			// "application/json; charset=UTF-8");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setInstanceFollowRedirects(false);
			// byte[] gZip = BaseCommand.gZip(sendData.getBytes());
			httpConn.getOutputStream().write("".getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream(), "UTF-8"));
			// InputStreamReader inss = new InputStreamReader(
			// httpConn.getInputStream(), "UTF-8");
			StringBuilder sb = new StringBuilder();
			String line;
			String head = in.readLine().toString().trim();
			if ("1".equals(head)) {
				while ((line = in.readLine()) != null) {
					sb.append(line).append("\n");
				}
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 拼接请求字符串
	 * 
	 * @param url
	 * @param list
	 * @return
	 */
	public String createUrl(String url, List<String> list) {
		StringBuffer urlStr = new StringBuffer(url);
		String urldata = "";
		StringBuffer urlStr2 = new StringBuffer();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				urlStr2.append("\t").append(list.get(i));
			}
		}
		try {
			urldata = URLEncoder.encode(urlStr2.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return urlStr.append(urldata).toString();
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public String exeRequestBackGzipNull(String sendData, String urlStr)
			throws Exception {
		try {
			URL url = new URL(urlStr.toString());
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setConnectTimeout(1000 * 4);
			httpConn.setReadTimeout(1000 * 6);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setInstanceFollowRedirects(false);
			httpConn.getOutputStream().write(sendData.getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			String[] resultObj = sb.toString().split("\t");
			if ("1".equals(resultObj[0])) {
				if ("1005".equals(resultObj[1])) {// 没有数据
					return "1005";
				} else {
					String res = resultObj[resultObj.length - 1];
					BASE64Decoder base64Decoder = new BASE64Decoder();
					byte[] decodeBuffer = base64Decoder.decodeBuffer(res);
					byte[] resu = BaseCommand.unGZip(decodeBuffer);
					return new String(resu);
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 网络获取图片存到本地
	 * */
	public static void getImgFromNet(Context context, String filePath) {
		LogUtils.e("即将下载的图片地址是：  " + filePath);
		URL url;
		Bitmap mBitmap = null;
		try {
			url = new URL(filePath);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				mBitmap = BitmapFactory.decodeStream(conn.getInputStream());
			}
			saveFile(context, mBitmap, filePath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveFile(Context context, Bitmap bm, String fileName) {
		File dirFile = new File(Constant.SD);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		try {
			final String[] ssStrings = fileName.split("/");
			File myCaptureFile = new File(Constant.SD
					+ ssStrings[ssStrings.length - 1]);
			BufferedOutputStream bos;
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.PNG, 80, bos);
			bos.flush();
			bos.close();
			// 刷新回话窗口
			Intent intent = new Intent("msg.success");
			intent.putExtra("msg", "notread");
			context.sendBroadcast(intent);
		} catch (FileNotFoundException e) {
			LogUtils.e("" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

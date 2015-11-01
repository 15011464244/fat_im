package com.newcdc.version;

import java.util.Random;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
/**
 * 
 * 作者:	
 * 业务名:
 * 功能说明: 用于服务器请求封装
 * 编写日期:	2014-12-4
 *
 */
public class VtionHttpClient {
	public static final String TAG = "Art";
	private static HttpUtils client;
	static {
		client = new HttpUtils(6000);
	}
	public static void get(String url, RequestCallBack<String> callBack) {
		client.send(HttpMethod.GET, url, callBack);
	}

	/**
	 * 
	 * 方法说明：服务器接口对接
	 * @param context 上下文对象
	 * @param url	服务器请求地址
	 * @param params	服务器请求参数POST
	 * @param callBack	服务器请求回调接口
	 */
	public static void post(Context context, String url, RequestParams params,
			RequestCallBack<String> callBack) {
		//接口加密
		Random random = new Random();
		String nonce = random.nextInt(1000) + "";
		String biaoshifu = PhoneInfoUtils.getIMEI(context);
//		String signature = SHA1Util.hex_sha1(nonce + biaoshifu
//				+ "jasonsgfcrscm");
		params.addBodyParameter("nonce", nonce);
//		params.addBodyParameter("signature", signature);
		//正式启动请求
		client.send(HttpMethod.POST, url, params, callBack);
	}
	// 测试库接口
	public static void post2(Context context, String url, RequestParams params,
			RequestCallBack<String> callBack) {
//		MLog.i("post data ", params.toString());
		client.send(HttpMethod.POST, url, params, callBack);
	}
	/**
	 * 
	 * 方法说明：普通请求，接口未加密使用，本程序已弃用
	 *
	 * @param url
	 * @param params
	 * @param callBack
	 */
	public static void clientUpdatePost(String url, RequestParams params,
			RequestCallBack<String> callBack) {
		client.send(HttpMethod.POST, url, params, callBack);
	}

}

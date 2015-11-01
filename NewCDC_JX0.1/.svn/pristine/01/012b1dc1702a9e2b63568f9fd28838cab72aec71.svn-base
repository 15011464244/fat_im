package com.newcdc.tools;

import com.lidroid.xutils.util.LogUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {
	private static AsyncHttpClient client = new AsyncHttpClient(); // ʵ�����?
	static {
		client.setTimeout(30000); // 设置链接超时，如果不设置，默认为10s
		LogUtils.i("====getTimeout"+client.getTimeout());
	}

	public static void get(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取�?��string对象
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) // url里面带参�?
	{
		client.get(urlString, params, res);
	}

	public static void get(String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或�?数组
	{
		client.get(urlString, res); 
	}

	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) // 带参数，获取json对象或�?数组
	{
		client.get(urlString, params, res);
	}

	public static void get(String uString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
	{
		client.get(uString, bHandler); 
	}

	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.post(urlString, params, res);
	}
	

}

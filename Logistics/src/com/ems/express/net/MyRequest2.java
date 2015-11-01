package com.ems.express.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MyRequest2<T> extends Request<T> {
	private final Listener<T> mListener;
	private final Class<T> clazz;
	/**
	 * 设置访问服务器时要传递的头部参数
	 */
	private Map<String, String> mHeader = new HashMap<String, String>();
	private String mJson;

	public MyRequest2(int method, Class<T> clazz, String url,
			Listener<T> listener, ErrorListener errorlistener, String json) {
		super(method, url, errorlistener);
		this.mListener = listener;
		this.clazz = clazz;
		this.mJson = json;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			/**
			 * 得到返回的数据
			 */
			String jsonStr = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			/**
			 * 转化成对象
			 */
			return Response.success(new Gson().fromJson(jsonStr, clazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeader;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		return mJson.getBytes();
	}

	@Override
	protected void deliverResponse(T response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}

	}

}

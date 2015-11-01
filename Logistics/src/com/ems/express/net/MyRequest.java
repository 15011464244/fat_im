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
import com.google.gson.JsonSyntaxException;

public class MyRequest<T> extends Request<T> {
	private final Listener<T> mListener;
	private final Class<T> clazz;
	/**
	 * 设置访问服务器时要传递的头部参数
	 */
	private Map<String, String> mHeader = new HashMap<String, String>();
	private String mJson;
	private Map<String, String> mParams;

	public MyRequest(int method, Class<T> clazz, String url,
			Listener<T> listener, ErrorListener errorlistener, String json) {
		super(method, url, errorlistener);
		this.mListener = listener;
		this.clazz = clazz;
		this.mJson = json;
	}
	public MyRequest(int method, Class<T> clazz, String url,
			Listener<T> listener, ErrorListener errorlistener, Map<String, String> params) {
		super(method, url, errorlistener);
		this.mListener = listener;
		this.clazz = clazz;
		this.mParams = params;
		System.out.println("mParams--"+mParams);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			/**
			 * 得到返回的数据
			 */
			String jsonStr = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			System.out.println("jsonStr:"+jsonStr);
			/**
			 * 转化成对象
			 */
			return (Response<T>) Response.success(jsonStr,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
	@Override
	public Map<String, String> getParams(){
		System.out.println("mParams--"+mParams);
		return this.mParams;
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeader;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		if(mJson!=null){
			return mJson.getBytes();
		}else{
			return null;
		}
	}

	@Override
	protected void deliverResponse(T response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}
}

package com.ems.express.net;

import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.GsonObj;
import com.android.volley.toolbox.GsonPostParamsRequest;
import com.android.volley.toolbox.GsonRequest;
import com.ems.express.App;

public class Request {

	public static <T> void todo(HashMap<String, String> params, Class<T> clazz,
			String url, GsonObj obj, Listener<T> listener,
			ErrorListener errorListener) {
		RequestQueue queue = App.getQueue();

		GsonPostParamsRequest<T> request = new GsonPostParamsRequest<T>(
				Method.POST, url, obj, listener, errorListener, clazz, params);
		queue.add(request);
	}
	
	public static <T> void todo(int method,HashMap<String, String> params, Class<T> clazz,
			String url, GsonObj obj, Listener<T> listener,
			ErrorListener errorListener) {
		RequestQueue queue = App.getQueue();

		GsonPostParamsRequest<T> request = new GsonPostParamsRequest<T>(
				method, url, obj, listener, errorListener, clazz, params);
		queue.add(request);
	}
}

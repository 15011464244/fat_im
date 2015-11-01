package com.newcdc.chat.net;

import java.util.HashMap;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.GsonObj;
import com.android.volley.toolbox.GsonPostParamsRequest;
import com.newcdc.chat.frame.widget.gallery.App;

public class Request {

	public static <T> void todo(HashMap<String, String> params, Class<T> clazz,
			String url, GsonObj obj, Listener<T> listener,
			ErrorListener errorListener) {
		RequestQueue queue = App.getQueue();
		
		GsonPostParamsRequest<T> request = new GsonPostParamsRequest<T>(
				Method.POST, url, obj, listener, errorListener, clazz, params);
		queue.add(request);
	}
}

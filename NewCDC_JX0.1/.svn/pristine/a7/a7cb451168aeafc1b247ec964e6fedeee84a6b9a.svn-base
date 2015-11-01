package com.newcdc.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.NetHelper;

/**
 * 下载分组信息
 * 
 * @author liunannan
 * 
 */
public class DownGroupAsyncTask extends AsyncTask<String, Void, String> {
	private onPostExecuteListener listener;
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public interface onPostExecuteListener {
		void onPostExecute(String result);
	}

	@Override
	protected String doInBackground(String... params) {
		String result = NetHelper.doGet(params[0]);
		if (result == null || "请求服务器失败".equals(result)) {
		} else if (resState(result).equals("0")) {
			JSONObject object;
			String error = "error";
			try {
				object = new JSONObject(result);
				error = object.getJSONObject("body").getString("error");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			JsonUtils.parseGroupJson(getContext(), result);
		}
		return result;
	}

	public void setListener(onPostExecuteListener onPostExecuteListener) {
		this.listener = onPostExecuteListener;
	}

	@Override
	protected void onPostExecute(String result) {
		listener.onPostExecute(result);
		super.onPostExecute(result);
	}

	// 获得返回值中result字段的值
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
}

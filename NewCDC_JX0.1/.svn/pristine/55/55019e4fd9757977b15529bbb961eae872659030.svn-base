package com.newcdc.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.newcdc.tools.NetHelper;

/**
 * 下载邮件分类
 * 
 * @author liunannan
 * 
 */
public class DownMailTypeAsyncTask extends AsyncTask<String, Void, String> {
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
		return result;
	}

	public void setListener(
			onPostExecuteListener onPostExecuteListener) {
		this.listener = onPostExecuteListener;
	}

	@Override
	protected void onPostExecute(String result) {
		listener.onPostExecute(result);
		super.onPostExecute(result);
	}
}

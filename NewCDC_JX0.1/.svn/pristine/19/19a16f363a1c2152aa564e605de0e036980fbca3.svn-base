package com.newcdc.asynctask;

import android.os.AsyncTask;

import com.newcdc.tools.NetHelper;

public class LanShouFeedbackTask extends AsyncTask<String, String, String> {

	private onPostExecuteListener listener;

	public interface onPostExecuteListener {
		void onPostExecute(String result);
	}
	
	public void setListener(onPostExecuteListener onPostExecuteListener) {
		this.listener = onPostExecuteListener;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result = NetHelper.doPostJson(params[0], params[1], params[2]);
//		Log.e("params[1]",params[1].toString());
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		listener.onPostExecute(result);
		super.onPostExecute(result);
	}
	
}
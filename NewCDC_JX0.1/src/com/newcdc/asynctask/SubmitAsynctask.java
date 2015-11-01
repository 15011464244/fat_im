package com.newcdc.asynctask;

import android.os.AsyncTask;

import com.newcdc.tools.NetHelper;

/**
 * 交班异步任务
 * 
 * @author zhangfan 2014-12-29
 * 
 */
public class SubmitAsynctask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String result = NetHelper.doPostJson(params[0], params[1], "json");
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

}

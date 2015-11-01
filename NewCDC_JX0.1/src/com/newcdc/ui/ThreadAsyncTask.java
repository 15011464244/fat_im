/**
 * 
 */
package com.newcdc.ui;

import android.os.Handler;

/**
 * 自定义Thread，替换AsyncTask
 * 
 * @author zhangfan 2015-1-26,下午5:06:16
 * 
 */
public class ThreadAsyncTask extends Thread {
	private Handler mHandler;
	private onPreExecuteListener mPreExecuteListener;
	private onPostExcuteListener mPostExcuteListener;

	public interface onPreExecuteListener {
		void onPreExecute();
	}

	public interface onPostExcuteListener {
		void onPostExecute();
	}

	public void myRun() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				mPreExecuteListener.onPreExecute();
			}
		});
		run();
	}

	public ThreadAsyncTask(Handler mHandler,
			onPreExecuteListener mPreExecuteListener,
			onPostExcuteListener mPostExcuteListener) {
		super();
		this.mHandler = mHandler;
		this.mPreExecuteListener = mPreExecuteListener;
		this.mPostExcuteListener = mPostExcuteListener;
	}

}

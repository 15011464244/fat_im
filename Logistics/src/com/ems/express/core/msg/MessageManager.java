package com.ems.express.core.msg;

import android.util.Log;



public final class MessageManager {
	static String TAG = "MessageManager";
	static final MessageManager  instance = new MessageManager();
	static IObserverBase mObservers = null;
	public static MessageManager getInstance() {
		return instance;
	}
	
	public void attach(IObserverBase observerBase){
		Log.e(TAG, "attach："+observerBase.getClass().getName());
//		if(mObservers==null){
			mObservers = observerBase;
//		}else{
			Log.e(TAG, "attach：队列没删除");
//		}
		
	}
	public void detach(IObserverBase observerBase){
		Log.e(TAG, "detach："+observerBase.getClass().getName());
		mObservers = null;
	}
	public void asyncNotify(String message){
		if(mObservers!=null){
			mObservers.sendMessage(message);
		}else{
			Log.e(TAG, "接受者没有注册");
		}
	}
}

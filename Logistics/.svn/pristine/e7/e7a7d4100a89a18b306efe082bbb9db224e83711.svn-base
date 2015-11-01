package com.ems.express.ui;

import java.text.AttributedCharacterIterator.Attribute;



import com.ems.express.ui.BaiduMapActivity.SDKReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BaseActivityForRequest extends BaseActivity{
	//判定是否离开当前页面
	public Boolean stayThisPage = true;
	// 写一个广播的内部类，当收到动作时，结束activity  
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			 unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错  
//	            ((Activity) context).finish();  
//		}  
//    }; 
    
    @Override  
    protected void onResume() {  
        super.onResume();  
        stayThisPage = true;
    }  
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    @Override
    protected void onPause() {
    	stayThisPage = false;
    	super.onPause();
    }
    @Override
    protected void onStop() {
    	stayThisPage = false;
    	
    	super.onStop();
    }
   
}

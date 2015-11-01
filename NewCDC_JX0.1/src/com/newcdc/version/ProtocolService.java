package com.newcdc.version;

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.newcdc.tools.Global;

/**
 * 
 * 作者:  
 * 业务名:联网业务 
 * 功能说明: 联网下载数据封装类 
 * 编写日期: 2014-12-4
 * 
 */
public class ProtocolService {

	private static Context context ;
	public static void initConfig(Context mContext) {
		context = mContext;
	}
	/**
	 * 方法说明：获取当前版本号
	 * @param biaozhifu
	 * @param handler
	 */
	public static void getViersion(String biaozhifu,
			RequestCallBack<String> handler) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("biaoshifu", biaozhifu);
		VtionHttpClient.get(Global.CHECKVERSION, handler);
	}


}

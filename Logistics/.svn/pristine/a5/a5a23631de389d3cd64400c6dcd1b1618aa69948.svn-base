package com.ems.express.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ems.express.App;
import com.ems.express.R;



public class LoadCitysUtils extends AsyncTask<String, Integer, String> { 
	private Context mContext;
    @Override
    protected String doInBackground(String... parameter) {
    	//加载省市县
    	if(!(App.dbHelper.getCityCount(App.db, "province")>0)){
//    		RangeSelectUtil.loadLocalData(mContext);
    		loadCityData(mContext);
    	}
    	return null;
    }
    public LoadCitysUtils(Context mContext) {
		super();
		this.mContext = mContext;
	}
	@Override
    protected void onProgressUpdate(Integer... progress) { 
    }
	@Override
    protected void onPostExecute(String result) { 
    } 


/*
 * 加载省市县数据
 */
public void loadCityData(Context mContext){
	//将json文件读取到buffer数组中
			InputStream is = mContext.getResources().openRawResource(R.raw.city);
			byte[] buffer;
			try {
				buffer = new byte[is.available()];
				is.read(buffer);
				//将字节数组转换为以GB2312编码的字符串
//				String json = new String(buffer, "GB2312");
				String json = new String(buffer);
				
				//将字符串json转换为json对象，以便于取出数据
				JSONObject jsonObject = JSON.parseObject(json);
				
				//解析info数组，解析中括号括起来的内容就表示一个数组，使用JSONArray对象解析
				JSONArray proArray = jsonObject.getJSONArray("province");
				
				App.dbHelper.addProvince(App.db, proArray);
				
				//解析info数组，解析中括号括起来的内容就表示一个数组，使用JSONArray对象解析
				JSONArray cityArray = jsonObject.getJSONArray("city");
				
				App.dbHelper.addCity(App.db, cityArray);
				
				//解析info数组，解析中括号括起来的内容就表示一个数组，使用JSONArray对象解析
				JSONArray countyArray = jsonObject.getJSONArray("county");
				
				App.dbHelper.addCounty(App.db, countyArray);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
}
}

package com.newcdc.weather;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtils {
	
	public static boolean isNetWorkConn(Context context){
		ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=manager.getActiveNetworkInfo();
		if(info!=null){
			return true;
		}else{
			return false;
		}
	}
	
	public static byte[] getHttpResult(String path){
		ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		try {
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setReadTimeout(5000);
			conn.connect();
			if(conn.getResponseCode()==200){
				InputStream inputStream=conn.getInputStream();
				int temp=0;
				byte[] buff=new byte[1024];
				while((temp=inputStream.read(buff))!=-1){
					outputStream.write(buff, 0, temp);
					outputStream.flush();
				}
			}
			return outputStream.toByteArray();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}


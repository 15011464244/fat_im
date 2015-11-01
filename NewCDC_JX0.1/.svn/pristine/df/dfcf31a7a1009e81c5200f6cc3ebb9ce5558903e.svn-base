package com.newcdc.weather;

import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


public class DownLoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
	private String imageUrl;
	private Map<String,Bitmap> imageCache;
	private CallBack callBack;
	public DownLoadImageAsyncTask(Map<String, Bitmap> imageCache,CallBack callBack ){
		this.imageCache = imageCache;
		this.callBack = callBack;
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		imageUrl = params[0];
		byte[] buffs = HttpUtils.getHttpResult(imageUrl);
		Bitmap bitmap = null;
		if(buffs != null && buffs.length != 0){
			bitmap = BitmapFactory.decodeByteArray(buffs, 0, buffs.length);
		}
		return bitmap;
		
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		imageCache.put(imageUrl, result);
		callBack.sendResult(imageUrl, result);
	}
	public interface CallBack{
		public void sendResult(String imagePath,Bitmap bitmap);
	}
}






















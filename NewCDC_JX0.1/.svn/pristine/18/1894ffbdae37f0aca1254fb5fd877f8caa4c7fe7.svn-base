package com.newcdc.asynctask;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.lidroid.xutils.util.LogUtils;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.ui.GetBitmap;

/**
 * 聊天中下载图片
 * 
 * @author liunannan
 * 
 */
public class DownImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
	private onPostExecuteListener listener;
	private Context context;
	private com.newcdc.ui.RoundImageView mImageView;
	private String sid = "";

	public Context getContext() {
		return context;
	}

	public void setmImageView(com.newcdc.ui.RoundImageView mImageView) {
		this.mImageView = mImageView;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		 String sid = params[0];
//		sid = "296329037377214";
		LogUtils.e("sid: " + sid);
		Bitmap result = null;
		try {
			result = NetHelper.doGetImg2(Global.FINDUSEREIMAGEBYSID + "sid="
					+ sid);
			GetBitmap.saveBitmap(result, sid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public interface onPostExecuteListener {
		void onPostExecute(Bitmap result);
	}

	public void setListener(onPostExecuteListener onPostExecuteListener) {
		this.listener = onPostExecuteListener;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		File f = new File(Constant.SD + sid);
		if (f.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(Constant.SD + sid);
			if (null != bitmap) {
				mImageView.setImageBitmap(bitmap);
			}
		}
		listener.onPostExecute(result);
		super.onPostExecute(result);
	}
}

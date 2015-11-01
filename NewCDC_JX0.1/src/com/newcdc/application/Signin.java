/**
 * E速递签到
 */
package com.newcdc.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.arvin.koalapush.util.ToastUtil;
import com.newcdc.R;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author hanrong 2015-3-20,上午9:46:31
 * 
 */
public class Signin {

	private View layout;
	private LayoutInflater inflater;
	private Activity mActivity;
	private int width;
	private Dialog longdia;
	private UserInfoUtils mInfoUtils;
	private List<NameValuePair> params;
	private int count = 0;
	private SharePreferenceUtilDaoFactory shareDao;
	private ProgressDialog dialog;

	public Signin(Activity mActivity) {
		super();
		if (mActivity != null) {
			this.mActivity = mActivity;
			mInfoUtils = new UserInfoUtils(mActivity);
			shareDao = SharePreferenceUtilDaoFactory.getInstance(mActivity);
			DisplayMetrics metric = new DisplayMetrics();
			mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
			width = metric.widthPixels;
			if (longdia == null) {
				longdia = new Dialog(this.mActivity, R.style.dialogss);
				alertSigninDialog(longdia);
			} else {
				alertSigninDialog(longdia);
			}
		}

	}

	/**
	 * 
	 * **/
	private void alertSigninDialog(final Dialog mDialog) {
		inflater = mActivity.getLayoutInflater();
		layout = inflater.inflate(R.layout.dialog_signin_layout, null);
		Button sure = (Button) layout.findViewById(R.id.sigin_sure);
		longdia.setContentView(layout, new LayoutParams(width * 17 / 20,
				LayoutParams.WRAP_CONTENT));
		longdia.setCancelable(false);
		sure.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				if (dialog == null) {
					dialog = new ProgressDialog(mActivity, "签到中，请稍后");
					dialog.toShow();
				} else {
					dialog.toShow();
				}

				// shareDao.setSignin(true);
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userCode", mInfoUtils
						.getUserName()));
				params.add(new BasicNameValuePair("orgCode", mInfoUtils
						.getUserDelvorgCode()));
				params.add(new BasicNameValuePair("segmentCode", mInfoUtils
						.getDlvsectionid()));
				params.add(new BasicNameValuePair("provName", BaiduGpsContants
						.getInstance().getPro() != null ? BaiduGpsContants
						.getInstance().getPro() : ""));
				params.add(new BasicNameValuePair("cityName", BaiduGpsContants
						.getInstance().getCity() != null ? BaiduGpsContants
						.getInstance().getCity() : ""));
				params.add(new BasicNameValuePair(
						"countyName",
						BaiduGpsContants.getInstance().getDistrict() != null ? BaiduGpsContants
								.getInstance().getDistrict() : ""));
				params.add(new BasicNameValuePair(
						"signStreet",
						BaiduGpsContants.getInstance().getStreet() != null ? BaiduGpsContants
								.getInstance().getStreet() : ""));
				params.add(new BasicNameValuePair("signTime", Utils
						.getCurrTime()));
				params.add(new BasicNameValuePair("latCode", BaiduGpsContants
						.getInstance().getLat() != null ? BaiduGpsContants
						.getInstance().getLat() : ""));
				params.add(new BasicNameValuePair("lonCde", BaiduGpsContants
						.getInstance().getLng() != null ? BaiduGpsContants
						.getInstance().getLng() : ""));
				params.add(new BasicNameValuePair("dataSource", "3"));
				params.add(new BasicNameValuePair("deviceNo", Utils
						.getDeviceId(mActivity)));
				params.add(new BasicNameValuePair("deviceType", "android"));
				Log.e("tag", params.toString());
				// new SigninAsyncTask().execute(params);
				singinThread();
				longdia.dismiss();
			}
		});
		longdia.show();
	}

	class SigninAsyncTask extends
			AsyncTask<List<NameValuePair>, String, String> {

		@Override
		protected String doInBackground(List<NameValuePair>... params) {
			// TODO Auto-generated method stub
			String result = NetHelper.doPost(Global.SIGNIN_URL, params[0]);
			Log.e("tag", "=url===" + Global.SIGNIN_URL);
			return result;
		}
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.e("tag", result + count);
			Log.e("tag", "=result===" + result);
			if (result == null || "请求服务器失败".equals(result)) {
				count++;
				if (count < 10) {
					new SigninAsyncTask().execute(params);
				} else {
					// Toast.makeText(mActivity, "签到失败，请检查网络",
					// Toast.LENGTH_SHORT)
					// .show();
				}
			} else if ("0".equals(resState(result))) {
				count++;
				if (count < 10) {
					new SigninAsyncTask().execute(params);
				} else {
					// ToastUtil.show(mActivity, "签到失败，请检查网络");
				}
			} else if ("1".equals(resState(result))) {
				Toast.makeText(mActivity, "签到成功", Toast.LENGTH_SHORT).show();
				shareDao.setSignin(true);
			}

		}

	}

	/**
 * 
 */
	private void singinThread() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result = NetHelper.doPost(Global.SIGNIN_URL, params);
				Log.e("tag", "=urlH===" + Global.SIGNIN_URL);
				Message msg = new Message();
				msg.what = 1;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (dialog != null) {
				dialog.toDimiss();
			}
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				Log.e("tag", result + count);
				Log.e("tag", "=resultH===" + result);
				if (result == null ||  "请求服务器失败".equals(result)) {
					count++;
					Toast.makeText(mActivity, "签到失败，请检查网络后继续签到",
							Toast.LENGTH_SHORT).show();
					if (longdia == null) {
						longdia = new Dialog(mActivity, R.style.dialogss);
						alertSigninDialog(longdia);
					} else {
						alertSigninDialog(longdia);
					}        
					// if (count < 10) {
					// singinThread();
					// } else {
					// // Toast.makeText(mActivity, "签到失败，请检查网络",
					// // Toast.LENGTH_SHORT)
					// // .show();
					// }
				} else if ("0".equals(resState(result))) {
					count++;
					Toast.makeText(mActivity, "签到失败，请检查网络后继续签到",
							Toast.LENGTH_SHORT).show();
					if (longdia == null) {
						longdia = new Dialog(mActivity, R.style.dialogss);
						alertSigninDialog(longdia);
					} else {
						alertSigninDialog(longdia);
					}
					// if (count < 10) {
					// singinThread();
					// } else {
					// // ToastUtil.show(mActivity, "签到失败，请检查网络");
					// }
				} else if ("1".equals(resState(result))) {
					Toast.makeText(mActivity, "签到成功", Toast.LENGTH_SHORT)
							.show();
					shareDao.setSignin(true);
				}
				break;

			default:
				break;
			}
		}
	};
	private String resState(String s) {
		try {
			JSONObject json = new JSONObject(s);
			String str = json.get("result").toString();
			return str;
		} catch (JSONException e) {
			e.printStackTrace();
			return "0";
		}
	}
}

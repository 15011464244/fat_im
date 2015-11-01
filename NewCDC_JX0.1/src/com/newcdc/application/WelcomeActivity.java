package com.newcdc.application;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.newcdc.R;
import com.newcdc.asynctask.TestServiceNetStateAsync;
import com.newcdc.asynctask.TestServiceNetStateAsync.onPostExecuteListener_NetState;
import com.newcdc.service.TestServiceNetStateService;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.version.NewVersion;

/**
 * @author lp 2014-12-23 欢迎页
 */

public class WelcomeActivity extends BaseActivity {
	private Context context = this;
	private String downloadPath;
	private String downLoadJson;
	private String appVsrsion = "android_version.json";
	private Intent mTestServiceNetStateService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		startNetState();
		if (SharePreferenceUtilDaoFactory.getInstance(this).getIsFirst()) {
			startActivity(new Intent(WelcomeActivity.this, GuidActivity.class));
			SharePreferenceUtilDaoFactory.getInstance(this).setIsFirst(false); // 将第一次进入的标记设为false
			this.finish();
		} else {
			new TestServiceNetStateAsync(WelcomeActivity.this,
					new onPostExecuteListener_NetState() {
						@Override
						public void onPostExecute(String result) {
							updateVersion();
						}
					}).execute();
		}
	}

	/**
	 * 版本跟新
	 */
	private void updateVersion() {
		// 开启版本检查更新的地方
		/**
		 * 判断如果用户是第一次进入APP，则进入引导界面 如果不是则进入登入界面
		 */
		if (SharePreferenceUtilDaoFactory.getInstance(this).getIsFirst()) {
			startActivity(new Intent(WelcomeActivity.this, GuidActivity.class));
			SharePreferenceUtilDaoFactory.getInstance(this).setIsFirst(false); // 将第一次进入的标记设为false
			this.finish();
		} else {
			try {
				NewVersion newVersion = new NewVersion(context, downloadPath,
						downLoadJson, appVsrsion);
				newVersion.setActivity(WelcomeActivity.this);
				newVersion.checkUpdateVersion();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 启动网络状态监听服务
	 */
	private void startNetState() {
		mTestServiceNetStateService = new Intent(WelcomeActivity.this,
				TestServiceNetStateService.class);
		startService(mTestServiceNetStateService);// 启动网络监听
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return 是否可用
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni != null && ni.isAvailable());
	}

}

package com.newcdc.activity.usercenter;

import java.util.Date;

import org.json.JSONObject;

import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.application.LoginActivity;
import com.newcdc.service.CollectionUploadService;
import com.newcdc.tools.Global;
import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordActivity extends BaseActivity {

	private EditText et_jiu_password, et_new_password, et_sure_password;
	private Button btn_back_password_install, btn_password_ok;
	private String orgCode, username, password, newPassword;
	private ProgressDialog dialog;
	private SharePreferenceUtilDaoFactory spDao;
	private String ret;
	private Handler mHandler1 = new Handler();

	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_password_tx);
		UserInfoUtils user = new UserInfoUtils(PasswordActivity.this);
		orgCode = user.getUserDelvorgCode();
		username = user.getUserName();
		spDao = SharePreferenceUtilDaoFactory
				.getInstance(PasswordActivity.this);
		password = spDao.getPass();
		Log.e("password----", password);
		initView();
	}

	public void initView() {
		et_jiu_password = (EditText) findViewById(R.id.et_jiu_password);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_sure_password = (EditText) findViewById(R.id.et_sure_password);
		btn_back_password_install = (Button) findViewById(R.id.btn_back_password_install);
		btn_back_password_install.setOnClickListener(clicklistener);
		btn_password_ok = (Button) findViewById(R.id.btn_password_ok);
		btn_password_ok.setOnClickListener(clicklistener);
	}

	private OnClickListener clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back_password_install:
				finish();
				break;
			case R.id.btn_password_ok:
				passwordOk();
				break;
			}
		}
	};

	public void passwordOk() {
		if (et_jiu_password.getText().toString() == null
				|| et_jiu_password.getText().toString().trim().length() <= 0) {
			Toast.makeText(PasswordActivity.this, "请输入旧密码", Toast.LENGTH_SHORT)
					.show();
		} else if (!password.equals(et_jiu_password.getText().toString())) {
			Toast.makeText(PasswordActivity.this, "旧密码不正确", Toast.LENGTH_SHORT)
					.show();
		} else if (et_new_password.getText().toString() == null
				|| et_new_password.getText().toString().trim().length() <= 0) {
			Toast.makeText(PasswordActivity.this, "请输入新密码", Toast.LENGTH_SHORT)
					.show();
		} else if (et_sure_password.getText().toString() == null
				|| et_sure_password.getText().toString().trim().length() <= 0) {
			Toast.makeText(PasswordActivity.this, "请再输入新密码", Toast.LENGTH_SHORT)
					.show();
		} else if (et_jiu_password.getText().toString()
				.equals(et_new_password.getText().toString())) {
			Toast.makeText(PasswordActivity.this, "新密码与原密码不能一致",
					Toast.LENGTH_SHORT).show();
		} else if (et_sure_password.getText().toString()
				.equals(et_new_password.getText().toString())) {
			if (isNetworkAvailable()) {
				passWordData();
			} else {
				Toast.makeText(PasswordActivity.this, "网络不可用，无法修改密码",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(PasswordActivity.this, "新密码与确认密码要一致",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void passWordData() {
		newPassword = et_new_password.getText().toString();
		final JSONObject pObjNO = new JSONObject();
		try {
			pObjNO.put("orgCode", orgCode);
			pObjNO.put("userCode", username);
			pObjNO.put("password", et_new_password.getText().toString());
			dialog = new ProgressDialog(PasswordActivity.this, "正在修改密码...");
			dialog.setCanCalcelable(false);
			if (!dialog.isShowing()) {
				dialog.toShow();
			}
			new Thread() {
				@Override
				public void run() {
					// String result = NetHelper.doPostJson(Global.PassWord_URL,
					// pObjNO.toString(), "json");
					String url = Global.PassWord_URL + "userCode=" + username
							+ "&password=" + newPassword + "&orgCode="
							+ orgCode;
					String result = NetHelper.doGet(url);
					Log.e("reqest----------", pObjNO.toString());
					Log.e("result----------", result);
					if (result.equals("请求服务器失败")) {
						Toast.makeText(PasswordActivity.this, "请求服务器失败",
								Toast.LENGTH_SHORT).show();
					} else {
						JSONObject resultObj;
						try {
							resultObj = new JSONObject(result);
							ret = resultObj.getString("result");
						} catch (Exception e) {
							e.printStackTrace();
						}
						mHandler1.post(new Runnable() {
							@Override
							public void run() {
								if (dialog != null) {
									dialog.toDimiss();
								}
								if (ret.equals("1")) {
									Toast.makeText(PasswordActivity.this,
											"密码修改成功", Toast.LENGTH_SHORT)
											.show();
									spDao.setPass(newPassword);
									Intent intent = new Intent();
									intent.setClass(PasswordActivity.this,
											LoginActivity.class);
									startActivity(intent);
									finish();
								} else if (ret.equals("请求服务器失败")) {
									Toast.makeText(PasswordActivity.this,
											"请求服务器失败", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(PasswordActivity.this,
											"密码修改失败", Toast.LENGTH_SHORT)
											.show();
								}
							}
						});
					}
				};
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return 是否可用
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni != null && ni.isAvailable());
	}
}

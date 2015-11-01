package com.ems.express.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mengxianzheng on 15-2-10.
 */
public class ResetPWActivity extends BaseActivity {

	@InjectView(R.id.et_phone)
	EditText mPhone;

	@InjectView(R.id.et_auth_code)
	EditText mETAuthCode;

	@InjectView(R.id.btn_auth_code)
	TextView mTVauthCode;

	@InjectView(R.id.et_pw)
	EditText mPW;
	
	private Context mContext = ResetPWActivity.this;

	private ImageView imgProgress;

	private RelativeLayout rllayout;

	private AnimationDrawable mAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pw);
		ButterKnife.inject(this);
		imgProgress =(ImageView)findViewById(R.id.reset_chrysanthemum);
		rllayout =(RelativeLayout)findViewById(R.id.reset_layout);
		setHeadTitle(getString(R.string.C_RESET_PW));
		mPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
		mETAuthCode
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
	}

	/**
	 * 获取验证码
	 * */
	@OnClick(R.id.btn_auth_code)
	void getAuthCode() {
		String phone = mPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, getString(R.string.C_HINT_REGIST_USER));
			return;
		}

		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("phone_num", phone);
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST,
				null, Constant.ValidateCode, new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						try {
							String resultJson = (String) arg0;
							JSONObject object = new JSONObject(
									resultJson.toString());
							if (object.getInt("result") == 1) {
								String success = object.getJSONObject("body")
										.getString("success");
								ToastUtil.show(ResetPWActivity.this, success);
							} else {
								String error = object.getJSONObject("body")
										.getString("error");
								ToastUtil.show(ResetPWActivity.this, error);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						ToastUtil.show(ResetPWActivity.this,
								getString(R.string.C_NET_ERROR));

					}
				}, params);
		App.getQueue().add(req);
	}

	private void setRegistDialog() {
		imgProgress.setBackgroundResource(R.drawable.progress_chrysanthemum_wordless);  
	     mAnimation = (AnimationDrawable) imgProgress.getBackground();  
	     rllayout.setVisibility(View.VISIBLE);
	     imgProgress.post(new Runnable() {
			@Override
			public void run() {
					mAnimation.start();
			}
		});
	}
	/**
	 * 提交
	 * */
	@OnClick(R.id.btn_commit)
	void toCommit() {
		String phone = mPhone.getText().toString().trim();
		String authCode = mETAuthCode.getText().toString().trim();
		String pw = mPW.getText().toString().trim();

		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, getString(R.string.C_HINT_REGIST_USER));
			return;
		}
		if (TextUtils.isEmpty(authCode)) {
			ToastUtil.show(this, getString(R.string.C_HINT_AUTHOR_CODE));
			return;
		}
		if (TextUtils.isEmpty(pw)) {
			ToastUtil.show(this, getString(R.string.C_HINT_LOGIN_PW));
			return;
		}
		setRegistDialog();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("phone_num", phone);
		json.put("verify_code", authCode);
		json.put("password", pw);
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST,
				null, Constant.ResetPwd, new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						try {
							String resultJson = (String) arg0;
							JSONObject object = new JSONObject(
									resultJson.toString());
							if (object.getInt("result") == 1) {
								String success = object.getJSONObject("body")
										.getString("success");
								ToastUtil.show(ResetPWActivity.this, success);
								LoginActivity.startAction(mContext);
							} else {
								String error = object.getJSONObject("body")
										.getString("error");
								ToastUtil.show(ResetPWActivity.this, error);
								rllayout.setVisibility(View.GONE);
								mAnimation.stop();
							}
						} catch (Exception e) {
							e.printStackTrace();
							rllayout.setVisibility(View.GONE);
							mAnimation.stop();
						}
						rllayout.setVisibility(View.GONE);
						mAnimation.stop();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						ToastUtil.show(ResetPWActivity.this,
								getString(R.string.C_NET_ERROR));
						rllayout.setVisibility(View.GONE);
						mAnimation.stop();
					}
				}, params);
		App.getQueue().add(req);
	}
	public void back(View v) {
		finish();
	}

}

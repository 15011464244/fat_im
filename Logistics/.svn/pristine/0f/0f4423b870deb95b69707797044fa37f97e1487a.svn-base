package com.ems.express.ui.settle;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.LoginActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 评价
 */
public class CommentActivity extends Activity implements OnClickListener {
	private Context mContext = CommentActivity.this;
	private ImageView mIvBack;// 返回键
	private EditText inputEvaluation;
	private Intent intent;
	private RatingBar ratingBar;
	
	//菊花
	private AnimationUtil util;

	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CommentActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_comment);

		initView();
		loadData();
	}
	
	  @Override
		protected void onResume() {
			super.onResume();
			//友盟统计
			MobclickAgent.onResume(this);
		}
		@Override
		protected void onPause() {
			super.onPause();
			//友盟统计
			MobclickAgent.onPause(this);
		}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.back_button);
		findViewById(R.id.submit).setOnClickListener(this);
		inputEvaluation = (EditText)findViewById(R.id.editText1);
		mIvBack.setOnClickListener(this);
		ratingBar = (RatingBar)findViewById(R.id.ratingBar1);
		//菊花
		util = new AnimationUtil(mContext, R.style.dialog_animation);
		intent = getIntent();
	}
	
	public void loadData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.submit:
			submit();
			break;
		default:
			break;
		}
	}
	private void submit() {
		if(SpfsUtil.loadPhone().equals("")){
			Intent intent1 = new Intent(this,LoginActivity.class);
			startActivity(intent1);
			ToastUtil.show(this, "请先登录");
			}else{
			Log.e("msgggRatingBar", String.valueOf(ratingBar.getRating()));
			if(inputEvaluation.getText().toString().isEmpty() || 0.0 == ratingBar.getRating()){
			DialogUtils.getNullCommentDialog(mContext, "评价/评价星级不能为空");
			return;
			}
			util.show();//开启菊花
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mail_num", intent.getStringExtra("MAIL_NUMBER"));
			map.put("appraise_content", inputEvaluation.getText().toString());
			map.put("appraise_grade",ratingBar.getRating());
			map.put("create_by", SpfsUtil.loadPhone());
			String params = ParamsUtil.getUrlParamsByMap(map);
			MyRequest<Object> request = new MyRequest<Object>(Method.POST,
					null, Constant.MailComment,
					new Listener<Object>() {
	
						@Override
						public void onResponse(Object arg0) {
							JSONObject obj = JSONObject.parseObject(arg0
									.toString());
							if ("1".equals(obj.getString("result"))) {
								
								
								final Dialog dialog = DialogUtils.successMessage(mContext, "感谢您对我们的评价\n我们会更好的为您服务");
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										dialog.dismiss();
										finish();
									}
								}, 3*1000);
								
								//结束菊花
								util.dismiss();
							}else if("0".equals(obj.getString("result"))){
								//结束菊花
								util.dismiss();
							}
						}
					}, new ErrorListener() {
	
						@Override
						public void onErrorResponse(VolleyError arg0) {
							arg0.printStackTrace();
							ToastUtil.show(mContext, "评价失败");
							//结束菊花
							util.dismiss();
						}
					}, params);
			App.getQueue().add(request);
		}
	}
}

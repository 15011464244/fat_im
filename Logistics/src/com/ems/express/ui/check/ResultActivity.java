package com.ems.express.ui.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.ExpressBean4Jiangxi;
import com.ems.express.bean.ExpressRecordBean;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.settle.CommentActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.google.gson.JsonSyntaxException;
import com.umeng.analytics.MobclickAgent;

/**
 * 查询结果
 */
public class ResultActivity extends Activity implements OnClickListener {
	private ImageView mIvBack;// 返回键
	private ImageView mIvIcon;// icon
	private TextView mTvNumber;// 单号
	private ListView mLvRecord;// 查件记录列表
	private Context mContext;
	
	private AnimationUtil util;
	
	private TextView mTvComment;//评价
	static Intent intent = null;

	public static void actionStart(Context context, String mailNumber) {
		intent = new Intent(context, ResultActivity.class);
		intent.putExtra("MAIL_NUMBER", mailNumber);
		context.startActivity(intent);
	}
	
	/**
	 * 
	 * @param context
	 * @param mailNumber
	 * @param state 判断订单的状态
	 */
	public static void actionStart(Context context, String mailNumber,String state) {
		intent = new Intent(context, ResultActivity.class);
		intent.putExtra("MAIL_NUMBER", mailNumber);
		intent.putExtra("mailState",state);
		context.startActivity(intent);
	}

	private ResultAdapter4Jiangxi mAdapter;
	private List<ExpressRecordBean> mData = new ArrayList<ExpressRecordBean>();
	private List<ExpressBean4Jiangxi> mData4Jiangxi = new ArrayList<ExpressBean4Jiangxi>();
	private String mMailNumber;

	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ResultActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		mContext = this;
		Intent intent = getIntent();
		mMailNumber = intent.getStringExtra("MAIL_NUMBER");
		initView();
		loadData();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.back_button);
		mIvIcon = (ImageView) findViewById(R.id.icon);
		mTvNumber = (TextView) findViewById(R.id.number);
		mLvRecord = (ListView) findViewById(R.id.list);
		
		((TextView)findViewById(R.id.tv_title)).setText("查询结果");
		((TextView)findViewById(R.id.tv_info)).setText("评价");
		
		mTvComment = (TextView) findViewById(R.id.tv_info);
		mTvNumber.setText("单号: " + mMailNumber);
		//菊花
		util = new AnimationUtil(mContext, R.style.dialog_animation);
		mIvBack.setOnClickListener(this);
		mIvIcon.setOnClickListener(this);
		mTvNumber.setOnClickListener(this);
		mTvComment.setOnClickListener(this);
		
		//评价按钮显现
		intent = getIntent();
		String mailState = intent.getStringExtra("mailState");
		if("4".equals(mailState)){
			mTvComment.setVisibility(View.VISIBLE);
		}

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
	
	

	public void loadData() {
		//开启菊花
		util.show();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("mailNo", mMailNumber);
//		json.put("expressNo", mMailNumber);
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
//		//吧邮件号添加到查询历史记录中
//		String mails = SpfsUtil.loadMailNums();
//		List<String> list = new ArrayList<String>();
//		if(null == mails){
//			 list.add(mMailNumber);
//		}else{
////			 list = Arrays.asList(mails.split(","));
//			 //string转list<string>
//			 String[] array = mails.split(",");
//			 for (int i = 0; i < array.length; i++) {
//				list.add(array[i]);
//			}
//			 
//			if(!mails.contains(mMailNumber)){
//				list.add(mMailNumber);
//				if(list.size()>5){
//					list.remove(0);
//				}
//			}
//		}
//		//list转string
//		StringBuffer sb = new StringBuffer();
//		for (int j = 0; j < list.size(); j++) {
//			sb.append(list.get(j));
//			sb.append(",");
//		}
//		sb.substring(0, sb.length()-1);
//		SpfsUtil.saveMailNums(sb.toString());
		
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null,
				"http://202.105.44.4/chinapost/postMailAction!ajaxQuery.do", new Listener<Object>() {
					@Override
					public void onResponse(Object resp) {
						System.out.println("obj:" + resp);
						Log.e("gongjie", resp.toString());
						if (resp == null || resp.toString().isEmpty()|| "\"\"".equals(resp.toString())) {
							ToastUtil.show(mContext, "没有找到该寄件");
							//结束菊花
							util.dismiss();
							return;
						}
						try {
							JSONObject jso = new JSONObject(resp.toString());
							if ("no".equals(jso.get("flag"))) {
								JSONArray info = jso.getJSONArray("redo");
								if(null == info || info.length() == 0){
									ToastUtil.show(mContext, "未查询到该运单记录");
									//结束菊花
									util.dismiss();
									return;
								}
								for (int i = 0; i < info.length(); i++) {
									ExpressBean4Jiangxi bean = new ExpressBean4Jiangxi();
									JSONObject obj = info.getJSONObject(i);
									String date = obj.getString("d44_70_tran_date");
									date = date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6);
									String time = obj.getString("d44_70_tran_time");
									time = time.substring(0, 2)+":"+time.substring(2, 4)+":"+time.substring(4);
									String dateTime = date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6)+" "+
											time.substring(0, 2)+":"+time.substring(2, 4)+":"+time.substring(4);
									String lastBrch = obj.getString("d4496_mail_last_brch");
									String mailState = obj.getString("d4496_mail_status");
									String dealDest = obj.getString("d4496_mail_deal_desc");
									String mailRemark = obj.getString("d4496_mail_remark");
									String flag = obj.getString("flag");
									
									bean.setDate(date);
									bean.setTime(time);
									bean.setMailRemark(mailRemark);
									bean.setMailState(mailState);
									bean.setDealDest(dealDest);
									bean.setFlag(flag);
									bean.setLastBrch(lastBrch);
											
									mData4Jiangxi.add(bean);
//									mData.add(bean);
								}
								mAdapter = new ResultAdapter4Jiangxi(ResultActivity.this, mData4Jiangxi);
								mLvRecord.setAdapter(mAdapter);
							} else {
								ToastUtil.show(mContext, "提交失败，请稍后重试");
								//结束菊花
								util.dismiss();
							}
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						//结束菊花
						util.dismiss();
					}
					
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						//结束菊花
						util.dismiss();

					}
				}, params);

		RequestQueue queue = App.getQueue();
		queue.add(req);
		queue.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.icon:
			break;
		case R.id.number:
			break;
		case R.id.tv_info:
			intent.setClass(ResultActivity.this, CommentActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}

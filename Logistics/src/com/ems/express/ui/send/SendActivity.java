package com.ems.express.ui.send;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.OrderInfoBean;
import com.ems.express.bean.PeopleInfo;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.BaseActivityForRequest;
import com.ems.express.ui.mail.MailOrderListActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DateTimeUtil;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 我要寄件
 */
public class SendActivity extends BaseActivityForRequest implements OnClickListener, OnCheckedChangeListener, OnDateChangedListener,
		OnTimeChangedListener, OnGetGeoCoderResultListener  {

	private Context mContext;
	@InjectView(R.id.arrow_weight)
	ImageView mWeightArrowView;
	@InjectView(R.id.weight_selection)
	RadioGroup mWeightSelectionView;
	@InjectView(R.id.selected_weight)
	TextView mSelectedWeight;
	@InjectView(R.id.tv_selected_date)
	TextView mSelecteDate;
	@InjectView(R.id.arrow_date)
	ImageView mDateArrowView;
	@InjectView(R.id.date_selection)
	View mDateSelectionView;
	@InjectView(R.id.tv_date)
	TextView mTvDate;
	@InjectView(R.id.tv_time)
	TextView mTvTime;
	@InjectView(R.id.tv_date_line)
	TextView mTvDateLine;
	@InjectView(R.id.tv_time_line)
	TextView mTvTimeLine;
	@InjectView(R.id.date_picker)
	DatePicker mDatePicker;
	@InjectView(R.id.time_picker)
	TimePicker mTimePicker;
	@InjectView(R.id.payment_selection)
	RadioGroup mPaymentSelectionView;
	@InjectView(R.id.mail_type)
	RadioGroup mMailTypeSelectionView;
	@InjectView(R.id.sender_name)
	TextView mTvSenderName;
	@InjectView(R.id.receiver_name)
	TextView mTvReceiverName;
	@InjectView(R.id.et_weight)
	EditText mEtWeight;
	@InjectView(R.id.tv_info)
	TextView mTvInfo;
	@InjectView(R.id.business_type)
	RadioGroup mBusinessTypeSelectionView;
	@InjectView(R.id.send_type)
	RadioGroup mSendTypeSelectionView;
	@InjectView(R.id.rl_statement)
	RelativeLayout mRelayStatement;
	@InjectView(R.id.iv_statement)
	ImageView mIvStatement;
	@InjectView(R.id.tv_content)
	TextView content;
	@InjectView(R.id.ll_date_time)
	LinearLayout llDateTime;
	@InjectView(R.id.line_4_time)
	View line4Time;
	@InjectView(R.id.tv_Explain)
	TextView mTvExplain;
	
	//优惠
//	@InjectView(R.id.rb_jifen)
//	RadioButton mRbJifen;
//	@InjectView(R.id.rb_youhuiquan)
//	RadioButton mRbYouhuiquan;
	@InjectView(R.id.tv_Explain_jifen)
	TextView mTvExplainJifen;
	@InjectView(R.id.line_4_youhui)
	View line4youhui;
	@InjectView(R.id.ll_4_jifen)
	LinearLayout ll4jifen;
	@InjectView(R.id.et_used_jifen)
	EditText jifenUse;
	@InjectView(R.id.tv_jifen_total)
	TextView jifenTotal;
	
	@InjectView(R.id.pay_now)
	RadioButton mRbPayNow ;
	@InjectView(R.id.pay_later)
	RadioButton mRbPayLater ;
	@InjectView(R.id.rb_jifen)
	RadioButton mRbJifen;
	
	
	private Integer mBusinessType = 1;
	private Integer mSendType = 1;

	private String[] mWeightValues = { "0--5kg", "5--10kg", "10kg以上" };
	private Calendar now;
	private String mDate;
	private String mTime;
	//map
	private GeoCoder coder;
	private PeopleInfo senderInfo;
	private PeopleInfo receiverInfo;
	private RequestQueue mQueue;
	private int mPayment = 1;
	private String mMailType = "2";
	private AnimationUtil util;
	
	private boolean timeFlag = false;
	
	private int totalJifen;
	private int useJifen;
	
	private Context appContext;
	
	//地图请求超时
//	private boolean flag1 = false;
//	private boolean flag2 = false;
	
	
	public static void actionStart(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SendActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send);
		ButterKnife.inject(this);
		mContext = this;
		appContext  = this.getApplicationContext();
		mQueue = App.getQueue();
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

	public void back(View v) {
		finish();
	}

	public void initView() {
		((TextView) findViewById(R.id.tv_title)).setText("寄件");
		findViewById(R.id.sender_info).setOnClickListener(this);
		findViewById(R.id.receiver_info).setOnClickListener(this);
		findViewById(R.id.select_weight).setOnClickListener(this);
		findViewById(R.id.select_date).setOnClickListener(this);
		findViewById(R.id.tv_date).setOnClickListener(this);
		findViewById(R.id.tv_time).setOnClickListener(this);
		mTvExplain.setOnClickListener(this);
		mRelayStatement.setOnClickListener(this);
		mTvExplainJifen.setOnClickListener(this);
		
		util = new AnimationUtil(this, R.style.dialog_animation);
		mTvInfo.setVisibility(View.VISIBLE);
		mTvInfo.setOnClickListener(this);
		mTvInfo.setText("记录");
		
		
		mWeightSelectionView.setOnCheckedChangeListener(this);
		mPaymentSelectionView.setOnCheckedChangeListener(this);
		mMailTypeSelectionView.setOnCheckedChangeListener(this);
		mBusinessTypeSelectionView.setOnCheckedChangeListener(this);
		mSendTypeSelectionView.setOnCheckedChangeListener(this);
		
		now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 60);
		
		mDate = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);
		mTime = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);// + ":" + now.get(Calendar.SECOND);
		mSelecteDate.setText(mDate + " " + mTime);
		mDatePicker.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), this);
		
		mTimePicker.setOnTimeChangedListener(this);
		if (SpfsUtil.getDefaultSender()!=null) {
			mTvSenderName.setText(SpfsUtil.getDefaultSender().getName());
		}
//		getJifenTotal(SpfsUtil.loadPhone());
		
		
		//map
//		mBaiduMap = mMapView.getMap();
		coder = GeoCoder.newInstance();
		coder.setOnGetGeoCodeResultListener(this);
	}

	public void loadData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sender_info:
//			SenderInfoActivity.actionStartForResult(this, SenderInfoActivity.SENDER);
			SenderListActivity.actionStartForResult(this, SenderInfoActivity.SENDER,SenderListActivity.FROM_SEND);
			break;
		case R.id.receiver_info:
//			SenderInfoActivity.actionStartForResult(this, SenderInfoActivity.RECEIVER);
			SenderListActivity.actionStartForResult(this, SenderInfoActivity.RECEIVER,SenderListActivity.FROM_SEND);
			break;
		case R.id.select_weight:
			toggleWeight();
			break;
		case R.id.select_date:
			toggleDate();
			break;
		case R.id.tv_date:
			mTvDate.setTextColor(getResources().getColor(R.color.orange));
			mTvTime.setTextColor(getResources().getColor(R.color.black));
			mTvDateLine.setBackgroundColor(getResources().getColor(R.color.orange));
			mTvTimeLine.setBackgroundColor(getResources().getColor(R.color.white));
			
			mDatePicker.setVisibility(View.VISIBLE);
			mTimePicker.setVisibility(View.GONE);
			break;
		case R.id.tv_time:
			mTvDate.setTextColor(getResources().getColor(R.color.black));
			mTvTime.setTextColor(getResources().getColor(R.color.orange));
			mTvTimeLine.setBackgroundColor(getResources().getColor(R.color.orange));
			mTvDateLine.setBackgroundColor(getResources().getColor(R.color.white));
			
			
			mDatePicker.setVisibility(View.GONE);
			mTimePicker.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_info:
//			SendRecordActivity.actionStart(mContext);
			MailOrderListActivity.actionStart(mContext);
			break;
		case R.id.rl_statement:
			toggleStatement();
			break;
		case R.id.tv_Explain:
			String content = "1、即时下单：寄件信息会即时发送给附近快递员，快递员收到信息会尽快上门取件！\n\n2、预约下单：快递员会根据您填写的预约时间，在预定时间上门取件！";
			DialogUtils.noticeDialog(this, content, "知道了", "寄件类型说明");
			break;
		case R.id.tv_Explain_jifen:
			String jifenExplain = "1、积分获取：注册赠送20积分，通过签到和寄件获取相应的积分！\n\n2、积分使用：寄件的时候可以使用积分抵现金，1积分=1元，积分和优惠券不能同时使用！";
			DialogUtils.noticeDialog(this, jifenExplain, "知道了", "积分说明");
			break;
			
		default:
			break;
		}
	}

	/**
	 * http://localhost:8080/micro-channel-service/NetworkAction/sendMail?sname=
	 * %E5%B7%A7%E5%85%8B%E5%8A%9B&sphone=1231&send_prov=%E5%90%88%E8%82%A5&
	 * send_city=as&send_county=ccc&send_prov_code=123&send_city_code=1231&
	 * send_county_code
	 * =3141&slocation=qqqq&rname=%E7%B3%96%E8%B1%86&rphone=1231231
	 * &receive_prov=
	 * sllle&receive_city=wew&receive_county=xxxx&receive_prov_code
	 * =123141&receive_city_code
	 * =3423432&receive_county_code=12311&rlocation=ccccc&dateID=2015-02-08
	 * 2018:54&weight=1&payment=1&extraService=1,2
	 */
	public void submit(View v) {
		
		String userId = SpfsUtil.loadId();
		/*if (userId.isEmpty()) {
			AlertDialog dialog = new AlertDialog.Builder(mContext).setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							LoginActivity.startAction(mContext);
						}
					}).create();
			dialog.setMessage("您还未登录，请先登录。");
			dialog.show();
			return;
		}*/
		if (senderInfo == null) {
			if (SpfsUtil.getDefaultSender()==null) {
				ToastUtil.show(mContext, "请编辑发件人信息");
				return;
			}else {
				senderInfo = SpfsUtil.getDefaultSender();
			}
		}
		
		/*if (senderInfo == null) {
			ToastUtil.show(mContext, "请编辑发件人信息");
			return;
		}*/
		if (receiverInfo == null) {
			ToastUtil.show(mContext, "请编辑收件人信息");
			return;
		}
		if (mEtWeight.getText().toString().isEmpty()) {
			ToastUtil.show(mContext, "请填写重量");
			return;
		}
		
		if(null == jifenUse.getText()|| "".equals((jifenUse.getText()+"").trim())){
			useJifen = 0;
		}else{
			useJifen = Integer.valueOf(jifenUse.getText()+"");
		}
		
		
		if(useJifen > totalJifen){
			ToastUtil.show(mContext, "使用积分不能超过现有积分");
			return;
		}
		
		if(mSendType == 1){
			now = Calendar.getInstance();
			now.add(Calendar.MINUTE, 60);
			mDate = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);
			mTime = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);// + ":" + now.get(Calendar.SECOND);
			mSelecteDate.setText(mDate + " " + mTime);
			mDatePicker.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), this);
		}else{
			now = Calendar.getInstance();
			Calendar pickTime = Calendar.getInstance();
			pickTime.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),Integer.valueOf(mTime.split(":")[0]),Integer.valueOf(mTime.split(":")[1]));
			if(pickTime.compareTo(now) == -1){
					DialogUtils.noticeDialog(mContext, "寄件时间不能小于当前时间", "知道了");
					return;
			}
			
		}
		
		//隐藏软键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘 
		//获取经纬度
		util.show();
		coder.geocode(new GeoCodeOption().city(senderInfo.getProv()).address(senderInfo.getLocation()));
//		new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				if(flag1){
//					flag1 = false;
//					return ;
//				}else{
//					ToastUtil.show(mContext, "请求超时！");
//					util.dismiss();
//					flag2 = true;
//				}
//				
//			}
//		}, 1000*15);
	}

	private void toggleDate() {
		boolean selectionShown = mDateSelectionView.getVisibility() == View.VISIBLE;
		mDateArrowView.setImageResource(selectionShown ? R.drawable.icon_down : R.drawable.icon_up);
		mDateSelectionView.setVisibility(selectionShown ? View.GONE : View.VISIBLE);
	}
	
	private void toggleStatement() {
		boolean selectionShown = content.getVisibility() == View.VISIBLE;
		mIvStatement.setImageResource(selectionShown ? R.drawable.icon_down : R.drawable.icon_up);
		content.setVisibility(selectionShown ? View.GONE : View.VISIBLE);
	}

	private void toggleWeight() {
		boolean selectionShown = mWeightSelectionView.getVisibility() == View.VISIBLE;
		mWeightArrowView.setImageResource(selectionShown ? R.drawable.icon_down : R.drawable.icon_up);
		mWeightSelectionView.setVisibility(selectionShown ? View.GONE : View.VISIBLE);
		System.out.println("checked: " + mWeightSelectionView.getCheckedRadioButtonId());
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group == mPaymentSelectionView) {
			if(checkedId == R.id.pay_now){
				mPayment = 1;
				line4youhui.setVisibility(View.GONE);
				ll4jifen.setVisibility(View.GONE);
			}else if(checkedId == R.id.pay_later){
				mPayment = 2;
				line4youhui.setVisibility(View.GONE);
				ll4jifen.setVisibility(View.GONE);
			}else if(checkedId == R.id.rb_jifen){
				mPayment = 3;
				getJifenTotal(SpfsUtil.loadPhone());
				line4youhui.setVisibility(View.VISIBLE);
				ll4jifen.setVisibility(View.VISIBLE);
			}
		} else if (group == mWeightSelectionView) {
			int index = mWeightSelectionView.indexOfChild(mWeightSelectionView.findViewById(checkedId));
			mSelectedWeight.setText(mWeightValues[index]);
			toggleWeight();
		} else if (group == mMailTypeSelectionView) {
			mMailType = checkedId == R.id.file ? "1" : "2";
		}else if(group == mBusinessTypeSelectionView){
			mBusinessType = checkedId == R.id.nomal ? 2 : 1;
		}else if(group == mSendTypeSelectionView){
			mSendType = checkedId == R.id.send_type_delay ? 2 : 1;
			if(mSendType == 2){
				line4Time.setVisibility(View.VISIBLE);
				llDateTime.setVisibility(View.VISIBLE);
				mDateSelectionView.setVisibility(View.VISIBLE);
			}else{
				line4Time.setVisibility(View.GONE);
				llDateTime.setVisibility(View.GONE);
				mDateSelectionView.setVisibility(View.GONE);
			}
		}/*else if(group == mPreferentiaSelectionView){
			if(checkedId == R.id.rb_jifen){
				mPayment = 3;
				line4youhui.setVisibility(View.VISIBLE);
				ll4jifen.setVisibility(View.VISIBLE);
			}else if(checkedId == R.id.rb_youhuiquan){
				mPayment = 4;
				line4youhui.setVisibility(View.GONE);
				ll4jifen.setVisibility(View.GONE);
			}
			mRbPayNow.setChecked(false);
			mRbPayLater.setChecked(false);
		}*/
	}
	
	/**
	 * 获取积分
	 * @param phone
	 */
	private void getJifenTotal(String phone){
		//开启菊花
		util.show();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("mobile", phone);
		String params = ParamsUtil.getUrlParamsByMap(json);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.jifenTotal,
				new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						LogUtils.e("jifenTotal_response == "+arg0+"");
						//关闭菊花
						util.dismiss();
						//请求成功
						if(((BaseActivityForRequest)mContext).stayThisPage){
							try {
								String result = (String) arg0;
								JSONObject object = new JSONObject(result.toString());
								if (object.getInt("result") == 1) {
									JSONObject content = object.getJSONObject("content");
									//剩余积分
									totalJifen = content.getInt("surplusIntegral");
									String str = "( "+totalJifen+" 积分可用)";
									
									SpannableStringBuilder builder = new SpannableStringBuilder(str);
									ForegroundColorSpan orangeSpan = new ForegroundColorSpan(Color.RED);
									builder.setSpan(orangeSpan, 1, str.indexOf("积"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
									
									jifenTotal.setText(builder);
									
								} else {
									//请求失败
									ToastUtil.show(mContext,"获取总积分失败");
								}
							} catch (Exception e) {
//										ToastUtil.show(mContext,"数据解析异常");
								e.printStackTrace();
							}
						}
					}
				
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						//关闭菊花
						util.dismiss();
						Toast.makeText(mContext, "请求异常!", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
					}
				}, params);
		App.getQueue().add(req);
		
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		//时间判断
		
			now = Calendar.getInstance();
			now.add(Calendar.MINUTE, 1);
			Calendar pickTime = Calendar.getInstance();
			pickTime.set(year, monthOfYear, dayOfMonth);
			if(pickTime.compareTo(now) == -1){
				if(!timeFlag){
					timeFlag = true;
					DialogUtils.noticeDialog(mContext, "寄件时间不能小于当前时间", "知道了");
				}
			}else{
				timeFlag = false;
			}
		
		
		
		
		mDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
		mSelecteDate.setText(mDate + " " + mTime);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		//时间判断
		
			now = Calendar.getInstance();
			now.add(Calendar.MINUTE, 1);
			Calendar pickTime = Calendar.getInstance();
			pickTime.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),hourOfDay,minute);
			if(pickTime.compareTo(now) == -1){
				if(!timeFlag){
					timeFlag = true;
					DialogUtils.noticeDialog(mContext, "寄件时间不能超过当前时间", "知道了");
				}
			}else{
				timeFlag = false;
			}
		
		mTime = hourOfDay + ":" + minute;// + ":00";
		mSelecteDate.setText(mDate + " " + mTime);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			if (requestCode == SenderInfoActivity.SENDER) {
				senderInfo = (PeopleInfo) data.getSerializableExtra("info");
				SpfsUtil.saveDefaultSender(senderInfo);
				mTvSenderName.setText(senderInfo.getName());
			} else {
				receiverInfo = (PeopleInfo) data.getSerializableExtra("info");
				mTvReceiverName.setText(receiverInfo.getName());
			}
		}
		//当返回的是空
		if(data == null){
			if (requestCode == SenderInfoActivity.SENDER) {
				/*senderInfo = null;
				mTvSenderName.setText("");*/
				senderInfo = SpfsUtil.getDefaultSender();
				mTvSenderName.setText(senderInfo.getName());
			} else {
				receiverInfo = null;
				mTvReceiverName.setText("");
			}
		}
	}
	
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
//		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			Toast.makeText(SendActivity.this, "抱歉，未找到寄件人地址", Toast.LENGTH_LONG)
//					.show();
//			return;
//		}
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("userSid", SpfsUtil.loadId());
		json.put("useIntegral", useJifen);
		
		json.put("sname", senderInfo.getName());
		json.put("sphone", senderInfo.getPhone());
		json.put("send_prov", senderInfo.getProv());
		json.put("send_city", senderInfo.getCity());
		json.put("send_county", senderInfo.getCounty());
		json.put("send_prov_code", senderInfo.getProvCode());
		json.put("send_city_code", senderInfo.getCityCode());
		json.put("send_county_code", senderInfo.getCountyCode());
		json.put("slocation", senderInfo.getLocation());
		json.put("rname", receiverInfo.getName());
		json.put("rphone", receiverInfo.getPhone());
		json.put("receive_prov", receiverInfo.getProv());
		json.put("receive_city", receiverInfo.getCity());
		json.put("receive_county", receiverInfo.getCounty());
		json.put("receive_prov_code", receiverInfo.getProvCode());
		json.put("receive_city_code", receiverInfo.getCityCode());
		json.put("receive_county_code", receiverInfo.getCountyCode());
		json.put("rlocation", receiverInfo.getLocation());
		json.put("dateID", mDate + " " + mTime);
		json.put("weight", mEtWeight.getText().toString());
		json.put("payment", mPayment);
		json.put("extraService", "1,2");
		json.put("objectstate", mMailType);
		json.put("tb_user_e_id", SpfsUtil.loadPhone());
		json.put("ydyname", "");
		json.put("ydytel", "");
		json.put("jgh", "");
		json.put("ygh", "");
		json.put("weixinzhanghao", "");
		json.put("dataSource", "2");
		
		//业务类型
		json.put("serviceType", mBusinessType);
		//寄件类型
		json.put("sendType", mSendType);
		
		//添加参数    20150321
		json.put("senderTel", senderInfo.getPhone());
		json.put("deviceType", "android");
		TelephonyManager mTelephonyMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		json.put("deviceNo", mTelephonyMgr.getDeviceId());
		//经纬度
		if(result != null && result.error == SearchResult.ERRORNO.NO_ERROR){
			json.put("bdLat",String.valueOf(result.getLocation().latitude));
			json.put("bdLon",String.valueOf(result.getLocation().longitude));
		}
//		Log.e("msgggLat", String.valueOf(result.getLocation().latitude));
//		Log.e("msgggLat", String.valueOf(result.getLocation().longitude));
//		json.put("bdLat","39.32111221");
//		json.put("bdLon","112.212323121");
//		
		String params = ParamsUtil.getUrlParamsByMap(json);
		Log.e("msgggSendParams", params);
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null,Constant.SendMail, new Listener<Object>() {

					@Override
					public void onResponse(Object resp) {
						Log.e("send result", resp+"");
						//超时
//						flag1 = true;
//						if(flag2){
//							flag2 = false;
//							return;
//						}
						
						if (resp == null || resp.toString().isEmpty()) {
							ToastUtil.show(mContext, "提交失败，请稍后重试");
							util.dismiss();
							return;
						}
						try {
							JSONObject jso = new JSONObject(resp.toString());
							if ("1".equals(jso.get("result"))) {
								DialogUtils.successMessage(mContext,"您的订单已经提交成功！\n通过“寄件记录”可以催单与退单！");//统计发件次数
//								DialogUtils.successMessage(appContext,"您的订单已经提交成功！\n通过“寄件记录”可以催单与退单！");//统计发件次数
								//寄件成功统计
								MobclickAgent.onEvent(mContext,"sendmail");
								//存入数据库
								String contentStr = jso.getString("content");
								JSONObject content= new JSONObject(contentStr);
								String orderNo = content.getString("orderId");
								OrderInfoBean bean = new OrderInfoBean();
								bean.setOrderNo(orderNo);
								bean.setSenderName(senderInfo.getName());
								bean.setSenderPhone(senderInfo.getPhone());
								bean.setSenderAddress(senderInfo.getLocation());
								
								bean.setReceiveName(receiverInfo.getName());
								bean.setReceivePhone(receiverInfo.getPhone());
								bean.setReceiveAddress(receiverInfo.getLocation());
								
								bean.setWeight(mEtWeight.getText().toString());
								bean.setType(mMailType);
								bean.setPayWay(mPayment+"");
								
								String time = DateTimeUtil.validateDateTime(new Date());
								bean.setOrderTime(time);
								App.dbHelper.insertOrderInfo(App.db, bean);
								util.dismiss();
							}
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
							util.dismiss();
						} catch (JSONException e) {
							e.printStackTrace();
							util.dismiss();
						}
						util.dismiss();;
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						ToastUtil.show(mContext, "提交失败，请稍后重试");
						util.dismiss();
					}
				}, params);

		mQueue.add(req);
		mQueue.start();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
}

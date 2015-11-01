package com.ems.express.fragment.message;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.message.OtherMessageAdapter;
import com.ems.express.adapter.message.ReceivePaymnetBean;
import com.ems.express.bean.PayResult;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.util.MD5;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SignUtils;
import com.lidroid.xutils.util.LogUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class OtherFragment extends Fragment {
	
	
	PayReq req;//微信
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(getActivity(), null);
	  //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    String APP_ID = "wxf2f565574a968187";
     //商户号
    final String MCH_ID = "1233848001";
     //  API密钥，在商户平台设置
    String API_KEY="412fde4e9c2e2bb619514ecea142e449";
    Map<String,String> resultunifiedorder;
	
	
	
	
	private boolean stayInThisFragment = true;
	private View view;
	
	private SwipeMenuListView listmessage;
	private OtherMessageAdapter adapter;
	private List<ReceivePaymnetBean> messageListData;
	
	private ImageView imgview;
	private Context mContext;
	String payType  = ""; //付费方式，1：微信支付 2：支付宝支付
	
    //商户PID
	public static final String PARTNER = "2088021282301748";
	//商户收款账号
	public static final String SELLER = "pingandaijia126@126.com";
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJn2KKkgxGDWq757jyHtJYiPQYtki8S1HAKJdXhkMerPFqSB4i5F0KiKH5EDSVGgBRw7oSiiPtnD8AEPNQMwDzyMs1oFmx/AeYKQScalMilUPQdOC7OMprM8zorfhpOVQ7RkYIVvuQD2MmCXpIf1PAqe/LGe6W3MS7qk0PRy4SHPAgMBAAECgYAjS3smyow6ZvwYPtshO+xO0giEnBgukBZLvpdfchi/a5oVPHFNilO7T27NH6O/Qp/pSQI4/njKE1EB7SqKAIp9S/2D3g8S+lE3xAkSyl3wdqEf33vKlzaJgcnL9KeniiLNFBU+KSX7SqqNH1Xhs4ib6/J9adJNB4so8VeGyFhwAQJBAMcZuTlsKZlRiJkz9jaScJ3Gp7Kbkd3AAQ+ayuSLXhXB/nKGIdWC5kmK0Hy9jDI6hv4jphz4AYOusG8RHqdoA08CQQDF9g1hQx5XnOY/C5xNE/NAHRzNZzxZjHFr2dIAGYkqRRBXO2hDt4crmcx16N/LAldicUkvV0UhNxFboxIMX1mBAkB/izQD3A1eAUQvWIEufmsUN5FwMoaj9n73fyLge4M/DvIwbUq5W0yo6fsbHdX0y1d08GNWhW167OprjB0GAvSzAkAWAUzpc+GKkalSdsLwGniett29w20E80SkXXkng68ooLa5S6RCasM+yIDe1n0R/vehvMAK4COSFqH6Ur0t3OeBAkA+F+JWtAN0LcND8qguPtgHOXQHjNWO4Y+MjCdWF+YHfyRo/Dw6FkTrSF90qiX1CI2tVPkjTx05vlX5nW6D/Qa4";
	//支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	
	
	private RelativeLayout mNotPackage;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Map map = null ;
			switch (msg.what) {
			
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(getActivity(), "支付成功",
							Toast.LENGTH_SHORT).show();
//					updatePaymentMessage(map);
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(getActivity(), "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(getActivity(), "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(getActivity(), "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case 3://数据的初始化
			{
				map = (Map) msg.obj;
				break;
			}
			default:
				break;
			}
		};
	};
	
	public BroadcastReceiver newMsgBReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			messageListData =  App.dbHelper.queryPaymentByOrderStatus(App.db,"6","7");
			if(messageListData == null || messageListData.size() == 0){
				mNotPackage.setVisibility(View.VISIBLE);
			}else{
				mNotPackage.setVisibility(View.GONE);
			}
			adapter.notifyData(messageListData);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.message_arrive_fragment, null);
		mContext = this.getActivity();
//		msgApi.registerApp(APP_ID);
		req = new PayReq();
		initView();
		
		// 注册一个自定义的广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("NewMsgReceiver_Action");
		mContext.registerReceiver(newMsgBReceiver, filter);
		
		return view;
	}
	
	void initView(){
		messageListData = App.dbHelper.queryPaymentByOrderStatus(App.db,"6","7");
		
		listmessage =(SwipeMenuListView)view.findViewById(R.id.list_message);
		imgview  =(ImageView)view.findViewById(R.id.img_view2);
		adapter = new OtherMessageAdapter(mContext, messageListData);
		
		mNotPackage = (RelativeLayout) view.findViewById(R.id.rl_notpackage);
		
		listmessage.setAdapter(adapter);
		addMenu();	
		int count = listmessage.getCount();
		if(count==0){
			mNotPackage.setVisibility(View.VISIBLE);
		}else{
			mNotPackage.setVisibility(View.GONE);
		}
	}//添加按钮
	void addMenu(){
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			
			@Override
			public void create(SwipeMenu menu) {
					createMenu2(menu);
			}
			//积分消息
			public void createMenu2(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						mContext.getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(255,
						255, 255)));
				// set item width
				deleteItem.setWidth(dp2px(84));
				// set a icon
				deleteItem.setIcon(R.drawable.img_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		listmessage.setMenuCreator(creator);
		
		listmessage.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				 ReceivePaymnetBean item = messageListData.get(position);
				switch (index) {
				case 0:
					
					if(!"1".equals(item.getMessageStatus())){
						delete(position);
					}else if("3".equals(item.getMailStatus())){
//						queryCurrier(item);
					}else if("7".equals(item.getMailStatus())){
						delete(position);
					}else if("6".equals(item.getMailStatus())){
						delete(position);
					}
					
					break;
				case 1:
					delete(position);
					break;
				}
				//这边返回值有点问题
				return false;
			}
		});
		
		
		listmessage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int i, long l) {
				App.dbHelper.updateReceiveNotice(App.db, messageListData.get(i).getReceiveId()+"");
				messageListData =  App.dbHelper.queryPaymentByOrderStatus(App.db,"6","7");
				adapter.notifyData(messageListData);
				Log.e("gongjie", messageListData.get(i).toString());
				String price = messageListData.get(i).getPrice();
				String orderNum = messageListData.get(i).getOrderNum();
				String usercode = messageListData.get(i).getUserCode();
				String orgCode = messageListData.get(i).getOrgCode();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("orderNum", orderNum);
				map.put("actFee", price);
				map.put("usercode", usercode);
				map.put("orgCode", orgCode);
				if (!TextUtils.isEmpty(price)) {
//			        DialogUtils.getPaymentDialog(mContext, null);
					getPaymentDialog(mContext, map);
				}
//				adapter.notifyDataSetChanged();
//			if("10".equals(messageListData.get(i).getMailStatus())){
//				ResultActivity.actionStart(mContext, messageListData.get(i).getMailNum(), "4");
//			}
			sendReceiver();
				
			}
		});
		
		// set SwipeListener
		listmessage.setOnSwipeListener(new OnSwipeListener() {
			
			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}
			
			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});
	}
	static int checkId = 0; 
	public Dialog getPaymentDialog(final Context context,final Map map) {
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_payment, null);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		Button btn_makesure = (Button) view.findViewById(R.id.btn_makesure);
		RelativeLayout rl_wx = (RelativeLayout) view.findViewById(R.id.rl_wx);
		RelativeLayout rl_zfb = (RelativeLayout) view.findViewById(R.id.rl_zfb);
		RelativeLayout rl_money = (RelativeLayout) view.findViewById(R.id.rl_money);
		final ImageView iv_wx_right = (ImageView) view.findViewById(R.id.iv_wx_right);
		final ImageView iv_zfb_right = (ImageView) view.findViewById(R.id.iv_zfb_right);
		final ImageView iv_face2face_right = (ImageView) view.findViewById(R.id.iv_face2face_right);
		if (checkId==1) {
			iv_wx_right.setBackgroundResource(R.drawable.color);
			iv_zfb_right.setBackgroundResource(R.drawable.nocolor);
			iv_face2face_right.setBackgroundResource(R.drawable.nocolor);
		}else if (checkId==2) {
			iv_wx_right.setBackgroundResource(R.drawable.nocolor);
			iv_zfb_right.setBackgroundResource(R.drawable.color);
			iv_face2face_right.setBackgroundResource(R.drawable.nocolor);
		}else if (checkId == 3) {
			iv_wx_right.setBackgroundResource(R.drawable.nocolor);
			iv_zfb_right.setBackgroundResource(R.drawable.nocolor);
			iv_face2face_right.setBackgroundResource(R.drawable.color);
		}
		rl_wx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iv_wx_right.setBackgroundResource(R.drawable.color);
				iv_zfb_right.setBackgroundResource(R.drawable.nocolor);
				iv_face2face_right.setBackgroundResource(R.drawable.nocolor);
				checkId = 1;
			}
		});
		rl_zfb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iv_wx_right.setBackgroundResource(R.drawable.nocolor);
				iv_zfb_right.setBackgroundResource(R.drawable.color);
				iv_face2face_right.setBackgroundResource(R.drawable.nocolor);
				checkId = 2;
			}
		});
		rl_money.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iv_wx_right.setBackgroundResource(R.drawable.nocolor);
				iv_zfb_right.setBackgroundResource(R.drawable.nocolor);
				iv_face2face_right.setBackgroundResource(R.drawable.color);
				checkId = 3;
			}
		});
		btn_makesure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (checkId) {
				   
				case 0:// 没有选择
					Toast.makeText(context, "您还没有选择支付方式", Toast.LENGTH_LONG).show();
					break;
					
				case 1:// 选择了微信
					payType = "1";
					ProgressDialog dialog = ProgressDialog.show(getActivity(), getString(R.string.app_tip), getString(R.string.getting_prepayid));
					getPrepayId("10", "1233848001", "1217752501201407033233368018", "测试商品","http://www.baidu.com");
					genPayReq();
					sendPayReq();
					break;
					
				case 2:// 选择了支付宝
					payType = "2";
					check(null);
					Message message = Message.obtain();
					message.obj = map;
					message.what = 3;
					mHandler.sendMessage(message);
					pay("测试的商品",payType,map.get("actFee").toString(),map.get("orderNum").toString());
					
					break;
					
				case 3:// 选择了现金支付
					payType = "3";
					Toast.makeText(context, "您选择了现金支付", Toast.LENGTH_LONG).show();
					break;

				default:
					break;
					
				}
				
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkId = 0;
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return null;
	}
	 public void pay(String subject,String body,String price,String orderNum ) {
		// 订单
		String orderInfo = getOrderInfo(subject, body, price,orderNum);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(getActivity());
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	 /**
		 * check whether the device has authentication alipay account.
		 * 查询终端设备是否存在支付宝认证账户
		 * 
		 */
		public void check(View v) {
			Runnable checkRunnable = new Runnable() {

				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask payTask = new PayTask(getActivity());
					// 调用查询接口，获取查询结果
					boolean isExist = payTask.checkAccountIfExist();

					Message msg = new Message();
					msg.what = SDK_CHECK_FLAG;
					msg.obj = isExist;
					mHandler.sendMessage(msg);
				}
			};

			Thread checkThread = new Thread(checkRunnable);
			checkThread.start();

		}

		/**
		 * get the sdk version. 获取SDK版本号
		 * 
		 */
		public void getSDKVersion() {
			PayTask payTask = new PayTask(getActivity());
			String version = payTask.getVersion();
			Toast.makeText(getActivity(), version, Toast.LENGTH_SHORT).show();
		}

		/**
		 * create the order info. 创建订单信息
		 * 
		 */
		public String getOrderInfo(String subject, String body, String price ,String orderNum) {
			// 签约合作者身份ID
			String orderInfo = "partner=" + "\"" + PARTNER + "\"";

			// 签约卖家支付宝账号
			orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

			// 商户网站唯一订单号
//			orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
			orderInfo += "&out_trade_no=" + "\"" + orderNum + "\"";

			// 商品名称
			orderInfo += "&subject=" + "\"" + subject + "\"";

			// 商品详情
			orderInfo += "&body=" + "\"" + body + "\"";

			// 商品金额
			orderInfo += "&total_fee=" + "\"" + price + "\"";

			// 服务器异步通知页面路径
			orderInfo += "&notify_url=" + "\"" + Constant.alipayNotify
					
					+ "\"";
//			"http://notify.msp.hk/notify.htm"

			// 服务接口名称， 固定值
			orderInfo += "&service=\"mobile.securitypay.pay\"";

			// 支付类型， 固定值
			orderInfo += "&payment_type=\"1\"";

			// 参数编码， 固定值
			orderInfo += "&_input_charset=\"utf-8\"";

			// 设置未付款交易的超时时间
			// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
			// 取值范围：1m～15d。
			// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
			// 该参数数值不接受小数点，如1.5h，可转换为90m。
			orderInfo += "&it_b_pay=\"30m\"";

			// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
			// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

			// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
			orderInfo += "&return_url=\"m.alipay.com\"";

			// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
			// orderInfo += "&paymethod=\"expressGateway\"";

			return orderInfo;
		}

		/**
		 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
		 * 
		 */
		public String getOutTradeNo() {
			SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
					Locale.getDefault());
			Date date = new Date();
			String key = format.format(date);

			Random r = new Random();
			key = key + r.nextInt();
			key = key.substring(0, 15);
			return key;
		}

		/**
		 * sign the order info. 对订单信息进行签名
		 * 
		 * @param content
		 *            待签名订单信息
		 */
		public String sign(String content) {
			return SignUtils.sign(content, RSA_PRIVATE);
		}

		/**
		 * get the sign type we use. 获取签名方式
		 * 
		 */
		public String getSignType() {
			return "sign_type=\"RSA\"";
		}

		//删除item
		void delete(int index){
			int receiveId = messageListData.get(index).getReceiveId();
//			ToastUtil.show(mContext, "sendId:"+sendId+";index:"+index);
			App.dbHelper.deleteReceiveNotice(App.db, ""+receiveId);
			messageListData.clear();
			messageListData =  App.dbHelper.queryPaymentByOrderStatus(App.db,"6","7");
			adapter = new OtherMessageAdapter(mContext, messageListData);
			listmessage.setAdapter(adapter);
			adapter.notifyData(messageListData);
			sendReceiver();
		}
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	//发送有未读消息广播
		public void sendReceiver(){
			//新建一个Intent.ACTION_VIEW类型的intent。  
		    Intent intent = new Intent("NewMsgReceiver_Action");  
		    //发送广播，注册了该类型的广播接收者就会接受到。  
		    mContext.sendBroadcast(intent); 
		}
		/**
		 * getPrepayId
		 * @param 金额，商户号，订单号，商品详情，异步通知网址
		 * @return
		 */
		public void getPrepayId(String total_fee,String mch_id,String out_trade_no,String body,String notify_url){
			 if(!msgApi.isWXAppInstalled())
	            {
	                Toast.makeText(getActivity(), "没有安装微信", Toast.LENGTH_LONG).show();
	                return;
	            }            
	            if(!msgApi.isWXAppSupportAPI())
	            {
	            	Toast.makeText(getActivity(), "当前版本不支持支付功能", Toast.LENGTH_LONG).show();
	                
	                return;
	            }
			HashMap<String, Object> json = new HashMap<String, Object>();
			String	nonceStr = genNonceStr();
			
			json.put("app_id", APP_ID);
			json.put("total_fee", total_fee);
			json.put("mch_id", mch_id);
			json.put("out_trade_no", out_trade_no);//订单号
			json.put("nonce_str", nonceStr);//随机字符串
			json.put("body", body);//商品详情
			json.put("spbill_create_ip", "127.0.0.1");//终端IP
			json.put("notify_url", notify_url);//接收微信支付异步通知回调地址
			json.put("trade_type", "APP");//接收微信支付异步通知回调地址
			
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", APP_ID));
			packageParams.add(new BasicNameValuePair("body", body));
			packageParams.add(new BasicNameValuePair("mch_id", MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", notify_url));
			packageParams.add(new BasicNameValuePair("out_trade_no",out_trade_no));
			packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", total_fee));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring =toXml(packageParams);
			
			String params = ParamsUtil.getUrlParamsByMap(json);
			MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.checkSign,
					new Response.Listener<Object>() {

						@Override
						public void onResponse(Object arg0) {
							LogUtils.e("签到check  "+ arg0.toString());
								try {
									resultunifiedorder= (Map<String, String>) arg0;
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
					
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							LogUtils.e("签到check异常  "+ arg0.toString());
							Toast.makeText(getActivity(), "查询是否签到失败!", Toast.LENGTH_LONG).show();
							arg0.printStackTrace();
						}
					}, params);
			App.getQueue().add(req);
			
		}
		private void genPayReq() {
			StringBuilder sb = new StringBuilder();	
			
			req.appId = APP_ID;
			req.partnerId = MCH_ID;
			req.prepayId = resultunifiedorder.get("prepay_id");
			req.packageValue = "Sign=WXPay";
			req.nonceStr = genNonceStr();
			req.timeStamp = String.valueOf(genTimeStamp());


			List<NameValuePair> signParams = new LinkedList<NameValuePair>();
			signParams.add(new BasicNameValuePair("appid", req.appId));
			signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
			signParams.add(new BasicNameValuePair("package", req.packageValue));
			signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
			signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
			signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

			req.sign = genAppSign(signParams);

			sb.append("sign\n"+req.sign+"\n\n");
			
			Log.e("orion", signParams.toString());

		}
		private String genNonceStr() {
			Random random = new Random();
			return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
		}
		private long genTimeStamp() {
			return System.currentTimeMillis() / 1000;
		}
		private String genAppSign(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName());
				sb.append('=');
				sb.append(params.get(i).getValue());
				sb.append('&');
			}
			sb.append("key=");
			sb.append(API_KEY);

	        sb.append("sign str\n"+sb.toString()+"\n\n");
			String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
			Log.e("orion",appSign);
			return appSign;
		}
		private void sendPayReq() {
			

			msgApi.registerApp(APP_ID);
			msgApi.sendReq(req);
		}
		/**
		 生成签名
		 */

		private String genPackageSign(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName());
				sb.append('=');
				sb.append(params.get(i).getValue());
				sb.append('&');
			}
			sb.append("key=");
			sb.append(API_KEY);
			

			String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
			Log.e("orion",packageSign);
			return packageSign;
		}
		private String toXml(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();
			sb.append("<xml>");
			for (int i = 0; i < params.size(); i++) {
				sb.append("<"+params.get(i).getName()+">");


				sb.append(params.get(i).getValue());
				sb.append("</"+params.get(i).getName()+">");
			}
			sb.append("</xml>");

			Log.e("orion",sb.toString());
			return sb.toString();
		}
		/*
		 * 上传支付结果
		 * 
		 */
		private void updatePaymentMessage(Map map){
			HashMap<String, Object> json = new HashMap<String, Object>();
			String	nonceStr = genNonceStr();
			
				
			
			json.put("payType",payType);
			json.put("orderNum", map.get("orderNum"));
			json.put("actFee", map.get("actFee"));
			json.put("userCode", map.get("userCode"));
			json.put("orgCode", map.get("orgCode"));
			
			String params = ParamsUtil.getUrlParamsByMap(json);
			MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.updatePayment,
					new Response.Listener<Object>() {

						@Override
						public void onResponse(Object arg0) {
							LogUtils.e("上传支付返回"+ arg0.toString());
							}
					
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							LogUtils.e("上传支付异常  "+ arg0.toString());
							Toast.makeText(getActivity(), "上传支付失败!", Toast.LENGTH_LONG).show();
							arg0.printStackTrace();
						}
					}, params);
			App.getQueue().add(req);
			
			
			
		}
	
}

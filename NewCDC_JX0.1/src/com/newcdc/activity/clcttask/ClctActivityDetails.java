package com.newcdc.activity.clcttask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MyArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.address.DaoFactory;
import com.address.MySpinnerAdapter;
import com.address.jifei.FeeArea;
import com.address.jifei.FeeAreaDao;
import com.address.jifei.StandardFee;
import com.address.jifei.StandardFeeDao;
import com.address.jifei.WeightRange;
import com.address.jifei.WeightRangeDao;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.platform.comapi.map.h;
import com.dtr.zxing.activity.CaptureActivity;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.application.CDCApplication;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.db.JxClctDao;
import com.newcdc.model.FastLanBean;
import com.newcdc.model.JxClctBean;
import com.newcdc.service.CollectionUploadService;
import com.newcdc.tools.Global;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.InfoDialog;
import com.newcdc.ui.XFAudio;
import com.newcdc.util.DialogUtils;

public class ClctActivityDetails extends BaseActivity implements OnClickListener {
	private String senderPhoneNum;//寄件人电话号
	private String orderNum ; //订单号
	private boolean isOnclick = true; //是否可以点击
	private Button btn_payment;//收费
	private EditText mmainmail, edt_weight, edt_fee, edt_receiverName,
			edt_receiverMobile, edt_senderName, edt_senderMobile;
	private Spinner sp_payment, sp_serviceType;
	private String orderWeight, serviceType, startTime, sendType,
			senderProvString = "", senderProvCodeString = null,
			senderCityString = "", senderCityCodeString = null,
			senderCountyString = "", senderCountyCodeString = null,
			receiverProvString = "", receiverProvCodeString = null,
			receiverCityString = "", receiverCityCodeString = null,
			receiverCountyString = "", receiverCountyCodeString = null;// 省市县根据代码关联起来
	private TextView tv_dingdanhao, tv_useIntegral, tv_actFee, tv_startTime;
	private LinearLayout ll_dingdanhao, ll_useIntegral, ll_startTime;
	private Button mbtn_mailsay, msaoyisao, btn_tijiaolanshou, back, btn_jipao;
	private FastLanBean fastLanBean = new FastLanBean();
	// 揽投Bean，江西E速递用
	private JxClctBean jxClctBean;
	private UserInfoUtils userInfo;
	private JxClctDao mJxClctDao;
	private String results = "";
	private EditText sendAddress;
	private EditText receiverAddress;
	private Spinner sendProvince, sendCity, sendCounty;
	private Spinner receiverProvince, receiverCity, receiverCounty;
	private List<Map<String, String>> receiveprovinceList, receivecityList,
			receivecountyList;
	public ProgressDialog myDialog = null;
	private List<Map<String, String>> provinceList, cityList, countyList;
	private List<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
	private List<String> paymentList = new ArrayList<String>();
	private List<String> paymentList1 = new ArrayList<String>();
	private Map<String, String> senderaddressName = new HashMap<String, String>();
	private Map<String, String> receiveraddressName = new HashMap<String, String>();
	private java.text.DecimalFormat df = new java.text.DecimalFormat("#.#");
	private TextView tx_no_title;
	private Gather_MsgDao gather_MsgDao;
	private String reservenum = "";
	private String mailType = "0";
	private String companyid = "";
	private String userSid = "";// 快递帮手注册用户的id
	private String userIntegral = "0";// 使用积分
	private String userValidIntegral = "0";// 用户可用积分
	private String actFee = "";// 实收资费
	private String integral = "";
	private List<Map<String, String>> ScityList = null, RcityList = null,
			ScountyList = null, RcountyList = null;
	private Map<String, String> sendP, sendC, sendCou, receiveP, receiveC,
			receiveCou;
	private FeeAreaDao feeAreaDao;
	private StandardFeeDao standardFeeDao;
	private WeightRangeDao weightRangeDao;
	private List<FeeArea> feeAreasList;
	private List<WeightRange> weightRangesList;
	private List<StandardFee> standardFeesList;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				isOnclick =false;
				btn_payment.setEnabled(false);
				break;
            case 1:
            	isOnclick =true;
//            	btn_payment.setBackgroundColor(Color.parseColor("#277c09"));
//            	btn_payment.setBackgroundResource(R.drawable.sound_my_normal);
            	btn_payment.setBackgroundResource(R.drawable.btn_big_up);
            	btn_payment.setEnabled(true);
            	btn_payment.setText("收费");
				break;
            case 2:
            	btn_payment.setText(msg.obj+"秒");
            	break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clct_jx_details);
		init();
		addListener();
		initData();
		edt_weight.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if ("请选择".equals(sp_serviceType.getSelectedItem().toString())) {
					showdiag("请先选择业务类型");
					return;
				}
				if ("快递包裹".equals(sp_serviceType.getSelectedItem().toString())) {
					serviceType = "1";
				} else if ("标准快件".equals(sp_serviceType.getSelectedItem()
						.toString())) {
					serviceType = "2";
				}
				if (!arg0.toString().equals("") && null != arg0.toString()) {
					if (Double.valueOf(arg0.toString()) > 1000000) {
						Toast.makeText(ClctActivityDetails.this, "请输的重量超过范围",
								Toast.LENGTH_SHORT).show();
						edt_fee.setText("");
						return;
					}
					FindCode(arg0.toString());
				} else {
					edt_fee.setText("");
					Toast.makeText(ClctActivityDetails.this, "请输入重量",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		edt_fee.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				/*if (!arg0.toString().equals("") && null != arg0.toString()) {
					if (Double.parseDouble(userValidIntegral) >= Double
							.parseDouble(userIntegral.toString().trim())) {
						actFee = (Double.parseDouble(arg0.toString().trim()) - Double
								.parseDouble(userIntegral.toString().trim()))
								+ "";
					} else {
						actFee = (Double.parseDouble(arg0.toString().trim()) - Double
								.parseDouble(userValidIntegral.toString()
										.trim()))
								+ "";
					}
					if (Double.parseDouble(actFee) <= 0.0) {
						tv_actFee.setText("0.0");
					} else {
						tv_actFee.setText("" + actFee);
					}
				} else {
					tv_actFee.setText("0.0");f
				}*/
				/*if (!arg0.toString().equals("") && null != arg0.toString()) {
					if (Double.parseDouble(userValidIntegral.toString().trim())>=Double
								.parseDouble(edt_fee.getText().toString())) {
						tv_useIntegral.setText(edt_fee.getText().toString());
						tv_actFee.setText("0.0");
					}else {
						if ("积分抵扣".equals(sp_payment.getSelectedItem().toString())) {
							tv_useIntegral.setText(Html.fromHtml("<font color=\'#FF0000\'>目前的积分无法全额支付邮件费用，请修改支付方式</font>)"));
						}else {
							tv_useIntegral.setText(Html.fromHtml("<font color=\'#00EE00\'>目前的积分无法全额支付邮件费用</font>)"));
						}
						tv_actFee.setText(edt_fee.getText().toString());
					}
				}*/
				if ("积分抵扣".equals(sp_payment.getSelectedItem().toString())) {
				    if (Double.parseDouble(userValidIntegral.toString().trim())>=Double
							.parseDouble(edt_fee.getText().toString())) {
				    	tv_useIntegral.setText(edt_fee.getText().toString() + "");
				    	tv_actFee.setText("0.0");
				    }else {
						Builder builder = new AlertDialog.Builder(ClctActivityDetails.this);
						builder.setMessage("拥有的积分不足以支付邮费");
						tv_useIntegral.setText("0");
						tv_actFee.setText(edt_fee.getText().toString());
						builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				}else {
					tv_useIntegral.setText("0");
					tv_actFee.setText(edt_fee.getText().toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
	}

	private void init() {
		feeAreaDao = DaoFactory.getInstance().getFeeAreaDao(
				ClctActivityDetails.this);
		standardFeeDao = DaoFactory.getInstance().getStandardFeeDao(
				ClctActivityDetails.this);
		weightRangeDao = DaoFactory.getInstance().getWeightRangeDao(
				ClctActivityDetails.this);
		userInfo = new UserInfoUtils(ClctActivityDetails.this);
		mJxClctDao = DeliverDaoFactory.getInstance().getJxClctDao(
				getApplicationContext());
		gather_MsgDao = DeliverDaoFactory.getInstance().getGather_msgdao(
				getApplicationContext());
		
		btn_payment = (Button) findViewById(R.id.btn_payment);
		btn_payment.setOnClickListener(this);
		mmainmail = (EditText) findViewById(R.id.edt_mainmail);
		mmainmail.setOnClickListener(this);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		mbtn_mailsay = (Button) findViewById(R.id.jxmailsay);
		mbtn_mailsay.setOnClickListener(this);
		msaoyisao = (Button) findViewById(R.id.jxsaoyisao);
		btn_jipao = (Button) findViewById(R.id.btn_jipao);
		msaoyisao.setOnClickListener(this);
		btn_jipao.setOnClickListener(this);
		tv_dingdanhao = (TextView) findViewById(R.id.tv_dingdanhao);
		tv_startTime = (TextView) findViewById(R.id.tv_startTime);
		tv_useIntegral = (TextView) findViewById(R.id.tv_useIntegral);
		tv_actFee = (TextView) findViewById(R.id.tv_actFee);
		ll_dingdanhao = (LinearLayout) findViewById(R.id.ll_dingdanhao);
		ll_useIntegral = (LinearLayout) findViewById(R.id.ll_useIntegral);
		ll_startTime = (LinearLayout) findViewById(R.id.ll_startTime);
		edt_weight = (EditText) findViewById(R.id.edt_weight);
		edt_fee = (EditText) findViewById(R.id.edt_fee);
		sp_payment = (Spinner) findViewById(R.id.sp_payment);
		sp_serviceType = (Spinner) findViewById(R.id.serviceType);
		edt_receiverName = (EditText) findViewById(R.id.edt_receiverName);
		edt_receiverMobile = (EditText) findViewById(R.id.edt_receiverMobile);
		edt_senderName = (EditText) findViewById(R.id.edt_senderName);
		edt_senderMobile = (EditText) findViewById(R.id.edt_senderMobile);
		btn_tijiaolanshou = (Button) findViewById(R.id.btn_jxtijiaolanshou);
		btn_tijiaolanshou.setOnClickListener(this);
		sendAddress = (EditText) findViewById(R.id.sendAddress);
		sendProvince = (Spinner) findViewById(R.id.sendProvince);
		sendCity = (Spinner) findViewById(R.id.sendCity);
		sendCounty = (Spinner) findViewById(R.id.sendCounty);
		receiverAddress = (EditText) findViewById(R.id.receiverAddress);
		receiverProvince = (Spinner) findViewById(R.id.receiverProvince);
		receiverCity = (Spinner) findViewById(R.id.receiverCity);
		receiverCounty = (Spinner) findViewById(R.id.receiverCounty);
		tx_no_title = (TextView) findViewById(R.id.tx_no_title);
		spinnerServiceType();
	}

	private void initData() {
		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			tx_no_title.setText(bundle.getString("CollectionType"));
			if ("无派单揽收".equals(bundle.getString("CollectionType"))) {// 无派单时没有订单号
				spinnerInit_Wu();
				ll_dingdanhao.setVisibility(View.GONE);
				ll_useIntegral.setVisibility(View.GONE);
				ll_startTime.setVisibility(View.GONE);
				btn_payment.setVisibility(View.GONE);
			} else {// 派单信息
				ll_useIntegral.setVisibility(View.VISIBLE);
				ll_startTime.setVisibility(View.VISIBLE);
				spinnerInit();
				orderNum = bundle.getString("reservenum");
				tv_dingdanhao.setText(orderNum);
				edt_receiverName.setText(bundle.getString("receiverName"));
				receiverAddress.setText(bundle.getString("receiverStreet"));
				
				edt_receiverMobile.setText(bundle.getString("receiverMobile"));
				serviceType = bundle.getString("serviceType");
				// 业务类型
				if (serviceType.equals("1")) {
					sp_serviceType.setSelection(1);
				} else if (serviceType.equals("2")) {
					sp_serviceType.setSelection(2);
				} else {
					sp_serviceType.setSelection(0);
				}
				// 下单类型 下单类型 1及时 2预约
				sendType = bundle.getString("sendType");
				LogUtils.e(" 下单类型-----------" + sendType);
				if (sendType.equals("2")) {
					startTime = bundle.getString("startTime");
					LogUtils.e("预约时间----------" + startTime);
				} else {
					ll_startTime.setVisibility(View.GONE);
				}
				// 付款方式 经过了修改没有了2：到付
				LogUtils.e("付款方式-----------" + bundle.getString("payment"));
				if ("1".equals(bundle.getString("payment"))) {
					sp_payment.setSelection(1);// 1：寄件现结 2：到付3:积分抵扣
				} else if ("2".equals(bundle.getString("payment"))) {
					sp_payment.setSelection(2);// 1：寄件现结 2：到付3:积分抵扣
				} else if ("3".equals(bundle.getString("payment"))) {
					sp_payment.setSelection(2);// 1：寄件现结 2：到付3:积分抵扣
				} else {
					sp_payment.setSelection(0);
				}
				tv_startTime.setText(startTime + "");
				orderWeight = bundle.getString("orderWeight");
				edt_weight.setText(orderWeight);
				senderProvString = bundle.getString("senderProv");
				senderProvCodeString = bundle.getString("senderProvCode");
				senderCityString = bundle.getString("senderCity");
				senderCityCodeString = bundle.getString("senderCityCode");
				senderCountyString = bundle.getString("senderCounty");
				senderCountyCodeString = bundle.getString("senderCountyCode");
				receiverProvString = bundle.getString("receiverProv");
				receiverProvCodeString = bundle.getString("receiverProvCode");
				receiverCityString = bundle.getString("receiverCity");
				receiverCityCodeString = bundle.getString("receiverCityCode");
				receiverCountyCodeString = bundle
						.getString("receiverCountyCode");
				receiverCountyString = bundle.getString("receiverCounty");
				LogUtils.e("" + senderProvCodeString + ""
						+ senderCityCodeString + "" + senderCountyCodeString);
				LogUtils.e("" + receiverProvCodeString + ""
						+ receiverCityCodeString + ""
						+ receiverCountyCodeString);
				reservenum = bundle.getString("reservenum");
				userSid = bundle.getString("userSid");
				userIntegral = bundle.getString("userIntegral");
				userValidIntegral = bundle.getString("userValidIntegral");
				if ("".equals(userValidIntegral)) {
					userValidIntegral = "0";
				}
				/*// 直接在跳转的时候就进行了判断，将积分的内容进行的改变,并将积分进行显示
				if ("积分抵扣".equals(sp_payment.getSelectedItem().toString())) {
				    if (Double.parseDouble(userValidIntegral.toString().trim())>=Double
							.parseDouble(edt_fee.getText().toString())) {
				    	tv_useIntegral.setText(edt_fee.getText().toString() + "");
				    }else {
						Builder builder = new AlertDialog.Builder(this);
						builder.setMessage("拥有的积分不足以支付邮费");
						builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				}else {
					tv_useIntegral.setText(userIntegral);
				}*/
				
				tv_useIntegral.setText(userIntegral);
				LogUtils.e("用户可用积分userValidIntegral：" + userValidIntegral
						+ "   用户想用积分userIntegral: " + userIntegral);
			}
			edt_senderName.setText(bundle.getString("clientname"));
			senderPhoneNum = bundle.getString("phone");
			edt_senderMobile.setText(senderPhoneNum);
			sendAddress.setText(bundle.getString("address"));
			mailType = bundle.getString("mailType");
			companyid = bundle.getString("companyid");
		}
	}

	/**
	 * 计费算法 第一步:查询计费区间表，通过产品类型，发货地，收货地，查询计费区间ID。步骤是首先用发货市代码，收货市代码，即市市模式，
	 * 匹配计费区间表中的send_area_code和receive_area_code
	 * ，如果匹配不上，再去匹配市省，再次匹配不上，最后匹配省省，最终正确结果应查到一条记录，如无法查到或大于1条，抛出异常：无法正确匹配计费区间。
	 * 第二步：查询重量范围表，通过产品类型，重量范围，大于多少小于等于多少的范围，查询记录，同样获取一条记录
	 * 第三步：根据计费区间ID，重量范围ID，在标准资费表查询到唯一一条资费记录，如果无法查到，抛异常：无法查询到资费标准。
	 * 查到的资费记录数据有，首重，首重费用，续重，续重费用。
	 * */
	public void FindCode(String weiString) {
		feeAreasList=null;
		sendP = (Map<String, String>) sendProvince.getSelectedItem();
		sendC = (Map<String, String>) sendCity.getSelectedItem();
		sendCou = (Map<String, String>) sendCounty.getSelectedItem();
		receiveP = (Map<String, String>) receiverProvince.getSelectedItem();
		receiveC = (Map<String, String>) receiverCity.getSelectedItem();
		receiveCou = (Map<String, String>) receiverCounty.getSelectedItem();
		// LogUtils.e("发件人地址：  " + sendP.get("value") + sendC.get("value")
		// + sendCou.get("value"));
		String send_area_code = null, receive_area_code = null;
		// TODO 判断城市编码是不是有 如果这些都是null 就是无派单揽收 code都是要查询出来的
//		if (null == senderCityCodeString || null == receiverCityCodeString) {
			send_area_code = feeAreaDao.querySenderCode(sendC.get("value"));
			receive_area_code = feeAreaDao.queryCode(receiveC.get("value"));
			if (null != send_area_code && null != receive_area_code) {
				feeAreasList = feeAreaDao.queryFeeIDByCode(send_area_code,
						receive_area_code, Integer.valueOf(serviceType));// 市市
			}
			if (null != feeAreasList && feeAreasList.size() == 1) {
				Fee(weiString, feeAreasList.get(0).getFee_area_id());
			} else {
				send_area_code = feeAreaDao.querySenderCode(sendC.get("value"));
				receive_area_code = feeAreaDao.queryCode(receiveP.get("value"));
				if (null != send_area_code && null != receive_area_code) {
					feeAreasList = feeAreaDao.queryFeeIDByCode(send_area_code,
							receive_area_code, Integer.valueOf(serviceType));// 市省
				}
				if (null != feeAreasList && feeAreasList.size() == 1) {
					Fee(weiString, feeAreasList.get(0).getFee_area_id());
				} else {
					send_area_code = feeAreaDao.querySenderCode(sendP.get("value"));
					receive_area_code = feeAreaDao.queryCode(receiveP
							.get("value"));
					if (null != send_area_code && null != receive_area_code) {
						feeAreasList = feeAreaDao.queryFeeIDByCode(
								send_area_code, receive_area_code,
								Integer.valueOf(serviceType));// 省省
					}
					if (null != feeAreasList && feeAreasList.size() == 1) {
						Fee(weiString, feeAreasList.get(0).getFee_area_id());
					} else {
						Toast.makeText(ClctActivityDetails.this, "查询计费区间失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
//		} else {
//			feeAreasList = feeAreaDao.queryFeeIDByCode(senderCityCodeString,
//					receiverCityCodeString, Integer.valueOf(serviceType));// 市市
//			if (null != feeAreasList && feeAreasList.size() == 1) {
//				Fee(weiString, feeAreasList.get(0).getFee_area_id());
//			} else {
//				feeAreasList = feeAreaDao.queryFeeIDByCode(
//						senderCityCodeString, receiverProvCodeString,
//						Integer.valueOf(serviceType));// 市省
//				if (null != feeAreasList && feeAreasList.size() == 1) {
//					Fee(weiString, feeAreasList.get(0).getFee_area_id());
//				} else {
//					feeAreasList = feeAreaDao.queryFeeIDByCode(
//							senderProvCodeString, receiverProvCodeString,
//							Integer.valueOf(serviceType));// 省省
//					if (null != feeAreasList && feeAreasList.size() == 1) {
//						Fee(weiString, feeAreasList.get(0).getFee_area_id());
//					} else {
//						Toast.makeText(NoClctActivityJX.this, "查询计费区间失败",
//								Toast.LENGTH_SHORT).show();
//					}
//				}
//			}
//		}
	}

	private void Fee(String weiString, int fee_area_id) {
		weightRangesList=null;
		standardFeesList=null;
		double weight = Double.valueOf(weiString);
		int weight_min = 0;
		int weight_max = 500;
		if (weight > 0 && weight <= 500) {
			weight_min = 0;
			weight_max = 500;
		} else if (weight > 500 && weight <= 1000000) {
			weight_min = 500;
			weight_max = 1000000;
		}
		weightRangesList = weightRangeDao.queryWeight_range_id(weight_min,
				weight_max, Integer.valueOf(serviceType));
		LogUtils.e("-------" + weightRangesList.size());
		if (null != weightRangesList && weightRangesList.size() == 1) {
			standardFeesList = standardFeeDao.queryFee(fee_area_id,
					weightRangesList.get(0).getWeight_range_id());
			LogUtils.e("-------" + standardFeesList.size());
			if (null != standardFeesList && standardFeesList.size() == 1) {
				/**
				 * 首先先判断订单重量或者前台的查询重量参数，是否小于等于首重重量，如果订单重量小于等于首重重量，
				 * 那么此订单费用直接等于首重费用。 举例，订单重量400g，资费记录首重500g，首重费用10元，订单总费用10元。
				 * 接上一步，如果订单重量大于首重，那么订单的总费用分为两块，一块是首重的费用，直接获得。
				 * 另一块是续重费用，由订单续重（订单总重减去资费首重），除以资费续重，除法向上取整，乘以续重的资费 整体的公式为
				 * 首重费用+【（订单总重 - 资费首重）/资费续重】 * 续重资费
				 * 
				 * 举例，资费记录首重1000g，首重费用10元，续重500g，续重费用 2元，订单重量2200g， 订单费用=10+【
				 * （2200-1000）/500】*2=16元，注意本处的除法，这个除法一定要向上取整，如果不是整除
				 * ，例如1200除以500，结果应等于3，即不满500g，也要按照500g计算。
				 * 
				 * */
				int first_weight = standardFeesList.get(0).getFirst_weight();// 首重
				int first_fee = standardFeesList.get(0).getFirst_fee();// 首重资费
				if (Double.valueOf(first_weight) >= weight) {// 小于等于首重重量
																// 费用就是首重费用
					LogUtils.e("费用---------" + first_fee);
					edt_fee.setText(first_fee + "");
				} else {// 大于首重
					int follow_weight = standardFeesList.get(0)
							.getFollow_weight();// 续重
					// follow_weight为0 还是按照首重资费算
					if (follow_weight == 0) {
						LogUtils.e("费用---------" + first_fee);
						edt_fee.setText(first_fee + "");
					} else {
						int follow_fee = standardFeesList.get(0)
								.getFollow_fee();// 续重资费
						Double double1 = weight - Double.valueOf(first_weight);// 总重-首重=续重
						Double double2 = (Double) double1 / follow_weight;// 续重
						LogUtils.e("double1   " + double1 + "   double2  "
								+ double2);
						Double double3 = Double.valueOf(first_fee)
								+ Math.ceil(double2)
								* Double.valueOf(follow_fee);
						LogUtils.e("资费----" + double3 + "    "
								+ Math.ceil(double2));
						edt_fee.setText(double3 + "");
					}
				}
			} else {
				Toast.makeText(ClctActivityDetails.this, "查询计费失败",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(ClctActivityDetails.this, "查询重量范围失败",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				provinceList = DaoFactory.getInstance()
						.getProvinceDao(ClctActivityDetails.this).getProvs();
				setSendSpinnerData();
				setReceiverSpinnerData();
			}
		}, 500);
	}

	protected void setReceiverSpinnerData() {
		if ("".equals(receiverProvString) || null == receiverProvString) {
			return;
		}
		for (int i = 0; i < provinceList.size(); i++) {
			if (provinceList.get(i).get("value").toString()
					.contains(receiverProvString)
					|| receiverProvString.contains(provinceList.get(i)
							.get("value").toString())) {
				receiverProvince.setSelection(i);
				RcityList = DaoFactory.getInstance()
						.getCityDao(ClctActivityDetails.this)
						.getCitys(provinceList.get(i).get("code"));
			}
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < RcityList.size(); i++) {
					if (RcityList.get(i).get("value").toString()
							.contains(receiverCityString)
							|| receiverCityString.contains(RcityList.get(i)
									.get("value").toString())) {
						receiverCity.setSelection(i);
						RcountyList = DaoFactory.getInstance()
								.getCountyDao(ClctActivityDetails.this)
								.getCountys(RcityList.get(i).get("code"));
					}
				}
			}
		}, 500);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < RcountyList.size(); i++) {
					if (RcountyList.get(i).get("value").toString()
							.contains(receiverCountyString)
							|| receiverCountyString.contains(RcountyList.get(i)
									.get("value").toString())) {
						receiverCounty.setSelection(i);
					}
				}
			}
		}, 2000);
	}

	protected void setSendSpinnerData() {
		if ("".equals(senderProvString) || null == senderProvString) {
			return;
		}
		for (int i = 0; i < provinceList.size(); i++) {
			if (provinceList.get(i).get("value").toString()
					.contains(senderProvString)
					|| senderProvString.contains(provinceList.get(i)
							.get("value").toString())) {
				sendProvince.setSelection(i);
				ScityList = DaoFactory.getInstance()
						.getCityDao(ClctActivityDetails.this)
						.getCitys(provinceList.get(i).get("code"));
			}
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ScityList.size(); i++) {
					if (ScityList.get(i).get("value").toString()
							.contains(senderCityString)
							|| senderCityString.contains(ScityList.get(i)
									.get("value").toString())) {
						sendCity.setSelection(i);
						ScountyList = DaoFactory.getInstance()
								.getCountyDao(ClctActivityDetails.this)
								.getCountys(ScityList.get(i).get("code"));
					}
				}
			}
		}, 500);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ScountyList.size(); i++) {
					if (ScountyList.get(i).get("value").toString()
							.contains(senderCountyString)
							|| senderCountyString.contains(ScountyList.get(i)
									.get("value").toString())) {
						sendCounty.setSelection(i);
					}
				}
			}
		}, 2000);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				FindCode(orderWeight);// 为了避免城市是null放这里
			}
		}, 1000);
	}

	private void addListener() {
		addSound();
	}

	private void spinnerInit_Wu() {
		paymentList.add("请选择");
		paymentList.add("寄件现结");
		paymentList.add("到付");
		MyArrayAdapter<String> transportAdapter = new MyArrayAdapter<String>(
				ClctActivityDetails.this, R.layout.pcc_spinner, R.id.name_text,
				paymentList);
		transportAdapter.setDropDownViewResource(R.layout.my_spinner_down_item);
		sp_payment.setAdapter(transportAdapter);
		initPro();
		initReceivePro();
	}

	private void spinnerInit() {
		paymentList.add("请选择");
		paymentList.add("寄件现结");
//		paymentList.add("到付");
		paymentList.add("积分抵扣");
		paymentList.add("支付宝支付");
		paymentList.add("微信支付");
		MyArrayAdapter<String> transportAdapter = new MyArrayAdapter<String>(
				ClctActivityDetails.this, R.layout.pcc_spinner, R.id.name_text,
				paymentList);
		transportAdapter.setDropDownViewResource(R.layout.my_spinner_down_item);
		sp_payment.setAdapter(transportAdapter);
		initPro();
		initReceivePro();
	}

	private void spinnerServiceType() {
		paymentList1.add("请选择");
		paymentList1.add("快递包裹");
		paymentList1.add("标准快件");
		MyArrayAdapter<String> transportAdapter = new MyArrayAdapter<String>(
				ClctActivityDetails.this, R.layout.pcc_spinner, R.id.name_text,
				paymentList1);
		transportAdapter.setDropDownViewResource(R.layout.my_spinner_down_item);
		sp_serviceType.setAdapter(transportAdapter);
		initPro();
		initReceivePro();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!"".equals(Global.jipaoWeight)) {
			edt_weight.setText(Global.jipaoWeight);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_payment:
			btn_payment.setBackgroundColor(Color.parseColor("#c0c0c0"));
			if (isOnclick) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						handler.sendEmptyMessage(0);
						for (int i = 1; i < 30; i++) {
							try {
								Thread.sleep(1000);
								int b = 30-i;
								Message message = Message.obtain();
								message.obj = b;
								message.what = 2;
								handler.sendMessage(message);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						handler.sendEmptyMessage(1);
						
					}
				}).start();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						HashMap<String, Object> json = new HashMap<String, Object>();
						json.put("senderPhoneNum", senderPhoneNum);
						json.put("orderNum", tv_dingdanhao.getText().toString());
						json.put("actFee", tv_actFee.getText().toString());
						json.put("userCode", userInfo.getUserName());
						json.put("orgCode", userInfo.getUserDelvorgCode());
						
						String params = com.newcdc.util.ParamsUtil.getUrlParamsByMap(json);
						com.newcdc.util.MyRequest<Object> req = new com.newcdc.util.MyRequest<Object>(Request.Method.POST, null, Global.CHARGE,
								new Response.Listener<Object>() {

									@Override
									public void onResponse(Object arg0) {
										LogUtils.e("gongjie"+ arg0.toString());
										Log.e("gongjie",arg0.toString() );
											try {
												JSONObject jsonObject = new JSONObject(arg0.toString());
												if (jsonObject.getInt("result")==1) {
													Toast.makeText(ClctActivityDetails.this, "发送成功请等待", Toast.LENGTH_LONG).show();
													
												}else {
													Toast.makeText(ClctActivityDetails.this, "发送失败请30秒后再发送", Toast.LENGTH_LONG).show();
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
								
								}, new Response.ErrorListener() {

									@Override
									public void onErrorResponse(VolleyError arg0) {
										Log.e("gongjie", arg0.toString());
										Toast.makeText(ClctActivityDetails.this, "发送请求失败", Toast.LENGTH_LONG).show();
										arg0.printStackTrace();
									}
								}, params);
						CDCApplication.getQueue().add(req);
						
					}
					}
				).start();
			}else {
				Toast.makeText(this, "请稍后再点击", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.back:
			Global.jipaoWeight = "";
			finish();
			break;
		case R.id.btn_jipao:
			startActivity(new Intent(ClctActivityDetails.this, JiPaoActivity.class));
			break;
		case R.id.mainmail:

			break;
		case R.id.btn_mailsay:

			break;
		case R.id.jxsaoyisao:
			startActivityForResult(new Intent(ClctActivityDetails.this,
					CaptureActivity.class), 111);
			break;
		// TODO 提交信息时带星号的做下验证
		case R.id.btn_jxtijiaolanshou:
			Editable editable = mmainmail.getText();
			if (editable != null) {
				String input = editable.toString();
				if (Utils.stringEmpty(input)) {
					showdiag("请输入正确的邮件号");
					return;
				}
				//对积分和付款方式的判断，新的规则是只能单使用积分和单付款 不能一起使用
				if ("积分抵扣".equals(sp_payment.getSelectedItem().toString())) {
					if (Double.parseDouble(edt_fee.getText().toString())!=Double.parseDouble(tv_useIntegral.getText().toString())) {
						Builder builder = new AlertDialog.Builder(ClctActivityDetails.this);
						builder.setMessage("拥有的积分不足以支付邮费");
						tv_useIntegral.setText("0");
						tv_actFee.setText(edt_fee.getText().toString());
						builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						AlertDialog dialog = builder.create();
						dialog.show();
						return;
					}
				}
				
				if ("请选择".equals(sp_payment.getSelectedItem().toString())) {
					showdiag("请选择付款方式");
					return;
				}
				if ("请选择".equals(sp_serviceType.getSelectedItem().toString())) {
					showdiag("请选择业务类型");
					return;
				}
				if (Utils.stringEmpty(edt_receiverName.getText().toString()
						.trim())) {
					showdiag("请输入正确的收件人姓名");
					return;
				}
				if (Utils.stringEmpty(edt_receiverMobile.getText().toString()
						.trim())) {
					showdiag("请输入正确的收件人电话");
					return;
				}
				if (!Utils.isAllNum(edt_receiverMobile.getText().toString()
						.trim())) {
					showdiag("请输入正确的收件人电话");
					return;
				}
				if (Utils.stringEmpty(receiverAddress.getText().toString()
						.trim())) {
					showdiag("请输入正确的收件人详细地址");
					return;
				}
				if (edt_weight.getText().toString() == null
						|| edt_weight.getText().toString().trim().length() <= 0) {
					showdiag("请输入正确的重量");
					return;
				}
				if (edt_fee.getText().toString() == null
						|| edt_fee.getText().toString().trim().length() <= 0) {
					// ||Double.parseDouble(shishoukuan.getText().toString().trim())==0
					showdiag("请输入正确的实收款");
					return;
				}
				saveData();
				gather_MsgDao.updateStauts(reservenum, "1");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 保存到数据库
	 * */
	private void saveData() {
		sendP = (Map<String, String>) sendProvince.getSelectedItem();
		sendC = (Map<String, String>) sendCity.getSelectedItem();
		sendCou = (Map<String, String>) sendCounty.getSelectedItem();
		receiveP = (Map<String, String>) receiverProvince.getSelectedItem();
		receiveC = (Map<String, String>) receiverCity.getSelectedItem();
		receiveCou = (Map<String, String>) receiverCounty.getSelectedItem();
		LogUtils.e("发件人地址：  " + sendP.get("value") + sendC.get("value")
				+ sendCou.get("value") + sendAddress.getText().toString());
		LogUtils.e("收件人地址：  " + receiveP.get("value") + receiveC.get("value")
				+ receiveCou.get("value")
				+ receiverAddress.getText().toString());
		jxClctBean = new JxClctBean();
		jxClctBean.setMailNo((mmainmail.getText().toString()));
		jxClctBean.setWeight(edt_weight.getText().toString()); // 重量
		jxClctBean.setReceiverName(edt_receiverName.getText().toString());// 收件人姓名
		jxClctBean.setReceiverMobile(edt_receiverMobile.getText().toString());// 收件人电话
		jxClctBean.setReceiverAddress(receiveP.get("value")
				+ receiveC.get("value") + receiveCou.get("value")
				+ receiverAddress.getText().toString());// 收件人地址
		jxClctBean.setFee(edt_fee.getText().toString());// 费用
		jxClctBean.setSenderName(edt_senderName.getText().toString());// 寄件人姓名
		jxClctBean.setSenderMobile(edt_senderMobile.getText().toString());// 寄件人电话
		jxClctBean.setSenderAddress(sendP.get("value") + sendC.get("value")
				+ sendCou.get("value") + sendAddress.getText().toString());// 寄件人地址
		jxClctBean.setOrgCode(userInfo.getUserDelvorgCode());// 机构号
		jxClctBean.setUserCode(userInfo.getUserName());// 工号
		jxClctBean.setUserName(userInfo.getRealname());// 姓名
		jxClctBean.setMailType(mailType);// 邮件类型 0：无派单揽收 1：有派单揽收
		jxClctBean.setSendType(sendType);// 下单类型 1及时 2预约
		jxClctBean.setStartTime(startTime);// 预约时间 是有类型是2时才会有预约时间
		if ("寄件现结".equals(sp_payment.getSelectedItem().toString())) {
			jxClctBean.setPayment("1");
		} else if ("到付".equals(sp_payment.getSelectedItem().toString())) {
			jxClctBean.setPayment("2");
		} else if ("积分抵扣".equals(sp_payment.getSelectedItem().toString())) {
			jxClctBean.setPayment("3");
		}
		if ("快递包裹".equals(sp_serviceType.getSelectedItem().toString())) {
			jxClctBean.setServiceType("1");
		} else if ("标准快件".equals(sp_serviceType.getSelectedItem().toString())) {
			jxClctBean.setServiceType("2");
		} else {
			jxClctBean.setServiceType("");
		}
		jxClctBean.setReservenum(reservenum);
		jxClctBean.setCompanyid(companyid);
		if ("快递包裹".equals(sp_serviceType.getSelectedItem().toString())) {
			jxClctBean.setServiceType("1");
		} else if ("标准快件".equals(sp_serviceType.getSelectedItem().toString())) {
			jxClctBean.setServiceType("2");
		} else {
			jxClctBean.setServiceType("");
		}
		jxClctBean.setUserSid(userSid);
		// if (Double.parseDouble(edt_fee.getText().toString().trim()) > Double
		// .parseDouble(userIntegral.toString().trim())) {
		// actFee = (Double.parseDouble(edt_fee.getText().toString().trim()) -
		// Double
		// .parseDouble(userIntegral.toString().trim())) + "";
		// integral = userIntegral;
		// } else {
		// actFee = 0 + "";
		// integral = edt_fee.getText().toString();
		// }
		// 先判断用户可用积分跟用户填写的积分大小 如果用户填写的积分小于可用的积分的话 就可以直接用用户填写的积分数
		// 如果填写的积分大于可用积分
		// 那就的用可用积分了
		if (Double.parseDouble(userValidIntegral) >= Double
				.parseDouble(userIntegral.toString().trim())) {
			if (Double.parseDouble(edt_fee.getText().toString().trim()) >= Double
					.parseDouble(userIntegral.toString().trim())) {
				integral = userIntegral;
			} else {
				integral = edt_fee.getText().toString();
			}
		} else {
			if (Double.parseDouble(edt_fee.getText().toString().trim()) >= Double
					.parseDouble(userValidIntegral.toString().trim())) {
				integral = userValidIntegral;
			} else {
				integral = edt_fee.getText().toString();
			}
		}
		jxClctBean.setUserIntegral(userIntegral);
		jxClctBean.setActFee(tv_actFee.getText().toString().trim());
		jxClctBean.setIntegral(integral);// 实际使用的积分数
		mJxClctDao.saveJxClctMessage(jxClctBean);
		Global.jipaoWeight = "";
		Intent intent = new Intent(ClctActivityDetails.this,
				CollectionUploadService.class);
		startService(intent);
		showcomplelteDiloage1();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 111) {
			results = data.getStringExtra("txtResult");
			LogUtils.e("results========" + results);
			if (!"".equals(results)) {
				if (true) {
					if (mailExits(results)) {
						// Toast.makeText(NoClctActivity.this, "该邮件号已使用",
						// Toast.LENGTH_SHORT).show();
						showdiag("该邮件号已使用");
					} else {
						mmainmail.setText(results);
					}
				}
				// else {
				// // Toast.makeText(NoClctActivity.this, "邮件号不正确,请重新扫入",
				// // Toast.LENGTH_SHORT).show();
				// showdiag("邮件号不正确,请重新扫入");
				// }
			}

		}
	}

	/**
	 * 揽收确定
	 */

	private void showcomplelteDiloage1() {
		final Dialog dialog = new Dialog(ClctActivityDetails.this,
				R.style.dialogss);
		View v = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_lantoudelete, null);
		dialog.setCancelable(false);
		Button info_cancel_lantou_delete = (Button) v
				.findViewById(R.id.info_cancel_lantou_delete);
		Button info_sure_lantou_delete = (Button) v
				.findViewById(R.id.info_sure_lantou_delete);
		dialog.setContentView(v, new LayoutParams(BaseActivity.width * 17 / 20,
				LayoutParams.WRAP_CONTENT));
		info_cancel_lantou_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				dialog.dismiss();

			}
		});
		info_sure_lantou_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getIntent().getExtras() != null) {
					Bundle bundle = getIntent().getExtras();
					if ("派单信息".equals(bundle.getString("CollectionType"))) {
						finish();
						dialog.dismiss();
					} else {
						mmainmail.setText("");
						dialog.dismiss();
					}
				}

			}
		});
		dialog.show();
	}

	/*
	 * 页面dialog展示
	 */
	private void showdiag(String info) {
		// InfoDialog infoDialog = new InfoDialog(this, info);
		InfoDialog infoDialog = new InfoDialog(ClctActivityDetails.this, info);
		infoDialog.Dismiss();
		infoDialog.Show();
	}

	/**
	 * 添加语音识别
	 */
	public void addSound() {
		XFAudio mailAudio = new XFAudio(ClctActivityDetails.this, mbtn_mailsay,
				mmainmail);
		mailAudio.toSay();
	}

	// 显示信息
	private void initPro() {
		provinceList = DaoFactory.getInstance()
				.getProvinceDao(ClctActivityDetails.this).getProvs();
		MySpinnerAdapter<Map<String, String>> provinceadapter = new MySpinnerAdapter<Map<String, String>>(
				ClctActivityDetails.this, R.layout.pcc_spinner, R.id.name_text,
				provinceList);
		MySpinnerAdapter<Map<String, String>> receiveradapter = new MySpinnerAdapter<Map<String, String>>(
				ClctActivityDetails.this, R.layout.pcc_spinner, R.id.name_text,
				provinceList);
		// 设置下拉列表的风格
		provinceadapter.setDropDownViewResource(R.layout.my_spinner_down_item);
		receiveradapter.setDropDownViewResource(R.layout.my_spinner_down_item);
		sendProvince.setAdapter(provinceadapter);
		sendProvince.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, String> provMap = (Map<String, String>) sendProvince
						.getItemAtPosition(position);
				setCityAdapter(provMap);
				senderaddressName.put("proName", provMap.get("value"));

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		receiverProvince
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						Map<String, String> provMap = (Map<String, String>) receiverProvince
								.getItemAtPosition(position);
						setCityAdapter(provMap);
						receiveraddressName.put("proName", provMap.get("value"));

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		sendCity.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, String> cityMap = (Map<String, String>) sendCity
						.getItemAtPosition(position);
				setCountyAdapter(cityMap);
				senderaddressName.put("cityName", cityMap.get("value"));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		receiverCity.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, String> cityMap = (Map<String, String>) receiverCity
						.getItemAtPosition(position);
				setCountyAdapter(cityMap);
				receiveraddressName.put("cityName", cityMap.get("value"));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void setCityAdapter(Map<String, String> provMap) {
		cityList = DaoFactory.getInstance().getCityDao(ClctActivityDetails.this)
				.getCitys(provMap.get("code"));
		if (cityList != null) {
			MySpinnerAdapter<Map<String, String>> cityadapter = new MySpinnerAdapter<Map<String, String>>(
					ClctActivityDetails.this, R.layout.pcc_spinner,
					R.id.name_text, cityList);
			cityadapter.setDropDownViewResource(R.layout.my_spinner_down_item);
			MySpinnerAdapter<Map<String, String>> receivercityadapter = new MySpinnerAdapter<Map<String, String>>(
					ClctActivityDetails.this, R.layout.pcc_spinner,
					R.id.name_text, cityList);
			receivercityadapter
					.setDropDownViewResource(R.layout.my_spinner_down_item);
			sendCity.setAdapter(cityadapter);
		}
	}

	private void setCountyAdapter(Map<String, String> cityMap) {
		countyList = DaoFactory.getInstance()
				.getCountyDao(ClctActivityDetails.this)
				.getCountys(cityMap.get("code"));
		if (countyList != null) {
			MySpinnerAdapter<Map<String, String>> countyadapter = new MySpinnerAdapter<Map<String, String>>(
					ClctActivityDetails.this, R.layout.pcc_spinner,
					R.id.name_text, countyList);
			countyadapter
					.setDropDownViewResource(R.layout.my_spinner_down_item);
			MySpinnerAdapter<Map<String, String>> receivercountyadapter = new MySpinnerAdapter<Map<String, String>>(
					ClctActivityDetails.this, R.layout.pcc_spinner,
					R.id.name_text, countyList);
			receivercountyadapter
					.setDropDownViewResource(R.layout.my_spinner_down_item);
			sendCounty.setAdapter(countyadapter);
		}
	}

	private void initReceivePro() {
		receiveprovinceList = DaoFactory.getInstance()
				.getProvinceDao(ClctActivityDetails.this).getProvs();
		MySpinnerAdapter<Map<String, String>> provinceadapter = new MySpinnerAdapter<Map<String, String>>(
				ClctActivityDetails.this, R.layout.pcc_spinner, R.id.name_text,
				receiveprovinceList);
		// 设置下拉列表的风格
		provinceadapter.setDropDownViewResource(R.layout.my_spinner_down_item);
		receiverProvince.setAdapter(provinceadapter);
		receiverProvince
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						Map<String, String> provMap = (Map<String, String>) receiverProvince
								.getItemAtPosition(position);
						setCityReceiveAdapter(provMap);
						receiveraddressName.put("proName", provMap.get("value"));

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		receiverCity.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, String> cityMap = (Map<String, String>) receiverCity
						.getItemAtPosition(position);
				setCountyReceiveAdapter(cityMap);
				receiveraddressName.put("cityName", cityMap.get("value"));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void setCityReceiveAdapter(Map<String, String> provMap) {
		receivecityList = DaoFactory.getInstance()
				.getCityDao(ClctActivityDetails.this)
				.getCitys(provMap.get("code"));
		if (receivecityList != null) {
			MySpinnerAdapter<Map<String, String>> cityadapter = new MySpinnerAdapter<Map<String, String>>(
					ClctActivityDetails.this, R.layout.pcc_spinner,
					R.id.name_text, receivecityList);
			cityadapter.setDropDownViewResource(R.layout.my_spinner_down_item);
			receiverCity.setAdapter(cityadapter);
		}
	}

	private void setCountyReceiveAdapter(Map<String, String> cityMap) {
		receivecountyList = DaoFactory.getInstance()
				.getCountyDao(ClctActivityDetails.this)
				.getCountys(cityMap.get("code"));
		if (receivecountyList != null) {
			MySpinnerAdapter<Map<String, String>> countyadapter = new MySpinnerAdapter<Map<String, String>>(
					ClctActivityDetails.this, R.layout.pcc_spinner,
					R.id.name_text, receivecountyList);
			countyadapter
					.setDropDownViewResource(R.layout.my_spinner_down_item);
			receiverCounty.setAdapter(countyadapter);
		}
	}

	/*
	 * 判断邮件是否存在
	 */
	public boolean mailExits(String mail_num) {
		int clct_Size = 0;
		clct_Size = mJxClctDao.selectMessage_mailNo(mail_num,
				userInfo.getUserDelvorgCode(), userInfo.getUserName()).size();
		if (clct_Size > 0) {
			return true;
		} else {
			return false;
		}

	}
}
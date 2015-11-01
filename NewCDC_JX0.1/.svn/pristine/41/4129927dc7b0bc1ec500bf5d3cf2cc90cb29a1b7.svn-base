package com.newcdc.activity.censcus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dtr.zxing.activity.CaptureActivity;
import com.newcdc.R;
import com.newcdc.adapter.DefinitionAdapter.IOnItemRightClickListener;
import com.newcdc.application.BaseActivity;
import com.newcdc.db.CustomerDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.FastLanDao;
import com.newcdc.db.MailTypeDao;
import com.newcdc.db.QsjGndmDao;
import com.newcdc.db.TransportTypeDao;
import com.newcdc.model.FastLanBean;
import com.newcdc.model.GatherInfoBean;
import com.newcdc.model.LanShouBean1;
import com.newcdc.model.LanShouBean2;
import com.newcdc.model.PaymentMoneyBean;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.InfoDialog;
import com.newcdc.ui.SwipeListView;

/**
 * 揽投统计
 * 
 * @author 李朋
 * 
 */
public class CountLanTouActivity extends Activity {
	private static final int REFRESH = 0;
	private SwipeListView swipeListView;
	private TextView tv_count;
	private List<FastLanBean> deleteLanBeans = new ArrayList<FastLanBean>();
	private FastLanDao fastLanDao;// 揽投
	private int gatherCount;// 揽收个数
	private CountLanTouAdapter countLanTouAdapter;
	// private ProgressDialog mdialog;
	private String orgCode, username, deviceId, realname, postcode;
	private int m_posion = 0;
	private Button scan;
	private String result = "";
	private Handler myHandler;
	private List<GatherInfoBean> gatherBeans;
	private int mRightWidth = 0;
	private IOnItemRightClickListener mListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_count_lantou);
		swipeListView = (SwipeListView) findViewById(R.id.fragment_count_lantou_listView);
		tv_count = (TextView) findViewById(R.id.fragment_count_lantou_textView);
		scan = (Button) findViewById(R.id.scan);

		initData();
		findViewById(R.id.censcue_lantou_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						finish();
					}
				});

		tv_count.setText(gatherCount + "");
		countLanTouAdapter = new CountLanTouAdapter(this, swipeListView.getRightViewWidth());
		swipeListView.setAdapter(countLanTouAdapter);
		swipeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Intent intent = new Intent(CountLanTouActivity.this,
						LanTouDetailActivity.class);
				List<FastLanBean> beans = countLanTouAdapter
						.getDeleteLanBeans();
				FastLanBean bean = beans.get(position);
				int _id = bean.getId();// TODO
				intent.putExtra("detail_id", _id);
				startActivity(intent);
			}
		});
		scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CountLanTouActivity.this,
						CaptureActivity.class);
//				startActivity(intent);
				startActivityForResult(intent, 1);
			}
		});

		// new Thread(new GameThread()).start();
		// // 实例化一个handler
		// myHandler = new Handler() {
		// // 接收到消息后处理
		// public void handleMessage(Message msg) {
		// switch (msg.what) {
		// case CountLanTouActivity.REFRESH:
		// mGameView.invalidate(); // 刷新界面
		// break;
		// }
		// super.handleMessage(msg);
		// }
		// };

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 111){
			result=	data.getStringExtra("txtResult");
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	// class GameThread implements Runnable {
	// public void run() {
	// while (!Thread.currentThread().isInterrupted()) {
	// Message message = new Message();
	// message.what = CountLanTouActivity.REFRESH;
	// // 发送消息
	// CountLanTouActivity.this.myHandler.sendMessage(message);
	// try {
	// Thread.sleep(100);
	// } catch (InterruptedException e) {
	// Thread.currentThread().interrupt();
	// }
	// }
	// }
	// }
	@Override
	protected void onRestart() {
		fastLanDao = DeliverDaoFactory.getInstance().getFastLanDao(
				getApplicationContext());
		if (!Utils.stringEmpty(result)) {
			List<FastLanBean> beans = fastLanDao.queryFastLanMessageI(result);
			if (beans != null && beans.size() > 0 && beans.size() < 2) {
				showcomplelteDiloage2(beans);
			}
			if (beans != null && beans.size() > 1) {
				showcomplelteDiloage1(beans);
			}
			if (beans==null ||(beans!=null&&beans.size()<=0)) {
//				 Toast.makeText(getApplicationContext(), "没有可删除的邮件号！",
//				 Toast.LENGTH_SHORT).show();
				 showdiag("没有可删除的邮件号！");
				 }
		}
		if (result == null) {
//			Toast.makeText(getApplicationContext(), "没有可删除的邮件号",
//					Toast.LENGTH_SHORT).show();
			showdiag("没有可删除的邮件号");
		} 

		super.onRestart();
	}
	
	private void showdiag(String info) {
		// TODO Auto-generated method stub
		InfoDialog infoDialog=new InfoDialog(this, info);
		infoDialog.Show();

	}


	/**
	 * 删除揽投信息
	 */

	private void showcomplelteDiloage1(final List<FastLanBean> beans) {
		final Dialog dialog = new Dialog(CountLanTouActivity.this,
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
				dialog.dismiss();
			}
		});
		info_sure_lantou_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (beans != null) {
					int size = beans.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							upload("D", beans.get(i));
//							DeliverDaoFactory.getInstance()
//									.getFastLanDao(getApplicationContext())
//									.updateOpreate(beans.get(i).getId(), "D");
						}
//						Toast.makeText(getApplicationContext(), "删除成功",
//								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				}
			}
		});
		dialog.show();
	}

	private void showcomplelteDiloage2(final List<FastLanBean> beans) {
		final Dialog dialog = new Dialog(CountLanTouActivity.this,
				R.style.dialogss);
		View v = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_lantoudelete1, null);
		dialog.setCancelable(false);
		Button info_cancel_lantou_delete1 = (Button) v
				.findViewById(R.id.info_cancel_lantou_delete1);
		Button info_sure_lantou_delete1 = (Button) v
				.findViewById(R.id.info_sure_lantou_delete1);
		dialog.setContentView(v, new LayoutParams(BaseActivity.width * 17 / 20,
				LayoutParams.WRAP_CONTENT));
		info_cancel_lantou_delete1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		info_sure_lantou_delete1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (beans != null) {
					int size = beans.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							upload("D", beans.get(i));
							// DaoFactory.getInstance().getFastLanDao(getApplicationContext()).updateOpreate(deleteLanBeans.get(i).getId(),"D");
						}
//						Toast.makeText(getApplicationContext(), "删除成功",
//								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				}
			}
		});
		dialog.show();
	}

	private void initData() {
		fastLanDao = DeliverDaoFactory.getInstance().getFastLanDao(
				getApplicationContext());
		gatherCount = fastLanDao.queryFastLanMessageI().size();
		deleteLanBeans = fastLanDao.queryFastLanMessageI();
		// listData = deliverDao.queryTuotou();// 已妥投邮件集合
		// List<MessageInfoBean> queueData = deliverDao.queryTuotouQueue();
		// for (int i = 0; i < deleteLanBeans.size(); i++) {
		// Log.e("ssshangchuan",deleteLanBeans.get(i).getShangChuan());
		// }
		// deliverCount = listData.size();// 妥投个数
	}

	private void upload(final String operate, final FastLanBean fastlan) {
		Log.e("upload", "aaa");

		new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				String isSuc = "1";

				UserInfoUtils user = new UserInfoUtils(getApplicationContext());
				deviceId = Utils.getDeviceId(getApplicationContext());
				orgCode = user.getUserDelvorgCode();
				username = user.getUserName();
				realname = user.getRealname();
				postcode = user.getPostcode();
				String reObjc = "";
				String result = "";
				String isnanji = "";
				LanShouBean1 bean1 = new LanShouBean1();
				List<LanShouBean2> lists = new ArrayList<LanShouBean2>();
				bean1.setDeviceId(deviceId);// 设备号
				bean1.setClct_bureau_org_code(orgCode);// 机构号
				bean1.setClct_staff_name(realname);// 姓名
				bean1.setClct_staff_code(username);// 工号
				LanShouBean2 bean2 = new LanShouBean2();
				isnanji = "";
				bean2.setActual_receipt_fees(null);// 实收资费
				bean2.setActual_total_fee(fastlan.getActualTotalFee());// 实收款
				bean2.setActual_weight(fastlan.getActualWeight());// 重量
				bean2.setActualconcessional_rate(null);// 实收优惠率
				bean2.setAddi_service_code("");
				bean2.setAgency_code("");
				bean2.setAirport_expense(null);// 机场费
				bean2.setApply_chk_flag("");
				bean2.setAttach_list_quan("0");// 随附单据数
				bean2.setBack_mail_num(fastlan.getReturnMailNum());// 返单号
				bean2.setBasic_fee(null);// 基本资费
				bean2.setBilling_weight(null);// 计费重量
				bean2.setBulgarian_price_amount(null);// 报价金额
				bean2.setBulgarian_price_expense(null);// 保价费
				bean2.setBulgarian_price_handling(null);// 保价手续费
				bean2.setBusi_kind_code("");
				bean2.setCant_dlv_deal_mode_code("");
				bean2.setClct_bureau_post_code(postcode);// 收寄局邮编
				bean2.setClct_staff_code(username);
				bean2.setClct_staff_name(realname);
				bean2.setData_clct_mode_code("2");// 数据采集方式
				bean2.setData_src_sys("");
				bean2.setData_upload_timestamp("");
				bean2.setDeclaration_expense(null);// 报关验关费
				bean2.setDlv_area_code("");
				bean2.setDuty_code("");
				bean2.setDzz_num("");// 单证照件数
				bean2.setEachfeeflag("");
				bean2.setElectronfavorable_num("");
				bean2.setEmbraces_charge(null);// 揽收费
				bean2.setExtra_charge(null);// 附加费
				bean2.setFeedback_mode_code("");
				bean2.setFeedback_para("");
				bean2.setFrequency("");// 频次
				bean2.setHeight("");// 高
				bean2.setInformation_fee(null);// 信息费
				bean2.setInitiative_feedback_flag("");
				bean2.setInsurance_amount(null);// 保险金额
				bean2.setInsurance_expenses(null);// 保险费
				bean2.setInter_flag("0");// 国内
				bean2.setClct_date(fastlan.getClct_date().toString());// 时间
				bean2.setClct_time(fastlan.getClct_time().toString());// 时间
				if (" 南集".equals(fastlan.getSourthcode())) {
					isnanji = "1";
				} else if (" 非南集".equals(fastlan.getSourthcode())) {
					isnanji = "0";
				}
				bean2.setIs_distributed(isnanji);// 是否参与集散
				bean2.setLength("");// 长
				bean2.setLoans_amount(null);// 货款金额
				bean2.setMail_kind_code("");
				bean2.setMail_num(fastlan.getMailNum());// 邮件号
				bean2.setMail_other_remark("");
				bean2.setMail_prpty_code("");
				bean2.setMail_remark_code("");
				bean2.setMail_sort_code(getMailCode(fastlan.getFenlei()
						.toString()));// 邮件分类代码
				bean2.setMain_mailno(fastlan.getMainmail());// 一票多件主单号
				bean2.setOperator_code("");
				bean2.setOperator_name("");
				bean2.setOperatortype(operate);
				bean2.setOrder_check_flag("");
				bean2.setOrderno("");
				bean2.setOther_expenses(null);// 其他费
				bean2.setPacking_charges(null);// 包装费
				bean2.setPart_kind_quan("0");// 内件总类数
				bean2.setPay_side_code("");
				bean2.setPlandlvdate("");
				bean2.setPlandlvtime("");
				bean2.setPreview_info_type("99");// 数据预告类型
				bean2.setProd_code("");// 产品代码
				bean2.setRcpt_invoice_num("");
				bean2.setRcv_area(fastlan.getRcvArea());// 寄达地代码
				bean2.setRcver_addr("");
				bean2.setRcver_contract_phone("");
				bean2.setRcver_dept_name("");
				bean2.setRcver_home_area_name("");
				bean2.setRcver_name("");
				bean2.setRcver_post_code(fastlan.getPostcode());// 寄达地邮编
				bean2.setReceipt_expense(null);// 回执费
				bean2.setResv_col10("");
				bean2.setSender_addr(fastlan.getSender_addr());
				bean2.setSender_contact_phone(fastlan.getSender_contact_phone());
				bean2.setSender_cust_code(fastlan.getCustomer());// 大客户代码
				bean2.setSender_dept_name("");
				bean2.setSender_home_area_name("");
				bean2.setSender_name(fastlan.getSender_name());
				bean2.setSender_post_code("");
				bean2.setSetlmt_mode_code("");
				bean2.setShould_pay_total_fee(null);// 应收总资费
				bean2.setTime_limit_code("");
				bean2.setTrans_mode_code(getTransCode(fastlan.getYunshu()
						.toString()));// 运输方式
				bean2.setTrial_no("");
				bean2.setUnitary_expense(null);// 单式费
				bean2.setVip_card("");
				bean2.setVol_weight(null);// 体积重量
				bean2.setWay_code(fastlan.getGekoucode());// 格口代码
				bean2.setWidth("");// 宽
				bean2.setWl_term_num("");
				bean2.setYpdj_flag(fastlan.getYpdj_flag() == null ? ""
						: fastlan.getYpdj_flag().toString());// 一票多件标示
				bean2.setYpdj_quan(fastlan.getYpdj_quan() == null ? ""
						: fastlan.getYpdj_quan().toString());// 一票多件件数
				lists.add(bean2);

				bean1.setData(lists);
				// 揽收数据上传接口
				String urlString = Global.LanShouShuJu_URL;
				String jsonString = JSON.toJSONString(bean1);
				Log.e("sssss", jsonString);

				result = NetHelper.doPostJson(urlString, jsonString, "json");

				if (result != null) {
					JSONObject obj;
					try {
						obj = new JSONObject(result);
						reObjc = obj.get("result").toString();
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				return reObjc;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				// mdialog.toDimiss();
				if (result != null) {
					try {
						if ("1".equals(result)) {
							DeliverDaoFactory.getInstance()
									.getFastLanDao(getApplicationContext())
									.updateShangChuan(fastlan.getId(), 1);
							if ("D".equals(operate)) {
//								DeliverDaoFactory.getInstance()
//										.getFastLanDao(getApplicationContext())
//										.updateOpreate(fastlan.getId(), "D");
//								gatherBeans = fastLanDao.queryFastLanPatmentById(fastlan.getId(),"D");
								delete();
							}
//							Toast.makeText(getApplicationContext(), "上传成功",
//									Toast.LENGTH_SHORT).show();
							deleteLanBeans = DeliverDaoFactory.getInstance()
									.getFastLanDao(getApplicationContext())
									.queryFastLanMessageI();
							countLanTouAdapter.notifyDataSetChanged();
						} else {
							Toast.makeText(getApplicationContext(), "上传失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO: handle exception
						Log.e("e", e.getMessage());
					}
				} else {
					Toast.makeText(getApplicationContext(), "上传失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			protected void onPreExecute() {
				// mdialog = new ProgressDialog(getApplicationContext(),
				// "正在上传揽收信息");
				// mdialog.setCanCalcelable(false);
				// mdialog.toShow();
				super.onPreExecute();
			}
		}.execute();

	}

	private void delete() {
		new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				String reObjc="";
				NetHelper.doPostJson(Global.PUTMONEY,mailPutMoneyData(new ArrayList<GatherInfoBean>(),gatherBeans), "json");
				return reObjc;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				// mdialog.toDimiss();
				if (result != null) {
					try {
						if ("1".equals(result)) {
//							Toast.makeText(getApplicationContext(), "上传成功",
//									Toast.LENGTH_SHORT).show();
						} else {
//							Toast.makeText(getApplicationContext(), "上传失败",
//									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO: handle exception
						Log.e("e", e.getMessage());
					}
				} else {
					Toast.makeText(getApplicationContext(), "上传失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			protected void onPreExecute() {
				// mdialog = new ProgressDialog(getApplicationContext(),
				// "正在上传揽收信息");
				// mdialog.setCanCalcelable(false);
				// mdialog.toShow();
				super.onPreExecute();
			}
		}.execute();

	}
	private String getTransCode(String name) {
		String code = "";
		TransportTypeDao transportTypeDao = DeliverDaoFactory.getInstance()
				.getTransportTypeDao(this);
		code = transportTypeDao.queryTransferCode(name);
		return code;
	}

	private String getMailCode(String name) {
		String code = "";
		MailTypeDao mailtypedao = DeliverDaoFactory.getInstance()
				.getMailTypeDao(this);
		code = mailtypedao.queryMailCode(name);
		return code;
	}

	public class CountLanTouAdapter extends BaseAdapter {
		private Context context;

		public CountLanTouAdapter(Context context,int rightWidth) {
			this.context = context;
			mRightWidth = rightWidth;
		}

		@Override
		public int getCount() {
			return deleteLanBeans.size();
		}

		@Override
		public Object getItem(int position) {
			return deleteLanBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final ViewHolder viewHolder;
			final int temppos = position;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_list_tongji, null);

				viewHolder = new ViewHolder();
				viewHolder.item_left=convertView.findViewById(R.id.item_left1);
				viewHolder.item_right=convertView.findViewById(R.id.item_right1);
				viewHolder.item_right_txt=(TextView) convertView.findViewById(R.id.item_right_txt1);
				viewHolder.textViewKeHu = (TextView) convertView
						.findViewById(R.id.fragment_countlantou_item_kehu1);
				viewHolder.textViewNumber = (TextView) convertView
						.findViewById(R.id.fragment_countlantou_item_number);
				viewHolder.textViewAddress = (TextView) convertView
						.findViewById(R.id.fragment_countlantou_item_address);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			LinearLayout.LayoutParams lp1 =  new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			viewHolder.item_left.setLayoutParams(lp1);
			LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
					LayoutParams.MATCH_PARENT);
			viewHolder.item_right.setLayoutParams(lp2);
			viewHolder.item_right.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					upload("D",deleteLanBeans.get(temppos));
				}
			});
			if (deleteLanBeans.get(position).getShangChuan().toString()
					.equals("0")) {
				viewHolder.btn_shangchuan.setVisibility(View.VISIBLE);
			} else {
				viewHolder.btn_shangchuan.setVisibility(View.GONE);
			}
			if (deleteLanBeans.get(position).getCustomer().toString() == null) {
				viewHolder.textViewKeHu.setText("");
			} else {
				CustomerDao customerDao = DeliverDaoFactory.getInstance()
						.getCustomerDao(context);
				viewHolder.textViewKeHu
						.setText(customerDao.FindDaName(deleteLanBeans.get(
								position).getCustomer()));
			}
			if (deleteLanBeans.get(position).getMailNum() == null) {
				viewHolder.textViewNumber.setText("");
			} else {
				viewHolder.textViewNumber.setText(deleteLanBeans.get(position)
						.getMailNum());
			}
			if (deleteLanBeans.get(position).getRcvArea() == null) {
				viewHolder.textViewAddress.setText("");
			} else {
				QsjGndmDao qsjGndmDao = DeliverDaoFactory.getInstance()
						.getQsjGndmDao(context);
				viewHolder.textViewAddress.setText(qsjGndmDao
						.FindDaName(deleteLanBeans.get(position).getRcvArea()));
			}
			viewHolder.btn_shangchuan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					m_posion = temppos;
					upload("I", deleteLanBeans.get(m_posion));

				}
			});
			return convertView;
		}

		public List<FastLanBean> getDeleteLanBeans() {
			return deleteLanBeans;
		}

		class ViewHolder {
			private TextView textViewKeHu, textViewNumber, textViewAddress,item_right_txt;
			private Button btn_shangchuan;
			View item_left;
			View item_right;
		}

	}
	private UserInfoUtils userInfo;
	private String mailPutMoneyData(List<GatherInfoBean> touBeans,
			List<GatherInfoBean> lanBeans) {
		userInfo = new UserInfoUtils(CountLanTouActivity.this);
		String resultURL;
		PaymentMoneyBean bean = new PaymentMoneyBean();
		if (!"".equals(userInfo.getUserName())) {
			bean.setEmployeeNo(userInfo.getUserName());
		}
		bean.setDeviceId(deviceId());
		if (!"".equals(userInfo.getUserDelvorgCode())) {
			bean.setSjvorgcode(userInfo.getUserDelvorgCode());
			bean.setDlvorgcode(userInfo.getUserDelvorgCode());
		}
		bean.setLan(lanBeans);
		bean.setTou(touBeans);
		resultURL = JSON.toJSONString(bean);
		return resultURL;

	}
	/**
	 * 手机设备号
	 */
	private String deviceId() {
		TelephonyManager telephonemanage = (TelephonyManager) CountLanTouActivity.this
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonemanage.getDeviceId();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(CountLanTouActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}
	
}

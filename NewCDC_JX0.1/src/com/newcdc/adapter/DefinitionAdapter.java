package com.newcdc.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.newcdc.R;
import com.newcdc.R.color;
import com.newcdc.activity.delivertask.DeliverErrorActivity;
import com.newcdc.activity.delivertask.DeliverOkActivity;
import com.newcdc.activity.delivertask.TaskShowActivity;
import com.newcdc.asynctask.sendMessageTask;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.GroupDao;
import com.newcdc.db.PushDao;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Constant;
import com.newcdc.tools.DealDeliverTools;
import com.newcdc.tools.Global;
import com.newcdc.tools.JsonUtils;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;

/**
 * 投递任务Adapter
 * 
 * @author zhangfan 2014-12-23
 * 
 */
public class DefinitionAdapter extends BaseAdapter {
	private Context context;// 上下文
	private List<MessageInfoBean> data;// 数据源
	private LayoutInflater inflater;
	private String groupName;// 组名
	private DeliverDao deliverDao;
	public static final String isMEx = "^((\\+86)?|\\(\\+86\\))0?1[358]\\d{9}$";
	public static final String regEx = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
	private View layout;
	private LayoutInflater inflaterdia;
	private Dialog longdia;
	private String mResult;// 请求返回值
	private static final int MSG_PAS = 1;
	private String mail_num = "";
	private String phone_num = "";
	private String warnTime;// 预警时间
	private SharePreferenceUtilDaoFactory shareDao;
	private Dialog delOrUpdialog;
	private ProgressDialog progressDialog;
	private onNotifyAdapterListener listener;
	// 用于显示按钮排和图标
	public static boolean visib = false;
	private Dialog dealDialog;
	private DeliverDaoFactory daoFactory;
	private String orgCode;
	private String userName;
	private String the_class_date;
	private Handler mHandler;
	/**
     * 
     */
	private int mRightWidth = 0;

	/**
	 * 单击事件监听器
	 */
	private IOnItemRightClickListener mListener = null;
	private List<Integer> list;

	public interface IOnItemRightClickListener {
		void onRightClick(View v, int position);
	}

	public onNotifyAdapterListener getListener() {
		return listener;
	}

	public void setListener(onNotifyAdapterListener listener) {
		this.listener = listener;
	}

	public static String text = "";

	/**
	 * 接口：当Adapter刷新完成之后，可执行操作
	 */
	public interface onNotifyAdapterListener {
		void onNotifyAdapter();
	}

	public DefinitionAdapter(Context context, List<MessageInfoBean> data,
			String groupName, int rightWidth, IOnItemRightClickListener l) {
		super();
		this.context = context;
		this.data = data;
		this.groupName = groupName;
		inflater = LayoutInflater.from(context);
		daoFactory = DeliverDaoFactory.getInstance();
		deliverDao = daoFactory.getDeliverDao(context);
		PushDao pushDao = daoFactory.getPushDao(context);
		the_class_date = pushDao
				.query_class_date(Constant.PUSH_TYPE_DELIVERTASK);
		shareDao = SharePreferenceUtilDaoFactory.getInstance(context);
		warnTime = shareDao.getYJ_TD_time();
		UserInfoUtils user = new UserInfoUtils(context);
		orgCode = user.getUserDelvorgCode();
		userName = user.getUserName();
		delOrUpdialog = new Dialog(context, R.style.dialogss);
		progressDialog = new ProgressDialog(context, "正在发送···");
		dealDialog = new Dialog(context, R.style.dialogss);
		mHandler = new Handler();
		longdia = new Dialog(context, R.style.dialogss);
		mRightWidth = rightWidth;
		mListener = l;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void notifyDataSetChanged() {
		if (listener != null) {
			listener.onNotifyAdapter();
		}
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_list_definition, null);
			holder = new ViewHolder();
			holder.item_left = convertView.findViewById(R.id.item_left);
			holder.item_right = convertView.findViewById(R.id.item_right);
			holder.item_right_txt = (TextView) convertView
					.findViewById(R.id.item_right_txt);

			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name_item_list_delivertask);
			holder.tv_mobile = (TextView) convertView
					.findViewById(R.id.tv_mobile_item_list_delivertask);
			holder.tv_mailNum = (TextView) convertView
					.findViewById(R.id.tv_mailNum_item_list_delivertask);
			holder.tv_addr = (TextView) convertView
					.findViewById(R.id.tv_address_item_list_delivertask);
			holder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money_item_list_delivertask);
			holder.tv_state = (TextView) convertView
					.findViewById(R.id.tv_state_item_list_delivertask);
			holder.tv_remainTime = (TextView) convertView
					.findViewById(R.id.tv_remainTime_item_list_delivertask);
			holder.btn_call = (Button) convertView
					.findViewById(R.id.btn_call_item_list_delivertask);
			holder.btn_deal = (Button) convertView
					.findViewById(R.id.btn_deal_item_list_delivertask);
			holder.btn_msg = (Button) convertView
					.findViewById(R.id.btn_msg_item_list_delivertask);
			holder.btn_call_count = (Button) convertView
					.findViewById(R.id.btn_callcount_item_list_delivertask);
			holder.btn_msg_count = (Button) convertView
					.findViewById(R.id.btn_msgcount_item_list_delivertask);
			holder.btn_tuotou = (Button) convertView
					.findViewById(R.id.btn_tuotou_item_list_delivertask);
			holder.btn_weituotou = (Button) convertView
					.findViewById(R.id.btn_weituotou_item_list_delivertask);
			holder.btn_selfgroup = (Button) convertView
					.findViewById(R.id.btn_selfgroup_item_list_delivertask);
			holder.chooseimge = (ImageView) convertView
					.findViewById(R.id.chooseimge);// 选中图标
			holder.tongxun = (LinearLayout) convertView
					.findViewById(R.id.tongxun);// 按钮排
			if (visib) {
				holder.tongxun.setVisibility(View.GONE);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LinearLayout.LayoutParams lp1 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		holder.item_left.setLayoutParams(lp1);
		LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
				LayoutParams.MATCH_PARENT);

		holder.item_right.setLayoutParams(lp2);
		holder.item_right_txt.setText("撤销 ");
		holder.item_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightClick(v, position);
				}
				data = deliverDao.querySelfGroupMail();
				GroupDao groupDao = daoFactory.getGroupDao(context);
				groupDao.updateDealMailCount(context);
				groupDao.updateUncommitMailCount(context);
				groupDao.updateOtherGroups(context);
				notifyDataSetChanged();
			}
		});
		final MessageInfoBean bean = data.get(position);
		final String mobileNum = bean.getRcver_contact_phone1();
		holder.tv_name.setText(bean.getRcver_name());
		holder.tv_mobile.setText(mobileNum + "");
		holder.tv_mailNum.setText(bean.getMail_num());
		holder.tv_addr.setText(bean.getRcver_street_addr());
		final int _id = bean.get_id();
		int call_time = bean.getCall_time();
		int msg_time = bean.getMsg_time();
		if (call_time != 0) {
			holder.btn_call_count.setVisibility(View.VISIBLE);
			holder.btn_call_count.setText(call_time + "");
		} else {
			holder.btn_call_count.setVisibility(View.GONE);
		}
		if (msg_time != 0) {
			holder.btn_msg_count.setVisibility(View.VISIBLE);
			holder.btn_msg_count.setText(msg_time + "");
		} else {
			holder.btn_msg_count.setVisibility(View.GONE);
		}
		final String money = bean.getMoney();
		final String mailNumber = bean.getMail_num();
		// 显示对勾图标
		if (text.equals(mailNumber.trim())) {
			holder.chooseimge.setVisibility(View.VISIBLE);
		} else {
			holder.chooseimge.setVisibility(View.GONE);
		}
		if (money != null && money.length() > 0 && (!"0.0".equals(money))) {
			holder.tv_money.setTextColor(Color.RED);
			holder.tv_money.setText(money + "元");
		} else {
			holder.tv_money.setText("无");
			holder.tv_money.setTextColor(color.glay);
		}
		switch (bean.getDealResult()) {
		case Constant.DAICHULI:
			holder.tv_state.setText("待处理");
			holder.tv_state.setTextColor(color.orange);
			break;
		case Constant.TUOTOU:// 妥投
			holder.tv_state.setText("已妥投");
			holder.tv_state.setTextColor(color.orange);
			break;
		case Constant.WEITUOTOU:// 未妥投
			holder.tv_state.setText("未妥投");
			holder.tv_state.setTextColor(Color.RED);
			break;

		}
		boolean openYj = shareDao.getIsYJ();
		if (openYj) {
			holder.tv_remainTime.setVisibility(View.VISIBLE);
			String stemp = Utils.getStemp(
					bean.getThe_class_date().split(" ")[1], warnTime);
			int endH = Integer.parseInt(stemp.split(":")[0]);
			if (endH >= 2) {
				holder.tv_remainTime.setTextColor(Color.BLACK);
			} else if (endH >= 1 && endH < 2) {
				holder.tv_remainTime.setTextColor(Color.BLUE);
			} else if (endH > 0 && endH < 1) {
				holder.tv_remainTime.setTextColor(Color.YELLOW);
			} else {
				holder.tv_remainTime.setTextColor(Color.RED);
			}
			holder.tv_remainTime.setText(stemp);
		} else {
			holder.tv_remainTime.setVisibility(View.GONE);
		}
		holder.btn_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TaskShowActivity.mobileNum = mobileNum;// 用于在通话结束之后，将通话信息上传服务器
				if (bean.getMail_num() != null
						&& !"".equals(bean.getMail_num())) {

					if (!"null".equals(mobileNum) && !"".equals(mobileNum)
							&& mobileNum != null) {
						Pattern pat = Pattern.compile(regEx);
						Matcher mat = pat.matcher(mobileNum);
						boolean rs = mat.find();
						if (rs) {
							if (longdia == null) {
								longdia = new Dialog(context, R.style.dialogss);
								sureCallDialog(longdia, mobileNum, _id);
							} else {
								sureCallDialog(longdia, mobileNum, _id);
							}
						} else {
							if (longdia == null) {
								longdia = new Dialog(context, R.style.dialogss);
								mail_num = mailNumber;
								callReplenishDialog(longdia, _id, position);
							} else {
								mail_num = mailNumber;
								callReplenishDialog(longdia, _id, position);
							}
						}
					} else {
						if (longdia == null) {
							longdia = new Dialog(context, R.style.dialogss);
							mail_num = mailNumber;
							callReplenishDialog(longdia, _id, position);
						} else {
							mail_num = mailNumber;
							callReplenishDialog(longdia, _id, position);
						}
					}
				} else {
					Toast.makeText(context, "无效的投递信息", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		holder.btn_deal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showdialog(_id);
			}
		});
		holder.btn_msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"null".equals(mobileNum) && !"".equals(mobileNum)
						&& mobileNum != null) {
					Pattern pat = Pattern.compile(regEx);
					Matcher mat = pat.matcher(mobileNum);
					boolean rs = mat.find();
					if (rs) {
						if (isMobileNum(mobileNum)) {
							sureMsgDialog(bean.getRcver_contact_phone1(), _id);
						} else {
							Toast.makeText(context, "非手机号", 1).show();
						}
					} else {
						mail_num = mailNumber;
						callReplenishDialog(longdia, _id, position);
					}
				} else {
					mail_num = mailNumber;
					callReplenishDialog(longdia, _id, position);
				}
			}
		});
		holder.btn_tuotou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dealDeliverTask(Constant.TUOTOU, position);
			}
		});
		holder.btn_weituotou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dealDeliverTask(Constant.WEITUOTOU, position);
			}
		});
		holder.btn_selfgroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹出dialog-同
				dealDeliverTask(Constant.INGROUP, position);
				// showMarkDialog(_id, position);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_name, tv_mobile, tv_mailNum, tv_addr, tv_money, tv_state,
				tv_remainTime;
		Button btn_call, btn_msg, btn_deal, btn_msg_count, btn_call_count,
				btn_tuotou, btn_weituotou, btn_selfgroup;
		ImageView chooseimge;// 选中图标
		LinearLayout tongxun;// button
		View item_left;
		View item_right;
		TextView item_right_txt;
	}

	public List<MessageInfoBean> getData() {
		return data;
	}

	public void setData(List<MessageInfoBean> data) {
		this.data = data;
	}

	// 是否手机号
	public boolean isMobileNum(String num) {
		Pattern pat = Pattern.compile(isMEx);
		Matcher mat = pat.matcher(num);
		boolean rs = mat.find();
		return rs;
	}

	/**
	 * 电话补录dialog hanrong
	 * **/
	private void callReplenishDialog(final Dialog longdia, final int _id,
			final int location) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		layout = LayoutInflater.from(context).inflate(
				R.layout.dialog_callreplenish_layout, null);
		Button cancel = (Button) layout.findViewById(R.id.info_call_cancel);
		Button sure = (Button) layout.findViewById(R.id.info_call_sure);
		final EditText dia__edit_call = (EditText) layout
				.findViewById(R.id.dia__edit_call);
		longdia.setContentView(layout, new LayoutParams(width * 18 / 20,
				LayoutParams.WRAP_CONTENT));
		longdia.setCancelable(true);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				longdia.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String callnum = dia__edit_call.getText().toString().trim();
				Pattern pat = Pattern.compile(regEx);
				Matcher mat = pat.matcher(callnum);
				boolean rs = mat.find();
				if (callnum != null && !"".equals(callnum) && rs) {
					phone_num = callnum;
					deliverDao.updatePhone(_id, callnum);
					data.get(location).setRcver_contact_phone1(callnum);
					notifyDataSetChanged();
					// callreplenishThread.start();
					new CallReplenishAsync().execute(Global.CALLREPLENISH_URL
							+ "mail_num=" + mail_num + "&rcver_contact_phone1="
							+ callnum);
					longdia.dismiss();
				} else {
					Toast.makeText(context, "请输入电话号码", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		longdia.show();
	}

	class CallReplenishAsync extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = NetHelper.doGet(params[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			if (result.equals("请求服务器失败")) {
				Toast.makeText(context, result, 0).show();
			} else if (resState(result).equals("0")) {
				Toast.makeText(context, "补录失败", 0).show();
			} else {
				Toast.makeText(context, "手机号补录成功", 0).show();
			}
		}

	}

	private Thread callreplenishThread = new Thread(new Runnable() {
		@Override
		public void run() {
			String result = NetHelper.doGet(Global.CALLREPLENISH_URL
					+ "mail_num=" + mail_num + "&rcver_contact_phone1="
					+ phone_num);
			Message message = handler.obtainMessage();
			message.what = MSG_PAS;
			message.obj = result;
			handler.sendMessageDelayed(message, 0);
		}
	});
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_PAS:
				mResult = (String) msg.obj;
				if (mResult.equals("请求服务器失败")) {
					Toast.makeText(context, "请求服务器失败", 1).show();
				} else if (resState(mResult).equals("0")) {
					Toast.makeText(context, "补录失败", 1).show();
				} else {
					Toast.makeText(context, "手机号补录成功", 1).show();
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

	// 发送短息线程

	private onSendMessageListener sendMessageListener;

	public void setSendMessageListener(onSendMessageListener sendMessageListener) {
		this.sendMessageListener = sendMessageListener;
	}

	/**
	 * 接口：当用户点击发送短信，界面的刷新
	 * 
	 */
	public interface onSendMessageListener {
		void onSendMessage();
	}

	// //////////////
	@SuppressLint("ServiceCast")
	public void showdialog(final int _id) {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);

		View v = LayoutInflater.from(context).inflate(
				R.layout.dialog_dlvorndlv, null);
		delOrUpdialog.setContentView(v, new LayoutParams(
				metric.widthPixels * 15 / 20, LayoutParams.WRAP_CONTENT));
		Button update = (Button) v.findViewById(R.id.tuo);
		Button delete = (Button) v.findViewById(R.id.wtuo);
		delOrUpdialog.setCancelable(true);
		// dialog_title.setText("对分组名" + groupList.get(arg2).getGroup_content()
		// + "做一下操作：");
		update.setOnClickListener(new OnClickListener() {// 妥投
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DeliverOkActivity.class);
				intent.putExtra("_id", _id);
				context.startActivity(intent);
				delOrUpdialog.dismiss();
			}
		});
		delete.setOnClickListener(new OnClickListener() {// 未妥投
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DeliverErrorActivity.class);
				intent.putExtra("_id", _id);
				context.startActivity(intent);
				delOrUpdialog.dismiss();
			}
		});
		delOrUpdialog.show();

	}

	// //////////////
	@SuppressLint("ServiceCast")
	public void showMarkDialog(final int _id, final int position) {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);

		View v = LayoutInflater.from(context).inflate(
				R.layout.dialog_dlvorndlv, null);
		delOrUpdialog.setContentView(v, new LayoutParams(
				metric.widthPixels * 15 / 20, LayoutParams.WRAP_CONTENT));
		Button update = (Button) v.findViewById(R.id.tuo);
		Button delete = (Button) v.findViewById(R.id.wtuo);
		delOrUpdialog.setCancelable(true);
		// dialog_title.setText("对分组名" + groupList.get(arg2).getGroup_content()
		// + "做一下操作：");
		update.setOnClickListener(new OnClickListener() {// 妥投
			@Override
			public void onClick(View v) {
				ArrayList<Integer> idList = new ArrayList<Integer>();
				idList.add(_id);
				deliverDao.updateToSelfGroup(idList);
				deliverDao.updateMailDealResult(_id, Constant.TUOTOU);
				Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
				delOrUpdialog.dismiss();
			}
		});
		delete.setOnClickListener(new OnClickListener() {// 未妥投
			@Override
			public void onClick(View v) {
				ArrayList<Integer> idList = new ArrayList<Integer>();
				idList.add(_id);
				deliverDao.updateToSelfGroup(idList);
				deliverDao.updateMailDealResult(_id, Constant.WEITUOTOU);
				Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
				delOrUpdialog.dismiss();
			}
		});
		delOrUpdialog.show();

	}

	public void notifySelf() {
		notifyDataSetChanged();
	}

	/**
	 * 确认打电话 hanrong
	 * **/
	private void sureCallDialog(final Dialog longdia, final String phoneNum,
			final int count) {
		layout = LayoutInflater.from(context).inflate(
				R.layout.dialog_call_deliver, null);
		Button cancel = (Button) layout.findViewById(R.id.info_cancel);
		Button sure = (Button) layout.findViewById(R.id.info_sure);
		TextView custom_dia_centercall = (TextView) layout
				.findViewById(R.id.custom_dia_centercall);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		longdia.setContentView(layout, new LayoutParams(width * 15 / 20,
				LayoutParams.WRAP_CONTENT));
		longdia.setCancelable(true);
		custom_dia_centercall.setText(phoneNum);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				longdia.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent();
				// intent.setAction(Intent.ACTION_CALL); //
				// android.intent.action.DIAL
				// intent.setData(Uri.parse("tel:" + phoneNum));
				// // MainActivity.number = map.get("telephonenum")
				// // .toString();
				// // MainActivity.yeslan = true;
				// context.startActivity(intent);
				deliverDao.addCallTime(count);
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + phoneNum));
				context.startActivity(intent);
				longdia.dismiss();
			}
		});
		longdia.show();
	}

	/**
	 * 确认发短信 hanrong
	 * **/
	private void sureMsgDialog(String phoneNum, final int _id) {
		layout = LayoutInflater.from(context)
				.inflate(R.layout.dialog_sms, null);
		Button cancel = (Button) layout.findViewById(R.id.info_cancel);
		Button sure = (Button) layout.findViewById(R.id.info_sure);
		TextView custom_dia_centercall = (TextView) layout
				.findViewById(R.id.custom_dia_centercall);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		longdia.setContentView(layout, new LayoutParams(width * 15 / 20,
				LayoutParams.WRAP_CONTENT));
		longdia.setCancelable(true);
		custom_dia_centercall.setText(phoneNum);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				longdia.dismiss();
			}
		});
		final ArrayList<Integer> phoneList = new ArrayList<Integer>();
		phoneList.add(_id);
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				progressDialog.toShow();
				new Thread() {
					@Override
					public void run() {
						String result = sendMessageTask.upload(phoneList,
								context);
						final String[] split = result.split(",");
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								if (progressDialog.isShowing()) {
									progressDialog.toDimiss();
								}
								if ("1".equals(split[1])) {
									deliverDao.updateMsgUser(_id);// 设置为用户手动发送
									notifySelf();
									Toast.makeText(context, "发送短信成功",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(context, "发送短信失败",
											Toast.LENGTH_SHORT).show();
								}
							}
						});
					};
				}.start();
				//
				// new sendMessageTask(new onPostExecuteListener() {
				//
				// @Override
				// public void onPostExecute(String result) {
				// progressDialog.toDimiss();
				// String[] split = result.split(",");
				// if ("1".equals(split[1])) {
				// deliverDao.updateMsgUser(_id);// 设置为用户手动发送
				// notifySelf();
				// Toast.makeText(context, "发送短信成功",
				// Toast.LENGTH_SHORT).show();
				// } else {
				// Toast.makeText(context, "发送短信失败",
				// Toast.LENGTH_SHORT).show();
				// }
				// }
				// }, new sendMessageTask.onPreExecuteListener() {
				//
				// @Override
				// public void onPreExecute() {
				// progressDialog.toShow();
				// }
				// }, context, phoneList).execute();
				longdia.dismiss();
			}
		});
		longdia.show();
	}

	/**
	 * 做投递
	 * **/
	private void dealDeliverTask(final int dealType, final int position) {
		View layout = LayoutInflater.from(context).inflate(R.layout.dialog_sms,
				null);
		Button cancel = (Button) layout.findViewById(R.id.info_cancel);
		Button sure = (Button) layout.findViewById(R.id.info_sure);
		TextView custom_dia_centercall = (TextView) layout
				.findViewById(R.id.custom_dia_centercall);
		TextView title = (TextView) layout.findViewById(R.id.txt_tit);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		title.setText("提示");
		manager.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		dealDialog.setContentView(layout, new LayoutParams(width * 15 / 20,
				LayoutParams.WRAP_CONTENT));
		dealDialog.setCancelable(true);
		switch (dealType) {
		case Constant.TUOTOU:
			custom_dia_centercall.setText("确定要执行妥投操作吗？");
			break;
		case Constant.WEITUOTOU:
			custom_dia_centercall.setText("确定要执行未妥投操作吗？");
			break;
		case Constant.INGROUP:
			custom_dia_centercall.setText("确定要加入自定义分组吗？");
			break;
		}
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dealDialog.dismiss();
			}
		});
		final MessageInfoBean bean = data.get(position);
		final ArrayList<MessageInfoBean> messageBeanlist = new ArrayList<MessageInfoBean>();
		messageBeanlist.add(bean);
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (dealType) {
				case Constant.TUOTOU:
					DealDeliverTools.startServiceNormal(messageBeanlist,
							Constant.TUOTOU, context);
					break;
				case Constant.WEITUOTOU:
					DealDeliverTools.startServiceNormal(messageBeanlist,
							Constant.WEITUOTOU, context);
					data.get(position).setDealResult(Constant.WEITUOTOU);
					break;
				case Constant.INGROUP:
					ArrayList<Integer> idList = new ArrayList<Integer>();
					idList.add(data.get(position).get_id());
					deliverDao.updateToSelfGroup(idList);// 标记到自定义分组
					break;
				}
				notifySelf();
				Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
				dealDialog.dismiss();
			}
		});
		dealDialog.show();
	}
}

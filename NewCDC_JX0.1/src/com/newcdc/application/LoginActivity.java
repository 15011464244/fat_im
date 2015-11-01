package com.newcdc.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newcdc.R;
import com.newcdc.db.ChatMessageDao;
import com.newcdc.db.CustomerDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.DeliverTaskListDao;
import com.newcdc.db.MailTypeDao;
import com.newcdc.db.NewUserDao;
import com.newcdc.model.LoginNew_ContentBean;
import com.newcdc.model.LoginNew_ResultBean;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;
import com.newcdc.version.CurrentVersion;

/**
 * 
 * @author Lion 2014-12-23 登录页面
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText compnum = null;
	private EditText worknumber = null;
	private EditText password = null;
	private Button btn_login = null;
	private TextView tv_version = null;
	public static String userId = "";// 百度推送--在BDPushMessageReceiver->onBind时赋值
	public static String channelId = "";// 百度推送
	// private UserDao udb = null;
	private NewUserDao newUserDao = null;
	private LinearLayout login_top = null;
	private CheckBox login_savepass = null;
	private CheckBox login_autologin = null;
	private String delvocode = null;
	private String username = null;
	ProgressDialog dialog = null;
	private SharePreferenceUtilDaoFactory spDao;
	public static String outlogin_pass = "";
	LoginTask task = null;
	private DeliverDaoFactory daoFactory;
	private CustomerDao customerDao = null;
	// private TransportTypeDao transportTypeDao = null;
	private MailTypeDao mailTypeDao = null;
	private ChatMessageDao mChatMessageDao = null;
	private Button mbundlingOrgCode = null;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				if (dialog != null) {
					dialog.toDimiss();
				}
				if (task != null) {
					task.cancel(true);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		spDao = SharePreferenceUtilDaoFactory.getInstance(LoginActivity.this);
		daoFactory = DeliverDaoFactory.getInstance();
		mChatMessageDao = DeliverDaoFactory.getInstance().getChatMessageDao(
				LoginActivity.this);
		// udb = daoFactory.getUserDao(this);
		newUserDao = daoFactory.getNewUserDao(this);
		inits();
		addListers();
	}

	// 初始化
	private void inits() {
		customerDao = DeliverDaoFactory.getInstance().getCustomerDao(
				LoginActivity.this);
		mTaskListDao = DeliverDaoFactory.getInstance().getDeliverTaskListDao(
				getApplicationContext());
		// transportTypeDao =
		// DeliverDaoFactory.getInstance().getTransportTypeDao(
		// LoginActivity.this);
		mailTypeDao = DeliverDaoFactory.getInstance().getMailTypeDao(
				LoginActivity.this);
		// UserInfoBean uib = udb.getUserInfo();
		LoginNew_ContentBean contentBean = newUserDao.getUserInfo();
		compnum = (EditText) findViewById(R.id.compnum);
		compnum.setEnabled(false);
		worknumber = (EditText) findViewById(R.id.worknumber);
		password = (EditText) findViewById(R.id.password);
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_version = (TextView) findViewById(R.id.tv_version_login);
		login_top = (LinearLayout) findViewById(R.id.login_top);
		LayoutParams params = new LayoutParams(width, width / 72 * 31);
		login_top.setLayoutParams(params);
		login_savepass = (CheckBox) findViewById(R.id.login_savepass);
		login_autologin = (CheckBox) findViewById(R.id.login_autologin);
		mbundlingOrgCode = (Button) findViewById(R.id.btn_bundling_orgcode);

		tv_version.setText("软件版本：v"
				+ CurrentVersion.getVersinName(LoginActivity.this));
		login_savepass.setChecked(spDao.getSavePass());
		login_autologin.setChecked(spDao.getAutoLogin());
		if (contentBean.getUsercode() != null) {
			worknumber.setText(contentBean.getUsercode());
		}
		if (contentBean.getOrgcode() != null) {
			compnum.setText(contentBean.getOrgcode());
		}
		if (spDao.getOrgCode() != null && !"".equals(spDao.getOrgCode())) {
			compnum.setText(spDao.getOrgCode());
		}

		if (spDao.getSavePass()) {
			password.setText(spDao.getPass());
		}
		if (login_autologin.isChecked()) {
			password.setText(spDao.getPass());
			login();
		}

	}

	// 监听
	private void addListers() {
		btn_login.setOnClickListener(this);
		login_autologin
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							login_savepass.setChecked(true);
						}
					}
				});
		login_savepass
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (login_autologin.isChecked()) {
							login_savepass.setChecked(true);
						}
					}
				});
		mbundlingOrgCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (longdia == null) {
					longdia = new Dialog(LoginActivity.this, R.style.dialogss);
					bundling_OrgCode(longdia);
				} else {
					bundling_OrgCode(longdia);
				}
			}
		});

	}

	/**
	 * sharesd记录帐号，密码，自动登录，记住密码状态
	 */
	private void saveState() {
		String pass = password.getText().toString().trim();
		spDao.setUSERNAME(username);
		spDao.setDELVORGCODE(delvocode);
		spDao.setPass(pass);
		if (login_savepass.isChecked()) {
			spDao.setSavePass(true);
		} else {
			spDao.setSavePass(false);
		}
		if (login_autologin.isChecked()) {
			spDao.setAutoLogin(true);
		} else {
			spDao.setAutoLogin(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			if (!"".equals(spDao.getCLIENTID()) || null != spDao.getCLIENTID()) {
				login();
			}
			break;
		}
	}

	// 登录
	private void login() {

		String deviceId = Utils.getDeviceId(LoginActivity.this);
		username = worknumber.getText().toString().trim();
		String passstr = password.getText().toString().trim();
		delvocode = compnum.getText().toString().trim();
		// 拼url
		if ("".equals(delvocode)) {
			showToast("机构号不可为空,请先绑定机构号");
		} else if ("".equals(passstr)) {
			showToast("密码不可为空");
		} else if ("".equals(username)) {
			showToast("工号不可为空");
		} else if (deviceId == null || "null".equals(deviceId)) {
			showToast("获取设备信息失败");
		} else {

			dialog = new ProgressDialog(LoginActivity.this, "正在登录...", handler);
			dialog.setCanCalcelable(false);
			if (!dialog.isShowing()) {
				dialog.toShow();
			}
			// UserInfoBean bean = udb.getUserInfo();
			LoginNew_ContentBean contentBean = newUserDao.getUserInfo();
			String url = "";
			if (contentBean.getOrgcode() == null) {
				url = Global.THISLOGIN + "userCode=" + username + "&password="
						+ passstr + "&orgCode=" + delvocode + "&userId="
						+ userId + "&channelId=" + channelId + "&deviceId="
						+ deviceId + "&smis=" + deviceId + "&sim="
						+ "&softwareVersion="
						+ CurrentVersion.getVersionCode(LoginActivity.this)
						+ "&imPushId=" + spDao.getCLIENTID()
						+ "&expressCompanyCode=&expressCompanyName";
			} else {
				url = Global.THISLOGIN + "userCode=" + username + "&password="
						+ passstr + "&orgCode=" + delvocode + "&userId="
						+ userId + "&channelId=" + channelId + "&deviceId="
						+ deviceId + "&smis=" + deviceId + "&sim="
						+ contentBean.getMobile() + "&softwareVersion="
						+ CurrentVersion.getVersionCode(LoginActivity.this)
						+ "&imPushId=" + spDao.getCLIENTID()
						+ "&expressCompanyCode=&expressCompanyName";
			}
			Log.e("LoginUrl", url);
			task = new LoginTask();
			task.execute(url);
		}
	}

	// 登录异步任务
	class LoginTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = NetHelper.doGet(params[0]);
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.e("登录-------------", result);
			dialog.toDimiss();
			if (result == null || result.trim().length() == 0
					|| result.trim().equals("null") || "请求服务器失败".equals(result)) {
				showToast("请求服务器失败");
			} else if (resState(result).equals("0")) {
				JSONObject object;
				String error = "error";
				try {
					object = new JSONObject(result);
					error = object.getJSONObject("body").getString("error");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				showErrorDialog(error);
			} else {
				outlogin_pass = password.getText().toString().trim();
				// 判断是否更换了用户
				if (!delvocode.equals(spDao.getDELVORGCODE())
						|| !username.equals(spDao.getUSERNAME())) {
					daoFactory.getGroupDao(LoginActivity.this).deleteGroup();
					daoFactory.getAddressDao(LoginActivity.this)
							.deleteAlltelphone();
					daoFactory.getDeliverDao(LoginActivity.this).deleteAll();
					daoFactory.getQueueDao(LoginActivity.this).deleteCommit();
					daoFactory.getPushDao(LoginActivity.this).deletepush();
					daoFactory.getFastLanDao(LoginActivity.this)
							.deleteFastLan_YiShangChuang();
					daoFactory.getGather_msgdao(LoginActivity.this).deleteAll();
					customerDao.DeleteData();
					mChatMessageDao.DeleteData();// 删除消息列表
					mTaskListDao.deleteAll();
					// transportTypeDao.DeleteData();
					mailTypeDao.DeleteData();
					SharePreferenceUtilDaoFactory.getInstance(
							getApplicationContext()).setDate(null);// 删除时间标记
				}
				newUserDao.deleteUserInfo();//
				saveState();//
				// LoginResultBean lrb = JSON.parseObject(result,
				// LoginResultBean.class);
				LoginNew_ResultBean lrb = JSON.parseObject(result,
						LoginNew_ResultBean.class);
				// UserInfoBean bean = udb.getUserInfo();
				LoginNew_ContentBean contentBean = newUserDao.getUserInfo();
				if (contentBean.getOrgcode() == null) {
					// udb.inertUserInfo(lrb.getBody().getSuccess().get(0)
					// .getUinfo().get(0),
					// lrb.getBody().getSuccess().get(0).getDinfo().get(0));
					// udb.inertUserInfo(lrb.getBody().getSuccess().get(0)
					// .getUinfo().get(0), new LoginDinfoBean());
					newUserDao.inertUserInfo(lrb.getContent());
				}
				pcData();
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}

	/**
	 * 提示框
	 */
	public void showErrorDialog(String message) {
		final Dialog dialog = new Dialog(LoginActivity.this, R.style.dialogss);
		View view = getLayoutInflater().inflate(R.layout.dialog_info_manydlv,
				null);
		dialog.setContentView(view, new LayoutParams(width * 18 / 20,
				LayoutParams.WRAP_CONTENT));
		dialog.setCancelable(true);
		TextView tv_msg = (TextView) view
				.findViewById(R.id.tv_message_dialog_info_manydlv);
		Button btn_positive = (Button) view
				.findViewById(R.id.btn_positive_dialog_info_manydlv);
		tv_msg.setText(message);
		btn_positive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}

	/**
	 * 频次测试数据
	 */
	private UserInfoUtils mUserInfo = null;
	private DeliverTaskListDao mTaskListDao;

	private void pcData() {
		mUserInfo = new UserInfoUtils(getApplicationContext());

		int mNums = mTaskListDao.queryGatherTaskMessage(
				mUserInfo.getUserDelvorgCode(), mUserInfo.getUserName()).size();
		Log.e("tag", "chaxun" + mNums);
		if (mNums == 0) {
			ArrayList<Map<String, String>> tasklist1 = new ArrayList<Map<String, String>>();
			for (int i = 1; i < 6; i++) {
				Map<String, String> map = new HashMap<String, String>();
				if (i == 5) {
					map.put("delivertasknum", "" + i);
					map.put("delivertaskname", "国际：" + i);
					map.put("delivertaskcount", "0");
					map.put("delivertaskallcount", "0");
				} else {
					map.put("delivertasknum", "" + i);
					map.put("delivertaskname", "频次：" + i);
					map.put("delivertaskcount", "0");
					map.put("delivertaskallcount", "0");
				}
				tasklist1.add(map);
			}
			mTaskListDao.saveGatherTaskMessage(tasklist1,
					mUserInfo.getUserDelvorgCode(), mUserInfo.getUserName());
			int pc = mTaskListDao.queryGatherTaskMessage(
					mUserInfo.getUserDelvorgCode(), mUserInfo.getUserName())
					.size();
			Log.e("taag", "频次；；；；；" + pc);

		}

	}

	private View layout;
	private LayoutInflater inflater;
	private Dialog longdia;

	/**
	 * 绑定机构号
	 * **/
	private void bundling_OrgCode(final Dialog longdia) {
		inflater = LoginActivity.this.getLayoutInflater();
		layout = inflater.inflate(R.layout.dialog_bundling_orgcode, null);
		Button sure = (Button) layout.findViewById(R.id.info_sure);
		final EditText edt_orgcode = (EditText) layout
				.findViewById(R.id.edt_orgcode);
		longdia.setContentView(layout, new LayoutParams(width * 17 / 20,
				LayoutParams.WRAP_CONTENT));
		longdia.setCancelable(true);
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// compnum.setText(sure.get);
				String orgcode = edt_orgcode.getText().toString().trim();
				if (orgcode == null || "".equals(orgcode)) {
					Toast.makeText(LoginActivity.this, "请输入要绑定的机构号",
							Toast.LENGTH_SHORT).show();
				} else {
					compnum.setText(orgcode);
					spDao.setOrgCode(orgcode);
					longdia.dismiss();
				}
			}
		});
		longdia.show();
	}
}

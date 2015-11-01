package com.newcdc.activity.usercenter;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.newcdc.R;
import com.newcdc.model.CustomServerResultBean;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;

/**
 * @author hanrong
 * @version 创建时间：2014-12-22 上午10:27:01 类说明 ： 客户管理 服务记录页
 */
public class CustomServerActivity extends Activity {
	private TextView mname_customserver;
	private TextView mmobiles_customserver;
	private TextView mspeak_customserver;
	private TextView mmsg_customserver;
	private StringBuffer sb = new StringBuffer();
	private StringBuffer sbMsg = new StringBuffer();
	// private ProgressDialog dialog;
	private String phoneNum = "";
	private ProgressDialog dialog = null;
	private Button btn_back_customserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_custom_server_main);
		if (getIntent().getExtras() != null) {
			phoneNum = getIntent().getExtras().getString("phonenum");
		}
		inits();
		addListener();
	}

	private void inits() {
		mname_customserver = (TextView) findViewById(R.id.name_customserver);
		mmobiles_customserver = (TextView) findViewById(R.id.mobiles_customserver);
		mspeak_customserver = (TextView) findViewById(R.id.speak_customserver);
		mmsg_customserver = (TextView) findViewById(R.id.msg_customserver);
		btn_back_customserver = (Button) findViewById(R.id.btn_back_customserver);
		if (dialog == null) {
			dialog = new ProgressDialog(CustomServerActivity.this, "同步中...");
			dialog.toShow();
		} else {
			dialog.toShow();
		}
		new CustomServerAsyncTask().execute();
	}

	private void addListener() {
		btn_back_customserver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CustomServerActivity.this.finish();
			}
		});
	}

	class CustomServerAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = NetHelper.doGet(Global.CUSTOMSERVER_URL + "mobile="
					+ phoneNum);
//			Log.e("tag", Global.CUSTOMSERVER_URL + "mobile=" + phoneNum);
			return result;
		}

		@SuppressLint("DefaultLocale")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//			Log.e("tag", result);
			if (dialog != null) {
				dialog.toDimiss();
			}
			if (result.equals("请求服务器失败")) {
				Toast.makeText(CustomServerActivity.this, "请求服务器失败,请检查网络", 0)
						.show();
				CustomServerActivity.this.finish();
			} else if (resState(result).equals("0")) {
				Toast.makeText(CustomServerActivity.this, "无服务记录", 0).show();
				CustomServerActivity.this.finish();
			} else {
				initText(result);

				// Toast.makeText(CustomServerActivity.this, result, 1).show();
			}
		}
	}

	private void initText(String result) {
		CustomServerResultBean dealBean = JSONArray.parseObject(
				result.toLowerCase(), CustomServerResultBean.class);
		if (dealBean.getBody().getSuccess().size() > 0) {
			mname_customserver.setText(dealBean.getBody().getSuccess().get(0)
					.getContacts_name());
			mmobiles_customserver.setText(dealBean.getBody().getSuccess()
					.get(0).getMobile());
			for (int i = 0; i < dealBean.getBody().getSuccess().size(); i++) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				// long l = new Long(dealBean.getBody().getSuccess().get(i)
				// .getCreate_time().getTime());
				// Calendar ca = Calendar.getInstance();
				// ca.setTimeInMillis(l);
				if ("0".equals(dealBean.getBody().getSuccess().get(i)
						.getStatus())) {
					sb.append(dealBean.getBody().getSuccess().get(i)
							.getCreate_time());
					sb.append("    拨打电话未接通").append("\n\n");
				} else if ("1".equals(dealBean.getBody().getSuccess().get(i)
						.getStatus())) {
					sb.append(dealBean.getBody().getSuccess().get(i)
							.getCreate_time());
					sb.append(
							"    通话时间："
									+ dealBean.getBody().getSuccess().get(i)
											.getRecord_time() + "秒").append(
							"\n\n");
					;
				} else {
					sbMsg.append(
							dealBean.getBody().getSuccess().get(i)
									.getCreate_time()).append("   发送短信")
							.append("\n\n");
					;
				}
			}
			mspeak_customserver.setText(sb.toString());
			mmsg_customserver.setText(sbMsg.toString());

		}
	}

	private String resState(String s) {
		try {
			JSONObject json = new JSONObject(s);
			String str = json.get("result").toString();
			System.out.println("json-------" + str);
			return str;
		} catch (JSONException e) {
			e.printStackTrace();
			return "0";
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(CustomServerActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}
	
}

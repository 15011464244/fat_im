package com.newcdc.activity.usercenter;

import java.util.List;

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
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.newcdc.R;
import com.newcdc.adapter.CustomDealAdapter;
import com.newcdc.model.CustomDealResultBean;
import com.newcdc.model.CustomDealVauleBean;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;

/**
 * @author hanrong
 * @version 创建时间：2014-12-22 上午10:27:01 类说明 ： 客户管理 订单记录页
 */
public class CustomDealActivity extends Activity {
	private ListView listview;
	private String phoneNum = "";
	private ProgressDialog dialog = null;
	private Button btn_back_customdeal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_custom_deal);
		if (getIntent().getExtras() != null) {
			phoneNum = getIntent().getExtras().getString("phonenum");
		}
		inits();
		addListener();
	}

	private void inits() {
		listview = (ListView) findViewById(R.id.listview_deal);
		btn_back_customdeal = (Button) findViewById(R.id.btn_back_customdeal);
		if (dialog == null) {
			dialog = new ProgressDialog(CustomDealActivity.this, "同步中...");
			dialog.toShow();
		} else {
			dialog.toShow();
		}
		new MyAsyncTask().execute();
	}

	private void initListViewData(List<CustomDealVauleBean> bean) {
		if (bean.size() > 0) {
			CustomDealAdapter adapter = new CustomDealAdapter(
					CustomDealActivity.this, bean);
			listview.setAdapter(adapter);
		}

	}

	private void addListener() {
		btn_back_customdeal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CustomDealActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class MyAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = NetHelper.doGet(Global.CUSTOMDEAL_URL + "mobile="
					+ phoneNum);
			return result;
		}

		@SuppressLint("DefaultLocale")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.toDimiss();
			}
			if (result == null || "请求服务器失败".equals(result)) {
				Toast.makeText(CustomDealActivity.this, "请求服务器失败，请检查网络", 0)
						.show();
				CustomDealActivity.this.finish();
			} else if (resState(result).equals("0")) {
				Toast.makeText(CustomDealActivity.this, "无订单记录", 0).show();
				CustomDealActivity.this.finish();
			} else {
				CustomDealResultBean dealBean = JSONArray.parseObject(
						result.toLowerCase(), CustomDealResultBean.class);
				// Toast.makeText(CustomDealActivity.this, result, 0).show();
				initListViewData(dealBean.getBody().getSuccess());
			}
		}
	}

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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(CustomDealActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}
	
}

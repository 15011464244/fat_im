package com.newcdc.activity.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.service.LocationService;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.Utils;

/**
 * 设置中的提醒
 * 
 * @author liunannan
 */
public class Setting_TX extends BaseActivity implements OnClickListener {

	private EditText et_dw_distance, et_dw_time, et_yj_td_time, et_yj_ls_time;
	private LinearLayout ll_dw, ll_yj, ll_back;
	private CheckBox cb_dingwei, cb_yujing;
	private boolean isDW = false, isYJ = false;
	private TextView tv_ok;
	private SharePreferenceUtilDaoFactory shareDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.function_install_tx);
		initView();
		addListener();
		initData();
	}

	private void initView() {
		shareDao = SharePreferenceUtilDaoFactory.getInstance(getActivity());

		et_dw_distance = (EditText) findViewById(R.id.et_dw_distance);// 定位提醒中的距离
		et_dw_time = (EditText) findViewById(R.id.et_dw_time);// 定位提醒中的时间
		et_yj_td_time = (EditText) findViewById(R.id.et_yj_td_time);// 预警提醒中的时间
		et_yj_ls_time = (EditText) findViewById(R.id.et_yj_ls_time);// 预警提醒中揽收的时间

		ll_dw = (LinearLayout) findViewById(R.id.ll_dw); // 定位提醒隐藏的布局
		ll_yj = (LinearLayout) findViewById(R.id.ll_yj); // 预警提醒隐藏的布局
		ll_back = (LinearLayout) findViewById(R.id.ll_back); //

		cb_dingwei = (CheckBox) findViewById(R.id.cb_dingwei); //
		cb_yujing = (CheckBox) findViewById(R.id.cb_yujing); //

		tv_ok = (TextView) findViewById(R.id.tv_ok); //
	}

	private void addListener() {
		ll_dw.setOnClickListener(this);
		ll_yj.setOnClickListener(this);
		ll_back.setOnClickListener(this);
		cb_dingwei.setOnClickListener(this);
		cb_yujing.setOnClickListener(this);
		tv_ok.setOnClickListener(this);
		et_dw_distance.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					if (s != null && !"".equals(s.toString())) {
						int userInput = Integer.parseInt(s.toString());
						if (userInput > 200) {
							Toast.makeText(getActivity(), "不可大于200米!",
									Toast.LENGTH_SHORT).show();
							et_dw_distance.setText("200");
							Editable editable = et_dw_distance.getText();
							Selection.setSelection(editable, editable.length());
						}
					}
				} catch (Exception e) {
				}
			}
		});
		et_yj_ls_time.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					if (s != null && !"".equals(s.toString())) {
						int userInput = Integer.parseInt(s.toString());
						if (userInput > 60) {
							Toast.makeText(getActivity(), "揽收预警时间不可大于60分钟！",
									Toast.LENGTH_SHORT).show();
							et_yj_ls_time.setText("60");
							Editable editable = et_yj_ls_time.getText();
							Selection.setSelection(editable, editable.length());
						}
					}
				} catch (Exception e) {
				}
			}
		});
		et_yj_td_time.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					if (s != null && !"".equals(s.toString())) {
						int userInput = Integer.parseInt(s.toString());
						if (userInput > 120) {
							Toast.makeText(getActivity(), "投递预警时间不可大于120分钟！",
									Toast.LENGTH_SHORT).show();
							et_yj_td_time.setText("120");
							Editable editable = et_yj_td_time.getText();
							Selection.setSelection(editable, editable.length());
						}
					}
				} catch (Exception e) {
				}
			}
		});
	}

	private void initData() {
		if (shareDao.getIsDWTX()) {
			ll_dw.setVisibility(View.VISIBLE);
			cb_dingwei.setChecked(true);
			isDW = true;
			setDW();
			setYJ();
		} else {
			ll_dw.setVisibility(View.GONE);
			cb_dingwei.setChecked(false);
		}
		if (shareDao.getIsYJ()) {
			cb_yujing.setChecked(true);
			ll_yj.setVisibility(View.VISIBLE);
			isYJ = true;

		} else {
			ll_yj.setVisibility(View.GONE);
			cb_yujing.setChecked(false);
		}
	}

	private void setYJ() {
		if (!shareDao.getYJ_TD_time().equals("")) {
			et_yj_td_time.setText(shareDao.getYJ_TD_time());
		} else {
			et_yj_td_time.setText(2 * 60 + "");
		}
		if (!shareDao.getYJ_LS_time().equals("")) {
			et_yj_ls_time.setText(shareDao.getYJ_LS_time());
		} else {
			et_yj_ls_time.setText(1 * 60 + "");
		}
	}

	private void setDW() {
		et_dw_distance.setText(shareDao.getDW_distance());
		if (!shareDao.getDW_time().equals("")) {
			et_dw_time.setText(shareDao.getDW_time());
		} else {
			et_dw_time.setText(3 * 60 + "");
		}
	}

	private Activity getActivity() {
		return Setting_TX.this;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.cb_dingwei:
			if (isDW) {
				ll_dw.setVisibility(View.GONE);
				isDW = false;
			} else {
				ll_dw.setVisibility(View.VISIBLE);
				setDW();
				isDW = true;
			}
			break;
		case R.id.cb_yujing:
			if (isYJ) {
				ll_yj.setVisibility(View.GONE);
				isYJ = false;
			} else {
				ll_yj.setVisibility(View.VISIBLE);
				setYJ();
				isYJ = true;
			}
			break;
		case R.id.tv_ok:
			Intent locationService = new Intent(Setting_TX.this,
					LocationService.class);

			if (cb_dingwei.isChecked()) {
				// 如果选中定位提醒了 要保存的值就不能为空的
				if ("".equals(et_dw_distance.getText().toString().trim())) {
					Toast.makeText(getActivity(), "定位距离不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if ("".equals(et_dw_time.getText().toString().trim())) {
					Toast.makeText(getActivity(), "定位时间不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!shareDao.getDW_distance().equals(
						et_dw_distance.getText().toString())) {
					// 如果改变了定位距离设置,则重启定位服务
					stopService(locationService);
					startService(locationService);
				}
				shareDao.setIsDWTX(true);
			} else {// 用户选择关闭定位服务
				stopService(locationService);
				shareDao.setIsDWTX(false);
			}
			shareDao.setDW_distance(et_dw_distance.getText().toString());
			shareDao.setDW_time(et_dw_time.getText().toString());
			if (cb_yujing.isChecked()) {
				// 如果选中预警提醒了 要保存的值就不能为空的
				if ("".equals(et_yj_ls_time.getText().toString().trim())) {
					Toast.makeText(getActivity(), "揽收时间不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if ("".equals(et_yj_td_time.getText().toString().trim())) {
					Toast.makeText(getActivity(), "投递时间不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				shareDao.setIsYJ(true);
			} else {
				shareDao.setIsYJ(false);
			}
			shareDao.setYJ_TD_time(et_yj_td_time.getText().toString());
			shareDao.setYJ_LS_time(et_yj_ls_time.getText().toString());
			Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(Setting_TX.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}
	
}

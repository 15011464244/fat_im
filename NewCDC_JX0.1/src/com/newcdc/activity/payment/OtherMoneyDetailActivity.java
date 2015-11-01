package com.newcdc.activity.payment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newcdc.R;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.ExtraCastDao;
import com.newcdc.model.ExtraCast;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * @author hanrong
 * @version 创建时间：2014-12-29 下午7:04:42 类说明 其他金额明细
 */
public class OtherMoneyDetailActivity extends Activity {
	private Dialog longdia;
	private Button btn_back_othermoney;
	private LinearLayout container;
	private ExtraCastDao mExtraCastDao;
	private List<ExtraCast> queryExtraCastall = null;
	private List<EditText> item_list = null;
	private UserInfoUtils mInfoUtils;
	private Button msave_othermoney;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_othermoneydetail);
		mExtraCastDao = DeliverDaoFactory.getInstance().getExtraCastDao(OtherMoneyDetailActivity.this);
		mInfoUtils = new UserInfoUtils(getApplicationContext());
		item_list = new ArrayList<EditText>();
		init();
//		 initData();
		 addListener();
	}

	private void init() {
//		ArrayList<ExtraCast> list = new ArrayList<ExtraCast>();
//		for (int i = 0; i < 5; i++) {
//			ExtraCast bean = new ExtraCast();
//			bean.setFlmc("面单" + i);
//			bean.setFldh("c" + i);
//			bean.setMoney("1" + i);
//			list.add(bean);
//		}
//		mExtraCastDao.deleteGroup();
//		mExtraCastDao.insertExtraCast(list);
		queryExtraCastall = mExtraCastDao.queryExtraCastall_username(mInfoUtils.getUserDelvorgCode(),mInfoUtils.getUserName());
		btn_back_othermoney = (Button) findViewById(R.id.btn_back_othermoney);
		msave_othermoney = (Button) findViewById(R.id.save_othermoney);
		container = (LinearLayout) findViewById(R.id.othermoney_lin);
		Log.e("tag", "qita=" + queryExtraCastall.get(0).getMoney());
		
		if (queryExtraCastall.size() > 0) {

			for (int i = 0; i < queryExtraCastall.size(); i++) {
				View item = LayoutInflater.from(OtherMoneyDetailActivity.this).inflate(R.layout.othermoney_item, null);
				final TextView txt_neme_item = (TextView) item.findViewById(R.id.name_item);
				final EditText edit_money_item = (EditText) item.findViewById(R.id.othermoney_item);
				txt_neme_item.setText(queryExtraCastall.get(i).getFlmc() + ":");
				if (!"0.0".equals(queryExtraCastall.get(i).getMoney())) {
					edit_money_item.setText(queryExtraCastall.get(i).getMoney());
					Log.e("tag", "money="+queryExtraCastall.get(i).getMoney());
				}
				edit_money_item.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						try {
							if (s != null && !"".equals(s.toString())) {
								double userInput = Double.parseDouble(s.toString());
								if (userInput > 100000.000) {
									Toast.makeText(OtherMoneyDetailActivity.this, "实收款最多为100000", Toast.LENGTH_SHORT).show();
									edit_money_item.setText("100000");
									Editable editable = edit_money_item.getText();
									Selection.setSelection(editable, editable.length());
								} else if ((userInput + "").length() - (userInput + "").indexOf(".") - 1 > 3) {
									Toast.makeText(OtherMoneyDetailActivity.this, "最大精确3位小数", Toast.LENGTH_SHORT).show();
									NumberFormat nf = NumberFormat.getNumberInstance();
									nf.setMaximumFractionDigits(3);
									edit_money_item.setText(nf.format(userInput));
									Editable editable = edit_money_item.getText();
									Selection.setSelection(editable, editable.length());
								}
							}
						} catch (Exception e) {
						}
					}
				});
				item_list.add(edit_money_item);
				container.addView(item);
			}
		}
		
	}
	private void addListener() {
		btn_back_othermoney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OtherMoneyDetailActivity.this.finish();
			}
		});
		msave_othermoney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i = 0; i < item_list.size(); i++) {
					String str = item_list.get(i).getText().toString();
					if(!"".equals(str) && str != null ){
						mExtraCastDao.updateOther_Money(Integer.parseInt(queryExtraCastall.get(i).get_id()), str);
					}
					Toast.makeText(OtherMoneyDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
					OtherMoneyDetailActivity.this.finish();
					Log.e("tag", "edt=" + str );
				}
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			OtherMoneyDetailActivity.this.finish();
//			for (int i = 0; i < item_list.size(); i++) {
//				Log.e("tag", "quer=" + mExtraCastDao.queryExtraCastall_username(mInfoUtils.getUserDelvorgCode(), mInfoUtils.getUserName()).get(i).getMoney() + mExtraCastDao.queryExtraCastall().get(i).get_id()+"--");
//			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		Utils.startIntentService(OtherMoneyDetailActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

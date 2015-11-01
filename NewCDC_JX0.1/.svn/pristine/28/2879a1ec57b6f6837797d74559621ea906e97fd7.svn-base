package com.newcdc.activity.clcttask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.newcdc.R;
import com.newcdc.tools.Global;
import com.newcdc.tools.Utils;

public class JiPaoActivity extends Activity {

	private Button jisuan, back;
	private EditText et_length, et_width, et_hight;
	private TextView et_volume;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ji_pao);
		initView();
	}

	public void initView() {
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(clicklistener);
		jisuan = (Button) findViewById(R.id.jisuan);
		jisuan.setOnClickListener(clicklistener);
		et_length = (EditText) findViewById(R.id.length);
		et_width = (EditText) findViewById(R.id.width);
		et_hight = (EditText) findViewById(R.id.hight);
		et_volume = (TextView) findViewById(R.id.tj);
	}

	private OnClickListener clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.jisuan:
				if (isEnter()) {
					double result = initData();
					Global.jipaoWeight = result + "";
					et_volume.setText(Global.jipaoWeight);
				}
				break;
			}
		}
	};

	private boolean isEnter() {
		if ("".equals(et_length.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "请输入长度！",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if ("".equals(et_width.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "请输入宽度！",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if ("".equals(et_hight.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "请输入高度！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public double initData() {
		double length = 0, width = 0, hight = 0;
		length = Double.parseDouble(et_length.getText().toString().trim());
		width = Double.parseDouble(et_width.getText().toString().trim());
		hight = Double.parseDouble(et_hight.getText().toString().trim());
		return (double) (length * width * hight / 8000);
	}

	@Override
	protected void onResume() {
		Utils.startIntentService(JiPaoActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

package com.newcdc.activity.clcttask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dtr.zxing.activity.CaptureActivity;
import com.newcdc.R;
import com.newcdc.model.FastLanBean;
import com.newcdc.tools.Utils;
import com.newcdc.ui.InfoDialog;
import com.newcdc.ui.XFAudio;

public class BackMailActivity extends Activity {
	
	private Button saoyisao,back,say,return_bucun;
	private TextView et_fanshishoukuan,et_fandakehu,et_fanjidadi;
	private FastLanBean fastLanBean;
	private EditText et_itemNum;
	private String mainmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.back_mail);
		
		saoyisao = (Button)findViewById(R.id.saoyisao);
		saoyisao.setOnClickListener(clicklistener);
		
		back = (Button)findViewById(R.id.back);
		back.setOnClickListener(clicklistener);
		
		Bundle bundle = this.getIntent().getExtras();
		 mainmail = bundle.getString("mainmail");
		String adrr = bundle.getString("adrr");
		String customer = bundle.getString("customer");
		
		et_fanshishoukuan=(TextView) findViewById(R.id.et_fanshishoukuan);
		et_fandakehu=(TextView) findViewById(R.id.et_fandakehu);
		et_fanjidadi=(TextView) findViewById(R.id.et_fanjidadi);
		et_fanshishoukuan.setText(mainmail);
		et_fandakehu.setText(customer);
		et_fanjidadi.setText(adrr);
		
		return_bucun=(Button) findViewById(R.id.return_bucun);
		return_bucun.setOnClickListener(clicklistener);
		addSound();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 111){
		String 	result=	data.getStringExtra("txtResult");
		if (mainmail.equals(result)) {
			showdiag("返单号与邮件号不能相同");
		}else {
			et_itemNum.setText(result);
		}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 给返单号添加语音功能
	 */
	public void addSound(){
		say = (Button) findViewById(R.id.say);
		et_itemNum = (EditText) findViewById(R.id.et_itemNum);
		XFAudio sayAudio = new XFAudio(BackMailActivity.this, say, et_itemNum);
		sayAudio.toSay();
	}
	 private OnClickListener clicklistener = new OnClickListener() {
			@Override
			public void onClick(View v) {
			    switch (v.getId()) {
			    case R.id.saoyisao:
			    	startActivityForResult(new Intent(BackMailActivity.this,
							CaptureActivity.class),1);
			    	break;
			    case R.id.back:
			    	finish();
			    	break;
			    case R.id.return_bucun:	
			    	/*if (et_fanshishoukuan.getText().toString() == null|| et_fanshishoukuan.getText().toString().trim().length() <= 0)
					 {Toast.makeText(BackMailActivity.this, "请输入正确的实收款",
						Toast.LENGTH_SHORT).show();
			} else if (et_fandakehu.getText().toString().toString() == null|| et_fandakehu.getText().toString().trim().length() <= 0) {
				Toast.makeText(BackMailActivity.this, "请输入大客户名",
						Toast.LENGTH_SHORT).show();
			} else if (et_fanjidadi.getText().toString()==null|| et_fanjidadi.getText().toString().trim().length() <= 0) {
				Toast.makeText(BackMailActivity.this, "请输入正确的地址",
						Toast.LENGTH_SHORT).show();
			}else */
				if (et_itemNum.getText().toString()==null|| et_itemNum.getText().toString().trim().length() <= 0) {
					
					
					showdiag("请输入正确的返单号");
//				Toast.makeText(BackMailActivity.this, "请输入正确的返单号",
//						Toast.LENGTH_SHORT).show();
				}else {
				    Intent intent = new Intent(BackMailActivity.this, NoClctActivity.class);
			    	Bundle bundle = new Bundle();
			    	bundle.putString("et_itemNum", et_itemNum.getText().toString().trim());
			    	intent.putExtras(bundle);
			    	setResult(1, intent);
					Toast.makeText(BackMailActivity.this, "添加成功",
							Toast.LENGTH_SHORT).show();
					finish();
			    	break;
					}
			    }
			}
		    };
		    /*
		     * 返单页面dialog
		     */
		    private void showdiag(String info) {
				// TODO Auto-generated method stub
				InfoDialog infoDialog=new InfoDialog(this, info);
				infoDialog.Show();

			}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(BackMailActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

			

}

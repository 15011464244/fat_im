package com.ems.express.ui;

import com.ems.express.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BaseActivity extends Activity {
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			onBackClick();
		}
	};
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		View back = findViewById(R.id.img_back);
		if(back != null){
			back.setOnClickListener(clickListener);
		}
	}
	protected void setHeadTitle(String title){
		TextView textView = (TextView)findViewById(R.id.tv_title);
		if(textView != null){
			textView.setText(title);
		}
	}
	protected void onBackClick(){
		this.finish();
	}
}

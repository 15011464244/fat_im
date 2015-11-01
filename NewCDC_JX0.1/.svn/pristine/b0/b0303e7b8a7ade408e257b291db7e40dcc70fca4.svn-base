package com.newcdc.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.application.BaseActivity;

/**
 * 
 * @author lion提示dialog
 * 
 */
public class InfoDialog {
	private Dialog dialog = null;
	private String info = "";
	private Context context = null;

	public InfoDialog(Context context, String info) {
		this.context = context;
		this.info = info;
		initView();
	}

	private void initView() {
		if (null == dialog) {
			dialog = new Dialog(context, R.style.dialogss);
		}
		View layout = LayoutInflater.from(context).inflate(
				R.layout.dialog_info, null);
		dialog.setContentView(layout, new LayoutParams(
				BaseActivity.width * 17 / 20, LayoutParams.WRAP_CONTENT));
		dialog.setCanceledOnTouchOutside(false);
		TextView txt_info = (TextView) layout.findViewById(R.id.txt_info);
		txt_info.setText(info);
		Button btn_info = (Button) layout.findViewById(R.id.btn_info);
		btn_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	public void Show() {
		try {
			if (null != dialog) {
				dialog.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void Dismiss() {
		try {
			if (null != dialog) {
				dialog.dismiss();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}

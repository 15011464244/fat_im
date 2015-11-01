package com.newcdc.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.application.BaseActivity;

/**
 * 
 * 
 * @author lion 自定义progress 2014-12-26
 * 
 */
public class ProgressDialog {
	Dialog mydialog = null;
	Handler handler;
	private boolean isHandler = false;
	private TextView dialogtxt;

	public ProgressDialog(Context context, String info) {
		if (mydialog == null) {
			mydialog = new Dialog(context, R.style.dialogss);
		}
		initView(context, mydialog, info);
	}

	public ProgressDialog(Context context, String info, Handler handler) {
		this.handler = handler;
		if (mydialog == null) {
			mydialog = new Dialog(context, R.style.dialogss);
		}
		isHandler = true;
		initView(context, mydialog, info);
	}

	public void setMessage(String msg) {
		dialogtxt.setText(msg);
	}

	public boolean isShowing() {
		return mydialog.isShowing();
	}

	public void setCanCalcelable(boolean canCancel) {
		mydialog.setCancelable(canCancel);
		mydialog.setCanceledOnTouchOutside(canCancel);
	}

	private void initView(Context context, Dialog dialog, String info) {
		float scale = BaseActivity.scale;
		View layout = LayoutInflater.from(context).inflate(
				R.layout.dialog_progress, null);
		dialogtxt = (TextView) layout.findViewById(R.id.progress_txt);
		ImageView dialogiv = (ImageView) layout.findViewById(R.id.progress_iv);
		dialog.setContentView(layout, new LayoutParams((int) (175 * scale),
				(int) (175 * scale)));
		dialog.setCanceledOnTouchOutside(false);
		dialogtxt.setText(info);
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.anim_dialog);
		anim.setInterpolator(new LinearInterpolator());
		dialogiv.startAnimation(anim);
		// final AnimationDrawable ad = (AnimationDrawable) dialogiv
		// .getBackground();
		// dialogiv.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// ad.start();
		// }
		// });
		if (isHandler) {
			dialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						handler.sendEmptyMessage(1);
						return true;
					}
					return false;
				}
			});
		}
	}

	public void toShow() {
		if (!mydialog.isShowing()) {
			mydialog.show();
		}
	}

	public void toDimiss() {
		if (mydialog.isShowing()) {
			mydialog.dismiss();
		}
	}
}

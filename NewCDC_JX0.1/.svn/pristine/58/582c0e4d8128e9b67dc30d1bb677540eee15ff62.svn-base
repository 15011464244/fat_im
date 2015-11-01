/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dtr.zxing.activity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.dtr.zxing.camera.CameraManager;
import com.dtr.zxing.decode.DecodeThread;
import com.dtr.zxing.utils.BeepManager;
import com.dtr.zxing.utils.CaptureActivityHandler;
import com.dtr.zxing.utils.InactivityTimer;
import com.google.zxing.Result;
import com.newcdc.R;

/**
 * @author hanrong
 * @version 创建时间：2014-12-22 上午10:27:01 类说明 ：扫码页
 */
public final class CaptureActivity extends Activity implements
		SurfaceHolder.Callback, OnClickListener {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private Context context;
	private SurfaceView scanPreview = null;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;
	private TextView back;
	private TextView text_isLamp;
	private Rect mCropRect = null;
	private boolean isLamp = false;
	private int width;
	private int height;
	private boolean fromDF;// 是否来自于下段页
	private ArrayList<String> mails;

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	private boolean isHasSurface = false;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_capture);
		DisplayMetrics metric = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		height = metric.heightPixels;
		context = getApplicationContext();
		fromDF = getIntent().getBooleanExtra("fromDF", false);// 初始化fromDF
		mails = new ArrayList<String>();
		scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(this);
		text_isLamp = (TextView) findViewById(R.id.text_isLamp);
		text_isLamp.setOnClickListener(this);
		RelativeLayout.LayoutParams linearParams = (LayoutParams) scanCropView
				.getLayoutParams();
		linearParams.height = width * 3 / 4;
		linearParams.width = width * 3 / 4;
		scanCropView.setLayoutParams(linearParams);

		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.9f);
		animation.setDuration(3000);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		scanLine.startAnimation(animation);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			if (fromDF) {
				Intent data = new Intent();
				data.putExtra("mailList", mails);
				setResult(111, data);
				finish();
			} else {
				finish();
			}
			break;
		case R.id.text_isLamp:
			if (!isLamp) {
				cameraManager.setTorch(true);
				text_isLamp.setBackgroundResource(R.drawable.barcode_torch_on);
				isLamp = true;
			} else {
				cameraManager.setTorch(false);
				text_isLamp.setBackgroundResource(R.drawable.barcode_torch_off);
				isLamp = false;
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		handler = null;

		if (isHasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(scanPreview.getHolder());
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			scanPreview.getHolder().addCallback(this);
		}

		inactivityTimer.onResume();
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		beepManager.close();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * 
	 * @param bundle
	 *            The extras
	 */
	public void handleDecode(Result rawResult, Bundle bundle) {
		inactivityTimer.onActivity();
		beepManager.playBeepSoundAndVibrate();
		// TODO
		String mail_num = rawResult.getText();
		bundle.putInt("width", mCropRect.width());
		bundle.putInt("height", mCropRect.height());
		bundle.putString("result", mail_num);
		if (fromDF) {// 如果是从下段页过来则按照队列形式进行存储邮件号，直到返回
			// showDialog(mail_num);
			if (mailExits(mail_num)) {
				Toast.makeText(context, "邮件号已扫描，请勿重复扫描", Toast.LENGTH_SHORT)
						.show();
			} else {
				mails.add(mail_num);
				Toast.makeText(context, "扫描结果:" + mail_num, Toast.LENGTH_SHORT)
						.show();
				Toast.makeText(context,
						"当前已扫描" + mails.size() + "个订单，按返回键结束扫描",
						Toast.LENGTH_SHORT).show();
			}
			if (handler != null) {
				handler.sendEmptyMessageDelayed(R.id.restart_preview, 3000);
			}
		} else {
			Intent intent = new Intent();
			Bundle bundle2 = new Bundle();
			bundle2.putString("txtResult", mail_num);
			intent.putExtras(bundle2);
			setResult(111, intent);
			CaptureActivity.this.finish();
		}

		// Intent intent = new
		// Intent(CaptureActivity.this,CountLanTouActivity.class);
		// intent.putExtras(bundle);
		// setResult(1, intent);
		// CaptureActivity.this.finish();
		// startActivity(new Intent(CaptureActivity.this,
		// ResultActivity.class).putExtras(bundle));

	}

	/**
	 * 做投递
	 */
	private void showDialog(final String mail_num) {
		View layout = LayoutInflater.from(context).inflate(R.layout.dialog_sms,
				null);
		Button cancel = (Button) layout.findViewById(R.id.info_cancel);
		Button sure = (Button) layout.findViewById(R.id.info_sure);
		TextView custom_dia_centercall = (TextView) layout
				.findViewById(R.id.custom_dia_centercall);
		TextView title = (TextView) layout.findViewById(R.id.txt_tit);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		title.setText("确认扫描结果:");
		custom_dia_centercall.setText("邮件号：<" + mail_num + ">");
		manager.getDefaultDisplay().getMetrics(dm);
		final Dialog dealDialog = new Dialog(context, R.style.dialogss);
		int width = dm.widthPixels;
		dealDialog.setContentView(layout, new LayoutParams(width * 15 / 20,
				LayoutParams.WRAP_CONTENT));
		dealDialog.setCancelable(true);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dealDialog.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mailExits(mail_num)) {
					Toast.makeText(context, "邮件号已存在于列表中，无需再次扫描",
							Toast.LENGTH_SHORT).show();
				} else {
					mails.add(mail_num);
					Toast.makeText(getApplicationContext(),
							"当前已扫描" + mails.size() + "个订单，按返回键结束扫描",
							Toast.LENGTH_SHORT).show();
				}
				dealDialog.dismiss();
			}
		});
		dealDialog.show();
	}

	public boolean mailExits(String mail_num) {
		for (int i = 0; i < mails.size(); i++) {
			if (mail_num.equals(mails.get(i))) {
				return true;
			}
		}
		return false;
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, cameraManager,
						DecodeThread.ALL_MODE);
			}

			initCrop();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		// camera error
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
//		builder.setMessage("相机打开出错，请稍后重试");
		builder.setMessage("无法获取摄像头数据，请检查是否已经打开摄像头权限。");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public Rect getCropRect() {
		return mCropRect;
	}

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (fromDF) {
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("mailList", mails);
				data.putExtras(bundle);
				setResult(111, data);
			} else {
				finish();
			}
			break;
		case KeyEvent.KEYCODE_FOCUS:
		case KeyEvent.KEYCODE_CAMERA:
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (!isLamp) {
				cameraManager.setTorch(true);
				text_isLamp.setBackgroundResource(R.drawable.barcode_torch_on);
				isLamp = true;
			} else {
				cameraManager.setTorch(false);
				text_isLamp.setBackgroundResource(R.drawable.barcode_torch_off);
				isLamp = false;
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (!isLamp) {
				cameraManager.setTorch(true);
				text_isLamp.setBackgroundResource(R.drawable.barcode_torch_on);
				isLamp = true;
			} else {
				cameraManager.setTorch(false);
				text_isLamp.setBackgroundResource(R.drawable.barcode_torch_off);
				isLamp = false;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
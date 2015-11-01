package com.newcdc.version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.application.LoginActivity;
import com.newcdc.application.WelcomeActivity;

/**
 * 新版本更新信息
 * 
 * @author
 * @since 2014_12_2
 * 
 */
public class NewVersion {
	private String tag = "Config";
	private Context context;
	private String downloadPath = "这里是下载apk的地址";
	private String downloadJson;
	private String appVersion;
	private ProgressDialog progress;
	private File file;
	private int sid;
	private String newProjectName;
	private String URL;
	private String newcontent;
	private String newVersionCode;
	private String view;
	private String test = "cdc.apk";
	// private String download = "http://172.27.35.8:8080/myhtml/test.apk";
	private WelcomeActivity activity;

	// downloadApk(download);
	/**
	 * 构造函数
	 * 
	 * @param context上下文
	 * @param downloadPath下载地址
	 * @param appVersion
	 *            应用程序版本信息文件
	 */
	public NewVersion(Context context, String downloadPath,
			String downloadJson, String appVersion) {
		this.context = context;
		this.downloadPath = downloadPath;
		this.downloadJson = downloadJson;
		this.appVersion = appVersion;
	}

	public void setActivity(WelcomeActivity activity) {
		this.activity = activity;
	}

	/**
	 * 检测更新
	 * 
	 * @throws Exception
	 */
	public void checkUpdateVersion() throws Exception {
		if (!isNetworkAvailable()) {
			Toast.makeText(context, "网络不可用！", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(context, LoginActivity.class);
					context.startActivity(intent);
					activity.finish();
				}
			}, 500);
		} else {

			// JSON版本信息下载
			// 下载方式自由选择 只需要下载完成得到字符串后传入getServer()方法中进行解析并判断是否需要弹窗提示更新版本
			// 以下为我的封装请求类 请自行更换 此处请使用 downloadJson该链接进行JSON数据下载
			ProtocolService.getViersion(PhoneInfoUtils.getIMEI(context),
					new RequestCallBack<String>() {
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// Log.i("newversion", arg0.result);
							// Toast.makeText(context, arg0.result,
							// Toast.LENGTH_SHORT).show();
							if (getServerVersion(arg0.result)) {
								// !CurrentVersion.getVersinName(context).equals(newVersionCode)

								if (Double.parseDouble(newVersionCode
										.toString().trim()) > Double
										.parseDouble(CurrentVersion
												.getVersinName(context)
												.toString().trim())) {
									showUpdateDialog();
								} else {
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											Intent intent = new Intent(context,
													LoginActivity.class);
											context.startActivity(intent);
											activity.finish();
										}
									}, 500);
								}
							}
						}

						@Override
						public void onFailure(
								com.lidroid.xutils.exception.HttpException arg0,
								String arg1) {
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									Intent intent = new Intent(context,
											LoginActivity.class);
									context.startActivity(intent);
									activity.finish();
								}
							}, 500);
						}
					});
		}
	}

	/**
	 * 获取新版本信息 对网络下载信息进行解析，如果解析异常说明不需要更新
	 * 
	 * @param newVersionJson
	 * @return
	 */
	private boolean getServerVersion(String newVersionJson) {

		try {
			JSONObject jsonObject = new JSONObject(newVersionJson);
			JSONObject jsonObject2 = jsonObject.getJSONObject("body");
			JSONArray jsonArray = jsonObject2.getJSONArray("success");
			JSONObject jsonObject3 = jsonArray.getJSONObject(0);
			if (jsonArray.length() > 0) {
				newcontent = jsonObject3.getString("content");
				sid = Integer.parseInt(jsonObject3.getString("sid"));
				newProjectName = jsonObject3.getString("project_name");
				newVersionCode = jsonObject3.getString("version");
				URL = jsonObject3.getString("url");
//				Log.e("tag", "apkURL"+URL);
				// Toast.makeText(context, URL, Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			newcontent = "";
			sid = -1;
			newProjectName = "";
			newVersionCode = "1.0";
			URL = "";
			// Log.e(tag, e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 显示更新对话框
	 */

	private void showUpdateDialog() {
		final Dialog dialog = new Dialog(context, R.style.dialogss);
		View v = LayoutInflater.from(context).inflate(
				R.layout.dialog_new_verson, null);
		dialog.setContentView(v, new LayoutParams(BaseActivity.width * 17 / 20,
				LayoutParams.WRAP_CONTENT));
		dialog.setCancelable(false);
		TextView tv_cur_verson_dialog_newverson = (TextView) v
				.findViewById(R.id.tv_cur_verson_dialog_newverson);
		TextView tv_new_version_dialog_newverson = (TextView) v
				.findViewById(R.id.tv_new_version_dialog_newverson);
		TextView tv_content_dialog_newverson = (TextView) v
				.findViewById(R.id.tv_content_dialog_newverson);
		Button btn_cancel_dialog_newverson = (Button) v
				.findViewById(R.id.btn_cancel_dialog_newverson);
		Button btn_positive_dialog_newverson = (Button) v
				.findViewById(R.id.btn_positive_dialog_newverson);
		tv_cur_verson_dialog_newverson.setText(CurrentVersion
				.getVersinName(context));
		tv_new_version_dialog_newverson.setText(newVersionCode);
		tv_content_dialog_newverson.setText(newcontent);
		btn_cancel_dialog_newverson.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				activity.finish();
			}
		});
		btn_positive_dialog_newverson.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				progress = new ProgressDialog(context);
				progress.setTitle("正在下载...");
				progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progress.setIndeterminate(false);
				progress.setCanceledOnTouchOutside(false);
				progress.setProgress(0);
				progress.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						Intent intent = new Intent(context, LoginActivity.class);
						context.startActivity(intent);
						activity.finish();
					}
				});
				// progress.show();
				// downloadApk(URL);
				openUrl(URL);
			}
		});
		dialog.show();
	}

	/**
	 * 使用默认浏览器打开窗口
	 * */
	protected void openUrl(String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		activity.startActivity(intent);
	}

	/**
	 * 下载文件
	 */
	private void downloadApk(String apkPath) {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.get(apkPath, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				if (file != null) {
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							progress.dismiss();
							showcomplelteDiloage1();
						}
					}, 500);
				} else {
					progress.dismiss();
					Toast.makeText(context, "下载文件失败，请退出程序，重新进入下载！",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(context, LoginActivity.class);
					context.startActivity(intent);
					activity.finish();
				}

			}

			@SuppressLint("NewApi")
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				progress.setProgressNumberFormat("%1d k/%2d k");
				progress.setMax(byteToKB(totalSize));
				progress.setProgress(byteToKB(bytesWritten));
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				super.onFailure(arg0, arg1, arg2, arg3);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				System.out.println(arg1);
				try {
					file = new File(Environment.getExternalStorageDirectory(),
							test);
					FileOutputStream fs = new FileOutputStream(file);
					fs.write(arg2, 0, arg2.length);
					fs.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 下载完成对话框
	 */

	private void showcomplelteDiloage1() {
		Dialog dialog = new Dialog(context, R.style.dialogss);
		View v = LayoutInflater.from(context).inflate(
				R.layout.dialog_installnewapk, null);
		dialog.setCancelable(false);
		Button info_cancel_installnewapk = (Button) v
				.findViewById(R.id.info_cancel_installnewapk);
		Button info_sure_installnewapk = (Button) v
				.findViewById(R.id.info_sure_installnewapk);
		dialog.setContentView(v, new LayoutParams(BaseActivity.width * 17 / 20,
				LayoutParams.WRAP_CONTENT));
		info_cancel_installnewapk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				progress.dismiss();
				activity.finish();
			}
		});
		info_sure_installnewapk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				installNewApk();
				progress.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 安装文件
	 */
	private void installNewApk() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 字节转换为Mb
	 * 
	 * @param 字节大小
	 * @return Mb大小
	 */
	private float byteToMB(int bt) {
		int mb = 1024 * 1024;
		float f = (float) bt / mb;
		float temp = Math.round(f * 100);
		return temp / 100;
	}

	/**
	 * 字节转换为kb
	 * 
	 * @param 字节大小
	 * @return kb大小
	 */
	private int byteToKB(int bt) {
		return Math.round(bt / 1024);
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return 是否可用
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni != null && ni.isAvailable());
	}
}

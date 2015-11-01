package com.ems.express.util;

import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ems.express.R;

import android.R.bool;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AppUpdateUtil {
	
	private  Context mContext;
	private AnimationUtil animationUtil;
	
	private Boolean Downing = SpfsUtil.loadDowning();
	
	public AppUpdateUtil( Context mContext) {
		this.mContext = mContext;
		animationUtil = new AnimationUtil(mContext, R.style.dialog_animation);
	}

	// 获取本地的版本信息
	public  PackageInfo getLocalInfo() {
		PackageManager manager;
		PackageInfo info = null;
		manager = mContext.getPackageManager();
		try {
			info = manager.getPackageInfo(mContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}

	// 从服务器下载apk:
	public static File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// 获取到文件的大小
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(),
					"updata.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
		}
	}
	
	
	/*
	 * 从服务器中下载APK
	 */ 
	protected void downLoadApk(final String path) { 
	    final ProgressDialog pd;    //进度条对话框  
	    pd = new ProgressDialog(mContext); 
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
	    pd.setMessage("正在下载更新"); 
	    pd.show(); 
	    new Thread(){ 
	        @Override 
	        public void run() { 
	            try { 
	                File file = getFileFromServer(path, pd); 
	                sleep(3000); 
	                installApk(file); 
	                pd.dismiss(); //结束掉进度条对话框  
	                SpfsUtil.saveDowning(false);
	            } catch (Exception e) { 
	                Message msg = new Message(); 
	                msg.what = DOWN_ERROR; 
	                handler.sendMessage(msg); 
	                SpfsUtil.saveDowning(false);
	                e.printStackTrace(); 
	            } 
	        }}.start(); 
	}
	
	
	
	//安装apk   
	protected void installApk(File file) { 
	    Intent intent = new Intent(); 
	    //执行动作  
	    intent.setAction(Intent.ACTION_VIEW); 
	    //执行的数据类型  
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");//编者按：此处Android应为android，否则造成安装不了   
	    mContext.startActivity(intent); 
	}
	/*
	 * 进入程序的主界面
	 */ 
	private void LoginMain(Context mContext){ 
//	    Intent intent = new Intent(mContext,MainActivity.class); 
//	    startActivity(intent); 
	    //结束掉当前的activity   
//	    this.finish(); 
	}
	
	Handler handler = new Handler(){     
	    @Override 
	    public void handleMessage(Message msg) { 
	        // TODO Auto-generated method stub  
	        super.handleMessage(msg); 
	        switch (msg.what) { 
	        case UPDATA_CLIENT: 
	            //对话框通知用户升级程序   
//	            showUpdataDialog(); 
	            break; 
	            case GET_UNDATAINFO_ERROR: 
	            	SpfsUtil.saveDowning(false);
	                //服务器超时   
	                Toast.makeText(mContext.getApplicationContext(), "获取服务器更新信息失败", 1).show(); 
	                return;
			case DOWN_ERROR: 
				SpfsUtil.saveDowning(false);
	                //下载apk失败  
	                Toast.makeText(mContext.getApplicationContext(), "下载新版本失败", 1).show(); 
	                return;   
	            } 
	    } 
	};
	
	 public final static int UPDATA_CLIENT = 1;
	 public final static int GET_UNDATAINFO_ERROR = 2;
	 public final static int DOWN_ERROR = 3;
//	 public final static int UPDATA_CLIENT = 1;
	
	
	//版本更新
	public void appUpdate(final PackageInfo info){
			FIR.checkForUpdateInFIR(mContext,"215210965@qq.com" , new VersionCheckCallback() {

				@Override
				public void onError(Exception arg0) {
					// TODO Auto-generated method stub
					System.out.println("onError"+"       "+arg0);
					ToastUtil.show(mContext, "检测更新出错！");
					animationUtil.dismiss();
				}

				@Override
				public void onFail(String arg0, int arg1) {
					// TODO Auto-generated method stub
					System.out.println("onFail"+"==== "+arg0+"===="+arg1);
					ToastUtil.show(mContext, "检测更新失败，请您检测是否开启网络！");
					animationUtil.dismiss();
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					System.out.println("onFinish");
					animationUtil.dismiss();
					
				}

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					System.out.println("onStart");
					animationUtil.show();
				}

				@Override
				public void onSuccess(AppVersion arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
					PackageInfo localInfo = getLocalInfo();
					String localVersion = localInfo.versionName;
					String serverVersion = arg0.getVersionName();
					String updateUrl = arg0.getUpdateUrl();
					
					float localV = Float.valueOf("0."+localVersion.replace(".", ""));
					float serverV = Float.valueOf("0."+serverVersion.replace(".", ""));
					
					if(localV > serverV){
						updateConfirm(mContext, arg0.getUpdateUrl(),"您安装的是新版本，是否退回旧版本？");
					}else if(localV < serverV){
						updateConfirm(mContext, arg0.getUpdateUrl(),"检测到新版本，是否更新？");
					}else{
						DialogUtils.successMessage(mContext, "您当前使用的是最新版本！");
					}
					
//					if(localVersion.equals(serverVersion)){
//						DialogUtils.successMessage(mContext, "您当前使用的是最新版本！");
//					}else {
//						updateConfirm(mContext, arg0.getUpdateUrl());
//					}
					
					
					
				}
			
			});
		}
	
	//更新确认
	public Dialog updateConfirm(final Context context,final String path,String content){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		((TextView) view.findViewById(R.id.dialog_content)).setText("检测到新版本，是否更新？\n版本更新消耗流量，建议在WiFi下进行更新！");
		
		Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
		buttonDetermine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(!Downing){
					SpfsUtil.saveDowning(true);
					downLoadApk(path);
				}else{
					ToastUtil.show(mContext, "后台下载中！");
				}
				
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return null;
	}
	
}

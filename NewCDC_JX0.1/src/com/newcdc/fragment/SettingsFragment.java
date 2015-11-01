package com.newcdc.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gicom.device.SerialPortFactory;
import com.gicom.device.Bluetooth.BluetoothSerialPortInfo;
import com.gicom.device.Bluetooth.SerialPort;
import com.newcdc.R;
import com.newcdc.activity.bluetooth.BluetoothSetActivity;
import com.newcdc.activity.usercenter.PasswordActivity;
import com.newcdc.activity.usercenter.Setting_TX;
import com.newcdc.application.LoginActivity;
import com.newcdc.application.MainActivity;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.FastLanDao;
import com.newcdc.db.QueueDao;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.service.AutoBlurtoothonnectionService;
import com.newcdc.service.BlurtoothService;
import com.newcdc.service.CollectionUploadService;
import com.newcdc.service.DownDCDataService;
import com.newcdc.service.JXAsyncQueueService;
import com.newcdc.service.JXDownDataService;
import com.newcdc.service.ListenNetStateService;
import com.newcdc.service.LocationService;
import com.newcdc.service.ResetPushServise;
import com.newcdc.service.SendMessService;
import com.newcdc.service.TestServiceNetStateService;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.CustomDialog;
import com.newcdc.ui.GetBitmap;
import com.newcdc.ui.ProgressDialog;
import com.newcdc.ui.XFAudio;
import com.newcdc.version.CurrentVersion;

/**
 * 功能：设置
 * 
 * @author liunannan
 */
public class SettingsFragment extends Fragment implements OnClickListener {
	private View mView;
	private TextView tv_update_tx, tv_clean_cache, tv_nicname, tv_name,
			tv_version;
	private Button btn_logout;
	private Button btn_mobile;
	private Button btn_updatepassword;
	private ImageView btn_back;
	private CheckBox cb_pda;
	private boolean isPDA = false;
	private SharePreferenceUtilDaoFactory shareDao;
	private View layout;
	private LayoutInflater inflater;
	private int width;
	private Dialog longdia;
	private Dialog changeMobileDialog;
	private com.newcdc.ui.RoundImageView iv_head;
	private Bitmap bitmap = null;
	private UserInfoUtils userDao;
	private byte[] imgs;// 要上传的图片字节
	private String dlvcode = "";
	private String usercode = "";
	private String mobile = "";
	private Button btn_loginout = null;
	private ProgressDialog dialog = null;
	// private UserDao user;
	private DeliverDaoFactory daoFactory;
	private FastLanDao mFastLanDao;
	private QueueDao mQueueDao;
	private TextView mbtn_bluetooth;
	private TextView mtxt_bluetooth_conn;
	private Intent bluetoothService;// 蓝牙扫描枪监听服务
	private Intent autobluetoothService;// 蓝牙枪自动连接服务
	private int bluetoothCount = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.function_install, container, false);
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		initView();
		addListener();
		return mView;
	}

	private void initView() {
		shareDao = SharePreferenceUtilDaoFactory.getInstance(getActivity());
		daoFactory = DeliverDaoFactory.getInstance();
		// user = daoFactory.getUserDao(getActivity());
		mFastLanDao = daoFactory.getFastLanDao(getActivity());
		mQueueDao = daoFactory.getQueueDao(getActivity());
		btn_loginout = (Button) mView.findViewById(R.id.btn_loginout);
		tv_update_tx = (TextView) mView.findViewById(R.id.tv_update_tx);// 提醒设置
		tv_clean_cache = (TextView) mView.findViewById(R.id.tv_clean_cache);// 清除缓存
		btn_logout = (Button) mView.findViewById(R.id.btn_logout);// 注销账号
		btn_updatepassword = (Button) mView
				.findViewById(R.id.btn_updatepassword);// 修改密码
		cb_pda = (CheckBox) mView.findViewById(R.id.cb_pda);// 蓝牙打印机绑定
		tv_version = (TextView) mView.findViewById(R.id.tv_version);
		iv_head = (com.newcdc.ui.RoundImageView) mView
				.findViewById(R.id.iv_head);// 头像
		btn_mobile = (Button) mView.findViewById(R.id.btn_mobile);// 手机号
		tv_nicname = (TextView) mView.findViewById(R.id.tv_nicname);// 名字
		tv_name = (TextView) mView.findViewById(R.id.tv_name);// 工号
		mbtn_bluetooth = (TextView) mView.findViewById(R.id.btn_bluetooth);
		mtxt_bluetooth_conn = (TextView) mView
				.findViewById(R.id.txt_bluetooth_conn);
		userDao = new UserInfoUtils(getActivity().getApplicationContext());
		usercode = userDao.getUserName();
		dlvcode = userDao.getUserDelvorgCode();
		mobile = userDao.getMobile();
		changeMobileDialog = new Dialog(getActivity(), R.style.dialogss);
		tv_version.setText("v" + CurrentVersion.getVersinName(getActivity()));
		btn_back = (ImageView) mView
				.findViewById(R.id.btn_back_function_install);
		btn_mobile.setText(mobile + "");
		if (null != userDao.getRealname()) {
			tv_nicname.setText(userDao.getRealname());
		}
		if (null != usercode) {
			tv_name.setText(usercode);
		}
		File f = new File(Constant.SD + "myhead" + dlvcode + usercode
				+ Constant.HEADIMG);
		if (f.exists()) {
			bitmap = BitmapFactory.decodeFile(Constant.SD + "myhead" + dlvcode
					+ usercode + Constant.HEADIMG);
			if (null != bitmap) {
				iv_head.setImageBitmap(bitmap);
			}
		}
		if (shareDao.getisPDA()) {
			cb_pda.setChecked(true);
			isPDA = true;
		} else {
			cb_pda.setChecked(false);
		}
	}

	private void addListener() {
		tv_update_tx.setOnClickListener(this);
		cb_pda.setOnClickListener(this);
		tv_clean_cache.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		btn_updatepassword.setOnClickListener(this);
		iv_head.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_mobile.setOnClickListener(this);
		tv_version.setOnClickListener(this);
		btn_loginout.setOnClickListener(this);
		mbtn_bluetooth.setOnClickListener(this);
		mtxt_bluetooth_conn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head:
			Intent imageCaptureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(imageCaptureIntent, 1);
			break;
		case R.id.tv_update_tx:
			Intent intent = new Intent(getActivity(), Setting_TX.class);
			startActivity(intent);
			break;
		case R.id.cb_pda:
			if (isPDA) {
				isPDA = false;
				shareDao.setisPDA(isPDA);
			} else {
				isPDA = true;
				shareDao.setisPDA(isPDA);
			}
			break;
		case R.id.tv_clean_cache:
			// 显示对话框 提示是否清除缓存
			clearCathedialog();
			break;
		case R.id.btn_logout:
			// List<GatherInfoBean> lanList = mFastLanDao
			// .queryFastLanWeiShangChuang();
			ArrayList<DeliverQueueBean> allList = mQueueDao.queryAll();
			if (allList.size() > 0) {
				if (longdia == null) {
					longdia = new Dialog(getActivity(), R.style.dialogss);
					userExit_Judge_Dialog(longdia);
					XFAudio xf = new XFAudio(getActivity(), "您有未上传的揽投信息");
					xf.startSay();
				} else {
					XFAudio xf = new XFAudio(getActivity(), "您有未上传的揽投信息");
					xf.startSay();
					userExit_Judge_Dialog(longdia);
				}
			} else {
				userExitDialog();
			}

			break;
		case R.id.btn_back_function_install:
			switchFragment(6);
			break;
		case R.id.btn_mobile:
			showChangeMobileDialog();
			break;
		case R.id.btn_loginout:
			userLogout();
			break;
		case R.id.btn_bluetooth:
			startActivity(new Intent(getActivity(), BluetoothSetActivity.class));
			break;

		case R.id.txt_bluetooth_conn:
			connectBluetooth();
			break;
		case R.id.btn_updatepassword:
			Intent intentpassword = new Intent();
			intentpassword.setClass(getActivity(), PasswordActivity.class);
			startActivity(intentpassword);
			break;
		default:
			break;
		}
	}

	public void showChangeMobileDialog() {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_info_changemobile, null);
		changeMobileDialog.setContentView(view, new LayoutParams(
				width * 17 / 20, LayoutParams.WRAP_CONTENT));
		changeMobileDialog.setCancelable(true);
		final EditText et_input = (EditText) view
				.findViewById(R.id.et_number_changeMobile);
		view.findViewById(R.id.btn_negtive__changeMobile).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						changeMobileDialog.dismiss();
					}
				});
		view.findViewById(R.id.btn_positive_changeMobile).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Editable editable = et_input.getText();
						int length = editable.length();
						if (length == 0) {
							Toast.makeText(getActivity(), "不可为空！",
									Toast.LENGTH_SHORT).show();
						} else if (length != 11 && length != 0) {
							Toast.makeText(getActivity(), "手机号格式有误！",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getActivity(), "操作成功！",
									Toast.LENGTH_SHORT).show();
							String mobile = editable.toString();
							btn_mobile.setText(mobile);
							changeMobileDialog.dismiss();
							// UserInfoBean userInfo = user.getUserInfo();
							// user.updateMobile(mobile, userInfo.getLdb()
							// .getSid());
						}
					}
				});
		changeMobileDialog.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Toast.makeText(getActivity(),
						"SD card is not avaiable and writeable right now.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Bundle bundle = data.getExtras();
			bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			GetBitmap.saveBitmap(bitmap, "myhead" + dlvcode + usercode
					+ Constant.HEADIMG);
			iv_head.setImageBitmap(bitmap);
			imgs = GetBitmap.getByteFromImage(bitmap);
			if (null != imgs) {
				new SetHeadImg().execute();
			}
		}
	}

	class SetHeadImg extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject result = null;
			String photo = Base64.encodeToString(imgs, 0, imgs.length,
					Base64.DEFAULT);
			try {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("dlvorgcode", userDao
						.getUserDelvorgCode()));
				pairs.add(new BasicNameValuePair("username", userDao
						.getUserName()));
				pairs.add(new BasicNameValuePair("image", photo));
				result = new JSONObject(NetHelper.doPost(
						Global.UPDATEEMPLOYEEIMAGE, pairs));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (null != result) {
				try {
					if ("1".equals(result.get("result"))) {// 成功
						Toast.makeText(getActivity(), "头像上传成功",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(
								getActivity(),
								result.getJSONObject("body").getString("error"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 清除缓存dialog
	 * **/
	private void clearCathedialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle("清除缓存");
		builder.setMessage("确定要清除图片缓存么？");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DelCache();
			}
		});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void DelCache() {
		File file = new File(Constant.SD);
		if (file != null && file.exists() && file.isDirectory()) {
			for (File item : file.listFiles()) {
				item.delete();
			}
			file.delete();
		}
		shareDao.setHead_img(null);
		Toast.makeText(getActivity(), "清除图片缓存成功", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 用户退出登录
	 * **/
	private void userExitDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle("退出登录");
		builder.setMessage("退出登录后，您将无法使用投递与揽收功能，是否确定退出？");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				shareDao.clearEditor();
				// UserDao.deleteUserInfo();
				// PushDao.deletepush();
				stopAllService();
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * 判断是否有未上传数据
	 * **/
	private void userExit_Judge_Dialog(final Dialog longdia) {
		inflater = getActivity().getLayoutInflater();
		layout = inflater.inflate(R.layout.dialog_userexitjude_layout, null);
		Button sure = (Button) layout.findViewById(R.id.jude_sure);
		Button cancel = (Button) layout.findViewById(R.id.jude_cancel);
		TextView title = (TextView) layout
				.findViewById(R.id.title_dialog_userexit);
		title.setText("上传提示");
		longdia.setContentView(layout, new LayoutParams(width * 17 / 20,
				LayoutParams.WRAP_CONTENT));
		longdia.setCancelable(true);
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				longdia.dismiss();
				userExitDialog();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				longdia.dismiss();
			}
		});
		longdia.show();
	}

	/**
	 * 用户注销
	 */
	private void userLogout() {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle("注销账号");
		builder.setMessage("注销账号后，将取消本机和您账号的绑定，确定要注销吗？");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				outLogin();
			}
		});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * 关闭服务
	 */
	public void stopAllService() {
		getActivity().stopService(
				new Intent(getActivity(), LocationService.class));
		getActivity().stopService(
				new Intent(getActivity(), DownDCDataService.class));
		getActivity().stopService(
				new Intent(getActivity(), SendMessService.class));
		getActivity().stopService(
				new Intent(getActivity(), JXAsyncQueueService.class));
		getActivity().stopService(
				new Intent(getActivity(), CollectionUploadService.class));
		getActivity().stopService(
				new Intent(getActivity(), JXDownDataService.class));
		getActivity().stopService(
				new Intent(getActivity(), ListenNetStateService.class));
		getActivity().stopService(
				new Intent(getActivity(), ResetPushServise.class));
		getActivity().stopService(
				new Intent(getActivity(), AutoBlurtoothonnectionService.class));
		getActivity().stopService(
				new Intent(getActivity(), BlurtoothService.class));
		getActivity().stopService(
				new Intent(getActivity(), TestServiceNetStateService.class));
	}

	private void switchFragment(int fragmentFlag) {
		MainActivity activity = (MainActivity) getActivity();
		activity.switchContentFragment(fragmentFlag);
		// activity.toggle();
	}

	// 退出登录
	private void outLogin() {
		String url = Global.OUTLOGIN + "username=" + usercode + "&password="
				+ LoginActivity.outlogin_pass + "&delvorgcode=" + dlvcode
				+ "&device_id=" + Utils.getDeviceId(getActivity());
		Log.e("settings->url", url);
		dialog = new ProgressDialog(getActivity(), "正在注销");
		dialog.toShow();
		new MyOutLogin().execute(url);
	}

	class MyOutLogin extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = com.newcdc.tools.NetHelper.doGet(params[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.toDimiss();
			Log.e("settings->result", result);
			if ("请求服务器失败".equals(result)) {
				Toast.makeText(getActivity(), "退出登录失败", Toast.LENGTH_SHORT)
						.show();
			} else if ("0".equals(resState(result))) {
				JSONObject object;
				String error = "error";
				try {
					object = new JSONObject(result);
					error = object.getJSONObject("body").getString("error");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
			} else if ("1".equals(resState(result))) {
				JSONObject object;
				String success = "success";
				try {
					object = new JSONObject(result);
					success = object.getJSONObject("body").getString("success");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Toast.makeText(getActivity(), success, Toast.LENGTH_SHORT)
						.show();
				shareDao.clearEditor();
				// user.deleteUserInfo();
				DeliverDaoFactory.getInstance().getNewUserDao(getActivity())
						.deleteUserInfo();
				daoFactory.getPushDao(getActivity()).deletepush();
				stopAllService();
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		}

	}

	// 获得返回值中result字段的值
	public String resState(String s) {
		String str = "";
		try {
			JSONObject json = new JSONObject(s);
			str = json.get("result").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return str;
	}

	@Override
	public void onResume() {
		Utils.startIntentService(getActivity());// 启动投递、揽收异步上传服务
		super.onResume();
	}

	/**
	 * 蓝牙连接
	 */
	private void connectBluetooth() {
		Log.e("tag", "连接");
		if (!"".equals(shareDao.getBlueTooth())) {
			Log.e("tag", "blue_shareDao存了");
			BluetoothSerialPortInfo serialInfo = new BluetoothSerialPortInfo();
			serialInfo.mac = shareDao.getBlueTooth();
			SerialPort serialPort = SerialPortFactory.getInstance(serialInfo);
			try {
				serialPort.Start();
				shareDao.setBlueTooth(shareDao.getBlueTooth());
				bluetoothService = new Intent(getActivity(),
						BlurtoothService.class);
				getActivity().startService(bluetoothService);
				autobluetoothService = new Intent(getActivity(),
						AutoBlurtoothonnectionService.class);
				getActivity().startService(autobluetoothService);
				Log.e("tag", "SettingsFragment==chenggong");
				Toast.makeText(getActivity(), "蓝牙连接成功", Toast.LENGTH_LONG)
						.show();
			} catch (Exception e) {
				// if (bluetoothCount < 4) {
				// bluetoothCount++;
				// try {
				// new Thread().sleep(1000);
				// } catch (InterruptedException e1) {
				// e1.printStackTrace();
				// }
				// connectBluetooth();
				// }
				Toast.makeText(getActivity(), "蓝牙连接失败请手动连接", Toast.LENGTH_LONG)
						.show();

				Log.e("tag", "SettingsFragment==shibai");
			}
		} else {
			Toast.makeText(getActivity(), "请先进行蓝牙配对", Toast.LENGTH_LONG).show();
		}
	}
}

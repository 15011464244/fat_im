package com.newcdc.application;

import java.io.File;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.db.PaymentDao;
import com.newcdc.tools.Constant;
import com.newcdc.tools.DragListView;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.ShowToast;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.ui.GetBitmap;

/**
 * 
 * @author zhangfan 2014-12-23 左滑菜单
 * 
 */
public class LeftFragment extends Fragment {
	private TextView tv_unread_count;//没有读的消息的数量
	private View mView;
	private com.newcdc.ui.RoundImageView iv_headimg;
	private SharePreferenceUtilDaoFactory shareDao;
	private Bitmap bitmap = null;
	private Dialog ivdialog;
	private int width;
	private int height;
	private UserInfoUtils userUtils;
	private String dlvcode = "";
	private String usercode = "";
	public BroadcastReceiver newMsgBReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			showMessageTag();
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_left, container, false);
		iv_headimg = (com.newcdc.ui.RoundImageView) mView
				.findViewById(R.id.iv_headimg);
		tv_unread_count =(TextView) mView.findViewById(R.id.tv_unread_count);
		shareDao = SharePreferenceUtilDaoFactory.getInstance(getActivity());
		userUtils = new UserInfoUtils(getActivity());
		
		// 注册一个自定义的广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("NewMsgReceiver_Action");
		getActivity().registerReceiver(newMsgBReceiver, filter);

		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		height = metric.heightPixels;
		initListener();// 设置监听器
		return mView;
	}

	private void setImg() {
		usercode = userUtils.getUserName();
		dlvcode = userUtils.getUserDelvorgCode();
		File f = new File(Constant.SD + "myhead" + dlvcode + usercode
				+ Constant.HEADIMG);
		if (f.exists()) {
			bitmap = BitmapFactory.decodeFile(Constant.SD + "myhead" + dlvcode
					+ usercode + Constant.HEADIMG);
			if (null != bitmap) {
				iv_headimg.setImageBitmap(bitmap);
			}
		} else {
			new GetHeadImg().execute();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		showMessageTag();
		userUtils = new UserInfoUtils(getActivity());
		setImg();
	}

	class GetHeadImg extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap result = null;
			try {
				result = NetHelper.doGetImg(Global.FINDEMPLOYEEIMAGE
						+ "dlvorgcode=" + dlvcode + "&username=" + usercode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// @SuppressWarnings("resource")
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null) {
				// try {
				// File fil=new File(Constant.SD);
				// fil.mkdirs();
				// File file=new File(fil, "myhead" + dlvcode
				// + usercode + Constant.HEADIMG);
				// FileOutputStream fos=new FileOutputStream(file);
				// fos.write(result);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				iv_headimg.setImageBitmap(result);
				GetBitmap.saveBitmap(result, "myhead" + dlvcode + usercode
						+ Constant.HEADIMG);
			}
		}
	}

	private void initListener() {
		mView.findViewById(R.id.rl_paymentfragment_left_fragment).setOnClickListener(
				new OnClickListener() {// 支付界面

					@Override
					public void onClick(View v) {
						switchFragment(0);
					}
				});
		mView.findViewById(R.id.btn_custom_left_fragment).setOnClickListener(
				new OnClickListener() {// 客户管理

					@Override
					public void onClick(View v) {
						switchFragment(1);
					}
				});
		mView.findViewById(R.id.btn_chat_left_fragment).setOnClickListener(
				new OnClickListener() {// 聊天

					@Override
					public void onClick(View v) {
						switchFragment(3);
					}
				});
		// mView.findViewById(R.id.btn_deliver_left_fragment).setVisibility(View.GONE);
		mView.findViewById(R.id.btn_deliver_left_fragment).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						switchFragment(4);// 下段
					}
				});
		mView.findViewById(R.id.btn_downdata_leftfragment).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						switchFragment(5);// 基础数据下载
					}
				});
		mView.findViewById(R.id.btn_settings_left_fragment).setOnClickListener(
				new OnClickListener() {// 设置

					@Override
					public void onClick(View v) {
						switchFragment(2);
					}
				});
		mView.findViewById(R.id.iv_headimg).setOnClickListener(
				new OnClickListener() {// 查看图片
					@Override
					public void onClick(View v) {
						File f = new File(Constant.SD + "myhead" + dlvcode
								+ usercode + Constant.HEADIMG);
						if (f.exists()) {
							reViewImage();
						} else {
							ShowToast.showToast(getActivity(), "请到设置界面中添加头像",
									1000);
						}
					}
				});
		mView.findViewById(R.id.btn_help_left_fragment).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								GuidActivity.class);
						intent.putExtra("fromMain", true);
						startActivityForResult(intent, 100);
					}
				});
	}

	private void reViewImage() {
		if (ivdialog == null) {
			ivdialog = new Dialog(getActivity(), R.style.dialogss);
		}
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_image_review, null);
		ivdialog.setContentView(v, new LayoutParams(width, height));
		ImageView review_image = (ImageView) v.findViewById(R.id.review_image);
		bitmap = BitmapFactory.decodeFile(Constant.SD + "myhead" + dlvcode
				+ usercode + Constant.HEADIMG);
		bitmap = GetBitmap.big(bitmap);
		review_image.setImageBitmap(bitmap);
		review_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ivdialog.dismiss();
			}
		});
		ivdialog.show();
	}

	private void switchFragment(int fragmentFlag) {
		MainActivity activity = (MainActivity) getActivity();
		activity.switchContentFragment(fragmentFlag);
		activity.toggle();
	}
	public void showMessageTag() {

		// 是否有未读寄件消息
		PaymentDao dao = new PaymentDao(getActivity());
		UserInfoUtils infoUtils = new UserInfoUtils(getActivity());
		Log.e("gongjie", "查询登陆用户的名字"+infoUtils.getUserName());
		int sendMsgCount = dao.queryUnReadMessage(null);
		if (sendMsgCount == 0) {
			tv_unread_count.setVisibility(View.GONE);
		} else {
			tv_unread_count.setVisibility(View.VISIBLE);
			tv_unread_count.setText(sendMsgCount + "");
		}
	}
}

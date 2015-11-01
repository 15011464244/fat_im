package com.newcdc.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.activity.censcus.CountActivity;
import com.newcdc.activity.clcttask.CollectionActivity_JX;
import com.newcdc.activity.delivertask.DeliverActivity;
import com.newcdc.activity.delivertask.DeliverTaskListActivity;
import com.newcdc.activity.payment.GiveMoneyActivity;
import com.newcdc.activity.usercenter.CustomActivity;
import com.newcdc.application.MainActivity;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.Gather_MsgDao;
import com.newcdc.model.GatherTableBean;
import com.newcdc.tools.BaiduGpsContants;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.GetBitmap;
import com.newcdc.weather.DownLoadImageAsyncTask;
import com.newcdc.weather.DownLoadImageAsyncTask.CallBack;
import com.newcdc.weather.HttpUtils;
import com.newcdc.weather.PaserJson;
import com.newcdc.weather.WeatherBean;

/**
 * 主页
 * 
 * @author zhangfan
 * 
 */
public class MainFragment extends Fragment {
	private View mView;
	private static TextView tv_titleAddr;
	private Gather_MsgDao gatherDao;
	private MyReceiver myReceiver;
	private Button btn_pushtask, btn_gather_pushtask;// 推送消息红点
	private TextView textView1, textView2, textView3;
	private ImageView imageView;
	private SharePreferenceUtilDaoFactory shareDao;
	private String share_weather;
	Handler mHandler1 = new Handler();
	public static String titleInfomation;// 标题跑马灯显示的内容
	private Button btn_census, btn_clcttask, btn_payment, btn_custom,
			btn_delivertask, btn_deliver;
	private String userCode, orgCode;
	private List<String> tmpList = new ArrayList<String>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_main, container, false);
		UserInfoUtils user = new UserInfoUtils(getActivity());
		gatherDao = DeliverDaoFactory.getInstance().getGather_msgdao(
				getActivity());
		orgCode = user.getUserDelvorgCode();// 机构号
		userCode = user.getUserName();// 用户的工号
		// Datamm();
		selectAppMeun();//员工权限菜单
		initView();
		initData();
		initListener();
		return mView;
	}

	private void Datamm() {
		for (int i = 1; i < 3; i++) {
			tmpList.add("" + i);
		}
	}

	private void initData() {
		shareDao = new SharePreferenceUtilDaoFactory();
		try {
			myReceiver = new MyReceiver();
			try {
				if (shareDao != null) {
					share_weather = shareDao.getWeatherInfo();
				}
				if (!"".equals(share_weather)) {
					String[] weatherInfo = share_weather.split(",");
					textView1.setText(weatherInfo[3]);
					textView2.setText(weatherInfo[4]);
					textView3.setText(weatherInfo[2]);
					File file = new File(Constant.WEATHER + weatherInfo[0]);
					if (file.exists()) {
						Time t = new Time();
						t.setToNow();
						int timeH = t.hour;
						if (timeH >= 8 && timeH < 20) {
							imageView.setImageBitmap(BitmapFactory
									.decodeFile(Constant.WEATHER
											+ weatherInfo[0]));
						} else {
							imageView.setImageBitmap(BitmapFactory
									.decodeFile(Constant.WEATHER
											+ weatherInfo[1]));
						}
					}

				}
			} catch (Exception e) {
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 获取标题跑马灯文字
	 * 
	 * @return
	 */
	public static String getTitle() {
		return tv_titleAddr.getText().toString();
	}

	/*
	 * 员工权限菜单
	 */
	public void selectAppMeun() {
		try {
			new Thread() {
				@Override
				public void run() {
					String result = NetHelper.doGet(Global.SELECTAPPMEUN
							+ "orgCode=" + orgCode + "&userCode=" + userCode);
					JSONObject resultObj;
					try {
						resultObj = new JSONObject(result);
						if (resultObj.getString("result").equals("1")) {
							org.json.JSONArray content = resultObj
									.getJSONArray("content");

							if (content != null && content.length() != 0) {
								for (int i = 0; i < content.length(); i++) {
									org.json.JSONObject jObject = content
											.getJSONObject(i);
									String menuCode = jObject
											.getString("menuCode");
									tmpList.add(menuCode);
								}
								Message msg = new Message();
								msg.what = 1;
								selectAppHandler.sendMessage(msg);
							}
						} else {
							Toast.makeText(getActivity(), "员工权限菜单返回失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					mHandler1.post(new Runnable() {
						@Override
						public void run() {
							initListener();
						}
					});
				};
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Handler selectAppHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				initListener();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 更新标题内容
	 */
	public void updateTitle() {
		if (Utils.stringEmpty(titleInfomation)) {// 没有需要的提示内容，则显示地址信息
			if (BaiduGpsContants.getInstance().getAddressStr() != null) {
				// 设置title的地址
				tv_titleAddr.setText(BaiduGpsContants.getInstance()
						.getAddressStr());
			} else {
				tv_titleAddr.setText("");
			}
		} else {
			tv_titleAddr.setText(titleInfomation);
		}
	}

	@Override
	public void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_LOCATION);// 接收到地址信息
		filter.addAction(Constant.ACTION_DOWN_DATA_OVER);// 数据更新完毕
		filter.addAction(Constant.ACTION_LOADING);// 正在加载数据
		getActivity().registerReceiver(myReceiver, filter);
		updateTitle();
		Utils.startIntentService(getActivity());// 启动投递、揽收异步上传服务
		super.onResume();
	}

	@Override
	public void onPause() {
		getActivity().unregisterReceiver(myReceiver);
		super.onPause();
	}

	private void initView() {
		tv_titleAddr = (TextView) mView
				.findViewById(R.id.tv_titleAddr_fragment_main);
		btn_pushtask = (Button) mView
				.findViewById(R.id.btn_new_pushtask_fragment_main);
		btn_gather_pushtask = (Button) mView
				.findViewById(R.id.btn_gather_pushtask_fragment_main);
		textView1 = (TextView) mView.findViewById(R.id.lp_main_textview1);
		textView2 = (TextView) mView.findViewById(R.id.lp_main_textview2);
		textView3 = (TextView) mView.findViewById(R.id.lp_main_textview3);
		imageView = (ImageView) mView.findViewById(R.id.lp_main_imageview);
	}

	private void initListener() {
		final View back = mView.findViewById(R.id.btn_back_fragment_main);
		back.setOnClickListener(new OnClickListener() {// 返回键
			@Override
			public void onClick(View v) {
				MainActivity main = (MainActivity) getActivity();
				main.toggle();
			}
		});
		btn_census = (Button) mView.findViewById(R.id.btn_census_fragment_main);
		if (tmpList != null && tmpList.size() != 0) {
			if (tmpList.contains("4")) {
				btn_census.setClickable(true);
				btn_census.setOnClickListener(new OnClickListener() {// 统计
							@Override
							public void onClick(View v) {
								startActivity(new Intent(getActivity(),
										CountActivity.class));
							}
						});
			} else {
				btn_census.setClickable(false);
				btn_census.setBackgroundResource(R.drawable.btn_census);
			}
		}
		btn_clcttask = (Button) mView
				.findViewById(R.id.btn_clcttask_fragment_main);
		if (tmpList != null && tmpList.size() != 0) {
			if (tmpList.contains("1")) {
				btn_clcttask.setClickable(true);
				btn_clcttask.setOnClickListener(new OnClickListener() {// 揽收任务
							@Override
							public void onClick(View v) {
								if (!Global.isLan) {
									Toast.makeText(getActivity(), "揽收功能敬请期待！",
											Toast.LENGTH_SHORT).show();
								} else {
									if (btn_gather_pushtask.isShown()) {
										Animation anim = AnimationUtils
												.loadAnimation(getActivity(),
														R.anim.anim_top);
										btn_gather_pushtask
												.startAnimation(anim);
										anim.setAnimationListener(new AnimationListener() {

											@Override
											public void onAnimationStart(
													Animation animation) {

											}

											@Override
											public void onAnimationRepeat(
													Animation animation) {

											}

											@Override
											public void onAnimationEnd(
													Animation animation) {
												// TODO 点击之后把状态修改了
//												List<GatherTableBean> beans = new ArrayList<GatherTableBean>();
//												beans = gatherDao
//														.orderByIsShow(orgCode,
//																userCode);
//												int size = gatherDao
//														.orderByIsShow(orgCode,
//																userCode)
//														.size();
//												for (int i = 0; i < size; i++) {
//													gatherDao
//															.updateIsShow(beans
//																	.get(i)
//																	.get_id());
//												}
												btn_gather_pushtask
														.setVisibility(View.GONE);
											}
										});
									}

									// startActivity(new Intent(getActivity(),
									// CollectionActivity.class));
									startActivity(new Intent(getActivity(),
											CollectionActivity_JX.class));
								}
							}
						});
			} else {
				btn_clcttask.setClickable(false);
				btn_clcttask.setBackgroundResource(R.drawable.btn_clcttask);
			}
		}
		btn_payment = (Button) mView
				.findViewById(R.id.btn_payment_fragment_main);
		if (tmpList != null && tmpList.size() != 0) {
			if (tmpList.contains("5")) {
				btn_payment.setClickable(true);
				btn_payment.setOnClickListener(new OnClickListener() {// 缴费
							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.setClass(getActivity(),
										GiveMoneyActivity.class);
								startActivity(intent);
							}
						});
			} else {
				btn_payment.setClickable(false);
				btn_payment.setBackgroundResource(R.drawable.btn_payment);
			}
		}
		btn_delivertask = (Button) mView
				.findViewById(R.id.btn_delivertask_fragment_main);
		if (tmpList != null && tmpList.size() != 0) {
			if (tmpList.contains("2")) {
				btn_delivertask.setClickable(true);
				btn_delivertask.setOnClickListener(new OnClickListener() {// 投递任务
							@Override
							public void onClick(View v) {
								// if (!DownDCDataService.inServer) {
								// getActivity().startService(
								// new Intent(getActivity(),
								// DownDCDataService.class));
								// }
								// if (btn_pushtask.isShown()) {
								// Animation anim = AnimationUtils
								// .loadAnimation(getActivity(),
								// R.anim.anim_top);
								// btn_pushtask.startAnimation(anim);
								// anim.setAnimationListener(new
								// AnimationListener() {
								//
								// @Override
								// public void onAnimationStart(
								// Animation animation) {
								//
								// }
								//
								// @Override
								// public void onAnimationRepeat(
								// Animation animation) {
								//
								// }
								//
								// @Override
								// public void onAnimationEnd(
								// Animation animation) {
								// btn_pushtask
								// .setVisibility(View.GONE);
								// }
								// });
								// }
								// // if (DownDCDataService.downDeliverComplete)
								// {
								// startActivity(new Intent(getActivity(),
								// TaskShowActivity.class));
								//

								// } else {
								// Toast.makeText(getActivity(),
								// "休息会儿，等下载完下段信息再点吧~",
								// Toast.LENGTH_SHORT).show();
								// }

								startActivity(new Intent(getActivity(),
										DeliverTaskListActivity.class));
							}
						});
			} else {
				btn_delivertask.setClickable(false);
				btn_delivertask
						.setBackgroundResource(R.drawable.btn_delivertask);
			}
		}

		btn_custom = (Button) mView.findViewById(R.id.btn_custom_fragment_main);
		if (tmpList != null && tmpList.size() != 0) {
			if (tmpList.contains("6")) {
				btn_custom.setClickable(true);
				btn_custom.setOnClickListener(new OnClickListener() {// 客户管理

							@Override
							public void onClick(View v) {
								startActivity(new Intent(getActivity(),
										CustomActivity.class));
							}
						});
			} else {
				btn_custom.setClickable(false);
				btn_custom.setBackgroundResource(R.drawable.btn_custom);
			}
		}

		btn_deliver = (Button) mView
				.findViewById(R.id.btn_deliver_fragment_main);
		if (tmpList != null && tmpList.size() != 0) {
			if (tmpList.contains("3")) {
				btn_deliver.setClickable(true);
				btn_deliver.setOnClickListener(new OnClickListener() {// 下段
							@Override
							public void onClick(View v) {
								startActivity(new Intent(getActivity(),
										DeliverActivity.class));
							}
						});
			} else {
				btn_deliver.setClickable(false);
				btn_deliver.setBackgroundResource(R.drawable.btn_deliver);
			}
		}

	}

	/**
	 * 接收广播：1-加载数据前 2-定位完成 3-推送信息
	 * 
	 * @author zhangfan
	 */
	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_LOADING.equals(intent.getAction())) {
				// 更新标题内容
				updateTitle();
			} else if (Constant.ACTION_LOCATION.equals(intent.getAction())) {
				// 定位获取到的地址改变通知
				String addr = intent.getStringExtra("addr");
				titleInfomation = addr;
				tv_titleAddr.setText(addr);
				if (!"定位失败".equals(addr)) {
					BaiduGpsContants gps = BaiduGpsContants.getInstance();
					String url = Global.TIANQI + "location=" + gps.getLng()
							+ "," + gps.getLat()
							+ "&output=json&ak=14BfsVtVmhlKSQnL6LuzrULL";
					Log.e("gongjie", "天气url"+url);
					new TianQiTask().execute(url);
				}
			} else if (Constant.ACTION_DOWN_DATA_OVER
					.equals(intent.getAction())) {
				// 数据更新完毕的通知
				int count = intent.getIntExtra("count", -2);
				int messageType = intent.getIntExtra("messageType", -1);
				switch (messageType) {
				case Constant.PUSH_TYPE_DELIVERTASK:
					switch (count) {
					case -1:
						btn_pushtask.setVisibility(View.GONE);
						break;
					case 0:
						btn_pushtask.setVisibility(View.GONE);
						break;
					default:
						btn_pushtask.setVisibility(View.VISIBLE);
						btn_pushtask.setText(count + "");
						break;
					}
					break;
				case Constant.PUSH_TYPE_CLCTTASK:
					switch (count) {
					case -1:
						btn_gather_pushtask.setVisibility(View.GONE);
						break;
					default:
						btn_gather_pushtask.setVisibility(View.VISIBLE);
						btn_gather_pushtask.setText(count + "");
						break;
					}
					break;
				case Constant.PUSH_TYPE_CLCTTASK2:
					// TODO 先查询出来数量
					int size = gatherDao.orderByIsShow(orgCode, userCode)
							.size();
					LogUtils.e("查询出来的数量是------------" + size);
					if (size > 0) {
						btn_gather_pushtask.setVisibility(View.VISIBLE);
						btn_gather_pushtask.setText(size + "");
					} else {
						btn_gather_pushtask.setVisibility(View.GONE);
					}
					break;
				}

			}
		}
	}

	class TianQiTask extends AsyncTask<String, String, List<WeatherBean>> {
		private Map<String, Bitmap> imageCache = new HashMap<String, Bitmap>();

		@Override
		protected List<WeatherBean> doInBackground(String... params) {
			List<WeatherBean> list = null;
			byte[] b = HttpUtils.getHttpResult(params[0]);
			if (b == null) {
				return null;
			}
			try {
				String jsonString = new String(b);
				try {
					list = PaserJson.paserJson(jsonString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (list != null && list.size() > 0) {
					StringBuffer sb = new StringBuffer();
					WeatherBean bean = list.get(0);
					String[] daiPic = bean.getDayPictureUrl().split("/");
					sb.append(daiPic[daiPic.length - 1] + ",");
					String[] neightPic = bean.getNightPictureUrl().split("/");
					sb.append(neightPic[neightPic.length - 1] + ",");
					sb.append(bean.getTemperature() + ",");
					sb.append(bean.getWeather() + ",");
					sb.append(bean.getWind());
					shareDao.setWeatherInfo(sb.toString());
				}
			} catch (Exception e) {

			}
			return list;

		}

		@Override
		protected void onPostExecute(List<WeatherBean> result) {
			if (result == null) {
				FragmentActivity activity = getActivity();
				if (activity != null) {
					Toast.makeText(activity, "请求服务器失败，请检查网络",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				if (result.size() > 0) {
					textView1.setText(result.get(0).getWeather());
					textView2.setText(result.get(0).getWind());
					textView3.setText(result.get(0).getTemperature());

					Time t = new Time();
					t.setToNow();
					int timeH = t.hour;
					if (timeH >= 8 && timeH < 20) {
						// 白天的图片的异步下载
						new DownLoadImageAsyncTask(imageCache, new CallBack() {

							@Override
							public void sendResult(String imagePath,
									Bitmap bitmap) {
								if (bitmap != null) {
									imageView.setImageBitmap(bitmap);
									// String weather =
									// shareDao.getWeatherInfo();
									// String[] split = weather.split(",");
									String weather = shareDao.getWeatherInfo();
									String[] weatherInfo = weather.split(",");
									GetBitmap.saveBitmapW(bitmap,
											weatherInfo[0]);
								}
							}
						}).execute(result.get(0).getDayPictureUrl());
					} else if (timeH < 8 || timeH >= 20) {
						// 晚上的图片的异步下载
						new DownLoadImageAsyncTask(imageCache, new CallBack() {

							@Override
							public void sendResult(String imagePath,
									Bitmap bitmap) {
								if (bitmap != null) {
									imageView.setImageBitmap(bitmap);
									String weather = shareDao.getWeatherInfo();
									String[] weatherInfo = weather.split(",");
									GetBitmap.saveBitmapW(bitmap,
											weatherInfo[1]);
								}
							}
						}).execute(result.get(0).getNightPictureUrl());
					}
				}
			}
		}

	}

}

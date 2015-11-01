package com.ems.express.ui.send;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.swu.pulltorefresh.RefreshTime;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.IXListViewListener;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.OnMenuItemClickListener;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.OnSwipeListener;
import cn.swu.swipemenulistview.SwipeMenu;
import cn.swu.swipemenulistview.SwipeMenuCreator;
import cn.swu.swipemenulistview.SwipeMenuItem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.send.SenderAdapter;
import com.ems.express.adapter.send.SenderAdapter.OnBtnClickListenerFinish;
import com.ems.express.bean.PeopleInfo;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.BaseActivityForRequest;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.lidroid.xutils.util.LogUtils;

public class SenderListActivity extends BaseActivityForRequest implements OnClickListener, IXListViewListener{
//	private SwipeMenuListView listView;
//	CustomListView listView;
	PullToRefreshSwipeMenuListView listView;

	private List<PeopleInfo> senderList;
	private Intent intent;
	private Context mContext;
	private int type = 0;
	private SenderAdapter senderAdapter;
	private TextView mTvInfo;
	
	private AnimationUtil animationUtil;
	
	public static int from;
	public final static int FROM_PERSONAL = 0;
	public final static int FROM_SEND = 1;
	
	
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sender_list);
		animationUtil = new AnimationUtil(this, R.style.dialog_animation);
		initView();
		loadView();
	}
	
	public void initView(){
		intent = getIntent();
		type = intent.getIntExtra("type", SenderInfoActivity.SENDER);
		from = intent.getIntExtra("from", SenderListActivity.FROM_SEND);
		((TextView) findViewById(R.id.tv_title))
				.setText(type == SenderInfoActivity.SENDER ? "寄件人列表" : "收件人列表");
		mTvInfo = (TextView) findViewById(R.id.tv_info);
		mTvInfo.setVisibility(View.VISIBLE);
		mTvInfo.setOnClickListener(SenderListActivity.this);
		mTvInfo.setText("添加");
		mTvInfo.setOnClickListener(this);
		mContext = this;
		listView = (PullToRefreshSwipeMenuListView) findViewById(R.id.listView);
		
		
	}
	
	public void loadView(){
		intent = getIntent();
		type = intent.getIntExtra("type", SenderInfoActivity.SENDER);
		senderList = new ArrayList<PeopleInfo>();
//		senderList = App.dbHelper.querySenderList(App.db, type);
		//以下是从数据库中查询
		if (App.dbHelper.querySenderMessage(App.db,SpfsUtil.loadPhone(),type+"") == null) {
			App.dbHelper.deleteSenderMessageByPhone(App.db, SpfsUtil.loadPhone(),type+"");
			getSenderList(SpfsUtil.loadPhone(), type);
		}else {
			senderList  = App.dbHelper.querySenderMessage(App.db,SpfsUtil.loadPhone(),type+"");
		}
		senderAdapter = new SenderAdapter(mContext, senderList,from,type);
		listView.setAdapter(senderAdapter);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		mHandler = new Handler();
		
		 // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
            	
            	/*// create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);*/

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
            
        };
        // set creator
        listView.setMenuCreator(creator);

        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
    			// TODO Auto-generated method stub

    			PeopleInfo sender = senderList.get(arg2-1);
    			Intent intent = new Intent();
    			intent.putExtra("info", sender);
    			setResult(RESULT_OK, intent);
    			if(from == FROM_SEND){
    				finish();
    			}
//    			Toast.makeText(SenderListActivity.this, "finish", Toast.LENGTH_SHORT).show();
    		}
        	
		});
        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                PeopleInfo item = senderList.get(position);
                Toast.makeText(SenderListActivity.this, "haha", 100).show();
                switch (index) {
                /*case 0:
                    // open
                    break;*/
                case 0:
                    // delete
                    // delete(item);
                	App.dbHelper.deleteSenderMessage(App.db,senderList.get(position).getCreateDate());
					delete(senderList.get(position).getId(),position);
                    break;
                }
            }
        });
        // set SwipeListener
        listView.setOnSwipeListener(new OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
        senderAdapter.setFinishListener(new OnBtnClickListenerFinish() {
    		
    		@Override
    		public void onClick(int position) {
    			// TODO Auto-generated method stub

    			PeopleInfo sender = senderList.get(position);
    			Intent intent = new Intent();
    			intent.putExtra("info", sender);
    			setResult(RESULT_OK, intent);
    			if(from == FROM_SEND){
    				finish();
    			}
//    			Toast.makeText(SenderListActivity.this, "finish", Toast.LENGTH_SHORT).show();
    		}
    	});
	}
	
 		/*DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
		int px = (int) (100 * (dm.densityDpi / 220f));
		listView.setOffsetLeft(mScreenWidth-px);
//		listView = (SwipeMenuListView)this.findViewById(R.id.lv_senderlist);
		listView.setAdapter(senderAdapter);
		//刷新寄件人列表
//		getSenderList(SpfsUtil.loadPhone(),type);
		
		 listView.setonRefreshListener(new OnRefreshListener() {
	            
	            @Override
	            public void onRefresh() {
	            	App.dbHelper.deleteSenderMessageByPhone(App.db, SpfsUtil.loadPhone(),type+"");
					getSenderListRefresh(SpfsUtil.loadPhone(), type);
	            }
	        });
		 senderAdapter.setDelListener(new OnBtnClickListener(){

				@Override
				public void onClick(int position) {
//					Toast.makeText(SenderListActivity.this, "deletde", Toast.LENGTH_SHORT).show();
					App.dbHelper.deleteSenderMessage(App.db,senderList.get(position).getCreateDate());
					delete(senderList.get(position).getId(),position);
				}	
			});
//		addMenu();
		 senderAdapter.setFinishListener(new OnBtnClickListenerFinish() {
			
			@Override
			public void onClick(int position) {
				// TODO Auto-generated method stub

				PeopleInfo sender = senderList.get(position);
				Intent intent = new Intent();
				intent.putExtra("info", sender);
				setResult(RESULT_OK, intent);
				if(from == FROM_SEND){
					finish();
				}
//				Toast.makeText(SenderListActivity.this, "finish", Toast.LENGTH_SHORT).show();
			}
		});
	
//		
	}*/
//	添加按钮
//		void addMenu(){
//			SwipeMenuCreator creator = new SwipeMenuCreator() {
//				@Override
//				public void create(SwipeMenu menu) {
//
//					// create "delete" item
//					SwipeMenuItem deleteItem = new SwipeMenuItem(
//							getApplicationContext());
//					// set item background
//					deleteItem.setBackground(new ColorDrawable(Color.rgb(255,
//							255, 255)));
//					// set item width
//					deleteItem.setWidth(dp2px(84));
//					// set a icon
//					deleteItem.setIcon(R.drawable.img_delete);
//					// add to menu
//					menu.addMenuItem(deleteItem);
//				}
//			};
//			listView.setMenuCreator(creator);
//			
//			listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//				@Override
//				public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//					 PeopleInfo item = senderList.get(position);
//					switch (index) {
//					case 0:
//						App.dbHelper.deleteSenderMessage(App.db,item.getCreateDate());
//						delete(item.getId(),position);
//					}
//					return true;
//				}
//			});
//			
//			// set SwipeListener
//			listView.setOnSwipeListener(new OnSwipeListener() {
//				@Override
//				public void onSwipeStart(int position) {}
//				
//				@Override
//				public void onSwipeEnd(int position) {}
//			});
//		}
	
	public static void actionStartForResult(Activity context, int flag,int from) {
		Intent intent = new Intent(context, SenderListActivity.class);
		intent.putExtra("type", flag);
		intent.putExtra("from", from);
		context.startActivityForResult(intent, flag);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			setResult(RESULT_OK, data);
			//若是来源于个人中心，停留在列表页，刷新，来源于寄件，返回到寄件页
			if(data.getIntExtra("from", FROM_SEND) == FROM_SEND){
				finish();
			}else{
//				senderList = App.dbHelper.querySenderList(App.db, type);
//				senderAdapter.notifyList(senderList);
				//刷新寄件人列表
//				getSenderList(SpfsUtil.loadPhone(),type);
				if (App.dbHelper.querySenderMessage(App.db,SpfsUtil.loadPhone(),type+"") == null) {
					App.dbHelper.deleteSenderMessageByPhone(App.db, SpfsUtil.loadPhone(),type+"");
					getSenderList(SpfsUtil.loadPhone(), type);
				}else {
					senderList  = App.dbHelper.querySenderMessage(App.db,SpfsUtil.loadPhone(),type+"");
				}
				senderAdapter.notifyList(senderList);
			}
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_info:
//			SenderInfoActivity.actionStartForResult(this,type);
			Intent intent = new Intent(mContext, SenderInfoActivity.class);
			intent.putExtra(SenderInfoActivity.INFO_TYPE,type );
			intent.putExtra("from",from);
			startActivityForResult(intent, type);
		/*case R.id.tv_info:
			getSenderList(SpfsUtil.loadPhone(), type);
			break;
*/
		default:
			break;
		}
		
	}
	private void startActivityForResult(Intent intent2, Intent intent3) {
		// TODO Auto-generated method stub
		
	}

	void edit(PeopleInfo info){
//		SenderInfoActivity.actionStartForResult(mContext, info,type);
		Intent intent = new Intent(mContext, SenderInfoActivity.class);
		intent.putExtra(SenderInfoActivity.INFO_TYPE,type );
		intent.putExtra("info", info);
		intent.putExtra("from", from);
		startActivityForResult(intent, type);
	}
	/**
	 * 删除寄件人（本地）
	 * @param index
	 */
	void delete(int index){
		App.dbHelper.removeSender(App.db, senderList.get(index));
		senderList.remove(index);
		senderAdapter.notifyDataSetChanged();
	}
	/**
	 * 删除寄件人（服务器）
	 * @param id
	 */
	void delete(String id,final int position){
		//开启菊花
		animationUtil.show();
		
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("sid", id);
		String params = ParamsUtil.getUrlParamsByMap(json);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.deleteSender,
				new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						LogUtils.e("deleteSender_response == "+arg0+"");
						//关闭菊花
						animationUtil.dismiss();
						//请求成功
//						if(((BaseActivityForRequest)mContext).stayThisPage){
							String result = (String) arg0;
							JSONObject object = JSONObject.parseObject(result.toString());
							if (object.getInteger("result") == 1) {
								ToastUtil.show(mContext, "删除成功");
//								senderList.remove(position);
								senderList  = App.dbHelper.querySenderMessage(App.db,SpfsUtil.loadPhone(),type+"");
								senderAdapter.notifyList(senderList);
							} else {
								//请求失败
								ToastUtil.show(mContext,object.getString("服务器数据删除失败"));
							}
//						}
					}
				
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						//关闭菊花
						animationUtil.dismiss();
						Toast.makeText(mContext, "请求异常!", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
					}
				}, params);
		App.getQueue().add(req);
	}
	
	/**
	 * 获取寄件人列表
	 * @return
	 */
	public void getSenderList(String phone,int type){
		//开启菊花
				animationUtil.show();
				HashMap<String, Object> json = new HashMap<String, Object>();
				json.put("mobile", phone);
				json.put("userType",type);
				String params = ParamsUtil.getUrlParamsByMap(json);
				MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.querySenderList,
						new Response.Listener<Object>() {

							@Override
							public void onResponse(Object arg0) {
								LogUtils.e("saveSender_response == "+arg0+"");
								Log.e("gongjie", ""+arg0);
								//关闭菊花
								animationUtil.dismiss();
//								listView.onRefreshComplete();
								//请求成功
								if(((BaseActivityForRequest)mContext).stayThisPage){
									String result = (String) arg0;
									JSONObject object = JSONObject.parseObject(result.toString());
									if (object.getInteger("result") == 1) {
//										ToastUtil.show(mContext, "保存成功");
										JSONArray array = object.getJSONArray("dataList");
										senderList.clear();
										for (Object people : array) {
											JSONObject peopleJson = JSONObject.parseObject(people.toString());
											PeopleInfo sender = new PeopleInfo();
											sender.setId(peopleJson.getString("sid"));
											sender.setUserId(peopleJson.getString("userId"));
											sender.setPhone(peopleJson.getString("userTel"));
											sender.setName(peopleJson.getString("userName"));
											sender.setType(peopleJson.getInteger("userType"));
											sender.setProv(peopleJson.getString("prov"));
											sender.setProvCode(peopleJson.getString("provCode"));
											sender.setCity(peopleJson.getString("city"));
											sender.setCityCode(peopleJson.getString("cityCode"));
											sender.setCounty(peopleJson.getString("county"));
											sender.setCountyCode(peopleJson.getString("countyCode"));
											sender.setLocation(peopleJson.getString("address"));
											sender.setCreateDate(peopleJson.getString("createDate"));
												String createDate = 
														peopleJson
																.getString("createDate");
												String updateDate = "";
												if (peopleJson.getString("updateDate") != null) {
													updateDate = peopleJson.getString("updateDate");
												}
												App.dbHelper
														.insertSenderMessage(
																App.db,
																sender.getId(),
																sender.getUserId(),
																SpfsUtil.loadPhone(),
																peopleJson.getString("userType"),
																sender.getName(),
																sender.getPhone(),
																sender.getProvCode(),
																sender.getCityCode(),
																sender.getCountyCode(),
																sender.getLocation(),
																sender.getProv(),
																sender.getCity(),
																sender.getCounty(),
																createDate,
																updateDate);
//											} catch (Exception e) {
//												Log.e("gongjie", "插入寄件人列表出错"+e);
//											}
											senderList.add(sender);
										}
										senderAdapter.notifyList(senderList);
									} else {
										//请求失败
										ToastUtil.show(mContext,object.getString("errorMsg"));
										
										
									}
								}
							}
						
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								//关闭菊花
								animationUtil.dismiss();
								Toast.makeText(mContext, "请求异常!", Toast.LENGTH_LONG).show();
								arg0.printStackTrace();
							}
						}, params);
				App.getQueue().add(req);
	}
	/**
	 * 刷新的时候获取寄件人列表，只少了一个动画
	 * @return
	 */
	public void getSenderListRefresh(String phone,final int type){
		//开启菊花
				HashMap<String, Object> json = new HashMap<String, Object>();
				json.put("mobile", phone);
				json.put("userType",type);
				String params = ParamsUtil.getUrlParamsByMap(json);
				MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.querySenderList,
						new Response.Listener<Object>() {

							@Override
							public void onResponse(Object arg0) {
								LogUtils.e("saveSender_response == "+arg0+"");
								Log.e("gongjie", ""+arg0);
							
								//关闭菊花
//								listView.onRefreshComplete();
								//请求成功
								if(((BaseActivityForRequest)mContext).stayThisPage){
									App.dbHelper.deleteSenderMessageByPhone(App.db, SpfsUtil.loadPhone(),type+"");
									String result = (String) arg0;
									JSONObject object = JSONObject.parseObject(result.toString());
									if (object.getInteger("result") == 1) {
//										ToastUtil.show(mContext, "保存成功");
										JSONArray array = object.getJSONArray("dataList");
										senderList.clear();
										for (Object people : array) {
											JSONObject peopleJson = JSONObject.parseObject(people.toString());
											PeopleInfo sender = new PeopleInfo();
											sender.setId(peopleJson.getString("sid"));
											sender.setUserId(peopleJson.getString("userId"));
											sender.setPhone(peopleJson.getString("userTel"));
											sender.setName(peopleJson.getString("userName"));
											sender.setType(peopleJson.getInteger("userType"));
											sender.setProv(peopleJson.getString("prov"));
											sender.setProvCode(peopleJson.getString("provCode"));
											sender.setCity(peopleJson.getString("city"));
											sender.setCityCode(peopleJson.getString("cityCode"));
											sender.setCounty(peopleJson.getString("county"));
											sender.setCountyCode(peopleJson.getString("countyCode"));
											sender.setLocation(peopleJson.getString("address"));
											sender.setCreateDate(peopleJson.getString("createDate"));
												String createDate = 
														peopleJson
																.getString("createDate");
												String updateDate = "";
												if (peopleJson.getString("updateDate") != null) {
													updateDate = peopleJson.getString("updateDate");
												}
												App.dbHelper
														.insertSenderMessage(
																App.db,
																sender.getId(),
																sender.getUserId(),
																SpfsUtil.loadPhone(),
																peopleJson.getString("userType"),
																sender.getName(),
																sender.getPhone(),
																sender.getProvCode(),
																sender.getCityCode(),
																sender.getCountyCode(),
																sender.getLocation(),
																sender.getProv(),
																sender.getCity(),
																sender.getCounty(),
																createDate,
																updateDate);
//											} catch (Exception e) {
//												Log.e("gongjie", "插入寄件人列表出错"+e);
//											}
											senderList.add(sender);
										}
										senderAdapter.notifyList(senderList);
									} else {
										//请求失败
										ToastUtil.show(mContext,object.getString("errorMsg"));
										
										
									}
								}
								onLoad();
							}
						
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								//关闭菊花
								animationUtil.dismiss();
								Toast.makeText(mContext, "请求异常!", Toast.LENGTH_LONG).show();
								arg0.printStackTrace();
							}
						}, params);
				App.getQueue().add(req);
	}
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	public void back(View v) {
		finish();
	}

	@Override
	public void onRefresh() {
		 /*mHandler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
	                RefreshTime.setRefreshTime(getApplicationContext(), df.format(new Date()));
	            	
	        		getSenderListRefresh(SpfsUtil.loadPhone(), type);
	        		
	            }
	        }, 2000);*/
		 mHandler.post(new Runnable() {
	            @Override
	            public void run() {
	            	SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
	                RefreshTime.setRefreshTime(getApplicationContext(), df.format(new Date()));
	            	
	        		getSenderListRefresh(SpfsUtil.loadPhone(), type);
	        		
	            }
	        });
		 
	}

	@Override
	public void onLoadMore() {
		  /*mHandler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                onLoad();
	            }
	        }, 2000);*/
		  mHandler.post(new Runnable() {
	            @Override
	            public void run() {
	            	SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
	                RefreshTime.setRefreshTime(getApplicationContext(), df.format(new Date()));
	            	
	        		getSenderListRefresh(SpfsUtil.loadPhone(), type);
	        		
	            }
	        });
		  
	}
	
	 private void onLoad() {
		    listView.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
	        listView.stopRefresh();

	        listView.stopLoadMore();

	    }

}

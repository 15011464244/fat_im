package com.ems.express.ui.message.send;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.R.drawable;
import com.ems.express.R.id;
import com.ems.express.R.layout;
import com.ems.express.adapter.message.MailMeassageAdapter;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.ui.BaiduMapActivity2;
import com.ems.express.ui.check.ResultActivity;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SendMessageActivity extends Activity {
	
	private SwipeMenuListView listmessage;
	private MailMeassageAdapter adapter;
	private List<SendNoticeBean> messageListData;
	
	private ImageView imgview;
	private Context mContext;
	
	private RelativeLayout mNotPackage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message);
		mContext = this;
		((TextView)this.findViewById(R.id.tv_title)).setText("消息");
		
//		Timer timer = new Timer();
//		timer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
				//测试数据
//				addTestData();
//				Intent intent = new Intent("NewMsgReceiver_Action"); 
//				sendBroadcast(intent);
//			}
//		}, 10, 1000*10);
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
	}
	
	void addTestData(){
		App.dbHelper.insertSendNotice(App.db,
				"2015-03-31 14:06", "1",12.3, 
				32.1, "15210887823", "11cc02c16e1f366d9759aca9fd23d62b", 
				"1009", "啦啦啦", "88888888", "1", 
				"1","3","ED000058385CN", "1",SpfsUtil.loadPhone(), "11");
		App.dbHelper.insertSendNotice2(App.db,
				"2015-03-31 14:06", "1", 
				"10", System.currentTimeMillis()+"",SpfsUtil.loadPhone());
	}
	
	void initView(){
		messageListData =  App.dbHelper.querySendNoticeByOrderStatus(App.db,"3","4");
		
		listmessage =(SwipeMenuListView)this.findViewById(R.id.list_message);
		imgview  =(ImageView)this.findViewById(R.id.img_view2);
		adapter = new MailMeassageAdapter(mContext, messageListData);
		
		mNotPackage = (RelativeLayout) this.findViewById(R.id.rl_notpackage);
		
		listmessage.setAdapter(adapter);
		addMenu();	
		int count = listmessage.getCount();
		if(count==0){
			mNotPackage.setVisibility(View.VISIBLE);
		}else{
			mNotPackage.setVisibility(View.GONE);
		}
	}
	//添加按钮
	void addMenu(){
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			
			@Override
			public void create(SwipeMenu menu) {
				// Create different menus depending on the view type
				switch (menu.getViewType()) {
				case 3:
					createMenu1(menu);
					break;
				case 10:
					createMenu2(menu);
					break;
				case 1:
					createMenu2(menu);
					break;
				}
			}
			//已取件
			public void createMenu1(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem currrier = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				currrier.setBackground(new ColorDrawable(Color.rgb(255,
						255, 255)));
				// set item width
				currrier.setWidth(dp2px(84));
				// set a icon
				currrier.setIcon(R.drawable.img_currrier);
				// add to menu
				menu.addMenuItem(currrier);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(255,
						255, 255)));
				// set item width
				deleteItem.setWidth(dp2px(84));
				// set a icon
				deleteItem.setIcon(R.drawable.img_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
			//已妥投
			public void createMenu2(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(255,
						255, 255)));
				// set item width
				deleteItem.setWidth(dp2px(84));
				// set a icon
				deleteItem.setIcon(R.drawable.img_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		listmessage.setMenuCreator(creator);
		
		listmessage.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				 SendNoticeBean item = messageListData.get(position);
				switch (index) {
				case 0:
					
					if(!"1".equals(item.getMessageStatus())){
						delete(position);
					}else if("3".equals(item.getMailStatus())){
						queryCurrier(item);
					}else if("10".equals(item.getMailStatus())){
						delete(position);
					}
					
					break;
				case 1:
					delete(position);
					break;
				}
				//这边返回值有点问题
				return false;
			}
		});
		
	
		
		
		listmessage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int i, long l) {
				App.dbHelper.updateSendNotice(App.db, messageListData.get(i).getSendId()+"");
//				messageListData.clear();
				messageListData =  App.dbHelper.querySendNoticeByOrderStatus(App.db,"3","4");
				adapter = new MailMeassageAdapter(mContext, messageListData);
				listmessage.setAdapter(adapter);
//				adapter.notifyDataSetChanged();
				
			if("10".equals(messageListData.get(i).getMailStatus())){
				ResultActivity.actionStart(mContext, messageListData.get(i).getMailNum(), "4");
			}
				
			}
		});
		
		// set SwipeListener
		listmessage.setOnSwipeListener(new OnSwipeListener() {
			
			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}
			
			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});
	}
	//查询邮递员位置
	public void queryCurrier(SendNoticeBean bean){
		Intent intent = new Intent(mContext,BaiduMapActivity2.class);
		intent.putExtra(BaiduMapActivity.KEY_TYPE,
				BaiduMapActivity.TYPE_CARRIER);
		intent.putExtra("LONGITUDE",bean.getLongitude());
		intent.putExtra("LATITUDE",bean.getLatitude());
		intent.putExtra("orgcode",bean.getOrgcode());
		intent.putExtra("username",bean.getUsername());
		intent.putExtra("phoneNum",bean.getMobile());
		intent.putExtra("sendNoticeBean",bean);
		intent.putExtra("messageIsSign", "2");
		intent.putExtra("activity", "signMessage");
		mContext.startActivity(intent);
	}
	
	//删除item
	void delete(int index){
		int sendId = messageListData.get(index).getSendId();
//		ToastUtil.show(mContext, "sendId:"+sendId+";index:"+index);
		
		App.dbHelper.deleteSendNotice(App.db, ""+sendId);
		messageListData.clear();
		messageListData =  App.dbHelper.querySendNoticeByOrderStatus(App.db,"3","4");
		adapter = new MailMeassageAdapter(mContext, messageListData);
		listmessage.setAdapter(adapter);
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
	public void back(View v) {
		finish();
	}
}

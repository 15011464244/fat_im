package com.ems.express.fragment.message;

import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.message.MailMeassageAdapter;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.ui.BaiduMapActivity2;
import com.ems.express.ui.check.ResultActivity;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class ArriveFragment extends Fragment{
	
	private boolean stayInThisFragment = true;
	private View view;
	private ListView listView;
	
	private SwipeMenuListView listmessage;
	private MailMeassageAdapter adapter;
	private List<SendNoticeBean> messageListData;
	
	private ImageView imgview;
	private Context mContext;
	
	private RelativeLayout mNotPackage;
	
	public BroadcastReceiver newMsgBReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			messageListData =  App.dbHelper.querySendNoticeByOrderStatus(App.db,"10",null);
			if(messageListData == null || messageListData.size() == 0){
				mNotPackage.setVisibility(View.VISIBLE);
			}else{
				mNotPackage.setVisibility(View.GONE);
			}
			adapter.notifyData(messageListData);
			adapter.notifyData(messageListData);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.message_arrive_fragment, null);
		mContext = this.getActivity();
		
		initView();
		
		// 注册一个自定义的广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("NewMsgReceiver_Action");
		mContext.registerReceiver(newMsgBReceiver, filter);
		
		return view;
	}

	void initView(){
		messageListData = App.dbHelper.querySendNoticeByOrderStatus(App.db,"10",null);
		
		listmessage =(SwipeMenuListView)view.findViewById(R.id.list_message);
		imgview  =(ImageView)view.findViewById(R.id.img_view2);
		adapter = new MailMeassageAdapter(mContext, messageListData);
		
		mNotPackage = (RelativeLayout) view.findViewById(R.id.rl_notpackage);
		
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
					createMenu2(menu);
			}
			//已妥投
			public void createMenu2(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						mContext.getApplicationContext());
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
				messageListData =  App.dbHelper.querySendNoticeByOrderStatus(App.db,"10",null);
				adapter.notifyData(messageListData);
			if("10".equals(messageListData.get(i).getMailStatus())){
				ResultActivity.actionStart(mContext, messageListData.get(i).getMailNum(), "4");
			}
			sendReceiver();
				
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
		messageListData =  App.dbHelper.querySendNoticeByOrderStatus(App.db,"10",null);
		adapter = new MailMeassageAdapter(mContext, messageListData);
		listmessage.setAdapter(adapter);
		sendReceiver();
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
	//发送有未读消息广播
	public void sendReceiver(){
		//新建一个Intent.ACTION_VIEW类型的intent。  
	    Intent intent = new Intent("NewMsgReceiver_Action");  
	    //发送广播，注册了该类型的广播接收者就会接受到。  
	    mContext.sendBroadcast(intent); 
	}
	
}

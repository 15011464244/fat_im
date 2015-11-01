package com.ems.express.ui.chat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.ChatListItemBean;
import com.ems.express.bean.PeopleInfo;
import com.ems.express.core.msg.IObserverBase;
import com.ems.express.core.msg.MessageManager;
import com.ems.express.net.Carrier;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class ChatListActivity extends Activity implements OnClickListener {
	private Context mContext = ChatListActivity.this;
	private SwipeMenuListView mLvFriend;
	private List<ChatListItemBean> mData = new ArrayList<ChatListItemBean>();
	private ChatListAdapter mAdapter;
	private ImageView mBtnBack;
	private TextView mBtnNext;
	private PopupWindow popWin = null;//
	private View popView = null;//
	private RelativeLayout mLoutTitle;
	private Boolean mFlag = false;
	private RelativeLayout mNotCourier;
//	private TextView mNotCourier2;

	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ChatListActivity.class);
		context.startActivity(intent);
	}

	IObserverBase ib = new IObserverBase() {

		@Override
		public void sendMessage(String message) {
//			ToastUtil.show(ChatListActivity.this, "ChatListActivity接收被观察者发送的消息");
			setMessageView();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_list);
		MessageManager.getInstance().attach(ib);
		mBtnBack = (ImageView) findViewById(R.id.back_button);
		mBtnNext = (TextView) findViewById(R.id.tv_info);
		mLoutTitle = (RelativeLayout) findViewById(R.id.title);
		mLvFriend = (SwipeMenuListView) findViewById(R.id.friend_list);
		mNotCourier = (RelativeLayout) findViewById(R.id.rl_notpackage);
//		mNotCourier2 = (TextView) findViewById(R.id.not_courier2);
		mAdapter = new ChatListAdapter(this, mData);
		mLvFriend.setAdapter(mAdapter);
		mBtnBack.setOnClickListener(this);
		mBtnNext.setOnClickListener(this);
		
		mBtnNext.setVisibility(View.VISIBLE);
		mBtnNext.setText("周边");
		((TextView)findViewById(R.id.tv_title)).setText("实时通讯");
		addMenu();
//		Carrier point = new Carrier();
//		point.setLongitude(123.11);
//		point.setLatitude(123.123);
//		point.setPeople("张三");
//		point.setMobile("15027004360");
//		//607d653ab5a33669b3e8838b3cb76bf2
//		point.setClientId("2a1c7e52894b333c82833398375b9235");
//		point.setEmployeeNo("123");
//		point.setSID("null");
//		App.dbHelper.insertChatList(App.db, point);
//
//		loadChatListData();// 测试数据

		mLvFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MainActivity.startAction(ChatListActivity.this,
						mData.get(position));
//				String employeeNo = App.dbHelper.queryEmployeeNoIsClientId(App.db,mData.get(position).getClientId());
//				App.dbHelper.updateUnReadMessage(App.db, employeeNo);
			}
		});
	}
	@Override
	protected void onPause() {
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
	}
	
	//添加按钮
	void addMenu(){
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem phone = new SwipeMenuItem(
						getApplicationContext());
				phone.setBackground(new ColorDrawable(Color.rgb(255,
						152, 0)));
				phone.setWidth(dp2px(84));
				phone.setIcon(R.drawable.img_call_phone);
				menu.addMenuItem(phone);

				SwipeMenuItem sendMsg = new SwipeMenuItem(
						getApplicationContext());
				sendMsg.setBackground(new ColorDrawable(Color.rgb(255,
						152, 0)));
				sendMsg.setWidth(dp2px(84));
				sendMsg.setIcon(R.drawable.img_send_msg);
				menu.addMenuItem(sendMsg);
				
				SwipeMenuItem delete = new SwipeMenuItem(
						getApplicationContext());
				delete.setBackground(new ColorDrawable(Color.rgb(243,
						55, 41)));
				delete.setWidth(dp2px(84));
				delete.setIcon(R.drawable.img_delete2);
				menu.addMenuItem(delete);
				
			}
		};
		mLvFriend.setMenuCreator(creator);
		
		mLvFriend.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				 ChatListItemBean item = mData.get(position);
				switch (index) {
				case 0:
					//call phone
					callPhone(item);
					break;
				case 1:
					//send msg
					sendMsg(item);
					break;
				case 2:
					//delete
					delete(item);
					mData.remove(position);
					mAdapter.notifyDataSetChanged();
					break;
				}
				return true;
			}
		});
		
		// set SwipeListener
		mLvFriend.setOnSwipeListener(new OnSwipeListener() {
			@Override
			public void onSwipeStart(int position) {}
			
			@Override
			public void onSwipeEnd(int position) {}
		});
	}
	
	//call phone
	void callPhone( ChatListItemBean item){
		String phone = item.getMobile();
		Intent intent=new  Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
		startActivity(intent);
	}
	//send msg
	void sendMsg( ChatListItemBean item){
		String phone = item.getMobile();
		Intent mIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phone));
		startActivity( mIntent );
	}
	//delete
	void delete( ChatListItemBean item){
		App.dbHelper.deleteCurrierByClientId(App.db, item.getClientId());
	}
	
	
	public void loadChatListData() {
		mData.clear();
		List<Carrier> list = App.dbHelper.queryAllChatList(App.db);
		if (!(list.size() > 0)) {
			mNotCourier.setVisibility(View.VISIBLE);
//			mNotCourier2.setVisibility(View.VISIBLE);
			mLvFriend.setVisibility(View.GONE);
		} else {
			mNotCourier.setVisibility(View.GONE);
//			mNotCourier2.setVisibility(View.GONE);
			mLvFriend.setVisibility(View.VISIBLE);
			for (int i = 0; i < list.size(); i++) {
				ChatListItemBean bean = new ChatListItemBean();
				bean.setImage(list.get(i).getSID());
				bean.setMobile(list.get(i).getMobile());
				bean.setName(list.get(i).getPeople());
				bean.setClientId(list.get(i).getClientId());
				bean.setUnRedCount(list.get(i).getUnRedCount());
				mData.add(bean);
			}
			mAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.tv_info:
			if(SpfsUtil.getIsLoadingCity() == true){
				DialogUtils.successMessage(mContext,"初始化数据加载中\n请20秒后重新再试！");
				return;
			}
			 Intent intent = new Intent();
			 intent.putExtra(BaiduMapActivity.KEY_TYPE,
			 BaiduMapActivity.TYPE_CARRIER);
			 intent.setClass(this, BaiduMapActivity.class);
			 startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	public void setMessageView(){
		mData.clear();
		List<Carrier> list = App.dbHelper.queryAllChatList(App.db);
		for (int i = 0; i < list.size(); i++) {
			ChatListItemBean bean = new ChatListItemBean();
			bean.setImage(list.get(i).getSID());
			bean.setMobile(list.get(i).getMobile());
			bean.setName(list.get(i).getPeople());
			bean.setClientId(list.get(i).getClientId());
			bean.setUnRedCount(list.get(i).getUnRedCount());
			mData.add(bean);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		//友盟统计
		MobclickAgent.onResume(this);
		loadChatListData();
		setMessageView();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		MessageManager.getInstance().attach(ib);
	}
	@Override
	protected void onStop() {
		super.onStop();
//		MessageManager.getInstance().detach(ib);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		MessageManager.getInstance().detach(ib);
	}
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}

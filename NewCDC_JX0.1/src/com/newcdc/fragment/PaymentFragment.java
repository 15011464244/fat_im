package com.newcdc.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.newcdc.R;
import com.newcdc.adapter.message.PaymentAdapter;
import com.newcdc.application.MainActivity;
import com.newcdc.bean.ReceivePaymentBean;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.PaymentDao;

public class PaymentFragment extends Fragment {
	private boolean stayInThisFragment = true;
	private View view;
	private ImageView im_back;
	
	private SwipeMenuListView listmessage;
	private PaymentAdapter adapter;
	private List<ReceivePaymentBean> messageListData;
	
	private ImageView imgview;
	private Context mContext;
	
	private RelativeLayout mNotPackage;
	
	public BroadcastReceiver newMsgBReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			messageListData =  App.dbHelper.queryPaymentByOrderStatus(App.db,"6",null);
			PaymentDao paymentDao =DeliverDaoFactory.getInstance().getPaymentDao(getActivity());
			messageListData = paymentDao.queryPaymentByOrderStatus(null, "7");
			if(messageListData == null || messageListData.size() == 0){
				mNotPackage.setVisibility(View.VISIBLE);
			}else{
				mNotPackage.setVisibility(View.GONE);
			}
			adapter.notifyData(messageListData);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.message_payment_fragment, null);
		mContext = this.getActivity();
		
		initView();
		
		// 注册一个自定义的广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("NewMsgReceiver_Action");
		mContext.registerReceiver(newMsgBReceiver, filter);
		
		return view;
	}
	
	void initView(){
		TextView textView = (TextView) view.findViewById(R.id.tv_title);
		textView.setText("支付消息");
		im_back = (ImageView) view.findViewById(R.id.im_back);
		im_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchFragment(6);
				
			}
		});
//		messageListData = App.dbHelper.queryPaymentByOrderStatus(App.db,"6",null);
		PaymentDao paymentDao =DeliverDaoFactory.getInstance().getPaymentDao(getActivity());
		messageListData = paymentDao.queryPaymentByOrderStatus(null, "7");
		listmessage =(SwipeMenuListView)view.findViewById(R.id.list_message);
		imgview  =(ImageView)view.findViewById(R.id.img_view2);
		adapter = new PaymentAdapter(mContext, messageListData);
		
		mNotPackage = (RelativeLayout) view.findViewById(R.id.rl_notpackage);
		
		listmessage.setAdapter(adapter);
		addMenu();	
		int count = listmessage.getCount();
		if(count==0){
			mNotPackage.setVisibility(View.VISIBLE);
		}else{
			mNotPackage.setVisibility(View.GONE);
		}
	}//添加按钮
	void addMenu(){
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			
			@Override
			public void create(SwipeMenu menu) {
					createMenu2(menu);
			}
			//积分消息
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
				 ReceivePaymentBean item = messageListData.get(position);
				switch (index) {
				case 0:
					
					if(!"1".equals(item.getMessageStatus())){
						delete(position);
					}else if("3".equals(item.getMailStatus())){
//						queryCurrier(item);
					}else if("10".equals(item.getMailStatus())){
						delete(position);
					}else if("6".equals(item.getMailStatus())){
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
//				App.dbHelper.updateReceiveNotice(App.db, messageListData.get(i).getReceiveId()+"");
//				messageListData =  App.dbHelper.queryPaymentByOrderStatus(App.db,"6",null);
				PaymentDao paymentDao =DeliverDaoFactory.getInstance().getPaymentDao(getActivity());
				paymentDao.updateReceiveNotice(messageListData.get(i).getReceiveId()+"");
				adapter.notifyData(messageListData);
//				adapter.notifyDataSetChanged();
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
	
		
		//删除item
		void delete(int index){
			int receiveId = messageListData.get(index).getReceiveId();
//			ToastUtil.show(mContext, "sendId:"+sendId+";index:"+index);
//			App.dbHelper.deleteSendNotice(App.db, ""+receiveId);
			PaymentDao paymentDao =DeliverDaoFactory.getInstance().getPaymentDao(getActivity());
			paymentDao.deleteReceiveNotice(receiveId+"");
			messageListData.clear();
			messageListData = paymentDao.queryPaymentByOrderStatus(null, "7");
			adapter = new PaymentAdapter(mContext, messageListData);
			listmessage.setAdapter(adapter);
			adapter.notifyData(messageListData);
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
		
		private void switchFragment(int fragmentFlag) {
			MainActivity activity = (MainActivity) getActivity();
			activity.switchContentFragment(fragmentFlag);
			// activity.toggle();
		}
}

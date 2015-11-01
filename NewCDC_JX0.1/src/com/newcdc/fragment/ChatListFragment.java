package com.newcdc.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.application.MainActivity;
import com.newcdc.chat.ui.ChatListAdapter;
import com.newcdc.chat.ui.MainChatActivity;
import com.newcdc.db.ChatMessageDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.ChatMessageTabBean;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;

public class ChatListFragment extends Fragment implements OnClickListener {
	private View mView;
	private ListView mList;
	private List<ChatMessageTabBean> mData = null;
	private ChatListAdapter mAdapter;
	private ImageView mBtnBack;
	private ChatMessageDao mChatMessageDao = null;
	private SharePreferenceUtilDaoFactory shareDao;
	protected static int count = 0;

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals("msg.success")) {// 接收消息的接收器
				loadChatListData();
			} else if (intent.getAction().equals("msg.username")) {// 接收更新完成客户名字的接收器
				LogUtils.e("接收到广播");
				loadChatListData();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.function_chat_list, container, false);
		mChatMessageDao = DeliverDaoFactory.getInstance().getChatMessageDao(
				getActivity());
		shareDao = SharePreferenceUtilDaoFactory.getInstance(getActivity());
		initView();
		loadChatListData();
		addListener();
		return mView;
	}

	private void initView() {
		mBtnBack = (ImageView) mView
				.findViewById(R.id.btn_back_function_chat_list);
		mList = (ListView) mView.findViewById(R.id.friend_list);
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter("msg.success");
		getActivity().registerReceiver(broadcastReceiver, intentFilter);
		IntentFilter intentFilter2 = new IntentFilter("msg.username");
		getActivity().registerReceiver(broadcastReceiver, intentFilter2);
		loadChatListData();
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	private void addListener() {
		mBtnBack.setOnClickListener(this);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mChatMessageDao.SetMessageIsRead(mData.get(position)
						.getSource());
				MainChatActivity.startAction(getActivity(), mData.get(position)
						.getSource(), mData.get(position).getSid() + "");
			}
		});
	}

	public void loadChatListData() {
		mData = mChatMessageDao.queryFriendList(shareDao.getCLIENTID());// 从数据库中查询
		// 查询两人聊天集合
		int len = mData.size();
		List<String> textString = new ArrayList<String>();// 回话最后一条消息的集合
		List<String> megTypeString = new ArrayList<String>();// 回话最后一条消息的类型
		for (int i = 0; i < len; i++) {
			List<ChatMessageTabBean> messages = mChatMessageDao
					.queryAllMessage(mData.get(i).getSource());
			textString.add(messages.get(messages.size() - 1).getUrl());
			megTypeString.add(messages.get(messages.size() - 1).getMessagetype());
		}
		mAdapter = new ChatListAdapter(getActivity(), mData, textString,megTypeString);
		mList.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_function_chat_list:
			switchFragment(6);
			break;
		default:
			break;
		}
	}

	private void switchFragment(int fragmentFlag) {
		MainActivity activity = (MainActivity) getActivity();
		activity.switchContentFragment(fragmentFlag);
	}

}

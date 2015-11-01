package com.ems.express.adapter.chat;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.ChatMessageBean;
import com.ems.express.bean.ChatMessageBean.ContentType;
import com.ems.express.frame.widget.emotion.SmileyParser;
import com.ems.express.net.UrlUtils;
import com.ems.express.ui.chat.MainActivity;
import com.ems.express.ui.view.CircleImageView;
import com.ems.express.util.SpfsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChatBoxMessageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<ChatMessageBean> mList;
	private SmileyParser mSmileyParser;
	private Boolean isComMsg;
	private ImageLoader mImageLoader = ImageLoader.getInstance();

	public ChatBoxMessageAdapter(Context context, List<ChatMessageBean> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mList = list;
		this.mSmileyParser = new SmileyParser(context);// 初始化自定义控件
	}

	public void addItem(ChatMessageBean list) {
		this.mList.add(list);
		this.notifyDataSetChanged();
	}

	public void setItem(List<ChatMessageBean> list) {
		this.mList = list;
		this.notifyDataSetChanged();
	}

	public List<ChatMessageBean> getmList() {
		return mList;
	}

	// ListView视图的内容由IMsgViewType决定
	public static interface IMsgViewType {
		// 对方发来的信息
		int IMVT_COM_MSG = 0;
		// 自己发出的信息
		int IMVT_TO_MSG = 1;
	}

	// 获取项的类型
	@Override
	public int getItemViewType(int position) {
		ChatMessageBean bean = mList.get(position);

		if (bean.isSendIsReceive()) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	// 获取项的类型数
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ChatMessageBean getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ChatMessageBean bean = mList.get(position);
		System.out.println("position:" + position + "isComMsg:" + isComMsg);
		System.out.println("Source:" + bean.getSource() + "Target:"
				+ bean.getTarget());
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			if (bean.isSendIsReceive()) {
				convertView = mInflater.inflate(R.layout.chat_box_item_right,
						null);
				System.out.println("发送");
				holder.welcomeLoading = (ImageView) convertView
						.findViewById(R.id.welcomeLoading);
				holder.reSendButton = (ImageView) convertView
						.findViewById(R.id.re_send_button);
				holder.reSendButton.setTag(position);
			} else {
				convertView = mInflater.inflate(R.layout.chat_box_item_left,
						null);
				System.out.println("接收");
			}

			holder.chatIcon = (ImageView) convertView
					.findViewById(R.id.chat_icon);// 头像
			holder.chatImage = (ImageView) convertView
					.findViewById(R.id.chat_image);// 图片
			holder.chatText = (TextView) convertView
					.findViewById(R.id.chat_text);// 文字
			holder.chatVoice = (LinearLayout) convertView
					.findViewById(R.id.chat_voice);// 语音
			holder.chatVoiceSize = (TextView) convertView
					.findViewById(R.id._chat_voice_size);// 语音大小
			holder.chatVoiceImage = (ImageView) convertView
					.findViewById(R.id._chat_voice_image);// 语音图片
			holder.chatVoiceTime = (TextView) convertView
					.findViewById(R.id._chat_voice_time);// 语音时间
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			if (bean.isSendIsReceive()) {
				holder.reSendButton.setTag(position);
			}
		}
		if (bean.isSendIsReceive()) {
			String headImageUrl = SpfsUtil.loadHeadImageUrl();
			if (!headImageUrl.equals("")) {
				Bitmap photo = BitmapFactory.decodeFile(headImageUrl);
				holder.chatIcon.setImageBitmap(photo);
			} else {
				Bitmap aa = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.default_head);
				holder.chatIcon.setImageBitmap(aa);
			}
		} else {
			if (!bean.getSourceIcon().equals("null")
					&& !bean.getSourceIcon().equals("")) {
				String url = UrlUtils.URL_CARRIER_IMG + "?sid="
						+ bean.getSourceIcon();
				mImageLoader.displayImage(url, holder.chatIcon);
			} else {
				Bitmap aa = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.defualt_header);
				holder.chatIcon.setImageBitmap(aa);
			}
		}
		if (ContentType.TEXT.equals(bean.getContentType())) {
			holder.chatText.setVisibility(View.VISIBLE);
			holder.chatImage.setVisibility(View.GONE);
			holder.chatVoice.setVisibility(View.GONE);
			holder.chatVoiceTime.setVisibility(View.GONE);
			holder.chatText.setText(mSmileyParser.replace(bean.getContent()));

		} else if (ContentType.FILE.equals(bean.getContentType())) {
			// File voice = new File(bean.getContent());
			holder.chatText.setVisibility(View.GONE);
			holder.chatImage.setVisibility(View.GONE);
			holder.chatVoice.setVisibility(View.VISIBLE);
			holder.chatVoiceTime.setVisibility(View.VISIBLE);
			holder.chatVoiceTime.setText(bean.getSpeechMessageTime() + "''");
			String str = "";
			for (int i = 0; i < bean.getSpeechMessageTime();) {
				str = str + " ";
				if (i > 20) {
					i = i + 2;
				} else {
					i++;
				}
			}
			holder.chatVoiceSize.setText(str);
			if (!bean.isSendIsReceive())
				holder.chatVoiceImage
						.setBackgroundResource(R.drawable.voice_come3);
			else
				holder.chatVoiceImage
						.setBackgroundResource(R.drawable.voice_send3);

		} else if (ContentType.IMG.equals(bean.getContentType())) {
			holder.chatText.setVisibility(View.GONE);
			holder.chatImage.setVisibility(View.VISIBLE);
			holder.chatVoice.setVisibility(View.GONE);
			holder.chatVoiceTime.setVisibility(View.GONE);
			holder.chatImage.setImageResource(R.drawable.card_photofail);// bean.getContent_image());

			if (bean.getPicpath() == null || "".equals(bean.getPicpath())) {
				mImageLoader.displayImage(bean.getServerPath(),
						holder.chatImage);
			} else {
				mImageLoader.displayImage(
						"file://" + bean.getPicpath(), holder.chatImage);
			}

		}

		holder.chatVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ContentType.FILE.equals(bean.getContentType())) {
					try {
						App.getmChatService().getmMediaPlayer().reset();
						if (bean.getPicpath() == null
								|| bean.getPicpath().equals("")) {
							App.getmChatService()
									.getmMediaPlayer()
									.setDataSource(mContext,
											Uri.parse(bean.getServerPath()));
						} else {
							App.getmChatService().getmMediaPlayer()
									.setDataSource(bean.getPicpath());
						}
						App.getmChatService().getmMediaPlayer().prepare();
						App.getmChatService().getmMediaPlayer().start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		if (bean.isSendIsReceive()) {
			AnimationDrawable ad = (AnimationDrawable) holder.welcomeLoading
					.getBackground();
			if ("0".equals(bean.getMessageSendStatus())) {
				holder.welcomeLoading.setVisibility(View.GONE);
				holder.reSendButton.setVisibility(View.GONE);
			} else if ("1".equals(bean.getMessageSendStatus())) {
				ad.stop();
				holder.welcomeLoading.setVisibility(View.GONE);
				holder.reSendButton.setVisibility(View.VISIBLE);
			} else {
				holder.welcomeLoading.setVisibility(View.VISIBLE);
				holder.reSendButton.setVisibility(View.GONE);
				ad.start();
			}
			holder.reSendButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int number = (Integer) v.getTag();
					// App.mToastUtil.show("当前行数是："+number+";当前消息ID："+mList.get(number).getMessageId());
					MainActivity.sendMessage(mList.get(number).getContent(), 1,
							mList.get(number).getMessageId(), mList.get(number)
									.getContentType(), mList.get(number)
									.getPicpath());
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
//		CircleImageView chatIcon;// 头像
		ImageView chatIcon;// 头像
		ImageView chatImage, welcomeLoading, reSendButton;// 图片信息
		TextView chatText;// 文字消息
		LinearLayout chatVoice;
		TextView chatVoiceSize;// 语音信息（语音消息框长度）
		ImageView chatVoiceImage;// 图片信息
		TextView chatVoiceTime;// 语音时间
	}
}

package com.newcdc.chat.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.asynctask.DownImageAsyncTask;
import com.newcdc.chat.frame.widget.emotion.SmileyParser;
import com.newcdc.chat.ui.ChatMessageBean.ContentType;
import com.newcdc.tools.Constant;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.ShowToast;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.ui.GetBitmap;

public class ChatBoxMessageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private MainChatActivity mContext;
	private List<ChatMessageBean> mList;
	private String sid;
	private SmileyParser mSmileyParser;
	private MediaPlayer mPlayer = null;// 用于语音播放
	private Boolean isComMsg;
	private UserInfoUtils userUtils;
	BitmapUtils bitmapUtils = null;
	private Dialog ivdialog;
	private Bitmap bitmap = null;
	private static SharePreferenceUtilDaoFactory shareDao;

	// private AnimationDrawable animaition;

	public ChatBoxMessageAdapter(MainChatActivity context,
			List<ChatMessageBean> list, String sid) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mList = list;
		this.mSmileyParser = new SmileyParser(context);// 初始化自定义控件
		this.sid = sid;
		userUtils = new UserInfoUtils(context);
		bitmapUtils = new BitmapUtils(context);
		shareDao = SharePreferenceUtilDaoFactory.getInstance(context);
	}

	public void addItem(ChatMessageBean list) {
		this.mList.add(list);
		this.notifyDataSetChanged();
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

		if ("".equals(bean.getTarget())) {
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ChatMessageBean bean = mList.get(position);
		isComMsg = false;
		if ("".equals(bean.getTarget()))
			isComMsg = true;
		LogUtils.e("position:" + position + "   isComMsg:" + isComMsg);
		ViewHolder holder = null;
		if (convertView == null) {
			if (!isComMsg) {
				convertView = mInflater.inflate(R.layout.chat_box_item_right,
						null);
				LogUtils.e("此条消息是发送的");
			} else {
				convertView = mInflater.inflate(R.layout.chat_box_item_left,
						null);
				LogUtils.e("此条消息是接收的");
			}
			holder = new ViewHolder();
			mPlayer = new MediaPlayer();
			holder.chatIcon = (com.newcdc.ui.RoundImageView) convertView
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
			holder.chatStatus = (TextView) convertView
					.findViewById(R.id.chat_status);// 发送状态
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LogUtils.e("消息类型：  " + bean.getContentType() + "  消息内容：  "
				+ bean.getContent() + "   消息状态： " + bean.getStatus());
		if (!isComMsg) {
			holder.chatIcon.setBackground(mContext.getResources().getDrawable(
					R.drawable.ic_launcher));
			File f = new File(Constant.SD + "myhead"
					+ userUtils.getUserDelvorgCode() + userUtils.getUserName()
					+ Constant.HEADIMG);
			if (f.exists()) {
				Bitmap bitmap = BitmapFactory.decodeFile(Constant.SD + "myhead"
						+ userUtils.getUserDelvorgCode()
						+ userUtils.getUserName() + Constant.HEADIMG);
				if (null != bitmap) {
					holder.chatIcon.setImageBitmap(bitmap);
				}
			}
			// 设置发送状态成功?0失败?1未发送之前?2
			if ("0".equals(bean.getStatus())) {
				holder.chatStatus.setVisibility(View.GONE);
				holder.chatStatus.setText("成功");
			} else if ("1".equals(bean.getStatus())) {
				holder.chatStatus.setVisibility(View.VISIBLE);
				holder.chatStatus.setText("重发");
			} else if ("2".equals(bean.getStatus())) {
				holder.chatStatus.setVisibility(View.GONE);
			}
		} else {
			// 左边的头像
			holder.chatIcon.setBackground(mContext.getResources().getDrawable(
					R.drawable.headimg));
			File f = new File(Constant.SD + sid);
			if (f.exists()) {
				Bitmap bitmap = BitmapFactory.decodeFile(Constant.SD + sid);
				if (null != bitmap) {
					holder.chatIcon.setImageBitmap(bitmap);
				}
			} else {
				DownImageAsyncTask async = new DownImageAsyncTask();
				async.setmImageView(holder.chatIcon);
				async.setContext(mContext);
				async.setListener(new DownImageAsyncTask.onPostExecuteListener() {
					@Override
					public void onPostExecute(Bitmap result) {
					}
				});
				async.execute(sid);
			}
		}
		if (ContentType.TEXT.equals(bean.getContentType())) {
			holder.chatText.setVisibility(View.VISIBLE);
			holder.chatImage.setVisibility(View.GONE);
			holder.chatVoice.setVisibility(View.GONE);
			holder.chatText.setText(mSmileyParser.replace(bean.getContent()));
		} else if (ContentType.FILE.equals(bean.getContentType())) {
			File voice = bean.getContent_voice();
			holder.chatText.setVisibility(View.GONE);
			holder.chatImage.setVisibility(View.GONE);
			holder.chatVoice.setVisibility(View.VISIBLE);
			holder.chatVoiceSize.setText(bean.getMessageTime() + " '");
			if (isComMsg) {
				holder.chatVoiceImage
						.setBackgroundResource(R.drawable.voice_come3);
				// 设置动画背景
				// holder.chatVoiceImage
				// .setBackgroundResource(R.anim.selctor_voice_come);
			} else {
				holder.chatVoiceImage
						.setBackgroundResource(R.drawable.voice_send3);
				// holder.chatVoiceImage
				// .setBackgroundResource(R.anim.selctor_voice_send);
			}
			// animaition = (AnimationDrawable) holder.chatVoiceImage
			// .getBackground();
		} else if (ContentType.IMG.equals(bean.getContentType())) {
			holder.chatText.setVisibility(View.GONE);
			holder.chatImage.setVisibility(View.VISIBLE);
			holder.chatVoice.setVisibility(View.GONE);
			holder.chatImage.setImageResource(R.drawable.card_photofail);// bean.getContent_image());
			final String uRL = bean.getContent();
			if (isComMsg) {
				final String[] ssStrings = uRL.split("/");
				File f = new File(Constant.SD + ssStrings[ssStrings.length - 1]);
				if (f.exists()) {
					Bitmap bitmap = BitmapFactory.decodeFile(Constant.SD
							+ ssStrings[ssStrings.length - 1]);
					if (null != bitmap) {
						LogUtils.e("显示bitmap");
						holder.chatImage.setImageBitmap(bitmap);
					} else {
						LogUtils.e("显示URL");
						holder.chatImage.setImageURI(Uri.parse(Constant.SD
								+ ssStrings[ssStrings.length - 1]));
					}
				} else {
					new AsyncTask<Object, Void, String>() {
						@Override
						protected String doInBackground(Object... params) {
							NetHelper.getImgFromNet(mContext, uRL);
							return null;
						}

						@Override
						protected void onPostExecute(String result) {
							super.onPostExecute(result);
						}

					}.execute();
				}
			} else {
				// File f = new File(bean.getContent());
				// if (f.exists()) {
				// Bitmap bitmap = BitmapFactory.decodeFile(bean.getContent());
				// if (null != bitmap) {
				// holder.chatImage.setImageBitmap(bitmap);
				// }
				// }
				bitmapUtils.display(holder.chatImage, bean.getContent());
				// holder.chatImage.setImageURI(Uri.parse(bean.getContent()));
			}
		}
		holder.chatVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ContentType.FILE.equals(bean.getContentType())) {
					// if (animaition.isRunning()) {// 是否正在运行？
					// animaition.stop();// 停止
					// }
					// animaition.start();// 启动
					try {
						mPlayer.reset();
						if (bean.getContent() == null
								|| bean.getContent().equals("")) {
							mPlayer.setDataSource(mContext,
									Uri.parse(bean.getServerPath()));
						} else {
							mPlayer.setDataSource(bean.getContent());
						}
						mPlayer.prepare();
						LogUtils.e("时长：  " + mPlayer.getDuration() / 1000);// 获取语音的时长
						mPlayer.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		holder.chatImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LogUtils.e("点击的消息内容是：   " + bean.getContent());
				if (!shareDao.getCLIENTID().equals(bean.getSource())) {
					final String[] ssStrings = bean.getContent().split("/");
					File f = new File(Constant.SD
							+ ssStrings[ssStrings.length - 1]);
					if (f.exists()) {
						reViewImage(Constant.SD
								+ ssStrings[ssStrings.length - 1]);
					} else {
						ShowToast.showToast(mContext, "展示出错", 1000);
					}
				} else {
					File f = new File(bean.getContent());
					if (f.exists()) {
						reViewImage(bean.getContent());
					} else {
						ShowToast.showToast(mContext, "展示出错", 1000);
					}
				}

			}
		});
		// 重发消息
		if (!"null".equals(bean.getStatus())) {
			holder.chatStatus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					LogUtils.e("点击的消息内容是：   " + bean.getContent() + "   "
							+ bean.getStatus() + "  " + bean.getMessageId());
					if (shareDao.getCLIENTID().equals(bean.getSource())) {
						if ("1".equals(bean.getStatus())) {// 说明发送的消息是失败的可以再重新发送
							mContext.sendMessage(1, bean.getMessageId(),
									bean.getContentType(), bean.getContent());
						}
					}
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		com.newcdc.ui.RoundImageView chatIcon;// 头像

		ImageView chatImage;// 图片信息
		TextView chatText;// 文字消息
		LinearLayout chatVoice;
		TextView chatVoiceSize;// 语音信息（语音消息框长度）
		ImageView chatVoiceImage;// 图片信息
		TextView chatVoiceTime;// 语音时间
		TextView chatStatus;// 发送状态
	}

	private void reViewImage(String path) {
		if (ivdialog == null) {
			ivdialog = new Dialog(mContext, R.style.dialogss);
		}
		View v = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_image_review, null);
		ivdialog.setContentView(v, new LayoutParams(BaseActivity.width,
				BaseActivity.height));
		ImageView review_image = (ImageView) v.findViewById(R.id.review_image);
		bitmap = BitmapFactory.decodeFile(path);
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

	// mListview.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// System.out.println("onItemClick"+position);
	// ChatMessageBean cm = mAdapter.getItem(position);
	// if("voice".equals(cm.getContentType()))
	// {
	// try {
	// System.out.println("mPlayer.start();");
	// mPlayer.reset();
	// mPlayer.setDataSource(cm.getContent());
	// mPlayer.prepare();
	// mPlayer.start();
	// } catch (IOException e) {
	// Log.e(LOG_TAG, "播放失败");
	// }
	// }
	// }
	// });

}

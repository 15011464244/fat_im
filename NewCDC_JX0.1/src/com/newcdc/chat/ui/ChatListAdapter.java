package com.newcdc.chat.ui;

import java.io.File;
import java.util.List;
import org.apache.mina.proxy.utils.StringUtilities;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.android.bbalbs.common.a.b;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.asynctask.DownImageAsyncTask;
import com.newcdc.chat.frame.widget.emotion.SmileyParser;
import com.newcdc.db.ChatMessageDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.ChatMessageTabBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Utils;

public class ChatListAdapter extends BaseAdapter {
	private List<ChatMessageTabBean> mData;
	private List<String> strings;
	private List<String> megTypeString;
	private LayoutInflater mInflater;
	private Context mContext;
	private SmileyParser mSmileyParser;
	private ChatMessageDao mChatMessageDao = null;

	public ChatListAdapter(Context context, List<ChatMessageTabBean> mData,
			List<String> strings, List<String> megTypeString) {
		super();
		this.mData = mData;
		this.strings = strings;
		this.megTypeString = megTypeString;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		mChatMessageDao = DeliverDaoFactory.getInstance().getChatMessageDao(
				context);
		this.mSmileyParser = new SmileyParser(context);// 初始化自定义控件
	}

	public void updateData(List<ChatMessageTabBean> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	public void append(List<ChatMessageTabBean> mData) {
		this.mData.addAll(mData);
		notifyDataSetChanged();
	}

	public void append(ChatMessageTabBean mData) {
		this.mData.add(mData);
		notifyDataSetChanged();
	}

	public void clear() {
		this.mData.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public ChatMessageTabBean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.activity_friend_list_item,
					null);
			holder.image = (com.newcdc.ui.RoundImageView) convertView
					.findViewById(R.id.image);
			holder.iv_red = (ImageView) convertView.findViewById(R.id.iv_red);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.message = (TextView) convertView.findViewById(R.id.msg);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChatMessageTabBean bean = mData.get(position);
		// holder.image.setBitmapResourceId(Integer.parseInt(bean.getImage()));
		holder.image.setImageResource(R.drawable.headimg);
		if ("".equals(bean.getSourcename())) {
			holder.name.setText("正在查询客户信息");
		} else {
			holder.name.setText(bean.getSourcename());
		}
		// 客户的名字不为空 就可以去获取客户的头像了 起个线程去获取客户的头像 存储命名方式是以sid命名不会重复\
		File f = new File(Constant.SD + bean.getSid());// 296329037377214
		if (f.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(Constant.SD
					+ bean.getSid());
			if (null != bitmap) {
				holder.image.setImageBitmap(bitmap);
			}
		} else {
			DownImageAsyncTask async = new DownImageAsyncTask();
			async.setmImageView(holder.image);
			async.setContext(mContext);
			async.setListener(new DownImageAsyncTask.onPostExecuteListener() {
				@Override
				public void onPostExecute(Bitmap result) {
				}
			});
			async.execute(bean.getSid());
		}
		// 最后一条心事是文本的显示文本的 语音和图片的都不显示内容
		if ("0".equals(megTypeString.get(position))) {
			holder.message
					.setText(mSmileyParser.replace(strings.get(position)));
		} else if ("1".equals(megTypeString.get(position))) {
			holder.message.setText("[图片]");
		} else if ("2".equals(megTypeString.get(position))) {
			holder.message.setText("[语音]");
		}
		if (mChatMessageDao.queryIsReadMessage(bean.getSource())) {
			holder.iv_red.setVisibility(View.VISIBLE);
		} else {
			holder.iv_red.setVisibility(View.GONE);
		}
		if(!"".equals(bean.getCreatetime())){
			holder.time.setText(Utils.getTime(bean.getCreatetime()));
		}
		return convertView;
	}

	public static class ViewHolder {
		com.newcdc.ui.RoundImageView image;
		ImageView iv_red;
		TextView name;
		TextView message;
		TextView time;
	}
}

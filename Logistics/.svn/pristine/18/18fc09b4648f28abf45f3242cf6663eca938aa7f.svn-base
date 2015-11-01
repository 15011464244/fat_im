package com.ems.express.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.ems.express.R;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class AddCommonActivity extends Activity implements OnClickListener {

//	@InjectView(R.id.common_word)
	static ListView mLvCommonWord;
	@InjectView(R.id.tv_info)
	TextView mTvInfo;
	@InjectView(R.id.et_common_word) EditText mEtCommonWord;
	@InjectView(R.id.btn_save) TextView mBtnSave;
	
	

	public static final String DEFAULT_COMMON_WORDS = "default_words";
	private ArrayList<String> mCommonWords = null;
	private CommonWordAdapter mAdapter;

	public static void actionStart(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, AddCommonActivity.class);
		context.startActivity(intent);
	}

	public void back(View v) {
		finish();
	}

//	/** 
//     * 设置布局显示的属性 
//     */  
//    private LayoutParams mLayoutParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);  
//    /** 
//     * 设置布局显示目标最大化属性 
//     */  
//    private LayoutParams FFlayoutParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_common);
		mLvCommonWord = (ListView) this.findViewById(R.id.common_word);
		ButterKnife.inject(this);
		((TextView) findViewById(R.id.tv_title)).setText("添加常用语");
		// mLvCommonWord.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mTvInfo.setVisibility(View.VISIBLE);
		mTvInfo.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);
		mTvInfo.setText("删除");
//		mTvInfo.setBackgroundResource(R.drawable.img_delete_common);
		mCommonWords = SpfsUtil.loadCommonWords();
		mAdapter = new CommonWordAdapter(this, mCommonWords);
//		if (mCommonWords != null && !mCommonWords.isEmpty()) {
			mLvCommonWord.setAdapter(mAdapter);
//		}
		//设置listview的高度
		this.setListViewHeightBasedOnChildren(mLvCommonWord);
		
		mLvCommonWord.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//隐藏软键盘
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(AddCommonActivity.this.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘 
				
			}
		});
		
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_info:
			mTvInfo.setText(mAdapter.isShowDelete() ? "删除" : "取消");
			mAdapter.setShowDelete(!mAdapter.isShowDelete());
			mAdapter.notifyDataSetChanged();
			break;

		case R.id.btn_save:
			String commonWord = mEtCommonWord.getText().toString();
			if(commonWord == null || commonWord.isEmpty()){
				ToastUtil.show(this, "请输入常用语");
				return;
			}
			if(null == mCommonWords){
				mCommonWords = new ArrayList<String>();
			}
			mCommonWords.add(commonWord);
			mAdapter.notifyDataSetChanged();
			mEtCommonWord.setText("");
			this.setListViewHeightBasedOnChildren(mLvCommonWord);
			onCommonWordsChanged();
			break;
		default:
			break;
		}
	}

	public void onCommonWordsChanged() {
		SpfsUtil.saveCommonWords(mCommonWords);
		if (mCommonWords.isEmpty()) {
			return;
		}
//		SpfsUtil.saveCommonWords(mCommonWords);
	}

	public static class CommonWordAdapter extends BaseAdapter implements OnClickListener {
		private boolean showDelete = false;
		private ArrayList<String> mData;
		private Context mContext;
		private LayoutInflater mInflater;

		public CommonWordAdapter(Context context, ArrayList<String> mData) {
			this.mData = mData;
			this.mContext = context;
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public String getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		ViewHolder viewHolder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_common_word, null);
				viewHolder = new ViewHolder();
				viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
				viewHolder.tvCommonWord = (TextView) convertView.findViewById(R.id.tv_common_word);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tvCommonWord.setText(getItem(position));
			viewHolder.ivDelete.setVisibility(showDelete ? View.VISIBLE : View.GONE);
			viewHolder.ivDelete.setTag(position);
			viewHolder.ivDelete.setOnClickListener(this);
			return convertView;
		}

		static class ViewHolder {
			public ImageView ivDelete;
			public TextView tvCommonWord;
		}

		public boolean isShowDelete() {
			return showDelete;
		}

		public void setShowDelete(boolean showDelete) {
			this.showDelete = showDelete;
		}

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			mData.remove(mData.get(position));
			notifyDataSetChanged();
			((AddCommonActivity) mContext).onCommonWordsChanged();
			
			((AddCommonActivity) mContext).setListViewHeightBasedOnChildren(mLvCommonWord);;
			
		}

	}
	

	//定义一个函数将dp转换为像素 
	public int Dp2Px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	}  


	//定义函数动态控制listView的高度 
	public void setListViewHeightBasedOnChildren(ListView listView) {
            //获取listview的适配器
            ListAdapter listAdapter = listView.getAdapter();
            //item的高度
            int itemHeight = 50;
            int totalHeight = 0;
            int size = 0;
            if (listAdapter != null) {
            	  size = (listAdapter.getCount() > 7)? 8:listAdapter.getCount();
            }
            for (int i = 0; i < size; i++) {
              totalHeight += Dp2Px(getApplicationContext(),itemHeight)+listView.getDividerHeight();
            }
            //分割线问题
            totalHeight -= Dp2Px(getApplicationContext(),1);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight;
            listView.setLayoutParams(params);
	}

}


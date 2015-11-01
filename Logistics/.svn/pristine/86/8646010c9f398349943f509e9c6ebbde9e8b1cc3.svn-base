package com.ems.express.frame.widget.emotion;

import com.ems.express.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class FaceLayout extends RelativeLayout {

	public static final int MAX_PAGE_COUNT = 24;
	public static final int MAX_PAGE = 4;
	private ViewPager mViewPager;
	private LayoutInflater mInflater;
	private ImageView mCursor;
	private Context mContext;
	private SmileyParser mSmileyParser;
	private OnDeleteListener mOnDeleteListener;
	private OnFaceClickListener mOnFaceClickListener;

	public interface OnDeleteListener {
		public void onDelete();
	};

	public interface OnFaceClickListener {
		public void onFaceClick(int face);
	};

	public void setOnFaceClickListener(OnFaceClickListener listener) {
		mOnFaceClickListener = listener;
	}

	public void setDeleteListener(OnDeleteListener listener) {
		mOnDeleteListener = listener;
	}

	public void setmSmileyParser(SmileyParser mSmileyParser) {
		this.mSmileyParser = mSmileyParser;
	}

	private int[] cursorBg = { R.drawable.cursor_1, R.drawable.cursor_2,
			R.drawable.cursor_3, R.drawable.cursor_4 };

	public FaceLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mInflater.inflate(R.layout.chat_emotion_layout, this);
		findView();
		setListener();
	}

	private void findView() {
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mCursor = (ImageView) findViewById(R.id.cursor);
		mCursor.setImageResource(cursorBg[0]);
	}

	private void setListener() {
		FaceViewPagerAdapter adapter = new FaceViewPagerAdapter(mContext, this);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 < MAX_PAGE && arg0 >= 0) {
					mCursor.setImageResource(cursorBg[arg0]);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public GridView createGridView(int page) {

		int[] imageIds = new int[MAX_PAGE_COUNT];

		final int[] mImageIds = mSmileyParser.getImageIds();

		for (int i = 0; i < (imageIds.length - 1); i++) {
			int faceIndex = getFaceIndex(page, i);
			if (faceIndex < mImageIds.length) {
				imageIds[i] = mImageIds[faceIndex];
			} else {
				imageIds[i] = 0;
			}
		}

		imageIds[MAX_PAGE_COUNT - 1] = SmileyParser.EMOTION_DEL_RESOURCE;
		GridView view = new GridView(mContext);
		view.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		view.setTag(page);
		ListAdapter adapter = new FaceItemAdapter(mContext, imageIds,
				R.layout.chat_emotion_team_single, view);
		view.setAdapter(adapter);
		view.setNumColumns(6);
		view.setHorizontalSpacing(1);
		view.setVerticalSpacing(1);
		view.setGravity(Gravity.CENTER);
		view.setSelector(new ColorDrawable(Color.TRANSPARENT));
		view.setOnItemClickListener(mItemClickListener);
		return view;
	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {
			int page = (Integer) paramAdapterView.getTag();
            int faceIndex = getFaceIndex(page, paramInt);
			if (paramInt < MAX_PAGE_COUNT - 1) {
				if (mOnFaceClickListener != null) {
					mOnFaceClickListener.onFaceClick(faceIndex);
				}
			} else if (paramInt == MAX_PAGE_COUNT - 1) {
				if (mOnDeleteListener != null) {
					mOnDeleteListener.onDelete();
				}
			}
		}

	};

	private int getFaceIndex(int page, int i) {
		int faceIndex = page * (MAX_PAGE_COUNT - 1) + i;
		return faceIndex;
	}

	static class FaceItemAdapter extends BaseAdapter {
		private int mTo = R.id.image;

		private int[] mData;

		private int mResource;
		private LayoutInflater mInflater;
		int height;
		int width;
		GridView view;

		/**
         * 
         */
		public FaceItemAdapter(Context context, int[] data, int resource,
				GridView view) {
			mResource = resource;
			mData = data;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.view = view;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			height = view.getHeight() / 4 - 1;
			width = view.getWidth() / 6;
			View v;
			if (convertView == null) {
				v = mInflater.inflate(mResource, parent, false);
			} else {
				v = convertView;
			}

			v.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, height));

			bindView(position, v);

			return v;
		}

		/**
		 * @param position
		 * @param v
		 */
		private void bindView(int position, View view) {

			final ImageView v = (ImageView) view.findViewById(mTo);

			setViewImage(v, mData[position]);

		}

		private void setViewImage(ImageView v, int value) {
			v.setImageResource(value);
		}

		/**
		 * @see android.widget.Adapter#getCount()
		 */
		public int getCount() {
			return mData.length;
		}

		/**
		 * @see android.widget.Adapter#getItem(int)
		 */
		public Object getItem(int position) {
			return mData[position];
		}

		/**
		 * @see android.widget.Adapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}
	}

}

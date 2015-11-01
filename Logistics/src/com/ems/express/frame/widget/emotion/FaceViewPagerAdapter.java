package com.ems.express.frame.widget.emotion;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

public class FaceViewPagerAdapter extends PagerAdapter {

	private Context mContext;
	private FaceLayout mLayout;
	private GridView [] views = new GridView[FaceLayout.MAX_PAGE];
	
	public FaceViewPagerAdapter(Context context,FaceLayout layout){
		mContext = context;
		mLayout = layout;
	}
	
	@Override
	public int getCount() {
		return views.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		if(views[arg1]==null){
			views[arg1] = mLayout.createGridView(arg1);
		}
		((ViewPager)arg0).addView(views[arg1]);
		return views[arg1];
	}
	
	@Override
    public void destroyItem(View view, int position, Object arg2) {
		((ViewPager)view).removeView(views[position]);
	}
	
}

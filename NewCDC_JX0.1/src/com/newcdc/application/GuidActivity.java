package com.newcdc.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.newcdc.R;

public class GuidActivity extends Activity implements OnClickListener {
	private ViewPager viewPager;
	private List<Integer> imageIds;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guid);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		button = (Button) findViewById(R.id.button);
		imageIds = new ArrayList<Integer>();
		imageIds.add(R.drawable.guid_1);
		imageIds.add(R.drawable.guid_2);
		new Thread() {
			@Override
			public void run() {
				imageIds.add(R.drawable.guid_3);
				imageIds.add(R.drawable.guid_4);
				imageIds.add(R.drawable.guid_5);
			};
		}.start();
	}

	@Override
	protected void onResume() {
		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// 给引导页面添加监听事件，如果为最后一页，显示立即体验按钮
				if (arg0 == 4) {
					button.setVisibility(View.VISIBLE);
				} else {
					button.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		button.setOnClickListener(this);
		super.onResume();
	}

	/**
	 * 动态创建viewpager内容，解决内存溢出问题
	 * 
	 * @author zhangfan 2015-2-2,下午2:12:46
	 * 
	 */
	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = new View(GuidActivity.this);// 关键地方，动态生成页面。
			view.setBackgroundResource(imageIds.get(position));
			view.setId(position);
			((ViewPager) container).addView(view, 0);
			return view;
		}

	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			// 点击启动登录界面
			boolean fromMain = getIntent().getBooleanExtra("fromMain", false);
			if (fromMain) {
				onActivityResult(100, 101, new Intent(GuidActivity.this,
						MainActivity.class));
				this.finish();
			} else {
				Intent intent = new Intent(GuidActivity.this,
						WelcomeActivity.class);
				startActivity(intent);
				this.finish();
			}
			break;

		default:
			break;
		}
	}
}

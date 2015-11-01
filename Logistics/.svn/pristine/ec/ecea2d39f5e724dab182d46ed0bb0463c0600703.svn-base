package com.ems.express.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ems.express.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideViewPager extends Activity implements OnPageChangeListener {

	View mGuideView1, mGuideView2, mGuideView3, mGuideView4;// /引导页
	List<View> mViewList;// 引导页List
	ViewPager mViewPager;
	Button startButton;// “马上体验”按钮
	LinearLayout dot_linearlayout;// LinearLayout小圆点L
	// int dot_cur_position = 0;// 小圆点的当前位置，初始为0
	// ImageButton mImgChoose;
	// EditText mEdtBirth;
	private Calendar calendar = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_guideviewpager);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		dot_linearlayout = (LinearLayout) findViewById(R.id.dot_linearlayout);

		LayoutInflater inflater = LayoutInflater.from(this);
		mGuideView1 = inflater.inflate(R.layout.guide_viewpager_page1, null);
		mGuideView2 = inflater.inflate(R.layout.guide_viewpager_page2, null);
		mGuideView3 = inflater.inflate(R.layout.guide_viewpager_page3, null);
		mGuideView4 = inflater.inflate(R.layout.guide_viewpager_page4, null);
		mGuideView4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(GuideViewPager.this,
						Home2Activity.class));
				finish();
			}
		});
		mViewList = new ArrayList<View>();
		mViewList.add(mGuideView1);
		mViewList.add(mGuideView2);
		mViewList.add(mGuideView3);
		mViewList.add(mGuideView4);

		mViewPager.setAdapter(new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			// 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
			@Override
			public int getCount() {
				return mViewList.size();
			}

			// PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
			@Override
			public void destroyItem(ViewGroup view, int position, Object object) {
				view.removeView(mViewList.get(position));
			}

			// 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(mViewList.get(position));
				return mViewList.get(position);
			}
		});

		for (int i = 0; i < mViewList.size(); i++) {
			ImageView imageView = new ImageView(this);
			imageView.setPadding(7, 0, 7, 0);
			// 默认第一个页面的小圆点为选中状态
			if (i == 0) {
				imageView.setImageResource(R.drawable.point_guide_checked);
			} else {
				imageView.setImageResource(R.drawable.point_guide_unchecked);
			}
			dot_linearlayout.addView(imageView);
		}
		// mImgChoose.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// new DatePickerDialog(GuideViewPager.this,
		// new OnDateSetListener() {
		// @Override
		// public void onDateSet(DatePicker view, int year,
		// int monthOfYear, int dayOfMonth) {
		// if(!contrastDate(year,monthOfYear+1,dayOfMonth)){
		// Toast.makeText(GuideViewPager.this, "日期应小于当前日期", 1).show();
		// return;
		// }
		// if(dayOfMonth<10){
		// mEdtBirth.setText(year + "-" + (monthOfYear+1)
		// + "-" + "0"+dayOfMonth);
		// }else{
		// mEdtBirth.setText(year + "-" + (monthOfYear+1)
		// + "-" + dayOfMonth);
		// }
		//
		// }
		// }, calendar.get(Calendar.YEAR), calendar
		// .get(Calendar.MONTH), calendar
		// .get(Calendar.DAY_OF_MONTH)).show();
		//
		// }
		// });
		// startButton.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		//
		// PreUtils.saveString(Constant.SP_KEY_USER_BIRTH,
		// mEdtBirth.getText().toString());
		// String age = "1";
		// try {
		// age = DensityUtil.diff(mEdtBirth.getText().toString(), "yyyy-MM-dd");
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		// PreUtils.saveString(Constant.SP_KEY_USER_AGE, age);
		// PreUtils.saveBoolean(Constant.SP_KEY_ISFIRSTIN, false);
		// Intent intent = new Intent(GuideViewPager.this,
		// MainActivity.class);
		// startActivity(intent);
		// GuideViewPager.this.finish();
		// }
		// });
		mViewPager.setOnPageChangeListener(this);

	}

	public boolean contrastDate(int year, int monthOfYear, int dayOfMonth) {
		String strMonthOfYear = "";
		String strdayOfMonth = "";
		if (monthOfYear < 10) {
			strMonthOfYear = "0" + monthOfYear;
		} else {
			strMonthOfYear = monthOfYear + "";
		}
		if (dayOfMonth < 10) {
			strdayOfMonth = "0" + dayOfMonth;
		} else {
			strdayOfMonth = dayOfMonth + "";
		}
		int mydate = Integer.parseInt(year + strMonthOfYear + strdayOfMonth);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		int intdate = Integer.parseInt(date);
		if (mydate > intdate) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		// // 设置前一个选中的不圆点不未选中状态
		// ImageView im1 = (ImageView) dot_linearlayout
		// .getChildAt(dot_cur_position);
		// im1.setImageResource(R.drawable.point_guide_unchecked);
		// // 设置当前选中的小圆点为选中状态
		// ImageView im2 = (ImageView) dot_linearlayout.getChildAt(position);
		// im2.setImageResource(R.drawable.point_guide_checked);
		// dot_cur_position = position;
	}
}

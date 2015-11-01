package com.ems.express.frame.widget.emotion;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.ems.express.R;

/**
 * 
 * @描述 表情资源加载<br>
 * 
 */
public class SmileyParser {

	public static final int EMOTION_DEL_RESOURCE = R.drawable.cj_emotion_del;

	private Context mContext;

	private static final int MAX_FACE_COUNT = 95;
	private String[] mSmileyTexts;
	private int[] mImageIds;
	private HashMap<String, Integer> mSmileyToRes;
	private HashMap<Integer, String> mResToSmiley;

	public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;

	private Pattern mPattern;

	private String TAG = "SmileyParser";

	public SmileyParser(Context context) {
		mContext = context;
		mSmileyTexts = context.getResources().getStringArray(
				DEFAULT_SMILEY_TEXTS);
		mSmileyToRes = new HashMap<String, Integer>(mSmileyTexts.length);
		mResToSmiley = new HashMap<Integer, String>(mSmileyTexts.length);
		mImageIds = new int[mSmileyTexts.length];
		buildSmileyToRes();
		mPattern = buildPattern();
	}

	private void buildSmileyToRes() {
		// 生成表情的id、含义及对应的Hash表，封装
		for (int i = 0, k = 0; i < MAX_FACE_COUNT; i++) {
			try {
				if (i < 10) {
					Field field;
					field = R.drawable.class.getDeclaredField("f00" + i);
					if (field == null) {
						continue;
					}
					int resourceId;
					resourceId = Integer.parseInt(field.get(null).toString());
					if (resourceId == 0) {
						continue;
					}
					mImageIds[k++] = resourceId;

				} else if (i < 91) {
					Field field = R.drawable.class.getDeclaredField("f0" + i);
					if (field == null) {
						continue;
					}
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					if (resourceId == 0) {
						continue;
					}
					mImageIds[k++] = resourceId;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < mSmileyTexts.length; i++) {
			String value = mSmileyTexts[i];
			mSmileyToRes.put(value, mImageIds[i]);
			mResToSmiley.put(mImageIds[i], value);
		}
	}

	private Pattern buildPattern() {
		StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);

		patternString.append('(');
		for (String s : mSmileyTexts) {
			patternString.append(Pattern.quote(s));
			patternString.append('|');
		}
		patternString.replace(patternString.length() - 1,
				patternString.length(), ")");

		return Pattern.compile(patternString.toString());
	}

	public int[] getImageIds() {
		return mImageIds;
	}

	public String getText(int faceIndex) {
		return mResToSmiley.get(getResource(faceIndex));
	}

	public int getResource(int faceIndex) {
		return mImageIds[faceIndex];
	}

	public CharSequence getImageSpan(int faceIndex) {
		String text = getText(faceIndex);
		if(text.equals("[NO]")){
			return new SpannableStringBuilder();
		}
		Drawable dr = mContext.getResources().getDrawable(getResource(faceIndex));
		dr.setBounds(0, 0, 60, 60);
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		builder.setSpan(new ImageSpan(dr,ImageSpan.ALIGN_BOTTOM), 0,
				text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	/**
	 * text中最后一个表情的长度
	 * 
	 * @param text
	 * @return
	 */
	public int getLastLength(CharSequence text) {
		// text为空时，返回0
		if (text == null || text.length() <= 0) {
			return 0;
		}
		int len = 1;
		Matcher matcher = mPattern.matcher(text);
		while (matcher.find()) {
			len = matcher.end() - matcher.start();
		}
		return len;
	}

	// 根据文本替换成图片
	public synchronized CharSequence replace(CharSequence text) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		Matcher matcher = mPattern.matcher(text);
		while (matcher.find()) {
			int resId = mSmileyToRes.get(matcher.group());
			Drawable dr = mContext.getResources().getDrawable(resId);
			dr.setBounds(0, 0, 60, 60);
			builder.setSpan(new ImageSpan(dr, ImageSpan.ALIGN_BOTTOM), matcher.start(),
					matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}

	// 根据文本替换成图片
	public synchronized CharSequence replace(CharSequence text, int hight) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		Matcher matcher = mPattern.matcher(text);
		while (matcher.find()) {
			int resId = mSmileyToRes.get(matcher.group());
			Drawable d = mContext.getResources().getDrawable(resId);
			d.setBounds(0, 0, 60, 60);
			builder.setSpan(new ImageSpan(d,ImageSpan.ALIGN_BOTTOM), matcher.start(), matcher.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}

}

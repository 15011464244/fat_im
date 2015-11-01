package com.newcdc.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.newcdc.tools.JsonParser;

/**
 * 
 * 
 * @author lion 2014-12-23 讯飞语音
 * 
 */
public class XFAudio {
	private Button press_say;
	private SpeechRecognizer mIat;
	private Context context;
	private TextView mytest;
	private EditText edit;
	private AutoCompleteTextView actv;
	private int state = 1;
	private static final String CLEAR = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]";
	// 语音合成对象
	private SpeechSynthesizer mTts;

	// 默认发音人
	private String voicer = "xiaoyan";
	private String editText = "";

	// 文字显示在textview上
	public XFAudio(Context context, Button press_say, TextView mytest) {
		this.press_say = press_say;
		this.context = context;
		this.mytest = mytest;
		state = 1;
		regist();
	}

	// 文字显示在editview上
	public XFAudio(Context context, Button press_say, EditText edit) {
		this.press_say = press_say;
		this.context = context;
		this.edit = edit;
		state = 2;
		regist();
	}

	// 文字显示在autocompletetextview上
	public XFAudio(Context context, Button press_say, AutoCompleteTextView actv) {
		this.press_say = press_say;
		this.context = context;
		this.actv = actv;
		state = 3;
		regist();
	}

	// 语音转文字
	public XFAudio(Context context, String edit_tosay) {
		this.context = context;
		this.editText = edit_tosay;
		synthesizer();
	}

	private void regist() {
		// 1.创建SpeechRecognizer对象
		mIat = SpeechRecognizer.createRecognizer(context, null);
		// 2.设置听写参数
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
	}

	private RecognizerListener mRecoListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
		}

		@Override
		public void onEndOfSpeech() {
		}

		@Override
		public void onError(SpeechError arg0) {
			String text = arg0.getErrorDescription();
//			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onResult(RecognizerResult arg0, boolean arg1) {
			String text = JsonParser.parseIatResult(arg0.getResultString());
			text = clearPunctuation(text);
			if (text.equals("")) {
//				Toast.makeText(context, "您没有说话", Toast.LENGTH_SHORT).show();
			} else {
				if (state == 1) {
					mytest.setText(text);
				} else if (state == 2) {
					edit.setText(text);
					edit.setSelection(edit.length());
				} else {
					actv.setText(text);
					actv.setSelection(actv.length());
				}
			}
		}

		@Override
		public void onVolumeChanged(int arg0) {

		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub

		}

	};

	// 清除语句中的符号，乱码
	public String clearPunctuation(String str) {
		Pattern pat = Pattern.compile(CLEAR);
		Matcher mat = pat.matcher(str);
		str = mat.replaceAll("");
		return str;
	}

	public void toSay() {
		press_say.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mIat.startListening(mRecoListener);
					break;
				case MotionEvent.ACTION_UP:
					mIat.stopListening();
					break;
				}
				return false;
			}
		});
	}

	// 初始化合成对象
	public void synthesizer() {
		mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
		setParam();
	}

	// 语音转文字
	public void startSay() {
		mTts.startSpeaking(editText, mTtsListener);
	}

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
		}

		@Override
		public void onSpeakPaused() {
		}

		@Override
		public void onSpeakResumed() {
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
			} else if (error != null) {
			}
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	private void setParam() {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 设置合成

		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置发音人
		mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);

		// 设置语速
		mTts.setParameter(SpeechConstant.SPEED, "50");

		// 设置音调
		mTts.setParameter(SpeechConstant.PITCH, "50");

		// 设置音量
		mTts.setParameter(SpeechConstant.VOLUME, "100");

		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

	}

	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
//				Toast.makeText(context, "初始化失败,错误码：" + code, 1).show();
			}
		}
	};
}

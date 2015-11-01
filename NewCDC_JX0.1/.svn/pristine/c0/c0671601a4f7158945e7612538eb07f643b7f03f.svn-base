package com.newcdc.chat.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.chat.frame.widget.emotion.FaceLayout;
import com.newcdc.chat.frame.widget.emotion.FaceLayout.OnDeleteListener;
import com.newcdc.chat.frame.widget.emotion.FaceLayout.OnFaceClickListener;
import com.newcdc.chat.frame.widget.emotion.SmileyParser;
import com.newcdc.chat.frame.widget.gallery.App;
import com.newcdc.chat.frame.widget.gallery.PickPhotoActivity;
import com.newcdc.chat.ui.ChatMessageBean.ContentType;
import com.newcdc.chat.util.Constant;
import com.newcdc.chat.util.FileUtil;
import com.newcdc.chat.util.ImageUtil;
import com.newcdc.db.ChatMessageDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.model.ChatMessageTabBean;
import com.newcdc.tools.Global;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.ui.GetBitmap;

public class MainChatActivity extends Activity implements OnClickListener {

	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private InputMethodManager SoftInput;// 软键盘管理
	private ImageView mIvVoice;// 语音按钮
	private EditText mEdtText;// 信息编辑框
	private Button mBtnSpeak;// 按住说话
	private ImageView mIvEmotion;// 表情按钮
	private ImageView mIvOther;// 添加按钮
	private ImageView btn_back_chat;// 添加按钮
	private TextView mTvSend;// 发送按钮
	private RelativeLayout mRelFace;// 表情栏
	private FaceLayout mFace;// 表情集
	private SmileyParser mSmileyParser;
	private MediaPlayer mPlayer = null;// 用于语音播放
	private RelativeLayout mRelOther;// 添加栏
	private LinearLayout mLinPicture;// 照片
	private LinearLayout mLinPhrases;// 常用语
	private LinearLayout mLinCamera;// 照相机
	private DeliverDaoFactory daoFactory;
	private static ChatMessageDao mChatMessageDao;
	private ListView mListview;
	private static SharePreferenceUtilDaoFactory shareDao;
	private List<ChatMessageBean> mList = new ArrayList<ChatMessageBean>();
	private static ChatBoxMessageAdapter mAdapter;
	/** http服务 */
	protected static RequestQueue sQueue;
	protected static int count = 0;

	@SuppressWarnings("unused")
	private FormFile formFile;
	private ChatMessageBean cm;
	String imgPath = "";
	 private boolean isMediaPlayering = false;

	/**
	 * 接收消息需要用到此static Handler 控制消息的显示
	 */
	// public static final Handler sendHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 1:
	// Log.e("msg", "是否上传成功:" + msg.obj);
	// break;
	// }
	// }
	// };

	/**
	 * 接收消息需要用到此static Handler 控制消息的显示
	 */
	public static final Handler sHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mAdapter.addItem((ChatMessageBean) msg.obj);
				break;
			}
		}
	};
	/**
	 * 控制麦克风声音大小的显示
	 */
	Handler imgHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (RECODE_STATE == RECORD_ING) {
					stopVoice();
					dialog.dismiss();
					RECODE_STATE = RECODE_ED;
				}
				break;
			case 1:
				setDialogImage();
				break;
			default:
				break;
			}
		}
	};

	private static String target;// 消息接收方
	private String sid;
	/** 语音文件保存路径 */
	private String mFileName = null;
	/** 用于完成录音 */
	private MediaRecorder mRecorder = null;
	/** 录音动画 Dialog */
	private Dialog dialog;
	/** 录音动画ImageView控件 */
	private ImageView dialog_img;

	private static int MAX_TIME = 30; // 最长录制时间，单位秒，0为无时间限制
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音
	private static int RECODE_STATE = 0; // 录音的状态

	private static float recodeTime = 0.0f; // 录音的时间
	private static double voiceValue = 0.0; // 麦克风获取的音量值

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals("msg.success")) {// 接收消息的接收器
				mList.clear();
				initData();
			} else if (intent.getAction().equals("msg.username")) {// 接收更新完成客户名字的接收器
				mList.clear();
				initData();
			}
		}
	};

	public static void startAction(Context context, String target, String sid) {
		Intent intent = new Intent();
		intent.setClass(context, MainChatActivity.class);
		intent.putExtra("target", target);
		intent.putExtra("sid", sid);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatmain);
		sQueue = Volley.newRequestQueue(MainChatActivity.this);
		sQueue.start();
		daoFactory = DeliverDaoFactory.getInstance();
		mChatMessageDao = daoFactory.getChatMessageDao(MainChatActivity.this);
		shareDao = SharePreferenceUtilDaoFactory
				.getInstance(MainChatActivity.this);
		Intent intent = getIntent();
		sid = intent.getStringExtra("sid");
		target = intent.getStringExtra("target");
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 启动activity时不自动弹出软键盘
		SoftInput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
		loadData();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter("msg.success");
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}

	private void initView() {
		mIvVoice = (ImageView) findViewById(R.id.voice);
		mEdtText = (EditText) findViewById(R.id.edit);
		mBtnSpeak = (Button) findViewById(R.id.speak);
		mIvEmotion = (ImageView) findViewById(R.id.emotion);
		mIvOther = (ImageView) findViewById(R.id.other);// +
		btn_back_chat = (ImageView) findViewById(R.id.btn_back_chat);// +
		mFace = (FaceLayout) findViewById(R.id.single_emotion);
		mTvSend = (TextView) findViewById(R.id.send);
		mRelFace = (RelativeLayout) findViewById(R.id.emotion_gridview);
		mRelOther = (RelativeLayout) findViewById(R.id.other_tool);
		mLinPicture = (LinearLayout) findViewById(R.id.picture);
		mLinPhrases = (LinearLayout) findViewById(R.id.phrases);
		mLinCamera = (LinearLayout) findViewById(R.id.camera);
		mListview = (ListView) findViewById(R.id.listview);

		btn_back_chat.setOnClickListener(this);
		mEdtText.setOnClickListener(this);
		mIvVoice.setOnClickListener(this);
		mIvEmotion.setOnClickListener(this);
		mIvOther.setOnClickListener(this);
		mTvSend.setOnClickListener(this);
		mLinPicture.setOnClickListener(this);
		mLinPhrases.setOnClickListener(this);
		mLinCamera.setOnClickListener(this);

		if (mSmileyParser == null) {
			mSmileyParser = new SmileyParser(this);
			mFace.setmSmileyParser(mSmileyParser);
		}
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
	}

	private void initData() {
		List<ChatMessageTabBean> messages = mChatMessageDao
				.queryAllMessage(target);
		int messagessize = messages.size();
		for (int i = 0; i < messagessize; i++) {
			ChatMessageBean cm = new ChatMessageBean();
			cm.setTarget(messages.get(i).getTarget());
			cm.setContent(messages.get(i).getUrl());
			cm.setSource(messages.get(i).getSource());
			cm.setContentType(messages.get(i).getMessagetype());
			cm.setStatus(messages.get(i).getSndStatus());
			cm.setMessageId(messages.get(i).getMessageId());
			cm.setMessageTime(messages.get(i).getSpeechMessageTime());
			mList.add(cm);
		}
		mChatMessageDao.SetMessageIsRead(target);
		mAdapter.notifyDataSetChanged();
		mListview.setSelection(mList.size() - 1);
	}

	private void loadData() {
		mAdapter = new ChatBoxMessageAdapter(this, mList, sid);
		mListview.setAdapter(mAdapter);
		mListview.setSelection(mList.size() - 1);
		mFace.setOnFaceClickListener(new OnFaceClickListener() {

			@Override
			public void onFaceClick(int face) {
				CharSequence text = mSmileyParser.getImageSpan(face);
				int position = mEdtText.getSelectionStart();
				mEdtText.getText().insert(position, text);
			}
		});
		mFace.setDeleteListener(new OnDeleteListener() {

			@Override
			public void onDelete() {
				int position = mEdtText.getSelectionStart();
				CharSequence textPre = mEdtText.getText().subSequence(0,
						position);
				int len = mSmileyParser.getLastLength(textPre);
				if (position > 0) {
					mEdtText.getText().delete(position - len, position);
				}
			}
		});
		mEdtText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int paramInt1,
					int paramInt2, int paramInt3) {

				if (s != null && !"".equals(s.toString())) {
					mTvSend.setVisibility(View.VISIBLE);
				} else {
					mTvSend.setVisibility(View.GONE);
					mIvOther.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}

			@Override
			public void afterTextChanged(Editable paramEditable) {
				if (mEdtText.getText().toString().equals("")) {
					mTvSend.setVisibility(View.GONE);
					mIvOther.setVisibility(View.VISIBLE);
				}
			}
		});
		mEdtText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View paramView, boolean paramBoolean) {
				if (paramBoolean) {
					hideEmotion();
					hideVoice();
					hideOther();
					SoftInput.showSoftInput(mEdtText,
							InputMethodManager.SHOW_FORCED);// 显示键盘
				} else {
					SoftInput.hideSoftInputFromWindow(
							mEdtText.getWindowToken(), 0);
				}
			}
		});

		mBtnSpeak.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					System.out.println("ACTION_DOWN");
					startVoice();
					RECODE_STATE = RECORD_ING;
					showVoiceDialog();
					Thread recordThread = new Thread(ImgThread);
					recordThread.start();
					break;
				case MotionEvent.ACTION_UP:
					System.out.println("ACTION_UP");
					if (RECODE_STATE == RECORD_ING)
						stopVoice();
					RECODE_STATE = RECODE_ED;

					File file = new File(mFileName);

					Message msg = new Message();
					msg.what = 1;
					cm = new ChatMessageBean(ContentType.FILE, "", App.loginId,
							target, "2");
					cm.setContent(mFileName);
					formFile = new FormFile(mFileName, file, "amr",
							"application/octet-stream");
					// new Thread() {
					// public void run() {
					// try {
					// String message = HttpRequester.post(
					// Constant.FileUpload, cm, formFile);
					// // Message mess = new Message();
					// // mess.what = 1;
					// // mess.obj = message;
					// // MainActivity.sendHandler.sendMessage(mess);
					// } catch (Exception e) {
					// e.printStackTrace();
					// Log.e("msg", e.getMessage());
					// }
					// };
					// }.start();
					sendMessage(0, mChatMessageDao.getMesNo(),
							ContentType.FILE, "" + mFileName);
					msg.obj = cm;
					MainChatActivity.sHandler.sendMessage(msg);
					hideVoiceDialog();
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println(resultCode);
		switch (requestCode) {
		case CAMERA_REQUEST_CODE:
			if (resultCode == -1) {
				Intent intentFromCapture = new Intent();
				intentFromCapture.setClass(this, PhotoLView.class);
				intentFromCapture.putExtra("photo_path", imgPath);
				intentFromCapture.putExtra("type", "0");
				startActivityForResult(intentFromCapture, RESULT_REQUEST_CODE);
			}
			break;
		case Constant.RESULT_CODE_PIC:
			imagephotoThread();
			break;
		case RESULT_REQUEST_CODE:
			if (data != null) {
				if (UtilMethod.checkSDCard()) {
					String imgPath = data.getStringExtra("sendImg");
					sendCameraPic(imgPath);
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}
			}
			break;
		default:
			break;
		}

		// Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 发送照片
	public void sendCameraPic(String picpath) {
		File file = GetBitmap.getimageyang(picpath);
		sendMessage(0, mChatMessageDao.getMesNo(), ContentType.IMG, "" + file);
		setImageFile(file, file + "");
	}

	// 发送图片
	private void imagephotoThread() {
		// mLinAdd.setVisibility(View.GONE);
		if (Bimp.drr.size() > 0) {
			String path = "";
			final File[] files = new File[Bimp.drr.size()];
			// ChatMessageBean bean = null;
			for (int i = 0; i < Bimp.drr.size(); i++) {
				// path = ImageUtil.comPressNewPath(Bimp.drr.get(i),
				// String.valueOf(i));
				path = ImageUtil.comPressNewPath(Bimp.drr.get(i),
						String.valueOf(i));
				files[i] = new File(path);
				setImageFile(files[i], path);
				// 发送图片
				sendMessage(0, mChatMessageDao.getMesNo(), ContentType.IMG,
						path);
				// bean = new ChatMessageBean();
				// bean.setSource(App.loginId);
				// bean.setTarget(target);
				// bean.setContentType(ContentType.IMG);
				// bean.setPicpath(path);
				// mAdapter.addItem(bean);
			}
			Bimp.drr.clear();
		}
	}

	private void setImageFile(File file, String path) {
		formFile = new FormFile(path, file, "jpg", "application/octet-stream");
		cm = new ChatMessageBean(ContentType.IMG, path, App.loginId, target,
				"2");
		cm.setPicpath(path);
		mAdapter.addItem(cm);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_chat:
			finish();
			break;
		case R.id.voice:
			System.out.println("voice");
			if (mBtnSpeak.getVisibility() == View.GONE) {
				hideEmotion();
				hideOther();
				showVoice();
			} else {
				hideVoice();
			}
			break;

		case R.id.edit:
			mRelOther.setVisibility(View.GONE);
			hideVoice();
			mRelFace.setVisibility(View.GONE);
			// mIvEmotion.setImageResource(R.drawable.image_chat_emotion_normal);
			SoftInput.showSoftInput(mEdtText, InputMethodManager.SHOW_FORCED);// 显示键盘
			break;

		case R.id.emotion:
			if (mRelFace.getVisibility() == View.GONE) {
				hideOther();
				hideVoice();
				showEmotion();
			} else {
				hideEmotion();
			}
			break;

		case R.id.other:
			if (mRelOther.getVisibility() == View.GONE) {
				hideEmotion();
				hideVoice();
				showOther();
			} else {
				hideOther();
			}
			break;

		case R.id.picture:
			Intent intent = new Intent(this, PickPhotoActivity.class);
			intent.putExtra("type", "0");
			startActivityForResult(intent, Constant.RESULT_CODE_PIC);
			break;
		case R.id.phrases:
			System.out.println("onclick:phrases");
			break;

		case R.id.camera:
			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// 判断存储卡是否可以用，可用进行存储
			if (UtilMethod.checkSDCard()) {
				imgPath = FileUtil.getTempFileName();
				intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(imgPath)));
			}
			startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
			break;
		case R.id.send:
			// 这里只是发送文本的消息
			final String message = mEdtText.getText().toString();
			final ChatMessageBean sendMsg = new ChatMessageBean("0", message,
					App.loginId, target, "2");
			mAdapter.addItem(sendMsg);
			if (!"".equals(shareDao.getCLIENTID()) && !"".equals(target)
					&& !"".equals(message)) {
				LogUtils.e("MesageID:  " + mChatMessageDao.getMesNo());
				sendMessage(0, mChatMessageDao.getMesNo(), "0", message);
			}
			mEdtText.getText().clear();
			break;

		default:
			break;
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 *            消息内容
	 * @param messageStatus消息状态
	 *            0 新消息，1是消息重发
	 * @param messageId
	 *            重发消息的ID
	 * @param messageType
	 *            消息类型 0文本，1图片，2语音
	 */
	public void sendMessage(final int messageStatus, final int messageId,
			String messageType, String url) {
		if (0 == messageStatus) {// 发送新消息
			int speechMessageTime = 0;
			if ("2".equals(messageType)) {
				if (mFileName != null && !"".equals(mFileName)) {
					try {
						mPlayer.reset();
						mPlayer.setDataSource(mFileName);
						mPlayer.prepare();
						speechMessageTime = mPlayer.getDuration() / 1000;
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			Log.e("要保存到本地的信息是：  ", "" + url);
			// 消息存本地
			ArrayList<ChatMessageTabBean> insertBeans = new ArrayList<ChatMessageTabBean>();
			ChatMessageTabBean iBean = new ChatMessageTabBean();
			iBean.setTarget(target);
			iBean.setMessageId(messageId);
			iBean.setSndStatus("2");
			iBean.setSpeechMessageTime(speechMessageTime);
			iBean.setSource(shareDao.getCLIENTID());
			iBean.setUrl(url);
			iBean.setMessagetype(messageType);
			insertBeans.add(iBean);
			mChatMessageDao.insertChatMessage(insertBeans);
			sendMessageData(url, messageId, messageType);
		} else if (1 == messageStatus) {// 重新发送
			mChatMessageDao.UpdateMegIDStatus(messageId, "2");// 发送状态成功?0失败?1未发送之前?2
			mList.clear();
			initData();
			sendMessageData(url, messageId, messageType);
		}
	}

	public void sendMessageData(String message, final int messageId,
			String messageType) {
		// com.loopj.android.http.RequestParams params = new
		// com.loopj.android.http.RequestParams();
		// params.put("source", shareDao.getCLIENTID());
		// params.put("target", target);
		// params.put("message_type", messageType);
		// params.put("content", message);
		// String postUrl = "";
		// if (ContentType.TEXT.equals(messageType)) {
		// postUrl = Global.CHATTEXT;
		// } else if (ContentType.IMG.equals(messageType)
		// || ContentType.FILE.equals(messageType)) {
		// try {
		// params.put("uploadFile", new File(message));
		// postUrl = Global.CHATIMG;
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// }
		// HttpUtil.post(postUrl, params, new AsyncHttpResponseHandler() {
		// @Override
		// public void onStart() {
		// }
		//
		// public void onSuccess(String arg0) {
		// LogUtils.i("返回的结果： " + arg0.toString());
		// JSONObject jo = JSONObject.parseObject(arg0.toString());
		// if ("0".equals(jo.getString("resCode"))) {
		// // 根据消息id修改发送成功的状态值
		// mChatMessageDao.UpdateMegIDStatus(messageId, "0");//
		// 发送状态成功?0失败?1未发送之前?2
		// mList.clear();
		// initData();
		// } else if ("1".equals(jo.getString("resCode"))) {
		// mChatMessageDao.UpdateMegIDStatus(messageId, "1");//
		// 发送状态成功?0失败?1未发送之前?2
		// mList.clear();
		// initData();
		// }
		// };
		//
		// @Override
		// public void onProgress(int bytesWritten, int totalSize) {
		// super.onProgress(bytesWritten, totalSize);
		// }
		//
		// @Override
		// public void onFinish() {
		// super.onFinish();
		// }
		//
		// @Override
		// public void onFailure(Throwable error) {
		// LogUtils.e("msg" + error.toString());// TODO 发送失败重新发送
		// mChatMessageDao.UpdateMegIDStatus(messageId, "1");//
		// 发送状态成功?0失败?1未发送之前?2
		// mList.clear();
		// initData();
		// };
		// });
		// XUtils 会有超时的情况出现
		JSONObject myjson = new JSONObject();
		myjson.put("source", shareDao.getCLIENTID());
		myjson.put("target", target);
		myjson.put("message_type", messageType);
		myjson.put("content", message);
		String postUrl = "";
		RequestParams params = new RequestParams(); // 默认编码UTF-8
		if (ContentType.TEXT.equals(messageType)) {
			params.addBodyParameter("message", myjson.toString());
			postUrl = Global.CHATTEXT;
		} else if (ContentType.IMG.equals(messageType)
				|| ContentType.FILE.equals(messageType)) {
			params.addBodyParameter("uploadFile", new File(message));
			params.addBodyParameter("message", myjson.toString());
			postUrl = Global.CHATIMG;
		}

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, postUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							LogUtils.e("msg" + "文件上传中...");
							LogUtils.e("msg" + "总共大小" + total + ";已传大小"
									+ current);
						} else {
							LogUtils.e("msg" + "上传完成");
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.e("code:" + responseInfo.result);
						JSONObject jo = JSONObject
								.parseObject(responseInfo.result);
						if ("0".equals(jo.getString("resCode"))) {
							// 根据消息id修改发送成功的状态值
							mChatMessageDao.UpdateMegIDStatus(messageId, "0");// 发送状态成功?0失败?1未发送之前?2

							mList.clear();
							initData();
						} else if ("1".equals(jo.getString("resCode"))) {
							mChatMessageDao.UpdateMegIDStatus(messageId, "1");// 发送状态成功?0失败?1未发送之前?2
							mList.clear();
							initData();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.e("msg" + msg);// TODO 发送失败重新发送
						mChatMessageDao.UpdateMegIDStatus(messageId, "1");// 发送状态成功?0失败?1未发送之前?2

						mList.clear();
						initData();
					}

				});
	}

	public void hideVoice() {
		if (mBtnSpeak.getVisibility() == View.VISIBLE) {
			// mIvVoice.setImageResource(R.drawable.image_chat_voice_normal);
			mEdtText.setVisibility(View.VISIBLE);
			mBtnSpeak.setVisibility(View.GONE);
			mIvEmotion.setVisibility(View.VISIBLE);
		}
	}

	public void showVoice() {
		if (mBtnSpeak.getVisibility() == View.GONE) {
			// mIvVoice.setImageResource(R.drawable.image_chat_voice_selected);
			mEdtText.setVisibility(View.GONE);
			mBtnSpeak.setVisibility(View.VISIBLE);
			SoftInput.hideSoftInputFromWindow(mEdtText.getWindowToken(), 0);// 隐藏键盘
			mIvEmotion.setVisibility(View.GONE);
		}
	}

	public void hideOther() {
		if (mRelOther.getVisibility() == View.VISIBLE) {
			mRelOther.setVisibility(View.GONE);
			// mIvOther.setImageResource(R.drawable.image_chat_other_normal);
		}
	}

	public void showOther() {
		if (mRelOther.getVisibility() == View.GONE) {
			SoftInput.hideSoftInputFromWindow(mEdtText.getWindowToken(), 0);// 隐藏键盘
			mRelOther.setVisibility(View.VISIBLE);
			// mIvOther.setImageResource(R.drawable.image_chat_other_selected);
		}
	}

	public void hideEmotion() {
		if (mRelFace.getVisibility() == View.VISIBLE) {
			mRelFace.setVisibility(View.GONE);
			SoftInput.showSoftInput(mEdtText, InputMethodManager.SHOW_FORCED);// 显示键盘
			// mIvEmotion.setImageResource(R.drawable.image_chat_emotion_normal);
		}
	}

	public void showEmotion() {
		if (mRelFace.getVisibility() == View.GONE) {
			SoftInput.hideSoftInputFromWindow(mEdtText.getWindowToken(), 0);// 隐藏键盘
			// mIvEmotion.setImageResource(R.drawable.image_chat_emotion_selected);
			mRelFace.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		if (mRelFace.getVisibility() == View.VISIBLE) {
			mIvEmotion.setImageResource(R.drawable.image_chat_emotion_normal);
			mRelFace.setVisibility(View.GONE);
		} else {
			finish();
		}
	}

	/** 开始录音 */
	private void startVoice() {
		// 设置录音保存路径
		mFileName = com.newcdc.tools.Constant.MYVOICEFORDER
				+ UUID.randomUUID().toString() + ".amr";
		// 获取SD卡可读写状态
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			System.out.println("SD Card is not mounted,It is  " + state);
		}

		File directory = new File(mFileName).getParentFile();

		if (!directory.exists() && !directory.mkdirs()) {
			System.out.println("Path to file could not be created");
		}
		// ToastUtil.show(MainChatActivity.this, "开始录音");
		if (isMediaPlayering) {
		//
		 } else {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			System.out.println("prepare() failed");
			e.printStackTrace();
		}
		mRecorder.start();
		 isMediaPlayering = true;
		 }
	}

	/** 停止录音 */
	private void stopVoice() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		 isMediaPlayering = false;
		// ToastUtil.show(MainChatActivity.this, "保存录音" + mFileName);
	}

	// 录音时显示Dialog
	void showVoiceDialog() {
		dialog = new Dialog(MainChatActivity.this, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_dialog);
		dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
		dialog.show();
	}

	// 录音停止时隐藏Dialog
	void hideVoiceDialog() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	// 录音Dialog图片随声音大小切换
	void setDialogImage() {
		// if (voiceValue < 200.0) {
		// dialog_img.setImageResource(R.drawable.record_animate_01);
		// } else if (voiceValue > 200.0 && voiceValue < 300) {
		// dialog_img.setImageResource(R.drawable.record_animate_02);
		// } else if (voiceValue > 300.0 && voiceValue < 400) {
		// dialog_img.setImageResource(R.drawable.record_animate_03);
		// } else if (voiceValue > 400.0 && voiceValue < 500) {
		// dialog_img.setImageResource(R.drawable.record_animate_04);
		// } else if (voiceValue > 500.0 && voiceValue < 600) {
		// dialog_img.setImageResource(R.drawable.record_animate_05);
		// } else if (voiceValue > 600.0 && voiceValue < 700) {
		// dialog_img.setImageResource(R.drawable.record_animate_06);
		// } else if (voiceValue > 700.0 && voiceValue < 800) {
		// dialog_img.setImageResource(R.drawable.record_animate_07);
		// } else if (voiceValue > 800.0 && voiceValue < 900.0) {
		dialog_img.setImageResource(R.drawable.record_animate_08);
		// } else if (voiceValue > 900.0 && voiceValue < 1000.0) {
		// dialog_img.setImageResource(R.drawable.record_animate_09);
		// } else if (voiceValue > 1000.0 && voiceValue < 1200.0) {
		// dialog_img.setImageResource(R.drawable.record_animate_10);
		// } else if (voiceValue > 1200.0 && voiceValue < 1400.0) {
		// dialog_img.setImageResource(R.drawable.record_animate_11);
		// } else if (voiceValue > 1400.0 && voiceValue < 1600.0) {
		// dialog_img.setImageResource(R.drawable.record_animate_12);
		// } else if (voiceValue > 1600.0 && voiceValue < 1800.0) {
		// dialog_img.setImageResource(R.drawable.record_animate_13);
		// } else if (voiceValue > 1800.0) {
		// dialog_img.setImageResource(R.drawable.record_animate_14);
		// }
	}

	// 录音线程
	private Runnable ImgThread = new Runnable() {
		@Override
		public void run() {
			recodeTime = 0.0f;
			while (RECODE_STATE == RECORD_ING) {
				if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
					imgHandle.sendEmptyMessage(0);
				} else {
					try {
						Thread.sleep(100);
						recodeTime += 0.1;
						if (RECODE_STATE == RECORD_ING) {
							voiceValue = mRecorder.getMaxAmplitude();
							imgHandle.sendEmptyMessage(1);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	};

}

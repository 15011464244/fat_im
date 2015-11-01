package com.ems.express.ui.chat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
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
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.GsonPostParamsRequest;
import com.arvin.koalapush.potter.Kpush;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.chat.ChatBoxMessageAdapter;
import com.ems.express.adapter.common.MainCommonAdapter;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.adapter.message.SignMessageBean;
import com.ems.express.bean.ChatListItemBean;
import com.ems.express.bean.ChatMessageBean;
import com.ems.express.bean.ChatMessageBean.ContentType;
import com.ems.express.constant.Constant;
import com.ems.express.core.msg.IObserverBase;
import com.ems.express.core.msg.MessageManager;
import com.ems.express.core.service.ChatService.PlayState;
import com.ems.express.frame.widget.emotion.FaceLayout;
import com.ems.express.frame.widget.emotion.FaceLayout.OnDeleteListener;
import com.ems.express.frame.widget.emotion.FaceLayout.OnFaceClickListener;
import com.ems.express.frame.widget.emotion.SmileyParser;
import com.ems.express.frame.widget.gallery.Bimp;
import com.ems.express.frame.widget.gallery.PickPhotoActivity;
import com.ems.express.net.Carrier;
import com.ems.express.net.resp.BaseResp;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.FileUtil;
import com.ems.express.util.FormFile;
import com.ems.express.util.HttpRequester;
import com.ems.express.util.ImageUtil;
import com.ems.express.util.NotificationUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.ems.express.util.UtilMethod;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity implements OnClickListener {
	
	private Context mContext;
	private AnimationUtil animationUtil;

	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private InputMethodManager SoftInput;// 软键盘管理
	private ImageView mIvVoice;// 语音按钮
	private static EditText mEdtText;// 信息编辑框
	private Button mBtnSpeak,test;// 按住说话
	private ImageView mIvBack;// 返回键
	private ImageView mIvEmotion;// 表情按钮
	private ImageView mIvOther;// 添加按钮
	private TextView mTvSend, mMainChatTitle;// 发送按钮
	private RelativeLayout mRelFace;// 表情栏
	private FaceLayout mFace;// 表情集
	private SmileyParser mSmileyParser;
	private RelativeLayout mRelOther;// 添加栏
	private LinearLayout mLinPicture;// 照片
	private LinearLayout mLinPhrases;// 常用语
	private LinearLayout mLinCamera;// 照相机
	private RelativeLayout mlistLinear;// 聊天list背景
	private RelativeLayout mAdd;
	private ListView mCommonList;
	private MainCommonAdapter mMainCommonAdapter;
	private ArrayList<String> mCommonArrayList;

	private ListView mListview;
	private List<ChatMessageBean> mList = new ArrayList<ChatMessageBean>();
	private static ChatBoxMessageAdapter mAdapter;

	private FormFile formFile;
	private ChatMessageBean cm;
	String imgPath = "";
	private static String employeeNo;
	/**
	 * 控制麦克风声音大小的显示
	 */
	Handler imgHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (RECODE_STATE == RECORD_ING) {
					try {
						Thread.sleep(200);
						stopVoice();
						dialog.dismiss();
						RECODE_STATE = RECODE_ED;
						MainActivity.sendMessage("hello", 0, 0, "2", mFileName);
						test.setVisibility(View.GONE);
						mBtnSpeak.setVisibility(View.VISIBLE);
						sendStatus = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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

	private static ChatListItemBean mBean;// 聊天对象
	private String target;// 消息接收方
	/** 语音文件保存路径 */
	private static String mFileName = null;
	/** 用于完成录音 */
	private MediaRecorder mRecorder = null;
	/** 录音存储路径 */
	private static final String PATH = "/sdcard/express/speechSend/";
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
	private boolean sendStatus = true;//是否为超过30秒自动发送 true为不是，false为是

	public static void startAction(Context context, ChatListItemBean bean) {
		Intent intent = new Intent();
		intent.setClass(context, MainActivity.class);
		intent.putExtra("target", bean);
		context.startActivity(intent);
	}

	IObserverBase ib = new IObserverBase() {

		@Override
		public void sendMessage(String message) {
			// ToastUtil.show(MainActivity.this, "MainActivity接收被观察者发送的消息");
			String employeeNo = App.dbHelper.queryEmployeeNoIsClientId(App.db,
					mBean.getClientId());
			List<ChatMessageBean> list = App.dbHelper.queryAllMessage(App.db,
					employeeNo, mBean.getImage());
			mAdapter.setItem(list);
			mAdapter.notifyDataSetChanged();
			App.dbHelper.updateUnReadMessage(App.db, employeeNo);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		animationUtil = new AnimationUtil(this, R.style.dialog_animation);

		Intent intent = getIntent();
		mBean = (ChatListItemBean) intent.getSerializableExtra("target");
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 启动activity时不自动弹出软键盘
		SoftInput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		initView();
		/*
		 * 替换现有clientId替换缓存中的clientId
		 */
		//获取机构号员工号
		//开启菊花
		animationUtil.show();
		Carrier carrier = App.dbHelper.queryCarrierByNameAndPhone(App.db, mBean.getName(), mBean.getMobile());
		changeClientId(carrier.getCode(), carrier.getEmployeeNo());
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
	
	/**
	 * 根据机构号和员工号更改clientId
	 * @param orgcode
	 * @param employeeNo
	 * @return
	 */
	private void changeClientId(final String orgCode,final String employeeNo){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orgcode", orgCode);
		map.put("username", employeeNo);
		GsonPostParamsRequest<SignMessageBean> rep = new GsonPostParamsRequest<SignMessageBean>(
				Method.POST, Constant.QueryNextSection, null,
				new Listener<SignMessageBean>() {
					@Override
					public void onResponse(SignMessageBean arg0) {
						if (arg0 != null) {
							if ("1".equals(arg0.getResult())) {
								SendNoticeBean dmb = arg0.getBody()
										.getSuccess();
								String clientId = dmb.getChannelId();
//								String clientId = "607d653ab5a33669b3e8838b3cb76bf2";
								if(null == clientId || "".equals(clientId)||"null".equals(clientId)){
									ToastUtil.show(mContext, "无法和离线的邮递员聊天，请您再找其他邮递员！");
									//关闭菊花
									animationUtil.dismiss();
									return;
								}else{
									//替换原有的clientid
									App.dbHelper.replaceClientId(App.db, clientId, orgCode, employeeNo);
									loadData();
									//关闭菊花
									animationUtil.dismiss();
								}

							} else if ("0".equals(arg0.getResult())) {
								Log.e("IM", arg0.getBody().getError());
								//关闭菊花
								animationUtil.dismiss();
							}
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						ToastUtil.show(mContext, "网络异常");
						//关闭菊花
						animationUtil.dismiss();
					}
				}, SignMessageBean.class, map);
		App.getQueue().add(rep);
		
	}
		

	private void initView() {
		mIvVoice = (ImageView) findViewById(R.id.voice);
		mEdtText = (EditText) findViewById(R.id.edit);
		mBtnSpeak = (Button) findViewById(R.id.speak);
		mIvEmotion = (ImageView) findViewById(R.id.emotion);
		mIvOther = (ImageView) findViewById(R.id.other);// +
		mIvBack = (ImageView) findViewById(R.id.back_button);// 返回键
		mFace = (FaceLayout) findViewById(R.id.single_emotion);
		mTvSend = (TextView) findViewById(R.id.send);
		mRelFace = (RelativeLayout) findViewById(R.id.emotion_gridview);
		mRelOther = (RelativeLayout) findViewById(R.id.other_tool);
		mLinPicture = (LinearLayout) findViewById(R.id.picture);
		mLinPhrases = (LinearLayout) findViewById(R.id.phrases);
		mLinCamera = (LinearLayout) findViewById(R.id.camera);
		mListview = (ListView) findViewById(R.id.listview);
		mAdd = (RelativeLayout) findViewById(R.id.add);
		mCommonList = (ListView) findViewById(R.id.common_list);
		mMainChatTitle = (TextView) findViewById(R.id.main_chat_title);
		mlistLinear = (RelativeLayout) findViewById(R.id.list_linear);
		test = (Button) findViewById(R.id.test);
		mEdtText.setOnClickListener(this);
		mIvVoice.setOnClickListener(this);
		mIvEmotion.setOnClickListener(this);
		mIvOther.setOnClickListener(this);
		mTvSend.setOnClickListener(this);
		mLinPicture.setOnClickListener(this);
		mLinPhrases.setOnClickListener(this);
		mLinCamera.setOnClickListener(this);
		mIvBack.setOnClickListener(this);
		mlistLinear.setOnClickListener(this);
		if (mSmileyParser == null) {
			mSmileyParser = new SmileyParser(this);
			mFace.setmSmileyParser(mSmileyParser);
		}
	}

	private void loadData() {
		employeeNo = App.dbHelper.queryEmployeeNoIsClientId(App.db,
				mBean.getClientId());
		if (employeeNo == null) {
			App.mToastUtil.show("不存在的快递员");
			employeeNo = "123";
		}
		mList = App.dbHelper.queryAllMessage(App.db, employeeNo,
				mBean.getImage());
		App.dbHelper.updateUnReadMessage(App.db, employeeNo);
		mAdapter = new ChatBoxMessageAdapter(this, mList);
		mListview.setAdapter(mAdapter);
		mListview.setSelection(mList.size() - 1);
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SoftInput.hideSoftInputFromWindow(mEdtText.getWindowToken(), 0);// 隐藏键盘
				mRelFace.setVisibility(View.GONE);
				hideOther();
			}
		});
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
					sendStatus = true;
					test.setVisibility(View.VISIBLE);
					mBtnSpeak.setVisibility(View.GONE);
					System.out.println("ACTION_DOWN");
					if (App.getmChatService().getPlaySate()
							.equals(PlayState.Playing)) {
						App.getmChatService().stopSpeech();
					}
					startVoice();
					RECODE_STATE = RECORD_ING;
					showVoiceDialog();
					Thread recordThread = new Thread(ImgThread);
					recordThread.start();
					break;
				case MotionEvent.ACTION_UP:
					if(sendStatus){
						try {
							Thread.sleep(200);
							System.out.println("ACTION_UP");
							if (RECODE_STATE == RECORD_ING)
								stopVoice();
							RECODE_STATE = RECODE_ED;
							test.setVisibility(View.GONE);
							mBtnSpeak.setVisibility(View.VISIBLE);
							if (recodeTime > 4) {
								sendMessage("hello", 0, 0, "2", mFileName);
							} else {
								App.mToastUtil.show("录音太短");
								File file = new File(mFileName);
								FileUtil.deleteAllFiles(file);
							}
							hideVoiceDialog();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
				return false;
			}
		});

		mMainChatTitle.setText(mBean.getName());
		mCommonArrayList = SpfsUtil.loadCommonWords();
		mMainCommonAdapter = new MainCommonAdapter(this, mCommonArrayList);
		mCommonList.setAdapter(mMainCommonAdapter);

		mCommonList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				sendMessage(mCommonArrayList.get(position), 0, 0, "0", null);
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println(resultCode);
		Log.e("camera1", imgPath);
		switch (requestCode) {
		case CAMERA_REQUEST_CODE:
			if (resultCode == -1) {
				Intent intentFromCapture = new Intent();
				intentFromCapture.setClass(this, PhotoLView.class);
				intentFromCapture.putExtra("photo_path", SpfsUtil.getString("imgPath"));
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
		sendMessage("", 0, 0, ContentType.IMG, picpath);
	}

	// 发送图片
	private void imagephotoThread() {
		// mLinAdd.setVisibility(View.GONE);

		if (Bimp.drr.size() > 0) {
			String path = "";
			final File[] files = new File[Bimp.drr.size()];
			path = ImageUtil
					.comPressNewPath(Bimp.drr.get(0), String.valueOf(0));
			for (int i = 0; i < Bimp.drr.size(); i++) {
				// files[i] = new File(path);
				sendMessage("", 0, 0, ContentType.IMG, Bimp.drr.get(i));
			}

			Bimp.drr.clear();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back_button:
			SoftInput.hideSoftInputFromWindow(mEdtText.getWindowToken(), 0);// 隐藏键盘
			if (App.getmChatService().getPlaySate().equals(PlayState.Playing)) {
				App.getmChatService().stopSpeech();
			}
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
			mAdd.setVisibility(View.GONE);
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
			if (mRelOther.getVisibility() == View.GONE
					&& mAdd.getVisibility() == View.GONE) {
				hideEmotion();
				hideVoice();
				showOther();
			} else {
				hideOther();
			}
			break;

		case R.id.picture:
			hideOther();
			Intent intent = new Intent(this, PickPhotoActivity.class);
			intent.putExtra("type", "0");
			startActivityForResult(intent, Constant.RESULT_CODE_PIC);
			break;
		case R.id.phrases:
			hideOther();
			mAdd.setVisibility(View.VISIBLE);
			System.out.println("onclick:phrases");
			break;

		case R.id.camera:
			hideOther();
			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// 判断存储卡是否可以用，可用进行存储
			if (UtilMethod.checkSDCard()) {
				Log.e("camera1", MediaStore.EXTRA_OUTPUT);
				SpfsUtil.saveString("imgPath", FileUtil.getTempFileName());
				intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(SpfsUtil.getString("imgPath"))));
			}
			startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
			break;
		case R.id.send:
			final String message = mEdtText.getText().toString();
			sendMessage(message, 0, 0, "0", null);
			break;
		case R.id.list_linear:
			SoftInput.hideSoftInputFromWindow(mEdtText.getWindowToken(), 0);// 隐藏键盘
			mRelFace.setVisibility(View.GONE);
			hideOther();
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
	public static void sendMessage(String message, final int messageStatus,
			final int messageId, String messageType, String url) {
		if (0 == messageStatus) {// 发送新消息
			int speechMessageTime = 0;
			if ("2".equals(messageType)) {
				if (mFileName != null && !"".equals(mFileName)) {
					try {
						App.getmChatService().getmMediaPlayer().reset();
						App.getmChatService().getmMediaPlayer()
								.setDataSource(mFileName);
						App.getmChatService().getmMediaPlayer().prepare();
						speechMessageTime = App.getmChatService()
								.getmMediaPlayer().getDuration() / 1000;
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			final int myid = App.dbHelper.insertMessage(App.db, employeeNo,
					"0", "2", "1", messageType, message, url, new Date(), null,
					speechMessageTime);
			final ChatMessageBean sendMsg = new ChatMessageBean(messageType,
					message, true);
			sendMsg.setMessageSendStatus("2");
			sendMsg.setMessageId(myid);
			// sendMsg.setServerPath(url);
			sendMsg.setPicpath(url);
			sendMsg.setSpeechMessageTime(speechMessageTime);
			mAdapter.addItem(sendMsg);
			mEdtText.getText().clear();
			sendMessageData(message, myid, messageType, url);
		} else if (1 == messageStatus) {// 重新发送
			App.dbHelper.updateMessage(App.db, messageId, "2");
			List<ChatMessageBean> list = App.dbHelper.queryAllMessage(App.db,
					employeeNo, mBean.getImage());
			mAdapter.setItem(list);
			sendMessageData(message, messageId, messageType, url);
		}

	}

	public static void sendMessageData(String message, final int messageId,
			String messageType, String url) {
		JSONObject myjson = new JSONObject();
		myjson.put("source", Kpush.getInstence().getClientId());
		myjson.put("target", mBean.getClientId());
		myjson.put("message_type", messageType);
		myjson.put("content", message);
		String postUrl = "";
		RequestParams params = new RequestParams(); // 默认编码UTF-8
		if (ContentType.TEXT.equals(messageType)) {
			params.addBodyParameter("message", myjson.toString());
			postUrl = Constant.ImSendTextMessage;
		} else if (ContentType.IMG.equals(messageType)
				|| ContentType.FILE.equals(messageType)) {
			params.addBodyParameter("uploadFile", new File(url));
			params.addBodyParameter("message", myjson.toString());
			postUrl = Constant.ImSendImageMessage;
		}
		HttpUtils http = new HttpUtils(20 * 1000);// 超时时间20秒
		http.send(HttpRequest.HttpMethod.POST, postUrl + "234", params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							// resultText.setText("upload: " + current + "/"
							// + total);
							Log.e("msg", "文件上传中...");
							Log.e("msg", "总共大小" + total + ";长传大小" + current);
						} else {
							// resultText.setText("reply: " + current + "/"
							// + total);
							Log.e("msg", "上传完成");
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("code:" + responseInfo.result);
						JSONObject jo = JSONObject
								.parseObject(responseInfo.result);
						if ("0".equals(jo.getString("resCode"))) {
							App.dbHelper.updateMessage(App.db, messageId, "0");
							List<ChatMessageBean> list = App.dbHelper
									.queryAllMessage(App.db, employeeNo,
											mBean.getImage());
							mAdapter.setItem(list);
						} else if ("1".equals(jo.getString("resCode"))) {
							App.dbHelper.updateMessage(App.db, messageId, "1");
							List<ChatMessageBean> list = App.dbHelper
									.queryAllMessage(App.db, employeeNo,
											mBean.getImage());
							mAdapter.setItem(list);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.e("msg", msg);
						App.dbHelper.updateMessage(App.db, messageId, "1");
						List<ChatMessageBean> list = App.dbHelper
								.queryAllMessage(App.db, employeeNo,
										mBean.getImage());
						mAdapter.setItem(list);
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
		if (mAdd.getVisibility() == View.VISIBLE) {
			mAdd.setVisibility(View.GONE);
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
		mFileName = PATH + UUID.randomUUID().toString() + ".amr";
		// 获取SD卡可读写状态
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			System.out.println("SD Card is not mounted,It is  " + state);
		}

		File directory = new File(mFileName).getParentFile();

		if (!directory.exists() && !directory.mkdirs()) {
			System.out.println("Path to file could not be created");
		}
		// ToastUtil.show(MainActivity.this, "开始录音");
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			System.out.println("prepare() failed");
			e.printStackTrace();
		}
		mRecorder.start();


	}

	/** 停止录音 */
	private void stopVoice() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		// ToastUtil.show(MainActivity.this, "保存录音" + mFileName);
	}

	// 录音时显示Dialog
	void showVoiceDialog() {
		if (dialog == null) {
			dialog = new Dialog(MainActivity.this, R.style.DialogStyle);
		}
		if (!dialog.isShowing()) {
			dialog.show();
			dialog.setContentView(R.layout.my_dialog);
			dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
			dialog.setCanceledOnTouchOutside(false);
		}
	}

	// 录音停止时隐藏Dialog
	void hideVoiceDialog() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	// 录音Dialog图片随声音大小切换
	void setDialogImage() {
		if (voiceValue < 500.0) {
			dialog_img.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 500.0 && voiceValue < 1000) {
			dialog_img.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 1000.0 && voiceValue < 1300) {
			dialog_img.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 1300.0 && voiceValue < 1600) {
			dialog_img.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1600.0 && voiceValue < 1900) {
			dialog_img.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 1900.0 && voiceValue < 2200) {
			dialog_img.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 2200.0 && voiceValue < 2500) {
			dialog_img.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 2500.0 && voiceValue < 2800.0) {
			dialog_img.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 2800.0 && voiceValue < 3100.0) {
			dialog_img.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 3100.0 && voiceValue < 3400.0) {
			dialog_img.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue > 3400.0 && voiceValue < 3700.0) {
			dialog_img.setImageResource(R.drawable.record_animate_11);
		} else if (voiceValue > 3700.0 && voiceValue < 4000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue > 4000.0 && voiceValue < 4300.0) {
			dialog_img.setImageResource(R.drawable.record_animate_13);
		} else if (voiceValue > 4300.0) {
			dialog_img.setImageResource(R.drawable.record_animate_14);
		}
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
//						if (RECODE_STATE == RECORD_ING) {
//							if (mRecorder != null) {
//								voiceValue = mRecorder.getMaxAmplitude();
//								Log.e("MainActivity", voiceValue + "");
//								imgHandle.sendEmptyMessage(1);
//							}
//						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	};

	@Override
	protected void onStart() {
		super.onStart();
		MessageManager.getInstance().attach(ib);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// MessageManager.getInstance().detach(ib);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// MessageManager.getInstance().detach(ib);
	}
}

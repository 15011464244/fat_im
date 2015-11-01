package com.newcdc.activity.delivertask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.cdc.DaoFactory;
import com.cn.cdc.DlvStateDao;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.GroupDao;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.service.JXAsyncQueueService;
import com.newcdc.tools.Constant;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.GetBitmap;
import com.newcdc.ui.ProgressDialog;
import com.newcdc.ui.XFAudio;

@SuppressLint("NewApi")
public class DeliverOkActivity extends BaseActivity implements OnClickListener {
	private TextView money;
	private Spinner pay_type;// 邮件类型，签收类型,支付类型
	private Button takephoto, dlvEnsave;
	private Button isback;
	private List<Map<String, String>> typeList = null;
	private String signStsCode = "";
	private DeliverDao deliverDao;
	private byte[] imgs;// 要上传的图片字节
	private ImageView dlvimage;
	private ProgressDialog myDialog = null;
	// 多线程消息控制
	private static final int SUCCESS = 1;
	private static final int ERROR = 2;
	private FileOutputStream b = null;
	private Dialog ivdialog;
	private DlvStateDao dlvStateDao;
	private Bitmap mymap = null;
	private int width;
	private int height;
	private Bitmap bitmap;
	private String imagePath = "";
	private RadioButton benren, taren, danwei;// 本人 他人 单位
	private EditText dlvdanwei, dlvben, dlveta;
	// private UserDao userDao;
	private UserInfoUtils userInfoUtils = null;
	private String picturePatch = Constant.DELIVER_OK;
	private ArrayList<String> stateCodeList;
	private MessageInfoBean bean;// 单个提交的时候需要保存图片
	private Button mail_dlventer_say;
	private ArrayList<MessageInfoBean> mList;
	private int _id;
	private int type = 1;
	private DeliverDaoFactory daoFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mail_dlventer);
		initView();
		initData();
		initListener();// 监听
		if (bean.getPicture() != null && !"".equals(bean.getPicture())) {
			imagePath = bean.getPicture();
			bitmap = BitmapFactory.decodeFile(picturePatch + bean.getPicture());
			dlvimage.setImageBitmap(bitmap);
			dlvimage.setBackground(null);
		}
		// 签收人
		dlvdanwei = (EditText) findViewById(R.id.dlvdanwei);
		dlvben = (EditText) findViewById(R.id.dlvben);
		dlveta = (EditText) findViewById(R.id.dlveta);
		XFAudio xfAudio = new XFAudio(DeliverOkActivity.this,
				mail_dlventer_say, dlveta);
		xfAudio.toSay();
		if (bean.getMoney() != null) {
			money.setText(bean.getMoney() + "");
		} else {
			money.setText("0");
		}
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		daoFactory = DeliverDaoFactory.getInstance();
		// userDao = daoFactory.getUserDao(DeliverOkActivity.this);
		userInfoUtils = new UserInfoUtils(getApplicationContext());
		deliverDao = daoFactory.getDeliverDao(DeliverOkActivity.this);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		height = metric.heightPixels;
		dlvStateDao = new DlvStateDao(this);
		stateCodeList = new ArrayList<String>();
		_id = getIntent().getIntExtra("_id", -1);
		if (getIntent().getExtras() != null) {
			type = getIntent().getExtras().getInt("type");
			Log.e("tag", type + "");
			if (type == 2) {
				benren.setChecked(false);
				taren.setChecked(true);
				danwei.setChecked(false);
			} else if (type == 1) {
				benren.setChecked(true);
				taren.setChecked(false);
				danwei.setChecked(false);
			}

		}
		bean = deliverDao.query_id(_id);
		mList = new ArrayList<MessageInfoBean>();
		mList.add(bean);
		// 支付方式初始化
		List<String> payList = new ArrayList<String>();
		List<Map<String, String>> dlvStateList = DaoFactory.getInstance()
				.getBaseDataDao(getApplicationContext())
				.FindBaseDataByDataFlags("DLV_STS_CODE");
		Log.e("err", dlvStateList.toString());
		for (int i = 0; i < dlvStateList.size(); i++) {
			Map<String, String> map = dlvStateList.get(i);
			stateCodeList.add(map.get("dataKey"));
			if (stateCodeList.size() == 3) {
				break;
			}
		}
		payList.add("现金");
		payList.add("刷卡");
		payList.add("票据");
		ArrayAdapter<String> payAdapter = new ArrayAdapter<String>(
				DeliverOkActivity.this, R.layout.my_spinner, payList);
		payAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pay_type.setAdapter(payAdapter);
	}

	private void initView() {
		money = (TextView) findViewById(R.id.money);// 钱
		pay_type = (Spinner) findViewById(R.id.pay_type);// 支付类型
		dlvEnsave = (Button) findViewById(R.id.dlvEnsave);// 保存
		takephoto = (Button) findViewById(R.id.takephoto);// //////拍照
		isback = (Button) findViewById(R.id.isback);
		dlvimage = (ImageView) findViewById(R.id.dlvimage);// 图片
		mail_dlventer_say = (Button) findViewById(R.id.mail_dlventer_say);
		// 签收方式
		benren = (RadioButton) findViewById(R.id.benren);
		danwei = (RadioButton) findViewById(R.id.danwei);
		taren = (RadioButton) findViewById(R.id.taren);
		// 签收人
		dlvdanwei = (EditText) findViewById(R.id.dlvdanwei);
		dlvben = (EditText) findViewById(R.id.dlvben);
		dlveta = (EditText) findViewById(R.id.dlveta);
	}

	private void initListener() {// 监听
		takephoto.setOnClickListener(new OnClickListener() {
			// 拍照
			@Override
			public void onClick(View v) {
				Intent imageCaptureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(imageCaptureIntent, 1);
			}
		});
		dlvimage.setOnClickListener(this);
		dlvEnsave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				upload();
			}
		});
		isback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DeliverOkActivity.this.finish();
			}
		});

		benren.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					danwei.setChecked(false);
					taren.setChecked(false);
				}
			}
		});

		danwei.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					taren.setChecked(false);
					benren.setChecked(false);
				}
			}
		});
		taren.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					danwei.setChecked(false);
					benren.setChecked(false);
				}
			}
		});
	}

	/**
	 * 更改数据库
	 */
	public void upload() {
		String sts_code = "";
		String signer_name = "";
		String pay_mode = "";
		if (taren.isChecked()) {
			sts_code = stateCodeList.get(1);
			signer_name = dlveta.getText().toString();
		} else if (danwei.isChecked()) {
			sts_code = stateCodeList.get(2);
			signer_name = dlvdanwei.getText().toString();
		} else if (benren.isChecked()) {
			sts_code = stateCodeList.get(0);
			signer_name = dlvben.getText().toString();
		} else {
			sts_code = stateCodeList.get(0);
			signer_name = dlvben.getText().toString();
		}
		if (0 == pay_type.getSelectedItemPosition()) {
			pay_mode = "0";
		} else if (1 == pay_type.getSelectedItemPosition()) {
			pay_mode = "1";
		} else {
			pay_mode = "2";
		}
		bean = deliverDao.query_id(_id);
		bean.setPicture(imagePath);
		DeliverQueueBean queueBean = DeliverQueueBean.createBean(bean,
				sts_code, bean.getMoney() + "", "", "", signer_name, pay_mode,
				Constant.TUOTOU, getApplicationContext());
		ArrayList<DeliverQueueBean> queueList = new ArrayList<DeliverQueueBean>();
		queueList.add(queueBean);
		daoFactory.getQueueDao(DeliverOkActivity.this).insert(queueList,
				DeliverOkActivity.this);
		deliverDao.updateListMailDealResult(Utils.parseBeanToIdList(mList),
				Constant.TUOTOU);
		GroupDao groupDao = daoFactory.getGroupDao(DeliverOkActivity.this);
		groupDao.updateDealMailCount(DeliverOkActivity.this);
		startService(new Intent(DeliverOkActivity.this, JXAsyncQueueService.class));
		Toast.makeText(this, "操作成功", Toast.LENGTH_SHORT).show();
		this.finish();
	}

	private void reViewImage() {
		if (ivdialog == null) {
			ivdialog = new Dialog(DeliverOkActivity.this, R.style.dialogss);
		}
		View v = LayoutInflater.from(DeliverOkActivity.this).inflate(
				R.layout.dialog_image_review, null);
		ivdialog.setContentView(v, new LayoutParams(width, height));
		ImageView review_image = (ImageView) v.findViewById(R.id.review_image);
		bitmap = BitmapFactory.decodeFile(picturePatch + imagePath);
		bitmap = GetBitmap.big(bitmap);
		review_image.setImageBitmap(bitmap);
		dlvimage.setImageBitmap(bitmap);
		review_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ivdialog.dismiss();
			}
		});
		ivdialog.show();
	}

	/**
	 * 拍照返回操作
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Toast.makeText(this,
						"SD card is not avaiable and writeable right now.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Bundle bundle = data.getExtras();
			File file = new File(picturePatch);
			file.mkdirs();
			bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			String savefile = picturePatch + "s" + bean.getMail_num() + ".png";
			try {
				b = new FileOutputStream(savefile);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
				dlvimage.setImageBitmap(bitmap);
				dlvimage.setBackground(null);
				deliverDao.saveMailPicture(bean.get_id(),
						"s" + bean.getMail_num() + ".png");// 保存图片
				imagePath = "s" + bean.getMail_num() + ".png";
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != b) {
						b.flush();
						b.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mymap = null;
		imgs = null;
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.dlvimage:
			if (bitmap != null) {
				reViewImage();
			} else {
				Intent imageCaptureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(imageCaptureIntent, 1);
			}
			break;

		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		Utils.startIntentService(DeliverOkActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

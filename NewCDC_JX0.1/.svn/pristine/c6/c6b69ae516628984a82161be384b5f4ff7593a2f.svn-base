package com.newcdc.activity.delivertask;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.newcdc.tools.Constant;
import com.newcdc.tools.DealDeliverTools;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;
import com.newcdc.ui.XFAudio;

@SuppressLint("NewApi")
public class DeliverOkListActivity extends BaseActivity {
	private Spinner pay_type;// 邮件类型，签收类型,支付类型
	private Button takephoto, dlvEnsave, mail_dlventer_say;
	private Button isback;
	private String signStsCode = "";
	private ArrayList<MessageInfoBean> mList;
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
//	private UserDao userDao;
	private String picturePatch = Constant.DELIVER_OK;
	private ArrayList<String> stateCodeList;
	private int type = 1;
	private DeliverDaoFactory daoFactory;
private UserInfoUtils userInfoUtils = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mail_dlventer);
		initView();
		initData();
		initListener();// 监听
		takephoto.setVisibility(View.GONE);
		dlvimage.setVisibility(View.GONE);
		XFAudio xfAudio = new XFAudio(DeliverOkListActivity.this,
				mail_dlventer_say, dlveta);
		xfAudio.toSay();
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		daoFactory = DeliverDaoFactory.getInstance();
		userInfoUtils = new UserInfoUtils(getApplicationContext());
//		userDao = daoFactory.getUserDao(DeliverOkListActivity.this);
		deliverDao = daoFactory.getDeliverDao(DeliverOkListActivity.this);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		height = metric.heightPixels;
		dlvStateDao = new DlvStateDao(this);
		stateCodeList = new ArrayList<String>();
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
				DeliverOkListActivity.this, R.layout.my_spinner, payList);
		payAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pay_type.setAdapter(payAdapter);
	}

	private void initView() {
		findViewById(R.id.shoukuang).setVisibility(View.GONE);
		pay_type = (Spinner) findViewById(R.id.pay_type);// 支付类型
		dlvEnsave = (Button) findViewById(R.id.dlvEnsave);// 保存
		takephoto = (Button) findViewById(R.id.takephoto);// //////拍照
		isback = (Button) findViewById(R.id.isback);
		dlvimage = (ImageView) findViewById(R.id.dlvimage);// 图片
		// 签收方式
		benren = (RadioButton) findViewById(R.id.benren);
		danwei = (RadioButton) findViewById(R.id.danwei);
		taren = (RadioButton) findViewById(R.id.taren);
		// 签收人
		dlvdanwei = (EditText) findViewById(R.id.dlvdanwei);
		dlvben = (EditText) findViewById(R.id.dlvben);
		dlveta = (EditText) findViewById(R.id.dlveta);
		mail_dlventer_say = (Button) findViewById(R.id.mail_dlventer_say);
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
		dlvEnsave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				upload();
			}
		});
		isback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DeliverOkListActivity.this.finish();
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
		final ProgressDialog progressDialog = new ProgressDialog(
				DeliverOkListActivity.this, "正在处理");
		progressDialog.setCanCalcelable(false);
		progressDialog.toShow();
		final Handler handler = new Handler();
		final String final_sts_code = sts_code;
		final String final_signer_name = signer_name;
		final String final_pay_mode = pay_mode;
		new Thread() {
			@Override
			public void run() {
				String groupName = getIntent().getStringExtra("groupName");
				String queryInfo = getIntent().getStringExtra("queryInfo");
				boolean fromMany = getIntent().getBooleanExtra("fromMany",
						false);
				String frequence = getIntent().getStringExtra("frequence");
				if (fromMany) {// 查处自定义组的全部邮件
					mList = deliverDao.querySelfGroupMail();
				} else if (!Utils.stringEmpty(queryInfo)) {// 如果是具体条件查询进入，则根据条件筛选
					mList = (ArrayList<MessageInfoBean>) deliverDao
							.queryInGroup_m(groupName, queryInfo, frequence,
									DeliverOkListActivity.this);
				} else {// 查出分组内全部的邮件
					mList = (ArrayList<MessageInfoBean>) deliverDao
							.queryForGroupNotTuotou(groupName, frequence,
									DeliverOkListActivity.this);
				}
				Log.e("list", mList.size() + "");
				Log.e("list", mList.toString());
				// 转化为投递表list
				ArrayList<DeliverQueueBean> queueList = DeliverQueueBean
						.createBeanList(mList, final_sts_code, "", "",
								final_signer_name, final_pay_mode,
								Constant.TUOTOU, getApplicationContext());
				// 更新下端表为妥投状态
				deliverDao.updateListMailDealResult(
						Utils.parseBeanToIdList(mList), Constant.TUOTOU);
				// 更新分组表
				GroupDao groupDao = daoFactory
						.getGroupDao(DeliverOkListActivity.this);
				groupDao.updateDealMailCount(DeliverOkListActivity.this);
				// 将list在自定义组中抛出
				deliverDao.updateOutSelfGroup(Utils.parseBeanToIdList(mList));
				// 将list存入投递表，并启动提交服务

				DealDeliverTools.startServiceUserInput(queueList,
						DeliverOkListActivity.this.getApplicationContext());
				handler.post(new Runnable() {

					@Override
					public void run() {
						progressDialog.toDimiss();
						Toast.makeText(DeliverOkListActivity.this, "操作成功",
								Toast.LENGTH_SHORT).show();
						finish();
					}
				});
			};
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mymap = null;
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(DeliverOkListActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

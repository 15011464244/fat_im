package com.newcdc.activity.delivertask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.cn.cdc.DaoFactory;
import com.cn.cdc.MySpinnerAdapter;
import com.newcdc.R;
import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.GroupDao;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.service.JXAsyncQueueService;
import com.newcdc.tools.Constant;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;

/**
 * 未妥投录入
 */
public class DeliverErrorActivity extends Activity {
	private Spinner sp_reason, sp_nextStep;
	private Button btn_save;
	private Button nowback;
	private String mailNumber;
	private DeliverDao dao;
	private List<String> arraylist;
	private List<Map<String, String>> typeList = null;
	private String orgCode;
	private List<Map<String, String>> fllowtypeList = null;
	private String mailType = "0", undlvCauseCode = "", undlvNextModeCode = "";// 邮件类型
	private ProgressDialog myDialog = null;
	// 多线程消息控制
	private static final int SUCCESS = 1;
	private static final int ERROR = 2;
	// private UserDao userDao;
	private String frequence;
	private String money;
	private String dlv_pseg_code;
	private MessageInfoBean bean;
	private DeliverDaoFactory daoFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mail_dlvnenter);
		initView();
		initData();
		initListener();

	}

	private void initData() {
		int _id = getIntent().getIntExtra("_id", -1);
		daoFactory = DeliverDaoFactory.getInstance();
		// userDao = daoFactory.getUserDao(DeliverErrorActivity.this);
		UserInfoUtils userInfoUtils = new UserInfoUtils(getApplicationContext());
		dao = daoFactory.getDeliverDao(DeliverErrorActivity.this);
		bean = dao.query_id(_id);
		Cursor cursor = dao.queryBy_id_Detail(_id);
		cursor.moveToNext();
		mailNumber = cursor.getString(cursor.getColumnIndex("mail_num"));
		frequence = cursor.getString(cursor.getColumnIndex("frequence"));
		dlv_pseg_code = cursor
				.getString(cursor.getColumnIndex("dlv_pseg_code"));
		money = cursor.getString(cursor.getColumnIndex("money"));
		String telnum = cursor.getString(cursor
				.getColumnIndex("sender_contact_phone1"));// 电话

		String ren = cursor.getString(cursor.getColumnIndex("sender_name"));// 发件人

		typeList = DaoFactory.getInstance()
				.getBaseDataDao(getApplicationContext())
				.FindBaseDataByDataFlags("UNDLV_CAUSE_CODE");// 未妥投原因
		arraylist = new ArrayList<String>();
		for (Map<String, String> map : typeList) {
			String xk = map.get("dataValue");
			arraylist.add(xk);
		}

		MySpinnerAdapter<String> adad = new MySpinnerAdapter<String>(this,
				R.layout.my_spinner, arraylist);
		adad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_reason.setAdapter(adad);
		sp_reason.setSelection(5);

	}

	private void initNextStepSpinner(String standardCode, String fllow) {
		int position = 0;// TODO.
		UserInfoUtils userInfoUtils = new UserInfoUtils(getApplicationContext());
		fllowtypeList =DaoFactory.getInstance()
				.getBaseDataDao(getApplicationContext())
				.FindBaseDataByDataFlags("NEXT_ACTN_CODE");// 下一步动作
		List<String> fllowarraylists = new ArrayList<String>();
		for (int i = 0; i < fllowtypeList.size(); i++) {
			Map<String, String> map = fllowtypeList.get(i);
			String fllowstr = map.get("dataValue");
			fllowarraylists.add(fllowstr);
			if (fllow.equals(map.get("dataKey"))) { //
				position = i;
			}
		}
		// 下一步动作spinner数据源
		MySpinnerAdapter<String> fllowtypeadapter = new MySpinnerAdapter<String>(
				this, R.layout.my_spinner, fllowarraylists);
		fllowtypeadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_nextStep.setAdapter(fllowtypeadapter);
		sp_nextStep
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						undlvNextModeCode = fllowtypeList.get(arg2).get(
								"dataKey");
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});
		sp_nextStep.setSelection(position, true);
	}

	private void initListener() {
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// addupdate();
					String money = bean.getMoney();
					DeliverQueueBean queueBean = DeliverQueueBean
							.createBean(bean, "", "", undlvCauseCode,
									undlvNextModeCode, "", "",
									Constant.WEITUOTOU, getApplicationContext());
					ArrayList<DeliverQueueBean> queueList = new ArrayList<DeliverQueueBean>();
					queueList.add(queueBean);
					daoFactory.getQueueDao(DeliverErrorActivity.this).insert(
							queueList, DeliverErrorActivity.this);
					ArrayList<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
					list.add(bean);
					dao.updateListMailDealResult(Utils.parseBeanToIdList(list),
							Constant.WEITUOTOU);
					GroupDao groupDao = daoFactory
							.getGroupDao(DeliverErrorActivity.this);
					groupDao.updateDealMailCount(DeliverErrorActivity.this);
					startService(new Intent(DeliverErrorActivity.this,
							JXAsyncQueueService.class));
					Toast.makeText(DeliverErrorActivity.this, "操作成功",
							Toast.LENGTH_SHORT).show();
					DeliverErrorActivity.this.finish();
				} catch (Exception e) {
				}
			}
		});
		sp_reason.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				undlvCauseCode = typeList.get(position).get("dataKey");//
				initNextStepSpinner(typeList.get(position).get("dataKey"),
						typeList.get(position).get("dataKey"));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		nowback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DeliverErrorActivity.this.finish();
			}
		});
	}

	private void initView() {
		sp_nextStep = (Spinner) findViewById(R.id.sp_nextStep_err);
		sp_reason = (Spinner) findViewById(R.id.sp_reason_err);
		btn_save = (Button) findViewById(R.id.btn_save_err);
		nowback = (Button) findViewById(R.id.nowback);
	}

	@Override
	protected void onResume() {
		Utils.startIntentService(DeliverErrorActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

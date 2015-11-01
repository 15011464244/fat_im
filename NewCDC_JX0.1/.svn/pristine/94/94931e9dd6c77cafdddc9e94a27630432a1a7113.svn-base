package com.newcdc.activity.delivertask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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
import com.newcdc.tools.Constant;
import com.newcdc.tools.DealDeliverTools;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;
import com.newcdc.ui.ProgressDialog;

/**
 * 未妥投录入
 */
public class DeliverErrorListActivity extends Activity {
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
	private DeliverDaoFactory daoFactory;
	private UserInfoUtils userInfoUtils = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mail_dlvnenter);
		initView();
		initData();
		initListener();

	}

	@SuppressWarnings("unchecked")
	private void initData() {
		daoFactory = DeliverDaoFactory.getInstance();
		userInfoUtils = new UserInfoUtils(getApplicationContext());
		// userDao = daoFactory.getUserDao(DeliverErrorListActivity.this);
		dao = daoFactory.getDeliverDao(DeliverErrorListActivity.this);
		typeList = DaoFactory.getInstance()
				.getBaseDataDao(getApplicationContext())
				.FindBaseDataByDataFlags("UNDLV_CAUSE_CODE");
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
		int position = 0;
		fllowtypeList = DaoFactory.getInstance()
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
				// ArrayList<DeliverQueueBean> queueList = new
				// ArrayList<DeliverQueueBean>();
				// for (int i = 0; i < mList.size(); i++) {
				// DeliverQueueBean queueBean = DeliverQueueBean.createBean(
				// mList.get(i), "", "", undlvCauseCode,
				// undlvNextModeCode, "", "", Constant.WEITUOTOU,
				// DeliverErrorListActivity.this);
				// queueList.add(queueBean);
				// }

				final ProgressDialog progressDialog = new ProgressDialog(
						DeliverErrorListActivity.this, "正在处理");
				progressDialog.setCanCalcelable(false);
				progressDialog.toShow();
				final Handler handler = new Handler();
				new Thread() {
					@Override
					public void run() {
						String groupName = getIntent().getStringExtra(
								"groupName");
						String queryInfo = getIntent().getStringExtra(
								"queryInfo");
						boolean fromMany = getIntent().getBooleanExtra(
								"fromMany", false);
						String frequence = getIntent().getStringExtra(
								"frequence");
						List<MessageInfoBean> mList = new ArrayList<MessageInfoBean>();
						if (fromMany) {
							mList = dao.querySelfGroupMail();
						} else if (!Utils.stringEmpty(queryInfo)) {// 如果是具体条件查询进入，则根据条件筛选
							mList = (ArrayList<MessageInfoBean>) dao
									.queryInGroup_m(groupName, frequence,
											queryInfo,
											DeliverErrorListActivity.this);
						} else {
							mList = dao.queryForGroupNotTuotou(groupName,
									frequence, DeliverErrorListActivity.this);
						}
						final ArrayList<DeliverQueueBean> queueList = DeliverQueueBean
								.createBeanList(
										(ArrayList<MessageInfoBean>) mList, "",
										undlvCauseCode, undlvNextModeCode, "",
										"", Constant.WEITUOTOU,
										getApplicationContext());
						dao.updateListMailDealResult(
								Utils.parseBeanToIdList(mList),
								Constant.WEITUOTOU);
						dao.updateOutSelfGroup(Utils.parseBeanToIdList(mList));
						GroupDao groupDao = daoFactory
								.getGroupDao(DeliverErrorListActivity.this);
						groupDao.updateDealMailCount(DeliverErrorListActivity.this);
						DealDeliverTools.startServiceUserInput(queueList,
								DeliverErrorListActivity.this);
						handler.post(new Runnable() {

							@Override
							public void run() {
								progressDialog.toDimiss();
								Toast.makeText(DeliverErrorListActivity.this,
										"操作成功", Toast.LENGTH_SHORT).show();
								DeliverErrorListActivity.this.finish();
							}
						});
					};
				}.start();
			}
		});
		sp_reason.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				undlvCauseCode = typeList.get(position).get("dataKey");//
				initNextStepSpinner(typeList.get(position).get("dataKey"),
						typeList.get(position).get("dataKey"));//
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		nowback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DeliverErrorListActivity.this.finish();
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
		Utils.startIntentService(DeliverErrorListActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

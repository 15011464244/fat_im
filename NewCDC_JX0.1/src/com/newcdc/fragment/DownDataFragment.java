package com.newcdc.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.cdc.BaseData;
import com.cn.cdc.BaseDataDao;
import com.cn.cdc.DaoFactory;
import com.cn.cdc.DlvStateDao;
import com.cn.cdc.FollowActionDao;
import com.cn.cdc.StandardCode;
import com.cn.cdc.StateFollowDao;
import com.lidroid.xutils.util.LogUtils;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.application.MainActivity;
import com.newcdc.db.CustomerDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.ExtraCastDao;
import com.newcdc.model.CustomerBean;
import com.newcdc.model.ExtraCast;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.ui.ProgressDialog;

/**
 * 手动下载基础数据
 * 
 * @author liunannan 更新于2015-6-24
 * 
 */
public class DownDataFragment extends Fragment implements OnClickListener {
	private View mView;
	private Button btn_back;
	private BaseDataDao baseDataDao = null;
	private TextView tv_NEXT_ACTN_CODE;// 下一步动作数据下载
	private TextView tv_MAIL_NOTE_CODE;// 邮件备注
	private TextView tv_DLV_STS_CODE;// 投递备注
	private TextView tv_TURN_BACK_CAUSE_CODE;// 转退原因
	private TextView tv_UNDLV_CAUSE_CODE;// 未妥投原因数据下载
	private TextView tv_downdata_all;// 全部下载
	private Context context;
	private String orgCode = null;
	private String userName;
	private boolean downdata_all = false;// 是否是下载全部
	private boolean downdata_NEXT_ACTN_CODE = false;// 是否是下载完成
	private boolean downdata_MAIL_NOTE_CODE = false;// 是否是下载完成
	private boolean downdata_DLV_STS_CODE = false;// 是否是下载完成
	private boolean downdata_TURN_BACK_CAUSE_CODE = false;// 是否是下载完成
	private boolean downdata_UNDLV_CAUSE_CODE = false;// 是否是下载完成
	ProgressDialog dialog = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_downdata, container, false);
		context = getActivity();
		initView();
		initData();
		addListener();
		return mView;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			// switch (message.what) {
			// case 1:
			// downOk();
			// break;
			// case 2:
			//
			// break;
			//
			// default:
			// break;
			// }
			downOk();
		}
	};

	private void downOk() {
		if (downdata_all) {
			if (downdata_NEXT_ACTN_CODE && downdata_MAIL_NOTE_CODE
					&& downdata_DLV_STS_CODE && downdata_TURN_BACK_CAUSE_CODE
					&& downdata_UNDLV_CAUSE_CODE) {
				tv_NEXT_ACTN_CODE.setText("重新下载");
				tv_MAIL_NOTE_CODE.setText("重新下载");
				tv_DLV_STS_CODE.setText("重新下载");
				tv_TURN_BACK_CAUSE_CODE.setText("重新下载");
				tv_UNDLV_CAUSE_CODE.setText("重新下载");
				dimissDialog();
			}
		} else {
			if (downdata_NEXT_ACTN_CODE) {
				tv_NEXT_ACTN_CODE.setText("重新下载");
			} else if (downdata_MAIL_NOTE_CODE) {
				tv_MAIL_NOTE_CODE.setText("重新下载");
			} else if (downdata_DLV_STS_CODE) {
				tv_DLV_STS_CODE.setText("重新下载");
			} else if (downdata_TURN_BACK_CAUSE_CODE) {
				tv_TURN_BACK_CAUSE_CODE.setText("重新下载");
			} else if (downdata_UNDLV_CAUSE_CODE) {
				tv_UNDLV_CAUSE_CODE.setText("重新下载");
			}
			dimissDialog();
		}
	}

	private void dimissDialog() {
		if (dialog != null) {
			dialog.toDimiss();
		}
	}

	/**
	 * 初始化变量
	 */
	private void initData() {
		UserInfoUtils user = new UserInfoUtils(context);
		baseDataDao = DaoFactory.getInstance().getBaseDataDao(context);
		orgCode = user.getUserDelvorgCode();
		userName = user.getUserName();
	}

	/**
	 * 注册监听器
	 */
	private void addListener() {
		btn_back.setOnClickListener(this);
		tv_NEXT_ACTN_CODE.setOnClickListener(this);
		tv_DLV_STS_CODE.setOnClickListener(this);
		tv_MAIL_NOTE_CODE.setOnClickListener(this);
		tv_TURN_BACK_CAUSE_CODE.setOnClickListener(this);
		tv_UNDLV_CAUSE_CODE.setOnClickListener(this);
		tv_downdata_all.setOnClickListener(this);
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		btn_back = (Button) mView.findViewById(R.id.btn_back_fragment_deliver);
		tv_NEXT_ACTN_CODE = (TextView) mView
				.findViewById(R.id.tv_NEXT_ACTN_CODE);
		tv_MAIL_NOTE_CODE = (TextView) mView
				.findViewById(R.id.tv_MAIL_NOTE_CODE);
		tv_DLV_STS_CODE = (TextView) mView.findViewById(R.id.tv_DLV_STS_CODE);
		tv_TURN_BACK_CAUSE_CODE = (TextView) mView
				.findViewById(R.id.tv_TURN_BACK_CAUSE_CODE);
		tv_UNDLV_CAUSE_CODE = (TextView) mView
				.findViewById(R.id.tv_UNDLV_CAUSE_CODE);
		tv_downdata_all = (TextView) mView.findViewById(R.id.tv_downdata_all);
		dialog = new ProgressDialog(context, "正在下载...");
		dialog.setCanCalcelable(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_fragment_deliver:// 返回键
			MainActivity activity = (MainActivity) context;
			activity.switchContentFragment(6);
			break;
		case R.id.tv_NEXT_ACTN_CODE:
			if (!dialog.isShowing()) {
				dialog.toShow();
			}
			downdata_all = false;
			downdata_DLV_STS_CODE = false;
			downdata_MAIL_NOTE_CODE = false;
			downdata_TURN_BACK_CAUSE_CODE = false;
			downdata_UNDLV_CAUSE_CODE = false;
			// 下一步动作数据下载
			downNEXT_ACTN_CODE();
			break;
		case R.id.tv_MAIL_NOTE_CODE:
			if (!dialog.isShowing()) {
				dialog.toShow();
			}
			downdata_all = false;
			downdata_DLV_STS_CODE = false;
			downdata_TURN_BACK_CAUSE_CODE = false;
			downdata_UNDLV_CAUSE_CODE = false;
			downdata_NEXT_ACTN_CODE = false;
			downMAIL_NOTE_CODE();
			break;
		case R.id.tv_DLV_STS_CODE:
			if (!dialog.isShowing()) {
				dialog.toShow();
			}
			downdata_all = false;
			downdata_TURN_BACK_CAUSE_CODE = false;
			downdata_UNDLV_CAUSE_CODE = false;
			downdata_NEXT_ACTN_CODE = false;
			downdata_MAIL_NOTE_CODE = false;
			downDLV_STS_CODE();
			break;
		case R.id.tv_TURN_BACK_CAUSE_CODE:
			if (!dialog.isShowing()) {
				dialog.toShow();
			}
			downdata_all = false;
			downdata_UNDLV_CAUSE_CODE = false;
			downdata_NEXT_ACTN_CODE = false;
			downdata_MAIL_NOTE_CODE = false;
			downdata_DLV_STS_CODE = false;
			downTURN_BACK_CAUSE_CODE();
			break;
		case R.id.tv_UNDLV_CAUSE_CODE:
			if (!dialog.isShowing()) {
				dialog.toShow();
			}
			downdata_all = false;
			downdata_DLV_STS_CODE = false;
			downdata_NEXT_ACTN_CODE = false;
			downdata_MAIL_NOTE_CODE = false;
			downdata_TURN_BACK_CAUSE_CODE = false;
			downUNDLV_CAUSE_CODE();
			break;
		case R.id.tv_downdata_all:
			downdata_all = true;
			downdata_DLV_STS_CODE = false;
			downdata_NEXT_ACTN_CODE = false;
			downdata_MAIL_NOTE_CODE = false;
			downdata_TURN_BACK_CAUSE_CODE = false;
			downdata_UNDLV_CAUSE_CODE = false;
			if (!dialog.isShowing()) {
				dialog.toShow();
			}
			downloadAllBaseData();
			break;
		}
	}

	private void downNEXT_ACTN_CODE() {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					JSONArray jsonArray = new JSONArray();
					jsonObj = new JSONObject();
					jsonObj.put("orgCode", orgCode);
					jsonObj.put("userCode", userName);
					jsonArray.put("NEXT_ACTN_CODE");
					jsonObj.put("dataFlags", jsonArray);
					String result = NetHelper
							.doPostJson(Global.BASEDATADOWNLOAD,
									jsonObj.toString(), "json");
					if (result == null || "请求服务器失败".equals(result)) {
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
						// if (count < 5) {
						// count++;
						// downloadBaseData();
						// }
					} else if (BaseActivity.resState(result).equals("0")) {
					} else {
						baseDataDao.deleteBaseDataByDataFlags("NEXT_ACTN_CODE");
						saveNEXT_ACTN_CODE(result);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonObj;
			}
		}.execute();
	}

	private void downMAIL_NOTE_CODE() {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					JSONArray jsonArray = new JSONArray();
					jsonObj = new JSONObject();
					jsonObj.put("orgCode", orgCode);
					jsonObj.put("userCode", userName);
					jsonArray.put("MAIL_NOTE_CODE");
					jsonObj.put("dataFlags", jsonArray);
					String result = NetHelper
							.doPostJson(Global.BASEDATADOWNLOAD,
									jsonObj.toString(), "json");
					if (result == null || "请求服务器失败".equals(result)) {
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
						// if (count < 5) {
						// count++;
						// downloadBaseData();
						// }
					} else if (BaseActivity.resState(result).equals("0")) {
					} else {
						baseDataDao.deleteBaseDataByDataFlags("MAIL_NOTE_CODE");
						saveMAIL_NOTE_CODE(result);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonObj;
			}
		}.execute();
	}

	private void downDLV_STS_CODE() {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					JSONArray jsonArray = new JSONArray();
					jsonObj = new JSONObject();
					jsonObj.put("orgCode", orgCode);
					jsonObj.put("userCode", userName);
					jsonArray.put("DLV_STS_CODE");
					jsonObj.put("dataFlags", jsonArray);
					String result = NetHelper
							.doPostJson(Global.BASEDATADOWNLOAD,
									jsonObj.toString(), "json");
					if (result == null || "请求服务器失败".equals(result)) {
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
						// if (count < 5) {
						// count++;
						// downloadBaseData();
						// }
					} else if (BaseActivity.resState(result).equals("0")) {
					} else {
						baseDataDao.deleteBaseDataByDataFlags("DLV_STS_CODE");
						saveDLV_STS_CODE(result);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonObj;
			}
		}.execute();
	}

	private void downTURN_BACK_CAUSE_CODE() {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					JSONArray jsonArray = new JSONArray();
					jsonObj = new JSONObject();
					jsonObj.put("orgCode", orgCode);
					jsonObj.put("userCode", userName);
					jsonArray.put("TURN_BACK_CAUSE_CODE");
					jsonObj.put("dataFlags", jsonArray);
					String result = NetHelper
							.doPostJson(Global.BASEDATADOWNLOAD,
									jsonObj.toString(), "json");
					if (result == null || "请求服务器失败".equals(result)) {
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
						// if (count < 5) {
						// count++;
						// downloadBaseData();
						// }
					} else if (BaseActivity.resState(result).equals("0")) {
					} else {
						baseDataDao
								.deleteBaseDataByDataFlags("TURN_BACK_CAUSE_CODE");
						saveTURN_BACK_CAUSE_CODE(result);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonObj;
			}
		}.execute();
	}

	private void downUNDLV_CAUSE_CODE() {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					JSONArray jsonArray = new JSONArray();
					jsonObj = new JSONObject();
					jsonObj.put("orgCode", orgCode);
					jsonObj.put("userCode", userName);
					jsonArray.put("UNDLV_CAUSE_CODE");
					jsonObj.put("dataFlags", jsonArray);
					String result = NetHelper
							.doPostJson(Global.BASEDATADOWNLOAD,
									jsonObj.toString(), "json");
					if (result == null || "请求服务器失败".equals(result)) {
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
						// if (count < 5) {
						// count++;
						// downloadBaseData();
						// }
					} else if (BaseActivity.resState(result).equals("0")) {
					} else {
						baseDataDao
								.deleteBaseDataByDataFlags("UNDLV_CAUSE_CODE");
						saveUNDLV_CAUSE_CODE(result);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonObj;
			}
		}.execute();

	}

	private void downloadAllBaseData() {
		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... arg0) {
				JSONObject jsonObj = null;
				try {
					JSONArray jsonArray = new JSONArray();
					jsonObj = new JSONObject();
					jsonObj.put("orgCode", orgCode);
					jsonObj.put("userCode", userName);
					jsonArray.put("NEXT_ACTN_CODE");
					jsonArray.put("MAIL_NOTE_CODE");
					jsonArray.put("DLV_STS_CODE");
					jsonArray.put("TURN_BACK_CAUSE_CODE");
					jsonArray.put("UNDLV_CAUSE_CODE");
					jsonObj.put("dataFlags", jsonArray);
					String result = NetHelper
							.doPostJson(Global.BASEDATADOWNLOAD,
									jsonObj.toString(), "json");
					if (result == null || "请求服务器失败".equals(result)) {
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
					} else if (BaseActivity.resState(result).equals("0")) {
					} else {
						baseDataDao.DeleteData();
						saveDaseData(result);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonObj;
			}
		}.execute();
	}

	protected void saveDaseData(String result) {
		JSONObject jsonObj;
		JSONObject jsonObjdataList;
		JSONArray jsonObj_NEXT_ACTN_CODE;
		JSONArray jsonObj_MAIL_NOTE_CODE;
		JSONArray jsonObj_DLV_STS_CODE;
		JSONArray jsonObj_UNDLV_CAUSE_CODE;
		JSONArray jsonObj_TURN_BACK_CAUSE_CODE;
		try {
			jsonObj = new JSONObject(result);
			jsonObjdataList = jsonObj.getJSONObject("dataList");
			jsonObj_NEXT_ACTN_CODE = jsonObjdataList
					.getJSONArray("NEXT_ACTN_CODE");
			jsonObj_MAIL_NOTE_CODE = jsonObjdataList
					.getJSONArray("MAIL_NOTE_CODE");
			jsonObj_DLV_STS_CODE = jsonObjdataList.getJSONArray("DLV_STS_CODE");
			jsonObj_UNDLV_CAUSE_CODE = jsonObjdataList
					.getJSONArray("UNDLV_CAUSE_CODE");
			jsonObj_TURN_BACK_CAUSE_CODE = jsonObjdataList
					.getJSONArray("TURN_BACK_CAUSE_CODE");
			int NEXT_ACTN_CODESize = jsonObj_NEXT_ACTN_CODE.length();
			int MAIL_NOTE_CODESize = jsonObj_MAIL_NOTE_CODE.length();
			int DLV_STS_CODESize = jsonObj_DLV_STS_CODE.length();
			int UNDLV_CAUSE_CODESize = jsonObj_UNDLV_CAUSE_CODE.length();
			int TURN_BACK_CAUSE_CODESize = jsonObj_TURN_BACK_CAUSE_CODE
					.length();
			List<BaseData> NEXT_ACTN_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < NEXT_ACTN_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("NEXT_ACTN_CODE");
				bean.setDataKey(jsonObj_NEXT_ACTN_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_NEXT_ACTN_CODE.getJSONObject(i)
						.getString("dataValue"));
				NEXT_ACTN_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(NEXT_ACTN_CODElist, "NEXT_ACTN_CODE");

			List<BaseData> MAIL_NOTE_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < MAIL_NOTE_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("MAIL_NOTE_CODE");
				bean.setDataKey(jsonObj_MAIL_NOTE_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_MAIL_NOTE_CODE.getJSONObject(i)
						.getString("dataValue"));
				MAIL_NOTE_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(MAIL_NOTE_CODElist, "MAIL_NOTE_CODE");

			List<BaseData> DLV_STS_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < DLV_STS_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("DLV_STS_CODE");
				bean.setDataKey(jsonObj_DLV_STS_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_DLV_STS_CODE.getJSONObject(i)
						.getString("dataValue"));
				DLV_STS_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(DLV_STS_CODElist, "DLV_STS_CODE");

			List<BaseData> UNDLV_CAUSE_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < UNDLV_CAUSE_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("UNDLV_CAUSE_CODE");
				bean.setDataKey(jsonObj_UNDLV_CAUSE_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_UNDLV_CAUSE_CODE.getJSONObject(i)
						.getString("dataValue"));
				UNDLV_CAUSE_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(UNDLV_CAUSE_CODElist, "UNDLV_CAUSE_CODE");

			List<BaseData> TURN_BACK_CAUSE_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < TURN_BACK_CAUSE_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("TURN_BACK_CAUSE_CODE");
				bean.setDataKey(jsonObj_TURN_BACK_CAUSE_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_TURN_BACK_CAUSE_CODE.getJSONObject(i)
						.getString("dataValue"));
				TURN_BACK_CAUSE_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(TURN_BACK_CAUSE_CODElist,
					"TURN_BACK_CAUSE_CODE");
			
			downdata_NEXT_ACTN_CODE = true;
			downdata_MAIL_NOTE_CODE = true;
			downdata_UNDLV_CAUSE_CODE = true;
			downdata_DLV_STS_CODE = true;
			downdata_TURN_BACK_CAUSE_CODE = true;
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);

			LogUtils.e(NEXT_ACTN_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("NEXT_ACTN_CODE")
							.size());
			LogUtils.e(MAIL_NOTE_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("MAIL_NOTE_CODE")
							.size());
			LogUtils.e(DLV_STS_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("DLV_STS_CODE")
							.size());
			LogUtils.e(UNDLV_CAUSE_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("UNDLV_CAUSE_CODE")
							.size());
			LogUtils.e(TURN_BACK_CAUSE_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags(
							"TURN_BACK_CAUSE_CODE").size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void saveNEXT_ACTN_CODE(String result) {
		JSONObject jsonObj;
		JSONObject jsonObjdataList;
		JSONArray jsonObj_NEXT_ACTN_CODE;
		try {
			jsonObj = new JSONObject(result);
			jsonObjdataList = jsonObj.getJSONObject("dataList");
			jsonObj_NEXT_ACTN_CODE = jsonObjdataList
					.getJSONArray("NEXT_ACTN_CODE");
			int NEXT_ACTN_CODESize = jsonObj_NEXT_ACTN_CODE.length();
			List<BaseData> NEXT_ACTN_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < NEXT_ACTN_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("NEXT_ACTN_CODE");
				bean.setDataKey(jsonObj_NEXT_ACTN_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_NEXT_ACTN_CODE.getJSONObject(i)
						.getString("dataValue"));
				NEXT_ACTN_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(NEXT_ACTN_CODElist, "NEXT_ACTN_CODE");
			downdata_NEXT_ACTN_CODE = true;
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			LogUtils.e(NEXT_ACTN_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("NEXT_ACTN_CODE")
							.size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void saveMAIL_NOTE_CODE(String result) {
		JSONObject jsonObj;
		JSONObject jsonObjdataList;
		JSONArray jsonObj_MAIL_NOTE_CODE;
		try {
			jsonObj = new JSONObject(result);
			jsonObjdataList = jsonObj.getJSONObject("dataList");
			jsonObj_MAIL_NOTE_CODE = jsonObjdataList
					.getJSONArray("MAIL_NOTE_CODE");
			int MAIL_NOTE_CODESize = jsonObj_MAIL_NOTE_CODE.length();
			List<BaseData> MAIL_NOTE_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < MAIL_NOTE_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("MAIL_NOTE_CODE");
				bean.setDataKey(jsonObj_MAIL_NOTE_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_MAIL_NOTE_CODE.getJSONObject(i)
						.getString("dataValue"));
				MAIL_NOTE_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(MAIL_NOTE_CODElist, "MAIL_NOTE_CODE");
			downdata_MAIL_NOTE_CODE = true;
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			LogUtils.e(MAIL_NOTE_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("MAIL_NOTE_CODE")
							.size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void saveDLV_STS_CODE(String result) {
		JSONObject jsonObj;
		JSONObject jsonObjdataList;
		JSONArray jsonObj_DLV_STS_CODE;
		try {
			jsonObj = new JSONObject(result);
			jsonObjdataList = jsonObj.getJSONObject("dataList");
			jsonObj_DLV_STS_CODE = jsonObjdataList.getJSONArray("DLV_STS_CODE");
			int DLV_STS_CODESize = jsonObj_DLV_STS_CODE.length();
			List<BaseData> DLV_STS_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < DLV_STS_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("DLV_STS_CODE");
				bean.setDataKey(jsonObj_DLV_STS_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_DLV_STS_CODE.getJSONObject(i)
						.getString("dataValue"));
				DLV_STS_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(DLV_STS_CODElist, "DLV_STS_CODE");
			downdata_DLV_STS_CODE = true;
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			LogUtils.e(DLV_STS_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("DLV_STS_CODE")
							.size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void saveTURN_BACK_CAUSE_CODE(String result) {
		JSONObject jsonObj;
		JSONObject jsonObjdataList;
		JSONArray jsonObj_TURN_BACK_CAUSE_CODE;
		try {
			jsonObj = new JSONObject(result);
			jsonObjdataList = jsonObj.getJSONObject("dataList");
			jsonObj_TURN_BACK_CAUSE_CODE = jsonObjdataList
					.getJSONArray("TURN_BACK_CAUSE_CODE");
			int TURN_BACK_CAUSE_CODESize = jsonObj_TURN_BACK_CAUSE_CODE
					.length();
			List<BaseData> TURN_BACK_CAUSE_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < TURN_BACK_CAUSE_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("TURN_BACK_CAUSE_CODE");
				bean.setDataKey(jsonObj_TURN_BACK_CAUSE_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_TURN_BACK_CAUSE_CODE.getJSONObject(i)
						.getString("dataValue"));
				TURN_BACK_CAUSE_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(TURN_BACK_CAUSE_CODElist,
					"TURN_BACK_CAUSE_CODE");
			downdata_TURN_BACK_CAUSE_CODE = true;
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			LogUtils.e(TURN_BACK_CAUSE_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags(
							"TURN_BACK_CAUSE_CODE").size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void saveUNDLV_CAUSE_CODE(String result) {
		JSONObject jsonObj;
		JSONObject jsonObjdataList;
		JSONArray jsonObj_UNDLV_CAUSE_CODE;
		try {
			jsonObj = new JSONObject(result);
			jsonObjdataList = jsonObj.getJSONObject("dataList");
			jsonObj_UNDLV_CAUSE_CODE = jsonObjdataList
					.getJSONArray("UNDLV_CAUSE_CODE");
			int UNDLV_CAUSE_CODESize = jsonObj_UNDLV_CAUSE_CODE.length();
			List<BaseData> UNDLV_CAUSE_CODElist = new ArrayList<BaseData>();
			for (int i = 0; i < UNDLV_CAUSE_CODESize; i++) {
				BaseData bean = new BaseData();
				bean.setDataFlags("UNDLV_CAUSE_CODE");
				bean.setDataKey(jsonObj_UNDLV_CAUSE_CODE.getJSONObject(i)
						.getString("dataKey"));
				bean.setDataValue(jsonObj_UNDLV_CAUSE_CODE.getJSONObject(i)
						.getString("dataValue"));
				UNDLV_CAUSE_CODElist.add(bean);
			}
			baseDataDao.SaveBaseData(UNDLV_CAUSE_CODElist, "UNDLV_CAUSE_CODE");
			downdata_UNDLV_CAUSE_CODE = true;
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			LogUtils.e(UNDLV_CAUSE_CODESize
					+ "    "
					+ baseDataDao.FindBaseDataByDataFlags("UNDLV_CAUSE_CODE")
							.size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
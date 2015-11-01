package com.newcdc.activity.clcttask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.newcdc.asynctask.DownMailTypeAsyncTask;
import com.newcdc.asynctask.DownMailTypeAsyncTask.onPostExecuteListener;
import com.newcdc.db.CustomerDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.MailTypeDao;
import com.newcdc.model.CustomerBean;
import com.newcdc.model.MailTypeBean;
import com.newcdc.tools.Global;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * 揽收基础数据下载
 * 
 * @author quanyi
 * 
 */
public class DataDown {

	Context context;
	private UserInfoUtils userDao;
	private String termeseqno = null;
	private String clctorgcode = null;// 收寄机构号 投递机构号
	private String username = null;// 工号
	private CustomerDao customerDao = null;
	// private TransportTypeDao transportTypeDao = null;
	private MailTypeDao mailTypeDao = null;

	MailTypeAsyncTask MailTypeTask = null;
	CustomerAsyncTask CustomerTask = null;
	private String C_page = "1";
	private String CustomerTotalItems = null;
	private String M_page = "1";
	private String MailTypeTotalItems = null;
	private int M_count = 0;
	private int C_count = 0;

	public DataDown() {

	}

	public void oncreate(Context context, String terme, String orgcode,
			String name) {
		// userDao = new UserInfoUtils(context);
		// TelephonyManager telephonemanage = (TelephonyManager)
		// getSystemService(Context.TELEPHONY_SERVICE);
		this.context = context;
		termeseqno = terme;// telephonemanage.getDeviceId();
		clctorgcode = orgcode;// userDao.getUserDelvorgCode();
		username = name;// userDao.getUserName();
		customerDao = DeliverDaoFactory.getInstance().getCustomerDao(context);
		// transportTypeDao =
		// DeliverDaoFactory.getInstance().getTransportTypeDao(
		// context);
		mailTypeDao = DeliverDaoFactory.getInstance().getMailTypeDao(context);
		downloadData();
	}

	/**
	 * 数据下载
	 * 
	 * @return 返回当前操作的消息通告
	 */
	private String downloadData() {
		new AsyncTask<Object, Void, String>() {
			@Override
			protected String doInBackground(Object... params) {
				// 邮件分类基础数据下载
				DownloadMailType(M_page);
				// 大客户基础数据下载
				DownloadCustomer(C_page);
				// DownloadTransportType();
				return null;
			}

			@Override
			protected void onPostExecute(String obj) {
				super.onPostExecute(obj);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
		}.execute();

		return "";
	}

	/**
	 * 邮件分类数据下载 termeseqno 设备号 clctorgcode 收寄机构号 dlvorgcode 投递机构号 username 工号
	 * data_flags 数据同步标识(目前只提供10,29,38,并且格式为字符串,中间以逗号隔开) rq_page：页数 rq_total
	 * 一页要下载的条数
	 * 
	 * @return 返回当前操作的消息通告
	 */
	private void DownloadMailType(String M_page) {
		String url = Global.PDADOWNLOAD + "termeseqno=" + termeseqno
				+ "&clctorgcode=" + clctorgcode + "&dlvorgcode=" + clctorgcode
				+ "&username=" + username + "&data_flags=" + "29" + "&rq_page="
				+ M_page + "&rq_total=" + "100";
		// LogUtils.e("邮件分类请求页数：   " + M_page);
		MailTypeTask = new MailTypeAsyncTask();
		MailTypeTask.execute(url);
	}

	class MailTypeAsyncTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String result = com.newcdc.tools.NetHelper.doGet(params[0]);
			// 下载失败重新下载
			if (result == null || "请求服务器失败".equals(result)) {
				M_count++;
				if (M_count < 10) {
					if (Utils.isNetworkAvailable(context)) {
						if (!"1".equals(M_page)) {
							M_page = Integer.valueOf(M_page) - 1 + "";
						}
						DownloadMailType(M_page);
					}
				}
			} else if (resState(result).equals("0")) {
			} else {
				saveMailType(result);
			}
			return result;
		}
	}

	protected void saveMailType(String result) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(result);
			JSONArray array = jsonObj.getJSONObject("body").getJSONArray(
					"success");
			// MailTypeTotalItems = array.getJSONObject(0).get("totalItems")
			// .toString();// 总条数
			// LogUtils.e("邮件分类总条数：   " + MailTypeTotalItems);
			// LogUtils.e("MailTypeTotalItems   " + MailTypeTotalItems);
			JSONArray arrayofmail = array.getJSONObject(0).getJSONArray(
					"mailOfCategoryDownLoad");
			List<MailTypeBean> list = new ArrayList<MailTypeBean>();
			for (int i = 0; i < arrayofmail.length(); i++) {
				JSONObject obj = arrayofmail.getJSONObject(i);
				MailTypeBean bean = new MailTypeBean(obj);
				list.add(bean);
			}
			// 页数为1时删除表里面之前的数据
			if ("1".equals(M_page)) {
				mailTypeDao.DeleteData();
			}
			// 当下载页数小于等于总页数与100的余数时 说明还有数据 再把页数+1继续下载
			// if (Integer.valueOf(M_page) <=
			// Integer.valueOf(MailTypeTotalItems) / 100) {
			// M_page = Integer.valueOf(M_page) + 1 + "";
			// DownloadCustomer(M_page);
			// }
			if (arrayofmail.length() == 100) {
				M_page = Integer.valueOf(M_page) + 1 + "";
				DownloadCustomer(M_page);
			}
			// 保存到数据库中
			mailTypeDao.saveMailTypeInfo(list, clctorgcode);
			// LogUtils.e("MailType   "
			// + mailTypeDao.FindMailTypeInfo(clctorgcode).size());
			// LogUtils.e("邮件分类数据库中的条数：   "
			// + mailTypeDao.FindMailTypeInfo(clctorgcode).size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 大客户数据下载
	 * 
	 * @return 返回当前操作的消息通告
	 */
	private void DownloadCustomer(String C_page) {
		String url = Global.PDADOWNLOAD + "termeseqno=" + termeseqno
				+ "&clctorgcode=" + clctorgcode + "&dlvorgcode=" + clctorgcode
				+ "&username=" + username + "&data_flags=" + "10" + "&rq_page="
				+ C_page + "&rq_total=" + "100";
		// LogUtils.e("url:" + url);
		// LogUtils.e("大客户请求页数：   " + C_page);
		CustomerTask = new CustomerAsyncTask();
		CustomerTask.execute(url);
	}

	class CustomerAsyncTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String result = com.newcdc.tools.NetHelper.doGet(params[0]);
			if (result == null || "请求服务器失败".equals(result)) {
				C_count++;
				if (C_count < 10) {
					if (Utils.isNetworkAvailable(context)) {
						if (!"1".equals(C_page)) {
							C_page = Integer.valueOf(C_page) - 1 + "";
						}
						DownloadCustomer(C_page);
					}
				}
			} else if (resState(result).equals("0")) {
			} else {
				saveCustomer(result);
			}
			return result;
		}
	}

	protected void saveCustomer(String result) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(result);
			JSONArray array = jsonObj.getJSONObject("body").getJSONArray(
					"success");
			// CustomerTotalItems = array.getJSONObject(0).get("totalItems")
			// .toString();
			// LogUtils.e("大客户总条数：   " + CustomerTotalItems);
			// LogUtils.e("CustomerTotalItems   " + CustomerTotalItems);
			JSONArray arrayoCustomer = array.getJSONObject(0).getJSONArray(
					"bigCustomerResult");
			List<CustomerBean> list = new ArrayList<CustomerBean>();
			for (int i = 0; i < arrayoCustomer.length(); i++) {
				JSONObject obj = arrayoCustomer.getJSONObject(i);
				CustomerBean bean = new CustomerBean(obj);
				list.add(bean);
			}
			if ("1".equals(C_page)) {
				customerDao.DeleteData();
			}
			// if (Integer.valueOf(C_page) <=
			// Integer.valueOf(CustomerTotalItems) / 100) {
			// C_page = Integer.valueOf(C_page) + 1 + "";
			// DownloadCustomer(C_page);
			// }
			if (arrayoCustomer.length() == 100) {
				C_page = Integer.valueOf(C_page) + 1 + "";
				DownloadCustomer(C_page);
			}
			customerDao.savedate(list);
			// LogUtils.e("大客户数据库中的条数：   " + customerDao.FindData2().size());
			// LogUtils.e("Customer   " + customerDao.FindData2().size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 运输数据下载
	 * 
	 * @return 返回当前操作的消息通告
	 */
	private void DownloadTransportType() {
		String url = Global.PDADOWNLOAD + "termeseqno=" + termeseqno
				+ "&clctorgcode=" + clctorgcode + "&dlvorgcode=" + clctorgcode
				+ "&username=" + username + "&data_flags=" + "38";
		// LogUtils.e("运输方式:  " + url);
		DownMailTypeAsyncTask async = new DownMailTypeAsyncTask();
		async.setListener(new onPostExecuteListener() {

			@Override
			public void onPostExecute(String result) {
				// LogUtils.e("运输方式:  " + result);
				if (result == null || "请求服务器失败".equals(result)) {
					// if (transportTypeDao.FindTransportTypeInfo(clctorgcode)
					// .size() == 0) {
					// if (Utils.isNetworkAvailable(context)) {
					// DownloadTransportType();
					// }
					// }
				} else if (resState(result).equals("0")) {
					JSONObject object;
					String error = "error";
					try {
						object = new JSONObject(result);
						error = object.getJSONObject("body").getString("error");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					saveTransportType(result);
				}
			}
		});
		async.execute(url);
	}

	protected void saveTransportType(String result) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(result);
			JSONArray array = jsonObj.getJSONObject("body").getJSONArray(
					"success");
			JSONArray arrayofTransportation = array.getJSONObject(0)
					.getJSONArray("modeOfTransportationResult");
			List<TransportTypeBean> list = new ArrayList<TransportTypeBean>();
			for (int i = 0; i < arrayofTransportation.length(); i++) {
				JSONObject obj = arrayofTransportation.getJSONObject(i);
				TransportTypeBean bean = new TransportTypeBean(obj);
				list.add(bean);
			}
			// transportTypeDao.DeleteData();
			// transportTypeDao.saveTransportTypeInfo(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 获得返回值中result字段的值
	public String resState(String s) {
		String str = "";
		try {
			JSONObject json = new JSONObject(s);
			str = json.get("result").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 
	 * 运输方式数据封装
	 * 
	 * @author quanyi
	 * 
	 */
	public class TransportTypeBean {
		String TransferCode;
		String TransferName;
		String OldMailType;
		String ChangeMailType;

		public TransportTypeBean(JSONObject json) {

			try {
				TransferCode = json.get("TransferCode") == null ? "" : json
						.get("TransferCode").toString();
				TransferName = json.get("TransferName") == null ? "" : json
						.get("TransferName").toString();
				OldMailType = json.get("OldMailType") == null ? "" : json.get(
						"OldMailType").toString();
				ChangeMailType = json.get("ChangeMailType") == null ? "" : json
						.get("ChangeMailType").toString();
			} catch (Exception e) {

			}

		}

		public String getTransferCode() {
			return TransferCode == null ? "" : TransferCode;
		}

		public void setTransferCode(String TransferCode) {
			this.TransferCode = TransferCode;
		}

		public String getTransferName() {
			return TransferName == null ? "" : TransferName;
		}

		public void setTransferName(String TransferName) {
			this.TransferName = TransferName;
		}

		public String getOldMailType() {
			return OldMailType == null ? "" : OldMailType;
		}

		public void setOldMailType(String OldMailType) {
			this.OldMailType = OldMailType;
		}

		public String getChangeMailType() {
			return ChangeMailType == null ? "" : ChangeMailType;
		}

		public void ChangeMailType(String ChangeMailType) {
			this.ChangeMailType = ChangeMailType;
		}

		// public String getTemp1() {
		// return Temp1 == null ? "" : Temp1;
		// }
		//
		// public void setTemp1(String Temp1) {
		// this.Temp1 = Temp1;
		// }
		//
		// public String getTemp2() {
		// return Temp2 == null ? "" : Temp2;
		// }
		//
		// public void setTemp2(String Temp2) {
		// this.Temp2 = Temp2;
		// }
		//
		// public String getTemp3() {
		// return Temp3 == null ? "" : Temp3;
		// }
		//
		// public void setTemp3(String Temp3) {
		// this.Temp3 = Temp3;
		// }
		//
		// public String getTemp4() {
		// return Temp4 == null ? "" : Temp4;
		// }
		//
		// public void setTemp4(String Temp4) {
		// this.Temp4 = Temp4;
		// }
		//
		// public String getTemp5() {
		// return Temp5 == null ? "" : Temp5;
		// }
		//
		// public void setTemp5(String Temp5) {
		// this.Temp5 = Temp5;
		// }

	}
}

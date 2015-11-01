package com.newcdc.activity.usercenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.newcdc.R;
import com.newcdc.db.AddressDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

public class Addressdetail extends Activity {
	private AddressDao infodao;
	private List<Map<String, Object>> list;
	private TextView name, tel, address, alwaysaddress, add;
	private EditText name2, tel2, address2;
	private ListView listview;
	private Bundle bundle;
	ProgressDialog myDialog = null;
	private TextView bian, cancle, complete, txt_tit, delete;
	private ScrollView scroll1, scroll2;
	private static final int SUCCESS = 1;
	private static final int ERROR = 2;
	public List<Map<String, Object>> dataList;// list
	public Mydapter adapter = null;
	private UserInfoUtils userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_detail);
		userInfo = new UserInfoUtils(getApplicationContext());
		Intent intents = getIntent();
		bundle = intents.getExtras();
		innitview();// 初始化界面
		infodao = DeliverDaoFactory.getInstance().getAddressDao(this);
		list = new ArrayList<Map<String, Object>>();
		list = infodao.findistele(bundle.getString("telephonenum"));
		settex();// 赋值

		loadguanxi(bundle.getString("mainid"));// 加载关系客户

		bian.setOnClickListener(new OnClickListener() {// 点击编辑
			@Override
			public void onClick(View v) {
				complete.setVisibility(View.VISIBLE);
				cancle.setVisibility(View.VISIBLE);

				txt_tit.setVisibility(View.GONE);
				bian.setVisibility(View.GONE);

				scroll2.setVisibility(View.VISIBLE);
				scroll1.setVisibility(View.GONE);

				name2.setFocusable(true);
			}
		});

		cancle.setOnClickListener(new OnClickListener() {// 取消
			@Override
			public void onClick(View v) {
				complete.setVisibility(View.GONE);
				cancle.setVisibility(View.GONE);

				txt_tit.setVisibility(View.VISIBLE);
				bian.setVisibility(View.VISIBLE);

				scroll2.setVisibility(View.GONE);
				scroll1.setVisibility(View.VISIBLE);

			}
		});

		complete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(name2.getText().toString().trim())) {
					Toast.makeText(Addressdetail.this, "姓名不能为空",
							Toast.LENGTH_LONG).show();
				} else if ("".equals(tel2.getText().toString().trim())) {
					Toast.makeText(Addressdetail.this, "电话号码不能为空",
							Toast.LENGTH_LONG).show();
				} else if ("".equals(address2.getText().toString().trim())) {
					Toast.makeText(Addressdetail.this, "地址不能为空",
							Toast.LENGTH_LONG).show();
				} else {
					modify(name2.getText().toString().trim(), tel2.getText()
							.toString().trim(), address2.getText().toString()
							.trim(), bundle.getString("mainid"));
				}
			}
		});

		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertdialog("01", null);
			}
		});

		listview.setOnItemLongClickListener(new OnItemLongClickListener() {// 关系客户长按

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				alertdialogselect(
						dataList.get(position).get("name").toString(), dataList
								.get(position).get("tel").toString(), dataList
								.get(position).get("addr").toString(), dataList
								.get(position).get("SID").toString(), position);
				return false;
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {// 跳转到揽收
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", list.get(0).get("bodyname").toString());
				map.put("address", list.get(0).get("address").toString());
				map.put("telephonenum", list.get(0).get("telephonenum")
						.toString());
				map.put("relationname", dataList.get(position).get("name")
						.toString());
				map.put("relationaddress", dataList.get(position).get("addr")
						.toString());
				map.put("relationtelephonenum",
						dataList.get(position).get("tel").toString());
				List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
				alertdialog("02", lists);

			}
		});

		add.setOnClickListener(new OnClickListener() {// 添加关系用户
			@Override
			public void onClick(View v) {
				alertdialog("", "", "", "", "01", 9999);
			}
		});
	}

	private void innitview() {
		name = (TextView) findViewById(R.id.name);
		tel = (TextView) findViewById(R.id.tel);
		address = (TextView) findViewById(R.id.address);
		alwaysaddress = (TextView) findViewById(R.id.always);
		listview = (ListView) findViewById(R.id.list1);

		name2 = (EditText) findViewById(R.id.name2);
		tel2 = (EditText) findViewById(R.id.tel2);
		address2 = (EditText) findViewById(R.id.address2);

		bian = (TextView) findViewById(R.id.bian);
		complete = (TextView) findViewById(R.id.complete);
		cancle = (TextView) findViewById(R.id.cancle);
		txt_tit = (TextView) findViewById(R.id.txt_tit);
		delete = (TextView) findViewById(R.id.delete);// 删除

		add = (TextView) findViewById(R.id.add);// 添加关系用户

		scroll1 = (ScrollView) findViewById(R.id.scroll1);
		scroll2 = (ScrollView) findViewById(R.id.scroll2);

		dataList = new ArrayList<Map<String, Object>>();
		adapter = new Mydapter();
		listview.setAdapter(adapter);

	}

	private void settex() {
		name.setText(list.get(0).get("bodyname").toString());
		tel.setText(list.get(0).get("telephonenum").toString());
		address.setText(list.get(0).get("address").toString());

		name2.setText(list.get(0).get("bodyname").toString());
		tel2.setText(list.get(0).get("telephonenum").toString());
		address2.setText(list.get(0).get("address").toString());

	}

	private void alertdialog(final String type,
			final List<Map<String, String>> list) {// 删除提示弹出框
		LayoutInflater inflater = this.getLayoutInflater();
		final View view = inflater.inflate(R.layout.alert_delete, null);
		final Dialog dias = new Dialog(this, R.style.dialogss);
		DisplayMetrics metric = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		dias.setContentView(view, new LayoutParams(width * 17 / 20,
				LayoutParams.MATCH_PARENT));
		dias.setCanceledOnTouchOutside(false);

		Button complete = (Button) view.findViewById(R.id.dia_btn_ok);
		Button cancel = (Button) view.findViewById(R.id.dia_btn_not);

		TextView txt_tit = (TextView) view.findViewById(R.id.txt_tit);// 提示内容
		if ("02".equals(type)) {
			txt_tit.setText("确定添加此联系人到揽收吗？");
		}
		dias.show();
		complete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("01".equals(type)) {
					deleaddress(bundle.getString("mainid"));
					dias.dismiss();
				} else {
					// 跳转页面 无派单揽收
					// Intent intent=new
					// Intent(Addressdetail.this,RecieveActivity.class);
					// Bundle bundle=new Bundle();
					// bundle.putSerializable("list", (Serializable) list);
					// intent.putExtras(bundle);
					// startActivity(intent);
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dias.dismiss();
			}
		});
	}

	/**
	 * 长按listitem——dialog
	 * **/
	private void alertdialogselect(final String bodyname,
			final String telphone, final String address, final String sid,
			final int position) {
		LayoutInflater inflater = Addressdetail.this.getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_sele_layout, null);
		TextView mdia_sel_dele = (TextView) layout
				.findViewById(R.id.dia_sel_dele);
		TextView mdia_sel_edit = (TextView) layout
				.findViewById(R.id.dia_sel_edit);
		DisplayMetrics metric = new DisplayMetrics();
		final Dialog longdia = new Dialog(Addressdetail.this,
				R.style.dialogtheme);
		Addressdetail.this.getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		int width = metric.widthPixels;
		longdia.setContentView(layout, new LayoutParams(width * 15 / 20,
				LayoutParams.WRAP_CONTENT));
		longdia.setCancelable(true);
		mdia_sel_dele.setOnClickListener(new OnClickListener() {// 删除
					@Override
					public void onClick(View v) {
						deleteguanxi(sid);
						longdia.dismiss();
					}
				});
		mdia_sel_edit.setOnClickListener(new OnClickListener() {// 编辑
					@Override
					public void onClick(View v) {
						alertdialog(bodyname, telphone, address, sid, "02",
								position);
						longdia.dismiss();
					}
				});
		longdia.show();
	}

	// 关系客户 添加/ 编辑弹出框
	private void alertdialog(final String bodyname, final String telphone,
			final String address, final String sid, final String type,
			final int position) {
		LayoutInflater inflater = Addressdetail.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_guanxi, null);
		final Dialog dias = new Dialog(Addressdetail.this, R.style.dialogss);
		DisplayMetrics metric = new DisplayMetrics();
		int width = metric.widthPixels;
		dias.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		dias.setCanceledOnTouchOutside(false);
		final EditText name = (EditText) view.findViewById(R.id.name);
		final EditText tel = (EditText) view.findViewById(R.id.num);
		final EditText addr = (EditText) view.findViewById(R.id.addr);
		tel.setInputType(InputType.TYPE_CLASS_NUMBER);
		name.setText(bodyname);
		if (!"".equals(telphone)) {
			tel.setText(telphone);
		}
		addr.setText(address);
		Button complete = (Button) view.findViewById(R.id.dia_btn_ok);
		Button cancel = (Button) view.findViewById(R.id.dia_btn_not);
		dias.show();
		complete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(name.getText().toString().trim())) {
					Toast.makeText(Addressdetail.this, "姓名不能为空",
							Toast.LENGTH_LONG).show();
				} else if ("".equals(tel.getText().toString().trim())) {
					Toast.makeText(Addressdetail.this, "电话号码不能为空",
							Toast.LENGTH_LONG).show();
				} else if ("".equals(addr.getText().toString().trim())) {
					Toast.makeText(Addressdetail.this, "收件地址不能为空",
							Toast.LENGTH_LONG).show();
				} else {
					if ("01".equals(type)) {// 添加
						addguanxi(name.getText().toString(), tel.getText()
								.toString(), addr.getText().toString(), "");
					} else {// 修改
						modifyguanxi(name.getText().toString(), tel.getText()
								.toString(), addr.getText().toString(), sid,
								position);// 修改
					}
					dias.dismiss();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dias.dismiss();
			}
		});

	}

	/**
	 * 删除
	 */
	private void deleaddress(final String mainid) {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
					paramObject.add(new BasicNameValuePair("customer_sid",
							mainid));// id

					jsonObj = new JSONObject(NetHelper.doPost(Global.SERVER_URL
							+ Global.DELUSERCONTACTS, paramObject));
				} catch (Exception e) {

				}
				return jsonObj;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObj) {
				try {
					if (myDialog != null && myDialog.isShowing()) {
						myDialog.dismiss();
					}
					if (jsonObj != null) {
						if ("1".equals(jsonObj.get("result"))) {// 删除上传成功
							infodao.deletetelephone(mainid);
							Intent reintent = new Intent(
									"com.cn.net.usefulpro.Receiver");
							reintent.putExtra("msg", "update");
							Addressdetail.this.sendBroadcast(reintent); // 刷新列表
							Addressdetail.this.finish();

							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "删除通讯录成功";
							messageListener2.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "删除通讯录失败";
							messageListener2.sendMessage(msg);

						}
					} else {
						Message msg = new Message();
						msg.what = ERROR;
						msg.obj = "删除通讯录异常";
						messageListener2.sendMessage(msg);
					}
				} catch (Exception ex) {

				}
				super.onPostExecute(jsonObj);
			}

			@Override
			protected void onPreExecute() {
				if (myDialog != null && myDialog.isShowing()) {
					myDialog.dismiss();
				}
				myDialog = ProgressDialog.show(Addressdetail.this,
						Addressdetail.this.getString(R.string.app_name),
						"正在执行...", true, false);
				super.onPreExecute();
			}

		}.execute();
	}

	/**
	 * 修改
	 */
	private void modify(final String name, final String tel, final String addr,
			final String mainid) {
		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
					paramObject.add(new BasicNameValuePair("sid", mainid));// mainid
					paramObject.add(new BasicNameValuePair(
							"express_company_code", "EMS"));
					paramObject.add(new BasicNameValuePair(
							"express_company_name", "EMS"));
					paramObject.add(new BasicNameValuePair("employee_no",
							userInfo.getUserName()));// 工号
					paramObject.add(new BasicNameValuePair("org_code", userInfo
							.getUserDelvorgCode()));// 机构号
					paramObject.add(new BasicNameValuePair("contacts_address",
							addr));// 地址
					paramObject.add(new BasicNameValuePair("contacts_mobile",
							tel));// 电话
					paramObject.add(new BasicNameValuePair("contacts_name",
							name));// 联系姓名
					jsonObj = new JSONObject(NetHelper.doPost(Global.SERVER_URL
							+ Global.UPDATEUSERCONTACTS, paramObject));
				} catch (Exception e) {

				}
				return jsonObj;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObj) {
				try {
					if (myDialog != null && myDialog.isShowing()) {
						myDialog.dismiss();
					}
					if (jsonObj != null) {
						if ("1".equals(jsonObj.get("result"))) {// 修改成功
							infodao.updatetelephone(name, tel, addr, mainid);
							list = infodao.findistele(tel);// 重新赋值
							settex();
							complete.setVisibility(View.GONE);
							cancle.setVisibility(View.GONE);
							txt_tit.setVisibility(View.VISIBLE);
							bian.setVisibility(View.VISIBLE);
							scroll2.setVisibility(View.GONE);
							scroll1.setVisibility(View.VISIBLE);
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "修改通讯录成功";
							messageListener2.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "修改通讯录失败";
							messageListener2.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = ERROR;
						msg.obj = "修改通讯录异常";
						messageListener2.sendMessage(msg);
					}
				} catch (Exception ex) {

				}
				super.onPostExecute(jsonObj);
			}

			@Override
			protected void onPreExecute() {
				if (myDialog != null && myDialog.isShowing()) {
					myDialog.dismiss();
				}
				myDialog = ProgressDialog.show(Addressdetail.this,
						Addressdetail.this.getString(R.string.app_name),
						"正在执行...", true, false);
				super.onPreExecute();
			}

		}.execute();
	}

	// /////////////////////////////////////////////////关系客户///////////////////////////////////////////////////
	/**
	 * 查询关系客户
	 */
	private void loadguanxi(final String mainid) {
		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
					paramObject.add(new BasicNameValuePair("customer_sid",
							mainid));// mainid
					jsonObj = new JSONObject(NetHelper.doPost(Global.SERVER_URL
							+ Global.FINDCUSTOMERRELATION, paramObject));
				} catch (Exception e) {

				}
				return jsonObj;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObj) {
				try {
					if (myDialog != null && myDialog.isShowing()) {
						myDialog.dismiss();
					}
					if (jsonObj != null) {
						if ("1".equals(jsonObj.get("result"))) {
							// "result":"1","body":{"success":[{"EXPRESS_COMPANY_NAME":"44","CLCT_DLV_FLAG":"1","LAST_CLCT_DLV_TIME":null,"CREATE_TIME":null,"CONTACTS_NAME":"wanggenyao","EXPRESS_COMPANY_CODE":"333","CREATE_BY":null,"UPDATE_TIME":null,"CONTACTS_MOBILE":"11111111111","SID":11111,"UPDATE_BY":null,"CONTACTS_ADDRESS":"222","CUSTOMER_SID":23266935427118},{"EXPRESS_COMPANY_NAME":"44444","CLCT_DLV_FLAG":"1","LAST_CLCT_DLV_TIME":null,"CREATE_TIME":null,"CONTACTS_NAME":"www","EXPRESS_COMPANY_CODE":"444","CREATE_BY":null,"UPDATE_TIME":null,"CONTACTS_MOBILE":"22222222222","SID":22222,"UPDATE_BY":null,"CONTACTS_ADDRESS":"33","CUSTOMER_SID":23266935427118}
							JSONArray jsoncc = jsonObj.getJSONObject("body")
									.getJSONArray("success");

							for (int i = 0; i < jsoncc.length(); i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								JSONObject bbJsonObject = jsoncc
										.getJSONObject(i);
								map.put("name",
										bbJsonObject.getString("CONTACTS_NAME"));
								map.put("tel", bbJsonObject
										.getString("CONTACTS_MOBILE"));
								map.put("addr", bbJsonObject
										.getString("CONTACTS_ADDRESS"));
								map.put("SID", bbJsonObject.getString("SID"));
								dataList.add(map);
							}
							setListViewHeightBasedOnChildren(listview);
							adapter.notifyDataSetChanged();
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "查询关系用户成功";
							messageListener2.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "查询关系用户失败";
							messageListener2.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = ERROR;
						msg.obj = "查询关系用户异常";
						messageListener2.sendMessage(msg);
					}
				} catch (Exception ex) {

				}
				super.onPostExecute(jsonObj);
			}

			@Override
			protected void onPreExecute() {
				if (myDialog != null && myDialog.isShowing()) {
					myDialog.dismiss();
				}
				myDialog = ProgressDialog.show(Addressdetail.this,
						Addressdetail.this.getString(R.string.app_name),
						"正在执行...", true, false);
				super.onPreExecute();
			}

		}.execute();
	}

	/**
	 * 添加关系用户
	 */
	private void addguanxi(final String name, final String tel,
			final String addr, final String mainid) {
		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
					paramObject.add(new BasicNameValuePair("customer_sid",
							bundle.getString("mainid")));// mainid
					paramObject.add(new BasicNameValuePair(
							"express_company_code", "EMS"));
					paramObject.add(new BasicNameValuePair(
							"express_company_name", "EMS"));
					paramObject.add(new BasicNameValuePair("employee_no",
							userInfo.getUserName()));// 工号
					paramObject.add(new BasicNameValuePair("org_code", userInfo
							.getUserDelvorgCode()));// 机构号
					paramObject.add(new BasicNameValuePair("contacts_address",
							addr));// 地址
					paramObject.add(new BasicNameValuePair("contacts_mobile",
							tel));// 电话
					paramObject.add(new BasicNameValuePair("contacts_name",
							name));// 联系姓名
					paramObject
							.add(new BasicNameValuePair("clct_dlv_flag", "1"));// 收投标识
					jsonObj = new JSONObject(NetHelper.doPost(Global.SERVER_URL
							+ Global.ADDCUSTOMERRELATION, paramObject));
				} catch (Exception e) {

				}
				return jsonObj;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObj) {
				try {
					if (myDialog != null && myDialog.isShowing()) {
						myDialog.dismiss();
					}
					if (jsonObj != null) {
						if ("1".equals(jsonObj.get("result"))) {// 修改成功
							Addressdetail.this.finish();
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "添加关系用户成功";
							messageListener2.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = jsonObj.getJSONObject("body")
									.get("error").toString();
							messageListener2.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = ERROR;
						msg.obj = "添加关系用户异常";
						messageListener2.sendMessage(msg);
					}
				} catch (Exception ex) {

				}
				super.onPostExecute(jsonObj);
			}

			@Override
			protected void onPreExecute() {
				if (myDialog != null && myDialog.isShowing()) {
					myDialog.dismiss();
				}
				myDialog = ProgressDialog.show(Addressdetail.this,
						Addressdetail.this.getString(R.string.app_name),
						"正在执行...", true, false);
				super.onPreExecute();
			}

		}.execute();
	}

	/**
	 * 修改关系用户
	 */
	private void modifyguanxi(final String name, final String tel,
			final String addr, final String mainid, final int position) {
		new AsyncTask<Object, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
					paramObject.add(new BasicNameValuePair("sid", mainid));// mainid
					paramObject.add(new BasicNameValuePair("customer_sid",
							bundle.getString("mainid")));
					paramObject.add(new BasicNameValuePair(
							"express_company_code", "EMS"));
					paramObject.add(new BasicNameValuePair(
							"express_company_name", "EMS"));
					paramObject.add(new BasicNameValuePair("employee_no",
							userInfo.getUserName()));// 工号
					paramObject.add(new BasicNameValuePair("org_code", userInfo
							.getUserDelvorgCode()));// 机构号
					paramObject.add(new BasicNameValuePair("contacts_address",
							addr));// 地址
					paramObject.add(new BasicNameValuePair("contacts_mobile",
							tel));// 电话
					paramObject.add(new BasicNameValuePair("contacts_name",
							name));// 联系姓名
					paramObject
							.add(new BasicNameValuePair("clct_dlv_flag", "1"));// 收投标识
					jsonObj = new JSONObject(NetHelper.doPost(Global.SERVER_URL
							+ Global.UPDATECUSTOMERRELATION, paramObject));
				} catch (Exception e) {

				}
				return jsonObj;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObj) {
				try {
					if (myDialog != null && myDialog.isShowing()) {
						myDialog.dismiss();
					}
					if (jsonObj != null) {
						if ("1".equals(jsonObj.get("result"))) {// 修改成功
							Addressdetail.this.finish();
							// dataList.get(position);
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "修改关系用户成功";
							messageListener2.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = jsonObj.get("error").toString();
							messageListener2.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = ERROR;
						msg.obj = "修改关系用户异常";
						messageListener2.sendMessage(msg);
					}
				} catch (Exception ex) {

				}
				super.onPostExecute(jsonObj);
			}

			@Override
			protected void onPreExecute() {
				if (myDialog != null && myDialog.isShowing()) {
					myDialog.dismiss();
				}
				myDialog = ProgressDialog.show(Addressdetail.this,
						Addressdetail.this.getString(R.string.app_name),
						"正在执行...", true, false);
				super.onPreExecute();
			}

		}.execute();
	}

	/**
	 * 删除通讯录关系表
	 */
	private void deleteguanxi(final String sid) {
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject jsonObj = null;
				try {
					List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
					paramObject.add(new BasicNameValuePair("sid", sid));// id
					jsonObj = new JSONObject(NetHelper.doPost(Global.SERVER_URL
							+ Global.DELCUSTOMERRELATION, paramObject));
				} catch (Exception e) {

				}
				return jsonObj;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObj) {
				try {
					if (myDialog != null && myDialog.isShowing()) {
						myDialog.dismiss();
					}
					if (jsonObj != null) {
						if ("1".equals(jsonObj.get("result"))) {// 删除上传成功
							Addressdetail.this.finish();
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "删除关系用户成功";
							messageListener2.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = SUCCESS;
							msg.obj = "删除关系用户失败";
							messageListener2.sendMessage(msg);

						}
					} else {
						Message msg = new Message();
						msg.what = ERROR;
						msg.obj = "删除关系用户异常";
						messageListener2.sendMessage(msg);
					}
				} catch (Exception ex) {

				}
				super.onPostExecute(jsonObj);
			}

			@Override
			protected void onPreExecute() {
				if (myDialog != null && myDialog.isShowing()) {
					myDialog.dismiss();
				}
				myDialog = ProgressDialog.show(Addressdetail.this,
						Addressdetail.this.getString(R.string.app_name),
						"正在执行...", true, false);
				super.onPreExecute();
			}

		}.execute();
	}

	@SuppressLint("HandlerLeak")
	private Handler messageListener2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (myDialog.isShowing()) {
					myDialog.dismiss();
				}
				Toast.makeText(Addressdetail.this, msg.obj.toString(),
						Toast.LENGTH_LONG).show();
				break;
			case ERROR:
				if (myDialog.isShowing()) {
					myDialog.dismiss();
				}
				Toast.makeText(Addressdetail.this, msg.obj.toString(),
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};

	class Mydapter extends BaseAdapter {
		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Viewholder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(Addressdetail.this).inflate(
						R.layout.guanxi_list, null);
				holder = new Viewholder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.tel = (TextView) convertView.findViewById(R.id.tel);
				holder.addr = (TextView) convertView.findViewById(R.id.addr);// 地址
				convertView.setTag(holder);
			} else {
				holder = (Viewholder) convertView.getTag();

			}
			final Map<String, Object> map = dataList.get(position);

			holder.name.setText(map.get("name").toString());
			holder.tel.setText(map.get("tel").toString());
			holder.addr.setText(map.get("addr").toString());
			return convertView;
		}
	}

	class Viewholder {
		TextView name;
		TextView tel;
		TextView addr;
	}

	// 设置List高度
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = 10 + totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(Addressdetail.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}
	

}

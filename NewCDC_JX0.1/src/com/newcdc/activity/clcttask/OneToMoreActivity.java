package com.newcdc.activity.clcttask;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dtr.zxing.activity.CaptureActivity;
import com.newcdc.R;
import com.newcdc.application.BaseActivity;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.FastLanDao;
import com.newcdc.model.FastLanBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.ExpressMailUtils;
import com.newcdc.tools.Global;
import com.newcdc.tools.Utils;
import com.newcdc.ui.InfoDialog;

public class OneToMoreActivity extends Activity {

	private Button back, add, jipao, save, saoyisao;
	private ListView maillist;
	private BaseAdapter adapter;
	private ArrayList<Map<String, String>> list1 = null;
	private String mainmail = "", tempweight = "";
	private EditText childmail, weight;
	private Intent intent;
	private TextView allweight;
	private FastLanBean fastLanBean = new FastLanBean();
	private TextView name;
	private Dialog dialog;
	private View view;
	private float Allweight;
	private MyReceiver oneToMoreReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one_to_more);

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(clicklistener);
		add = (Button) findViewById(R.id.add);
		add.setOnClickListener(clicklistener);
		jipao = (Button) findViewById(R.id.jipao);
		jipao.setOnClickListener(clicklistener);
		saoyisao = (Button) findViewById(R.id.saoyisao);
		saoyisao.setOnClickListener(clicklistener);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(clicklistener);
		save.setVisibility(View.GONE);
		name = (TextView) findViewById(R.id.name);
		fastLanBean = new FastLanBean();
		childmail = (EditText) findViewById(R.id.childmail);
		weight = (EditText) findViewById(R.id.weight);
		allweight = (TextView) findViewById(R.id.allweight);
		maillist = (ListView) findViewById(R.id.maillist);
		oneToMoreReceiver = new MyReceiver();
		Bundle bundle = OneToMoreActivity.this.getIntent().getExtras();
		mainmail = bundle.getString("mainmail");
		tempweight = bundle.getString("weight");
		Allweight = Float.parseFloat(tempweight);
		String customer = bundle.getString("customer");
		list1 = (ArrayList<Map<String, String>>) bundle
				.getSerializable("list1");
		if (list1 == null) {
			list1 = new ArrayList<Map<String, String>>();
		}

		name.setText(customer);
		allweight.setText(Allweight + "");
		adapter = new SimpleAdapter(OneToMoreActivity.this, list1,
				R.layout.onetomorelist_item, new String[] { "mainmail",
						"childmail" }, new int[] { R.id.mainmail,
						R.id.childmail });
		maillist.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int arg2V = arg2;
				TextView textView = (TextView) arg1
						.findViewById(R.id.childmail);
				// if (dialog == null) {
				// dialog = new Dialog(OneToMoreActivity.this,
				// R.style.dialogss);
				// view = View.inflate(OneToMoreActivity.this,
				// R.layout.dialog_delete, null);
				// Button cancel = (Button) view
				// .findViewById(R.id.info_cancel);
				// Button sure = (Button) view.findViewById(R.id.info_sure);
				// WindowManager manager = (WindowManager)
				// OneToMoreActivity.this
				// .getSystemService(WINDOW_SERVICE);
				// DisplayMetrics dm = new DisplayMetrics();
				// manager.getDefaultDisplay().getMetrics(dm);
				//
				// int width = dm.widthPixels;
				// int height = dm.heightPixels;
				// dialog.setContentView(view, new LayoutParams(
				// width * 15 / 20, LayoutParams.WRAP_CONTENT));
				// dialog.setCancelable(false);
				//
				// cancel.setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// dialog.dismiss();
				// dialog = null;
				// }
				// });
				// sure.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// dialog.dismiss();
				// list1.remove(arg2V);
				// adapter.notifyDataSetChanged();
				// dialog = null;
				// }
				// });
				// dialog.show();
				// }
				showDeleteDialog((SimpleAdapter) adapter, list1, arg2V);

				return false;
			}
		});

		maillist.setAdapter(adapter);
		/**
		 * 重量输入限制
		 */
		weight.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				try {
					if (s != null && !"".equals(s.toString())) {
						double userInput = Double.parseDouble(s.toString());
						if (userInput > 100.000) {
							Toast.makeText(OneToMoreActivity.this, "不可大于100",
									Toast.LENGTH_SHORT).show();
							weight.setText("100");
							Editable editable = weight.getText();
							Selection.setSelection(editable, editable.length());
						} else if ((userInput + "").length()
								- (userInput + "").indexOf(".") - 1 > 3) {
							Toast.makeText(OneToMoreActivity.this, "最大保留三位小数",
									Toast.LENGTH_SHORT).show();
							NumberFormat nf = NumberFormat.getNumberInstance();
							nf.setMaximumFractionDigits(3);
							System.out.println(nf.format(userInput));
							weight.setText(nf.format(userInput));
							Editable editable = weight.getText();
							Selection.setSelection(editable, editable.length());
						}
					}
				} catch (Exception e) {
				}
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(OneToMoreActivity.this);// 启动投递、揽收异步上传服务
		IntentFilter filter = new IntentFilter(Constant.ACTION_BLUTTOOTH_MSG);
		registerReceiver(oneToMoreReceiver, filter);
		super.onResume();
		weight.setText(Global.jipaoWeightChild);
		if ("".equals(weight.getText().toString().trim())) {
			weight.setText(Global.jipaoWeightChild);
		} else {
			String temp = weight.getText().toString().trim();
			if (new BigDecimal(temp).compareTo(new BigDecimal(
					Global.jipaoWeightChild)) < 0) {
				weight.setText(Global.jipaoWeightChild);
			}
		}

	}

	/*
	 * 判断邮件是否存在
	 */
	public boolean mailExits(String mail_num) {
		FastLanDao fastLanDao = DeliverDaoFactory.getInstance().getFastLanDao(
				getApplicationContext());
		List<FastLanBean> list = fastLanDao.queryFastLanMessageI();
		for (int i = 0; i < list.size(); i++) {
			String mailNum = list.get(i).getMailNum();
			if (mail_num.equals(mailNum)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 处理回传结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 111) {
			String result = data.getStringExtra("txtResult");
			if (!"".equals(result)) {
				if (true) {
					if (mailExits(result)) {
						showdiag("该邮件号已使用");
					} else {
						childmail.setText(result);
					}

				}
//				else {
//					// Toast.makeText(NoClctActivity.this, "邮件号不正确,请重新扫入",
//					// Toast.LENGTH_SHORT).show();
//					showdiag("邮件号不正确,请重新扫入");
//				}
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (list1.size() > 0) {
				Intent intent1 = new Intent(OneToMoreActivity.this,
						NoClctActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("list1", list1);
				intent1.putExtras(bundle);
				setResult(0, intent1);
				// Toast.makeText(OneToMoreActivity.this, "添加成功",
				// Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(OneToMoreActivity.this, "请填写子单号",
						Toast.LENGTH_LONG).show();
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean childMailExits(String childmail) {
		boolean isExits = false;
		for (int i = 0; i < list1.size(); i++) {
			if (childmail.equals(list1.get(i).get("childmail"))) {
				isExits = true;
			}
		}
		return isExits;

	}

	private void showdiag(String info) {
		// TODO Auto-generated method stub
		InfoDialog infoDialog = new InfoDialog(this, info);
		infoDialog.Show();

	}

	private OnClickListener clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				if (list1.size() > 0) {
					Intent intent1 = new Intent(OneToMoreActivity.this,
							NoClctActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("list1", list1);
					intent1.putExtras(bundle);
					setResult(0, intent1);
					// Toast.makeText(OneToMoreActivity.this, "添加成功",
					// Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(OneToMoreActivity.this, "请填写子单号",
							Toast.LENGTH_LONG).show();
				}
				finish();
				break;
			case R.id.saoyisao:
				Global.jipaoWeightChild = weight.getText().toString();
				startActivityForResult(new Intent(OneToMoreActivity.this,
						CaptureActivity.class), 1);
				break;
			case R.id.add:
				if (!childmail.getText().toString().trim().equals("")) {
					if (!childmail.getText().toString().equals(mainmail)) {
						if (!childMailExits(childmail.getText().toString())) {
							Map<String, String> tempHashMap = new HashMap<String, String>();
							tempHashMap.put("mainmail", mainmail);
							tempHashMap.put("childmail", childmail.getText()
									.toString().trim());
							tempHashMap.put("weight", weight.getText()
									.toString().trim());
							list1.add(tempHashMap);
							// Allweight=Allweight+Float.parseFloat(weight.getText().toString());
							allweight.setText(Allweight + "");
							childmail.setText("");
							weight.setText("");
							adapter.notifyDataSetChanged();
						} else {
							showdiag("子单号已存在");
						}

					} else {
						showdiag("子单号和主单号不能相同");
					}
				} else {
					showdiag("请正确输入子单号");
				}

				break;
			case R.id.jipao:
				intent = new Intent(OneToMoreActivity.this, JiPaoActivity.class);
				Bundle bundle2 = new Bundle();
				bundle2.putString("flag", "1");
				intent.putExtras(bundle2);
				startActivity(intent);
				break;
			// case R.id.save:
			// if (list1.size()>0) {
			// Log.e("list1", "list2"+list1.size());
			// Intent intent1 = new Intent(OneToMoreActivity.this,
			// NoClctActivity.class);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("list1", list1);
			// intent1.putExtras(bundle);
			// setResult(0,intent1);
			// Toast.makeText(OneToMoreActivity.this, "添加成功",
			// Toast.LENGTH_SHORT).show();
			//
			// }else {
			// Toast.makeText(OneToMoreActivity.this, "请填写子单号",
			// Toast.LENGTH_LONG).show();
			// }
			// finish();
			// break;
			default:
				break;
			}
		}
	};

	public void showDeleteDialog(final SimpleAdapter adapter,
			final ArrayList<Map<String, String>> list, final int position) {
		if (dialog == null) {
			dialog = new Dialog(OneToMoreActivity.this, R.style.dialogss);
		}
		view = View.inflate(OneToMoreActivity.this, R.layout.dialog_delete,
				null);
		Button cancel = (Button) view.findViewById(R.id.info_cancel);
		Button sure = (Button) view.findViewById(R.id.info_sure);
		dialog.setContentView(view, new LayoutParams(
				BaseActivity.width * 15 / 20, LayoutParams.WRAP_CONTENT));

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				dialog = null;
			}
		});
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				list.remove(position);
				adapter.notifyDataSetChanged();
				dialog = null;
			}
		});
		dialog.show();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(oneToMoreReceiver);
		super.onPause();
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_BLUTTOOTH_MSG.equals(intent.getAction())) {
				String code = intent.getStringExtra("code");
				if (code != null) {
					childmail.setText(code);
				}
			}
		}
	}
}

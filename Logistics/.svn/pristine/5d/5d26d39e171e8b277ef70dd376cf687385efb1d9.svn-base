package com.ems.express.ui.message.receive;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.R.id;
import com.ems.express.R.layout;
import com.ems.express.R.style;
import com.ems.express.adapter.message.MailDeliverAdapter;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.SpfsUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReceiveMailActivity extends Activity implements OnClickListener{
	
	private Context mContext;
	
	private ListView listView;
	private MailDeliverAdapter adapter;
	private Dialog dialog;
	private List<DeliveryMessageBean> dmList;
//	private ImageView imgView;
	
	private RelativeLayout mNotPackage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_mail);
		mContext = this;
		
//		Timer timer = new Timer();
//		timer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				//测试数据
//				addTestData();
//				Intent intent = new Intent("NewMsgReceiver_Action"); 
//				sendBroadcast(intent);
//			}
//		}, 10, 1000*10);
		
		initView();
		loadView();
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
	
	void addTestData(){
		App.dbHelper.insertDeliveryMessage(App.db, 
		12.3, 32.1, "18510811449", 
		"11cc02c16e1f366d9759aca9fd23d62b", "1009", "张三丰",
		"88888888", "123", "1231", "1",
		"2015-03-31 14:05", "1", "ED000058385CN", "123123",
		"123", SpfsUtil.loadPhone(),"1");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 999:
			System.out.println("扫扫待处理");
			if(data!=null){
				String mailNum = data.getStringExtra("mailNum");
				String resultStr = data.getExtras().getString("txtResult");
				if(resultStr.isEmpty()){
					DialogUtils.successMessage(this, "扫件失败");
					return;
				}else if(!resultStr.equals(mailNum)){
					DialogUtils.successMessage(this, "邮件不匹配");
					return;
				}else{
				//签收
				MailDeliverAdapter.receiptHandle(data.getIntExtra("index", 0));
//				MessageActivity.setPromptMessage();
				}
			}
			break;

		default:
			break;
		}
	}
	
	
	
	
	public static void actionStart(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ReceiveMailActivity.class);
		context.startActivity(intent);
	}
	
	public void back(View v) {
		finish();
	}
	
	void initView(){
		listView = (ListView)this.findViewById(R.id.list_deliver);
//		imgView = (ImageView)this.findViewById(R.id.iv_nomessage);
		
		mNotPackage = (RelativeLayout) this.findViewById(R.id.rl_notpackage);
		((TextView) findViewById(R.id.tv_title)).setText("收件");
		
//		((TextView) findViewById(R.id.tv_info)).setText("删除");
//		((TextView) findViewById(R.id.tv_info)).setVisibility(View.VISIBLE);
//		((TextView) findViewById(R.id.tv_info)).setOnClickListener(this);
		
		
	}
	void loadView(){
		dmList = App.dbHelper.queryAllDeliveryMessage(App.db);
		adapter  =new MailDeliverAdapter(mContext, dmList);
		listView.setAdapter(adapter);
		int count = listView.getCount();
//		if(count==0){
//			imgView.setVisibility(View.VISIBLE);
//		}else{
//			imgView.setVisibility(View.GONE);
//		}
		if(count==0){
			mNotPackage.setVisibility(View.VISIBLE);
		}else{
			mNotPackage.setVisibility(View.GONE);
		}
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				deleteConfirm(arg2);
				return false;
			}
		});
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.tv_info){
			//删除所有已读数据
			App.dbHelper.deleteReadedDelivery(App.db);
//			dmList.clear();
//			dmList = App.dbHelper.queryAllDeliveryMessage(App.db);
//			adapter.notifyDataSetChanged();
			dmList = App.dbHelper.queryAllDeliveryMessage(App.db);
			adapter  =new MailDeliverAdapter(mContext, dmList);
			listView.setAdapter(adapter);
		}
		
	}
	//删除确认
	public void deleteConfirm(final int position){
		final Dialog dialog = new Dialog(mContext,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText("确认要删除此信息吗？\n(若未收件，建议保留)");;
		
		Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
		buttonDetermine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				App.dbHelper.deleteDeliveryMessageIsDelId(App.db, dmList.get(position).getDelId()+"");
//				dmList = App.dbHelper.queryAllDeliveryMessage(App.db);
				dmList.remove(position);
				adapter.notifyDataSetChanged();
				
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}	
	
	
	
	
	
}

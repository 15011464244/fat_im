package com.newcdc.activity.payment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;

import com.newcdc.R;
import com.newcdc.adapter.MoneyAdapter;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.MoneyDao;
import com.newcdc.model.MoneyBean;
import com.newcdc.model.MoneyListBean;
import com.newcdc.tools.Utils;

/**
 * @author hanrong
 * @version 创建时间：2014-12-5 下午3:01:36 
 * 类说明 ：缴款明细页
 */
public class MoneyActivity extends Activity {
	private ListView mdetail_listview;
	private List<MoneyBean> list;
	private MoneyListBean listBean;
	private String isLT;
	private DeliverDaoFactory daoFactory;
	private MoneyDao moneyDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.moneyactivity_layout);
		daoFactory = DeliverDaoFactory.getInstance();
		moneyDao = daoFactory.getMoneyDao(MoneyActivity.this);
		if(getIntent().getExtras() != null){
//			listBean = (MoneyListBean) getIntent().getExtras().getSerializable(
//					"moneyBeans");
//			list = listBean.getList();
			isLT = getIntent().getExtras().getString(
					"moneytype");
		}
		inits();
	}

	private void inits() {
		if ("lan".equals(isLT)) {
			list = moneyDao.queryBy_Money_Detail("1");
		}else if("tou".equals(isLT)){
			list = moneyDao.queryBy_Money_Detail("0");
		}
		mdetail_listview = (ListView) findViewById(R.id.detail_listview);
		MoneyAdapter adapter = new MoneyAdapter(MoneyActivity.this, list);
		mdetail_listview.setAdapter(adapter);
		findViewById(R.id.btn_money_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MoneyActivity.this.finish();
			}
		});
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(MoneyActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}
}

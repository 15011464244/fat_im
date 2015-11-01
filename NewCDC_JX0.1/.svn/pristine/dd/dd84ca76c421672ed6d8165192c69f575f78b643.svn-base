package com.newcdc.activity.censcus;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.db.CustomerDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.FastLanDao;
import com.newcdc.db.QsjGndmDao;
import com.newcdc.model.FastLanBean;
import com.newcdc.tools.Utils;

/**
 * @author 
 */
@SuppressLint("NewApi")
public class LanTouDetailActivity extends Activity implements OnClickListener {
	private int _id = 0;
	private FastLanDao fastLanDao;// 数据库工具类
	private List<FastLanBean> bean=new ArrayList<FastLanBean>();
	private TextView tx_dannub;
	private TextView tx_mailnub;
	private TextView tx_dakehu;
	private TextView tx_jidadi;
	private TextView tx_weight;
	private TextView tx_shishoukuan;
	private TextView tx_wupinleixing;
	private TextView tx_shifoudaofu;
	private TextView tv_feilei;
	private TextView tx_yunshu;
	private TextView tx_gekou;
	private TextView gekouname;
	private TextView sourthcode;
	private TextView tx_fandanhao;
	private Button btn_back;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lantoudetail);
		
		init();
		_id=getIntent().getIntExtra("detail_id", -1);
		queryData();
		btn_back.setOnClickListener(this);
	}

	/**
	 * 初始化组件
	 * **/
	private void init() {
		btn_back = (Button) findViewById(R.id.deliver_back);
		tx_mailnub=(TextView) findViewById(R.id.tx_mailnub);
		tx_dakehu=(TextView) findViewById(R.id.tx_dakehu);
		tx_jidadi=(TextView) findViewById(R.id.tx_jidadi);
		tx_weight=(TextView) findViewById(R.id.tx_weight);
		tx_shishoukuan=(TextView) findViewById(R.id.tx_shishoukuan);
		tx_wupinleixing=(TextView) findViewById(R.id.tx_wupinleixing);
		tx_shifoudaofu=(TextView) findViewById(R.id.tx_shifoudaofu);
		tv_feilei=(TextView) findViewById(R.id.tv_feilei);
		tx_yunshu=(TextView) findViewById(R.id.tx_yunshu);
		tx_gekou=(TextView) findViewById(R.id.tx_gekou);
		tx_fandanhao=(TextView) findViewById(R.id.tx_fandanhao);
		tx_dannub=(TextView) findViewById(R.id.tx_dannub);
		gekouname=(TextView) findViewById(R.id.tx_gekouname);
		sourthcode=(TextView) findViewById(R.id.tx_sourthcode);
		
		
	}


	
	private void queryData() {
		
		fastLanDao=DeliverDaoFactory.getInstance().getFastLanDao(getApplicationContext());
		bean = fastLanDao.query_id(_id);
		if (bean.get(0).getDan_num()!=null) {
			tx_dannub.setText(bean.get(0).getDan_num());
		}
		if (bean.get(0).getMailNum()!=null) {
			tx_mailnub.setText(bean.get(0).getMailNum());
		}
		if (bean.get(0).getCustomer()!=null) {
			CustomerDao customerDao = DeliverDaoFactory.getInstance()
					.getCustomerDao(LanTouDetailActivity.this);
			tx_dakehu.setText(customerDao.FindDaName(bean.get(0).getCustomer()));
		}
		if (bean.get(0).getRcvArea()!=null) {
			QsjGndmDao qsjGndmDao = DeliverDaoFactory.getInstance()
					.getQsjGndmDao(LanTouDetailActivity.this);
			tx_jidadi.setText(qsjGndmDao.FindDaName(bean.get(0).getRcvArea()));
//			tx_jidadi.setText(bean.get(0).getRcvArea());
		}
     	if (bean.get(0).getActualWeight()!=null) {
		tx_weight.setText(bean.get(0).getActualWeight());	   
		}
    	if (bean.get(0).getActualTotalFee()!=null) {
    		tx_shishoukuan.setText(bean.get(0).getActualTotalFee());	   
    		}
    	if (tx_dannub.getText()==null||tx_dannub.getText().toString().equals("")) {
    		tx_wupinleixing.setText("");
    	}else {
    		if (bean.get(0).getFoodType().equals("1")) {
        		tx_wupinleixing.setText("文件");	   
        	}else if(bean.get(0).getFoodType().equals("3")){
        			tx_wupinleixing.setText("物品");	   
    			}else{
    				tx_wupinleixing.setText("");	   
    			}
		}
    	if (bean.get(0).getDaoFu().equals("1")){
    		tx_shifoudaofu.setText("否");	   
    		}else {
    			tx_shifoudaofu.setText("是");
			}
    	if (bean.get(0).getFenlei()!=null) {
    		tv_feilei.setText(bean.get(0).getFenlei());	   
    		}
    	if (bean.get(0).getYunshu()!=null) {
    		String trans = "";
    		if ("".equals(bean.get(0).getYunshu())) {
    			trans="";
    		} else if ("0".equals(bean.get(0).getYunshu())) {
    			trans ="全程陆运"; // 运输方式
    		} else if ("4".equals(bean.get(0).getYunshu())) {
    			trans = "特殊物品"; // 运输方式
    		}else if ("A".equals(bean.get(0).getYunshu())) {
    			trans="无"; // 运输方式
    		}
    		tx_yunshu.setText(trans);	   
    		}
    	if (bean.get(0).getGekouname()!=null) {
    		tx_gekou.setText(bean.get(0).getGekouname());	   
    		}
    	if (bean.get(0).getReturnLiuShui()!=null) {
    		gekouname.setText(bean.get(0).getReturnLiuShui());	   
    		}
    	if (bean.get(0).getSourthcode()!=null) {
    		sourthcode.setText(bean.get(0).getSourthcode());	   
        	}
    	if (bean.get(0).getReturnMailNum()!=null) {
    		tx_fandanhao.setText(bean.get(0).getReturnMailNum());	   
    		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.deliver_back:
			finish();
			break;
		
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Utils.startIntentService(LanTouDetailActivity.this);// 启动投递、揽收异步上传服务
		super.onResume();
	}

}

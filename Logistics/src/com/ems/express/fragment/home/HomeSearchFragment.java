package com.ems.express.fragment.home;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.mail.MailTransItemAdapter;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.scan.activity.CaptureActivity;
import com.ems.express.ui.Home2Activity;
import com.ems.express.ui.check.ResultActivity;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class HomeSearchFragment extends Fragment implements OnClickListener{
	private ImageButton mScan;
	private EditText mOrderId;
	private TextView mSearch;
	private ListView mSeaHis;
	
//	private ImageButton mScan;
	
	private View view;
	
	private MailTransItemAdapter adapter;
	
	private Context mContext;
	
	private AutoCompleteTextView autoCompleteTextView;
	private TextView mClear;
	
	private ArrayAdapter<String>   autoCompleteAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.home_search_fragment, null);
		mContext = this.getActivity();
		initView();
		loadView();
		return view;
	}
	
	public void initView(){
		mOrderId = (EditText) view.findViewById(R.id.et_orderid);
		mSearch = (TextView) view.findViewById(R.id.tv_search);
		mSeaHis = (ListView) view.findViewById(R.id.lv_history);
		mScan = (ImageButton) view.findViewById(R.id.ib_scan);
		mScan.setOnClickListener(this);
		mClear = (TextView) view.findViewById(R.id.tv_clear);
		mClear.setOnClickListener(this);
		
		autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.et_orderid);//找到相应的控件
//		String[] countries = new String[]{"Afghanistan","Albania","Algeria","American Samoa","Andorra"};//这里只举例说明，数据较少
//		  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, countries);
		   
//	        autoCompleteTextView.setAdapter(adapter);
		initAutoComplete("history", autoCompleteTextView);
		//隐藏扫件
		Home2Activity parent = (Home2Activity)getActivity();
		parent.showScan(true);
		parent.showSign(false);
		
		mSearch.setOnClickListener(this);
	}
	
	public void loadView(){
		List<SendNoticeBean> orders = App.dbHelper.querySendNoticeByOrderStatus(App.db, "4", null);
		if(orders != null && orders.size()>0){
			final List<String> mails = new ArrayList<String>();
			for (int i = 0; i < orders.size(); i++) {
				SendNoticeBean order = orders.get(i);
				if(order != null){
					String mailNum = orders.get(i).getMailNum();
					if(!mailNum.contains(",")){
						mails.add(mailNum);
					}else{
						String[] split = mailNum.split(",");
						for (String string : split) {
							mails.add(string);
						}
					}
				}
			}
			
			adapter = new MailTransItemAdapter(mContext, mails);
			mSeaHis.setAdapter(adapter);
			mSeaHis.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					if(imm.isActive()&& getActivity().getCurrentFocus()!=null){
						if ( getActivity().getCurrentFocus().getWindowToken()!=null) {
						imm.hideSoftInputFromWindow( getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
						}			  
					 }

					search(mails.get(arg2));
					
				}
			});
		}
	}

	@Override
	public void onClick(View view) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		switch (view.getId()) {
//		case R.id.ib_scan:
//			Intent intent1 = new Intent(mContext, CaptureActivity.class);
//			startActivityForResult(intent1, 999);
//			break;
		case R.id.tv_search:
			
			saveHistory("history", autoCompleteTextView);
			
			if(imm.isActive()&& getActivity().getCurrentFocus()!=null){
				if ( getActivity().getCurrentFocus().getWindowToken()!=null) {
				imm.hideSoftInputFromWindow( getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}			  
			 }
			search(mOrderId.getEditableText().toString());
			
			break;
		case R.id.ib_scan:
			if(imm.isActive()&& getActivity().getCurrentFocus()!=null){
				if ( getActivity().getCurrentFocus().getWindowToken()!=null) {
				imm.hideSoftInputFromWindow( getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}			  
			 }
			
			Intent intent1 = new Intent(mContext, CaptureActivity.class);
			((Activity)mContext).startActivityForResult(intent1, 999);
			break;
		case R.id.tv_clear:
			SpfsUtil.saveHistory("");
			initAutoComplete("history", autoCompleteTextView);
			break;

		default:
			break;
		}
	}
	
	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 999:
			System.out.println("扫扫待处理");
			if(data!=null){
				Bundle bundle = data.getExtras();
				String resultStr = bundle.getString("txtResult");
				mOrderId.setText(resultStr);
				search(mOrderId.getEditableText().toString());
			}
			break;

		default:
			break;
		}
	}*/
	
	/** 
     * 初始化AutoCompleteTextView，最多显示5项提示，使 
     * AutoCompleteTextView在一开始获得焦点时自动提示 
     * @param field 保存在sharedPreference中的字段名 
     * @param auto 要操作的AutoCompleteTextView 
     */  
    private void initAutoComplete(String field,AutoCompleteTextView auto) {  
//        SharedPreferences sp = getSharedPreferences("network_url", 0);  
//        String longhistory = sp.getString("history", "nothing");  
    	String longhistory = SpfsUtil.getHistory();
        String[]  hisArrays = longhistory.split(",");  
        autoCompleteAdapter = new ArrayAdapter<String>(getActivity(),  
                android.R.layout.simple_dropdown_item_1line, hisArrays);
        //只保留最近的50条的记录  
        if(hisArrays.length > 50){  
            String[] newArrays = new String[50];  
            System.arraycopy(hisArrays, 0, newArrays, 0, 50);  
            autoCompleteAdapter = new ArrayAdapter<String>(getActivity(),  
                    android.R.layout.simple_dropdown_item_1line, newArrays);  
        } 
        	
        auto.setAdapter(autoCompleteAdapter);  
        auto.setDropDownHeight(530);  
//        auto.setDropDownVerticalOffset(3);
        auto.setThreshold(0);  
//        auto.setCompletionHint("最近的5条记录");  
        auto.setOnFocusChangeListener(new OnFocusChangeListener() {  
            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  
                AutoCompleteTextView view = (AutoCompleteTextView) v;  
                if (hasFocus) {  
                        view.showDropDown();  
                }  
            }  
        }); 
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			}
		});
    }  
	
	 /** 
     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段 
     * @param field  保存在sharedPreference中的字段名 
     * @param auto  要操作的AutoCompleteTextView 
     */ 
	 private void saveHistory(String field,AutoCompleteTextView auto) {  
	        String text = auto.getText().toString();  
//	        SharedPreferences sp = getSharedPreferences("network_url", 0); 
	        String longhistory=SpfsUtil.getHistory();
//	        String longhistory = sp.getString(field, "nothing");  
	        if (!longhistory.contains(text + ",")) {  
	            StringBuilder sb = new StringBuilder(longhistory);  
	            sb.insert(0, text + ",");  
	            SpfsUtil.saveHistory(sb.toString());
	            initAutoComplete("history", auto);
	        }
	        } 
	
	
	public void search(String id) {
//		String id = mOrderId.getEditableText().toString();
		//统计查件次数
		MobclickAgent.onEvent(mContext,"Search mail");
		if (TextUtils.isEmpty(id)) {
			ToastUtil.show(mContext, "运单号不能为空");
		} else {
			// TODO 跳转到运单查询界面
			ResultActivity.actionStart(mContext, id);
		}
	}


}

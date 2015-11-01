package com.ems.express.util;

import java.util.ArrayList;
import java.util.List;

import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.City;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RangeSelectUtil2 {
	
	 	ArrayAdapter<Object> provinceAdapter = null;  //省级适配器
	    ArrayAdapter<Object> cityAdapter = null;    //地级适配器
	    ArrayAdapter<Object> countyAdapter = null;    //县级适配器
	    
	    public List<City> provList = App.dbHelper.queryProvinceList(App.db);
		public List<City> cityList = new ArrayList<City>();
		public List<City> countyList = new ArrayList<City>();
		
		int proindex = 0;
		int cityindex = 0;
		int countyindex = 0;
		
		Spinner provinceSpinner;
		Spinner citySpinner;
		Spinner countySpinner;
		
		Context mContext;
	
		public RangeSelectUtil2(Context mContext,Spinner provinceSpinner,Spinner citySpinner,Spinner countySpinner) {
			this.provinceSpinner = provinceSpinner;
			this.citySpinner = citySpinner;
			this.countySpinner = countySpinner;
			this.mContext = mContext;
		}
		
		/**
		 * 初始化index
		 */
		public void initIndexByCode(int provCode,int cityCode,int countyCode){
//			provList = App.dbHelper.queryProvinceList(App.db);
    		
    		for (int j = 0; j < provList.size(); j++) {
    			if(provCode == provList.get(j).getCode()){
    				proindex = j;
    				Log.e("province", provList.get(j)+"");
    				break;
    			}
			}
    		
    		cityList = App.dbHelper.queryCityList(App.db,provList.get(proindex).getCode()+"");
    			
    		for (int j = 0; j < cityList.size(); j++) {
    			if(cityCode == cityList.get(j).getCode()){
    				cityindex = j;
    				Log.e("city", cityList.get(j)+"");
    				break;
    			}
			}
    		
    		countyList = App.dbHelper.queryCountyList(App.db,cityList.get(cityindex).getCode()+"");
    		
    		for (int j = 0; j < countyList.size(); j++) {
    			if(countyCode == countyList.get(j).getCode()){
    				countyindex = j;
    				Log.e("county", countyList.get(j)+"");
    				break;
    			}
			}
		}
		
		/**
		 * 初始化index
		 */
		private void initIndexByName(String provName,String cityName,String countyName){
//			provList = App.dbHelper.queryProvinceList(App.db);
    		
    		for (int j = 0; j < provList.size(); j++) {
    			if(provName.contains(provList.get(j).getName())){
    				proindex = j;
    				Log.e("province", provList.get(j)+"");
    				break;
    			}
			}
    		
    		cityList = App.dbHelper.queryCityList(App.db,provList.get(proindex).getCode()+"");
    			
    		for (int j = 0; j < cityList.size(); j++) {
    			if(cityName.contains(cityList.get(j).getName())){
    				cityindex = j;
    				Log.e("city", cityList.get(j)+"");
    				break;
    			}
			}
    		
    		countyList = App.dbHelper.queryCountyList(App.db,cityList.get(cityindex).getCode()+"");
    		
    		for (int j = 0; j < countyList.size(); j++) {
    			if(countyName.contains(countyList.get(j).getName())){
    				countyindex = j;
    				Log.e("county", countyList.get(j)+"");
    				break;
    			}
			}
		}
		
		/**
		 * 初始化省市县列表
		 */
		public void initCityList(){
//			provList =  App.dbHelper.queryProvinceList(App.db);
			cityList = App.dbHelper.queryCityList(App.db,"11");
			countyList = App.dbHelper.queryCountyList(App.db,"110000");
		}
		
		/**
		 * 初始化adapt
		 */
		public void setAdapter(){
			//绑定适配器和值--省
	        provinceAdapter = new ArrayAdapter<Object>(mContext,
	                R.layout.my_spinner_hint,  provList.toArray());
	        provinceAdapter.setDropDownViewResource(R.layout.my_spinner_down_item);
	        provinceSpinner.setAdapter(provinceAdapter);
	        
	        //绑定适配器和值--市
	        cityAdapter = new ArrayAdapter<Object>(mContext, 
	               R.layout.my_spinner_hint, cityList.toArray());
	        cityAdapter.setDropDownViewResource(R.layout.my_spinner_down_item);
	        citySpinner.setAdapter(cityAdapter);
	        
	        //绑定适配器和值--县
	        countyAdapter = new ArrayAdapter<Object>(mContext, 
	               R.layout.my_spinner_hint, cityList.toArray());
	        countyAdapter.setDropDownViewResource(R.layout.my_spinner_down_item);
	        countySpinner.setAdapter(countyAdapter);
	        
		}
		/**
		 * 刷新选中项
		 */
		private void freshSelected(){
			provinceSpinner.setSelection(proindex,true);  //设置默认选中项，此处为默认选中第4个值
	        
	        //延时加载
	      	new Handler(){
	            @Override
	            public void handleMessage(Message msg) {
	                citySpinner.setSelection(cityindex,true);  
	            }
	            
	        }.sendMessageDelayed(new Message(), 100);
	        
	        new Handler(){
	            @Override
	            public void handleMessage(Message msg) {
	                countySpinner.setSelection(countyindex, true);
	            }
	            
	        }.sendMessageDelayed(new Message(), 200);
		}
		
		/**
		 * 设置选项变更事件
		 */
		public void setListener(){
		      //省级下拉框监听
		        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		        {
		            @Override
		            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
		            {
		            	 provinceSpinner.setSelection(position, true);
		            	 
		            	 cityList = App.dbHelper.queryCityList(App.db, ((City)provinceSpinner.getSelectedItem()) .getCode()+"");
		            	 cityAdapter = new ArrayAdapter<Object>(mContext, 
		                        R.layout.my_spinner_hint, cityList.toArray());
		            	 cityAdapter.setDropDownViewResource(R.layout.my_spinner_down_item);
		                 citySpinner.setAdapter(cityAdapter);

		                 citySpinner.setSelection(0,true);  //默认选中第0个
		                 
		                 
		                 countyList = App.dbHelper.queryCountyList(App.db,((City)citySpinner.getSelectedItem()).getCode()+"");
		                 countyAdapter = new ArrayAdapter<Object>(mContext,
		                        R.layout.my_spinner_hint,  countyList.toArray());
		                 countyAdapter.setDropDownViewResource(R.layout.my_spinner_down_item);
		                 countySpinner.setAdapter(countyAdapter);
		                 
		                 countySpinner.setSelection(0, true);
		                 
		            }
		            @Override
		            public void onNothingSelected(AdapterView<?> arg0)
		            {
		            	ToastUtil.show(mContext, "没有选项");
		            }

		        });
		        //市级下拉监听
		        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		        {
		            @Override
		            public void onItemSelected(AdapterView<?> arg0, View arg1,
		                    int position, long arg3)
		            {
		            	citySpinner.setSelection(position, true);
		                
		                countyList = App.dbHelper.queryCountyList(App.db,((City)citySpinner.getSelectedItem()).getCode()+"");
		                countyAdapter = new ArrayAdapter<Object>(mContext,
		                        R.layout.my_spinner_hint,  countyList.toArray());
		                countyAdapter.setDropDownViewResource(R.layout.my_spinner_down_item);
		                countySpinner.setAdapter(countyAdapter);
		                
		                countySpinner.setSelection(0, true);
		                
		            }
		            @Override
		            public void onNothingSelected(AdapterView<?> arg0)
		            {
		            	ToastUtil.show(mContext, "没有选项");
		            }
		        });
		        //县级下拉监听
		        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		        {
		            @Override
		            public void onItemSelected(AdapterView<?> arg0, View arg1,
		                    int position, long arg3)
		            {
		            	countySpinner.setSelection(position,true);
		            }
		            @Override
		            public void onNothingSelected(AdapterView<?> arg0)
		            {
		            	ToastUtil.show(mContext, "没有选项");
		            }
		        });
		}
		
	/**
	 * 初始化spinner
	 */
	public void loadData(){
		setAdapter();
		freshSelected();
		setListener();
	}
	/**
	 * 根据code刷新列表
	 */
	public void freshByCode(int provCode,int cityCode,int countyCode){
		initIndexByCode(provCode, cityCode, countyCode);
		freshSelected();
	}
	/**
	 * 根据city名刷新列表
	 */
	public void freshByName(String provName,String cityName,String countyName){
		initIndexByName(provName, cityName, countyName);
		freshSelected();
	}
	
	
}

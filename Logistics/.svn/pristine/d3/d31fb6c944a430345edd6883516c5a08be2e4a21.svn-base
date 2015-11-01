package com.ems.express.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import u.aly.ad;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.R.id;
import com.ems.express.R.layout;
import com.ems.express.R.style;
import com.ems.express.bean.JifenBean;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyJifenActivity extends BaseActivityForRequest implements OnClickListener{
	
	private List<JifenBean> jifenALLList;
	private List<JifenBean> jifenInList;
	private List<JifenBean> jifenOutList;
	
	private TextView jifenTotal;
	private RelativeLayout showAll,showIn,showOut;
	
	private ImageView font1,font2,font3;
	private TextView text1,text2,text3;
	
	private AnimationUtil util;
	private String total;
	
	private ListView listView;
	private Context mContext;
	
	private JifenAdapter adapter;
	
	private Map<Integer, String> map = new HashMap<Integer, String>();
	
	private static final int SIGN_TYPE1 = 1;
	private static final int SEND_OUT_TYPE1 = 2;
	private static final int SEND_IN_TYPE1 = 3;
	private static final int REGIST_TYPE1 = 4;
	private static final int SIGN_TYPE2 = 11;
	private static final int SEND_OUT_TYPE2 = 12;
	private static final int SEND_IN_TYPE2 = 13;
	private static final int REGIST_TYPE2 = 14;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_jifen);
		mContext = this;
		setHeadTitle("我的积分");
		init();
	}
	
	private void init(){
		adapter = new JifenAdapter();
		jifenALLList = new ArrayList<JifenBean>();
		jifenInList = new ArrayList<JifenBean>();
		jifenOutList = new ArrayList<JifenBean>();
		
		util = new AnimationUtil(this, R.style.dialog_animation);
		
		jifenTotal = (TextView) findViewById(R.id.tv_jifen_total);
		showAll = (RelativeLayout) findViewById(R.id.rl_show_all);
		showIn = (RelativeLayout) findViewById(R.id.rl_show_in);
		showOut = (RelativeLayout) findViewById(R.id.rl_show_out);
		listView = (ListView) findViewById(R.id.lv_jifen_list);
		
		text1 = (TextView) findViewById(R.id.tv_show_all_text);
		text2 = (TextView) findViewById(R.id.tv_show_in_text);
		text3 = (TextView) findViewById(R.id.tv_show_out_text);
		
		font1 = (ImageView) findViewById(R.id.iv_show_all_font);
		font2 = (ImageView) findViewById(R.id.iv_show_in_font);
		font3 = (ImageView) findViewById(R.id.iv_show_out_font);
		
		map.put(1,"签到" );
		map.put(2,"寄件使用" );
		map.put(3,"寄件获得" );
		map.put(4,"注册获得" );
		map.put(5, "邀请好友");
		
		map.put(11,"签到成功赠送积分" );
		map.put(12,"寄件金额总计：" );
		map.put(13,"实收金额总计：" );
		map.put(14,"注册成功赠送积分" );
		map.put(15, "邀请成功赠送积分");
		
		showAll.setOnClickListener(this);
		showIn.setOnClickListener(this);
		showOut.setOnClickListener(this);
		
		changeStyle(text1, font1);
		
		getJifenTotal(SpfsUtil.loadPhone());
		getJifenList(SpfsUtil.loadPhone());
		
	}
	/**
	 * 获取积分
	 * @param phone
	 */
	private void getJifenTotal(String phone){
		
		//开启菊花
				util.show();
				HashMap<String, Object> json = new HashMap<String, Object>();
				json.put("mobile", phone);
				String params = ParamsUtil.getUrlParamsByMap(json);
				MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.jifenTotal,
						new Response.Listener<Object>() {

							@Override
							public void onResponse(Object arg0) {
								LogUtils.e("jifenTotal_response == "+arg0+"");
								//关闭菊花
								util.dismiss();
								//请求成功
								if(((BaseActivityForRequest)mContext).stayThisPage){
									try {
										String result = (String) arg0;
										JSONObject object = new JSONObject(result.toString());
										if (object.getInt("result") == 1) {
											JSONObject content = object.getJSONObject("content");
											//剩余积分
											total =content.getString("surplusIntegral");
											jifenTotal.setText(total);
										} else {
											//请求失败
//											ToastUtil.show(mContext,"请求失败");
										}
									} catch (Exception e) {
//										ToastUtil.show(mContext,"数据解析异常");
										e.printStackTrace();
									}
								}
							}
						
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								//关闭菊花
								util.dismiss();
								Toast.makeText(mContext, "请求异常!", Toast.LENGTH_LONG).show();
								arg0.printStackTrace();
							}
						}, params);
				App.getQueue().add(req);
		
	}
	
	/**
	 * 获取积分列表
	 * @param phone
	 */
	private void getJifenList(String phone){
		
		//开启菊花
				util.show();
				HashMap<String, Object> json = new HashMap<String, Object>();
				json.put("mobile", phone);
				String params = ParamsUtil.getUrlParamsByMap(json);
				MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.jifenList,
						new Response.Listener<Object>() {

							@Override
							public void onResponse(Object arg0) {
								LogUtils.e("jifenList_response == "+arg0);
								//关闭菊花
								util.dismiss();
								//请求成功
								if(((BaseActivityForRequest)mContext).stayThisPage){
									try {
										String result = (String) arg0;
										JSONObject object = new JSONObject(result.toString());
										if (object.getInt("result") == 1) {
											org.json.JSONArray dataList = object.getJSONArray("dataList");
											//剩余积分
											for (int i = 0; i < dataList.length(); i++) {
												JifenBean bean = new JifenBean();
												String objStr = dataList.get(i).toString();
												JSONObject obj = new JSONObject(objStr);
												String userId = obj.getString("userSid");
												bean.setUserId(userId);
												String createTime = obj.getString("createTime");
												bean.setCreateTime(createTime);
												String operateType = obj.getString("operateType");
												bean.setOperateType(operateType);
												String changeType = obj.getString("changeType");
												bean.setChangeType(changeType);
												int integral = obj.getInt("integral");
												bean.setIntegral(integral);
												if(objStr.contains("postage")){
													String postage = obj.getString("postage") + "元";
													bean.setPostage(postage);
												}else{
													bean.setPostage("");
												}
												
												if("1".equals(changeType)){
													jifenInList.add(bean);
												}else if("2".equals(changeType)){
													jifenOutList.add(bean);
												}
												jifenALLList.add(bean);
											}
											adapter.setDataList(jifenALLList);
											listView.setAdapter(adapter);
											
										} else {
											//请求失败
											if(result.contains("errorMsg")){
												ToastUtil.show(mContext,object.getString("errorMsg"));
											}else{
												ToastUtil.show(mContext,"用户还没有积分信息");
											}
											
										}
									} catch (Exception e) {
										ToastUtil.show(mContext,"jifenList_  数据解析异常");
										e.printStackTrace();
									}
								}
							}
						
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								//关闭菊花
								util.dismiss();
								Toast.makeText(mContext, "请求异常!", Toast.LENGTH_LONG).show();
								arg0.printStackTrace();
							}
						}, params);
				App.getQueue().add(req);
		
	}
	
	class JifenAdapter extends BaseAdapter{
		private List<JifenBean> list;
		public JifenAdapter(List<JifenBean> list) {
			this.list = list;
		}
		
		public JifenAdapter() {
		}
		
		public void setDataList(List<JifenBean> list){
			this.list = list;
		}
		
		public void notifyList(List<JifenBean> list){
			this.list = list;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			if(list != null && list.size() >0){
				return list.size();
			}
			return 0;
		}

		@Override
		public JifenBean getItem(int i) {
			// TODO Auto-generated method stub
			return  list.get(i);
		}
		

		@Override
		public long getItemId(int i) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.jifen_list_item, null);
				new ViewHolder(view);
			}
			ViewHolder holder = (ViewHolder) view.getTag();
			JifenBean item = getItem(i);
			holder.time.setText(item.getCreateTime());
			holder.type1.setText(map.get(Integer.valueOf(item.getOperateType())));
			holder.type2.setText(map.get(10+Integer.valueOf(item.getOperateType()))+item.getPostage());
			String addStr = "";
			if("1" .equals(item.getChangeType())){
				addStr = "+"+item.getIntegral();
			}else if("2" .equals(item.getChangeType())){
				addStr = "-"+item.getIntegral();
			}
			
			holder.add.setText(addStr);
			return view;
		}
		
		class ViewHolder {
			TextView type1,type2,time,add;

			public ViewHolder(View view) {
				type1 = (TextView) view.findViewById(R.id.tv_jifen_type1);
				type2 = (TextView) view.findViewById(R.id.tv_jifen_type2);
				time = (TextView) view.findViewById(R.id.tv_time);
				add = (TextView) view.findViewById(R.id.tv_jifen_add);
				view.setTag(this);
			}
			
			public void setOnclickEvent(final String packageName){
				
			}
		}
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rl_show_all:
			changeStyle(text1, font1);
			adapter.notifyList(jifenALLList);
			break;
		case R.id.rl_show_in:
			changeStyle(text2, font2);
			adapter.notifyList(jifenInList);
			break;
		case R.id.rl_show_out:
			changeStyle(text3, font3);
			adapter.notifyList(jifenOutList);
			break;

		default:
			break;
		}
		
	}
	/**
	 * 改变按钮的style
	 * @param textV
	 * @param imgV
	 */
	void changeStyle(TextView textV,ImageView imgV){
		text1.setSelected(false);
		text2.setSelected(false);
		text3.setSelected(false);
		
		font1.setVisibility(View.GONE);
		font2.setVisibility(View.GONE);
		font3.setVisibility(View.GONE);
		
		textV.setSelected(true);
		imgV.setVisibility(View.VISIBLE);
		
		
	}
	public void back(View v) {
		finish();
	}
}

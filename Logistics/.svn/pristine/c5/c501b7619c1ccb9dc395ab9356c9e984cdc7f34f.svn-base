package com.ems.express.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.ems.express.App;
import com.ems.express.adapter.mail.MailHistoryItemAdapter;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.bean.MailInfo;
import com.ems.express.constant.Constant;
import com.ems.express.fragment.mail.MailHistoryFragment;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.mail.MailOrderListActivity;
import com.umeng.analytics.MobclickAgent;

public class RequestUtil {
	/**
	 * 撤单样式设定
	 * @return
	 */
	public static void turnBackStyle(final Context mContext,final String sid,final AnimationUtil animationUtil,/*刷新用*/final List<MailInfo> mList){
		if(null == sid){
			ToastUtil.show(mContext, "订单号为空");
			return;
		}
		//开启菊花
		animationUtil.show();
		//设置参数
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("orderIid", sid);
		json.put("userCode",SpfsUtil.loadPhone());
//		json.put("userCode","258729103277791");
		json.put("optTime",DateTimeUtil.dateTimeFormat.format(new Date()));
		json.put("dataSource", 2);
		json.put("deviceNo", DeviceUtil.getDeviceNo(mContext));
		json.put("deviceType", "android");
		json.put("reasonCode", 26);
		json.put("reasonRemark", "其他");
		//添加机构号和员工姓名
		SendNoticeBean bean = App.dbHelper.querySendMessageBySid(App.db, sid);
		if(null != bean){
			//上海
//			json.put("employee_num", bean.getEmployeeNo());
			//江西
			json.put("emploveeNo", bean.getEmployeeNo());
			
			json.put("orgCode", bean.getOrgcode());
		}
		String params = ParamsUtil.getUrlParamsByMap(json);
		Log.e("turnBackParams", params.toString());
		//撤单请求
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null,Constant.TurnBackOrder, new Listener<Object>() {
			@Override
			public void onResponse(Object resp) {
				Log.e("msgggTurnBack", resp.toString());
				if (resp == null || resp.toString().isEmpty()) {
//					DialogUtils.getNullCommentDialog(mContext, "退单失败！");
					DialogUtils.noticeDialog(mContext, "您的订单退订失败\n请您稍后再试", "知道了");
					//关闭菊花
					animationUtil.dismiss();
					return;
				}
				JSONObject jso;
				try {
					jso = new JSONObject(resp.toString());
					if ("1".equals(jso.get("result"))) {
//						DialogUtils.getNullCommentDialog(mContext, "退单成功！");
						DialogUtils.noticeDialog(mContext, "您的订单已经成功退订\n感谢您使用快递帮手", "知道了");
						
						boolean deleteOrder = App.dbHelper.deleteOrder(App.db, sid);
						Log.e("删除本地订单数据", deleteOrder+"");
						
						//关闭菊花
						animationUtil.dismiss();
//						MailOrderListActivity.actionStart(mContext);
						//去除退单项,刷新界面
						for (MailInfo mailInfo : mList) {
							if(sid.equals(mailInfo.getSid())){
								mList.remove(mailInfo);
//								MailHistoryFragment.listView.setAdapter(new MailHistoryItemAdapter(mContext, mList));
								MailHistoryFragment.historyAdapter.notifyDataSetChanged();
								return;
							}
						}
					}
					else{
						DialogUtils.noticeDialog(mContext, "您的订单退订失败\n请您稍后再试", "知道了");
						//关闭菊花
						animationUtil.dismiss();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError arg0) {
			Log.e("turnBack response", arg0+"");
			DialogUtils.noticeDialog(mContext, "您的订单退订失败\n请您稍后再试", "知道了");
				//关闭菊花
				animationUtil.dismiss();
				//去除退单项,刷新界面
			}
		}, params);
		App.getQueue().add(req);
	}
	
	/**
	 * 催单
	 * @param sid
	 */
	public static void  urge(final Context mContext,String sid,final AnimationUtil animationUtil){
		if(null == sid){
			ToastUtil.show(mContext, "订单号为空");
			return;
		}
		//开启菊花
		animationUtil.show();
		//设置参数
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("orderIid", sid);
		json.put("userCode",SpfsUtil.loadPhone());
//		json.put("userCode","258729103277791");
		json.put("optTime",DateTimeUtil.dateTimeFormat.format(new Date()));
		json.put("dataSource", 2);
		json.put("deviceNo", DeviceUtil.getDeviceNo(mContext));
		json.put("deviceType", "android");
		//添加机构号和员工姓名
		SendNoticeBean bean = App.dbHelper.querySendMessageBySid(App.db, sid);
		if(null != bean){
			json.put("emploveeNo", bean.getEmployeeNo());
			json.put("orgCode", bean.getOrgcode());
		}else{
			ToastUtil.show(mContext, "该订单推送已被您删除，无法催单！");
			//关闭菊花
			animationUtil.dismiss();
			return;
		}
		
		
		String params = ParamsUtil.getUrlParamsByMap(json);
		//崔单请求
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null,Constant.Remind, new Listener<Object>() {
			@Override
			public void onResponse(Object resp) {
				Log.e("msgggRemin", resp.toString());
				if (resp == null || resp.toString().isEmpty()) {
					DialogUtils.noticeDialog(mContext, "催单失败\n请您稍后再试", "知道了");
					
					return;
				}
				JSONObject jso;
				try {
					jso = new JSONObject(resp.toString());
					if ("1".equals(jso.get("result"))) {
						//寄件退单统计
						MobclickAgent.onEvent(mContext,"turnback");
//						DialogUtils.getNullCommentDialog(mContext, "催单成功，快递员会火速赶来取件！");
						DialogUtils.noticeDialog(mContext, "您已催单成功\n快递员会火速赶来取件", "知道了");
						//关闭菊花
						animationUtil.dismiss();
					}
					else{
//						DialogUtils.getNullCommentDialog(mContext, "催单失败，请您重新尝试！");
						DialogUtils.noticeDialog(mContext, "催单失败\n请您稍后再试", "知道了");
						//关闭菊花
						animationUtil.dismiss();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError arg0) {
				DialogUtils.noticeDialog(mContext, "催单失败\n请您稍后再试", "知道了");
				//关闭菊花
				animationUtil.dismiss();
			}
		}, params);
		App.getQueue().add(req);
	}

}

package com.ems.express.adapter.message;

import java.util.List;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.R.color;
import com.ems.express.global.GlobalVar;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.ui.check.ResultActivity;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ToastUtil;

public class MailMeassageAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<SendNoticeBean> mList;
	private String messageIsSign = "2";
	private ViewHolder holder ;
	
	public MailMeassageAdapter(Context context,List<SendNoticeBean> list ) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if(mList != null){
			return mList.size();
		}
		return mList.size();
	}

	@Override
	public SendNoticeBean getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		//消息已读
//		if(!"0".equals(mList.get(position).getMessageStatus())){
//			return 1;
//		}
		//3已取件，10已妥投，4已揽收  , 6由于邀请手机得到积分  
		return Integer.valueOf(mList.get(position).getMailStatus());
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int index = position;
		SendNoticeBean bean = mList.get(index);
		String mailStatus = bean.getMailStatus();
//		final String sid = bean.getSid();
		Log.e("msggg", "mailState:"+bean.getMailStatus());
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mail_message_item, null);
			holder.commonData = (TextView) convertView.findViewById(R.id.tv_time);
			holder.messageName = (TextView) convertView.findViewById(R.id.tv_content);
			holder.body = (LinearLayout)convertView.findViewById(R.id.ll_body);
			holder.sysMsg = (TextView) convertView.findViewById(R.id.sys_msg);
			holder.call = (ImageView)convertView.findViewById(R.id.iv_call);
			holder.isNotRead = (ImageView)convertView.findViewById(R.id.iv_not_read);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		//前面是否有小红点
		if("1".equals(bean.getMessageStatus())){
			holder.isNotRead.setVisibility(View.VISIBLE);
		}else{
			holder.isNotRead.setVisibility(View.GONE);
		}
		if("10".equals(mailStatus)){
			//已妥投处理
			convertView = hand10(convertView, bean);
		}else if("3".equals(mailStatus)){
			//已派单处理
			convertView = hand3(mContext,convertView, bean,index);
		}else if("4".equals(mailStatus)){
			//已揽收处理
			convertView = hand4(mContext,convertView, bean,index);
		}else if("6".equals(mailStatus)){
			//积分消息
			convertView = hand6(mContext,convertView, bean,index);
//			holder.isNotRead.setVisibility(View.VISIBLE);
		}else if("7".equals(mailStatus)){
			//积分消息
			convertView = hand7(mContext,convertView, bean,index);
//			holder.isNotRead.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	} 
	
	

	

	/*
	 * 已妥投处理
	 */
	public View hand10(View convertView, final SendNoticeBean bean){
		if(mList != null){
			holder.sysMsg.setText("已签收");
			holder.commonData.setText(""+bean.getMessageTime());
			String mailNum = bean.getMailNum();
			holder.messageName.setText("您的"+mailNum+"邮件已经成功送达！");
			
			holder.call.setVisibility(View.GONE);
		}
		
//		holder.body.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ResultActivity.actionStart(mContext, bean.getMailNum(), "4");
//			}
//		});
		return convertView;
	}
	/*
	 * 揽投助手通过智能平台传回来的信息
	 */
	private View hand7(Context mContext2, View convertView,
			SendNoticeBean bean, int index) {
		
		return null;
	}
	// 积分消息
		private View hand6(Context mContext2, View convertView,
				SendNoticeBean bean, int index) {
			holder.sysMsg.setText("积分消息");
//			holder.sysMsg.setTextSize(23);
			holder.commonData.setText(""+bean.getMessageTime());
//			holder.messageName.setText("新注册用户"+bean.getInvitedMobile()+",使用了您的邀请手机号获取"+bean.getIntegral()+"积分");
//			holder.messageName.setText(Html.fromHtml("<font color=\'#858585\'>购买前如有任何疑问，欢迎使用：</font><font color=\'#f02387\'><U>购物咨询</U></font>"));
			holder.messageName.setText(Html.fromHtml("<font color=\'#858585\'>新注册用户</font><font color=\'#ff9803\'>"+bean.getInvitedMobile()+"</font><font color=\'#858585\'>,使用了您的邀请手机号获取</font><font color=\'#ff9803\'>"+bean.getIntegral()+"</font><font color=\'#858585\'>积分</font>"));
			holder.call.setVisibility(View.GONE);
			
			return convertView;
		}
	/*
	 * 已取件处理
	 */
	public View hand3(Context context,View convertView, final SendNoticeBean bean,final int index){
		if(mList != null){
			holder.sysMsg.setText("快递员:"+bean.getPeople());
		holder.commonData.setText(""+bean.getMessageTime());
		holder.messageName.setText(/*bean.getOrderNo()+*/"已收到寄件信息，请等待上门取件！");
		
		holder.call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getMobile()));
//                mContext.startActivity(intent);
				if(bean.getMobile()!= null){
					DialogUtils.callConfirm(mContext, bean.getMobile()); 
				}else{
					DialogUtils.getNullCommentDialog(mContext, "电话号码为空！"); 
				}
				
				
			}
		});
		
		}
		/*holder.courier.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,BaiduMapActivity.class);
				intent.putExtra(BaiduMapActivity.KEY_TYPE,
						BaiduMapActivity.TYPE_CARRIER);
				intent.putExtra("LONGITUDE",bean.getLongitude());
				intent.putExtra("LATITUDE",bean.getLatitude());
				intent.putExtra("orgcode",bean.getOrgcode());
				intent.putExtra("username",bean.getUsername());
				intent.putExtra("phoneNum",bean.getMobile());
				intent.putExtra("sendNoticeBean",bean);
				intent.putExtra("messageIsSign", messageIsSign);
				intent.putExtra("activity", "signMessage");
				mContext.startActivity(intent);
			}
		});*/
		
		return convertView;
	}
	
	/*
	 * 已取件处理
	 */
	public  View hand4(Context context,View convertView, final SendNoticeBean bean,final int index){
		if(mList != null){
		holder.sysMsg.setText("快递员:"+bean.getPeople());
		holder.commonData.setText(""+bean.getMessageTime());
		//改变字体颜色
		String str= "";
		if("1".equals(bean.getServiceType())){
			 str = "您的标准寄件已揽收，";
		}else{
			 str = "您的快递包裹已揽收，";
		}
		
		if("3".equals(bean.getPayment())){
			str = str.substring(2);
		}
		
		if("1".equals(bean.getPayment())){
			str = str + "已付款"+bean.getPrice()+"元！";
		}else if("2".equals(bean.getPayment())){
			str = str + "邮件到付！";
		}else if("3".equals(bean.getPayment())){
			str = str + "付款" +bean.getActPrice() +"元，"+"使用" + bean.getIntegral() + "积分！";
		}
		
		SpannableStringBuilder builder = new SpannableStringBuilder(str); 
	
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.rgb(255, 152, 03));
		ForegroundColorSpan redSpan2 = new ForegroundColorSpan(Color.rgb(255, 152, 03));
		ForegroundColorSpan redSpan3 = new ForegroundColorSpan(Color.rgb(255, 152, 03));
		if("3".equals(bean.getPayment())){
			builder.setSpan(redSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		}else{
			builder.setSpan(redSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		}
		if("1".equals(bean.getPayment())){
			builder.setSpan(redSpan2, 13, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		}else if("2".equals(bean.getPayment())){
			builder.setSpan(redSpan2, 12, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		}else if("3".equals(bean.getPayment())){
			builder.setSpan(redSpan2, 10, str.indexOf("元"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			builder.setSpan(redSpan3, str.indexOf("使用")+2, str.indexOf("积分"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		}
		
		holder.messageName.setText(builder);
		
		holder.call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getMobile()));
//                mContext.startActivity(intent);
				if(bean.getMobile()!= null){
					DialogUtils.callConfirm(mContext, bean.getMobile()); 
				}else{
					DialogUtils.getNullCommentDialog(mContext, "电话号码为空！"); 
				} 
				
			}
		});
		
		}
		/*holder.courier.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,BaiduMapActivity.class);
				intent.putExtra(BaiduMapActivity.KEY_TYPE,
						BaiduMapActivity.TYPE_CARRIER);
				intent.putExtra("LONGITUDE",bean.getLongitude());
				intent.putExtra("LATITUDE",bean.getLatitude());
				intent.putExtra("orgcode",bean.getOrgcode());
				intent.putExtra("username",bean.getUsername());
				intent.putExtra("phoneNum",bean.getMobile());
				intent.putExtra("sendNoticeBean",bean);
				intent.putExtra("messageIsSign", messageIsSign);
				intent.putExtra("activity", "signMessage");
				mContext.startActivity(intent);
			}
		});*/
		
		return convertView;
	}
	
	class ViewHolder{
		TextView commonData;
		TextView messageName;
		TextView sysMsg;
		ImageButton courier,delete;
		LinearLayout body;
		
		ImageView call;
		ImageView isNotRead;
	}
	
	public void notifyData(List<SendNoticeBean> list){
		mList = list;
		this.notifyDataSetChanged();
	}
}

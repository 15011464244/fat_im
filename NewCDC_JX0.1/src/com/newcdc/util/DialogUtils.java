package com.newcdc.util;

import android.app.Dialog;

public class DialogUtils{
	private static Dialog dialogfuj;
	private static Dialog dialog ;
	/*public static Dialog getServicePointDialog(ServicePoint point,
			final Context context) {
		dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_service_point, null);
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		title.setText(point.getAUTHNAME());
		TextView people = (TextView) view.findViewById(R.id.dialog_people);
		people.setText("联系人："+point.getCONTACTNAME());
		TextView mobile = (TextView) view.findViewById(R.id.dialog_mobile);
		mobile.setText("电话："+point.getCONTACTMOBILE());
		TextView address = (TextView) view.findViewById(R.id.dialog_address);
		address.setText("地址："+point.getADDRESS());
		TextView startTime = (TextView) view.findViewById(R.id.dialog_start_time);
		startTime.setText("开门时间：09:00");
		TextView stopTime = (TextView) view.findViewById(R.id.dialog_stop_time);
		stopTime.setText("关门时间：18:00");
		final String strmobile = point.getCONTACTMOBILE();
		view.findViewById(R.id.dialog_call).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+strmobile));
				 context.startActivity(intent);//内部类
				 dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	
	public static Dialog getCarrierDialog(final Carrier point,
			final Context context,LatLng mylocaltion) {
		dialogfuj = new Dialog(context,R.style.DialogStyle4);
		dialogfuj.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_carrier, null);
		TextView people = (TextView) view.findViewById(R.id.dialog_people);
		people.setText(point.getPeople());
		TextView mobile = (TextView) view.findViewById(R.id.dialog_mobile);
		mobile.setText("电话:"+point.getMobile());
		
		TextView distane = (TextView) view.findViewById(R.id.dialog_distance);
		distane.setText("距我 " + DistanceUtils.covert(DistanceUtils.GetShortDistance(point.getLongitude(), point.getLatitude()
				, mylocaltion.longitude, mylocaltion.latitude)));
		ImageView imageView = (ImageView)view.findViewById(R.id.img);
		if(!point.getSID().equals("null")&&!point.getSID().equals("")){
			String url = UrlUtils.URL_CARRIER_IMG+"?sid=" + point.getSID();
			LogUtil.print("获取头像地址："+url);
			ImageLoader.getInstance().displayImage(url, imageView);	
		}else{
			Bitmap aa = BitmapFactory.decodeResource(context.getResources(), R.drawable.defualt_header);
			imageView.setImageBitmap(aa);
		}
		view.findViewById(R.id.dialog_notebook).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(point.getClientId().equals("null")||point.getClientId()==null||point.getClientId().equals("")){
					dialogfuj.dismiss();
					ToastUtil.show(context, "不能与离线快递员聊天");
					Log.e("DialogUtils", "clientId为空不能聊天");
					return;
				}
				App.dbHelper.insertChatList(App.db, point);
				ChatListItemBean bean = new ChatListItemBean();
				bean.setImage(point.getSID());
				bean.setMobile(point.getMobile());
				bean.setName(point.getPeople());
				bean.setClientId(point.getClientId());
				MainActivity.startAction(context, bean);
				dialogfuj.dismiss();
			}
		});
		dialogfuj.setContentView(view);
		dialogfuj.show();
		return dialogfuj;
	}*/
	/*public static Dialog getNullCommentDialog(final Context context,String str) {
		Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_null_comment, null);
		TextView tip = (TextView) view.findViewById(R.id.tip);
		tip.setText(str);
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	//评价，寄件成功弹出dialog
	public static Dialog successMessage(final Context context,String str) {
		Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置成系统级别弹窗
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.setCancelable(true);
		
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_null_comment, null);
		TextView tip = (TextView) view.findViewById(R.id.tip);
		tip.setText(str);
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	//评价，寄件成功弹出dialog
		public static Dialog getPaymentDialog(final Context context,String str) {
			final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
			TextView content = (TextView) view.findViewById(R.id.dialog_content);
			content.setText("您确定要注销吗？");
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
					SpfsUtil.saveAddress("");
					SpfsUtil.saveHeadImageUrl("");
					SpfsUtil.saveName("");
					SpfsUtil.saveId("");
					SpfsUtil.savePhone("");
					SpfsUtil.saveTelephone("");
//					((TextView)HomeActivity.slideMenu.findViewById(R.id.btn_regist)).setText("登录");
					Bitmap aa = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_head);
//					HomeActivity.mIconView.setImageBitmap(aa);
//					HomeActivity.mUserNameView.setText("");
					dialog.dismiss();
					Intent intent = new Intent(context,LoginActivity.class);
					context.startActivity(intent);
				}
			});
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			return null;
		}
	
	public static Dialog showTwoButton(final Context context){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText("您确定要注销吗？");
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
				SpfsUtil.saveAddress("");
				SpfsUtil.saveHeadImageUrl("");
				SpfsUtil.saveName("");
				SpfsUtil.saveId("");
				SpfsUtil.savePhone("");
				SpfsUtil.saveTelephone("");
//				((TextView)HomeActivity.slideMenu.findViewById(R.id.btn_regist)).setText("登录");
				Bitmap aa = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_head);
//				HomeActivity.mIconView.setImageBitmap(aa);
//				HomeActivity.mUserNameView.setText("");
				dialog.dismiss();
				Intent intent = new Intent(context,LoginActivity.class);
				context.startActivity(intent);
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return null;
	}
	
	//是否清除缓存
	public static Dialog showTwoButton2(final Context context){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText("清除的数据不能恢复！\n您确定要清除缓存吗？");
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
				dialog.dismiss();
			     App.dbHelper.deleteAllCarrier(App.db);
			     App.dbHelper.deleteAllMessage(App.db);
			     App.dbHelper.deleteAllDeliveryMessage(App.db);
			     App.dbHelper.deleteAllSendMessage(App.db);
				ToastUtil.show(context, "成功清除缓存");
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return null;
	}
	
	public static Dialog getMessageDialog(
			final Context context,List<DeliveryMessageBean> dmList,int position) {
		dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_message_center, null);
		TextView title = (TextView) view.findViewById(R.id.dialog_message_title);
		title.setText("快递员");
		TextView people = (TextView) view.findViewById(R.id.dialog_message_people);
		people.setText("联系人："+dmList.get(position).getPeople());
		TextView mobile = (TextView) view.findViewById(R.id.dialog_message_mobile);
		mobile.setText("电话："+dmList.get(position).getMobile());
		
		
		
		view.findViewById(R.id.message_dialog_call).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel: 150111111111"));
				 context.startActivity(intent);//内部类
				 dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	
public static boolean isMobileNO(String mobiles){  
//	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//	Pattern p = Pattern.compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
	Pattern p = Pattern.compile("^[1][3,4,5,7,8]\\d{9}$");
//	Pattern p1 = Pattern.compile("^(((0\\d{3}[\\-])?\\d{7}|(0\\d{2}[\\-])?\\d{8}))([\\-]\\d{2,4})?$");
	
	
//	Matcher m1 = p1.matcher(mobiles);  
	Matcher m = p.matcher(mobiles);  

	return m.matches();
	} 
public static boolean isPhoneNO(String mobiles){  
//	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//	Pattern p = Pattern.compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
	Pattern p = Pattern.compile("^[1][3,4,5,7,8]\\d{9}$");
	Pattern p1 = Pattern.compile("^(((0\\d{3}[\\-])?\\d{7}|(0\\d{2}[\\-])?\\d{8}))([\\-]\\d{2,4})?$");
	
	
	Matcher m1 = p1.matcher(mobiles);  
	Matcher m = p.matcher(mobiles);  
	if (m.matches()|m1.matches()) {
		return true;
	}
	return false;
	} 
*//**
 * 退单确认
 * @param context
 * @return
 *//*
public static Dialog turnBackConfirm(final Context context,final String sid,final AnimationUtil animationUtil,刷新用到final List<MailInfo> mList){
	final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
	TextView content = (TextView) view.findViewById(R.id.dialog_content);
	content.setText("您确定要取消寄件订单吗？");
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
			dialog.dismiss();
			RequestUtil.turnBackStyle(context, sid,animationUtil,mList);
		}
	});
	dialog.setContentView(view);
	dialog.setCanceledOnTouchOutside(false);
	dialog.show();
	return null;
}



*//**
 * 更新确认
 * @param context
 * @return
 *//*
public static Dialog updateConfirm(final Context context,final String path){
	final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
	TextView content = (TextView) view.findViewById(R.id.dialog_content);
	content.setText("检测到新版本，是否更新？\n版本更新消耗流量，建议在WiFi下进行更新！");
	
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
			dialog.dismiss();
			
		}
	});
	dialog.setContentView(view);
	dialog.setCanceledOnTouchOutside(false);
	dialog.show();
	return null;
}
//删除确认
public static Dialog deleteConfirm(final Context context,final String path){
	final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
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
			
			dialog.dismiss();
		}
	});
	dialog.setContentView(view);
	dialog.setCanceledOnTouchOutside(false);
	dialog.show();
	return null;
}

public static void downApp(String updateUrl){
	
}

*//**
 * 给快递员电话确认
 * @param context
 * @return
 *//*
public static Dialog callConfirm(final Context context,final String phone){
	final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
	TextView content = (TextView) view.findViewById(R.id.dialog_content);
	content.setText("拨号："+phone);
	
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
			dialog.dismiss();
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            context.startActivity(intent);
		}
	});
	dialog.setContentView(view);
	dialog.setCanceledOnTouchOutside(false);
	dialog.show();
	return null;
}


*//**
 * 应用退出确认
 * @param context
 * @return
 *//*
	public static Dialog outConfirm(final Context context){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText("您确定要退出应用？");
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
				dialog.dismiss();
				((Activity)context).finish();
//				System.exit(0);
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return  null;
	}
	
	
*//**
 * 通知提示框
 * @param context
 * @param Content
 * @param btnStr
 * @return
 *//*
	public static Dialog noticeDialog(final Context context,final String Content,String btnStr){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText(Content);
		
		Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
		buttonDetermine.setText(btnStr);
		buttonDetermine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
		return null;
	}
	
	*//**
	 * 通知提示框
	 * @param context
	 * @param Content
	 * @param btnStr
	 * @param title
	 * @return
	 *//*
		public static Dialog noticeDialog(final Context context,final String Content,String btnStr,String title){
			final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_notice2, null);
			TextView titleView = (TextView) view.findViewById(R.id.dialog_title2);
			titleView.setVisibility(View.VISIBLE);
			titleView.setText(title);
			View line = (View)view.findViewById(R.id.line_1);
			line.setVisibility(view.VISIBLE);
					
			TextView content = (TextView) view.findViewById(R.id.dialog_content);
			content.setText(Content);
			
			Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
			buttonDetermine.setText(btnStr);
			buttonDetermine.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.setContentView(view);
			dialog.show();
			return null;
		}
	*//**
	 * 签到成功
	 * @return
	 *//*
	public static Dialog signSuccess(final Context context,final int score,final int days){
		final Dialog dialog = new Dialog(context,R.style.dialog_circle_sign);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_success, null);
		
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		String str = "已签到"+days+"天，共累计"+score+"分";
		
		int index1 = str.indexOf("到")+1;
		int index2 = str.indexOf("天");
		
		int index3 = str.indexOf("计")+1;
		int index4 = str.indexOf("分");
		
//		int index5 = str.length() - 8;
//		int index6 = str.length() - 6;
		
		SpannableStringBuilder style=new SpannableStringBuilder(str);  
        style.setSpan(new ForegroundColorSpan(0xfffff500),index1,index2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        style.setSpan(new ForegroundColorSpan(0xfffff500),index3,index4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
//        style.setSpan(new ForegroundColorSpan(0xfffff500),index5,index6,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
        
        content.setText(style); 
        
		view.findViewById(R.id.iv_get_score).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		
		dialog.setContentView(view);
		dialog.show();
		
		return null;
	}
	
	*//**
	 * 注册成功
	 * @return
	 *//*
	public static Dialog registSuccess(final Context context){
		final Dialog dialog = new Dialog(context,R.style.dialog_circle_sign);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_regist_success, null);
		view.findViewById(R.id.iv_get_score).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				((LoginActivity)context).finish();
//				Intent intent = new Intent(context, Home2Activity.class);
//				context.startActivity(intent);
				
			}
		});
		dialog.setCanceledOnTouchOutside(false);
//		view.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				
//			}
//		});
		
		dialog.setContentView(view);
		dialog.show();
		
		return null;
	}

	*//**
	 * 二维码dialog
	 * @return 
	 *//*
	public static Dialog getQrcodeDialog(Context context) {
		dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_qrcode, null);
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}*/
}

package com.newcdc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newcdc.bean.ReceivePaymentBean;
import com.newcdc.db.helper.PaymentDaoHelper;

/**
 * @author gongjie
 */
public class PaymentDao extends PaymentDaoHelper {
	private Context mContext ;
	/**
	 * @param context
	 */
	public PaymentDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
		mContext = context;
	}
	/**
	 * 收到的费用信息查询未读
	 * @param db
	 * @return
	 */
	public int queryUnReadMessage(String usercode){
		SQLiteDatabase db = getWritableDatabase();
		Cursor cur ;
		
		if (usercode == null) {
			String sql ="select count(*) from jxpayment where messageStatus=1";
			cur = db.rawQuery(sql, null);
		}else {
			String sql ="select count(*) from jxpayment where messageStatus=1 and usercode = ?";
			cur = db.rawQuery(sql, new String[]{usercode});
		}
		int i = 0;
		if(cur.moveToFirst()){
			i = cur.getInt(0);
		}
		cur.close();
		return i;
	}
	/**
	 *  添加一条支付表里
	 * @param db
	 * @param 
	 */
	public void insertPaymentMessage(String messageStatus,String messageTime,String mailStatus,String orgCode,String usercode,double actFee,String orderNum,String senderPhoneNum,String senderName){
		SQLiteDatabase db = getWritableDatabase();
		String sql ="insert into jxpayment(messageStatus,messageTime,mailStatus,orgCode,usercode,price,orderNum,senderPhoneNum,senderName) values(?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{messageStatus,messageTime,mailStatus,orgCode,usercode,actFee,orderNum,senderPhoneNum,senderName});
	}
	/**
	 * 将未读消息修改为已读
	 * @param db
	 * @param employeeNo快递员编号
	 */
	public void updateUnReadMessage(String orderNum){
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update jxpayment set messageStatus = 0 where orderNum=?";
		db.execSQL(sql, new Object[]{orderNum});
	}
	/**
	 * 根据主键，删除消息
	 * @param db
	 * @param delId
	 */
	public void deleteReceiveNotice(String delId){
		SQLiteDatabase db = getWritableDatabase();
		db.delete("jxpayment", "receiveId = ?", new String[]{delId});
	}
	/**
	 * 查询所有时间支付
	 * @param db
	 * @return 
	 */
	public List<ReceivePaymentBean> queryPaymentByOrderStatus(String userCode,String mailStatus){
		SQLiteDatabase db = getWritableDatabase();
		String sql = null;
		Cursor cur = null;
		
		 sql = "select * from jxpayment where mailStatus = ? order by receiveId desc";
		 cur = db.rawQuery(sql,new String[]{mailStatus});
		/*if(userCode == null){
			
		}else {
			 sql = "select * from jxpayment where usercode = ? and mailStatus = ? order by receiveId desc";
			 cur = db.rawQuery(sql,new String[]{userCode,mailStatus});
		}*/
		
		List<ReceivePaymentBean> list = new ArrayList<ReceivePaymentBean>(); 
		while(cur.moveToNext()){
			ReceivePaymentBean dmb = new ReceivePaymentBean();
			dmb.setReceiveId(cur.getInt(cur.getColumnIndex("receiveId")));
			dmb.setMessageStatus(cur.getString(cur.getColumnIndex("messageStatus")));
			dmb.setMessageTime(cur.getString(cur.getColumnIndex("messageTime")));
			dmb.setMailStatus(cur.getString(cur.getColumnIndex("mailStatus")));
			dmb.setOrderNum(cur.getString(cur.getColumnIndex("orderNum")));
			dmb.setPrice(cur.getString(cur.getColumnIndex("price")));
			dmb.setMobNum(cur.getString(cur.getColumnIndex("senderPhoneNum")));
			dmb.setUserCode(cur.getString(cur.getColumnIndex("usercode")));
			dmb.setOrgCode(cur.getString(cur.getColumnIndex("orgCode")));
			dmb.setSenderName(cur.getString(cur.getColumnIndex("senderName")));
			list.add(dmb);
		}
		cur.close();
		Log.e("gongjie", "查询到的付费消息"+list.toString());
		return list;
	}
	/**
	 * 根据主键修改，状态值为未读
	 * @param db
	 * @param sendId
	 */
	public void updateReceiveNotice(String sendId){
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update jxpayment set messageStatus = '0' where receiveId=?";
		db.execSQL(sql, new String[]{sendId});
	}
	
	/**
	 * 查询通讯录
	 *//*
	public synchronized List<Map<String, Object>> Findtelephone(
			String username, String orgcode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		SQLiteDatabase db =  getReadableDatabase();
		Cursor cursor = null;
		try {
			String[] colums = { "telephonenum", "bodyname", "mainid", "address" };
			cursor = db.query( ADDRESSBOOKTAB, colums,
					"username = ? and orgcode=?", new String[] { username,
							orgcode }, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("telephonenum", cursor.getString(0));
					paramsMap.put("bodyname", cursor.getString(1));
					paramsMap.put("mainid", cursor.getString(2));
					paramsMap.put("address", cursor.getString(3));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			cursor.close();
		}
		return dataList;
	}

	*//**
	 * 模糊查询通讯录
	 *//*
	public List<Map<String, Object>> querytelreason(String reason) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		SQLiteDatabase database =  getReadableDatabase();
		String sql = "select * from " +  ADDRESSBOOKTAB
				+ " where telephonenum like '%" + reason
				+ "%' or bodyname like '%" + reason + "%' or address like '%"
				+ reason + "%'";
		Cursor cursor = database.rawQuery(sql, null);
		try {
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("telephonenum", cursor.getString(cursor
							.getColumnIndex("telephonenum")));
					paramsMap
							.put("bodyname", cursor.getString(cursor
									.getColumnIndex("bodyname")));
					paramsMap.put("mainid",
							cursor.getString(cursor.getColumnIndex("mainid")));
					paramsMap.put("address",
							cursor.getString(cursor.getColumnIndex("address")));
					dataList.add(paramsMap);
				}
			}
			return dataList;
		} catch (Exception e) {
		}
		return null;
	}

	*//**
	 * 插入一条电话到通讯录
	 * 
	 * @param telephone
	 *//*
	public synchronized void inserttelephone(String telephone, String bodyname,
			String username, String orgcode, String mainid, String address) {
		SQLiteDatabase database =  getWritableDatabase();
		ContentValues values = new ContentValues();
		if (telephone != null && !"null".equals(telephone)) {
			values.put("telephonenum", telephone);
		} else {
			values.put("telephonenum", "");
		}
		if (mainid != null && !"null".equals(mainid)) {
			values.put("mainid", mainid);
		} else {
			values.put("mainid", "");
		}
		if (orgcode != null && !"null".equals(orgcode)) {
			values.put("orgcode", orgcode);
		} else {
			values.put("orgcode", "");
		}
		if (username != null && !"null".equals(username)) {
			values.put("username", username);
		} else {
			values.put("username", "");
		}
		if (bodyname != null && !"null".equals(bodyname)) {
			values.put("bodyname", bodyname);
		} else {
			values.put("bodyname", "");
		}
		if (bodyname != null && !"null".equals(bodyname)) {
			values.put("bodyname", bodyname);
		} else {
			values.put("bodyname", "");
		}
		if (address != null && !"null".equals(address)) {
			values.put("address", address);
		} else {
			values.put("address", "");
		}
		@SuppressWarnings("unused")
		long ccd = database.insert("ADDRESSBOOKTAB", null, values);
		database.close();
	}

	// 删除
	public synchronized int deletetelephone(String mainid) {
		SQLiteDatabase db =  getReadableDatabase();
		int a = 0;
		if (mainid != null) {
			a = db.delete( ADDRESSBOOKTAB, "mainid=?",
					new String[] { mainid });
		}
		return a;
	}

	*//**
	 * 删除全部
	 * 
	 * @param tabName
	 *//*
	public synchronized void deleteAlltelphone() {
		SQLiteDatabase database =  getWritableDatabase();
		database.delete( ADDRESSBOOKTAB, null, null);
		database.close();
	}

	// 更新 通讯录
	public synchronized void updatetelephone(String name, String tel,
			String addr, String mainid) {
		SQLiteDatabase db =  getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("telephonenum", tel);
		contentValues.put("bodyname", name);
		contentValues.put("address", addr);
		long count = db.update( ADDRESSBOOKTAB,
				contentValues, "mainid = ?", new String[] { mainid });
		Log.e("connt", count + "");
	}

	// 修改通讯录地址
	public synchronized void updateaddress(String tel, String address) {
		SQLiteDatabase db =  getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("address", address);
		long count = db.update( ADDRESSBOOKTAB,
				contentValues, "telephonenum = ?", new String[] { tel });
		Log.e("connt", count + "");
	}

	*//**
	 * 查询通讯录
	 *//*
	public synchronized List<Map<String, Object>> findistele(String tele) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		try {
			String[] colums = { "telephonenum", "bodyname", "mainid", "address" };
			cursor = db
.query(ADDRESSBOOKTAB, colums,
							"telephonenum = ?", new String[] { tele }, null,
							null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("telephonenum", cursor.getString(0));
					paramsMap.put("bodyname", cursor.getString(1));
					paramsMap.put("mainid", cursor.getString(2));
					paramsMap.put("address", cursor.getString(3));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			cursor.close();
		}
		return dataList;
	}*/

}

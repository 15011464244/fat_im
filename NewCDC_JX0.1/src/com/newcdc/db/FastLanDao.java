package com.newcdc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newcdc.db.helper.FastLanDaoHelper;
import com.newcdc.model.FastLanBean;

/**
 * 
 * 快揽数据操作
 * 
 */

public class FastLanDao extends FastLanDaoHelper {

	public FastLanDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	// 快揽存储
	public synchronized void insertFastLan(FastLanBean fastLanBean) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			values.put("dan_num", fastLanBean.getDan_num());
			values.put("MailNum", fastLanBean.getMailNum().toUpperCase());
			values.put("Customer", fastLanBean.getCustomer());
			values.put("RcvArea", fastLanBean.getRcvArea());
			values.put("ActualWeight", fastLanBean.getActualWeight());
			values.put("ActualTotalFee", fastLanBean.getActualTotalFee());
			values.put("FoodType", fastLanBean.getFoodType());
			values.put("DaoFu", fastLanBean.getDaoFu());
			values.put("ShangChuan", fastLanBean.getShangChuan());
			values.put("yunshu", fastLanBean.getYunshu());
			values.put("ReturnLiuShui", fastLanBean.getReturnLiuShui());
			values.put("fenlei", fastLanBean.getFenlei());
			values.put("sourthcode", fastLanBean.getSourthcode());
			values.put("gekoucode", fastLanBean.getGekoucode());
			values.put("gekouname", fastLanBean.getGekouname());
			values.put("ReturnMailNum", fastLanBean.getReturnMailNum()
					.toUpperCase());
			values.put("mainmail", fastLanBean.getMainmail().toUpperCase());
			values.put("pay_type", "1");
			values.put("actualGoodsFee", "1");
			values.put("addressCode", "1");
			values.put("ypdj_flag", fastLanBean.getYpdj_flag());
			values.put("ypdj_quan", fastLanBean.getYpdj_quan());
			values.put("postcode", fastLanBean.getPostcode());
			values.put("clct_date", fastLanBean.getClct_date());
			values.put("clct_time", fastLanBean.getClct_time());
			values.put("opreate", fastLanBean.getOpreate());
			values.put("sender_name", fastLanBean.getSender_name());
			values.put("sender_addr", fastLanBean.getSender_addr());
			values.put("sender_contact_phone",
					fastLanBean.getSender_contact_phone());
			values.put("delvorgcode", fastLanBean.getDelvorgcode());
			values.put("username", fastLanBean.getUsername());
			db.insert(FastLanDaoHelper.TB_FASTLAN, null, values);
		} catch (Exception e) {
			Log.e("insert", "insert error");
		} finally {
			db.close();
		}
	}

	/**
	 * 根据输入条件模糊查询,查询范围：匹配范围：手机号or姓名or地址or邮件号/已揽收
	 */
	public List<FastLanBean> queryAutoFastData(String queryInfo,
			String delvorgcode, String username) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select fast.*,TC.KHMC ,qg.DMMC from "
				+ TB_FASTLAN
				+ " fast "
				+ "left join TB_CUSTOMER TC on TC.KHDM =fast.Customer "
				+ "left join qsj_gndm qg on qg.DMDM = fast.RcvArea where (fast.MailNum like '%"
				+ queryInfo + "%' or qg.DMMC like '%" + queryInfo
				+ "%' or  qg.DHQH like '%" + queryInfo
				+ "%' or TC.KHDM like '%" + queryInfo
				+ "%' or TC.KHMC like '%" + queryInfo+"%' ) and fast.delvorgcode='" + delvorgcode
				+ "' and fast.username='" + username
				+ "' and fast.opreate='I' order by fast._id desc";
		Cursor cursor = database.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 模糊查询派揽表未上传信息
	 */
	public List<FastLanBean> queryFastLan(String queryInfo, String delvorgcode,
			String username) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select fast.*,TC.KHMC ,qg.DMMC from "
				+ TB_FASTLAN
				+ " fast "
				+ "left join "+TB_CUSTOMER+" TC on TC.KHDM =fast.Customer "
				+ "left join qsj_gndm qg on qg.DMDM = fast.RcvArea where (fast.MailNum like '%"
				+ queryInfo + "%' or qg.DMMC like '%" + queryInfo
				+ "%' or  qg.DHQH like '%" + queryInfo
				+ "%' or TC.KHDM like '%" + queryInfo
				+ "%' or TC.KHMC like '%" + queryInfo+"%' ) and fast.delvorgcode='" + delvorgcode
				+ "' and fast.username='" + username
				+ "' and fast.ShangChuan = '0' order by fast.MailNum asc";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 查询派揽表所有信息
	 */
	public List<FastLanBean> queryFastLanMessage() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_FASTLAN;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 未提交删除信息的揽收信息
	 */
	public List<FastLanBean> queryFastLanMessageI() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_FASTLAN + " where opreate='I'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}
	/**
	 * 根据工号查询未提交删除信息的揽收信息
	 */
	public List<FastLanBean> queryFastLanMessageIdelvorgcode(String delvorgcode,
			String username) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_FASTLAN + " where opreate='I' and delvorgcode='" + delvorgcode
				+ "' and username='" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}
	/**
	 * 揽收未上传的邮件
	 */
	public List<FastLanBean> queryFastLanMessageweishangchuan() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_FASTLAN + " where ShangChuan='0'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}


	/**
	 * 转化成List
	 */
	public List<FastLanBean> parseCursorToList(Cursor cursor) {
		ArrayList<FastLanBean> list = new ArrayList<FastLanBean>();
		while (cursor.moveToNext()) {
			FastLanBean fastLanBean = new FastLanBean();
			fastLanBean.setDan_num(cursor.getString(cursor
					.getColumnIndex("dan_num")));
			fastLanBean.setActualTotalFee(cursor.getString(cursor
					.getColumnIndex("ActualTotalFee")));
			fastLanBean.setActualWeight(cursor.getString(cursor
					.getColumnIndex("ActualWeight")));
			fastLanBean.setYunshu(cursor.getString(cursor
					.getColumnIndex("yunshu")));
			fastLanBean.setMainmail(cursor.getString(cursor
					.getColumnIndex("mainmail")));
			fastLanBean.setCustomer(cursor.getString(cursor
					.getColumnIndex("Customer")));
			fastLanBean.setDaoFu(cursor.getString(cursor
					.getColumnIndex("DaoFu")));
			fastLanBean.setFoodType(cursor.getString(cursor
					.getColumnIndex("FoodType")));
			fastLanBean.setMailNum(cursor.getString(cursor
					.getColumnIndex("MailNum")));
			fastLanBean.setFenlei(cursor.getString(cursor
					.getColumnIndex("fenlei")));
			fastLanBean.setRcvArea(cursor.getString(cursor
					.getColumnIndex("RcvArea")));
			fastLanBean.setSourthcode(cursor.getString(cursor
					.getColumnIndex("sourthcode")));
			fastLanBean.setGekoucode(cursor.getString(cursor
					.getColumnIndex("gekoucode")));
			fastLanBean.setReturnLiuShui(cursor.getString(cursor
					.getColumnIndex("ReturnLiuShui")));
			fastLanBean.setReturnMailNum(cursor.getString(cursor
					.getColumnIndex("ReturnMailNum")));
			fastLanBean.setGekouname(cursor.getString(cursor
					.getColumnIndex("gekouname")));
			fastLanBean.setShangChuan(cursor.getString(cursor
					.getColumnIndex("ShangChuan")));
			fastLanBean.setCharge(cursor.getString(cursor
					.getColumnIndex("ActualTotalFee")));
			fastLanBean.setId(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("_id"))));
			fastLanBean.setYpdj_flag(cursor.getString(cursor
					.getColumnIndex("ypdj_flag")));
			fastLanBean.setYpdj_quan(cursor.getString(cursor
					.getColumnIndex("ypdj_quan")));
			fastLanBean.setPostcode(cursor.getString(cursor
					.getColumnIndex("postcode")));
			fastLanBean.setClct_date(cursor.getString(cursor
					.getColumnIndex("clct_date")));
			fastLanBean.setClct_time(cursor.getString(cursor
					.getColumnIndex("clct_time")));
			fastLanBean.setOpreate(cursor.getString(cursor
					.getColumnIndex("opreate")));
			fastLanBean.setSender_name(cursor.getString(cursor
					.getColumnIndex("sender_name")));
			fastLanBean.setSender_addr(cursor.getString(cursor
					.getColumnIndex("sender_addr")));
			fastLanBean.setSender_contact_phone(cursor.getString(cursor
					.getColumnIndex("sender_contact_phone")));
			fastLanBean.setDelvorgcode(cursor.getString(cursor
					.getColumnIndex("delvorgcode")));
			fastLanBean.setUsername(cursor.getString(cursor
					.getColumnIndex("username")));
			list.add(fastLanBean);

		}
		cursor.close();
		return list;
	}

	/**
	 * 根据mainmail删除数据
	 */
	public synchronized void deletemainmail(String MailNum) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from " + TB_FASTLAN + " where MailNum=" + MailNum);
		db.close();
	}

	/**
	 * 
	 * 更新是否上传
	 */
	public synchronized boolean updateShangChuan(int _id, int ShangChuan) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + TB_FASTLAN + " set ShangChuan='" + ShangChuan
				+ "' where _id=" + _id;
		try {
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
		} finally {
			db.close();
		}
		return false;
	}


	/**
	 * 
	 * 更新是否上传
	 */
	public synchronized boolean updateOrder(String orderno, int ShangChuan) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + TB_FASTLAN + " set ShangChuan='" + ShangChuan
				+ "' where dan_num='" + orderno+"'";
		try {
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
		} finally {
			db.close();
		}
		return false;
	}

	/**
	 * 
	 * 更新操作方式
	 */
	public synchronized boolean updateOpreate(String opreate,String mailnum) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + TB_FASTLAN + " set opreate= '" + opreate+"' ,ShangChuan=0 "
				+ " where opreate='I' and ((MailNum='" + mailnum
				+ "') or (ypdj_flag='1' and mainmail='" + mailnum + "')"
				+ " or ( mainmail in (select mainmail from " + TB_FASTLAN
				+ " where ypdj_flag='1' and MailNum='" + mailnum + "')))";
		try {
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
		} finally {
			db.close();
		}
		return false;
	}

	/**
	 * 根据邮件号查询未提交删除信息的揽收信息
	 */
	public List<FastLanBean> queryFastLanMessageI(String mailnum) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_FASTLAN
				+ " where opreate='I' and ((MailNum='" + mailnum
				+ "') or (ypdj_flag='1' and mainmail='" + mailnum + "')"
				+ " or ( mainmail in (select mainmail from " + TB_FASTLAN
				+ " where ypdj_flag='1' and MailNum='" + mailnum + "')))";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}


	/**
	 * 根据_id查询头地标信息
	 * 
	 * @return
	 */
	public List<FastLanBean> query_id(int _id) {
		Log.e("query_id", _id + "");
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_FASTLAN + " where _id=" + _id;
		Cursor cursor = db.rawQuery(sql, null);

		Log.e("query_id-cursor", cursor.getCount() + "");
		List<FastLanBean> list = new ArrayList<FastLanBean>();
		list = parseCursorToList(cursor);
		return list;
	}




/*
 * 删除揽收以上传的
 */
	public synchronized void deleteFastLan_YiShangChuang() {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from " + TB_FASTLAN + " where ShangChuan = '1'";
		db.execSQL(sql);
		db.close();
	}

}

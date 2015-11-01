package com.newcdc.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newcdc.db.helper.Gather_MsgDaoHelper;
import com.newcdc.db.helper.JxClctDaoHelper;
import com.newcdc.model.GatherTableBean;
import com.newcdc.model.JxClctBean;
import com.newcdc.tools.UserInfoUtils;

/**
 * @author hanrong
 * @version 创建时间：2015-1-9 下午4:28:44 类说明 派揽信息dao
 */
public class JxClctDao extends JxClctDaoHelper {

	public JxClctDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	/**
	 * 江西揽收数据插入
	 * 
	 * @param bean
	 * @return
	 */
	public synchronized boolean saveJxClctMessage(JxClctBean bean) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (bean != null) {
			values = new ContentValues();
			values.put("mailNo", bean.getMailNo());
			values.put("weight", bean.getWeight());
			values.put("receiverName", bean.getReceiverName());
			values.put("receiverMobile", bean.getReceiverMobile());
			values.put("receiverAddress", bean.getReceiverAddress());
			values.put("fee", bean.getFee());
			values.put("senderName", bean.getSenderName());
			values.put("senderMobile", bean.getSenderMobile());
			values.put("senderAddress", bean.getSenderAddress());
			values.put("orgCode", bean.getOrgCode());
			values.put("userCode", bean.getUserCode());
			values.put("userName", bean.getUserName());
			values.put("clct_stauts", "0");// 上传状态：未上传0已上传1
			values.put("createtime", new Date().getTime());
			values.put("distance", Integer.MAX_VALUE);
			values.put("mailType", bean.getMailType());
			values.put("payment", bean.getPayment());
			values.put("reservenum", bean.getReservenum());
			values.put("companyid", bean.getCompanyid());
			values.put("serviceType", bean.getServiceType());
			values.put("userSid", bean.getUserSid());
			values.put("userIntegral", bean.getUserIntegral());
			values.put("integral", bean.getIntegral());
			values.put("actFee", bean.getActFee());
			values.put("sendType", bean.getSendType());
			values.put("startTime", bean.getStartTime());
			values.put("isPayment", "0");
			db.insert(JXCLCT_TABLE, null, values);
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}

	}

	/**
	 * 江西:根据揽收状态查询数据
	 */
	public List<JxClctBean> queryJxClctMessage(String orgCode, String userCode,
			String clct_stauts) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE + " where clct_stauts='"
				+ clct_stauts + "' and orgCode='" + orgCode
				+ "' and userCode='" + userCode + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 江西:根据揽收状态查询数据和搜索内容
	 */
	public List<JxClctBean> queryJxClctMessage_S(String queryInfo,
			String orgCode, String userCode, String clct_stauts) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE
				+ " where (receiverName like '%" + queryInfo
				+ "%' or receiverMobile like '%" + queryInfo
				+ "%' or  receiverAddress like '%" + queryInfo
				+ "%' or  mailNo like '%" + queryInfo
				+ "%' or senderName like '%" + queryInfo
				+ "%' or senderMobile like '%" + queryInfo
				+ "%'  or senderAddress like '%" + queryInfo
				+ "%' ) and clct_stauts='" + clct_stauts + "' and orgCode='"
				+ orgCode + "' and userCode='" + userCode + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 江西查询揽收数据
	 */
	public List<JxClctBean> queryJxClctMessage_All(String orgCode,
			String userCode) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE + " where orgCode='"
				+ orgCode + "' and userCode='" + userCode + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 江西查询揽收数据 根据搜索内容
	 */
	public List<JxClctBean> queryJxClctMessage_All_S(String queryInfo,
			String orgCode, String userCode) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE
				+ " where (receiverName like '%" + queryInfo
				+ "%' or receiverMobile like '%" + queryInfo
				+ "%' or  receiverAddress like '%" + queryInfo
				+ "%' or senderName like '%" + queryInfo
				+ "%' or mailNo like '%" + queryInfo
				+ "%' or senderMobile like '%" + queryInfo
				+ "%'  or senderAddress like '%" + queryInfo
				+ "%' ) and orgCode='" + orgCode + "' and userCode='"
				+ userCode + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 将cursor转化成List
	 */
	public ArrayList<JxClctBean> parseCursorToList(Cursor cursor) {
		ArrayList<JxClctBean> list = new ArrayList<JxClctBean>();
		while (cursor.moveToNext()) {
			JxClctBean bean = new JxClctBean();
			bean.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			bean.setMailNo(cursor.getString(cursor.getColumnIndex("mailNo")));
			bean.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
			bean.setReceiverName(cursor.getString(cursor
					.getColumnIndex("receiverName")));
			bean.setReceiverMobile(cursor.getString(cursor
					.getColumnIndex("receiverMobile")));
			bean.setReceiverAddress(cursor.getString(cursor
					.getColumnIndex("receiverAddress")));
			bean.setFee(cursor.getString(cursor.getColumnIndex("fee")));
			bean.setSenderName(cursor.getString(cursor
					.getColumnIndex("senderName")));
			bean.setSenderMobile(cursor.getString(cursor
					.getColumnIndex("senderMobile")));
			bean.setSenderAddress(cursor.getString(cursor
					.getColumnIndex("senderAddress")));
			bean.setOrgCode(cursor.getString(cursor.getColumnIndex("orgCode")));
			bean.setUserCode(cursor.getString(cursor.getColumnIndex("userCode")));
			bean.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
			bean.setClct_stauts(cursor.getString(cursor
					.getColumnIndex("clct_stauts")));
			bean.setCreatetime(cursor.getString(cursor
					.getColumnIndex("createtime")));
			bean.setSender_latlng(cursor.getString(cursor
					.getColumnIndex("sender_latlng")));
			bean.setIsPayment(cursor.getString(cursor
					.getColumnIndex("isPayment")));
			bean.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
			bean.setMailType(cursor.getString(cursor.getColumnIndex("mailType")));
			bean.setReservenum(cursor.getString(cursor
					.getColumnIndex("reservenum")));
			bean.setCompanyid(cursor.getString(cursor
					.getColumnIndex("companyid")));
			bean.setPayment(cursor.getString(cursor.getColumnIndex("payment")));
			bean.setServiceType(cursor.getString(cursor
					.getColumnIndex("serviceType")));
			bean.setUserSid(cursor.getString(cursor.getColumnIndex("userSid")));
			bean.setUserIntegral(cursor.getString(cursor
					.getColumnIndex("userIntegral")));
			bean.setActFee(cursor.getString(cursor.getColumnIndex("actFee")));
			bean.setIntegral(cursor.getString(cursor.getColumnIndex("integral")));
			bean.setSendType(cursor.getString(cursor.getColumnIndex("sendType")));
			bean.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
			list.add(bean);
		}
		cursor.close();
		return list;
	}

	/**
	 * 根据_id删除数据
	 */
	public synchronized void deleteGatherValue(int _id) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from " + JXCLCT_TABLE + " where _id=" + _id);
		db.close();
	}

	/**
	 * 根据邮件号查询数据数据
	 */
	public List<JxClctBean> selectMessage_mailNo(String mailNo, String orgCode,
			String userCode) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE + " where mailNo='"
				+ mailNo + "' and orgCode='" + orgCode + "' and userCode='"
				+ userCode + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 根据邮件类型查询数据数据
	 */
	public List<JxClctBean> selectMessage_mailType(String mailType,
			String orgCode, String userCode) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE + " where mailType='"
				+ mailType + "' and orgCode='" + orgCode + "' and userCode='"
				+ userCode + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	// /**
	// * 更新邮件距离信息
	// */
	// public synchronized void updateDistance(
	// ArrayList<Map<String, Object>> distanceList) {
	// SQLiteDatabase database = getWritableDatabase();
	// for (int i = 0; i < distanceList.size(); i++) {
	// Map<String, Object> map = distanceList.get(i);
	// String sql = "update " + TB_GATHER_MSG + " set distance= '"
	// + map.get("distance") + "' where _id=" + map.get("_id");
	// database.execSQL(sql);
	// }
	// database.close();
	// }

	/**
	 * 
	 * 更新揽收数据
	 */
	public boolean updateStauts(String mailNo, String stauts) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + JXCLCT_TABLE + " set clct_stauts='" + stauts
				+ "' where mailNo='" + mailNo + "'";
		try {
			db.execSQL(sql);

		} catch (Exception e) {
			return false;
		} finally {
			db.close();
		}
		return true;
	}

	/**
	 * 
	 * 更新揽收数据
	 */
	public boolean updateClctStauts(List<JxClctBean> beans, String stauts) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < beans.size(); i++) {
			String sql = "update " + JXCLCT_TABLE + " set clct_stauts='"
					+ stauts + "' where mailNo='" + beans.get(i).getMailNo()
					+ "'";
			db.execSQL(sql);

		}
		db.close();
		return true;
	}

	// /**
	// *
	// * 更新智能平台信息
	// */
	// public boolean updateEnSureTask(String reservenum, String cnday) {
	// SQLiteDatabase db = getWritableDatabase();
	// String sql = "update " + TB_GATHER_MSG + " set cnday='" + cnday
	// + "' where reservenum='" + reservenum + "'";
	// try {
	// db.execSQL(sql);
	//
	// } catch (Exception e) {
	// return false;
	// } finally {
	// db.close();
	// }
	// return true;
	// }

	/**
	 * 更新经纬度信息
	 */
	public void updateLatlng(ArrayList<Map<String, Object>> latlngList) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < latlngList.size(); i++) {
			Map<String, Object> map = latlngList.get(i);
			String sql = "update " + JXCLCT_TABLE + " set sender_latlng= '"
					+ map.get("latlng") + "' where _id=" + map.get("_id");
			db.execSQL(sql);
		}
		db.close();
	}

	// /**
	// *
	// * 更新是否阅读
	// */
	// public boolean updateReadType(int _id, int readtype) {
	// SQLiteDatabase db = getWritableDatabase();
	// String sql = "update " + TB_GATHER_MSG + " set readtype= " + readtype
	// + " where _id=" + _id;
	// try {
	// db.execSQL(sql);
	// return true;
	// } catch (Exception e) {
	// } finally {
	// db.close();
	// }
	// return false;
	// }

	// /**
	// *
	// * 更新催单
	// */
	// public boolean updateRemind(String reservenum, String remind) {
	// SQLiteDatabase db = getWritableDatabase();
	// String sql = "update " + TB_GATHER_MSG + " set remind='" + remind
	// + "' where reservenum='" + reservenum + "'";
	// try {
	// db.execSQL(sql);
	// return true;
	// } catch (Exception e) {
	// } finally {
	// db.close();
	// }
	// return false;
	// }

	/**
	 * 根据distance字段大小 升序排序查询未揽收件
	 */
	public List<JxClctBean> orderByDistance_Query(String queryInfo,
			String delvorgcode, String username) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE
				+ " where  ( receiverName like '%" + queryInfo
				+ "%' or receiverMobile like '%" + queryInfo
				+ "%' or receiverAddress like '%" + queryInfo
				+ "%' or senderName like '%" + queryInfo
				+ "%' or senderMobile like '%" + queryInfo
				+ "%' or senderAddress like '%" + queryInfo
				+ "%' ) and stauts='0' and orgCode='" + delvorgcode
				+ "' and userCode='" + username + "' order by distance asc";
		Cursor cursor = database.rawQuery(sql, null);
		return parseCursorToList(cursor);

	}

	/**
	 * 根据distance查询未揽收件
	 */
	public List<JxClctBean> FromDistance_Query(String delvorgcode,
			String username, String distance) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE
				+ " where clct_stauts=0 and orgCode=" + delvorgcode
				+ " and userCode=" + username + " and distance<" + distance;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 根据_id查询信息
	 * 
	 * @return
	 */
	public List<JxClctBean> query_id(int _id) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE + " where _id=" + _id;
		Cursor cursor = db.rawQuery(sql, null);
		List<JxClctBean> list = new ArrayList<JxClctBean>();
		list = parseCursorToList(cursor);
		return list;
	}

	/**
	 * 根据输入条件模糊查询,查询范围：匹配范围：手机号or姓名or地址or邮件号
	 */
	public List<JxClctBean> queryAutoData(String queryInfo, String delvorgcode,
			String username) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + JXCLCT_TABLE
				+ " where receiverMobile like '%" + queryInfo
				+ "%' or receiverName like '%" + queryInfo
				+ "%' or receiverAddress like '%" + queryInfo
				+ "%' or senderName like '%" + queryInfo
				+ "%' or senderMobile like '%" + queryInfo
				+ "%' or senderAddress like '%" + queryInfo
				+ "%' and orgCode='" + delvorgcode + "' and userCode='"
				+ username + "' order by distance asc";
		Cursor cursor = database.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 删除派揽数据
	 */
	public synchronized void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(JXCLCT_TABLE, null, null);
		db.close();
	}

}

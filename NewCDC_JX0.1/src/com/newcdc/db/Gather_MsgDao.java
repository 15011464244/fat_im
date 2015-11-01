package com.newcdc.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lidroid.xutils.util.LogUtils;
import com.newcdc.db.helper.Gather_MsgDaoHelper;
import com.newcdc.model.GatherTableBean;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * @author hanrong
 * @version 创建时间：2015-1-9 下午4:28:44 类说明 派揽信息dao
 */
public class Gather_MsgDao extends Gather_MsgDaoHelper {

	private Context context;
	private String delvorgcode;
	private String username;

	public Gather_MsgDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
		this.context = context;
		UserInfoUtils user = new UserInfoUtils(context);
		delvorgcode = user.getUserDelvorgCode();// 机构号
		username = user.getUserName();// 用户的工号
	}

	public synchronized boolean saveGatherMessage(
			ArrayList<Map<String, String>> params) {
		UserInfoUtils user = new UserInfoUtils(context);
		String Orgcode = user.getUserDelvorgCode();// 机构号
		String Username = user.getUserName();// 用户的工号
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				Map<String, String> maps = params.get(i);
				values = new ContentValues();
				values.put("phone",
						maps.get("phone") != null ? maps.get("phone")
								.toString() : "");
				values.put("clientname", maps.get("clientname") != null ? maps
						.get("clientname").toString() : "");
				values.put("address",
						maps.get("address") != null ? maps.get("address")
								.toString() : "");
				values.put(
						"senderProvCode",
						maps.get("senderProvCode") != null ? maps.get(
								"senderProvCode").toString() : "");
				values.put(
						"senderCityCode",
						maps.get("senderCityCode") != null ? maps.get(
								"senderCityCode").toString() : "");
				values.put(
						"senderCountyCode",
						maps.get("senderCountyCode") != null ? maps.get(
								"senderCountyCode").toString() : "");
				values.put(
						"senderCountryCode",
						maps.get("senderCountryCode") != null ? maps.get(
								"senderCountryCode").toString() : "");
				values.put("senderProv", maps.get("senderProv") != null ? maps
						.get("senderProv").toString() : "");
				values.put("senderCity", maps.get("senderCity") != null ? maps
						.get("senderCity").toString() : "");
				values.put(
						"senderCounty",
						maps.get("senderCounty") != null ? maps.get(
								"senderCounty").toString() : "");
				values.put(
						"receiverName",
						maps.get("receiverName") != null ? maps.get(
								"receiverName").toString() : "");
				values.put(
						"receiverMobile",
						maps.get("receiverMobile") != null ? maps.get(
								"receiverMobile").toString() : "");
				values.put(
						"receiverProvCode",
						maps.get("receiverProvCode") != null ? maps.get(
								"receiverProvCode").toString() : "");
				values.put(
						"receiverCityCode",
						maps.get("receiverCityCode") != null ? maps.get(
								"receiverCityCode").toString() : "");
				values.put(
						"receiverCountyCode",
						maps.get("receiverCountyCode") != null ? maps.get(
								"receiverCountyCode").toString() : "");
				values.put(
						"receiverProv",
						maps.get("receiverProv") != null ? maps.get(
								"receiverProv").toString() : "");
				values.put(
						"receiverCity",
						maps.get("receiverCity") != null ? maps.get(
								"receiverCity").toString() : "");
				values.put(
						"receiverCounty",
						maps.get("receiverCounty") != null ? maps.get(
								"receiverCounty").toString() : "");
				values.put(
						"receiverCountryCode",
						maps.get("receiverCountryCode") != null ? maps.get(
								"receiverCountryCode").toString() : "");
				values.put(
						"receiverStreet",
						maps.get("receiverStreet") != null ? maps.get(
								"receiverStreet").toString() : "");
				values.put(
						"orderWeight",
						maps.get("orderWeight") != null ? maps.get(
								"orderWeight").toString() : "");
				values.put("payment",
						maps.get("payment") != null ? maps.get("payment")
								.toString() : "");
				values.put(
						"taskAllotTime",
						maps.get("taskAllotTime") != null ? maps.get(
								"taskAllotTime").toString() : "");
				values.put("lssx", maps.get("lssx") != null ? maps.get("lssx")
						.toString() : "");
				values.put("cnday",
						maps.get("cnday") != null ? maps.get("cnday")
								.toString() : "");
				values.put("createtime", Utils.getCurrTime() + "");
				values.put("distance", Integer.MAX_VALUE);
				values.put("reservenum", maps.get("reservenum") != null ? maps
						.get("reservenum").toString() : "");
				values.put("stauts", 0);
				// values.put(
				// "device_number",
				// maps.get("device_number") != null ? maps.get(
				// "device_number").toString() : "");
				values.put(
						"operationtime",
						maps.get("operationtime") != null ? maps.get(
								"operationtime").toString() : "");
				values.put("msg_id",
						maps.get("msg_id") != null ? maps.get("msg_id")
								.toString() : "");
				values.put(
						"msg_typecode",
						maps.get("msg_typecode") != null ? maps.get(
								"msg_typecode").toString() : "");
				values.put("msg_code",
						maps.get("msg_code") != null ? maps.get("msg_code")
								.toString() : "");
				values.put(
						"userValidIntegral",
						maps.get("userValidIntegral") != null ? maps.get(
								"userValidIntegral").toString() : "");
				values.put("sendType",
						maps.get("sendType") != null ? maps.get("sendType")
								.toString() : "");
				values.put("startTime", maps.get("startTime") != null ? maps
						.get("startTime").toString() : "");
				values.put("companyid", maps.get("companyid") != null ? maps
						.get("companyid").toString() : "");
				values.put("readtype", 0);
				values.put("remind", 0);
				values.put("delvorgcode", Orgcode);
				values.put("username", Username);
				values.put("Isspeech", "1");// 是否语音播报 是？1 否？0 用于派单超时提醒 默认否
				values.put("IsShow", "1");// 是否加入要显示的个数中 是？1 否？0 用于推送数量显示
				db.insert(TB_GATHER_MSG, null, values);
			}
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}

	}

	/**
	 * 查询派揽表未揽收信息信息
	 */
	public List<GatherTableBean> queryGatherMessage(String delvorgcode,
			String username) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_GATHER_MSG
				+ " where stauts=0 and delvorgcode='" + delvorgcode
				+ "' and username='" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 将gethercursor转化成List
	 */
	public ArrayList<GatherTableBean> parseCursorToList(Cursor cursor) {
		ArrayList<GatherTableBean> list = new ArrayList<GatherTableBean>();
		while (cursor.moveToNext()) {
			GatherTableBean bean = new GatherTableBean();
			bean.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			bean.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
			bean.setClientname(cursor.getString(cursor
					.getColumnIndex("clientname")));
			bean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			bean.setSenderProvCode(cursor.getString(cursor
					.getColumnIndex("senderProvCode")));
			bean.setSenderCityCode(cursor.getString(cursor
					.getColumnIndex("senderCityCode")));
			bean.setSenderCountyCode(cursor.getString(cursor
					.getColumnIndex("senderCountyCode")));
			bean.setSenderCountryCode(cursor.getString(cursor
					.getColumnIndex("senderCountryCode")));
			bean.setSenderProv(cursor.getString(cursor
					.getColumnIndex("senderProv")));
			bean.setSenderCity(cursor.getString(cursor
					.getColumnIndex("senderCity")));
			bean.setSenderCounty(cursor.getString(cursor
					.getColumnIndex("senderCounty")));
			bean.setReceiverName(cursor.getString(cursor
					.getColumnIndex("receiverName")));
			bean.setReceiverMobile(cursor.getString(cursor
					.getColumnIndex("receiverMobile")));
			bean.setReceiverProvCode(cursor.getString(cursor
					.getColumnIndex("receiverProvCode")));
			bean.setReceiverCityCode(cursor.getString(cursor
					.getColumnIndex("receiverCityCode")));
			bean.setReceiverCountyCode(cursor.getString(cursor
					.getColumnIndex("receiverCountyCode")));
			bean.setReceiverProv(cursor.getString(cursor
					.getColumnIndex("receiverProv")));
			bean.setReceiverCity(cursor.getString(cursor
					.getColumnIndex("receiverCity")));
			bean.setReceiverCounty(cursor.getString(cursor
					.getColumnIndex("receiverCounty")));
			bean.setReceiverCountryCode(cursor.getString(cursor
					.getColumnIndex("receiverCountryCode")));
			bean.setReceiverStreet(cursor.getString(cursor
					.getColumnIndex("receiverStreet")));
			bean.setOrderWeight(cursor.getString(cursor
					.getColumnIndex("orderWeight")));
			bean.setPayment(cursor.getString(cursor.getColumnIndex("payment")));
			bean.setLssx(cursor.getString(cursor.getColumnIndex("lssx")));
			bean.setCnday(cursor.getString(cursor.getColumnIndex("cnday")));
			bean.setIsspeech(cursor.getString(cursor.getColumnIndex("Isspeech")));
			bean.setCreatetime(cursor.getString(cursor
					.getColumnIndex("createtime")));
			bean.setLatlng(cursor.getString(cursor.getColumnIndex("latlng")));
			bean.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
			bean.setReservenum(cursor.getString(cursor
					.getColumnIndex("reservenum")));
			bean.setStauts(cursor.getString(cursor.getColumnIndex("stauts")));
			// bean.setDevice_number(cursor.getString(cursor
			// .getColumnIndex("device_number")));
			bean.setOperationtime(cursor.getString(cursor
					.getColumnIndex("operationtime")));
			bean.setMsg_id(cursor.getString(cursor.getColumnIndex("msg_id")));
			bean.setMsg_typecode(cursor.getString(cursor
					.getColumnIndex("msg_typecode")));
			bean.setMsg_code(cursor.getString(cursor.getColumnIndex("msg_code")));
			bean.setUserValidIntegral(cursor.getString(cursor
					.getColumnIndex("userValidIntegral")));
			bean.setReadtype(cursor.getInt(cursor.getColumnIndex("readtype")));
			bean.setCompanyid(cursor.getString(cursor
					.getColumnIndex("companyid")));
			bean.setTaskAllotTime(cursor.getString(cursor
					.getColumnIndex("taskAllotTime")));
			bean.setIsShow(cursor.getString(cursor.getColumnIndex("IsShow")));
			bean.setRemind(cursor.getString(cursor.getColumnIndex("remind")));
			bean.setSendType(cursor.getString(cursor.getColumnIndex("sendType")));
			bean.setStartTime(cursor.getString(cursor
					.getColumnIndex("startTime")));
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
		db.execSQL("delete from " + TB_GATHER_MSG + " where _id=" + _id);
		db.close();
	}

	/**
	 * 更新邮件距离信息
	 */
	public synchronized void updateDistance(
			ArrayList<Map<String, Object>> distanceList) {
		SQLiteDatabase database = getWritableDatabase();
		for (int i = 0; i < distanceList.size(); i++) {
			Map<String, Object> map = distanceList.get(i);
			String sql = "update " + TB_GATHER_MSG + " set distance= '"
					+ map.get("distance") + "' where _id=" + map.get("_id");
			database.execSQL(sql);
		}
		database.close();
	}

	/**
	 * 
	 * 更新揽收数据
	 */
	public boolean updateStauts(String reservenum, String stauts) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + TB_GATHER_MSG + " set stauts='" + stauts
				+ "' where reservenum='" + reservenum + "'";
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
	 * 更新智能平台信息
	 */
	public boolean updateEnSureTask(String reservenum, String cnday) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + TB_GATHER_MSG + " set cnday='" + cnday
				+ "' where reservenum='" + reservenum + "'";
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
	 * 更新经纬度信息
	 */
	public void updateLatlng(ArrayList<Map<String, Object>> latlngList) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < latlngList.size(); i++) {
			Map<String, Object> map = latlngList.get(i);
			String sql = "update " + TB_GATHER_MSG + " set latlng= '"
					+ map.get("latlng") + "' where _id=" + map.get("_id");
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 
	 * 更新是否阅读
	 */
	public boolean updateReadType(int _id, int readtype) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + TB_GATHER_MSG + " set readtype= " + readtype
				+ " where _id=" + _id;
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
	 * 更新催单
	 */
	public boolean updateRemind(String reservenum, String remind) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + TB_GATHER_MSG + " set remind='" + remind
				+ "' where reservenum='" + reservenum + "'";
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
	 * 更新语音播报
	 */
	public synchronized boolean updateIsspeech(int _id) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + TB_GATHER_MSG + " set Isspeech=0"
				+ " where _id='" + _id + "'";
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
	 * 根据distance字段大小 升序排序查询未揽收件
	 */
	public synchronized List<GatherTableBean> orderByDistance_Query(
			String queryInfo, String delvorgcode, String username) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + TB_GATHER_MSG
				+ " where  ( phone like '%" + queryInfo
				+ "%' or clientname like '%" + queryInfo
				+ "%' or address like '%" + queryInfo
				+ "%' or reservenum like '%" + queryInfo
				+ "%' ) and stauts='0' and delvorgcode='" + delvorgcode
				+ "' and username='" + username + "' order by distance asc";
		Cursor cursor = database.rawQuery(sql, null);
		return parseCursorToList(cursor);

	}

	public List<GatherTableBean> orderByIsspeech(String delvorgcode,
			String username, String cnday) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + TB_GATHER_MSG + " where delvorgcode='"
				+ delvorgcode + "' and username='" + username + "' and cnday='"
				+ cnday + "' and Isspeech=1";
		Cursor cursor = database.rawQuery(sql, null);
		return parseCursorToList(cursor);

	}

	/**
	 * 更新是否是新接收的推送
	 */
	public synchronized boolean updateIsShow(int _id) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + TB_GATHER_MSG + " set IsShow=0"
				+ " where _id='" + _id + "'";
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
	 * 查询新接收的推送数量
	 * 
	 * */
	public synchronized List<GatherTableBean> orderByIsShow(String delvorgcode,
			String username) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + TB_GATHER_MSG + " where delvorgcode='"
				+ delvorgcode + "' and username='" + username + "' and cnday=1"
				+ " and IsShow=1";
		Cursor cursor = database.rawQuery(sql, null);
		return parseCursorToList(cursor);

	}

	/**
	 * 根据distance查询未揽收件
	 */
	public List<GatherTableBean> FromDistance_Query(String delvorgcode,
			String username, String distance) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_GATHER_MSG
				+ " where stauts=0 and delvorgcode=" + delvorgcode
				+ " and username=" + username + " and distance<" + distance;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 根据order查询信息
	 * 
	 * @return
	 */
	public GatherTableBean query_order(String order) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_GATHER_MSG + " where reservenum='"
				+ order + "'";
		Cursor cursor = db.rawQuery(sql, null);
		GatherTableBean bean = new GatherTableBean();
		while (cursor.moveToNext()) {
			bean.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			bean.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
			bean.setClientname(cursor.getString(cursor
					.getColumnIndex("clientname")));
			bean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			bean.setSenderProvCode(cursor.getString(cursor
					.getColumnIndex("senderProvCode")));
			bean.setSenderCityCode(cursor.getString(cursor
					.getColumnIndex("senderCityCode")));
			bean.setSenderCountyCode(cursor.getString(cursor
					.getColumnIndex("senderCountyCode")));
			bean.setSenderCountryCode(cursor.getString(cursor
					.getColumnIndex("senderCountryCode")));
			bean.setSenderProv(cursor.getString(cursor
					.getColumnIndex("senderProv")));
			bean.setSenderCity(cursor.getString(cursor
					.getColumnIndex("senderCity")));
			bean.setSenderCounty(cursor.getString(cursor
					.getColumnIndex("senderCounty")));
			bean.setReceiverName(cursor.getString(cursor
					.getColumnIndex("receiverName")));
			bean.setReceiverMobile(cursor.getString(cursor
					.getColumnIndex("receiverMobile")));
			bean.setReceiverProvCode(cursor.getString(cursor
					.getColumnIndex("receiverProvCode")));
			bean.setReceiverCityCode(cursor.getString(cursor
					.getColumnIndex("receiverCityCode")));
			bean.setReceiverCountyCode(cursor.getString(cursor
					.getColumnIndex("receiverCountyCode")));
			bean.setReceiverProv(cursor.getString(cursor
					.getColumnIndex("receiverProv")));
			bean.setReceiverCity(cursor.getString(cursor
					.getColumnIndex("receiverCity")));
			bean.setReceiverCounty(cursor.getString(cursor
					.getColumnIndex("receiverCounty")));
			bean.setReceiverCountryCode(cursor.getString(cursor
					.getColumnIndex("receiverCountryCode")));
			bean.setReceiverStreet(cursor.getString(cursor
					.getColumnIndex("receiverStreet")));
			bean.setOrderWeight(cursor.getString(cursor
					.getColumnIndex("orderWeight")));
			bean.setPayment(cursor.getString(cursor.getColumnIndex("payment")));
			bean.setIsspeech(cursor.getString(cursor.getColumnIndex("Isspeech")));
			bean.setLssx(cursor.getString(cursor.getColumnIndex("lssx")));
			bean.setCnday(cursor.getString(cursor.getColumnIndex("cnday")));
			bean.setCreatetime(cursor.getString(cursor
					.getColumnIndex("createtime")));
			bean.setLatlng(cursor.getString(cursor.getColumnIndex("latlng")));
			bean.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
			bean.setReservenum(cursor.getString(cursor
					.getColumnIndex("reservenum")));
			bean.setStauts(cursor.getString(cursor.getColumnIndex("stauts")));
			// bean.setDevice_number(cursor.getString(cursor
			// .getColumnIndex("device_number")));
			bean.setOperationtime(cursor.getString(cursor
					.getColumnIndex("operationtime")));
			bean.setMsg_id(cursor.getString(cursor.getColumnIndex("msg_id")));
			bean.setMsg_typecode(cursor.getString(cursor
					.getColumnIndex("msg_typecode")));
			bean.setMsg_code(cursor.getString(cursor.getColumnIndex("msg_code")));
			bean.setUserValidIntegral(cursor.getString(cursor
					.getColumnIndex("userValidIntegral")));
			bean.setReadtype(cursor.getInt(cursor.getColumnIndex("readtype")));
			bean.setCompanyid(cursor.getString(cursor
					.getColumnIndex("companyid")));
			bean.setRemind(cursor.getString(cursor.getColumnIndex("remind")));
			bean.setTaskAllotTime(cursor.getString(cursor
					.getColumnIndex("taskAllotTime")));
			bean.setIsShow(cursor.getString(cursor.getColumnIndex("IsShow")));
			bean.setSendType(cursor.getString(cursor.getColumnIndex("sendType")));
			bean.setStartTime(cursor.getString(cursor
					.getColumnIndex("startTime")));
		}
		cursor.close();
		return bean;
	}

	/**
	 * 根据任务号删除数据
	 */
	public synchronized void deletecompanyid(String companyid) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from " + TB_GATHER_MSG + " where companyid='"
				+ companyid + "'");
		db.close();
	}

	/**
	 * 根据任务号查询信息
	 * 
	 * @return
	 */
	public List<GatherTableBean> query_companyid(String companyid) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_GATHER_MSG + " where companyid='"
				+ companyid + "'";
		Cursor cursor = db.rawQuery(sql, null);
		List<GatherTableBean> list = new ArrayList<GatherTableBean>();
		list = parseCursorToList(cursor);
		return list;
	}

	/**
	 * 根据_id查询信息
	 * 
	 * @return
	 */
	public List<GatherTableBean> query_id(int _id) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + TB_GATHER_MSG + " where _id=" + _id;
		Cursor cursor = db.rawQuery(sql, null);
		List<GatherTableBean> list = new ArrayList<GatherTableBean>();
		list = parseCursorToList(cursor);
		return list;
	}

	/**
	 * 根据输入条件模糊查询,查询范围：匹配范围：手机号or姓名or地址or邮件号
	 */
	public List<GatherTableBean> queryAutoData(String queryInfo,
			String delvorgcode, String username) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + TB_GATHER_MSG + " where phone like '%"
				+ queryInfo + "%' or clientname like '%" + queryInfo
				+ "%' or address like '%" + queryInfo
				+ "%' or reservenum like '%" + queryInfo
				+ "%' and delvorgcode='" + delvorgcode + "' and username='"
				+ username + "' order by distance asc";
		Cursor cursor = database.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 根据邮件号删除数据
	 */
	public synchronized void deletereservenum(String reservenum) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from " + TB_GATHER_MSG + " where reservenum="
				+ reservenum);
		db.close();
	}

	/**
	 * 删除派揽数据
	 */
	public synchronized void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TB_GATHER_MSG, null, null);
		db.close();
	}

	/**
	 * 删除派揽数据
	 */
	public synchronized void deleteAll(String orgCode, String userCode) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from " + MONEY_TABLE
				+ " where ismoney='0' and org_code='" + orgCode
				+ "' and username='" + userCode + "'";
		db.execSQL(sql);
		db.close();
	}

}

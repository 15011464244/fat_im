package com.newcdc.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newcdc.db.helper.MoneyDaoHelper;
import com.newcdc.model.FastLanBean;
import com.newcdc.model.GatherInfoBean;
import com.newcdc.model.GiveMoneyBean;
import com.newcdc.model.MoneyBean;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * 缴款表
 * 
 * @author hanrong
 */
public class MoneyDao extends MoneyDaoHelper {
	UserInfoUtils userinfo;
	Context context;

	public MoneyDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
		this.context = context;
		userinfo = new UserInfoUtils(context);
	}

	/**
	 * 
	 * @param list
	 *            要插入的MessageInfoBean集合---投递缴款
	 */
	public synchronized void insertDeliverMoney(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(MONEY_TABLE, null, values);
		db.close();
		// if(!(bean.getActualGoodsFee()==
		// null||"".equals(bean.getActualGoodsFee())||bean.getDlv_date()==null||"".equals(bean.getDlv_date())||bean.getDlv_time()==null||"".equals(bean.getDlv_time())))
		// {
		// SQLiteDatabase db = getWritableDatabase();
		// ContentValues values = new ContentValues();
		// values.put("user_code", userinfo.getUserName() != null ?
		// userinfo.getUserName() : "");
		// values.put("org_code",userinfo.getUserDelvorgCode() != null ?
		// userinfo.getUserDelvorgCode() : "");
		// values.put("device_num", Utils.getDeviceId(context) != null ?
		// Utils.getDeviceId(context) : "");
		// values.put("pay_order_num", "");
		// values.put("mail_num", bean.getMail_num() != null ?
		// bean.getMail_num() : "");
		// values.put("amount", bean.getMoney() != null ? bean.getMoney() :
		// "0.0");
		// values.put("pay_sort", bean.getActualGoodsFee() );
		// values.put("payment_mode", bean.getPay_type() != null ?
		// bean.getPay_type() : "1");
		// values.put("operationtime", "");
		// values.put("flag", "1");
		// values.put("transmission", "G");
		// values.put("weight", bean.getWeight() != null ? bean.getWeight() :
		// "");
		// values.put("arri_code", "");
		// values.put("cust_code", "");
		// values.put("mail_operation_time",
		// bean.getDlv_date()+bean.getDlv_time()!= null ?
		// bean.getDlv_date()+bean.getDlv_time() : "");
		// values.put("other_payment_type_code", "");
		// values.put("vip_card", "");
		// values.put("resv_col1", "");
		// values.put("resv_col2", "");
		// values.put("resv_col3", "");
		// values.put("resv_col4", "");
		// values.put("resv_col5", "");
		// values.put("operatortype","I");
		// values.put("way_code", "");
		// values.put("resv_col10","");
		// values.put("ismoney","0");
		// values.put("isLT","0");
		// values.put("frequency","");
		// values.put("createtime",new Date().getTime());
		// values.put("username",bean.getRcver_name() != null ?
		// bean.getRcver_name() : "");
		// db.insert(MONEY_TABLE, null, values);
		// db.close();
		// }

	}

	/**
	 * 插入揽收缴款信息
	 * 
	 * @param list
	 *            要插入的MessageInfoBean集合
	 */
	public synchronized void insertGatherMoney(FastLanBean bean) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_code",
				userinfo.getUserName() != null ? userinfo.getUserName() : "");
		values.put(
				"org_code",
				userinfo.getUserDelvorgCode() != null ? userinfo
						.getUserDelvorgCode() : "");
		values.put("device_num",
				Utils.getDeviceId(context) != null ? Utils.getDeviceId(context)
						: "");
		values.put("pay_order_num", "");
		values.put("mail_num", bean.getMailNum() != null ? bean.getMailNum()
				: "");
		values.put("mainNUM", bean.getMainmail() != null ? bean.getMainmail()
				: "");
		values.put("amount",
				bean.getActualTotalFee() != "" ? bean.getActualTotalFee()
						: "0.0");
		values.put("pay_sort", "1");
		values.put("payment_mode", "1");
		values.put("operationtime", "");
		values.put("flag", "1");
		values.put("transmission", "G");
		values.put("weight",
				bean.getActualWeight() != null ? bean.getActualWeight() : "");
		values.put("arri_code", bean.getRcvArea() != null ? bean.getRcvArea()
				: "");
		values.put("cust_code", bean.getCustomer() != null ? bean.getCustomer()
				: "");
		values.put(
				"mail_operation_time",
				bean.getClct_date() + bean.getClct_time() != null ? bean
						.getClct_date() + bean.getClct_time() : "");
		values.put("other_payment_type_code", "");
		values.put("vip_card", "");
		values.put("resv_col1", "");
		values.put("resv_col2", "");
		values.put("resv_col3", "");
		values.put("resv_col4", "");
		values.put("resv_col5", "");
		values.put("operatortype", "I");
		values.put("way_code",
				bean.getGekoucode() != null ? bean.getGekoucode() : "");
		values.put("resv_col10", "");
		values.put("ismoney", "0");
		values.put("isLT", "1");
		values.put("frequency", "");
		values.put("createtime", new Date().getTime());
		values.put("username", bean.getUsername() != null ? bean.getUsername()
				: "");
		db.insert(MONEY_TABLE, null, values);
		db.close();
	}

	/**
	 * 更改缴款 isMoney 0 未缴款 ；1已缴款
	 * 
	 * @param localMail
	 * @param dlv_date
	 * @param dlv_time
	 */
	public synchronized void updateIsMoney(List<GatherInfoBean> list,
			String isMoney, String operatorTime) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < list.size(); i++) {
			String sql = "update " + MONEY_TABLE + " set ismoney='" + isMoney
					+ "',operationtime='" + operatorTime + "' where mail_num='"
					+ list.get(i).getMail_num() + "'";
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 根据 邮件类型 '是否缴款' 查询缴款邮件
	 */
	public List<GatherInfoBean> queryByMailNumber(String givemoneytype,
			String ismoney) {
		SQLiteDatabase db = getReadableDatabase();
		userinfo = new UserInfoUtils(context);
		String sql = "select * from " + MONEY_TABLE + " where org_code='"
				+ userinfo.getUserDelvorgCode() + "' and user_code='"
				+ userinfo.getUserName() + "' and isLT='" + givemoneytype
				+ "' and ismoney='" + ismoney + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 查询缴款邮件
	 */
	public List<GiveMoneyBean> queryBy_Money(String givemoneytype) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + MONEY_TABLE + " where org_code='"
				+ userinfo.getUserDelvorgCode() + "' and user_code='"
				+ userinfo.getUserName() + "' and isLT='" + givemoneytype + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList_GiveMoneyBean(cursor);
	}

	/**
	 * 查询缴款邮件
	 */
	public List<MoneyBean> queryBy_Money_Detail(String givemoneytype) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + MONEY_TABLE + " where org_code='"
				+ userinfo.getUserDelvorgCode() + "' and user_code='"
				+ userinfo.getUserName() + "' and isLT='" + givemoneytype + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList_GiveMoneyBean_deal(cursor);
	}

	/**
	 * 转GatherInfoBean集合list
	 */
	public synchronized List<GatherInfoBean> parseCursorToList(Cursor cursor) {
		ArrayList<GatherInfoBean> list = new ArrayList<GatherInfoBean>();
		while (cursor.moveToNext()) {
			GatherInfoBean bean = new GatherInfoBean();
			bean.setUser_code(cursor.getString(cursor
					.getColumnIndex("user_code")));
			bean.setOrg_code(cursor.getString(cursor.getColumnIndex("org_code")));
			bean.setDevice_num(cursor.getString(cursor
					.getColumnIndex("device_num")));
			bean.setPay_order_num(cursor.getString(cursor
					.getColumnIndex("pay_order_num")));
			bean.setMail_num(cursor.getString(cursor.getColumnIndex("mail_num")));
			bean.setAmount(cursor.getString(cursor.getColumnIndex("amount")));
			bean.setPay_sort(cursor.getString(cursor.getColumnIndex("pay_sort")));
			bean.setPayment_mode(cursor.getString(cursor
					.getColumnIndex("payment_mode")));
			bean.setOperation_time(cursor.getString(cursor
					.getColumnIndex("operationtime")));
			bean.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
			bean.setTransmission(cursor.getString(cursor
					.getColumnIndex("transmission")));
			bean.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
			bean.setArri_code(cursor.getString(cursor
					.getColumnIndex("arri_code")));
			bean.setCust_code(cursor.getString(cursor
					.getColumnIndex("cust_code")));
			bean.setMail_operation_time(cursor.getString(cursor
					.getColumnIndex("mail_operation_time")));
			bean.setOther_payment_type_code(cursor.getString(cursor
					.getColumnIndex("other_payment_type_code")));
			bean.setVip_card(cursor.getString(cursor.getColumnIndex("vip_card")));
			bean.setResv_col1(cursor.getString(cursor
					.getColumnIndex("resv_col1")));
			bean.setResv_col2(cursor.getString(cursor
					.getColumnIndex("resv_col2")));
			bean.setResv_col3(cursor.getString(cursor
					.getColumnIndex("resv_col3")));
			bean.setResv_col4(cursor.getString(cursor
					.getColumnIndex("resv_col4")));
			bean.setResv_col5(cursor.getString(cursor
					.getColumnIndex("resv_col5")));
			bean.setOperatortype(cursor.getString(cursor
					.getColumnIndex("operatortype")));
			bean.setWay_code(cursor.getString(cursor.getColumnIndex("way_code")));
			bean.setResv_col10(cursor.getString(cursor
					.getColumnIndex("resv_col10")));
			bean.setIsmoney(cursor.getString(cursor.getColumnIndex("ismoney")));
			bean.setIsLT(cursor.getString(cursor.getColumnIndex("isLT")));
			bean.setFrequency(cursor.getString(cursor
					.getColumnIndex("frequency")));
			list.add(bean);
		}
		cursor.close();
		return list;
	}

	/**
	 * 转GiveMoneyBean集合list
	 */
	public synchronized List<MoneyBean> parseCursorToList_GiveMoneyBean_deal(
			Cursor cursor) {
		ArrayList<MoneyBean> list = new ArrayList<MoneyBean>();
		while (cursor.moveToNext()) {
			MoneyBean bean = new MoneyBean();
			bean.setMailId(cursor.getString(cursor.getColumnIndex("mail_num")));
			bean.setMoneyNum(cursor.getString(cursor.getColumnIndex("amount")));
			bean.setPay_type(cursor.getString(cursor
					.getColumnIndex("payment_mode")));
			bean.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			bean.setIsMoney(cursor.getString(cursor.getColumnIndex("ismoney")));

			list.add(bean);
		}
		cursor.close();
		return list;
	}

	/**
	 * 转GiveMoneyBean集合list
	 */
	public synchronized List<GiveMoneyBean> parseCursorToList_GiveMoneyBean(
			Cursor cursor) {
		ArrayList<GiveMoneyBean> list = new ArrayList<GiveMoneyBean>();
		while (cursor.moveToNext()) {
			GiveMoneyBean bean = new GiveMoneyBean();
			bean.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			bean.setMail_num(cursor.getString(cursor.getColumnIndex("mail_num")));
			bean.setCharge(cursor.getString(cursor.getColumnIndex("amount")));
			bean.setActualGoodsFee(cursor.getString(cursor
					.getColumnIndex("pay_sort")));
			bean.setPay_type(cursor.getString(cursor
					.getColumnIndex("payment_mode")));
			bean.setOrg_Code(cursor.getString(cursor.getColumnIndex("org_code")));
			bean.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			bean.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
			bean.setAddress(cursor.getString(cursor.getColumnIndex("arri_code")));
			bean.setOperatortype(cursor.getString(cursor
					.getColumnIndex("operatortype")));
			bean.setOperatorTime(cursor.getString(cursor
					.getColumnIndex("operationtime")));
			bean.setLtData(cursor.getString(cursor
					.getColumnIndex("mail_operation_time")));
			bean.setCreatetime(cursor.getString(cursor
					.getColumnIndex("createtime")));
			bean.setIsmoney(cursor.getString(cursor.getColumnIndex("ismoney")));
			bean.setGivemoneytype(cursor.getString(cursor
					.getColumnIndex("isLT")));
			list.add(bean);
		}
		cursor.close();
		return list;
	}

	/**
	 * 根据 邮件号， 机构号， 工号 删除缴费数据
	 */
	public synchronized void deleteBy_MoneyMailNumber(String mail_num) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from " + MONEY_TABLE + " where mail_num='"
				+ mail_num + "' and org_code='" + userinfo.getUserDelvorgCode()
				+ "' and username='" + userinfo.getUserName() + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 根据揽收的主单号删除缴款信息
	 */
	public synchronized void delete_GatherMoney(String mainNUM) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from " + MONEY_TABLE + " where mainNUM='"
				+ mainNUM + "' and org_code='" + userinfo.getUserDelvorgCode()
				+ "' and username='" + userinfo.getUserName() + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 根据 机构号，工号删除已交款邮件
	 */
	public synchronized void delete_Money() {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from " + MONEY_TABLE
				+ " where ismoney='0' and org_code='"
				+ userinfo.getUserDelvorgCode() + "' and username='"
				+ userinfo.getUserName() + "'";
		db.execSQL(sql);
		db.close();
	}
}

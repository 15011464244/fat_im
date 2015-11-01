package com.newcdc.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newcdc.db.helper.DeliverDaoHelper;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;
import com.newcdc.tools.UserInfoUtils;
import com.newcdc.tools.Utils;

/**
 * 下段信息表
 * 
 * @author zhangfan
 */
public class DeliverDao extends DeliverDaoHelper {

	public DeliverDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	/**
	 * 插入下段信息
	 * 
	 * @param list
	 *            要插入的MessageInfoBean集合
	 */
	public synchronized void insertDeliver(List<MessageInfoBean> list) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < list.size(); i++) {
			MessageInfoBean mib = list.get(i);
			String mail_num = mib.getMail_num();
			List<MessageInfoBean> bean = queryByMailNumber(mail_num);
			if (bean.size() == 1) {// 如果存在这条数据，则删除，同时插入新的
				String sql = "delete from " + DELIVER_TABLE
						+ " where mail_num='" + mail_num + "'";
				db.execSQL(sql);
			}
			ContentValues values = new ContentValues();
			if (mib.getRcver_loc_county() != null
					&& !"null".equals(mib.getRcver_loc_county().trim())) {
				values.put("rcver_loc_county", mib.getRcver_loc_county());
			} else {
				values.put("rcver_loc_county", "");
			}
			if (mib.getRcver_contact_phone1() != null
					&& !"null".equals(mib.getRcver_contact_phone1().trim())) {
				values.put("rcver_contact_phone1",
						mib.getRcver_contact_phone1());
			} else {
				values.put("rcver_contact_phone1", "");
			}
			if (mib.getSender_loc_county() != null
					&& !"null".equals(mib.getSender_loc_county().trim())) {
				values.put("sender_loc_county", mib.getSender_loc_county());
			} else {
				values.put("sender_loc_county", "");
			}
			if (mib.getSender_street_addr() != null
					&& !"null".equals(mib.getSender_street_addr().trim())) {
				values.put("sender_street_addr", mib.getSender_street_addr());
			} else {
				values.put("sender_street_addr", "");
			}
			if (mib.getRcver_loc_prov() != null
					&& !"null".equals(mib.getRcver_loc_prov().trim())) {
				values.put("rcver_loc_prov", mib.getRcver_loc_prov());
			} else {
				values.put("rcver_loc_prov", "");
			}
			if (mib.getSender_contact_phone1() != null
					&& !"null".equals(mib.getSender_contact_phone1().trim())) {
				values.put("sender_contact_phone1",
						mib.getSender_contact_phone1());
			} else {
				values.put("sender_contact_phone1", "");
			}
			if (mib.getRcver_name() != null
					&& !"null".equals(mib.getRcver_name().trim())) {
				values.put("rcver_name", mib.getRcver_name());
			} else {
				values.put("rcver_name", "");
			}
			if (mib.getThe_class_date() != null
					&& !"null".equals(mib.getThe_class_date().trim())) {
				values.put("the_class_date", mib.getThe_class_date());
			} else {
				values.put("the_class_date", "");
			}
			if (mib.getRcver_street_addr() != null
					&& !"null".equals(mib.getRcver_street_addr().trim())) {
				values.put("rcver_street_addr", mib.getRcver_street_addr());
			} else {
				values.put("rcver_street_addr", "");
			}
			if (mib.getSender_name() != null
					&& !"null".equals(mib.getSender_name().trim())) {
				values.put("sender_name", mib.getSender_name());
			} else {
				values.put("sender_name", "");
			}
			if (mib.getSender_loc_prov() != null
					&& !"null".equals(mib.getSender_loc_prov().trim())) {
				values.put("sender_loc_prov", mib.getSender_loc_prov());
			} else {
				values.put("sender_loc_prov", "");
			}
			if (mib.getFrequence() != null
					&& !"null".equals(mib.getFrequence().trim())) {
				values.put("frequence", mib.getFrequence());
			} else {
				values.put("frequence", "");
			}
			if (mib.getSender_loc_city() != null
					&& !"null".equals(mib.getSender_loc_city().trim())) {
				values.put("sender_loc_city", mib.getSender_loc_city());
			} else {
				values.put("sender_loc_city", "");
			}
			if (mib.getRcver_loc_city() != null
					&& !"null".equals(mib.getRcver_loc_city().trim())) {
				values.put("rcver_loc_city", mib.getRcver_loc_city());
			} else {
				values.put("rcver_loc_city", "");
			}
			if (mib.getMail_num() != null
					&& !"null".equals(mib.getMail_num().trim())) {
				values.put("mail_num", mib.getMail_num());
			} else {
				values.put("mail_num", "");
			}
			if (mib.getMoney() == null || "".equals(mib.getMoney().trim())
					|| "0".equals(mib.getMoney())) {
				values.put("money", 0.0);
			} else {
				values.put("money", mib.getMoney());
			}
			if (mib.getSender_addr() != null
					&& !"null".equals(mib.getSender_addr().trim())) {
				values.put("sender_addr", mib.getSender_addr());
			} else {
				values.put("sender_addr", "");
			}
			if (mib.getRcver_addr() != null
					&& !"null".equals(mib.getRcver_addr().trim())) {
				values.put("rcver_addr",
						mib.getRcver_addr() != null ? mib.getRcver_addr() : "");
			} else {
				values.put("rcver_addr", "");
			}
			values.put("sid", mib.getSid());// sid
			values.put("dlv_pseg_code", mib.getDlv_pseg_code());// 道段号
			values.put("type", Constant.OPER_NULL);// 设置无操作
			values.put("distance", Integer.MAX_VALUE);// 设置距离为最远
			values.put("pay_type", "1");
			values.put("pay_type", mib.getFlag() != null ? mib.getFlag() : "");
			values.put("dealResult", mib.getDealResult());

			if ("1".equals(mib.getFlag())) {
				values.put("actualGoodsFee", "3");
			} else if ("2".equals(mib.getFlag())) {
				values.put("actualGoodsFee", "4");
			} else if ("".equals(mib.getFlag()) || mib.getFlag() == null) {
				values.put("actualGoodsFee", "");
			}
			values.put("address", "1");
			values.put("weight", "1");
			values.put("call_time", "0");
			values.put("msg_time", "0");
			values.put("dlv_date", mib.getDlv_date());
			values.put("dlv_time", mib.getDlv_time());
			values.put("in_self_group", Constant.NOT_INGROUP);
			values.put("notified", Constant.NOT_NOTIFY);
			values.put("flag", mib.getFlag());
			values.put("sendmsg_by_user", Constant.MSG_NEVER_BY_USER);
			values.put("picture", "");
			values.put("checked", Constant.CHECKED_FALSE);// 本条记录是否在妥投队列中；0不在，1在。
			db.insert(DELIVER_TABLE, null, values);
		}
		db.close();
	}

	/**
	 * 插入下段信息
	 * 
	 * @param mib
	 *            要插入的MessageInfoBean
	 */
	public synchronized void insertDeliver(SQLiteDatabase db,
			MessageInfoBean mib) {
		String mail_num = mib.getMail_num();
		List<MessageInfoBean> bean = queryByMailNumber(mail_num);
		if (bean.size() == 1) {// 如果存在这条数据，则删除，同时插入新的
			String sql = "delete from " + DELIVER_TABLE + " where mail_num='"
					+ mail_num + "'";
			db.execSQL(sql);
		}
		ContentValues values = new ContentValues();
		if (mib.getRcver_loc_county() != null
				&& !"null".equals(mib.getRcver_loc_county().trim())) {
			values.put("rcver_loc_county", mib.getRcver_loc_county());
		} else {
			values.put("rcver_loc_county", "");
		}
		if (mib.getRcver_contact_phone1() != null
				&& !"null".equals(mib.getRcver_contact_phone1().trim())) {
			values.put("rcver_contact_phone1", mib.getRcver_contact_phone1());
		} else {
			values.put("rcver_contact_phone1", "");
		}
		if (mib.getSender_loc_county() != null
				&& !"null".equals(mib.getSender_loc_county().trim())) {
			values.put("sender_loc_county", mib.getSender_loc_county());
		} else {
			values.put("sender_loc_county", "");
		}
		if (mib.getSender_street_addr() != null
				&& !"null".equals(mib.getSender_street_addr().trim())) {
			values.put("sender_street_addr", mib.getSender_street_addr());
		} else {
			values.put("sender_street_addr", "");
		}
		if (mib.getRcver_loc_prov() != null
				&& !"null".equals(mib.getRcver_loc_prov().trim())) {
			values.put("rcver_loc_prov", mib.getRcver_loc_prov());
		} else {
			values.put("rcver_loc_prov", "");
		}
		if (mib.getSender_contact_phone1() != null
				&& !"null".equals(mib.getSender_contact_phone1().trim())) {
			values.put("sender_contact_phone1", mib.getSender_contact_phone1());
		} else {
			values.put("sender_contact_phone1", "");
		}
		if (mib.getRcver_name() != null
				&& !"null".equals(mib.getRcver_name().trim())) {
			values.put("rcver_name", mib.getRcver_name());
		} else {
			values.put("rcver_name", "");
		}
		values.put("the_class_date", Utils.getCurrTime());
		if (mib.getRcver_street_addr() != null
				&& !"null".equals(mib.getRcver_street_addr().trim())) {
			values.put("rcver_street_addr", mib.getRcver_street_addr());
		} else {
			values.put("rcver_street_addr", "");
		}
		if (mib.getSender_name() != null
				&& !"null".equals(mib.getSender_name().trim())) {
			values.put("sender_name", mib.getSender_name());
		} else {
			values.put("sender_name", "");
		}
		if (mib.getSender_loc_prov() != null
				&& !"null".equals(mib.getSender_loc_prov().trim())) {
			values.put("sender_loc_prov", mib.getSender_loc_prov());
		} else {
			values.put("sender_loc_prov", "");
		}
		if (mib.getFrequence() != null
				&& !"null".equals(mib.getFrequence().trim())) {
			values.put("frequence", mib.getFrequence());
		} else {
			values.put("frequence", "");
		}
		if (mib.getSender_loc_city() != null
				&& !"null".equals(mib.getSender_loc_city().trim())) {
			values.put("sender_loc_city", mib.getSender_loc_city());
		} else {
			values.put("sender_loc_city", "");
		}
		if (mib.getRcver_loc_city() != null
				&& !"null".equals(mib.getRcver_loc_city().trim())) {
			values.put("rcver_loc_city", mib.getRcver_loc_city());
		} else {
			values.put("rcver_loc_city", "");
		}
		if (mib.getMail_num() != null
				&& !"null".equals(mib.getMail_num().trim())) {
			values.put("mail_num", mib.getMail_num());
		} else {
			values.put("mail_num", "");
		}
		if (mib.getMoney() == null || "".equals(mib.getMoney().trim())
				|| "0".equals(mib.getMoney())) {
			values.put("money", 0.0);
		} else {
			values.put("money", mib.getMoney());
		}
		if (mib.getSender_addr() != null
				&& !"null".equals(mib.getSender_addr().trim())) {
			values.put("sender_addr", mib.getSender_addr());
		} else {
			values.put("sender_addr", "");
		}
		if (mib.getRcver_addr() != null
				&& !"null".equals(mib.getRcver_addr().trim())) {
			values.put("rcver_addr",
					mib.getRcver_addr() != null ? mib.getRcver_addr() : "");
		} else {
			values.put("rcver_addr", "");
		}
		values.put("sid", Integer.MAX_VALUE);// sid
		values.put("dlv_pseg_code", mib.getDlv_pseg_code());// 道段号
		values.put("type", Constant.OPER_NULL);// 设置无操作
		values.put("distance", Integer.MAX_VALUE);// 设置距离为最远
		values.put("pay_type", "1");
		values.put("pay_type", mib.getFlag() != null ? mib.getFlag() : "");
		values.put("dealResult", Constant.DAICHULI);

		if ("1".equals(mib.getFlag())) {
			values.put("actualGoodsFee", "3");
		} else if ("2".equals(mib.getFlag())) {
			values.put("actualGoodsFee", "4");
		} else if ("".equals(mib.getFlag()) || mib.getFlag() == null) {
			values.put("actualGoodsFee", "");
		}
		values.put("address", "1");
		values.put("weight", "1");
		values.put("call_time", "0");
		values.put("msg_time", "0");
		values.put("dlv_date", mib.getDlv_date());
		values.put("dlv_time", mib.getDlv_time());
		values.put("in_self_group", Constant.NOT_INGROUP);
		values.put("notified", Constant.NOT_NOTIFY);
		values.put("flag", mib.getFlag());
		values.put("sendmsg_by_user", Constant.MSG_NEVER_BY_USER);
		values.put("picture", "");
		values.put("checked", Constant.CHECKED_FALSE);// 本条记录是否在妥投队列中；0不在，1在。
		db.insert(DELIVER_TABLE, null, values);
	}

	/**
	 * 插入下段信息
	 * 
	 * @param mib
	 *            要插入的MessageInfoBean
	 */
	public synchronized void insertDeliver(MessageInfoBean mib) {
		SQLiteDatabase db = getWritableDatabase();
		String mail_num = mib.getMail_num();
		List<MessageInfoBean> bean = queryByMailNumber(mail_num);
		if (bean.size() == 1) {// 如果存在这条数据，则删除，同时插入新的
			String sql = "delete from " + DELIVER_TABLE + " where mail_num='"
					+ mail_num + "'";
			db.execSQL(sql);
		}
		ContentValues values = new ContentValues();
		if (mib.getRcver_loc_county() != null
				&& !"null".equals(mib.getRcver_loc_county().trim())) {
			values.put("rcver_loc_county", mib.getRcver_loc_county());
		} else {
			values.put("rcver_loc_county", "");
		}
		if (mib.getRcver_contact_phone1() != null
				&& !"null".equals(mib.getRcver_contact_phone1().trim())) {
			values.put("rcver_contact_phone1", mib.getRcver_contact_phone1());
		} else {
			values.put("rcver_contact_phone1", "");
		}
		if (mib.getSender_loc_county() != null
				&& !"null".equals(mib.getSender_loc_county().trim())) {
			values.put("sender_loc_county", mib.getSender_loc_county());
		} else {
			values.put("sender_loc_county", "");
		}
		if (mib.getSender_street_addr() != null
				&& !"null".equals(mib.getSender_street_addr().trim())) {
			values.put("sender_street_addr", mib.getSender_street_addr());
		} else {
			values.put("sender_street_addr", "");
		}
		if (mib.getRcver_loc_prov() != null
				&& !"null".equals(mib.getRcver_loc_prov().trim())) {
			values.put("rcver_loc_prov", mib.getRcver_loc_prov());
		} else {
			values.put("rcver_loc_prov", "");
		}
		if (mib.getSender_contact_phone1() != null
				&& !"null".equals(mib.getSender_contact_phone1().trim())) {
			values.put("sender_contact_phone1", mib.getSender_contact_phone1());
		} else {
			values.put("sender_contact_phone1", "");
		}
		if (mib.getRcver_name() != null
				&& !"null".equals(mib.getRcver_name().trim())) {
			values.put("rcver_name", mib.getRcver_name());
		} else {
			values.put("rcver_name", "");
		}
		values.put("the_class_date", mib.getThe_class_date());
		if (mib.getRcver_street_addr() != null
				&& !"null".equals(mib.getRcver_street_addr().trim())) {
			values.put("rcver_street_addr", mib.getRcver_street_addr());
		} else {
			values.put("rcver_street_addr", "");
		}
		if (mib.getSender_name() != null
				&& !"null".equals(mib.getSender_name().trim())) {
			values.put("sender_name", mib.getSender_name());
		} else {
			values.put("sender_name", "");
		}
		if (mib.getSender_loc_prov() != null
				&& !"null".equals(mib.getSender_loc_prov().trim())) {
			values.put("sender_loc_prov", mib.getSender_loc_prov());
		} else {
			values.put("sender_loc_prov", "");
		}
		if (mib.getFrequence() != null
				&& !"null".equals(mib.getFrequence().trim())) {
			values.put("frequence", mib.getFrequence());
		} else {
			values.put("frequence", "");
		}
		if (mib.getSender_loc_city() != null
				&& !"null".equals(mib.getSender_loc_city().trim())) {
			values.put("sender_loc_city", mib.getSender_loc_city());
		} else {
			values.put("sender_loc_city", "");
		}
		if (mib.getRcver_loc_city() != null
				&& !"null".equals(mib.getRcver_loc_city().trim())) {
			values.put("rcver_loc_city", mib.getRcver_loc_city());
		} else {
			values.put("rcver_loc_city", "");
		}
		if (mib.getMail_num() != null
				&& !"null".equals(mib.getMail_num().trim())) {
			values.put("mail_num", mib.getMail_num());
		} else {
			values.put("mail_num", "");
		}
		if (mib.getMoney() == null || "".equals(mib.getMoney().trim())
				|| "0".equals(mib.getMoney())) {
			values.put("money", 0.0);
		} else {
			values.put("money", mib.getMoney());
		}
		if (mib.getSender_addr() != null
				&& !"null".equals(mib.getSender_addr().trim())) {
			values.put("sender_addr", mib.getSender_addr());
		} else {
			values.put("sender_addr", "");
		}
		if (mib.getRcver_addr() != null
				&& !"null".equals(mib.getRcver_addr().trim())) {
			values.put("rcver_addr",
					mib.getRcver_addr() != null ? mib.getRcver_addr() : "");
		} else {
			values.put("rcver_addr", "");
		}
		values.put("sid", Integer.MIN_VALUE);// sid
		values.put("dlv_pseg_code", mib.getDlv_pseg_code());// 道段号
		values.put("type", Constant.OPER_NULL);// 设置无操作
		values.put("distance", Integer.MAX_VALUE);// 设置距离为最远
		values.put("pay_type", "1");
		values.put("pay_type", mib.getFlag() != null ? mib.getFlag() : "");
		values.put("dealResult", Constant.DAICHULI);

		if ("1".equals(mib.getFlag())) {
			values.put("actualGoodsFee", "3");
		} else if ("2".equals(mib.getFlag())) {
			values.put("actualGoodsFee", "4");
		} else if ("".equals(mib.getFlag()) || mib.getFlag() == null) {
			values.put("actualGoodsFee", "");
		}
		values.put("address", "1");
		values.put("weight", "1");
		values.put("call_time", "0");
		values.put("msg_time", "0");
		values.put("dlv_date", mib.getDlv_date());
		values.put("dlv_time", mib.getDlv_time());
		values.put("in_self_group", Constant.NOT_INGROUP);
		values.put("notified", Constant.NOT_NOTIFY);
		values.put("flag", mib.getFlag());
		values.put("sendmsg_by_user", Constant.MSG_NEVER_BY_USER);
		values.put("picture", "");
		values.put("checked", Constant.CHECKED_FALSE);// 本条记录是否在妥投队列中；0不在，1在。
		db.insert(DELIVER_TABLE, null, values);
	}

	/**
	 * 更改上传日期，时间
	 * 
	 * @param localMail
	 * @param dlv_date
	 * @param dlv_time
	 */
	public synchronized void updatedlv_info(List<MessageInfoBean> localMail,
			String dlv_date, String dlv_time) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < localMail.size(); i++) {
			String sql = "update " + DELIVER_TABLE + " set dlv_date='"
					+ dlv_date + "',dlv_time='" + dlv_time + "' where _id="
					+ localMail.get(i).get_id();
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 根据flag查出邮件号个数
	 * 
	 * @param flag
	 * @return
	 */
	public ArrayList<MessageInfoBean> queryPaymentMails(int flag,
			String frequence) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from  " + DELIVER_TABLE + " where flag=" + flag
				+ " and in_self_group=" + Constant.NOT_INGROUP
				+ " and frequence='" + frequence + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	public ArrayList<MessageInfoBean> queryAllMails() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from  " + DELIVER_TABLE;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 将数据放入自定义组
	 * 
	 * @param idList
	 *            _id 集合
	 */
	public synchronized void updateToSelfGroup(String mail_num) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + DELIVER_TABLE + " set in_self_group="
				+ Constant.INGROUP + " where mail_num='" + mail_num + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 在分组内模糊查询
	 * 
	 * @param queryInfo
	 *            查询条件
	 * @return
	 */
	public List<MessageInfoBean> queryInGroup_m(String groupName,
			String frequence, String queryInfo, Context context) {
		List<MessageInfoBean> list = queryForGroup(groupName, frequence,
				context);
		List<MessageInfoBean> data = new ArrayList<MessageInfoBean>();
		for (int i = 0; i < list.size(); i++) {
			MessageInfoBean bean = list.get(i);
			String string_m = bean.toString_m();
			if (string_m.contains(queryInfo)) {
				data.add(bean);
			}
		}
		return data;

	}

	/**
	 * 将数据放入自定义组
	 * 
	 * @param idList
	 *            _id 集合
	 */
	public synchronized void updateToSelfGroup(ArrayList<Integer> idList) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < idList.size(); i++) {
			String sql = "update " + DELIVER_TABLE + " set in_self_group="
					+ Constant.INGROUP + " where _id=" + idList.get(i);
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 将根据_id移出自定义分组
	 * 
	 * @param idList
	 *            _id 集合
	 */
	public synchronized void updateOutSelfGroup(ArrayList<Integer> idList) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < idList.size(); i++) {
			String sql = "update " + DELIVER_TABLE + " set in_self_group="
					+ Constant.NOT_INGROUP + " where _id=" + idList.get(i);
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 给通话次数+1
	 * 
	 * @param _id
	 */
	public synchronized void addCallTime(int _id) {
		SQLiteDatabase db = getWritableDatabase();
		int time = queryCallTime(_id);
		++time;
		String sql = "update " + DELIVER_TABLE + " set call_time=" + time
				+ " where _id=" + _id;
		db.execSQL(sql);
		db.close();
	}

	public synchronized void addCallTime_PhoneNum(String phoneNum) {
		SQLiteDatabase db = getWritableDatabase();
		int time = queryCallTime_PhoneNum(phoneNum);
		++time;
		String sql = "update " + DELIVER_TABLE + " set call_time=" + time
				+ " where rcver_contact_phone1='" + phoneNum + "'";
		db.execSQL(sql);
		db.close();
	}

	public int queryCallTime_PhoneNum(String phoneNum) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE
				+ " where rcver_contact_phone1='" + phoneNum + "'";
		Cursor cursor = db.rawQuery(sql, null);
		int call_time = 0;
		// cursor.moveToNext();
		if (cursor.moveToNext()) {
			call_time = cursor.getInt(cursor.getColumnIndex("call_time"));
		}

		cursor.close();
		return call_time;
	}

	// public int queryInQueue(String mail_num) {
	// SQLiteDatabase db = getReadableDatabase();
	// String sql = "select * from " + DELIVER_TABLE
	// + " where queue_task_type=" + Constant.QUEUE_GOUWUCHE
	// + " and mail_num='" + mail_num + "'";
	// Cursor cursor = db.rawQuery(sql, null);
	// return cursor.getCount();
	// }

	/**
	 * 给短信次数+1
	 */
	public synchronized void addMsgTime(int _id) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where _id=" + _id;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToNext();
		int call_time = cursor.getInt(cursor.getColumnIndex("msg_time"));
		++call_time;
		String sql2 = "update " + DELIVER_TABLE + " set msg_time=" + call_time
				+ " where _id=" + _id;
		db.execSQL(sql2);
		cursor.close();
		db.close();
	}

	public synchronized void setDeliverMailNeverRequest(String mail_num) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + DELIVER_TABLE + " set sid="
				+ Integer.MAX_VALUE + " where mail_num='" + mail_num + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 给短信次数+1
	 */
	public synchronized void addListMsgTime(ArrayList<Integer> list) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < list.size(); i++) {
			int _id = list.get(i);
			String sql = "select * from " + DELIVER_TABLE + " where _id=" + _id;
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToNext();
			int call_time = cursor.getInt(cursor.getColumnIndex("msg_time"));
			++call_time;
			String sql2 = "update " + DELIVER_TABLE + " set msg_time="
					+ call_time + " where _id=" + _id;
			db.execSQL(sql2);
			cursor.close();
		}
		db.close();
	}

	/**
	 * 发送短信后刷新数据
	 */
	public synchronized List<MessageInfoBean> notifyForMsg(int _id,
			String frequence, Context context, String groupName) {
		SQLiteDatabase db = getWritableDatabase();
		int time = queryMsgTime(_id);
		++time;
		String sql = "update " + DELIVER_TABLE + " set msg_time=" + time
				+ " where _id=" + _id;
		db.execSQL(sql);
		List<MessageInfoBean> list = queryForGroup(groupName, frequence,
				context);
		db.close();
		return list;
	}

	/**
	 * 查询打电话次数
	 */
	public int queryCallTime(int _id) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where _id=" + _id;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToNext();
		int call_time = cursor.getInt(cursor.getColumnIndex("call_time"));
		cursor.close();
		return call_time;
	}

	/**
	 * 查询在自定义分组中的数据
	 * 
	 * @return
	 */
	public ArrayList<MessageInfoBean> querySelfGroupMail() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where in_self_group="
				+ Constant.INGROUP;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 查询发短信次数
	 */
	public int queryMsgTime(int _id) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where _id=" + _id;
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			int call_time = cursor.getInt(cursor.getColumnIndex("msg_time"));
			cursor.close();
			return call_time;
		}
		return 0;
	}

	/**
	 * 根据邮件号查询
	 */
	public List<MessageInfoBean> queryByMailNumber(String mailNumber) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where mail_num='"
				+ mailNumber + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 根据邮件号删除一条数据
	 */
	public synchronized void deleteByMailNumber(String mail_num) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "delete from " + DELIVER_TABLE + " where mail_num='"
				+ mail_num + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 查询到名址信息之后，更新邮件信息
	 */
	public synchronized void updateDeliverMail(ArrayList<MessageInfoBean> mibs) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < mibs.size(); i++) {
			insertDeliver(mibs.get(i));
		}
		db.close();
	}

	public synchronized void deleteMails(ArrayList<String> mails) {
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < mails.size(); i++) {
			String sql = "delete from " + DELIVER_TABLE + " where mail_num='"
					+ mails.get(i) + "'";
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 在妥投邮件中根据邮件号查询
	 */
	public List<MessageInfoBean> queryByMailNumberInTuotou(String mailNumber) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where mail_num='"
				+ mailNumber + "' and dealResult=" + Constant.TUOTOU;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 根据处理结果查询邮件 待处理：Constant.DAICHULI 妥投：Constant.TUOTOU
	 * 未妥投：Constant.WEITUOTOU
	 * 
	 * @return
	 */
	public List<MessageInfoBean> queryByDealResult(int dealResult,
			String frequence) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = null;
		switch (dealResult) {
		case Constant.TUOTOU:

		case Constant.WEITUOTOU:
			sql = "select distinct dt.* from " + DELIVER_TABLE
					+ " dt left join " + QUEUE_TABLE
					+ " qt on dt.mail_num=qt.mail_num where dt.dealResult="
					+ dealResult + " and dt.frequence='" + frequence + "'"
					+ " order by qt.commit_result asc,qt.dlv_time desc";
			break;
		default:
			sql = "select * from " + DELIVER_TABLE + " where dealResult="
					+ dealResult + " and in_self_group=" + Constant.NOT_INGROUP
					+ " and frequence='" + frequence
					+ "' order by distance asc";
			break;
		}
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	public List<MessageInfoBean> queryByDealResultAll(int dealResult) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = null;
		switch (dealResult) {
		case Constant.DAICHULI:
			// sql = "select * from " + DELIVER_TABLE + " where dealResult="
			// + dealResult + " and in_self_group=" + Constant.NOT_INGROUP
			// + " order by distance asc";
			sql = "select distinct dt.* from " + DELIVER_TABLE
					+ " dt left join " + QUEUE_TABLE
					+ " qt on dt.mail_num=qt.mail_num where dt.dealResult="
					+ dealResult
					+ " order by qt.commit_result asc,qt.dlv_time desc";
			break;
		case Constant.WEITUOTOU:
			sql = "select distinct dt.* from " + DELIVER_TABLE
					+ " dt left join " + QUEUE_TABLE
					+ " qt on dt.mail_num=qt.mail_num where dt.dealResult="
					+ dealResult
					+ " order by qt.commit_result asc,qt.dlv_time desc";
			break;
		case Constant.TUOTOU:
			sql = "select distinct dt.* from " + DELIVER_TABLE
					+ " dt left join " + QUEUE_TABLE
					+ " qt on dt.mail_num=qt.mail_num where dt.dealResult="
					+ dealResult
					+ " order by qt.commit_result asc,qt.dlv_time desc";
			break;
		}
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 根据处理结果查询邮件,查询所有频次下的邮件 待处理：Constant.DAICHULI 妥投：Constant.TUOTOU
	 * 未妥投：Constant.WEITUOTOU
	 * 
	 * @return
	 */
	public List<MessageInfoBean> queryByDealResultAllFrequence(int dealResult) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = null;
		switch (dealResult) {
		case Constant.TUOTOU:
		case Constant.WEITUOTOU:
			sql = "select distinct dt.* from " + DELIVER_TABLE
					+ " dt left join " + QUEUE_TABLE
					+ " qt on dt.mail_num=qt.mail_num where dt.dealResult="
					+ dealResult
					+ " order by qt.commit_result asc,qt.dlv_time desc";
			break;
		default:
			sql = "select * from " + DELIVER_TABLE + " where dealResult="
					+ dealResult + " and in_self_group=" + Constant.NOT_INGROUP
					+ " order by distance asc";
			break;
		}
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 查询未上传的邮件（按照频次查询）
	 */
	public List<MessageInfoBean> queryUncommitMail(Context context,
			String frequence) {
		SQLiteDatabase db = getReadableDatabase();
		UserInfoUtils user = new UserInfoUtils(context);
		String sql = "select distinct dt.* from " + DELIVER_TABLE
				+ " dt left join " + QUEUE_TABLE
				+ " qt on dt.mail_num=qt.mail_num where qt.commit_result="
				+ Constant.UNCOMMIT + " and qt.orgCode='"
				+ user.getUserDelvorgCode() + "' and qt.userName='"
				+ user.getUserName() + "' and dt.frequence='" + frequence + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 查询未上传的邮件（查询所有的）
	 */
	public List<MessageInfoBean> queryUncommitMail(Context context) {
		SQLiteDatabase db = getReadableDatabase();
		UserInfoUtils user = new UserInfoUtils(context);
		String sql = "select distinct dt.* from " + DELIVER_TABLE
				+ " dt left join " + QUEUE_TABLE
				+ " qt on dt.mail_num=qt.mail_num where qt.commit_result="
				+ Constant.UNCOMMIT + " and qt.orgCode='"
				+ user.getUserDelvorgCode() + "' and qt.userName='"
				+ user.getUserName() + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 查询未妥投+待处理的邮件
	 * 
	 * @return
	 */
	public List<MessageInfoBean> querybesidesTuoTou() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where dealResult!="
				+ Constant.TUOTOU;
		Cursor cursor = db.rawQuery(sql, null);
		List<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		list = parseCursorToList(cursor);
		return list;
	}

	// 查询附近的任务
	public List<MessageInfoBean> queryNearTask(String distance) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where distance<="
				+ distance;
		Cursor cursor = database.rawQuery(sql, null);
		List<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		list = parseCursorToList(cursor);
		return list;
	}

	/**
	 * 根据_id查询头地标信息
	 * 
	 * @return
	 */
	public MessageInfoBean query_id(int _id) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where _id=" + _id;

		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		list = parseCursorToList(cursor);
		return list.get(0);
	}

	/**
	 * 根据_id更新投递表手机号
	 * **/
	public synchronized void updatePhone(int _id, String rcver_contact_phone1) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + DELIVER_TABLE + " set rcver_contact_phone1="
				+ rcver_contact_phone1 + " where _id=" + _id;
		database.execSQL(sql);
		database.close();
	}

	/**
	 * 更改邮件集合发送通知的状态，标记为已发送过
	 * 
	 */
	public synchronized void setMailListSendNotify(List<MessageInfoBean> beans) {
		SQLiteDatabase database = getWritableDatabase();
		for (int i = 0; i < beans.size(); i++) {
			MessageInfoBean bean = beans.get(i);
			String sql = "update " + DELIVER_TABLE + " set notified="
					+ Constant.NOTIFY + " where _id=" + bean.get_id();
			database.execSQL(sql);
		}
		database.close();
	}

	/**
	 * 投递缴费查询
	 */
	public synchronized List<MessageInfoBean> queryByTypeMoney() {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where dealResult="
				+ Constant.TUOTOU + " and money!='0.0'";
		Cursor cursor = database.rawQuery(sql, null);
		try {
			ArrayList<MessageInfoBean> list = parseCursorToList(cursor);
			return list;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 分组查询
	 * 
	 * @param groupName
	 *            分组名
	 * @return 查询结果集
	 */
	public synchronized List<MessageInfoBean> queryForGroup(String groupName,
			String frequence, Context context) {// TODO
		SQLiteDatabase database = getReadableDatabase();
		List<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		if ("待处理".equals(groupName)) {
			if (!frequence.equals("")) {
				list = queryByDealResult(Constant.DAICHULI, frequence);// 查出待处理邮件
			} else {
				list = queryByDealResultAll(Constant.DAICHULI);
			}
		} else if ("未妥投".equals(groupName)) {
			if (!frequence.equals("")) {
				list = queryByDealResult(Constant.WEITUOTOU, frequence);// 查出未妥投邮件
			} else {
				list = queryByDealResultAll(Constant.WEITUOTOU);
			}

		} else if ("妥投".equals(groupName)) {
			if (!frequence.equals("")) {
				list = queryByDealResult(Constant.TUOTOU, frequence);// 查出妥投邮件
			} else {
				list = queryByDealResultAll(Constant.TUOTOU);
			}

		} else if ("其他".equals(groupName)) {// 其他邮件
			list = queryOthers(context, frequence);
		} else if ("未上传".equals(groupName)) {// 未上传邮件
			if (!frequence.equals("")) {
				list = queryUncommitMail(context, frequence);
			} else {
				list = queryUncommitMail(context);
			}
		} else if ("附近邮件".equals(groupName)) {
			list = queryLocalNotTuoTou(context, frequence);
		} else if ("代收邮件".equals(groupName)) {
			list = queryPaymentMails(1, frequence);
		} else if ("收件人付费".equals(groupName)) {
			list = queryPaymentMails(2, frequence);
		} else {// 根据地址模糊查询
			String sql = "select * from " + DELIVER_TABLE
					+ " where rcver_street_addr like '%" + groupName
					+ "%' and in_self_group=" + Constant.NOT_INGROUP
					+ " and frequence='" + frequence
					+ "' order by dealResult asc,distance asc";
			Cursor cursor = database.rawQuery(sql, null);
			list = parseCursorToList(cursor);
		}

		return list;
	}

	/**
	 * 查出分组内批量操作的邮件，不包括妥投邮件
	 * 
	 * @return
	 */
	public List<MessageInfoBean> queryForGroupNotTuotou(String groupName,
			String frequence, Context context) {
		SQLiteDatabase database = getReadableDatabase();
		List<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		if ("待处理".equals(groupName)) {
			list = queryByDealResult(Constant.DAICHULI, frequence);// 查出待处理邮件
		} else if ("未妥投".equals(groupName)) {
			list = queryByDealResult(Constant.WEITUOTOU, frequence);// 查出未妥投邮件
		} else if ("妥投".equals(groupName)) {
			list = queryByDealResult(Constant.TUOTOU, frequence);// 查出妥投邮件
		} else if ("其他".equals(groupName)) {// 其他邮件
			list = queryOthers(context, frequence);
		} else if ("未上传".equals(groupName)) {// 未上传邮件
			list = queryUncommitMail(context, frequence);
		} else if ("附近邮件".equals(groupName)) {
			list = queryLocalNotTuoTou(context, frequence);
		} else if ("代收邮件".equals(groupName)) {
			list = queryPaymentMails(1, frequence);
		} else if ("收件人付费".equals(groupName)) {
			list = queryPaymentMails(2, frequence);
		} else {// 根据地址模糊查询:1-不在自定义分组中 2-未执行妥投操作的
			String sql = "select * from " + DELIVER_TABLE
					+ " where rcver_street_addr like '%" + groupName
					+ "%' and in_self_group=" + Constant.NOT_INGROUP
					+ " and dealResult!=" + Constant.TUOTOU
					+ " and frequence='" + frequence + "'";
			Cursor cursor = database.rawQuery(sql, null);
			list = parseCursorToList(cursor);
		}
		return list;
	}

	/**
	 * 查询”其他“分组
	 * 
	 * @param context
	 * @return
	 */
	public synchronized ArrayList<MessageInfoBean> queryOthers(Context context,
			String frequence) {
		SQLiteDatabase db = getReadableDatabase();
		GroupDao groupDao = DeliverDaoFactory.getInstance()
				.getGroupDao(context);
		ArrayList<String> keys = groupDao.queryKeys();
		StringBuffer sb = new StringBuffer();
		sb.append("not like ");
		for (int i = 0; i < keys.size(); i++) {
			sb.append("'%" + keys.get(i) + "%' and rcver_street_addr not like ");
		}
		sb.append("'%abc%'");
		String sql = "select * from " + DELIVER_TABLE
				+ " where rcver_street_addr " + sb.toString()
				+ " and in_self_group=" + Constant.NOT_INGROUP
				+ " and frequence='" + frequence
				+ "' order by dealResult asc,distance asc";
		return parseCursorToList(db.rawQuery(sql, null));
	}

	// /**
	// * 根据输入条件模糊查询,查询范围：非妥投邮件；匹配范围：手机号or收件人姓名or收件地址or邮件号
	// */
	// public List<MessageInfoBean> queryNotTuoTou_m(String groupName,
	// String queryInfo, Context context) {
	// SQLiteDatabase database = getReadableDatabase();
	// List<MessageInfoBean> list = queryForGroup(groupName, context);
	// List<MessageInfoBean> data = new ArrayList<MessageInfoBean>();
	// for (int i = 0; i < list.size(); i++) {
	// MessageInfoBean bean = list.get(i);
	// String string_m = bean.toString_m();
	// if (string_m.contains(queryInfo)) {
	// data.add(bean);
	// }
	// }
	// // String sql = "select * from " + DELIVER_TABLE
	// // + " where dealResult!=" + Constant.TUOTOU
	// // + " and rcver_contact_phone1 like '%" + queryInfo
	// // + "%' or rcver_name like '%" + queryInfo
	// // + "%' or rcver_street_addr like '%" + queryInfo
	// // + "%' or mail_num like '%" + queryInfo
	// // + "%' order by distance asc";
	// Cursor cursor = database.rawQuery(sql, null);
	// return parseCursorToList(cursor);
	// }

	/**
	 * 查询需要到付的邮件
	 * 
	 * @return
	 */
	public ArrayList<MessageInfoBean> queryMoneyMail() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where money!='0.0'";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<MessageInfoBean> list = parseCursorToList(cursor);
		return list;
	}

	/**
	 * 根据邮件号查询表中数据是否存在 没有为真
	 * 
	 * @return
	 */
	public boolean queryMail(String mail) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where mail_num='"
				+ mail + "'";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<MessageInfoBean> list = parseCursorToList(cursor);
		return list == null || list.size() == 0;
	}

	/**
	 * 删除下段数据
	 */
	public synchronized void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(DELIVER_TABLE, null, null);
		db.close();
	}

	/**
	 * 查询附近未妥投的邮件
	 * 
	 * @return
	 */
	public synchronized ArrayList<MessageInfoBean> queryLocalNotTuoTou(
			Context context, String frequence) {
		SQLiteDatabase db = getReadableDatabase();
		String distance = SharePreferenceUtilDaoFactory.getInstance(context)
				.getDW_distance();// 用户设置的定位范围
		String sql = "select * from " + DELIVER_TABLE + " where dealResult!="
				+ Constant.TUOTOU + " and distance<" + distance
				+ " and frequence='" + frequence + "' order by distance asc";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<MessageInfoBean> list = parseCursorToList(cursor);
		return list;
	}

	/**
	 * 查询附近未妥投的邮件
	 * 
	 * @return
	 */
	public synchronized ArrayList<MessageInfoBean> queryLocalNotTuoTouAllFrequence(
			Context context) {
		SQLiteDatabase db = getReadableDatabase();
		String distance = SharePreferenceUtilDaoFactory.getInstance(context)
				.getDW_distance();// 用户设置的定位范围
		String sql = "select * from " + DELIVER_TABLE + " where dealResult!="
				+ Constant.TUOTOU + " and distance<" + distance
				+ " order by distance asc";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<MessageInfoBean> list = parseCursorToList(cursor);
		return list;
	}

	/**
	 * 查询附近需要预妥投和需要弹通知的邮件 规则：查询处理结果不是妥投且不是预妥投的且距离限定内的邮件
	 * 
	 * @return
	 */
	public synchronized List<MessageInfoBean> queryLocalNotifationMail(
			Context context) {
		SQLiteDatabase db = getReadableDatabase();
		String distance = SharePreferenceUtilDaoFactory.getInstance(context)
				.getDW_distance();// 用户设置的定位范围
		String sql = "select * from " + DELIVER_TABLE + " where dealResult!="
				+ Constant.TUOTOU + " and notified=" + Constant.NOT_NOTIFY
				+ " and distance<" + distance;
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<MessageInfoBean> list = parseCursorToList(cursor);
		return list;
	}

	/**
	 * 查询最大的sid
	 * 
	 * @return
	 */
	public int queryMaxSid() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select max(sid) as maxSid from " + DELIVER_TABLE;
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			return cursor.getInt(cursor.getColumnIndex("maxSid"));
		}
		return 0;
	}

	/**
	 * 查询附近未妥投而且是到付类型,并且没有发送过通知的邮件
	 * 
	 * @return
	 */
	public synchronized List<MessageInfoBean> queryLocalMoney(Context context) {
		SQLiteDatabase db = getReadableDatabase();
		String distance = SharePreferenceUtilDaoFactory.getInstance(context)
				.getDW_distance();// 用户设置的定位范围
		String sql = "select * from " + DELIVER_TABLE + " where dealResult!="
				+ Constant.TUOTOU + " and notified=" + Constant.NOT_NOTIFY
				+ " and distance<" + distance + " and money!=0.0";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<MessageInfoBean> list = parseCursorToList(cursor);
		return list;
	}

	/**
	 * 根据邮件号模糊查询,查询结果不包含妥投邮件
	 */
	public synchronized ArrayList<String> queryMailNumber_m(String s,
			ArrayList<String> checkedList) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE
				+ " where mail_num like '%" + s + "%' and dealResult!="
				+ Constant.TUOTOU;
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<MessageInfoBean> list = parseCursorToList(cursor);
		ArrayList<String> mailNumberList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String mail_num = list.get(i).getMail_num();
			if (!inChecked(mail_num, checkedList)) {
				mailNumberList.add(mail_num);
			}
		}
		return mailNumberList;
	}

	/**
	 * mail_num是否在checkedList集合里面
	 * 
	 * @param mail_num
	 * @param checkedList
	 * @return
	 */
	public synchronized boolean inChecked(String mail_num,
			ArrayList<String> checkedList) {
		for (int i = 0; i < checkedList.size(); i++) {
			if (mail_num.equals(checkedList.get(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 更新未妥投邮件经纬度信息
	 * 
	 * @param locationinfoList
	 *            其中的Map应包含三个key：latlng,distance,_id
	 */
	public synchronized void updateMailLocationInfo(
			ArrayList<Map<String, Object>> locationinfoList) {
		SQLiteDatabase database = getWritableDatabase();
		for (int i = 0; i < locationinfoList.size(); i++) {
			Map<String, Object> map = locationinfoList.get(i);
			String sql = "update " + DELIVER_TABLE + " set latlng='"
					+ map.get("latlng") + "',distance='" + map.get("distance")
					+ "' where _id=" + map.get("_id") + " and dealResult!="
					+ Constant.TUOTOU;
			database.execSQL(sql);
		}
		database.close();
	}

	/**
	 * 将下段表cursor转化成List
	 */
	public synchronized ArrayList<MessageInfoBean> parseCursorToList(
			Cursor cursor) {
		ArrayList<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		while (cursor.moveToNext()) {
			MessageInfoBean bean = new MessageInfoBean(
					cursor.getString(cursor.getColumnIndex("rcver_loc_county")),
					cursor.getString(cursor
							.getColumnIndex("rcver_contact_phone1")),
					cursor.getString(cursor.getColumnIndex("sender_loc_county")),
					cursor.getString(cursor
							.getColumnIndex("sender_street_addr")),
					cursor.getString(cursor.getColumnIndex("rcver_loc_prov")),
					cursor.getString(cursor
							.getColumnIndex("sender_contact_phone1")),
					cursor.getString(cursor.getColumnIndex("rcver_name")),
					cursor.getString(cursor.getColumnIndex("the_class_date")),
					cursor.getString(cursor.getColumnIndex("rcver_street_addr")),
					cursor.getString(cursor.getColumnIndex("sender_name")),
					cursor.getString(cursor.getColumnIndex("sender_loc_prov")),
					cursor.getString(cursor.getColumnIndex("frequence")),
					cursor.getString(cursor.getColumnIndex("sender_loc_city")),
					cursor.getString(cursor.getColumnIndex("rcver_loc_city")),
					cursor.getString(cursor.getColumnIndex("mail_num")), cursor
							.getString(cursor.getColumnIndex("sender_addr")),
					cursor.getString(cursor.getColumnIndex("rcver_addr")),
					cursor.getString(cursor.getColumnIndex("money")), cursor
							.getInt(cursor.getColumnIndex("distance")), cursor
							.getInt(cursor.getColumnIndex("type")), cursor
							.getString(cursor.getColumnIndex("latlng")), cursor
							.getInt(cursor.getColumnIndex("_id")), cursor
							.getString(cursor.getColumnIndex("pay_type")),
					cursor.getString(cursor.getColumnIndex("actualGoodsFee")),
					cursor.getString(cursor.getColumnIndex("address")), cursor
							.getString(cursor.getColumnIndex("weight")));
			bean.setCharge(cursor.getString(cursor.getColumnIndex("money")));
			bean.setChecked(cursor.getString(cursor.getColumnIndex("checked")));
			bean.setDealResult(cursor.getInt(cursor
					.getColumnIndex("dealResult")));
			bean.setMsg_time(cursor.getInt(cursor.getColumnIndex("msg_time")));
			bean.setCall_time(cursor.getInt(cursor.getColumnIndex("call_time")));
			bean.setDlv_pseg_code(cursor.getString(cursor
					.getColumnIndex("dlv_pseg_code")));
			if ("1".equals(cursor.getString(cursor.getColumnIndex("flag")))) {
				bean.setActualGoodsFee("3");
			} else if ("2".equals(cursor.getString(cursor
					.getColumnIndex("flag")))) {
				bean.setActualGoodsFee("4");
			}
			bean.setIn_self_group(cursor.getString(cursor
					.getColumnIndex("in_self_group")));
			bean.setNotified(cursor.getString(cursor.getColumnIndex("notified")));
			bean.setPicture(cursor.getString(cursor.getColumnIndex("picture")));
			bean.setQueue_task_type(cursor.getInt(cursor
					.getColumnIndex("queue_task_type")));
			bean.setMsg_user(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("sendmsg_by_user"))));
			bean.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
			bean.setDlv_date(cursor.getString(cursor.getColumnIndex("dlv_date")));
			bean.setDlv_time(cursor.getString(cursor.getColumnIndex("dlv_time")));
			bean.setSid(cursor.getInt(cursor.getColumnIndex("sid")));
			bean.setLtData(bean.getDlv_date() + bean.getDlv_time());
			list.add(bean);
		}
		cursor.close();
		return list;
	}

	/**
	 * 妥投拍照，将图片名字存储到picture字段
	 * 
	 * @param _id
	 * @param pictureName
	 *            图片名，以.png结尾
	 */
	public synchronized void saveMailPicture(int _id, String pictureName) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + DELIVER_TABLE + " set picture='" + pictureName
				+ "' where _id=" + _id;
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 更改投递状态
	 * 
	 * @param _id
	 * @param type
	 * @return
	 */
	public synchronized void updateMailDealResult(int _id, int dealResult) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + DELIVER_TABLE + " set dealResult="
				+ dealResult + " where _id=" + _id;
		database.execSQL(sql);
		database.close();
	}

	/**
	 * 判断输入的邮件号是不是在分组中，如果在则返回true
	 * 
	 * @param mail_num
	 * @return
	 */
	public synchronized boolean mailInGroup(String mail_num) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select in_self_group from " + DELIVER_TABLE
				+ " where mail_num='" + mail_num + "'";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			String in_self_group = cursor.getString(cursor
					.getColumnIndex("in_self_group"));
			cursor.close();
			if (in_self_group.equals(Constant.INGROUP + "")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 批量更改投递状态
	 * 
	 * @return
	 */
	public synchronized void updateListMailDealResult(ArrayList<Integer> list,
			int dealResult) {
		SQLiteDatabase database = getWritableDatabase();
		for (int i = 0; i < list.size(); i++) {
			String sql = "update " + DELIVER_TABLE + " set dealResult="
					+ dealResult + " where _id=" + list.get(i);
			database.execSQL(sql);
		}
		database.close();
	}

	/**
	 * 根据_id查询记录，返回值为cursor
	 */
	public synchronized Cursor queryBy_id_Detail(int id) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where _id=" + id;
		Cursor cursor = database.rawQuery(sql, null);
		return cursor;
	}

	public synchronized List<MessageInfoBean> queryischeck(Context context) {// 4
																				// checked是1
		ArrayList<MessageInfoBean> list = new ArrayList<MessageInfoBean>();
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + DELIVER_TABLE + " where checked="
				+ Constant.CHECKED_TRUE + "";
		Cursor cursor = database.rawQuery(sql, null);
		try {
			list = parseCursorToList(cursor);
			return list;
		} catch (Exception e) {
		}
		return list;
	}

	public synchronized void updateMsgUser(int _id) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + DELIVER_TABLE + " set sendmsg_by_user="
				+ Constant.MSG_BY_USER + " where _id=" + _id;
		db.execSQL(sql);
		db.close();
	}

	/***
	 * 根据_id查询记录，返回值为cursor
	 * **/
	public synchronized Cursor queryBy_id_Detail(String id, String tableName) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + tableName + " where _id=" + id;
		Cursor cursor = database.rawQuery(sql, null);
		return cursor;
	}
}

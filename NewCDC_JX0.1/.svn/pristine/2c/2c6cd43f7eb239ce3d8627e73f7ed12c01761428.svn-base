package com.newcdc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newcdc.db.helper.GroupDaoHelper;
import com.newcdc.model.GroupBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Utils;

public class GroupDao extends GroupDaoHelper {

	/**
	 * 分组dao
	 * 
	 * @author liunannan
	 * */
	public GroupDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	/**
	 * 是否飞过单
	 */
	public boolean hasFeed(int _id) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select * from " + GROUP_TABLE + " where _id=" + _id;
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToNext();
		if (cursor.getInt(cursor.getColumnIndex("fee")) == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 设置一条数据飞单成功
	 */
	public void setFeed(int _id) {
		SQLiteDatabase database = getReadableDatabase();
		database.execSQL("update " + GROUP_TABLE + " set fee=1 where _id="
				+ _id);
		database.close();
	}

	/**
	 * 给新增的分组添加sid属性
	 */
	public synchronized void setSid(String sid, String groupName) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + GROUP_TABLE + " set sid=" + sid
				+ " where group_content='" + groupName + "'";
		database.execSQL(sql);
		database.close();
	}

	/**
	 * 更改分组名
	 */
	public synchronized void updateGroupName(String sid, String newName,
			Context context) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + GROUP_TABLE + " set group_content='" + newName
				+ "',mail_count='" + getOtherGroupMailCount(newName, context)
				+ "' where sid=" + sid;
		database.execSQL(sql);
		database.close();
	}

	/**
	 * 插入分组信息
	 */
	public synchronized void insertGroup(List<GroupBean> list, Context context) {
		SQLiteDatabase db = getWritableDatabase();
		DeliverDao deliverDao = DeliverDaoFactory.getInstance().getDeliverDao(
				context);
		for (int i = 0; i < list.size(); i++) {
			GroupBean group = list.get(i);
			ContentValues values = new ContentValues();
			int group_type = Integer.parseInt(group.getGroup_type());// 分组类型，共5种
			String group_content = group.getGroup_content();// 分组名
			values.put("sid", group.getSid());
			values.put("group_content", group_content);
			values.put("group_type", group_type);
			values.put("create_by", group.getCreate_by());
			values.put("create_time", group.getCreate_time());
			values.put("update_by", group.getUpdate_by());
			values.put("update_time", group.getUpdate_time());
			values.put("latlng", group.getLatlng());
			values.put("distance", group.getDistance());
			switch (group_type) {
			case Constant.TUOTOU:
			case Constant.WEITUOTOU:
			case Constant.DAICHULI:
				// 逗号分隔的每个频次下的邮件个数
				values.put(
						"mail_count",
						getMailCountOfDealResult(context,
								Integer.parseInt(group.getGroup_type())));
				// values.put(
				// "mail_count",
				// deliverDao.queryByDealResult(
				// Integer.parseInt(group.getGroup_type())).size());
				// 根据分组类型，查询出个数
				break;
			case Constant.UNCOMMIT:// 查询出未上传的邮件个数

				values.put("mail_count", getUncommitMailCount(context));
				//
				// values.put("mail_count",
				// deliverDao.queryUncommitMail(context)
				// .size());// 查询出未上传的邮件个数
				break;
			case Constant.OTHERGROUP:// 服务器返回的分组，根据分组名模糊查询即可
				values.put("mail_count",
						getOtherGroupMailCount(group_content, context));
				// values.put("mail_count",
				// deliverDao.queryForGroup(group_content, context).size());//
				// 服务器返回的分组，根据分组名模糊查询即可
				break;
			case Constant.LOCALMAIL:
				// 附近邮件分组要等定位完成才更新数量,初始为0个
				values.put("mail_count", 0);
				break;
			case Constant.DAISHOUGROUP:
				values.put("mail_count", 0);
				break;
			case Constant.SHOUJIANRENPAY:
				values.put("mail_count", 0);
				break;
			}
			db.insert(GROUP_TABLE, null, values);
		}
		db.close();
	}

	private String getMailCountOfDealResult(Context context, int dealResult) {
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		DeliverDao deliverDao = daoFactory.getDeliverDao(context);
		DeliverTaskListDao taskDao = daoFactory.getDeliverTaskListDao(context);
		// TODO 查出所有的频次
		ArrayList<String> frequences = new ArrayList<String>();
		frequences.add("0");
		frequences.add("1");
		frequences.add("2");
		frequences.add("3");
		frequences.add("4");
		frequences.add("5");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < frequences.size(); i++) {
			// 拼出的邮件个数格式为：10-1，2-2，4-3 三个频次下的邮件个数
			String frequence = frequences.get(i);
			int mailCount = deliverDao.queryByDealResult(dealResult, frequence)
					.size();
			if (i == frequences.size() - 1) {
				sb.append(mailCount + "-" + frequence);
			} else {
				sb.append(mailCount + "-" + frequence + ",");
			}
		}
		return sb.toString();
	}

	private String getMailCountLocalMail(Context context) {
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		DeliverDao deliverDao = daoFactory.getDeliverDao(context);
		DeliverTaskListDao taskDao = daoFactory.getDeliverTaskListDao(context);
		// TODO 查出所有的频次
		ArrayList<String> frequences = new ArrayList<String>();
		frequences.add("0");
		frequences.add("1");
		frequences.add("2");
		frequences.add("3");
		frequences.add("4");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < frequences.size(); i++) {
			String frequence = frequences.get(i);
			int mailCount = deliverDao.queryLocalNotTuoTou(context, frequence)
					.size();
			if (i == frequences.size() - 1) {
				sb.append(mailCount + "-" + frequence);
			} else {
				sb.append(mailCount + "-" + frequence + ",");
			}
		}
		return sb.toString();
	}

	private String getPaymentMailCount(Context context, int flag) {
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		DeliverDao deliverDao = daoFactory.getDeliverDao(context);
		DeliverTaskListDao taskDao = daoFactory.getDeliverTaskListDao(context);
		// TODO 查出所有的频次
		ArrayList<String> frequences = new ArrayList<String>();
		frequences.add("0");
		frequences.add("1");
		frequences.add("2");
		frequences.add("3");
		frequences.add("4");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < frequences.size(); i++) {
			String frequence = frequences.get(i);
			int mailCount = deliverDao.queryPaymentMails(flag, frequence)
					.size();
			if (i == frequences.size() - 1) {
				sb.append(mailCount + "-" + frequence);
			} else {
				sb.append(mailCount + "-" + frequence + ",");
			}
		}
		return sb.toString();

	}

	private String getUncommitMailCount(Context context) {
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		DeliverDao deliverDao = daoFactory.getDeliverDao(context);
		DeliverTaskListDao taskDao = daoFactory.getDeliverTaskListDao(context);
		// TODO 查出所有的频次
		ArrayList<String> frequences = new ArrayList<String>();
		frequences.add("0");
		frequences.add("1");
		frequences.add("2");
		frequences.add("3");
		frequences.add("4");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < frequences.size(); i++) {
			String frequence = frequences.get(i);
			int mailCount = deliverDao.queryUncommitMail(context, frequence)
					.size();
			if (i == frequences.size() - 1) {
				sb.append(mailCount + "-" + frequence);
			} else {
				sb.append(mailCount + "-" + frequence + ",");
			}
		}
		return sb.toString();
	}

	private String getOtherGroupMailCount(String group_content, Context context) {
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		DeliverDao deliverDao = daoFactory.getDeliverDao(context);
		DeliverTaskListDao taskDao = daoFactory.getDeliverTaskListDao(context);
		// TODO 查出所有的频次
		ArrayList<String> frequences = new ArrayList<String>();
		frequences.add("0");
		frequences.add("1");
		frequences.add("2");
		frequences.add("3");
		frequences.add("4");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < frequences.size(); i++) {
			String frequence = frequences.get(i);
			int mailCount = deliverDao.queryForGroup(group_content, frequence,
					context).size();
			if (i == frequences.size() - 1) {
				sb.append(mailCount + "-" + frequence);
			} else {
				sb.append(mailCount + "-" + frequence + ",");
			}
		}
		return sb.toString();
	}

	/**
	 * 更新其他组的mail_count
	 */
	public synchronized void updateOtherGroups(Context context) {
		SQLiteDatabase db = getWritableDatabase();
		ArrayList<String> alllGroupName = queryByGroupType(Constant.OTHERGROUP);
		for (int i = 0; i < alllGroupName.size(); i++) {
			String groupName = alllGroupName.get(i);
			String sql = "update " + GROUP_TABLE + " set mail_count='"
					+ getOtherGroupMailCount(groupName, context)
					+ "' where group_content='" + groupName + "'";
			// String sql = "update " + GROUP_TABLE
			// + " set mail_count=(select count(*) from " + DELIVER_TABLE
			// + " where rcver_street_addr like '%" + groupName
			// + "%' and in_self_group=" + Constant.NOT_INGROUP
			// + ") where group_content='" + groupName + "'";
			// Log.e("更新其他组-" + groupName, sql);
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * 更新收费邮件两个分组的邮件个数
	 * 
	 * @param context
	 */
	public synchronized void updatePaymentGroup(Context context) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + GROUP_TABLE + " set mail_count='"
				+ getPaymentMailCount(context, 1) + "' where group_type="
				+ Constant.DAISHOUGROUP;
		String sql2 = "update " + GROUP_TABLE + " set mail_count='"
				+ getPaymentMailCount(context, 2) + "' where group_type="
				+ Constant.SHOUJIANRENPAY;
		// Log.e("更新收费邮件两个分组-到付", sql);
		// Log.e("更新收费邮件两个分组-收件人付款", sql2);
		db.execSQL(sql);
		db.execSQL(sql2);
		db.close();
	}

	/**
	 * 根据groupType查询分组名
	 * 
	 * @param groupType
	 * @return
	 */
	public ArrayList<String> queryByGroupType(int groupType) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select group_content from " + GROUP_TABLE
				+ " where group_type=" + groupType;
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("group_content")));
		}
		return list;
	}

	/**
	 * 更新分组下的邮件数量,只根据邮件处理结果更新，不更新服务器返回的分组
	 */
	public synchronized void updateDealMailCount(Context context) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + GROUP_TABLE + " set mail_count='"
				+ getMailCountOfDealResult(context, Constant.DAICHULI)
				+ "' where group_type=" + Constant.DAICHULI;
		String sql2 = "update " + GROUP_TABLE + " set mail_count='"
				+ getMailCountOfDealResult(context, Constant.TUOTOU)
				+ "' where group_type=" + Constant.TUOTOU;
		String sql3 = "update " + GROUP_TABLE + " set mail_count='"
				+ getMailCountOfDealResult(context, Constant.WEITUOTOU)
				+ "' where group_type=" + Constant.WEITUOTOU;
		// String sql = "update " + GROUP_TABLE
		// + " set mail_count=(select count(1) from " + DELIVER_TABLE
		// + " where dealResult=group_type" + " and in_self_group="
		// + Constant.NOT_INGROUP + " group by dealResult)"
		// + " where group_type not in (" + Constant.LOCALMAIL + ","
		// + Constant.OTHERGROUP + ")";
		db.execSQL(sql);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.close();
	}

	/**
	 * 更新未上传分组下的邮件个数
	 */
	public synchronized void updateUncommitMailCount(Context context) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + GROUP_TABLE + " set mail_count='"
				+ getUncommitMailCount(context) + "' where group_type="
				+ Constant.UNCOMMIT;
		// Log.e("更新未上传分组", sql);
		// String sql = "update " + GROUP_TABLE + " set mail_count=" + "("
		// + "select distinct count(*) from " + DELIVER_TABLE
		// + " dt left join " + QUEUE_TABLE
		// + " qt on dt.mail_num=qt.mail_num where qt.commit_result="
		// + Constant.UNCOMMIT + ") where group_type=" + Constant.UNCOMMIT;
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 更新附近分组的邮件数量
	 */
	public synchronized void updateLocalMailCount(Context context) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + GROUP_TABLE + " set mail_count='"
				+ getMailCountLocalMail(context) + "' where group_type="
				+ Constant.LOCALMAIL;
		// Log.e("更新附近分组", sql);
		// String sql = "update " + GROUP_TABLE + " set mail_count=" +
		// mail_count
		// + " where group_type=" + Constant.LOCALMAIL;
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 插入一条数据到分组信息表中
	 */
	public synchronized void insertGroupInfo(String group_content, String sid,
			String group_type, String create_by, String create_time,
			String update_by, String update_time, String latlng,
			Integer distance) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("group_content", group_content);
		values.put("sid", sid);
		values.put("group_type", group_type);
		values.put("create_by", create_by);
		values.put("create_time", create_time);
		values.put("update_by", update_by);
		values.put("update_time", update_time);
		values.put("latlng", latlng);
		values.put("distance", distance);
		database.insert(GROUP_TABLE, null, values);
		database.close();
	}

	/**
	 * 将group表数据转化为list
	 */
	public List<GroupBean> queryGroupall(String frequence) {
		SQLiteDatabase database = getReadableDatabase();
		List<GroupBean> list = new ArrayList<GroupBean>();
		Cursor cursor = database.query(GROUP_TABLE, null, null, null, null,
				null, null);
		try {
			list = parseGroupCursorToList(cursor, frequence);
		} catch (Exception e) {
		}
		return list;
	}

	/**
	 * 删除分组
	 * */
	public synchronized int deleteGroupBySid(String sid) {
		SQLiteDatabase db = getWritableDatabase();
		int a = 0;
		if (sid != null) {
			a = db.delete(GROUP_TABLE, "sid=?", new String[] { sid });
		}
		db.close();
		return a;
	}

	/**
	 * 根据分组名字查询sid
	 */
	public String findSidBy(String name) {
		String sid = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		try {
			String sql = "select * from " + GROUP_TABLE
					+ " where group_content= ?";
			Cursor c = db.rawQuery(sql, new String[] { name });
			c.moveToFirst();
			sid = c.getString(c.getColumnIndex("sid"));
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return sid;
	}

	/**
	 * ======= 查询GroupTable表数据个数
	 */
	public int queryGroupCount() {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(GROUP_TABLE, null, null, null, null,
				null, null);
		int count = cursor.getCount();
		closeCursor(cursor);
		return count;
	}

	/**
	 * 查询所有的分组名
	 */
	public ArrayList<String> queryKeys() {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(GROUP_TABLE, null, null, null, null,
				null, null);
		ArrayList<String> list = parseGroupCursorToshuzu(cursor);
		return list;
	}

	/**
	 * 根据分组名查询邮件个数
	 * 
	 * @param groupName
	 * @return
	 */
	public String queryMailCountByName(String groupName, String frequence) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select mail_count from " + GROUP_TABLE
				+ " where group_content='" + groupName + "'";
		Cursor cursor = database.rawQuery(sql, null);
		String count = "0";
		while (cursor.moveToNext()) {
			count = cursor.getString(cursor.getColumnIndex("mail_count"));
			cursor.close();
			return spiltMailCount(count, frequence);
		}
		cursor.close();
		return count;
	}

	/**
	 * 根据分组名查询邮件个数不分频次
	 * 
	 * @param groupName
	 * @return
	 */
	public String queryMailCountByNameAll(String groupName) {
		SQLiteDatabase database = getReadableDatabase();
		String sql = "select mail_count from " + GROUP_TABLE
				+ " where group_content='" + groupName + "'";
		Cursor cursor = database.rawQuery(sql, null);
		String count = "0";
		int iconut = 0;
		while (cursor.moveToNext()) {
			count = cursor.getString(cursor.getColumnIndex("mail_count"));
			Log.e("count-----", count);
			String[] spilt = count.split(",");
			for (int i = 0; i < spilt.length; i++) {
				String mailCountInfo = spilt[i];
				String[] countFrequence = mailCountInfo.split("-");
				try {
					iconut += Integer.parseInt(countFrequence[0]);
				} catch (Exception e) {
				}
			}
			Log.e("iconut-----", iconut + "");
			cursor.close();
		}
		cursor.close();
		return iconut + "";
	}

	/**
	 * 切割mail_count字段，根据频次获取到该频次下的邮件个数
	 * 
	 * @param mail_count
	 *            表中的对应字段
	 * @param frequence
	 *            要获取的频次号
	 * @return 返回对应的邮件条数，否则返回0
	 */
	private String spiltMailCount(String mail_count, String frequence) {
		String[] spilt = mail_count.split(",");
		for (int i = 0; i < spilt.length; i++) {
			String mailCountInfo = spilt[i];
			String[] countFrequence = mailCountInfo.split("-");
			try {
				if (frequence.equals(countFrequence[1])) {
					return countFrequence[0];
				}
			} catch (Exception e) {
			}

		}
		return "0";
	}

	private String spiltMailCountAll(String mail_count) {
		String[] spilt = mail_count.split(",");
		int icount = 0;
		for (int i = 0; i < spilt.length; i++) {
			String mailCountInfo = spilt[i];
			String[] countFrequence = mailCountInfo.split("-");
			try {
				icount += Integer.parseInt(countFrequence[0]);
			} catch (Exception e) {
			}

		}
		return String.valueOf(icount);
	}

	/**
	 * 根据距离得到升序的分组名字
	 */
	public List<GroupBean> queryKeysByDiatance(String frequence) {

		SQLiteDatabase database = getReadableDatabase();
		List<GroupBean> list = new ArrayList<GroupBean>();
		String sql = "select * from " + GROUP_TABLE;
		Cursor cursor = database.rawQuery(sql, null);
		list = parseGroupCursorToList(cursor, frequence);
		return list;
	}

	/**
	 * 根据距离得到升序的分组名字
	 */
	public List<GroupBean> queryGroupList(String frequence) {
		SQLiteDatabase database = getReadableDatabase();
		List<GroupBean> list = new ArrayList<GroupBean>();
		String sql = "select * from " + GROUP_TABLE;
		Cursor cursor = database.rawQuery(sql, null);
		list = parseGroupCursorToList(cursor, frequence);
		return list;
	}

	/**
	 * 根据分组名字查询经纬度
	 */
	public String findlatlng(String name) {
		String lat = null;
		String lng = null;
		String latlng = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		try {
			String sql = "select * from " + GROUP_TABLE
					+ " where group_content='" + name + "'";
			Cursor c = db.rawQuery(sql, null);
			c.moveToNext();
			lat = c.getString(c.getColumnIndex("lat"));
			lng = c.getString(c.getColumnIndex("lng"));
			latlng = lat + "," + lng;
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return latlng;
	}

	/**
	 * 根据分组名字查询距离
	 */
	public String findDistance(String name) {
		String distance = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		try {
			String sql = "select * from " + GROUP_TABLE
					+ " where group_content='" + name + "'";
			Cursor c = db.rawQuery(sql, null);
			c.moveToNext();
			distance = c.getString(c.getColumnIndex("distance"));
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return distance;
	}

	/**
	 * 根据分组名修改distance
	 */
	public synchronized void updateDistance(String group_content,
			Integer distance) {
		SQLiteDatabase database = getWritableDatabase();
		String sql = "update " + GROUP_TABLE + " set distance= " + distance
				+ " where group_content= '" + group_content + "'";
		database.execSQL(sql);
	}

	/**
	 * 将group表数据转化为list
	 */
	public ArrayList<String> parseGroupCursorToshuzu(Cursor cursor) {
		ArrayList<String> keys = new ArrayList<String>();
		while (cursor.moveToNext()) {
			if (cursor != null && cursor.getCount() != 0) {
				keys.add(cursor.getString(cursor
						.getColumnIndex("group_content")));
			}
		}
		closeCursor(cursor);
		return keys;
	}

	/**
	 * 将group表数据转化为list
	 */
	public ArrayList<GroupBean> parseGroupCursorToList(Cursor cursor,
			String frequence) {
		ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
		while (cursor.moveToNext()) {
			GroupBean groupBean = new GroupBean();
			if (cursor != null && cursor.getCount() != 0) {
				groupBean.setGroup_content(cursor.getString(cursor
						.getColumnIndex("group_content")));
				groupBean.setCreate_by(cursor.getString(cursor
						.getColumnIndex("create_by")));
				groupBean.setCreate_time(cursor.getString(cursor
						.getColumnIndex("create_time")));
				groupBean.setGroup_type(cursor.getString(cursor
						.getColumnIndex("group_type")));
				groupBean
						.setSid(cursor.getString(cursor.getColumnIndex("sid")));
				groupBean.setUpdate_by(cursor.getString(cursor
						.getColumnIndex("update_by")));
				groupBean.setUpdate_time(cursor.getString(cursor
						.getColumnIndex("update_time")));
				groupBean.setLatlng(cursor.getString(cursor
						.getColumnIndex("latlng")));
				groupBean.setDistance(cursor.getString(cursor
						.getColumnIndex("distance")));
				String mail_count = "";
				if (!frequence.equals("")) {
					mail_count = spiltMailCount(cursor.getString(cursor
							.getColumnIndex("mail_count")), frequence);
				} else {
					mail_count = spiltMailCountAll(cursor.getString(cursor
							.getColumnIndex("mail_count")));
				}
				if (Utils.stringEmpty(mail_count)) {
					groupBean.setMailCount("0");
				} else {
					groupBean.setMailCount(mail_count);
				}
				groupList.add(groupBean);
			}
		}
		closeCursor(cursor);
		return groupList;
	}

	/**
	 * 清楚分组表内容
	 */
	public synchronized void deleteGroup() {
		SQLiteDatabase database = getWritableDatabase();
		database.delete(GROUP_TABLE, null, null);
		database.close();
	}
}

//package com.newcdc.db;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.newcdc.db.helper.NewUserDaoHelper;
//import com.newcdc.db.helper.UserDaoHelper;
//import com.newcdc.model.LoginDinfoBean;
//import com.newcdc.model.LoginUinfoBean;
//import com.newcdc.model.UserInfoBean;
//
//public class UserDao extends UserDaoHelper {
//
//	public UserDao(Context context) {
//		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
//	}
//
//	/**
//	 * 存储用户信息
//	 */
//	public synchronized void inertUserInfo(LoginUinfoBean lub,
//			LoginDinfoBean ldb) {
//		SQLiteDatabase db = getWritableDatabase();
//		try {
//			db.execSQL(
//					"insert into "
//							+ USER_TABLE
//							+ "(delvorgcode,username,usid,udlvsectionid,dlvflag,"
//							+ "realname,mobile,loginofficeid,change_time,group_sid,pdacode,"
//							+ "dlvsectioncode,dlvsectionid,actionuser,dlvsectionname,dsid,postcode) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
//					new Object[] { lub.getDelvorgcode(), lub.getUsername(),
//							lub.getSid(), lub.getDlvsectionid(),
//							lub.getDlvflag(), lub.getRealname(),
//							lub.getMobile(), ldb.getLoginofficeid(),
//							ldb.getChange_time(), ldb.getGroup_sid(),
//							ldb.getPdacode(), lub.getDlvsectioncode(),
//							ldb.getDlvsectionid(), ldb.getActionuser(),
//							ldb.getDlvsectionname(), ldb.getSid(),
//							lub.getPostcode() });
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.close();
//		}
//	}
//
//	public void updateOrgcode() {
//		SQLiteDatabase db = getWritableDatabase();
//		String sql = "update " + USER_TABLE + " set delvorgcode='77777777'";
//		db.execSQL(sql);
//		db.close();
//	}
//
//	/**
//	 * 读取用户信息
//	 * 
//	 */
//	public UserInfoBean getUserInfo() {
//		SQLiteDatabase database = getReadableDatabase();
//		UserInfoBean uib = new UserInfoBean();
//		LoginUinfoBean lub = new LoginUinfoBean();
//		LoginDinfoBean ldb = new LoginDinfoBean();
//		try {
//			String sql = "select * from " + USER_TABLE;
//			Cursor cursor = database.rawQuery(sql, null);
//			if (cursor.moveToFirst()) {
//				lub.setDelvorgcode(cursor.getString(cursor
//						.getColumnIndex("delvorgcode")));
//				lub.setUsername(cursor.getString(cursor
//						.getColumnIndex("username")));
//				lub.setSid(cursor.getString(cursor.getColumnIndex("usid")));
//				lub.setDlvsectionid(cursor.getString(cursor
//						.getColumnIndex("udlvsectionid")));
//				lub.setDlvflag(cursor.getString(cursor
//						.getColumnIndex("dlvflag")));
//				lub.setRealname(cursor.getString(cursor
//						.getColumnIndex("realname")));
//				lub.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
//				ldb.setLoginofficeid(cursor.getString(cursor
//						.getColumnIndex("loginofficeid")));
//				ldb.setChange_time(cursor.getString(cursor
//						.getColumnIndex("change_time")));
//				ldb.setGroup_sid(cursor.getString(cursor
//						.getColumnIndex("group_sid")));
//				ldb.setPdacode(cursor.getString(cursor
//						.getColumnIndex("pdacode")));
//				ldb.setDlvsectioncode(cursor.getString(cursor
//						.getColumnIndex("dlvsectioncode")));
//				ldb.setDlvsectionid(cursor.getString(cursor
//						.getColumnIndex("dlvsectionid")));
//				ldb.setActionuser(cursor.getString(cursor
//						.getColumnIndex("actionuser")));
//				ldb.setDlvsectionname(cursor.getString(cursor
//						.getColumnIndex("dlvsectionname")));
//				ldb.setSid(cursor.getString(cursor.getColumnIndex("dsid")));
//				lub.setPostcode(cursor.getString(cursor
//						.getColumnIndex("postcode")));
//
//			}
//			uib.setLub(lub);
//			uib.setLdb(ldb);
//			cursor.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return uib;
//	}
//
//	/**
//	 * 更改手机号
//	 */
//	public synchronized void updateMobile(String mobile, String sid) {
//		SQLiteDatabase db = getWritableDatabase();
//		String sql = "update " + USER_TABLE + " set mobile=" + mobile;
//		db.execSQL(sql);
//		db.close();
//	}
//
//	/**
//	 * 删除用户信息
//	 */
//	public synchronized void deleteUserInfo() {
//		SQLiteDatabase db = getWritableDatabase();
//		try {
//			db.delete(USER_TABLE, "'1'=?", new String[] { "1" });
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			db.close();
//		}
//	}
//
//}

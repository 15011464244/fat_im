package com.newcdc.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.newcdc.db.helper.NewUserDaoHelper;
import com.newcdc.db.helper.UserDaoHelper;
import com.newcdc.model.LoginDinfoBean;
import com.newcdc.model.LoginNew_ContentBean;
import com.newcdc.model.LoginUinfoBean;
import com.newcdc.model.UserInfoBean;

public class NewUserDao extends NewUserDaoHelper {

	public NewUserDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	/**
	 * 存储用户信息
	 */

	public synchronized void inertUserInfo(LoginNew_ContentBean lub) {
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.execSQL(
					"insert into "
							+ NEWUSER_TABLE
							+ "(orgcode,usercode,logindate,loginusername,orgname,"
							+ "sectioncode,sectionname,shift,userid,username,usertype) "
							+ "values (?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { lub.getOrgcode(), lub.getUsercode(),
							lub.getLogindate(), lub.getLoginusername(),
							lub.getOrgname(), lub.getSectioncode(),
							lub.getSectionname(), lub.getShift(),
							lub.getUserid(), lub.getUsername(),
							lub.getUsertype() });
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public void updateOrgcode() {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + NEWUSER_TABLE + " set orgcode='77777777'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 读取用户信息
	 * 
	 */
	public LoginNew_ContentBean getUserInfo() {
		SQLiteDatabase database = getReadableDatabase();
		LoginNew_ContentBean content= new LoginNew_ContentBean();
		try {
			String sql = "select * from " + NEWUSER_TABLE;
			Cursor cursor = database.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				content.setOrgcode(cursor.getString(cursor
						.getColumnIndex("orgcode")));
				content.setUsercode(cursor.getString(cursor
						.getColumnIndex("usercode")));
				content.setLogindate(cursor.getString(cursor.getColumnIndex("logindate")));
				content.setLoginusername(cursor.getString(cursor
						.getColumnIndex("loginusername")));
				content.setOrgname(cursor.getString(cursor
						.getColumnIndex("orgname")));
				content.setSectioncode(cursor.getString(cursor
						.getColumnIndex("sectioncode")));
				content.setSectionname(cursor.getString(cursor.getColumnIndex("sectionname")));
				content.setShift(cursor.getString(cursor
						.getColumnIndex("shift")));
				content.setUserid(cursor.getString(cursor
						.getColumnIndex("userid")));
				content.setUsername(cursor.getString(cursor
						.getColumnIndex("username")));
				content.setUsertype(cursor.getString(cursor
						.getColumnIndex("usertype")));
				
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

//	/**
//	 * 更改手机号
//	 */
//	public synchronized void updateMobile(String mobile, String sid) {
//		SQLiteDatabase db = getWritableDatabase();
//		String sql = "update " + NEWUSER_TABLE + " set mobile=" + mobile;
//		db.execSQL(sql);
//		db.close();
//	}

	/**
	 * 删除用户信息
	 */
	public synchronized void deleteUserInfo() {
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.delete(NEWUSER_TABLE, "'1'=?", new String[] { "1" });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

}

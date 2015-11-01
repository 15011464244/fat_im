/**
 * 
 */
package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

/**
 * @author zhangfan 2015-1-26,上午11:19:25
 * 
 */
public class UserDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public UserDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, USER_TABLE)) {
			db.execSQL("create table "
					+ USER_TABLE
					+ "(_id integer primary key autoincrement,"
					+ "delvorgcode text," // 机构号
					+ "username text,"// 工号
					+ "usid text,"
					+ "udlvsectionid text"
					+ ",dlvflag text"
					+ ",realname text"// 真实姓名
					+ ",mobile text"// 手机号
					+ ",loginofficeid text"
					+ ",change_time text"
					+ ",group_sid text"
					+ ",pdacode text" // 交班机构号
					+ ",dlvsectioncode text" + ",dlvsectionid text"
					+ ",actionuser text"
					+ ",dlvsectionname text,dsid text,postcode text)");
		}
	}
}

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
 * @author hanrong 2015-6-10,上午11:19:25
 * 
 */
public class NewUserDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public NewUserDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, NEWUSER_TABLE)) {
			db.execSQL("create table " + NEWUSER_TABLE
					+ "(_id integer primary key autoincrement"
					+ ",logindate text" // 登录时间
					+ ",loginusername text"// 员工登录名
					+ ",orgcode text"// 机构号
					+ ",orgname text"// 机构名称
					+ ",sectioncode text"// 道段代码
					+ ",sectionname text"// 道段名称
					+ ",shift text"// 班次
					+ ",usercode text"// 员工代码
					+ ",userid text"// 员工ID
					+ ",username text"// 员工姓名
					+ ",usertype text"// 员工类型
					+ ")");
		}
	}
}

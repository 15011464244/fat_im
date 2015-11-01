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
 * @author zhangfan 2015-1-26,上午11:19:07
 * 
 */
public class PushDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public PushDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, PUSHTABLE)) {
			db.execSQL("create table " + PUSHTABLE
					+ "(_id integer primary key autoincrement,"
					+ "messageType integer," + // 透传消息类型：4-下段信息
					"the_class_date text)"); // 下段信息中时间最靠后的邮件时间
		}
	}
}

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
 * @author zhangfan 2015-1-26,上午11:18:42
 * 
 */
public class AddressDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public AddressDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, ADDRESSBOOKTAB)) {
			db.execSQL("create table "
					+ ADDRESSBOOKTAB
					+ "(_id integer primary key autoincrement,mainid text,telephonenum text,bodyname text,username text,orgcode text,address text)");
		}
	}
}

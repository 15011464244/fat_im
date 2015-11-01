package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

/**
 * @author liunannan
 * 
 */
public class ExtraCastDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public ExtraCastDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TB_EXTRACAST)) {
			db.execSQL("create table " + TB_EXTRACAST
					+ "(_id integer primary key autoincrement,"
					+ "flmc text,"			//名称
					+ "money text,"
					+ "delvorgcode text,"	//用户的机构号
					+ "username text,"	//用户工号
					+ "fldh text)");// 代号
		}
	}

}

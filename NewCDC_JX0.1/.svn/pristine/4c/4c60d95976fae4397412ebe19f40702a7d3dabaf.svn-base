package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

public class CustomerDaoHelper extends BaseSQLiteOpenHelper implements
DeliverDbConstants{

	public CustomerDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onCreate(db);
		if (!tabIsExist(db, TB_CUSTOMER)) {
			db.execSQL("create table "
					+ TB_CUSTOMER
					+ " (KHDM VARCHAR(30),KHCDM VARCHAR(30),KHMC VARCHAR(30),KHJC VARCHAR(30),KHJM VARCHAR(30),PIJM VARCHAR(30))");
		}
	}
	
}

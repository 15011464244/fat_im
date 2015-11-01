package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

public class TransportTypeDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public TransportTypeDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
//		if (!tabIsExist(db, TB_TRANSPORT)) {
//			db.execSQL("create table "
//					+ TB_TRANSPORT
//					+ "(TransferCode VARCHAR(30),TransferName VARCHAR(30),OldMailType VARCHAR(30),ChangeMailType VARCHAR(30))");
//		}
	}
}

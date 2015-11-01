package com.cn.cdc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.newcdc.db.DeliverDbConstants;

public class TransferDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	protected static final String TABLE_NAME = "tb_transfer";

	protected static final String CREATESQL = "create table tb_transfer (key varchar(100),dlvNum varchar(30)  ,ticketNum  varchar(30) ,custNum  varchar(30) ,projId varchar(30), projName varchar(30),projCd varchar(30),rcverCntct varchar(30),rcverCompany varchar(70),orgcode  varchar(20));";

	public TransferDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL(CREATESQL);
		}

	}
}

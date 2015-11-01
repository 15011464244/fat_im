package com.cn.cdc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.newcdc.db.DeliverDbConstants;

public class MailHandDetailHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	protected static final String TABLE_NAME = "tb_mail_hand_detail";

	protected static final String CREATESQL = "create table tb_mail_hand_detail(mail_num varcahr(20),"
			+ "mail_type char(1),principal varcahr(20),principal_type char(1),"
			+ "abnormity_time varcahr(30),create_time varcahr(30),upload_time varcahr(30),"
			+ "sid long,is_out cahr(1),out_time varcahr(30),"
			+ "code2d_num number,paper_num number,operatorType char(1),oldSid long, signatureImg clob,orgcode  varchar(20));";

	public MailHandDetailHelper(Context context, String name,
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

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}

}

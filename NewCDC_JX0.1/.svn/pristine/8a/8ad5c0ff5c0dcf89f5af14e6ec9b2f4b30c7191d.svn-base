package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

public class MailTypeDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public MailTypeDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		if (!tabIsExist(db, TB_TYPE)) {
			db.execSQL("create table "
					+ TB_TYPE
					+ "(OrgID VARCHAR(30),ClassCode VARCHAR(30),StartDate VARCHAR(30),ClassName VARCHAR(30),EndDate VARCHAR(30),Sort VARCHAR(30),SJLimitTime VARCHAR(30),SJLimitWeight VARCHAR(30),MailType VARCHAR(30),Temp1 VARCHAR(30))");
		}
	}
}

package com.cn.cdc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import com.newcdc.db.DeliverDbConstants;

/**
 * 江西版 基础数据 更新于2015.6.24
 * */

public class BaseDataDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	// "dataKey ":"",
	// "dataValue ":
	// "dataFlags":"", //所属类型 分为五种
	// NEXT_ACTN_CODE 下一步动作
	// MAIL_NOTE_CODE 邮件备注
	// DLV_STS_CODE 投递备注
	// TURN_BACK_CAUSE_CODE 转退原因
	// UNDLV_CAUSE_CODE 未妥投原因

	protected static final String TABLE_NAME = "BASE_TABLE";

	protected static final String CREATESQL = "create table BASE_TABLE (  dataKey VARCHAR2(30) , dataValue   VARCHAR2(90), dataFlags   VARCHAR2(90));";

	public BaseDataDaoHelper(Context context, String name,
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

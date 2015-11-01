package com.cn.cdc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.newcdc.db.DeliverDbConstants;

public class LocCountyNanJingHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	protected static final String TABLE_NAME = "LOC_COUNTY_NANJING";

	protected static final String CREATESQL = "create table LOC_COUNTY_NANJING( COUNTY_CODE varchar(10),CN_NAME varchar(20),EN_NAME varchar(20),PROV_CODE varchar(2),CITY_CODE varchar(8),POSTCODE varchar(10),EXPORTJOINHUB char(1),IMPORTJOINHUB char(1),ORGCODE  varchar(20));";

	public LocCountyNanJingHelper(Context context, String name,
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

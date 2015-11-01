package com.cn.cdc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.newcdc.db.DeliverDbConstants;

public class ResOrgDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	protected static final String TABLE_NAME = "RES_ORG";

	// "org_code":"36300042", //�����
	// "org_sname":"���ݵ�ϼ�ٵ�", //�����
	// "prov_code":"35", //ʡ�ݴ���
	// "prov_name":"����", //ʡ�����
	// "city_code":"350600", //���д���
	// "city_name":"������", //�������
	// "county_code":"350602", //���ش���
	// "county_name":"ܼ����", //�������
	// "postcode":"363007", //�ʱ�
	// "orgTraditionalName":"363007", //�����������
	// "orgEnName":"363007" //��Ӣ�����

	protected static final String CREATESQL = "create table RES_ORG ( org_code VARCHAR2(8), org_sname   VARCHAR2(100), prov_code   VARCHAR2(2),"
			+ " prov_name VARCHAR2(20),  city_code VARCHAR2(10),  city_name  VARCHAR2(50),  county_code  VARCHAR2(10),  county_name  VARCHAR2(100),"
			+ " postcode  VARCHAR2(10),  orgTraditionalName  VARCHAR2(60),  orgEnName  VARCHAR2(60),orgcode  VARCHAR2(20));";

	// private AssetManager assetManager = null;

	public ResOrgDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// assetManager = context.getAssets();
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {// TODO Auto-generated method stub
			db.execSQL(CREATESQL);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}

}

package com.cn.cdc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;

import com.newcdc.db.DeliverDbConstants;

public class DlvStateDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	// "pdaCode ":"11", //pda代码
	// "stateCode":"17", //投递状况代码
	// "stateType ":"H", //投递情况代码
	// "stateDescCHS":"邮件错发", //投递状况中文描述
	// "followAction":"F", //未妥投下有一部动作
	// "stateDescENG":"Item wrongly directed", //投递状况英文描述
	// "stateDescTRADITIONAL": //邮件状态繁体描述

	protected static final String TABLE_NAME = "DLV_STATE";

	protected static final String CREATESQL = "create table DLV_STATE (  pdaCode VARCHAR2(3) not null, stateCode   VARCHAR2(2), stateType   VARCHAR2(1),"
			+ " stateDescCHS VARCHAR2(90),  followAction  VARCHAR2(1),  stateDescENG  VARCHAR2(90),  stateDescTRADITIONAL  VARCHAR2(90),orgname  VARCHAR2(20),orgcode  VARCHAR2(20));";

	private AssetManager assetManager = null;

	public DlvStateDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		assetManager = context.getAssets();
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL(CREATESQL);
			readFromAsset("dlv_state.txt", db);
		}

	}

	private void readFromAsset(String fileName, SQLiteDatabase database) {
		String sqlUpdate = null;
		try {
			InputStream in = assetManager.open(fileName);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, "GBK"));
			database.beginTransaction();
			while ((sqlUpdate = bufferedReader.readLine()) != null) {
				if (!TextUtils.isEmpty(sqlUpdate)) {
					database.execSQL(sqlUpdate);
				}
			}
			database.setTransactionSuccessful();
			database.endTransaction();
			bufferedReader.close();
			in.close();
		} catch (Exception e) {
		}

	}

}

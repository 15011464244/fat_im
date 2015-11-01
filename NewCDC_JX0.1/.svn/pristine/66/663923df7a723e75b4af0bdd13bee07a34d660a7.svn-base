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

public class FollowActionDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	// "followAction ":"A", //下一部动作代码
	// "actionDescCHS ": //下一部动作中文描述
	// "actionDescENG":"", //英文
	// "actionDescTRADITIONAL": //繁体描述

	protected static final String TABLE_NAME = "FOLLOW_ACTION";

	protected static final String CREATESQL = "create table FOLLOW_ACTION (  followAction VARCHAR2(1) , actionDescCHS   VARCHAR2(90), actionDescENG   VARCHAR2(90),"
			+ " actionDescTRADITIONAL VARCHAR2(90),orgcode  VARCHAR2(20));";

	private AssetManager assetManager = null;

	public FollowActionDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		assetManager = context.getAssets();
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL(CREATESQL);
			readFromAsset("follow_action.txt", db);
		}

	}

	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	// onCreate(db);
	//
	// }
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

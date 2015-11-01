package com.newcdc.db.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;
import android.util.Log;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

public class QsjGndmDaoHelper extends BaseSQLiteOpenHelper implements
DeliverDbConstants{
	private AssetManager assetManager = null;
	public QsjGndmDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		assetManager = context.getAssets();
		onCreate(getWritableDatabase());
	}
	/*comment on table TB_ZH_DM_GNDM
	  is '国内地名';
	-- Add comments to the columns 
	comment on column TB_ZH_DM_GNDM.DMDM
	  is '地名代码';
	comment on column TB_ZH_DM_GNDM.DMMC
	  is '地名名称';
	comment on column TB_ZH_DM_GNDM.SJDMDM
	  is '上级地名代码';
	comment on column TB_ZH_DM_GNDM.XZQH
	  is '行政区划';
	comment on column TB_ZH_DM_GNDM.DMJB
	  is '地名级别';
	comment on column TB_ZH_DM_GNDM.SFMC
	  is '省份名称';
	comment on column TB_ZH_DM_GNDM.SFJM
	  is '省份简名';
	comment on column TB_ZH_DM_GNDM.SMJC
	  is '省名简称';
	comment on column TB_ZH_DM_GNDM.SFDMDM
	  is '省份地名代码';
	comment on column TB_ZH_DM_GNDM.DSMC
	  is '地市名称';
	comment on column TB_ZH_DM_GNDM.DSJM
	  is '地市简名';
	comment on column TB_ZH_DM_GNDM.DSDMDM
	  is '地市地名代码';
	comment on column TB_ZH_DM_GNDM.XSMC
	  is '县市名称';
	comment on column TB_ZH_DM_GNDM.XSJM
	  is '县市简名';
	comment on column TB_ZH_DM_GNDM.XSDMDM
	  is '县市地名代码';
	comment on column TB_ZH_DM_GNDM.PYDM
	  is '拼音代码';
	comment on column TB_ZH_DM_GNDM.WBDM
	  is '五笔代码';
	comment on column TB_ZH_DM_GNDM.QPM
	  is '全拼码';
	comment on column TB_ZH_DM_GNDM.GATBZ
	  is '港澳台标志';
	comment on column TB_ZH_DM_GNDM.SYZT
	  is '使用状态';
	yzbm 邮政编码 
	xssx  显示顺序*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL("create table " + TABLE_NAME + " (DMDM   VARCHAR2(8) not null,  DMMC   VARCHAR2(40),SJDMDM VARCHAR2(8), XZQH   VARCHAR2(6), DMJB   CHAR(1), SFMC   VARCHAR2(40),SFJM   VARCHAR2(10), SMJC   VARCHAR2(2),SFDMDM VARCHAR2(8),DSMC   VARCHAR2(40),DSJM   VARCHAR2(10),DSDMDM VARCHAR2(8), XSMC   VARCHAR2(40), XSJM   VARCHAR2(10), XSDMDM VARCHAR2(8),  PYDM   VARCHAR2(20), WBDM   VARCHAR2(20), QPM    VARCHAR2(100), GATBZ  CHAR(1), SYZT   CHAR(1),DHQH   VARCHAR2(20), DMBM   VARCHAR2(20),  YZBM   VARCHAR2(8), XSSX   VARCHAR2(8))");
			readFromAsset("qsj_gndm.sql", db);
		}
		
	}
	private void readFromAsset(String fileName, SQLiteDatabase database) {
		String sqlUpdate = null;
		try {
			InputStream in = assetManager.open(fileName);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, "UTF-8"));
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
			Log.e("message", e.getMessage());
		}

	}

}

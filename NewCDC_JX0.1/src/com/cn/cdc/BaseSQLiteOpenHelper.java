package com.cn.cdc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.newcdc.db.helper.DeliverDaoHelper;

public class BaseSQLiteOpenHelper extends SQLiteOpenHelper {

	public BaseSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (tabIsExist(db, DeliverDaoHelper.GROUP_TABLE)) {
			db.execSQL("drop table " + DeliverDaoHelper.GROUP_TABLE);
		}
		if (tabIsExist(db, DeliverDaoHelper.PUSHTABLE)) {
			db.execSQL("drop table " + DeliverDaoHelper.PUSHTABLE);
		}
		if (tabIsExist(db, DeliverDaoHelper.DELIVER_TABLE)) {
			db.execSQL("drop table " + DeliverDaoHelper.DELIVER_TABLE);
		}
		if (tabIsExist(db, DeliverDaoHelper.TB_FASTLAN)) {
			db.execSQL("drop table " + DeliverDaoHelper.TB_FASTLAN);
		}
		if (tabIsExist(db, DeliverDaoHelper.TB_GATHER_MSG)) {
			db.execSQL("drop table " + DeliverDaoHelper.TB_GATHER_MSG);
		}
		if (tabIsExist(db, DeliverDaoHelper.TABLE_NAME)) {
			db.execSQL("drop table " + DeliverDaoHelper.TABLE_NAME);
		}
		if (tabIsExist(db, "clctTab")) {
			db.execSQL("drop table clctTab");
		}
		if (tabIsExist(db, DeliverDaoHelper.QUEUE_TABLE)) {
			db.execSQL("drop table " + DeliverDaoHelper.QUEUE_TABLE);
		}
		if (tabIsExist(db, DeliverDaoHelper.MONEY_TABLE)) {
			db.execSQL("drop table " + DeliverDaoHelper.MONEY_TABLE);
		}
		if (tabIsExist(db, DeliverDaoHelper.CHATMESSAGE_TABLE)) {
			db.execSQL("drop table " + DeliverDaoHelper.CHATMESSAGE_TABLE);
		}
		if (tabIsExist(db, DeliverDaoHelper.DELIVER_MAIL_TABLE)) {
			db.execSQL("drop table " + DeliverDaoHelper.DELIVER_MAIL_TABLE);
		}
		if (tabIsExist(db, DeliverDaoHelper.DELIVTERTASKLIST_TABLE)) {
			db.execSQL("drop table " + DeliverDaoHelper.DELIVTERTASKLIST_TABLE);
		}
		if (tabIsExist(db, DeliverDaoHelper.JXPAYMNET_TABLE)) {
			db.execSQL("drop table " + DeliverDaoHelper.JXPAYMNET_TABLE);
		}
	}

	public boolean tabIsExist(SQLiteDatabase db, String tabName) {
		boolean result = false;
		if (tabName == null) {
			return result;
		}
		Cursor cursor = null;
		String sql = "select count(*) as c from sqlite_master where type='table' and name = '"
				+ tabName.trim() + "'";
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			return result;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}

	public void readFromAsset(String fileName, SQLiteDatabase database,
			AssetManager assetManager) {
		try {
			InputStream in = assetManager.open(fileName);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, "GBK"));
			String sqlUpdate = null;
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

	public String StringFormate(Object str) {
		if (str != null && !"null".equals(str)) {
			return str.toString();
		} else {
			return "";
		}
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

}

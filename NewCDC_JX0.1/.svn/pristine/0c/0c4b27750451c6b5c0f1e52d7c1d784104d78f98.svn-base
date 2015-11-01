package com.address.jifei;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;
import com.address.BaseSQLiteOpenHelper;
import com.address.DatabaseConstants;

/**
 * 计费区间表
 * fee_area_id   number 计费区间ID，主键ID，全表唯一索引
 * send_area_code   varchar2(32) 寄件地区行政区划代码，可以省市
 * send_area_name   varchar2(32) 寄件地区行政区划名称，冗余字段
 * receive_area_code   varchar2(32) 收件地区行政区划代码，可以省市
 * receive_area_name   varchar2(32) 计费区间ID，收件地区行政区划名称，冗余字段
 * product_type  number 业务类型，快递包裹为1，标准快递为2
 * */

public class FeeAreaDaoHelper extends BaseSQLiteOpenHelper implements
		DatabaseConstants {

	protected static final String TABLE_NAME = "tb_fee_area";

	private AssetManager assetManager = null;

	public FeeAreaDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		assetManager = context.getAssets();
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL("create table tb_fee_area "
					+ "(fee_area_id integer primary key autoincrement,"//计费区间ID，主键ID，全表唯一索引
					+ "send_area_code text,"// 寄件地区行政区划代码，可以省市
					+ "send_area_name text,"// 寄件地区行政区划名称，冗余字段
					+ "receive_area_code text,"// 收件地区行政区划代码，可以省市
					+ "receive_area_name text,"// 计费区间ID，收件地区行政区划名称，冗余字段
					+ "product_type integerr)");// 业务类型，快递包裹为1，标准快递为2
			readFromAsset("tb_fee_area.txt", db);
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
		}
	}

}

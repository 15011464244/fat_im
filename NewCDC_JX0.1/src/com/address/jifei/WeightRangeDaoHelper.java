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
 * 重量范围表
 * */

public class WeightRangeDaoHelper extends BaseSQLiteOpenHelper implements
		DatabaseConstants {

	protected static final String TABLE_NAME = "tb_weight_range";

	private AssetManager assetManager = null;

	public WeightRangeDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		assetManager = context.getAssets();
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL("create table tb_weight_range "
					+ "(weight_range_id integer primary key autoincrement,"// 重量范围ID，主键ID，全表唯一索引
					+ "weight_min integerr,"// 单位：克(g),订单的重量大于这个重量
					+ "weight_max integerr,"//单位：克(g),订单的重量小于这个重量
					+ "product_type integerr)");// 业务类型，快递包裹为1，标准快递为2
			readFromAsset("tb_weight_range.txt", db);
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

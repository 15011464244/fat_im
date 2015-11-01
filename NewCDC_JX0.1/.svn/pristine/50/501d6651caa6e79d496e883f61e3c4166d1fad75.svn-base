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
 * 标准资费表
 * */

public class StandardFeeDaoHelper extends BaseSQLiteOpenHelper implements
		DatabaseConstants {

	protected static final String TABLE_NAME = "tb_standard_fee";

	private AssetManager assetManager = null;

	public StandardFeeDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		assetManager = context.getAssets();
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL("create table tb_standard_fee "
					+ "(fee_id integer primary key autoincrement,"// 标准资费表ID，主键ID，唯一索引
					+ "fee_area_id integerr,"// 关联计费区间表主键ID
					+ "weight_range_id integerr,"// 关联重量范围表主键ID
					+ "first_weight integerr,"// 单位：克(g),首重重量,不超过这个重量的订单，费用一致
					+ "first_fee integerr,"// 单位：元，首重费用，对应首重范围内的重量
					+ "follow_weight integerr,"// 单位：克(g),续重计费单位重量，如果是固定价钱，此处为0
					+ "follow_fee integerr)");// 单位：元，续重费用，同上
			readFromAsset("tb_standard_fee.txt", db);
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

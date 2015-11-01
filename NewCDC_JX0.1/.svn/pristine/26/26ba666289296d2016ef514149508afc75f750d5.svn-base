package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

public class FastLanDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public FastLanDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		if (!tabIsExist(db, TB_FASTLAN)) {
			db.execSQL("create table "
					+ TB_FASTLAN
					+ "(_id integer primary key autoincrement,"
					+ "MailNum text,mainmail text,Customer text,RcvArea text,ActualWeight text,"
					+ "ActualTotalFee text,FoodType text,DaoFu text,ShangChuan text,"
					+ "fenlei text,ReturnLiuShui text,yunshu text,sourthcode text,"
					+ "gekoucode text,gekouname text,ReturnMailNum text,pay_type text,"
					+ "actualGoodsFee text,dan_num text,addressCode text,"
					+ "ypdj_flag text," + // 一票多件标示
					"ypdj_quan text," + // 一票多件件数
					"postcode text," + // 寄达地邮编
					"clct_date text," + // 揽收时间
					"clct_time text," + // 揽收时间
					"opreate text," + // 揽收时间
					"sender_name text," + // 名字
					"sender_addr text," + // 地址
					"delvorgcode text," + // 机构号
					"username text," + // 工号
					"sender_contact_phone text)");// 电话);
		}
	}

}

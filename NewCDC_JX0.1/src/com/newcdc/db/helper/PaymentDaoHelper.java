/**
 * 
 */
package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

/**
 * @author gongjie 2015-1-26,上午11:18:42
 * 
 */
public class PaymentDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	
	public PaymentDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		System.out.println("223333333");
		SQLiteDatabase aa = getWritableDatabase();
		System.out.println("~~~~~~~~~~~~~~~~~~"+aa);
		
		onCreate(getWritableDatabase());
	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("2222222222222");
		
		if (!tabIsExist(db, JXPAYMNET_TABLE)) {
			
			String sql = "create table "+ JXPAYMNET_TABLE
					+ "(receiveId integer primary key ,"
					+ "orderNum varchar(128)," // 商品号
					+ "messageStatus varchar(2)," //是否显示小红点
					+ "price varchar(8),"
					+ "messageTime date,"
					+ "usercode varchar(22),"
					+ "orgCode varchar(22),"
					+ "mailStatus varchar(2)," 
					+ "senderPhoneNum varchar(22)," //寄件人手机号
					+ "senderName varchar(128)"//快递帮手的用户名
					+ ")";
			db.execSQL(sql);
		}
		
	}
}

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
 * @author zhangfan 2015-1-26,上午11:19:16
 * 
 */
public class QueueDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public QueueDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, QUEUE_TABLE)) {
			db.execSQL("create table "
					+ QUEUE_TABLE
					+ "(_id integer primary key autoincrement,"
					+ "flag text,mail_num text,dlv_date text,"
					+ "dlv_time text,dlv_bureau_org_code text,"
					+ "sign_sts_code text,"
					+ "actual_goods_fee text,"
					+ "dlv_pseg_code text,dlv_sts_code text"
					+ ",dlv_staff_code text,"
					+ "dlv_staff_name text,undlv_cause_code text"
					+ ",undlv_next_actn_code text,"
					+ "data_clct_mode_code text,signer_name text,"
					+ "wl_term_num text,"
					+ "pay_mode text,"
					+ "data_upload_timestamp text,frequence text"
					+ ",commit_result text,picture text"
					+ ",orgCode text,userName text,realName text,rcver_phone text,sender_phone text)");
		}
	}
}

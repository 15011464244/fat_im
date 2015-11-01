package com.cn.cdc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.newcdc.db.DeliverDbConstants;

/**
 * 下段数据
 * 
 * @author Administrator
 * 
 */
public class DownloadDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	protected static final String TABLE_NAME = "TB_DOWLOAD_MAIL";
	/*
	 * 是否上传、是否核对、 邮件号 、 应收金额、代收标识 (1：代收邮件
	 * 2：收件人付费邮件)、一票多件主单号、一票多件件数、上一次下段操作员工号、姓名、操作时间、一票多件邮件类型标识(区分经济快递、苹果一票多件)
	 * 0：苹果一票多件 1：经济快递一票多件、返单标识 5-电子返单：电子返单邮件，扫描妥投详单。 6-格式返单：格式返单邮件，返回收件人存联。
	 * 7-个性返单：该邮件需要回收单据。 、指定投递时间 （yyyymmddhh24mi） 判断规则：指定投递时间 > 当日日期
	 * 既“预计配送日晚于当天”不允许下段。、带货换货标识 1-是 0-否 、COD支付邮件标识、邮件重量、扫描时间
	 */

	protected static final String CREATESQL = "create table "
			+ TABLE_NAME
			+ "(IS_UPLOAD CHAR(1)  NOT NULL, IS_CHECK CHAR(1) NOT NULL, mailNo VARCHAR2(20)  , "
			+ "actualFee   VARCHAR2(20) , replaceSign  VARCHAR2(1) , manyMainNo  char(20), manyCount char(5),"
			+ "lastUserCode VARCHAR2(20) ,lastUserName  VARCHAR2(20),lastDate  VARCHAR2(20),manySign  VARCHAR2(1),backSign  VARCHAR2(1),"
			+ "appointTime  VARCHAR2(20),changeSign  VARCHAR2(1),codSign  VARCHAR2(1),weight VARCHAR2(5),createTime  VARCHAR2(20),orgcode  VARCHAR2(20))";

	public DownloadDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL(CREATESQL);
		}
	}
}

package com.cn.cdc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.newcdc.db.DeliverDbConstants;

public class MailDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	protected static final String TABLE_NAME = "TB_PDA_APPLE_DLV_MAIL";
	// is_payup 是否缴款上传 0未 1已
	// IS_CHANGE 是否交班
	// CHANGNEXWAY 投递交班方式
	// CHANGESTATION 投递交班揽投站
	// pay_type 支付方式
	// role 1 ?�� 2Ͷ��
	// interFlag ��ʱ�ʾ ���ڹ���� 1��0
	protected static final String CREATESQL = "create table TB_PDA_APPLE_DLV_MAIL(IS_UPLOAD CHAR(1)  NOT NULL,  deviceNumber VARCHAR2(64)  , "
			+ "orgCode    VARCHAR2(20) not null,userCode  VARCHAR2(20) not null,role  char(1),operationMode char(1),"
			+ "mailCode VARCHAR2(20) not null,dlvOrgCode   VARCHAR2(20),dlvOrgName  VARCHAR2(200),dlvOrgPostCode  VARCHAR2(12),dlvStsCode    VARCHAR2(1),"
			+ "signStsCode  VARCHAR2(1),actualGoodsFee  NUMBER(18,2),actualTax   NUMBER(9,2),actualFee   NUMBER(18,2),otherFee  NUMBER(18,2),dlvUserCode  VARCHAR2(20),"
			+ "dlvUserName   VARCHAR2(100),undlvCauseCode VARCHAR2(2),undlvNextModeCode  VARCHAR2(1),IS_CHANGE VARCHAR2(1),is_payup VARCHAR2(1), CHANGNEXWAY VARCHAR2(1), CHANGESTATION  VARCHAR2(64),"
			+ "signerName VARCHAR2(100),interFlag   VARCHAR2(1),pay_type  VARCHAR2(1),actioncode VARCHAR2(1),createDate  varchar(25),operationTime varchar(25),dlvAddress varchar2(300), signatureImg clob,longitude varcahr(30),latitude varcahr(30),province   varchar(100) ,city	varchar(100),county	varchar(100),street	varchar(300),remark varchar(200),creatTime varchar(25))";

	public MailDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL(CREATESQL);
		}

	}

	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	// onCreate(db);
	//
	// }
}

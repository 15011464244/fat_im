package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

/**
 * @author hanrong 2015-2-4,上午9:18:33
 * 
 */
public class MoneyDaoHelper extends BaseSQLiteOpenHelper implements DeliverDbConstants {

	public MoneyDaoHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, MONEY_TABLE)) {
			db.execSQL("create table " + MONEY_TABLE + "(_id integer primary key autoincrement" 
		           + ",user_code text"// 工号
					+ ",org_code text"// 机构号
					+ ",device_num text" // 设备号
					+ ",pay_order_num text" // 缴款单号
					+ ",mail_num text"// 邮件号
					+ ",amount text"// 金额
					+ ",pay_sort text"// 缴款方式
					+ ",payment_mode text"// 支付方式
					+ ",operationtime text" // 操作时间
					+ ",flag text" // 有效标识
					+ ",transmission text"
					+ ",weight text" // 重量
					+ ",arri_code text" // 寄达地代码
					+ ",cust_code text" // 大客户代码
					+ ",mail_operation_time text"// 邮件揽收/投递时间
					+ ",other_payment_type_code text" // 其他缴款分类代码
					+ ",vip_card text" // VIP卡号
					+ ",resv_col1 text" // 附加字段1-5
					+ ",resv_col2 text" 
					+ ",resv_col3 text"
					+ ",resv_col4 text" 
					+ ",resv_col5 text" 
					+ ",operatortype text" // 操作类型
					+ ",way_code text" // 格口代码
					+ ",resv_col10 text" // 频次日期
					+ ",ismoney text" // 0，未缴款 1 已缴款
					+ ",username text" // 客户名称
					+ ",createtime text" // 客户名称
					+ ",isLT text" // 0，投递 ， 1 揽收， 2其他
					+ ",mainNUM text" // 揽收主单号
					+ ",frequency text)");// 频次
		}
	}
}

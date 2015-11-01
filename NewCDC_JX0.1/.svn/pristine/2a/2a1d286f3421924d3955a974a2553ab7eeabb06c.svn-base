package com.newcdc.db.helper;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class JxClctDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public JxClctDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		if (!tabIsExist(db, JXCLCT_TABLE)) {
			db.execSQL("create table " + JXCLCT_TABLE
					+ " (_id integer primary key autoincrement,"
					+ "mailNo text," // 邮件号
					+ "weight text,"// 重量
					+ "receiverName text," // 收件人姓名
					+ "receiverMobile text," // 收件人手机
					+ "receiverAddress text," // 收件人地址
					+ "fee text," // 费用
					+ "senderName text," // 寄件人姓名
					+ "senderMobile text," // 寄件人手机
					+ "senderAddress text," // 寄件人地址
					+ "orgCode text," // 用户机构号
					+ "userCode text," // 用户工号
					+ "userName text," // 用户姓名
					+ "payment text," // 付款方式
					+ "clct_stauts text," // 上传状态：未上传0已上传1
					+ "createtime text," // 创建时间
					+ "sender_latlng text," // 发件人地址经纬度
					+ "distance text," // 与发件人距离
					+ "mailType text," // 邮件类型 0：无派单揽收 1：有派单揽收
					+ "reservenum text," // 派单邮件的任务号、
					+ "companyid text," 
					+ "userSid text," 
					+ "userIntegral text," //使用积分
					+ "sendType text," //下单类型 1及时 2预约
					+ "startTime text," //预约时间 是有类型是2时才会有预约时间
					+ "actFee text," //实收资费
					+ "integral text," 
					+ "serviceType text," // 业务类型
					+ "isPayment text" // 是否缴款：0未缴费，1已缴费
					+ ")");

		}
	}

}

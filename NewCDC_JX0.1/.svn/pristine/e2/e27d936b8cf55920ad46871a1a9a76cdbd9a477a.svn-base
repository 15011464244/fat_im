package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

public class Gather_MsgDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public Gather_MsgDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		if (!tabIsExist(db, TB_GATHER_MSG)) {
			db.execSQL("create table " + TB_GATHER_MSG
					+ " (_id integer primary key autoincrement,"
					+ "phone text," // 发件人手机号
					+ "clientname text,"// 发件人姓名
					+ "address text," // 发件人地址
					+ "senderProvCode text," // 发件人省代码
					+ "senderCityCode text," // 发件人市代码
					+ "senderCountyCode text," // 发件人县代码
					+ "senderCountryCode text," // CN
					+ "senderProv text," // 发件人省
					+ "senderCity text," // 发件人市
					+ "senderCounty text," // 发件人县
					+ "receiverName text," // 收件人名字
					+ "receiverMobile text," // 收件人电话
					+ "receiverProvCode text," // 收件人省代码
					+ "receiverCityCode text," // 收件人市代码
					+ "receiverCountyCode text," // 收件人县代码
					+ "receiverProv text," // 收件人省
					+ "receiverCity text," // 收件人市
					+ "receiverCounty text," // 收件人县
					+ "receiverCountryCode text," // CN
					+ "receiverStreet text," // 收件人地址
					+ "orderWeight text," // 重量
					+ "payment text," // 付款方式
					+ "lssx text," // 揽收时间
					+ "taskAllotTime text," // 分派时间
					+ "cnday text," + "latlng text," // 发件人地址经纬度
					+ "createtime text," // 创建时间
					+ "distance integer," // 与发件人的距离
					+ "reservenum text," // 任务号
//					+ "device_number text," // 设备号
					+ "operationtime text," // 创建时间
					+ "msg_id text," // ID
					+ "msg_typecode text," // 消息种类
					+ "msg_code text," // 消息码
					+ "userValidIntegral text," // 客户可用积分
					+ "Isspeech text," // 是否语音播报 是？1 否？0 用于派单超时提醒
					+ "IsShow text," // 是否加入要显示的个数中 是？1 否？0 用于推送数量显示
					+ "companyid text," // 消息来源
					+ "readtype text,"// 是否阅读:
					+ "stauts text," // 揽收状态：0未揽收，1已揽收
					+ "delvorgcode text," // 用户的机构号
					+ "username text," // 用户工号
					+ "sendType text," // 下单类型 1及时 2预约
					+ "startTime text," // 预约时间 是有类型是2时才会有预约时间
					+ "remind text" // 催单次数
					+ ")");
		}
	}

}

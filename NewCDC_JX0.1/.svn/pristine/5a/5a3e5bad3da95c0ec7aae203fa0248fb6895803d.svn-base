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
 * @author zhangfan 2015-1-26,上午11:18:33
 * 
 */
public class DeliverDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public DeliverDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, DELIVER_TABLE)) {
			db.execSQL("create table " + DELIVER_TABLE
					+ "(_id integer primary key autoincrement,"
					+ "rcver_loc_county text,"// 收件人地区
					+ "rcver_contact_phone1 text,"// 收件人电话
					+ "sender_loc_county text,"// 发件人地区
					+ "sender_street_addr text,"// 发件人街道
					+ "rcver_loc_prov text,"// 收件人所在省
					+ "sender_contact_phone1 text,"// 发件人电话
					+ "rcver_name text,"// 收件人名字
					+ "the_class_date text,"// 创建日期
					+ "rcver_street_addr text," // 收货地址
					+ "sender_name text" // 发件人名字
					+ ",sender_loc_prov text" // 发件人所在省
					+ ",frequence text,"// 频次
					+ "sender_loc_city text,"// 发件人城市
					+ "rcver_loc_city text,"// 收件人城市
					+ "mail_num text,"// 邮件号
					+ "money text,"// 代收货款
					+ "sender_addr text,"// 发件人地址
					+ "rcver_addr text," // 收件人地址
					+ "dlv_pseg_code text,"// 道段
					+ "dlv_date text,"// 定位日期
					+ "dlv_time text,"// 定位时间
					+ "distance integer"// 邮件和用户距离
					+ ",latlng text,"// 收件地址经纬度
					+ "type integer"// 邮件操作状态:constant.oper_call/constant.oper_msg
					+ ",pay_type text" // 支付方式
					+ ",dealResult integer"// 邮件处理结果：constant
					+ ",actualGoodsFee text" // 缴款方式
					+ ",address text" // 寄达地代码
					+ ",weight text"// 重量
					+ ",checked text" // 本条记录是否在妥投队列中,constant
					+ ",call_time integer"// 拨打电话次数
					+ ",msg_time integer"// 发短信次数
					+ ",picture text" // 拍照图片存储地址
					+ ",sendmsg_by_user text" // 用户手动发短信
					+ ",uplode_date text" // 首次上传时间
					+ ",dlv_sts_code text" // 操作方式，妥投I，未妥投H
					+ ",flag text" // 缴款方式 根据flag，1 对应actualGoodsFee 3，2
									// 对应actualGoodsFee 4
					+ ",queue_task_type integer"// 在异步队列中的任务类型
					+ ",in_self_group text" // 是否在自定义分组中
					+ ",notified text" // 是否同通知提醒过
					+ ",groupsid text" + // 关联组sid;
					",sid integer)");// 服务器端主键，用于查询下段
		}
	}
}

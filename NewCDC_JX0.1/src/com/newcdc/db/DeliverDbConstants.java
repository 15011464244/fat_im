/**
 * 
 */
package com.newcdc.db;

/**
 * @author zhangfan 2015-1-26,上午11:20:46
 * 
 */
public interface DeliverDbConstants {
	public static int DELIVER_DBVERSION = 12;
	public static String DELIVER_DBNAME = "EMS";
	// 表名
	public static final String DELIVER_TABLE = "deliverTab";// 下段信息表
	public static final String DELIVER_MAIL_TABLE = "deliverMailTab";// 下段邮件表
	public static final String QUEUE_TABLE = "queueTab";// 投递信息表
	public static final String GROUP_TABLE = "groupTab";// 分组表
	public static final String MAILID_PREFABRICATE = "mailid_prefabricate";// 单号预置表
	public static final String ADDRESSBOOKTAB = "addressbookTab";// 客户管理表
	public static final String JIAOBAN_TABLE = "jiaobanTab";// 交班表
	public static final String GUANXITABLE = "guanxitable";// 关系表
	public static final String PUSHTABLE = "pushTable";// 推送信息存储表
	public static final String CLCT_TABLE = "clctTab";// 揽收表
	public static final String USER_TABLE = "userTab";// 用户表
	public static final String TABLE_NAME = "qsj_gndm";// 寄达地表
	public static final String TB_TRANSPORT = "TB_TRANSPORT";// 创建运输表
	public static final String TB_TYPE = "TB_TYPE";// 分类表
	public static final String TB_CUSTOMER = "TB_CUSTOMER"; // 大客户表
	public static final String TB_FASTLAN = "TB_FASTLAN";// 快速揽收表
	public static final String TB_GATHER_MSG = "TB_GATHER_MSG";// 派揽信息表
	public static final String TB_EXTRACAST = "TB_EXTRACAST";// 额外费用
	public static final String MONEY_TABLE = "money_table";// 下段信息表
	public static final String CHATMESSAGE_TABLE = "chatmessage_table";// 聊天信息表
	public static final String DELIVTERTASKLIST_TABLE = "delivtertasklist_table";// 聊天信息表
	public static final String NEWUSER_TABLE = "newuser_table";// 聊天信息表
	public static final String JXCLCT_TABLE = "jxclct_table";// 江西揽收表
	public static final String JXCLCTPENDING_TABLE = "jxclctpending";// 江西派揽表
	public static final String JXPAYMNET_TABLE = "jxpayment";// 收到的支付的信息表
}

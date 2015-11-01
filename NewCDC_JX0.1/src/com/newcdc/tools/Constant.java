package com.newcdc.tools;

import android.os.Environment;

/**
 * 静态常量
 */
public final class Constant {
	// 用户手动发短信
	public static final int MSG_NEVER_BY_USER = 0;// 无操作
	public static final int MSG_BY_USER = 1;// 用户手动点击发送过短信

	// 邮件操作状态：
	public static final int OPER_NULL = 0;// 无操作
	public static final int OPER_CALL = 1;// 拨过电话
	public static final int OPER_MSG = 2;// 发过短信
	// 邮件处理结果
	public static final int DAICHULI = 1;// 待处理->未做任何处理
	public static final int WEITUOTOU = 2;// 未妥投
	public static final int TUOTOU = 3;// 妥投
	// 揽收处理结果
	public static final int NOCOLLECTION = 0;// 待处理
	public static final int COLLECTION = 1;// 揽收
	public static final int NOUPDATA = 2;// 未上传

	public static final int UNCOMMIT = 0;// 未提交
	public static final int COMMIT = 1;// 提交成功

	public static final int LOCALMAIL = 4;// 附近邮件分组
	public static final int OTHERGROUP = 5;// 服务器返回分组
	public static final int DAISHOUGROUP = 6;// 代收货款
	public static final int SHOUJIANRENPAY = 7;// 收件人付费
	// 邮件是否在分组中
	public static final int INGROUP = 0;// 在自定义分组中
	public static final int NOT_INGROUP = 1;// 不在自定义分组
	// 是否对邮件有过定位提醒
	public static final int NOTIFY = 0;// 提醒过
	public static final int NOT_NOTIFY = 1;// 没提醒过
	// public static final int YUTUOTOU = 5;// 购物车
	// 本条记录是否在妥投队列中：
	public static final int CHECKED_TRUE = 0;// 在妥投队列
	public static final int CHECKED_FALSE = 1;// 不在妥投队列
	// 解析妥投情况代码
	public static final int CODE_REASON = 0;// 未妥投原因
	public static final int CODE_NEXTSTEP = 1;// 下一步动作
	// 分组类型
	public static final int GROUP_BY_USER = 1;// 用户手动添加的分组
	public static final int GROUP_BY_SERVER = 2;// 地标信息分组
	// 透传消息类型
	public static final int PUSH_TYPE_DELIVERTASK = 1;// 透传下段消息
	public static final int PUSH_TYPE_CLCTTASK = 3;// 透传派揽消息
	public static final int PUSH_TYPE_PAYMENT = 7;// 收到的支付信息
	public static final int PUSH_TYPE_USEROPERATE = 8;// 用户操作下端
	public static final int PUSH_TYPE_CLCTTASK2 = 10;// 智能平台派揽消息
	public static final int PUSH_TYPE_CLCTTASK3 = 11;// 智能平台派揽消息撤单
	public static final int PUSH_TYPE_CLCTTASK4 = 12;// 任务重分派
	public static final String SAVE_USER = "saveUser";// 保存用户信息的xml文件
	// 投递反馈标识
	public static final String DLVCODE = "I";// 妥投  		<提交对应TT>
	public static final String UNDLVCODE = "H";// 未妥投	<提交对应CRZT>
	// 下段邮件类型
	public static final int MAILTYPE_NORMAL = 0;// 普通邮件
	public static final int MAILTYPE_DAISHOU = 1;// 代收邮件
	// 下段强制提交类型
	public static final int SUBMITTYPE_FEIQIANGZHI = 0;// 邮件提交方式-非强制
	public static final int SUBMITTYPE_QIANGZHI = 1;// 邮件提交方式-强制
	// 下段结果
	public static final int RESULT_ERR = 0;// 邮件下段结果-失败-必须删除
	public static final int RESULT_QIANGZHI = 1;// 邮件下段结果-可强制上传
	public static final int RESULT_SUCC = 2;// 邮件下段结果-成功
	public static final int RESULT_NORMAL = 3;// 邮件下段结果-未操作
	// 下段查询结果
	public static final int QUERY_DELIVER_ERR = 0;// 下段查询失败
	public static final int QUERY_DELIVER_OK = 1;// 下段查询成功
	// 基础数据下载的一些标识
	public static final String MAILIN = "0";
	public static final String UPLOAD = "1";
	public static final String UNUPLOAD = "0";
	public static final String HEADIMG = ".png";// 头像名字
	public static String SDCARDPATH = Environment.getExternalStorageDirectory()
			+ "/";

	public final static int REPEAT_TIME = 50;
	// 存贮图片的app文件夹名
	public final static String APP_WENJIANJIA = "lantou/images/";
	public final static String SD = Constant.SDCARDPATH
			+ Constant.APP_WENJIANJIA;
	public final static String MYVOICEFORDER = Constant.SDCARDPATH
			+ "lantou/voice/";
	public final static String EXCEPTION_INFORMATION = Constant.SDCARDPATH
			+ "lantou/except/";
	public final static String DELIVER_OK = Constant.SDCARDPATH
			+ "lantou/deliverok/images/";
	public final static String WEATHER = Constant.SDCARDPATH
			+ "lantou/weather/images/";
	// 广播Action
	public final static String ACTION_LOADING = "loadingData";
	public final static String ACTION_LOCATION = "locationAddr";
	public static final String ACTION_NAME = "groupBroadcast";
	public static final String ACTION_DOWN_DATA_OVER = "downDeliverTaskComplete";
	public static final String ACTION_ASYNCQUEUE = "asyncQueuqOnPost";
	public final static String ACTION_GATHER_SC = "gatherSC";
	public final static String ACTION_TITLE_COUNT = "titleCount";
	public final static String ACTION_BLUTTOOTH_MSG = "bluetooth_msg";
	public final static String ACTION_NOTIFY = "deliversuccess";
	public final static String ACTION_DODELIVERSUCCESS = "dodeliversuccess";
	public final static String ACTION_ONREFRESHCOMPLETE = "onRefreshComplete";
	// title标题栏文字
	public static final String TITLE_LOADINGDATA = "正在更新下段数据,请稍等...";
	public static final String TITLE_DATALATEST = "您当前的信息是最新数据哦";
	public static final String TITLE_LOCATION_PRE = "定位中";

}

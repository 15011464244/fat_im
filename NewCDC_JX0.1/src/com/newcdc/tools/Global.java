package com.newcdc.tools;

/**
 * URL工具类
 * 
 */
public class Global {
	// 因涉及接口地址更新增加接口时记得在下面的更新方法中添加接口；
	/**
	 * 外网测试环境
	 * */
	public static String SERVICE1 = "http://111.75.223.93:9008/post-carrier-service/";// 外网测试环境
	public static String SERVICE2 = "http://111.75.223.93:9008/post-carrier-service/";//外网测试环境
	public static String URL = "http://111.75.223.93:9008/post-carrier-service/";//外网测试环境
	/**
	 *外网正式环境
	 * */
//	public static String SERVICE1 = "http://111.75.223.93:9002/post-carrier-service/";// 外网正式环境
//	public static String SERVICE2 = "http://111.75.223.93:9002/post-carrier-service/";//外网正式环境
//	public static String URL = "http://111.75.223.93:9002/post-carrier-service/";//外网正式环境
	/**
	 *内网正式环境
	 * */
//	public static String SERVICE1 = "http://172.16.254.153:9002/post-carrier-service/";// 内网正式环境
//	public static String SERVICE2 = "http://172.16.254.153:9002/post-carrier-service/";//内网正式环境
//	public static String URL = "http://172.16.254.153:9002/post-carrier-service/";//内网正式环境
	/**
	 *内网测试环境
	 * */
//	public static String SERVICE1 = "http://172.16.254.153:9008/post-carrier-service/";// 内网测试环境
//	public static String SERVICE2 = "http://172.16.254.153:9008/post-carrier-service/";//内网测试环境
//	public static String URL = "http://172.16.254.153:9008/post-carrier-service/";//内网测试环境
	
	public static String SERVER_URL = URL + "PhoneAction/";// 投递
	public static String SERVER_URL2 = URL + "PhoneClctAction/"; // 派揽
	public static String SERVER_URL3 = URL + "UtilsAction/"; // 派揽

	// ------------------------------拼接URL-------------- ---------------------//
	// 版本更新l
	public static String CHECKVERSION = SERVER_URL + "findVersion?";
	// 登陆
	public static String THISLOGIN = SERVER_URL + "login?";
	public static String NEWLOGIN = SERVER_URL3 + "login?";
	public static String SELECTAPPMEUN = SERVER_URL + "selectAppMeun?";
	// 退出登陆
	public static String OUTLOGIN = SERVER_URL + "loginOut?";
	public static boolean isLan = true;

	public static String jipaoWeight = "";
	public static String jipaoWeightChild = "";
	// 下段名址
	public static String MINGZHI = SERVER_URL + "findXiaDuanmingzhi?";
	// 查询下段信息
	public static String DELIVER_URL = SERVER_URL + "findTbMailSectionStore?";
	public static String XIADUAN = SERVER_URL3 + "xdUpload";
	// 通话记录
	public static String CALLINFO_URL = SERVER_URL + "addRecord?";
	// 提交经纬度信息
	public static String UPDATE_GPS = SERVER_URL + "uploadEmployeeGpsMessage?";
	// 发送短信
	public static String SENDMESSAGE = SERVER_URL + "sendShortMessage";
	// 上传推送idxdUpload
	public static String UPLOADPUSHINFO = SERVER_URL + "updateDeceiveChanelId?";
	public static String SAVETRANSFERLOG = SERVER_URL + "saveTransferLog";
	// 预妥投
	public static String YUTUOTOU = SERVER_URL + "yuTuoTou";
	public static String CALLREPLENISH_URL = SERVER_URL // 电话补录
			+ "updateRcverContactPhone?";
	public static String PUTMONEY = SERVER_URL + "Payment";// 缴费
	public static String ADDUSERCONTACTS = "addCustomer";// 添加通讯录
	public static String DELUSERCONTACTS = "delCustomer";// 删除
	public static String UPDATEUSERCONTACTS = "updateCustomer";// 修改
	public static String FINDUSERCONTACTS = SERVER_URL + "findCustomer";
	public static String FINDCUSTOMERRELATION = "findCustomerRelation";// 查询关系用户
	public static String UPDATECUSTOMERRELATION = "updateCustomerRelation";// 更新关系用户
	public static String DELCUSTOMERRELATION = "delCustomerRelation";// 删除关系用户
	public static String ADDCUSTOMERRELATION = "addCustomerRelation";// 添加关系用户
	public static String CUSTOMSERVER_URL = SERVER_URL + "findRecord?"; // 客户服务-服务记录
	public static String CUSTOMDEAL_URL = SERVER_URL + "findOrderbyMobile?"; // 客户服务-订单记录
	public static String UPDATEGROUP = SERVER_URL + "updateGroup"; // 更新分组
	public static String DELGROUP = SERVER_URL + "delGroup"; // 删除分组
	public static String ADDGROUP = SERVER_URL + "addGroup"; // 增加分组
	public static String FINDGROUP = SERVER_URL + "findGroup?"; // 查询分组
	public static String DLVMSGRTN = SERVER_URL + "dlvMsgRtn";// 妥投
	public static String SAVETTIMAGE = SERVER_URL + "saveTtImage";// 上传照片
	public static String UPDATEEMPLOYEEIMAGE = SERVER_URL
			+ "updateEmployeeImage";// 设置上传头像
	public static String FINDEMPLOYEEIMAGE = SERVER_URL + "findEmployeeImage?";// 设置上传头像
	public static String EXCEPT_COMMIT = SERVER_URL + "saveException";
	// 订单记录图片
	public static String DEALIMAGE = SERVER_URL + "findTtImage?";
	// 异常处理/确认未妥投
	public static String EXCEPTIONHANDLER = SERVER_URL + "exceptionHandler";
	// --------------------------基础数据下载----------------------------------------//
	public static String BASE_URL = "http://shipping.ems.com.cn:8000/wireless-app-test/";
	// 数据下载
	public static String WIRELESS_DATA_DOWNLOAD = BASE_URL + "DataAction?h=4";
	/**
	 * 全收寄基础数据下载
	 */
	public static String WIRELESS_ALL_DATA_DOWNLOAD = BASE_URL
			+ "CollDataSynchronizationAction2?h=53";

	/**
	 * 天气查询接口
	 */
	public static String TIANQI = "http://api.map.baidu.com/telematics/v3/weather?";

	// 获取格口的数据的路径接口 测试地址
	// public static final String GEKOU =
	// "http://192.168.1.133:8989/message-service/PhoneClctAction/querygekou";
	public static String GEKOU = SERVER_URL2 + "querygekou";

	// 揽收 下载数据 大客户 邮件分类 运输方式
	public static String PDADOWNLOAD = SERVER_URL2 + "PDAdownload?";// 未分页
	public static String PDADOWNLOADN = SERVER_URL2 + "PDAdownloadN?";// 分页
	public static String BASEDATADOWNLOAD = SERVER_URL2 + "baseDataDownload?";// 江西基础数据下载

	public static String SENDDOWNLOAD = SERVER_URL2 + "senddownload?"; // 数据下载
																		// 投递情况
																		// 未妥投
																		// 未妥投原因

	// 揽收发送短信接口地址 测试地址
	// public static final String sendSms =
	// "http://172.27.35.4:8080/message-service/PhoneAction/sendShortMessage";
	public static String sendSms = SERVER_URL + "sendShortMessage";

	// 到付，测试地址
	// public static final String DAOFU_URL =
	// "http://172.21.200.5:8080/message-service/PhoneClctAction/findIsFreightCollect";
	public static String DAOFU_URL = SERVER_URL2 + "findIsFreightCollect";
	public static String LanShouShuJu_URL = SERVER_URL2 + "uploadClctData?"; // 揽收数据上传
	public static String JiaoBan_URL = SERVER_URL2 + "clctjb?"; // 交班数据上传
	public static String LanShouFeedback_URL = SERVER_URL2 + "lanShouFeedback?"; // 揽收反馈数据上传
	public static String GATHERMESSAGE_URL = SERVER_URL2 + "findNotifyMSG?"; // 派揽信息

	public static String backType = "1";
	// 聊天
	public static String CHATURL = "http://218.245.3.24:8081/chatserver/message/";
	// public static String CHATURL =
	// "http://101.200.81.28:8081/chatserver/message/";
	public static String CHATTEXT = CHATURL + "sendText.html";// 文本的
	public static String CHATIMG = CHATURL + "send.html";// 语音图片的

	// 聊天客户信息查询
	public static String FINDCUSTOMERMESSAGE = SERVER_URL
			+ "findCustomerMessage?";
	// 聊天客户头像查询
	public static String FINDUSEREIMAGEBYSID = SERVER_URL
			+ "findUserEImageBySid?";
	public static String SIGNIN_URL = SERVER_URL3 + "signIn";// 签到
	// 服务器状态查询
	public static String NETSTATE_URL = SERVER_URL2 + "connectWork";
	// 智能平台派揽信息
	public static String ZHINENGCLC_URL = SERVER_URL3 + "queryMailOrder?";
	// 智能平台派揽信息确认
	public static String EnSureTask_URL = SERVER_URL3 + "enSureTask?";

	// 下载员工任务查询
	public static String FindTaskByWorker_URL = URL
			+ "UtilsAction/findTaskByWorker?";
	// 修改密码
	public static String PassWord_URL = SERVER_URL3 + "modifyPassword?";

	/**
	 * 跟新URL
	 */
	public static void changUrl() {

		SERVER_URL = URL + "PhoneAction/";// 投递
		SERVER_URL2 = URL + "PhoneClctAction/"; // 派揽
		SERVER_URL3 = URL + "UtilsAction/"; // 派揽

		CHECKVERSION = SERVER_URL + "findVersion?";//版本检测
		THISLOGIN = SERVER_URL + "login?";
		OUTLOGIN = SERVER_URL + "loginOut?";
		isLan = true;

		jipaoWeight = "";
		jipaoWeightChild = "";
		MINGZHI = SERVER_URL + "findXiaDuanmingzhi?";
		DELIVER_URL = SERVER_URL + "findTbMailSectionStore?";
		XIADUAN = SERVER_URL3 + "xdUpload";
		CALLINFO_URL = SERVER_URL + "addRecord?";
		UPDATE_GPS = SERVER_URL + "uploadEmployeeGpsMessage?";
		SENDMESSAGE = SERVER_URL + "sendShortMessage";
		UPLOADPUSHINFO = SERVER_URL + "updateDeceiveChanelId?";
		SAVETRANSFERLOG = SERVER_URL + "saveTransferLog";
		YUTUOTOU = SERVER_URL + "yuTuoTou";
		CALLREPLENISH_URL = SERVER_URL // 电话补录
				+ "updateRcverContactPhone?";
		PUTMONEY = SERVER_URL + "Payment";// 缴费
		ADDUSERCONTACTS = "addCustomer";// 添加通讯录
		DELUSERCONTACTS = "delCustomer";// 删除
		UPDATEUSERCONTACTS = "updateCustomer";// 修改
		FINDUSERCONTACTS = SERVER_URL + "findCustomer";
		FINDCUSTOMERRELATION = "findCustomerRelation";// 查询关系用户
		UPDATECUSTOMERRELATION = "updateCustomerRelation";// 更新关系用户
		DELCUSTOMERRELATION = "delCustomerRelation";// 删除关系用户
		ADDCUSTOMERRELATION = "addCustomerRelation";// 添加关系用户
		CUSTOMSERVER_URL = SERVER_URL + "findRecord?"; // 客户服务-服务记录
		CUSTOMDEAL_URL = SERVER_URL + "findOrderbyMobile?"; // 客户服务-订单记录
		UPDATEGROUP = SERVER_URL + "updateGroup"; // 更新分组
		DELGROUP = SERVER_URL + "delGroup"; // 删除分组
		ADDGROUP = SERVER_URL + "addGroup"; // 增加分组
		FINDGROUP = SERVER_URL + "findGroup?"; // 查询分组
		DLVMSGRTN = SERVER_URL + "dlvMsgRtn";// 妥投
		SAVETTIMAGE = SERVER_URL + "saveTtImage";// 上传照片
		UPDATEEMPLOYEEIMAGE = SERVER_URL + "updateEmployeeImage";// 设置上传头像
		FINDEMPLOYEEIMAGE = SERVER_URL + "findEmployeeImage?";// 设置上传头像
		EXCEPT_COMMIT = SERVER_URL + "saveException";
		DEALIMAGE = SERVER_URL + "findTtImage?";
		EXCEPTIONHANDLER = SERVER_URL + "exceptionHandler";
		BASE_URL = "http://shipping.ems.com.cn:8000/wireless-app-test/";
		WIRELESS_DATA_DOWNLOAD = BASE_URL + "DataAction?h=4";
		WIRELESS_ALL_DATA_DOWNLOAD = BASE_URL
				+ "CollDataSynchronizationAction2?h=53";
		TIANQI = "http://api.map.baidu.com/telematics/v3/weather?";

		GEKOU = SERVER_URL2 + "querygekou";
		PDADOWNLOAD = SERVER_URL2 + "PDAdownload?";// 未分页
		PDADOWNLOADN = SERVER_URL2 + "PDAdownloadN?";// 分页
		SENDDOWNLOAD = SERVER_URL2 + "senddownload?"; // 数据下载
		sendSms = SERVER_URL + "sendShortMessage";

		DAOFU_URL = SERVER_URL2 + "findIsFreightCollect";
		LanShouShuJu_URL = SERVER_URL2 + "uploadClctData?"; // 揽收数据上传
		JiaoBan_URL = SERVER_URL2 + "clctjb?"; // 交班数据上传
		LanShouFeedback_URL = SERVER_URL2 + "lanShouFeedback?"; // 揽收反馈数据上传
		GATHERMESSAGE_URL = SERVER_URL2 + "findNotifyMSG?"; // 派揽信息

		backType = "1";
		CHATURL = "http://218.245.3.24:8081/chatserver/message/";
		CHATTEXT = CHATURL + "sendText.html";// 文本的
		CHATIMG = CHATURL + "send.html";// 语音图片的
		FINDCUSTOMERMESSAGE = SERVER_URL + "findCustomerMessage?";
		FINDUSEREIMAGEBYSID = SERVER_URL + "findUserEImageBySid?";
		SIGNIN_URL = SERVER_URL3 + "signIn";
		NETSTATE_URL = SERVER_URL2 + "connectWork";
		ZHINENGCLC_URL = SERVER_URL3 + "queryMailOrder?";
		EnSureTask_URL = SERVER_URL3 + "enSureTask?";
		FindTaskByWorker_URL = URL + "UtilsAction/findTaskByWorker?";
		PassWord_URL = SERVER_URL3 + "modifyPassword?";
	}
//	查询是否签到
	public static String CHECKSIGN = SERVICE1 +"UtilsAction/queryIsSignIn";
	// 点击收费后的提交
	public static String CHARGE = SERVICE1 + "PhoneClctAction/charge";
	//员工退出 ,为了实现后台知道揽投员什么时候退出的登陆
	public static String LOGOUT = SERVICE1 + "PhoneAction/userLoginOut";

}

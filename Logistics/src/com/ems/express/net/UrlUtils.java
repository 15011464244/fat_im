package com.ems.express.net;

public class UrlUtils {
	/*
	 * 上海
	 */
//	public static String HOST = "http://211.156.193.130:8091/message-service";
	/*
	 * 江西
	 */
//	public static String HOST = "http://111.75.223.93:9002/post-customer-service/";
	//赵杰测试
	public static String HOST = "http://192.168.1.114:8080/post-customer-service/";
//	新的时效查询和收送范围的网址
	public static String NHOST = "http://211.156.193.130:8091/post-customer-service/";
	
	
	public static String URL_ADDRESS = HOST+ "/NetworkAction/findCity";
	public static String URL_EXPRENSE_PRO = HOST + "/UtilsAction/API/costqueryprov";//
	public static String URL_TIME_PRO = HOST +"/UtilsAction/API/timequeryprov";
	public static String URL_RANGE_PRO = HOST + ""; //
	public static String URL_EXPRENSE_CITY = HOST + "/UtilsAction/API/costquerycity";//
	public static String URL_RANGE_CITY = HOST + ""; //
	public static String URL_TIME_CITY = HOST +"/UtilsAction/API/timequerycity";
	
//	public static String URL_SERVICE_RANGE = NHOST + "/UtilsAction/API/rangequery";
	public static String URL_SERVICE_RANGE = "http://211.156.193.130:8091/micro-channel-service/UtilsAction/API/rangequery";
	
	public static String URL_QUERY_TIME = "http://211.156.193.130:8091/message-service/UtilsAction/API/timequery";

	
	
//	public static String URL_QUERY_TIME = NHOST +"/UtilsAction/API/timequery";
	public static String URL_QUERY_SERVICE_POINT = "http://agent.ems.com.cn/ToPoinAction/findZitidian";
	public static String URL_QUERY_CARRIER = HOST +"/EmployeeAction/findEmlpoyee";
	public static String URL_FIND_CITY_ID = HOST + "/NetworkAction/findCityId";
	public static String URL_CARRIER_IMG = HOST + "/EmlpoyeeAction/findEmployeeImage";


}

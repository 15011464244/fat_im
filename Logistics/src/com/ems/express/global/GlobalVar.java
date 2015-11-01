package com.ems.express.global;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局变量  单例
 * @author mingsheng
 *
 */
public class GlobalVar {
	//存储催单和退单的可见状态
	public final static Map<Integer,Integer> urgeVisibility = new HashMap<Integer, Integer>();
	public final static Map<Integer,String> mailMessageStatus = new HashMap<Integer, String>();
	
	
	private static GlobalVar globalVar = new GlobalVar();
	private GlobalVar(){}
	
	public static GlobalVar getInstance(){
		return globalVar;
	}
	
}

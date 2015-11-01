package com.ems.express.bean;

import java.io.Serializable;

import android.text.TextUtils;

public class City implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String[] zhixiashi = {"北京","上海","天津","重庆"};
	public final static int TYPE_PROVINCE = 0;
	public final static int TYPE_CITY = 1;
	public final static int TYPE_COUNTY = 2;
	private int code;
	private String name;
	private int type;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}

	/**
	 * 判断这个市是不是直辖市
	 * @return 是直辖市返回true 否则false
	 * */
	public boolean isZhixiashi(){
		if(TextUtils.isEmpty(name)){
			return false;
		}
		for(String str : zhixiashi){
			if(name.contains(str)){
				return true;
			}
		}
		return false;
	}
}

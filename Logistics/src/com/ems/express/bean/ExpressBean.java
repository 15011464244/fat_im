package com.ems.express.bean;

import java.io.Serializable;

public class ExpressBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 快递公司code
	 */
	private String code;
	/**
	 * 快递公司name
	 */
	private String name;
	/**
	 * 快递公司icon
	 */
	private String icon;

	public ExpressBean() {
		super();
	}

	public ExpressBean(String code, String name, String image) {
		super();
		this.code = code;
		this.name = name;
		this.icon = image;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return icon;
	}

	public void setImage(String image) {
		this.icon = image;
	}

	@Override
	public String toString() {
		return "ExpressBean [code=" + code + ", name=" + name + ", image="
				+ icon + "]";
	}

}

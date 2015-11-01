package com.ems.express.bean;

import java.io.Serializable;

public class ExpressRecordBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 快递公司Code
	 */
	private String code;
	/**
	 * 快递公司名称
	 */
	private String name;
	/**
	 * 查件记录编号
	 */
	private String number;
	/**
	 * 查件记录时间
	 */
	private String time;
	/**
	 * 原地址Code
	 */
	private String sourceCode;
	/**
	 * 原地址名称
	 */
	private String source;
	/**
	 * 目标地址Code
	 */
	private String targetCode;
	/**
	 * 目标地址名称
	 */
	private String target;
	/**
	 * 签收人名称
	 */
	private String who;
	/**
	 * 签收人联系方式
	 */
	private String phone;

	public ExpressRecordBean() {
		super();
	}

	public ExpressRecordBean(String code, String name, String number, String time,
			String sourceCode, String source, String targetCode, String target,
			String who, String phone) {
		super();
		this.code = code;
		this.name = name;
		this.number = number;
		this.time = time;
		this.sourceCode = sourceCode;
		this.source = source;
		this.targetCode = targetCode;
		this.target = target;
		this.who = who;
		this.phone = phone;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "RecordBean [code=" + code + ", name=" + name + ", number="
				+ number + ", sourceCode=" + sourceCode + ", source=" + source
				+ ", targetCode=" + targetCode + ", target=" + target + "]";
	}

}

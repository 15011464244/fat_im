package com.newcdc.model;

import java.io.Serializable;

/**
 * @author hanrong
 * @version 创建时间：2014-12-5 下午3:11:37
 * 类说明	缴款明细页显示的信息Bean
 */
public class MoneyBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mailId;//邮件号
	private String MoneyNum;//资费
	private String pay_type;//支付方式
	private String username;//客户名
	private String isMoney; //是否缴款
	
	public String getIsMoney() {
		return isMoney;
	}
	public void setIsMoney(String isMoney) {
		this.isMoney = isMoney;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getMoneyNum() {
		return MoneyNum;
	}
	public void setMoneyNum(String moneyNum) {
		MoneyNum = moneyNum;
	}
	
}

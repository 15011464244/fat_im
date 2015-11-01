package com.ems.express.bean;

import java.io.Serializable;
import java.util.Date;

public class DeliveryMessageBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int delId;					//主键ID
	private double longitude;			//经度
	private double latitude;			//纬度
	private String mobile;				//电话
	private String channelId;			//推送标示
	private String employeeNo;			//快递员编号
	private String people;				//快递员姓名
	private String code;				//机构号
	private String sid;					//头像ID
	private String waybill;				//运单号
	private String messageStatus;		//消息状态 0已读 1未读
	private String messageTime;			//时间
	private String receiptStatus;		//签收状态0已签收1未签收
	private String mailNum;				//邮寄信息
	private String orgcode; 			//揽投员机构号
	private String username;			//揽投员工号
	private String signStatus;			//签收状态1已签收0未签收
	
	
	
	public String getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	public String getOrgcode() {
		return orgcode;
	}
	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMailNum() {
		return mailNum;
	}
	public void setMailNum(String mailNum) {
		this.mailNum = mailNum;
	}
	public String getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public int getDelId() {
		return delId;
	}
	public void setDelId(int delId) {
		this.delId = delId;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public String getPeople() {
		return people;
	}
	public void setPeople(String people) {
		this.people = people;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getWaybill() {
		return waybill;
	}
	public void setWaybill(String waybill) {
		this.waybill = waybill;
	}
	public String getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}
	public String getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}
	
	
	
}

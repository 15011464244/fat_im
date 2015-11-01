package com.ems.express.adapter.message;

public class OrderMessageInfo {
	private String sid;
	private String messageTime;
	private String orderStatus;
	private String orderStatusDesc;
	private String orgCode;
	private String employeeNo;
	@Override
	public String toString() {
		return "OrderMessageInfo [sid=" + sid + ", messageTime=" + messageTime
				+ ", orderStatus=" + orderStatus + ", orderStatusDesc="
				+ orderStatusDesc + ", orgCode=" + orgCode + ", employeeNo="
				+ employeeNo + "]";
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}
	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	
	

}

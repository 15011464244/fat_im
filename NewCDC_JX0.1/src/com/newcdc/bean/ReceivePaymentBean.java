package com.newcdc.bean;

import java.io.Serializable;

public class ReceivePaymentBean implements Serializable {
	//以下的是支付的相关的字段
		private int receiveId;
		private String orderNum;//订单号
		private String price;//服务端收到的金额
		private String mobNum;//电话号
		private String SenderName;//用户的名字
		
		
		private String mailStatus;      //订单的状态
		private String messageTime; 
		private String messageStatus;       //状态
		private String userCode;      //工号
		private String orgCode;       //机构号
		
		public int getReceiveId() {
			return receiveId;
		}
		public void setReceiveId(int receiveId) {
			this.receiveId = receiveId;
		}
		public String getOrderNum() {
			return orderNum;
		}
		public void setOrderNum(String orderNum) {
			this.orderNum = orderNum;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getMobNum() {
			return mobNum;
		}
		public void setMobNum(String mobNum) {
			this.mobNum = mobNum;
		}
		
		
		public String getMailStatus() {
			return mailStatus;
		}
		public void setMailStatus(String mailStatus) {
			this.mailStatus = mailStatus;
		}
		public String getMessageTime() {
			return messageTime;
		}
		public void setMessageTime(String messageTime) {
			this.messageTime = messageTime;
		}
		public String getMessageStatus() {
			return messageStatus;
		}
		public void setMessageStatus(String messageStatus) {
			this.messageStatus = messageStatus;
		}
		
		public String getSenderName() {
			return SenderName;
		}
		public void setSenderName(String senderName) {
			SenderName = senderName;
		}
		
		public String getUserCode() {
			return userCode;
		}
		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}
		public String getOrgCode() {
			return orgCode;
		}
		public void setOrgCode(String orgCode) {
			this.orgCode = orgCode;
		}
		public ReceivePaymentBean() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString() {
			return "ReceivePaymentBean [receiveId=" + receiveId + ", orderNum="
					+ orderNum + ", price=" + price + ", mobNum=" + mobNum
					+ ", SenderName=" + SenderName + ", mailStatus="
					+ mailStatus + ", messageTime=" + messageTime
					+ ", messageStatus=" + messageStatus + ", userCode="
					+ userCode + ", orgCode=" + orgCode + "]";
		}
		
}

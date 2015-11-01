package com.ems.express.bean;

import java.io.Serializable;

public class PushBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7853537012015300404L;
	
	private MessageEntity messageEntity ;
	public MessageEntity getMessageEntity() {
		return messageEntity;
	}
	public void setMessageEntity(MessageEntity messageEntity) {
		this.messageEntity = messageEntity;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String resCode;
	private String resMsg;
	private String resType;
}

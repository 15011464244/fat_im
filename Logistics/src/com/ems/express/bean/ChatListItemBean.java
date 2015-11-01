package com.ems.express.bean;

import java.io.Serializable;

public class ChatListItemBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 聊天列表好友的id
	 */
	private String id;
	/**
	 * 聊天列表好友的头像
	 */
	private String image;
	/**
	 * 聊天列表好友的名称
	 */
	private String name;
	/**
	 * 聊天列表好友的最后一条信息
	 */
	private String msg;
	/**
	 * 聊天列表好友的最后聊天时间
	 */
	private String time;

	private String mobile;
	private String clientId; //聊天标示
	private int unRedCount;//未读条数
	
	

	public int getUnRedCount() {
		return unRedCount;
	}

	public void setUnRedCount(int unRedCount) {
		this.unRedCount = unRedCount;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}

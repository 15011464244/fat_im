package com.ems.express.bean;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import android.graphics.Bitmap;

public class ChatMessageBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int messageId;
	/**
	 * Msg内容
	 */
	private String content;
	/**
	 * Msg图片
	 */
	private Bitmap content_image;
	/**
	 * Msg语音
	 */
	private File content_voice;
	/**
	 * 1信息,2语音,3图片
	 */
	private String contentType;

	/**
	 * 发送方
	 */
	private String source;

	/**
	 * 接收方
	 */
	private String target;
	
	private String sourceIcon;
	/**
	 * 发送方还是接收方
	 */
	private boolean status = false;
	/**
	 * 是显示发送气泡还是接收气泡。默认true显示发送气泡
	 */
	private boolean sendIsReceive = true;
	public String serverPath;//服务器地址
	public String picpath;//本地地址
	private String messageSendStatus;//发送状态 0-成功，1-失败 ，2-发送中
	private int speechMessageTime;//语音文件时间
	
	public int getSpeechMessageTime() {
		return speechMessageTime;
	}
	public void setSpeechMessageTime(int speechMessageTime) {
		this.speechMessageTime = speechMessageTime;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getMessageSendStatus() {
		return messageSendStatus;
	}
	public void setMessageSendStatus(String messageSendStatus) {
		this.messageSendStatus = messageSendStatus;
	}
	public boolean isSendIsReceive() {
		return sendIsReceive;
	}
	public void setSendIsReceive(boolean sendIsReceive) {
		this.sendIsReceive = sendIsReceive;
	}
	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	public String getPicpath() {
		return picpath;
	}

	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

	public ChatMessageBean() {
		super();
	}
	
	public ChatMessageBean(String contentType,String content_text,boolean bo){
		super();
		this.contentType = contentType;
		this.content = content_text;
		this.sendIsReceive = bo;
	}

	public ChatMessageBean(String contentType, String content_text,
			String source, String target) {
		super();
		this.contentType = contentType;
		this.content = content_text;
		this.source = source;
		this.target = target;
	}
	public ChatMessageBean(String contentType, Bitmap content_image,
			String source, String target) {
		super();
		this.contentType = contentType;
		this.content_image = content_image;
		this.source = source;
		this.target = target;
	}

	public ChatMessageBean(String contentType, File content_voice,
			String source, String target) {
		super();
		this.contentType = contentType;
		this.content_voice = content_voice;
		this.source = source;
		this.target = target;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Bitmap getContent_image() {
		return content_image;
	}

	public void setContent_image(Bitmap content_image) {
		this.content_image = content_image;
	}

	public File getContent_voice() {
		return content_voice;
	}

	public void setContent_voice(File content_voice) {
		this.content_voice = content_voice;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
	public String getSourceIcon() {
		return sourceIcon;
	}
	public void setSourceIcon(String sourceIcon) {
		this.sourceIcon = sourceIcon;
	}
	
	public static class ContentType{
		public final static String TEXT = "0";//普通文字信息
		public final static String FILE = "2";//语音信息
		public final static String IMG = "1";//图片
	}
	
}

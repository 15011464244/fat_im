package com.newcdc.chat.ui;

import java.io.File;
import java.io.Serializable;

import android.graphics.Bitmap;

public class ChatMessageBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String content;// Msg内容
	private Bitmap content_image;// Msg图片
	private File content_voice;// Msg语音
	private String contentType;// 0信息,1图片,2语音
	private String source;// 发送方
	private String target;// 接收方
	private String status;// 发送状态
	private int messageId;// 消息id
	public String serverPath;// 服务器地址
	public String picpath;// 本地地址
	public int messageTime;// 语音时间

	public int getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(int messageTime) {
		this.messageTime = messageTime;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
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

	public ChatMessageBean(String contentType, String content_text,
			String source, String target, String status) {
		super();
		this.contentType = contentType;
		this.content = content_text;
		this.source = source;
		this.target = target;
		this.status = status;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static class ContentType {
		public final static String TEXT = "0";// 普通文字信息
		public final static String FILE = "2";// 语音信息
		public final static String IMG = "1";// 图片
	}

}

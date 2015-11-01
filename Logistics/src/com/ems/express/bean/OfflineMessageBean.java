package com.ems.express.bean;

import java.io.Serializable;

public class OfflineMessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4885334292010216864L;

	private long create_time;
	private String message_type;
	private int pkid;
	private String source;
	private String state;
	private String target;
	private String url;
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	public int getPkid() {
		return pkid;
	}
	public void setPkid(int pkid) {
		this.pkid = pkid;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}

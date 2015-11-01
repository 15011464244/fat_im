package com.newcdc.model;

import java.io.Serializable;

/**
 * create_by 创建人 create_time 创建时间 update_by 修改人 update_time 修改时间
 * 
 * @author liunannan
 * */

public class GroupBean implements Serializable {
	private String sid;
	private String group_content;
	private String group_type;
	private String create_by;
	private String create_time;
	private String update_by;
	private String update_time;
	private String latlng;
	private String distance;
	private String mailCount;
	
	public String getMailCount() {
		return mailCount;
	}

	public void setMailCount(String mailCount) {
		this.mailCount = mailCount;
	}

	public GroupBean() {
		super();
	}

	public String getLatlng() {
		return latlng;
	}

	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getGroup_content() {
		return group_content;
	}

	public void setGroup_content(String group_content) {
		this.group_content = group_content;
	}

	public String getGroup_type() {
		return group_type;
	}

	public void setGroup_type(String group_type) {
		this.group_type = group_type;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "GroupBean [sid=" + sid + ", group_content=" + group_content
				+ ", group_type=" + group_type + ", create_by=" + create_by
				+ ", create_time=" + create_time + ", update_by=" + update_by
				+ ", update_time=" + update_time + ", latlng=" + latlng
				+ ", distance=" + distance + "]";
	}

	/**
	 * 解析json使用的构造器
	 */
	public GroupBean(String sid, String group_content, String group_type,
			String create_by, String create_time, String update_by,
			String update_time, String latlng, String distance) {
		super();
		this.sid = sid;
		this.group_content = group_content;
		this.group_type = group_type;
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.latlng = latlng;
		this.distance = distance;
	}
}

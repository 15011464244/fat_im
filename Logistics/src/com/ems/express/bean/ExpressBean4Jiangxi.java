package com.ems.express.bean;

import java.io.Serializable;

public class ExpressBean4Jiangxi implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String date;
	private String time;
	private String lastBrch;
	private String mailState;
	private String dealDest;
	private String mailRemark;
	private String flag;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLastBrch() {
		return lastBrch;
	}
	public void setLastBrch(String lastBrch) {
		this.lastBrch = lastBrch;
	}
	public String getMailState() {
		return mailState;
	}
	public void setMailState(String mailState) {
		this.mailState = mailState;
	}
	public String getDealDest() {
		return dealDest;
	}
	public void setDealDest(String dealDest) {
		this.dealDest = dealDest;
	}
	public String getMailRemark() {
		return mailRemark;
	}
	public void setMailRemark(String mailRemark) {
		this.mailRemark = mailRemark;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}

}

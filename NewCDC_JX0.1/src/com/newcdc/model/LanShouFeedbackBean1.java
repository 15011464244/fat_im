package com.newcdc.model;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class LanShouFeedbackBean1 {
	String deviceId;
	String employee;
	String delvorgcode;
	String postmankind;
	String number;
	List<LanShouFeedbackBean2> dataDetail;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getDelvorgcode() {
		return delvorgcode;
	}
	public void setDelvorgcode(String delvorgcode) {
		this.delvorgcode = delvorgcode;
	}
	public String getPostmankind() {
		return postmankind;
	}
	public void setPostmankind(String postmankind) {
		this.postmankind = postmankind;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public List<LanShouFeedbackBean2> getDataDetail() {
		return dataDetail;
	}
	public void setDataDetail(List<LanShouFeedbackBean2> dataDetail) {
		this.dataDetail = dataDetail;
	}
	@Override
	public String toString() {
		return "LanShouFeedbackBean1 [deviceId=" + deviceId + ", employee="
				+ employee + ", delvorgcode=" + delvorgcode + ", postmankind="
				+ postmankind + ", number=" + number + ", dataDetail="
				+ dataDetail + "]";
	}
	
}

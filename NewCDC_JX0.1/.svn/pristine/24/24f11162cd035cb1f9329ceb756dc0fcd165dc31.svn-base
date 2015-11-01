package com.cn.cdc;

import java.io.Serializable;

import org.json.JSONObject;

public class Dlv_state implements Serializable {

	private String pdaCode;
	private String stateCode;
	private String stateType;
	private String stateDescCHS;
	private String followAction;

	public Dlv_state(JSONObject json) {

		try {
			pdaCode = json.get("PDACODE") == null ? "" : json.get("PDACODE")
					.toString();
			stateCode = json.get("STATECODE") == null ? "" : json.get(
					"STATECODE").toString();
			stateType = json.get("STATETYPE") == null ? "" : json.get(
					"STATETYPE").toString();
			stateDescCHS = json.get("STATEDESCCHS") == null ? "" : json.get(
					"STATEDESCCHS").toString();
			followAction = json.get("FOLLOWACTION") == null ? "" : json.get(
					"FOLLOWACTION").toString();
		} catch (Exception e) {

		}
	}

	public String getPdaCode() {
		return pdaCode;
	}

	public void setPdaCode(String pdaCode) {
		this.pdaCode = pdaCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateType() {
		return stateType;
	}

	public void setStateType(String stateType) {
		this.stateType = stateType;
	}

	public String getStateDescCHS() {
		return stateDescCHS;
	}

	public void setStateDescCHS(String stateDescCHS) {
		this.stateDescCHS = stateDescCHS;
	}

	public String getFollowAction() {
		return followAction;
	}

	public void setFollowAction(String followAction) {
		this.followAction = followAction;
	}

}

package com.cn.cdc;

import java.io.Serializable;

import org.json.JSONObject;

public class StandardCode implements Serializable {

	private String standardCode;
	private String followAction;

	public StandardCode(JSONObject json) {

		try {
			followAction = json.get("FOLLOWACTION") == null ? "" : json.get(
					"FOLLOWACTION").toString();
			standardCode = json.get("STANDARDCODE") == null ? "" : json.get(
					"STANDARDCODE").toString();
		} catch (Exception e) {

		}
	}

	public String getStandardCode() {
		return standardCode;
	}

	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
	}

	public String getFollowAction() {
		return followAction;
	}

	public void setFollowAction(String followAction) {
		this.followAction = followAction;
	}

}

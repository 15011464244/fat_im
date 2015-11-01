package com.cn.cdc;

import java.io.Serializable;

import org.json.JSONObject;

public class FollowAction implements Serializable {

	private String followAction;
	private String actionDescCHS;

	public FollowAction(JSONObject json) {

		try {
			followAction = json.get("FOLLOWACTION") == null ? "" : json.get(
					"FOLLOWACTION").toString();
			actionDescCHS = json.get("ACTIONDESCCHS") == null ? "" : json.get(
					"ACTIONDESCCHS").toString();
		} catch (Exception e) {

		}
	}

	public String getFollowAction() {
		return followAction;
	}

	public void setFollowAction(String followAction) {
		this.followAction = followAction;
	}

	public String getActionDescCHS() {
		return actionDescCHS;
	}

	public void setActionDescCHS(String actionDescCHS) {
		this.actionDescCHS = actionDescCHS;
	}

}

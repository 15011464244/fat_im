package com.ems.express.bean;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
	private static final long serialVersionUID = 8330482387552302479L;
	private boolean login = false;
	private boolean logout = false;
	private String date;
	private String content;
	private String LoginName;
	private List<String> reciverDeps;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public String getLoginName() {
		return LoginName;
	}

	public void setLoginName(String loginName) {
		LoginName = loginName;
	}

	public boolean isLogout() {
		return logout;
	}

	public void setLogout(boolean logout) {
		this.logout = logout;
	}

	public List<String> getReciverDeps() {
		return reciverDeps;
	}

	public void setReciverDeps(List<String> reciverDeps) {
		this.reciverDeps = reciverDeps;
	}
}

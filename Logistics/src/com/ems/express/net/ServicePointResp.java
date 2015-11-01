package com.ems.express.net;

import java.io.Serializable;

public class ServicePointResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2631452727351614130L;

	
	private String result;
	private ServicePointBody body;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ServicePointBody getBody() {
		return body;
	}
	public void setBody(ServicePointBody body) {
		this.body = body;
	}
	
	
}

package com.ems.express.net.resp;

import java.io.Serializable;

public class BaseResp implements Serializable {
	private static final long serialVersionUID = 1L;
	private String resCode;
	private String resMsg;
//	private String retType;

//	public String getRetType() {
//		return retType;
//	}
//
//	public void setRetType(String retType) {
//		this.retType = retType;
//	}

	public String getRetCode() {
		return resCode;
	}

	public void setRetCode(String retCode) {
		this.resCode = retCode;
	}

	public String getRetMsg() {
		return resMsg;
	}

	public void setRetMsg(String retMsg) {
		this.resMsg = retMsg;
	}

}

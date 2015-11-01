package com.ems.express.net.resp;

import java.io.Serializable;

public class QueryAddressBean implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 投递机构代码
	 */
	private String dlvOrgCode;
	/**
	 * 投递机构名称
	 */
	private String dlvOrgName;
	/**
	 * 地址
	 */
	private String dlvAddr;
	/**
	 * @return the dlvOrgCode
	 */
	public String getDlvOrgCode() {
		return dlvOrgCode;
	}
	/**
	 * @param dlvOrgCode the dlvOrgCode to set
	 */
	public void setDlvOrgCode(String dlvOrgCode) {
		this.dlvOrgCode = dlvOrgCode;
	}
	/**
	 * @return the dlvOrgName
	 */
	public String getDlvOrgName() {
		return dlvOrgName;
	}
	/**
	 * @param dlvOrgName the dlvOrgName to set
	 */
	public void setDlvOrgName(String dlvOrgName) {
		this.dlvOrgName = dlvOrgName;
	}
	/**
	 * @return the dlvAddr
	 */
	public String getDlvAddr() {
		return dlvAddr;
	}
	/**
	 * @param dlvAddr the dlvAddr to set
	 */
	public void setDlvAddr(String dlvAddr) {
		this.dlvAddr = dlvAddr;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QueryAddressBean [dlvOrgCode=" + dlvOrgCode + ", dlvOrgName="
				+ dlvOrgName + ", dlvAddr=" + dlvAddr + "]";
	}
	
	
}

package com.ems.express.bean;

import java.io.Serializable;

public class Success implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ERALNAME;
	private String MOBILE;
	private String EMPLOYEENO;
	private String LONGITUDE;
	private String LATITUDE;
	private String CODE;

	/**
	 * @return the eRALNAME
	 */
	public String getERALNAME() {
		return ERALNAME;
	}

	/**
	 * @param eRALNAME
	 *            the eRALNAME to set
	 */
	public void setERALNAME(String eRALNAME) {
		ERALNAME = eRALNAME;
	}

	/**
	 * @return the mOBILE
	 */
	public String getMOBILE() {
		return MOBILE;
	}

	/**
	 * @param mOBILE
	 *            the mOBILE to set
	 */
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}

	/**
	 * @return the eMPLOYEENO
	 */
	public String getEMPLOYEENO() {
		return EMPLOYEENO;
	}

	/**
	 * @param eMPLOYEENO
	 *            the eMPLOYEENO to set
	 */
	public void setEMPLOYEENO(String eMPLOYEENO) {
		EMPLOYEENO = eMPLOYEENO;
	}

	/**
	 * @return the lONGITUDE
	 */
	public String getLONGITUDE() {
		return LONGITUDE;
	}

	/**
	 * @param lONGITUDE
	 *            the lONGITUDE to set
	 */
	public void setLONGITUDE(String lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}

	/**
	 * @return the lATITUDE
	 */
	public String getLATITUDE() {
		return LATITUDE;
	}

	/**
	 * @param lATITUDE
	 *            the lATITUDE to set
	 */
	public void setLATITUDE(String lATITUDE) {
		LATITUDE = lATITUDE;
	}

	/**
	 * @return the cODE
	 */
	public String getCODE() {
		return CODE;
	}

	/**
	 * @param cODE
	 *            the cODE to set
	 */
	public void setCODE(String cODE) {
		CODE = cODE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Success [ERALNAME=" + ERALNAME + ", MOBILE=" + MOBILE
				+ ", EMPLOYEENO=" + EMPLOYEENO + ", LONGITUDE=" + LONGITUDE
				+ ", LATITUDE=" + LATITUDE + ", CODE=" + CODE + "]";
	}

}

package com.newcdc.chat.model;

import java.io.Serializable;
import java.util.List;


public class ReceivedMsgResp implements Serializable {
	private static final long serialVersionUID = 1L;
	private String resCode;
	private String resMsg;
	private List<MsgBean> offlineMsgList;
	/**
	 * @return the resCode
	 */
	public String getResCode() {
		return resCode;
	}
	/**
	 * @param resCode the resCode to set
	 */
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	/**
	 * @return the resMsg
	 */
	public String getResMsg() {
		return resMsg;
	}
	/**
	 * @param resMsg the resMsg to set
	 */
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	/**
	 * @return the offlineMsgList
	 */
	public List<MsgBean> getOfflineMsgList() {
		return offlineMsgList;
	}
	/**
	 * @param offlineMsgList the offlineMsgList to set
	 */
	public void setOfflineMsgList(List<MsgBean> offlineMsgList) {
		this.offlineMsgList = offlineMsgList;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
//	@Override
//	public String toString() {
//		return "ReceivedMsgBean [resCode=" + resCode + ", resMsg=" + resMsg
//				+ ", offlineMsgList=" + offlineMsgList + "]";
//	}
	
}

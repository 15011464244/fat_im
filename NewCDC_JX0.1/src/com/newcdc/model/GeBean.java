package com.newcdc.model;

/**
 * 
 * 
 * @author 交班 格口代码
 * 
 */
public class GeBean {
	String mail_num;// 邮件号
	String receiveAddressDaiMa;// 寄达地代码
	String cpDaima;// 产品代码
	String receivPost;// 收件人邮编
	String fenleiDaima;// 分类代码
	String emsZhonglei;// 邮件种类
	String transport;// 运输方式代码

	public String getMail_num() {
		return mail_num;
	}

	public void setMail_num(String mail_num) {
		this.mail_num = mail_num;
	}

	public String getReceiveAddressDaiMa() {
		return receiveAddressDaiMa;
	}

	public void setReceiveAddressDaiMa(String receiveAddressDaiMa) {
		this.receiveAddressDaiMa = receiveAddressDaiMa;
	}

	public String getCpDaima() {
		return cpDaima;
	}

	public void setCpDaima(String cpDaima) {
		this.cpDaima = cpDaima;
	}

	public String getReceivPost() {
		return receivPost;
	}

	public void setReceivPost(String receivPost) {
		this.receivPost = receivPost;
	}

	public String getFenleiDaima() {
		return fenleiDaima;
	}

	public void setFenleiDaima(String fenleiDaima) {
		this.fenleiDaima = fenleiDaima;
	}

	public String getEmsZhonglei() {
		return emsZhonglei;
	}

	public void setEmsZhonglei(String emsZhonglei) {
		this.emsZhonglei = emsZhonglei;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	@Override
	public String toString() {
		return "GeBean [mail_num=" + mail_num + ", receiveAddressDaiMa="
				+ receiveAddressDaiMa + ", cpDaima=" + cpDaima
				+ ", receivPost=" + receivPost + ", fenleiDaima=" + fenleiDaima
				+ ", emsZhonglei=" + emsZhonglei + ", transport=" + transport
				+ "]";
	}

}

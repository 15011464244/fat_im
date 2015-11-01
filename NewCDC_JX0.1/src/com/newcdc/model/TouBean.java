package com.newcdc.model;

/**
 * 
 * 
 * @author 交班tou
 * 
 */
public class TouBean {
	String mail_num;// 邮件号 -
	String charge;// 金额 -
	String totailCharge;// 实收金额 -
	String deliveryConditions;// 投递情况 -
	String sign;// 签收情况 -
	String nextAction;// 未妥投下一步情况
	String pay_type;// 支付方式 -
	String frequency;// 频次 -
	String frequencyDate;// 频次日期 -
	String pdacode;// 揽投站代码

	public String getPdacode() {
		return pdacode;
	}

	public void setPdacode(String pdacode) {
		this.pdacode = pdacode;
	}

	public String getMail_num() {
		return mail_num;
	}

	public void setMail_num(String mail_num) {
		this.mail_num = mail_num;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getTotailCharge() {
		return totailCharge;
	}

	public void setTotailCharge(String totailCharge) {
		this.totailCharge = totailCharge;
	}

	public String getDeliveryConditions() {
		return deliveryConditions;
	}

	public void setDeliveryConditions(String deliveryConditions) {
		this.deliveryConditions = deliveryConditions;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getFrequencyDate() {
		return frequencyDate;
	}

	public void setFrequencyDate(String frequencyDate) {
		this.frequencyDate = frequencyDate;
	}

	@Override
	public String toString() {
		return "TouBean [mail_num=" + mail_num + ", charge=" + charge
				+ ", totailCharge=" + totailCharge + ", deliveryConditions="
				+ deliveryConditions + ", sign=" + sign + ", nextAction="
				+ nextAction + ", pay_type=" + pay_type + ", frequency="
				+ frequency + ", frequencyDate=" + frequencyDate + "]";
	}

}

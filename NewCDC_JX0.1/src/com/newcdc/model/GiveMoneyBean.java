package com.newcdc.model;

/**
 * @author hanrong
 * @version 创建时间：2015-2-4 下午4:13:49 类说明
 */
public class GiveMoneyBean {
	private int _id;
	private String mail_num;// 邮件号
	private String charge; // 费用
	private String actualGoodsFee; // 缴款方式
	private String pay_type; // 支付方式
	private String org_Code;// 机构号
	private String username;// 机构号
	private String org_user;// 用户名
	private String weight;// 重量
	private String address; // 寄达地代码
	private String operatortype; // 操作类型
	private String operatorTime;// 操作时间
	private String ltData;
	private String createtime; // 创建时间;
	private String ismoney; // 是否缴款;0：未缴；1已缴
	private String givemoneytype;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
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
	public String getActualGoodsFee() {
		return actualGoodsFee;
	}
	public void setActualGoodsFee(String actualGoodsFee) {
		this.actualGoodsFee = actualGoodsFee;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getOrg_Code() {
		return org_Code;
	}
	public void setOrg_Code(String org_Code) {
		this.org_Code = org_Code;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOrg_user() {
		return org_user;
	}
	public void setOrg_user(String org_user) {
		this.org_user = org_user;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOperatortype() {
		return operatortype;
	}
	public void setOperatortype(String operatortype) {
		this.operatortype = operatortype;
	}
	public String getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}
	public String getLtData() {
		return ltData;
	}
	public void setLtData(String ltData) {
		this.ltData = ltData;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getIsmoney() {
		return ismoney;
	}
	public void setIsmoney(String ismoney) {
		this.ismoney = ismoney;
	}
	public String getGivemoneytype() {
		return givemoneytype;
	}
	public void setGivemoneytype(String givemoneytype) {
		this.givemoneytype = givemoneytype;
	}

}

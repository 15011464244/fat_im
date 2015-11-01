package com.newcdc.model;

public class MessageInfoBean {
	private String rcver_loc_county;
	private String dlvorgcode;
	private String rcver_contact_phone1;
	private String sender_loc_county;
	private String sender_street_addr;
	private String rcver_loc_prov;
	private String sender_contact_phone1;
	private String rcver_name;
	private String the_class_date;
	private String rcver_street_addr;
	private String sender_name;
	private String sender_loc_prov;
	private String frequence;
	private String sender_loc_city;
	private String rcver_loc_city;
	private String mail_num;
	private String username;
	private String sender_addr;
	private String rcver_addr;
	private String charge;
	private String money;
	private int distance;
	private int type;
	private String latlng;
	private int _id;
	/* 支付方式 */
	private String pay_type;
	/* 缴款方式 */
	private String actualGoodsFee;
	/* 寄达地代码 */
	private String address;
	private String operatortype; // 操作类型
	/* 重量 */
	private String weight;
	private int dealResult;// 处理结果
	private String checked;
	private int call_time;// 打电话次数
	private int msg_time;// 发短信次数
	private String picture;// 照片名字
	private int msg_user;
	private String dlv_pseg_code;
	private String dlv_date;
	private String dlv_time;
	private String flag;
	private String uplode_date;// 首次跟新时间
	private int queue_task_type;
	private String in_self_group;
	private String notified;
	private int sid;
	private String undlv_next_actn_code;// 下一步动作
	private String undlv_cause_code;// 未妥投原因
	private String sign_sts_code;// 签收情况代码
	private String signer_name;// 签收人姓名
	private String operatorTime;	//操作时间
	private String ltData;
	
	public String getLtData() {
		return ltData;
	}

	public void setLtData(String ltData) {
		this.ltData = ltData;
	}

	public String getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getOperatortype() {
		return operatortype;
	}

	public void setOperatortype(String operatortype) {
		this.operatortype = operatortype;
	}

	public String getUndlv_next_actn_code() {
		return undlv_next_actn_code;
	}

	public void setUndlv_next_actn_code(String undlv_next_actn_code) {
		this.undlv_next_actn_code = undlv_next_actn_code;
	}

	public String getUndlv_cause_code() {
		return undlv_cause_code;
	}

	public void setUndlv_cause_code(String undlv_cause_code) {
		this.undlv_cause_code = undlv_cause_code;
	}

	public String getSign_sts_code() {
		return sign_sts_code;
	}

	public void setSign_sts_code(String sign_sts_code) {
		this.sign_sts_code = sign_sts_code;
	}

	public String getSigner_name() {
		return signer_name;
	}

	public void setSigner_name(String signer_name) {
		this.signer_name = signer_name;
	}

	public String getIn_self_group() {
		return in_self_group;
	}

	public void setIn_self_group(String in_self_group) {
		this.in_self_group = in_self_group;
	}

	public String getNotified() {
		return notified;
	}

	public void setNotified(String notified) {
		this.notified = notified;
	}

	public int getQueue_task_type() {
		return queue_task_type;
	}

	public void setQueue_task_type(int queue_task_type) {
		this.queue_task_type = queue_task_type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getDlv_date() {
		return dlv_date;
	}

	public void setDlv_date(String dlv_date) {
		this.dlv_date = dlv_date;
	}

	public String getDlv_time() {
		return dlv_time;
	}

	public void setDlv_time(String dlv_time) {
		this.dlv_time = dlv_time;
	}

	public String getUplode_date() {
		return uplode_date;
	}

	public void setUplode_date(String uplode_date) {
		this.uplode_date = uplode_date;
	}

	public String getDlv_pseg_code() {
		return dlv_pseg_code;
	}

	public void setDlv_pseg_code(String dlv_pseg_code) {
		this.dlv_pseg_code = dlv_pseg_code;
	}

	public int getMsg_user() {
		return msg_user;
	}

	public void setMsg_user(int msg_user) {
		this.msg_user = msg_user;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getChecked() {
		return checked;
	}

	public int getCall_time() {
		return call_time;
	}

	public void setCall_time(int call_time) {
		this.call_time = call_time;
	}

	public int getMsg_time() {
		return msg_time;
	}

	public void setMsg_time(int msg_time) {
		this.msg_time = msg_time;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getDlvorgcode() {
		return dlvorgcode;
	}

	public void setDlvorgcode(String dlvorgcode) {
		this.dlvorgcode = dlvorgcode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getDealResult() {
		return dealResult;
	}

	public void setDealResult(int dealResult) {
		this.dealResult = dealResult;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int get_id() {
		return _id;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getActualGoodsFee() {
		return actualGoodsFee;
	}

	public void setActualGoodsFee(String actualGoodsFee) {
		this.actualGoodsFee = actualGoodsFee;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLatlng() {
		return latlng;
	}

	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getRcver_loc_county() {
		return rcver_loc_county;
	}

	public void setRcver_loc_county(String rcver_loc_county) {
		this.rcver_loc_county = rcver_loc_county;
	}

	public String getRcver_contact_phone1() {
		return rcver_contact_phone1;
	}

	public void setRcver_contact_phone1(String rcver_contact_phone1) {
		this.rcver_contact_phone1 = rcver_contact_phone1;
	}

	public String getSender_loc_county() {
		return sender_loc_county;
	}

	public void setSender_loc_county(String sender_loc_county) {
		this.sender_loc_county = sender_loc_county;
	}

	public String getSender_street_addr() {
		return sender_street_addr;
	}

	public void setSender_street_addr(String sender_street_addr) {
		this.sender_street_addr = sender_street_addr;
	}

	public String getRcver_loc_prov() {
		return rcver_loc_prov;
	}

	public void setRcver_loc_prov(String rcver_loc_prov) {
		this.rcver_loc_prov = rcver_loc_prov;
	}

	public String getSender_contact_phone1() {
		return sender_contact_phone1;
	}

	public void setSender_contact_phone1(String sender_contact_phone1) {
		this.sender_contact_phone1 = sender_contact_phone1;
	}

	public String getRcver_name() {
		return rcver_name;
	}

	public void setRcver_name(String rcver_name) {
		this.rcver_name = rcver_name;
	}

	public String getThe_class_date() {
		return the_class_date;
	}

	public void setThe_class_date(String the_class_date) {
		this.the_class_date = the_class_date;
	}

	public String getRcver_street_addr() {
		return rcver_street_addr;
	}

	public MessageInfoBean() {
		super();
	}

	public void setRcver_street_addr(String rcver_street_addr) {
		this.rcver_street_addr = rcver_street_addr;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSender_loc_prov() {
		return sender_loc_prov;
	}

	public void setSender_loc_prov(String sender_loc_prov) {
		this.sender_loc_prov = sender_loc_prov;
	}

	public String getFrequence() {
		return frequence;
	}

	public void setFrequence(String frequence) {
		this.frequence = frequence;
	}

	public String getSender_loc_city() {
		return sender_loc_city;
	}

	public void setSender_loc_city(String sender_loc_city) {
		this.sender_loc_city = sender_loc_city;
	}

	public String getRcver_loc_city() {
		return rcver_loc_city;
	}

	public void setRcver_loc_city(String rcver_loc_city) {
		this.rcver_loc_city = rcver_loc_city;
	}

	public String getMail_num() {
		return mail_num;
	}

	public void setMail_num(String mail_num) {
		this.mail_num = mail_num;
	}

	public String getSender_addr() {
		return sender_addr;
	}

	public void setSender_addr(String sender_addr) {
		this.sender_addr = sender_addr;
	}

	public String getRcver_addr() {
		return rcver_addr;
	}

	public void setRcver_addr(String rcver_addr) {
		this.rcver_addr = rcver_addr;
	}

	/**
	 * 获取到bean的有效字段，拼接成字符串进行模糊匹配
	 * 
	 * @return
	 */
	public String toString_m() {
		return rcver_contact_phone1 + "," + rcver_name + ","
				+ rcver_street_addr + "," + mail_num;
	}

	@Override
	public String toString() {
		return "MessageInfoBean [rcver_loc_county=" + rcver_loc_county
				+ ", dlvorgcode=" + dlvorgcode + ", rcver_contact_phone1="
				+ rcver_contact_phone1 + ", sender_loc_county="
				+ sender_loc_county + ", sender_street_addr="
				+ sender_street_addr + ", rcver_loc_prov=" + rcver_loc_prov
				+ ", sender_contact_phone1=" + sender_contact_phone1
				+ ", rcver_name=" + rcver_name + ", the_class_date="
				+ the_class_date + ", rcver_street_addr=" + rcver_street_addr
				+ ", sender_name=" + sender_name + ", sender_loc_prov="
				+ sender_loc_prov + ", frequence=" + frequence
				+ ", sender_loc_city=" + sender_loc_city + ", rcver_loc_city="
				+ rcver_loc_city + ", mail_num=" + mail_num + ", username="
				+ username + ", sender_addr=" + sender_addr + ", rcver_addr="
				+ rcver_addr + ", charge=" + charge + ", money=" + money
				+ ", distance=" + distance + ", type=" + type + ", latlng="
				+ latlng + ", _id=" + _id + ", pay_type=" + pay_type
				+ ", actualGoodsFee=" + actualGoodsFee + ", address=" + address
				+ ", weight=" + weight + ", dealResult=" + dealResult
				+ ", checked=" + checked + ", call_time=" + call_time
				+ ", msg_time=" + msg_time + ", picture=" + picture
				+ ", msg_user=" + msg_user + ", dlv_pseg_code=" + dlv_pseg_code
				+ ", dlv_date=" + dlv_date + ", dlv_time=" + dlv_time
				+ ", flag=" + flag + ", uplode_date=" + uplode_date
				+ ", queue_task_type=" + queue_task_type + ", in_self_group="
				+ in_self_group + ", notified=" + notified + "]";
	}

	public MessageInfoBean(String rcver_loc_county,
			String rcver_contact_phone1, String sender_loc_county,
			String sender_street_addr, String rcver_loc_prov,
			String sender_contact_phone1, String rcver_name,
			String the_class_date, String rcver_street_addr,
			String sender_name, String sender_loc_prov, String frequence,
			String sender_loc_city, String rcver_loc_city, String mail_num,
			String sender_addr, String rcver_addr, String money, int distance,
			int type, String latlng, int _id, String pay_type,
			String actualGoodsFee, String address, String weight) {
		super();
		this.rcver_loc_county = rcver_loc_county;
		this.rcver_contact_phone1 = rcver_contact_phone1;
		this.sender_loc_county = sender_loc_county;
		this.sender_street_addr = sender_street_addr;
		this.rcver_loc_prov = rcver_loc_prov;
		this.sender_contact_phone1 = sender_contact_phone1;
		this.rcver_name = rcver_name;
		this.the_class_date = the_class_date;
		this.rcver_street_addr = rcver_street_addr;
		this.sender_name = sender_name;
		this.sender_loc_prov = sender_loc_prov;
		this.frequence = frequence;
		this.sender_loc_city = sender_loc_city;
		this.rcver_loc_city = rcver_loc_city;
		this.mail_num = mail_num;
		this.sender_addr = sender_addr;
		this.rcver_addr = rcver_addr;
		this.money = money;
		this.distance = distance;
		this.type = type;
		this.latlng = latlng;
		this._id = _id;
		this.pay_type = pay_type;
		this.actualGoodsFee = actualGoodsFee;
		this.address = address;
		this.weight = weight;
	}

	/**
	 * 解析json使用的构造器
	 */
	public MessageInfoBean(String rcver_loc_county, String dlvorgcode,
			String rcver_contact_phone1, String sender_loc_county,
			String sender_street_addr, String rcver_loc_prov,
			String sender_contact_phone1, String rcver_name,
			String the_class_date, String rcver_street_addr,
			String sender_name, String sender_loc_prov, String frequence,
			String sender_loc_city, String rcver_loc_city, String mail_num,
			String username, String sender_addr, String rcver_addr,
			String money, String dlv_pseg_code) {
		super();
		this.rcver_loc_county = rcver_loc_county;
		this.dlvorgcode = dlvorgcode;
		this.rcver_contact_phone1 = rcver_contact_phone1;
		this.sender_loc_county = sender_loc_county;
		this.sender_street_addr = sender_street_addr;
		this.rcver_loc_prov = rcver_loc_prov;
		this.sender_contact_phone1 = sender_contact_phone1;
		this.rcver_name = rcver_name;
		this.the_class_date = the_class_date;
		this.rcver_street_addr = rcver_street_addr;
		this.sender_name = sender_name;
		this.sender_loc_prov = sender_loc_prov;
		this.frequence = frequence;
		this.sender_loc_city = sender_loc_city;
		this.rcver_loc_city = rcver_loc_city;
		this.mail_num = mail_num;
		this.username = username;
		this.sender_addr = sender_addr;
		this.rcver_addr = rcver_addr;
		this.money = money;
		this.dlv_pseg_code = dlv_pseg_code;
	}

}

package com.newcdc.model;

import org.json.JSONObject;

public class MailTypeBean {
	String OrgID;
	String ClassCode;
	String StartDate;
	String ClassName;
	String EndDate;
	String Sort;
	String SJLimitTime;
	String SJLimitWeight;
	String MailType;
	String Temp1;

	public MailTypeBean(JSONObject json) {

		try {
			OrgID = json.get("OrgID") == null ? "" : json.get("OrgID")
					.toString();
			ClassCode = json.get("ClassCode") == null ? "" : json.get(
					"ClassCode").toString();
			StartDate = json.get("StartDate") == null ? "" : json.get(
					"StartDate").toString();
			ClassName = json.get("ClassName") == null ? "" : json.get(
					"ClassName").toString();
			EndDate = json.get("EndDate") == null ? "" : json
					.get("EndDate").toString();
			Sort = json.get("Sort") == null ? "" : json.get("Sort")
					.toString();
			SJLimitTime = json.get("SJLimitTime") == null ? "" : json.get(
					"SJLimitTime").toString();
			SJLimitWeight = json.get("SJLimitWeight") == null ? "" : json
					.get("SJLimitWeight").toString();
			MailType = json.get("MailType") == null ? "" : json.get(
					"MailType").toString();
			Temp1 = json.get("Temp1") == null ? "" : json.get("Temp1")
					.toString();
		} catch (Exception e) {

		}

	}

	public String getOrgID() {
		return OrgID == null ? "" : OrgID;
	}

	public void setOrgID(String OrgID) {
		this.OrgID = OrgID;
	}

	public String getClassCode() {
		return ClassCode == null ? "" : ClassCode;
	}

	public void setClassCode(String ClassCode) {
		this.ClassCode = ClassCode;
	}

	public String getStartDate() {
		return StartDate == null ? "" : StartDate;
	}

	public void setStartDate(String StartDate) {
		this.StartDate = StartDate;
	}

	public String getClassName() {
		return ClassName == null ? "" : ClassName;
	}

	public void setClassName(String ClassName) {
		this.ClassName = ClassName;
	}

	public String getEndDate() {
		return EndDate == null ? "" : EndDate;
	}

	public void setEndDate(String EndDate) {
		this.EndDate = EndDate;
	}

	public String getSort() {
		return Sort == null ? "" : Sort;
	}

	public void setSort(String Sort) {
		this.Sort = Sort;
	}

	public String getSJLimitTime() {
		return SJLimitTime == null ? "" : SJLimitTime;
	}

	public void setSJLimitTime(String SJLimitTime) {
		this.SJLimitTime = SJLimitTime;
	}

	public String getSJLimitWeight() {
		return SJLimitWeight == null ? "" : SJLimitWeight;
	}

	public void setSJLimitWeight(String SJLimitWeight) {
		this.SJLimitWeight = SJLimitWeight;
	}

	public String getMailType() {
		return MailType == null ? "" : MailType;
	}

	public void setMailType(String MailType) {
		this.MailType = MailType;
	}

	public String getTemp1() {
		return Temp1 == null ? "" : Temp1;
	}

	public void setTemp1(String Temp1) {
		this.Temp1 = Temp1;
	}

	// public String getTemp2() {
	// return Temp2 == null ? "" : Temp2;
	// }
	//
	// public void setTemp2(String Temp2) {
	// this.Temp2 = Temp2;
	// }
	//
	// public String getTemp3() {
	// return Temp3 == null ? "" : Temp3;
	// }
	//
	// public void setTemp3(String Temp3) {
	// this.Temp3 = Temp3;
	// }
	//
	// public String getTemp4() {
	// return Temp4 == null ? "" : Temp4;
	// }
	//
	// public void setTemp4(String Temp4) {
	// this.Temp4 = Temp4;
	// }
	//
	// public String getTemp5() {
	// return Temp5 == null ? "" : Temp5;
	// }
	//
	// public void setTemp5(String Temp5) {
	// this.Temp5 = Temp5;
	// }
}

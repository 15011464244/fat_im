package com.cn.cdc;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * 江西版 基础数据 更新于2015.6.24
 * */
public class BaseData implements Serializable {

	private String dataKey;
	private String dataValue;
	private String dataFlags;// 所属类型 分为五种

	// NEXT_ACTN_CODE 下一步动作
	// MAIL_NOTE_CODE 邮件备注
	// DLV_STS_CODE 投递备注
	// TURN_BACK_CAUSE_CODE 转退原因
	// UNDLV_CAUSE_CODE 未妥投原因

//	public BaseData(JSONObject json) {
//		try {
//			dataKey = json.get("dataKey") == null ? "" : json.get("dataKey")
//					.toString();
//			dataValue = json.get("dataValue") == null ? "" : json.get(
//					"dataValue").toString();
//		} catch (Exception e) {
//
//		}
//	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getDataFlags() {
		return dataFlags;
	}

	public void setDataFlags(String dataFlags) {
		this.dataFlags = dataFlags;
	}

}

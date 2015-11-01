package com.address.jifei;

import java.io.Serializable;

public class StandardFee implements Serializable {
	/**
	 * 标准资费表
	 * */

	private int fee_id;// 标准资费表ID，主键ID，唯一索引
	private int fee_area_id;// 关联计费区间表主键ID
	private int weight_range_id;// 关联重量范围表主键ID
	private int first_weight;// 单位：克(g),首重重量,不超过这个重量的订单，费用一致
	private int first_fee;// 单位：元，首重费用，对应首重范围内的重量
	private int follow_weight;// 单位：克(g),续重计费单位重量，如果是固定价钱，此处为0
	private int follow_fee;// 单位：元，续重费用，同上

	public int getFee_id() {
		return fee_id;
	}

	public void setFee_id(int fee_id) {
		this.fee_id = fee_id;
	}

	public int getFee_area_id() {
		return fee_area_id;
	}

	public void setFee_area_id(int fee_area_id) {
		this.fee_area_id = fee_area_id;
	}

	public int getWeight_range_id() {
		return weight_range_id;
	}

	public void setWeight_range_id(int weight_range_id) {
		this.weight_range_id = weight_range_id;
	}

	public int getFirst_weight() {
		return first_weight;
	}

	public void setFirst_weight(int first_weight) {
		this.first_weight = first_weight;
	}

	public int getFirst_fee() {
		return first_fee;
	}

	public void setFirst_fee(int first_fee) {
		this.first_fee = first_fee;
	}

	public int getFollow_weight() {
		return follow_weight;
	}

	public void setFollow_weight(int follow_weight) {
		this.follow_weight = follow_weight;
	}

	public int getFollow_fee() {
		return follow_fee;
	}

	public void setFollow_fee(int follow_fee) {
		this.follow_fee = follow_fee;
	}

}

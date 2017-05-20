package com.mdm.request;

import java.util.List;

import com.mdm.pojo.PointAccount;

public class PointAccountRequest {
	private String pointAccountId;
	private List<PointAccount> orgIds;
	private String orgType;
	private String pointAccount;
	/**
	 * 类型：商家（merchant） or 组织（organization）
	 */
	private String type;

	public String getPointAccountId() {
		return pointAccountId;
	}
	public void setPointAccountId(String pointAccountId) {
		this.pointAccountId = pointAccountId;
	}
	public List<PointAccount> getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(List<PointAccount> orgIds) {
		this.orgIds = orgIds;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getPointAccount() {
		return pointAccount;
	}
	public void setPointAccount(String pointAccount) {
		this.pointAccount = pointAccount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}

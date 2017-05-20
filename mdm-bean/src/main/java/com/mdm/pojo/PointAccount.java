package com.mdm.pojo;

public class PointAccount {
	/**
	 * 组织类型
	 */
	private String orgType;
	/**
	 * 组织ID
	 */
	private String orgId;
	/**
	 * 是否排除
	 */
	private boolean isExcepted;
	/**
	 * 类型：商家（merchant） or 组织（organization）
	 */
	private String type;

	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public boolean isExcepted() {
		return isExcepted;
	}
	public void setIsExcepted(boolean isExcepted) {
		this.isExcepted = isExcepted;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

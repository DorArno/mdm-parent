package com.mdm.pojo;

import java.util.Date;

import com.mdm.core.util.DataUtils;
import com.mdm.core.util.HttpContent;

public class UserOrganizationRole {
	private String id;
	private String roleId;
	private String userId;
	private String organizationId;
	private String systemId;
	private String systemCode;
	private Integer type;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Byte isDeleted;
	private UserBasicsInfo user;
	private String departmentName;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Byte getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}
	public UserBasicsInfo getUser() {
		return user;
	}
	public void setUser(UserBasicsInfo user) {
		this.user = user;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public void init(String userId, String systemId) {
		this.id = DataUtils.uuid();
		this.userId = userId;
		this.systemId = systemId;
		this.createdBy = HttpContent.getOperatorId();
		this.setIsDeleted(new Byte("0"));
		this.setCreatedOn(new Date());
	}
	public void init( String systemId) {
		this.id = DataUtils.uuid();
		this.systemId = systemId;
		this.createdBy = HttpContent.getOperatorId();
		this.setIsDeleted(new Byte("0"));
		this.setCreatedOn(new Date());
	}
}

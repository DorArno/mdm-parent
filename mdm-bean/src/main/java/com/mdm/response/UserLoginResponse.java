package com.mdm.response;

import java.sql.Date;

/**
 * @ClassName: UserLoginResponse
 * @Description: TODO
 * @author: Han
 * @date: 2016年11月10日 下午2:42:03
 */
public class UserLoginResponse {
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 渠道
	 */
	private String channel;
	/**
	 * 登录IP
	 */
	private String loginIp;
	/**
	 * 状态(0：失败;1:成功)
	 */
	private Integer status;
	/**
	 * 登录时间
	 */
	private String loginTime;
	/**
	 * 类型(0:登出,1：登录)
	 */
	private Integer type;
	/**
	 * 用户主健Id
	 */
	private String userId;
	private Date CreatedOn;
	private String CreatedBy;
	private Date ModifiedOn;
	private String ModifiedBy;
	private Integer IsDeleted;
	private Integer exceptionCode;
	private String exceptionDetails;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(Date createdOn) {
		CreatedOn = createdOn;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public Date getModifiedOn() {
		return ModifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		ModifiedOn = modifiedOn;
	}
	public String getModifiedBy() {
		return ModifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}
	public Integer getIsDeleted() {
		return IsDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		IsDeleted = isDeleted;
	}
	public Integer getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(Integer exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public String getExceptionDetails() {
		return exceptionDetails;
	}
	public void setExceptionDetails(String exceptionDetails) {
		this.exceptionDetails = exceptionDetails;
	}
}

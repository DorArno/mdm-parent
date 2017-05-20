/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserResponse.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.user.response 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月11日 下午3:07:04 
 * @version: V1.0   
 */
package com.mdm.response;

/**
 * @ClassName: UserResponse
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月11日 下午3:07:04
 */
public class UserGridResponse {
	private Long pk;
	private String id;
	private String account;
	private String customerCode;
	private String username;
	private Integer status;
	private Integer type;
	private String cellPhone;
	private String email;
	private String systemId;
	private String corpCode;
	private Integer sex;
	private String phoneNumber;
	private String weChatID;
	private String createdOn;
	private String createdBy;
	private String sourceSystem;
	private boolean isBackEnd;	//	是否存在后端用户
	private Integer userChannels;
	
	private Integer phoneNumberConfirmed;// 用户绑定状态： 0 未绑定， 1 已绑定
	/**
	 * @return the pk
	 */
	public Long getPk() {
		return pk;
	}
	/**
	 * @param pk the pk to set
	 */
	public void setPk(Long pk) {
		this.pk = pk;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}
	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return the cellPhone
	 */
	public String getCellPhone() {
		return cellPhone;
	}
	/**
	 * @param cellPhone the cellPhone to set
	 */
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the systemId
	 */
	public String getSystemId() {
		return systemId;
	}
	/**
	 * @param systemId the systemId to set
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	/**
	 * @return the corpCode
	 */
	public String getCorpCode() {
		return corpCode;
	}
	/**
	 * @param corpCode the corpCode to set
	 */
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	/**
	 * @return the sex
	 */
	public Integer getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the weChatID
	 */
	public String getWeChatID() {
		return weChatID;
	}
	/**
	 * @param weChatID the weChatID to set
	 */
	public void setWeChatID(String weChatID) {
		this.weChatID = weChatID;
	}
	/**
	 * @return the createdOn
	 */
	public String getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public boolean isBackEnd() {
		return isBackEnd;
	}
	public void setBackEnd(boolean isBackEnd) {
		this.isBackEnd = isBackEnd;
	}
	public Integer getUserChannels() {
		return userChannels;
	}
	public void setUserChannels(Integer userChannels) {
		this.userChannels = userChannels;
	}
	public Integer getPhoneNumberConfirmed() {
		return phoneNumberConfirmed;
	}
	public void setPhoneNumberConfirmed(Integer phoneNumberConfirmed) {
		this.phoneNumberConfirmed = phoneNumberConfirmed;
	}
	
}

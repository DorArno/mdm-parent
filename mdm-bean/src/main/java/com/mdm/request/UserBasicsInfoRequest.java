/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserBasicsInfoRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.user.request 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月11日 下午3:38:00 
 * @version: V1.0   
 */
package com.mdm.request;

import com.mdm.common.CommonPojo;

/**
 * @ClassName: UserBasicsInfoRequest
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月11日 下午3:38:00
 */
public class UserBasicsInfoRequest extends CommonPojo {
	private String account;
	private String password;
	private String customerCode;
	private String username;
	private Integer status;
	private Integer type;
	private String cellPhone;
	private String email;
	private String systemId;
	private String systemCode;
	private String corpCode;
	private Integer sex;
	private String phoneNumber;
	private String weChatID;
	private String encryptionMode;
	private Integer userChannels;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the systemCode
	 */
	public String getSystemCode() {
		return systemCode;
	}
	/**
	 * @param systemCode the systemCode to set
	 */
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
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
	 * @return the encryptionMode
	 */
	public String getEncryptionMode() {
		return encryptionMode;
	}
	/**
	 * @param encryptionMode the encryptionMode to set
	 */
	public void setEncryptionMode(String encryptionMode) {
		this.encryptionMode = encryptionMode;
	}
	public Integer getUserChannels() {
		return userChannels;
	}
	public void setUserChannels(Integer userChannels) {
		this.userChannels = userChannels;
	}
	
}

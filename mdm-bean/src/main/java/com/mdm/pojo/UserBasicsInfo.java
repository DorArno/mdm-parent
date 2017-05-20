/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: User.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.user.pojo 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月11日 下午3:06:17 
 * @version: V1.0   
 */
package com.mdm.pojo;

import java.util.Date;

import com.mdm.common.CommonPojo;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.EncryptionUtil;
import com.mdm.core.util.HttpContent;

/**
 * @ClassName: User
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月11日 下午3:06:17
 */
public class UserBasicsInfo extends CommonPojo {
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
	private Integer eMode;//加密方式
	private Integer userChannels;
	// 新增字段，用于待合并表使用
	private String mainId;//主数据id
	private String conflictId;//冲突解决id
	private Integer conflictType;//冲突类型
	
	private Integer phoneNumberConfirmed;//手机认证状态
	
	private Integer bindState;//各业务系统的数据绑定状态
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
	public String getEncryptionMode() {
		return encryptionMode;
	}
	public void setEncryptionMode(String encryptionMode) {
		this.encryptionMode = encryptionMode;
	}
	public Integer getUserChannels() {
		return userChannels;
	}
	public void setUserChannels(Integer userChannels) {
		this.userChannels = userChannels;
	}
	
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public String getConflictId() {
		return conflictId;
	}
	public void setConflictId(String conflictId) {
		this.conflictId = conflictId;
	}	
	public Integer getConflictType() {
		return conflictType;
	}
	public void setConflictType(Integer conflictType) {
		this.conflictType = conflictType;
	}
	
	public Integer getPhoneNumberConfirmed() {
		return phoneNumberConfirmed;
	}
	public void setPhoneNumberConfirmed(Integer phoneNumberConfirmed) {
		this.phoneNumberConfirmed = phoneNumberConfirmed;
	}
	
	public Integer getBindState() {
		return bindState;
	}
	public void setBindState(Integer bindState) {
		this.bindState = bindState;
	}
	
	public Integer geteMode() {
		return eMode;
	}
	public void seteMode(Integer eMode) {
		this.eMode = eMode;
	}
	/**
	 * 初始化用户信息
	 * @Title: init
	 * @Description: TODO
	 * @return
	 * @return: String
	 */
	public String init(String systemId, String sysCode) {
		this.setId(DataUtils.uuid());
		if (this.password == null || this.password.equals(""))
			this.password = EncryptionUtil.getEncryptionByMD5(MdmConstants.DEFAULT_PASSWORD);
//		else
//			this.password = EncryptionUtil.getEncryptionByMD5(this.password);
		this.setCreatedBy(HttpContent.getOperatorId());
		this.systemId = systemId;
		this.systemCode = sysCode; 
		if (this.encryptionMode == null || this.encryptionMode.equals("")){
			this.encryptionMode = "1";
		}
		this.userChannels = 0;
		this.setVersion(System.currentTimeMillis() + "");
		this.setIsDeleted(0);
		this.setCreatedOn(new Date());
		return this.getId();
	}
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserDetailInfoRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.user.request 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月11日 下午3:38:16 
 * @version: V1.0   
 */
package com.mdm.request;

import java.text.SimpleDateFormat;

import com.mdm.common.CommonPojo;

/**
 * @ClassName: UserDetailInfoRequest
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月11日 下午3:38:16
 */
public class UserDetailInfoRequest extends CommonPojo {
	private String userId;
	private String nickName;
	private String birthDay;
	private String registerDate;
	private Integer emailConfirmed;
	private Integer phoneNumberConfirmed;
	private String memberFrom;
	private String description;
	private String areaID;
	private String homeAddress;
	private Integer userNo;
	private String signature;
	private String lastLoginTime;
	private String loginIp;
	private Integer isInternal;
	private String salt;
	private String enteryTime;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * @return the birthDay
	 */
	public String getBirthDay() {
		return this.birthDay;
	}
	/**
	 * @param birthDay the birthDay to set
	 */
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	/**
	 * @return the registerDate
	 */
	public String getRegisterDate() {
		return registerDate;
	}
	/**
	 * @param registerDate the registerDate to set
	 */
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	/**
	 * @return the emailConfirmed
	 */
	public Integer getEmailConfirmed() {
		return emailConfirmed;
	}
	/**
	 * @param emailConfirmed the emailConfirmed to set
	 */
	public void setEmailConfirmed(Integer emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}
	/**
	 * @return the phoneNumberConfirmed
	 */
	public Integer getPhoneNumberConfirmed() {
		return phoneNumberConfirmed;
	}
	/**
	 * @param phoneNumberConfirmed the phoneNumberConfirmed to set
	 */
	public void setPhoneNumberConfirmed(Integer phoneNumberConfirmed) {
		this.phoneNumberConfirmed = phoneNumberConfirmed;
	}
	/**
	 * @return the memberFrom
	 */
	public String getMemberFrom() {
		return memberFrom;
	}
	/**
	 * @param memberFrom the memberFrom to set
	 */
	public void setMemberFrom(String memberFrom) {
		this.memberFrom = memberFrom;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the areaID
	 */
	public String getAreaID() {
		return areaID;
	}
	/**
	 * @param areaID the areaID to set
	 */
	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}
	/**
	 * @return the homeAddress
	 */
	public String getHomeAddress() {
		return homeAddress;
	}
	/**
	 * @param homeAddress the homeAddress to set
	 */
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	/**
	 * @return the userNo
	 */
	public Integer getUserNo() {
		return userNo;
	}
	/**
	 * @param userNo the userNo to set
	 */
	public void setUserNo(Integer userNo) {
		this.userNo = userNo;
	}
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}
	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	/**
	 * @return the lastLoginTime
	 */
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	/**
	 * @param lastLoginTime the lastLoginTime to set
	 */
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	/**
	 * @return the loginIp
	 */
	public String getLoginIp() {
		return loginIp;
	}
	/**
	 * @param loginIp the loginIp to set
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	/**
	 * @return the isInternal
	 */
	public Integer getIsInternal() {
		return isInternal;
	}
	/**
	 * @param isInternal the isInternal to set
	 */
	public void setIsInternal(Integer isInternal) {
		this.isInternal = isInternal;
	}
	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	/**
	 * @return the enteryTime
	 */
	public String getEnteryTime() {
		return enteryTime;
	}
	/**
	 * @param enteryTime the enteryTime to set
	 */
	public void setEnteryTime(String enteryTime) {
		this.enteryTime = enteryTime;
	}
}

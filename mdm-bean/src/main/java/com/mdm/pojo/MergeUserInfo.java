/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MergeUserInfo.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月24日 下午2:23:04 
 * @version: V1.0   
 */
package com.mdm.pojo;

import java.util.Date;

/** 
 * @ClassName: MergeUserInfo 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月24日 下午2:23:04  
 */
public class MergeUserInfo {
	
	 	private String id;

	    private String account;

	    private String password;

	    private String customerCode;

	    private String username;

	    private Byte status;

	    private Integer type;

	    private String cellPhone;

	    private String email;

	    private String corpCode;

	    private Byte sex;

	    private String phoneNumber;

	    private String weChatID;

	    private String systemId;

	    private Date createdOn;

	    private String createdBy;

	    private Date modifiedOn;

	    private String modifiedBy;

	    private Byte isDeleted;
	    
	    private String nickName;

	    private Date birthDay;

	    private Date registerDate;

	    private Boolean emailConfirmed;

	    private Byte phoneNumberConfirmed;

	    private String memberFrom;

	    private String description;

	    private String areaID;

	    private String homeAddress;

	    private Integer userNo;

	    private String signature;

	    private Date lastLoginTime;

	    private String loginIp;

	    private Boolean isInternal;

	    private String salt;

	    private Date enteryTime;
	    
		private String tmpIdArray;
			    
		public String getTmpIdArray() {
			return tmpIdArray;
		}

		
		public void setTmpIdArray(String tmpIdArray) {
			this.tmpIdArray = tmpIdArray;
		}

		public String getId() {
	        return id;
	    }

	    public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public Date getBirthDay() {
			return birthDay;
		}

		public void setBirthDay(Date birthDay) {
			this.birthDay = birthDay;
		}

		public Date getRegisterDate() {
			return registerDate;
		}

		public void setRegisterDate(Date registerDate) {
			this.registerDate = registerDate;
		}

		public Boolean getEmailConfirmed() {
			return emailConfirmed;
		}

		public void setEmailConfirmed(Boolean emailConfirmed) {
			this.emailConfirmed = emailConfirmed;
		}

		public Byte getPhoneNumberConfirmed() {
			return phoneNumberConfirmed;
		}

		public void setPhoneNumberConfirmed(Byte phoneNumberConfirmed) {
			this.phoneNumberConfirmed = phoneNumberConfirmed;
		}

		public String getMemberFrom() {
			return memberFrom;
		}

		public void setMemberFrom(String memberFrom) {
			this.memberFrom = memberFrom;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getAreaID() {
			return areaID;
		}

		public void setAreaID(String areaID) {
			this.areaID = areaID;
		}

		public String getHomeAddress() {
			return homeAddress;
		}

		public void setHomeAddress(String homeAddress) {
			this.homeAddress = homeAddress;
		}

		public Integer getUserNo() {
			return userNo;
		}

		public void setUserNo(Integer userNo) {
			this.userNo = userNo;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

		public Date getLastLoginTime() {
			return lastLoginTime;
		}

		public void setLastLoginTime(Date lastLoginTime) {
			this.lastLoginTime = lastLoginTime;
		}

		public String getLoginIp() {
			return loginIp;
		}

		public void setLoginIp(String loginIp) {
			this.loginIp = loginIp;
		}

		public Boolean getIsInternal() {
			return isInternal;
		}

		public void setIsInternal(Boolean isInternal) {
			this.isInternal = isInternal;
		}

		public String getSalt() {
			return salt;
		}

		public void setSalt(String salt) {
			this.salt = salt;
		}

		public Date getEnteryTime() {
			return enteryTime;
		}

		public void setEnteryTime(Date enteryTime) {
			this.enteryTime = enteryTime;
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

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getCustomerCode() {
	        return customerCode;
	    }

	    public void setCustomerCode(String customerCode) {
	        this.customerCode = customerCode;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public Byte getStatus() {
	        return status;
	    }

	    public void setStatus(Byte status) {
	        this.status = status;
	    }

	    public Integer getType() {
	        return type;
	    }

	    public void setType(Integer type) {
	        this.type = type;
	    }

	    public String getCellPhone() {
	        return cellPhone;
	    }

	    public void setCellPhone(String cellPhone) {
	        this.cellPhone = cellPhone;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getCorpCode() {
	        return corpCode;
	    }

	    public void setCorpCode(String corpCode) {
	        this.corpCode = corpCode;
	    }

	    public Byte getSex() {
	        return sex;
	    }

	    public void setSex(Byte sex) {
	        this.sex = sex;
	    }

	    public String getPhoneNumber() {
	        return phoneNumber;
	    }

	    public void setPhoneNumber(String phoneNumber) {
	        this.phoneNumber = phoneNumber;
	    }

	    public String getWeChatID() {
	        return weChatID;
	    }

	    public void setWeChatID(String weChatID) {
	        this.weChatID = weChatID;
	    }

	    public String getSystemId() {
	        return systemId;
	    }

	    public void setSystemId(String systemId) {
	        this.systemId = systemId;
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
	
	
}

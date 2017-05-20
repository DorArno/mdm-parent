/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.user.request 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月11日 下午3:06:50 
 * @version: V1.0   
 */
package com.mdm.request;

/**
 * @ClassName: UserRequest
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月11日 下午3:06:50
 */
public class UserGridRequest {
	//add by hyz begin
	private long lastMaxPk;//前一次查询最大的pk
	private long lastMinPk;//前一次查询最小的pk
	private int lastPageNum;//前一次页码
	private String direction;//翻页方向，正向 or 逆向
	private int from;
	//add by hyz end
	
	
	private String id;
	private String organizationId;
	private String name;
	private String account;
	private String orgType;
	private Integer pageNum; // 当前页码
	private Integer pageSize; // 每页显示数量
	private String userId;
	private String cellPhone;
	private String sex;
	private String status;
	private String systemId;	//来源系统
	private Integer userChannels;
	
	private String beginDate;
	private String endDate;
	
	private String corpCode;
	/**
	 * @return the pageNum
	 */
	public Integer getPageNum() {
		return pageNum;
	}
	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
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
	 * @return the orgType
	 */
	public String getOrgType() {
		return orgType;
	}
	/**
	 * @param orgType the orgType to set
	 */
	public void setOrgType(String orgType) {
		this.orgType = orgType;
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
	 * @return the organizationId
	 */
	public String getOrganizationId() {
		return organizationId;
	}
	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getUserChannels() {
		return userChannels;
	}
	public void setUserChannels(Integer userChannels) {
		this.userChannels = userChannels;
	}

	
	public Integer getLastPageNum() {
		return lastPageNum;
	}
	public void setLastPageNum(Integer lastPageNum) {
		this.lastPageNum = lastPageNum;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Long getLastMaxPk() {
		return lastMaxPk;
	}
	public void setLastMaxPk(Long lastMaxPk) {
		this.lastMaxPk = lastMaxPk;
	}
	public Long getLastMinPk() {
		return lastMinPk;
	}
	public void setLastMinPk(Long lastMinPk) {
		this.lastMinPk = lastMinPk;
	}
	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	
}

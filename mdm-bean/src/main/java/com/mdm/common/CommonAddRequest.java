/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: CommonAddRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.common 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年9月27日 下午5:21:17 
 * @version: V1.0   
 */
package com.mdm.common;

/** 
 * @ClassName: CommonAddRequest 
 * @Description: TODO
 * @author: gaod003
 * @date: 2016年9月27日 下午5:21:17  
 */
public class CommonAddRequest {
	
	private String id;
	private String createdOn;
	private String createdBy;
	private Integer isDeleted;
	
	//来源系统
	private String sourceSystemId;
	
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
	/**
	 * @return the isDeleted
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}
	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getSourceSystemId() {
		return sourceSystemId;
	}
	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}
	
	
}

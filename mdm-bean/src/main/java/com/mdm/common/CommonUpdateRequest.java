/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: CommonUpdateRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.common 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年9月27日 下午5:13:48 
 * @version: V1.0   
 */
package com.mdm.common;

/** 
 * @ClassName: CommonUpdateRequest 
 * @Description: TODO
 * @author: gaod003
 * @date: 2016年9月27日 下午5:13:48  
 */
public class CommonUpdateRequest {
	
	private String id;
	private String modifiedOn;
	private String modifiedBy;
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
	 * @return the modifiedOn
	 */
	public String getModifiedOn() {
		return modifiedOn;
	}
	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
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

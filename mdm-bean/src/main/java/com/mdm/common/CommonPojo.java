/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: CommonBean.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.common 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年9月27日 上午10:00:42 
 * @version: V1.0   
 */
package com.mdm.common;

import java.util.Date;
import java.util.List;

import com.mdm.core.util.DataUtils;
import com.mdm.core.util.HttpContent;
import com.mdm.extend.request.DataExtendRequest;

/**
 * @ClassName: CommonBean
 * @Description: TODO
 * @author: gaod003
 * @date: 2016年9月27日 上午10:00:42
 */
public class CommonPojo {
	private String id;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Integer isDeleted;
	private String version;
	private String oldVersion;
	// 来源系统
	private String sourceSystemId;
	/**
	 * 扩展字段
	 */
	private List<DataExtendRequest> exts;

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
	public Date getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
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
	 * @return the modifiedOn
	 */
	public Date getModifiedOn() {
		return modifiedOn;
	}
	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn) {
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
	public List<DataExtendRequest> getExts() {
		return exts;
	}
	public void setExts(List<DataExtendRequest> exts) {
		this.exts = exts;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * 初始化（新增）
	 * @Title: init
	 * @Description: TODO
	 * @return
	 * @return: String
	 */
	public String init() {
		this.setId(DataUtils.uuid());
		this.setCreatedBy(HttpContent.getOperatorId());
		this.setVersion(System.currentTimeMillis() + "");
		return this.id;
	}
	/**
	 * 初始化（修改）
	 * @Title: initModify
	 * @Description: TODO
	 * @return: void
	 */
	public void initModify() {
		this.setOldVersion(this.getVersion());
		this.setVersion(System.currentTimeMillis() + "");
		if(HttpContent.getSysCode().equals("MDM")){
			this.setModifiedOn(new Date());
			this.setModifiedBy(HttpContent.getOperatorId());
		}
		//this.setModifiedBy(HttpContent.getOperatorId());
	}
	/**
	 * @return the oldVersion
	 */
	public String getOldVersion() {
		return oldVersion;
	}
	/**
	 * @param oldVersion the oldVersion to set
	 */
	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
	}
}

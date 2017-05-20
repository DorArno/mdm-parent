/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserMergeRela.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年12月6日 下午5:20:11 
 * @version: V1.0   
 */
package com.mdm.pojo;

import java.util.Date;

/**
 * @ClassName: UserMergeRela
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年12月6日 下午5:20:11
 */
public class UserMergeRela {

	private String id;

	private String userId;

	private String extraUserId;

	private Date startTime;

	private Date endTime;

	private Date createOn;

	private String createBy;

	private Date modifiedOn;

	private String modifiedBy;

	private int isDeleted;

	private String extraSystemId;

	public String getExtraSystemId() {
		return extraSystemId;
	}

	public void setExtraSystemId(String extraSystemId) {
		this.extraSystemId = extraSystemId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExtraUserId() {
		return extraUserId;
	}

	public void setExtraUserId(String extraUserId) {
		this.extraUserId = extraUserId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: AppSession.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.user.pojo 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月12日 下午3:49:02 
 * @version: V1.0   
 */
package com.mdm.pojo;

import org.elasticsearch.search.aggregations.support.format.ValueFormat.DateTime;

import com.mdm.common.CommonPojo;

/**
 * @ClassName: AppSession
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月12日 下午3:49:02
 */
public class AppSession extends CommonPojo {
	private String userId;
	private String sessionKey;
	private DateTime startTime;
	private DateTime endTime;
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
	 * @return the sessionKey
	 */
	public String getSessionKey() {
		return sessionKey;
	}
	/**
	 * @param sessionKey the sessionKey to set
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	/**
	 * @return the startTime
	 */
	public DateTime getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public DateTime getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
}

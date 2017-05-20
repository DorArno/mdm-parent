/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserChannel.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月25日 下午5:08:17 
 * @version: V1.0   
 */
package com.mdm.pojo;

import java.util.Date;

import com.mdm.common.CommonPojo;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.HttpContent;

/**
 * @ClassName: UserChannel
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月25日 下午5:08:17
 */
public class UserChannel extends CommonPojo {
	private String userId;
	private String channelCode;

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
	 * @return the channelCode
	 */
	public String getChannelCode() {
		return channelCode;
	}
	/**
	 * @param channelCode the channelCode to set
	 */
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	/**
	 * 初始化用户渠道
	 * @Title: init
	 * @Description: TODO
	 * @param userId
	 * @return: void
	 */
	public void init(String userId) {
		this.setId(DataUtils.uuid());
		this.userId = userId;
		this.setCreatedBy(HttpContent.getOperatorId());
		this.setIsDeleted(0);
		this.setCreatedOn(new Date());
	}
}

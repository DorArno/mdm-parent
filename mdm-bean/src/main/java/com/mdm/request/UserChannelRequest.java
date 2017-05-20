/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserChannelRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.request 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月25日 下午5:11:22 
 * @version: V1.0   
 */
package com.mdm.request;

import com.mdm.common.CommonPojo;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.HttpContent;

/**
 * @ClassName: UserChannelRequest
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月25日 下午5:11:22
 */
public class UserChannelRequest extends CommonPojo {
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
	public void init(String userId) {
		this.setId(DataUtils.uuid());
		this.userId = userId;
		this.setCreatedBy(HttpContent.getOperatorId());
	}
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserInfoRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.user.request 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月12日 上午10:16:38 
 * @version: V1.0   
 */
package com.mdm.request;

import java.util.List;

import com.mdm.common.CommonPojo;
import com.mdm.pojo.UserChannel;
import com.mdm.pojo.UserOrganizationRole;

/**
 * @ClassName: UserInfoRequest
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月12日 上午10:16:38
 */
public class UserInfoSFRequest {
	private UserBasicsInfoRequest userBasicsInfoRequest;
	private UserDetailInfoRequest userDetailInfoRequest;
	private List<UserChannel> userChannel;

	/**
	 * @return the userBasicsInfoRequest
	 */
	public UserBasicsInfoRequest getUserBasicsInfoRequest() {
		return userBasicsInfoRequest;
	}
	/**
	 * @param userBasicsInfoRequest the userBasicsInfoRequest to set
	 */
	public void setUserBasicsInfoRequest(UserBasicsInfoRequest userBasicsInfoRequest) {
		this.userBasicsInfoRequest = userBasicsInfoRequest;
	}
	/**
	 * @return the userDetailInfoRequest
	 */
	public UserDetailInfoRequest getUserDetailInfoRequest() {
		return userDetailInfoRequest;
	}
	/**
	 * @param userDetailInfoRequest the userDetailInfoRequest to set
	 */
	public void setUserDetailInfoRequest(UserDetailInfoRequest userDetailInfoRequest) {
		this.userDetailInfoRequest = userDetailInfoRequest;
	}
	/**
	 * @return the userChannel
	 */
	public List<UserChannel> getUserChannel() {
		return userChannel;
	}
	/**
	 * @param userChannel the userChannel to set
	 */
	public void setUserChannel(List<UserChannel> userChannel) {
		this.userChannel = userChannel;
	}
}

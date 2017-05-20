/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserInfoMqResponse.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.response 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年11月17日 上午11:26:43 
 * @version: V1.0   
 */
package com.mdm.response;

import java.util.List;

import com.mdm.pojo.UserBasicsInfo;
import com.mdm.pojo.UserChannel;
import com.mdm.pojo.UserDetailInfo;

/**
 * @ClassName: UserInfoMqResponse
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年11月17日 上午11:26:43
 */
public class UserInfoMqResponse {
	private String id;
	private UserBasicsInfo userBasicsInfoRequest;
	private UserDetailInfo userDetailInfoRequest;
	private List<UserChannel> userChannel;

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
	
	public UserBasicsInfo getUserBasicsInfoRequest() {
		return userBasicsInfoRequest;
	}
	public void setUserBasicsInfoRequest(UserBasicsInfo userBasicsInfoRequest) {
		this.userBasicsInfoRequest = userBasicsInfoRequest;
	}
	public UserDetailInfo getUserDetailInfoRequest() {
		return userDetailInfoRequest;
	}
	public void setUserDetailInfoRequest(UserDetailInfo userDetailInfoRequest) {
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
	public void init(UserBasicsInfo userBasicsInfo, UserDetailInfo userDetailInfo, List<UserChannel> userChannel) {
		this.id = userBasicsInfo.getId();
		this.userBasicsInfoRequest = userBasicsInfo;
		this.userDetailInfoRequest = userDetailInfo;
		this.userChannel = userChannel;
	}
}

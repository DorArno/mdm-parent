/**   
 * Copyright 漏 2016 Arvato. All rights reserved.
 * 
 * @Title: UserInfoResponse.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.user.response 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月�12日 9:47:09 
 * @version: V1.0   
 */
package com.mdm.response;

import java.util.List;

import com.mdm.pojo.UserBasicsInfo;
import com.mdm.pojo.UserChannel;
import com.mdm.pojo.UserDetailInfo;
import com.mdm.pojo.UserOrganizationRole;

/**
 * @ClassName: UserInfoResponse
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月12日 9:47:09
 */
public class UserInfoResponse {
	private String id;
	private String version;
	private UserBasicsInfo userBasicsInfo;
	private UserDetailInfo userDetailInfo;
	private List<UserChannel> userChannel;
	private List<UserOrganizationRole> userOrganizationRole;

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
	 * @return the userBasicsInfo
	 */
	public UserBasicsInfo getUserBasicsInfo() {
		return userBasicsInfo;
	}
	/**
	 * @param userBasicsInfo the userBasicsInfo to set
	 */
	public void setUserBasicsInfo(UserBasicsInfo userBasicsInfo) {
		this.userBasicsInfo = userBasicsInfo;
	}
	/**
	 * @return the userDetailInfo
	 */
	public UserDetailInfo getUserDetailInfo() {
		return userDetailInfo;
	}
	/**
	 * @param userDetailInfo the userDetailInfo to set
	 */
	public void setUserDetailInfo(UserDetailInfo userDetailInfo) {
		this.userDetailInfo = userDetailInfo;
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
	/**
	 * @return the userRole
	 */
	public List<UserOrganizationRole> getUserOrganizationRole() {
		return userOrganizationRole;
	}
	/**
	 * @param userRole the userRole to set
	 */
	public void setUserOrganizationRole(List<UserOrganizationRole> userOrganizationRole) {
		this.userOrganizationRole = userOrganizationRole;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void init(UserBasicsInfo userBasicsInfo, UserDetailInfo userDetailInfo, List<UserChannel> userChannel,
			List<UserOrganizationRole> userOrganizationRole) {
		this.id = null != userBasicsInfo ? userBasicsInfo.getId() : null;
		this.version = null != userBasicsInfo ? userBasicsInfo.getVersion() : null;
		this.userBasicsInfo = userBasicsInfo;
		this.userDetailInfo = userDetailInfo;
		this.userChannel = userChannel;
		this.userOrganizationRole = userOrganizationRole;
	}
}

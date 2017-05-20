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
import java.util.Map;

import com.mdm.pojo.UserChannel;
import com.mdm.request.UserBasicsInfoRequest;
import com.mdm.request.UserDetailInfoRequest;

/**
 * @ClassName: UserInfoResponse
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月12日 9:47:09
 */
public class UserInfoEditResponse {
	private UserBasicsInfoRequest userBasicsInfoRequest;
	private UserDetailInfoRequest userDetailInfoRequest;
	private List<UserChannel> userChannel;
	private List<TreeResponse> treeResponse;
	private String UserType;
	private String areaName;
	// private List<UserOrganizationRole> userOrganizationRole;
	private List<Map<String, Object>> sysBindStates;
	
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
	 * @return the treeResponse
	 */
	public List<TreeResponse> getTreeResponse() {
		return treeResponse;
	}
	/**
	 * @param treeResponse the treeResponse to set
	 */
	public void setTreeResponse(List<TreeResponse> treeResponse) {
		this.treeResponse = treeResponse;
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
	 * @return the userType
	 */
	public String getUserType() {
		return UserType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		if (UserType == null)
			UserType = userType;
		else
			UserType += "," + userType;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public List<Map<String, Object>> getSysBindStates() {
		return sysBindStates;
	}
	public void setSysBindStates(List<Map<String, Object>> sysBindStates) {
		this.sysBindStates = sysBindStates;
	}
	
	// /**
	// * @return the userRole
	// */
	// public List<UserOrganizationRole> getUserOrganizationRole() {
	// return userOrganizationRole;
	// }
	// /**
	// * @param userRole the userRole to set
	// */
	// public void setUserOrganizationRole(List<UserOrganizationRole> userOrganizationRole) {
	// this.userOrganizationRole = userOrganizationRole;
	// }
}

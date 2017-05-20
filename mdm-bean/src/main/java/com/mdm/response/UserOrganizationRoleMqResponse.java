/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserOrganizationRoleMqResponse.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.response 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年11月17日 上午11:27:59 
 * @version: V1.0   
 */
package com.mdm.response;

import java.util.List;

import com.mdm.pojo.UserOrganizationRole;
import com.mdm.request.UserInfoRequest;

/**
 * @ClassName: UserOrganizationRoleMqResponse
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年11月17日 上午11:27:59
 */
public class UserOrganizationRoleMqResponse {
	private String id;
	private String corpCode;
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
	
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	/**
	 * @return the userOrganizationRole
	 */
	public List<UserOrganizationRole> getUserOrganizationRole() {
		return userOrganizationRole;
	}
	/**
	 * @param userOrganizationRole the userOrganizationRole to set
	 */
	public void setUserOrganizationRole(List<UserOrganizationRole> userOrganizationRole) {
		this.userOrganizationRole = userOrganizationRole;
	}
	/**
	 * 初始化mq返回用户组织机构角色关联数据
	 * @Title: init
	 * @Description: TODO
	 * @param userInfoRequest
	 * @return: void
	 */
	public void init(String userId, List<UserOrganizationRole> userOrganizationRole, String corpCode) {
		this.id = userId;
		this.corpCode = corpCode;
		this.userOrganizationRole = userOrganizationRole;
	}
}

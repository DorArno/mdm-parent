/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年12月9日 下午4:54:16 *
**/ 
package com.mdm.request;

import com.mdm.common.RequestBean;

public class UserLogoutRequest extends RequestBean {

	private String userId;
	private String sessionKey;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
}

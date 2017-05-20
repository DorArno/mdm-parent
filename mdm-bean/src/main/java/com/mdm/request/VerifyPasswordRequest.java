package com.mdm.request;


public class VerifyPasswordRequest {
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 密码
	 */
	private String password;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

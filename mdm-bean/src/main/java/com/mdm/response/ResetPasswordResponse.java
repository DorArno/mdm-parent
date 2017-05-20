package com.mdm.response;

/**
 * 重置密码--返回消息体
 * @ClassName: ResetPasswordResponse 
 * @Description: TODO
 * @author: Han
 * @date: 2016年12月28日 上午10:39:50
 */
public class ResetPasswordResponse {
	public ResetPasswordResponse() {
	}
	public ResetPasswordResponse(String id, String version, String newPassword) {
		this.id = id;
		this.version = version;
		this.newPassword = newPassword;
	}

	private String id;
	private String version;
	private String newPassword;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}

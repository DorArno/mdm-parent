package com.mdm.request;

/**
 * 用户解绑手机号
 * @ClassName: UnBindingPhoneRequest
 * @Description: TODO
 * @author: Han
 * @date: 2017年1月4日 下午6:38:18
 */
public class UnBindingPhoneRequest {
	private String userId;
	private String cellPhone;
	private String sysCode;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
}

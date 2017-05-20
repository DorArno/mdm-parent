/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月19日 下午5:46:56 *
**/ 
package com.mdm.request;

import com.mdm.common.RequestBean;

public class UserRegisterRequest extends RequestBean {
	
	private String ip;
	private String captcha;
	private String captchaId;
	private String communityId;
	private String userName;

	private String password;
	private String encryptionMode;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getCaptchaId() {
		return captchaId;
	}
	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEncryptionMode() {
		return encryptionMode;
	}
	public void setEncryptionMode(String encryptionMode1) {
		encryptionMode = encryptionMode1;
	}
	
}

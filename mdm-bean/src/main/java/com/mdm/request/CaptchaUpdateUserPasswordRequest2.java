/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月20日 下午4:07:55 *
@Desc 根据验证码，手机号 新密码修改用户密码
**/ 
package com.mdm.request;

import com.mdm.common.RequestBean;

public class CaptchaUpdateUserPasswordRequest2  extends RequestBean{
	
	private String phone;
	private String captcha;
	private String captchaId;
	private String newPassword;
	private String passwordAgain;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getPasswordAgain() {
		return passwordAgain;
	}
	public void setPasswordAgain(String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}
	 
}

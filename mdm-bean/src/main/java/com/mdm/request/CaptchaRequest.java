/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月17日 下午4:47:14 *
**/ 
package com.mdm.request;

import com.mdm.common.RequestBean;

public class CaptchaRequest extends RequestBean {
	private String phoneNum;
	
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	
}

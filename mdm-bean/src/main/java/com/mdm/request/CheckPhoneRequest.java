/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月19日 下午6:12:48 *
**/ 
package com.mdm.request;

import com.mdm.common.RequestBean;

public class CheckPhoneRequest  extends RequestBean {
	private String phoneNum;
 
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}


}

/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年12月9日 下午4:50:41 *
**/ 
package com.mdm.request;

import com.mdm.common.RequestBean;

public class UserLoginRequest  extends RequestBean{
private String userName;
private String ip;
private String password;
private String imei;

public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getIp() {
	return ip;
}
public void setIp(String ip) {
	this.ip = ip;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getImei() {
	return imei;
}
public void setImei(String imei) {
	this.imei = imei;
}
 

}

/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年11月3日 下午4:24:32 *
**/ 
package com.mdm.response;

public class CommonPojoResponse {
	
	public CommonPojoResponse(){}
	public CommonPojoResponse(String id, String version){
		this.id = id;
		this.version = version;
	}
	
	private String id;
	
	private String version;
	
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
	
}

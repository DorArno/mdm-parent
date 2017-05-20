package com.mdm.response;

/**
 * 
 * @ClassName: UserInsertResponse 
 * @Description: 用于用户新增，执行结果返回
 * @author: Han
 * @date: 2017年3月15日 下午2:45:08
 */
public class UserInsertResponse {
	public UserInsertResponse() {
	}
	public UserInsertResponse(String id, String version, String message) {
		this.id = id;
		this.version = version;
		this.message = message;
	}

	private String id;
	private String version;
	private String message;

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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}

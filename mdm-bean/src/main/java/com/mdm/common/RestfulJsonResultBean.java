/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: RestfulJsonResultBean.java
 * @Prject: mdm-bean
 * @Package: com.mdm.common
 * @Description: TODO
 * @author: YANG044
 * @date: 2016年11月15日 下午5:20:51
 * @version: V1.0   
 */
package com.mdm.common;

/**
 * 此Bean用于在Service层调用Restful接口时，将返回的Json串转换为对象
 * 
 * @ClassName: RestfulJsonResultBean
 * @Description: 
 * @author: YANG044
 * @date: 2016年11月15日 下午5:20:51
 */
public class RestfulJsonResultBean {
	private String result;
	private String resCode;
	private RestfulJsonResultDataBean data;
	private String resMsg;
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the resCode
	 */
	public String getResCode() {
		return resCode;
	}
	/**
	 * @param resCode the resCode to set
	 */
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	/**
	 * @return the data
	 */
	public RestfulJsonResultDataBean getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(RestfulJsonResultDataBean data) {
		this.data = data;
	}
	/**
	 * @return the resMsg
	 */
	public String getResMsg() {
		return resMsg;
	}
	/**
	 * @param resMsg the resMsg to set
	 */
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	
}

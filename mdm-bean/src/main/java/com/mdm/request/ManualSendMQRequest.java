/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: ManualSendMQRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.request 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年10月27日 上午11:18:31 
 * @version: V1.0   
 */
package com.mdm.request;

/** 
 * @ClassName: ManualSendMQRequest 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年10月27日 上午11:18:31  
 */
public class ManualSendMQRequest {
	
	//主数据类型
	private Integer dataType;
	
	//主数据主键列表
	private String[] dataIds;
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public String[] getDataIds() {
		return dataIds;
	}
	public void setDataIds(String[] dataIds) {
		this.dataIds = dataIds;
	}
	
}

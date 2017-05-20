/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DeliveryMesssage.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.mq 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年10月10日 下午1:35:40 
 * @version: V1.0   
 */
package com.mdm.mq;

/** 
 * @ClassName: DeliveryMesssage 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年10月10日 下午1:35:40  
 */
public class DeliveryMessage {
	//操作
	private String operation;
	//主数据标识
	private String dataId;
	//时间戳
	private String timeStamp;
	//数据对象
	private Object data;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}	
}

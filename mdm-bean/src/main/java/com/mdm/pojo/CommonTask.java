/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: CommonTask.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年11月3日 下午1:51:05 
 * @version: V1.0   
 */
package com.mdm.pojo;

import com.mdm.common.CommonPojo;

/** 
 * @ClassName: CommonTask 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年11月3日 下午1:51:05  
 */
public class CommonTask extends CommonPojo {
	
	private String dataId;
	
	private Integer dataType;
	
	private Integer taskType;
	
	private Integer executeTime;
	
	private Integer status;

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Integer executeTime) {
		this.executeTime = executeTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}

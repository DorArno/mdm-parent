/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: JobInfo.java 
 * @Prject: quartz
 * @Package: com.arvato.quartz.bean 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年9月27日 上午10:43:43 
 * @version: V1.0   
 */
package com.mdm.pojo;

/** 
 * @ClassName: JobInfo 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年9月27日 上午10:43:43  
 */
public class JobInfo {
	
	//任务组名称
	private String jobGroup;
	
	//任务名称
	private String jobName;
	
	//参数名称
	private String[] argsNames;
	
	//参数值
	private String[] argsValues;
	
	//触发器类型
	private String triggerType;
	
	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String[] getArgsNames() {
		return argsNames;
	}

	public void setArgsNames(String[] argsNames) {
		this.argsNames = argsNames;
	}

	public String[] getArgsValues() {
		return argsValues;
	}

	public void setArgsValues(String[] argsValues) {
		this.argsValues = argsValues;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: Triggers.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月8日 上午11:12:54 
 * @version: V1.0   
 */
package com.mdm.pojo;

import java.util.Map;

/**
 * @ClassName: Triggers
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月8日 上午11:12:54
 */
public class Triggers {
	private String cronExpression;
	private String expressionSummary;
	private String fullJobName;
	private String fullName;
	private String group;
	private Map jobDataMap;
	private String jobGroup;
	private JobKey jobKey;
	private String jobName;
	private int misfireInstruction;
	private String name;
	private String nextFireTime;
	private String previousFireTime;
	private int priority;
	//@JsonIgnoreProperties(value={"scheduleBuilder"})
	//private ScheduleBuilder scheduleBuilder;
	private String startTime;
	//@JsonIgnoreProperties(value={"triggerBuilder"})
	//private TriggerBuilder triggerBuilder;

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getCronExpression() {
		return this.cronExpression;
	}
	public void setExpressionSummary(String expressionSummary) {
		this.expressionSummary = expressionSummary;
	}
	public String getExpressionSummary() {
		return this.expressionSummary;
	}
	public void setFullJobName(String fullJobName) {
		this.fullJobName = fullJobName;
	}
	public String getFullJobName() {
		return this.fullJobName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getFullName() {
		return this.fullName;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getGroup() {
		return this.group;
	}
	public void setJobDataMap(Map jobDataMap) {
		this.jobDataMap = jobDataMap;
	}
	public Map getJobDataMap() {
		return this.jobDataMap;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getJobGroup() {
		return this.jobGroup;
	}
	public void setJobKey(JobKey jobKey) {
		this.jobKey = jobKey;
	}
	public JobKey getJobKey() {
		return this.jobKey;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobName() {
		return this.jobName;
	}
	public void setMisfireInstruction(int misfireInstruction) {
		this.misfireInstruction = misfireInstruction;
	}
	public int getMisfireInstruction() {
		return this.misfireInstruction;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public void setNextFireTime(String nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public String getNextFireTime() {
		return this.nextFireTime;
	}
	public void setPreviousFireTime(String previousFireTime) {
		this.previousFireTime = previousFireTime;
	}
	public String getPreviousFireTime() {
		return this.previousFireTime;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getPriority() {
		return this.priority;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStartTime() {
		return this.startTime;
	}

}

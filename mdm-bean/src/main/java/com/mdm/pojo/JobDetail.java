/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: JobDetail.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月8日 上午11:06:04 
 * @version: V1.0   
 */
package com.mdm.pojo;

import java.util.Map;

/**
 * @ClassName: JobDetail
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月8日 上午11:06:04
 */
public class JobDetail {

	private boolean concurrentExectionDisallowed;

	private boolean durable;

	private String fullName;

	private String group;

	private JobBuilder jobBuilder;

	private String jobClass;

	private Map jobDataMap;

	private String name;

	private boolean persistJobDataAfterExecution;

	public void setConcurrentExectionDisallowed(boolean concurrentExectionDisallowed) {
		this.concurrentExectionDisallowed = concurrentExectionDisallowed;
	}

	public boolean getConcurrentExectionDisallowed() {
		return this.concurrentExectionDisallowed;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public boolean getDurable() {
		return this.durable;
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

	public void setJobBuilder(JobBuilder jobBuilder) {
		this.jobBuilder = jobBuilder;
	}

	public JobBuilder getJobBuilder() {
		return this.jobBuilder;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	public String getJobClass() {
		return this.jobClass;
	}

	public void setJobDataMap(Map jobDataMap) {
		this.jobDataMap = jobDataMap;
	}

	public Map getJobDataMap() {
		return this.jobDataMap;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPersistJobDataAfterExecution(boolean persistJobDataAfterExecution) {
		this.persistJobDataAfterExecution = persistJobDataAfterExecution;
	}

	public boolean getPersistJobDataAfterExecution() {
		return this.persistJobDataAfterExecution;
	}
}

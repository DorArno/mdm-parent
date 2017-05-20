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
public class CronJobInfo extends JobInfo{
	
	private String cronExpression;
	
	private String secondField;
	
	private String minutesField;
	
	private String hourField;
	
	private String dayField;
	
	private String monthField;
	
	private String weekField;
	
	private String method;
	
	private String url;
	
	private String argNames;
	
	private String argValues;
	
	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getArgNames() {
		return argNames;
	}

	public void setArgNames(String argNames) {
		this.argNames = argNames;
	}

	public String getArgValues() {
		return argValues;
	}

	public void setArgValues(String argValues) {
		this.argValues = argValues;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSecondField() {
		return secondField;
	}

	public void setSecondField(String secondField) {
		this.secondField = secondField;
	}

	public String getMinutesField() {
		return minutesField;
	}

	public void setMinutesField(String minutesField) {
		this.minutesField = minutesField;
	}

	public String getHourField() {
		return hourField;
	}

	public void setHourField(String hourField) {
		this.hourField = hourField;
	}

	public String getDayField() {
		return dayField;
	}

	public void setDayField(String dayField) {
		this.dayField = dayField;
	}

	public String getMonthField() {
		return monthField;
	}

	public void setMonthField(String monthField) {
		this.monthField = monthField;
	}

	public String getWeekField() {
		return weekField;
	}

	public void setWeekField(String weekField) {
		this.weekField = weekField;
	}
	
	
		
}

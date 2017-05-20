/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: Data.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月8日 上午11:13:23 
 * @version: V1.0   
 */
package com.mdm.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @ClassName: Data
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月8日 上午11:13:23
 */
public class Data implements Comparable<Data> {

	private String status;

	private JobDetail jobDetail;

	private List<Triggers> triggers;

	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public JobDetail getJobDetail() {
		return this.jobDetail;
	}

	public void setTriggers(List<Triggers> triggers) {
		this.triggers = triggers;
	}

	public List<Triggers> getTriggers() {
		return this.triggers;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/*
	 * (non Javadoc)
	 * @Title: compareTo
	 * @Description: TODO
	 * @param arg0
	 * @return
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Data arg0) {
		if (arg0 instanceof Data) {
			Calendar calendar = Calendar.getInstance();
			Calendar calendar1 = Calendar.getInstance();
			try {
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.getTriggers().get(0).getStartTime()));
				calendar1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(((Data) arg0).getTriggers().get(0).getStartTime()));
				if (calendar.after(calendar1)) {
					return -1;
				}
				return 1;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: JobKey.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月8日 上午11:11:54 
 * @version: V1.0   
 */
package com.mdm.pojo;

/**
 * @ClassName: JobKey
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月8日 上午11:11:54
 */
public class JobKey {
	private String group;
	private String name;

	public void setGroup(String group) {
		this.group = group;
	}
	public String getGroup() {
		return this.group;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
}

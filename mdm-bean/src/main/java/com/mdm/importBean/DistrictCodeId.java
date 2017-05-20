/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DistrictCodeId.java
 * @Prject: mdm-bean
 * @Package: com.mdm.importBean
 * @Description: TODO
 * @author: YANG044
 * @date: 2016年11月1日 上午10:50:49
 * @version: V1.0   
 */
package com.mdm.importBean;

/**
 * @ClassName: DistrictCodeId
 * @Description: 
 * @author: YANG044
 * @date: 2016年11月1日 上午10:50:49
 */
public class DistrictCodeId {
	private String code;
	private String Id;
	private String name;
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DistrictRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.district.request 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年9月27日 下午5:26:04 
 * @version: V1.0   
 */
package com.mdm.request;

import java.math.BigDecimal;

import com.mdm.common.BaseBean;
import com.mdm.common.CommonPojo;

/**
 * @ClassName: DistrictRequest
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年9月27日 下午5:26:04
 */
public class DistrictRequest extends CommonPojo {
	private String parentId;
	private String code;
	private String name;
	private String postCode;
	private BigDecimal longitude;
	private BigDecimal latitude;
	private Integer type;
	private Integer level;
	private String description;

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}
	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	/**
	 * @return the longitude
	 */
	public BigDecimal getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public BigDecimal getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}

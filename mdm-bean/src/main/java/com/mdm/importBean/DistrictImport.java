/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DistrictImport.java
 * @Prject: mdm-bean
 * @Package: com.mdm.importBean
 * @Description: TODO
 * @author: YANG044
 * @date: 2016年10月28日 下午3:14:05
 * @version: V1.0   
 */
package com.mdm.importBean;

import java.math.BigDecimal;

import com.mdm.core.util.ExcelMyAnnotation;

/**
 * 导入行政区划所用的Bean
 * 
 * @ClassName: DistrictImport
 * @Description: 
 * @author: YANG044
 * @date: 2016年10月28日 下午3:14:05
 */
public class DistrictImport {
	
	private String id;
	
	private String parentId;
	
	@ExcelMyAnnotation(isRequired = true, order = 0, name = "编码", isRepeat = true, isNum = true)
	private String code;
	
	@ExcelMyAnnotation(isRequired = false, order = 1, name = "上级行政区划编码", isNum = true)
	private String parentCode;
	
	@ExcelMyAnnotation(isRequired = true, order = 2, name = "名称")
	private String name;
	
	@ExcelMyAnnotation(isRequired = false, order = 3, name = "邮政编码", isNum = true)
	private String postCode;
	
	@ExcelMyAnnotation(isRequired = false, order = 4, name = "经度")
	private BigDecimal longitude;
	
	@ExcelMyAnnotation(isRequired = false, order = 5, name = "纬度")
	private BigDecimal latitude;
	
//	@ExcelMyAnnotation(isRequired = true, order = 6, name = "类型")
//	private Integer type;
	
//	@ExcelMyAnnotation(isRequired = true, order = 7, name = "层级")
//	private Integer level;
	
	@ExcelMyAnnotation(isRequired = false, order = 6, name = "描述")
	private String description;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

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
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
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

//	/**
//	 * @return the type
//	 */
//	public Integer getType() {
//		return type;
//	}
//
//	/**
//	 * @param type the type to set
//	 */
//	public void setType(Integer type) {
//		this.type = type;
//	}
//
//	/**
//	 * @return the level
//	 */
//	public Integer getLevel() {
//		return level;
//	}
//
//	/**
//	 * @param level the level to set
//	 */
//	public void setLevel(Integer level) {
//		this.level = level;
//	}

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

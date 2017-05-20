/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: OrganizationImport.java
 * @Prject: mdm-bean
 * @Package: com.mdm.importBean
 * @Description: TODO
 * @author: YANG044
 * @date: 2016年11月10日 上午11:02:53
 * @version: V1.0   
 */
package com.mdm.importBean;

import java.math.BigDecimal;

import com.mdm.core.util.ExcelMyAnnotation;

/**
 * @ClassName: OrganizationImport
 * @Description: 
 * @author: YANG044
 * @date: 2016年11月10日 上午11:02:53
 */
public class OrganizationImport {
	private String id;
	private String parentId;
	
	@ExcelMyAnnotation(isRequired = false, order = 0, name = "父级编码", isNum=true)
	private String parentCode;
	
	@ExcelMyAnnotation(isRequired = true, order = 1, name = "编码", isRepeat = true, isNum=true, length = 50)
    private String code;
    
	@ExcelMyAnnotation(isRequired = true, order = 2, name = "类型", isSelect=true, isNum=true)
    private String type;
    
	@ExcelMyAnnotation(isRequired = false, order = 3, name = "企业代码", length = 25)
    private String corpCode;
    
	@ExcelMyAnnotation(isRequired = true, order = 4, name = "名称", length = 50)
    private String name;
    
	@ExcelMyAnnotation(isRequired = true, order = 5, name = "状态", isSelect=true)
    private Integer status;
    
	@ExcelMyAnnotation(isRequired = false, order = 6, name = "联系电话", isNum=true, length = 25)
    private String contactTel;

	@ExcelMyAnnotation(isRequired = true, order = 7, name = "省份")
	private String provinceName;
	@ExcelMyAnnotation(isRequired = true, order = 8, name = "省份Code", isSelect=true, isNum=true)
    private String provinceId;
    
	@ExcelMyAnnotation(isRequired = true, order = 9, name = "城市")
    private String cityName;
	@ExcelMyAnnotation(isRequired = true, order = 10, name = "城市Code", isSelect=true, isNum=true)
	private String cityId;

	@ExcelMyAnnotation(isRequired = true, order = 11, name = "区域")
	private String districtName;
	@ExcelMyAnnotation(isRequired = true, order = 12, name = "区域Code", isSelect=true, isNum=true)
    private String districtID;
    
	@ExcelMyAnnotation(isRequired = false, order = 13, name = "社区", isSelect=true)
    private String communityId;
    
	@ExcelMyAnnotation(isRequired = false, order = 14, name = "详细地址", length = 75)
    private String addrDetails;
    
	@ExcelMyAnnotation(isRequired = false, order = 15, name = "经度")
    private BigDecimal longitude;
    
	@ExcelMyAnnotation(isRequired = false, order = 16, name = "纬度")
    private BigDecimal latitude;
    
	@ExcelMyAnnotation(isRequired = false, order = 17, name = "描述", length = 100)
    private String description;
    
	@ExcelMyAnnotation(isRequired = false, order = 18, name = "机构类型", isSelect=true)
	private String idOrganizationType;
    
	@ExcelMyAnnotation(isRequired = false, order = 19, name = "来源系统", isSelect=true)
    private String systemId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDistrictID() {
		return districtID;
	}

	public void setDistrictID(String districtID) {
		this.districtID = districtID;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getAddrDetails() {
		return addrDetails;
	}

	public void setAddrDetails(String addrDetails) {
		this.addrDetails = addrDetails;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdOrganizationType() {
		return idOrganizationType;
	}

	public void setIdOrganizationType(String idOrganizationType) {
		this.idOrganizationType = idOrganizationType;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}

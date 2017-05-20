/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: OrganazationExport.java
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo
 * @Description: TODO
 * @author: YANG044
 * @date: 2016年11月10日 上午9:31:25
 * @version: V1.0   
 */
package com.mdm.exportBean;

import java.math.BigDecimal;

/**
 * @ClassName: OrganazationExport
 * @Description: 
 * @author: YANG044
 * @date: 2016年11月10日 上午9:31:25
 */
public class OrganizationExport {
	/**
	 * 父级代码
	 */
	private String parentCode;
	/**
	 * 父级名称
	 */
	private String parentName;
	/**
	 * 代码
	 */
    private String code;
    /**
     * 类型
     */
    private String type;
    /**
     * 企业代码
     */
    private String corpCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态
     */
    private Integer status;
    private String statusText;
    /**
     * 联系电话
     */
    private String contactTel;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区域
     */
    private String district;
    /**
     * 社区
     */
    private String community;
    /**
     * 详细地址
     */
    private String addrDetails;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 描述
     */
    private String description;
    /**
     * 机构类型
     */
    private String idOrganizationType;
    /**
     * 来源系统
     */
    private String system;
    
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
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the corpCode
	 */
	public String getCorpCode() {
		return corpCode;
	}
	/**
	 * @param corpCode the corpCode to set
	 */
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
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
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the contactTel
	 */
	public String getContactTel() {
		return contactTel;
	}
	/**
	 * @param contactTel the contactTel to set
	 */
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * @return the community
	 */
	public String getCommunity() {
		return community;
	}
	/**
	 * @param community the community to set
	 */
	public void setCommunity(String community) {
		this.community = community;
	}
	/**
	 * @return the addrDetails
	 */
	public String getAddrDetails() {
		return addrDetails;
	}
	/**
	 * @param addrDetails the addrDetails to set
	 */
	public void setAddrDetails(String addrDetails) {
		this.addrDetails = addrDetails;
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
	/**
	 * @return the idOrganizationType
	 */
	public String getIdOrganizationType() {
		return idOrganizationType;
	}
	/**
	 * @param idOrganizationType the idOrganizationType to set
	 */
	public void setIdOrganizationType(String idOrganizationType) {
		this.idOrganizationType = idOrganizationType;
	}
	/**
	 * @return the system
	 */
	public String getSystem() {
		return system;
	}
	/**
	 * @param system the system to set
	 */
	public void setSystem(String system) {
		this.system = system;
	}
	/**
	 * @return the statusText
	 */
	public String getStatusText() {
		return statusText;
	}
	/**
	 * @param statusText the statusText to set
	 */
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MerchantExport.java
 * @Prject: mdm-bean
 * @Package: com.mdm.exportBean
 * @Description: TODO
 * @author: YANG044
 * @date: 2016年11月29日 下午3:49:23
 * @version: V1.0   
 */
package com.mdm.exportBean;

/**
 * @ClassName: MerchantExport
 * @Description: 
 * @author: YANG044
 * @date: 2016年11月29日 下午3:49:23
 */
public class MerchantExport {
	private String id;
	// 商家代码
	private String code;
	// 商家名称
	private String mName;
	// 负责人
	private String mManager;
	// 商家电话
	private String contactTel;
	//商家服务电话
	private String serviceTel;
	// 商家状态
	private Integer status;
	private String statusText;
	// 来源系统 systemId
	private String system;
	// 所属组织 organizationId
	private String organization;
	// 一级分类 firstCategoryId
	private String firstCategoryId;
	private String firstCategory;
	// 二级分类 secondCategoryId
	private String secondCategoryId;
	private String secondCategory;
	// 省份
	private String province;
	// 城市
	private String city;
	// 区域
	private String distict;
	// 是否夜场
	private Integer hasNight;
	private String hasNightText;
	// 精选商家
	private Integer isHighQualityMerchants;
	private String isHighQualityMerchantsText;
	// 停车位
	private Integer parkingNumber;
	// 营业时间开始
	private String startOperation;
	// 营业时间结束
	private String endOperation;
	// 晚间营业时间开始
	private String nightStart;
	// 晚间营业时间结束
	private String nightEnd;
	// 外部商家
	private Integer isExternal;
	private String isExternalText;
	// 商家地址
	private String address;
	// 商家标题
	private String title;
	// 商家描述
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
	 * @return the mName
	 */
	public String getMName() {
		return mName;
	}
	/**
	 * @param mName the mName to set
	 */
	public void setMName(String mName) {
		this.mName = mName;
	}
	/**
	 * @return the mManager
	 */
	public String getMManager() {
		return mManager;
	}
	/**
	 * @param mManager the mManager to set
	 */
	public void setMManager(String mManager) {
		this.mManager = mManager;
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
	 * @return the organization
	 */
	public String getOrganization() {
		return organization;
	}
	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	/**
	 * @return the firstCategory
	 */
	public String getFirstCategory() {
		return firstCategory;
	}
	/**
	 * @param firstCategory the firstCategory to set
	 */
	public void setFirstCategory(String firstCategory) {
		this.firstCategory = firstCategory;
	}
	/**
	 * @return the secondCategory
	 */
	public String getSecondCategory() {
		return secondCategory;
	}
	/**
	 * @param secondCategory the secondCategory to set
	 */
	public void setSecondCategory(String secondCategory) {
		this.secondCategory = secondCategory;
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
	 * @return the distict
	 */
	public String getDistict() {
		return distict;
	}
	/**
	 * @param distict the distict to set
	 */
	public void setDistict(String distict) {
		this.distict = distict;
	}
	/**
	 * @return the hasNight
	 */
	public Integer getHasNight() {
		return hasNight;
	}
	/**
	 * @param hasNight the hasNight to set
	 */
	public void setHasNight(Integer hasNight) {
		this.hasNight = hasNight;
	}
	/**
	 * @return the isHighQualityMerchants
	 */
	public Integer getIsHighQualityMerchants() {
		return isHighQualityMerchants;
	}
	/**
	 * @param isHighQualityMerchants the isHighQualityMerchants to set
	 */
	public void setIsHighQualityMerchants(Integer isHighQualityMerchants) {
		this.isHighQualityMerchants = isHighQualityMerchants;
	}
	/**
	 * @return the parkingNumber
	 */
	public Integer getParkingNumber() {
		return parkingNumber;
	}
	/**
	 * @param parkingNumber the parkingNumber to set
	 */
	public void setParkingNumber(Integer parkingNumber) {
		this.parkingNumber = parkingNumber;
	}
	/**
	 * @return the startOperation
	 */
	public String getStartOperation() {
		return startOperation;
	}
	/**
	 * @param startOperation the startOperation to set
	 */
	public void setStartOperation(String startOperation) {
		this.startOperation = startOperation;
	}
	/**
	 * @return the endOperation
	 */
	public String getEndOperation() {
		return endOperation;
	}
	/**
	 * @param endOperation the endOperation to set
	 */
	public void setEndOperation(String endOperation) {
		this.endOperation = endOperation;
	}
	/**
	 * @return the nightStart
	 */
	public String getNightStart() {
		return nightStart;
	}
	/**
	 * @param nightStart the nightStart to set
	 */
	public void setNightStart(String nightStart) {
		this.nightStart = nightStart;
	}
	/**
	 * @return the nightEnd
	 */
	public String getNightEnd() {
		return nightEnd;
	}
	/**
	 * @param nightEnd the nightEnd to set
	 */
	public void setNightEnd(String nightEnd) {
		this.nightEnd = nightEnd;
	}
	/**
	 * @return the isExternal
	 */
	public Integer getIsExternal() {
		return isExternal;
	}
	/**
	 * @param isExternal the isExternal to set
	 */
	public void setIsExternal(Integer isExternal) {
		this.isExternal = isExternal;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return the firstCategoryId
	 */
	public String getFirstCategoryId() {
		return firstCategoryId;
	}
	/**
	 * @param firstCategoryId the firstCategoryId to set
	 */
	public void setFirstCategoryId(String firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}
	/**
	 * @return the secondCategoryId
	 */
	public String getSecondCategoryId() {
		return secondCategoryId;
	}
	/**
	 * @param secondCategoryId the secondCategoryId to set
	 */
	public void setSecondCategoryId(String secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
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
	/**
	 * @return the hasNightText
	 */
	public String getHasNightText() {
		return hasNightText;
	}
	/**
	 * @param hasNightText the hasNightText to set
	 */
	public void setHasNightText(String hasNightText) {
		this.hasNightText = hasNightText;
	}
	/**
	 * @return the isHighQualityMerchantsText
	 */
	public String getIsHighQualityMerchantsText() {
		return isHighQualityMerchantsText;
	}
	/**
	 * @param isHighQualityMerchantsText the isHighQualityMerchantsText to set
	 */
	public void setIsHighQualityMerchantsText(String isHighQualityMerchantsText) {
		this.isHighQualityMerchantsText = isHighQualityMerchantsText;
	}
	/**
	 * @return the isExternalText
	 */
	public String getIsExternalText() {
		return isExternalText;
	}
	/**
	 * @param isExternalText the isExternalText to set
	 */
	public void setIsExternalText(String isExternalText) {
		this.isExternalText = isExternalText;
	}
	/**
	 * @return the serviceTel
	 */
	public String getServiceTel() {
		return serviceTel;
	}
	/**
	 * @param serviceTel the serviceTel to set
	 */
	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}
	
//	private Integer type;
//	private Integer levels;
//	private String imageUrl;
//	private String longitude;
//	private String latitude;
//	private Integer idOrganizationType;
//	private String corpID;
//	private String version;
}

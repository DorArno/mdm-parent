/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MerchantImport.java
 * @Prject: mdm-bean
 * @Package: com.mdm.importBean
 * @Description: TODO
 * @author: YANG044
 * @date: 2016年11月30日 下午4:50:35
 * @version: V1.0   
 */
package com.mdm.importBean;

import com.mdm.core.util.ExcelMyAnnotation;

/**
 * @ClassName: MerchantImport
 * @Description: 
 * @author: YANG044
 * @date: 2016年11月30日 下午4:50:35
 */
public class MerchantImport {
	private String id;
	// 商家代码
	@ExcelMyAnnotation(isRequired = true, order = 0, name = "商家编号", isNum=true, length = 50)
	private String code;
	// 商家名称
	@ExcelMyAnnotation(isRequired = true, order = 1, name = "商家名称", length = 50)
	private String mName;
	// 负责人
	@ExcelMyAnnotation(isRequired = true, order = 2, name = "负责人", length = 25)
	private String mManager;
	// 商家电话
	@ExcelMyAnnotation(isRequired = false, order = 3, name = "商家电话", isNum=true, length = 25)
	private String contactTel;
	// 商家服务电话
	@ExcelMyAnnotation(isRequired = false, order = 4, name = "商家服务电话", isNum=true, length = 25)
	private String serviceTel;
	// 商家状态
	@ExcelMyAnnotation(isRequired = true, order = 5, name = "商家状态", isSelect=true)
	private Integer status;
	// 来源系统 systemId
	@ExcelMyAnnotation(isRequired = true, order = 6, name = "来源系统", isSelect=true)
	private String systemId;
	// 所属组织 organizationId
	@ExcelMyAnnotation(isRequired = true, order = 7, name = "所属组织", isSelect=true)
	private String organizationId;
	// 一级分类 firstCategoryId
	@ExcelMyAnnotation(isRequired = true, order = 8, name = "一级分类", isSelect=true)
	private String firstCategoryId;
	// 二级分类 secondCategoryId
	@ExcelMyAnnotation(isRequired = true, order = 9, name = "二级分类", isSelect=true)
	private String secondCategoryId;
	// 省份
	@ExcelMyAnnotation(isRequired = false, order = 10, name = "省份")
	private String provinceName;
	@ExcelMyAnnotation(isRequired = false, order = 11, name = "省份Code", isSelect=true, isNum=true)
	private String province;
	// 城市
	@ExcelMyAnnotation(isRequired = false, order = 12, name = "城市")
	private String cityName;
	@ExcelMyAnnotation(isRequired = false, order = 13, name = "城市Code", isSelect=true, isNum=true)
	private String city;
	// 区域
	@ExcelMyAnnotation(isRequired = false, order = 14, name = "区域")
	private String distictName;
	@ExcelMyAnnotation(isRequired = false, order = 15, name = "区域Code", isSelect=true, isNum=true)
	private String distict;
	// 是否夜场
	//@ExcelMyAnnotation(isRequired = false, order = 13, name = "是否夜场")
	//private Integer hasNight;
	// 精选商家
	//@ExcelMyAnnotation(isRequired = false, order = 14, name = "精选商家")
	//private Integer isHighQualityMerchants;
	// 停车位
	@ExcelMyAnnotation(isRequired = false, order = 16, name = "停车位")
	private Integer parkingNumber;
	// 营业时间开始
	@ExcelMyAnnotation(isRequired = false, order = 17, name = "营业时间开始")
	private String startOperation;
	// 营业时间结束
	@ExcelMyAnnotation(isRequired = false, order = 18, name = "营业时间结束")
	private String endOperation;
	// 晚间营业时间开始
	//@ExcelMyAnnotation(isRequired = false, order = 18, name = "晚间营业时间开始")
	private String nightStart;
	// 晚间营业时间结束
	//@ExcelMyAnnotation(isRequired = false, order = 19, name = "晚间营业时间结束")
	private String nightEnd;
	// 外部商家
	@ExcelMyAnnotation(isRequired = false, order = 19, name = "外部商家")
	private Integer isExternal;
	// 商家地址
	@ExcelMyAnnotation(isRequired = false, order = 20, name = "商家地址", length = 125)
	private String address;
	// 商家标题
	@ExcelMyAnnotation(isRequired = false, order = 21, name = "商家标题", length = 125)
	private String title;
	// 商家描述
	@ExcelMyAnnotation(isRequired = false, order = 22, name = "商家描述", length = 125)
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMName() {
		return mName;
	}

	public void setMName(String mName) {
		this.mName = mName;
	}

	public String getMManager() {
		return mManager;
	}

	public void setMManager(String mManager) {
		this.mManager = mManager;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getFirstCategoryId() {
		return firstCategoryId;
	}

	public void setFirstCategoryId(String firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}

	public String getSecondCategoryId() {
		return secondCategoryId;
	}

	public void setSecondCategoryId(String secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistictName() {
		return distictName;
	}

	public void setDistictName(String distictName) {
		this.distictName = distictName;
	}

	public String getDistict() {
		return distict;
	}

	public void setDistict(String distict) {
		this.distict = distict;
	}

	public Integer getParkingNumber() {
		return parkingNumber;
	}

	public void setParkingNumber(Integer parkingNumber) {
		this.parkingNumber = parkingNumber;
	}

	public String getStartOperation() {
		return startOperation;
	}

	public void setStartOperation(String startOperation) {
		this.startOperation = startOperation;
	}

	public String getEndOperation() {
		return endOperation;
	}

	public void setEndOperation(String endOperation) {
		this.endOperation = endOperation;
	}

	public String getNightStart() {
		return nightStart;
	}

	public void setNightStart(String nightStart) {
		this.nightStart = nightStart;
	}

	public String getNightEnd() {
		return nightEnd;
	}

	public void setNightEnd(String nightEnd) {
		this.nightEnd = nightEnd;
	}

	public Integer getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(Integer isExternal) {
		this.isExternal = isExternal;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

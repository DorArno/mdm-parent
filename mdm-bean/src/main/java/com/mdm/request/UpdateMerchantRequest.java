package com.mdm.request;

import com.mdm.core.bean.common.CommonPojo;

public class UpdateMerchantRequest extends CommonPojo{
	private String organizationId;
	private String mName;
	private String code;
	private String title;
	private String mManager;
	private String contactTel;
	private String serviceTel;
	private String address;
	private String province;
	private String city;
	private String distict;
	private Integer type;
	private Integer levels;
	private Integer status;
	private String description;
	private String imageUrl;
	private String longitude;
	private String latitude;
	private Integer idOrganizationType;
	private Integer isHighQualityMerchants;
	private Integer parkingNumber;
	private Integer hasNight;
	private String  startOperation;
	private String endOperation;
	private String nightStart;
	private String nightEnd;
	private String corpID;
	private String systemId;
	private String systemCode;
	private String version;
	private Integer isExternal;
	private String firstCategoryId;
	private String secondCategoryId;
	

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
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public Integer getIsExternal() {
		return isExternal;
	}
	public void setIsExternal(Integer isExternal) {
		this.isExternal = isExternal;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getmManager() {
		return mManager;
	}
	public void setmManager(String mManager) {
		this.mManager = mManager;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistict() {
		return distict;
	}
	public void setDistict(String distict) {
		this.distict = distict;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLevels() {
		return levels;
	}
	public void setLevels(Integer levels) {
		this.levels = levels;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public Integer getIdOrganizationType() {
		return idOrganizationType;
	}
	public void setIdOrganizationType(Integer idOrganizationType) {
		this.idOrganizationType = idOrganizationType;
	}
	public Integer getIsHighQualityMerchants() {
		return isHighQualityMerchants;
	}
	public void setIsHighQualityMerchants(Integer isHighQualityMerchants) {
		this.isHighQualityMerchants = isHighQualityMerchants;
	}
	public Integer getParkingNumber() {
		return parkingNumber;
	}
	public void setParkingNumber(Integer parkingNumber) {
		this.parkingNumber = parkingNumber;
	}
	public Integer getHasNight() {
		return hasNight;
	}
	public void setHasNight(Integer hasNight) {
		this.hasNight = hasNight;
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
	public String getCorpID() {
		return corpID;
	}
	public void setCorpID(String corpID) {
		this.corpID = corpID;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	
}

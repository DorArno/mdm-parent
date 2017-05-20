package com.mdm.response;

public class MerchantResponse {
	private String id;
	private String createdBy;
	private String modifiedBy;
	private Integer isDeleted;
	
	//来源系统
	private String sourceSystemId;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getSourceSystemId() {
		return sourceSystemId;
	}
	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}
	private String createdOn;
	private String modifiedOn;
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
	private Integer isExternal;
	private String version;
	private String firstCategoryId;
	private String secondCategoryId;
	private String sourceSystem;
	
	public String getFirstCategoryId() {
		return firstCategoryId != null ? firstCategoryId.toLowerCase() : "";
	}
	public void setFirstCategoryId(String firstCategoryId) {
		this.firstCategoryId = firstCategoryId != null ? firstCategoryId.toLowerCase() : "";
	}
	public String getSecondCategoryId() {
		return secondCategoryId != null ? secondCategoryId.toLowerCase() : "";
	}
	public void setSecondCategoryId(String secondCategoryId) {
		this.secondCategoryId = secondCategoryId!= null ? secondCategoryId.toLowerCase() : "";
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
		return province != null ? province.toLowerCase() : "";
	}
	public void setProvince(String province) {
		this.province = province != null ? province.toLowerCase() : "";
	}
	public String getCity() {
		return city != null ? city.toLowerCase() : "";
	}
	public void setCity(String city) {
		this.city = city != null ? city.toLowerCase() : "";
	}
	public String getDistict() {
		return distict != null ? distict.toLowerCase() : "";
	}
	public void setDistict(String distict) {
		this.distict = distict != null ? distict.toLowerCase() : "";
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
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
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

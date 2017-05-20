package com.mdm.pojo;

import java.math.BigDecimal;

import com.mdm.common.CommonPojo;

public class Organazation extends CommonPojo {
	private String parentId;
	private String code;
	private String corpCode;
	private String name;
	private Integer status;
	private String type;
	private String contactTel;
	private String provinceId;
	private String cityId;
	private String districtID;
	private String communityId;
	private BigDecimal longitude;
	private BigDecimal latitude;
	private String addrDetails;
	private String description;
	private Integer levels;
	private String path;
	private String idOrganizationType;
	private String systemId;
	private String systemCode;
	private String version;
	private String pointAccountId;// 积分账户
	private boolean isExcepted;// 是否排除
	
	public String getPointAccountId() {
		return pointAccountId;
	}
	public void setPointAccountId(String pointAccountId) {
		this.pointAccountId = pointAccountId;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getProvinceId() {
		return provinceId != null ? provinceId.toLowerCase() : "";
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId != null ? provinceId.toLowerCase() : "";
	}
	public String getCityId() {
		return cityId != null ? cityId.toLowerCase() : "";
	}
	public void setCityId(String cityId) {
		this.cityId = cityId != null ? cityId.toLowerCase() : "";
	}
	public String getDistrictID() {
		return districtID != null ? districtID.toLowerCase() : "";
	}
	public void setDistrictID(String districtID) {
		this.districtID = districtID != null ? districtID.toLowerCase() : "";
	}
	public String getCommunityId() {
		return communityId != null ? communityId.toLowerCase() : "";
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId != null ? communityId.toLowerCase() : "";
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
	public String getAddrDetails() {
		return addrDetails;
	}
	public void setAddrDetails(String addrDetails) {
		this.addrDetails = addrDetails;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getLevels() {
		return levels;
	}
	public void setLevels(Integer levels) {
		this.levels = levels;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public boolean isExcepted() {
		return isExcepted;
	}
	public void setIsExcepted(boolean isExcepted) {
		this.isExcepted = isExcepted;
	}
	
}

/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月11日 下午1:41:15 *
**/
package com.mdm.request;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.mdm.common.CommonPojo;

public class OrganazationRequest extends CommonPojo {

	private String parentId;
	private String code;

	private String corpCode;
	@Length(max=50, message="名称小于50个字符")
	private String name;
	private Integer status;
	private String type;
	@Length(max=50, message="联系电话小于50个字符")
	private String contactTel;
	private String provinceId;
	private String cityId;
	private String districtID;
	private String communityId;
	private String path;
	private Integer levels;
	private BigDecimal longitude;
	private BigDecimal latitude;
	@Length(max=150, message="地址长度小于150个字符")
	private String addrDetails;
	@Length(max=200, message="描述长度小于200个字符")
	private String description;
	private String idOrganizationType;
	private String systemCode;
	private String systemId;
	

	public String getSystemId() {
		return systemId;
	}
 

	public void setSystemId(String systemId) {
		this.systemId = systemId;
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

	public String getIdOrganizationType() {
		return idOrganizationType;
	}

	public void setIdOrganizationType(String idOrganizationType) {
		this.idOrganizationType = idOrganizationType;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
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


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public Integer getLevels() {
		return levels;
	}


	public void setLevels(Integer levels) {
		this.levels = levels;
	}

}

package com.mdm.pojo;

import com.mdm.common.CommonPojo;
import com.mdm.core.bean.pojo.SystemInfo;

public class Role extends CommonPojo {
	
	private String systemCode;
	
	private String type;
	
	private String code;
	
	private String name;

	private String description;

	private Integer level;

	private String corpCode;

	private String systemId;

	private String source;

	private Byte status;

	private String version;

	private SystemInfo systemInfo;
	
	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		setSource(systemId);
		this.systemId = systemId;
	}


	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "Role [code=" + code + ", name=" + name + ", description=" + description + ", level=" + level
				+ ", corpCode=" + corpCode + ", systemId=" + systemId + ", source=" + source + ", status=" + status
				+ ", version=" + version + ", systemInfo=" + systemInfo + "]";
	}
	
	

}
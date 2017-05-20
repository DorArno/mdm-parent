package com.mdm.pojo;

import java.util.Date;
import java.util.List;

public class Module implements Comparable<Module>{

	private String id;

	private String moduleName;

	private String parentId;

	private Integer moduleLevel;

	private Integer moduleSort;

	private Integer moduleState;

	private String moduleUrl;

	private byte[] moduleIcon;

	private String description;

	private String remark;

	private Byte isDeleted;

	private String createdBy;

	private Date createdOn;

	private String modifiedBy;

	private Date modifiedOn;

	private String path;

	private Module parent;

	private List<Operation> operationList;

	public List<Operation> getOperationList() {
		return operationList;
	}

	public void setOperationList(List<Operation> operationList) {
		this.operationList = operationList;
	}

	public Module getParent() {
		return parent;
	}

	public void setParent(Module parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getModuleLevel() {
		return moduleLevel;
	}

	public void setModuleLevel(Integer moduleLevel) {
		this.moduleLevel = moduleLevel;
	}

	public Integer getModuleSort() {
		return moduleSort;
	}

	public void setModuleSort(Integer moduleSort) {
		this.moduleSort = moduleSort;
	}

	public Integer getModuleState() {
		return moduleState;
	}

	public void setModuleState(Integer moduleState) {
		this.moduleState = moduleState;
	}

	public String getModuleUrl() {
		return moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	public byte[] getModuleIcon() {
		return moduleIcon;
	}

	public void setModuleIcon(byte[] moduleIcon) {
		this.moduleIcon = moduleIcon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Module)) {
			return false;
		}
		Module other = (Module)obj;
		return other.getId().equals(this.getId());
	}

	/* (non Javadoc) 
	 * @Title: compareTo
	 * @Description: TODO
	 * @param o
	 * @return 
	 * @see java.lang.Comparable#compareTo(java.lang.Object) 
	 */
	@Override
	public int compareTo(Module o) {
		if(moduleSort < o.moduleSort){
			return -1;
		}
		return 1;
	}

}
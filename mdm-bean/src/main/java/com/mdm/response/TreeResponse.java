/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: TreeResponse.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.response 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月28日 下午4:53:24 
 * @version: V1.0   
 */
package com.mdm.response;

import java.util.List;

/**
 * @ClassName: TreeResponse
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月28日 下午4:53:24
 */
public class TreeResponse {
	private String id;
	private String pId;
	private String name;
	private Integer level;
	private Boolean open;
	private Boolean nocheck;
	private Boolean checked;
	private List<TreeResponse> nodes;
	
	private String ssyRole;
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
	 * @return the pId
	 */
	public String getpId() {
		return pId;
	}
	/**
	 * @param pId the pId to set
	 */
	public void setpId(String pId) {
		this.pId = pId;
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
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * @return the open
	 */
	public Boolean getOpen() {
		return open;
	}
	/**
	 * @param open the open to set
	 */
	public void setOpen(Boolean open) {
		this.open = open;
	}
	/**
	 * @return the nocheck
	 */
	public Boolean getNocheck() {
		return nocheck;
	}
	/**
	 * @param nocheck the nocheck to set
	 */
	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}
	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	/**
	 * @return the nodes
	 */
	public List<TreeResponse> getNodes() {
		return nodes;
	}
	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<TreeResponse> nodes) {
		this.nodes = nodes;
	}
	public String getSsyRole() {
		return ssyRole;
	}
	public void setSsyRole(String ssyRole) {
		this.ssyRole = ssyRole;
	}
	 
}

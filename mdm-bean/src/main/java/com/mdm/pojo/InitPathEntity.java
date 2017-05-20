/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月9日 下午2:58:10 *
**/ 
package com.mdm.pojo;
/**
 * 初始化树形结构Path字段
 * @author WANG427
 *
 */
public class InitPathEntity {
	private String id;
	private String parentId;
	private String path;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}

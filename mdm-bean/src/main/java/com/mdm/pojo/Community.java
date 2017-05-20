/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月11日 下午5:26:48 *
**/ 
package com.mdm.pojo;
public class Community {
private String id;
private String code;
private String Name;
private String areaId;
private Byte isDeleted;
public String getId() {
	return id != null ? id.toLowerCase() : "";
}
public void setId(String id) {
	this.id = id != null ? id.toLowerCase() : "";
}
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getName() {
	return Name;
}
public void setName(String name) {
	Name = name;
}
public String getAreaId() {
	return areaId;
}
public void setAreaId(String areaId) {
	this.areaId = areaId;
}
public Byte getIsDeleted() {
	return isDeleted;
}
public void setIsDeleted(Byte isDeleted) {
	this.isDeleted = isDeleted;
}

}

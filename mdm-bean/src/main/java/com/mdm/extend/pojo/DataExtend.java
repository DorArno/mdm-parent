/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月14日 下午2:40:47 *
**/
package com.mdm.extend.pojo;

import com.mdm.common.CommonPojo;

public class DataExtend extends CommonPojo{
	private String tableName;
	private String colName;
	private String colType;
	private int colLength;
	private String description;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public int getColLength() {
		return colLength;
	}
	public void setColLength(int colLength) {
		this.colLength = colLength;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}

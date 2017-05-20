/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月14日 下午2:41:08 *
**/
package com.mdm.extend.pojo;

import com.mdm.common.CommonPojo;

public class DataExtendRelevance extends CommonPojo {

	String tableName;
	String dataId;
	String dataExtendId;
	String colValue;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getDataExtendId() {
		return dataExtendId;
	}
	public void setDataExtendId(String dataExtendId) {
		this.dataExtendId = dataExtendId;
	}
	public String getColValue() {
		return colValue;
	}
	public void setColValue(String colValue) {
		this.colValue = colValue;
	}
	
}

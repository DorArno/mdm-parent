/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: RestfulJsonResultDataBean.java
 * @Prject: mdm-bean
 * @Package: com.mdm.common
 * @Description: TODO
 * @author: YANG044
 * @date: 2016年11月15日 下午5:23:41
 * @version: V1.0   
 */
package com.mdm.common;

import java.util.List;

import com.mdm.pojo.District;

/**
 * 此Bean用于在Service层调用Restful接口时，将返回的Json串转换为RestfulJsonResultBean中的Data属性。
 * 
 * @ClassName: RestfulJsonResultDataBean
 * @Description: 
 * @author: YANG044
 * @date: 2016年11月15日 下午5:23:41
 */
public class RestfulJsonResultDataBean {
	//主数据中暂时只有行政区划数据需要此类，暂时固定为List<District>。	comment by Yang Guowei.
	private List<District> list;
	private int totalCount;
	/**
	 * @return the list
	 */
	public List<District> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<District> list) {
		this.list = list;
	}
	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}

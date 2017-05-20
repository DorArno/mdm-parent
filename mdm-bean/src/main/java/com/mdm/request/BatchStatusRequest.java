/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: BatchStatusRequest.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.request 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年11月10日 下午5:50:55 
 * @version: V1.0   
 */
package com.mdm.request;

import java.util.List;

/**
 * @ClassName: BatchStatusRequest
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年11月10日 下午5:50:55
 */
public class BatchStatusRequest {
	private List<String> ids;
	private int flag;

	/**
	 * @return the ids
	 */
	public List<String> getIds() {
		return ids;
	}
	/**
	 * @param ids the ids to set
	 */
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}

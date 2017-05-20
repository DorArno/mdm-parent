/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: VerifyCode.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.user.pojo 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月12日 下午3:51:28 
 * @version: V1.0   
 */
package com.mdm.pojo;

import java.util.Date;

import org.elasticsearch.search.aggregations.support.format.ValueFormat.DateTime;

import com.mdm.common.CommonPojo;

/**
 * @ClassName: VerifyCode
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月12日 下午3:51:28
 */
public class VerifyCode extends CommonPojo {
	private String code;
	private String cellPhone;
	private Date createTime;
	private Date failureTime;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the cellPhone
	 */
	public String getCellPhone() {
		return cellPhone;
	}
	/**
	 * @param cellPhone the cellPhone to set
	 */
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the failureTime
	 */
	public Date getFailureTime() {
		return failureTime;
	}
	/**
	 * @param failureTime the failureTime to set
	 */
	public void setFailureTime(Date failureTime) {
		this.failureTime = failureTime;
	}
}

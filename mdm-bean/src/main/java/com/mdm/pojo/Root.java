/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: Root.java 
 * @Prject: mdm-bean
 * @Package: com.mdm.pojo 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月8日 上午11:14:52 
 * @version: V1.0   
 */
package com.mdm.pojo;

import java.util.List;

/**
 * @ClassName: Root
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月8日 上午11:14:52
 */
public class Root {
	private String result;
	private List<Data> data;
	private int resCode;

	public void setResult(String result) {
		this.result = result;
	}
	public String getResult() {
		return this.result;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public List<Data> getData() {
		return this.data;
	}
	public void setResCode(int resCode) {
		this.resCode = resCode;
	}
	public int getResCode() {
		return this.resCode;
	}
}

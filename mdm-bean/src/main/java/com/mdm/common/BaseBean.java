package com.mdm.common;

/**
* @author:gaoyb
* @version 创建时间：2016年9月13日 下午9:28:38
* 公共基础bean
*/

public class BaseBean {
	
	private Integer pageNum;  //当前页码
	
	private Integer pageSize; //每页显示数量
	
	private String userId;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the pageNum
	 */
	public Integer getPageNum() {
		if(pageNum == null){
			this.pageNum=1;
		}
		return pageNum;
	}

	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(Integer pageNum) {
		if(pageNum == null){
			pageNum=1;
		}
		this.pageNum = pageNum;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		if(pageSize == null){
			this.pageSize=10;
		}
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		if(pageSize == null){
			pageSize=10;
		}
		this.pageSize = pageSize;
	}

	
}

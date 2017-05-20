package com.mdm.request;

/**
 * @ClassName: UserLoginRecordRequest
 * @Description: TODO
 * @author: Han
 * @date: 2016年11月10日 下午2:25:12
 */
public class UserLoginRecordRequest {
	/**
	 * 账户
	 */
	private String account;
	/**
	 * 开始时间
	 */
	private String startDate;
	/**
	 * 结束时间
	 */
	private String endDate;
	/**
	 * 当前页码
	 */
	private Integer pageNum;
	/**
	 * 每页显示数量
	 */
	private Integer pageSize;

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}

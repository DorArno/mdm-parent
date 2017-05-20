/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年12月6日 下午3:30:13 *
**/ 
package com.mdm.response;

import com.mdm.core.bean.common.CommonDataResponse;

public class CheckOrganizationCommonData {

	private CommonDataResponse commonDataResponse;
	
	private String checkMsg;
	
	private boolean checkResult;

	public CommonDataResponse getCommonDataResponse() {
		return commonDataResponse;
	}

	public void setCommonDataResponse(CommonDataResponse commonDataResponse) {
		this.commonDataResponse = commonDataResponse;
	}

	public String getCheckMsg() {
		return checkMsg;
	}

	public void setCheckMsg(String checkMsg) {
		this.checkMsg = checkMsg;
	}

	public boolean isCheckResult() {
		return checkResult;
	}

	public void setCheckResult(boolean checkResult) {
		this.checkResult = checkResult;
	}
	
}

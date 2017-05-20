/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: BusinessLogController.java 
 * @Prject: mdm-web
 * @Package: com.mdm.controller.web.businessLog 
 * @Description: TODO
 * @author: ZHAN526   
 * @date: 2016年9月14日 下午5:17:51 
 * @version: V1.0   
 */
package com.mdm.controller.web.businessLog;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.controller.BaseController;
import com.mdm.core.bean.request.MQInvokeRequest;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.service.businesslog.BusinessLogService;
import com.mdm.core.util.MdmException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: BusinessLogController
 * @Description: MQ下发日志管理
 * @author: ZHAN526
 * @date: 2016年9月14日 下午5:17:51
 */
@Api("MQ下发日志管理")
@RestController
@RequestMapping("/businessLog")
public class BusinessLogController extends BaseController {
	@Autowired
	private BusinessLogService businessLogService;
	
	@ApiOperation(value = "查询MQ日志信息")
	@RequestMapping(value = "/businessLogs", method = RequestMethod.GET)
	public Object queryBusinessLogInfoList(@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "destSystemId", required = false) String destSystemId,
			@RequestParam(value = "logType", required = false) String logType,
			@RequestParam(value = "dataType", required = false) String dataType,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "beginDate", required = false) String beginDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageNum", required = false) int pageNum,
			@RequestParam(value = "pageSize", required = false) int pageSize) throws MdmException {
		Map<String, Object> map = new HashMap<>();
		map.put("content", content);
		map.put("destSystemId", destSystemId);
		map.put("logType", logType);
		map.put("dataType", dataType);
		map.put("status", status);
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		return this.getResponseResult(businessLogService.queryPageList(map, pageNum, pageSize));
	}
	
	@ApiOperation(value = "MQ下发后回调", notes = "更新MQ下发日志")
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody MQInvokeRequest mqInvokeRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(businessLogService.updateBySysCodeData(mqInvokeRequest)));
	}

}

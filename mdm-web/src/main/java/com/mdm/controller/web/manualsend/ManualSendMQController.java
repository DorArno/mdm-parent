/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: ManualSendMQController.java 
 * @Prject: mdm-web
 * @Package: com.mdm.controller.web.manualsend 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年10月27日 上午11:01:57 
 * @version: V1.0   
 */
package com.mdm.controller.web.manualsend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.controller.BaseController;
import com.mdm.core.util.MdmException;
import com.mdm.request.ManualSendMQRequest;
import com.mdm.service.manualsend.ManualSendMQService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** 
 * @ClassName: ManualSendMQController 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年10月27日 上午11:01:57  
 */

@Api("手工下发主数据")
@RestController
@RequestMapping("/activemq")
public class ManualSendMQController extends BaseController {
	
	@Autowired
	private ManualSendMQService manualSendMQService;

    @RequestMapping(value="/manualsend",method=RequestMethod.POST)
	@ApiOperation(value = "", notes = "手工触发数据下发")
	public ModelAndView handle(@RequestBody ManualSendMQRequest manualSendMQRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(manualSendMQService.sendMQ(manualSendMQRequest)));
	}
}

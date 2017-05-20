/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: ExtraSystemApiConfigController.java 
 * @Prject: mdm-web
 * @Package: com.mdm.controller.web.extrasystemapiconfig 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月18日 下午3:24:14 
 * @version: V1.0   
 */
package com.mdm.controller.web.extrasystemapiconfig;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mdm.controller.BaseController;
import com.mdm.core.bean.pojo.ExtraSystemApiConfig;
import com.mdm.core.util.MdmException;
import com.mdm.service.systemapiconfig.ExtraSystemApiConfigService;
import com.mdm.task.service.impl.MessageQueueServiceImpl;

import io.swagger.annotations.ApiOperation;

/** 
 * @ClassName: ExtraSystemApiConfigController 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月18日 下午3:24:14  
 */
@RestController
@RequestMapping("/extraSystemApiConfig")
public class ExtraSystemApiConfigController extends BaseController{
	
	@Autowired
	private ExtraSystemApiConfigService  extraSystemApiConfigService;
	
	@Autowired
	private MessageQueueServiceImpl messageQueueTaskService;
	
	@ApiOperation(value="消息补偿接口", notes="消息补偿接口")
    @RequestMapping(value="/messageQueueTask",method=RequestMethod.GET)
    public Object messageQueueTask() throws MdmException {
        return this.getResponseResult(messageQueueTaskService.messageQueueTaskCompensateService());
    }
	
	
	@ApiOperation(value="查询外部系统配置信息", notes="根据条件查询外部系统配置信息")
    @RequestMapping(value="/extraSystemApiInfos",method=RequestMethod.GET)
    public Object queryExtraSystemApiInfo(@RequestParam(value = "systemName", required = false) String systemName,
    		@RequestParam(value = "pageNum", required = true) int pageNum,
    		@RequestParam(value = "pageSize", required = true) int pageSize) throws MdmException {
    	
        return this.getResponseResult(extraSystemApiConfigService.querySystemApiConfigList(systemName,pageNum,pageSize));
    }
		
	@ApiOperation(value="新增外部系统配置信息", notes="新增外部系统配置信息")
    @RequestMapping(value="/addExtraSystemApiInfos",method=RequestMethod.POST)
    public Object addExtraSystemApiInfo(@RequestBody ExtraSystemApiConfig extraSystemApiConfig) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
		return this.getResponseResult(extraSystemApiConfigService.insert(extraSystemApiConfig));
    }
	
	@ApiOperation(value="编辑外部系统配置信息", notes="编辑外部系统配置信息")
    @RequestMapping(value="/editExtraSystemApiInfos",method=RequestMethod.POST)
    public Object editExtraSystemApiInfo(@RequestBody ExtraSystemApiConfig extraSystemApiConfig) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
		return this.getResponseResult(extraSystemApiConfigService.update(extraSystemApiConfig));
    }
	
	@ApiOperation(value="删除外部系统配置信息", notes="删除外部系统配置信息")
    @RequestMapping(value="/deletExtraSystemApiInfos",method=RequestMethod.POST)
    public Object deleteExtraSystemApiInfo(@RequestBody ExtraSystemApiConfig[] extraSystemApiConfig) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
        for(int i=0;i<extraSystemApiConfig.length;i++){
        	extraSystemApiConfigService.deleteById(extraSystemApiConfig[i].getId());
        };
		return this.getResponseResult(1);
    }
        
	
}

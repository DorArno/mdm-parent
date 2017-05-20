/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: QuartzManagerController.java 
 * @Prject: mdm-web
 * @Package: com.mdm.controller.web.quartzManager 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月8日 上午9:38:11 
 * @version: V1.0   
 */
package com.mdm.controller.web.quartzManager;

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
import com.mdm.core.util.MdmException;
import com.mdm.pojo.CronJobInfo;
import com.mdm.service.quartzmanager.QuartzManagerService;

import io.swagger.annotations.ApiOperation;

/** 
 * @ClassName: QuartzManagerController 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月8日 上午9:38:11  
 */
@RestController
@RequestMapping("/quartzManager")
public class QuartzManagerController extends BaseController{
	
	//定时任务管理服务
	@Autowired
	QuartzManagerService  quartzManagerService;
	
	/**
	 * 查询定时任务信息-QUARTZ查询服务(用户列表)
	 * 
	 * 前台页面通过此服务查询定时任务一览信息
	 * @Title: queryQuartzTaskInfo 
	 * @Description: TODO
	 * @param taskName 任务名称
	 * @param group 任务组
	 * @param pageNum 页数
	 * @param pageSize 页大小
	 * @return
	 * @throws MdmException
	 * @return: Object 成功返回定时任务信息，失败返回错误信息
	 */
	@ApiOperation(value="查询定时任务信息", notes="根据条件查询定时任务信息")
    @RequestMapping(value="/taskInfos",method=RequestMethod.GET)
    public Object queryQuartzTaskInfo(@RequestParam(value = "taskName", required = false) String taskName,
    		@RequestParam(value = "group", required = false) String group,
    		@RequestParam(value = "pageNum", required = true) int pageNum,
    		@RequestParam(value = "pageSize", required = true) int pageSize) throws MdmException {
    	
        return this.getResponseResult(quartzManagerService.queryQuartzTaskInfo(taskName,group,pageNum,pageSize));
    }
	
	/**
	 * 查询任务组信息
	 * 
	 * 前台页面需要模糊查询任务组信息，故提供此方法调用
	 * @Title: queryStaticDataList 
	 * @Description: TODO
	 * @param taskgroup 任务组名称
	 * @return 符合查询条件的任务组信息
	 * @throws MdmException
	 * @return: Object
	 */
	@ApiOperation(value = "任务组信息", notes = "任务组信息")
    @RequestMapping(value="/taskgroup",method=RequestMethod.GET)
	public Object queryStaticDataList(@RequestParam(value = "taskgroup", required = false) String taskgroup) throws MdmException {
		return this.getResponseResult(quartzManagerService.getCacheGroupInfo(taskgroup));
	}
	
	/**
	 * 新增定时任务信息
	 * 
	 * @Title: addQuartzTaskInfo 
	 * @Description: TODO
	 * @param cronJobInfo 定时任务JSON串
	 * @return
	 * @throws MdmException
	 * @throws JsonGenerationException JSON转化异常
	 * @throws JsonMappingException JSON映射异常
	 * @throws IOException 输入输出流异常
	 * @return: Object 成功/失败
	 */
	@ApiOperation(value="新增定时任务信息", notes="新增定时任务信息")
    @RequestMapping(value="/addTaskInfos",method=RequestMethod.POST)
    public Object addQuartzTaskInfo(@RequestBody CronJobInfo cronJobInfo) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
		return this.getResponseResult(quartzManagerService.addQuartzTaskInfo(cronJobInfo));
    }
	
	/**
	 * 编辑定时任务
	 * @Title: editQuartzTaskInfo 
	 * @Description: TODO
	 * @param cronJobInfo
	 * @return
	 * @throws MdmException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @return: Object
	 */
	@ApiOperation(value="编辑定时任务信息", notes="编辑定时任务信息")
    @RequestMapping(value="/editTaskInfos",method=RequestMethod.POST)
    public Object editQuartzTaskInfo(@RequestBody CronJobInfo cronJobInfo) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
		return this.getResponseResult(quartzManagerService.editQuartzTaskInfo(cronJobInfo));
    }
	
	/**
	 * 删除任务信息
	 * @Title: deleteQuartzTaskInfo 
	 * @Description: TODO
	 * @param cronJobInfo
	 * @return
	 * @throws MdmException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @return: Object
	 */
	@ApiOperation(value="删除定时任务信息", notes="删除定时任务信息")
    @RequestMapping(value="/deleteTaskInfos",method=RequestMethod.POST)
    public Object deleteQuartzTaskInfo(@RequestBody CronJobInfo cronJobInfo) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
        return this.getResponseResult(quartzManagerService.deleteQuartzTaskInfo(cronJobInfo));
    }
	
	/**
	 * 恢复定时任务信息
	 * @Title: resumeQuartzTaskInfo 
	 * @Description: TODO
	 * @param cronJobInfo
	 * @return
	 * @throws MdmException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @return: Object
	 */
	@ApiOperation(value="恢复定时任务信息", notes="恢复定时任务信息")
    @RequestMapping(value="/resumeTaskInfos",method=RequestMethod.POST)
    public Object resumeQuartzTaskInfo(@RequestBody CronJobInfo cronJobInfo) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
        return this.getResponseResult(quartzManagerService.resumeQuartzTaskInfo(cronJobInfo));
    }
	/**
	 * 
	 * @Title: pauseQuartzTaskInfo 
	 * @Description: TODO
	 * @param cronJobInfo
	 * @return
	 * @throws MdmException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @return: Object
	 */
	@ApiOperation(value="暂停定时任务信息", notes="暂停定时任务信息")
    @RequestMapping(value="/pauseTaskInfos",method=RequestMethod.POST)
    public Object pauseQuartzTaskInfo(@RequestBody CronJobInfo cronJobInfo) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
        return this.getResponseResult(quartzManagerService.pauseQuartzTaskInfo(cronJobInfo));
    }
	
	/**
	 * 
	 * @Title: executeQuartzTaskInfo 
	 * @Description: TODO
	 * @param cronJobInfo
	 * @return
	 * @throws MdmException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @return: Object
	 */
	@ApiOperation(value="执行定时任务信息", notes="执行定时任务信息")
    @RequestMapping(value="/executeTaskInfos",method=RequestMethod.POST)
    public Object executeQuartzTaskInfo(@RequestBody CronJobInfo cronJobInfo) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
        return this.getResponseResult(quartzManagerService.executeOnceTaskInfo(cronJobInfo));
    }
	
	/**
	 * 
	 * @Title: resumeAllQuartzTaskInfo 
	 * @Description: TODO
	 * @param choseArr
	 * @return
	 * @throws MdmException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @return: Object
	 */
	@ApiOperation(value="恢复定时任务信息", notes="恢复定时任务信息")
    @RequestMapping(value="/resumeAllTaskInfos",method=RequestMethod.POST)
    public Object resumeAllQuartzTaskInfo(@RequestBody CronJobInfo[] choseArr) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
		if(choseArr != null)
		for(int i = 0;i<choseArr.length;i++){
			quartzManagerService.resumeQuartzTaskInfo(choseArr[i]);
		}
		return this.getResponseResult(1);
    }
	
	/**
	 * 
	 * @Title: pauseAllQuartzTaskInfo 
	 * @Description: TODO
	 * @param choseArr
	 * @return
	 * @throws MdmException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @return: Object
	 */
	@ApiOperation(value="暂停定时任务信息", notes="暂停定时任务信息")
    @RequestMapping(value="/pauseAllTaskInfos",method=RequestMethod.POST)
    public Object pauseAllQuartzTaskInfo(@RequestBody CronJobInfo[] choseArr) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
        if(choseArr != null)
		for(int i = 0;i<choseArr.length;i++){
        	quartzManagerService.pauseQuartzTaskInfo(choseArr[i]);
        }
		return this.getResponseResult(1);
    }
	
	
	
	
	
}

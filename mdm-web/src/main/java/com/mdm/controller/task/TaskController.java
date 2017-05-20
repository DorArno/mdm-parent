/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: TaskController.java 
 * @Prject: mdm-web
 * @Package: com.mdm.controller.task 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年11月3日 上午11:33:16 
 * @version: V1.0   
 */
package com.mdm.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdm.core.util.MdmException;
import com.mdm.service.task.TaskService;

import io.swagger.annotations.ApiOperation;

/** 
 * @ClassName: TaskController 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年11月3日 上午11:33:16  
 */
@RestController
@RequestMapping(value="/task")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@ApiOperation(value="任务触发批量下发消息接口", notes="任务触发批量下发消息接口")
    @RequestMapping(value="/running/{taskType}",method=RequestMethod.GET)
    public Object executeTask(@PathVariable (value = "taskType") Integer taskType) throws MdmException {
		taskService.executeTask(taskType);
		return 1;
    }
	
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: TaskService.java 
 * @Prject: mdm-service
 * @Package: com.mdm.service.task 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年11月3日 上午11:42:22 
 * @version: V1.0   
 */
package com.mdm.service.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdm.core.constants.DigitalConstants;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.enums.CommonTaskStatusEnum;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.enums.LogTypeEnum;
import com.mdm.core.enums.TaskTypeEnum;
import com.mdm.mq.producter.MQSenderAdapter;
import com.mdm.pojo.CommonTask;
import com.mdm.pojo.Merchant;
import com.mdm.pojo.Organazation;
import com.mdm.pojo.Role;
import com.mdm.response.UserInfoEditResponse;
import com.mdm.service.merchant.MerchantService;
import com.mdm.service.organazation.OrganazationService;
import com.mdm.service.role.RoleService;
import com.mdm.service.user.UserService;

/** 
 * @ClassName: TaskService 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年11月3日 上午11:42:22  
 */
@Service
public class TaskService {
	
	@Autowired
	private CommonTaskService commonTaskService;
	
	@Autowired
	private MerchantService merchantService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrganazationService organazationService;

	@Autowired
	private MQSenderAdapter mqSenderAdapter;
	
	/**
	 * 
	 * @Title: executeTask 
	 * @Description: 批量下发MQ任务服务
	 * @param taskType
	 * @return
	 * @return: int
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public int executeTask(Integer taskType){
		//批量下发MQ
		if(taskType.equals(TaskTypeEnum.BATCHMQTASK.getValue())){
			Map<String, Object> map = new HashMap<>();
			map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
			map.put("status", CommonTaskStatusEnum.NEW.getValue());
			List<CommonTask> taskList =  (List<CommonTask>)commonTaskService.queryPageList(map, DigitalConstants.ONE, DigitalConstants.HUNDRED).getList();
			for(CommonTask commonTask: taskList){
				Integer dataType = commonTask.getDataType();
				//行政区划
				if(dataType.equals(DataTypeEnum.DISTRICT.getValue())){
					
				}
				//商家
				if(dataType.equals(DataTypeEnum.MERCHANT.getValue())){
					Merchant merchant = merchantService.getMerchantById(commonTask.getDataId());
					mqSenderAdapter.sendMQ(LogTypeEnum.ADD.getValue(), dataType, merchant);
				}
				//组织机构
				if(dataType.equals(DataTypeEnum.ORGANAZATION.getValue())){
					Organazation organazation = organazationService.queryById(commonTask.getDataId());
					mqSenderAdapter.sendMQ(LogTypeEnum.ADD.getValue(), dataType, organazation);					
				}
				//角色
				if(dataType.equals(DataTypeEnum.ROLE.getValue())){
					Role  role = roleService.getById(commonTask.getDataId());
					mqSenderAdapter.sendMQ(LogTypeEnum.ADD.getValue(), dataType, role);		
				}
				//用户
				if(dataType.equals(DataTypeEnum.USER.getValue())){
					UserInfoEditResponse userInfo = userService.queryUserInfoById(commonTask.getDataId());
					mqSenderAdapter.sendMQ(LogTypeEnum.ADD.getValue(), dataType, userInfo);	
				}
				//更新任务状态
				CommonTask updateTask = new CommonTask();
				updateTask.setId(commonTask.getId());
				updateTask.setModifiedOn(new Date());
				updateTask.setStatus(CommonTaskStatusEnum.COMPLETED.getValue());
				commonTaskService.updateStatus(updateTask);
			}
		}
	return 1;	
	}
}

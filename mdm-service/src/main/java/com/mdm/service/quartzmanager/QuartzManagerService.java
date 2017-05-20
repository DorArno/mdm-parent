/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: QuartzManagerService.java 
 * @Prject: mdm-service
 * @Package: com.mdm.service.quartzmanager 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月8日 上午9:58:34 
 * @version: V1.0   
 */
package com.mdm.service.quartzmanager;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mdm.common.PageResultBean;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.CronJobInfo;

/** 
 * @ClassName: QuartzManagerService 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月8日 上午9:58:34  
 */
public interface QuartzManagerService {
	
	/**
	 * 查询定时任务信息
	 * @Title: queryQuartzTaskInfo 
	 * @Description: TODO
	 * @return: void
	 */
	PageResultBean queryQuartzTaskInfo(String taskName,String group,int pageNum,int pageSize);
	
	/**
	 * 新增定时任务
	 * @Title: addQuartzTaskInfo 
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	int addQuartzTaskInfo(CronJobInfo cronJobInfo)  throws JsonGenerationException, JsonMappingException, IOException ;
	
	/**
	 * 更新定时任务
	 * @Title: updateQuartzTaskInfo 
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	int updateQuartzTaskInfo(CronJobInfo cronJobInfo);
	
	/**
	 * 暂停定时任务
	 * @Title: pauseQuartzTaskInfo 
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	int pauseQuartzTaskInfo(CronJobInfo cronJobInfo) throws JsonGenerationException, JsonMappingException, IOException;

	/**
	 * 继续定时任务
	 * @Title: resumeQuartzTaskInfo 
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	int resumeQuartzTaskInfo(CronJobInfo cronJobInfo) throws JsonGenerationException, JsonMappingException, IOException ;
	
	/**
	 * 执行一次定时任务
	 * @Title: executeOnceTaskInfo 
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	int executeOnceTaskInfo(CronJobInfo cronJobInfo) throws JsonGenerationException, JsonMappingException, IOException ;
	
	/**
	 * 编辑定时任务
	 * @Title: editQuartzTaskInfo 
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	int editQuartzTaskInfo(CronJobInfo cronJobInfo) throws MdmException, JsonGenerationException, JsonMappingException, IOException;
	
	/**
	 * 删除定时任务
	 * @Title: deleteQuartzTaskInfo 
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	int deleteQuartzTaskInfo(CronJobInfo cronJobInfo) throws JsonGenerationException, JsonMappingException, IOException ;
	
	/**
	 * 任务组信息
	 * @Title: getCacheGroupInfo 
	 * @Description: TODO
	 * @param groupName
	 * @return
	 * @return: List<String>
	 */
	public List<String> getCacheGroupInfo(String groupName);
}

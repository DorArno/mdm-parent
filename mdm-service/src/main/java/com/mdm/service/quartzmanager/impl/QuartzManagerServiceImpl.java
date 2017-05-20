/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: QuartzManagerServiceImpl.java 
 * @Prject: mdm-service
 * @Package: com.mdm.service.quartzmanager.impl 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月8日 上午10:10:44 
 * @version: V1.0   
 */
package com.mdm.service.quartzmanager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.arvato.operation.platform.tsg.base.core.utils.JsonUtil;
import com.arvato.operation.platform.tsg.util.RestUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mdm.common.PageResultBean;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.CronJobInfo;
import com.mdm.pojo.Data;
import com.mdm.pojo.Root;
import com.mdm.service.quartzmanager.QuartzManagerService;

/** 
 * @ClassName: QuartzManagerServiceImpl 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月8日 上午10:10:44  
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QuartzManagerServiceImpl implements QuartzManagerService{
	
	@Value("${quartz.server}")
	private String baseUrl;
	
	private static final ConcurrentSkipListSet<String> arrayList = new ConcurrentSkipListSet<String>();
	

	/* (non Javadoc) 
	 * @Title: queryQuartzTaskInfo
	 * @Description: TODO 
	 * @see com.mdm.service.quartzmanager.QuartzManagerService#queryQuartzTaskInfo() 
	 */
	@Override
	public PageResultBean queryQuartzTaskInfo(String taskName,String group,int pageNum,int pageSize) {
		PageResultBean pageResult = new PageResultBean();
		Page<Object> page = PageHelper.startPage(pageNum, pageSize,true);//分页与统计总数
		
		String restStr = RestUtil.sendData(baseUrl+"listjob", "GET", null, 20000, null, null, 0);
		Root root = null;
		root = JSONObject.parseObject(restStr, Root.class);
		List<Data> tmpList = new ArrayList<Data>(); 
		if(taskName!=null && !taskName.trim().equals("")){
			List<Data> list = root.getData();
			for(int i = 0;i<list.size();i++){
				Data data = list.get(i);
				String name = data.getJobDetail().getName();
				String groupName = data.getJobDetail().getGroup();
				if(name!=null && name.toUpperCase().contains(taskName.toUpperCase())){
					tmpList.add(data);
				}
				if(group!=null)
				if(groupName!=null && groupName.toUpperCase().contains(group.toUpperCase())){}else{
					tmpList.remove(data);
				}
			}
			
		}
		else if(group!=null && !group.trim().equals("")){
			List<Data> list = root.getData();
			for(int i = 0;i<list.size();i++){
				Data data = list.get(i);
				String groupName = data.getJobDetail().getGroup();
				if(groupName!=null && groupName.toUpperCase().contains(group.toUpperCase())){
					tmpList.add(data);
				}
			}
		}
		else{
			tmpList = root.getData();
		}
		cacheGroupInfo(tmpList);
		int totalsize = tmpList.size();
		processStatus(tmpList);
		Collections.sort(tmpList);
		List subList = tmpList.subList(pageSize * pageNum - pageSize >= totalsize ? 0 :  pageSize * pageNum - pageSize, pageSize * pageNum > totalsize ? totalsize : pageSize * pageNum);
		
		pageResult.setList(subList);
		pageResult.setTotalCount(totalsize);
		
		return pageResult ;
		
	}
	
	private static void cacheGroupInfo(List<Data> list){
		list.stream().forEach(e -> arrayList.add(e.getJobDetail().getGroup()));
	}
	
	public List<String> getCacheGroupInfo(String groupName){
		if(groupName != null)
		return arrayList.stream().filter(p -> p.toString().contains(groupName)).collect(Collectors.toList());
		return arrayList.stream().collect(Collectors.toList());
	}
	
	/**
	 * 状态英文变中文
	 * @Title: processStatus 
	 * @Description: TODO
	 * @param list
	 * @return: void
	 */
	private void processStatus(List<Data> list){
		list.forEach(data ->{
			String status = data.getStatus();
			if(status!=null && status.toUpperCase().equals("NORMAL")){
				data.setStatus("正常");
			}
			else if(status!=null && status.toUpperCase().equals("PAUSED")){
				data.setStatus("已停止");
			}
			else {
				data.setStatus("等待处理中");
			}
		});	
	}

	/* (non Javadoc) 
	 * @Title: addQuartzTaskInfo
	 * @Description: TODO
	 * @return 2
	 * @see com.mdm.service.quartzmanager.QuartzManagerService#addQuartzTaskInfo() 
	 */
	@Override
	public int addQuartzTaskInfo(CronJobInfo cronJobInfo) throws JsonGenerationException, JsonMappingException, IOException {

		String param = null;
		String[] argNames = cronJobInfo.getArgsNames();
		String[] argValues = cronJobInfo.getArgsValues();
		StringBuffer sb = new StringBuffer();
		if(argNames.length > 0){
			for(String tmp : argNames){
				sb.append(tmp + ",");
			}
			if(sb.toString()!=null && !"".equals(sb.toString())){
				sb.setLength(sb.length() - 1);
				cronJobInfo.setArgNames(sb.toString());
			}
			sb.setLength(0);
			for(String tmps : argValues){
				sb.append(tmps + ",");
			}
			if(sb.toString()!=null && !"".equals(sb.toString())){
				sb.setLength(sb.length() - 1);
				cronJobInfo.setArgValues(sb.toString());
			}
		}
		
		cronJobInfo.setArgsNames(null);
		cronJobInfo.setArgsValues(null);
		param = JsonUtil.toJsonString(cronJobInfo);
    	String restStr = RestUtil.sendData(baseUrl+"addCronJob", "POST", param, 2000, null, null, 0);
    	if(restStr !=null && restStr.contains("200")){
    		return 1;
    	}
    	return 0;
	}

	/* (non Javadoc) 
	 * @Title: updateQuartzTaskInfo
	 * @Description: TODO
	 * @return 
	 * @see com.mdm.service.quartzmanager.QuartzManagerService#updateQuartzTaskInfo() 
	 */
	@Override
	public int updateQuartzTaskInfo(CronJobInfo cronJobInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non Javadoc) 
	 * @Title: pauseQuartzTaskInfo
	 * @Description: TODO
	 * @return 
	 * @see com.mdm.service.quartzmanager.QuartzManagerService#pauseQuartzTaskInfo() 
	 */
	@Override
	public int pauseQuartzTaskInfo(CronJobInfo cronJobInfo) throws JsonGenerationException, JsonMappingException, IOException {
		String param = null;
		param = JsonUtil.toJsonString(cronJobInfo);
    	String restStr = RestUtil.sendData(baseUrl+"pauseJob", "POST", param, 2000, null, null, 0);
    	if(restStr !=null && restStr.contains("200")){
    		return 1;
    	}
    	return 0;
	}

	/* (non Javadoc) 
	 * @Title: resumeQuartzTaskInfo
	 * @Description: TODO
	 * @return 
	 * @see com.mdm.service.quartzmanager.QuartzManagerService#resumeQuartzTaskInfo() 
	 */
	@Override
	public int resumeQuartzTaskInfo(CronJobInfo cronJobInfo) throws JsonGenerationException, JsonMappingException, IOException {
		String param = null;
		param = JsonUtil.toJsonString(cronJobInfo);
    	String restStr = RestUtil.sendData(baseUrl+"resumeJob", "POST", param, 2000, null, null, 0);
    	if(restStr !=null && restStr.contains("200")){
    		return 1;
    	}
    	return 0;
	}

	/* (non Javadoc) 
	 * @Title: executeOnceTaskInfo
	 * @Description: TODO
	 * @return 
	 * @see com.mdm.service.quartzmanager.QuartzManagerService#executeOnceTaskInfo() 
	 */
	@Override
	public int executeOnceTaskInfo(CronJobInfo cronJobInfo) throws JsonGenerationException, JsonMappingException, IOException {
		String param = null;
		param = JsonUtil.toJsonString(cronJobInfo);
    	String restStr = RestUtil.sendData(baseUrl+"executeOnce", "POST", param, 2000, null, null, 0);
    	if(restStr !=null && restStr.contains("200")){
    		return 1;
    	}
    	return 0;
	}

	/* (non Javadoc) 
	 * @Title: editQuartzTaskInfo
	 * @Description: TODO
	 * @return 
	 * @see com.mdm.service.quartzmanager.QuartzManagerService#editQuartzTaskInfo() 
	 */
	@Override
	public int editQuartzTaskInfo(CronJobInfo cronJobInfo) throws MdmException, JsonGenerationException, JsonMappingException, IOException {
		String param = null;
		String[] argNames = cronJobInfo.getArgsNames();
		String[] argValues = cronJobInfo.getArgsValues();
		StringBuffer sb = new StringBuffer();
		if(argNames.length > 0){
			for(String tmp : argNames){
				sb.append(tmp + ",");
			}
			if(sb.toString()!=null && !"".equals(sb.toString())){
				sb.setLength(sb.length() - 1);
				cronJobInfo.setArgNames(sb.toString());
			}
			sb.setLength(0);
			for(String tmps : argValues){
				sb.append(tmps + ",");
			}
			if(sb.toString()!=null && !"".equals(sb.toString())){
				sb.setLength(sb.length() - 1);
				cronJobInfo.setArgValues(sb.toString());
			}
		}
		
		cronJobInfo.setArgsNames(null);
		cronJobInfo.setArgsValues(null);
		param = JsonUtil.toJsonString(cronJobInfo);
    	String restStr = RestUtil.sendData(baseUrl+"updateCronJob", "POST", param, 2000, null, null, 0);
    	if(restStr !=null && restStr.contains("200")){
    		return 1;
    	}
    	return 0;
	}

	/* (non Javadoc) 
	 * @Title: deleteQuartzTaskInfo
	 * @Description: TODO
	 * @return 
	 * @see com.mdm.service.quartzmanager.QuartzManagerService#deleteQuartzTaskInfo() 
	 */
	@Override
	public int deleteQuartzTaskInfo(CronJobInfo cronJobInfo) throws JsonGenerationException, JsonMappingException, IOException {
		String param = null;
		param = JsonUtil.toJsonString(cronJobInfo);
    	String restStr = RestUtil.sendData(baseUrl+"deleteJob", "POST", param, 2000, null, null, 0);
    	if(restStr !=null && restStr.contains("200")){
    		return 1;
    	}
    	return 0;
	}
}

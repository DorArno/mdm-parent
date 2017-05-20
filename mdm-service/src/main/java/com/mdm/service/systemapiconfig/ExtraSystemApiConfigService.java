/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: ExtraSystemApiConfigService.java 
 * @Prject: mdm-service
 * @Package: com.mdm.service.systemapiconfig 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月14日 下午2:59:02 
 * @version: V1.0   
 */
package com.mdm.service.systemapiconfig;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mdm.core.bean.common.PageResultBean;
import com.mdm.core.bean.pojo.BusinessLog;
import com.mdm.core.bean.pojo.ExtraSystemApiConfig;
import com.mdm.core.dao.write.systemapiconfig.ExtraSystemApiConfigMapper;
import com.mdm.core.util.DataUtils;


/** 
 * @ClassName: ExtraSystemApiConfigService 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月14日 下午2:59:02  
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtraSystemApiConfigService {
	
	@Autowired
    private ExtraSystemApiConfigMapper extraSystemApiConfigMapper;
	
	
	public ExtraSystemApiConfig querySystemApiConfigInfo(Map<String,Object> map){
		return extraSystemApiConfigMapper.querySystemApiConfigInfo(map);
	}
	
	/**
	 * 查询列表
	 * @Title: querySystemApiConfigList 
	 * @Description: TODO
	 * @param systemName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @return: PageResultBean
	 */
	public PageResultBean querySystemApiConfigList(String systemName,int pageNum, int pageSize){
		Page<Object> page = PageHelper.startPage(pageNum, pageSize);//分页与统计总数
		List<ExtraSystemApiConfig> list = extraSystemApiConfigMapper.queryPageList(systemName);
		PageResultBean pageResult = new PageResultBean();
		pageResult.setList(list);
		pageResult.setTotalCount(page.getTotal());
		return 	pageResult ;
	}
	
	/**
	 * 增加操作
	 * @Title: insert 
	 * @Description: TODO
	 * @param extraSystemApiConfig
	 * @return
	 * @return: int
	 */
	public int insert(ExtraSystemApiConfig extraSystemApiConfig){
		byte Exist = 0;
		extraSystemApiConfig.setId(DataUtils.uuid());
		extraSystemApiConfig.setIsDeleted(Exist);
		extraSystemApiConfig.setCreatedOn(new Date());
		extraSystemApiConfig.setModifiedOn(new Date());
		return extraSystemApiConfigMapper.insert(extraSystemApiConfig);
	}

	/**
	 * 更新操作
	 * @Title: update 
	 * @Description: TODO
	 * @param extraSystemApiConfig
	 * @return
	 * @return: int
	 */
	public int update(ExtraSystemApiConfig extraSystemApiConfig){
		extraSystemApiConfig.setModifiedOn(new Date());
		return extraSystemApiConfigMapper.update(extraSystemApiConfig);
	}
	
	/**
	 * 删除操作
	 * @Title: deleteById 
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: int
	 */
	public int deleteById(String id){
		return extraSystemApiConfigMapper.deleteById(id);
	}
	
	public int updateStatusById(String id,String Status){
		return 0;
	}
	
	public BusinessLog getById(String id){
		return null;
	}
}

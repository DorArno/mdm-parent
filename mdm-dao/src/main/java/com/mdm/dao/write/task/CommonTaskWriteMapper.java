package com.mdm.dao.write.task;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.pojo.CommonTask;

@Mapper
public interface CommonTaskWriteMapper {

	List<CommonTask> queryPageList(Map<String, Object> params);

	int insert(CommonTask dmo);
	
	int batchInsert(@Param("list") List<CommonTask> list);

	int update(CommonTask dmo);
	
	int updateStatus(CommonTask dmo);
	
	int deleteById(CommonTask commonTask);

	CommonTask getById(String id);

	List<CommonTask> queryCommonTaskById(String id);
	
	int deleteCommonTaskById(CommonTask commonTask);

}

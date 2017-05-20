package com.mdm.service.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdm.dao.write.task.CommonTaskWriteMapper;
import com.mdm.pojo.CommonTask;
import com.mdm.service.BaseService;

@Service
public class CommonTaskService extends BaseService<CommonTask> {

	@Autowired
	private CommonTaskWriteMapper mapper;
 

	public int insert(CommonTask dmo) {
		int result = mapper.insert(dmo);
		return result;
	}
	
	public int batchInsert(List<CommonTask> list) {
		int result = mapper.batchInsert(list);
		return result;
	}

	public int update(CommonTask dmo) {
		int result = mapper.update(dmo);
		return result;
	}
	
	public int updateStatus(CommonTask dmo) {
		int result = mapper.updateStatus(dmo);
		return result;
	}

	public int deleteById(CommonTask role) {
		int result = mapper.deleteById(role);
		return result;
	}

	public CommonTask getById(String id) {
		return mapper.getById(id);
	}

	@Override
	public List<CommonTask> queryList(Map<String, Object> params) {
		return mapper.queryPageList(params);
	}
 

	public int deleteCommonTaskById(CommonTask commonTask) {
		return mapper.deleteCommonTaskById(commonTask);
	}

}
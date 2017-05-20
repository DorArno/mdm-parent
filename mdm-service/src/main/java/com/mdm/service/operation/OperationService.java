package com.mdm.service.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdm.core.constants.GlobalConstants;
import com.mdm.dao.write.operation.OperationMapper;
import com.mdm.pojo.Module;
import com.mdm.pojo.Operation;
import com.mdm.service.BaseService;

@Service
public class OperationService extends BaseService<Operation> {
	
	@Autowired
	private OperationMapper mapper;

	public int insert(Operation dmo) {
		return mapper.insert(dmo);
	}

	public int update(Operation dmo) {
		return mapper.update(dmo);
	}

	public int deleteById(Operation operation) {
		return mapper.deleteById(operation);
	}

	public int deleteByModuleId(Operation o) {
		return mapper.deleteByModuleId(o);
	}

	public Operation getById(Long id) {
		return mapper.getById(id);
	}
	
	public List<Module> queryModuleList(){
		return mapper.queryModuleList();
	}

	@Override
	public List<Operation> queryList(Map<String, Object> params) {
		return mapper.queryList(params);
	}

	public List<Operation> queryUserRoleOpertionList(String userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		map.put("moduleState", GlobalConstants.STATUS_START);
		return mapper.queryUserRoleOpertionList(map);
	}

}
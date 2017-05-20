package com.mdm.dao.write.operation;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mdm.pojo.Module;
import com.mdm.pojo.Operation;

@Mapper
public interface OperationMapper {

	List<Operation> queryList(Map<String, Object> params);

	int insert(Operation dmo);

	int update(Operation dmo);

	int deleteById(Operation operation);

	int deleteByModuleId(Operation o);

	Operation getById(Long id);

	List<Operation> queryUserRoleOpertionList(Map<String, Object> params);
	
	public List<Module> queryModuleList();

}

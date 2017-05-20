package com.mdm.service.rolemodule;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdm.dao.write.rolemodule.RoleModuleWriteMapper;
import com.mdm.pojo.RoleModule;

@Service
public class RoleModuleService {

	@Autowired
	private RoleModuleWriteMapper mapper;

	public List<RoleModule> queryPageList(Map<String, Object> params) {
		return mapper.queryPageList(params);
	}

	public int insert(List<RoleModule> list) {
		return mapper.insert(list);
	}

	public int update(RoleModule dmo) {
		return mapper.update(dmo);
	}

	public int deleteByRoleId(RoleModule rm) {
		return mapper.deleteByRoleId(rm);
	}

	public int deleteByModuleId(RoleModule rm) {
		return mapper.deleteByModuleId(rm);
	}

	public List<RoleModule> getByRoleId(String roleId, Byte isDeleted) {
		return mapper.getByRoleId(roleId, isDeleted);
	}

}
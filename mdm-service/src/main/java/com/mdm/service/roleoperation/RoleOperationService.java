package com.mdm.service.roleoperation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdm.dao.write.roleoperation.RoleOperationWriteMapper;
import com.mdm.pojo.RoleOperation;

@Service
public class RoleOperationService {

	@Autowired
	private RoleOperationWriteMapper mapper;

	public List<RoleOperation> queryPageList(Map<String, Object> params) {
		return mapper.queryPageList(params);
	}

	public int insert(List<RoleOperation> list) {
		return mapper.insert(list);
	}

	public int update(RoleOperation dmo) {
		return mapper.update(dmo);
	}

	public int deleteByRoleId(RoleOperation ro) {
		return mapper.deleteByRoleId(ro);
	}

	public int deleteByActionId(RoleOperation ro) {
		return mapper.deleteByActionId(ro);
	}
	
	public int deleteByOperationId(RoleOperation ro) {
		return mapper.deleteByOperationId(ro);
	}

	public List<RoleOperation> getByRoleId(String roleId, Byte isDeleted) {
		return mapper.getByRoleId(roleId, isDeleted);
	}

}
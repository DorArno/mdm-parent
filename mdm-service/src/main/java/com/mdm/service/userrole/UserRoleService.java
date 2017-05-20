package com.mdm.service.userrole;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdm.core.constants.GlobalConstants;
import com.mdm.dao.write.userrole.UserroleMapper;

@Service
public class UserRoleService {

	@Autowired
	private UserroleMapper userRoleMapper;

	/**
	 * 判断是否系统管理员
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isAdmin(String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		params.put("level", GlobalConstants.ROLE_LEVEL_0);
		params.put("status", GlobalConstants.STATUS_START);
		return userRoleMapper.queryUserRoleCountByLevel(params) > 0;
	}

}

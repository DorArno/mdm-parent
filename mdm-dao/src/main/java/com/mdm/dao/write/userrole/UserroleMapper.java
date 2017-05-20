package com.mdm.dao.write.userrole;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mdm.pojo.UserOrganizationRole;

@Mapper
public interface UserroleMapper {

	List<UserOrganizationRole> queryPageList(Map<String, Object> params);

	int insert(UserOrganizationRole dmo);

	int update(UserOrganizationRole dmo);

	int deleteById(Long id);

	int deleteByRoleId(UserOrganizationRole ur);

	UserOrganizationRole getById(Long id);

	long queryUserRoleCountByLevel(Map<String, Object> params);

	List<UserOrganizationRole> queryUserRoleByRoleId(Map<String, Object> params);
	
	public List<UserOrganizationRole> queryUserIdByRoleId(Map<String, Object> params);

}

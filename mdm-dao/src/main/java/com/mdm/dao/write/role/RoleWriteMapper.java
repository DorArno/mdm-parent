package com.mdm.dao.write.role;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.pojo.Role;

@Mapper
public interface RoleWriteMapper {

	List<Role> queryPageList(Map<String, Object> params);

	int insert(Role dmo);

	int update(Role dmo);

	int deleteById(Role role);

	Role getById(String id);

	List<Role> queryRoleByUserId(String userId);

	int deleteUserRoleByRoleId(Role role);

	Role getMinLevelRoleByUserId(Map<String, Object> params);
	
	public int queryRoleNameExist(Map<String,Object> params);
	
	List<Role> batchQueryRolesByIds(@Param("list") List<String> list, @Param("isDeleted") String isDeleted, @Param("systemId") String systemId);

}

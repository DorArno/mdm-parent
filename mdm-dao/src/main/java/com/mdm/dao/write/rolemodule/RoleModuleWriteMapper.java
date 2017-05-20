package com.mdm.dao.write.rolemodule;

import com.mdm.pojo.RoleModule;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleModuleWriteMapper {

	List<RoleModule> queryPageList(Map<String, Object> params);

	int insert(List<RoleModule> list);

	int update(RoleModule dmo);

	int deleteByRoleId(RoleModule rm);
	
	int deleteByModuleId(RoleModule rm);

	List<RoleModule> getByRoleId(@Param("roleId") String roleId, @Param("isDeleted") Byte isDeleted);

}

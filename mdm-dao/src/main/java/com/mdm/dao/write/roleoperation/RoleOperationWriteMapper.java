package com.mdm.dao.write.roleoperation;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.pojo.RoleOperation;

@Mapper
public interface RoleOperationWriteMapper {

	List<RoleOperation> queryPageList(Map<String, Object> params);

	int insert(List<RoleOperation> list);

	int update(RoleOperation dmo);

	int deleteByRoleId(RoleOperation ro);

	int deleteByActionId(RoleOperation ro);

	int deleteByOperationId(RoleOperation ro);

	List<RoleOperation> getByRoleId(@Param("roleId") String roleId, @Param("isDeleted") Byte isDeleted);

}

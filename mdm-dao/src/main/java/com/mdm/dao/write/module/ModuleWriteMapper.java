package com.mdm.dao.write.module;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.pojo.Module;

@Mapper
public interface ModuleWriteMapper {

	List<Module> queryList(Map<String, Object> params);

	int insert(Module dmo);

	int update(Module dmo);

	Module getById(String id);

	Module getParent(String id);

	List<Module> queryAll(Map<String, Object> params);

	int updatePath(@Param("originalPath") String originalPath, @Param("path") String path, @Param("level") int level);

	int updateState(@Param("moduleState") Integer moduleState, @Param("path") String path);

	List<Module> getChildren(String parentId);

	int deleteByPath(Module module);

	List<Module> queryModuleOperationList(Byte isDeleted);

	List<Module> queryUserRoleModuleList(Map<String, Object> params);

}

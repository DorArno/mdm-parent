package com.mdm.dao.write.initpath;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.pojo.InitPathEntity;
import com.mdm.pojo.Organazation;

@Mapper
public interface InitPathWriteMapper {

	int update(@Param("tableName")String tableName, @Param("pathColumnName") String pathColumnName, @Param("path")String path,@Param("level") int level,  @Param("id")  String whereid);
//
	List<InitPathEntity> queryAllList(@Param("tableName") String tableName, @Param("parentIdColumnName") String parentIdColumnName, @Param("pathColumnName") String pathColumnName);

	InitPathEntity findById(@Param("tableName") String tableName, @Param("parentIdColumnName") String parentIdColumnName, @Param("pathColumnName") String pathColumnName,    @Param("id") String id);
}

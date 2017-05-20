package com.mdm.dao.write.dataextend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import com.mdm.extend.pojo.DataExtend;

@Mapper
public interface DataExtendWriteMapper {

	int insert(DataExtend dmo);

//	@Cacheable(value="DataExtendCache", key="'DataExtend:'+ #a0 +':' + #a1")
	DataExtend queryByTableNameAndColName(@Param("tableName") String tableName, @Param("colName") String colName);

	int deletById(@Param("dataId") String dataId, @Param("isDeleted") Byte isDeleted,
			@Param("isNotDeleted") Byte isNotDeleted);
	
	int insertBatch(List<DataExtend> list);

}

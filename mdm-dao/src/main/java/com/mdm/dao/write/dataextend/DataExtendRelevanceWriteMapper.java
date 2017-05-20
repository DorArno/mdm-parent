package com.mdm.dao.write.dataextend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.extend.pojo.DataExtendRelevance;
import com.mdm.extend.request.DataExtendRequest;

@Mapper
public interface DataExtendRelevanceWriteMapper {
	  
		int insert(List<DataExtendRelevance> dataExtendRelevance);

		int deleteByDataId(@Param("id") String id);

		List<DataExtendRequest> queryByDataid(@Param("tablename")String tablename, @Param("dataid") String dataid);
}

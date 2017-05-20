package com.mdm.dao.write.sys;

import com.mdm.pojo.DataDeliveryRelation;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataDeliveryRelationMapper {

    List<DataDeliveryRelation> queryPageList(Map<String, Object> params);
    
    int insert(DataDeliveryRelation dmo);

    int update(DataDeliveryRelation dmo);

    int deleteById(Long id);

    DataDeliveryRelation getById(Long id);

}

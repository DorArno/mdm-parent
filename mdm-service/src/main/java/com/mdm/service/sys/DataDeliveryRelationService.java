package com.mdm.service.sys;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdm.dao.write.sys.DataDeliveryRelationMapper;
import com.mdm.pojo.DataDeliveryRelation;

@Service
public class DataDeliveryRelationService {

    @Autowired
    private DataDeliveryRelationMapper mapper;

    public List<DataDeliveryRelation> queryPageList(Map<String, Object> params) {
        return mapper.queryPageList(params);
    }

    public int insert(DataDeliveryRelation dmo) {
        return mapper.insert(dmo);
    }

    public int update(DataDeliveryRelation dmo) {
        return mapper.update(dmo);
    }

    public int deleteById(Long id) {
        return mapper.deleteById(id);
    }

    public DataDeliveryRelation getById(Long id) {
        return mapper.getById(id);
    }

}
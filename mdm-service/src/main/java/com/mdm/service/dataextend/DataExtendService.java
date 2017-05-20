/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月14日 下午2:48:21 *
**/
package com.mdm.service.dataextend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.mdm.common.CommonPojo;
import com.mdm.core.util.BeanUtils;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.HttpContent;
import com.mdm.dao.write.dataextend.DataExtendRelevanceWriteMapper;
import com.mdm.dao.write.dataextend.DataExtendWriteMapper;
import com.mdm.extend.pojo.DataExtend;
import com.mdm.extend.pojo.DataExtendRelevance;
import com.mdm.extend.request.DataExtendRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class DataExtendService {

	@Autowired
	private DataExtendWriteMapper dataExtendWriteMapper;
	@Autowired
	private DataExtendRelevanceWriteMapper dataExtendRelevanceWriteMapper;

	/**
	 * 插入commonPojo中所有扩展字段
	 * 
	 * @param commonPojo
	 * @return
	 */
	public int insertAllExts(CommonPojo commonPojo) {
		if (commonPojo == null || commonPojo.getExts() == null || commonPojo.getExts().isEmpty()) {
			return 0;
		}
		deteleByDataid(commonPojo.getId());
		String tableName = commonPojo.getClass().getSimpleName();
		List<DataExtendRelevance> dataExtendRelevances = new ArrayList<DataExtendRelevance>();
		for (DataExtendRequest dataExtendRequest : commonPojo.getExts()) {
			if (StringUtil.isNotEmpty(tableName) && StringUtil.isNotEmpty(dataExtendRequest.getColName())) {
				DataExtend dataExtend = dataExtendWriteMapper.queryByTableNameAndColName(tableName, dataExtendRequest.getColName());
				if (dataExtend == null) {
					dataExtend = new DataExtend();
					dataExtend.setId(DataUtils.uuid());
//					BeanUtils.copyProperties(dataExtendRequest, dataExtend);
					dataExtend.setTableName(tableName);
					dataExtend.setColLength(dataExtendRequest.getColLength());
					dataExtend.setColName(dataExtendRequest.getColName());
					dataExtend.setColType(dataExtendRequest.getColType());
					dataExtend.setDescription(dataExtendRequest.getDescription());
					insertDataExtend(dataExtend);
				}

				DataExtendRelevance dataExtendRelevance = new DataExtendRelevance();
				dataExtendRelevance.setId(DataUtils.uuid());
				dataExtendRelevance.setTableName(tableName);
				dataExtendRelevance.setColValue(dataExtendRequest.getColValue());
				dataExtendRelevance.setDataId(commonPojo.getId());
				dataExtendRelevance.setDataExtendId(dataExtend.getId());
				dataExtendRelevance.setCreatedOn(new Date());
				dataExtendRelevance.setModifiedOn(new Date());
				dataExtendRelevance.setIsDeleted(0);
				dataExtendRelevance.setCreatedBy(HttpContent.getOperatorId());
				dataExtendRelevance.setModifiedBy(HttpContent.getOperatorId());
				dataExtendRelevances.add(dataExtendRelevance);
			}
		}
		insertDataExtendRelevances(dataExtendRelevances);
		return 0;
	}

	// public List<DataExtendRequest> getExtsByCommonPojo(CommonPojo
	// commonPojo){
	//
	// }

	private int insertDataExtend(DataExtend dataExtend) {
		dataExtend.setCreatedOn(new Date());
		dataExtend.setModifiedOn(new Date());
		dataExtend.setIsDeleted(0);
		dataExtend.setCreatedBy(HttpContent.getOperatorId());
		dataExtend.setModifiedBy(HttpContent.getOperatorId());
		return dataExtendWriteMapper.insert(dataExtend);
	}

	public int insertDataExtendRelevances(List<DataExtendRelevance> dataExtendRelevances) {
		return dataExtendRelevanceWriteMapper.insert(dataExtendRelevances);
	}

	/**
	 * 删除指定记录的所有扩展字段
	 * 
	 * @param id
	 * @return
	 */
	public int deteleByDataid(String id) {
		return dataExtendRelevanceWriteMapper.deleteByDataId(id);
	}

	/**
	 * 查询commonPojo 的所有扩展字段
	 * 
	 * @param commonPojo
	 */
	public void queryExtsByCommonPojo(CommonPojo commonPojo) {
		if (commonPojo == null)
			return;
		String tableName = commonPojo.getClass().getSimpleName();
		List<DataExtendRequest> list = dataExtendRelevanceWriteMapper.queryByDataid(tableName, commonPojo.getId());
		commonPojo.setExts(list);
	}

	/**
	 * 查询List<CommonPojo>数据的扩展字段
	 */
	public void queryExtsByCommonPojoList(List<? extends CommonPojo> commonPojoList) {
		for (CommonPojo commonPojo : commonPojoList) {
			queryExtsByCommonPojo(commonPojo);
		}
	}

	public int deletById(String dataId, Byte isDeleted, Byte isNotDeleted) {
		return dataExtendWriteMapper.deletById(dataId, isDeleted, isNotDeleted);
	}

	public int insertBatch(List<DataExtend> list) {
		return dataExtendWriteMapper.insertBatch(list);
	}

}

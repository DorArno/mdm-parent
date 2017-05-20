/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DistrictMapper.java 
 * @Prject: mdm-dao
 * @Package: com.mdm.user.dao.write 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年9月27日 下午4:14:13 
 * @version: V1.0   
 */
package com.mdm.dao.district;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.exportBean.DistrictExport;
import com.mdm.importBean.DistrictCodeId;
import com.mdm.pojo.District;

/**
 * @ClassName: DistrictMapper
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年9月27日 下午4:14:13
 */
@Mapper
public interface DistrictMapper {
	/**
	 * 插入行政区划
	 * @Title: insert
	 * @Description: TODO
	 * @param district
	 * @return
	 * @return: int
	 */
	int insert(District district);
	/**
	 * 根据id查询行政区划
	 * @Title: queryById
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: District
	 */
	District queryById(@Param("id") String id);
	/**
	 * 根据父id查询行政区划
	 * @Title: queryByParentId
	 * @Description: TODO
	 * @param parentId
	 * @param name
	 * @param code
	 * @return
	 * @return: List<District>
	 */
	List<District> queryByParentId(District district);
	/**
	 * 删除行政区划
	 * @Title: delete
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: int
	 */
	int delete(@Param("id") String id);
	/**
	 * 修改行政区划
	 * @Title: update
	 * @Description: TODO
	 * @param district
	 * @return
	 * @return: int
	 */
	int update(District district);
	
	/**
	 * 查询所有数据的Code与Id的Map
	 * 
	 * @return
	 */
	List<DistrictCodeId> queryCodeIdMap();

	/**
	 *获取所有3级以上行政区划Code与Id的Map
	 * @return
	 */
	List<DistrictCodeId> queryAllLevel3List();
	/**
	 * 批量新增
	 * 
	 * @param list
	 * @return
	 */
	int batchInsert(@Param("list") List<District> list);
	/**
	 * 批量修改
	 * 
	 * @param list
	 * @return
	 */
	int batchUpdate(@Param("list") List<District> list);
	/**
	 * 导出行政区划
	 * @param district
	 * @return
	 */
	List<DistrictExport> exportDistrictList(District district);
}

/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月11日 下午5:18:57 *
**/ 
package com.mdm.dao.write.community;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.importBean.CommunityCodeId;
import com.mdm.pojo.Community;

@Mapper
public interface CommunityWriteMapper {

	  List<Community> queryCommunityByAreaid(@Param(value="areaId") String areaId);
	  
	  List<CommunityCodeId> queryCodeIdMap();
	  
	  /**
	   * 根据id查询社区信息
	   * @Title: queryCommunityById 
	   * @Description: TODO
	   * @param id
	   * @return
	   * @return: Community
	   */
	  Community queryCommunityById(@Param(value="id") String id);
}

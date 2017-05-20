package com.mdm.dao.write.organazation;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.exportBean.OrganizationExport;
import com.mdm.importBean.OrganizationCodeId;
import com.mdm.pojo.Organazation;

@Mapper
public interface OrganazationWriteMapper {
	   
//	    List<Organazation> queryPageList(Map<String, Object> params);
	    
	    int insert(Organazation dmo);

	    int update(Organazation dmo);
	    
	    int updateFromCss(Organazation dmo);

	    int deleteById(String id);

	    Organazation getById(String id);

		List<Organazation> queryOrganazationList(@Param("orgType") String orgType,@Param("corpCode")  String corpCode, @Param("sourceid")  String sourceid, @Param("beginDate")  String beginDate, @Param("endDate")  String endDate, @Param("orgCode") String orgCode,@Param("orgName") String orgName, @Param("parentId")  String parentid, @Param("parentPath")  String parentpath);
		
		List<Organazation> queryOrganazationListForTree(@Param("orgType") String orgType,@Param("corpCode")  String corpCode, @Param("status")  int status);
		
		List<Organazation> queryOrganazationRoot(@Param("orgType")String orgType,@Param("status")Integer status);
		
		List<Organazation> queryAllList();
		

		List<Organazation> queryOrganazationByParentid(@Param("parentId")  String parentid,@Param("status")Integer status);

		Organazation queryByCode(@Param("code") String orgCode);
		
		Organazation queryByCodeAndCorpCodeAndSystemId(@Param("code") String orgCode,  @Param("corpCode") String corpCode,  @Param("systemId") String systemId);
		
		List<Organazation> batchGetByIds(List<String> list);
		
		List<OrganizationExport> exportOrganizationList(@Param("orgType") String orgType,@Param("corpCode")  String corpCode, @Param("sourceid")  String sourceid, @Param("beginDate")  String beginDate, @Param("endDate")  String endDate, @Param("orgCode") String orgCode,@Param("orgName") String orgName, @Param("parentId")  String parentid, @Param("parentPath")  String parentpath);
		
		List<OrganizationCodeId> queryCodeIdMap();

		int batchInsert(@Param("list") List<Organazation> list);

		int batchUpdate(@Param("list") List<Organazation> list);
		/**
		 * 更新积分帐户
		 * @Title: updatePointAccount 
		 * @Description: TODO
		 * @param pointAccountId
		 * @param id
		 * @param orgType
		 * @return
		 * @return: int
		 */
		int updatePointAccount(@Param("pointAccountId") String pointAccountId, @Param("id") String id, @Param("orgType") String orgType, @Param("isExcepted") boolean isExcepted, @Param("modifiedBy") String modifiedBy);
		/**
		 * 查询积分帐户
		 * @Title: getOrganazationByParams 
		 * @Description: TODO
		 * @param id
		 * @param orgType
		 * @return
		 * @return: Organazation
		 */
		Organazation queryPointAccount(@Param("id") String id, @Param("orgType") String orgType);
		/**
		 * 通过积分账户查询组织
		 * @Title: getOrgIdByAccount 
		 * @Description: TODO
		 * @param pointAccountId
		 * @param orgType
		 * @return
		 * @return: List<Map<String, Object>>
		 */
		List<Map<String, Object>> getOrgIdByAccount(@Param("pointAccountId") String pointAccountId, @Param("orgType") String orgType);
		/**
		 * 清空积分账户、是否排除
		 * @Title: resetPointAccount 
		 * @Description: TODO
		 * @param pointAccount
		 * @return
		 * @return: int
		 */
		int resetPointAccount(@Param("pointAccountId") String pointAccountId);
}

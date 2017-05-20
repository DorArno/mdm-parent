/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MergeUserMapper.java 
 * @Prject: mdm-dao
 * @Package: com.mdm.dao.write.mergeuser 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月24日 下午2:21:35 
 * @version: V1.0   
 */
package com.mdm.dao.write.mergeuser;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.bean.pojo.UserDetailInfo;
import com.mdm.pojo.MergeUserInfo;
import com.mdm.pojo.UserMergeRela;

/**
 * @ClassName: MergeUserMapper
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月24日 下午2:21:35
 */
@Mapper
public interface MergeUserMapper {

	List<MergeUserInfo> queryPageList(Map map);

	int updateMergeUserStatus(@Param(value = "array") String[] id);

	int updateMergeUserDetailStatus(@Param(value = "array") String[] id);

	int mergeUser(UserBasicsInfo userBasicsInfo);

	int mergeUserDetail(UserDetailInfo userDetailInfo);

	/**
	 * @Title: mergeUserRelation
	 * @Description: TODO
	 * @param userMergeRela
	 * @return: void
	 */
	void mergeUserRelation(UserMergeRela userMergeRela);

	/** 
	 * @Title: querySystemId 
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @return: String
	 */
	String querySystemId(String userId);
}

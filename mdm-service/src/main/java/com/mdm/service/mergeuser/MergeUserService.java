/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MergeUserService.java 
 * @Prject: mdm-service
 * @Package: com.mdm.service.mergeuser 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月24日 下午2:17:17 
 * @version: V1.0   
 */
package com.mdm.service.mergeuser;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mdm.core.bean.common.PageResultBean;
import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.bean.pojo.UserDetailInfo;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.DateUtils;
import com.mdm.core.util.EncryptionUtil;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;
import com.mdm.dao.write.mergeuser.MergeUserMapper;
import com.mdm.pojo.MergeUserInfo;
import com.mdm.pojo.UserMergeRela;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * @ClassName: MergeUserService
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月24日 下午2:17:17
 */
@Service
public class MergeUserService {

	@Autowired
	private MergeUserMapper mergeUserMapper;

	@Transactional
	public int mergeUser(MergeUserInfo mergeUserInfo) throws Exception {
		String userId = DataUtils.uuid();
		UserBasicsInfo userBasicsInfo = new UserBasicsInfo();
		UserDetailInfo userDetailInfo = new UserDetailInfo();
		BeanUtils.copyProperties(mergeUserInfo, userBasicsInfo);
		BeanUtils.copyProperties(mergeUserInfo, userDetailInfo);
		userDetailInfo.setId(DataUtils.uuid());
		userDetailInfo.setUserId(userId);
		userDetailInfo.setIsDeleted(String.valueOf(MdmConstants.IS_NOT_DELETE));
		userBasicsInfo.setId(userId);
		userBasicsInfo.setVersion(String.valueOf(System.currentTimeMillis()));
		userBasicsInfo.setPassword(EncryptionUtil.getEncryptionByMD5(MdmConstants.DEFAULT_PASSWORD));
		userBasicsInfo.setStatus(MdmConstants.USER_NORMAL_STATUS);
		userBasicsInfo.setCreatedOn(DateUtils.currentDate());
		userBasicsInfo.setIsDeleted(MdmConstants.IS_NOT_DELETE);
		try{
			mergeUserMapper.mergeUser(userBasicsInfo);
		}catch(DuplicateKeyException e){
			throw new MdmException("用户账号已存在！");
		}
		mergeUserMapper.mergeUserDetail(userDetailInfo);
		String extraUserIds[] = userBasicsInfo.getTmpIdArray().split(",");
		updateUserMergeRela(userId, extraUserIds);
		mergeUserMapper.updateMergeUserStatus(extraUserIds);
		mergeUserMapper.updateMergeUserDetailStatus(extraUserIds);
		return MdmConstants.SUCCESS;
	}

	private UserMergeRela createUserMergeRelaInfo(String userId, String extraUserId) {
		UserMergeRela userMergeRela = new UserMergeRela();
		userMergeRela.setId(DataUtils.uuid());
		userMergeRela.setCreateBy(HttpContent.getOperatorId());
		userMergeRela.setCreateOn(new Date());
		userMergeRela.setExtraUserId(extraUserId);
		userMergeRela.setUserId(userId);
		userMergeRela.setExtraSystemId(mergeUserMapper.querySystemId(userId));
		userMergeRela.setIsDeleted(MdmConstants.IS_NOT_DELETE);
		return userMergeRela;
	}

	private void updateUserMergeRela(String userId, String[] extraUserIds) {
		for (String extraUserId : extraUserIds) {
			UserMergeRela userMergeRela = createUserMergeRelaInfo(userId, extraUserId);
			mergeUserMapper.mergeUserRelation(userMergeRela);
		}
	}

	/**
	 * 查询待合并用户信息
	 * @param <MergeUserInfo>
	 * @Title: queryMergeUserInfo
	 * @Description: TODO
	 * @param userName
	 * @return
	 * @return: int
	 */
	public PageResultBean queryMergeUserInfo(Map<String, String> map, int pageNum, int pageSize) {
		Page<Object> page = PageHelper.startPage(pageNum, pageSize);// 分页与统计总数
		List<MergeUserInfo> list = mergeUserMapper.queryPageList(map);
		PageResultBean pageResult = new PageResultBean();
		pageResult.setList(list);
		pageResult.setTotalCount(page.getTotal());
		return pageResult;
	}

	/**
	 * 增加合并后用户信息
	 * @Title: addMergeUserInfo
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	public int addMergeUserInfo() {
		return 0;
	}

	/**
	 * 删除合并后的原始合并信息
	 * @Title: deleteMergeUserInfo
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	public int deleteMergeUserInfo() {
		return 0;
	}
}

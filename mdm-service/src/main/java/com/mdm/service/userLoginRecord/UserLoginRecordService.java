package com.mdm.service.userLoginRecord;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mdm.common.PageResultBean;
import com.mdm.dao.write.userLoginRecord.UserLoginRecordMapper;
import com.mdm.request.UserLoginRecordRequest;
import com.mdm.response.UserLoginResponse;

/**
 * 
 * @ClassName: UserLoginRecordService 
 * @Description: TODO
 * @author: Han
 * @date: 2016年11月10日 下午2:29:25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserLoginRecordService {
	
	@Autowired
	private UserLoginRecordMapper userLoginRecordMapper;
	
	/**
	 * 
	 * @Title: queryUserLoginRecordList 
	 * @Description: TODO
	 * @param userLoginRecordRequest
	 * @return
	 * @return: PageResultBean
	 */
	public PageResultBean queryUserLoginRecordList(@Param("userLoginRecordRequest") UserLoginRecordRequest userLoginRecordRequest) {
		Page<Object> page = PageHelper.startPage(userLoginRecordRequest.getPageNum(), userLoginRecordRequest.getPageSize());
		List<UserLoginResponse> userLoginRecordList = userLoginRecordMapper.queryUserLoginRecordList(userLoginRecordRequest);
		PageResultBean pageResult = new PageResultBean();
		pageResult.setList(userLoginRecordList);
		pageResult.setTotalCount(page.getTotal());
		
		return pageResult;
	}
}

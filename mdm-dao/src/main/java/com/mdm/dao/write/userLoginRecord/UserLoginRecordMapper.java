package com.mdm.dao.write.userLoginRecord;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mdm.request.UserLoginRecordRequest;
import com.mdm.response.UserLoginResponse;

/**
 * 
 * @ClassName: UserLoginRecordMapper 
 * @Description: TODO
 * @author: Han
 * @date: 2016年11月10日 下午2:37:02
 */
@Mapper
public interface UserLoginRecordMapper {
	
	/**
	 * 
	 * @Title: queryUserLoginRecordList 
	 * @Description: TODO
	 * @param userLoginRecordRequest
	 * @return
	 * @return: List<UserLoginResponse>
	 */
	List<UserLoginResponse> queryUserLoginRecordList(UserLoginRecordRequest userLoginRecordRequest);
}

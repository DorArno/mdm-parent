/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: LoginService.java 
 * @Prject: mdm-service
 * @Package: com.mdm.service.login 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月19日 下午1:58:13 
 * @version: V1.0   
 */
package com.mdm.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.dao.write.login.LoginCheckMapper;
import com.mdm.dao.write.user.UserWriteMapper;

import java.util.List;

/** 
 * @ClassName: LoginService 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月19日 下午1:58:13  
 */
@Service
public class LoginService {
	
	@Autowired
	private LoginCheckMapper loginCheckMapper;
	@Autowired
	private UserWriteMapper userWriteMapper;
	
	/**
	 * 登陆验证
	 * @param <UserBasicsInfo>
	 * @Title: loginCheck 
	 * @Description: TODO
	 * @param username
	 * @param password
	 * @return
	 * @return: int
	 */
	public List<UserBasicsInfo> loginCheck(String username,String password){
		return loginCheckMapper.check(username,password);
	}
	
	public com.mdm.pojo.UserBasicsInfo queryUserAccountOrPhone(String username){
//		return userWriteMapper.queryUserAccountOrCellphone(username); //账号
		return userWriteMapper.queryUserAccountOrCellphoneNoS(username); //账号
	}
}

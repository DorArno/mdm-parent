package com.mdm.dao.write.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.pojo.UserBasicsInfo;
import com.mdm.pojo.UserChannel;
import com.mdm.pojo.UserDetailInfo;
import com.mdm.pojo.UserOrganizationRole;
import com.mdm.request.UserGridRequest;
import com.mdm.response.UserGridResponse;

/**
 * @ClassName: UserWriteMapper
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月11日 下午4:14:13
 */
@Mapper
public interface UserWriteMapper {
	/**
	 * 新增用户基础信息
	 * @Title: insertUserBasicsInfo
	 * @Description: TODO
	 * @param userBasicsInfo
	 * @return
	 * @return: string
	 */
	int insertUserBasicsInfo(UserBasicsInfo userBasicsInfo);
	/**
	 * 更新用户基础信息
	 * @Title: updateUserBasicsInfo
	 * @Description: TODO
	 * @param userBasicsInfo
	 * @return
	 * @return: int
	 */
	int updateUserBasicsInfo(@Param("userBasicsInfo") UserBasicsInfo userBasicsInfo, @Param("version") String version);
	/**
	 * 删除用户基础信息
	 * @Title: deleteUserBasicsInfo
	 * @Description: TODO
	 * @param userBasicsInfo
	 * @return
	 * @return: int
	 */
	int deleteUserBasicsInfo(@Param("id") String id, @Param("version") String version,
			@Param("modifiedBy") String modifiedBy);
	/**
	 * 查询用户基础信息
	 * @Title: queryUserBasicsInfoById
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: UserBasicsInfo
	 */
	UserBasicsInfo queryUserBasicsInfoById(@Param("id") String id);
	
	/**
	 * 查询单条用户信息（MQ下发使用）
	 * @Title: queryUserBasicsInfoForMQById 
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: UserBasicsInfo
	 */
	UserBasicsInfo queryUserBasicsInfoForMQById(@Param("id") String id);
	/**
	 * 根据组织机构编号获取用户基础信息列表（不展示前端用户信息）
	 * @Title: queryUserBasicsInfoByParentId
	 * @Description: TODO
	 * @param organizationId
	 * @return
	 * @return: List<UserBasicsInfo>
	 */
	List<UserGridResponse> queryUserListByOrganizationId(UserGridRequest userGridRequest);
	/**
	 * 按条件，查询用户数据条数
	 * @Title: queryUserListByOrganizationIdCount 
	 * @Description: TODO
	 * @param userGridRequest
	 * @return
	 * @return: Integer
	 */
	Integer queryUserListByOrganizationIdCount(UserGridRequest userGridRequest);
	/**
	 * 查询前端用户列表
	 * @Title: queryMemberList 
	 * @Description: TODO
	 * @param userGridRequest
	 * @return
	 * @return: List<UserGridResponse>
	 */
	List<UserGridResponse> queryMemberList(UserGridRequest userGridRequest);
	/**
	 * 新增用户明细信息
	 * @Title: insertUserDetailInfo
	 * @Description: TODO
	 * @param userBasicsInfo
	 * @return
	 * @return: int
	 */
	int insertUserDetailInfo(UserDetailInfo userDetailInfo);
	/**
	 * 更新用户明细信息
	 * @Title: updateUserDetailInfo
	 * @Description: TODO
	 * @param userBasicsInfo
	 * @return
	 * @return: int
	 */
	int updateUserDetailInfo(UserDetailInfo userDetailInfo);
	/**
	 * 删除用户明细信息
	 * @Title: deleteUserDetailInfo
	 * @Description: TODO
	 * @param userBasicsInfo
	 * @return
	 * @return: int
	 */
	int deleteUserDetailInfo(@Param("id") String id, @Param("version") String version,
			@Param("modifiedBy") String modifiedBy);
	/**
	 * 根据用户编号获取用户明细信息
	 * @Title: queryUserDetailInfoById
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @return: UserDetailInfo
	 */
	UserDetailInfo queryUserDetailInfoByUserId(@Param("userId") String userId);
	/**
	 * 根据用户编号获取用户组织角色列表
	 * @Title: queryUserOrganizationRoleList
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @return: List<UserRole>
	 */
	List<UserOrganizationRole> queryUserOrganizationRoleList(@Param("userId") String userId);
	/**
	 * 根据用户id删除用户组织角色关联数据
	 * @Title: deleteUserOrganizationRole
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @return: int
	 */
	int deleteUserOrganizationRole(@Param("userId") String userId, @Param("systemId") String systemId);
	/**
	 * 根据角色id删除用户组织角色关联数据
	 * @Title: deleteUserOrganizationRoleByRoleId 
	 * @Description: TODO
	 * @param roleId
	 * @return
	 * @return: int
	 */
	int deleteUserOrganizationRoleByRoleId(@Param("roleId") String roleId);
	/**
	 * 批量新增用户组织角色关联关系数据
	 * @Title: insertUserOrganizationRoleList
	 * @Description: TODO
	 * @param userOrganizationRoleList
	 * @return
	 * @return: int
	 */
	int insertUserOrganizationRoleList(List<UserOrganizationRole> userOrganizationRoleList);
	/**
	 * 批量新增用户渠道关联数据
	 * @Title: insertUserChannelList
	 * @Description: TODO
	 * @param userChannelList
	 * @return
	 * @return: int
	 */
	int insertUserChannelList(List<UserChannel> userChannelList);
	/**
	 * 根据用户id删除用户渠道关联数据
	 * @Title: deleteUserChannel
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @return: int
	 */
	int deleteUserChannel(String userId);
	/**
	 * 根据用户编号查询用户渠道列表
	 * @Title: queryUserChannelList
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @return: List<UserChannel>
	 */
	List<UserChannel> queryUserChannelList(String userId);
	
	List<UserChannel> queryUserChannelForDel(String userId);
	/**
	 * 根据账号查询用户基本信息
	 * @Title: queryUserAccount 
	 * @Description: TODO
	 * @param account
	 * @return
	 * @return: UserBasicsInfo
	 */
	UserBasicsInfo queryUserAccount(@Param("account") String account);
	/**
	 * 根据手机号查询用户基本信息（已认证）
	 * @Title: queryUserByCellphone 
	 * @Description: TODO
	 * @param cellphone
	 * @return
	 * @return: UserBasicsInfo
	 */
	UserBasicsInfo queryUserByCellphone(@Param("cellphone") String cellphone);
	
	/**
	 * 更具手机号码查询用户基本信息
	 * @Title: queryUserByCellphoneNoS 
	 * @Description: TODO
	 * @param cellphone
	 * @return
	 * @return: UserBasicsInfo
	 */
	UserBasicsInfo queryUserByCellphoneNoS(@Param("cellphone") String cellphone);
	/**
	 * 根据手机号或账号查询用户基本信息（已认证）
	 * @Title: queryUserAccountOrCellphone 
	 * @Description: TODO
	 * @param account
	 * @return
	 * @return: UserBasicsInfo
	 */
	UserBasicsInfo queryUserAccountOrCellphone(@Param("account") String account);
	
	/**
	 * 根据手机号查询用户基本信息
	 * @Title: queryUserAccountOrCellphoneNoS 
	 * @Description: TODO
	 * @param account
	 * @return
	 * @return: UserBasicsInfo
	 */
	UserBasicsInfo queryUserAccountOrCellphoneNoS(@Param("account") String account);
	/**
	 * 批量删除用户
	 * @Title: batchDeleteUsers 
	 * @Description: TODO
	 * @param ids
	 * @param version
	 * @return
	 * @return: int
	 */
	int batchDeleteUsers(@Param("ids") List<String> ids, @Param("version") String version);
	/**
	 * 修改用户密码
	 * @Title: updatePasswordByPassword
	 * @Description: TODO
	 * @param id
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 * @return: int
	 */
	int updatePasswordByPassword(@Param("id") String id, @Param("newPassword") String newPassword,
			@Param("oldPassword") String oldPassword, @Param("version") String version);
	/**
	 * 重置密码
	 * @Title: resetPassword 
	 * @Description: TODO
	 * @param id
	 * @param password
	 * @param version
	 * @return
	 * @return: int
	 */
	int resetPassword(@Param("id") String id, @Param("password") String password, @Param("version") String version);
	/**
	 * 批量更新用户状态
	 * @Title: batchUpdateStatus 
	 * @Description: TODO
	 * @param ids
	 * @param status
	 * @param version
	 * @return
	 * @return: int
	 */
	int batchUpdateStatus(@Param("ids") List<String> ids, @Param("status") int status,
			@Param("version") String version);
	/**
	 * 验证是否存在手机号码（已认证）
	 * @Title: validateCellPhone 
	 * @Description: TODO
	 * @param cellPhone
	 * @param id
	 * @return
	 * @return: int
	 */
	int validateCellPhone(@Param("cellPhone") String cellPhone, @Param("id") String id);
	
	/**
	 * 验证是否存在手机号码
	 * @Title: validateCellPhoneNoS 
	 * @Description: TODO
	 * @param cellPhone
	 * @param id
	 * @return
	 * @return: int
	 */
	int validateCellPhoneNoS(@Param("cellPhone") String cellPhone, @Param("id") String id);
	/**
	 * 验证是否存在账号
	 * @Title: validateAccount 
	 * @Description: TODO
	 * @param account
	 * @param id
	 * @return
	 * @return: int
	 */
	int validateAccount(@Param("account") String account, @Param("id") String id);
	/**
	 * 批量修改（重置）密码
	 * @Title: batchUsersResetPassword
	 * @Description: TODO
	 * @param ids
	 * @param version
	 * @param password
	 * @return
	 * @return: int
	 */
	int batchUsersResetPassword(@Param("ids") List<String> ids, @Param("version") String version,
			@Param("password") String password);
	/**
	 * 验证手机号是否重复
	 * @Title: verifyPhone
	 * @Description: TODO
	 * @param cellPhont
	 * @param id
	 * @return
	 * @return: int
	 */
	int verifyPhone(@Param("cellPhone") String cellPhone, @Param("id") String id);
	/**
	 * 验证密码是否正确
	 * @Title: verifyPhone
	 * @Description: TODO
	 * @param cellPhont
	 * @param id
	 * @return
	 * @return: int
	 */
	int validatePassword(@Param("password") String password, @Param("id") String id);
	
	/**
	 * 是否为商家类型
	 * @Title: isMerChant
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: int
	 */
	int isMerChant(@Param("id") String id);
	/**
	 * 恢复数据（用户基础信息）
	 * @Title: recoverUserBasicsInfo
	 * @Description: TODO
	 * @param userBasicsInfo
	 * @param version
	 * @return
	 * @return: int
	 */
	int recoverUserBasicsInfo(@Param("userBasicsInfo") UserBasicsInfo userBasicsInfo, @Param("version") String version);
	/**
	 * 恢复数据（用户详细信息）
	 * @Title: recoverUserDetailInfo
	 * @Description: TODO
	 * @param userDetailInfo
	 * @return
	 * @return: int
	 */
	int recoverUserDetailInfo(UserDetailInfo userDetailInfo);
	/**
	 * 删除用户渠道信息（后端）
	 * @Title: deleteBackEndUserChannel
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @return: int
	 */
	int deleteBackEndUserChannel(String userId);
	/**
	 * 根据用户渠道编码，查询用户渠道
	 * @Title: queryBackEndUserChannelList
	 * @Description: TODO
	 * @param userId
	 * @param channelCode
	 * @return
	 * @return: List<UserChannel>
	 */
	List<UserChannel> queryBackEndUserChannelList(@Param("userId") String userId,
			@Param("channelCode") Integer channelCode);
	/**
	 * 获取下一个PK
	 * @return
	 */
	int nextPK();
	/**
	 * 判断用户是否属于某种组织类型
	 * @Title: validateUserType
	 * @Description: TODO
	 * @param userId
	 * @param orgType
	 * @return
	 * @return: int
	 */
	int validateUserType(@Param("userId") String userId, @Param("orgType") String orgType);
}

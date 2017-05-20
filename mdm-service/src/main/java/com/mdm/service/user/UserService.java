/**
 * Copyright © 2016 Arvato. All rights reserved.
 *
 * @Title: UserService.java
 * @Prject: mdm-service
 * @Package: com.mdm.service.user
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月12日 上午9:54:15
 * @version: V1.0
 */
package com.mdm.service.user;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.arvato.operation.platform.tsg.util.RestUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.mdm.common.PageResultBean;
import com.mdm.core.bean.pojo.StaticData;
import com.mdm.core.bean.response.SystemInfoResponse;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.dao.write.invokesystem.SystemInfoWriteMapper;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.enums.LogTypeEnum;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.service.sys.StaticDataService;
import com.mdm.core.util.BeanUtils;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.DateUtils;
import com.mdm.core.util.EncryptionUtil;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MD5Util;
import com.mdm.core.util.MdmException;
import com.mdm.dao.write.community.CommunityWriteMapper;
import com.mdm.dao.write.role.RoleWriteMapper;
import com.mdm.dao.write.user.UserWriteMapper;
import com.mdm.extend.request.DataExtendRequest;
import com.mdm.mq.producter.MQSenderAdapter;
import com.mdm.pojo.Community;
import com.mdm.pojo.Merchant;
import com.mdm.pojo.Organazation;
import com.mdm.pojo.Role;
import com.mdm.pojo.UserBasicsInfo;
import com.mdm.pojo.UserChannel;
import com.mdm.pojo.UserDetailInfo;
import com.mdm.pojo.UserOrganizationRole;
import com.mdm.request.CaptchaLoginRequest;
import com.mdm.request.CaptchaUpdateUserPasswordRequest;
import com.mdm.request.CaptchaUpdateUserPasswordRequest2;
import com.mdm.request.UnBindingPhoneRequest;
import com.mdm.request.UploadMember;
import com.mdm.request.UserBasicsInfoRequest;
import com.mdm.request.UserDetailInfoRequest;
import com.mdm.request.UserGridRequest;
import com.mdm.request.UserInfoRequest;
import com.mdm.request.UserInfoSFRequest;
import com.mdm.request.UserRegisterRequest;
import com.mdm.request.UserUpdatePhoneRequest;
import com.mdm.response.CommonPojoResponse;
import com.mdm.response.ResetPasswordResponse;
import com.mdm.response.TreeResponse;
import com.mdm.response.UserGridResponse;
import com.mdm.response.UserInfoEditResponse;
import com.mdm.response.UserInfoMqResponse;
import com.mdm.response.UserInfoResponse;
import com.mdm.response.UserInsertResponse;
import com.mdm.response.UserOrganizationRoleMqResponse;
import com.mdm.service.captchal.CaptchaService;
import com.mdm.service.dataextend.DataExtendService;
import com.mdm.service.merchant.MerchantService;
import com.mdm.service.organazation.OrganazationService;
import com.mdm.service.role.RoleService;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月12日 上午9:54:15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserWriteMapper userWriteMapper;
	@Autowired
	private CaptchaService captchaService;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private OrganazationService organazationService;
	@Autowired
	private InvokeSystemService invokeSystemService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleWriteMapper roleWriteMapper;
	@Autowired
	DataExtendService dataExtendService;
	@Autowired
	private MQSenderAdapter mqSenderAdapter;
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private SystemInfoWriteMapper systemInfoWriteMapper;
	@Autowired
	private CommunityWriteMapper communityWriteMapper;
	@Value("${sms.server.host}")
	private String serverHost;
	@Value("${sms.single.url}")
	private String singleUrl;
	@Value("${sms.partnerId.key}")
	private String partKey;
	@Value("${sms.partnerId.encryptKey}")
	private String encryptKey;

	/**
	 * 新增用户信息
	 * @param userBasicsInfo
	 * @return
	 * @throws MdmException
	 * @Title: insertUserBasicsInfo
	 * @Description: TODO
	 * @return: int
	 */
	public UserInsertResponse insertUserInfo(UserInfoRequest userInfoRequest) throws MdmException {
		// 验证手机是否可用
		logger.info("正常新增流程，验证手机号是否重复（新增）");
		validateCellPhoneNoS(null, userInfoRequest.getUserBasicsInfoRequest().getCellPhone());
		UserBasicsInfo userBasicsInfo = new UserBasicsInfo();
		UserDetailInfo userDetailInfo = new UserDetailInfo();
		BeanUtils.copyProperties(userInfoRequest.getUserBasicsInfoRequest(), userBasicsInfo);
		BeanUtils.copyProperties(userInfoRequest.getUserDetailInfoRequest(), userDetailInfo);
		String systemId = invokeSystemService.querySystemId(userBasicsInfo.getSystemId(),
				userBasicsInfo.getSystemCode());
		String usysCode = invokeSystemService.querySysCodeById(userBasicsInfo.getSystemId(),
				userBasicsInfo.getSystemCode());
		// 初始化用户基础信息，返回用户编号
		String userId = userBasicsInfo.init(systemId, usysCode);
		// 设置手机绑定状态
		userBasicsInfo.setPhoneNumberConfirmed(userDetailInfo.getPhoneNumberConfirmed());
		// 初始化用户渠道信息
		if (null == userBasicsInfo.getUserChannels()) {
			userBasicsInfo.setUserChannels(0);
		}
		if (null != userInfoRequest.getUserChannel() && userInfoRequest.getUserChannel().size() > 0) {
			int memberNum = 0;
			for (UserChannel userChannel : userInfoRequest.getUserChannel()) {
				userChannel.init(userId);
				int channelCode = Integer.parseInt(userChannel.getChannelCode());
				double channelValue = Math.pow(2, channelCode);
				userBasicsInfo
						.setUserChannels(userBasicsInfo.getUserChannels() | (new Double(channelValue)).intValue());
				if (!userChannel.getChannelCode().equals(MdmConstants.BACKEND.toString())) {
					memberNum = 1;
				}
			}
			if (memberNum < 1) {
				UserChannel channel = new UserChannel();
				channel.init(userId);
				channel.setChannelCode(MdmConstants.MEMBER_THREE.toString());
				userInfoRequest.getUserChannel().add(channel);
				double channelValue = Math.pow(2, MdmConstants.MEMBER_THREE);
				userBasicsInfo
						.setUserChannels(userBasicsInfo.getUserChannels() | (new Double(channelValue)).intValue());
			}
		}
		setCorpcode(usysCode, userBasicsInfo);
		// 设置用户类型（组织/商家）
		if (null != userInfoRequest.getUserRoleRequest() && userInfoRequest.getUserRoleRequest().size() > 0) {
			getUserType(userInfoRequest.getUserRoleRequest(), userBasicsInfo);
		}
		logger.info("正常新增流程，插入数据库基础表数据（新增）");
		userWriteMapper.insertUserBasicsInfo(userBasicsInfo);
		dataExtendService.insertAllExts(userBasicsInfo);
		userDetailInfo.init(userId);
		logger.info("正常新增流程，插入详情表数据（新增）");
		userWriteMapper.insertUserDetailInfo(userDetailInfo);
		// 批量新增用户渠道信息
		if (null != userInfoRequest.getUserChannel() && userInfoRequest.getUserChannel().size() > 0) {
			userWriteMapper.insertUserChannelList(userInfoRequest.getUserChannel());
		}
		if (null != userInfoRequest.getUserRoleRequest() && userInfoRequest.getUserRoleRequest().size() > 0) {
			logger.info("正常新增流程，处理组织角色关系（新增）");
			// 去除重复组织角色
			removeRepeat(userInfoRequest.getUserRoleRequest());
			// 初始化用户组织结构角色关联信息
			userInfoRequest.getUserRoleRequest().forEach(model -> {
				String sysId = invokeSystemService.querySystemId(model.getSystemId(), model.getSystemCode());
				model.init(userId, sysId);
				// 用户组织机构下发，根据systemId初始化systemCode
				String sysCode = invokeSystemService.querySysCodeById(model.getSystemId(), model.getSystemCode());
				model.setSystemCode(sysCode);
			});
			// 批量新增用户组织角色信息
			userWriteMapper.insertUserOrganizationRoleList(userInfoRequest.getUserRoleRequest());
			// 初始化用户组织机构角色mq消息队列实体
			UserOrganizationRoleMqResponse userOrganizationRoleMqResponse = new UserOrganizationRoleMqResponse();
			userOrganizationRoleMqResponse.init(userId, userInfoRequest.getUserRoleRequest(),
					userBasicsInfo.getCorpCode());
			// 发送用户组织机构角色mq消息、记录操作日志
			mqSenderAdapter.sendMQ(LogTypeEnum.ADD.getValue(), DataTypeEnum.SOUR.getValue(),
					userOrganizationRoleMqResponse);
		}
		// 初始化用户信息mq消息队列实体
		UserInfoMqResponse userInfoMqResponse = new UserInfoMqResponse();
		userBasicsInfo = userWriteMapper.queryUserBasicsInfoForMQById(userBasicsInfo.getId());
		userInfoMqResponse.init(userBasicsInfo, userDetailInfo, userInfoRequest.getUserChannel());
		// 发送用户信息MQ消息、记录操作日志
		mqSenderAdapter.sendMQ(LogTypeEnum.ADD.getValue(), DataTypeEnum.USER.getValue(), userInfoMqResponse);
		logger.info("正常新增流程结束（新增）");
		return new UserInsertResponse(userBasicsInfo.getId(), userBasicsInfo.getVersion(), "新增");
	}
	/**
	 * 合并用户
	 * @Title: mergeUserInfo
	 * @Description: TODO
	 * @param userInfoRequest
	 * @return
	 * @throws MdmException
	 * @return: CommonPojoResponse
	 */
	public UserInsertResponse mergeUserInfo(UserInfoRequest userInfoRequest) throws MdmException {
		String jsonStr = JSONObject.toJSONString(userInfoRequest);
		logger.info("（新增）业务系统上报用户数据：" + jsonStr);
		String cellPhone = userInfoRequest.getUserBasicsInfoRequest().getCellPhone();
		Integer phoneNumberConfirmed = userInfoRequest.getUserDetailInfoRequest().getPhoneNumberConfirmed();
		// 1 手机号是否为空
		if (null == cellPhone || cellPhone.isEmpty()) {
			throw new MdmException("手机号不能为空");
		} else if (null == phoneNumberConfirmed || phoneNumberConfirmed < 1) {
			throw new MdmException("手机号不能为未认证");
		} else {
			// 2 手机号验证存在，返回id，version(查询到相同手机号码，就可以做合并处理)
			UserBasicsInfo resultInfo = userWriteMapper.queryUserByCellphoneNoS(cellPhone);
			if (null != resultInfo) {
				logger.info("存在相同手机号，进行合并（新增）");
				String userId = resultInfo.getId();
				// 判断传递来的存在后端用户,且数据库只有前端用户,添加为后端用户(覆盖原账号)
				handleChannel(userInfoRequest.getUserChannel(), resultInfo);
				if (null != userInfoRequest.getUserRoleRequest() && userInfoRequest.getUserRoleRequest().size() > 0) {
					try {
						logger.info("合并组织角色关系（新增）");
						List<UserOrganizationRole> userOrganizationRoleList = userWriteMapper
								.queryUserOrganizationRoleList(userId);
						if (null == userOrganizationRoleList) {
							userOrganizationRoleList = new ArrayList<UserOrganizationRole>();
						}
						userOrganizationRoleList.addAll(userInfoRequest.getUserRoleRequest());
						// 去除重复组织角色
						removeRepeat(userOrganizationRoleList);
						// 初始化用户组织结构角色关联信息
						userOrganizationRoleList.forEach(model -> {
							String sysId = invokeSystemService.querySystemId(model.getSystemId(),
									model.getSystemCode());
							model.init(userId, sysId);
							// 用户组织机构下发，根据systemId初始化systemCode
							String sysCode = invokeSystemService.querySysCodeById(model.getSystemId(),
									model.getSystemCode());
							model.setSystemCode(sysCode);
						});
						// 合并组织角色关系
						userWriteMapper.deleteUserOrganizationRole(userId, null);
						userWriteMapper.insertUserOrganizationRoleList(userOrganizationRoleList);
					} catch (Exception e) {
						logger.error(e.getMessage());
						throw new MdmException("合并组织角色信息失败");
					}
				}
				resultInfo = userWriteMapper.queryUserByCellphoneNoS(cellPhone);
				
				String newVersion = String.valueOf(System.currentTimeMillis());
				// 重置密码，发送短信通知
				String randomPWD = randomPD(), oldCorpCode = resultInfo.getCorpCode(), newCorpCode = userInfoRequest.getUserBasicsInfoRequest().getCorpCode();
				// 更新type,corpCode
				if (HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.SSY) || HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.MDM)) {
					resultInfo.setType(userInfoRequest.getUserBasicsInfoRequest().getType());
				}
				resultInfo.setCorpCode(userInfoRequest.getUserBasicsInfoRequest().getCorpCode());
				resultInfo.setVersion(newVersion);
				resultInfo.setPhoneNumberConfirmed(phoneNumberConfirmed);
				if (!HttpContent.getSysCode().equals(MdmConstants.MDM)) {
					String sysCode = HttpContent.getSysCode();
					Integer newState = resetBindState(resultInfo.getBindState(), sysCode, MdmConstants.PHONENUMBERCONFIRMED_ONE.toString());
					if (sysCode.equals(MdmConstants.SSY)) {
						newState = resetBindState(newState,MdmConstants.SCM, MdmConstants.PHONENUMBERCONFIRMED_ONE.toString());
					}
					if (sysCode.equals(MdmConstants.SCM)) {
						newState = resetBindState(newState,MdmConstants.SSY, MdmConstants.PHONENUMBERCONFIRMED_ONE.toString());
					}
					resultInfo.setBindState(newState);
				}
				
//				resultInfo.setPassword(EncryptionUtil.getEncryptionByMD5(randomPWD));
//				resultInfo.setEncryptionMode("1");
				logger.info("合并数据时，修改基础表数据（新增）");
				if (null != resultInfo.getEncryptionMode()) {
					resultInfo.seteMode(handleEncryptionMode(resultInfo.getEncryptionMode()));
				}
				userWriteMapper.updateUserBasicsInfo(resultInfo, null);
				// 更新认证状态
				UserDetailInfo detailInfo = new UserDetailInfo();
				detailInfo.setPhoneNumberConfirmed(phoneNumberConfirmed);
				detailInfo.setUserId(userId);
				detailInfo.setVersion(newVersion);
				logger.info("合并数据时，修改详情表数据（新增）");
				userWriteMapper.updateUserDetailInfo(detailInfo);
				// 发送短信
//				sendSMS(randomPWD, cellPhone);
				// MQ下发
				sendMQ(userId);
				// 下发MQ-CSS（解绑原corpCode对应的数据）
				if(HttpContent.getSysCode().equals(MdmConstants.CSS) || HttpContent.getSysCode().equals(MdmConstants.BMS)){
					if (null != oldCorpCode && null != newCorpCode && !oldCorpCode.equalsIgnoreCase(newCorpCode)) {
						logger.info("企业代码发送改变，下发物业解绑MQ（新增）");
						sendMqForCSS(userId, oldCorpCode, cellPhone);
					}
				}
				logger.info("新增合并数据流程结束（新增）");
				return new UserInsertResponse(userId, resultInfo.getVersion(), "合并");
			}
		}
		// 不符合带合并数据，走正常新增操作
		logger.info("不存在相同手机号【走正常新增操作】（新增）");
		UserInsertResponse response = insertUserInfo(userInfoRequest);
		return response;
	}
	/**
	 * 判断传递来的存在后端用户,且数据库只有前端用户,添加为后端用户
	 * @Title: handleChannel
	 * @Description: TODO
	 * @param userChannels
	 * @param resultInfo
	 * @return: void
	 */
	private void handleChannel(List<UserChannel> userChannels, UserBasicsInfo resultInfo) throws MdmException {
		if (null != userChannels && userChannels.size() > 0) {
			for (UserChannel userChannel : userChannels) {
				if (userChannel.getChannelCode().equals(MdmConstants.BACKEND.toString())) {
					if ((resultInfo.getUserChannels() != 0) && ((resultInfo.getUserChannels() & 2) != 2)) {
						// 仅为前端用户(会员),添加为后端用户
						setBackEndUserStrong(resultInfo.getId(), resultInfo.getAccount());
					}
					break;
				}
			}
		}
	}
	/**
	 * 新增用户信息
	 * @Title: insertUserForSF
	 * @Description: TODO
	 * @param userInfoRequest
	 * @return
	 * @throws MdmException
	 * @return: CommonPojoResponse
	 */
	public CommonPojoResponse insertUserForSF(UserInfoSFRequest userInfoRequest) throws MdmException {
		validateCellPhoneNoS(null, userInfoRequest.getUserBasicsInfoRequest().getCellPhone());
		UserBasicsInfo userBasicsInfo = new UserBasicsInfo();
		UserDetailInfo userDetailInfo = new UserDetailInfo();
		BeanUtils.copyProperties(userInfoRequest.getUserBasicsInfoRequest(), userBasicsInfo);
		BeanUtils.copyProperties(userInfoRequest.getUserDetailInfoRequest(), userDetailInfo);
		String systemId = invokeSystemService.querySystemId(userBasicsInfo.getSystemId(),
				userBasicsInfo.getSystemCode());
		String usysCode = invokeSystemService.querySysCodeById(userBasicsInfo.getSystemId(),
				userBasicsInfo.getSystemCode());
		// 初始化用户基础信息，返回用户编号
		String userId = userBasicsInfo.init(systemId, usysCode);
		// 设置手机绑定状态
		userBasicsInfo.setPhoneNumberConfirmed(userDetailInfo.getPhoneNumberConfirmed());
		// 设置默认corpCode
		setCorpcode(usysCode, userBasicsInfo);
		// 初始化渠道
		if (null == userBasicsInfo.getUserChannels()) {
			userBasicsInfo.setUserChannels(0);
		}
		if (null != userInfoRequest.getUserChannel() && userInfoRequest.getUserChannel().size() > 0) {
			userInfoRequest.getUserChannel().forEach(model -> {
				model.init(userId);
				int channelCode = Integer.parseInt(model.getChannelCode());
				double channelValue = Math.pow(2, channelCode);
				userBasicsInfo
						.setUserChannels(userBasicsInfo.getUserChannels() | (new Double(channelValue)).intValue());
			});
		}
		// 新增用户基础信息
		userWriteMapper.insertUserBasicsInfo(userBasicsInfo);
		// 初始化用户明细信息
		userDetailInfo.init(userId);
		// 新增用户明细信息
		userWriteMapper.insertUserDetailInfo(userDetailInfo);
		// 新增用户渠道信息
		if (null != userInfoRequest.getUserChannel() && userInfoRequest.getUserChannel().size() > 0) {
			userWriteMapper.insertUserChannelList(userInfoRequest.getUserChannel());
		}
		// 初始化用户信息mq消息队列实体
		UserInfoMqResponse userInfoMqResponse = new UserInfoMqResponse();
		userInfoMqResponse.init(userBasicsInfo, userDetailInfo, userInfoRequest.getUserChannel());
		// 发送用户信息MQ消息、记录操作日志
		mqSenderAdapter.sendMQ(LogTypeEnum.ADD.getValue(), DataTypeEnum.USER.getValue(), userInfoMqResponse);
		return new CommonPojoResponse(userBasicsInfo.getId(), userBasicsInfo.getVersion());
	}
	/**
	 * 更新用户信息
	 * @Title: updateUserInfo
	 * @Description: TODO
	 * @param userInfoRequest
	 * @return
	 * @throws MdmException
	 * @return: CommonPojoResponse
	 */
	public CommonPojoResponse updateUserInfo(UserInfoRequest userInfoRequest) throws MdmException {
		String jsonStr = JSONObject.toJSONString(userInfoRequest);
		logger.info("（修改）业务系统上报用户数据：" + jsonStr);
		UserBasicsInfo userBasicsInfo = new UserBasicsInfo();
		UserDetailInfo userDetailInfo = new UserDetailInfo();
		BeanUtils.copyProperties(userInfoRequest.getUserBasicsInfoRequest(), userBasicsInfo);
		BeanUtils.copyProperties(userInfoRequest.getUserDetailInfoRequest(), userDetailInfo);
		String userId = userBasicsInfo.getId();
		if (null == userId || userId.isEmpty()) {
			throw new MdmException("上报数据，用户Id不能为空");
		}
		if (null == userBasicsInfo.getCellPhone() || userBasicsInfo.getCellPhone().isEmpty()) {
			throw new MdmException("上报数据，手机号码不能为空");
		}
		if (!HttpContent.getSysCode().equals(MdmConstants.MDM) && (null == userDetailInfo.getPhoneNumberConfirmed()
				|| userDetailInfo.getPhoneNumberConfirmed() == 0)) {
			throw new MdmException("上报数据，手机号码需要认证");
		}
		UserBasicsInfo basicsInfo = userWriteMapper.queryUserBasicsInfoById(userId);
		if (null == basicsInfo) {
			throw new MdmException("主数据Id对应的用户信息不存在");
		}
		boolean b = false;
		if ((basicsInfo.getUserChannels() != 0) && ((basicsInfo.getUserChannels() & 2) != 2)) {
			// 仅为前端用户(会员),允许合并
			b = true;
		}
		
		UserDetailInfo detailInfo = userWriteMapper.queryUserDetailInfoByUserId(userId);
		if (null != detailInfo || b) {
			// 试着获取数据库已认证的相同手机号的用户数据
			UserBasicsInfo userInfo = userWriteMapper.queryUserByCellphoneNoS(userBasicsInfo.getCellPhone());
			if (null != userInfo  && !userId.equalsIgnoreCase(userInfo.getId())) {
				if (userInfo.getBindState() > 0) {
					logger.info("存在相同已绑定手机号【进行合并】（修改）");
					// 合并 userInfo
					userInfoRequest.getUserBasicsInfoRequest().setId(userInfo.getId());
					userInfoRequest.getUserBasicsInfoRequest().setVersion(userInfo.getVersion());
					userInfoRequest.getUserDetailInfoRequest().setUserId(userInfo.getId());
					userInfoRequest.getUserDetailInfoRequest().setVersion(userInfo.getVersion());
					return updateUserInfoInner(userInfoRequest, userInfo.getId(), userId, true, false);
					/* throw new MdmException("其他账号已经绑定该手机号码"); */
				}else{
					logger.info("存在相同未绑定手机号【进行合并】（修改）");
					// 覆盖更新
					userInfoRequest.getUserBasicsInfoRequest().setId(userInfo.getId());
					userInfoRequest.getUserBasicsInfoRequest().setVersion(userInfo.getVersion());
					userInfoRequest.getUserDetailInfoRequest().setUserId(userInfo.getId());
					userInfoRequest.getUserDetailInfoRequest().setVersion(userInfo.getVersion());
					return updateUserInfoInner(userInfoRequest, userInfo.getId(), userId, true, true);
				}
			}
		}
		// 正常更新
		logger.info("不存在可以合并的数据对象【普通修改】（修改）");
		return updateUserInfoInner(userInfoRequest, userId, null, false, false);
	}
	/**
	 * 更新用户信息（内部私有使用）
	 * @Title: updateUserInfoInner
	 * @Description: TODO
	 * @param userInfoRequest
	 * @param userId
	 * @param merge
	 * @return
	 * @throws MdmException
	 * @return: CommonPojoResponse
	 */
	private CommonPojoResponse updateUserInfoInner(UserInfoRequest userInfoRequest, String userId, String oldUserId,
			boolean merge, boolean cover) throws MdmException {
		UserBasicsInfo userBasicsInfo = new UserBasicsInfo();
		UserDetailInfo userDetailInfo = new UserDetailInfo();
		BeanUtils.copyProperties(userInfoRequest.getUserBasicsInfoRequest(), userBasicsInfo);
		BeanUtils.copyProperties(userInfoRequest.getUserDetailInfoRequest(), userDetailInfo);
		String old_version = userInfoRequest.getUserBasicsInfoRequest().getVersion();
		// if (userInfoRequest.getUserDetailInfoRequest().getPhoneNumberConfirmed() == 1) {
		logger.info("验证数据是否重复【除自身数据之外，是否存在相同手机号用户】（修改）");
		validateCellPhoneNoS(userInfoRequest.getUserBasicsInfoRequest().getId(),
				userInfoRequest.getUserBasicsInfoRequest().getCellPhone());
		// }
		// 版本赋值
		userBasicsInfo.initModify();
		userDetailInfo.initModify();
		// 设置默认corpCode
		UserBasicsInfo mUser = userWriteMapper.queryUserBasicsInfoById(userId);
		if (null != mUser && (null == mUser.getCorpCode() || mUser.getCorpCode().isEmpty())) {
			setCorpcode(HttpContent.getSysCode(), userBasicsInfo);
		}
		if (null != mUser) {
			// 获取绑定状态
			userBasicsInfo.setBindState(mUser.getBindState());
		}
		String systemId = invokeSystemService.querySystemId("", HttpContent.getSysCode());
		if (HttpContent.getSysCode().equals("MDM")) {
			systemId = null;
		} else {
			try {
				filterColumns(userBasicsInfo, null, HttpContent.getSysCode());
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new MdmException("控制字段修改权限错误");
			}
		}
		// 设置用户类型（组织/商家）
		if (null != userInfoRequest.getUserRoleRequest() && userInfoRequest.getUserRoleRequest().size() > 0) {
			if (HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.SSY) || HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.MDM)) {
				getUserType(userInfoRequest.getUserRoleRequest(), userBasicsInfo);
			}
		}
		// 初始化用户渠道
		if (null == userBasicsInfo.getUserChannels()) {
			userBasicsInfo.setUserChannels(0);
		}
		// 获取原有数据信息，用于合并
		if (merge) {
			// 渠道
			List<UserChannel> channels = userWriteMapper.queryUserChannelList(oldUserId);
			userInfoRequest.getUserChannel().addAll(channels);
			// 组织角色
			List<UserOrganizationRole> userOrganizationRoleList = userWriteMapper
					.queryUserOrganizationRoleList(oldUserId);
			if (null == userInfoRequest.getUserRoleRequest()) {
				userInfoRequest.setUserRoleRequest(new ArrayList<UserOrganizationRole>());
			}
			userInfoRequest.getUserRoleRequest().addAll(userOrganizationRoleList);
		}
		// 用户渠道合并处理
		if (null != userInfoRequest.getUserChannel() && userInfoRequest.getUserChannel().size() > 0) {
			List<UserChannel> channels = userWriteMapper.queryUserChannelList(userId);
			channels.addAll(userInfoRequest.getUserChannel());
			removeRepeatForChannel(channels);
			channels.forEach(model -> {
				model.init(userId);
				// 设置userChannels
				int channelCode = Integer.parseInt(model.getChannelCode());
				double channelValue = Math.pow(2, channelCode);
				userBasicsInfo
						.setUserChannels(userBasicsInfo.getUserChannels() | (new Double(channelValue)).intValue());
			});
			userInfoRequest.setUserChannel(channels);
		}
		// 更新用户基础信息、用户明细信息
		String randomPWD = randomPD(), oldCorpCode = "",
				newCorpCode = userInfoRequest.getUserBasicsInfoRequest().getCorpCode(),
				oldCellPhone = "";
		if (merge) {
			// 重置为新密码，并以短信方式通知用户
//			userBasicsInfo.setPassword(EncryptionUtil.getEncryptionByMD5(randomPWD));
//			userBasicsInfo.setEncryptionMode("1");
			UserBasicsInfo basicsInfo = userWriteMapper.queryUserBasicsInfoById(oldUserId);
			oldCorpCode = basicsInfo.getCorpCode();
			oldCellPhone = basicsInfo.getCellPhone();
		} else {
			userBasicsInfo.setPassword(null);// 强制去除密码的修改，为null时不做修改
			UserBasicsInfo basicsInfo = userWriteMapper.queryUserBasicsInfoById(userId);
			oldCorpCode = basicsInfo.getCorpCode();
			oldCellPhone = basicsInfo.getCellPhone();
		}
		// 设置手机绑定状态
		userBasicsInfo.setPhoneNumberConfirmed(userDetailInfo.getPhoneNumberConfirmed());
		if (!HttpContent.getSysCode().equals(MdmConstants.MDM)) {
			String sysCode = HttpContent.getSysCode();
			Integer newState = resetBindState(userBasicsInfo.getBindState(),sysCode, MdmConstants.PHONENUMBERCONFIRMED_ONE.toString());
			if (sysCode.equals(MdmConstants.SSY)) {
				newState = resetBindState(newState,MdmConstants.SCM, MdmConstants.PHONENUMBERCONFIRMED_ONE.toString());
			}
			if (sysCode.equals(MdmConstants.SCM)) {
				newState = resetBindState(newState,MdmConstants.SSY, MdmConstants.PHONENUMBERCONFIRMED_ONE.toString());
			}
			userBasicsInfo.setBindState(newState);
		}
		logger.info("修改基础表数据（修改）");
		if (null == userBasicsInfo.getCreatedBy() || "".equals(userBasicsInfo.getCreatedBy())) {
			userBasicsInfo.setCreatedBy(HttpContent.getOperatorId());
		}
		if (null != userBasicsInfo.getEncryptionMode()) {
			userBasicsInfo.seteMode(handleEncryptionMode(userBasicsInfo.getEncryptionMode()));
		}
		int num = userWriteMapper.updateUserBasicsInfo(userBasicsInfo, old_version);
		if (num == 0) {
			String version = userWriteMapper.queryUserBasicsInfoById(userBasicsInfo.getId()).getVersion();
			if (!userBasicsInfo.getVersion().equals(version)) {
				throw new MdmException(MdmConstants.RESCODE_VERSION_FAIL, "版本错误");
			}
		}
		// 扩展字段处理
		dataExtendService.insertAllExts(userBasicsInfo);
		if (HttpContent.getSysCode().equals(MdmConstants.MDM)) {
			systemId = null;
		} else {
			try {
				filterColumns(null, userDetailInfo, HttpContent.getSysCode());
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new MdmException("控制字段修改权限错误");
			}
		}
		logger.info("修改详情表数据（修改）");
		userWriteMapper.updateUserDetailInfo(userDetailInfo);
		if (null != userInfoRequest.getUserChannel() && userInfoRequest.getUserChannel().size() > 0) {
			userWriteMapper.deleteUserChannel(userId);
			userWriteMapper.insertUserChannelList(userInfoRequest.getUserChannel());
		}
		// 初始化用户信息mq消息队列实体
		UserInfoMqResponse userInfoMqResponse = new UserInfoMqResponse();
		// 从数据库获取最新用户信息
		UserBasicsInfo userBasicsInfomq = userWriteMapper.queryUserBasicsInfoForMQById(userId);
		String systemCode = invokeSystemService.querySysCodeById(userBasicsInfomq.getSystemId(), null);
		userBasicsInfomq.setSystemCode(systemCode);
		// 获取用户明细信息
		UserDetailInfo userDetailInfomq = userWriteMapper.queryUserDetailInfoByUserId(userId);
		// 获取用户渠道
		List<UserChannel> userChannel = userWriteMapper.queryUserChannelList(userId);
		userInfoMqResponse.init(userBasicsInfomq, userDetailInfomq, userChannel);
		userInfoMqResponse.getUserBasicsInfoRequest().setPassword(null);// 禁止密码记入操作日志
		
		if (null == userBasicsInfo.getCorpCode()) {
			userBasicsInfo.setCorpCode(oldCorpCode);
		}
		
		if (null != userInfoRequest.getUserRoleRequest() && userInfoRequest.getUserRoleRequest().size() > 0) {
			if (merge) {// 需要做合并处理
				logger.info("处理合并的组织角色关系数据（修改）");
				try {
					List<UserOrganizationRole> userOrganizationRoleList = new ArrayList<UserOrganizationRole>();
					if (!cover) {// 覆盖组织角色
						userOrganizationRoleList.addAll(userWriteMapper.queryUserOrganizationRoleList(userId));
					}
					if ((null != userOrganizationRoleList && userOrganizationRoleList.size() > 0)
							|| userInfoRequest.getUserRoleRequest().size() > 0) {
						userOrganizationRoleList.addAll(userInfoRequest.getUserRoleRequest());
						removeRepeat(userOrganizationRoleList);
						// 初始化用户组织结构角色关联信息
						userOrganizationRoleList.forEach(model -> {
							String sysId = invokeSystemService.querySystemId(model.getSystemId(),
									model.getSystemCode());
							model.init(userId, sysId);
							model.setType(userBasicsInfo.getType());
							// 用户组织机构下发，根据systemId初始化systemCode
							String sysCode = invokeSystemService.querySysCodeById(model.getSystemId(),
									model.getSystemCode());
							model.setSystemCode(sysCode);
						});
						// 合并组织角色关系
						userWriteMapper.deleteUserOrganizationRole(oldUserId, null);// 删除原数据对应的组织角色信息
						userWriteMapper.deleteUserOrganizationRole(userId, null);
						userWriteMapper.insertUserOrganizationRoleList(userOrganizationRoleList);
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new MdmException("合并组织角色信息失败");
				}
			} else {// 正常修改组织角色关系
				logger.info("正常修改组织角色关系（修改）");
				removeRepeat(userInfoRequest.getUserRoleRequest());
				// 根据用户编号删除用户关联的组织机构和角色信息
				if (HttpContent.getSysCode().equals(MdmConstants.CSS)
						|| HttpContent.getSysCode().equals(MdmConstants.BMS)) {
					String css_systemId = invokeSystemService.querySystemId("", MdmConstants.CSS);
					if (null != css_systemId && "" != css_systemId)
						userWriteMapper.deleteUserOrganizationRole(userBasicsInfo.getId(), css_systemId);
					String bms_systemId = invokeSystemService.querySystemId("", MdmConstants.BMS);
					if (null != bms_systemId && "" != bms_systemId)
						userWriteMapper.deleteUserOrganizationRole(userBasicsInfo.getId(), bms_systemId);
				} else if (HttpContent.getSysCode().equals(MdmConstants.SSY)) {
					userWriteMapper.deleteUserOrganizationRole(userBasicsInfo.getId(), systemId);
					String scm_systemId = invokeSystemService.querySystemId("", MdmConstants.SCM);
					if (null != scm_systemId && "" != scm_systemId)
						userWriteMapper.deleteUserOrganizationRole(userBasicsInfo.getId(), scm_systemId);
				} else {
					userWriteMapper.deleteUserOrganizationRole(userBasicsInfo.getId(), systemId);
				}
				if (userInfoRequest.getUserRoleRequest().size() > 0) {
					// 初始化用户组织结构角色关联信息
					userInfoRequest.getUserRoleRequest().forEach(model -> {
						String sysId = invokeSystemService.querySystemId(model.getSystemId(), model.getSystemCode());
						model.init(userId, sysId);
						model.setType(userBasicsInfo.getType());
						// 用户组织机构下发，根据systemId初始化systemCode
						String sysCode = invokeSystemService.querySysCodeById(model.getSystemId(),
								model.getSystemCode());
						model.setSystemCode(sysCode);
					});
					userWriteMapper.insertUserOrganizationRoleList(userInfoRequest.getUserRoleRequest());
				}
			}
			// 初始化用户组织机构角色mq消息队列实体
			UserOrganizationRoleMqResponse userOrganizationRoleMqResponse = new UserOrganizationRoleMqResponse();
			// 下发所有组织角色，重新获取
			List<UserOrganizationRole> userOrganizationRoleList = userWriteMapper.queryUserOrganizationRoleList(userId);
			userOrganizationRoleList.forEach(model -> {
				// 用户组织机构下发，根据systemId初始化systemCode
				String sysCode = invokeSystemService.querySysCodeById(model.getSystemId(), model.getSystemCode());
				model.setSystemCode(sysCode);
			});
			
			userOrganizationRoleMqResponse.init(userId, userOrganizationRoleList, userBasicsInfo.getCorpCode());
			// 发送用户组织机构角色mq消息、记录操作日志
			mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.SOUR.getValue(),
					userOrganizationRoleMqResponse);
			logger.info("组织角色关系，处理完成（修改）");
		}else{
			// 没有上报组织角色关系，但是需要下发所有组织角色给业务系统
			logger.info("没有上报组织角色关系，但是需要下发所有组织角色给业务系统");
			UserOrganizationRoleMqResponse userOrganizationRoleMqResponse = new UserOrganizationRoleMqResponse();
			List<UserOrganizationRole> userOrganizationRoleList = new ArrayList<UserOrganizationRole>();
			userOrganizationRoleList = userWriteMapper.queryUserOrganizationRoleList(userId);
			if (null == userInfoRequest.getUserRoleRequest()) {
				userInfoRequest.setUserRoleRequest(new ArrayList<UserOrganizationRole>());
			}
			userInfoRequest.getUserRoleRequest().addAll(userOrganizationRoleList);
			
			userInfoRequest.getUserRoleRequest().forEach(model -> {
				String sysId = invokeSystemService.querySystemId(model.getSystemId(), model.getSystemCode());
				model.init(userId, sysId);
				String sysCode = invokeSystemService.querySysCodeById(model.getSystemId(), model.getSystemCode());
				model.setSystemCode(sysCode);
			});
			userOrganizationRoleMqResponse.init(userId, userInfoRequest.getUserRoleRequest(), userBasicsInfo.getCorpCode());
			mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.SOUR.getValue(),
					userOrganizationRoleMqResponse);
			logger.info("下发组织角色关系，处理完成（修改）");
		}
		// 发送用户信息MQ消息、记录操作日志
		mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.USER.getValue(), userInfoMqResponse);
		// 下发原数据
		if (merge) {
			logger.info("对于合并的数据，需要补发一条删除原修改数据后端身份的MQ（修改）");
			UserBasicsInfo oldUserBasicInfo = userWriteMapper.queryUserBasicsInfoById(oldUserId);
			// 获取用户渠道--用户MQ下发
			List<UserChannel> oldUserChannel = new ArrayList<UserChannel>();
			String currentVersion = System.currentTimeMillis() + "";
			oldUserChannel = userWriteMapper.queryUserChannelForDel(oldUserId);
			userWriteMapper.deleteBackEndUserChannel(oldUserId);
			// （去除后台账号）设置UserChannels
			int channelCodeValue = (int) Math.pow(2, MdmConstants.BACKEND);
			oldUserBasicInfo.setUserChannels(userBasicsInfo.getUserChannels() & (~channelCodeValue));
			String oldVersion = oldUserBasicInfo.getVersion();
			oldUserBasicInfo.setVersion(currentVersion);
			if (null != oldUserBasicInfo.getEncryptionMode()) {
				oldUserBasicInfo.seteMode(handleEncryptionMode(oldUserBasicInfo.getEncryptionMode()));
			}
			userWriteMapper.updateUserBasicsInfo(oldUserBasicInfo, oldVersion);
			// 获取用户明细信息
			UserDetailInfo oldUserDetailInfo = userWriteMapper.queryUserDetailInfoByUserId(oldUserId);
			oldUserDetailInfo.setIsDeleted(1);
			oldUserDetailInfo.setVersion(currentVersion);
			// 只删除后端用户对应的channel信息，下发内容需要设置为已删除状态
			UserBasicsInfo MQUserBasicsInfo = userWriteMapper.queryUserBasicsInfoForMQById(oldUserId);
			MQUserBasicsInfo.setIsDeleted(1);
			MQUserBasicsInfo.setVersion(currentVersion);
			String oldSystemCode = invokeSystemService.querySysCodeById(oldUserBasicInfo.getSystemId(), null);
			MQUserBasicsInfo.setSystemCode(oldSystemCode);
			// 初始化用户信息mq消息队列实体
			UserInfoMqResponse oldUserInfoMqResponse = new UserInfoMqResponse();
			
			List<DataExtendRequest> exts = new ArrayList<DataExtendRequest>();
			DataExtendRequest ext = new DataExtendRequest();
			ext.setDescription("【用户修改上报】因出现可以上报的数据，所以下发这条MQ用以删除原上报数据的后端身份");
			MQUserBasicsInfo.setExts(exts);
			oldUserInfoMqResponse.init(MQUserBasicsInfo, oldUserDetailInfo, oldUserChannel);
			// 发送用户信息MQ消息、记录操作日志
			mqSenderAdapter.sendMQ(LogTypeEnum.DELETE.getValue(), DataTypeEnum.USER.getValue(), oldUserInfoMqResponse);
			// 发送短信
//			sendSMS(randomPWD, userBasicsInfo.getCellPhone());
		}
		if (null != oldCorpCode && null != newCorpCode && !oldCorpCode.equalsIgnoreCase(newCorpCode)) {
			logger.info("如果出现企业代码的变更，下发一条物业的MQ【解绑】（修改）");
			if (merge) {
				// 下发合并用户，设置corpCode/解绑状态
				sendMqForCSS(oldUserId, oldCorpCode, oldCellPhone);
			} else {
				// 下发修改用户，设置corpCode/解绑状态
				sendMqForCSS(userId, oldCorpCode, oldCellPhone);
			}
		}
		logger.info("修改数据完成（修改）");
		return new CommonPojoResponse(userBasicsInfo.getId(), userBasicsInfo.getVersion());
	}
	/**
	 * 针对企业代码变更的数据，单独下发MQ消息给CSS-物业系统
	 * @Title: sendMqForCSS
	 * @Description: TODO
	 * @param userId
	 * @param corpCode
	 * @throws MdmException
	 * @return: void
	 */
	private void sendMqForCSS(String userId, String corpCode, String cellPhone) throws MdmException {
		// 获取用户渠道--用户MQ下发
		List<UserChannel> MQUserChannel = new ArrayList<UserChannel>();
		MQUserChannel = userWriteMapper.queryUserChannelForDel(userId);
		// 获取用户明细信息
		UserDetailInfo MQUserDetailInfo = userWriteMapper.queryUserDetailInfoByUserId(userId);
		MQUserDetailInfo.setPhoneNumberConfirmed(MdmConstants.PHONENUMBERCONFIRMED_ZERO);
		UserBasicsInfo MQUserBasicsInfo = userWriteMapper.queryUserBasicsInfoForMQById(userId);
		MQUserBasicsInfo.setPhoneNumberConfirmed(MdmConstants.PHONENUMBERCONFIRMED_ZERO);
		MQUserBasicsInfo.setCorpCode(corpCode);
		MQUserBasicsInfo.setCellPhone(cellPhone);
		String systemCode = invokeSystemService.querySysCodeById(MQUserBasicsInfo.getSystemId(), null);
		MQUserBasicsInfo.setSystemCode(systemCode);
		// 初始化用户信息mq消息队列实体
		UserInfoMqResponse userInfoMqResponse = new UserInfoMqResponse();
		userInfoMqResponse.init(MQUserBasicsInfo, MQUserDetailInfo, MQUserChannel);
		// 发送用户信息MQ消息(CSS-物业)
		mqSenderAdapter.sendMQForCSS(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.USER.getValue(), userInfoMqResponse);
	}
	/**
	 * 去除重复的渠道信息
	 * @Title: removeRepeatForChannel
	 * @Description: TODO
	 * @param list
	 * @return: void
	 */
	private void removeRepeatForChannel(List<UserChannel> list) {
		if (null != list && list.size() > 0) {
			List<UserChannel> newList = new ArrayList<UserChannel>();
			for (UserChannel uc : list) {
				if (newList.size() < 1) {
					newList.add(uc);
				} else {
					boolean b = false;
					for (UserChannel u : newList) {
						if (u.getChannelCode().equals(uc.getChannelCode())) {
							b = true;
							break;
						}
					}
					if (!b) {
						newList.add(uc);
					}
				}
			}
			list.clear();
			list.addAll(newList);
		}
	}
	/**
	 * 更新用户信息（恢复数据）
	 * @Title: updateUserForSF
	 * @Description: TODO
	 * @param userInfoRequest
	 * @return
	 * @throws MdmException
	 * @return: CommonPojoResponse
	 */
	public CommonPojoResponse updateUserForSF(UserInfoSFRequest userInfoRequest) throws MdmException {
		// if (userInfoRequest.getUserDetailInfoRequest().getPhoneNumberConfirmed() > 0) {
		validateCellPhoneNoS(userInfoRequest.getUserBasicsInfoRequest().getId(),
				userInfoRequest.getUserBasicsInfoRequest().getCellPhone());
		// }
		UserBasicsInfo userBasicsInfo = new UserBasicsInfo();
		UserDetailInfo userDetailInfo = new UserDetailInfo();
		BeanUtils.copyProperties(userInfoRequest.getUserBasicsInfoRequest(), userBasicsInfo);
		BeanUtils.copyProperties(userInfoRequest.getUserDetailInfoRequest(), userDetailInfo);
		String userId = userBasicsInfo.getId();
		// 版本赋值
		userBasicsInfo.initModify();
		userDetailInfo.initModify();
		// 初始化用户渠道
		if (null == userBasicsInfo.getUserChannels()) {
			userBasicsInfo.setUserChannels(0);
		}
		if (null != userInfoRequest.getUserChannel() && userInfoRequest.getUserChannel().size() > 0) {
			userInfoRequest.getUserChannel().forEach(model -> {
				model.init(userId);
				// 设置userChannels
				int channelCode = Integer.parseInt(model.getChannelCode());
				double channelValue = Math.pow(2, channelCode);
				userBasicsInfo
						.setUserChannels(userBasicsInfo.getUserChannels() | (new Double(channelValue)).intValue());
			});
		}
		// 设置手机绑定状态
		userBasicsInfo.setPhoneNumberConfirmed(userDetailInfo.getPhoneNumberConfirmed());
		// 更新用户基础信息、用户明细信息
		int num = userWriteMapper.recoverUserBasicsInfo(userBasicsInfo, null);
		if (num == 0) {
			String version = userWriteMapper.queryUserBasicsInfoById(userBasicsInfo.getId()).getVersion();
			if (!userBasicsInfo.getVersion().equals(version)) {
				throw new MdmException(MdmConstants.RESCODE_VERSION_FAIL, "版本错误");
			}
		}
		userWriteMapper.recoverUserDetailInfo(userDetailInfo);
		// 删除用户渠道
		userWriteMapper.deleteUserChannel(userId);
		// 新增用户渠道
		if (null != userInfoRequest.getUserChannel() && userInfoRequest.getUserChannel().size() > 0) {
			userWriteMapper.insertUserChannelList(userInfoRequest.getUserChannel());
		}
		// 初始化用户信息mq消息队列实体
		UserInfoMqResponse userInfoMqResponse = new UserInfoMqResponse();
		// 从数据库获取最新用户信息
		UserBasicsInfo userBasicsInfomq = userWriteMapper.queryUserBasicsInfoForMQById(userId);
		String systemCode = invokeSystemService.querySysCodeById(userBasicsInfomq.getSystemId(), null);
		userBasicsInfomq.setSystemCode(systemCode);
		// 获取用户明细信息
		UserDetailInfo userDetailInfomq = userWriteMapper.queryUserDetailInfoByUserId(userId);
		// 获取用户渠道
		List<UserChannel> userChannel = userWriteMapper.queryUserChannelList(userId);
		userInfoMqResponse.init(userBasicsInfomq, userDetailInfomq, userChannel);
		// 发送用户信息MQ消息、记录操作日志
		mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.USER.getValue(), userInfoMqResponse);
		return new CommonPojoResponse(userBasicsInfo.getId(), userBasicsInfo.getVersion());
	}
	/**
	 * 设置业务系统用户数据的默认corpCode
	 * @param sysCode
	 * @param userBasicsInfo
	 * @Title: setCorpcode
	 * @Description: TODO
	 * @return: void
	 */
	private void setCorpcode(String sysCode, UserBasicsInfo userBasicsInfo) {
		if (null == userBasicsInfo.getCorpCode()) {
			for (String code : MdmConstants.CODES) {
				if (sysCode.equalsIgnoreCase(code)) {
					userBasicsInfo.setCorpCode(MdmConstants.CCPG);
				}
			}
		}
	}
	/**
	 * 删除用户基础信息
	 * @param userBasicsInfo
	 * @return
	 * @Title: deleteUserBasicsInfo
	 * @Description: TODO
	 * @return: Boolean
	 */
	public CommonPojoResponse deleteUserInfo(@Param("userBasicsId") String userBasicsId) throws MdmException {
		// 获取用户基础信息
		int channelCodeValue = (int) Math.pow(2, MdmConstants.BACKEND);
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(userBasicsId);
		boolean isMember = false;
		if ((userBasicsInfo.getUserChannels() != 0) && (userBasicsInfo.getUserChannels() != channelCodeValue)) {
			// 判断是否存在前端用户（存在：只删除后端用户，channelCode =1；不存在：删除所有信息）
			isMember = true;
		}
		String version = System.currentTimeMillis() + "";
		// 获取用户渠道--用户MQ下发
		List<UserChannel> userChannel = new ArrayList<UserChannel>();
		// 获取用户组织角色关联关系---用户MQ下发
		List<UserOrganizationRole> userOrganizationRoleList = userWriteMapper
				.queryUserOrganizationRoleList(userBasicsId);
		if (isMember) { // 只删除后端用户对应的channel信息
			userChannel = userWriteMapper.queryUserChannelForDel(userBasicsId);
			userWriteMapper.deleteBackEndUserChannel(userBasicsId);
			// （去除后台账号）设置UserChannels中ChannelCode为1的位为0
			userBasicsInfo.setUserChannels(userBasicsInfo.getUserChannels() & (~channelCodeValue));
			String oldVersion = userBasicsInfo.getVersion();
			userBasicsInfo.setVersion(version);
			if (null != userBasicsInfo.getEncryptionMode()) {
				userBasicsInfo.seteMode(handleEncryptionMode(userBasicsInfo.getEncryptionMode()));
			}
			userWriteMapper.updateUserBasicsInfo(userBasicsInfo, oldVersion);
		} else { // 删除所有信息
			userChannel = userWriteMapper.queryUserChannelList(userBasicsId);
			// 删除用户基础信息
			userWriteMapper.deleteUserDetailInfo(userBasicsId, version, HttpContent.getOperatorId());
			// 删除用户明细信息
			userWriteMapper.deleteUserBasicsInfo(userBasicsId, version, HttpContent.getOperatorId());
			// 删除用户渠道信息
			userWriteMapper.deleteUserChannel(userBasicsId);
		}
		// 删除用户组织角色关联信息（删除用户，就要删除组织角色关系，前端会员不需要组织角色信息）
		userWriteMapper.deleteUserOrganizationRole(userBasicsId, null);
		String systemCode = invokeSystemService.querySysCodeById(userBasicsInfo.getSystemId(), null);
		// 获取用户明细信息
		UserDetailInfo userDetailInfo = userWriteMapper.queryUserDetailInfoByUserId(userBasicsId);
		userDetailInfo.setIsDeleted(1);
		// 只删除后端用户对应的channel信息，下发内容需要设置为已删除状态
		UserBasicsInfo mqUserBasicsInfo = userWriteMapper.queryUserBasicsInfoForMQById(userBasicsId);
		mqUserBasicsInfo.setVersion(version);
		mqUserBasicsInfo.setIsDeleted(1);
		mqUserBasicsInfo.setSystemCode(systemCode);
		// 初始化用户信息mq消息队列实体
		UserInfoMqResponse userInfoMqResponse = new UserInfoMqResponse();
		userInfoMqResponse.init(mqUserBasicsInfo, userDetailInfo, userChannel);
		// 发送用户信息MQ消息、记录操作日志
		mqSenderAdapter.sendMQ(LogTypeEnum.DELETE.getValue(), DataTypeEnum.USER.getValue(), userInfoMqResponse);
		// 初始化用户组织结构角色关联信息
		if (null != userOrganizationRoleList && userOrganizationRoleList.size() > 0) {
			userOrganizationRoleList.forEach(model -> {
				// 用户组织机构下发，根据systemId初始化systemCode
				String sysCode = invokeSystemService.querySysCodeById(model.getSystemId(), model.getSystemCode());
				model.setSystemCode(sysCode);
			});
		}
		// 初始化用户组织机构角色mq消息队列实体
		UserOrganizationRoleMqResponse userOrganizationRoleMqResponse = new UserOrganizationRoleMqResponse();
		// 初始化用户组织机构角色mq消息队列实体
		userOrganizationRoleMqResponse.init(userBasicsId, userOrganizationRoleList, userBasicsInfo.getCorpCode());
		// 发送用户组织机构角色mq消息、记录操作日志
		mqSenderAdapter.sendMQ(LogTypeEnum.DELETE.getValue(), DataTypeEnum.SOUR.getValue(),
				userOrganizationRoleMqResponse);
		return new CommonPojoResponse(userBasicsId, version);
	}
	/**
	 * 根据用户id，获取用户信息
	 * @param id
	 * @return
	 * @Title: queryById
	 * @Description: TODO
	 * @return: UserInfoRequest
	 */
	public UserInfoResponse queryById(@Param("id") String id) {
		UserInfoResponse userInfoResponse = new UserInfoResponse();
		// 获取用户基础信息
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(id);
		String systemCode = invokeSystemService.querySysCodeById(userBasicsInfo.getSystemId(), null);
		userBasicsInfo.setSystemCode(systemCode);
		// 获取用户明细信息
		final UserDetailInfo userDetailInfo = userWriteMapper.queryUserDetailInfoByUserId(id);
		// 获取用户组织角色关联关系
		final List<UserOrganizationRole> userOrganizationRoleList = userWriteMapper.queryUserOrganizationRoleList(id);
		// 获取用户渠道
		final List<UserChannel> userChannel = userWriteMapper.queryUserChannelList(id);
		userInfoResponse.init(userBasicsInfo, userDetailInfo, userChannel, userOrganizationRoleList);
		return userInfoResponse;
	}
	/**
	 * 维护用户组织结构角色关联
	 * @Title: setOrganizationRole
	 * @Description: TODO
	 * @param userId
	 * @param corpCode
	 * @param userOrganizationRoles
	 * @return
	 * @throws MdmException
	 * @return: List<UserOrganizationRole>
	 */
	public List<UserOrganizationRole> setOrganizationRole(String userId, String corpCode,
			List<UserOrganizationRole> userOrganizationRoles) throws MdmException {
		// 去除重复组织角色
		removeRepeat(userOrganizationRoles);
		String systemCode = HttpContent.getSysCode();
		String systemId = invokeSystemService.querySystemId("", systemCode);
		// 如果为MDM，则删除全部关联组织角色
		if (HttpContent.getSysCode().equals(MdmConstants.MDM))
			systemId = null;
		userWriteMapper.deleteUserOrganizationRole(userId, systemId);
		if (null != userOrganizationRoles && userOrganizationRoles.size() > 0) {
			userOrganizationRoles.forEach(model -> {
				String sysId = invokeSystemService.querySystemId(model.getSystemId(), model.getSystemCode());
				model.init(userId, sysId);
			});
			userWriteMapper.insertUserOrganizationRoleList(userOrganizationRoles);
		}
		// 初始化用户组织机构角色mq消息队列实体
		UserOrganizationRoleMqResponse userOrganizationRoleMqResponse = new UserOrganizationRoleMqResponse();
		userOrganizationRoleMqResponse.init(userId, userOrganizationRoles, corpCode);
		// 发送用户组织机构角色mq消息、记录操作日志
		mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.SOUR.getValue(),
				userOrganizationRoleMqResponse);
		return userOrganizationRoles;
	}
	/**
	 * 维护用户组织结构角色关联
	 * @Title: addOrganizationRole
	 * @Description: TODO
	 * @param userOrganizationRoles
	 * @return
	 * @throws MdmException
	 * @return: List<UserOrganizationRole>
	 */
	public List<UserOrganizationRole> addOrganizationRole(List<UserOrganizationRole> userOrganizationRoles)
			throws MdmException {
		userOrganizationRoles.forEach(model -> {
			String sysId = invokeSystemService.querySystemId(model.getSystemId(), model.getSystemCode());
			model.init(sysId);
		});
		userWriteMapper.insertUserOrganizationRoleList(userOrganizationRoles);
		return userOrganizationRoles;
	}
	/**
	 * 维护用户组织结构角色关联
	 * @Title: setOrganizationRoleForRole
	 * @Description: TODO
	 * @param roleId
	 * @param userOrganizationRoles
	 * @return
	 * @throws MdmException
	 * @return: List<UserOrganizationRole>
	 */
	public List<UserOrganizationRole> setOrganizationRoleForRole(String roleId,
			List<UserOrganizationRole> userOrganizationRoles) throws MdmException {
		userWriteMapper.deleteUserOrganizationRoleByRoleId(roleId);
		if (null != userOrganizationRoles && userOrganizationRoles.size() > 0) {
			userOrganizationRoles.forEach(model -> {
				String sysId = invokeSystemService.querySystemId(model.getSystemId(), model.getSystemCode());
				model.init(sysId);
			});
			if (userOrganizationRoles.size() > 0)
				userWriteMapper.insertUserOrganizationRoleList(userOrganizationRoles);
		}
		return userOrganizationRoles;
	}
	
	/**
	 * 查询用户基础信息
	 * @param id
	 * @return
	 * @Title: queryUserBasicsInfoById
	 * @Description: TODO
	 * @return: UserBasicsInfo
	 */
	public UserInfoEditResponse queryUserInfoById(@Param("id") String id) {
		UserInfoEditResponse userInfoEditResponse = new UserInfoEditResponse();
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(id);
		UserDetailInfo userDetailInfo = userWriteMapper.queryUserDetailInfoByUserId(id);
		// 获取社区名称
		if (null != userDetailInfo && null != userDetailInfo.getAreaID()) {
			Community community = communityWriteMapper.queryCommunityById(userDetailInfo.getAreaID());
			if (null != community) {
				userInfoEditResponse.setAreaName(community.getName());
			}
		}
		if (null != userBasicsInfo) {
			userBasicsInfo.setPassword(null);
		}
		
		List<Map<String, Object>> sysBindStates = handViewBindState(userBasicsInfo.getBindState());
		
		UserBasicsInfoRequest userBasicsInfoRequest = new UserBasicsInfoRequest();
		UserDetailInfoRequest userDetailInfoRequest = new UserDetailInfoRequest();
		BeanUtils.copyProperties(userBasicsInfo, userBasicsInfoRequest);
		if (userDetailInfo != null)
			BeanUtils.copyProperties(userDetailInfo, userDetailInfoRequest);
		userInfoEditResponse.setUserBasicsInfoRequest(userBasicsInfoRequest);
		userInfoEditResponse.setUserDetailInfoRequest(userDetailInfoRequest);
		userInfoEditResponse.setSysBindStates(sysBindStates);
		final List<UserOrganizationRole> userOrganizationRoleList = userWriteMapper.queryUserOrganizationRoleList(id);
		final List<UserChannel> userChannels = userWriteMapper.queryUserChannelList(id);
		userInfoEditResponse.setUserChannel(userChannels);
		// 用户组织角色树集合
		List<TreeResponse> treeNodes = new ArrayList<TreeResponse>();
		// 用户关联的组织集合
		List<String> orgIds = new ArrayList<String>(),
				corpCodes = new ArrayList<String>();
		userOrganizationRoleList.forEach((model) -> {
			Role role = roleService.getById(model.getRoleId());
			if (null != role && null != role.getCorpCode()
					&& !role.getCorpCode().equalsIgnoreCase(userBasicsInfo.getCorpCode())
					&& !corpCodes.contains(role.getCorpCode())) {
				corpCodes.add(role.getCorpCode());
			}
			if (!orgIds.contains(model.getOrganizationId()))
				orgIds.add(model.getOrganizationId());
		});
		// 没有组织角色，跳过下面流程
		if (orgIds.size() > 0) {
			// 获取用户所在的组织
			final List<Organazation> organizations = organazationService.batchGetByIds(orgIds);
			final List<Merchant> merchants = merchantService.batchQueryMerchantListById(orgIds);
			// 所有接入系统集合
			final List<SystemInfoResponse> systemInfos = invokeSystemService.queryAllSystemInfo();
			// 所有角色集合
			Map<String, Object> map = new HashMap<>();
			map.put("isDeleted", 0);
			List<Role> allRoles = new ArrayList<Role>();
			// 只有物业的用户，需要加上corpCode过滤角色
			if (userWriteMapper.validateUserType(id, "3") > 0) {
				// 物业角色--过滤
				corpCodes.add(userBasicsInfo.getCorpCode());
//				map.put("corpCode", userBasicsInfo.getCorpCode());
				map.put("userPage", "Y");
				SystemInfoResponse system = systemInfoWriteMapper.isExistSystemInfoByCode(MdmConstants.CSS);
//				map.put("systemId", system.getId());
//				allRoles.addAll(roleService.queryList(map));
//				// 如果物业云用户设置过多个企业代码组织/角色，需要分别查询多次组织角色
//				for (String corpCode : corpCodes) {
//					map.put("corpCode", corpCode);
//					allRoles.addAll(roleService.queryList(map));
//				}
				allRoles.addAll(roleWriteMapper.batchQueryRolesByIds(corpCodes,"0",system.getId().toString()));
				// 非物业角色
//				map.remove("systemId");
//				map.remove("corpCode");
				map.put("noCss", system.getId());
				allRoles.addAll(roleService.queryList(map));
			} else {
				allRoles = roleService.queryList(map);
			}
			final List<Role> roles = allRoles;
			organizations.forEach((orgModel) -> {
				boolean wy = false;
				if (null != orgModel.getType() && orgModel.getType().equals("3")) {
					wy = true;
				}
				final boolean isWy = wy;
				final String orgCorpCode = orgModel.getCorpCode();
				TreeResponse treeONode = new TreeResponse();
				treeONode.setId(orgModel.getId());
				treeONode.setLevel(0);
				treeONode.setName(orgModel.getName());
				treeONode.setNocheck(true);
				treeONode.setOpen(false);
				treeONode.setpId("0");
				if (orgModel.getType().equals("0")) {
					treeONode.setSsyRole("1");
				}
				treeNodes.add(treeONode);
				systemInfos.forEach((sysModel) -> {
					if ((sysModel.getType().toString()).equals((orgModel.getType().toString()))) {
						TreeResponse treeSNode = new TreeResponse();
						List<TreeResponse> nodes = new ArrayList<TreeResponse>();
						treeSNode.setId(sysModel.getId());
						treeSNode.setLevel(1);
						treeSNode.setName(sysModel.getSysName());
						treeSNode.setNocheck(true);
						treeSNode.setOpen(false);
						treeSNode.setpId(orgModel.getId());
						treeNodes.add(treeSNode);
						roles.forEach((roleModel) -> {
							if (isWy) {
								if(null != orgCorpCode && orgCorpCode.equalsIgnoreCase(roleModel.getCorpCode())){
									// 物业用户，需要根据企业代码，分别展示不同的角色
									if (roleModel.getSystemId() != null && (roleModel.getSystemId().toString()
											.equalsIgnoreCase(sysModel.getId().toString()))) {
										Boolean checked = false;
										for (UserOrganizationRole uorModel : userOrganizationRoleList) {
											if (uorModel.getRoleId().toString().equalsIgnoreCase(roleModel.getId().toString())
													&& uorModel.getOrganizationId().toString()
													.equalsIgnoreCase(orgModel.getId())) {
												checked = true;
											}
										}
										TreeResponse treeRNode = new TreeResponse();
										treeRNode.setId(roleModel.getId());
										treeRNode.setLevel(2);
										treeRNode.setName(roleModel.getName());
										treeRNode.setNocheck(false);
										treeRNode.setOpen(false);
										treeRNode.setpId(sysModel.getId());
										treeRNode.setChecked(checked);
										if (roleModel.getSystemInfo().getSysCode().equals("SSY")) {
											treeRNode.setSsyRole("1");
										}
										nodes.add(treeRNode);
									}
								}
							}else{
								if (roleModel.getSystemId() != null && (roleModel.getSystemId().toString()
										.equalsIgnoreCase(sysModel.getId().toString()))) {
									Boolean checked = false;
									for (UserOrganizationRole uorModel : userOrganizationRoleList) {
										if (uorModel.getRoleId().toString().equalsIgnoreCase(roleModel.getId().toString())
												&& uorModel.getOrganizationId().toString()
												.equalsIgnoreCase(orgModel.getId())) {
											checked = true;
										}
									}
									TreeResponse treeRNode = new TreeResponse();
									treeRNode.setId(roleModel.getId());
									treeRNode.setLevel(2);
									treeRNode.setName(roleModel.getName());
									treeRNode.setNocheck(false);
									treeRNode.setOpen(false);
									treeRNode.setpId(sysModel.getId());
									treeRNode.setChecked(checked);
									if (roleModel.getSystemInfo().getSysCode().equals("SSY")) {
										treeRNode.setSsyRole("1");
									}
									nodes.add(treeRNode);
								}
							}
						});
						treeSNode.setNodes(nodes);
					}
				});
			});
			merchants.forEach((orgModel) -> {
				TreeResponse treeONode = new TreeResponse();
				treeONode.setId(orgModel.getId());
				treeONode.setLevel(0);
				treeONode.setName(orgModel.getmName());
				treeONode.setNocheck(true);
				treeONode.setOpen(false);
				treeONode.setpId("0");
				treeONode.setSsyRole("1");
				treeNodes.add(treeONode);
				systemInfos.forEach((sysModel) -> {
					if ((sysModel.getType().toString()).equals(("0"))) {
						TreeResponse treeSNode = new TreeResponse();
						treeSNode.setId(sysModel.getId());
						treeSNode.setLevel(1);
						treeSNode.setName(sysModel.getSysName());
						treeSNode.setNocheck(true);
						treeSNode.setOpen(false);
						treeSNode.setpId(orgModel.getId());
						treeNodes.add(treeSNode);
						roles.forEach((roleModel) -> {
							if (roleModel.getSystemId() != null && (roleModel.getSystemId().toString()
									.equalsIgnoreCase(sysModel.getId().toString()))) {
								Boolean checked = false;
								for (UserOrganizationRole uorModel : userOrganizationRoleList) {
									if (uorModel.getRoleId().toString().equalsIgnoreCase(roleModel.getId().toString())
											&& uorModel.getOrganizationId().toString()
													.equalsIgnoreCase(orgModel.getId())) {
										checked = true;
									}
								}
								TreeResponse treeRNode = new TreeResponse();
								treeRNode.setId(roleModel.getId());
								treeRNode.setLevel(2);
								treeRNode.setName(roleModel.getName());
								treeRNode.setNocheck(false);
								treeRNode.setOpen(false);
								treeRNode.setpId(sysModel.getId());
								treeRNode.setChecked(checked);
								treeNodes.add(treeRNode);
							}
						});
					}
				});
			});
		}
		userInfoEditResponse.setTreeResponse(treeNodes);
		return userInfoEditResponse;
	}
	/**
	 * 根据组织机构编号获取用户基础信息列表
	 * @Title: queryUserListByOrganizationId
	 * @Description: TODO
	 * @param userGridRequest
	 * @return
	 * @return: PageResultBean
	 */
	public PageResultBean queryUserListByOrganizationId(@Param("userGridRequest") UserGridRequest userGridRequest) {
		// 分页与统计总数
		Page<Object> page = PageHelper.startPage(userGridRequest.getPageNum(), userGridRequest.getPageSize(),
				userGridRequest.getLastMaxPk(), userGridRequest.getLastMinPk(), userGridRequest.getLastPageNum(),
				"u.Pk");
		PageResultBean pageResult = new PageResultBean();
		List<UserGridResponse> userGridList = userWriteMapper.queryUserListByOrganizationId(userGridRequest);
		if (userGridList != null && userGridList.size() > 0) {
			Collections.sort(userGridList, new Comparator<UserGridResponse>() {
				// 重写排序规则
				public int compare(UserGridResponse arg0, UserGridResponse arg1) {
					return arg1.getPk().compareTo(arg0.getPk());
				}
			});
			List<Long> pks = new ArrayList<Long>();
			userGridList.forEach(user -> {
				pks.add(user.getPk());
			});
			Long maxPk = Collections.max(pks);
			Long minPk = Collections.min(pks);
			pageResult.setMaxPk(maxPk);
			pageResult.setMinPk(minPk);
		}
		pageResult.setList(userGridList);
		pageResult.setTotalCount(page.getTotal());
		return pageResult;
	}
	/**
	 * 查询前端用户列表
	 * @Title: queryMemberList
	 * @Description: TODO
	 * @param userGridRequest
	 * @return
	 * @return: PageResultBean
	 */
	public PageResultBean queryMemberList(@Param("userGridRequest") UserGridRequest userGridRequest) {
		Page<Object> page = PageHelper.startPage(userGridRequest.getPageNum(), userGridRequest.getPageSize());// 分页与统计总数
		List<UserGridResponse> userGridList = userWriteMapper.queryMemberList(userGridRequest);
		if (null != userGridList && userGridList.size() > 0) {
			userGridList.forEach(user -> {
				// 不存在，可以提供设置为后端用户的按钮
				if ((user.getUserChannels() != 0) && ((user.getUserChannels() & 2) != 2)) {
					// 仅为前端用户(会员)
					user.setBackEnd(true);
				}
			});
		}
		PageResultBean pageResult = new PageResultBean();
		pageResult.setList(userGridList);
		pageResult.setTotalCount(page.getTotal());
		return pageResult;
	}
	/**
	 * 验证手机号是否已经被注册过
	 * @param checkPhoneRequest
	 * @return
	 */
	public boolean checkPhoneNumAndAccountRegister(String phoneNumOrAccount) throws MdmException {
		validateCellPhoneNoS(null, phoneNumOrAccount);
		return true;
	}
	/**
	 * 用户注册
	 * @param userRegisterRequest
	 * @return
	 * @throws MdmException
	 */
	public UserBasicsInfo userRegister(UserRegisterRequest userRegisterRequest) throws MdmException {
		String result = checkRegisterParam(userRegisterRequest);
		if (StringUtil.isEmpty(result)) {
			if (captchaService.checkCaptchaByCaptchaIdAndCode(userRegisterRequest.getCaptchaId(),
					userRegisterRequest.getCaptcha(), userRegisterRequest.getUserName())) {
				UserBasicsInfo userBasicsInfo = new UserBasicsInfo();
				userBasicsInfo.init(null, null);
				userBasicsInfo.setAccount("YY_" + userWriteMapper.nextPK());
				userBasicsInfo.setCellPhone(userRegisterRequest.getUserName());
				userBasicsInfo.setUsername(userRegisterRequest.getUserName());
				userBasicsInfo.setPassword(getEncryptionPasswordByName(null, userRegisterRequest.getPassword(),
						userRegisterRequest.getEncryptionMode()));
				userBasicsInfo.setType(0);
				userBasicsInfo.setSex(0);// 0 保密
				userBasicsInfo.setStatus(GlobalConstants.STATUS_START.intValue());
				userBasicsInfo
						.setEncryptionMode(getEncryptionModeDefaultValue(userRegisterRequest.getEncryptionMode()));
				// 默认用户渠道为3
				userBasicsInfo.setUserChannels((int) Math.pow(2, MdmConstants.MEMBER_THREE));
				// 设置手机绑定状态
				userBasicsInfo.setPhoneNumberConfirmed(MdmConstants.PHONENUMBERCONFIRMED_ONE);
				//默认来源系统是社商云
				userBasicsInfo.setSystemId("b764043c-81cc-413c-9cf9-4b19f65dfc36");
				//初始化版本号设置为0
				userWriteMapper.insertUserBasicsInfo(userBasicsInfo);
				UserDetailInfo userDetailInfo = new UserDetailInfo();
				userDetailInfo.init(userBasicsInfo.getId());
				userDetailInfo.setRegisterDate(DateUtils.currentDate());
				userDetailInfo.setIsDeleted(GlobalConstants.IS_NOT_DELETED.intValue());
				userDetailInfo.setAreaID(userRegisterRequest.getCommunityId());
				userDetailInfo.setPhoneNumberConfirmed(MdmConstants.PHONENUMBERCONFIRMED_ONE);
				userDetailInfo.setEmailConfirmed(0);
				if(StringUtils.isEmpty(userRegisterRequest.getCallSource())){
					userDetailInfo.setMemberFrom("PC");
				}else{
					userDetailInfo.setMemberFrom(userRegisterRequest.getCallSource());
				}
				userWriteMapper.insertUserDetailInfo(userDetailInfo);
				// 注册-用户渠道
				List<UserChannel> channelList = new ArrayList<UserChannel>();
				UserChannel userChannel = new UserChannel();
				userChannel.init(userBasicsInfo.getId());
				userChannel.setChannelCode(MdmConstants.MEMBER_THREE.toString());
				channelList.add(userChannel);
				userWriteMapper.insertUserChannelList(channelList);
				sendMQ(userBasicsInfo.getId());
				return userBasicsInfo;
			}
			return null;
		} else {
			throw new MdmException(result);
		}
	}
	private String checkRegisterParam(UserRegisterRequest userRegisterRequest) throws MdmException {
		if (StringUtil.isEmpty(userRegisterRequest.getUserName())) {
			return "用户名不能为空";
		} else if (StringUtil.isEmpty(userRegisterRequest.getCaptcha())) {
			return "验证码不能为空";
		} else if (StringUtil.isEmpty(userRegisterRequest.getEncryptionMode())) {
			return "加密方式不能为空";
		}
		if (!checkPhoneNumAndAccountRegister(userRegisterRequest.getUserName())) {
			return "该用户名已被注册";
		}
		return null;
	}
	public JSONObject captchaUpdateUserPasswordRequest(
			CaptchaUpdateUserPasswordRequest captchaUpdateUserPasswordRequest) throws MdmException {
		if (StringUtil.isEmpty(captchaUpdateUserPasswordRequest.getNewPwd())) {
			throw new MdmException("密码不能为空");
		}
		UserBasicsInfo userBasicsInfo = userWriteMapper
				.queryUserBasicsInfoById(captchaUpdateUserPasswordRequest.getUserId());
		if (userBasicsInfo == null) {
			throw new MdmException("该用户不存在");
		}
		/****** 验证不通过，直接抛异常到Controller处理 *****/
		if (captchaService.checkCaptchaByCaptchaIdAndCode(captchaUpdateUserPasswordRequest.getCaptchaId(),
				captchaUpdateUserPasswordRequest.getCaptcha(), userBasicsInfo.getCellPhone())) {
			userBasicsInfo.setEncryptionMode("MD5"); // 重置密码为MD5
			String pwd = getEncryptionPasswordByName(null, captchaUpdateUserPasswordRequest.getNewPwd(),
					userBasicsInfo.getEncryptionMode());
			userBasicsInfo.setPassword(pwd);
			userBasicsInfo.initModify();
			userWriteMapper.resetPassword(captchaUpdateUserPasswordRequest.getUserId(), pwd,
					userBasicsInfo.getVersion());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("password", pwd);
			jsonObject.put("id", userBasicsInfo.getId());
			sendMQ(userBasicsInfo.getId());
			return jsonObject;
		}
		return null;
	}
	public JSONObject forgetPassword(CaptchaUpdateUserPasswordRequest2 captchaUpdateUserPasswordRequest)
			throws MdmException {
		if (StringUtil.isEmpty(captchaUpdateUserPasswordRequest.getPhone())) {
			throw new MdmException("手机号不能为空");
		}
		if (StringUtil.isEmpty(captchaUpdateUserPasswordRequest.getNewPassword())
				|| StringUtil.isEmpty(captchaUpdateUserPasswordRequest.getPasswordAgain())) {
			throw new MdmException("密码不能为空");
		}
		if (!captchaUpdateUserPasswordRequest.getNewPassword()
				.equals(captchaUpdateUserPasswordRequest.getPasswordAgain())) {
			throw new MdmException("两次输入的密码不一致");
		}
		UserBasicsInfo userBasicsInfo = userWriteMapper
				.queryUserByCellphoneNoS(captchaUpdateUserPasswordRequest.getPhone());
		if (userBasicsInfo == null) {
			throw new MdmException("该用户不存在");
		}
		/****** 验证不通过，直接抛异常到Controller处理 *****/
		if (captchaService.checkCaptchaByCaptchaIdAndCode(captchaUpdateUserPasswordRequest.getCaptchaId(),
				captchaUpdateUserPasswordRequest.getCaptcha(), userBasicsInfo.getCellPhone())) {
			userBasicsInfo.setEncryptionMode("MD5"); // 重置密码为MD5
			String pwd = getEncryptionPasswordByName(userBasicsInfo.getAccount(),
					captchaUpdateUserPasswordRequest.getNewPassword(), userBasicsInfo.getEncryptionMode());
			userBasicsInfo.setPassword(pwd);
			userBasicsInfo.setVersion(System.currentTimeMillis() + "");
			userWriteMapper.resetPassword(userBasicsInfo.getId(), userBasicsInfo.getPassword(),
					userBasicsInfo.getVersion());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("password", pwd);
			jsonObject.put("id", userBasicsInfo.getId());
			sendMQ(userBasicsInfo.getId());
			return jsonObject;
		}
		return null;
	}
	/**
	 * 根据用户的加密值（1,2）， 计算加密结果
	 * @param password
	 * @param encryptionValue
	 * @return
	 * @throws MdmException
	 */
	public String getEncryptionPasswordByValue(String account, String password, String encryptionValue)
			throws MdmException {
		if (StringUtil.isEmpty(encryptionValue) || StringUtil.isEmpty(password)) {
			throw new MdmException("参数有误");
		}
		StaticData staticData = staticDataService.queryTableNameAndColValueList("UserPassportEncryptionType",
				encryptionValue);
		if (staticData == null)
			throw new MdmException("无此加密方式");
		return getEncryptionPasswordByName(account, password, staticData.getColName());
	}
	/**
	 * @param password
	 * @param encryptionValue
	 * @return
	 * @throws MdmException
	 */
	public String getEncryptionModeDefaultValue(String... encryptionNames) throws MdmException {
		if (StringUtil.isEmpty(encryptionNames[0]))
			encryptionNames[0] = "MD5";
		StaticData staticData = staticDataService.queryStaticData("UserPassportEncryptionType", encryptionNames[0]);
		if (staticData == null)
			throw new MdmException("无此加密方式");
		return staticData.getColValue();
	}
	/**
	 * 根据加密名获取加密密文
	 * @param password
	 * @param encryptionName
	 * @return
	 * @throws MdmException
	 */
	public String getEncryptionPasswordByName(String account, String password, String encryptionName)
			throws MdmException {
		if (encryptionName.equals("MD5")) {
			return EncryptionUtil.getEncryptionByMD5(password);
		} else if (encryptionName.equals("HASH")) {
			String shapwd;
			try {
				shapwd = EncryptionUtil.getEncryptionBySHA384(password);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new MdmException("密码不符合规范，加密失败");
			}
			return shapwd;
		} else if (encryptionName.equals("UNPWDMD5")) {
			if (StringUtils.isNotEmpty(account)) {
				account = account.toLowerCase();
			}
			return EncryptionUtil.getEncryptionByMD5(account + password);
		} else {
			throw new MdmException("无此加密方式");
		}
	}
	public CommonPojoResponse uploadSyncMember(UploadMember uploadMember) throws MdmException {
		if (StringUtil.isEmpty(uploadMember.getAccount()))
			throw new MdmException("请求参数有误");
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserAccount(uploadMember.getAccount());
		UserDetailInfo userDetailInfo = null;
		boolean isBasicInsert = false;
		boolean isDetailInsert = false;
		if (userBasicsInfo == null) {
			userBasicsInfo = new UserBasicsInfo();
			userBasicsInfo.setId(DataUtils.uuid());
			userBasicsInfo.setAccount(uploadMember.getAccount());
			isBasicInsert = true;
			userDetailInfo = new UserDetailInfo();
			userDetailInfo.setId(DataUtils.uuid());
			userDetailInfo.setUserId(userBasicsInfo.getId());
			isDetailInsert = true;
		} else {
			userDetailInfo = userWriteMapper.queryUserDetailInfoByUserId(userBasicsInfo.getId());
			if (userDetailInfo == null) {
				userDetailInfo = new UserDetailInfo();
				userDetailInfo.setId(DataUtils.uuid());
				userDetailInfo.setUserId(userBasicsInfo.getId());
				isDetailInsert = true;
			}
		}
		userBasicsInfo.setUsername(uploadMember.getUserName());
		userBasicsInfo.setCustomerCode(uploadMember.getCustomerCode());
		userBasicsInfo.setUsername(uploadMember.getFullName());
		String password = getEncryptionPasswordByValue(userBasicsInfo.getAccount(), uploadMember.getPassword(),
				uploadMember.getEncryptionMode());
		userBasicsInfo.setPassword(password);
		userBasicsInfo.setEncryptionMode(uploadMember.getEncryptionMode());
		userBasicsInfo.setWeChatID(uploadMember.getWeChatID());
		userBasicsInfo.setSex(uploadMember.getSex());
		userBasicsInfo.setEmail(uploadMember.getEmail());
		userBasicsInfo.setCellPhone(uploadMember.getCellPhone());
		userBasicsInfo.setPhoneNumber(uploadMember.getPhoneNumber());
		userBasicsInfo.setPhoneNumberConfirmed(uploadMember.getPhoneNumberConfirmed());
		if (HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.SSY) || HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.MDM)) {
			userBasicsInfo.setType(uploadMember.getUserType());
		}
		userBasicsInfo.setStatus(uploadMember.getStatus());
		userBasicsInfo.setSystemId(uploadMember.getSystemId());
		userBasicsInfo.setModifiedBy(uploadMember.getModifiedBy());
		userBasicsInfo.setModifiedOn(uploadMember.getModifiedOn());
		userBasicsInfo.setIsDeleted(uploadMember.getIsDeleted());
		
		userDetailInfo.setNickName(uploadMember.getNickName());
		userDetailInfo.setBirthDay(uploadMember.getBirthDay());
		userDetailInfo.setRegisterDate(uploadMember.getRegisterDate());
		userDetailInfo.setEmailConfirmed(uploadMember.getEmailConfirmed());
		// 设置手机绑定状态
		userDetailInfo.setPhoneNumberConfirmed(uploadMember.getPhoneNumberConfirmed());
		userDetailInfo.setMemberFrom(uploadMember.getMemberFrom());
		userDetailInfo.setAreaID(uploadMember.getAreaID());
		userDetailInfo.setHomeAddress(uploadMember.getHomeAddress());
		userDetailInfo.setUserNo(uploadMember.getUserNo());
		userDetailInfo.setSignature(uploadMember.getSignature());
		userDetailInfo.setHomeAddress(uploadMember.getHomeAddress());
		userDetailInfo.setModifiedBy(uploadMember.getModifiedBy());
		userDetailInfo.setModifiedOn(uploadMember.getModifiedOn());
		userDetailInfo.setIsDeleted(uploadMember.getIsDeleted());
		if (isBasicInsert) {
			userBasicsInfo.setCreatedOn(uploadMember.getCreatedOn());
			userBasicsInfo.setCreatedBy(uploadMember.getCreatedBy());
			userWriteMapper.insertUserBasicsInfo(userBasicsInfo);
		} else {
			if (null != userBasicsInfo.getEncryptionMode()) {
				userBasicsInfo.seteMode(handleEncryptionMode(userBasicsInfo.getEncryptionMode()));
			}
			userWriteMapper.updateUserBasicsInfo(userBasicsInfo, userBasicsInfo.getVersion());
		}
		if (isDetailInsert) {
			userDetailInfo.setCreatedOn(uploadMember.getCreatedOn());
			userDetailInfo.setCreatedBy(uploadMember.getCreatedBy());
			userWriteMapper.insertUserDetailInfo(userDetailInfo);
		} else {
			userWriteMapper.updateUserDetailInfo(userDetailInfo);
		}
		sendMQ(userBasicsInfo.getId());
		return new CommonPojoResponse(userBasicsInfo.getId(), userBasicsInfo.getVersion());
	}
	public CommonPojoResponse userUpdatePhone(UserUpdatePhoneRequest userRegisterRequest) throws MdmException {
		String version = System.currentTimeMillis() + "";
		if (StringUtil.isEmpty(userRegisterRequest.getPhone())) {
			throw new MdmException("手机号不能为空");
		}
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(userRegisterRequest.getUserId());
		UserDetailInfo userDetailInfo = userWriteMapper.queryUserDetailInfoByUserId(userRegisterRequest.getUserId());
		if (userBasicsInfo == null || userDetailInfo == null) {
			throw new MdmException("该用户不存在");
		}
		/*
		 * if (userDetailInfo != null && userDetailInfo.getPhoneNumberConfirmed() == 1) { throw new
		 * MdmException("该用户已经认证，不用重复认证"); } 认证之后还能继续认证
		 */
		userDetailInfo.setPhoneNumberConfirmed(1);
		logger.info("认证手机号：{}", userRegisterRequest.getPhone());
		validateCellPhone(null, userRegisterRequest.getPhone());
		logger.info("认证手机号：{},{}", userRegisterRequest.getPhone(), "通过");
		/****** 验证不通过，直接抛异常到Controller处理 *****/
		if (captchaService.checkCaptchaByCaptchaIdAndCode(userRegisterRequest.getCaptchaId(),
				userRegisterRequest.getCaptcha(), userRegisterRequest.getPhone())) {
			String old_version = userBasicsInfo.getVersion();
			userBasicsInfo.setVersion(System.currentTimeMillis() + "");
			userBasicsInfo.setModifiedBy(HttpContent.getOperatorId());
			userBasicsInfo.setCellPhone(userRegisterRequest.getPhone());
			if (null != userBasicsInfo.getEncryptionMode()) {
				userBasicsInfo.seteMode(handleEncryptionMode(userBasicsInfo.getEncryptionMode()));
			}
			userWriteMapper.updateUserBasicsInfo(userBasicsInfo, old_version);
			userWriteMapper.updateUserDetailInfo(userDetailInfo);
			sendMQ(userRegisterRequest.getUserId());
			return new CommonPojoResponse(userBasicsInfo.getId(), userBasicsInfo.getVersion());
		}
		return new CommonPojoResponse(userRegisterRequest.getUserId(), version);
	}
	/**
	 * 批量删除用户
	 * @param ids
	 * @return
	 * @Title: deleteUsers
	 * @Description: TODO
	 * @return: int
	 */
	public List<CommonPojoResponse> batchDeleteUsers(List<String> ids) throws MdmException {
		List<CommonPojoResponse> re = new ArrayList<CommonPojoResponse>();
		ids.forEach(id -> {
			try {
				CommonPojoResponse commonPojoResponse = this.deleteUserInfo(id);
				re.add(commonPojoResponse);
			} catch (MdmException e) {
				logger.error("批量删除用户出现异常");
			}
		});
		return re;
	}
	/**
	 * 修改用户密码
	 * @param id
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 * @throws MdmException
	 * @Title: updatePasswordByPassword
	 * @Description: TODO
	 * @return: int
	 */
	public CommonPojoResponse updatePasswordByPassword(@Param("id") String id, @Param("newPassword") String newPassword,
			@Param("oldPassword") String oldPassword) throws MdmException {
		String version = System.currentTimeMillis() + "";
		UserBasicsInfo user = userWriteMapper.queryUserBasicsInfoById(id);
		oldPassword = getEncryptionPasswordByName(user.getAccount(), oldPassword, user.getEncryptionMode());
		int num = userWriteMapper.updatePasswordByPassword(id, EncryptionUtil.getEncryptionByMD5(newPassword),
				oldPassword, version);
		if (num != 1)
			throw new MdmException("原密码不正确");
		sendMQ(id);
		return new CommonPojoResponse(id, version);
	}
	/**
	 * 重置密码（批量）
	 * @param id
	 * @return
	 * @Title: resetPassword
	 * @Description: TODO
	 * @return: int
	 */
	public List<ResetPasswordResponse> resetPassword(List<String> ids) throws MdmException {
		String version = System.currentTimeMillis() + "";
		List<ResetPasswordResponse> responses = new ArrayList<ResetPasswordResponse>();
		String randomPD = randomPD();
		ids.forEach(userId -> {
			String password = EncryptionUtil.getEncryptionByMD5(randomPD);
			// 获取用户手机号，发送短信
			UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(userId);
			if (null != userBasicsInfo) {
				userWriteMapper.resetPassword(userId, password, version);
				sendSMS(randomPD, userBasicsInfo.getCellPhone());
				sendMQ(userId);
				responses.add(new ResetPasswordResponse(userId, version, randomPD));
			}
		});
		return responses;
	}
	/**
	 * 发送短信
	 * @Title: sendSMS
	 * @Description: TODO
	 * @param password
	 * @param cellPhone
	 * @return: void
	 */
	private void sendSMS(String password, String cellPhone) {
		String timestamp = DateUtils.getTodayStr(DateUtils.TIMESTAMP);
		Calendar calendar = Calendar.getInstance();
		String smsContent = "您于" + calendar.get(Calendar.YEAR) + "年 " + (calendar.get(Calendar.MONTH) + 1) + "月 "
				+ (calendar.get(Calendar.DAY_OF_MONTH)) + "日，重置的密码为" + password + " 。请尽快修改";
		JSONObject object = new JSONObject();
		object.put("partnerId", partKey);
		object.put("timeStamp", timestamp);
		object.put("encryptKey", encryptKey);
		object.put("encryptStr", getEncryptStr(encryptKey, timestamp, cellPhone + smsContent));
		JSONObject param = new JSONObject();
		param.put("mobile", cellPhone);
		param.put("text", smsContent);
		object.put("param", param);
		String url = serverHost + singleUrl;
		String restStr = RestUtil.sendData(url, "POST", object.toString(), 60000, null, null, 0);
		if (StringUtil.isNotEmpty(restStr)) {
			JSONObject resultObj = JSONObject.parseObject(restStr);
			if (resultObj.containsKey("resultCode") && resultObj.getInteger("resultCode") == 1) {
				logger.info("重置密码，发送密码成功");
			} else {
				logger.error("重置密码，发送短信失败");
			}
		} else {
			logger.error("重置密码，发送短信失败");
		}
	}
	/**
	 * 获取随机6位字符串
	 * @return
	 * @Title: randomPD
	 * @Description: TODO
	 * @return: String
	 */
	private String randomPD() {
		String result = "";
		for (int i = 0; i < 6; i++) {
			int intVal = (int) (Math.random() * 26 + 97);
			result += (char) intVal;
		}
		return result;
	}
	private String getEncryptStr(String key, String timestamp, String param) {
		List<String> params = new ArrayList<String>();
		params.add(key);
		params.add(timestamp);
		params.add(param);
		StringBuffer sb = new StringBuffer();
		for (String str : params) {
			sb.append(str);
		}
		return MD5Util.MD5(sb.toString()).toUpperCase();
	}
	/**
	 * 批量更新用户状态
	 * @param ids、status
	 * @return
	 * @Title: batchUpdateStatus
	 * @Description: TODO
	 * @return: int
	 */
	public List<CommonPojoResponse> batchUpdateStatus(@Param("ids") List<String> ids, @Param("flag") int flag) {
		String version = System.currentTimeMillis() + "";
		userWriteMapper.batchUpdateStatus(ids, flag, version);
		List<CommonPojoResponse> re = new ArrayList<CommonPojoResponse>();
		ids.forEach(id -> {
			sendMQ(id);
			re.add(new CommonPojoResponse(id, version));
		});
		return re;
	}
	/**
	 * 针对用户发送MQ消息
	 * @param id
	 * @Title: sendMQ
	 * @Description: TODO
	 * @return: void
	 */
	public void sendMQ(String id) {
		// 获取用户基础信息
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoForMQById(id);
		String systemCode = invokeSystemService.querySysCodeById(userBasicsInfo.getSystemId(), null);
		userBasicsInfo.setSystemCode(systemCode);
		// 获取用户明细信息
		UserDetailInfo userDetailInfo = userWriteMapper.queryUserDetailInfoByUserId(id);
		// 获取用户渠道
		List<UserChannel> userChannel = userWriteMapper.queryUserChannelList(id);
		// 初始化用户信息mq消息队列实体
		UserInfoMqResponse userInfoMqResponse = new UserInfoMqResponse();
		userInfoMqResponse.init(userBasicsInfo, userDetailInfo, userChannel);
		// 发送用户信息MQ消息、记录操作日志
		mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.USER.getValue(), userInfoMqResponse);
		// 获取用户组织角色关联关系
		List<UserOrganizationRole> userOrganizationRoleList = userWriteMapper.queryUserOrganizationRoleList(id);
		// 初始化用户组织结构角色关联信息
		if (null != userOrganizationRoleList && userOrganizationRoleList.size() > 0) {
			userOrganizationRoleList.forEach(model -> {
				// 用户组织机构下发，根据systemId初始化systemCode
				String sysCode = invokeSystemService.querySysCodeById(model.getSystemId(), model.getSystemCode());
				model.setSystemCode(sysCode);
			});
		}
		// 初始化用户组织机构角色mq消息队列实体
		UserOrganizationRoleMqResponse userOrganizationRoleMqResponse = new UserOrganizationRoleMqResponse();
		// 初始化用户组织机构角色mq消息队列实体
		userOrganizationRoleMqResponse.init(id, userOrganizationRoleList, userBasicsInfo.getCorpCode());
		// 发送用户组织机构角色mq消息、记录操作日志
		mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.SOUR.getValue(),
				userOrganizationRoleMqResponse);
	}
	/**
	 * 查询来源系统--静态数据
	 * @return
	 * @Title: querySystemInfoDataList
	 * @Description: TODO
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String, Object>> querySystemInfoDataList() {
		List<Map<String, Object>> list = systemInfoWriteMapper.querySystemInfoDataList();
		return list;
	}
	/**
	 * 验证账号或手机是否可用（已认证）
	 * @param id
	 * @param validValue
	 * @param validType
	 * @return
	 * @Title: validAccount
	 * @Description: TODO
	 * @return: boolean
	 */
	private void validateCellPhone(String id, String cellPhone) throws MdmException {
		if (userWriteMapper.validateCellPhone(cellPhone, id) > 0) {
			throw new MdmException(10, "已存在相同已认证手机号");
		}
	}
	/**
	 * 验证手机是否可用
	 * @Title: validateCellPhoneNoS
	 * @Description: TODO
	 * @param id
	 * @param cellPhone
	 * @throws MdmException
	 * @return: void
	 */
	private void validateCellPhoneNoS(String id, String cellPhone) throws MdmException {
		if (userWriteMapper.validateCellPhoneNoS(cellPhone, id) > 0) {
			throw new MdmException(10, "已存在相同手机号");
		}
	}
	/**
	 * 控制字段修改权限（修改用户）
	 * @param userBasicsInfo
	 * @param userDetailInfo
	 * @param systemCode
	 * @throws Exception
	 * @Title: filterColumns
	 * @Description: TODO
	 * @return: void
	 */
	private void filterColumns(UserBasicsInfo userBasicsInfo, UserDetailInfo userDetailInfo, String systemCode)
			throws Exception {
		List<StaticData> staticDatas = staticDataService.queryStaticDataList("UserControlType", null);
		String[] keys = {};
		Class<?> cls = null;
		Object obj = null;
		for (StaticData staticData : staticDatas) {
			// 判断系统类型
			if (staticData.getColType().equalsIgnoreCase(systemCode)
					|| staticData.getColValue().equalsIgnoreCase(systemCode)) {
				if (null != userBasicsInfo && staticData.getColName().indexOf("SICS") > 0) {
					cls = userBasicsInfo.getClass();
					obj = userBasicsInfo;
					// 限制字段
					if (null != staticData.getDescription()) {
						keys = staticData.getDescription().split(",");
					}
				}
				if (null != userDetailInfo && staticData.getColName().indexOf("TAIL") > 0) {
					cls = userDetailInfo.getClass();
					obj = userDetailInfo;
					// 限制字段
					if (null != staticData.getDescription()) {
						keys = staticData.getDescription().split(",");
					}
				}
			}
		}
		// 为限制字段设置空值（null）
		for (String key : keys) {
			for (Field field : cls.getDeclaredFields()) {
				String fieldName = field.getName();
				if (key.equalsIgnoreCase(fieldName)) {
					// 设置为null
					field = cls.getDeclaredField(fieldName);
					field.setAccessible(true);
					/*
					 * // 判断是否为基本类型 if (field.getType().isPrimitive()) { break; }
					 */
					field.set(obj, null);
					break;
				}
			}
		}
	}
	public CommonPojoResponse checkCaptchaLogin(CaptchaLoginRequest captchaRequest, String ip) throws MdmException {
		/****** 验证不通过，直接抛异常到Controller处理 *****/
		if (captchaService.checkCaptchaByCaptchaIdAndCode(captchaRequest.getCaptchaId(), captchaRequest.getCaptcha(),
				captchaRequest.getPhone())) {
			UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserByCellphoneNoS(captchaRequest.getPhone());
			if (userBasicsInfo == null) {
				throw new MdmException(100, "该用户不存在");
			}
			// 修改手机号验资状态为1
			UserDetailInfo userDetailInfo = new UserDetailInfo();
			userDetailInfo.setUserId(userBasicsInfo.getId());
			userDetailInfo.setPhoneNumberConfirmed(1);
			userDetailInfo.setLoginIp(ip);
			userDetailInfo.setLastLoginTime(DateUtils.currentDate());
			userWriteMapper.updateUserDetailInfo(userDetailInfo);
			// MQ下发
			sendMQ(userBasicsInfo.getId());
			return new CommonPojoResponse(userBasicsInfo.getId(), userBasicsInfo.getVersion());
		}
		return null;
	}
	/**
	 * 验证手机号是否重复（内部）
	 * @Title: verifyPhone
	 * @Description: TODO
	 * @param cellPhone
	 * @param id
	 * @return
	 * @throws MdmException
	 * @return: String
	 */
	public String verifyPhone(String cellPhone, String id) throws MdmException {
		// 验证手机是否可用
		int num = userWriteMapper.verifyPhone(cellPhone, id);
		if (num > 0) {
			// 存在，查询用户信息
			if (null == id || id.equals("")) {
				UserBasicsInfo basicsInfo = userWriteMapper.queryUserByCellphone(cellPhone);
				// 用户渠道判断可以借助 UserBasicsInfo
				id = basicsInfo.getId();
				if ((basicsInfo.getUserChannels() != 0) && ((basicsInfo.getUserChannels() & 2) != 2)) {
					// 仅为前端用户(会员)
					return id;
				} else {
					return "error";
				}
			}
			UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(id);
			if (null != userBasicsInfo
					&& userBasicsInfo.getPhoneNumberConfirmed() == MdmConstants.PHONENUMBERCONFIRMED_ZERO) {
				return null;
			}
			return "error";
		}
		return null;
	}
	/**
	 * 验证手机号是否重复（内部）
	 * @Title: verifyPassword
	 * @Description:
	 * @param password
	 * @param id
	 * @return
	 * @throws MdmException
	 * @return: Map
	 */
	public Map<String, Boolean> validatePassword(String password, String id) throws MdmException {
		// 验证手机是否可用
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(StringUtils.lowerCase(id));
		if (userBasicsInfo == null)
			throw new MdmException("该用户不存在");
		if (getEncryptionPasswordByName(userBasicsInfo.getAccount(), password, userBasicsInfo.getEncryptionMode())
				.toLowerCase().equals(userBasicsInfo.getPassword().toLowerCase())) {
			resultMap.put("validateResult", true);
		} else {
			resultMap.put("validateResult", false);
		}
		return resultMap;
	}
	/**
	 * 设置用户类型（组织/商家）
	 * @param UserRoleList
	 * @param userBasicsInfo
	 * @throws MdmException
	 * @Title: getUserType
	 * @Description: TODO
	 * @return: void
	 */
	private void getUserType(List<UserOrganizationRole> UserRoleList, UserBasicsInfo userBasicsInfo)
			throws MdmException {
		int type = 0;
		// 判断用户选择的角色中是否含有商家数据
		for (UserOrganizationRole uor : UserRoleList) {
			int num = userWriteMapper.isMerChant(uor.getOrganizationId());
			if (num > 0) {
				type = 1;
				break;
			}
		}
		userBasicsInfo.setType(type);
	}
	public void updateUserDetailInfo(UserDetailInfo userDetailInfo) {
		userWriteMapper.updateUserDetailInfo(userDetailInfo);
	}
	/**
	 * 设置为后台用户
	 * @param userId
	 * @return
	 * @throws MdmException
	 * @Title: setBackEndUser
	 * @Description: TODO
	 * @return: UserChannel
	 */
	public UserChannel setBackEndUser(String userId, String corpCode) throws MdmException {
		// 修改用户基本信息中的UserChannels值
		UserChannel channel = new UserChannel();
		channel.init(userId);
		channel.setChannelCode(MdmConstants.BACKEND.toString());
		List<UserChannel> userChannelList = new ArrayList<UserChannel>();
		userChannelList.add(channel);
		userWriteMapper.insertUserChannelList(userChannelList);
		// 更新用户的userChannels(添加后端标识1)
		double channelValue = Math.pow(2, MdmConstants.BACKEND);
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(userId);
		userBasicsInfo.setUserChannels(userBasicsInfo.getUserChannels() | (new Double(channelValue)).intValue());
		String old_verseion = userBasicsInfo.getVersion();
		userBasicsInfo.setVersion(System.currentTimeMillis() + "");
		userBasicsInfo.setCorpCode(corpCode);// 设置corpCode
		if (null != userBasicsInfo.getEncryptionMode()) {
			userBasicsInfo.seteMode(handleEncryptionMode(userBasicsInfo.getEncryptionMode()));
		}
		userWriteMapper.updateUserBasicsInfo(userBasicsInfo, old_verseion);
		// MQ下发
		sendMQ(userId);
		return channel;
	}
	/**
	 * 设置为后台用户（合并用户组织角色，并设置type）
	 * @Title: setBackEndUserForMerge
	 * @Description: TODO
	 * @param userInfoRequest
	 * @return
	 * @throws MdmException
	 * @return: CommonPojoResponse
	 */
	public CommonPojoResponse setBackEndUserForMerge(UserInfoRequest userInfoRequest) throws MdmException {
		String userId = userInfoRequest.getUserBasicsInfoRequest().getId();
		// 修改用户基本信息中的UserChannels值
		UserChannel channel = new UserChannel();
		channel.init(userId);
		channel.setChannelCode(MdmConstants.BACKEND.toString());
		List<UserChannel> userChannelList = new ArrayList<UserChannel>();
		userChannelList.add(channel);
		userWriteMapper.insertUserChannelList(userChannelList);
		// 更新用户的userChannels(添加后端标识1)
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(userId);
		double channelValue = Math.pow(2, MdmConstants.BACKEND);
		userBasicsInfo.setUserChannels(userBasicsInfo.getUserChannels() | (new Double(channelValue)).intValue());
		String old_verseion = userBasicsInfo.getVersion();
		userBasicsInfo.setVersion(System.currentTimeMillis() + "");
		// 设置corpCode
		if (StringUtils.isEmpty(userBasicsInfo.getCorpCode())) {
			userBasicsInfo.setCorpCode(userInfoRequest.getUserBasicsInfoRequest().getCorpCode());
		}
		// 合并用户组织角色
		if (null != userInfoRequest.getUserRoleRequest() && userInfoRequest.getUserRoleRequest().size() > 0) {
			try {
				List<UserOrganizationRole> userOrganizationRoleList = new ArrayList<UserOrganizationRole>();
				userOrganizationRoleList.addAll(userWriteMapper.queryUserOrganizationRoleList(userId));
				userOrganizationRoleList.addAll(userInfoRequest.getUserRoleRequest());
				// 去除重复组织角色
				removeRepeat(userOrganizationRoleList);
				// 初始化用户组织结构角色关联信息
				userOrganizationRoleList.forEach(model -> {
					String sysId = invokeSystemService.querySystemId(model.getSystemId(), model.getSystemCode());
					model.init(userId, sysId);
					// 用户组织机构下发，根据systemId初始化systemCode
					String sysCode = invokeSystemService.querySysCodeById(model.getSystemId(), model.getSystemCode());
					model.setSystemCode(sysCode);
				});
				// 设置用户类型（根据组织角色设置）
				if (HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.SSY) || HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.MDM)) {
					getUserType(userOrganizationRoleList, userBasicsInfo);
				}
				// 合并组织角色关系
				userWriteMapper.deleteUserOrganizationRole(userId, null);
				userWriteMapper.insertUserOrganizationRoleList(userOrganizationRoleList);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new MdmException("合并组织角色信息失败");
			}
		} else {
			// 设置用户类型（页面传入）
			if (HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.SSY) || HttpContent.getSysCode().equalsIgnoreCase(MdmConstants.MDM)) {
				userBasicsInfo.setType(userInfoRequest.getUserBasicsInfoRequest().getType());
			}
		}
		if (null != userBasicsInfo.getEncryptionMode()) {
			userBasicsInfo.seteMode(handleEncryptionMode(userBasicsInfo.getEncryptionMode()));
		}
		userWriteMapper.updateUserBasicsInfo(userBasicsInfo, old_verseion);
		// MQ下发
		sendMQ(userId);
		return new CommonPojoResponse(userId, userBasicsInfo.getVersion());
	}
	/**
	 * 设置后端用户-加强版，覆盖前端账户
	 * @Title: setBackEndUserStrong
	 * @Description: TODO
	 * @param userId
	 * @param account
	 * @return
	 * @throws MdmException
	 * @return: UserChannel
	 */
	public UserChannel setBackEndUserStrong(String userId, String account) throws MdmException {
		// 修改用户基本信息中的UserChannels值
		UserChannel channel = new UserChannel();
		channel.init(userId);
		channel.setChannelCode(MdmConstants.BACKEND.toString());
		List<UserChannel> userChannelList = new ArrayList<UserChannel>();
		userChannelList.add(channel);
		userWriteMapper.insertUserChannelList(userChannelList);
		// 更新用户的userChannels(添加后端标识1)
		double channelValue = Math.pow(2, MdmConstants.BACKEND);
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(userId);
		userBasicsInfo.setAccount(account);
		userBasicsInfo.setUserChannels(userBasicsInfo.getUserChannels() | (new Double(channelValue)).intValue());
		String old_verseion = userBasicsInfo.getVersion();
		userBasicsInfo.setVersion(System.currentTimeMillis() + "");
		if (null != userBasicsInfo.getEncryptionMode()) {
			userBasicsInfo.seteMode(handleEncryptionMode(userBasicsInfo.getEncryptionMode()));
		}
		userWriteMapper.updateUserBasicsInfo(userBasicsInfo, old_verseion);
		return channel;
	}
	/**
	 * 用户解绑
	 * @Title: unBindingPhone
	 * @Description: TODO
	 * @param unBindingPhoneRequest
	 * @return
	 * @throws MdmException
	 * @return: CommonPojoResponse
	 */
	public CommonPojoResponse unBindingPhone(UnBindingPhoneRequest unBindingPhoneRequest) throws MdmException {
		if (StringUtil.isEmpty(unBindingPhoneRequest.getCellPhone())) {
			throw new MdmException("手机号不能为空");
		}
		if (userWriteMapper.validateCellPhone(unBindingPhoneRequest.getCellPhone(), null) < 1) {
			throw new MdmException("手机号不存在或用户未绑定");
		}
		UserBasicsInfo userBasicsInfo = userWriteMapper.queryUserBasicsInfoById(unBindingPhoneRequest.getUserId());
		UserDetailInfo userDetailInfo = userWriteMapper.queryUserDetailInfoByUserId(unBindingPhoneRequest.getUserId());
		if (userBasicsInfo == null || userDetailInfo == null) {
			throw new MdmException("该用户不存在");
		}
		String sysCode = HttpContent.getSysCode();
		Integer newState = resetBindState(userBasicsInfo.getBindState(),sysCode, MdmConstants.PHONENUMBERCONFIRMED_ZERO.toString());
		if (sysCode.equals(MdmConstants.SSY)) {
			newState = resetBindState(newState,MdmConstants.SCM, MdmConstants.PHONENUMBERCONFIRMED_ZERO.toString());
		}
		if (sysCode.equals(MdmConstants.SCM)) {
			newState = resetBindState(newState,MdmConstants.SSY, MdmConstants.PHONENUMBERCONFIRMED_ZERO.toString());
		}
		// 设置解绑
		String version = System.currentTimeMillis() + "";
		userBasicsInfo.setVersion(version);
		// 设置手机绑定状态--业务系统的
		userBasicsInfo.setBindState(newState);
		if (null != userBasicsInfo.getEncryptionMode()) {
			userBasicsInfo.seteMode(handleEncryptionMode(userBasicsInfo.getEncryptionMode()));
		}
		userWriteMapper.updateUserBasicsInfo(userBasicsInfo, null);
		sendMQ(unBindingPhoneRequest.getUserId());
		return new CommonPojoResponse(unBindingPhoneRequest.getUserId(), userBasicsInfo.getVersion());
	}
	
	private Integer handleEncryptionMode(Object encryptionMode){
		Integer eType = 1;
		if (encryptionMode instanceof java.lang.Integer) {
			return Integer.valueOf(encryptionMode.toString());
		}else{
			if (encryptionMode.toString().equalsIgnoreCase("MD5")) {
				eType = 1;
			}
			if (encryptionMode.toString().equalsIgnoreCase("HASH")) {
				eType = 2;
			}
			if (encryptionMode.toString().equalsIgnoreCase("UNPWDMD5")) {
				eType = 3;
			}
		}
		return eType;
	}
	
	/**
	 * 去除重复组织角色关系
	 * @Title: removeRepeat
	 * @Description: TODO
	 * @param list
	 * @return: void
	 */
	private void removeRepeat(List<UserOrganizationRole> list) {
		if (null != list && list.size() > 0) {
			List<UserOrganizationRole> newList = new ArrayList<UserOrganizationRole>();
			for (UserOrganizationRole uor : list) {
				if (newList.size() < 1) {
					newList.add(uor);
				} else {
					boolean b = false;
					for (UserOrganizationRole uro : newList) {
						if (uro.getRoleId().toLowerCase().equals(uor.getRoleId().toLowerCase()) && uro
								.getOrganizationId().toLowerCase().equals(uor.getOrganizationId().toLowerCase())) {
							b = true;
							break;
						}
					}
					if (!b) {
						newList.add(uor);
					}
				}
			}
			list.clear();
			list.addAll(newList);
		}
	}
	
	/**
	 * 根据数据类型，获取不同类型的状态值（二进制/十进制）
	 * @Title: analysisBindState 
	 * @Description: TODO
	 * @param bindState
	 * @return
	 * @return: Object
	 */
	public static Object analysisBindState(Object bindState){
		if (null == bindState) {
			bindState = 0;
		}
		// 十进制转二进制
		if (bindState instanceof java.lang.Integer) {
			
			StringBuffer sb = new StringBuffer(Integer.toBinaryString((Integer)bindState)).reverse();
			if (sb.length()< (MdmConstants.STATE_SYSCODE.length-2)) {
				int len = MdmConstants.STATE_SYSCODE.length-2-sb.length();
				for(int i =0; i < len; i++){
					sb.append("0");
				}
			}
			return sb.toString();
		}else{
			// 二进制转十进制
			StringBuffer sb = new StringBuffer(bindState.toString());
			Integer intValue = Integer.parseInt(sb.reverse().toString(),2);
			return intValue;
		}
	}
	/**
	 * 根据业务系统Code，设置业务系统绑定的状态
	 * @Title: resetBindState 
	 * @Description: TODO
	 * @param bindState
	 * @param sysCode
	 * @param state
	 * @return
	 * @return: Integer
	 */
	public static Integer resetBindState(Integer bindState, String sysCode, String state){
		// 获取数据绑定状态，进行分析
		String states = (String)analysisBindState(bindState);
		
		int index = ArrayUtils.indexOf(MdmConstants.STATE_SYSCODE, sysCode);
		if (index == 6) {// 物业云不同的系统编码BMS
			index = 1;
		}
		if (index == 7) {// 收费云不同系统编码SFYOFF
			index = 5;
		}
		
		StringBuffer buffer = new StringBuffer(states);
		buffer.replace(index, index+1, state);
		
		return (Integer)analysisBindState(buffer);
	}
	/**
	 * 用户界面展示，各个业务系统的数据绑定状态
	 * @Title: handViewBindState 
	 * @Description: TODO
	 * @param bindState
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	private List<Map<String, Object>> handViewBindState(Integer bindState){
		List<Map<String, Object>> sysBindStates = new ArrayList<Map<String, Object>>();
		if (null != bindState) {
			String states = (String)analysisBindState(bindState);
			
			for (String sysCode : MdmConstants.STATE_SYSCODE) {
				int index = ArrayUtils.indexOf(MdmConstants.STATE_SYSCODE, sysCode);
				if (index == 6) {// 物业云不同的系统编码BMS
					index = 1;
				}
				if (index == 7) {// 收费云不同系统编码SFYOFF
					index = 5;
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sysCode", sysCode);
				map.put("bindState", Integer.parseInt(String.valueOf(states.charAt(index))));
				sysBindStates.add(map);
			}
		}else{
			for (String sysCode : MdmConstants.STATE_SYSCODE) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sysCode", sysCode);
				map.put("bindState", 0);
				sysBindStates.add(map);
			}
		}
		return sysBindStates;
	}
	
}

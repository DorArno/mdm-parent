/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: UserController.java 
 * @Prject: mdm-web
 * @Package: com.mdm.controller.web.user 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年10月12日 下午4:17:39 
 * @version: V1.0   
 */
package com.mdm.controller.web.user;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.controller.BaseController;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.UserOrganizationRole;
import com.mdm.request.BatchStatusRequest;
import com.mdm.request.CaptchaUpdateUserPasswordRequest;
import com.mdm.request.CaptchaUpdateUserPasswordRequest2;
import com.mdm.request.CheckPhoneRequest;
import com.mdm.request.UnBindingPhoneRequest;
import com.mdm.request.UpdatePasswordRequest;
import com.mdm.request.UploadMember;
import com.mdm.request.UserGridRequest;
import com.mdm.request.UserInfoRequest;
import com.mdm.request.UserInfoSFRequest;
import com.mdm.request.UserRegisterRequest;
import com.mdm.request.UserUpdatePhoneRequest;
import com.mdm.request.VerifyPasswordRequest;
import com.mdm.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: UserController
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年10月12日 下午4:17:39
 */
@Controller
@Api("用户")
@RequestMapping("/user")
public class UserController extends BaseController {
	@Autowired
	UserService userService;

	@ApiOperation(value = "新增用户", notes = "新增用户")
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView insert(@RequestBody UserInfoRequest userInfoRequest) throws MdmException {
		if(HttpContent.getSysCode().equals(MdmConstants.MDM)){
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(userService.insertUserInfo(userInfoRequest)));
		}else{
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(userService.mergeUserInfo(userInfoRequest)));
		}
	}
	
	@ApiOperation(value = "新增用户", notes = "新增用户基本信息及明细信息")
	@RequestMapping(value = "/insertUserInfo", method = RequestMethod.POST)
	public ModelAndView insertUserInfo(@RequestBody UserInfoSFRequest userInfoRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.insertUserForSF(userInfoRequest)));
	}
	@ApiOperation(value = "更新用户", notes = "更新用户")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ModelAndView update(@PathVariable String id, @RequestBody UserInfoRequest userInfoRequest)
			throws MdmException {
		userInfoRequest.getUserBasicsInfoRequest().setId(id);
		userInfoRequest.getUserDetailInfoRequest().setUserId(id);
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.updateUserInfo(userInfoRequest)));
	}
	@ApiOperation(value = "更新用户", notes = "更新用户")
	@RequestMapping(value = "/updateUserInfo/{id}", method = RequestMethod.PUT)
	public ModelAndView updateUserInfo(@PathVariable String id, @RequestBody UserInfoSFRequest userInfoRequestSF)
			throws MdmException {
		//	转换消息实体
		UserInfoRequest userInfoRequest = new UserInfoRequest();
		userInfoRequest.setUserBasicsInfoRequest(userInfoRequestSF.getUserBasicsInfoRequest());
		userInfoRequest.setUserDetailInfoRequest(userInfoRequestSF.getUserDetailInfoRequest());
		userInfoRequest.setUserChannel(userInfoRequestSF.getUserChannel());
		
		userInfoRequest.getUserBasicsInfoRequest().setId(id);
		userInfoRequest.getUserDetailInfoRequest().setUserId(id);
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.updateUserInfo(userInfoRequest)));
	}
	@ApiOperation(value = "删除用户", notes = "删除用户")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable String id) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult(userService.deleteUserInfo(id)));
	}
	@ApiOperation(value = "根据用户Id获取用户信息（内部编辑）", notes = "根据用户Id获取用户信息")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView queryById(@RequestParam(value = "id", required = true) String id) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.queryUserInfoById(id)));
	}
	@ApiOperation(value = "根据用户Id获取用户信息（对外开放API）", notes = "根据用户Id获取用户信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView queryUserInfoById(@PathVariable String id) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult(userService.queryById(id)));
	}
	@ApiOperation(value = "获取用户列表", notes = "获取用户列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ModelAndView queryList(@RequestBody UserGridRequest userGridRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.queryUserListByOrganizationId(userGridRequest)));
	}
	@ApiOperation(value = "分配用户组织机构角色", notes = "分配用户组织机构角色")
	@RequestMapping(value = "/SetOrganizationRole/{id}", method = RequestMethod.POST)
	public ModelAndView setOrganizationRole(@PathVariable String id, @PathVariable String corpCode,
			@RequestBody List<UserOrganizationRole> userOrganizationRoles) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.setOrganizationRole(id, corpCode, userOrganizationRoles)));
	}
	@ApiOperation(value = "增加用户组织机构角色", notes = "增加用户组织机构角色")
	@RequestMapping(value = "/AddOrganizationRole", method = RequestMethod.POST)
	public ModelAndView addOrganizationRole(@RequestBody List<UserOrganizationRole> userOrganizationRoles)
			throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(userService.addOrganizationRole(userOrganizationRoles)));
	}
	@ApiOperation(value = "分配用户组织机构角色", notes = "分配用户组织机构角色")
	@RequestMapping(value = "/SetOrganizationRoleForRole/{id}", method = RequestMethod.POST)
	public ModelAndView setOrganizationRoleForRole(@PathVariable String id,
			@RequestBody List<UserOrganizationRole> userOrganizationRoles) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.setOrganizationRoleForRole(id, userOrganizationRoles)));
	}
	@ApiOperation(value = "获取用户列表", notes = "获取用户列表")
	@RequestMapping(value = "/memberlist", method = RequestMethod.POST)
	public ModelAndView queryMemberList(@RequestBody UserGridRequest userGridRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.queryMemberList(userGridRequest)));
	}
	@ApiOperation(value = "验证手机号是否注册", notes = "用户")
	@RequestMapping(value = "/CheckPhoneNum", method = RequestMethod.POST)
	public ModelAndView CheckPhoneNum(@RequestBody CheckPhoneRequest userGridRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult("exist",
				userService.checkPhoneNumAndAccountRegister(userGridRequest.getPhoneNum())));
	}
	@ApiOperation(value = "用户注册", notes = "用户")
	@RequestMapping(value = "/UserRegister", method = RequestMethod.POST)
	public ModelAndView UserRegister(@RequestBody UserRegisterRequest userRegisterRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.userRegister(userRegisterRequest)));
	}
	@ApiOperation(value = "用户认证手机号和重新认证", notes = "用户")
	@RequestMapping(value = "/UserUpdatePhone", method = RequestMethod.POST)
	public ModelAndView UserRegister(@RequestBody UserUpdatePhoneRequest userRegisterRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.userUpdatePhone(userRegisterRequest)));
	}
	@ApiOperation(value = "验证码修改用户密码", notes = "用户")
	@RequestMapping(value = "/CaptchaUpdateUserPassword", method = RequestMethod.POST)
	public ModelAndView CaptchaUpdateUserPassword(
			@RequestBody CaptchaUpdateUserPasswordRequest captchaUpdateUserPasswordRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.captchaUpdateUserPasswordRequest(captchaUpdateUserPasswordRequest)));
	}
	/**
	 * @param captchaUpdateUserPasswordRequest
	 * @return
	 * @throws MdmException
	 */
	@ApiOperation(value = "忘记密码", notes = "用户")
	@RequestMapping(value = "/ForgetPassword", method = RequestMethod.POST)
	public ModelAndView CaptchaUpdateUserPassword2(
			@RequestBody CaptchaUpdateUserPasswordRequest2 captchaUpdateUserPasswordRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.forgetPassword(captchaUpdateUserPasswordRequest)));
	}
	@ApiOperation(value = "同步会员信息", notes = "用户")
	@RequestMapping(value = "/UploadSyncMember", method = RequestMethod.POST)
	public ModelAndView UploadSyncMember(@RequestBody UploadMember uploadMember) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.uploadSyncMember(uploadMember)));
	}
	@ApiOperation(value = "批量删除用户", notes = "批量删除用户")
	@RequestMapping(value = "/BatchDelete", method = RequestMethod.PUT)
	public ModelAndView batchDeleteUsers(@RequestBody BatchStatusRequest batchStatusRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.batchDeleteUsers(batchStatusRequest.getIds())));
	}
	@ApiOperation(value = "根据原始密码修改密码", notes = "根据原始密码修改密码")
	@RequestMapping(value = "/UpdatePassword/{id}", method = RequestMethod.PUT)
	public ModelAndView updatePasswordByPassword(@PathVariable String id,
			@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.updatePasswordByPassword(id, updatePasswordRequest.getNewPassword(),
						updatePasswordRequest.getOldPassword())));
	}
	@ApiOperation(value = "重置密码", notes = "重置密码")
	@RequestMapping(value = "/ResetPassword", method = RequestMethod.PUT)
	public ModelAndView resetPassword(@RequestBody BatchStatusRequest batchStatusRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.resetPassword(batchStatusRequest.getIds())));
	}
	@ApiOperation(value = "批量更新用户启用停用状态", notes = "批量更新用户启用停用状态")
	@RequestMapping(value = "/BatchUpdateStatus", method = RequestMethod.PUT)
	public ModelAndView batchUpdateStatus(@RequestBody BatchStatusRequest batchStatusRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult(
				userService.batchUpdateStatus(batchStatusRequest.getIds(), batchStatusRequest.getFlag())));
	}
	@ApiOperation(value = "查询来源系统", notes = "查询来源系统")
	@RequestMapping(value = "/systemInfo", method = RequestMethod.GET)
	public ModelAndView querySystemInfoDataList() throws MdmException {
		List<Map<String, Object>> list = userService.querySystemInfoDataList();
		return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult(list));
	}
	@ApiOperation(value = "验证手机号是否重复", notes = "验证手机号是否重复")
	@RequestMapping(value = "/verifyPhone", method = RequestMethod.POST)
	public Object verifyPhone(@RequestParam(value = "cellPhone", required = true) String cellPhone,@RequestParam(value = "id", required = true) String id) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.verifyPhone(cellPhone,id)));
	}
	
	@ApiOperation(value = "验证密码是否正确", notes = "验证密码是否正确")
	@RequestMapping(value = "/verifyPassword", method = RequestMethod.POST)
	public ModelAndView verifyPassword(@RequestBody VerifyPasswordRequest verifyPasswordRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.validatePassword(verifyPasswordRequest.getPassword(),verifyPasswordRequest.getUserId())));
	}
	
	@ApiOperation(value = "设置为后端用户", notes = "设置为后端用户")
	@RequestMapping(value = "/setBackEndUser", method = RequestMethod.POST)
	public Object setBackEndUser(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "corpCode", required = true) String corpCode) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.setBackEndUser(userId, corpCode)));
	}
	
	@ApiOperation(value = "设置为后端用户（合并）", notes = "设置为后端用户（合并）")
	@RequestMapping(value = "/setBackEndUserForMerge", method = RequestMethod.POST)
	public Object setBackEndUserForMerge(@RequestBody UserInfoRequest userInfoRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.setBackEndUserForMerge(userInfoRequest)));
	}
	
	@ApiOperation(value = "解绑用户认证手机号", notes = "解绑用户认证手机号")
	@RequestMapping(value = "/UnBindingPhone", method = RequestMethod.POST)
	public ModelAndView unBindingPhone(@RequestBody UnBindingPhoneRequest unBindingPhoneRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userService.unBindingPhone(unBindingPhoneRequest)));
	}
}

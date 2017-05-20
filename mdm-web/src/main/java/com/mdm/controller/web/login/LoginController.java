package com.mdm.controller.web.login;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.alibaba.fastjson.JSONObject;
import com.mdm.controller.BaseController;
import com.mdm.core.bean.common.CommonDataResponse;
import com.mdm.core.bean.response.AppLoginResponse;
import com.mdm.core.util.DateUtils;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.Role;
import com.mdm.pojo.UserDetailInfo;
import com.mdm.request.UserLoginRequest;
import com.mdm.request.UserLogoutRequest;
import com.mdm.service.login.LoginService;
import com.mdm.service.role.RoleService;
import com.mdm.service.user.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("login")
public class LoginController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Value("${ssoServerUrl}")
	private String ssoServerUrl;
	
	@Value("${localServerUrl}")
	private String localServerUrl;
	
	public String getSsoServerUrl() {
		return ssoServerUrl;
	}

	
	public void setSsoServerUrl(String ssoServerUrl) {
		this.ssoServerUrl = ssoServerUrl;
	}

	
	public String getLocalServerUrl() {
		return localServerUrl;
	}

	
	public void setLocalServerUrl(String localServerUrl) {
		this.localServerUrl = localServerUrl;
	}

	@Value("${server.context-path}")
	private String contextUrl;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService  userService;
	
    public static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";

	@RequestMapping("index/{userId}")
	public ModelAndView index(@PathVariable("userId") String userId) {
		List<Role> list = roleService.queryRoleByUserId(userId);
		if (CollectionUtils.isEmpty(list)) {
			return new ModelAndView(new MappingJackson2JsonView(), this.getSuccessResult(list));
		}
		if (isAdmin(list)) {
			// 是管理员则获取所有菜单
		}
		return null;
	}
	
	@RequestMapping("redirect")
	public ModelAndView redirect(HttpSession httpSession){
		if(httpSession.getAttribute("user")==null){
			String returnUrl = ssoServerUrl +"login?service="+localServerUrl +"mdm/test.html";
			return new ModelAndView(new RedirectView(returnUrl));
		}
		return new ModelAndView(new RedirectView(contextUrl+"/"));
	}
	
	
    /**
     * 登录校验
     *
     * @param user
     * @throws MdmException
     * @author wanglei
     */
	@ApiOperation(value="登陆校验", notes="根据输入的用户名密码进行校验")
    @RequestMapping(value="UserLogin",method=RequestMethod.POST)
    public Object loginCheck(@RequestBody UserLoginRequest userInfoRequest, HttpServletRequest request) throws MdmException {
		com.mdm.pojo.UserBasicsInfo  userBasicsInfo =  loginService.queryUserAccountOrPhone(userInfoRequest.getUserName());
		if(userBasicsInfo == null){
			return this.getFailResult("用户不存在", 100);
		}
		String pwd = userService.getEncryptionPasswordByName(userBasicsInfo.getAccount(), userInfoRequest.getPassword(),  userBasicsInfo.getEncryptionMode());
		if(pwd.equalsIgnoreCase(userBasicsInfo.getPassword()) || userBasicsInfo.getPassword().equalsIgnoreCase(userInfoRequest.getPassword())){
			UserDetailInfo userDetailInfo = new UserDetailInfo();
			userDetailInfo.setUserId(userBasicsInfo.getId());
			userDetailInfo.setLoginIp(request.getRemoteAddr());
			userDetailInfo.setLastLoginTime(DateUtils.currentDate());
			userService.updateUserDetailInfo(userDetailInfo);
			return this.getSuccessResult(new CommonDataResponse(userBasicsInfo.getId(), userBasicsInfo.getVersion()));
		}
		return this.getFailResult("密码错误", 12);
    }
	
	/**
	 * 
	 * @Title: loginCheckAPP 
	 * @Description: TODO
	 * @param userInfoRequest
	 * @param request
	 * @return
	 * @throws MdmException
	 * @return: Object
	 */
	@ApiOperation(value="登陆校验【APP】", notes="根据输入的用户名密码进行校验【APP】")
	@RequestMapping(value="UserAPILogin",method=RequestMethod.POST)
	public Object loginCheckAPP(@RequestBody UserLoginRequest userInfoRequest, HttpServletRequest request) throws MdmException {
		com.mdm.pojo.UserBasicsInfo  userBasicsInfo =  loginService.queryUserAccountOrPhone(userInfoRequest.getUserName());
		if(userBasicsInfo == null){
			return this.getFailResult("用户不存在", 100);
		}
		String pwd = userService.getEncryptionPasswordByName(userBasicsInfo.getAccount(), userInfoRequest.getPassword(),  userBasicsInfo.getEncryptionMode());
		if(pwd.equalsIgnoreCase(userBasicsInfo.getPassword()) || userBasicsInfo.getPassword().equalsIgnoreCase(userInfoRequest.getPassword())){
			UserDetailInfo userDetailInfo = new UserDetailInfo();
			userDetailInfo.setUserId(userBasicsInfo.getId());
			userDetailInfo.setLoginIp(request.getRemoteAddr());
			userDetailInfo.setLastLoginTime(DateUtils.currentDate());
			userService.updateUserDetailInfo(userDetailInfo);
			return this.getSuccessResult(new AppLoginResponse(userBasicsInfo.getId(), userBasicsInfo.getVersion(), userBasicsInfo.getCorpCode()));
		}
		return this.getFailResult("密码错误", 12);
	}
	
	@ApiOperation(value="登出", notes="登出操作")
    @RequestMapping(value="UserLogout",method=RequestMethod.POST)
	@ResponseBody
	public Object loginCheck(@RequestBody UserLogoutRequest userInfoRequest){
		//暂时没有业务逻辑
		logger.info("用户登出:{}", userInfoRequest.getUserId());
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("userId", userInfoRequest.getUserId() );
		return this.getSuccessResult(resultJSON);
	}
	
	@ApiOperation(value="登出", notes="sso登出操作")
    @RequestMapping(value="loginOut",method=RequestMethod.POST)
	public Object loginCheck(HttpSession httpSession){
		if(httpSession.getAttribute("user")!=null){
			httpSession.removeAttribute("user");
		}
		if(httpSession.getAttribute(CONST_CAS_ASSERTION)!=null){
			httpSession.removeAttribute(CONST_CAS_ASSERTION);
		}
		
		httpSession.invalidate();
		String returnUrl = ssoServerUrl +"logout?service="+localServerUrl +"mdm/test.html";
		return this.getSuccessResult(returnUrl);
	}

	/**
	 * 判断是否是管理员
	 * 
	 * @param list
	 * @return
	 */
	private boolean isAdmin(List<Role> list) {
		for (Role role : list) {
			if (StringUtils.equalsIgnoreCase(role.getCode(), "R001")) {
				return true;
			}
		}
		return false;
	}

}

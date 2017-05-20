/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月17日 下午4:18:00 *
**/ 

package com.mdm.controller.web.captchaI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.mdm.controller.BaseController;
import com.mdm.core.bean.common.CommonDataResponse;
import com.mdm.core.util.MdmException;
import com.mdm.request.CaptchaCheckRequest;
import com.mdm.request.CaptchaLoginRequest;
import com.mdm.request.CaptchaRequest;
import com.mdm.response.CommonPojoResponse;
import com.mdm.service.captchal.CaptchaService;
import com.mdm.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "captchal", description="验证码")
@RestController
@RequestMapping("/captchal")
public class CaptchaIController  extends BaseController {
	
	@Autowired
	private CaptchaService captchalService;
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "请求发送验证码")
	@RequestMapping(value="SendCaptchaSMS", method = RequestMethod.POST)
	public Object createCaptchal(@RequestBody CaptchaRequest captchaRequest) throws MdmException {
		return this.getResponseResult(captchalService.createCaptcha(captchaRequest));
	}
	
	@ApiOperation(value = "验证验证码")
	@RequestMapping(value ="UserCaptcha", method = RequestMethod.POST)
	public Object userCaptcha(@RequestBody CaptchaCheckRequest captchaRequest) throws MdmException {
		return this.getResponseResult(captchalService.checkCaptcha(captchaRequest));
	}
	
	@ApiOperation(value = "手机验证码登录")
	@RequestMapping(value ="UserCaptchaLogin", method = RequestMethod.POST)
	public Object userCaptchaLogin(@RequestBody CaptchaLoginRequest captchaRequest, HttpServletRequest request) throws MdmException {
		CommonPojoResponse response = userService.checkCaptchaLogin(captchaRequest, request.getRemoteAddr());
		if(response != null){
		     return this.getResponseResult(response);
		}
		return this.getFailResult("验证码错误",  11);
	}
	
	
	

}

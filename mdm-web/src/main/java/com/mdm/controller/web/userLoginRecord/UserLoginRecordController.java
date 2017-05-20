package com.mdm.controller.web.userLoginRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.controller.BaseController;
import com.mdm.core.util.MdmException;
import com.mdm.request.UserLoginRecordRequest;
import com.mdm.service.userLoginRecord.UserLoginRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @ClassName: UserLoginRecordController 
 * @Description: 用户登录日志
 * @author: Han
 * @date: 2016年11月10日 上午11:39:30
 */
@Api("用户登录日志")
@RestController
@RequestMapping("/userLoginRecord")
public class UserLoginRecordController extends BaseController {
	
	@Autowired
	private UserLoginRecordService userLoginRecordService;
	
	@ApiOperation(value = "获取用户登录记录列表", notes = "获取用户登录记录列表")
	@RequestMapping(value = "/userLoginRecordList", method = RequestMethod.POST)
	public ModelAndView queryUserLoginRecordList(@RequestBody UserLoginRecordRequest userLoginRecordRequest) throws MdmException {
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(userLoginRecordService.queryUserLoginRecordList(userLoginRecordRequest)));
	}
	
	
}

package com.mdm.aop;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.mdm.MyApplicationContextUtil;
import com.mdm.controller.BaseController;
import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.service.sys.StaticDataService;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.Operation;
import com.mdm.service.operation.OperationService;
import com.mdm.service.userrole.UserRoleService;

public class PermissionInterceptor extends HandlerInterceptorAdapter {

	private final PathMatcher matcher;
	
	private static Logger logger = LoggerFactory.getLogger(MdmInterceptor.class);

	public PermissionInterceptor() {
		this.matcher = new AntPathMatcher();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//开关判断
		StaticDataService staticDataService = MyApplicationContextUtil.getBean(StaticDataService.class);
		String switchFlag = staticDataService.getSingleConfigParam(MdmConstants.MDM_SWITCH, MdmConstants.OPER_AUTH_FLAG);
		if(switchFlag.isEmpty() || switchFlag.equals(MdmConstants.SWITCH_OFF)){//开关不存在或关闭状态无需拦截
			logger.info(MdmConstants.MDM_SWITCH +"---" +MdmConstants.OPER_AUTH_FLAG +": [0:OFF]");
			return true;
		}
		
		
		// 非mdm平台请求不拦截
		if (!StringUtils.equalsIgnoreCase(HttpContent.getSysCode(), MdmConstants.MDM)) {
			return true;
		}
		
		// 检查是否登录
		HttpSession session = request.getSession(false);
		if (session == null) {
			responseError(response);
			return false;
		}
		UserBasicsInfo user = (UserBasicsInfo) session.getAttribute("user");
		if (user == null) {
			responseError(response);
			return false;
		}
		// 超级管理员不拦截
		UserRoleService userRoleService = MyApplicationContextUtil.getBean(UserRoleService.class);
		if (userRoleService.isAdmin(user.getId())) {
			return true;
		}
		// 检查是否有权限
		if (!checkPermission(request, user.getId())) {
			throw new MdmException("对不起，你没有权限");
		}
		return true;
	}

	private void responseError(HttpServletResponse response) throws IOException {
		Map<String, Object> jsonMap = BaseController.createJson(true, HttpServletResponse.SC_MOVED_TEMPORARILY, "用户未登录",
				null);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(JSON.toJSONString(jsonMap));
		response.getWriter().flush();
	}

	/**
	 * 检查是否有权限
	 * 
	 * @param request
	 * @param userId
	 * @return
	 */
	private boolean checkPermission(HttpServletRequest request, String userId) {
		OperationService operationService = MyApplicationContextUtil.getBean(OperationService.class);
		List<Operation> list = operationService.queryUserRoleOpertionList(userId);
		for (Operation operation : list) {
			if (StringUtils.equalsIgnoreCase(request.getMethod(), operation.getHttpMethod())
					&& matcher.match(operation.getPath(), request.getServletPath())) {
				return true;
			}
		}
		return false;
	}

}

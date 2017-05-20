package com.mdm.controller.web.global;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdm.MyApplicationContextUtil;
import com.mdm.controller.BaseController;
import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.tree.TreeBuilder;
import com.mdm.pojo.Module;
import com.mdm.pojo.Operation;
import com.mdm.service.module.ModuleService;
import com.mdm.service.operation.OperationService;
import com.mdm.service.userrole.UserRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("公共管理")
@RestController
@RequestMapping("/global")
public class GlobalController extends BaseController {

	/**
	 * 代表具有所有操作权限
	 */
	private final static String ALL_PERMISSION = "all";

	@Autowired
	private ModuleService moduleService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private OperationService operationService;

	@Autowired
	private InvokeSystemService invokeSystemService;
	
	@RequestMapping(value = "/queryLoginName", method = RequestMethod.GET)
	public Object queryLoginName() {
		UserBasicsInfo user = (UserBasicsInfo) session.getAttribute("user");
		if (user != null) {
			return this.getResponseResult(user);
		}
		return this.getResponseResult("");
	}

	@ApiOperation(value = "查询菜单信息")
	@RequestMapping(value = "/queryMenuList", method = RequestMethod.GET)
	public Object queryAllModuleInfoList() {
		UserBasicsInfo user = (UserBasicsInfo) session.getAttribute("user");
		if (user == null) {
			return createJson(true, HttpServletResponse.SC_MOVED_TEMPORARILY, "用户未登录", null);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("userId", user.getId());
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		map.put("moduleState", GlobalConstants.STATUS_START);
		// 如果当前用户具有超级管理员权限，则显示所有菜单
		if (userRoleService.isAdmin(user.getId())) {
			List<Module> list = moduleService.queryAll(map);
			Set<Module> set = new TreeSet<>(list);
			return this.getResponseResult(set);
		}
		List<Module> list = moduleService.queryUserRoleModuleList(map);
		// 去重
		Set<Module> set = new TreeSet<>(list);
		return this.getResponseResult(set);
	}

	@ApiOperation(value = "查询用户角色权限信息")
	@RequestMapping(value = "/userOperation", method = RequestMethod.GET)
	public Object queryRoleOperationList() {
		UserBasicsInfo user = (UserBasicsInfo) session.getAttribute("user");
		if (user == null) {
			return createJson(true, HttpServletResponse.SC_MOVED_TEMPORARILY, "用户未登录", null);
		}
		if (userRoleService.isAdmin(user.getId())) {
			return this.getResponseResult(ALL_PERMISSION);
		}
		if(session.getAttribute("userOperation") == null){
			List<Operation> list = operationService.queryUserRoleOpertionList(user.getId());
			session.setAttribute("userOperation", list);
		}
		
		return this.getResponseResult(session.getAttribute("userOperation"));
	}

	@ApiOperation(value = "查询来源系统")
	@RequestMapping(value = "/systemInfos", method = RequestMethod.GET)
	public Object queryAllSystemInfo() {
		return this.getResponseResult(invokeSystemService.queryAllSystemInfo());
	}

	@ApiOperation(value = "生成树形结构")
	@RequestMapping(value = "/renderTree", method = RequestMethod.GET)
	public Object renderStaticTree(HttpServletRequest request,
			@RequestParam(value = "builder", required = true) String builder,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "checkable", required = false) boolean checkable,
			@RequestParam(value = "radio", required = false) boolean radio,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "needroot", required = false) boolean needroot,
			@RequestParam(value = "parameter", required = false) String parameter)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String treeId = StringUtils.isNotBlank(id) ? id : "tree";
		StringBuilder result = new StringBuilder();
		result.append("<ul id=\"").append(treeId).append("\" class=\"tree\"></ul>");
		result.append("<script>");
		result.append("var setting = {expandSpeed:''");
		if (checkable) {
			result.append(",checkable:true");
		}
		if (radio) {
			result.append(",checkStyle:'radio'");
			result.append(",checkRadioType:'").append("all").append("'");
		}
		if (StringUtils.isNotBlank(callback)) {
			result.append(",callback:").append(callback);
		}
		result.append("};");
		TreeBuilder tb = MyApplicationContextUtil.getBean(builder, TreeBuilder.class);
		tb.setNeedroot(needroot);
		tb.setParameter(parameter);
		result.append("var data = ").append(tb.printScript(request)).append(" ;");
		result.append(treeId).append(" = $('#").append(treeId).append("').zTree(setting, data);");
		result.append("</script>");
		return this.getResponseResult(result.toString());
	}

}

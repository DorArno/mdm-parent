package com.mdm.controller.web.module;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdm.controller.BaseController;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.Module;
import com.mdm.pojo.Operation;
import com.mdm.pojo.RoleModule;
import com.mdm.pojo.RoleOperation;
import com.mdm.service.module.ModuleService;
import com.mdm.service.operation.OperationService;
import com.mdm.service.rolemodule.RoleModuleService;
import com.mdm.service.roleoperation.RoleOperationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("菜单管理")
@RestController
@RequestMapping("/module")
public class ModuleController extends BaseController {

	@Autowired
	private ModuleService service;

	@Autowired
	private RoleModuleService rmService;

	@Autowired
	private OperationService oService;

	@Autowired
	private RoleOperationService roService;

	@ApiOperation(value = "查询菜单信息")
	@RequestMapping(value = "/moduleInfos", method = RequestMethod.GET)
	public Object queryModuleInfoList(@RequestParam(value = "moduleName", required = false) String moduleName,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "pageNum", required = false) int pageNum,
			@RequestParam(value = "pageSize", required = false) int pageSize) {
		Map<String, Object> map = new HashMap<>();
		map.put("moduleName", moduleName);
		map.put("status", status);
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		return this.getResponseResult(service.queryPageList(map, pageNum, pageSize));
	}

	private void validate(Module module) throws MdmException {
		
			if (module.getModuleName().length() > 50) {
				throw new MdmException("菜单名称不能超过50个字符");
			}
			if (StringUtils.isNotBlank(module.getDescription())
					&& module.getDescription().length() > 200) {
				throw new MdmException("描述不能超过200个字符");
			}
		
	}

	@ApiOperation(value = "新增菜单信息")
	@RequestMapping(value = "/moduleInfos", method = RequestMethod.POST)
	public Object addModuleInfo(@RequestBody Module module) throws MdmException {
		validate(module);
		module.setId(DataUtils.uuid());
		module.setCreatedOn(new Date());
		module.setCreatedBy(HttpContent.getOperatorId());
		module.setPath(getPath(module));
		module.setModuleLevel(getLevel(module));
		// 如果父节点处于停用状态，新增子节点状态也为停用
		if (module.getParent().getModuleState() != null
				&& module.getParent().getModuleState().byteValue() == GlobalConstants.STATUS_STOP) {
			module.setModuleState(GlobalConstants.STATUS_STOP.intValue());
		}
		return this.getResponseResult(service.insert(module));
	}

	@ApiOperation(value = "更新菜单信息")
	@RequestMapping(value = "/moduleInfos/{id}", method = RequestMethod.PUT)
	public Object updateModuleInfo(@PathVariable String id, @RequestBody Module module) throws MdmException {
		validate(module);
		Module originalModule = service.getById(id);
		module.setId(id);
		module.setModifiedOn(new Date());
		module.setModifiedBy(HttpContent.getOperatorId());
		module.setPath(StringUtils.equals(module.getParentId(), originalModule.getParentId()) ? originalModule.getPath()
				: getPath(module));
		module.setModuleLevel(StringUtils.equals(module.getParentId(), originalModule.getParentId())
				? originalModule.getModuleLevel() : getLevel(module));
		return this.getResponseResult(service.update(module, originalModule));
	}

	@ApiOperation(value = "删除菜单信息")
	@RequestMapping(value = "/moduleInfos/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Object deleteModuleInfo(@PathVariable String id, @RequestParam(value = "path") String path) {
		Module module = new Module();
		module.setPath(path);
		module.setId(id);
		module.setModifiedBy(HttpContent.getOperatorId());
		module.setModifiedOn(new Date());
		module.setIsDeleted(GlobalConstants.IS_DELETED);
		int result = service.deleteByPath(module);
		// 删除角色菜单关联表
		RoleModule rm = new RoleModule();
		rm.setModuleId(id);
		rm.setIsDeleted(GlobalConstants.IS_DELETED);
		rm.setModifiedBy(HttpContent.getOperatorId());
		rm.setModifiedOn(new Date());
		result += rmService.deleteByModuleId(rm);
		// 删除菜单下所有的功能
		Operation o = new Operation();
		o.setModuleId(id);
		o.setIsDeleted(GlobalConstants.IS_DELETED);
		o.setModifiedBy(HttpContent.getOperatorId());
		o.setModifiedOn(new Date());
		result += oService.deleteByModuleId(o);
		// 删除角色操作关联表
		RoleOperation ro = new RoleOperation();
		ro.setActionId(id);
		ro.setIsDeleted(GlobalConstants.IS_DELETED);
		ro.setModifiedBy(HttpContent.getOperatorId());
		ro.setModifiedOn(new Date());
		result += roService.deleteByActionId(ro);
		return this.getResponseResult(result);
	}

	private String getPath(Module module) {
		return StringUtils.isBlank(module.getParentId()) ? module.getId()
				: module.getParent().getPath() + "/" + module.getId();
	}

	private int getLevel(Module module) {
		return StringUtils.isBlank(module.getParentId()) ? 1 : module.getParent().getModuleLevel() + 1;
	}

}

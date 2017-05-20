package com.mdm.controller.web.operation;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.mdm.pojo.Operation;
import com.mdm.pojo.RoleOperation;
import com.mdm.service.operation.OperationService;
import com.mdm.service.roleoperation.RoleOperationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("操作管理")
@RestController
@RequestMapping("/operation")
public class OperationController extends BaseController {

	@Autowired
	private OperationService operationService;

	@Autowired
	private RoleOperationService roleOperationService;

	@ApiOperation(value = "查询操作信息")
	@RequestMapping(value = "/operationInfos", method = RequestMethod.GET)
	public Object queryModuleInfoList(@RequestParam(value = "actionName", required = false) String actionName,
			@RequestParam(value = "keyCode", required = false) String keyCode,
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "httpMethod", required = false) String httpMethod,
			@RequestParam(value = "moduleInfo", required = false) String moduleInfo,
			@RequestParam(value = "pageNum", required = false) int pageNum,
			@RequestParam(value = "pageSize", required = false) int pageSize) {
		Map<String, Object> map = new HashMap<>();
		map.put("actionName", actionName);
		map.put("keyCode", keyCode);
		map.put("path", path);
		map.put("httpMethod", httpMethod);
		map.put("moduleName", moduleInfo);
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		return this.getResponseResult(operationService.queryPageList(map, pageNum, pageSize));
	}
	
	
	@ApiOperation(value = "查询菜单列表")
	@RequestMapping(value = "/queryModuleList", method = RequestMethod.GET)
	public Object queryModuleInfoList() {
		return this.getResponseResult(operationService.queryModuleList());
	}
	
	private void validate(Operation operation) throws MdmException {
	
			if (operation.getActionName().length() > 50) {
				throw new MdmException("名称不能超过50个字符");
			}
			if (operation.getKeyCode().length() > 32) {
				throw new MdmException("KeyCode不能超过32个字符");
			}
			if (operation.getPath().length() > 128) {
				throw new MdmException("路径不能超过128个字符");
			}
		
	}

	@ApiOperation(value = "新增操作信息")
	@RequestMapping(value = "/operationInfos", method = RequestMethod.POST)
	public Object addOperationInfo(@RequestBody Operation operation) throws MdmException {
		validate(operation);
		operation.setId(DataUtils.uuid());
		operation.setCreatedOn(new Date());
		operation.setCreatedBy(HttpContent.getOperatorId());
		operation.setIsDeleted(GlobalConstants.IS_NOT_DELETED);
		return this.getResponseResult(operationService.insert(operation));
	}

	@ApiOperation(value = "更新操作信息")
	@RequestMapping(value = "/operationInfos/{id}", method = RequestMethod.PUT)
	public Object updateOperationInfo(@PathVariable String id, @RequestBody Operation operation) throws MdmException {
		validate(operation);
		operation.setModifiedOn(new Date());
		operation.setModifiedBy(HttpContent.getOperatorId());
		return this.getResponseResult(operationService.update(operation));
	}

	@ApiOperation(value = "删除操作信息")
	@RequestMapping(value = "/operationInfos/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Object deleteOperationInfo(@PathVariable String id) {
		Operation operation = new Operation();
		operation.setId(id);
		operation.setModifiedBy(HttpContent.getOperatorId());
		operation.setModifiedOn(new Date());
		operation.setIsDeleted(GlobalConstants.IS_DELETED);
		int result = operationService.deleteById(operation);
		// 删除角色操作关联表
		RoleOperation ro = new RoleOperation();
		ro.setOperationId(id);
		ro.setIsDeleted(GlobalConstants.IS_DELETED);
		ro.setModifiedBy(HttpContent.getOperatorId());
		ro.setModifiedOn(new Date());
		result += roleOperationService.deleteByOperationId(ro);
		return this.getResponseResult(result);
	}

}

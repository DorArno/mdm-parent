package com.mdm.controller.web.module;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.tree.TreeBuilder;
import com.mdm.core.tree.TreeNode;
import com.mdm.core.tree.TreeNodeConverter;
import com.mdm.core.tree.TreeUtil;
import com.mdm.pojo.Module;
import com.mdm.pojo.Operation;
import com.mdm.pojo.RoleModule;
import com.mdm.pojo.RoleOperation;
import com.mdm.service.module.ModuleService;
import com.mdm.service.rolemodule.RoleModuleService;
import com.mdm.service.roleoperation.RoleOperationService;

@Component
public class ModuleOperationTreeBuilder extends TreeBuilder {

	public final static String NODE_TYPE_MODULE = "M";

	public final static String NODE_TYPE_OPERATION = "O";

	@Autowired
	private ModuleService service;

	@Autowired
	private RoleModuleService rmService;

	@Autowired
	private RoleOperationService roService;

	@Override
	public List<TreeNode> buildTree(HttpServletRequest request) {
		List<RoleModule> rmList = rmService.getByRoleId(parameter, GlobalConstants.IS_NOT_DELETED);
		List<RoleOperation> roList = roService.getByRoleId(parameter, GlobalConstants.IS_NOT_DELETED);
		List<Module> list = service.queryModuleOperationList(GlobalConstants.IS_NOT_DELETED);
		return TreeUtil.buildTree(list, null, new TreeNodeConverter<Module>() {
			public TreeNode convert(Module module) {
				TreeNode node = new TreeNode();
				node.setId(module.getId());
				node.setName(module.getModuleName());
				node.setParentId(module.getParentId());
				node.setPath(module.getPath());
				node.addInfo("type", NODE_TYPE_MODULE);
				node.setChecked(existsModule(rmList, module.getId()));
				if (CollectionUtils.isNotEmpty(module.getOperationList())) {
					for (Operation operation : module.getOperationList()) {
						TreeNode child = new TreeNode();
						child.setId(operation.getId());
						child.setName(operation.getActionName());
						child.setParentId(module.getId());
						child.setChecked(existsOperation(roList, operation.getId()));
						child.addInfo("type", NODE_TYPE_OPERATION);
						node.addChild(child);
					}
				}
				return node;
			}
		});
	}

	private boolean existsModule(List<RoleModule> list, String moduleId) {
		return CollectionUtils.exists(list, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				RoleModule rm = (RoleModule) object;
				return StringUtils.equals(rm.getModuleId(), moduleId);
			}
		});
	}

	private boolean existsOperation(List<RoleOperation> list, String operationId) {
		return CollectionUtils.exists(list, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				RoleOperation ro = (RoleOperation) object;
				return StringUtils.equals(ro.getOperationId(), operationId);
			}
		});
	}

}

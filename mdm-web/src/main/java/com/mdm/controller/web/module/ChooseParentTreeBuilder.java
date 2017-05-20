package com.mdm.controller.web.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.tree.TreeBuilder;
import com.mdm.core.tree.TreeNode;
import com.mdm.core.tree.TreeNodeConverter;
import com.mdm.core.tree.TreeUtil;
import com.mdm.pojo.Module;
import com.mdm.service.module.ModuleService;

@Component
public class ChooseParentTreeBuilder extends TreeBuilder {

	@Autowired
	private ModuleService service;

	@Override
	public List<TreeNode> buildTree(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		List<Module> list = service.queryAll(map);
		return TreeUtil.buildTree(list, null, new TreeNodeConverter<Module>() {
			public TreeNode convert(Module module) {
				TreeNode node = new TreeNode();
				node.setId(module.getId());
				node.setName(module.getModuleName());
				node.setParentId(module.getParentId());
				node.setPath(module.getPath());
				node.addInfo("state", module.getModuleState());
				node.addInfo("level", module.getModuleLevel());
				return node;
			}
		});
	}

}

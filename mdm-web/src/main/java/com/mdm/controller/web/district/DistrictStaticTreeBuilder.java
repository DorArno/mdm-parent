package com.mdm.controller.web.district;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mdm.core.tree.TreeBuilder;
import com.mdm.core.tree.TreeNode;
import com.mdm.core.tree.TreeNodeConverter;
import com.mdm.core.tree.TreeUtil;
import com.mdm.pojo.District;
import com.mdm.service.district.DistrictService;

@Component
public class DistrictStaticTreeBuilder extends TreeBuilder {
	@Autowired
	DistrictService invokeDistrictService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<TreeNode> buildTree(HttpServletRequest request) {
		if ("district".equals(request.getParameter("treeType"))) {
			List<TreeNode> list = getNodeList(request);
			if ("0".equals(request.getParameter("parentId"))) {
				return list;
			}else{
				return TreeUtil.buildTree(list, request.getParameter("parentId"), new TreeNodeConverter() {
					public TreeNode convert(Object obj) {
						return (TreeNode) obj;
					}
				});
			}
				
		}else{
			List<TreeNode> list = getList();
			return TreeUtil.buildTree(list, "0", new TreeNodeConverter() {
				public TreeNode convert(Object obj) {
					return (TreeNode) obj;
				}
			});
		}
	}
	private List<TreeNode> getList() {
		List<TreeNode> nodelist = new ArrayList<>();
		List<District> list = invokeDistrictService.queryList();
		createTreeNode(null, "所有地区", "0", nodelist);
		for (District district : list) {
			createTreeNode(district.getId(), district.getName(), district.getParentId(), nodelist);
		}
		return nodelist;
	}
	private void createTreeNode(String id, String name, String parentId, List<TreeNode> list) {
		TreeNode node = new TreeNode();
		node.setId(id);
		node.setName(name);
		node.setParentId(parentId);
		list.add(node);
	}
	
	private List<TreeNode> getNodeList(HttpServletRequest request) {
		List<TreeNode> nodelist = new ArrayList<>();
		List<District> list = invokeDistrictService.queryNodeList(request);
		if ("0".equals(request.getParameter("parentId"))) {
			createTreeNode("0", "所有地区", null, nodelist);
			//	以所有地区作为父级
			List<TreeNode> childrenNodes = new ArrayList<>();
			for (District district : list) {
				createTreeNode(district.getId(), district.getName(), district.getParentId(), childrenNodes);
			}
			nodelist.get(0).setChildren(childrenNodes);
			nodelist.get(0).setOpen(true);
		}else{
			for (District district : list) {
				createTreeNode(district.getId(), district.getName(), district.getParentId(), nodelist);
			}
		}
		return nodelist;
	}
}

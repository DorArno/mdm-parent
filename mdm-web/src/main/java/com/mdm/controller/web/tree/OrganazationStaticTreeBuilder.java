/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月10日 上午9:48:34 *
**/ 
package com.mdm.controller.web.tree;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.util.StringUtil;
import com.mdm.core.tree.TreeBuilder;
import com.mdm.core.tree.TreeNode;
import com.mdm.core.tree.TreeNodeConverter;
import com.mdm.core.tree.TreeUtil;
import com.mdm.dao.write.organazation.OrganazationWriteMapper;
import com.mdm.pojo.Organazation;

@Component
public class OrganazationStaticTreeBuilder extends TreeBuilder  {

	
	@Autowired
	private OrganazationWriteMapper organazationWriteMapper;
	
	@Override
	public List<TreeNode> buildTree(HttpServletRequest request) {
		List<Organazation> organazations = organazationWriteMapper.queryOrganazationListForTree(parameter, corpCode, 1);
		List<TreeNode> list  = getTreeNodes(organazations);
		return TreeUtil.buildTree(list,  null, new TreeNodeConverter() {
			public TreeNode convert(Object obj) {
				return (TreeNode) obj;
			}
		});
	}

	private List<TreeNode> getTreeNodes(List<Organazation> organazations) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		for (Organazation organazation : organazations) {
			TreeNode treeNode = new TreeNode();
			if(StringUtil.isEmpty(organazation.getParentId())){
				treeNode.setOpen(true);
			}
			treeNode.setId(organazation.getId());
			treeNode.setParentId(organazation.getParentId());
			treeNode.setName(organazation.getName().concat("【").concat(organazation.getCode()).concat("】"));
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

}

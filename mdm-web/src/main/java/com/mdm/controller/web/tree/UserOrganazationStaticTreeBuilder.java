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
import com.mdm.dao.write.organazation.OrganazationWriteMapper;
import com.mdm.pojo.Organazation;
import com.mdm.service.organazation.OrganazationService;
import com.mdm.core.tree.TreeBuilder;
import com.mdm.core.tree.TreeNode;
import com.mdm.core.tree.TreeNodeConverter;
import com.mdm.core.tree.TreeUtil;

@Component
public class UserOrganazationStaticTreeBuilder extends TreeBuilder  {

	
	@Autowired
	private OrganazationWriteMapper organazationWriteMapper;
	
	@Override
	public List<TreeNode> buildTree(HttpServletRequest request) {
		String[] params = parameter.split(";");
		String orgType = params[0];
		String parentid = StringUtil.isEmpty(params[1]) ? null : params[1];
		int loadLevel = Integer.parseInt(params[2]);
		List<Organazation> organazations = null;
		if(parentid == null){
			organazations = organazationWriteMapper.queryOrganazationRoot(orgType,1);
		}else{
			organazations = organazationWriteMapper.queryOrganazationByParentid(parentid,1);
		}
		List<Organazation> allOrgs  =new ArrayList<>();
		if(loadLevel ==2){
			organazations.forEach(organazation -> {
				List<Organazation> orgs = organazationWriteMapper.queryOrganazationByParentid(organazation.getId(),1);
				allOrgs.addAll(orgs);
			});
		}
		allOrgs.addAll(organazations);
		return TreeUtil.buildTree(allOrgs,  parentid, new TreeNodeConverter<Organazation>() {
			public TreeNode convert(Organazation obj) {
				return (TreeNode) getTreeNode(obj);
			}
		});
	}
	
	private TreeNode getTreeNode(Organazation organazation){
		TreeNode treeNode = new TreeNode();
		if(StringUtil.isEmpty(organazation.getParentId())){
			treeNode.setOpen(true);
		}
		treeNode.setId(organazation.getId());
		treeNode.setParentId(organazation.getParentId());
		treeNode.setName(organazation.getName());
		return treeNode;
	}

}

package com.mdm.controller.web.tree;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.MyApplicationContextUtil;
import com.mdm.controller.BaseController;
import com.mdm.core.tree.TreeBuilder;

@Controller
@RequestMapping("/tree")
public class TreeController extends BaseController {
	@RequestMapping("static")
	public ModelAndView renderStaticTree(HttpServletRequest request,
			@RequestParam(value = "builder", required = true) String builder,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "loadChecked", required = false) String loadChecked,
			@RequestParam(value = "checkable", required = false) boolean checkable,
			@RequestParam(value = "radio", required = false) boolean radio,
			@RequestParam(value = "ztreecallback", required = false) String ztreecallback,
			@RequestParam(value = "needroot", required = false) boolean needroot,
			@RequestParam(value = "parameter", required = false) String parameter,
			@RequestParam(value = "corpCode", required = false) String corpCode)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		StringBuilder treeBuilder = new StringBuilder();
		if (id == null)
			treeBuilder.append("<ul id=\"ztree\" class=\"tree\"></ul>");
		else {
			treeBuilder.append("<ul id=\"");
			treeBuilder.append(id);
			treeBuilder.append("\" class=\"tree\"></ul>");
		}
		treeBuilder.append("<script>");
		treeBuilder.append("var setting = {expandSpeed:''");
		if (checkable) {
			treeBuilder.append(",checkable:true,checkType : { \"Y\": \"\", \"N\": \"\" }");
		}
		if (radio) {
			treeBuilder.append(",checkStyle:'radio'");
			treeBuilder.append(",checkRadioType:'").append("all").append("'");
		}
		if (ztreecallback == null) {
			treeBuilder.append(",callback:{click:").append("onSelect").append("}");
		} else {
			treeBuilder.append(",callback:").append(ztreecallback);
		}
		treeBuilder.append("};");
		TreeBuilder tb = MyApplicationContextUtil.getBean(builder, TreeBuilder.class);
		tb.setNeedroot(needroot);
		tb.setParameter(parameter);
		tb.setCorpCode(corpCode);
		String content = tb.printScript(request);
		treeBuilder.append("var data = ").append(content).append(" ;");
		if (id == null)
			treeBuilder.append("tree = $('#ztree').zTree(setting, data);");
		else {
			treeBuilder.append(id).append(" = $('#");
			treeBuilder.append(id);
			treeBuilder.append("').zTree(setting, data);");
		}
		if (loadChecked != null)
			treeBuilder.append(loadChecked);
		treeBuilder.append("</script>");
		return new ModelAndView(new MappingJackson2JsonView(), this.getSuccessResult(treeBuilder.toString()));
	}
	
	@RequestMapping("dynamicRoot")
	public ModelAndView renderDynamicRootTree(HttpServletRequest request,
			@RequestParam(value = "builder", required = true) String builder,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "loadChecked", required = false) String loadChecked,
			@RequestParam(value = "checkable", required = false) boolean checkable,
			@RequestParam(value = "radio", required = false) boolean radio,
			@RequestParam(value = "ztreecallback", required = false) String ztreecallback,
			@RequestParam(value = "needroot", required = false) boolean needroot,
			@RequestParam(value = "parameter", required = false) String parameter)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		StringBuilder treeBuilder = new StringBuilder();
		if (id == null)
			treeBuilder.append("<ul id=\"ztree\" class=\"tree\"></ul>");
		else {
			treeBuilder.append("<ul id=\"");
			treeBuilder.append(id);
			treeBuilder.append("\" class=\"tree\"></ul>");
		}
		treeBuilder.append("<script>");
		treeBuilder.append("var setting = {expandSpeed:''");
		if (checkable) {
			treeBuilder.append(",checkable:true,checkType : { \"Y\": \"\", \"N\": \"\" }");
		}
		if (radio) {
			treeBuilder.append(",checkStyle:'radio'");
			treeBuilder.append(",checkRadioType:'").append("all").append("'");
		}
		if (ztreecallback == null) {
			treeBuilder.append(",callback:{click:").append("onSelect").append("}");
			
		} else {
			treeBuilder.append(",callback:").append(ztreecallback);
		}
		treeBuilder.append("};");
		TreeBuilder tb = MyApplicationContextUtil.getBean(builder, TreeBuilder.class);
		tb.setNeedroot(needroot);
		tb.setParameter(parameter);
		String content = tb.printNodes(request);
		treeBuilder.append("var data = ").append(content).append(" ;");
		if (id == null)
			treeBuilder.append("tree = $('#ztree').zTree(setting, data);");
		else {
			treeBuilder.append(id).append(" = $('#");
			treeBuilder.append(id);
			treeBuilder.append("').zTree(setting, data);");
		}
		if (loadChecked != null)
			treeBuilder.append(loadChecked);
		treeBuilder.append("</script>");
		return new ModelAndView(new MappingJackson2JsonView(), this.getSuccessResult(treeBuilder.toString()));
	}
	
	@RequestMapping("dynamicNodes")
	public ModelAndView renderDynamicNodesTree(HttpServletRequest request,
			@RequestParam(value = "builder", required = true) String builder,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "loadChecked", required = false) String loadChecked,
			@RequestParam(value = "checkable", required = false) boolean checkable,
			@RequestParam(value = "radio", required = false) boolean radio,
			@RequestParam(value = "ztreecallback", required = false) String ztreecallback,
			@RequestParam(value = "needroot", required = false) boolean needroot,
			@RequestParam(value = "parameter", required = false) String parameter)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		TreeBuilder tb = MyApplicationContextUtil.getBean(builder, TreeBuilder.class);
		tb.setNeedroot(needroot);
		tb.setParameter(parameter);
		String content = tb.printNodes(request);
		
		return new ModelAndView(new MappingJackson2JsonView(), this.getSuccessResult(content));
	}
}

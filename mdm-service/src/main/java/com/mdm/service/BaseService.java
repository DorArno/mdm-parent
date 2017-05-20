package com.mdm.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mdm.common.PageResultBean;

public abstract class BaseService<T> {
	
	public PageResultBean queryPageList(Map<String, Object> params, int pageNum, int pageSize) {
		Page<T> page = PageHelper.startPage(pageNum, pageSize);
		PageResultBean pageResult = new PageResultBean();
		pageResult.setList(queryList(params));
		pageResult.setTotalCount(page.getTotal());
		return pageResult;
	}
	
	public abstract List<T> queryList(Map<String, Object> params);

}

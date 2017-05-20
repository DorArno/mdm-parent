/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年11月25日 上午10:49:38 *
**/ 
package com.mdm.controller.web.tenement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.controller.BaseController;
import com.mdm.core.bean.pojo.Tenement;
import com.mdm.core.service.tenement.TenementService;
import com.mdm.core.util.MdmException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tenement")
public class TenementController extends BaseController{

	@Autowired
	private TenementService tenementService;
	
	@ApiOperation(value = "企业代码", notes = "企业代码")
	@RequestMapping(method = RequestMethod.GET)
	public Object queryStaticDataList(@RequestParam(value = "corpCode", required = false) String corpCode) throws MdmException {
		return this.getResponseResult(tenementService.queryTenementList(corpCode));
	}
	
	@ApiOperation(value = "获取企业代码集合", notes = "获取企业代码集合")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Object queryStaticDatas(@RequestParam(value = "corpCode", required = false) String corpCode) throws MdmException {
		return this.getResponseResult(tenementService.queryTenements(corpCode));
	}
	
	@ApiOperation(value = "新增商家", notes = "新增商家")
	@RequestMapping(method = RequestMethod.POST)
	public Object insertTenement(@RequestBody Tenement tenement) throws MdmException{
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(tenementService.insertTenement(tenement)));
	}
	
}

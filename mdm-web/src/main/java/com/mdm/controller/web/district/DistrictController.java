/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DistrictController.java 
 * @Prject: mdm-web
 * @Package: com.mdm.controller.web.district 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年9月28日 上午10:09:38 
 * @version: V1.0   
 */
package com.mdm.controller.web.district;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mdm.controller.BaseController;
import com.mdm.core.util.ExcelUtil;
import com.mdm.core.util.MdmException;
import com.mdm.request.DistrictListRequest;
import com.mdm.request.DistrictRequest;
import com.mdm.service.district.DistrictRestfulService;
import com.mdm.service.district.DistrictService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: DistrictController
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年9月28日 上午10:09:38
 */
@RestController
@Api("行政区划")
@RequestMapping("/district")
public class DistrictController extends BaseController {
	@Autowired
	DistrictService invokeDistrictService;
	@Autowired
	DistrictRestfulService districtRestfulService;

	/**
	 * @Title: insert
	 * @Description: TODO
	 * @param districtRequest
	 * @return
	 * @throws MdmException
	 * @return: Object
	 */
	@ApiOperation(value = "新增行政区划", notes = "新增行政区划")
	@RequestMapping(method = RequestMethod.POST)
	public Object insert(@RequestBody DistrictRequest districtRequest) throws MdmException {
		return districtRestfulService.insert(districtRequest);
	}
	/**
	 * @Title: queryById
	 * @Description: TODO
	 * @param id
	 * @return
	 * @throws MdmException
	 * @return: Object
	 */
	@ApiOperation(value = "根据ID获取行政区划信息", notes = "根据ID获取行政区划信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Object queryById(@PathVariable String id) throws MdmException {
		return districtRestfulService.queryById(id);
	}
	/**
	 * @Title: queryByParentId
	 * @Description: TODO
	 * @param id
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws MdmException
	 * @return: Object
	 */
	@ApiOperation(value = "获取行政区划列表", notes = "获取行政区划列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Object queryByParentId(@RequestBody DistrictListRequest districtRequest) throws MdmException {
		return districtRestfulService.queryByParentId(districtRequest);
	}
	/**
	 * @Title: delete
	 * @Description: TODO
	 * @param id
	 * @return
	 * @throws MdmsException
	 * @return: Object
	 */
	@ApiOperation(value = "删除行政区划", notes = "删除行政区划")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Object delete(@PathVariable String id) throws MdmException {
		return districtRestfulService.delete(id);
	}
	/**
	 * @Title: update
	 * @Description: TODO
	 * @param districtRequest
	 * @return
	 * @throws MdmException
	 * @return: Object
	 */
	@ApiOperation(value = "更新行政区划", notes = "更新行政区划")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Object update(@PathVariable String id, @RequestBody DistrictRequest districtRequest) throws MdmException {
		return districtRestfulService.update(districtRequest);
	}
	@ApiOperation(value = "获取所有行政区划列表(ztree)", notes = "获取所有行政区划列表")
	@RequestMapping(value = "/treeList", method = RequestMethod.GET)
	public Object queryList() throws MdmException {
		return districtRestfulService.queryList();
	}
	@ApiOperation(value = "导入行政区划")
	@RequestMapping(value = "/importDistrict", method = RequestMethod.POST)
	public Object importDistrict(@RequestParam(value = "file", required = true) MultipartFile file)
			throws MdmException {
		return this.getResponseResult(invokeDistrictService.importDistrict(file));
	}
	@ApiOperation(value = "下载导入模板")
	@RequestMapping(value="/exportTemplete", method = RequestMethod.GET)
	public Object exportTemplete(HttpServletResponse response) throws MdmException {
		ExcelUtil.ExportExcelTemplate("*编码,上级行政区划编码,*名称,邮政编码,经度,纬度,描述".split(","),
				"D1234567,D001（省级为0或不填）,广东省,510000,23.08,113.14,示例描述".split(","),
				"行政区划模板", response);
		return this.getSuccessResult(1);
	}
	@ApiOperation(value = "导出行政区划")
	@RequestMapping(value="/export", method = RequestMethod.GET)
	public Object export(HttpServletResponse response, DistrictListRequest districtRequest) throws MdmException {
		return this.getResponseResult(invokeDistrictService.export(response, districtRequest));
	}
	
	@ApiOperation(value = "验证编码", notes = "验证编码")
	@RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
	public Object verifyCode(@RequestBody DistrictRequest districtRequest) throws MdmException {
		System.out.println(districtRestfulService.verifyCode(districtRequest));
		return districtRestfulService.verifyCode(districtRequest);
	}
	
	@ApiOperation(value = "获取行政区划树", notes = "获取行政区划树")
	@RequestMapping(value = "/queryDistrictForTree", method = RequestMethod.POST)
	public Object queryDistrictForTree(@RequestParam(value = "parentId", required = true) String parentId) throws MdmException {
		return districtRestfulService.queryDistrictForTree(parentId);
	}
	
	@ApiOperation(value = "获取行政区划（下拉级联）", notes = "获取行政区划（下拉级联）")
	@RequestMapping(value = "/districtForSelect", method = RequestMethod.POST)
	public Object queryDistrictForSelect(@RequestBody DistrictListRequest districtRequest) throws MdmException {
		return districtRestfulService.queryDistrictForSelect(districtRequest);
	}
}

package com.mdm.controller.web.organazation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.controller.BaseController;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.util.BeanUtils;
import com.mdm.core.util.ExcelUtil;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.Organazation;
import com.mdm.request.OrgUpdateBatchRequest;
import com.mdm.request.OrgUpdateBatchRequest.OrgUpdateBatchRequestFlag;
import com.mdm.request.OrganazationRequest;
import com.mdm.request.PointAccountRequest;
import com.mdm.response.CheckOrganizationCommonData;
import com.mdm.response.CommonPojoResponse;
import com.mdm.service.dataextend.DataExtendService;
import com.mdm.service.organazation.OrganazationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "organazation", description = "组织机构", position = 99)
@RestController
@RequestMapping("/organazation")
public class OrganazationController extends BaseController {

	@Autowired
	private OrganazationService organazationService;

	@Autowired
	private DataExtendService dataExtendService;

	@ApiOperation(value = "查询组织机构列表", notes = "根据条件查询组织机构信息", position = 1)
	@RequestMapping(method = RequestMethod.GET)
	public Object queryOrganazationList(@RequestParam(value = "orgType", required = true) String orgType, @RequestParam(value = "corpCode", required = false) String corpCode, @RequestParam(value = "sourceid", required = false) String sourceid, @RequestParam(value = "beginDate", required = false) String beginDate, @RequestParam(value = "endDate", required = false) String endDate, @RequestParam(value = "orgCode", required = false) String orgCode, @RequestParam(value = "orgName", required = false) String orgName, @RequestParam(value = "parentId", required = false) String parentId, @RequestParam(value = "pageNum", required = true) int pageNum, @RequestParam(value = "pageSize", required = true) int pageSize) throws MdmException {
		return this.getResponseResult(organazationService.queryOrganazationList(orgType, corpCode, sourceid, beginDate, endDate, orgCode, orgName, parentId, pageNum, pageSize));
	}
	
	

	@ApiOperation(value = "生成组织机构随机数")
	@RequestMapping(value = "/createRandomCode", method = RequestMethod.GET)
	public Object createRandomCode() {
		return this.getResponseResult(organazationService.createRandomCode());
	}

	@ApiOperation(value = "根据组织机构ID获取组织机构信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Object queryById(@PathVariable String id) {
		return this.getResponseResult(organazationService.queryById(id));
	}

	/**
	 * @Title: delete
	 * @Description: TODO
	 * @param id
	 * @return
	 * @throws MdmsException
	 * @return: Object
	 */
	@ApiOperation(value = "删除组织机构", notes = "删除组织机构")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable String id) throws MdmException {
		CommonPojoResponse result = organazationService.orgTreeDeleteAllById(id);
		if (result == null) {
			return new ModelAndView(new MappingJackson2JsonView(), this.getFailResult("删除失败"));
		} else {
			return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult(result));
		}
	}

	@ApiOperation(value = "创建组织机构")
	@RequestMapping(method = RequestMethod.POST)
	public Object insert(@RequestBody @Valid OrganazationRequest organazationRequest) throws MdmException {
		CheckOrganizationCommonData result = organazationService.checkParams(organazationRequest, null);
		if (result.isCheckResult()) {
			Organazation organazation = new Organazation();
			BeanUtils.copyProperties(organazationRequest, organazation);
			organazation.setSystemId(organazationRequest.getSystemId());
			return this.getResponseResult(organazationService.insert(organazation));
		} else {
			if(result.getCommonDataResponse() != null){
			    throw new MdmException(result.getCheckMsg(),  result.getCommonDataResponse());
			 }else{
				 throw new MdmException(result.getCheckMsg());
			 }
		}
	}

	@ApiOperation(value = "更新组织机构信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Object updateModuleInfo(@PathVariable String id, @RequestBody @Valid OrganazationRequest organazationRequest)  throws MdmException {
	    CheckOrganizationCommonData  result = organazationService.checkParams(organazationRequest, id);
		if (result.isCheckResult()) {
			Organazation organazation = new Organazation();
			BeanUtils.copyProperties(organazationRequest, organazation);
			organazation.setId(id);
			return this.getResponseResult(organazationService.update(organazation));
		} else {
			if(result.getCommonDataResponse() != null){
			    throw new MdmException(result.getCheckMsg(),  result.getCommonDataResponse());
			 }else{
				 throw new MdmException(result.getCheckMsg());
			 }
		}
	}

	@ApiOperation(value = "更新组织机构状态")
	@RequestMapping(value = "/batchUpdateOrgStatus", method = RequestMethod.PUT)
	public Object batchUpdateOrgStatus(@RequestBody OrgUpdateBatchRequest orgUpdateBatchRequest) throws MdmException {
		int result = 0;
		List<CommonPojoResponse> list = new ArrayList<CommonPojoResponse>();
		if (orgUpdateBatchRequest.getFlag() == OrgUpdateBatchRequestFlag.Open) {
			for (String id : orgUpdateBatchRequest.getIds()) {
				Organazation organazation = organazationService.queryById(id);
				if (organazation != null) {
					organazation.setStatus(GlobalConstants.STATUS_START.intValue());
					list.add(organazationService.update(organazation));
				}
			}
		} else if (orgUpdateBatchRequest.getFlag() == OrgUpdateBatchRequestFlag.Close) {
			for (String id : orgUpdateBatchRequest.getIds()) {
				Organazation organazation = organazationService.queryById(id);
				if (organazation != null) {
					list.add(organazationService.updateAllChild(id));
				}
			}
		} else if (orgUpdateBatchRequest.getFlag() == OrgUpdateBatchRequestFlag.Delete) {
			for (String id : orgUpdateBatchRequest.getIds()) {
				list.add(organazationService.orgTreeDeleteAllById(id));
			}
		}
		return this.getResponseResult(list);
	}
	
	@ApiOperation(value = "导入组织机构信息")
	@RequestMapping(value="/importOrganization", method = RequestMethod.POST)
	public Object inportOrganization(@RequestParam(value = "file", required = true) MultipartFile file) throws MdmException {
		return this.getResponseResult(organazationService.importOrganization(file));
	}
	
	@ApiOperation(value = "导出组织机构信息")
	@RequestMapping(value="/export", method = RequestMethod.GET)
	public Object export(HttpServletResponse response, 
						@RequestParam(value = "orgType", required = true) String orgType, 
						@RequestParam(value = "corpCode", required = false) String corpCode, 
						@RequestParam(value = "sourceid", required = false) String sourceid,
						@RequestParam(value = "beginDate", required = false) String beginDate, 
						@RequestParam(value = "endDate", required = false) String endDate, 
						@RequestParam(value = "orgCode", required = false) String orgCode, 
						@RequestParam(value = "orgName", required = false) String orgName, 
						@RequestParam(value = "parentId", required = false) String parentId) throws MdmException {
		return this.getResponseResult(organazationService.export(response, orgType, corpCode, sourceid, beginDate, endDate, orgCode, orgName, parentId));
	}
	
	@ApiOperation(value = "下载导入模板")
	@RequestMapping(value="/exportTemplete", method = RequestMethod.GET)
	public Object exportTemplete(HttpServletResponse response) throws MdmException {
		ExcelUtil.ExportExcelTemplate("父级编码,*编码,*类型,企业代码,*名称,*状态,联系电话,*省份,*省份Code,*城市,*城市Code,*区域,*区域Code,社区,详细地址,经度,纬度,描述,机构类型(物业云、收费云必填),来源系统".split(","),
							"O123（顶级为0或不填）,O12345,主数据平台,CCPG,示例名称,启用,13111111111,广东省,129936,深圳市,129936,福田区,143415,社区一,福田路18号,23.08,113.14,示例描述,管理处,主数据平台".split(","),
							"组织机构信息模板", response);
		/*ExcelUtil.ExportExcelTemplate("父级编码,*编码,*类型,企业代码,*名称,*状态,联系电话,*省份,*省份Code,*城市,*城市Code,*区域,*区域Code,社区,详细地址,经度,纬度,描述,来源系统".split(","),
				"O123（顶级为0或不填）,O12345,主数据平台,CCPG,示例名称,启用,13111111111,广东省,129936,深圳市,129936,福田区,143415,社区一,福田路18号,23.08,113.14,示例描述,主数据平台".split(","),
				"组织机构信息模板", response);*/
		return this.getSuccessResult(1);
	}
	
	@ApiOperation(value = "更新积分帐户", notes = "更新积分帐户")
	@RequestMapping(value="/UpdatePointAccount", method = RequestMethod.POST)
	public Object updatePointAccount(@RequestBody PointAccountRequest pointAccountRequest) throws MdmException{
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(organazationService.updatePointAccount(pointAccountRequest)));
	}
	
	
	@ApiOperation(value = "查询积分帐户", notes = "查询积分帐户")
	@RequestMapping(value="/QueryPointAccount", method = RequestMethod.POST)
	public Object queryPointAccount(@RequestBody PointAccountRequest pointAccountRequest) throws MdmException{
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(organazationService.queryPointAccount(pointAccountRequest)));
	}
	
	@ApiOperation(value = "通过积分账户查询组织", notes = "通过积分账户查询组织")
	@RequestMapping(value="/GetOrgIdByAccount", method = RequestMethod.POST)
	public Object getOrgIdByAccount(@RequestBody PointAccountRequest pointAccountRequest) throws MdmException{
		return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(organazationService.getOrgIdByAccount(pointAccountRequest)));
	}
}

package com.mdm.controller.web.merchant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * 商家管理控制
 * @author Zhaoy
 * @date 2016/10/24
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.controller.BaseController;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.util.BeanUtils;
import com.mdm.core.util.ExcelUtil;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.Merchant;
import com.mdm.pojo.Organazation;
import com.mdm.request.MerchantRequest;
import com.mdm.request.MerchantUpdateBatchRequest;
import com.mdm.request.MerchantUpdateBatchRequest.MerchantUpdateBatchRequestFlag;
import com.mdm.request.UpdateMerchantRequest;
import com.mdm.request.OrgUpdateBatchRequest.OrgUpdateBatchRequestFlag;
import com.mdm.response.CommonPojoResponse;
import com.mdm.service.merchant.MerchantService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/merchant")
public class MerchantController extends BaseController{
	@Autowired
	MerchantService merchanService ;
	@Autowired
	InvokeSystemService invokeSystemService ;
	/**
	 * 商家列表查询
	 * @param mName
	 * @param code
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws MdmException
	 */
    @RequestMapping(value="/merchantInfos",method=RequestMethod.GET)
    public ModelAndView queryMerchantList(@RequestParam(value = "mName", required = false) String mName,
    		@RequestParam(value = "code", required = false) String code,
    		@RequestParam(value = "mManager", required = false) String mManager,
    		@RequestParam(value = "contactTel", required = false) String contactTel,
    		@RequestParam(value = "firstCategoryId", required = false) String firstCategoryId,
    		@RequestParam(value = "status", required = false) String status,
    		@RequestParam(value = "systemId", required = false) String systemId,
    		@RequestParam(value = "pageNum", required = true) Integer pageNum,
    		@RequestParam(value = "pageSize", required = true) Integer pageSize) throws MdmException {
        return new ModelAndView(new MappingJackson2JsonView(),this.getResponseResult(merchanService.queryMerchantList(mName, code,pageNum,pageSize,mManager,contactTel,firstCategoryId,status, systemId)));
    }
    
    @RequestMapping(value="/merchantsForUser",method=RequestMethod.GET)
    public ModelAndView queryMerchantsForUser() throws MdmException {
    	return new ModelAndView(new MappingJackson2JsonView(),this.getResponseResult(merchanService.queryMerchantsForUser()));
    }
    
    @RequestMapping(value="/merchantCheck",method=RequestMethod.GET)
    public ModelAndView queryMerchantExist(@RequestParam(value = "mName", required = false) String mName,
    		@RequestParam(value = "code", required = false) String code,
    		@RequestParam(value = "pageNum", required = true) Integer pageNum,
    		@RequestParam(value = "pageSize", required = true) Integer pageSize) throws MdmException {
        return new ModelAndView(new MappingJackson2JsonView(),this.getResponseResult(merchanService.queryMerchantExist(mName,code,pageNum,pageSize)));
    }
    @RequestMapping(value="/merchantType",method=RequestMethod.GET)
    public ModelAndView queryMerchantType(@RequestParam(value = "parentId", required = false) String parentId) throws MdmException {
        return new ModelAndView(new MappingJackson2JsonView(),this.getResponseResult(merchanService.queryMerchantType(parentId)));
    }
    /**
     * 添加商家API
     * @param merchantRequest
     * @return
     * @throws MdmException
     */
    @RequestMapping(value="/addMerchant",method=RequestMethod.POST)
	public Object addMerchant(@RequestBody Merchant merchant) throws MdmException {
    	String systemId = invokeSystemService.querySystemId(merchant.getSystemId(), merchant.getSystemCode());
    	merchant.setSystemId(systemId);
        return this.getResponseResult(merchanService.addMerchant(merchant));
    }
    
    @RequestMapping(value="/updateMerchant/{id}",method=RequestMethod.PUT)
    public ModelAndView editMerchant(@PathVariable String id, @RequestBody UpdateMerchantRequest merchantRequest) throws MdmException {
    	merchantRequest.setId(id);
    	Merchant merchant = new Merchant();
    	String systemId = invokeSystemService.querySystemId(merchantRequest.getSystemId(), merchantRequest.getSystemCode());
    	merchant.setSystemId(systemId);
		BeanUtils.copyProperties(merchantRequest,merchant);
    	return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(merchanService.update(merchant)));
    }
    /**
     * 批量修改商家信息
     * @param id
     * @param merchantRequest
     * @return
     * @throws MdmException
     */
    @RequestMapping(value="/batchUpdateMerchant",method=RequestMethod.PUT)
    public Object batchUpdateMerchant( @RequestBody MerchantUpdateBatchRequest merchantUpdateBatchRequest) throws MdmException {
    	int result = 0;
		List<CommonPojoResponse> list = new ArrayList<CommonPojoResponse>();
		if (merchantUpdateBatchRequest.getFlag() == MerchantUpdateBatchRequestFlag.Open) {
			for (String id : merchantUpdateBatchRequest.getIds()) {
				Merchant merchant = merchanService.getMerchantById(id);
				if (merchant != null) {
					merchant.setStatus(GlobalConstants.STATUS_START.intValue());
					list.add(merchanService.update(merchant));//启用
				}
			}
		} else if (merchantUpdateBatchRequest.getFlag() == MerchantUpdateBatchRequestFlag.Close) {
			for (String id : merchantUpdateBatchRequest.getIds()) {
				Merchant merchant = merchanService.getMerchantById(id);
				if (merchant != null) {
					merchant.setStatus(GlobalConstants.STATUS_STOP.intValue());
					list.add(merchanService.update(merchant));//停用
				}
			}
		} else if (merchantUpdateBatchRequest.getFlag() == MerchantUpdateBatchRequestFlag.Delete) {
			for (String id : merchantUpdateBatchRequest.getIds()) {
				list.add(merchanService.deleteAllById(id));//删除
			}
		}
		return this.getResponseResult(list);
    }
    @RequestMapping(value="/deleteMerchant/{id}",method=RequestMethod.DELETE)
    public ModelAndView deleteMerchant(@PathVariable String id) throws MdmException {
    	Merchant merchant = new Merchant();
    	merchant.setId(id);  
    	return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(merchanService.deleteMerchant(merchant)));
    }
    
    @RequestMapping(value="/getMerchantById/{id}",method=RequestMethod.GET)
    public ModelAndView getMerchantById(@PathVariable String id) throws MdmException {
    	return new ModelAndView(new MappingJackson2JsonView(),this.getResponseResult(merchanService.getMerchantById(id)));
    }
    
    @ApiOperation(value = "导入商家信息")
	@RequestMapping(value="/importMerchant", method=RequestMethod.POST)
	public Object inportOrganization(@RequestParam(value = "file", required = true) MultipartFile file) throws MdmException {
		return this.getResponseResult(merchanService.importMerchant(file));
	}
    
    @ApiOperation(value = "导出商家信息")
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public Object export(HttpServletResponse response, 
						@RequestParam(value = "mName", required = false) String mName,
			    		@RequestParam(value = "code", required = false) String code,
			    		@RequestParam(value = "mManager", required = false) String mManager,
			    		@RequestParam(value = "contactTel", required = false) String contactTel,
			    		@RequestParam(value = "firstCategoryId", required = false) String firstCategoryId,
			    		@RequestParam(value = "status", required = false) String status) throws MdmException {
		return this.getResponseResult(merchanService.export(response, mName, code, mManager,contactTel,firstCategoryId,status));
	}
	
	@ApiOperation(value = "下载导入模板")
	@RequestMapping(value="/exportTemplete", method=RequestMethod.GET)
	public Object exportTemplete(HttpServletResponse response) throws MdmException {
		//ExcelUtil.ExportExcelTemplate("商家代码,商家名称,负责人,商家电话,商家服务电话,商家状态,来源系统,所属组织,一级分类,二级分类,省份,城市,区域,是否夜场,精选商家,停车位,营业时间开始,营业时间结束,晚间营业时间开始,晚间营业时间结束,外部商家,商家地址,商家标题,商家描述".split(","), "商家信息模板", response);
		//必填字段添加*标识
		//ExcelUtil.ExportExcelTemplate("*商家代码,*商家名称,*负责人,商家电话,商家服务电话,*商家状态,*来源系统,*所属组织,*一级分类,*二级分类,省份,城市,区域,是否夜场,精选商家,停车位,营业时间开始,营业时间结束,外部商家,商家地址,商家标题,商家描述".split(","),
		//							"M1234567,示例商家名称,李先生,13111111111,0755-11111111,启用,社商云,所属组织编码,超市,小型超市,广东省,深圳市,福田区,否,否,120,9:00,18:00,否,福田路18号,示例商家标题,示例商家描述".split(","),
		//							"商家信息模板", response);
		ExcelUtil.ExportExcelTemplate("*商家编号,*商家名称,*负责人,商家电话,商家服务电话,*商家状态,*来源系统,*所属组织编码,*一级分类,*二级分类,省份,省份Code,城市,城市Code,区域,区域Code,停车位,营业时间开始(时间前加单引号'),营业时间结束(时间前加单引号'),外部商家(0:否；1:是),商家地址,商家标题,商家描述".split(","),
									"M1234567,示例商家名称,李先生,13111111111,0755-11111111,启用,社商云,ORG121964915126,超市,小型超市,广东省,129936,深圳市,129936,福田区,143415,120,9:00,18:00,0,福田路18号,示例商家标题,示例商家描述".split(","),
									"商家信息模板", response);
		return this.getSuccessResult(1);
	}
}

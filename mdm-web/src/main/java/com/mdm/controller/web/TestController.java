package com.mdm.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdm.TestService;
import com.mdm.controller.BaseController;
import com.mdm.core.bean.es.DistrictEsQuery;
import com.mdm.core.es.document.DistrictDocument;
import com.mdm.core.es.service.DistrictEsService;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.TestBean;
import com.mdm.service.initpath.InitPathService;
import com.mdm.service.organazation.OrganazationService;

import io.swagger.annotations.ApiOperation;

/**
* @author:gaoyb
* @version 创建时间：2016年9月13日 下午5:12:54
* 类说明
*/

@RestController
@RequestMapping("/")
public class TestController extends BaseController {

	@Autowired
    private TestService testService;
	
	@Autowired
	private OrganazationService organazationService;
	
	@Autowired
	private InitPathService initPathServiceImpl;
	
	@Autowired
	private DistrictEsService districtEsService;
	
    /**
     * 登录校验
     *
     * @param user
     * @throws MdmException
     * @author gaoyb
     */
	@ApiOperation(value="登陆校验", notes="根据输入的用户名密码进行校验")
    @RequestMapping(value="loginCheck",method=RequestMethod.POST)
    public Object loginCheck(@RequestParam String userName,@RequestParam String password) throws MdmException {
    	
        return this.getSuccessResult(1);//返回json结果报文
    }
	
	/**
	 * 
	 * @Title: addUser 
	 * @Description: TODO
	 * @param user
	 * @return
	 * @throws MdmException
	 * @return: Object
	 */
	@ApiOperation(value="新增用户", notes="用户管理-新增用户")
    @RequestMapping(value="addUser",method=RequestMethod.POST)
    public Object addUser(@RequestBody TestBean user) throws MdmException {
    	
        return this.getSuccessResult(testService.checkUserExist(user));//返回json结果报文
    }
	
	/**
	 * initPathServiceName in   organazationInitPathService ...
	 * @param initPathServiceName
	 * @return
	 * @throws MdmException
	 */
//	@ApiOperation(value="初始化Path", notes="父级Path初始化")
//    @RequestMapping(value="initOrgPath",method=RequestMethod.POST)
//    public Object initOrgPath(@RequestParam(value = "initPathServiceName", required = true) String initPathServiceName) throws MdmException {
//		InitPathService initPathService = MyApplicationContextUtil.getBean(initPathServiceName, InitPathService.class);
//        return this.getSuccessResult(initPathService.initPath());//
//    }
	
	/**
	 * initPathServiceName in   organazationInitPathService ...
	 * @param initPathServiceName
	 * @return
	 * @throws MdmException
	 */
	@ApiOperation(value="初始化Path", notes="父级Path初始化")
    @RequestMapping(value="initPath",method=RequestMethod.POST)
    public Object initPath(
    		@RequestParam(value = "tablename", required = true) String tablename,
    		@RequestParam(value = "parentIdColumnName", required = true) String parentIdColumnName,
    		@RequestParam(value = "pathColumnName", required = true) String pathColumnName) throws MdmException {
		boolean result = initPathServiceImpl.initPath(tablename, parentIdColumnName, pathColumnName);
        return this.getSuccessResult(result);//
    }
	
	@ApiOperation(value="保存es district", notes="保存es district")
    @RequestMapping(value="/es/addEsDistrict",method=RequestMethod.POST)
    public Object addEsDistrict(@RequestBody DistrictDocument district) throws MdmException {
    	
        return this.getSuccessResult(districtEsService.save(district));
    }
	
	@ApiOperation(value="查询es district", notes="根据id查询")
    @RequestMapping(value="/es/queryEsDistrict",method=RequestMethod.GET)
    public Object queryEsDistrict(@RequestParam(value = "id", required = true) String id) throws MdmException {
    	
        return this.getSuccessResult(districtEsService.findOne(id));
    }
	
	@ApiOperation(value="查询es district list", notes="查询es district list")
    @RequestMapping(value="/es/districtList",method=RequestMethod.POST)
    public Object queryEsDistrictList(@RequestBody DistrictEsQuery query) throws MdmException {
        return this.getSuccessResult(districtEsService.findDistrictList(query));
    }
}

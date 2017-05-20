/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MergeUser.java 
 * @Prject: mdm-web
 * @Package: com.mdm.controller.web.mergeuser 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月24日 下午1:49:49 
 * @version: V1.0   
 */
package com.mdm.controller.web.mergeuser;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdm.controller.BaseController;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.MergeUserInfo;
import com.mdm.service.mergeuser.MergeUserService;

import io.swagger.annotations.ApiOperation;

/** 
 * @ClassName: MergeUser 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月24日 下午1:49:49  
 */
@RestController
@RequestMapping("/mergeManager")
public class MergeUserController extends BaseController{
	
	@Autowired
	private MergeUserService mergeUserService;
	
	@ApiOperation(value="查询待合并用户信息", notes="查询待合并用户信息")
    @RequestMapping(value="/queryMergeUser",method=RequestMethod.GET)
    public Object queryQuartzTaskInfo(@RequestParam(value = "account", required = false) String account,
    		@RequestParam(value = "sex", required = false) String sex,
    		@RequestParam(value = "syscode", required = false) String syscode,
    		@RequestParam(value = "sysname", required = false) String sysname,
    		@RequestParam(value = "status", required = false) String status,
    		@RequestParam(value = "type", required = false) String type,
    		@RequestParam(value = "phone", required = false) String phone,
    		@RequestParam(value = "pageNum", required = true) int pageNum,
    		@RequestParam(value = "pageSize", required = true) int pageSize) throws MdmException {
    	Map<String, String> map = new HashMap<String,String>();
    	map.put("account", account);
    	map.put("sex", sex);
    	map.put("syscode", syscode);
    	map.put("sysname", sysname);
    	map.put("status", status);
    	map.put("type", type);
    	map.put("phone", phone);
        return this.getResponseResult(mergeUserService.queryMergeUserInfo(map,pageNum,pageSize));
    }
	
	
	@ApiOperation(value="合并用户", notes="合并用户")
    @RequestMapping(value="/mergeUser",method=RequestMethod.POST)
    public Object mergeUser(@RequestBody MergeUserInfo mergeuser) throws Exception {
    	
        return this.getResponseResult(mergeUserService.mergeUser(mergeuser));
    }
}

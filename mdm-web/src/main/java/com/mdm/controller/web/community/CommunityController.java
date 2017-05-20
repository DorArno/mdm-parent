package com.mdm.controller.web.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdm.controller.BaseController;
import com.mdm.core.util.MdmException;
import com.mdm.service.community.CommunityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "community", description="社区")
@RestController
@RequestMapping("/community")
public class CommunityController extends BaseController {
	
	@Autowired
	private CommunityService communityService;
	
	
	@ApiOperation(value="查询社区列表", notes="根据条件查询组织机构信息")
    @RequestMapping(method=RequestMethod.GET)
    public Object queryCommunityList(
    		@RequestParam(value = "areaId", required = true) String areaId
    		) throws MdmException {
        return this.getResponseResult(communityService.queryCommunityList(areaId));
    }
	 
	
}

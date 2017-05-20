/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DistrictRestfulService.java 
 * @Prject: mdm-service
 * @Package: com.mdm.service.district 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年10月31日 下午5:12:12 
 * @version: V1.0   
 */
package com.mdm.service.district;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mdm.common.RestfulJsonResultBean;
import com.mdm.core.bean.response.SystemInfoResponse;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.restful.RestUtil;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.util.HttpContent;
import com.mdm.request.DistrictListRequest;
import com.mdm.request.DistrictRequest;
import com.mdm.service.dataextend.DataExtendService;

/**
 * @ClassName: DistrictRestfulService
 * @Description: TODO
 * @author: gaod003
 * @date: 2016年10月31日 下午5:12:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DistrictRestfulService {
	@Value("${district.server}")
	private String districtSeverUrl;
	@Autowired
	InvokeSystemService invokeSystemService;
	@Autowired
	private DataExtendService dataExtendService;
	
	/**
	 * @Title: insert
	 * @Description: 新增行政区划
	 * @param district
	 * @return
	 * @return: String
	 */
	public String insert(DistrictRequest districtRequest) {
		districtRequest.setCreatedBy(HttpContent.getOperatorId());
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(),
				JSON.toJSONString(districtRequest));
		String resultStr =  RestUtil.sendData(districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL,
				RequestMethod.POST.toString(), JSON.toJSONString(districtRequest), header);
		
		//	扩展字段
		JSONObject jsonObj = JSONObject.parseObject(resultStr);
		if(jsonObj.getInteger("resCode") == 200){
			JSONObject dataObj = JSONObject.parseObject(jsonObj.getString("data"));
			districtRequest.setId(dataObj.getString("id"));
			
			dataExtendService.insertAllExts(districtRequest);
		}
		return resultStr;
	}
	/**
	 * @Title: queryById
	 * @Description: 根据ID获取行政区划信息
	 * @param id
	 * @return
	 * @return: String
	 */
	public String queryById(String id) {
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(), "");
		return RestUtil.sendData(districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL + "/" + id,
				RequestMethod.GET.toString(), null, header);
	}
	/**
	 * @Title: queryByParentId
	 * @Description: 获取行政区划列表
	 * @param parentId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @return: List<District>
	 */
	public String queryByParentId(DistrictListRequest districtRequest) {
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(),
				JSON.toJSONString(districtRequest));
		return RestUtil.sendData(districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL + "/list",
				RequestMethod.POST.toString(), JSON.toJSONString(districtRequest), header);
	}
	/**
	 * @Title: delete
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: int
	 */
	public String delete(String id) {
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(), "");
		return RestUtil.sendData(districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL + "/" + id,
				RequestMethod.DELETE.toString(), null, header);
	}
	/**
	 * @Title: update
	 * @Description: 更新行政区划
	 * @param district
	 * @return
	 * @return: int
	 */
	public String update(DistrictRequest districtRequest) {
		districtRequest.setCreatedBy(HttpContent.getOperatorId());
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(),
				JSON.toJSONString(districtRequest));
		String resultStr = RestUtil.sendData(
				districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL + "/" + districtRequest.getId(),
				RequestMethod.PUT.toString(), JSON.toJSONString(districtRequest), header);
		
		//	扩展字段
		JSONObject jsonObj = JSONObject.parseObject(resultStr);
		if(jsonObj.getInteger("resCode") == 200){
			dataExtendService.insertAllExts(districtRequest);
		}
		
		return resultStr;
	}
	/**
	 * @Title: update
	 * @Description: 更新行政区划(恢复历史数据)
	 * @param district
	 * @return
	 * @return: int
	 */
	public String recoverDistrictHistory(DistrictRequest districtRequest) {
		districtRequest.setCreatedBy(HttpContent.getOperatorId());
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(),
				JSON.toJSONString(districtRequest));
		String resultStr = RestUtil.sendData(
				districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL + "/recoverDistrict",
				RequestMethod.PUT.toString(), JSON.toJSONString(districtRequest), header);
		
		//	扩展字段
		JSONObject jsonObj = JSONObject.parseObject(resultStr);
		if(jsonObj.getInteger("resCode") == 200){
			dataExtendService.insertAllExts(districtRequest);
		}
		
		return resultStr;
	}
	/**
	 * @Title: queryByParentId
	 * @Description: 获取所有行政区划列表(ztree)
	 * @return
	 * @return: PageResultBean
	 */
	public String queryList() {
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(), "");
		return RestUtil.sendData(districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL + "/treeList",
				RequestMethod.GET.toString(), null, header);
	}
	
	/**
	 * 
	 * @Title: verifyCode 
	 * @Description: TODO
	 * @param districtRequest
	 * @return
	 * @return: String
	 */
	public String verifyCode(DistrictRequest districtRequest) {
		districtRequest.setCreatedBy(HttpContent.getOperatorId());
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(),
				JSON.toJSONString(districtRequest));
		return RestUtil.sendData(districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL + "/verifyCode",
				RequestMethod.POST.toString(), JSON.toJSONString(districtRequest), header);
	}
	
	/**
	 * 通过rest API获取所有行政区划列表
	 * 
	 * @return
	 */
	public List<com.mdm.pojo.District> getDistrictList() {
		//查询省市区
		DistrictListRequest districtListRequest = new DistrictListRequest();
		districtListRequest.setPageNum(0);
		districtListRequest.setPageSize(0);
		String jsonDistricts = queryByParentId(districtListRequest);
		RestfulJsonResultBean jsonObj = JSON.parseObject(jsonDistricts, RestfulJsonResultBean.class);
		if(jsonObj != null && jsonObj.getData() != null) {
			districtListRequest.setPageSize(jsonObj.getData().getTotalCount());
		}
		jsonDistricts = queryByParentId(districtListRequest);
		jsonObj = JSON.parseObject(jsonDistricts, RestfulJsonResultBean.class);
		List<com.mdm.pojo.District> districtList = null;
		if(jsonObj != null && jsonObj.getData() != null && jsonObj.getData().getList() != null) {
			districtList = (List<com.mdm.pojo.District>)jsonObj.getData().getList();
		}
		return districtList;
	}
	
	/**
	 * 获取所有行政区划Map，Key = Id, Value = Name
	 * 
	 * @return
	 */
	public Map<String, String> getDistrictMap() {
		List<com.mdm.pojo.District> districtList = getDistrictList();
		Map<String, String> districtMap = new HashMap<>();
		if(districtList != null && !districtList.isEmpty()) {
			for(com.mdm.pojo.District d : districtList) {
				if(!districtMap.containsKey(d.getId())) {
					districtMap.put(d.getId(), d.getName());
					//logger.info(d.getId() + ", " + d.getName());
				}
			}
		}
		return districtMap;
	}
	
	/**
	 * 获取行政区划树
	 * @Title: queryDistrictForTree 
	 * @Description: TODO
	 * @return
	 * @return: String
	 */
	public String queryDistrictForTree(String parentId) {
		DistrictRequest districtRequest =new DistrictRequest();
		districtRequest.setParentId(parentId);
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(),
				JSON.toJSONString(districtRequest));
		return RestUtil.sendData(districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL + "/queryDistrictForTree",
				RequestMethod.POST.toString(), JSON.toJSONString(districtRequest), header);
	}
	
	/**
	 * 查询
	 * @Title: queryDistrictForSelect 
	 * @Description: TODO
	 * @param districtRequest
	 * @return
	 * @return: String
	 */
	public String queryDistrictForSelect(DistrictListRequest districtRequest) {
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(MdmConstants.MDM);
		Map<String, String> header = RestUtil.setRequestHeader(MdmConstants.MDM, systemInfo.getAuthCode(),
				JSON.toJSONString(districtRequest));
		return RestUtil.sendData(districtSeverUrl + MdmConstants.DISTRICT_REQUEST_MAPPING_URL + "/districtForSelect",
				RequestMethod.POST.toString(), JSON.toJSONString(districtRequest), header);
	}
}

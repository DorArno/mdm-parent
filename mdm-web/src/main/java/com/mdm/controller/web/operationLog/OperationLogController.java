package com.mdm.controller.web.operationLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.mdm.controller.BaseController;
import com.mdm.core.bean.pojo.OperationLog;
import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.service.operationlog.OperationLogService;
import com.mdm.core.util.BeanUtils;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;
import com.mdm.extend.pojo.DataExtend;
import com.mdm.extend.pojo.DataExtendRelevance;
import com.mdm.extend.request.DataExtendRequest;
import com.mdm.pojo.Merchant;
import com.mdm.pojo.Organazation;
import com.mdm.pojo.Role;
import com.mdm.request.DistrictRequest;
import com.mdm.request.UserBasicsInfoRequest;
import com.mdm.request.UserDetailInfoRequest;
import com.mdm.request.UserInfoSFRequest;
import com.mdm.response.UserInfoMqResponse;
import com.mdm.response.UserInfoResponse;
import com.mdm.response.UserOrganizationRoleMqResponse;
import com.mdm.service.dataextend.DataExtendService;
import com.mdm.service.district.DistrictRestfulService;
import com.mdm.service.merchant.MerchantService;
import com.mdm.service.organazation.OrganazationService;
import com.mdm.service.role.RoleService;
import com.mdm.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("操作日志管理")
@RestController
@RequestMapping("/operationLog")
public class OperationLogController extends BaseController {
	@Autowired
	private OperationLogService operationLogService;
	@Autowired
	private DataExtendService dataExtendService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DistrictRestfulService districtService;
	@Autowired
	private OrganazationService organazationService;
	@Autowired
	private MerchantService merchantService;

	@ApiOperation(value = "查询操作日志信息")
	@RequestMapping(value = "/operationLogs", method = RequestMethod.GET)
	public Object queryBusinessLogInfoList(@RequestParam(value = "dataId", required = false) String dataId,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "dataType", required = false) String dataType,
			@RequestParam(value = "createdBy", required = false) String createdBy,
			@RequestParam(value = "beginDate", required = false) String beginDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageNum", required = false) int pageNum,
			@RequestParam(value = "pageSize", required = false) int pageSize) throws MdmException {
		Map<String, Object> map = new HashMap<>();
		map.put("content", content);
		map.put("createdBy", createdBy);
		map.put("dataType", dataType);
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("dataId", dataId);
		// map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		return this.getResponseResult(operationLogService.queryPageList(map, pageNum, pageSize));
	}
	@ApiOperation(value = "恢复历史版本数据")
	@RequestMapping(value = "/recoverHistory/{id}", method = RequestMethod.POST)
	@Transactional
	public Object recoverHistory(@PathVariable String id, @RequestBody OperationLog log) throws MdmException {
		int result = 0;
		// 恢复主数据
		OperationLog operationLog = operationLogService.getById(id);
		if (operationLog.getDataType() == 1) {
			
			UserInfoMqResponse userInfoMqResponse = JSON.parseObject(operationLog.getContent(), UserInfoMqResponse.class);
			UserInfoSFRequest userInfoRequest = new UserInfoSFRequest();
			UserBasicsInfoRequest userBasicsInfoRequest = new UserBasicsInfoRequest();
			UserDetailInfoRequest userDetailInfoRequest = new UserDetailInfoRequest();
			
			if (null != userInfoMqResponse.getUserBasicsInfoRequest()) {
				BeanUtils.copyProperties(userInfoMqResponse.getUserBasicsInfoRequest(), userBasicsInfoRequest);
				BeanUtils.copyProperties(userInfoMqResponse.getUserDetailInfoRequest(), userDetailInfoRequest);
				userInfoRequest.setUserBasicsInfoRequest(userBasicsInfoRequest);
				userInfoRequest.setUserDetailInfoRequest(userDetailInfoRequest);
				userInfoRequest.setUserChannel(userInfoMqResponse.getUserChannel());
			}else{
				//	恢复操作日志中用户信息格式为UserBasicsInfo/UserDetailInfo 的数据
				UserInfoResponse userInfoResponse = JSON.parseObject(operationLog.getContent(), UserInfoResponse.class);
				BeanUtils.copyProperties(userInfoResponse.getUserBasicsInfo(), userBasicsInfoRequest);
				BeanUtils.copyProperties(userInfoResponse.getUserDetailInfo(), userDetailInfoRequest);
				userInfoRequest.setUserBasicsInfoRequest(userBasicsInfoRequest);
				userInfoRequest.setUserDetailInfoRequest(userDetailInfoRequest);
				userInfoRequest.setUserChannel(userInfoMqResponse.getUserChannel());
			}
			
			userService.updateUserForSF(userInfoRequest);
			result += recoverExts(operationLog, userInfoRequest.getUserBasicsInfoRequest().getExts());
		} else if (operationLog.getDataType() == 2) {
			Role origRole = roleService.getById(log.getDataId());
			Role minLevelRole = getMinLevelRoleByUserId();
			if (minLevelRole.getLevel() >= origRole.getLevel()) {
				throw new MdmException(String.format("当前用户最大角色级别为[%d]，不允许修改级别小于等于[%d]的角色", minLevelRole.getLevel(),
						minLevelRole.getLevel()));
			}
			Role role = JSON.parseObject(operationLog.getContent(), Role.class);
			if (minLevelRole.getLevel() >= role.getLevel()) {
				throw new MdmException(String.format("当前用户最大角色级别为[%d]，修改角色的级别必须大于[%d]", minLevelRole.getLevel(),
						minLevelRole.getLevel()));
			}
			role.setVersion(String.valueOf(System.currentTimeMillis()));
			result += roleService.update(role);
			result += recoverExts(operationLog, role.getExts());
		} else if (operationLog.getDataType() == 3) {
			DistrictRequest districtRequest = JSON.parseObject(operationLog.getContent(), DistrictRequest.class);
			districtService.recoverDistrictHistory(districtRequest);
			result += recoverExts(operationLog, districtRequest.getExts());
		} else if (operationLog.getDataType() == 4) {
			Organazation organazation = JSON.parseObject(operationLog.getContent(), Organazation.class);
			Organazation dborganazation = organazationService.queryById(organazation.getId());
			organazation.setOldVersion(dborganazation.getVersion());
			organazation.setVersion(System.currentTimeMillis() + "");
			organazationService.updateHistory(organazation);
			result += recoverExts(operationLog, organazation.getExts());
		} else if (operationLog.getDataType() == 5) {
			Merchant merchant = JSON.parseObject(operationLog.getContent(), Merchant.class);
			merchantService.updateMerchant(merchant);
			result += recoverExts(operationLog, merchant.getExts());
		}else if(operationLog.getDataType() == 6){
			//	用户-关联的组织角色
			UserOrganizationRoleMqResponse userOrganizationRoleMqResponse = JSON.parseObject(operationLog.getContent(), UserOrganizationRoleMqResponse.class);
			userService.setOrganizationRole(userOrganizationRoleMqResponse.getId(), userOrganizationRoleMqResponse.getCorpCode(), userOrganizationRoleMqResponse.getUserOrganizationRole());
		}
		return this.getResponseResult(result);
	}
	private int recoverExts(OperationLog operationLog, List<DataExtendRequest> extList) {
		int result = 0;
		// 删除当前扩展字段
		result += dataExtendService.deletById(operationLog.getDataId(), GlobalConstants.IS_DELETED,
				GlobalConstants.IS_NOT_DELETED);
		result += dataExtendService.deteleByDataid(operationLog.getDataId());
		if (CollectionUtils.isEmpty(extList)) {
			return result;
		}
		List<DataExtend> dataExtendList = new ArrayList<>();
		List<DataExtendRelevance> dataExtendRelevanceList = new ArrayList<>();
		for (DataExtendRequest data : extList) {
			DataExtend dataExtend = new DataExtend();
			dataExtend.setId(DataUtils.uuid());
			dataExtend.setTableName(getTableName(operationLog.getDataType()));
			dataExtend.setColName(data.getColName());
			dataExtend.setColType(data.getColType());
			dataExtend.setColLength(data.getColLength());
			dataExtend.setDescription(data.getDescription());
			dataExtend.setCreatedOn(new Date());
			dataExtend.setCreatedBy(HttpContent.getOperatorId());
			dataExtend.setIsDeleted(GlobalConstants.IS_NOT_DELETED.intValue());
			dataExtendList.add(dataExtend);
			DataExtendRelevance dataExtendRelevance = new DataExtendRelevance();
			dataExtendRelevance.setId(DataUtils.uuid());
			dataExtendRelevance.setTableName(getTableName(operationLog.getDataType()));
			dataExtendRelevance.setColValue(data.getColValue());
			dataExtendRelevance.setDataId(operationLog.getDataId());
			dataExtendRelevance.setDataExtendId(dataExtend.getId());
			dataExtendRelevance.setCreatedOn(new Date());
			dataExtendRelevance.setCreatedBy(HttpContent.getOperatorId());
			dataExtendRelevance.setIsDeleted(GlobalConstants.IS_NOT_DELETED.intValue());
			dataExtendRelevanceList.add(dataExtendRelevance);
		}
		if (CollectionUtils.isNotEmpty(dataExtendList)) {
			result += dataExtendService.insertBatch(dataExtendList);
		}
		if (CollectionUtils.isNotEmpty(dataExtendRelevanceList)) {
			result += dataExtendService.insertDataExtendRelevances(dataExtendRelevanceList);
		}
		return result;
	}
	private String getTableName(Integer dataType) {
		for (DataTypeEnum d : DataTypeEnum.values()) {
			if (d.getValue() == dataType) {
				return d.getCode();
			}
		}
		return null;
	}
	private Role getMinLevelRoleByUserId() {
		UserBasicsInfo user = (UserBasicsInfo) session.getAttribute("user");
		Map<String, Object> map = new HashMap<>();
		map.put("userId", user.getId());
		map.put("status", GlobalConstants.STATUS_START);
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		return roleService.getMinLevelRoleByUserId(map);
	}
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MQTaskServiceImpl.java 
 * @Prject: mdm-service
 * @Package: com.mdm.task.service.impl 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年10月13日 下午4:47:45 
 * @version: V1.0   
 */
package com.mdm.task.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mdm.core.bean.pojo.ExtraSystemApi;
import com.mdm.core.bean.pojo.OperationLog;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.service.businesslog.BusinessLogService;
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
import com.mdm.response.UserOrganizationRoleMqResponse;
import com.mdm.service.dataextend.DataExtendService;
import com.mdm.service.district.DistrictRestfulService;
import com.mdm.service.merchant.MerchantService;
import com.mdm.service.organazation.OrganazationService;
import com.mdm.service.role.RoleService;
import com.mdm.service.user.UserService;
import com.mdm.task.service.MessageQueueTaskService;

/**
 * @ClassName: MQTaskServiceImpl
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年10月13日 下午4:47:45
 */
@Service
public class MessageQueueServiceImpl implements MessageQueueTaskService {

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

	@Autowired
	private BusinessLogService businessLogService;

	private static final String SUCCESS = "3";
	
	private static final String FAILD = "8";

	/*
	 * (non Javadoc)
	 * @Title: mqTaskCompensateService
	 * @Description: TODO
	 * @return
	 * @see com.mdm.task.service.MQTaskService#mqTaskCompensateService()
	 */
	@Override
	public String messageQueueTaskCompensateService() {
		List<ExtraSystemApi> list = businessLogService.queryRollbackInfoList();
		for (int i = 0; i < list.size(); i++) {
			ExtraSystemApi extraSystemapi = list.get(i);
			if(extraSystemapi.getBusinesslogid() == null)
				continue;
			try {
				if(StringUtils.isEmpty(extraSystemapi.getContent()) || StringUtils.isEmpty(extraSystemapi.getDataId())  || StringUtils.isEmpty(extraSystemapi.getDataType()) ){
					continue;
				}
				String id = businessLogService.queryCompensateTask(extraSystemapi);
				if(id == null){
					continue;
				}
				recoverHistory(id);
				businessLogService.updateStatusById(list.get(i).getBusinesslogid(),SUCCESS);
			} catch (Exception e) {
				e.printStackTrace();
				businessLogService.updateStatusById(list.get(i).getBusinesslogid(),FAILD);
				continue;
			}
		}
		return "success";
	}

	private Object recoverHistory(String id) throws MdmException {
		// 恢复主数据
		OperationLog operationLog = operationLogService.getById(id);
		if (operationLog.getDataType() == 1) {
			UserInfoMqResponse userInfoMqResponse = JSON.parseObject(operationLog.getContent(), UserInfoMqResponse.class);
			UserInfoSFRequest userInfoRequest = new UserInfoSFRequest();
			UserBasicsInfoRequest userBasicsInfoRequest = new UserBasicsInfoRequest();
			UserDetailInfoRequest userDetailInfoRequest = new UserDetailInfoRequest();
			BeanUtils.copyProperties(userInfoMqResponse.getUserBasicsInfoRequest(), userBasicsInfoRequest);
			BeanUtils.copyProperties(userInfoMqResponse.getUserDetailInfoRequest(), userDetailInfoRequest);
			userInfoRequest.setUserBasicsInfoRequest(userBasicsInfoRequest);
			userInfoRequest.setUserDetailInfoRequest(userDetailInfoRequest);
			userInfoRequest.setUserChannel(userInfoMqResponse.getUserChannel());
			userService.updateUserForSF(userInfoRequest);
		} else if (operationLog.getDataType() == 2) {
			Role role = JSON.parseObject(operationLog.getContent(), Role.class);
			roleService.update(role);
			recoverExts(operationLog, role.getExts());
		} else if (operationLog.getDataType() == 3) {
			DistrictRequest districtRequest = JSON.parseObject(operationLog.getContent(), DistrictRequest.class);
			districtService.update(districtRequest);
		} else if (operationLog.getDataType() == 4) {
			Organazation organazation = JSON.parseObject(operationLog.getContent(), Organazation.class);
			organazationService.update(organazation);
		} else if (operationLog.getDataType() == 5) {
			Merchant merchant = JSON.parseObject(operationLog.getContent(), Merchant.class);
			merchantService.updateMerchant(merchant);
		} else if (operationLog.getDataType() == 6) {
			// 用户-关联的组织角色
			UserOrganizationRoleMqResponse userOrganizationRoleMqResponse = JSON.parseObject(operationLog.getContent(),
					UserOrganizationRoleMqResponse.class);
			userService.setOrganizationRole(userOrganizationRoleMqResponse.getId(), userOrganizationRoleMqResponse.getCorpCode(),
					userOrganizationRoleMqResponse.getUserOrganizationRole());
		}
		return SUCCESS;
	}

	private int recoverExts(OperationLog operationLog, List<DataExtendRequest> extList) {
		int result = 0;
		// 删除当前扩展字段
		result += dataExtendService.deletById(operationLog.getDataId(), GlobalConstants.IS_DELETED, GlobalConstants.IS_NOT_DELETED);
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
}

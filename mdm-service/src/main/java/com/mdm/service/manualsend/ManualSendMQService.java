/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: ManualSendService.java 
 * @Prject: mdm-service
 * @Package: com.mdm.service.manualsend 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年10月27日 下午12:29:32 
 * @version: V1.0   
 */
package com.mdm.service.manualsend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.enums.LogTypeEnum;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.mq.producter.MQSenderAdapter;
import com.mdm.pojo.Role;
import com.mdm.request.ManualSendMQRequest;
import com.mdm.service.district.DistrictRestfulService;
import com.mdm.service.merchant.MerchantService;
import com.mdm.service.organazation.OrganazationService;
import com.mdm.service.role.RoleService;
import com.mdm.service.user.UserService;

/** 
 * @ClassName: ManualSendService 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年10月27日 下午12:29:32  
 */
@Service
public class ManualSendMQService {
	
	@Autowired
	private MQSenderAdapter mqSenderAdapter;

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private OrganazationService organazationService;
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private InvokeSystemService invokeSystemService;
	
	@Autowired
	DistrictRestfulService districtRestfulService;
	
	/**
	 * 
	 * @Title: sendMQ 
	 * @Description: 实现手工发送MQ的消息
	 * @param manualSendMQRequest
	 * @return: void
	 */
	public int sendMQ(ManualSendMQRequest manualSendMQRequest){
		String[] mainIds = manualSendMQRequest.getDataIds();
		Integer dataType = manualSendMQRequest.getDataType();
			//用户
			if(dataType.equals(DataTypeEnum.USER.getValue())){
				for(String dataId:mainIds){
//					mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.USER.getValue(),userService.queryById(dataId));
					userService.sendMQ(dataId);
				}
			}
			//角色
			if(dataType.equals(DataTypeEnum.ROLE.getValue())){
				for(String dataId:mainIds){
					Role role = roleService.getById(dataId);
					String systemCode = invokeSystemService.querySysCodeById(role.getSystemId()).getSysCode();
					role.setSystemCode(systemCode);
					mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.ROLE.getValue(),role);
				}
			}
			//组织机构
			if(dataType.equals(DataTypeEnum.ORGANAZATION.getValue())){
				for(String dataId:mainIds){
					mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.ORGANAZATION.getValue(),organazationService.queryById(dataId));
				}
			}
			//商家
			if(dataType.equals(DataTypeEnum.MERCHANT.getValue())){
				for(String dataId:mainIds){
					mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.MERCHANT.getValue(),merchantService.getMerchantById(dataId));
				}
			}
			// 行政区划
			if (dataType.equals(DataTypeEnum.DISTRICT.getValue())) {
				for (String dataId : mainIds) {
					JSONObject jsonObject = JSONObject.parseObject(districtRestfulService.queryById(dataId));
					if (jsonObject.get("data") != null) {
						mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.DISTRICT.getValue(), jsonObject.get("data"));
					}
				}
			}
			return 1;
	}
}

/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MQSenderAdapter.java 
 * @Prject: district-service
 * @Package: com.mdm.district.mq.producter 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年10月18日 下午3:02:03 
 * @version: V1.0   
 */
package com.mdm.mq.producter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.mdm.core.bean.common.CommonPojo;
import com.mdm.core.bean.pojo.BusinessLog;
import com.mdm.core.bean.pojo.OperationLog;
import com.mdm.core.bean.response.SystemInfoResponse;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.enums.BusinessLogStatusEnum;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.enums.LogTypeEnum;
import com.mdm.core.service.businesslog.BusinessLogService;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.service.operationlog.OperationLogService;
import com.mdm.core.service.sys.StaticDataService;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.DateUtils;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.JsonUtil;
import com.mdm.mq.DeliveryMessage;
import com.mdm.response.UserInfoMqResponse;
import com.mdm.service.user.UserService;

/**
 * @ClassName: MQSenderAdapter
 * @Description: TODO
 * @author: guox005
 * @date: 2016年10月18日 下午3:02:03
 */
@Component
public class MQSenderAdapter {

	private static Logger logger = LoggerFactory.getLogger(MQSenderAdapter.class);

	@Autowired
	private BusinessLogService businessLogService;

	@Autowired
	private OperationLogService operationLogService;

	@Autowired
	private StaticDataService staticDataService;

	@Autowired
	private QueueProducter queueProducter;

	@Autowired
	private TopicProducter topicProducter;

	@Autowired
	private InvokeSystemService invokeSystemService;

	private static Map<Integer, String> logTypeMap = new HashMap<Integer, String>();
	static {
		logTypeMap.put(LogTypeEnum.ADD.getValue(), LogTypeEnum.ADD.getCode());
		logTypeMap.put(LogTypeEnum.UPDATE.getValue(), LogTypeEnum.UPDATE.getCode());
		logTypeMap.put(LogTypeEnum.DELETE.getValue(), LogTypeEnum.DELETE.getCode());
	}

	/**
	 * 
	 * @Title: sendMQ
	 * @Description: 发送MQ
	 * @param logType
	 * @param dataType
	 * @param obj
	 * @return: void
	 */
	@Async
	@Transactional
	public void sendMQ(Integer logType, Integer dataType, Object obj) {

		// LogBack设置
		MDC.put("operatorId", HttpContent.getOperatorId());
		MDC.put("logId", HttpContent.getLogId());

		BusinessLog businessLog = new BusinessLog();
		// 获得操作内容后可插入数据库中
		String jsonString = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss");
		CommonPojo commonPojo = JsonUtil.toBean(jsonString, CommonPojo.class);
		// 获取主数据主键
		businessLog.setDataId(commonPojo.getId());
		businessLog.setContent(jsonString);
		businessLog.setLogType(logType);
		// 设置数据类型
		businessLog.setDataType(dataType);
		
		try {
			businessLog.setCreatedOn(DateUtils.parseStr2Date(DateUtils.TIMESTAMP, DateUtils.parseDate2Str(DateUtils.TIMESTAMP, new Date())));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		businessLog.setSysCode(HttpContent.getSysCodeLocal());
		// 设置下行标记
		if(HttpContent.getSysCodeLocal().trim().equalsIgnoreCase("MDM")){
			businessLog.setFlowFlag(MdmConstants.FLOW_DOWN);
		} else {
			businessLog.setFlowFlag(MdmConstants.FLOW_UP);
		}
		businessLog.setCreatedBy(HttpContent.getOperatorId());
		// 保存操作日志
		saveOperationLog(businessLog);
		
		//	针对“用户”下发，设置account为null
		if (dataType.equals(DataTypeEnum.USER.getValue())) {
			UserInfoMqResponse userInfoMqResponse = (UserInfoMqResponse)obj;
			userInfoMqResponse.getUserBasicsInfoRequest().setAccount(null);
			businessLog.setContent(JSON.toJSONStringWithDateFormat(userInfoMqResponse, "yyyy-MM-dd HH:mm:ss"));
		}
		
		// 下发数据
		DeliveryMessage deliveryMessage = new DeliveryMessage();
		deliveryMessage.setTimeStamp(DateUtils.parseDate2Str(DateUtils.TIMESTAMP, businessLog.getCreatedOn()));
		deliveryMessage.setOperation(logTypeMap.get(logType));
		deliveryMessage.setData(obj);
		deliveryMessage.setDataId(businessLog.getDataId());
		logger.info("下发消息内容 deliveryMessage ={}", JSON.toJSONStringWithDateFormat(deliveryMessage, "yyyy-MM-dd HH:mm:ss"));
		// 获取MQ下发消息方式
		String mqFlag = staticDataService.getSingleConfigParam(MdmConstants.MDM_SWITCH, MdmConstants.MQ_FLAG);
		try {
			if ("1".equals(mqFlag)) {
				sendQueue(businessLog.getDataType(), deliveryMessage);
			} else {
				sendTopic(businessLog.getDataType(), deliveryMessage);
			}
			// 已发送状态保存入库
			businessLog.setStatus(BusinessLogStatusEnum.SENT.getValue());
			saveBusinessLog(businessLog, obj);
		} catch (Exception e) {
			logger.error("send MQ error ={}", e.getMessage());
			// 失败状态保存入库
			businessLog.setStatus(BusinessLogStatusEnum.FAIL.getValue());
			saveBusinessLog(businessLog, obj);
		} finally {
			MDC.remove("operatorId");
			MDC.remove("logId");
		}

	}
	
	/**
	 * 针对（物业用户）系统下发MQ
	 * @Title: sendMQForCSS 
	 * @Description: TODO
	 * @param logType
	 * @param dataType
	 * @param obj
	 * @return: void
	 */
	@Async
	@Transactional
	public void sendMQForCSS(Integer logType, Integer dataType, Object obj) {
		
		// LogBack设置
		MDC.put("operatorId", HttpContent.getOperatorId());
		MDC.put("logId", HttpContent.getLogId());
		
		BusinessLog businessLog = new BusinessLog();
		// 获得操作内容后可插入数据库中
		String jsonString = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss");
		CommonPojo commonPojo = JsonUtil.toBean(jsonString, CommonPojo.class);
		// 获取主数据主键
		businessLog.setDataId(commonPojo.getId());
		businessLog.setContent(jsonString);
		businessLog.setLogType(logType);
		// 设置数据类型
		businessLog.setDataType(dataType);
		
		try {
			businessLog.setCreatedOn(DateUtils.parseStr2Date(DateUtils.TIMESTAMP, DateUtils.parseDate2Str(DateUtils.TIMESTAMP, new Date())));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		businessLog.setSysCode(HttpContent.getSysCodeLocal());
		// 设置下行标记
		if(HttpContent.getSysCodeLocal().trim().equalsIgnoreCase("MDM")){
			businessLog.setFlowFlag(MdmConstants.FLOW_DOWN);
		} else {
			businessLog.setFlowFlag(MdmConstants.FLOW_UP);
		}
		businessLog.setCreatedBy(HttpContent.getOperatorId());
		// 保存操作日志
		saveOperationLog(businessLog);
		
		//	针对“用户”下发，设置account为null
		if (dataType.equals(DataTypeEnum.USER.getValue())) {
			UserInfoMqResponse userInfoMqResponse = (UserInfoMqResponse)obj;
			userInfoMqResponse.getUserBasicsInfoRequest().setAccount(null);
			businessLog.setContent(JSON.toJSONStringWithDateFormat(userInfoMqResponse, "yyyy-MM-dd HH:mm:ss"));
		}
		
		// 下发数据
		DeliveryMessage deliveryMessage = new DeliveryMessage();
		deliveryMessage.setTimeStamp(DateUtils.parseDate2Str(DateUtils.TIMESTAMP, businessLog.getCreatedOn()));
		deliveryMessage.setOperation(logTypeMap.get(logType));
		deliveryMessage.setData(obj);
		deliveryMessage.setDataId(businessLog.getDataId());
		logger.info("下发消息内容 deliveryMessage ={}", JSON.toJSONString(deliveryMessage));
		// 获取MQ下发消息方式
		String mqFlag = staticDataService.getSingleConfigParam(MdmConstants.MDM_SWITCH, MdmConstants.MQ_FLAG);
		try {
			if ("1".equals(mqFlag)) {
				String msg = "";
				UserInfoMqResponse userInfoMqResponse = (UserInfoMqResponse)deliveryMessage.getData();
				userInfoMqResponse.getUserBasicsInfoRequest().setAccount(null);
				Integer bindState = userInfoMqResponse.getUserBasicsInfoRequest().getBindState();
				// 物业系统下发用户消息
				msg = queueProducter.getUserBindState(deliveryMessage, MdmConstants.CSS, bindState);
				queueProducter.sendUserCSSQueue(msg);
				msg = queueProducter.getUserBindState(deliveryMessage, MdmConstants.SFYON, bindState);
				queueProducter.sendUserSFYONQueue(msg);
				msg = queueProducter.getUserBindState(deliveryMessage, MdmConstants.SFYOFF, bindState);
				queueProducter.sendUserSFYOFFQueue(msg);
			} else {
				sendTopic(businessLog.getDataType(), deliveryMessage);
			}
			// 已发送状态保存入库
			businessLog.setStatus(BusinessLogStatusEnum.SENT.getValue());
			saveBusinessLogForCSS(businessLog, obj);
		} catch (Exception e) {
			logger.error("send MQ error ={}", e.getMessage());
			// 失败状态保存入库
			businessLog.setStatus(BusinessLogStatusEnum.FAIL.getValue());
			saveBusinessLogForCSS(businessLog, obj);
		} finally {
			MDC.remove("operatorId");
			MDC.remove("logId");
		}
		
	}

	/**
	 * 
	 * @Title: saveOperationLog
	 * @Description: 保存操作日志
	 * @param businessLog
	 * @return: void
	 */
	private void saveOperationLog(BusinessLog businessLog) {
		OperationLog operationLog = new OperationLog();
		operationLog.setId(DataUtils.uuid());
		operationLog.setDataId(businessLog.getDataId());
		operationLog.setContent(businessLog.getContent());
		operationLog.setCreatedOn(businessLog.getCreatedOn());
		operationLog.setCreatedBy(businessLog.getCreatedBy());
		operationLog.setDataType(businessLog.getDataType());
		operationLog.setSysCode(businessLog.getSysCode());
		operationLog.setFlowFlag(businessLog.getFlowFlag());
		// 设置操作人IP
		operationLog.setOperatorIp(HttpContent.getOperatorIpLocal());
		
		operationLogService.insert(operationLog);
	}

	/**
	 * 
	 * @Title: saveBusinessLog
	 * @Description: 根据一个日志模板，统一批量生成下发日志
	 * @param businessLog
	 * @return: void
	 */
	private List<String> saveBusinessLog(BusinessLog businessLog, Object obj) {
		List<String> ids = new ArrayList<String>();
		Map<String, String> sytemMap = new HashMap<String, String>();
		// 获取所有接入信息
		List<SystemInfoResponse> list = invokeSystemService.queryAllSystemInfo();
		for (SystemInfoResponse systeminfo : list) {
			sytemMap.put(systeminfo.getSysCode(), systeminfo.getId());
		}
		String sysCode = HttpContent.getSysCodeLocal();
		if (businessLog.getDataType().equals(DataTypeEnum.USER.getValue())) {
			if (!MdmConstants.ACRM.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.ACRM, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("ACRM"), businessLog));
			}
			if (!MdmConstants.CSS.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.CSS, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("CSS"), businessLog));
			}
			if (!MdmConstants.SCM.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.SCM, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SCM"), businessLog));
			}
			if (!MdmConstants.SFYOFF.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.SFYOFF, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SFYOFF"), businessLog));
			}
			if (!MdmConstants.SFYON.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.SFYON, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SFYON"), businessLog));
			}
			if (!MdmConstants.SSY.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.SSY, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SSY"), businessLog));
			}
			if (!MdmConstants.SAAS.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.SAAS, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SAAS"), businessLog));
			}
		}
		if (businessLog.getDataType().equals(DataTypeEnum.ROLE.getValue()) || businessLog.getDataType().equals(DataTypeEnum.SOUR.getValue()) || businessLog.getDataType().equals(DataTypeEnum.ORGANAZATION.getValue())) {
			if (!MdmConstants.ACRM.equals(sysCode)) {
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("ACRM"), businessLog));
			}
			if (!MdmConstants.CSS.equals(sysCode)) {
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("CSS"), businessLog));
			}
			if (!MdmConstants.SCM.equals(sysCode)) {
				// ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SAAS"),businessLog));
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SCM"), businessLog));
			}
			if (!MdmConstants.SFYOFF.equals(sysCode)) {
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SFYOFF"), businessLog));
			}
			if (!MdmConstants.SFYON.equals(sysCode)) {
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SFYON"), businessLog));
			}
			if (!MdmConstants.SSY.equals(sysCode)) {
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SSY"), businessLog));
			}
			if (!MdmConstants.SAAS.equals(sysCode)) {
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SAAS"), businessLog));
			}
			// ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("YB"),businessLog));

		}
		if (businessLog.getDataType().equals(DataTypeEnum.MERCHANT.getValue())) {
			if (!MdmConstants.ACRM.equals(sysCode)) {
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("ACRM"), businessLog));
			}
			if (!MdmConstants.SCM.equals(sysCode)) {
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SCM"), businessLog));
			}
			if (!MdmConstants.SSY.equals(sysCode)) {
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SSY"), businessLog));
			}
		}
		// 行政区划下发
		if(businessLog.getDataType().equals(DataTypeEnum.DISTRICT.getValue())){
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("ACRM"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("CSS"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SAAS"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SCM"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SFYOFF"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SSY"),businessLog));
//			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("YB"),businessLog));
		}
		return ids;
	}
	
	
	private List<String> saveBusinessLogForCSS(BusinessLog businessLog, Object obj) {
		List<String> ids = new ArrayList<String>();
		Map<String, String> sytemMap = new HashMap<String, String>();
		// 获取所有接入信息
		List<SystemInfoResponse> list = invokeSystemService.queryAllSystemInfo();
		for (SystemInfoResponse systeminfo : list) {
			sytemMap.put(systeminfo.getSysCode(), systeminfo.getId());
		}
		String sysCode = HttpContent.getSysCodeLocal();
		if (businessLog.getDataType().equals(DataTypeEnum.USER.getValue()) || businessLog.getDataType().equals(DataTypeEnum.ROLE.getValue()) || businessLog.getDataType().equals(DataTypeEnum.SOUR.getValue()) || businessLog.getDataType().equals(DataTypeEnum.ORGANAZATION.getValue())) {
			if (MdmConstants.BMS.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.BMS, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("BMS"), businessLog));
			}
			if (MdmConstants.CSS.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.CSS, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("CSS"), businessLog));
			}
			if (!MdmConstants.SFYOFF.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.SFYOFF, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SFYOFF"), businessLog));
			}
			if (!MdmConstants.SFYON.equals(sysCode)) {
				handleUserBindState(businessLog, MdmConstants.SFYON, obj);
				ids.add(saveSingleBusinessLog(sytemMap.get("MDM"), sytemMap.get("SFYON"), businessLog));
			}
		}
		return ids;
	}

	private void handleUserBindState(BusinessLog businessLog, String sysCode, Object obj){
		
		UserInfoMqResponse userInfoMqResponse = (UserInfoMqResponse)obj;
		Integer bindState = userInfoMqResponse.getUserBasicsInfoRequest().getBindState();
		String states = (String)UserService.analysisBindState(bindState);
		int index = ArrayUtils.indexOf(MdmConstants.STATE_SYSCODE, sysCode);
		if (index == 6) {// 物业云不同的系统编码BMS
			index = 1;
		}
		if (index == 7) {// 收费云不同系统编码SFYOFF
			index = 5;
		}
		Integer state = Integer.parseInt(String.valueOf(states.charAt(index)));
		// 设置下发的绑定状态
		userInfoMqResponse.getUserBasicsInfoRequest().setPhoneNumberConfirmed(state);
		userInfoMqResponse.getUserDetailInfoRequest().setPhoneNumberConfirmed(state);
		
		userInfoMqResponse.getUserBasicsInfoRequest().setAccount(null);
		businessLog.setContent(JSON.toJSONStringWithDateFormat(userInfoMqResponse, "yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
	 * 
	 * @Title: saveSingleBusinessLog
	 * @Description: 保存单条下发消息日志
	 * @param sourceSystemId
	 * @param destSystemId
	 * @param templeteLog
	 * @return: void
	 */
	private String saveSingleBusinessLog(String sourceSystemId, String destSystemId, BusinessLog templeteLog) {
		templeteLog.setId(DataUtils.uuid());
		templeteLog.setSourceSystemId(sourceSystemId);
		templeteLog.setDestSystemId(destSystemId);
		templeteLog.setCreatedBy(sourceSystemId);
		templeteLog.setIsDeleted(0);
		businessLogService.insert(templeteLog);
		return templeteLog.getId();
	}

	/**
	 * 
	 * @Title: sendTopic
	 * @Description:
	 * @param dataType
	 * @param deliveryMessage
	 * @return: void
	 */
	private void sendQueue(Integer dataType, DeliveryMessage deliveryMessage) {
		String msg = JSON.toJSONStringWithDateFormat(deliveryMessage, "yyyy-MM-dd HH:mm:ss");
		// 用户主数据
		if (dataType == DataTypeEnum.USER.getValue()) {
			queueProducter.sendAllUserQueue(deliveryMessage);
		} else if (dataType == DataTypeEnum.ROLE.getValue()) {
			queueProducter.sendAllRoleQueue(msg);
		} else if (dataType == DataTypeEnum.ORGANAZATION.getValue()) {
			queueProducter.sendAllOrganazationQueue(msg);
		} else if (dataType == DataTypeEnum.MERCHANT.getValue()) {
			queueProducter.sendAllMerchantQueue(msg);
		} else if (dataType == DataTypeEnum.SOUR.getValue()) {
			queueProducter.sendAllSourQueue(msg);
		} else if (dataType == DataTypeEnum.DISTRICT.getValue()) {
			queueProducter.sendAllDistrictQueue(msg);
		}
	}

	/**
	 * 
	 * @Title: sendQueue
	 * @Description: 通过队列方式下发消息
	 * @param dataType
	 * @param deliveryMessage
	 * @return: void
	 */
	private void sendTopic(Integer dataType, DeliveryMessage deliveryMessage) {
		String msg = JSON.toJSONStringWithDateFormat(deliveryMessage, "yyyy-MM-dd HH:mm:ss");
		// 用户主数据
		if (dataType == DataTypeEnum.USER.getValue()) {
			topicProducter.sendUserTopic(msg);
		} else if (dataType == DataTypeEnum.ROLE.getValue()) {
			topicProducter.sendRoleTopic(msg);
		} else if (dataType == DataTypeEnum.ORGANAZATION.getValue()) {
			topicProducter.sendOrganizationTopic(msg);
		} else if (dataType == DataTypeEnum.MERCHANT.getValue()) {
			topicProducter.sendMerchantTopic(msg);
		} else if (dataType == DataTypeEnum.SOUR.getValue()) {
			topicProducter.sendSourTopic(msg);
		}
	}

	/**
	 * 
	 * @Title: asynSendMQ
	 * @Description: 异步发送消息，适应批量导入数据，后台定时任务发送。
	 * @param businessLog
	 * @return: void
	 */
	public void asynSendMQ(BusinessLog businessLog) {

		DeliveryMessage deliveryMessage = new DeliveryMessage();
		deliveryMessage.setTimeStamp(DateUtils.parseDate2Str(DateUtils.TIMESTAMP, businessLog.getCreatedOn()));
		deliveryMessage.setOperation(logTypeMap.get(businessLog.getLogType()));
		deliveryMessage.setData(businessLog.getContent());
		deliveryMessage.setDataId(businessLog.getDataId());
		logger.info("下发消息内容 deliveryMessage ={}", JSON.toJSONString(deliveryMessage));
		// 获取MQ下发消息方式
		String mqFlag = staticDataService.getSingleConfigParam(MdmConstants.MDM_SWITCH, MdmConstants.MQ_FLAG);
		try {
			if ("1".equals(mqFlag)) {
				sendQueue(businessLog.getDataType(), deliveryMessage);
			} else {
				sendTopic(businessLog.getDataType(), deliveryMessage);
			}
			// 已发送状态保存入库
			businessLog.setStatus(BusinessLogStatusEnum.SENT.getValue());
			businessLogService.update(businessLog);
		} catch (Exception e) {
			logger.error("send MQ error ={}", e.getMessage());
			// 失败状态保存入库
			businessLog.setStatus(BusinessLogStatusEnum.FAIL.getValue());
			businessLogService.update(businessLog);
		}
	}
}
/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: BusinessLogAspect.java 
 * @Prject: mdm-web
 * @Package: com.mdm.aop 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年10月8日 下午1:25:16 
 * @version: V1.0   
 */
package com.mdm.aop;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.mdm.mq.producter.QueueProducter;
import com.mdm.mq.producter.TopicProducter;
import com.mdm.pojo.District;
import com.mdm.pojo.Organazation;
import com.mdm.pojo.Role;
import com.mdm.service.district.DistrictService;
import com.mdm.service.organazation.OrganazationService;
import com.mdm.service.role.RoleService;

/**
 * @ClassName: BusinessLogAspect
 * @Description: TODO
 * @author: guox005
 * @date: 2016年10月8日 下午1:25:16
 */
//@Aspect
//@Component
public class BusinessLogAspect {
	private static Logger logger = LoggerFactory.getLogger(BusinessLogAspect.class);
	private final String afterInsertOrUpdate = "execution(* com.mdm.service.organazation.OrganazationService.insert*(..))"
			+ " or execution(* com.mdm.service.organazation.OrganazationService.update*(..))"
			+ " or execution(* com.mdm.service.organazation.OrganazationService.delete*(..))"
			+ " or execution(* com.mdm.service.role.RoleService.insert*(..))"
			+ " or execution(* com.mdm.service.role.RoleService.update*(..))"
			+ " or execution(* com.mdm.service.role.RoleService.delete*(..))"
			+ " or execution(* com.mdm.service.user.UserService.insert*(..))"
			+ " or execution(* com.mdm.service.user.UserService.update*(..))"
			+ " or execution(* com.mdm.service.user.UserService.delete*(..))";
	@Autowired
	private OrganazationService organazationService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DistrictService districtService;
	
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
	//数据操作类型
	private String operation;
	//操作数据主对象
	private Object object;
	
	/**
	 * 
	 * @Title: after 
	 * @Description: 拦截方法
	 * @param joinPoint
	 * @return: void
	 */
	@After(afterInsertOrUpdate) // 此处为pointcut
	public void after(JoinPoint joinPoint) {
		// 每一个符合表达式条件的位置为joinPoint
		Object[] args = joinPoint.getArgs();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		// 拦截的实体类
		Object target = joinPoint.getTarget();
		String className = target.getClass().getName();
		logger.debug("className={}", className);
		BusinessLog businessLog = new BusinessLog();
		// 数据操作类型-删除
		if (method.getName().indexOf("delete") >= 0) {
			// 删除的参数是数据主键
			businessLog.setDataId(args[0].toString());
			businessLog.setLogType(LogTypeEnum.DELETE.getValue());
			operation = LogTypeEnum.DELETE.getCode();
			// 拦截用户服务
			if (className.indexOf(DataTypeEnum.USER.getCode()) > 0) {
			} else if (className.indexOf(DataTypeEnum.ROLE.getCode()) > 0) {
				Role role = roleService.getById(args[0].toString());
				businessLog.setContent(JsonUtil.toString(args[0]));
				businessLog.setDataType(DataTypeEnum.ROLE.getValue());
				object = role;
			} else if (className.indexOf(DataTypeEnum.ORGANAZATION.getCode()) > 0) {
				Organazation organazation = organazationService.queryById(args[0].toString());
				businessLog.setContent(JsonUtil.toString(args[0]));
				businessLog.setDataType(DataTypeEnum.ORGANAZATION.getValue());
				object = organazation;
			} else if (className.indexOf(DataTypeEnum.DISTRICT.getCode()) > 0) {
				District district = districtService.queryById(args[0].toString());
				businessLog.setContent(JsonUtil.toString(args[0]));
				businessLog.setDataType(DataTypeEnum.DISTRICT.getValue());
				object = district;
			}else if (className.indexOf(DataTypeEnum.MERCHANT.getCode()) > 0) {
				businessLog.setDataType(DataTypeEnum.MERCHANT.getValue());
			}
		} else {
			// 获得操作内容后可插入数据库中
			String jsonString = JsonUtil.toString(args[0]);
			CommonPojo commonPojo = JsonUtil.toBean(jsonString, CommonPojo.class);
			// 获取主数据主键
			businessLog.setDataId(commonPojo.getId());
			businessLog.setContent(jsonString);
			object = args[0];
			if (method.getName().indexOf("add") > 0) {
				businessLog.setLogType(LogTypeEnum.ADD.getValue());
				operation = LogTypeEnum.ADD.getCode();
			} else {
				businessLog.setLogType(LogTypeEnum.UPDATE.getValue());
				operation = LogTypeEnum.UPDATE.getCode();
				
			}
			// 设置数据类型
			if (className.indexOf(DataTypeEnum.USER.getCode()) > 0) {
				businessLog.setDataType(DataTypeEnum.USER.getValue());
			} else if (className.indexOf(DataTypeEnum.ROLE.getCode()) > 0) {
				businessLog.setDataType(DataTypeEnum.ROLE.getValue());
			} else if (className.indexOf(DataTypeEnum.DISTRICT.getCode()) > 0) {
				businessLog.setDataType(DataTypeEnum.DISTRICT.getValue());
			} else if (className.indexOf(DataTypeEnum.ORGANAZATION.getCode()) > 0) {
				businessLog.setDataType(DataTypeEnum.ORGANAZATION.getValue());
			} else if (className.indexOf(DataTypeEnum.MERCHANT.getCode()) > 0) {
				businessLog.setDataType(DataTypeEnum.MERCHANT.getValue());
			}
		}
		// 设置下行标记
		businessLog.setFlowFlag(1);
		
		try {
			businessLog.setCreatedOn(DateUtils.parseStr2Date(DateUtils.TIMESTAMP, DateUtils.parseDate2Str(DateUtils.TIMESTAMP, new Date())));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		//保存操作日志
		saveOperationLog(businessLog);
		//下发数据
		
		DeliveryMessage deliveryMessage = new DeliveryMessage();
		deliveryMessage.setTimeStamp(DateUtils.parseDate2Str(DateUtils.TIMESTAMP,businessLog.getCreatedOn()));
		deliveryMessage.setOperation(operation);
		deliveryMessage.setData(object);
		deliveryMessage.setDataId(businessLog.getDataId());
		logger.info("下发消息内容 deliveryMessage ={}", deliveryMessage);
		//获取MQ下发消息方式
		String mqFlag = staticDataService.getSingleConfigParam(MdmConstants.MDM_SWITCH, MdmConstants.MQ_FLAG);
		try{
			if("1".equals(mqFlag)){
				sendQueue(businessLog.getDataType(),deliveryMessage);
			}else {
				sendTopic(businessLog.getDataType(),deliveryMessage);
			}
			//已发送状态保存入库
			businessLog.setStatus(BusinessLogStatusEnum.SENT.getValue());
			saveBusinessLog(businessLog);
		}catch(Exception e){
			logger.error("send MQ error ={}",e.getMessage());
			//失败状态保存入库
			businessLog.setStatus(BusinessLogStatusEnum.FAIL.getValue());
			saveBusinessLog(businessLog);
		}
		
	}
	/**
	 * 
	 * @Title: saveOperationLog 
	 * @Description: 保存操作日志
	 * @param businessLog
	 * @return: void
	 */
	private void saveOperationLog(BusinessLog businessLog){
		OperationLog operationLog = new OperationLog();
		operationLog.setId(DataUtils.uuid());
		operationLog.setDataId(businessLog.getDataId());
		operationLog.setContent(businessLog.getContent());
		operationLog.setCreatedOn(businessLog.getCreatedOn());
		operationLog.setCreatedBy(businessLog.getCreatedBy());
		
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
	private List<String> saveBusinessLog(BusinessLog businessLog){
		List<String> ids = new ArrayList<String>();
		Map<String,String> sytemMap = new HashMap<String,String>();
		//获取所有接入信息
		List<SystemInfoResponse> list = invokeSystemService.queryAllSystemInfo();
		for(SystemInfoResponse systeminfo:list){
			sytemMap.put(systeminfo.getSysCode(),systeminfo.getId());
		}
		if(businessLog.getDataType().equals(DataTypeEnum.USER.getValue())||
				businessLog.getDataType().equals(DataTypeEnum.ROLE.getValue())||
				businessLog.getDataType().equals(DataTypeEnum.ORGANAZATION.getValue())){
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("ACRM"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("CSS"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SAAS"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SCM"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SFYOFF"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SFYON"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SSY"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("YB"),businessLog));
			
		}
		if(businessLog.getDataType().equals(DataTypeEnum.DISTRICT.getValue())){
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("ACRM"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("CSS"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SAAS"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SCM"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SFYOFF"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SSY"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("YB"),businessLog));
		}
		if(businessLog.getDataType().equals(DataTypeEnum.MERCHANT.getValue())){
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("ACRM"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SCM"),businessLog));
			ids.add(saveSingleBusinessLog(sytemMap.get("MDM"),sytemMap.get("SSY"),businessLog));
		}
		return ids;
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
	private String   saveSingleBusinessLog(String sourceSystemId,String destSystemId,BusinessLog templeteLog){
		templeteLog.setId(DataUtils.uuid());
		templeteLog.setSourceSystemId(sourceSystemId);
		templeteLog.setDestSystemId(destSystemId);
		templeteLog.setCreatedBy("MDM");
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
	private void sendQueue(Integer dataType,DeliveryMessage deliveryMessage) {
		String msg = JsonUtil.toString(deliveryMessage);
		//用户主数据
		if(dataType==DataTypeEnum.USER.getValue()){
			queueProducter.sendAllUserQueue(deliveryMessage);
		}else if(dataType==DataTypeEnum.ROLE.getValue()){
			queueProducter.sendAllRoleQueue(msg);
		}else if(dataType==DataTypeEnum.ORGANAZATION.getValue()){
			queueProducter.sendAllOrganazationQueue(msg);
		}else if(dataType==DataTypeEnum.SOUR.getValue()){
			queueProducter.sendAllSourQueue(msg);
		}else if(dataType==DataTypeEnum.MERCHANT.getValue()){
			queueProducter.sendAllMerchantQueue(msg);
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
	private void sendTopic(Integer dataType,DeliveryMessage deliveryMessage){
		String msg = JsonUtil.toString(deliveryMessage);
		//用户主数据
		if(dataType==DataTypeEnum.USER.getValue()){
			topicProducter.sendUserTopic(msg);
		}else if(dataType==DataTypeEnum.ROLE.getValue()){
			topicProducter.sendRoleTopic(msg);
		}else if(dataType==DataTypeEnum.ORGANAZATION.getValue()){
			topicProducter.sendOrganizationTopic(msg);
		}else if(dataType==DataTypeEnum.DISTRICT.getValue()){
			topicProducter.sendDistrictTopic(msg);
		}else if(dataType==DataTypeEnum.MERCHANT.getValue()){
			topicProducter.sendMerchantTopic(msg);
		}		
	}
}

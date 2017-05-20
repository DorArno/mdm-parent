/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: QueueProducter.java 
 * @Prject: mdm-service
 * @Package: com.mdm.mq.producter 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年10月9日 下午3:07:16 
 * @version: V1.0   
 */
package com.mdm.mq.producter;

import javax.annotation.Resource;
import javax.jms.Queue;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.mdm.core.constants.DigitalConstants;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.util.HttpContent;
import com.mdm.mq.DeliveryMessage;
import com.mdm.response.UserInfoMqResponse;
import com.mdm.service.user.UserService;

/**
 * @ClassName: QueueProducter
 * @Description: TODO
 * @author: guox005
 * @date: 2016年10月9日 下午3:07:16
 */
@Component
public class QueueProducter {
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name = "userSSYQueue")
	private Queue userSSYQueue;
	@Resource(name = "userCSSQueue")
	private Queue userCSSQueue;
	@Resource(name = "userSCMQueue")
	private Queue userSCMQueue;
	@Resource(name = "userSFYONQueue")
	private Queue userSFYONQueue;
	@Resource(name = "userYBQueue")
	private Queue userYBQueue;
	@Resource(name = "userSAASQueue")
	private Queue userSAASQueue;
	@Resource(name = "userACRMQueue")
	private Queue userACRMQueue;
	@Resource(name = "userSFYOFFQueue")
	private Queue userSFYOFFQueue;
	@Resource(name = "districtSSYQueue")
	private Queue districtSSYQueue;
	@Resource(name = "districtCSSQueue")
	private Queue districtCSSQueue;
	@Resource(name = "districtSCMQueue")
	private Queue districtSCMQueue;
	@Resource(name = "districtYBQueue")
	private Queue districtYBQueue;
	@Resource(name = "districtSAASQueue")
	private Queue districtSAASQueue;
	@Resource(name = "districtACRMQueue")
	private Queue districtACRMQueue;
	@Resource(name = "districtSFYOFFQueue")
	private Queue districtSFYOFFQueue;
	@Resource(name = "roleSSYQueue")
	private Queue roleSSYQueue;
	@Resource(name = "roleCSSQueue")
	private Queue roleCSSQueue;
	@Resource(name = "roleSCMQueue")
	private Queue roleSCMQueue;
	@Resource(name = "roleSFYONQueue")
	private Queue roleSFYONQueue;
	@Resource(name = "roleYBQueue")
	private Queue roleYBQueue;
	@Resource(name = "roleSAASQueue")
	private Queue roleSAASQueue;
	@Resource(name = "roleACRMQueue")
	private Queue roleACRMQueue;
	@Resource(name = "roleSFYOFFQueue")
	private Queue roleSFYOFFQueue;
	@Resource(name = "sourSSYQueue")
	private Queue sourSSYQueue;
	@Resource(name = "sourCSSQueue")
	private Queue sourCSSQueue;
	@Resource(name = "sourSCMQueue")
	private Queue sourSCMQueue;
	@Resource(name = "sourSFYONQueue")
	private Queue sourSFYONQueue;
	@Resource(name = "sourYBQueue")
	private Queue sourYBQueue;
	@Resource(name = "sourSAASQueue")
	private Queue sourSAASQueue;
	@Resource(name = "sourACRMQueue")
	private Queue sourACRMQueue;
	@Resource(name = "sourSFYOFFQueue")
	private Queue sourSFYOFFQueue;
	@Resource(name = "organizationSSYQueue")
	private Queue organizationSSYQueue;
	@Resource(name = "organizationCSSQueue")
	private Queue organizationCSSQueue;
	@Resource(name = "organizationSCMQueue")
	private Queue organizationSCMQueue;
	@Resource(name = "organizationSFYONQueue")
	private Queue organizationSFYONQueue;
	@Resource(name = "organizationYBQueue")
	private Queue organizationYBQueue;
	@Resource(name = "organizationSAASQueue")
	private Queue organizationSAASQueue;
	@Resource(name = "organizationACRMQueue")
	private Queue organizationACRMQueue;
	@Resource(name = "organizationSFYOFFQueue")
	private Queue organizationSFYOFFQueue;
	@Resource(name = "merchantSSYQueue")
	private Queue merchantSSYQueue;
	@Resource(name = "merchantSCMQueue")
	private Queue merchantSCMQueue;
	@Resource(name = "merchantACRMQueue")
	private Queue merchantACRMQueue;

	public void sendUserSSYQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.userSSYQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendUserCSSQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.userCSSQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendUserSCMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.userSCMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendUserSFYONQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.userSFYONQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendUserYBQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.userYBQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendUserSAASQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.userSAASQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendUserACRMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.userACRMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendUserSFYOFFQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.userSFYOFFQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendDistrictSSYQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.districtSSYQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendDistrictCSSQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.districtCSSQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendDistrictSCMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.districtSCMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendDistrictYBQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.districtYBQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendDistrictSAASQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.districtSAASQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendDistrictACRMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.districtACRMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendDistrictSFYOFFQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.districtSFYOFFQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendRoleSSYQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.roleSSYQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendRoleCSSQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.roleCSSQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendRoleSCMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.roleSCMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendRoleSFYONQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.roleSFYONQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendRoleYBQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.roleYBQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendRoleSAASQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.roleSAASQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendRoleACRMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.roleACRMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendRoleSFYOFFQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.roleSFYOFFQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendOrganizationSSYQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.organizationSSYQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendOrganizationCSSQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.organizationCSSQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendOrganizationSCMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.organizationSCMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendOrganizationSFYONQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.organizationSFYONQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendOrganizationYBQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.organizationYBQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendOrganizationSAASQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.organizationSAASQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendOrganizationACRMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.organizationACRMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendOrganizationSFYOFFQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.organizationSFYOFFQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendSourSSYQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.sourSSYQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendSourCSSQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.sourCSSQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendSourSCMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.sourSCMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendSourSFYONQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.sourSFYONQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendSourYBQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.sourYBQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendSourSAASQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.sourSAASQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendSourACRMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.sourACRMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendSourSFYOFFQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.sourSFYOFFQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendMerchantSSYQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.merchantSSYQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendMerchantSCMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.merchantSCMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendMerchantACRMQueue(String msg) {
		this.jmsTemplate.convertAndSend(this.merchantACRMQueue, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	/**
	 * @Title: sendAllUserQueue
	 * @Description: 队列下发所有子系统用户信息
	 * @param msg
	 * @return: void
	 */
	public void sendAllUserQueue(DeliveryMessage deliveryMessage) {
		
		String msg = "";
		UserInfoMqResponse userInfoMqResponse = (UserInfoMqResponse)deliveryMessage.getData();
		userInfoMqResponse.getUserBasicsInfoRequest().setAccount(null);
		Integer bindState = userInfoMqResponse.getUserBasicsInfoRequest().getBindState();
		
		String sysCode = HttpContent.getSysCodeLocal();
		if (!MdmConstants.ACRM.equals(sysCode)) {
			msg = getUserBindState(deliveryMessage, MdmConstants.ACRM, bindState);
			this.sendUserACRMQueue(msg);
		}
		if (!MdmConstants.CSS.equals(sysCode)) {
			msg = getUserBindState(deliveryMessage, MdmConstants.CSS, bindState);
			this.sendUserCSSQueue(msg);
		}
		if (!MdmConstants.SAAS.equals(sysCode)) {
			msg = getUserBindState(deliveryMessage, MdmConstants.SAAS, bindState);
			this.sendUserSAASQueue(msg);
		}
		if (!MdmConstants.SCM.equals(sysCode)) {
			msg = getUserBindState(deliveryMessage, MdmConstants.SCM, bindState);
			this.sendUserSCMQueue(msg);
		}
		if (!MdmConstants.SFYOFF.equals(sysCode)) {
			msg = getUserBindState(deliveryMessage, MdmConstants.SFYOFF, bindState);
			this.sendUserSFYOFFQueue(msg);
		}
		if (!MdmConstants.SFYON.equals(sysCode)) {
			msg = getUserBindState(deliveryMessage, MdmConstants.SFYON, bindState);
			this.sendUserSFYONQueue(msg);
		}
		if (!MdmConstants.SSY.equals(sysCode)) {
			msg = getUserBindState(deliveryMessage, MdmConstants.SSY, bindState);
			this.sendUserSSYQueue(msg);
		}
//		if (!MdmConstants.YB.equals(sysCode)) {
//			this.sendUserYBQueue(msg);
//		}
	}
	/**
	 * 针对不同业务系统的下发，设置不同的数据绑定状态
	 * @Title: getUserBindState 
	 * @Description: TODO
	 * @param deliveryMessage
	 * @param sysCode
	 * @return
	 * @return: String
	 */
	public String getUserBindState(DeliveryMessage deliveryMessage, String sysCode, Integer bindState){
		
		UserInfoMqResponse userInfoMqResponse = (UserInfoMqResponse)deliveryMessage.getData();
		userInfoMqResponse.getUserBasicsInfoRequest().setAccount(null);
		
		// 获取数据绑定状态，进行分析
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
		userInfoMqResponse.getUserBasicsInfoRequest().setBindState(null);
		deliveryMessage.setData(userInfoMqResponse);
		String msg = JSON.toJSONStringWithDateFormat(deliveryMessage, "yyyy-MM-dd HH:mm:ss");
		
		return msg;
	}
	/**
	 * @Title: sendAllRoleQueue
	 * @Description: 队列下发所有子系统角色信息
	 * @param msg
	 * @return: void
	 */
	public void sendAllRoleQueue(String msg) {
		String sysCode = HttpContent.getSysCodeLocal();
		if (!MdmConstants.ACRM.equals(sysCode)) {
			this.sendRoleACRMQueue(msg);
		}
		if (!MdmConstants.CSS.equals(sysCode)) {
			this.sendRoleCSSQueue(msg);
		}
		if (!MdmConstants.SAAS.equals(sysCode)) {
			this.sendRoleSAASQueue(msg);
		}
		if (!MdmConstants.SCM.equals(sysCode)) {
			this.sendRoleSCMQueue(msg);
		}
		if (!MdmConstants.SFYOFF.equals(sysCode)) {
			this.sendRoleSFYOFFQueue(msg);
		}
		if (!MdmConstants.SFYON.equals(sysCode)) {
			this.sendRoleSFYONQueue(msg);
		}
		if (!MdmConstants.SSY.equals(sysCode)) {
			this.sendRoleSSYQueue(msg);
		}
//		if (!MdmConstants.YB.equals(sysCode)) {
//			this.sendRoleYBQueue(msg);
//		}
	}
	/**
	 * @Title: sendAllOrganazationQueue
	 * @Description: 队列下发所有子系统组织信息
	 * @param msg
	 * @return: void
	 */
	public void sendAllOrganazationQueue(String msg) {
		String sysCode = HttpContent.getSysCodeLocal();
		if (!MdmConstants.ACRM.equals(sysCode)) {
			this.sendOrganizationACRMQueue(msg);
		}
		if (!MdmConstants.CSS.equals(sysCode)) {
			this.sendOrganizationCSSQueue(msg);
		}
		if (!MdmConstants.SAAS.equals(sysCode)) {
			this.sendOrganizationSAASQueue(msg);
		}
		if (!MdmConstants.SCM.equals(sysCode)) {
			this.sendOrganizationSCMQueue(msg);
		}
		if (!MdmConstants.SFYOFF.equals(sysCode)) {
			this.sendOrganizationSFYOFFQueue(msg);
		}
		if (!MdmConstants.SFYON.equals(sysCode)) {
			this.sendOrganizationSFYONQueue(msg);
		}
		if (!MdmConstants.SSY.equals(sysCode)) {
			this.sendOrganizationSSYQueue(msg);
		}
//		if (!MdmConstants.YB.equals(sysCode)) {
//			this.sendOrganizationYBQueue(msg);
//		}
	}
	/**
	 * @Title: sendAllDistrictQueue
	 * @Description: 队列下发所有子系统行政区域信息
	 * @param msg
	 * @return: void
	 */
	public void sendAllDistrictQueue(String msg) {
		String sysCode = HttpContent.getSysCodeLocal();
		if (!MdmConstants.ACRM.equals(sysCode)) {
			this.sendDistrictACRMQueue(msg);
		}
		if (!MdmConstants.CSS.equals(sysCode)) {
			this.sendDistrictCSSQueue(msg);
		}
		if (!MdmConstants.SAAS.equals(sysCode)) {
			this.sendDistrictSAASQueue(msg);
		}
		if (!MdmConstants.SCM.equals(sysCode)) {
			this.sendDistrictSCMQueue(msg);
		}
		if (!MdmConstants.SFYOFF.equals(sysCode)) {
			this.sendDistrictSFYOFFQueue(msg);
		}
		if (!MdmConstants.SSY.equals(sysCode)) {
			this.sendDistrictSSYQueue(msg);
		}
//		if (!MdmConstants.YB.equals(sysCode)) {
//			this.sendDistrictYBQueue(msg);
//		}
	}
	/**
	 * @Title: sendAllMerchantQueue
	 * @Description: 队列下发所有子系统商家信息
	 * @param msg
	 * @return: void
	 */
	public void sendAllMerchantQueue(String msg) {
		String sysCode = HttpContent.getSysCodeLocal();
		if (!MdmConstants.ACRM.equals(sysCode)) {
			this.sendMerchantACRMQueue(msg);
		}
		if (!MdmConstants.SCM.equals(sysCode)) {
			this.sendMerchantSCMQueue(msg);
		}
		if (!MdmConstants.SSY.equals(sysCode)) {
			this.sendMerchantSSYQueue(msg);
		}
	}
	/**
	 * @Title: sendAllMerchantQueue
	 * @Description: 队列下发所有子系统用户角色组织机构系统关系信息
	 * @param msg
	 * @return: void
	 */
	public void sendAllSourQueue(String msg) {
		String sysCode = HttpContent.getSysCodeLocal();
		if (!MdmConstants.ACRM.equals(sysCode)) {
			this.sendSourACRMQueue(msg);
		}
		if (!MdmConstants.CSS.equals(sysCode)) {
			this.sendSourCSSQueue(msg);
		}
		if (!MdmConstants.SAAS.equals(sysCode)) {
			this.sendSourSAASQueue(msg);
		}
		if (!MdmConstants.SCM.equals(sysCode)) {
			this.sendSourSCMQueue(msg);
		}
		if (!MdmConstants.SFYOFF.equals(sysCode)) {
			this.sendSourSFYOFFQueue(msg);
		}
		if (!MdmConstants.SFYON.equals(sysCode)) {
			this.sendSourSFYONQueue(msg);
		}
		if (!MdmConstants.SSY.equals(sysCode)) {
			this.sendSourSSYQueue(msg);
		}
		if (!MdmConstants.YB.equals(sysCode)) {
			this.sendSourYBQueue(msg);
		}
	}
}

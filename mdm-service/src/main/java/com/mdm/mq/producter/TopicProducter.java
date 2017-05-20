/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: TopicProducter.java 
 * @Prject: mdm-service
 * @Package: com.mdm.mq.producter 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年10月9日 下午4:04:52 
 * @version: V1.0   
 */
package com.mdm.mq.producter;

import javax.annotation.Resource;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.mdm.core.constants.DigitalConstants;

/**
 * @ClassName: TopicProducter
 * @Description: TODO
 * @author: guox005
 * @date: 2016年10月9日 下午4:04:52
 */
@Component
public class TopicProducter {
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	@Autowired
	private JmsTemplate jmsTemplate;

    @Resource(name="userTopic")
	private Topic userTopic;
    
    @Resource(name="districtTopic")
	private Topic districtTopic;
    
    @Resource(name="roleTopic")
	private Topic roleTopic;
    
    @Resource(name="organizationTopic")
	private Topic organizationTopic;
    
    @Resource(name="merchantTopic")
	private Topic merchantTopic;
    
    @Resource(name="sourTopic")
	private Topic sourTopic;

	public void sendUserTopic(String msg) {
		this.jmsTemplate.convertAndSend(this.userTopic, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendDistrictTopic(String msg) {
		this.jmsTemplate.convertAndSend(this.districtTopic, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendRoleTopic(String msg) {
		this.jmsTemplate.convertAndSend(this.roleTopic, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendOrganizationTopic(String msg) {
		this.jmsTemplate.convertAndSend(this.organizationTopic, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendMerchantTopic(String msg) {
		this.jmsTemplate.convertAndSend(this.merchantTopic, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
	public void sendSourTopic(String msg) {
		this.jmsTemplate.convertAndSend(this.sourTopic, msg, new ScheduleMessagePostProcessor(DigitalConstants.FIVE_THOUSAND));
	}
}

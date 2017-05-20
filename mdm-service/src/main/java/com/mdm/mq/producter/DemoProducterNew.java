/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DemoProducter.java 
 * @Prject: mdm-service
 * @Package: com.mdm.mq.producter 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年9月13日 下午1:33:26 
 * @version: V1.0   
 */
package com.mdm.mq.producter;

import javax.jms.Queue;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 
 * @ClassName: DemoProducter 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年9月13日 下午1:33:26  
 */
@Component
//@EnableScheduling
public class DemoProducterNew {
	
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Topic topic;

    //@Scheduled(fixedDelay=30000)//每30s执行1次
    public void send() {
       this.jmsMessagingTemplate.convertAndSend(this.topic, "topic...efefefe");

    }

    

}

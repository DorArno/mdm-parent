/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MessageConfig.java 
 * @Prject: mdm-web
 * @Package: com.mdm 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年9月29日 下午3:24:35 
 * @version: V1.0   
 */
package com.mdm;

import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 
 * @ClassName: MessageConfig 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年9月29日 下午3:24:35  
 */

@Configuration
public class MessageConfig {
	@Bean
	public Queue queue() {
		return new ActiveMQQueue("mdm.queue");
	}

	@Bean
	public Queue demoQueue() {
		return new ActiveMQQueue("mdm.demo.queue");
	}
	
	@Bean
	public Topic topic(){
		return new ActiveMQTopic("mdm.maindata.download");
	}
	@Bean
	public Queue userSSYQueue() {
		return new ActiveMQQueue("User_Download_SSY");
	}

	@Bean
	public Queue userCSSQueue() {
		return new ActiveMQQueue("User_Download_CSS");
	}

	@Bean
	public Queue userSCMQueue() {
		return new ActiveMQQueue("User_Download_SCM");
	}

	@Bean
	public Queue userSFYONQueue() {
		return new ActiveMQQueue("User_Download_SFYON");
	}

	@Bean
	public Queue userYBQueue() {
		return new ActiveMQQueue("User_Download_YB");
	}

	@Bean
	public Queue userSAASQueue() {
		return new ActiveMQQueue("User_Download_SAAS");
	}

	@Bean
	public Queue userACRMQueue() {
		return new ActiveMQQueue("User_Download_ACRM");
	}

	@Bean
	public Queue userSFYOFFQueue() {
		return new ActiveMQQueue("User_Download_SFYOFF");
	}
	
	@Bean
	public Queue districtSSYQueue() {
		return new ActiveMQQueue("District_Download_SSY");
	}

	@Bean
	public Queue districtCSSQueue() {
		return new ActiveMQQueue("District_Download_CSS");
	}

	@Bean
	public Queue districtSCMQueue() {
		return new ActiveMQQueue("District_Download_SCM");
	}
	
	@Bean
	public Queue districtYBQueue() {
		return new ActiveMQQueue("District_Download_YB");
	}

	@Bean
	public Queue districtSAASQueue() {
		return new ActiveMQQueue("District_Download_SAAS");
	}

	@Bean
	public Queue districtACRMQueue() {
		return new ActiveMQQueue("District_Download_ACRM");
	}

	@Bean
	public Queue districtSFYOFFQueue() {
		return new ActiveMQQueue("District_Download_SFYOFF");
	}

	@Bean
	public Queue roleSSYQueue() {
		return new ActiveMQQueue("Role_Download_SSY");
	}

	@Bean
	public Queue roleCSSQueue() {
		return new ActiveMQQueue("Role_Download_CSS");
	}

	@Bean
	public Queue roleSCMQueue() {
		return new ActiveMQQueue("Role_Download_SCM");
	}

	@Bean
	public Queue roleSFYONQueue() {
		return new ActiveMQQueue("Role_Download_SFYON");
	}

	@Bean
	public Queue roleYBQueue() {
		return new ActiveMQQueue("Role_Download_YB");
	}

	@Bean
	public Queue roleSAASQueue() {
		return new ActiveMQQueue("Role_Download_SAAS");
	}
	@Bean
	public Queue roleACRMQueue() {
		return new ActiveMQQueue("Role_Download_ACRM");
	}

	@Bean
	public Queue roleSFYOFFQueue() {
		return new ActiveMQQueue("Role_Download_SFYOFF");
	}

	@Bean
	public Queue sourSSYQueue() {
		return new ActiveMQQueue("Sour_Download_SSY");
	}

	@Bean
	public Queue sourCSSQueue() {
		return new ActiveMQQueue("Sour_Download_CSS");
	}

	@Bean
	public Queue sourSCMQueue() {
		return new ActiveMQQueue("Sour_Download_SCM");
	}

	@Bean
	public Queue sourSFYONQueue() {
		return new ActiveMQQueue("Sour_Download_SFYON");
	}

	@Bean
	public Queue sourYBQueue() {
		return new ActiveMQQueue("Sour_Download_YB");
	}

	@Bean
	public Queue sourSAASQueue() {
		return new ActiveMQQueue("Sour_Download_SAAS");
	}
	@Bean
	public Queue sourACRMQueue() {
		return new ActiveMQQueue("Sour_Download_ACRM");
	}

	@Bean
	public Queue sourSFYOFFQueue() {
		return new ActiveMQQueue("Sour_Download_SFYOFF");
	}

	@Bean
	public Queue organizationSSYQueue() {
		return new ActiveMQQueue("Organization_Download_SSY");
	}

	@Bean
	public Queue organizationCSSQueue() {
		return new ActiveMQQueue("Organization_Download_CSS");
	}

	@Bean
	public Queue organizationSCMQueue() {
		return new ActiveMQQueue("Organization_Download_SCM");
	}

	@Bean
	public Queue organizationSFYONQueue() {
		return new ActiveMQQueue("Organization_Download_SFYON");
	}

	@Bean
	public Queue organizationYBQueue() {
		return new ActiveMQQueue("Organization_Download_YB");
	}

	@Bean
	public Queue organizationSAASQueue() {
		return new ActiveMQQueue("Organization_Download_SAAS");
	}

	@Bean
	public Queue organizationACRMQueue() {
		return new ActiveMQQueue("Organization_Download_ACRM");
	}

	@Bean
	public Queue organizationSFYOFFQueue() {
		return new ActiveMQQueue("Organization_Download_SFYOFF");
	}
	
	@Bean
	public Queue merchantSSYQueue() {
		return new ActiveMQQueue("Merchant_Download_SSY");
	}

	@Bean
	public Queue merchantSCMQueue() {
		return new ActiveMQQueue("Merchant_Download_SCM");
	}

	@Bean
	public Queue merchantACRMQueue() {
		return new ActiveMQQueue("Merchant_Download_ACRM");
	}

	@Bean
	public Topic userTopic(){
		return new ActiveMQTopic("User_Download_Topic");
	}
	
	@Bean
	public Topic districtTopic(){
		return new ActiveMQTopic("District_Download_Topic");
	}
	
	@Bean
	public Topic roleTopic(){
		return new ActiveMQTopic("Role_Download_Topic");
	}

	@Bean
	public Topic organizationTopic(){
		return new ActiveMQTopic("Organization_Download_Topic");
	}
	
	@Bean
	public Topic merchantTopic(){
		return new ActiveMQTopic("Merchant_Download_Topic");
	}
	
	@Bean
	public Topic sourTopic(){
		return new ActiveMQTopic("Sour_Download_Topic");
	}
}

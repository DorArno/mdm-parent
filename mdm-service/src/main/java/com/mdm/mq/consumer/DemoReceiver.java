/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DemoReceiver.java 
 * @Prject: mdm-service
 * @Package: com.mdm.mq.receiver 
 * @Description: TODO
 * @author: guox005   
 * @date: 2016年9月13日 下午12:56:30 
 * @version: V1.0   
 */
package com.mdm.mq.consumer;

/** 
 * @ClassName: DemoReceiver 
 * @Description: TODO
 * @author: guox005
 * @date: 2016年9月13日 下午12:56:30  
 */
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class DemoReceiver {

    @JmsListener(destination = "mdm.demo.queue")
    public void receiveMessage(String msg) {
//        System.out.println("Received <" + msg + ">");
    }

}

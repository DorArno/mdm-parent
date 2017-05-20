/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: ServletInitializer.java 
 * @Prject: mdm-web
 * @Package: com.mdm 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年9月30日 上午11:19:49 
 * @version: V1.0   
 */
package com.mdm;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/** 
 * @ClassName: ServletInitializer 
 * @Description: TODO
 * @author: gaod003
 * @date: 2016年9月30日 上午11:19:49  
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MdmWebApplication.class);
    }
}
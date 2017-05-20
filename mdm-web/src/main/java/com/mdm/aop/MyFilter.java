/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MyFilter.java 
 * @Prject: mdm-web
 * @Package: com.mdm.aop 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年10月12日 下午11:41:23 
 * @version: V1.0   
 */
package com.mdm.aop;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.mdm.core.bean.common.AuthenticationInfo;
import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.HttpHelper;
import com.mdm.core.util.MyRequestWrapper;

/** 
 * @ClassName: MyFilter 
 * @Description: TODO
 * @author: gaod003
 * @date: 2016年10月12日 下午11:41:23  
 */
@Component("myFilter")  
public class MyFilter implements Filter{  
    /** 
     *  
     * @see javax.servlet.Filter#destroy() 
     */  
    @Override  
    public void destroy() {  
//        System.out.println("destroy...");  
    }  
    /** 
     * @param arg0 
     * @param arg1 
     * @param arg2 
     * @throws IOException 
     * @throws ServletException 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain) 
     */  
    @Override  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  throws IOException,  
                                                                    ServletException {  
//    	System.out.println("---fefesfeddddddddddddddddddd------"+myRequestWrapper.getBody());
//    	HttpContent.setBody(myRequestWrapper.getBody());
    	
    	HttpServletRequest httpRequest =new MyRequestWrapper((HttpServletRequest) request);;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        //设置操作人(对于业务子系统从请求头里取operatorId，对于MDM后台界面管理系统发起的则从session里取)
        String operatorId = httpRequest.getHeader("operatorId");
		if(!StringUtil.isEmpty(operatorId)){
			HttpContent.setOperatorId(operatorId);
		} else {
	        UserBasicsInfo user = (UserBasicsInfo) httpRequest.getSession().getAttribute("user");
	        if(user != null){
	        	HttpContent.setOperatorId(user.getId());
	        }
		}
		
		String authInfoStr =httpRequest.getHeader("authentication");
		
		if(!StringUtil.isEmpty(authInfoStr)){
        	AuthenticationInfo authInfo =JSON.parseObject(authInfoStr, AuthenticationInfo.class);
        	HttpContent.setSysCodeLocal(authInfo ==null?"":authInfo.getSysCode());
		}
        
        
        HttpContent.setRequest(httpRequest);
        
        // 设置操作人IP
        HttpContent.setOperatorIpLocal(HttpHelper.getIpAddress(httpRequest));
        
    	chain.doFilter(httpRequest, httpResponse);  
    }  
    /** 
     * @param arg0 
     * @throws ServletException 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig) 
     */  
    @Override  
    public void init(FilterConfig arg0) throws ServletException {  
//        System.out.println("filter init...");  
    }  
       
}

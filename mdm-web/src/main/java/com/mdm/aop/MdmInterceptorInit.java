/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MdmInterceptorPre.java 
 * @Prject: mdm-common
 * @Package: com.mdm.aop 
 * @Description: TODO
 * @author: Allison   
 * @date: 2017年3月16日 下午9:28:56 
 * @version: V1.0   
 */
package com.mdm.aop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.mdm.MyApplicationContextUtil;
import com.mdm.core.bean.common.AuthenticationInfo;
import com.mdm.core.bean.pojo.SystemAccessLog;
import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.bean.response.ServiceInfoResponse;
import com.mdm.core.bean.response.SystemInfoResponse;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.enums.MethodTypeEnum;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.service.sys.StaticDataService;
import com.mdm.core.service.systemaccesslog.SystemAccessLogService;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.HttpHelper;

/** 
 * @ClassName: MdmInterceptorPre 
 * @Description: 对主数据以及对外API接口
 * @author: Allison
 * @date: 2017年3月16日 下午9:28:56 
 */
@Configuration
public class MdmInterceptorInit  extends HandlerInterceptorAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(MdmInterceptorInit.class);
	
	@Override  
    public boolean preHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler) throws Exception { 
		
        //设置操作人(对于业务子系统从请求头里取operatorId，对于MDM后台界面管理系统发起的则从session里取)
        String operatorId = request.getHeader("operatorId");
		if(!StringUtil.isEmpty(operatorId)){
			HttpContent.setOperatorId(operatorId);
		} else {
	        UserBasicsInfo user = (UserBasicsInfo) request.getSession().getAttribute("user");
	        if(user != null){
	        	HttpContent.setOperatorId(user.getId());
	        }
		}
		
		String authInfoStr =request.getHeader("authentication");
		
		if(!StringUtil.isEmpty(authInfoStr)){
        	AuthenticationInfo authInfo =JSON.parseObject(authInfoStr, AuthenticationInfo.class);
        	HttpContent.setSysCodeLocal(authInfo ==null?"":authInfo.getSysCode());
		}
        
        
        HttpContent.setRequest(request);
        
        // 设置操作人IP
        HttpContent.setOperatorIpLocal(HttpHelper.getIpAddress(request));
        
		//LOGBACK 日志设置
		MDC.put("operatorId", HttpContent.getOperatorId());
		String logId =DataUtils.uuid()+"-"+request.getMethod()+"-"+request.getServletPath();
		MDC.put("logId", logId);
		HttpContent.setLogId(logId);
		
		//获取请求URL参数 
		Map<String, String> paramMap = getRequestMap(request);
		//logger.info("request url: {},{}, urlParamMap:{}", request.getRequestURL(), request.getMethod(), paramMap);
		
		//获取请求body数据
		String bodyStr = HttpHelper.getBodyString(request);
		//logger.info("request url: {},{}, requestBodyMsg:{}", request.getRequestURL(), request.getMethod(), bodyStr);
		
		String requestMsg = JSON.toJSONString(paramMap);
		if(!"GET".equals(request.getMethod())){
			requestMsg = bodyStr;
		}
		String ip = HttpHelper.getIpAddress(request);
		
		logger.info("request url: {},{}; requestMsg:{} ;IP:{}", request.getRequestURL(), request.getMethod(), requestMsg,ip);
		
		//设置线程共享参数
		Map<String, String> shareParamMap = new HashMap<String,String>();
		shareParamMap.put("startTime", String.valueOf(System.currentTimeMillis()));//开始时间
		shareParamMap.put("requestMsg",requestMsg);//请求体报文
		HttpContent.setShareParams(shareParamMap);
        return true;  
    }  
  
    @Override  
    public void postHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler,  
            ModelAndView modelAndView) throws Exception {  
    }  
  
    @Override  
    public void afterCompletion(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex)  
      { 
    	if(!MdmConstants.MDM.equals(HttpContent.getSysCode())){
    		
	    	String servletPath = request.getServletPath();
	    	Map<String,String> shareParamMap = HttpContent.getShareParams();
	    	
//	    	logger.info("==afterCompletion=== insertApiAccessLog === request url: {},{}, responseMsg:{},requestMsg:{}", request.getRequestURL(), request.getMethod(), HttpContent.getResonseMsg(), shareParamMap.get("requestMsg"));
	    	
	    	StaticDataService staticDataService = MyApplicationContextUtil.getBean(StaticDataService.class);
			String switchFlag = staticDataService.getSingleConfigParam(MdmConstants.MDM_SWITCH, MdmConstants.API_LOG_FLAG);
			if(MdmConstants.SWITCH_ON.equals(switchFlag)){//api记录日志开关打开
	    	
		    	SystemAccessLog log = new SystemAccessLog();
		    	log.setId(DataUtils.uuid());
		    	log.setServiceUrl(request.getRequestURL().toString()+","+request.getMethod());
		    	log.setRequestMsg(shareParamMap.get("requestMsg"));
		    	log.setResponseMsg(HttpContent.getResonseMsg());
		    	log.setErrorMsg(ex == null?"":ex.getMessage());
		    	log.setCreatedBy(HttpContent.getOperatorId());
		    	log.setCostTime((int)(System.currentTimeMillis() - Long.valueOf(shareParamMap.get("startTime"))));
		    	
		    	log.setSysCode(HttpContent.getSysCode());
		    	
		    	InvokeSystemService invokeSystemService = MyApplicationContextUtil.getBean(InvokeSystemService.class);
		    	if(shareParamMap.get("systemId") != null){
		    		log.setSystemId(shareParamMap.get("systemId"));
		    	} else {
		    		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(HttpContent.getSysCode());
		    		if(systemInfo !=null){
		    			log.setSystemId(systemInfo.getId());
		    		}
		    	}
		    	
		    	ServiceInfoResponse serviceInfo = invokeSystemService.queryServiceInfoByCode(DataUtils.replaceUuid(servletPath),MethodTypeEnum.getIdByValue(request.getMethod()));
		    	if(serviceInfo != null){
		    		log.setServiceId(serviceInfo.getId());
		    	}
		    	
		    	
		    	SystemAccessLogService systemAccessLogService = MyApplicationContextUtil.getBean(SystemAccessLogService.class);
		    	systemAccessLogService.insertAccessLog(log);//记录接口访问日志
		    	logger.info("request url: {},{}, requestMsg:{}", request.getRequestURL(), request.getMethod(), HttpContent.getResonseMsg());
			}
    	
    	}
    	
    	MDC.remove("operatorId");
    	MDC.remove("logId");
    }  
    
    /**
     * 
     * @Title: getRequestMap 
     * @Description: TODO
     * @param request
     * @return
     * @return: Map<String,String>
     */
    private static Map<String, String> getRequestMap(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<>();
        Map<?, ?> requestMap = request.getParameterMap();
        for (Object key : requestMap.keySet()) {
            resultMap.put(String.valueOf(key), request.getParameter(String.valueOf(key)) == null ? "" : request
                    .getParameter(String.valueOf(key)).trim());
        }
        return resultMap;
    }
    
  
}

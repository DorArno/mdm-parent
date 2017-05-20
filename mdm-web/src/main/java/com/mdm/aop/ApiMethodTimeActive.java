/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: ControllerMethodTimeActive.java 
 * @Prject: mdm-common
 * @Package: com.mdm.aop 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年9月15日 上午11:02:32 
 * @version: V1.0   
 */
package com.mdm.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** 
 * @ClassName: ApiMethodTimeActive 
 * @Description: 记录 api执行时间 && 修改类api入参字段的限制
 * @author: gaod003
 * @date: 2016年9月15日 上午11:02:32  
 */
@Aspect
@Component
public class ApiMethodTimeActive {
	
	private static Logger logger = LoggerFactory.getLogger(ApiMethodTimeActive.class);
	private final String rule = "execution(* com.mdm.controller..*.*(..)) "
//			   + "|| execution(* com.mdm.*.service..*.*(..)) "
//			   + "|| execution(* com.mdm.service..*.*(..)) "
//			   + "|| execution(* com.mdm.dao.write..*.*(..))"
//			   + "|| execution(* com.mdm.mq.producter..*.*(..))"
			;
	@Around(rule)
    public Object aroundInterface(ProceedingJoinPoint point) throws Throwable {
    	
        long start = System.currentTimeMillis();
        Object[] args = point.getArgs();
        String className = point.getTarget().getClass().getSimpleName();
        String methodName =  point.getSignature().getName();
        
//        HttpServletRequest request = HttpContent.getRequest();
//        String sysCode =HttpContent.getSysCode();
//        String servletPath =request.getServletPath();
//        
//        if(!MdmConstants.MDM.equals(sysCode) && className.endsWith("Controller") && "PUT".equals(request.getMethod()) 
//        	&& !servletPath.endsWith("/mdm") && args.length >1 && args[1].toString().indexOf("MyRequestWrapper") == -1
//        	&& args[1].toString().indexOf("CookieHttpSession") == -1){//对业务子系统发起的更新类API进行参数拦截校验
//        	
//        	logger.info("{}-before deal-:{}",request.getServletPath(),JSON.toJSONString(args[1]));
//        	
//        	//根据系统编码查询系统授权主键
//    		InvokeSystemService invokeSystemService = MyApplicationContextUtil.getBean(InvokeSystemService.class);
//    		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(sysCode);
//    		
//    		//根据服务编码获取服务主键
//    		ServiceInfoResponse serviceInfo = invokeSystemService.queryServiceInfoByCode(DataUtils.replaceUuid(servletPath),MethodTypeEnum.getIdByValue(request.getMethod()));
//    		
//    		//根据系统授权主键 服务主键 查询授权参数
//    		if(systemInfo !=null && serviceInfo !=null) {
//	    		List<String> authParamList = invokeSystemService.queryAuthParamInfoByCond(serviceInfo.getId(), systemInfo.getAuthId());
//	    		if(!authParamList.isEmpty()){
//	    			
//	    			JSONObject jsonObj = JSONObject.parseObject(JSON.toJSONString(args[1]));
//		        	Set<String>  keys = jsonObj.keySet();
//		        	Iterator<String> it =  keys.iterator();
//		        	
//		        	while (it.hasNext()) {
//		        		String key = (String)it.next();
//		        		if(!authParamList.contains(key)){//将API中没有授权给该子系统的参数设置为空（不能修改这些字段）
//		        			jsonObj.put(key, "");
//		        		}
//		        	}
//	    	        
//	    	        args[1] = JSON.parseObject(jsonObj.toJSONString(), args[1].getClass());
//	    			
//	    		}
//    		}
//    		
//    		logger.info("{}-after deal-:{}",request.getServletPath(),JSON.toJSONString(args[1]));
//    		
//        }
        
        
        Object object = point.proceed(args);
        long cost = System.currentTimeMillis() - start;
        
        if (cost > 1000) {
        	logger.info("{}.{}.{}", className,methodName, "[消耗时长:"+cost+"毫秒]");
        }
        
        return object;
    }
}

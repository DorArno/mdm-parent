/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MdmInterceptor.java 
 * @Prject: mdm-common
 * @Package: com.mdm.aop 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年9月17日 下午9:28:56 
 * @version: V1.0   
 */
package com.mdm.aop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.mdm.MyApplicationContextUtil;
import com.mdm.core.bean.common.AuthenticationInfo;
import com.mdm.core.bean.pojo.WhiteInfo;
import com.mdm.core.bean.response.ServiceInfoResponse;
import com.mdm.core.bean.response.SystemInfoResponse;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.enums.MethodTypeEnum;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.service.sys.StaticDataService;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.DateUtils;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.HttpHelper;
import com.mdm.core.util.MD5Util;
import com.mdm.core.util.MdmException;



/** 
 * @ClassName: MdmInterceptor 
 * @Description: 对主数据平台提供的api访问进行拦截授权验证
 * @author: gaod003
 * @date: 2016年9月17日 下午9:28:56  
 */
@Configuration
public class MdmInterceptor  extends HandlerInterceptorAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(MdmInterceptor.class);
	
	@Override  
    public boolean preHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler) throws Exception { 
		
		
		String bodyStr = HttpHelper.getBodyString(request);
		
		//判断拦截开关是否打开,打开则做拦截鉴权
		StaticDataService staticDataService = MyApplicationContextUtil.getBean(StaticDataService.class);
		String switchFlag = staticDataService.getSingleConfigParam(MdmConstants.MDM_SWITCH, MdmConstants.AUTHENTICATION_FLAG);
		if(switchFlag.isEmpty() || switchFlag.equals(MdmConstants.SWITCH_OFF)){//开关不存在或关闭状态无需拦截
			logger.info(MdmConstants.MDM_SWITCH +"---" +MdmConstants.AUTHENTICATION_FLAG +": [0:OFF]");
			return true;
		}
		
		String operationId = request.getHeader("operatorId");
		if("quartz-mission".equals(operationId)){
			return true;
		}
		
		//获取Header授权信息
		String authInfoStr = request.getHeader("authentication");
		logger.debug("header authentication:" +authInfoStr);
		if(StringUtil.isEmpty(authInfoStr)){
			logger.error(request.getServletPath()+": header authentication is null");
			throw new MdmException("header authentication is null");
		}
		
		//校验header授权参数
		AuthenticationInfo authInfo =JSON.parseObject(authInfoStr, AuthenticationInfo.class);
		if(StringUtil.isEmpty(authInfo.getTimeStamp())){
			logger.error(request.getServletPath()+": header timeStamp is null");
			throw new MdmException("header timeStamp is null");
		}
		
		if(StringUtil.isEmpty(authInfo.getSysCode())){
			logger.error(request.getServletPath()+": header sysCode is null");
			throw new MdmException("header sysCode is null");
		}
		
		if(MdmConstants.MDM.equals(authInfo.getSysCode())){//MDM界面发起的不进行授权拦截
			return true;
		}
		
		if(StringUtil.isEmpty(authInfo.getSign())){
			logger.error(request.getServletPath()+": header sign is null");
			throw new MdmException("header sign is null");
		}
		
		//验证时间戳是否已超过当前时间15分钟 
		Long timeStamp = DateUtils.getMsByDateStr(authInfo.getTimeStamp());
		Long currentTime = System.currentTimeMillis();
		if((currentTime -timeStamp) > 15*60*1000){
			logger.error(request.getServletPath()+": header timeStamp expires");
			throw new MdmException("header timeStamp expires");
		}
		
		//根据系统编码查询系统信息
		InvokeSystemService invokeSystemService = MyApplicationContextUtil.getBean(InvokeSystemService.class);
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(authInfo.getSysCode());
		if(systemInfo == null){//校验系统是否存在及可用
			logger.error(request.getServletPath()+": "+authInfo.getSysCode()+" not active");
			throw new MdmException(authInfo.getSysCode()+" not active");
		}
		
		ServiceInfoResponse serviceInfo = invokeSystemService.queryServiceInfoByCode(DataUtils.replaceUuid( request.getServletPath()),MethodTypeEnum.getIdByValue(request.getMethod()));
    	if(serviceInfo == null || GlobalConstants.STATUS_STOP.toString().equals(serviceInfo.getStatus())){//校验服务状态是否启用
    		logger.error(request.getServletPath()+": not active");
			throw new MdmException(request.getServletPath()+"-"+request.getMethod()+" api not active");
    	}
    	
    	if(null!=HttpContent.getShareParams()){
			HttpContent.getShareParams().put("systemId",systemInfo.getId());
		}
			
		//验证授权生失效时间
		if(DateUtils.compare_Time(authInfo.getTimeStamp(),systemInfo.getBeginDate(),DateUtils.TIMESTAMP) == -1 ||
				DateUtils.compare_Time(authInfo.getTimeStamp(),systemInfo.getEndDate(),DateUtils.TIMESTAMP) == 1){
			logger.error(request.getServletPath()+": authorization code expires");
			throw new MdmException("Authorization code expires");
		}
		
		String ip = HttpHelper.getIpAddress(request);//获取request IP
		
		//验证白名单
		List<WhiteInfo> whiteInfoList = invokeSystemService.queryWhiteInfoBySystemId(systemInfo.getId());
		if(whiteInfoList.isEmpty()){
			logger.error(request.getServletPath()+": whiteInfoList is null");
			throw new MdmException(" whiteInfoList is null");
		} else {
			boolean flag = false;
			for(WhiteInfo white : whiteInfoList){//如果是ip地址，网段，域名有一个满足条件即校验通过
				if(ip.equals(white.getIpAddress()) 
						|| request.getServerName().equals(white.getIpAddress())
						|| HttpHelper.isInRange(ip, white.getIpAddress())){
					flag = true;
					break;
				}
			}
			if(!flag){
				logger.error(request.getServletPath()+": ip["+ip+"] is not exist in whiteList");
				throw new MdmException("ip["+ip+"] is not exist in whiteList");
			}
		}
		
		//MD5加密验证sign（系统授权码+时间戳+body所有参数json串）
//		logger.info(request.getServletPath()+"==md5 content=="+systemInfo.getAuthCode()+authInfo.getTimeStamp()+bodyStr);
		String md5Sign = MD5Util.MD5(systemInfo.getAuthCode()+authInfo.getTimeStamp()+bodyStr);
//		logger.info(request.getServletPath()+"==request sign:"+authInfo.getSign()+", MDM Sign:"+md5Sign);
		if(!StringUtils.equalsIgnoreCase(md5Sign,authInfo.getSign())){
			logger.error(request.getServletPath()+": sign error,  url:{},  method:{},  body:{}, auth:{}", request.getRequestURL(), request.getMethod(), bodyStr, authInfoStr);
			throw new MdmException("sign error");
		}
		
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
    
    /**
     * 
     * @Title: setOperatorId 
     * @Description: TODO
     * @param bodyStr
     * @return: void
     */
//    private void setOperatorId(String bodyStr){
//    	
//    	if(!StringUtil.isEmpty(bodyStr)){
//			JSONObject obj = null;
//			if(bodyStr.startsWith("[")){//请求body是json数组的场景
//				JSONArray jsonarray = JSONArray.parseArray(bodyStr);	
//				if(jsonarray.size() > 0 && !String.class.equals(jsonarray.get(0).getClass())){
//					obj =  (JSONObject)jsonarray.get(0);
//				}
//			} else {
//				obj = JSON.parseObject(bodyStr);
//			}
//			if(obj !=null){
//				String operatorId= obj.get("createdBy") == null ?"":String.valueOf(obj.get("createdBy"));
//				if(!StringUtil.isEmpty(operatorId)){
//					HttpContent.setOperatorId(operatorId);//接口操作人
//				}
//			}
//		}
//    }
  
}

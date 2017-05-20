package com.mdm.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;

import net.logstash.logback.encoder.org.apache.commons.lang.exception.ExceptionUtils;

public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The action execution was successful.
     */
    public static final String SUCCESS = "success";

    /**
     * The action execution was a fail.
     */
    public static final String FAIL = "fail";

    /**
     * The Remote execution was a error
     */
    public static final String ERROR = "error";

    /**
     * 成功的Status Code
     */
    private static final int RESCODE_OK = 200;
    /**
     * 失败的Status Code
     */
    private static final int RESCODE_FAIL = 201;
    
    private static final int RESCODE_VERSION_FAIL = 202;
    
    protected HttpServletRequest request;  
    protected HttpServletResponse response;  
    protected HttpSession session;  
      
    @ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;  
        this.response = response;  
        this.session = request.getSession();  
    }


    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletRequest request) {
        logger.error(this.getRequestMap(request) + ex.getMessage());

        if (ex instanceof MdmException) {
        	MdmException me = (MdmException) ex;
        	String fullStackTrace = ExceptionUtils.getFullStackTrace(ex);
            logger.error("业务异常:{}\n", ex.getMessage(), fullStackTrace);
            if(me.getCommonData() != null){
            	return new ModelAndView(new MappingJackson2JsonView(), getResult(false, Integer.parseInt(me.getErrorCode()), me.getErrorMsg(),me.getCommonData()));
            }
            return new ModelAndView(new MappingJackson2JsonView(), getFailResult(me.getErrorMsg(), Integer.parseInt(me.getErrorCode())));
        }
        else if(ex instanceof MethodArgumentNotValidException){
        	String fullStackTrace = ExceptionUtils.getFullStackTrace(ex);
        	logger.error("参数校验异常:{}\n{}", ex.getMessage(),fullStackTrace);
        	List<ObjectError>  errorList = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
        	return new ModelAndView(new MappingJackson2JsonView(), getFailResult(errorList.get(0).getDefaultMessage(), RESCODE_FAIL));
        }
        else if(ex instanceof DuplicateKeyException){
        	DuplicateKeyException me = (DuplicateKeyException) ex;
        	if(me.getMessage().contains("Index_Operation_KeyCode")){
        		return new ModelAndView(new MappingJackson2JsonView(), getFailResult("KEYCODE存在重复", RESCODE_FAIL));
        	}
        } 
        else {
        	String fullStackTrace = ExceptionUtils.getFullStackTrace(ex);
        	logger.error("系统运行异常:{}\n{}", ex.getMessage(), fullStackTrace);
        }
        
        //其他异常
        
        return new ModelAndView(new MappingJackson2JsonView(), getFailResult("服务器错误", RESCODE_FAIL));
    }

    protected Map<String, Object> getFailResult(String msg, int code) {
        return getResult(false, code, msg, Collections.EMPTY_MAP);
    }

    protected Map<String, Object> getFailResult(String msg, String str) {
        return getResult(false, RESCODE_FAIL, msg, str);
    }

    protected Map<String, Object> getFailResult(String msg) {
        return getResult(false, RESCODE_FAIL, msg, Collections.EMPTY_MAP);
    }

    protected Map<String, Object> getResult(boolean isOk, int resCode, String errorMsg, Object obj) {
        return createJson(isOk, resCode, errorMsg, obj);
    }

    public static Map<String, Object> createJson(boolean isOk, int resCode, String errorMsg, Object obj) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("result", isOk ? "true" : "false");
        jsonMap.put("resCode", resCode);
        jsonMap.put("resMsg", errorMsg);
        jsonMap.put("data", obj);
        HttpContent.setResponseMsg(JSON.toJSONString(jsonMap));
        return jsonMap;
    }

    protected boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    protected Map<String, Object> getSuccessResult() {
        return getResult(true, RESCODE_OK, "操作成功！", Collections.EMPTY_MAP);
    }

    protected Map<String, Object> getSuccessResult(Object obj) {
        return getResult(true, RESCODE_OK, "操作成功", obj);
    }
    
    protected Map<String, Object> getResponseResult(Object obj) {
        return getResult(true, RESCODE_OK, "操作成功", obj);
    }
    
    protected Map<String, Object> getResponseResult(String key, Object obj) {
    	JSONObject object = new JSONObject();
    	object.put(key, obj);
        return getResult(true, RESCODE_OK, "操作成功", object);
    }

    protected ModelAndView getSuccessResultView(Object obj) {
        return new ModelAndView(new MappingJackson2JsonView(), this.getSuccessResult(obj));
    }


    public Map<String, String> getRequestMap(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<>();
        Map<?, ?> requestMap = request.getParameterMap();
        for (Object key : requestMap.keySet()) {
            resultMap.put(String.valueOf(key), request.getParameter(String.valueOf(key)) == null ? "" : request.getParameter(String.valueOf(key)).trim());
        }
        return resultMap;
    }

    /**
     * 提交大量数据，使用Body内的JSON字符串
     *
     * @return
     */
    public String getRequestJsonData() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        InputStream ins = null;
        StringBuffer jsonStr = new StringBuffer();
        try {
            ins = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            jsonStr.delete(0, jsonStr.length());
            String buffer = "";
            while ((buffer = br.readLine()) != null) {
                jsonStr.append(buffer);
            }
        } catch (IOException ex) {
            logger.error("参数错误", ex);
        }
        return jsonStr.toString();
    }

    protected HttpSession getSessionContext() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
    }

}

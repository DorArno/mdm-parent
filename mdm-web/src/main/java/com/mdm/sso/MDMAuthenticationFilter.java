/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: MDMSingleSignOutFilter.java 
 * @Prject: mdm-web
 * @Package: com.mdm.sso 
 * @Description: TODO
 * @author: LUPE004   
 * @date: 2016年11月16日 上午10:56:26 
 * @version: V1.0   
 */
package com.mdm.sso;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.util.StringUtils;

import com.mdm.MyApplicationContextUtil;
import com.mdm.controller.web.login.LoginController;
import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.constants.MdmConstants;
import com.mdm.service.login.LoginService;

/** 
 * @ClassName: MDMSingleSignOutFilter 
 * @Description: TODO
 * @author: LUPE004
 * @date: 2016年11月16日 上午10:56:26  
 */
public class MDMAuthenticationFilter extends AbstractCasFilter {
    /**
     * The URL to the CAS Server login.
     */
    private String casServerLoginUrl;

    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;

    /**
     * Whether to send the gateway request or not.
     */
    private boolean gateway = false;
    
    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));
            log.trace("Loaded CasServerLoginUrl parameter: " + this.casServerLoginUrl);
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
            log.trace("Loaded renew parameter: " + this.renew);
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
            log.trace("Loaded gateway parameter: " + this.gateway);

            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);

            if (gatewayStorageClass != null) {
                try {
                    this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();
                } catch (final Exception e) {
                    log.error(e,e);
                    throw new ServletException(e);
                }
            }
        }
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
    }

    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpSession session = request.getSession(false);
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;
        
        if(request.getRequestURI().contains("captchal")
        		|| request.getRequestURI().contains("CheckPhoneNum")
        		|| request.getRequestURI().contains("UserRegister")
        		|| request.getRequestURI().contains("ForgetPassword")
        		|| request.getRequestURI().contains("businessLog")
        		|| request.getRequestURI().contains("createRandomCode")
        		|| request.getRequestURI().contains("login")
        		|| request.getRequestURI().contains("CaptchaUpdateUserPassword")
        		|| request.getRequestURI().contains("verifyPassword")
        		|| request.getRequestURI().contains("UserUpdatePhone")){
        	filterChain.doFilter(request, response);
            return;
        }
        
        if(!StringUtils.isEmpty(request.getHeader("operatorId"))){
        	 filterChain.doFilter(request, response);
             return;
        }
        
       
        
        if (assertion != null) {
        	if(session.getAttribute("user")==null){
        		
        		 String ticket1 = session != null ?(String)session.getAttribute("ticket") : null;
//        		 if(!request.getRequestURI().contains("test.html") && !request.getRequestURI().contains("redirect")){
//        	        if(MDMAbstractTicketValidationFilter.copyOnWriteArrayList.contains(ticket1)){
//        	        	LoginController loginController = MyApplicationContextUtil.getBean(LoginController.class);
//        	        	String returnUrl = loginController.getSsoServerUrl() +"logout?service="+loginController.getLocalServerUrl() +"mdm/test.html";
//        	        	response.sendRedirect(returnUrl);
//        	        	return;
//        	        }
//        		 }
        		
	            AttributePrincipal principal = assertion.getPrincipal();  
	            String username = principal.getName(); 
	            LoginService loginService = MyApplicationContextUtil.getBean(LoginService.class);
	            List<UserBasicsInfo> list = loginService.loginCheck(username,null);
	    		if(list !=null && list.size() > 0){
	    			
	    			//2017-01-05 企业代码非CCPG无法登录MDM
	    			if(!MdmConstants.CCPG.equalsIgnoreCase(list.get(0).getCorpCode())){
	    				LoginController loginController = MyApplicationContextUtil.getBean(LoginController.class);
	    				if(session.getAttribute(CONST_CAS_ASSERTION) != null) session.removeAttribute(CONST_CAS_ASSERTION);
	    				response.setContentType("text/html; charset=utf-8");
	    				response.setCharacterEncoding("utf-8");
	    				//response.getWriter().print("<script>alert('非长城物业用户无法登录主数据平台！');</script>");
	    				response.getWriter().print("<script>alert('非长城物业用户无法登录主数据平台！');window.location.href='"+loginController.getSsoServerUrl() +"logout?service="+loginController.getLocalServerUrl() +"mdm/test.html" +";'</script>");
        	        	//String returnUrl = loginController.getSsoServerUrl() +"logout?service="+loginController.getLocalServerUrl() +"mdm/test.html";
        	        	//response.sendRedirect(returnUrl);
        	        	return;
	    			}
	    			else{
	    				session.setAttribute("user", list.get(0));
	    			}
	    			//session.setMaxInactiveInterval(1 * 60);
	    		}
        	}
            filterChain.doFilter(request, response);
            return;
        }

        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = CommonUtils.safeGetParameter(request,getArtifactParameterName());
        final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

        if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
            filterChain.doFilter(request, response);
            return;
        }

        final String modifiedServiceUrl;

        log.debug("no ticket and no assertion found");
        if (this.gateway) {
            log.debug("setting gateway attribute in session");
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }

        if (log.isDebugEnabled()) {
            log.debug("Constructed service url: " + modifiedServiceUrl);
        }

        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);

        if (log.isDebugEnabled()) {
            log.debug("redirecting to \"" + urlToRedirectTo + "\"");
        }

        response.sendRedirect(urlToRedirectTo);
    }

    public final void setRenew(final boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(final boolean gateway) {
        this.gateway = gateway;
    }

    public final void setCasServerLoginUrl(final String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }
    
    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
    	this.gatewayStorage = gatewayStorage;
    }
}


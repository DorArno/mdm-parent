	/**   
	 * Copyright © 2016 Arvato. All rights reserved.
	 * 
	 * @Title: InvokeSystemController.java 
	 * @Prject: mdm-web
	 * @Package: com.mdm.controller.web.invokesystem 
	 * @Description: TODO
	 * @author: gaod003   
	 * @date: 2016年9月23日 下午3:32:30 
	 * @version: V1.0   
	 */
	package com.mdm.controller.web.invokesystem;

	import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.controller.BaseController;
import com.mdm.core.bean.pojo.WhiteInfo;
import com.mdm.core.bean.request.ServiceInfoRequest;
import com.mdm.core.bean.request.SystemInfoRequest;
import com.mdm.core.bean.request.UpdateServiceInfoRequest;
import com.mdm.core.bean.request.UpdateSystemInfoRequest;
import com.mdm.core.bean.response.SystemInfoResponse;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.util.HttpHelper;
import com.mdm.core.util.MD5Util;
import com.mdm.core.util.MdmException;

	/**
	 * @ClassName: InvokeSystemController
	 * @Description: 接入系统信息管理controller
	 * @author: gaod003
	 * @param <HttpServletRquest>
	 * @date: 2016年9月23日 下午3:32:30
	 */
	// @Api("接入系统管理")
	@RestController
	@RequestMapping("/invokeSystem")
	public class InvokeSystemController extends BaseController {
		@Autowired
		InvokeSystemService invokeSystemService;
		
		private static Logger logger = LoggerFactory.getLogger(InvokeSystemController.class);

		/**
		 * @Title: querySystemInfoList
		 * @Description: TODO
		 * @param sysName
		 * @param sysCode
		 * @return
		 * @throws MdmException
		 * @return: Object
		 */
		// @ApiOperation(value="查询接入系统信息", notes="根据条件查询接入系统信息")
		@RequestMapping(value = "/systemInfo", method = RequestMethod.GET)
		public ModelAndView querySystemInfoList(@RequestParam(value = "sysName", required = false) String sysName,
				@RequestParam(value = "sysCode", required = false) String sysCode,
				@RequestParam(value = "type", required = false) String type,
				@RequestParam(value = "isDeleted", required = false) Integer isDeleted,
				@RequestParam(value = "status", required = false) Integer status,
				@RequestParam(value = "pageNum", required = true) Integer pageNum,
				@RequestParam(value = "pageSize", required = true) Integer pageSize) throws MdmException {
			return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult(
					invokeSystemService.querySystemInfoList(sysName, sysCode, type, isDeleted, status,pageNum, pageSize)));
		}
		
		@RequestMapping(value = "/serviceInfo", method = RequestMethod.GET)
		public ModelAndView queryServiceInfoList(@RequestParam(value = "sysName", required = false) String sysName,
				@RequestParam(value = "sysCode", required = false) String sysCode,
				@RequestParam(value = "status", required = false) String status,
				@RequestParam(value = "serviceType", required = false) String serviceType,
				@RequestParam(value = "pageNum", required = true) Integer pageNum,
				@RequestParam(value = "pageSize", required = true) Integer pageSize) throws MdmException {
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.queryServiceInfoList(sysName, sysCode,status,serviceType, pageNum, pageSize)));
		}
		
		@RequestMapping(value = "/querySystemNameList", method = RequestMethod.GET)
		public ModelAndView querySystemNameList() throws MdmException {
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.querySystemNames()));
		}
		
		/**
		 * @Title: queryAllSystemInfo
		 * @Description: TODO
		 * @return
		 * @throws MdmException
		 * @return: ModelAndView
		 */
		@RequestMapping(value = "/systemInfos", method = RequestMethod.GET)
		public ModelAndView queryAllSystemInfo(@RequestParam(value = "type", required = false) String type) throws MdmException {
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.queryAllSystemInfoByOrgType(type)));
		}
		
		/**
		 * @Title: addSystemInfo
		 * @Description: TODO
		 * @param systemInfoRequest
		 * @return
		 * @return: Object
		 * @throws MdmException 
		 */
		@RequestMapping(value = "/systemInfo", method = RequestMethod.POST)
		public ModelAndView addSystemInfo(@RequestBody @Valid SystemInfoRequest systemInfoRequest) throws MdmException {
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.addSystem(systemInfoRequest)));
		}
		
		@RequestMapping(value = "/serviceInfo", method = RequestMethod.POST)
		public ModelAndView addServiceInfo(@RequestBody @Valid ServiceInfoRequest serviceInfoRequset) throws MdmException {
			
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.addService(serviceInfoRequset)));
		}
		
		/**
		 * @Title: addSystemInfo
		 * @Description: TODO
		 * @param systemInfoRequest
		 * @return
		 * @throws MdmException
		 * @return: Object
		 */
		@RequestMapping(value = "/systemInfo/{id}", method = RequestMethod.PUT)
		public ModelAndView updateSystemInfo(@PathVariable String id,
				@RequestBody  @Valid UpdateSystemInfoRequest systemInfoRequest) throws MdmException {
			systemInfoRequest.setId(id);
			return new ModelAndView(new MappingJackson2JsonView(),
				this.getResponseResult(invokeSystemService.updateSystemInfo(systemInfoRequest)));
		}
		
		/**
		 * 
		 * @Title: updateStatus 
		 * @Description: TODO
		 * @param id
		 * @param systemInfoRequest
		 * @return
		 * @throws MdmException
		 * @return: ModelAndView
		 */
		@RequestMapping(value = "/systemInfo/{id}/status", method = RequestMethod.PUT)
		public ModelAndView updateStatus(@PathVariable String id,
				@RequestBody UpdateSystemInfoRequest systemInfoRequest) throws MdmException {
			
				return new ModelAndView(new MappingJackson2JsonView(),
						this.getResponseResult(invokeSystemService.updateSystemInfo(id,systemInfoRequest.getStatus())));
		}
		
		/**
		 * 
		 * @Title: deleteSystemInfo 
		 * @Description: TODO
		 * @param id
		 * @return
		 * @throws MdmException
		 * @return: ModelAndView
		 */
		@RequestMapping(value = "/systemInfo/{id}", method = RequestMethod.DELETE)
		public ModelAndView deleteSystemInfo(@PathVariable String id) throws MdmException {
			
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.deleteSystemInfo(id)));
		}
		
		/**
		 * @Title: updateServiceInfo
		 * @Description: TODO
		 * @param serviceInfoRequest
		 * @return
		 * @throws MdmException
		 * @return: Object
		 */
		@RequestMapping(value = "/serviceInfo/{id}", method = RequestMethod.PUT)
		public ModelAndView updateServiceInfo(@PathVariable String id,
				@RequestBody UpdateServiceInfoRequest serviceInfoRequest) throws MdmException {
			serviceInfoRequest.setId(id);
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.updateServiceInfo(serviceInfoRequest)));
		}
//		/**
//		 * @author zhaoyao
//		 * 
//		 */
//		@RequestMapping(value = "/getSysNamesByServiceCode", method = RequestMethod.GET)
//		public ModelAndView getSysNamesByServiceCode(@RequestBody UpdateServiceInfoRequest serviceInfoRequest) throws MdmException{
//			
//			return new ModelAndView(new MappingJackson2JsonView(),
//					this.getResponseResult(invokeSystemService.getSysNamesByServiceCode(serviceInfoRequest.getServiceCode())));
//		}
		/**
		 * 查询系统白名单信息列表
		 */
		@RequestMapping(value = "/systemInfo/{id}/whiteInfo", method = RequestMethod.GET)
		public ModelAndView queryWhiteInfoList(@PathVariable String id) throws MdmException {
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.queryWhiteInfoBySystemId(id)));
		}
		
		/**
		 * @Title: addSystemInfo
		 * @Description: TODO
		 * @param systemInfoRequest
		 * @return
		 * @throws MdmException
		 * @return: ModelAndView
		 */
		@RequestMapping(value = "/systemInfo/{id}/whiteInfo", method = RequestMethod.POST)
		public ModelAndView addWhiteInfo(@PathVariable String id, @RequestBody WhiteInfo whiteInfo) throws MdmException {
			whiteInfo.setSystemId(id);
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.addWhiteInfo(whiteInfo)));
		}
		
		/**
		 * @Title: deleteWhiteInfo
		 * @Description: TODO
		 * @param id
		 * @return
		 * @throws MdmException
		 * @return: ModelAndView
		 */
		@RequestMapping(value = "/systemInfo/whiteInfo/{id}", method = RequestMethod.PUT)
		public ModelAndView deleteWhiteInfo(@PathVariable String id) throws MdmException {
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.deleteWhiteInfo(id)));
		}
		
		@RequestMapping(value = "/systemInfo/code", method = RequestMethod.POST)
		public ModelAndView getSign(@RequestParam(value = "timeStamp", required = true) String timeStamp)
				throws MdmException {
			SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode("MDM");
			String content = systemInfo.getAuthCode() + timeStamp + HttpHelper.getBodyString(request);
			logger.info("===getSign ===" + content);
			return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult(MD5Util.MD5(content)));
		}
		
		@RequestMapping(value = "/deleteServiceInfo/{id}", method = RequestMethod.PUT)
		public ModelAndView deleteServiceInfo(@PathVariable String id) throws MdmException {
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.deleteServiceInfo(id)));
		}
		
		/**
		 * @author zhaoyao
		 * 
		 */
		@RequestMapping(value = "/getSysNamesByServiceCode", method = RequestMethod.GET)
		public ModelAndView getSysNamesByServiceCode(@RequestParam(value = "id", required = false) String id) throws MdmException{
			
			return new ModelAndView(new MappingJackson2JsonView(),
					this.getResponseResult(invokeSystemService.getSysNamesByServiceCode(id)));
		}
		
		//=================================供测试用==========================================================
		@RequestMapping(value = "/systemInfo/sign", method = RequestMethod.GET)
		public ModelAndView getTestSign(@RequestParam(value = "timeStamp", required = true) String timeStamp,
				@RequestParam(value = "sysCode", required = true) String sysCode,
				@RequestParam(value = "bodyStr", required = true) String bodyStr)
				throws MdmException {
			SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(sysCode);
			String content = systemInfo.getAuthCode() + timeStamp +bodyStr;
			logger.info("=============GET:/systemInfo/sign == Test:getSignContent ===" + content);
			return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult(MD5Util.MD5(content)));
		}
		
		@RequestMapping(value = "/systemInfo/sign", method = RequestMethod.POST)
		public ModelAndView getTestSign(@RequestParam(value = "timeStamp", required = true) String timeStamp,
				@RequestParam(value = "sysCode", required = true) String sysCode)
				throws MdmException {
			SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(sysCode);
			String content = systemInfo.getAuthCode() + timeStamp + HttpHelper.getBodyString(request);
			logger.info("=============POST:/systemInfo/sign == Test:getSignContent ===" + content);
			return new ModelAndView(new MappingJackson2JsonView(), this.getResponseResult(MD5Util.MD5(content)));
		}
	}


		
package com.mdm.controller.web.role;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.util.StringUtil;
import com.mdm.controller.BaseController;
import com.mdm.controller.web.module.ModuleOperationTreeBuilder;
import com.mdm.core.bean.common.PageResultBean;
import com.mdm.core.bean.pojo.StaticData;
import com.mdm.core.bean.pojo.UserBasicsInfo;
import com.mdm.core.bean.response.SystemInfoResponse;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.service.sys.StaticDataService;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;
import com.mdm.pojo.Merchant;
import com.mdm.pojo.Organazation;
import com.mdm.pojo.Role;
import com.mdm.pojo.RoleBo;
import com.mdm.pojo.RoleList;
import com.mdm.pojo.RoleModule;
import com.mdm.pojo.RoleOperation;
import com.mdm.pojo.UserOrganizationRole;
import com.mdm.response.CommonPojoResponse;
import com.mdm.service.dataextend.DataExtendService;
import com.mdm.service.merchant.MerchantService;
import com.mdm.service.organazation.OrganazationService;
import com.mdm.service.role.RoleService;
import com.mdm.service.rolemodule.RoleModuleService;
import com.mdm.service.roleoperation.RoleOperationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("角色管理")
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService service;

	@Autowired
	private RoleModuleService rmService;

	@Autowired
	private RoleOperationService roService;

	@Autowired
	private InvokeSystemService invokeSystemService;

	@Autowired
	private DataExtendService exService;

	@Autowired
	private OrganazationService organazationService;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private StaticDataService staticDataService;

	/**
	 * 查询同一个子系统是否只存在一个角度名称
	 * @Title: queryRoleNameExist
	 * @Description: TODO
	 * @return
	 * @return: Object
	 */
	@RequestMapping(value = "/queryRoleNameExist", method = RequestMethod.POST)
	public Object queryRoleNameExist(@RequestBody Role role) {
		String name = role.getName();
		String systemid = role.getSource();
		return this.getResponseResult(service.queryRoleNameExist(name, systemid));
	}

	@ApiOperation(value = "查询角色信息")
	@RequestMapping(value = "/roleInfos", method = RequestMethod.GET)
	public Object queryRoleInfoList(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status, @RequestParam(value = "systemId", required = false) String systemId,
			@RequestParam(value = "code", required = false) String code, @RequestParam(value = "corpCode", required = false) String corpCode,
			@RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageNum", required = false) int pageNum, @RequestParam(value = "pageSize", required = false) int pageSize,
			@RequestParam(value="userPage", required = false) String userPage)
			throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("corpCode", corpCode);
		map.put("name", name);
		map.put("status", status);
		map.put("systemId", systemId);
		map.put("userPage", userPage);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.isNotBlank(startDate)) {
			map.put("startDate", df.parse(startDate));
		}
		if (StringUtils.isNotBlank(endDate)) {
			map.put("endDate", df.parse(endDate));
		}
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		return this.getResponseResult(service.queryPageList(map, pageNum, pageSize));
	}

	private void validate(Role role) throws MdmException {
		if (role.getName().length() > 50) {
			throw new MdmException("角色名称不能超过50个字符");
		}
		if (StringUtils.isNotBlank(role.getDescription()) && role.getDescription().length() > 200) {
			throw new MdmException("描述不能超过200个字符");
		}
	}

	private Role getMinLevelRoleByUserId() {
		UserBasicsInfo user = (UserBasicsInfo) session.getAttribute("user");
		Map<String, Object> map = new HashMap<>();
		map.put("userId", user.getId());
		map.put("status", GlobalConstants.STATUS_START);
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		return service.getMinLevelRoleByUserId(map);
	}

	private void checkLevel(int destRoleLevel, String errorMsg) throws MdmException {
		Role minLevelRole = getMinLevelRoleByUserId();
		checkLevel(minLevelRole.getLevel(), destRoleLevel, errorMsg);
	}

	private void checkLevel(int minRoleLevel, int destRoleLevel, String errorMsg) throws MdmException {
		if (minRoleLevel >= destRoleLevel) {
			throw new MdmException(String.format(errorMsg, minRoleLevel, minRoleLevel));
		}
	}

	@ApiOperation(value = "新增角色信息")
	@RequestMapping(value = "/roleInfos", method = RequestMethod.POST)
	public Object addRoleInfo(@RequestBody Role role) throws MdmException {
		validate(role);
		checkLevel(role.getLevel(), "当前用户最大角色级别为[%d]，新增角色的级别必须大于[%d]");
		role.setId(DataUtils.uuid());
		role.setCreatedOn(new Date());
		role.setCreatedBy(HttpContent.getOperatorId());
		role.setVersion(String.valueOf(System.currentTimeMillis()));
		// 生成编码
		role.setCode(DataUtils.generateRoleCode());
		return this.getResponseResult(service.insert(role));
	}

	private int updateRole(Role role) throws MdmException {
		role.setModifiedOn(new Date());
		role.setModifiedBy(HttpContent.getOperatorId());
		role.setVersion(String.valueOf(System.currentTimeMillis()));
		return service.update(role);
	}

	@ApiOperation(value = "更新角色信息")
	@RequestMapping(value = "/roleInfos/{id}", method = RequestMethod.PUT)
	public Object updateRoleInfo(@PathVariable String id, @RequestBody Role role, @RequestParam(value = "level", required = true) Integer level)
			throws MdmException {
		validate(role);
		Role minLevelRole = getMinLevelRoleByUserId();
		// 比较当前用户角色级别和角色原级别
		checkLevel(minLevelRole.getLevel(), level, "当前用户最大角色级别为[%d]，不允许修改级别小于等于[%d]的角色");
		// 比较修改后的角色级别
		checkLevel(minLevelRole.getLevel(), role.getLevel(), "当前用户最大角色级别为[%d]，修改角色的级别必须大于[%d]");
		return this.getResponseResult(updateRole(role));
	}

	@ApiOperation(value = "更新角色状态")
	@RequestMapping(value = "/updateRoleStatus/{id}", method = RequestMethod.PUT)
	public Object updateRoleStatus(@PathVariable String id, @RequestBody Role role) throws MdmException {
		// 比较当前用户角色级别和角色原级别
		checkLevel(role.getLevel(), "当前用户最大角色级别为[%d]，不允许修改级别小于等于[%d]的角色");
		return this.getResponseResult(updateRole(role));
	}

	private void batchCheckLevel(List<Role> list, String errorMsg) throws MdmException {
		Role minLevelRole = getMinLevelRoleByUserId();
		for (Role role : list) {
			// 比较当前用户角色级别和角色原级别
			checkLevel(minLevelRole.getLevel(), role.getLevel(), errorMsg);
		}
	}

	@ApiOperation(value = "更新角色状态")
	@RequestMapping(value = "/batchUpdateRoleStatus", method = RequestMethod.PUT)
	@Transactional
	public Object batchUpdateRoleStatus(@RequestBody List<Role> list) throws MdmException {
		batchCheckLevel(list, "当前用户最大角色级别为[%d]，不允许修改级别小于等于[%d]的角色");
		int result = 0;
		for (Role role : list) {
			result += updateRole(role);
		}
		return this.getResponseResult(result);
	}

	@ApiOperation(value = "更新角色状态")
	@RequestMapping(value = "/batchDeleteRole", method = RequestMethod.PUT)
	@Transactional
	public Object batchDeleteRole(@RequestBody List<Role> list) throws MdmException {
		batchCheckLevel(list, "当前用户最大角色级别为[%d]，不允许删除级别小于等于[%d]的角色");
		int result = 0;
		for (Role role : list) {
			result += deleteRole(role.getId());
		}
		return this.getResponseResult(result);
	}

	@ApiOperation(value = "删除角色信息")
	@RequestMapping(value = "/roleInfos/{id}", method = RequestMethod.DELETE)
	public Object deleteRoleInfo(@PathVariable String id, @RequestParam(value = "level", required = true) Integer level) throws MdmException {
		checkLevel(level, "当前用户最大角色级别为[%d]，不允许删除级别小于等于[%d]的角色");
		return this.getResponseResult(deleteRole(id));
	}

	private int deleteRole(String id) {
		logger.info("执行删除角色操作");
		Role role = service.getById(id);
		logger.info("查询到的角色信息 " + role.toString());
		role.setId(id);
		role.setModifiedBy(HttpContent.getOperatorId());
		role.setModifiedOn(new Date());
		role.setVersion(String.valueOf(System.currentTimeMillis()));
		role.setIsDeleted(GlobalConstants.IS_DELETED.intValue());
		logger.info("执行删除的角色信息 ");
		int result = service.deleteById(role);
//		// 删除角色菜单关联表
//		RoleModule rm = new RoleModule();
//		rm.setRoleId(id);
//		rm.setIsDeleted(GlobalConstants.IS_DELETED);
//		rm.setModifiedBy(HttpContent.getOperatorId());
//		rm.setModifiedOn(new Date());
//		result += rmService.deleteByRoleId(rm);
//		// 删除角色操作关联表
//		RoleOperation ro = new RoleOperation();
//		ro.setRoleId(id);
//		ro.setIsDeleted(GlobalConstants.IS_DELETED);
//		ro.setModifiedBy(HttpContent.getOperatorId());
//		ro.setModifiedOn(new Date());
//		result += roService.deleteByRoleId(ro);
//		// 删除用户角色关联表
//		result += service.deleteUserRoleByRoleId(role);
		return result;
	}

	@ApiOperation(value = "设置角色权限信息")
	@RequestMapping(value = "/setOperation/{id}", method = RequestMethod.POST)
	@Transactional
	public Object setOperation(@PathVariable String id, @RequestBody List<Map<String, String>> list) {
		int result = 0;
		List<RoleModule> rmList = new ArrayList<>();
		List<RoleOperation> roList = new ArrayList<>();
		for (Map<String, String> map : list) {
			if (StringUtils.equals(ModuleOperationTreeBuilder.NODE_TYPE_MODULE, map.get("type"))) {
				RoleModule rm = new RoleModule();
				rm.setId(DataUtils.uuid());
				rm.setRoleId(id);
				rm.setModuleId(map.get("id"));
				rm.setCreatedOn(new Date());
				rm.setCreatedBy(HttpContent.getOperatorId());
				rmList.add(rm);
			} else {
				RoleOperation ro = new RoleOperation();
				ro.setId(DataUtils.uuid());
				ro.setRoleId(id);
				ro.setOperationId(map.get("id"));
				ro.setActionId(map.get("parentId"));
				ro.setCreatedOn(new Date());
				ro.setCreatedBy(HttpContent.getOperatorId());
				roList.add(ro);
			}
		}
		RoleModule rm = new RoleModule();
		rm.setRoleId(id);
		rm.setIsDeleted(GlobalConstants.IS_DELETED);
		rm.setModifiedBy(HttpContent.getOperatorId());
		rm.setModifiedOn(new Date());
		result += rmService.deleteByRoleId(rm);
		if (CollectionUtils.isNotEmpty(rmList)) {
			result += rmService.insert(rmList);
		}
		RoleOperation ro = new RoleOperation();
		ro.setRoleId(id);
		ro.setIsDeleted(GlobalConstants.IS_DELETED);
		ro.setModifiedBy(HttpContent.getOperatorId());
		ro.setModifiedOn(new Date());
		result += roService.deleteByRoleId(ro);
		if (CollectionUtils.isNotEmpty(roList)) {
			result += roService.insert(roList);
		}
		return this.getResponseResult(result);
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "查询角色对应的用户信息")
	@RequestMapping(value = "/userRoleInfos/{id}", method = RequestMethod.GET)
	public Object queryUserRoleInfoList(@PathVariable String id, @RequestParam(value = "pageNum", required = false) int pageNum,
			@RequestParam(value = "pageSize", required = false) int pageSize, @RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "cellPhone", required = false) String cellPhone, @RequestParam(value = "sex", required = false) String sex) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", id);
		map.put("username", username);
		map.put("cellPhone", cellPhone);
		map.put("sex", sex);
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		
		List<UserOrganizationRole> useridList = service.queryUserIdByRoleId(map);
		final StringBuffer sb = new StringBuffer();
		if(useridList == null || useridList.size() == 0){
			PageResultBean pageResult = new PageResultBean();
			return this.getResponseResult(pageResult);
		}
		else{
			useridList.forEach(k->sb.append("'"+k.getUserId() + "',"));
		}
		sb.setLength(sb.length() - 1);
		map.put("userid", sb.toString());
		PageResultBean page = service.queryUserRoleByRoleId(pageNum, pageSize, map);
		List<UserOrganizationRole> list = (List<UserOrganizationRole>) page.getList();
		
		Optional.ofNullable(list).ifPresent(k->{
			List<String> organizationIdList = new ArrayList<>();
			List<String> merchantIdList = new ArrayList<>();
			k.forEach(e->{
				Optional.ofNullable(e.getOrganizationId()).ifPresent(z->{
					if(null == e.getType()){
						organizationIdList.add(z);
						merchantIdList.add(z);
					}else{
						if (e.getType() == 0) {
							organizationIdList.add(z);
						} else if (e.getType() == 1) {
							merchantIdList.add(z);
						}
					}
				});
			});
			
			Map<String, String> nameMap = new HashMap<>();
			Optional.ofNullable(organizationIdList).ifPresent(t->{
				List<Organazation> tmporganizationList = organazationService.batchGetByIds(t);
				tmporganizationList.forEach(org->nameMap.put(org.getId(), org.getName()+"(组织)"));
				});
			Optional.ofNullable(merchantIdList).ifPresent(t->{
				List<Merchant> tmpmerchantList = merchantService.batchQueryMerchantListById(t);
				tmpmerchantList.forEach(org->nameMap.put(org.getId(), org.getmName()+"商家"));
			});
			list.forEach(org->org.setDepartmentName(nameMap.get(org.getOrganizationId())));
		});
		
//		if (CollectionUtils.isNotEmpty(list)) {
//			List<String> organizationIdList = new ArrayList<>();
//			List<String> merchantIdList = new ArrayList<>();
//			
//			
//			for (UserOrganizationRole userRole : list) {
//				if (StringUtils.isNotBlank(userRole.getOrganizationId())) {
//					
//					if(null == userRole.getType()){
//						organizationIdList.add(userRole.getOrganizationId());
//						merchantIdList.add(userRole.getOrganizationId());
//					}else{
//						if (userRole.getType() == 0) {
//							organizationIdList.add(userRole.getOrganizationId());
//						} else if (userRole.getType() == 1) {
//							merchantIdList.add(userRole.getOrganizationId());
//						}
//					}
//					
//				}
//			}
//			
//			Map<String, String> nameMap = new HashMap<>();
//			Optional.ofNullable(organizationIdList).ifPresent(t->{
//				List<Organazation> organizationList = organazationService.batchGetByIds(t);
//				organizationList.forEach(k->nameMap.put(k.getId(), k.getName()+"(组织)"));
//				});
//			Optional.ofNullable(merchantIdList).ifPresent(t->{
//				List<Merchant> merchantList = merchantService.batchQueryMerchantListById(t);
//				merchantList.forEach(k->nameMap.put(k.getId(), k.getmName()+"商家"));
//			});
//			list.forEach(k->k.setDepartmentName(nameMap.get(k.getOrganizationId())));
//		}
		
	List<UserOrganizationRole> tmp = (List<UserOrganizationRole>) page.getList();
		HashMap<String,UserOrganizationRole> tmpHash = new HashMap<>();
		tmp.forEach(k->tmpHash.put(k.getUser().getCellPhone(), k));
		tmp.clear();
		tmp.addAll(tmpHash.values());
		return this.getResponseResult(page);
	}

	private Object buildResponse(Role role) {
		Map<String, String> map = new HashMap<>();
		map.put("id", role.getId());
		map.put("version", role.getVersion());
		return this.getResponseResult(map);
	}

	@ApiOperation(value = "新增角色api")
	@RequestMapping(value = "/uploadAddRole", method = RequestMethod.POST)
	@Transactional
	public Object uploadAddRole(@RequestBody Role role) {
		if (StringUtils.isBlank(role.getName())) {
			return getFailResult("名称不能为空");
		}
		role.setCreatedBy(HttpContent.getOperatorId());
		if (role.getLevel() == null) {
			return getFailResult("级别不能为空");
		}
		if (StringUtils.isBlank(role.getSource())) {
			return getFailResult("来源系统不能为空");
		}
		if (role.getCreatedOn() == null) {
			return getFailResult("创建时间不能为空");
		}
		if (StringUtils.isBlank(role.getCreatedBy())) {
			return getFailResult("创建人不能为空");
		}
		
		//2017-04-13修改解决bug编号CVAFJGJDA-2439
		if(service.queryRoleNameExist(role.getName(),role.getSource()) > 0){
			return getFailResult("DB中已存在同名数据！");
		}
		
		role.setStatus(GlobalConstants.STATUS_START);
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(role.getSource());
		if (systemInfo == null) {
			return getFailResult(String.format("根据系统来源编码[{%}]无法匹配来源系统", role.getSource()));
		}
		role.setId(DataUtils.uuid());
		role.setVersion(String.valueOf(System.currentTimeMillis()));
		role.setSystemId(systemInfo.getId());
		try {
			service.insert(role);
		} catch (MdmException e) {
			return getFailResult("物业云 或 收费云 企业代码不能为空");
		}
		exService.insertAllExts(role);
		return buildResponse(role);
	}
	
	
	@ApiOperation(value = "更新角色api")
	@RequestMapping(value = "/uploadUpdateRole/{id}", method = RequestMethod.PUT)
	@Transactional
	public Object uploadUpdateRole(@PathVariable String id, @RequestBody Role role) {
		role.setId(id);
		role.setModifiedBy(HttpContent.getOperatorId());
		if (StringUtils.isBlank(role.getName())) {
			return getFailResult("名称不能为空");
		}
		if (role.getLevel() == null) {
			return getFailResult("级别不能为空");
		}
		if (StringUtils.isBlank(role.getSource())) {
			return getFailResult("来源系统不能为空");
		}
		if (role.getModifiedOn() == null) {
			return getFailResult("修改时间不能为空");
		}
		if (StringUtils.isBlank(role.getModifiedBy())) {
			return getFailResult("修改人不能为空");
		}
		Role dbrole = service.getById(id);
		
		if(null == dbrole){
			return getFailResult("数据库不存在此角色记录");
		}
		if(StringUtils.isBlank(role.getVersion())){
			return getFailResult("传入的版本号为空");
			
		}
		String oldVersion = dbrole.getVersion();
		if(!StringUtils.isBlank(oldVersion)){
			if(!Long.valueOf(oldVersion).equals(Long.valueOf(role.getVersion()))){
				return getFailResult("当前传入版本号与主数据版本号不一致!");
			}
		}
		//社商云没有CORPCODE字段，当CORPCODE字段为空时，不更新  modify by lupeng 2017-02-13
		if(StringUtils.isEmpty(role.getCorpCode())){
			role.setCorpCode(dbrole.getCorpCode());
		}
		role.setVersion(String.valueOf(System.currentTimeMillis()));
		SystemInfoResponse systemInfo = invokeSystemService.querySystemInfoByCode(role.getSource());
		if (systemInfo == null) {
			return getFailResult(String.format("根据系统来源编码[{%}]无法匹配来源系统", role.getSource()));
		}
		role.setStatus(GlobalConstants.STATUS_START);
		role.setSystemId(systemInfo.getId());
		try {
			service.update(role);
		} catch (MdmException e) {
			return getFailResult("物业云 或 收费云 企业代码不能为空");
		}
		exService.insertAllExts(role);
		return buildResponse(role);
	}

	@ApiOperation(value = "删除角色api")
	@RequestMapping(value = "/uploadDeleteRole/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Object uploadDeleteRole(@PathVariable String id, @RequestParam(value = "modifyBy", required = false) String modifyBy) {
		return buildResponse(uploadDelete(id, modifyBy));
	}

	public Role uploadDelete(String id, String modifyBy) {
		Role role = service.getById(id);
		role.setId(id);
		role.setModifiedBy(HttpContent.getOperatorId());
		role.setModifiedOn(new Date());
		role.setVersion(String.valueOf(System.currentTimeMillis()));
		role.setIsDeleted(GlobalConstants.IS_DELETED.intValue());
		service.deleteById(role);
		exService.deteleByDataid(id);
		return role;
	}

	@ApiOperation(value = "批量删除角色api")
	@RequestMapping(value = "/batchuploadDeleteRole", method = RequestMethod.PUT)
	@Transactional
	public Object batchUploadDeleteRole(@RequestBody RoleList ids) {
		List<CommonPojoResponse> list = new ArrayList<CommonPojoResponse>();
		for (String id : ids.getIds()) {
			Role role = uploadDelete(id, "");
			CommonPojoResponse commonPojoResponse = new CommonPojoResponse();
			commonPojoResponse.setId(role.getId());
			commonPojoResponse.setVersion(role.getVersion());
			list.add(commonPojoResponse);
		}
		return this.getResponseResult(list);
	}

	@ApiOperation(value = "查询角色信息api")
	@RequestMapping(value = "/queryRoleList", method = RequestMethod.GET)
	public Object queryRoleList(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageNum", required = false) int pageNum, @RequestParam(value = "pageSize", required = false) int pageSize)
			throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.isNotBlank(startDate)) {
			map.put("startDate", df.parse(startDate));
		}
		if (StringUtils.isNotBlank(endDate)) {
			map.put("endDate", df.parse(endDate));
		}
		map.put("isDeleted", GlobalConstants.IS_NOT_DELETED);
		com.mdm.common.PageResultBean pageresultBean = service.queryPageList(map, pageNum, pageSize);
		List<Role> list = (List<Role>) pageresultBean.getList();
		exService.queryExtsByCommonPojoList(list);
		return this.getResponseResult(pageresultBean);
	}

	@ApiOperation(value = "查询角色等级")
	@RequestMapping(value = "/queryRoleLevel", method = RequestMethod.GET)
	public Object queryRoleLevel() {
		List<StaticData> list = staticDataService.queryStaticDataList("RoleLevel", null);
		Role role = getMinLevelRoleByUserId();
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(list.get(i).getColName()) < role.getLevel()) {
				list.remove(list.get(i));
			}
		}
		return this.getResponseResult(list);
	}

	@ApiOperation(value = "查询角色信息api")
	@RequestMapping(value = "/queryRole/{id}", method = RequestMethod.GET)
	public Object queryRole(@PathVariable String id) {
		Role role = service.getById(id);
		if(role == null){
			return getFailResult("当前数据库中不存在此记录，请刷新后重试!");
		}
		//if (role != null) { 
		role.setSourceSystemId(role.getSystemId());
		//}
		exService.queryExtsByCommonPojo(role);
		RoleBo rolebo = new RoleBo();
		BeanUtils.copyProperties(role, rolebo);
		return this.getResponseResult(role);
	}
	

}

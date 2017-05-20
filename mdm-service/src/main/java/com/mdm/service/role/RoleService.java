package com.mdm.service.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mdm.common.CommonPojo;
import com.mdm.core.bean.common.PageResultBean;
import com.mdm.core.enums.CommonEnum;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.enums.LogTypeEnum;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.util.MdmException;
import com.mdm.dao.write.role.RoleWriteMapper;
import com.mdm.dao.write.userrole.UserroleMapper;
import com.mdm.mq.producter.MQSenderAdapter;
import com.mdm.pojo.Role;
import com.mdm.pojo.UserOrganizationRole;
import com.mdm.service.BaseService;
import com.mdm.service.dataextend.DataExtendService;

@Service
public class RoleService extends BaseService<Role> {

	private static Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleWriteMapper mapper;

	@Autowired
	private UserroleMapper userRoleMapper;

	@Autowired
	private MQSenderAdapter mqSenderAdapter;

	@Autowired
	private DataExtendService exService;

	@Autowired
	private InvokeSystemService invokeSystemService;

	public int queryRoleNameExist(String roleName, String systemid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", roleName);
		map.put("id", systemid);
		String systemCode = invokeSystemService.querySysCodeById(systemid).getSysCode();
		// 物业云可以有重复数据。
		if (systemCode.equals("BMS") || systemCode.equals("CSS")) {
			return 0;
		}
		return mapper.queryRoleNameExist(map);
	}

	@Transactional
	public int insert(Role dmo) throws MdmException {
		// 角色不需要状态，下发时status不能为空，默认值为1
		byte status = 1;
		dmo.setIsDeleted(CommonEnum.ISNOTDELETE.getValue());
		dmo.setStatus(status);
		String systemCode = querySysCode(dmo.getSystemId());
		if (systemCode.equalsIgnoreCase("BMS") || systemCode.equalsIgnoreCase("CSS") || systemCode.equalsIgnoreCase("SFYON")
				|| systemCode.equalsIgnoreCase("SFYOFF")) {
			if(StringUtils.isEmpty(dmo.getCorpCode())){
				throw new MdmException("物业云 或 收费云 企业代码不能为空!");
			}
		}
		dmo.setSystemCode(systemCode);
		int result = mapper.insert(dmo);
		mqSenderAdapter.sendMQ(LogTypeEnum.ADD.getValue(), DataTypeEnum.ROLE.getValue(), dmo);
		return result;
	}

	private String querySysCode(String sysCode) {
		String systemCode = invokeSystemService.querySysCodeById(sysCode).getSysCode();
		return systemCode;
	}

	@Transactional
	public int update(Role dmo) throws MdmException {
		// 角色不需要状态，下发时status不能为空，默认值为1
		byte status = 1;
		dmo.setIsDeleted(CommonEnum.ISNOTDELETE.getValue());
		dmo.setStatus(status);
		String systemCode = querySysCode(dmo.getSystemId());
		if (systemCode.equalsIgnoreCase("BMS") || systemCode.equalsIgnoreCase("CSS") || systemCode.equalsIgnoreCase("SFYON")
				|| systemCode.equalsIgnoreCase("SFYOFF")) {
			if(StringUtils.isEmpty(dmo.getCorpCode())){
				throw new MdmException("物业云 或 收费云 企业代码不能为空!");
			}
		}
		dmo.setSystemCode(systemCode);
		int result = mapper.update(dmo);
		mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.ROLE.getValue(), dmo);
		return result;
	}

	@Transactional
	public int deleteById(Role role) {
		logger.info("删除角色服务 " + role.toString());
		int result = mapper.deleteById(role);
		CommonPojo commonPojo = new Role();
		commonPojo.setId(role.getId());
		role.setSystemCode(invokeSystemService.querySysCodeById(role.getSystemId()).getSysCode());
		byte status = 1;
		role.setStatus(status);
		// role = getById(role.getId());
		logger.info("删除角色服务查询扩展信息 " + role.toString());
		exService.queryExtsByCommonPojo(role);
		logger.info("删除角色服务准备开始下发MQ " + role.toString());
		mqSenderAdapter.sendMQ(LogTypeEnum.DELETE.getValue(), DataTypeEnum.ROLE.getValue(), role);
		return result;
	}

	public Role getById(String id) {
		return mapper.getById(id);
	}

	public List<Role> queryRoleByUserId(String userId) {
		return mapper.queryRoleByUserId(userId);
	}

	@Override
	public List<Role> queryList(Map<String, Object> params) {
		return mapper.queryPageList(params);
	}
	
	public List<UserOrganizationRole> queryUserIdByRoleId(Map<String, Object> params) {
		return userRoleMapper.queryUserIdByRoleId(params);
	}
	
	public PageResultBean queryUserRoleByRoleId(int pageNum, int pageSize, Map<String, Object> params) {
		Page<UserOrganizationRole> page = PageHelper.startPage(pageNum, pageSize);
		PageResultBean pageResult = new PageResultBean();
		pageResult.setList(userRoleMapper.queryUserRoleByRoleId(params));
		pageResult.setTotalCount(page.getTotal());
		return pageResult;
	}

	public int deleteUserRoleByRoleId(Role role) {
		return mapper.deleteUserRoleByRoleId(role);
	}

	public Role getMinLevelRoleByUserId(Map<String, Object> params) {
		return mapper.getMinLevelRoleByUserId(params);
	}
}

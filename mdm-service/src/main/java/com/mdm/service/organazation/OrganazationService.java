package com.mdm.service.organazation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.mdm.common.PageResultBean;
import com.mdm.core.bean.common.CommonDataResponse;
import com.mdm.core.bean.pojo.StaticData;
import com.mdm.core.bean.response.SystemInfoResponse;
import com.mdm.core.constants.GlobalConstants;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.dao.write.invokesystem.SystemInfoWriteMapper;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.enums.LogTypeEnum;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.service.sys.StaticDataService;
import com.mdm.core.tree.TreeNode;
import com.mdm.core.util.BeanUtils;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.DateUtils;
import com.mdm.core.util.ExcelUtil;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;
import com.mdm.dao.write.merchant.MerchantWriteMapper;
import com.mdm.dao.write.organazation.OrganazationWriteMapper;
import com.mdm.exportBean.OrganizationExport;
import com.mdm.importBean.CommunityCodeId;
import com.mdm.importBean.DistrictCodeId;
import com.mdm.importBean.OrganizationCodeId;
import com.mdm.importBean.OrganizationImport;
import com.mdm.mq.producter.MQSenderAdapter;
import com.mdm.pojo.CommonTask;
import com.mdm.pojo.Merchant;
import com.mdm.pojo.Organazation;
import com.mdm.pojo.PointAccount;
import com.mdm.request.OrganazationRequest;
import com.mdm.request.PointAccountRequest;
import com.mdm.response.CheckOrganizationCommonData;
import com.mdm.response.CommonPojoResponse;
import com.mdm.service.community.CommunityService;
import com.mdm.service.dataextend.DataExtendService;
import com.mdm.service.district.DistrictRestfulService;
import com.mdm.service.district.DistrictService;
import com.mdm.service.initpath.InitPathService;
import com.mdm.service.task.CommonTaskService;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrganazationService {

    @Autowired
    private InvokeSystemService invokeSystemService;

    @Autowired
    OrganazationWriteMapper organazationWriteMapper;
    @Autowired
    SystemInfoWriteMapper systemInfoWriteMapper;
    @Autowired
    DataExtendService dataExtendService;
    @Autowired
    private MQSenderAdapter mqSenderAdapter;
    @Autowired
    private DistrictRestfulService districtRestfulService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private CommonTaskService commonTaskService;
    @Autowired
    private InitPathService initPathService;
    @Autowired
    private StaticDataService staticDataService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private MerchantWriteMapper merchantWriteMapper;
    
    private static Logger logger = LoggerFactory.getLogger(OrganazationService.class);

    public List<Organazation> queryOrganazationListByOrgType(String orgType) {
        return organazationWriteMapper.queryOrganazationList(orgType, null, null, null, null, null, null, null, null);
    }

    /**
     * @param orgType
     * @param corpCode
     * @param sourceid
     * @param beginDate
     * @param endDate
     * @param orgCode
     * @param orgName
     * @param parentid
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResultBean queryOrganazationList(String orgType, String corpCode, String sourceid, String beginDate, String endDate, String orgCode, String orgName, String parentid, int pageNum, int pageSize) {
        PageResultBean pageResult = new PageResultBean();
        Page<Organazation> page = null;
        List<Organazation> list = null;
        if (pageNum != 0 && pageSize != 0) {
            page = PageHelper.startPage(pageNum, pageSize);// 分页与统计总数
        }
        if (StringUtil.isEmpty(parentid)) {
            list = organazationWriteMapper.queryOrganazationList(orgType, corpCode, sourceid, beginDate, endDate, orgCode, orgName, null, null);
        } else {
            Organazation organazation = organazationWriteMapper.getById(parentid);
            if (pageNum != 0 && pageSize != 0) {
                page = PageHelper.startPage(pageNum, pageSize);// 分页与统计总数
            }
            list = organazationWriteMapper.queryOrganazationList(orgType, corpCode, sourceid, beginDate, endDate, orgCode, orgName, parentid, StringUtil.isEmpty(organazation.getPath()) ? "" : organazation.getPath());
        }
        dataExtendService.queryExtsByCommonPojoList(list);
        list.forEach(org -> setIdOrganizationType(org));
        pageResult.setList(list);
        pageResult.setTotalCount(page == null ? list.size() : page.getTotal());
        return pageResult;

    }

    /**
     * 生成组织机构Code
     *
     * @return code
     */
    public String createRandomCode() {
        String orgCode = System.currentTimeMillis() + String.valueOf((int) (Math.random() * 1000));
        // orgCode = orgCode.replace("-", "");
        orgCode = "ORG" + orgCode;
//		while (checkIsExistsOrgCode(orgCode)) {
//			return createRandomCode();
//		}
        return orgCode;
    }

    public CommonPojoResponse orgTreeDeleteAllById(String id) throws MdmException {
        Organazation organazation = organazationWriteMapper.getById(id);
        if (organazation == null) {
            throw new MdmException("无该组织机构");
        }
        if (StringUtil.isNotEmpty(organazation.getPath())) {
            List<Organazation> list = organazationWriteMapper.queryOrganazationList(organazation.getType(), null, null, null, null, null, null, organazation.getId(), organazation.getPath());
            for (Organazation org : list) {
                delete(org.getId());
            }
            return delete(id);
        } else {
            throw new MdmException("根节点无法删除");
        }
    }

    public CommonPojoResponse updateAllChild(String id) throws MdmException {
        Organazation organazation = organazationWriteMapper.getById(id);
        if (organazation == null) {
            throw new MdmException("无该组织机构");
        }
        if (StringUtil.isNotEmpty(organazation.getPath())) {
            List<Organazation> list = organazationWriteMapper.queryOrganazationList(organazation.getType(), null, null, null, null, null, null, organazation.getId(), organazation.getPath());
            for (Organazation org : list) {
                org.setStatus(GlobalConstants.STATUS_STOP.intValue());
                updateForAllChild(org);
            }
            organazation.setStatus(GlobalConstants.STATUS_STOP.intValue());
            return updateForAllChild(organazation);
        } else {
            throw new MdmException("根节点无法停用");
        }
    }


    public CommonPojoResponse delete(String id) {
        dataExtendService.deteleByDataid(id);
        // int result = organazationWriteMapper.deleteById(id);
        Organazation organazation = queryById(id);
        //organazation.setModifiedBy(HttpContent.getOperatorId());
        organazation.setModifiedOn(new Date());
        //organazation.setVersion(System.currentTimeMillis() + "");
        organazation.initModify();
        organazation.setIsDeleted(GlobalConstants.IS_DELETED.intValue());
        organazationWriteMapper.update(organazation);
        sendOrgMQ(LogTypeEnum.DELETE.getValue(), organazation);
        return new CommonPojoResponse(organazation.getId(), organazation.getVersion());
    }

    /**
     * 根据Id查询Org
     *
     * @param id
     * @return
     */
    public Organazation queryById(String id) {
        Organazation result = organazationWriteMapper.getById(id);
        if (result != null) {
            dataExtendService.queryExtsByCommonPojo(result);
            setIdOrganizationType(result);
            String sysCode = invokeSystemService.querySysCodeById(result.getSystemId(), result.getSystemCode());
            setIdOrganizationType(result);
            result.setSystemCode(sysCode);
        }
        return result;
    }

    private boolean checkIsExistsOrgCode(String orgCode, String corpCode, String systemId) {
        Organazation organazation = organazationWriteMapper.queryByCodeAndCorpCodeAndSystemId(orgCode, corpCode, systemId);
        return organazation != null;
    }

    public CommonPojoResponse insert(Organazation organazation) throws MdmException {
        organazation.setId(DataUtils.uuid());
        if (StringUtil.isNotEmpty(organazation.getParentId())) {
            Organazation parentOrg = organazationWriteMapper.getById(organazation.getParentId());
            if (parentOrg == null) {
                throw new MdmException("父节点已被删除");
            }
            organazation.setPath((parentOrg.getPath() == null ? "" : parentOrg.getPath()) + "/" + organazation.getParentId());
            organazation.setType(parentOrg.getType());
            organazation.setLevels(organazation.getPath().split("/").length-1);
        } else {
            organazation.setPath(null);
            organazation.setLevels(0);
        }
        organazation.setCreatedOn(new Date());
        organazation.setModifiedOn(new Date());
        organazation.setIsDeleted(0);
        if (StringUtil.isNotEmpty(organazation.getIdOrganizationType())) {
            organazation.setIdOrganizationType(getIdOrganizationType(organazation.getIdOrganizationType()));
        }
        organazation.setCreatedBy(HttpContent.getOperatorId());
        organazation.setModifiedBy(HttpContent.getOperatorId());
        organazation.setVersion(System.currentTimeMillis() + "");
        dataExtendService.insertAllExts(organazation);
        int result = organazationWriteMapper.insert(organazation);
        sendOrgMQ(LogTypeEnum.ADD.getValue(), organazation);
        return new CommonPojoResponse(organazation.getId(), organazation.getVersion());
    }

    public CommonPojoResponse update(Organazation organazation) throws MdmException {
        //organazation.setModifiedBy(HttpContent.getOperatorId());
        organazation.setModifiedOn(new Date());
        //organazation.setVersion(System.currentTimeMillis() + "");
        organazation.initModify();
        dataExtendService.insertAllExts(organazation);
        organazation.setIdOrganizationType(getIdOrganizationType(organazation.getIdOrganizationType()));
//        int result = organazationWriteMapper.update(organazation);
        if(MdmConstants.CSS.equals(HttpContent.getSysCode())){
        	if(organazationWriteMapper.updateFromCss(organazation) == 0) {
                throw new MdmException("更新失败：根据ID及Version查找不到指定数据！");
            }      	
        }else{
	        if(organazationWriteMapper.update(organazation) == 0) {
	            throw new MdmException("更新失败：根据ID及Version查找不到指定数据！");
	        }
        }
        sendOrgMQ(LogTypeEnum.UPDATE.getValue(), organazation);
        return new CommonPojoResponse(organazation.getId(), organazation.getVersion());
    }
    /**
     * @param organazation
     * @Title: updateForAllChild
     * @Description: 仅仅针对updateAllChild方法，不需用转换类型
     * @return: CommonPojoResponse
     */
    private CommonPojoResponse updateForAllChild(Organazation organazation) throws MdmException {
        organazation.setModifiedOn(new Date());
        organazation.initModify();
        dataExtendService.insertAllExts(organazation);
        if(organazationWriteMapper.update(organazation) == 0) {
            throw new MdmException("更新失败：根据ID及Version查找不到指定数据！");
        }
        sendOrgMQ(LogTypeEnum.UPDATE.getValue(), organazation);
        return new CommonPojoResponse(organazation.getId(), organazation.getVersion());
    }

    public CommonPojoResponse updateHistory(Organazation organazation) {
        //organazation.setModifiedBy(HttpContent.getOperatorId());
        organazation.setModifiedOn(new Date());
        //organazation.setVersion(System.currentTimeMillis() + "");
        //organazation.initModify();
        dataExtendService.insertAllExts(organazation);
        organazation.setIdOrganizationType(getIdOrganizationType(organazation.getIdOrganizationType()));
        int result = organazationWriteMapper.update(organazation);
        sendOrgMQ(LogTypeEnum.UPDATE.getValue(), organazation);
        return new CommonPojoResponse(organazation.getId(), organazation.getVersion());
    }

    /**
     * @param organazation
     * @param path
     * @Title: batchUpdateOrganazationPath
     * @Description: 批量更新
     * @return: void
     */
    private void batchUpdateOrganazationPath(Organazation organazation, String path) {
        organazation.setPath(path);
        List<Organazation> childs = organazationWriteMapper.queryOrganazationByParentid(organazation.getParentId(),null);
        for (Organazation org : childs) {
            batchUpdateOrganazationPath(org, organazation.getPath() + "/" + org.getId());
        }
        this.updateOrganazation(organazation);
    }

    /**
     * @param organazation
     * @Title: updateOrganazation
     * @Description: 更新一条数据
     * @return: void
     */
    private void updateOrganazation(Organazation organazation) {
        organazationWriteMapper.update(organazation);
        sendOrgMQ(LogTypeEnum.UPDATE.getValue(), organazation);
    }

    /**
     * 根据Organazation 获取TreeNodeList
     *
     * @param organazations
     * @return
     */
    public List<TreeNode> getTreeNodes(List<Organazation> organazations) {
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (Organazation organazation : organazations) {
            TreeNode treeNode = new TreeNode();
            if (StringUtil.isEmpty(organazation.getParentId())) {
                treeNode.setOpen(true);
            }
            treeNode.setPath(organazation.getPath());
            treeNode.setId(organazation.getId());
            treeNode.setParentId(organazation.getParentId());
            treeNode.setName(organazation.getName());
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    /**
     * 检查参数是否正常
     *
     * @param organazationRequest
     * @return
     */
    public CheckOrganizationCommonData checkParams(OrganazationRequest organazationRequest, String orgId) {
        CheckOrganizationCommonData checkOrganizationCommonData = new CheckOrganizationCommonData();
        List<StaticData> staticDatas = staticDataService.queryStaticDataList("OrganizationType", null);
        boolean typeFlag = false;
        //判断是否属于开通的数据类型
        for (StaticData staticData : staticDatas) {
            if (staticData.getColValue().equals(organazationRequest.getType())) {
                typeFlag = true;
                break;
            }
        }
        if (!typeFlag) {
            checkOrganizationCommonData.setCheckResult(false);
            checkOrganizationCommonData.setCheckMsg("暂无该数据类型");
        } else if (StringUtil.isEmpty(organazationRequest.getCode())) {
            checkOrganizationCommonData.setCheckResult(false);
            checkOrganizationCommonData.setCheckMsg("代码不能为空");
        } else if (StringUtil.isEmpty(organazationRequest.getCorpCode())) {
            checkOrganizationCommonData.setCheckResult(false);
            checkOrganizationCommonData.setCheckMsg("企业代码不能为空");
        } else if (StringUtil.isEmpty(organazationRequest.getName())) {
            checkOrganizationCommonData.setCheckResult(false);
            checkOrganizationCommonData.setCheckMsg("名称不能为空");
        } else if (StringUtil.isEmpty(organazationRequest.getSystemId()) && StringUtil.isEmpty(organazationRequest.getSystemCode())) {
            checkOrganizationCommonData.setCheckResult(false);
            checkOrganizationCommonData.setCheckMsg("来源不能为空");
        } else if (organazationRequest.getStatus() == null) {
            checkOrganizationCommonData.setCheckResult(false);
            checkOrganizationCommonData.setCheckMsg("状态不能为空");
        }
        String systemId = invokeSystemService.querySystemId(organazationRequest.getSystemId(), organazationRequest.getSystemCode());
        if (StringUtil.isEmpty(systemId)) {
            checkOrganizationCommonData.setCheckResult(false);
            checkOrganizationCommonData.setCheckMsg("来源不存在");
        }
        organazationRequest.setSystemId(systemId);
        SystemInfoResponse systemInfoResponse = invokeSystemService.querySysCodeById(systemId);
        if (systemInfoResponse.getSysCode().equals(MdmConstants.SFYOFF) || systemInfoResponse.getSysCode().equals(MdmConstants.CSS)) {
            if (StringUtil.isEmpty(organazationRequest.getIdOrganizationType())) {
                checkOrganizationCommonData.setCheckResult(false);
                checkOrganizationCommonData.setCheckMsg("机构类型不能为空");
            }
        } else {
            organazationRequest.setIdOrganizationType(null);
        }
        if (StringUtil.isNotEmpty(orgId)) {
            Organazation organazation = organazationWriteMapper.getById(orgId);
            if (organazation == null) {
                checkOrganizationCommonData.setCheckResult(false);
                checkOrganizationCommonData.setCheckMsg("该组织机构已被删除");
            }
            Organazation existsOrganazation = organazationWriteMapper.queryByCodeAndCorpCodeAndSystemId(organazationRequest.getCode(), organazationRequest.getCorpCode(), organazationRequest.getSystemId());
            if (existsOrganazation != null && !existsOrganazation.getId().equalsIgnoreCase(orgId)) {
                checkOrganizationCommonData.setCheckResult(false);
                checkOrganizationCommonData.setCheckMsg("代码不能重复");
                checkOrganizationCommonData.setCommonDataResponse(new CommonDataResponse(existsOrganazation.getId(), existsOrganazation.getVersion()));
            }
        } else {
            if (checkIsExistsOrgCode(organazationRequest.getCode(), organazationRequest.getCorpCode(), organazationRequest.getSystemId())) {
                checkOrganizationCommonData.setCheckResult(false);
                checkOrganizationCommonData.setCheckMsg("代码不能重复");
//				Organazation organazationCode = organazationWriteMapper.queryByCode(organazationRequest.getCode());
                Organazation organazationCode = organazationWriteMapper.queryByCodeAndCorpCodeAndSystemId(organazationRequest.getCode(), organazationRequest.getCorpCode(), organazationRequest.getSystemId());
                checkOrganizationCommonData.setCommonDataResponse(new CommonDataResponse(organazationCode.getId(), organazationCode.getVersion()));
            }
        }
        if (StringUtil.isEmpty(checkOrganizationCommonData.getCheckMsg())) {
            checkOrganizationCommonData.setCheckResult(true);
            return checkOrganizationCommonData;
        }
        return checkOrganizationCommonData;
    }

    public List<Organazation> batchGetByIds(List<String> list) {
        return organazationWriteMapper.batchGetByIds(list);
    }

    public SystemInfoResponse querySystemInfo(String code) {
        return systemInfoWriteMapper.querySystemInfoByCode(code);
    }

    public Map<String, Object> importOrganization(MultipartFile file) throws MdmException {
        logger.info("导入组织机构开始时间:" + DateUtils.dateToString(new Date(), DateUtils.TIMESTAMP));
        //查询省市区
        List<DistrictCodeId> districtList = districtService.queryAllLevel3List();
        // 查询系统信息
        List<SystemInfoResponse> systemList = invokeSystemService.queryAllSystemInfo();
        // 查询机构类型
        List<StaticData> orgTypeList = staticDataService.queryStaticDataList("CCPGOrgType", null);
        // 查询组织机构所属系统
        List<StaticData> organizationTypeList = staticDataService.queryStaticDataList("OrganizationType", null);
        // 查询社区
        List<CommunityCodeId> communityCodeIdList = communityService.queryCodeIdList();

        // 封装下拉
        List<String> strDistrictList = new ArrayList<>(); // 区域
        districtList.forEach(t -> strDistrictList.add(t.getId() + ":" + t.getCode()));

        List<String> strSystemList = new ArrayList<>(); // 来源系统
        for (SystemInfoResponse system : systemList) {
            String str = system.getId() + ":" + system.getSysName();
            strSystemList.add(str);
        }

        List<String> strStatusList = new ArrayList<>(); // 状态
        strStatusList.add("0:停用");
        strStatusList.add("1:启用");

        List<String> strOrgType = new ArrayList<>(); // 组织机构类型
        orgTypeList.forEach(t -> strOrgType.add(t.getId() + ":" + t.getColValue()));

        List<String> strOrganizationTypeList = new ArrayList<>();    //组织机构所属系统(类型)
        organizationTypeList.forEach(t -> strOrganizationTypeList.add(t.getColValue() + ":" + t.getColName()));

        List<String> strCommunityList = new ArrayList<>(); // 社区
        communityCodeIdList.forEach(t -> strCommunityList.add(t.getId() + ":" + t.getName()));

        Map<String, List<String>> selectMap = new HashMap<>();
        selectMap.put("provinceId", strDistrictList);
        selectMap.put("cityId", strDistrictList);
        selectMap.put("districtID", strDistrictList);
        selectMap.put("systemId", strSystemList);
        selectMap.put("status", strStatusList);
        selectMap.put("idOrganizationType", strOrgType);
        selectMap.put("type", strOrganizationTypeList);
        selectMap.put("communityId", strCommunityList);

        List<OrganizationImport> organizationList = ExcelUtil.importExcel(file, OrganizationImport.class, selectMap);
        String errStr = "";
        String CSSType = "3";      //物业云Id
        String SFYOFFType = "2";   //收费云Id
        boolean hasError = false;
        //物业云、收费云下组织机构【机构类型】字段为必填字段
        for(OrganizationImport o : organizationList) {
            if((o.getType().equals(CSSType) || o.getType().equals(SFYOFFType))
                    && org.springframework.util.StringUtils.isEmpty(o.getIdOrganizationType())) {
                hasError = true;
                errStr += "Code：" + o.getCode() + "【机构类型】不可为空，";
            }
        }
        if(hasError) {
            throw new MdmException(String.valueOf(errStr));
        }

        Map<String, Object> result = new HashMap();
        result = importBatch(organizationList);
        logger.info("导入组织机构结束时间:" + DateUtils.dateToString(new Date(), DateUtils.TIMESTAMP));
        return result;
    }

    public Map<String, Object> importBatch(List<OrganizationImport> organizationImportList) throws MdmException {

        List<OrganizationImport> organizationlistToAdd = new ArrayList<>();
        List<OrganizationImport> organizationListToUpdate = new ArrayList<>();
        List<Organazation> addList = new ArrayList<>();
        List<Organazation> updateList = new ArrayList<>();
        List<CommonTask> commonTaskList = new ArrayList<>();
        // if code 存在，加入到organizationListToUpdate; else 加入到organizationlistToAdd
        Map<String, String> organizationCodeIdMap = this.queryCodeIdMap();
        for (OrganizationImport organizationImport : organizationImportList) {
            if (organizationCodeIdMap.containsKey(organizationImport.getCode())) {
                organizationImport.setId(organizationCodeIdMap.get(organizationImport.getCode()));
                organizationListToUpdate.add(organizationImport);
            } else {
                organizationImport.setId(DataUtils.uuid());
                organizationCodeIdMap.put(organizationImport.getCode(), organizationImport.getId());
                organizationlistToAdd.add(organizationImport);
            }
        }
        // 设置父子级关系，并copy到addList、updateList
        if (!organizationlistToAdd.isEmpty()) {
            for (OrganizationImport o : organizationlistToAdd) {
                OrganazationRequest organazationRequest = new OrganazationRequest();
                setParentId(organizationCodeIdMap, o);
                Organazation organization = new Organazation();
                BeanUtils.copyProperties(o, organization);
                //setOrganizationPathLevels(organization, addList);
                organization.setCreatedOn(new Date());
                organization.setModifiedOn(new Date());
                organization.setIsDeleted(0);
                organization.setCreatedBy(HttpContent.getOperatorId());
                organization.setModifiedBy(HttpContent.getOperatorId());
                organization.setVersion(System.currentTimeMillis() + "");
                addList.add(organization);
                commonTaskList.add(getCommonTask(organization));
            }
        }
        if (!organizationListToUpdate.isEmpty()) {
            for (OrganizationImport o : organizationListToUpdate) {
                setParentId(organizationCodeIdMap, o);
                Organazation organization = new Organazation();
                BeanUtils.copyProperties(o, organization);
                //setOrganizationPathLevels(organization, addList);
                organization.setModifiedBy(HttpContent.getOperatorId());
                organization.setModifiedOn(new Date());
                organization.setVersion(System.currentTimeMillis() + "");
                updateList.add(organization);
                commonTaskList.add(getCommonTask(organization));
            }
        }

        //计算Path及Levels
        calculatePathAndLevels(addList, updateList);

        int addRows = 0, updateRows = 0;
        if (!addList.isEmpty()) {
            addRows = organazationWriteMapper.batchInsert(addList);
        }
        if (!updateList.isEmpty()) {
            organazationWriteMapper.batchUpdate(updateList);
            updateRows = updateList.size();
        }
        // 批量添加commonTask信息，用于MQ等处理
        commonTaskService.batchInsert(commonTaskList);

        Map<String, Object> result = new HashMap<>();
        result.put("addRows", addRows);
        result.put("updateRows", updateRows);
        return result;
    }

    /**
     * 导出组织机构数据到Excel，参数与queryOrganazationList相同
     *
     * @param response
     * @param orgType
     * @param corpCode
     * @param sourceid
     * @param beginDate
     * @param endDate
     * @param orgCode
     * @param orgName
     * @param parentid
     * @return
     * @throws MdmException
     */
    public String export(HttpServletResponse response, String orgType, String corpCode, String sourceid, String beginDate, String endDate, String orgCode, String orgName, String parentid) throws MdmException {
        String result = "";
        try {
            List<OrganizationExport> list = null;
            // 查询
            if (StringUtil.isEmpty(parentid)) {
                list = organazationWriteMapper.exportOrganizationList(orgType, corpCode, sourceid, beginDate, endDate, orgCode, orgName, null, null);
            } else {
                Organazation organazation = organazationWriteMapper.getById(parentid);
                list = organazationWriteMapper.exportOrganizationList(orgType, corpCode, sourceid, beginDate, endDate, orgCode, orgName, parentid, StringUtil.isEmpty(organazation.getPath()) ? "" : organazation.getPath());
            }
            // 更新省市区等信息
            Map<String, String> districtMap = districtService.getDistrictMap();
            // 状态Map
            Map<String, String> statusMap = new HashMap<>();
            statusMap.put("0", "停用");
            statusMap.put("1", "启用");
            // 查询机构类型
            List<StaticData> orgTypeList = staticDataService.queryStaticDataList("CCPGOrgType", null);
            Map<String, String> orgTypeMap = new HashMap<>();
            for (StaticData s : orgTypeList) {
                if (!orgTypeMap.containsKey(s.getId().toString())) {
                    orgTypeMap.put(s.getId().toString(), s.getColValue());
                }
            }
            // 查询组织机构所属系统
            List<StaticData> organizationTypeList = staticDataService.queryStaticDataList("OrganizationType", null);
            Map<String, String> organizationTypeMap = new HashMap<>();
            for (StaticData s : organizationTypeList) {
                if (!organizationTypeMap.containsKey(s.getColValue())) {
                    organizationTypeMap.put(s.getColValue(), s.getColName());
                }
            }
            // 查询社区
            List<CommunityCodeId> communityCodeIdList = communityService.queryCodeIdList();
            Map<String, String> communityMap = new HashMap<>();
            for (CommunityCodeId c : communityCodeIdList) {
                if (!communityMap.containsKey(c.getId())) {
                    communityMap.put(c.getId(), c.getName());
                }
            }
            for (OrganizationExport o : list) {
                if (districtMap.containsKey(o.getProvince())) {
                    o.setProvince(districtMap.get(o.getProvince()));
                }
                if (districtMap.containsKey(o.getCity())) {
                    o.setCity(districtMap.get(o.getCity()));
                }
                if (districtMap.containsKey(o.getDistrict())) {
                    o.setDistrict(districtMap.get(o.getDistrict()));
                }
                // 更新状态
                if (o.getStatus() != null && statusMap.containsKey(o.getStatus().toString())) {
                    o.setStatusText(statusMap.get(o.getStatus().toString()));
                }
                // 更新机构类型
                if (o.getIdOrganizationType() != null && orgTypeMap.containsKey(o.getIdOrganizationType().toString())) {
                    o.setIdOrganizationType(orgTypeMap.get(o.getIdOrganizationType().toString()));
                }
                // 更新所属系统
                if (o.getType() != null && organizationTypeMap.containsKey(o.getType())) {
                    o.setType(organizationTypeMap.get(o.getType()));
                }
                // 更新社区
                if (o.getCommunity() != null && communityMap.containsKey(o.getCommunity())) {
                    o.setCommunity(communityMap.get(o.getCommunity()));
                }
            }

            // dataExtendService.queryExtsByCommonPojoList(list);
            if (list != null && list.size() > 0) {
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.addHeader("Content-Disposition", new String("attachment; filename=组织机构信息.xls".getBytes(), "ISO-8859-1"));
                ExcelUtil.ExportExcelByBean("父级编码,父级名称,编码,类型,企业代码,名称,状态,联系电话,省份,城市,区域,社区,详细地址,经度,纬度,描述,机构类型,来源系统".split(","),
                        "parentCode,parentName,code,type,corpCode,name,statusText,contactTel,province,city,district,community,addrDetails,longitude,latitude,description,idOrganizationType,system".split(","), list,
                        "组织机构信息", response);
                result = "导出成功";
            } else {
                result = "当前查询条件无数据";
            }
        } catch (UnsupportedEncodingException e) {
            throw new MdmException("导出组织机构列表失败，请重新导出！");
        } catch (IOException e) {
            throw new MdmException("io异常");
        }

        return result;
    }

    /**
     * 设置上下级关系
     *
     * @param organizationCodeIdMap
     * @param organizationImport
     * @throws MdmException
     */
    private void setParentId(Map<String, String> organizationCodeIdMap, OrganizationImport organizationImport) throws MdmException {
        if (organizationImport.getParentCode().equals("0")) {
            organizationImport.setParentId("0");
        } else {
            if (organizationCodeIdMap.containsKey(organizationImport.getParentCode())) {
                organizationImport.setParentId(organizationCodeIdMap.get(organizationImport.getParentCode()));
            } else {
                throw new MdmException("无法找到code:【" + organizationImport.getCode() + "】的上级节点");
            }
        }
    }

    /**
     * @param organization
     */
    private Map<String, Organazation> orgCacheMap = new HashMap<>();

    private  void calculatePathAndLevels(List<Organazation> addList, List<Organazation> updateList) throws MdmException {
        if(addList != null && addList.size() > 0) {
            for(Organazation o : addList) {
                setOrganizationPathLevels(o, addList, updateList);
            }
        }
        if(updateList != null && updateList.size() > 0) {
            for(Organazation o : updateList) {
                setOrganizationPathLevels(o, addList, updateList);
            }
        }
    }

    private void setOrganizationPathLevels(Organazation organization, List<Organazation> addList, List<Organazation> updateList) throws MdmException {
        if (!organization.getParentId().equals("0")) {
            if(orgCacheMap.containsKey(organization.getId())) {
                //说明已经设置过Path、Levels属性，直接返回
                return;
            }
            //获取父节点
            Organazation parentOrg = null;
            if (orgCacheMap.containsKey(organization.getParentId())) {
                parentOrg = orgCacheMap.get(organization.getParentId());
            } else {
                //从新增列表中查找
                if(addList != null && addList.size() > 0) {
                    for(Organazation o : addList) {
                        if(o.getId().equals(organization.getParentId())) {
                            parentOrg = o;
                            break;
                        }
                    }
                }
                //从更新列表中查找
                if(parentOrg == null && updateList != null && updateList.size() > 0) {
                    for(Organazation o : updateList) {
                        if(o.getId().equals(organization.getParentId())) {
                            parentOrg = o;
                            break;
                        }
                    }
                }
                //从数据库中查找
                if(parentOrg == null) {
                    parentOrg = queryById(organization.getParentId());
                    orgCacheMap.put(parentOrg.getId(), parentOrg);
                }
            }

            if (parentOrg != null) {
                //父节点Levels为null时，递归获取
                if(parentOrg.getLevels() == null) {
                    setOrganizationPathLevels(parentOrg, addList, updateList);
                }
                organization.setLevels(parentOrg.getLevels() + 1);
                if (StringUtils.isEmpty(parentOrg.getPath())) {
                    organization.setPath("/" + parentOrg.getId());
                } else {
                    organization.setPath(parentOrg.getPath() + "/" + parentOrg.getId());
                }
                //设置过Path、Levels后放入cache
                orgCacheMap.put(organization.getId(), organization);
            }
            else
            {
                throw new MdmException("无法找到名称【" + organization.getName() + "】的父节点");
            }
        }
    }

    /**
     * 创建CommonTask
     *
     * @param organization
     * @return
     */
    private CommonTask getCommonTask(Organazation organization) {
        CommonTask task = new CommonTask();
        task.setId(DataUtils.uuid());
        task.setDataId(organization.getId());
        task.setCreatedBy(MdmConstants.ADMIN_ID);
        task.setCreatedOn(new Date());
        task.setDataType(DataTypeEnum.ORGANAZATION.getValue());
        task.setTaskType(1);
        task.setStatus(0);
        return task;
    }

    /**
     * 获取数据库中所有数据的code, id: Map<code, id>
     *
     * @return
     */
    public Map<String, String> queryCodeIdMap() {
        Map<String, String> organizationCodeIdMap = new HashMap<>();
        List<OrganizationCodeId> organizationCodeIdList = queryCodeIdList();
        for (OrganizationCodeId o : organizationCodeIdList) {
            if (!organizationCodeIdMap.containsKey(o.getCode())) {
                organizationCodeIdMap.put(o.getCode(), o.getId());
            }
        }
        return organizationCodeIdMap;
    }

    /**
     * 获取数据库中所有数据的code, id
     *
     * @return
     */
    public List<OrganizationCodeId> queryCodeIdList() {
        return organazationWriteMapper.queryCodeIdMap();
    }

    private void sendOrgMQ(Integer logType, Organazation organazation) {
        String sysCode = invokeSystemService.querySysCodeById(organazation.getSystemId(), organazation.getSystemCode());
        setIdOrganizationType(organazation);
        organazation.setSystemCode(sysCode);
        mqSenderAdapter.sendMQ(logType, DataTypeEnum.ORGANAZATION.getValue(), organazation);
    }

    private void setIdOrganizationType(Organazation organazation) {
        StaticData staticData = staticDataService.getById(organazation.getIdOrganizationType());
        if (staticData != null) {
            organazation.setIdOrganizationType(staticData.getColName());
        }
    }

    private String getIdOrganizationType(String orgtypeCode) {
        StaticData staticData = staticDataService.queryStaticData("CCPGOrgType", orgtypeCode);
        if (staticData != null) {
            return staticData.getId();
        }
        return null;
    }
    /**
     * 更新积分帐户
     * @Title: updatePointAccount 
     * @Description: TODO
     * @param accountRequest
     * @return
     * @throws MdmException
     * @return: boolean
     */
    public List<Map<String, Object>> updatePointAccount(PointAccountRequest accountRequest) throws MdmException{
    	List<PointAccount> accounts = accountRequest.getOrgIds();
    	String pointAccount = accountRequest.getPointAccountId();
    	List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    	organazationWriteMapper.resetPointAccount(pointAccount);
    	merchantWriteMapper.resetPointAccount(pointAccount);
    	for (PointAccount model : accounts) {
			try {
				if (null == model.getType()) {
					// 组织
					int num = organazationWriteMapper.updatePointAccount(pointAccount, model.getOrgId(), model.getOrgType(), model.isExcepted(), HttpContent.getOperatorId());
					if (num < 1) {
						// 商家
						num = merchantWriteMapper.updatePointAccount(pointAccount, model.getOrgId(), model.getOrgType(), model.isExcepted(), HttpContent.getOperatorId());
						if (num < 1) {
							logger.error("更新积分账户失败，PointAccountId=%s，OrgId=%s，OrgType=%s，isExcepted=%s", pointAccount,model.getOrgId(),model.getOrgType(), model.isExcepted());
							handleResult(returnList, model, pointAccount);
						}
					}
				}else{
					if (model.getType().equalsIgnoreCase("organization")) {
						int num = organazationWriteMapper.updatePointAccount(pointAccount, model.getOrgId(), model.getOrgType(), model.isExcepted(), HttpContent.getOperatorId());
						if (num < 1) {
							logger.error("更新积分账户失败，PointAccountId=%s，OrgId=%s，OrgType=%s，isExcepted=%s", pointAccount,model.getOrgId(),model.getOrgType(), model.isExcepted());
							handleResult(returnList, model, pointAccount);
						}
					}
					if (model.getType().equalsIgnoreCase("merchant")) {
						int num = merchantWriteMapper.updatePointAccount(pointAccount, model.getOrgId(), model.getOrgType(), model.isExcepted(), HttpContent.getOperatorId());
						if (num < 1) {
							logger.error("更新积分账户失败，PointAccountId=%s，OrgId=%s，OrgType=%s，isExcepted=%s", pointAccount,model.getOrgId(),model.getOrgType(), model.isExcepted());
							handleResult(returnList, model, pointAccount);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	return returnList;
    }
    private void handleResult(List<Map<String, Object>> returnList, PointAccount model, String pointAccount){
    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("pointAccount", pointAccount);
		map.put("orgId", model.getOrgId());
		map.put("orgType", model.getOrgType());
		map.put("isExcepted", model.isExcepted());
		map.put("errorMsg", model.getType()==null?"更新积分账户失败，没有匹配的组织或商家。":model.getType().equalsIgnoreCase("merchant")?"更新积分账户失败，没有匹配的商家。":"更新积分账户失败，没有匹配的组织。");
		
		returnList.add(map);
    }
    /**
     * 查询积分帐户
     * @Title: queryPointAccount 
     * @Description: TODO
     * @param accountRequest
     * @return
     * @throws MdmException
     * @return: Map<String,Object>
     */
    public Map<String, Object> queryPointAccount(PointAccountRequest accountRequest) throws MdmException{
    	List<PointAccount> accounts = accountRequest.getOrgIds();
    	List<Map<String, Object>> resultAccounts = new ArrayList<Map<String, Object>>();
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    	accounts.forEach(model ->{
    		String orgId = model.getOrgId();
    		Map<String, Object> map = new HashMap<String, Object>();
    		// 获取积分账户
			getPointAccount(orgId,orgId,model.getOrgType(),map);
    		resultAccounts.add(map);
    	});
    	
    	resultMap.put("pointAccountIds", resultAccounts);
    	return resultMap;
    }
    /**
     * 查询积分帐户--内部使用迭代父级查询
     * @Title: getPointAccount 
     * @Description: TODO
     * @param orgId
     * @param orgType
     * @param map
     * @return: void
     */
    private void getPointAccount(String oldOrgId, String orgId, String orgType, Map<String, Object> map){
    	map.put("orgId", oldOrgId);
    	map.put("onwerOrgid", orgId);
    	Organazation organazation = organazationWriteMapper.queryPointAccount(orgId, orgType);
    	
    	if (null !=organazation) {
    		if (null != organazation.getPointAccountId() && !organazation.getPointAccountId().isEmpty()) {
    			if (organazation.isExcepted()) {
    				map.put("pointAccountId", null);
				} else {
					map.put("pointAccountId", organazation.getPointAccountId());
				}
    			map.put("type", "organization");
			}else{
				if (null != organazation.getParentId() && !organazation.getParentId().isEmpty()) {
					getPointAccount(oldOrgId,organazation.getParentId(),orgType,map);
				}else{
					map.put("pointAccountId", null);
					map.put("type", "organization");
				}
			}
		} else {
			// 组织中没有找到，需要查询商家表
			Merchant merchant = merchantWriteMapper.queryPointAccount(orgId);
			if (null == merchant) {
				map.put("pointAccountId", null);
			}else{
				if (!merchant.getPointAccountId().isEmpty() && !merchant.isExcepted()) {
					map.put("pointAccountId", merchant.getPointAccountId());
				}else {
					map.put("pointAccountId", null);
				}
				map.put("type", "merchant");
			}
		}
    }
    /**
     * 通过积分账户查询组织
     * @Title: getOrgIdByAccount 
     * @Description: TODO
     * @param accountRequest
     * @return
     * @throws MdmException
     * @return: String
     */
    public List<Map<String, Object>> getOrgIdByAccount(PointAccountRequest accountRequest) throws MdmException{
    	List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    	if (null == accountRequest.getType() || "".equals(accountRequest.getType())) {
			returnList.addAll(organazationWriteMapper.getOrgIdByAccount(accountRequest.getPointAccount(), accountRequest.getOrgType()));
			returnList.addAll(merchantWriteMapper.queryMerchantIdByAccount(accountRequest.getPointAccount()));
		}else{
			if (accountRequest.getType().equalsIgnoreCase("organization")) {
				returnList = organazationWriteMapper.getOrgIdByAccount(accountRequest.getPointAccount(), accountRequest.getOrgType());
			}
			if (accountRequest.getType().equalsIgnoreCase("merchant")) {
				returnList = merchantWriteMapper.queryMerchantIdByAccount(accountRequest.getPointAccount());
			}
		}
    	return returnList;
    }
}

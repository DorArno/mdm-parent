package com.mdm.service.merchant;
/**@author Zhaoyao
 * 商家服务类
 * @date 2016、10、24
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.mdm.importBean.DistrictCodeId;
import com.mdm.service.district.DistrictService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mdm.core.bean.common.PageResultBean;
import com.mdm.core.bean.response.SystemInfoResponse;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.dao.write.invokesystem.SystemInfoWriteMapper;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.enums.LogTypeEnum;
import com.mdm.core.service.invokesystem.InvokeSystemService;
import com.mdm.core.util.BeanUtils;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.DateUtils;
import com.mdm.core.util.ExcelUtil;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;
import com.mdm.dao.write.merchant.MerchantWriteMapper;
import com.mdm.exportBean.MerchantExport;
import com.mdm.importBean.MerchantCodeId;
import com.mdm.importBean.MerchantImport;
import com.mdm.importBean.OrganizationCodeId;
import com.mdm.mq.producter.MQSenderAdapter;
import com.mdm.pojo.CommonTask;
import com.mdm.pojo.Merchant;
import com.mdm.pojo.MerchantCategory;
import com.mdm.response.CommonPojoResponse;
import com.mdm.response.MerchantResponse;
import com.mdm.service.dataextend.DataExtendService;
import com.mdm.service.district.DistrictRestfulService;
import com.mdm.service.organazation.OrganazationService;
import com.mdm.service.task.CommonTaskService;

@Service
@Transactional(rollbackFor = Exception.class)
public class MerchantService {
	
	@Autowired
	private MerchantWriteMapper mwMapper;
	@Autowired
	private SystemInfoWriteMapper sysMapper;
	@Autowired
	DataExtendService dataExtendService;
	@Autowired
	private MQSenderAdapter mqSenderAdapter;
	@Autowired
	private DataExtendService exService;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private InvokeSystemService invokeSystemService;
	@Autowired
	private OrganazationService organazationService;
	@Autowired
	private CommonTaskService commonTaskService;
	
	private static Logger logger = LoggerFactory.getLogger(OrganazationService.class);
	
	public PageResultBean queryMerchantList(String mName, String code,int pageNum, int pageSize,
			String mManager,String contactTel,String firstCategoryId,String status, String systemId){
		Page<Object> page = PageHelper.startPage(pageNum, pageSize);//分页与统计总数
		List<MerchantResponse> list = mwMapper.queryMerchantList(mName,code,mManager,contactTel,firstCategoryId,status, systemId);
		PageResultBean pageResult = new PageResultBean();
		pageResult.setList(list);
		pageResult.setTotalCount(page.getTotal());
		
		return pageResult ;
	}
	
	public List<MerchantResponse> queryMerchantsForUser(){
		List<MerchantResponse> list = mwMapper.queryMerchantsForUser();
		return list ;
	}
	public PageResultBean queryMerchantExist(String mName, String code,int pageNum, int pageSize){
		Page<Object> page = PageHelper.startPage(pageNum, pageSize);//分页与统计总数
		List<Merchant> list = mwMapper.queryMerchantCheck(mName,code);
		PageResultBean pageResult = new PageResultBean();
		pageResult.setList(list);
		pageResult.setTotalCount(page.getTotal());
		
		return pageResult ;
	}
	public PageResultBean queryMerchantType(String parentId){
		List<MerchantCategory> list = mwMapper.queryMerchantType(parentId);
		PageResultBean pageResult = new PageResultBean();
		pageResult.setList(list);
		return pageResult ;
	}
	public Merchant getMerchantById(String id){
		Merchant merchant = mwMapper.getMerchantById(id);
		dataExtendService.queryExtsByCommonPojo(merchant);
		return merchant;
	}
	
	public CommonPojoResponse addMerchant(Merchant merchant){
//		merchant.setSystemId(sysMapper.querySystemInfoByCode(merchant.getSystemId()).getId());//讲systemCode值转化为systemId存入
		merchant.setId(DataUtils.uuid());
		if(StringUtils.isEmpty(merchant.getCreatedBy())) {
			merchant.setCreatedBy(HttpContent.getOperatorId());
		}
		if(merchant.getCreatedOn() == null) {
			merchant.setCreatedOn(new Date());
		}
		merchant.setVersion(System.currentTimeMillis() + "");
		merchant.setIsDeleted(0);
		dataExtendService.insertAllExts(merchant);
		int num = mwMapper.addMerchant(merchant);
		sendMerchantMQ(LogTypeEnum.ADD.getValue(), merchant);
		return new CommonPojoResponse(merchant.getId(), merchant.getVersion());
	}
	
	public int updateMerchant(Merchant merchant) throws MdmException {
		merchant.setVersion(System.currentTimeMillis() + "");
		if(HttpContent.getSysCode().equals("MDM")){
			merchant.setModifiedOn(new Date());
			merchant.setModifiedBy(HttpContent.getOperatorId());
		}
//		merchant.initModify();
		dataExtendService.insertAllExts(merchant);
		int num = mwMapper.update(merchant);
//		if(mwMapper.update(merchant) == 0) {
//			throw new MdmException("更新失败：根据ID及Version查找不到指定数据！");
//		}
		sendMerchantMQ(LogTypeEnum.UPDATE.getValue(), merchant);
		return num;
	}
	public CommonPojoResponse update(Merchant merchant) throws MdmException {
//		if(HttpContent.getSysCode().equals("MDM")){
//			merchant.setModifiedOn(new Date());
//			merchant.setModifiedBy(HttpContent.getOperatorId());
//		}
//		merchant.setVersion(System.currentTimeMillis() + "");
		merchant.initModify();
		dataExtendService.insertAllExts(merchant);
//		int result = mwMapper.update(merchant);
		if(mwMapper.update(merchant) == 0) {
			throw new MdmException("更新失败：根据ID及Version查找不到指定数据！");
		}
		sendMerchantMQ(LogTypeEnum.UPDATE.getValue(), merchant);
		return new CommonPojoResponse(merchant.getId(), merchant.getVersion());
	}
	
	public CommonPojoResponse deleteAllById(String id) throws MdmException {
		Merchant merchant = mwMapper.getMerchantById(id);
		if (merchant == null) {
			throw new MdmException("无商家信息");
		}
		merchant.setModifiedBy(HttpContent.getOperatorId());
		merchant.setVersion(System.currentTimeMillis() + "");
		dataExtendService.deteleByDataid(id);
		int result = mwMapper.deleteById(merchant);
		merchant.setIsDeleted(1);
		sendMerchantMQ(LogTypeEnum.DELETE.getValue(), merchant);
		return new CommonPojoResponse(merchant.getId(), merchant.getVersion());
	}
	public CommonPojoResponse deleteMerchant(Merchant merchant) throws MdmException {
//		merchant.setVersion(System.currentTimeMillis() + "");
//		merchant.setModifiedBy(HttpContent.getOperatorId());
		merchant.initModify();
		dataExtendService.deteleByDataid(merchant.getId());
//		int num = mwMapper.deleteById(merchant);
		if(mwMapper.deleteById(merchant) == 0) {
			throw new MdmException("删除失败：根据ID及Version查找不到指定数据！");
		}
		merchant.setIsDeleted(1);
		merchant = getMerchantById(merchant.getId());
		sendMerchantMQ(LogTypeEnum.DELETE.getValue(), merchant);
		return new CommonPojoResponse(merchant.getId(), merchant.getVersion());
	}
	
	public List<Merchant> batchQueryMerchantListById(List<String> list) {
		return mwMapper.batchQueryMerchantListById(list);
	}
	/**
	 * @param file
	 * @return
	 * @throws MdmException 
	 */
	public Map<String, Object> importMerchant(MultipartFile file) throws MdmException {
		logger.info("导入商家开始时间:" + DateUtils.dateToString(new Date(), DateUtils.TIMESTAMP));
		//查询系统
		List<SystemInfoResponse> systemList = invokeSystemService.queryAllSystemInfo();
		//查询省市区
		List<DistrictCodeId> districtList = districtService.queryAllLevel3List();
		//查询一级/二级分类
		List<MerchantCategory> firstCategoryList = getFirstCategoryList();
		List<MerchantCategory> secondCategoryList = getSecondCategoryList();
		//查询组织机构
		List<OrganizationCodeId> orgList = organazationService.queryCodeIdList();
		//封装下拉
		List<String> strDistrictList = new ArrayList<>();	//区域
		districtList.forEach(t -> strDistrictList.add(t.getId() + ":" + t.getCode()));
		
		List<String> strSystemList = new ArrayList<>();		//来源系统
		systemList.forEach(t -> strSystemList.add(t.getId() + ":" + t.getSysName()));
		
		List<String> strFirstCategoryList = new ArrayList<>();	//一级分类
		List<String> strSecondCategoryList = new ArrayList<>();	//二级分类
		firstCategoryList.forEach(t -> strFirstCategoryList.add(t.getId() + ":" + t.getName()));
		secondCategoryList.forEach(t -> strSecondCategoryList.add(t.getId() + ":" + t.getName()));
		
		List<String> strOrgList = new ArrayList<>();
		orgList.forEach(t -> strOrgList.add(t.getId() + ":" + t.getCode()));
		
		List<String> strStatusList = new ArrayList<>();		//状态
		strStatusList.add("0:停用");
		strStatusList.add("1:启用");
		
		Map<String, List<String>> selectMap = new HashMap<>();
		selectMap.put("province", strDistrictList);
		selectMap.put("city", strDistrictList);
		selectMap.put("distict", strDistrictList);
		selectMap.put("systemId", strSystemList);
		selectMap.put("status", strStatusList);
		selectMap.put("firstCategoryId", strFirstCategoryList);
		selectMap.put("secondCategoryId", strSecondCategoryList);
		selectMap.put("organizationId", strOrgList);
		
		List<MerchantImport> merchantList = ExcelUtil.importExcel(file, MerchantImport.class, selectMap);
		Map<String, Object> result = new HashMap();
		result = importBatch(merchantList);
		logger.info("导入商家结束时间:" + DateUtils.dateToString(new Date(), DateUtils.TIMESTAMP));
		return result;
	}
	
	public Map<String, Object> importBatch(List<MerchantImport> merchantList) throws MdmException {
		List<Merchant> addList = new ArrayList<>();
		List<Merchant> updateList = new ArrayList<>();
		List<CommonTask> commonTaskList = new ArrayList<>();
		//if code 存在，加入到merchantListToUpdate; else 加入到merchantListToAdd
		Map<String, String> codeIdMap = this.queryCodeIdMap();
		for(MerchantImport m : merchantList) {
			if(codeIdMap.containsKey(m.getCode())) {
				m.setId(codeIdMap.get(m.getCode()));
				Merchant merchant = new Merchant();
				BeanUtils.copyProperties(m, merchant);
				formatMerchantDate(merchant);
				merchant.setModifiedOn(new Date());
				merchant.setIsDeleted(0);
				merchant.setModifiedBy(HttpContent.getOperatorId());
				merchant.setVersion(System.currentTimeMillis() + "");
				updateList.add(merchant);
				commonTaskList.add(getCommonTask(merchant));
			} else {
				m.setId(DataUtils.uuid());
				codeIdMap.put(m.getCode(), m.getId());
				Merchant merchant = new Merchant();
				BeanUtils.copyProperties(m, merchant);
				formatMerchantDate(merchant);
				merchant.setCreatedOn(new Date());
				merchant.setModifiedOn(new Date());
				merchant.setIsDeleted(0);
				merchant.setCreatedBy(HttpContent.getOperatorId());
				merchant.setModifiedBy(HttpContent.getOperatorId());
				merchant.setVersion(System.currentTimeMillis() + "");
				addList.add(merchant);
				commonTaskList.add(getCommonTask(merchant));
			}
		}
		int addRows = 0, updateRows = 0;
		if(!addList.isEmpty()) {
			addRows = mwMapper.batchInsert(addList);
		}
		if(!updateList.isEmpty()) {
			mwMapper.batchUpdate(updateList);
			updateRows = updateList.size();
		}
		//批量添加commonTask信息，用于MQ等处理
		commonTaskService.batchInsert(commonTaskList);
		
		Map<String, Object> result = new HashMap<>();
		result.put("addRows", addRows);
		result.put("updateRows", updateRows);
		return result;
	}

	private void formatMerchantDate(Merchant merchant) throws MdmException {
		try {
			merchant.setStartOperation(DateUtils.parseDate2Str("mm:ss", DateUtils.parseStr2Date("mm:ss", merchant.getStartOperation())));
			merchant.setEndOperation(DateUtils.parseDate2Str("mm:ss", DateUtils.parseStr2Date("mm:ss", merchant.getEndOperation())));
		} catch (ParseException e) {
			throw new MdmException("营业时间格式转换错误");
		}
	}
	
	/**
	 * 
	 * @param response
	 * @param mName
	 * @param code
	 * @param mManager
	 * @param contactTel
	 * @param firstCategoryId
	 * @param status
	 * @return
	 * @throws MdmException 
	 */
	public String export(HttpServletResponse response, String mName, String code, String mManager,String contactTel,String firstCategoryId,String status)
			throws MdmException {
		String result = "";
		try {
			//查询商家数据
			List<MerchantExport> list = mwMapper.exportMerchantList(mName,code,mManager,contactTel,firstCategoryId,status);
			//省市区Map
			Map<String, String> districtMap = districtService.getDistrictMap();
			//商家一级/二级分类Map
			Map<String, String> firstCategoryMap = getCategoryMap(null);
			Map<String, Map<String, String>> secondCategoryMap = new HashMap<>();
			//来源系统Map
			List<SystemInfoResponse> systemList = invokeSystemService.queryAllSystemInfo();
			Map<String, String> systemMap = new HashMap<>();
			for(SystemInfoResponse s : systemList) {
				if(!systemMap.containsKey(s.getId())) {
					systemMap.put(s.getId(), s.getSysName());
				}
			}
			//组织机构Map
			List<OrganizationCodeId> orgList = organazationService.queryCodeIdList();
			Map<String, String> orgMap = new HashMap<>();
			for(OrganizationCodeId o : orgList) {
				orgMap.put(o.getId(), o.getName());
			}
			//状态Map
			Map<String, String> statusMap = new HashMap<>();
			statusMap.put("0", "停用");
			statusMap.put("1", "启用");
			//是否Map
			Map<String, String> isMap = new HashMap<>();
			isMap.put("0", "否");
			isMap.put("1", "是");
			for(MerchantExport m : list) {
				//更新省市区
				if(districtMap.containsKey(m.getProvince())) {
					m.setProvince(districtMap.get(m.getProvince()));
				}
				if(districtMap.containsKey(m.getCity())) {
					m.setCity(districtMap.get(m.getCity()));
				}
				if(districtMap.containsKey(m.getDistict())) {
					m.setDistict(districtMap.get(m.getDistict()));
				}
				//更新来源系统
				if(systemMap.containsKey(m.getSystem())) {
					m.setSystem(systemMap.get(m.getSystem()));
				}
				//更新一级分类
				if(firstCategoryMap.containsKey(m.getFirstCategoryId())) {
					m.setFirstCategory(firstCategoryMap.get(m.getFirstCategoryId()));
				}
				//更新二级分类
				if(!secondCategoryMap.containsKey(m.getFirstCategoryId())) {
					secondCategoryMap.put(m.getFirstCategoryId(), getCategoryMap(m.getFirstCategoryId()));
				}
				if(secondCategoryMap.containsKey(m.getFirstCategoryId())) {
					if(secondCategoryMap.get(m.getFirstCategoryId()).containsKey(m.getSecondCategoryId())) {
						m.setSecondCategory(secondCategoryMap.get(m.getFirstCategoryId()).get(m.getSecondCategoryId()));
					}
				}
				//更新组织机构
				if(orgMap.containsKey(m.getOrganization())) {
					m.setOrganization(orgMap.get(m.getOrganization()));
				}
				//更新状态
				if(statusMap.containsKey(m.getStatus().toString())) {
					m.setStatusText(statusMap.get(m.getStatus().toString()));
				}
				//更新是否精选商家
				if(m.getIsHighQualityMerchants() != null && isMap.containsKey(m.getIsHighQualityMerchants().toString())) {
					m.setIsHighQualityMerchantsText(isMap.get(m.getIsHighQualityMerchants().toString()));
				}
				//更新是否夜场
				if(m.getHasNight() != null && isMap.containsKey(m.getHasNight().toString())) {
					m.setHasNightText(isMap.get(m.getHasNight().toString()));
				}
				//更新是否外部商家
				if(m.getIsExternal() != null && isMap.containsKey(m.getIsExternal().toString())) {
					m.setIsExternalText(isMap.get(m.getIsExternal().toString()));
				}
			}
			
			if(list != null && list.size() > 0) {
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.addHeader("Content-Disposition",
						new String("attachment; filename=商家信息.xls".getBytes(), "ISO-8859-1"));
//				ExcelUtil.ExportExcelByBean("商家代码,商家名称,负责人,商家电话,商家服务电话,商家状态,来源系统,所属组织,一级分类,二级分类,省份,城市,区域,是否夜场,精选商家,停车位,营业时间开始,营业时间结束,晚间营业时间开始,晚间营业时间结束,外部商家,商家地址,商家标题,商家描述".split(","),
//						"code,mName,mManager,contactTel,serviceTel,statusText,system,organization,firstCategory,secondCategory,province,city,distict,hasNightText,isHighQualityMerchantsText,parkingNumber,startOperation,endOperation,nightStart,nightEnd,isExternalText,address,title,description".split(","), list,
//						"商家信息", response);
				ExcelUtil.ExportExcelByBean("商家编号,商家名称,负责人,商家电话,商家服务电话,商家状态,来源系统,所属组织,一级分类,二级分类,省份,城市,区域,停车位,营业时间开始,营业时间结束,外部商家,商家地址,商家标题,商家描述".split(","),
						"code,mName,mManager,contactTel,serviceTel,statusText,system,organization,firstCategory,secondCategory,province,city,distict,parkingNumber,startOperation,endOperation,isExternalText,address,title,description".split(","), list,
						"商家信息", response);
				result = "导出成功";
			} else {
				result = "当前查询条件无数据";
			}
		} catch (UnsupportedEncodingException e) {
			throw new MdmException("导出商家列表失败，请重新导出！");
		} catch (IOException e) {
			throw new MdmException("io异常");
		}
		return result;
	}
	
	/**
	 * 获取商家分类Map<Id, Name>
	 * 
	 * @param parentId
	 * @return
	 */
	private Map<String, String> getCategoryMap(String parentId) {
		Map<String, String> map = new HashMap<String, String>();
		List<MerchantCategory> list = mwMapper.queryMerchantType(parentId);
		for(MerchantCategory m : list) {
			map.put(m.getId(), m.getName());
		}
		return map;
	}
	/**
	 * 获取商家所有一级/二级分类
	 * 
	 * @return
	 */
	private List<MerchantCategory> getFirstCategoryList() {
		return mwMapper.queryMerchantType(null);
	}
	private List<MerchantCategory> getSecondCategoryList() {
		return mwMapper.querySecondMerchantType();
	}
	/**
	 * 获取所有商家的code,id
	 * 
	 * @return
	 */
	private Map<String, String> queryCodeIdMap() {
		Map<String, String> codeIdMap = new HashMap<>();
		List<MerchantCodeId> list = mwMapper.queryCodeIdMap();
		for(MerchantCodeId m : list) {
			if(!codeIdMap.containsKey(m.getCode())) {
				codeIdMap.put(m.getCode(), m.getId());
			}
		}
		return codeIdMap;
	}
	/**
	 * 创建CommonTask
	 * 
	 * @param merchant
	 * @return
	 */
	private CommonTask getCommonTask(Merchant merchant) {
		CommonTask task = new CommonTask();
		task.setId(DataUtils.uuid());
		task.setDataId(merchant.getId());
		task.setCreatedBy(MdmConstants.ADMIN_ID);
		task.setCreatedOn(new Date());
		task.setDataType(DataTypeEnum.MERCHANT.getValue());
		task.setTaskType(1);
		task.setStatus(0);
		return task;
	}
	/**
	 * MQ下发
	 * @param logType
	 * @param merchant
	 */
	private void sendMerchantMQ(Integer logType, Merchant merchant) {
		String sysCode = invokeSystemService.querySysCodeById(merchant.getSystemId(), merchant.getSystemCode());
		merchant.setSystemCode(sysCode);
		mqSenderAdapter.sendMQ(logType, DataTypeEnum.MERCHANT.getValue(), merchant);
	}
}

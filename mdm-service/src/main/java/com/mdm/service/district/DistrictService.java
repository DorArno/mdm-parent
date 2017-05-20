/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DistrictService.java 
 * @Prject: mdm-service
 * @Package: com.mdm.service.district 
 * @Description: TODO
 * @author: MAJA005   
 * @date: 2016年9月14日 下午4:36:26 
 * @version: V1.0   
 */
package com.mdm.service.district;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mdm.common.PageResultBean;
import com.mdm.core.bean.es.DistrictEsQuery;
import com.mdm.core.constants.MdmConstants;
import com.mdm.core.enums.DataTypeEnum;
import com.mdm.core.enums.LogTypeEnum;
import com.mdm.core.es.document.DistrictDocument;
import com.mdm.core.es.service.DistrictEsService;
import com.mdm.core.service.sys.StaticDataService;
import com.mdm.core.util.BeanUtils;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.DateUtils;
import com.mdm.core.util.ExcelUtil;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MdmException;
import com.mdm.dao.district.DistrictMapper;
import com.mdm.exportBean.DistrictExport;
import com.mdm.importBean.DistrictCodeId;
import com.mdm.importBean.DistrictImport;
import com.mdm.mq.producter.MQSenderAdapter;
import com.mdm.pojo.CommonTask;
import com.mdm.pojo.District;
import com.mdm.request.DistrictListRequest;
import com.mdm.request.DistrictRequest;
import com.mdm.service.task.CommonTaskService;

/**
 * @ClassName: DistrictService
 * @Description: TODO
 * @author: MAJA005
 * @date: 2016年9月27日 下午4:36:26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DistrictService {
	@Autowired
	private DistrictMapper districtMapper;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private DistrictEsService districtEsService;
	@Autowired
	private CommonTaskService commonTaskService;
	@Autowired
	private MQSenderAdapter mqSenderAdapter;
	
	private static Logger logger = LoggerFactory.getLogger(DistrictService.class);

	/**
	 * @Title: insert
	 * @Description: TODO
	 * @param district
	 * @return
	 * @return: int
	 */
	public int insert(DistrictRequest districtRequest) {
		District districtInfo = new District();
		BeanUtils.copyProperties(districtRequest, districtInfo);
		districtInfo.setId(DataUtils.uuid());
		// districtInfo.setCreatedBy(districtRequest.getUserId());
		// districtInfo.setCreatedBy(districtRequest.getUserId());
		return districtMapper.insert(districtInfo);
	}
	/**
	 * @Title: queryById
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: District
	 */
	public District queryById(String id) {
		if (staticDataService.isOpenSwitch(MdmConstants.MDM_SWITCH, MdmConstants.ES_District_FLAG)) {
			DistrictDocument dd = districtEsService.findOne(id);
			logger.info(
					"DistrictService.queryById(" + id + ") of Elasticsearch result : " + JSONObject.toJSONString(dd));
			District district = new District();
			BeanUtils.copyProperties(dd, district);
			return district;
		} else {
			return districtMapper.queryById(id);
		}
	}
	/**
	 * @Title: queryByParentId
	 * @Description: TODO
	 * @param parentId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @return: List<District>
	 */
	public PageResultBean queryByParentId(DistrictListRequest districtRequest) {
		PageResultBean pageResult = new PageResultBean();
		if (staticDataService.isOpenSwitch(MdmConstants.MDM_SWITCH, MdmConstants.ES_District_FLAG)) {
			logger.info("DistrictService.queryByParentId  from Elasticsearch params : "
					+ JSONObject.toJSONString(districtRequest));
			DistrictEsQuery esQuery = new DistrictEsQuery();
			BeanUtils.copyProperties(districtRequest, esQuery);
			com.mdm.core.bean.common.PageResultBean result = districtEsService.findDistrictList(esQuery);
			pageResult.setList(result.getList());
			pageResult.setTotalCount(result.getTotalCount());
		} else {
			Page<Object> page = PageHelper.startPage(districtRequest.getPageNum(), districtRequest.getPageSize());// 分页与统计总数
			District districtInfo = new District();
			BeanUtils.copyProperties(districtRequest, districtInfo);
			List<District> list = districtMapper.queryByParentId(districtInfo);
			pageResult.setList(list);
			pageResult.setTotalCount(page.getTotal());
		}
		return pageResult;
	}
	/**
	 * @Title: delete
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: int
	 */
	public int delete(String id) {
		return districtMapper.delete(id);
	}
	/**
	 * @Title: update
	 * @Description: TODO
	 * @param district
	 * @return
	 * @return: int
	 */
	public int update(DistrictRequest districtRequest) {
		District districtInfo = new District();
		BeanUtils.copyProperties(districtRequest, districtInfo);
		return districtMapper.update(districtInfo);
	}
	/**
	 * @Title: queryByParentId
	 * @Description: TODO
	 * @return
	 * @return: PageResultBean
	 */
	public List<District> queryList() {
		List<District> list = null;
		if (staticDataService.isOpenSwitch(MdmConstants.MDM_SWITCH, MdmConstants.ES_District_FLAG)) {
			logger.info("DistrictService.queryList  from Elasticsearch  ");
			com.mdm.core.bean.common.PageResultBean result = districtEsService.findDistrictList(new DistrictEsQuery());
			list = (List<District>) result.getList();
		} else {
			list = districtMapper.queryByParentId(null);
		}
		return list;
	}
	/**
	 * @Title: queryByParentId
	 * @Description: TODO
	 * @return
	 * @return: PageResultBean
	 */
	public List<District> queryNodeList(HttpServletRequest request) {
		List<District> list = null;
		if (staticDataService.isOpenSwitch(MdmConstants.MDM_SWITCH, MdmConstants.ES_District_FLAG)) {
			logger.info("DistrictService.queryList  from Elasticsearch  ");
			com.mdm.core.bean.common.PageResultBean result = districtEsService.findDistrictList(new DistrictEsQuery());
			list = (List<District>) result.getList();
		} else {
			District district = new District();
			district.setParentId(request.getParameter("parentId"));
			list = districtMapper.queryByParentId(district);
		}
		return list;
	}
	/**
	 * 导入行政区划数据
	 * @param file
	 * @return
	 * @throws MdmException
	 */
	public Map<String, Object> importDistrict(MultipartFile file) throws MdmException {
		logger.info("导入行政区划开始时间:" + DateUtils.dateToString(new Date(), DateUtils.TIMESTAMP));
		List<DistrictImport> districtImportList = ExcelUtil.importExcel(file, DistrictImport.class, new HashMap<>());
		Map<String, Object> result = new HashMap();
		if (!districtImportList.isEmpty()) {
			result = importBatch(districtImportList);
		}
		logger.info("导入行政区划结束时间:" + DateUtils.dateToString(new Date(), DateUtils.TIMESTAMP));
		return result;
	}
	/**
	 * 批量新增行政区划
	 * @param districtImportList
	 * @return
	 * @throws MdmException
	 */
	public Map<String, Object> importBatch(List<DistrictImport> districtImportList) throws MdmException {
		/**
		 * 套路A： 1. 找出1级节点（parentCode=0, level=1），通过code查询数据库中是否有相同code的节点： if
		 * 无：生成ID并加入districtListToAdd if 有：获取ID并设置给bean，添加到districtListToUpdate 2.
		 * 依次处理2级到N级节点，通过code查询数据库中是否有相同code的节点（同1.） 再按parnetCode查询districtListToAdd中的上级节点的Id： if
		 * districtListToAdd中没有，从数据库中查询获取： if 无：返回：无法找到code:【code】的上级节点。 if 有：将查询到Id设置为parentId 套路B：
		 * 1. 获取数据库中所有code,id，并加入到districtCodeIdMap 2. 依次处理各节点，使用code到districtCodeIdMap是否存在： if
		 * 无：生成Id并加入到districtListToAdd，同时将code, id加入districtCodeIdMap if
		 * 有：获取ID并设置给bean，添加到districtListToUpdate 3. 建立上下级关系，遍历districtListToAdd,
		 * districtListToUpdate if parentCode == 0：设置parentId=0； if parentCode !=
		 * 0：使用code到districtCodeIdMap中查询Id： if 有：设置parentId为districtCodeIdMap查到的Id if
		 * 无：返回：无法找到code:【code】的上级节点
		 */
		// 以下代码按套路B编写
		List<DistrictImport> districtListToAdd = new ArrayList<>();
		List<DistrictImport> districtListToUpdate = new ArrayList<>();
		List<District> addList = new ArrayList<>();
		List<District> updateList = new ArrayList<>();
		List<CommonTask> commonTaskList = new ArrayList<>();
		Map<String, String> districtCodeIdMap = this.queryCodeIdMap();
		for (DistrictImport di : districtImportList) {
			if(di.getCode().equals(di.getParentCode())) {
				throw new MdmException("code:【" + di.getCode() + "】与上级code相同");
			}
			if (districtCodeIdMap.containsKey(di.getCode())) {
				di.setId(districtCodeIdMap.get(di.getCode()));
				districtListToUpdate.add(di);
			} else {
				di.setId(DataUtils.uuid());
				districtListToAdd.add(di);
				districtCodeIdMap.put(di.getCode(), di.getId());
			}
		}
		if (!districtListToAdd.isEmpty()) {
			for (DistrictImport di : districtListToAdd) {
				setParentId(districtCodeIdMap, di);
				District districtInfo = new District();
				BeanUtils.copyProperties(di, districtInfo);
				districtInfo.setVersion(System.currentTimeMillis() + "");
				districtInfo.setCreatedBy(HttpContent.getOperatorId());
				addList.add(districtInfo);
//				commonTaskList.add(getCommonTask(districtInfo));
				mqSenderAdapter.sendMQ(LogTypeEnum.ADD.getValue(), DataTypeEnum.DISTRICT.getValue(), districtInfo);
			}
		}
		if (!districtListToUpdate.isEmpty()) {
			for (DistrictImport di : districtListToUpdate) {
				setParentId(districtCodeIdMap, di);
				District districtInfo = new District();
				BeanUtils.copyProperties(di, districtInfo);
				districtInfo.setVersion(System.currentTimeMillis() + "");
				districtInfo.setModifiedBy(HttpContent.getOperatorId());
				updateList.add(districtInfo);
//				commonTaskList.add(getCommonTask(districtInfo));
				mqSenderAdapter.sendMQ(LogTypeEnum.UPDATE.getValue(), DataTypeEnum.DISTRICT.getValue(), districtInfo);
			}
		}
		//计算层级
		calculateDistrictLevel(addList, updateList);
		
		int addRows = 0, updateRows = 0;
		if (!districtListToAdd.isEmpty()) {
			addRows = districtMapper.batchInsert(addList);
		}
		if (!districtListToUpdate.isEmpty()) {
			districtMapper.batchUpdate(updateList);
			updateRows = updateList.size();
		}
		
		//批量添加commonTask信息，用于MQ等处理
//		commonTaskService.batchInsert(commonTaskList);
		
		Map<String, Object> result = new HashMap<>();
		result.put("addRows", addRows);
		result.put("updateRows", updateRows);
		return result;
	}
	/**
	 * @param districtCodeIdMap
	 * @param district
	 * @throws MdmException
	 */
	private void setParentId(Map<String, String> districtCodeIdMap, DistrictImport district) throws MdmException {
		if (StringUtils.isEmpty(district.getParentCode()) || district.getParentCode().equals("0")) {
			district.setParentId("0");
		} else {
			if (districtCodeIdMap.containsKey(district.getParentCode())) {
				district.setParentId(districtCodeIdMap.get(district.getParentCode()));
			} else {
				throw new MdmException("无法找到code:【" + district.getCode() + "】的上级节点");
			}
		}
	}
	/**
	 * 计算导入的行政区划层级
	 * 
	 * @param addList
	 * @param updateList
	 */
	private Map<String, District> tempMap = new HashMap<>();
	private void calculateDistrictLevel(List<District> addList, List<District> updateList) {
		if(!addList.isEmpty()) {
			for(District d : addList) {
				if(d.getParentId().equals("0")) {
					d.setLevel(1);
					continue;
				}
				calculateLevel(addList, updateList, d);
			}
		}
		if(!updateList.isEmpty()) {
			for(District d : updateList) {
				if(d.getParentId().equals("0")) {
					d.setLevel(1);
					continue;
				}
				calculateLevel(addList, updateList, d);
			}
		}
	}
	private void calculateLevel(List<District> addList, List<District> updateList, District current) {
		boolean flag = false;
		if(!addList.isEmpty()) {
			for(District d : addList) {
				if(d.getId().equals(current.getParentId())) {
					flag = true;
					if(d.getLevel() != null && d.getLevel() > 0) {
						current.setLevel(d.getLevel() + 1);
						setDistrictPath(current, d);
						tempMap.put(current.getId(), current);
					} else {
						calculateLevel(addList, updateList, d);
					}
					break;
				}
			}
		}
		if(!flag) {
			for(District d : updateList) {
				if(d.getId().equals(current.getParentId())) {
					if(null != d.getLevel() &&	d.getLevel() > 0) {
						current.setLevel(d.getLevel() + 1);
						setDistrictPath(current, d);
						tempMap.put(current.getId(), current);
						flag =  true;
					} else {
						District temp;
						if(tempMap.containsKey(d.getId())) {
							temp = tempMap.get(d.getId());
						} else {
							temp = this.queryById(d.getId());
							tempMap.put(temp.getId(), temp);
						}
						if(temp != null) {
							current.setLevel(temp.getLevel() + 1);
							setDistrictPath(current, temp);
							tempMap.put(current.getId(), current);
							flag = true;
						}
					}
					break;
				}
			}
		}
		if(!flag) {
			District temp;
			if(tempMap.containsKey(current.getParentId())) {
				temp = tempMap.get(current.getParentId());
			} else {
				temp = this.queryById(current.getParentId());
				tempMap.put(temp.getId(), temp);
			}
			if(temp != null) {
				current.setLevel(temp.getLevel() + 1);
				setDistrictPath(current, temp);
				tempMap.put(current.getId(), current);
			}
		}
	}

	/**
	 * 设置Path值
	 * @param current
	 * @param parent
	 */
	private void setDistrictPath(District current, District parent) {
		if(!org.springframework.util.StringUtils.isEmpty(parent.getPath())) {
			current.setPath(parent.getPath() + "," + current.getId());
		}
	}
	
	/**
	 * 创建CommonTask
	 * 
	 * @param district
	 * @return
	 */
	private CommonTask getCommonTask(District district) {
		CommonTask task = new CommonTask();
		task.setId(DataUtils.uuid());
		task.setDataId(district.getId());
		task.setCreatedBy(MdmConstants.ADMIN_ID);
		task.setCreatedOn(new Date());
		task.setDataType(DataTypeEnum.DISTRICT.getValue());
		task.setTaskType(1);
		task.setStatus(0);
		return task;
	}
	/**
	 * 导出行政区划数据到Excel
	 * @param response
	 * @return
	 * @throws MdmException
	 */
	public String export(HttpServletResponse response, DistrictListRequest districtRequest) throws MdmException {
		String result = "";
		try {
			// 查询
			District districtInfo = new District();
			BeanUtils.copyProperties(districtRequest, districtInfo);
			List<DistrictExport> districtList = districtMapper.exportDistrictList(districtInfo);
			if (districtList != null && districtList.size() > 0) {
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.addHeader("Content-Disposition",
						new String("attachment; filename=行政区划信息.xls".getBytes(), "ISO-8859-1"));
				ExcelUtil.ExportExcelByBean("上级地区,编码,名称,邮政编码,经度,纬度,层级,描述".split(","),
						"parentName,code,name,postCode,longitude,latitude,level,description".split(","), districtList,
						"行政区划信息", response);
				result = "导出成功";
			} else {
				result = "当前查询条件无数据";
			}
		} catch (UnsupportedEncodingException e) {
			throw new MdmException("导出行政区划列表失败，请重新导出！");
		} catch (IOException e) {
			throw new MdmException("io异常");
		}
		return result;
	}
	/**
	 * 获取数据库中所有数据的code, id
	 * @return
	 */
	public Map<String, String> queryCodeIdMap() {
		Map<String, String> districtCodeIdMap = new HashMap<>();
		List<DistrictCodeId> districtCodeIdList = districtMapper.queryCodeIdMap();
		for (DistrictCodeId d : districtCodeIdList) {
			if (!districtCodeIdMap.containsKey(d.getCode())) {
				districtCodeIdMap.put(d.getCode(), d.getId());
			}
		}
		return districtCodeIdMap;
	}

	/**
	 * 获取所有行政区划Map，Key = Id, Value = Name
	 *
	 * @return
	 */
	public Map<String, String> getDistrictMap() {
		List<DistrictCodeId> districtList = queryAllLevel3List();
		Map<String, String> districtMap = new HashMap<>();
		if(districtList != null && !districtList.isEmpty()) {
			for(DistrictCodeId d : districtList) {
				if(!districtMap.containsKey(d.getId())) {
					districtMap.put(d.getId(), d.getName());
					//logger.info(d.getId() + ", " + d.getName());
				}
			}
		}
		return districtMap;
	}

	/**
	 * 获取数据库中所有数据的code, id
	 * @return
	 */
	public List<DistrictCodeId> queryAllLevel3List() {
		return districtMapper.queryAllLevel3List();
	}
}

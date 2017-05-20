/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月11日 下午5:37:21 *
**/ 
package com.mdm.service.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdm.dao.write.community.CommunityWriteMapper;
import com.mdm.importBean.CommunityCodeId;
import com.mdm.pojo.Community;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommunityService {

	@Autowired
	private CommunityWriteMapper communityWriteMapper;
	
	public List<Community> queryCommunityList(String areaId) {
		return communityWriteMapper.queryCommunityByAreaid(areaId);
	}
	
	public List<CommunityCodeId> queryCodeIdList() {
		return communityWriteMapper.queryCodeIdMap();
	}

}

/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月9日 下午2:34:16 *
**/
package com.mdm.service.initpath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.mdm.dao.write.initpath.InitPathWriteMapper;
import com.mdm.pojo.InitPathEntity;
import com.mdm.pojo.Organazation;

@Service
@Transactional(rollbackFor = Exception.class)
public class InitPathService {

	@Autowired
	private InitPathWriteMapper initPathWriteMapper;

	public boolean initPath(String tablename, String parentIdColumnName, String pathColumnName) {
		List<InitPathEntity> allList = initPathWriteMapper.queryAllList(tablename, parentIdColumnName, pathColumnName);
		for (InitPathEntity initPath : allList) {
			if (StringUtil.isNotEmpty(initPath.getParentId())) {
				StringBuffer pathStr = new StringBuffer("/");
				int level = 1;
				if (initPath.getParentId() != null) {
					pathStr.append(initPath.getParentId());
					level = getParentPath(level, pathStr, tablename, parentIdColumnName, pathColumnName, initPath.getParentId());
					initPathWriteMapper.update(tablename, pathColumnName, pathStr.toString(), level, initPath.getId());
				}
			}
		}
		return false;
	}

	private int getParentPath(Integer level, StringBuffer pathStr, String tablename, String parentIdColumnName, String pathColumnName, String parentid) {
		InitPathEntity parent = initPathWriteMapper.findById(tablename, parentIdColumnName, pathColumnName, parentid);
		if (parent != null && StringUtil.isNotEmpty(parent.getParentId())) {
			pathStr.insert(1, parent.getParentId() + "/");
			level++;
			getParentPath(level, pathStr, tablename, parentIdColumnName, pathColumnName, parent.getParentId());
		}
		return level;
	}

}

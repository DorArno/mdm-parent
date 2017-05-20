package com.mdm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mdm.core.util.MdmException;
import com.mdm.dao.write.user.UserWriteMapper;
import com.mdm.pojo.TestBean;

/**
 * 测试service
 * @author gaod003
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TestService {
	private static Logger logger = LoggerFactory.getLogger(TestService.class);
	@Autowired
	private UserWriteMapper userTestWriteMapper;

	/**
	 * @Title: checkUserExist
	 * @Description: TODO
	 * @param user
	 * @return
	 * @throws MdmException
	 * @return: int
	 */
	public int checkUserExist(TestBean user) throws MdmException {
		if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
			throw new MdmException("用户名或密码不能为空");
		}
		return 1;
		// return userTestWriteMapper.checkUserExist(user.getUserName(), user.getPassword());
	}
}

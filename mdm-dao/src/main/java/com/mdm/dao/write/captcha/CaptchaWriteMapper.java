/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月17日 下午5:21:57 *
**/ 
package com.mdm.dao.write.captcha;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.pojo.VerifyCode;


@Mapper
public interface CaptchaWriteMapper {

	int insert(VerifyCode captcha);
	
	int deleteByCellphone(String cellphone);
	
	VerifyCode queryById(String id);

}

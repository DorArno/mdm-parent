/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月17日 下午4:52:30 *
**/
package com.mdm.service.captchal;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.mdm.core.restful.RestUtil;
import com.mdm.core.util.DataUtils;
import com.mdm.core.util.DateUtils;
import com.mdm.core.util.HttpContent;
import com.mdm.core.util.MD5Util;
import com.mdm.core.util.MdmException;
import com.mdm.dao.write.captcha.CaptchaWriteMapper;
import com.mdm.mq.producter.MQSenderAdapter;
import com.mdm.pojo.VerifyCode;
import com.mdm.request.CaptchaCheckRequest;
import com.mdm.request.CaptchaRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class CaptchaService {
	private static Logger logger = LoggerFactory.getLogger(CaptchaService.class);
	@Autowired
	private CaptchaWriteMapper captchaWriteMapper;

	@Value("${sms.server.host}")
	private String serverHost;

	@Value("${sms.single.url}")
	private String singleUrl;

	@Value("${sms.partnerId.key}")
	private String partKey;

	@Value("${sms.partnerId.encryptKey}")
	private String encryptKey;

	@Value("${sms.failuretime}")
	private int failuretime;
	
 
	public JSONObject createCaptcha(CaptchaRequest captchalRequest) throws MdmException {

		if(StringUtil.isEmpty(captchalRequest.getPhoneNum())){
			throw new MdmException(20, "手机号不能为空");
		}
		String timestamp = DateUtils.getTodayStr(DateUtils.TIMESTAMP);
		String code = DataUtils.random(6);
		Calendar calendar1 = Calendar.getInstance();
		String smsContent =  "您于" +calendar1.get(Calendar.YEAR)+ "年 " +(calendar1.get(Calendar.MONTH) +1)
		+"月 " + (calendar1.get(Calendar.DAY_OF_MONTH))+"日申请的验证码为"+ code+" 。验证码有效时间为" + failuretime+ "分钟";

		JSONObject object = new JSONObject();
		object.put("partnerId", partKey);
		object.put("timeStamp", timestamp);
		object.put("encryptKey", encryptKey);
		object.put("encryptStr", getEncryptStr(encryptKey, timestamp, captchalRequest.getPhoneNum() + smsContent));
		JSONObject param = new JSONObject();
		param.put("mobile", captchalRequest.getPhoneNum());
		param.put("text", smsContent);
		object.put("param", param);
		String url = serverHost + singleUrl;
//		String restStr = RestUtil.sendData(url, "POST", object.toString(), 60000, null, null, 0);
		String restStr = RestUtil.doJsonPost(url,  object.toJSONString(), 60000);
		logger.info("sendSMS:" + restStr);
		if (StringUtil.isNotEmpty(restStr)) {
			JSONObject resultObj = JSONObject.parseObject(restStr);
			if (resultObj.containsKey("resultCode") && resultObj.getString("resultCode").equals("1")) {
				captchaWriteMapper.deleteByCellphone(captchalRequest.getPhoneNum());
				VerifyCode captcha = new VerifyCode();
				captcha.setId(DataUtils.uuid());
				captcha.setCellPhone(captchalRequest.getPhoneNum());
				captcha.setCode(code);
				captcha.setCreatedBy(HttpContent.getOperatorId());
				captcha.setCreatedOn(new Date());
				captcha.setModifiedBy(HttpContent.getOperatorId());
				captcha.setCreateTime(new Date());
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MINUTE, failuretime);
				captcha.setFailureTime(calendar.getTime());
				captcha.setModifiedOn(new Date());
				captcha.setIsDeleted(0);
				captcha.setSourceSystemId(captchalRequest.getCallSource() + "");
				int resultcode = insert(captcha);
				JSONObject resultJSON = new JSONObject();
				resultJSON.put("captchaId", captcha.getId());
				return resultJSON;
			} else {
				throw new MdmException("发送失败");
			}
		} else {
			throw new MdmException("发送失败");
		}
	}

	public JSONObject checkCaptcha(CaptchaCheckRequest checkRequest) throws MdmException{
		JSONObject jsonObject = new JSONObject();
		String checkkey = "checkResult";
		boolean result =  checkCaptchaByCaptchaIdAndCode(checkRequest.getCaptchaId(), checkRequest.getCaptcha(), checkRequest.getPhone());
		jsonObject.put(checkkey, result);
		return jsonObject;
	}
	
	public boolean checkCaptchaByCaptchaIdAndCode(String captchaId, String code, String phoneNum) throws MdmException{
		VerifyCode verifyCode = captchaWriteMapper.queryById(captchaId);
		if(verifyCode == null){
			throw new  MdmException(89,"验证码不正确");
		}
		long now = new Date().getTime();
		long create = verifyCode.getCreatedOn().getTime();
		long failure = verifyCode.getFailureTime().getTime();
		if(!(create < now && now < failure)){
			throw new MdmException( 87, "验证码已失效");
		}
		if(!(verifyCode.getCode().equalsIgnoreCase(code) &&  verifyCode.getCellPhone().equals(phoneNum))){
			throw new MdmException(89, "验证码不正确");
		}
		return true;
	}

	public int insert(VerifyCode captcha) {
		int result = captchaWriteMapper.insert(captcha);
		return result;
	}

	private String getEncryptStr(String key, String timestamp, String param) {
		List<String> params = new ArrayList<String>();
		params.add(key);
		params.add(timestamp);
		params.add(param);

		StringBuffer sb = new StringBuffer();
		for (String str : params) {
			sb.append(str);
		}
		return MD5Util.MD5(sb.toString()).toUpperCase();
	}

}

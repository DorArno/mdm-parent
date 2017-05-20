package com.mdm.controller;

import java.util.Base64;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.mdm.pojo.TestBean;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping("/")
public class TestRestFulController {
	
	 	@RequestMapping("testRestFul")
	 	@ResponseBody
	    public byte[] rest(@RequestParam(value="key") String key){
	 		String str = new String(Base64.getDecoder().decode(key));
	 		TestBean testBean = JSON.parseObject(str, TestBean.class);
	 		testBean.setPassword("test ok");
			String json = JSON.toJSONString(testBean);
			byte[] b = Base64.getEncoder().encode(json.getBytes());
			return b;
	      
	    }
	 	
	 	/*
	 	@RequestMapping("testRestFul1")
	 	@ResponseBody
	    public byte[] rest1(@RequestParam(value="key") String key){
	 		String str = new String(Base64.getDecoder().decode(key));
			String json = JSON.toJSONString(this.execute(str));
			byte[] b = Base64.getEncoder().encode(json.getBytes());
			return b;
	       // return new ModelAndView(new MappingJackson2JsonView(), this.getSuccessResult(testService.checkUserExist(user)));
	    }
	 	
	 	public Object execute(String str){
	 		TestBean testBean = JSON.parseObject(str, TestBean.class);
	 		testBean.setPassword("test ok");
	 		return testBean; 
	 		
	 		
	 	}
	*/
}

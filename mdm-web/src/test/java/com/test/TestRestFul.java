package com.test;

import java.io.IOException;



import com.arvato.operation.platform.tsg.base.core.utils.JsonUtil;
import com.arvato.operation.platform.tsg.util.RestUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;


/**
 * Hello world!
 *
 */

public class TestRestFul 
{
    public static void main( String[] args ) throws JsonGenerationException, JsonMappingException, IOException
    {
    	User user = new User();
    	
    	user.setUser("你好");
    	
    	user.setPassword("password");
    	
    	String param = JsonUtil.toJsonString(user);
   	
    	String restStr = RestUtil.sendData("http://localhost:8080/mdm/testRestFul/", "GET", param, 2000, null, null, 0);
    	
    	System.out.println(restStr);
    	
    }
}

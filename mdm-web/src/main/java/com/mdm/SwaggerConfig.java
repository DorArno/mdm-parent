/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: SwaggerConfig.java 
 * @Prject: mdm-web
 * @Package: com.mdm 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年9月19日 下午9:52:41 
 * @version: V1.0   
 */
package com.mdm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** 
 * @ClassName: SwaggerConfig 
 * @Description: sw配置
 * @author: gaod003
 * @date: 2016年9月19日 下午9:52:41  
 */
@Configuration
@EnableSwagger2
//@EnableWebMvc
public class SwaggerConfig{
	
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
//                .pathProvider(new BasePathAwareRelativePathProvider("/mdm"))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mdm.controller"))
                .paths(PathSelectors.any())
                .build();
//			    .securitySchemes(getSecuritySchemes()) ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MDM RESTful APIs")
//                .description("")
//                .termsOfServiceUrl("")
//                .contact("mdm")
                .version("1.0")
                .build();
    }
    
//    private List<SecurityScheme> getSecuritySchemes(){
//    	
//    	List<SecurityScheme> list = new ArrayList<SecurityScheme>();
//    	list.add(new ApiKey("authorization", "authorization", "header"));
//    	list.add(new ApiKey("operatorId", "operatorId", "header"));
//    	
//    	return list;
//    }


//
//	private ApiKey apiKey() {
//		return new ApiKey("mykey", "api_key", "header");
//	}


    @Bean
    SecurityConfiguration security() {
      return new SecurityConfiguration(
          "test-app-client-id",
          "test-app-client-secret",
          "test-app-realm",
          "test-app",
          "apiKey",
          ApiKeyVehicle.HEADER, 
          "authorization", 
          "," /*scope separator*/);
    }
    
    @Bean
    UiConfiguration uiConfig() {
      return new UiConfiguration(
          "validatorUrl",// url
          "none",       // docExpansion          => none | list
          "alpha",      // apiSorter             => alpha
          "schema",     // defaultModelRendering => schema
          false,        // enableJsonEditor      => true | false
          true);         // showRequestHeaders    => true | false
    }

}

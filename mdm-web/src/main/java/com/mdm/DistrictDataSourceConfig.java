/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: DistrictDataSourceConfig.java 
 * @Prject: mdm-web
 * @Package: com.mdm 
 * @Description: TODO
 * @author: gaod003   
 * @date: 2016年9月27日 上午9:38:08 
 * @version: V1.0   
 */
package com.mdm;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.github.pagehelper.PageInterceptor;

/**
 * @ClassName: DistrictDataSourceConfig
 * @Description: TODO
 * @author: gaod003
 * @date: 2016年9月27日 上午9:38:08
 */
@Configuration
@MapperScan(basePackages = "com.mdm.dao.district", sqlSessionFactoryRef = "sqlSessionFactoryDistrict")
public class DistrictDataSourceConfig {
	@Bean(name = "dataSourceDistrict")
	@ConfigurationProperties(prefix = "districtdb")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	@Bean(name = "sqlSessionFactoryDistrict")
	public SqlSessionFactory sqlSessionFactoryDistrict(@Qualifier("dataSourceDistrict") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		// 增加分页插件
		sessionFactoryBean.setPlugins(new Interceptor[] { pager() });
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mybatis/district/*.xml"));
		return sessionFactoryBean.getObject();
	}
	@Bean
	public PageInterceptor pager() {
		PageInterceptor pageHelper = new PageInterceptor();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "false");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}
	
	protected void doBeforeSqlSessionCreated(DataSource dataSource, ConfigurableApplicationContext appctx) {
	}
	
}

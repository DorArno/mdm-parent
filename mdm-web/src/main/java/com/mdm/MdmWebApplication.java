package com.mdm;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.github.pagehelper.PageInterceptor;
import com.mdm.aop.MdmInterceptor;
import com.mdm.aop.MdmInterceptorInit;
import com.mdm.aop.PermissionInterceptor;
import com.mdm.sso.MDMAuthenticationFilter;
import com.mdm.sso.MDMCasTicketValidationFilter;

@Configuration
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@ComponentScan(basePackages = "com.mdm")
@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
@MapperScan(basePackages = "com.mdm.**.dao.write", sqlSessionFactoryRef = "sqlSessionFactoryWrite")
public class MdmWebApplication extends WebMvcConfigurerAdapter {
	
	
	@Value("${ssoServerUrl}")
	private String ssoServerUrl;
	
	
	@Value("${localServerUrl}")
	private String localServerUrl;
	
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(MdmWebApplication.class, args);
//		new MyApplicationContextUtil().setApplicationContext(applicationContext);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public ServletListenerRegistrationBean singleSignOutHttpSession(){
		ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
		servletListenerRegistrationBean.setListener(new org.jasig.cas.client.session.SingleSignOutHttpSessionListener());
		servletListenerRegistrationBean.setOrder(1);
		return servletListenerRegistrationBean;
	}
	
	
	@Bean  
    public FilterRegistrationBean filterRegistrationBean4(){ 
		SingleSignOutFilter myFilter = new SingleSignOutFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();  
        filterRegistrationBean.setFilter(myFilter);  
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.setEnabled(true);  
        filterRegistrationBean.addUrlPatterns("/*");  
        return filterRegistrationBean;  
    }
	
	@Bean  
    public FilterRegistrationBean filterRegistrationBean5(){
		MDMAuthenticationFilter myFilter = new MDMAuthenticationFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(); 
        filterRegistrationBean.addInitParameter("serverName", localServerUrl);
        filterRegistrationBean.addInitParameter("casServerLoginUrl", ssoServerUrl+"login");
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.setFilter(myFilter);  
        filterRegistrationBean.setEnabled(true);  
        filterRegistrationBean.addUrlPatterns("/*"); 
        return filterRegistrationBean;  
    } 
	
	@Bean  
    public FilterRegistrationBean filterRegistrationBean1(){
		MDMCasTicketValidationFilter myFilter = new MDMCasTicketValidationFilter();
		//myFilter.setService("http://10.11.21.5:8080/sso");
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.addInitParameter("casServerUrlPrefix", ssoServerUrl);
        filterRegistrationBean.addInitParameter("serverName", localServerUrl);
        filterRegistrationBean.setFilter(myFilter);  
        filterRegistrationBean.setEnabled(true); 
        filterRegistrationBean.setOrder(4);
        filterRegistrationBean.addUrlPatterns("/*");  
        return filterRegistrationBean;  
    } 
	
	@Bean  
    public FilterRegistrationBean filterRegistrationBean2(){
		HttpServletRequestWrapperFilter myFilter = new HttpServletRequestWrapperFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();  
        filterRegistrationBean.setFilter(myFilter);  
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setOrder(5);
        filterRegistrationBean.addUrlPatterns("/*");  
        return filterRegistrationBean;  
    }
	
	@Bean  
    public FilterRegistrationBean filterRegistrationBean3(){ 
		AssertionThreadLocalFilter myFilter = new AssertionThreadLocalFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();  
        filterRegistrationBean.setFilter(myFilter);  
        filterRegistrationBean.setEnabled(true); 
        filterRegistrationBean.setOrder(6);
        filterRegistrationBean.addUrlPatterns("/*");  
        return filterRegistrationBean;  
    }

	/*@Bean  
    public FilterRegistrationBean filterRegistrationBean(MyFilter myFilter){  
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();  
        filterRegistrationBean.setFilter(myFilter);  
        filterRegistrationBean.setEnabled(true); 
        filterRegistrationBean.setOrder(7);
        filterRegistrationBean.addUrlPatterns("/*");  
        return filterRegistrationBean;  
    }
	*/
	private static String[] excludePaths = { "/error", "/app/**", "/v2/api-docs","/swagger-resources","/","/sign",
			"/configuration/**","/js/**", "/resources/**","/static/**", "/*.js", "/*.less", "/webjars/**", "/*.txt", "/*.ico", "/*.html",
			"/*.xls","/**/import**","/**/task**","/**/export**","/captchal/**","/global/**","/login/**","/tree/**","/module/**","/invokeSystem/**", "/staticdata**","/staticdata/**", "/tenement/**",
			"/user/CheckPhoneNum", "/user/UserRegister", "/user/ForgetPassword", "/organazation/createRandomCode", "/initPath", "/community", "/user/UserUpdatePhone",
			"/businessLog", "/user/CaptchaUpdateUserPassword"};

	private static String[] excludePathsPre = { "/error", "/app/**","/swagger-resources","/","/sign",
		"/configuration/**","/js/**", "/resources/**","/static/**", "/*.js", "/*.less", "/webjars/**", "/*.txt", "/*.ico", "/*.html"};
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MdmInterceptorInit()).addPathPatterns("/**")// 要拦截的请求
		.excludePathPatterns(excludePathsPre);// 不拦截的请求
		registry.addInterceptor(new MdmInterceptor()).addPathPatterns("/**")// 要拦截的请求
				.excludePathPatterns(excludePaths);// 不拦截的请求
		// 权限拦截器
		registry.addInterceptor(new PermissionInterceptor()).addPathPatterns("/**")// 要拦截的请求
		.excludePathPatterns(excludePaths);// 不拦截的请求
	}

	@Bean(name = "dataSourceWrite")
	@ConfigurationProperties(prefix = "mdmdb")
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "sqlSessionFactoryWrite")
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceWrite") DataSource dataSource,
			ConfigurableApplicationContext appctx, @Value("${mdmdb.driverClassName}") String aaa) throws Exception {
		doBeforeSqlSessionCreated(dataSource, appctx);
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		// 增加分页插件
		//sessionFactoryBean.setPlugins(new Interceptor[] { pageHelper() });
		sessionFactoryBean.setPlugins(new Interceptor[] { pager() });
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mybatis/write/*/*Mapper.xml"));
		return sessionFactoryBean.getObject();
	}
	
	/*@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "false");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}*/
	
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
	
	@Bean
	@Primary
	public PlatformTransactionManager transactionManager(@Qualifier("dataSourceWrite") DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
		return dataSourceTransactionManager;
	}
	
	/**
	 * @Title: containerCustomizer
	 * @Description: 配置404，500错误页面
	 * @return
	 * @return: EmbeddedServletContainerCustomizer
	 */
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return container -> {
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
			container.addErrorPages(error404Page, error500Page);
		};
	}
	
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
	   TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
	   factory.addContextCustomizers((e)->e.addWelcomeFile("/test.html"));
	   return factory;
	}
	
	@Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5242880);
        return multipartResolver;
    }
	
	
	
	
}

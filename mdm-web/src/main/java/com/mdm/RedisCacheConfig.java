/**   
 * Copyright © 2016 Arvato. All rights reserved.
 * 
 * @Title: RedisCacheConfig.java 
 * @Prject: mdm-web
 * @Package: com.mdm 
 * @Description: redis缓存配置
 * @author: gaod003   
 * @date: 2016年9月16日 上午10:31:45 
 * @version: V1.0   
 */
package com.mdm;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @ClassName: RedisCacheConfig
 * @Description: redis缓存配置
 * @author: gaod003
 * @date: 2016年9月16日 上午10:31:45
 */
@Configuration
@EnableCaching
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 8)
public class RedisCacheConfig extends CachingConfigurerSupport {
	/**
	 * @Title: keyGenerator
	 * @Description: key生成策略:默认是按照类路径+方法名的形式，在具体的@Cacheable缓存使用时可以自定义key格式
	 * @return
	 * @return: KeyGenerator
	 */
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(":");
				sb.append(method.getName());
//				for (Object obj : params) {
//					sb.append(obj.toString());
//				}
				return sb.toString();
			}
		};
	}

	/**
	 * @Title: cacheManager
	 * @Description: 管理缓存
	 * @param redisTemplate
	 * @return
	 * @return: CacheManager
	 */
	@SuppressWarnings("rawtypes")
	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate) {
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
		// 设置缓存过期时间
		// rcm.setDefaultExpiration(60);//秒
		// 设置value的过期时间
		// Map<String,Long> map=new HashMap();
		// map.put("test",60L);
		// rcm.setExpires(map);
		return rcm;
	}
	/**
	 * @Title: redisTemplate
	 * @Description: redisTemplate配置
	 * @param factory
	 * @return
	 * @return: RedisTemplate<String,String>
	 */
	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate(factory);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}
}

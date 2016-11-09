package com.transcoderG;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * http://yakolla.tistory.com/42
 * http://jdm.kr/blog/202
 * http://arcsit.tistory.com/entry/SpringRedis-%EC%8A%A4%ED%94%84%EB%A7%81%EA%B3%BC-%EB%A0%88%EB%94%94%EC%8A%A4-%EC%97%B0%EB%8F%99
 * 
 * @author user
 *
 */
@Configuration
public class RedisConfig {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    public RedisProperties redisProperties;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		log.info("redisHostName {} redisPort {}", redisProperties.getHost(), redisProperties.getPort());
		factory.setHostName(redisProperties.getHost());
		factory.setPort(redisProperties.getPort());
		factory.setTimeout(0);
		factory.setUsePool(true);
		return factory;
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setEnableTransactionSupport(true);
		
		// http://stackoverflow.com/questions/13215024/weird-redis-key-with-spring-data-jedis
		redisTemplate.setDefaultSerializer(stringRedisSerializer);
//		redisTemplate.setKeySerializer(stringRedisSerializer);
//		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		return redisTemplate;
	}
	
	@Bean
	public StringRedisTemplate stringRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
		stringRedisTemplate.setEnableTransactionSupport(true);
		return stringRedisTemplate;
	}

	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
		return redisCacheManager;
	}
}

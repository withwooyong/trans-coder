package com.transcoderG.redis;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.transcoderG.RedisConfig;

@Service
public class TranscoderGRedisService {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
    private RedisConfig config;
	
	/**
	 * 
	 * @param key
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String redisGet(String key) throws JsonParseException, JsonMappingException, IOException {
		
		RedisTemplate<Object, Object> template = config.redisTemplate();
		ValueOperations<Object, Object> valueOps = template.opsForValue();
		log.info("key={}", key);
		String value = (String)valueOps.get(key);
		log.info("value={}", value);
		
//		JedisConnectionFactory jedisConnectionFactory = jedisConnectionFactory();
//		jedisConnectionFactory.setHostName("119.149.188.226");
//		jedisConnectionFactory.setPort(6379);
//		jedisConnectionFactory.afterPropertiesSet();
//		RedisTemplate<String, String> template = redisTemplate(jedisConnectionFactory);
//		
//		ValueOperations<String, String> valueOps = template.opsForValue();
//		ListOperations<String, String> listOps = template.opsForList();
//		
//		RedisOperations<String, String> redis = listOps.getOperations();        
//        Set<String> setKeys = redis.keys("FFMPEG.COMMAND" + "*");
//		
//		String[] strKeys = setKeys.toArray(new String[setKeys.size()]);
//		
//		for (int i = 0; i < strKeys.length; i++) {
//			System.out.println(strKeys[i].substring(strKeys[i].indexOf("FFMPEG.COMMAND" + "-")));
//			System.out.println(valueOps.get(strKeys[i].substring(strKeys[i].indexOf("FFMPEG.COMMAND" + "-"))));
//		}
		return value;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String redisGetSet(String key, String value) throws JsonParseException, JsonMappingException, IOException {
		RedisTemplate<Object, Object> template = config.redisTemplate();
		ValueOperations<Object, Object> valueOps = template.opsForValue();
		return (String)valueOps.getAndSet(key, value);
	}	
}



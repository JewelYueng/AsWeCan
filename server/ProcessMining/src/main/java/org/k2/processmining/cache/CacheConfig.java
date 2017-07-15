package org.k2.processmining.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nyq on 2017/6/23.
 */
//@Configuration
//@EnableCaching
public class CacheConfig {

    public static final String NET_CACHE = "SimpleHeuristicsNet";
    public static final String RAW_LOG_CACHE = "RawLog";
    public static final String NORMAL_LOG_CACHE = "NormalLog";
    public static final String EVENT_LOG_CACHE = "EventLog";

    private static final Map<String, Long> cacheMap = new HashMap<>();

    static {
        cacheMap.put(NET_CACHE, 10 * 60L);
        cacheMap.put(RAW_LOG_CACHE, 30 * 60L);
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setUsePrefix(true);
        redisCacheManager.setExpires(cacheMap);
        return redisCacheManager;
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                      RedisSerializer redisSerializer) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName("127.0.0.1");
        connectionFactory.setPort(6739);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
//        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
//        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//        redisSerializer.setObjectMapper(objectMapper);
//        return redisSerializer;
        return new JdkSerializationRedisSerializer();
    }

}

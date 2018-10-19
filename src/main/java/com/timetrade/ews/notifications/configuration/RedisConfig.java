package com.timetrade.ews.notifications.configuration;

import java.nio.charset.Charset;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timetrade.ews.notifications.model.PushSubscriptionMeta;
import com.timetrade.ews.notifications.model.history.HistoryRecord;

@Configuration
public class RedisConfig {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(EwsProperties ewsProperties) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(ewsProperties.getRedisDb().getHost());
        connectionFactory.setPort(ewsProperties.getRedisDb().getPort());
        connectionFactory.setUsePool(true);
        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, PushSubscriptionMeta> subscriptionsTemplate(
            JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, PushSubscriptionMeta> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer(CHARSET));
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(PushSubscriptionMeta.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, String> checkInsTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer(CHARSET));
        redisTemplate.setValueSerializer(new StringRedisSerializer(CHARSET));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Set<HistoryRecord>> historyTemplate(JedisConnectionFactory jedisConnectionFactory, 
            ObjectMapper objectMapper) {
        RedisTemplate<String, Set<HistoryRecord>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer(CHARSET));
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(
                objectMapper.getTypeFactory().constructCollectionType(Set.class, HistoryRecord.class)));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, String> reattemptTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer(CHARSET));
        redisTemplate.setValueSerializer(new StringRedisSerializer(CHARSET));
        return redisTemplate;
    }

}

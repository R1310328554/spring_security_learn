package com.lk.learn.springboot.demo.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.expression.Maps;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        List<String> nodes = redisProperties.getCluster().getNodes();
        String[] serverArray = redisProperties.getHost().split(",");// 获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(nodes);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 最大空闲连接数, 默认8个
        poolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        poolConfig.setMaxTotal(500);
        // 最小空闲连接数, 默认0
        poolConfig.setMinIdle(0);
        LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(15))
                .poolConfig(poolConfig)
                .shutdownTimeout(Duration.ZERO)
                .build();
        return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
    }
    /**     * 注入redis template     *     * @return     */
    @Bean
    @Qualifier("redisTemplate")
    public RedisTemplate redisTemplate(LettuceConnectionFactory lettuceConnectionFactory, Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
    /**     * redis cache manager     *     * @return     */
    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory, ObjectProvider<List<RedisCacheConfigurationProvider>> configurationProvider) {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        List<RedisCacheConfigurationProvider> configurations = configurationProvider.getIfAvailable();
        if (!CollectionUtils.isEmpty(configurations)) {
            for (RedisCacheConfigurationProvider configuration : configurations) {
                redisCacheConfigurationMap.putAll(configuration.resolve());
            }
        }
        RedisCacheManager cacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
//                .cacheDefaults(resovleRedisCacheConfiguration(Duration.ofSeconds(300), JacksonHelper.genJavaType(Object.class)))
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
        return cacheManager;
    }

    private static RedisCacheConfiguration resovleRedisCacheConfiguration(Duration duration, JavaType javaType) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(javaType)))
                .entryTtl(duration);
    }

//    @Bean
//    public RedisLockRegistry redisLockRegistry(LettuceConnectionFactory lettuceConnectionFactory) {
//        return new RedisLockRegistry(lettuceConnectionFactory, "recharge-plateform", 60000 * 20);
//    }

    /**     * 配置一个序列器, 将对象序列化为字符串存储, 和将对象反序列化为对象     */
    @Bean
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    public static abstract class RedisCacheConfigurationProvider {
        // key = 缓存名称， value = 缓存时间 和 缓存类型
        protected Map<String, Pair<Duration, JavaType>> configs;
        protected abstract void initConfigs();
        public Map<String, RedisCacheConfiguration> resolve() {
            initConfigs();
            Assert.notEmpty(configs, "RedisCacheConfigurationProvider 配置不能为空...");
            Map<String, RedisCacheConfiguration> result = new HashMap<>();
            configs.forEach((cacheName, pair) -> result.put(cacheName, resovleRedisCacheConfiguration(pair.getKey(), pair.getValue())));
            return result;
        }
    }

}

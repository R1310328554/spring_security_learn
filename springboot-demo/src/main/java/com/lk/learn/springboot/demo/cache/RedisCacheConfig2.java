//package com.lk.learn.springboot.demo.cache;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import javafx.util.Pair;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.util.Assert;
//import org.springframework.util.CollectionUtils;
//import org.thymeleaf.expression.Maps;
//
//import java.time.Duration;
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//@EnableCaching
//public class RedisCacheConfig2 extends CachingConfigurerSupport {
//    @Autowired
//    private RedisProperties redisProperties;
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory() {
//        // 获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
//        String[] serverArray = redisProperties.getClusterNodes().split(",");
//        RedisClusterConfiguration redisClusterConfiguration = new
//            RedisClusterConfiguration(Arrays.asList(serverArray));
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        // 最大空闲连接数, 默认8个
//        jedisPoolConfig.setMaxIdle(100);
//        // 最大连接数, 默认8个
//        jedisPoolConfig.setMaxTotal(500);
//        // 最小空闲连接数, 默认0
//        jedisPoolConfig.setMinIdle(0);
//        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,
//        // 默认-1
//        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
//        // 对拿到的connection进行validateObject校验
//        jedisPoolConfig.setTestOnBorrow(true);
//        return new JedisConnectionFactory(redisClusterConfiguration
//            ,jedisPoolConfig);
//    }
//    /**     * 注入redis template     *     * @return     */
//    @Bean
//    @Qualifier("redisTemplate")
//    public RedisTemplate redisTemplate(
//        JedisConnectionFactoryjedisConnectionFactory
//        , Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
//        RedisTemplate template = new RedisTemplate();
//        template.setConnectionFactory(jedisConnectionFactory);
//        template.setKeySerializer(new JdkSerializationRedisSerializer());
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//        return template;
//    }
//
//    /**     * redis cache manager     *     * @return     */
//    @Bean
//    @Primary
//    public RedisCacheManager redisCacheManager(
//        JedisConnectionFactory jedisConnectionFactory
//        , ObjectProvider<List<RedisCacheConfigurationProvider>>
//            configurationProvider) {
//        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap =
//            Maps.newHashMap();
//        List<RedisCacheConfigurationProvider> configurations
//             = configurationProvider.getIfAvailable();
//        if (!CollectionUtils.isEmpty(configurations)) {
//            for (RedisCacheConfigurationProvider configuration : configurations) {
//                redisCacheConfigurationMap.putAll(configuration.resolve());
//            }
//        }
//        RedisCacheManager cacheManager = RedisCacheManager.
//            RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
//                .cacheDefaults(resovleRedisCacheConfiguration(Duration.
//                    ofSeconds(300), JacksonHelper.genJavaType(Object.class)))
//                .withInitialCacheConfigurations(redisCacheConfigurationMap)
//                .build();
//        return cacheManager;
//    }
//    private static RedisCacheConfiguration resovleRedisCacheConfiguration(Duration duration, JavaType javaType) {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext
//                    .SerializationPair
//                    .fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext
//                    .SerializationPair.fromSerializer(
//                        new Jackson2JsonRedisSerializer<>(javaType)))
//                .entryTtl(duration);
//    }
//    /**     * 配置一个序列器, 将对象序列化为字符串存储, 和将对象反序列化为对象     */
//    @Bean
//    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        return jackson2JsonRedisSerializer;
//    }
//    public static abstract class RedisCacheConfigurationProvider {
//        // key = 缓存名称， value = 缓存时间 和 缓存类型
//        protected Map<String, Pair<Duration, JavaType>> configs;
//        protected abstract void initConfigs();
//        public Map<String, RedisCacheConfiguration> resolve() {
//            initConfigs();
//            Assert.notEmpty(configs, "RedisCacheConfigurationProvider 配置不能为空...");
//            Map<String, RedisCacheConfiguration> result = Maps.newHashMap();
//            configs.forEach((cacheName, pair) -> result.put(cacheName,
//                resovleRedisCacheConfiguration(pair.getKey(), pair.getValue())));
//            return result;
//        }
//    }
//
//}

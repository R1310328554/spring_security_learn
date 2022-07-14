package com.okta.spring.AuthorizationServerApplication.cfg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.List;

/**
 * @author luokai 2022年7月9日
 *
 *
 * redis 出现了5个key： access   auth    auth_to_access     client_id_to_access      uname_to_access
 *
 * access:d6c4ca12-82c9-46ab-8388-0d83964772e9      是一个string
 * auth:d6c4ca12-82c9-46ab-8388-0d83964772e9        是一个string
 * auth_to_access:c13cc97181314f20224217f2a62a1f89  是一个string
 * client_id_to_access:R2dpxQ3vPrtfgF72             client_id_to_access 是一个list
 * uname_to_access:R2dpxQ3vPrtfgF72:a               uname_to_access:R2dpxQ3vPrtfgF72 是一个list
 *
 * 还有 refresh
 *
 *  显然，redisTokenStore 也是支持分布式部署的！
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * Redis令牌管理
     * 步骤：
     * 1.启动redis
     * 2.添加redis依赖
     * 3.添加redis 依赖后, 容器就会有 RedisConnectionFactory 实例
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 使用 JDBC 方式管理客户端信息
     * @return
     */
//    @Bean
    public ClientDetailsService redisClientDetailsService(){
        return new RedisClientDetailsService(redisConnectionFactory);
    }

}

// todo 把用户信息存redis； 会不会不太好， 一般都是存数据库..
class RedisClientDetailsService  implements ClientDetailsService, ClientRegistrationService {

    private final RedisConnectionFactory redisConnectionFactory;

    public RedisClientDetailsService(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return null;
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {

    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {

    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {

    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {

    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return null;
    }
}

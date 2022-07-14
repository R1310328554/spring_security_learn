package com.lk.learn;
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
    public TokenStore redisTokenStore() {
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

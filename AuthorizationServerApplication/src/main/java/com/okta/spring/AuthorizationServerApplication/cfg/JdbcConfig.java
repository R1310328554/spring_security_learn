package com.okta.spring.AuthorizationServerApplication.cfg;
import cn.hutool.db.ds.simple.SimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * @author luokai 2022年7月11日
 *
 */
//@Configuration
@Profile("jdbc")
public class JdbcConfig {

    @Autowired
    DataSource druidDataSource;

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(druidDataSource);
    }

    /**
     * 使用 JDBC 方式管理客户端信息
     * @return
     */
    @Bean
    public ClientDetailsService jdbcClientDetailsService(){
        return new JdbcClientDetailsService(druidDataSource);
    }

}

package com.lk.learn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {


    public static final String RESOURCE_ID = "res1";

    @Value("${user.oauth.clientId}")
    private String ClientID;
    @Value("${user.oauth.clientSecret}")
    private String ClientSecret;
    @Value("${user.oauth.serverUrl}")
    private String oauthServerUrl;
    @Autowired
    TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)//资源 id， 默认是 oauth2-resource
                .tokenStore(tokenStore) // 默认是本地， 即 inMemory
                .tokenServices(tokenService())//验证令牌的服务
                .stateless(true);// 默认就是 stateless = true
    }

    /*
    GET http://192.168.1.103:8083/res/aa

        返回：
        HTTP/1.1 403
        {
          "error": "access_denied",
          "error_description": "Access is denied"
        }

        why? #oauth2.clientHasRole('ROLE_ADMIN')不行， hasRole('ROLE_ADMIN')则是可以的！
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**")
                 //   .access("isAuthenticated()")
                 //   .access("#oauth2.clientHasRole('ROLE_ADMIN')")
                 .access("hasRole('ROLE_ADMIN')")
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //资源服务令牌解析服务
    @Bean
    public ResourceServerTokenServices tokenService() {
        //使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
        RemoteTokenServices service=new RemoteTokenServices();
        service.setCheckTokenEndpointUrl(oauthServerUrl+"/oauth/check_token");
        service.setClientId(ClientID);
        service.setClientSecret(ClientSecret);
        return service;
    }
}

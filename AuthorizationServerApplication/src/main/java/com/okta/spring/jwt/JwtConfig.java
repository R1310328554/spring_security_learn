package com.okta.spring.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


/*
    TokenStore 有4个实现： jdbc/redis/jwt/inMemory

    jwt即JwtTokenStore，其实是不会在服务端存储token的，这样的做法适合分布式部署；

 */
@Configuration
public class JwtConfig {

    //公钥
    private static final String PUBLIC_KEY = "id_rsa.pub"; // "public.key";

    /***
     * 定义JwtTokenStore
     * @param jwtAccessTokenConverter
     * @return
     */
    @Bean // 如果添加了JwtTokenStore， 那么之前的请求方式就不行了，为什么？
//    因为.. jwt的getAccessToken 一直返回null； 因为JwtTokenStore不会存储任何东西；需要请求者把token传过来
    // 401 {"error":"invalid_token","error_description":"Cannot convert access token to JSON"}
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        //A TokenStore implementation that just reads data from the tokens themselves. Not really a store since it never persists anything, and methods like getAccessToken(OAuth2Authentication) always return null. But nevertheless a useful tool since it translates access tokens to and from authentications. Use this wherever a TokenStore is needed, but remember to use the same JwtAccessTokenConverter instance (or one with the same verifier) as was used when the tokens were minted.
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /***
     * 定义JJwtAccessTokenConverter 转化器 既能创建令牌 也能解析令牌 用来配置公钥
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // Caused by: java.lang.IllegalStateException: For MAC signing you do not need to specify the verifier key separately, and if you do it must match the signing key
        // converter.setVerifierKey(getPubKey()); // setVerifierKey需要的是一个字符串信息 getPubKey()得到文件中的公钥
        return converter;
    }
    /**
     * 获取非对称加密公钥 Key
     * @return 公钥 Key
     */
    private String getPubKey() {
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

//    版权声明：本文为CSDN博主「YxinMiracle」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/caiyongxin_001/article/details/119790949

}

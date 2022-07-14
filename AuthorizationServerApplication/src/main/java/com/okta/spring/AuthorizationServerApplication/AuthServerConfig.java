package com.okta.spring.AuthorizationServerApplication;
import com.okta.spring.AuthorizationServerApplication.cfg.JwtTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author luokai 2022年7月9日
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Value("${user.oauth.clientId}")
    private String ClientID;
    @Value("${user.oauth.clientSecret}")
    private String ClientSecret;
    @Value("${user.oauth.redirectUris}")
    private String RedirectURLs;

    @Autowired
    private PasswordEncoder passwordEncoder;

//   private final PasswordEncoder passwordEncoder;
//    public AuthServerConfig(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    /*

        参考 AuthorizationServerSecurityConfiguration#configure 方法
        		http
        	.authorizeRequests()
            	.antMatchers(tokenEndpointPath).fullyAuthenticated()
            	.antMatchers(tokenKeyPath).access(configurer.getTokenKeyAccess())
            	.antMatchers(checkTokenPath).access(configurer.getCheckTokenAccess())
        .and()
        	.requestMatchers()
            	.antMatchers(tokenEndpointPath, tokenKeyPath, checkTokenPath)
        .and()
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

     */
    @Override
    public void configure(
        AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        // 到底什么意思？ tokenKeyAccess的意思是  .antMatchers(tokenKeyPath).access(configurer.getTokenKeyAccess())

//        oauthServer.setBuilder(  );

        // 为什么我都写了 @EnableAuthorizationServer 还需要配置这个？ 这个算是定制化？ 是必须的？ 不是， 默认两个都是 denyAll(); 这个对于 资源服务器分开的时候就不行；
        // 不过这里确实可以不用 客制化； 其实这里，随便怎么写都行，因为实际用不到tokenKey、checkToken 两个http端点
        oauthServer
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("permitAll()") // 这里不能使用 isAuthenticated(), 否则远程访问checkToken端点返回 401
//        .and().logout().logoutUrl()

            // IllegalStateException: securityBuilder cannot be null, 这里为什么不能使用sessionManagement？
//            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)

//        .accessDeniedHandler(ac)
//        .realm()


//            .allowFormAuthenticationForClients()
//            .checkTokenAccess("isAuthenticated()")
//            .tokenKeyAccess("isAuthenticated()")

//        .authenticationEntryPoint()
//        .addTokenEndpointAuthenticationFilter()
//        .and()

                //
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                ;
    }

    @Autowired
    JwtTokenEnhancer enhancer;
//    /**
//     * JWT 令牌转换器  具体配置在: {@link com.qycq.oauth.security.config.JwtTokenStoreConfig}
//     */
//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//    // TokenStore就是Persistence interface for OAuth2 tokens.用来持久化OAuth2的tokens，也就是accessToken、refreshToken，包括存储、读取、查找
    @Autowired
    TokenStore tokenStore;

    //TokenGranter 通过grantType、tokenRequest 生成AccessToken
    // OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest);

    /*

        TokenEnhancer 提供一个OAuth2AccessToken，返回一个加强后的OAuth2AccessToken
        如何在access_token中自定义一些参数返回创建扩展器 TokenEnhancer,改扩展器可以让我们的请求信息中加入一些自定义信息,例如我现在在我的请求返回格式中加入一个userInfo的对象
        https://blog.csdn.net/weixin_54889617/article/details/121860500

        AccessTokenConverter 将map、OAuth2AccessToken相互转换
         因为要支持jwt、 json格式 ， 所以
         主要是下面的需要转换， 其他的如clientId、scope 一般直接赋值不需改变。
        AUD, resourceIds， 意为Audience，听众；受众；读者
        JTI, 来源于额外信息： token.getAdditionalInformation().get(JTI)

        为什么需要转换呢？什么场景需要accessTokenConverter？
     */
//    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.addInterceptor()
        // endpoints 应该配置哪些呢？ 如何配置？ 分别什么作用？
        // tokenGranter 默认是？ 不需要定制化吧

//        endpoints.implicitGrantService

        endpoints
                .tokenStore(tokenStore)
                .tokenEnhancer(enhancer)
        //  .tokenServices()

            .authenticationManager(authenticationManager) // 密码模式，必须配置AuthenticationManager，不然不生效
//            .tokenGranter(null)
        ;
//        System.out.println("authenticationManager = " + authenticationManager);
        super.configure(endpoints);
    }


    @Bean
    public TokenKeyEndpoint tokenKeyEndpoint() {
        return new TokenKeyEndpoint(new JwtAccessTokenConverter());
    }

    /**
     * 将{@link SpringSecurityConfig 中的 AuthenticationManager 注入}
     */
    @Autowired
    private AuthenticationManager authenticationManager;


    public void configureJwt(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.addInterceptor()
        // endpoints 应该配置哪些呢？ 如何配置？ 分别什么作用？
        // tokenGranter 默认是？ 不需要定制化吧
//        endpoints
//                .accessTokenConverter(jwtAccessTokenConverter)
//                .tokenStore(tokenStore)
//            .tokenEnhancer(enhancer)
//            .tokenGranter(null)
        ;

        /*
         * pathMapping用来配置端点URL链接，有两个参数，都将以 "/" 字符为开始的字符串
         *
         * defaultPath：这个端点URL的默认链接
         *
         * customPath：你要进行替代的URL链接
         */
        endpoints.pathMapping("/oauth/token", "/oauth/xwj");

        super.configure(endpoints);
    }

    @Autowired
    ClientDetailsService jdbcClientDetailsService;

    public void configureJdbc(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //BCryptPasswordEncoder     : Encoded password does not look like BCrypt
        // String finalSecret = "{bcrypt}" + passwordEncoder.encode("123456");
        String finalSecret = passwordEncoder.encode("123456");
        clients.inMemory()
            .withClient(ClientID)
             .secret(passwordEncoder.encode(ClientSecret))
            // .secret(finalSecret)
            // 这里的authorizedGrantTypes如果包含refresh_token， 返回客户端的token才会包含refresh_token
            .authorizedGrantTypes("authorization_code", "password", "implicit","client_credentials","refresh_token")
            .scopes("user_info")
            .autoApprove(true) // 否则会返回 页面 而不是json， 需要确认 是否授权。。
            .redirectUris(RedirectURLs)
        .and()
        .inMemory()
        .withClient("ee0e0710193b7cac1e68")
        .secret(finalSecret)
        // .authorizedGrantTypes("authorization_code")
         .authorizedGrantTypes("authorization_code", "password", "implicit","client_credentials","refresh_token")
        .scopes("user_info")
        .autoApprove(true)
        .redirectUris(RedirectURLs)

        .and()
        .inMemory()
        .withClient("973886123")
        .secret(passwordEncoder.encode("3253f16e8324a73f6ede08c7405c0bad"))
        .authorizedGrantTypes("authorization_code")
        .scopes("user_info")
        .autoApprove(true)
        .redirectUris(RedirectURLs)
        ;
    }
}

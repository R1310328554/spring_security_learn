package com.okta.spring.AuthorizationServerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
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
            .checkTokenAccess("isAuthenticated()")
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


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.addInterceptor()
//        endpoints.
        super.configure(endpoints);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(ClientID)
            .secret(passwordEncoder.encode(ClientSecret))
            .authorizedGrantTypes("authorization_code")
            .scopes("user_info")
            .autoApprove(true) // 否则会返回 页面 而不是json， 需要确认 是否授权。。
            .redirectUris(RedirectURLs)
        .and()
        .inMemory()
        .withClient("ee0e0710193b7cac1e68")
        .secret(passwordEncoder.encode("7419ca6fa807e11d144823d8c0f614676cb35da7"))
        .authorizedGrantTypes("authorization_code")
        // .authorizedGrantTypes("authorization_code", "password", "implicit","client_credentials","refresh_token")
        .scopes("user_info")
        .autoApprove(true)
        .redirectUris("http://localhost:8082/login/oauth2/code/")

        .and()
        .inMemory()
        .withClient("973886123")
        .secret(passwordEncoder.encode("3253f16e8324a73f6ede08c7405c0bad"))
        .authorizedGrantTypes("authorization_code")
        .scopes("user_info")
        .autoApprove(true)
        .redirectUris("http://localhost:8082/login/oauth2/code/")
        ;
    }
}

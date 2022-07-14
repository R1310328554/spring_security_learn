package com.okta.spring.SpringBootOAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 */
//@Configuration
public class TestSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OoSuccessAuthentication successAuthentication;
    @Autowired
    private OoFailureAuthentication ooFailureAuthentication;

    /**
     * oauth2Login 其实默认是使用 DefaultLoginPageGeneratingFilter 作为登录页面！( 从OAuth2LoginConfigurer可知)
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {

        OAuth2AccessTokenResponseClient< OAuth2AuthorizationCodeGrantRequest> tokenRespClient = new OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>() {
            @Override
            public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {

                return null;
            }
        };

        http.csrf().disable()
                .requestMatchers()
                .and()
                .oauth2Login()
                .tokenEndpoint().accessTokenResponseClient(tokenRespClient).and()
                .authorizedClientRepository(null)
                .authorizedClientService(null)
                .clientRegistrationRepository(null)
        ;
//                .redirectionEndpoint().baseUri()
//                .userInfoEndpoint().oidcUserService()
//                .authorizationEndpoint().authorizationRequestRepository()

    }


    /*
	@Configuration
	@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
	static class OAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().authenticated().and().oauth2Login()
					.and().oauth2Client();
		}
	}
     */
    public void configureAuthorization(HttpSecurity httpSecurity) throws Exception {
        OAuth2AccessTokenResponseClient< OAuth2AuthorizationCodeGrantRequest> tokenRespClient = new OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>() {
            @Override
            public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {

                return null;
            }
        };

        // oauth2Client() 其实完全可以不用配置，因为默认已经有一个实现了， 谁提供的呢？ .. 是
        // clientRegistrationRepository 就是说 有哪些oauth2客户端（需要具体信息），想去哪些oauth2 服务端进行认证？
        // authorizedClientRepository 就是说 认证成功后，client 保存在哪里？ 默认是内存里面
        AuthorizationRequestRepository<OAuth2AuthorizationRequest> repository = new HttpSessionOAuth2AuthorizationRequestRepository();
        httpSecurity
                .oauth2Client()
                    .authorizationCodeGrant()
                    .accessTokenResponseClient(tokenRespClient)
                    .authorizationRequestRepository(repository)
                .and()
                    .authorizedClientService(null)
                    .clientRegistrationRepository(null)
                    .authorizationCodeGrant()
                    .authorizationRequestResolver(null)

        .and();


        // todo 资源服务器 认证服务器， 分开部署！

        Converter<Jwt, ? extends AbstractAuthenticationToken> cvt = new Converter<Jwt, AbstractAuthenticationToken>() {
            @Override
            public AbstractAuthenticationToken convert(Jwt source) {
                System.out.println("TestSecurityConfiguration.convert");
                return null;
            }
        };
//        org.springframework.security.oauth2.server.resource.web.BearerTokenResolver btr;
        AuthenticationEntryPoint aep = new LoginUrlAuthenticationEntryPoint("");
        httpSecurity
//                .oauth2Client()
//                .and()

                // 配置 资源服务器的。。 其实 oauth2ResourceServer 的工作 已经基本由EnableOauth2ResourceServer 就完成了！
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(cvt)
                .and()
//                .bearerTokenResolver(null)
                .authenticationEntryPoint(aep)
//                .disable()
        ;

    }
}

package com.okta.spring.SpringBootOAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OoSuccessAuthentication successAuthentication;
    @Autowired
    private OoFailureAuthentication ooFailureAuthentication;

    /**
     * oauth2Login 其实默认是使用 DefaultLoginPageGeneratingFilter 作为登录页面！( 从OAuth2LoginConfigurer可知)
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/login**", "/myLogoutSuccessUrl").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()

                /*
                .redirectionEndpoint().baseUri("")
                .and()
                .userInfoEndpoint().userService(myUuserService)
                */
                .successHandler(successAuthentication)
                .failureHandler(ooFailureAuthentication)

                // oauth2Login虽然也有loginPage， 但是这样的话，就退化成为了 formLogin，失去了oauth2Login 的对oauth2 server交互等功能！
                // xxx 上面说的完全错误；oauth2Login的loginPage 不是表单登录，
                // 不需要真正的post 请求到loginProcessingUrl！  —— 这样反而导致OAuth2LoginAuthenticationFilter.attemptAuthentication 的OAuth2ErrorCodes.INVALID_REQUEST
                .loginPage("/myLogin").permitAll()

                // oauth2Login的loginPage 默认是 /login/oauth2/code/*， 这里可以改，但是需要同时改redirect-uri-template及oauth2 server的回调url
                // 是否需要每个配置后面跟一个 .permitAll() ？
                // 这里改了之后，
                .loginProcessingUrl("/doLogin").permitAll()

//                .loginPage("/my")
//                .loginProcessingUrl("/login2").permitAll()
                .and()

                // 我通过oauth2Login登录成功的，注销意味着什么？删除本地的session，但本地没有session.. 删除本地的token、cookie，重新获取？
                .logout()

                // 尽管如此，尽管定制了logoutUrl，但是原框架提供了 GET /logout 页面还是可以直接访问的！
                .logoutUrl("/myLogout")//默认logout 页面， 不是登出处理路径，其实也没有什么处理路径；没有logoutProcessingUrl

                // defaultTarget must start with '/' or with 'http(s)'
                // 必须要放行 logoutSuccessUrl； 通过上面的 .antMatchers("/myLogoutSuccessUrl").permitAll()或直接下面的 permitAll()
                // 否则访问logoutUrl 可以看到两个302： 它把302到logoutSuccessUrl，然后logoutSuccessUrl又被302到login page!
                .logoutSuccessUrl("/myLogoutSuccessUrl")

//                .permitAll()
                // .logoutSuccessHandler(tigerLogoutSuccessHandler)  //url和Handler只能配置一个
                .deleteCookies("JSESSIONID")//清除cook键值 ; 登录失败会也会执行 cookie删除操作？

                .and()
                .formLogin().disable()
                ;
        ;
    }
}

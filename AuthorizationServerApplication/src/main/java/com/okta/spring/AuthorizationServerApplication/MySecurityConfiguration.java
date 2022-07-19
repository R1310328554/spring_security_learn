
package com.okta.spring.AuthorizationServerApplication;
import com.okta.spring.AuthorizationServerApplication.cfg.FailureAuthentication;
import com.okta.spring.AuthorizationServerApplication.cfg.SuccessAuthentication;
import com.okta.spring.AuthorizationServerApplication.cfg.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author luokai 2022年7月9日
 */
@Configuration
@Order(1)
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${user.oauth.user.username}")
    private String username;
    @Value("${user.oauth.user.password}")
    private String password;

    @Autowired
    private SuccessAuthentication successAuthentication;
    @Autowired
    private FailureAuthentication failureAuthentication;
    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.httpFirewall()
        web.ignoring().mvcMatchers("/**.html"); // 好像不能 **.html这样！ 必须是 / 开头！
        // 静态资源方向放行； 如果设置不对，则不起作用！ 依然被拦截，依然302到login page
        web.ignoring().antMatchers("/assets/**", "/css/**", "/templates/**", "/static/**", "/images/**");
        web.ignoring().antMatchers("/js/**", "/js/**");
    }


    /**
     *   自定义各种页面！
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()

            .and()
                .csrf().disable()

                // 配置受formLogin 保护的资源端点， 不配置且不放行则 401； 当然，oauth2的端点不需要配置在这里，否则画蛇添足导致oauth2登录不正常！
                .requestMatchers()

                // 这里必须要有 /oauth/authorize，表示 oauth2 client需要先登录，然后才可以有权力去授权
                // 这里 /user/me 不能包含
                .antMatchers("/myLogin", "/doLogin", "/oauth/authorize"
                        , "/protected/**", "/mustLogin/**", "/securedPage*"
                        , "/user/refresh*"
                        , "/myLogout*", "/login?logout*" // login?logout 也需要保护起来，否则401——这样也不行， authorizeRequests里面 permit也不行，todo
                        // 首页也最好保护起来，否则..
                        , "/", "/index", "/tourist*", "/a*")// 这里antMatchers必须要包括/doLogin， 否则永远都是登录页面

            .and()
                .authorizeRequests()

                //antMatchers这里 "/user/me"不能放行，如果放行，则不能获取到Principal参数 —— 错错错，再次测试发现 这里 "/user/me"是否放行 都不要紧； 不知道哪里搞错了
                .antMatchers("/tourist", "/myLogin", "/login?logout*", "/logout?logout*", "/doLogin", "/user/me", "/user/*", "/oauth/authorize")
                .permitAll()
                .anyRequest().authenticated()

            .and()
                .formLogin()
                .loginPage("/myLogin")
                // 它的作用是什么？ 仅仅是一个通知作用吧..不对！ 测试发现，只有配置了loginPage，那么就一定需要配置loginProcessingUrl， 而且需要匹配！
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/index", false)
                .permitAll()
//                .and()
//                .authorizeRequests()
//                .anyRequest().authenticated() // 不能加这行，
//                否则：一直401 <oauth><error_description>Full authentication is required to access this resource</error_description><error>unauthorized</error></oauth>
            .and()
                .logout()

                // 设置logoutUrl之后，再访问/logout会出现401（如果不放行）， 或者404
                // 测试发现， /myLogout、 /logout 两个端点都可以注销成功，why？ 按理说只有一个;  测试发现如果antMatchers 发现/logout，则只有logoutUrl可以注销，而且访问 /logout不会注销，而是404
                // 测试发现有时候/myLogout 并没真正的注销，而是401，why？ 原因是logoutUrl需要受保护
                // 这里需要 保护起来， 否则也是 401， Full authentication is required to access this resource
                .logoutUrl("/myLogout")
                // defaultTarget must start with '/' or with 'http(s)'
                .logoutSuccessUrl("/myLogoutSuccessUrl")
                .permitAll()
                // .logoutSuccessHandler(tigerLogoutSuccessHandler)  //url和Handler只能配置一个
                .deleteCookies("JSESSIONID")//清除cook键值

            .and()

                // 这里的sessionManagement 并不能影响到AuthorizationServer， 因为..
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        ;

    }


    //  Full authentication is required to access this resource 参考 https://blog.csdn.net/zzzgd_666/article/details/107321257
    // 测试： curl -v -X POST  http://url:8080/api/oauth/token -u"yourclientid:yourclient_secret"   -d"grant_type=password"   -d"username=yourusername" -d"password=yourpassword"
    // https://www.codenong.com/49601371/
    protected void configure12(HttpSecurity http) throws Exception {
        http.requestMatchers()
                // 为什么需要放行 /login、/oauth/authorize? 他们是AuthorizationServer的端口
                .antMatchers("/login", "/oauth/authorize")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();

        /*
            http
//                .headers().frameOptions().disable()
//                .and()
                .csrf().disable()
                .requestMatchers()
                    .antMatchers("/myLogin","/doLogin", "/oauth/authorize")// 这里antMatchers必须要包括/login2， 否则永远都是登录页面
                .and()
                    .authorizeRequests()
                    .antMatchers("/aa","/myLogin", "/doLogin","/user/me2", "/oauth/authorize") // "/user/me" 放行之后， 不能获取到Principal参数
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                    .loginPage("/myLogin")
                    .loginProcessingUrl("/doLogin").permitAll()
                .anyRequest().authenticated()*/
        ;
    }

    /*
        前后端分离模式！

        使用自定义authenticationEntryPoint
        accessDeniedHandler 和 failureHandler 什么区别？
     */
    protected void configure2(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling()
                //
                .authenticationEntryPoint(unauthorizedEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successAuthentication).failureHandler(failureAuthentication);
    }

    /**
    * 配置用户， 为什么？ 因为有需要保护的端点资源，需要提供form登录
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
        /*
        也可以使用内存方式*/
       auth.inMemoryAuthentication()
            .withUser(username)
            .password(passwordEncoder.encode(password))
            .roles("USER")
        .and()
        .withUser("b")
                .password(passwordEncoder.encode("2"))
                .roles("ADMIN")
        ;
    }



//  	- Bean method 'inMemoryUserDetailsManager' in 'UserDetailsServiceAutoConfiguration' not loaded because @ConditionalOnMissingBean(types: org.springframework.security.authentication.AuthenticationManager,org.springframework.security.authentication.AuthenticationProvider,org.springframework.security.core.userdetails.UserDetailsService; SearchStrategy: all) found beans of type 'org.springframework.security.authentication.AuthenticationManager' authenticationManagerBean
//    @Autowired
//    private UserDetailsService userDetailsService; // 直接注入UserDetailsService会报错

    @Bean // https://cloud.tencent.com/developer/article/1680007
    @Override
    public UserDetailsService userDetailsService() {
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        UserDetails u1 = new User("a", passwordEncoder.encode("1"), roles("ADMIN"));
        // 此时，如何设置用户的角色？ 参考User.UserBuilder#roles
        UserDetails u2 = new User("b", passwordEncoder.encode("2"), roles("USER"));
        UserDetails u3 = new User("c", passwordEncoder.encode("3"), roles("ADMIN"));
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(u1);
        userDetailsManager.createUser(u2);
        userDetailsManager.createUser(u3);
        return userDetailsManager;
    }

    //参考User.UserBuilder#roles
    public List<? extends GrantedAuthority> roles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(
                roles.length);
        for (String role : roles) {
            Assert.isTrue(!role.startsWith("ROLE_"), () -> role
                    + " cannot start with ROLE_ (it is automatically added)");
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }

    @Bean // https://blog.csdn.net/weijianpeng2013_2015/article/details/97269048
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

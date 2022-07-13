package com.okta.spring.jwt;

import com.okta.spring.jwt.domain.AdminUserDetails;
import com.okta.spring.jwt.domain.UmsAdmin;
import com.okta.spring.jwt.domain.UmsAdminService;
import com.okta.spring.jwt.domain.UmsPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


/**
 * SpringSecurity的配置
 * Created by macro on 2018/4/26.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    /*

    当前过滤器（下面配置的 ）
        web.context.request.async.WebAsyncManagerIntegrationFilter
        web.context.SecurityContextPersistenceFilter
        web.header.HeaderWriterFilter
        web.authentication.logout.LogoutFilter
        com.okta.spring.jwt.JwtAuthenticationTokenFilter
        web.savedrequest.RequestCacheAwareFilter
        web.servletapi.SecurityContextHolderAwareRequestFilter
        web.authentication.AnonymousAuthenticationFilter
        web.session.SessionManagementFilter
        web.access.ExceptionTranslationFilter
        web.access.intercept.FilterSecurityInterceptor

     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()// 由于使用的是JWT，我们这里不需要csrf
                .disable()
                .sessionManagement()// 基于token，所以不需要session; 如果不这样呢？ 会有什么问题？
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Note that the matchers are considered in order. Therefore, the following is invalid because the first matcher matches every request and will never get to the second mapping:
                //	   http.authorizeRequests().antMatchers("/**").hasRole("USER").antMatchers("/admin/**")
                //	   		.hasRole("ADMIN")
                .authorizeRequests()

                //antMatchers和 mvcMatchers的区别是？ 其实非常类似；mvcMatchers 不需要*这样的，
                .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                )
                //！！！ permitAll 是前面的antMatchers 返回的 AuthorizedUrl对象进行 access 权限配置
                .permitAll()
                .antMatchers("/admin/login", "/admin/register")// 对登录注册要允许匿名访问
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
//                .antMatchers("/**")//测试时全部运行访问
//                .permitAll()
                .anyRequest()// 除上面外的所有请求全部需要鉴权认证
                .authenticated();
        // 禁用缓存
        httpSecurity.headers().cacheControl();

        // 添加JWT filter, 为什么在 UsernamePasswordAuthenticationFilter 之前？ 因为需要在它之前拦截token，然后登录
        // 因为没有启用 formLogin， 在过滤链之中也是看不到UsernamePasswordAuthenticationFilter的，但是这里却可以用它来定位，即before它，为何
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }


    protected void configureTest(HttpSecurity httpSecurity) throws Exception {

//        httpSecurity.
//        httpSecurity.oauth2ResourceServer()
//                .jwt().decoder();

//        httpSecurity
//                .logout() // 可以有多个 logoutHandler

        httpSecurity.formLogin()
                /*
                Specifies the URL to send users to if login is required. If used with WebSecurityConfigurerAdapter a default login page will be generated when this attribute is not specified.
                    If a URL is specified or this is not being used in conjuction with WebSecurityConfigurerAdapter, users are required to process the specified URL to generate a login page. In general, the login page should create a form that submits a request with the following requirements to work with UsernamePasswordAuthenticationFilter:
                    It must be an HTTP POST
                    It must be submitted to loginProcessingUrl(String)
                    It should include the username as an HTTP parameter by the name of usernameParameter(String)
                    It should include the password as an HTTP parameter by the name of passwordParameter(String)

                Impact on other defaults
                Updating this value, also impacts a number of other default values. For example, the following are the default values when only formLogin() was specified.
                /login GET - the login form
                /login POST - process the credentials and if valid authenticate the user
                /login?error GET - redirect here for failed authentication attempts
                /login?logout GET - redirect here after successfully logging out

                If "/authenticate" was passed to this method it update the defaults as shown below:
                /authenticate GET - the login form
                /authenticate POST - process the credentials and if valid authenticate the user
                /authenticate?error GET - redirect here for failed authentication attempts
                /authenticate?logout GET - redirect here after successfully logging out
                 */
                .loginPage("as")
.permitAll() // 1
                .loginProcessingUrl("aa")
.permitAll() // 2 ； 1、2多次调用， 其实效果是会覆盖的，最后一次调用生效。
                // Ensures the urls for failureUrl(String) as well as for the HttpSecurityBuilder, the getLoginPage and getLoginProcessingUrl are granted access to any user.
                .permitAll(false);

//        httpSecurity.headers().xssProtection().

        httpSecurity.oauth2ResourceServer();


    }


    public void configure111(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            /*
                Allows specifying which HttpServletRequest instances this HttpSecurity will be invoked on. This method allows for easily invoking the HttpSecurity for multiple different RequestMatcher instances. If only a single RequestMatcher is necessary consider using mvcMatcher(String), antMatcher(String), regexMatcher(String), or requestMatcher(RequestMatcher).
                Invoking requestMatchers() will not override previous invocations of mvcMatcher(String)}, requestMatchers(), antMatcher(String), regexMatcher(String), and requestMatcher(RequestMatcher).
             */
            .requestMatchers()
                .mvcMatchers("").anyRequest() // 这里， mvcMatchers后面怎么能够跟一个anyRequest， 其意义是？ todo
                .and().formLogin(); // 可以设置一部分端点  formLogin

        // 会覆盖之前的 xx配置
        httpSecurity.antMatcher("").httpBasic();// 同时 一部分端点  httpBasic

        // 直接httpSecurity 后面的， 只能是 antMatcher， 没有s， mvcMatcher也一样！
        // 他们只能配置一个， 但是如果使用requestMatchers， 则可以配置多个， 这个就是区别！
        httpSecurity.mvcMatcher("").oauth2Login();

        // requestMatchers直接跟httpSecurity后面， 同 antMatcher、mvcMatcher 一样， 是用来指定当前HttpSecurity 工作范围的！！
        httpSecurity.requestMatchers()
                .antMatchers("").anyRequest().and().anonymous();
                    // Allows configuring how an anonymous user is represented. This is automatically applied when used in conjunction with WebSecurityConfigurerAdapter. By default anonymous users will be represented with an org.springframework.security.authentication.AnonymousAuthenticationToken and contain the role "ROLE_ANONYMOUS"
                    // 匿名用户也是用户， 它其实也需要登录，但是，没有人知道它的姓名等属性；
                    // 这里可以配置它的 权限、伪姓名

        httpSecurity .antMatcher("/anno/**") .anonymous().principal(new UmsAdmin()).authorities("aa"); // /anno/ 目录下的 任何， 可以匿名登录。

        // 不过，匿名登录 怎么做？

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder())
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> {
            UmsAdmin admin = adminService.getAdminByUsername(username);
            if (admin != null) {
                List<UmsPermission> permissionList = adminService.getPermissionList(admin.getId());
                return new AdminUserDetails(admin,permissionList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}


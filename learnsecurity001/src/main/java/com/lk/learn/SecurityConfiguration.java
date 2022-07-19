package com.lk.learn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.learn.config.MyFailureHandler;
import com.lk.learn.config.MyLogoutHandler;
import com.lk.learn.config.MySuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author luokai
 * @date 2022年7月6日
 */
@Configuration
@Order(1)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private String username = "a";
    private String password = "1";

    @Autowired
    private LogoutSuccessHandler tigerLogoutSuccessHandler;
    @Autowired
    private MySuccessHandler successHandler;
    @Autowired
    private MyFailureHandler failureHandler;
    @Autowired
    private MyLogoutHandler logoutHandler;




//    protected void configure000(HttpSecurity http) throws Exception {
//        // login、logout page 使用外部url；
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//            .and()
//                .httpBasic().authenticationEntryPoint(null).authenticationDetailsSource()
//            .and()
//                .formLogin()
//                .authenticationDetailsSource()
//            .and()
//                .authorizeRequests().expressionHandler()
//                .filterSecurityInterceptorOncePerRequest(true)
//            .and()
//                .rememberMe().userDetailsService().tokenRepository().rememberMeServices()
//            .and()
//                .userDetailsService()
//            .authenticationProvider()
//
//            .oauth2ResourceServer()
//                .authenticationEntryPoint().bearerTokenResolver()
//
//            .and()
//                .oauth2Client().authorizedClientService().authorizationCodeGrant().accessTokenResponseClient()
//                .authorizationRequestRepository().authorizationRequestResolver()
//            .and()
//            .and()
//                .x509().authenticationDetailsSource()
//                .authenticationUserDetailsService()
//                .userDetailsService()
//        .and()
//
//        ;
//
//    }

    protected void configureErr(HttpSecurity http) throws Exception {
        http
                .formLogin()
//                .permitAll()  // 不需要permitAll，默认/login 是放行的
                .and()
                .authorizeRequests()
                .antMatchers("/js/**","/assets/**").permitAll()
                .anyRequest()
                .authenticated()
        ;

    }


    /**
     * test cors csrf
     */
    /*
        corsConfigurationSource 同名bean 已经有几个了！
     */
    @Autowired
    CorsConfigurationSource myCorsConfigurationSource;
    /*
        Access to XMLHttpRequest at 'http://192.168.1.103:8080/login' from origin 'http://192.168.1.103:8085' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
        xhr.js:177          POST http://192.168.1.103:8080/login net::ERR_FAILED 302
     */
    protected void configureasdf(HttpSecurity http) throws Exception {
        http
            .cors()
                // .disable() disable 相当于是不进行配置，但是不影响其他地方配置 CORSFilter
                .configurationSource(myCorsConfigurationSource)

            .and()
            .formLogin()
//            .loginPage("http://192.168.1.103:8085/login")
//                .loginProcessingUrl("/doLogin")
                .permitAll()
//        .jee().j2eePreAuthenticatedProcessingFilter()
//                .exceptionHandling().authenticationEntryPoint()
//            .and()
//        .anonymous().authorities()
//            .requestCache().disable()
//            .servletApi().rolePrefix()
//            .cors().configurationSource()
//                .x509().x509AuthenticationFilter()
//            .oauth2Login().userInfoEndpoint()
//            .oauth2Client()
//                .authorizationCodeGrant().accessTokenResponseClient()
//                .openidLogin().consumer()
            .and()
                .rememberMe()
                .alwaysRemember(true)
//                .apply()
            .and()
                .authorizeRequests()
                .antMatchers("/static/**","/index").permitAll()
                .antMatchers("/js/**","/assets/**").permitAll()
                .antMatchers("/templates/**","/doLogin").permitAll()
                .anyRequest()
                .authenticated()
            .and()

                //csrf 攻击主要是借助了浏览器默认发送 Cookie 的这一机制，所以如果你的前端是 App、小程序之类的应用，不涉及浏览器应用的话，其实可以忽略这个问题，
                // 如果你的前端包含浏览器应用的话，这个问题就要认真考虑了。
                // https://wangsong.blog.csdn.net/article/details/106206339
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        ;

        //403页面，无权限跳转
//        http.exceptionHandling().accessDeniedPage("/403");

    }



    /**
     * test rememberMe
     */
    // CsrfFilter
    //        Csrf Token
    //        用户登录时，系统发放一个CsrfToken值，用户携带该CsrfToken值与用户名、密码等参数完成登录。系统记录该会话的 CsrfToken 值，之后在用户的任何请求中，都必须带上该CsrfToken值，并由系统进行校验。
    //        这种方法需要与前端配合，包括存储CsrfToken值，以及在任何请求中（包括表单和Ajax）携带CsrfToken值。安全性相较于HTTP Referer提高很多，如果都是XMLHttpRequest，则可以统一添加CsrfToken值；但如果存在大量的表单和a标签，就会变得非常烦琐。

    // InvalidCsrfTokenException: Invalid CSRF Token 'e4b990cd-a807-48a1-99a6-4d0e7f65816c' was found on the request parameter '_csrf' or header 'X-CSRF-TOKEN'.]
    // spring security的csrf 要求所有的post请求都携带csrf 参数(名为 _csrf )，然后做校验（即和之前发放的csrf做匹配）。
    // 如果没有携带( Invalid CSRF Token 'null' )  // 或者匹配不上，那么报错 InvalidCsrfTokenException！

    protected void configure12321(HttpSecurity http) throws Exception {
        /*
        The Filter class com.lk.learn.SecurityConfiguration$1 does not have a registered order and cannot be added without a specified order. Consider using addFilterBefore or addFilterAfter instead.
        at org.springframework.security.config.annotation.web.builders.HttpSecurity.addFilter(HttpSecurity.java:1179) ~[spring-security-config-5.1.4.RELEASE.jar:5.1.4.RELEASE]
        at com.lk.learn.SecurityConfiguration.configure(SecurityConfiguration.java:63) ~[classes/:na]

        http .addFilter(new Filter() {
                    @Override
                    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                        System.out.println("servletRequest = [" + servletRequest + "], servletResponse = [" + servletResponse + "], filterChain = [" + filterChain + "]");
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                });
     */

        SessionInformationExpiredStrategy expiredSessionStrategy = new SessionInformationExpiredStrategy() {
            @Override
            public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
                System.out.println("expiredSessionStrategy event = " + event);
            }
        };
        // 默认就是这个，就是内存存储
        SessionRegistry sessionRegistry = new SessionRegistryImpl();

        http
            .exceptionHandling()

                // 什么是 deny？ 什么情况发生？
                //  errorPage must begin with '/'  accessDeniedPage 会导致执行setErrorPage
            .accessDeniedPage("/denied1")
//            .accessDeniedHandler(new AccessDeniedHandler() {
//                @Override
//                public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//                    System.out.println("SecurityConfiguration.handle    " + "request = [" + request + "], response = [" + response + "], accessDeniedException = [" + accessDeniedException + "]");
//                }
//            })

                // authenticationEntryPoint顾名思义，是进行用户名密码验证的业务处理端点！
//            .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                @Override
//                public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//                    System.out.println("SecurityConfiguration.commence");
//                }
//            })

        .and()
                .csrf().ignoringAntMatchers("/myLogout*")
                .and()
//        .authenticationProvider(ap)

                // Remember me 到底怎么理解？ 是.. 通过remember-me parameter 参数创建session ?
                // 如果勾选了Remember me，那么即使登出了， 服务端仍然保留了那个session；可以通过rememberMeCookie的cookie就登录
                // 如果勾选了Remember me，然后登录、然后操作、然后又登出了， 然后又登录，那么还需要重新勾选吗？ 是的！
                // 如果不勾选Remember me，然后登录，成功后，并不会产生名为rememberMeCookieName 的cookie
                // 感觉 rememberMe 正确启用的前提是 不主动的注销；因为注销之后cookie 都没有了； 如果不主动注销，那么rememberMe cookie还是在的，这样，在session过期或浏览器关闭后，过很长一段时间再访问网站，登录请求会携带rememberMe cookie， 而服务端就可以从这个cookie 解析出 原来的用户名密码等信息
                // rememberMe cookie 是需要包含用户信息的吗？如果仅仅是uuid 这样的无意义字符串，那么只能事先把用户信息保存起来（比如保存到内存），然后查询出用户信息
                // 但是过期再登录为什么出现401？ todo 大概是因为虽然 rememberMe cookie有效，但是JSESSIONID已经失效。如果还是拿这个JSESSIONID去请求，那么
                // 第一次登录使用UsernamePasswordAuthenticationFilter验证用户密码， 后面的登录使用RememberMeAuthenticationFilter获取rememberMe cookie进行验证
            .rememberMe()

            //Whether the cookie should always be created even if the remember-me parameter is not set. By default this will be set to false.
            // 是否创建cookie，即使remember-me parameter 参数不存在 ；默认 false；
            // 如果true 的话，用户在浏览器端是否勾选了Remember me， 都无所谓，都相当于是固定 勾选。
            .alwaysRemember(false)
//            .key("rem")
                .rememberMeCookieName("rememberMeCookieName")
                // 默认是 TWO_WEEKS_S
                .tokenValiditySeconds(6000)

            .and()
            .sessionManagement()

                // 限制 一个用户只能同时一个session在线！
                // 原理就是发放n个会话，通过jsession控制；
                // 默认的策略是： 如果多余了就把之前的提出，从而 之前那个session失效，它若想再次访问则有剔除 最旧的
                // 不能是0 —— MaximumLogins must be either -1 to allow unlimited logins, or a positive integer to specify a maximum
                .maximumSessions(1)
                .expiredSessionStrategy(expiredSessionStrategy)

                // If true, prevents a user from authenticating when the maximumSessions(int) has been reached. Otherwise (default), the user who authenticates is allowed access and an existing user's session is expired. The user's who's session is forcibly expired is sent to expiredUrl(String). The advantage of this approach is if a user accidentally does not log out, there is no need for an administrator to intervene or wait till their session expires
                // 默认false
                .maxSessionsPreventsLogin(true)

                // Controls the SessionRegistry implementation used. The default is SessionRegistryImpl which is an in memory implementation.
                .sessionRegistry(sessionRegistry)
                // url must start with '/' or with 'http(s)'
                .expiredUrl("/sessExpiredUrl")
//                .

        .and()

        .and()

//        .httpBasic()
//                .and()
                .formLogin()
        // .userDetailsService(ud)
        .and()
                .logout()
//                .deleteCookies("")// 不能为空，否则 Cookie name may not be null or zero length
                .and()
        .authorizeRequests().anyRequest().authenticated()
        ;

        // httpBasic formLogin 是否可以同时启用？
        // httpBasic 无法登出？ 登录一次？ 是因为 Authorization: Basic YTox 请求头 会一直存活，直到浏览器重启。
        // 而 cookie jsession 会在每次重启后 都改变
    }


    /**
     * test defaultLogoutSuccessHandlerFor
     */
    //下面来自于 https://juejin.cn/post/6979769509263458311
    protected void configure0(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/mylogin.html")
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/index.html")
                .failureHandler(new MyFailureHandler())
                .usernameParameter("uname")
                .passwordParameter("passwd")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout1", "GET"),
                        new AntPathRequestMatcher("/logout2", "POST")))
                .invalidateHttpSession(true)
                .clearAuthentication(true)

                // defaultLogoutSuccessHandlerFor 主要作用是覆盖.logoutSuccessHandler() 不太理解两者区别.. ?  todo
                // 可以配置多个..
                .defaultLogoutSuccessHandlerFor((req,resp,auth)->{
                    resp.setContentType("application/json;charset=utf-8");
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", 200);
                    result.put("msg", "使用 logout1 注销成功!");
                    ObjectMapper om = new ObjectMapper();
                    String s = om.writeValueAsString(result);
                    resp.getWriter().write(s);
                },new AntPathRequestMatcher("/logout1","GET"))
                .defaultLogoutSuccessHandlerFor((req,resp,auth)->{
                    resp.setContentType("application/json;charset=utf-8");
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", 200);
                    result.put("msg", "使用 logout2 注销成功!");
                    ObjectMapper om = new ObjectMapper();
                    String s = om.writeValueAsString(result);
                    resp.getWriter().write(s);
                },new AntPathRequestMatcher("/logout2","POST"))
                .and()
                .csrf().disable();
    }


    /**
     * test logoutRequestMatcher
     */
    protected void configure23(HttpSecurity http) throws Exception {
        RequestMatcher logoutRequestMatcher = new AntPathRequestMatcher("/out/b*", "GET");// 必须大写
        http.formLogin()
                //如果 login failure的url和Handler同时配置， 则使用 failureHandler， 忽略failure； 但是浏览器地址不变，保持doLogin状态,即： http://192.168.1.103:8080/doLogin
                .failureHandler(failureHandler)
                // .setBuilder(null)
//                .permitAll() // permitAll only works with HttpSecurity.authorizeRequests()
                .and()

                .authorizeRequests()
                .anyRequest()

                // 必须要有这个， 否则访问其他页面不会被302重定向到 login page!
                .authenticated()

                .and()
                .logout()
                .defaultLogoutSuccessHandlerFor(tigerLogoutSuccessHandler, logoutRequestMatcher)

                // 这个就替代了 POST /logout 请求，而 直接访问 POST /logout, 则报错404；当然 GET /logout 此时还是可以访问的！
                // 浏览器直接访问 /out/basd 这样的， 就触发登出； 无须配置 /out/b* 的controller
                .logoutRequestMatcher(new OrRequestMatcher(
                new AntPathRequestMatcher("/logout1", "GET"),
                new AntPathRequestMatcher("/logout2", "POST")))

        // 默认的get方式访问 /logout， 返回一个html， 需要用户确认，提供确认按钮，点击post方式请求/logout，然后302到 /login?logout，才是真正的退出登录！
        ;

        SecurityContextConfigurer<HttpSecurity> httpSecuritySecurityContextConfigurer = http.securityContext();
        System.out.println("httpSecuritySecurityContextConfigurer = " + httpSecuritySecurityContextConfigurer);
    }


    /**
     * test 自定义 formLogin！ 测试通过，完全没问题！
     */
    // 所有的路径必须是以/ 开头！ 当然，这里不需要添加 context-path前缀
    // defaultSuccessUrl  logoutSuccessUrl 都算是defaultTarget，即 default-target-url
//    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/**").authenticated();
//        http.requestMatchers().anyRequest().and().authorizeRequests().antMatchers("/test/*").authenticated();
//        http.requestMatchers().antMatchers("/test/**").and().authorizeRequests().antMatchers("/a*").authenticated();

        ObjectPostProcessor<?> myOPP = new ObjectPostProcessor<Object>() {

            // 貌似这里可以对所有的 org.springframework.security.web.authentication 包下的内容处理
            @Override
            public <O> O postProcess(O object) {
                System.out.println("SecurityConfiguration.postProcess " + object);
                return object;
            }
        };

        http
                .csrf().disable() // 先disable， 开启之后，所有请求都被302到loginPage，包括loginProcessingUrl

                .formLogin()

                //必须是以/ 开头, 否则启动报错：'myLogin?error' is not a valid redirect URL； 原因是 SimpleUrlAuthenticationFailureHandler.setDefaultFailureUrl
                // 这里loginPage不能设置"/myLogin.html"，需要经过controller然后返回到页面， 为什么？
                // loginPage的提交路径 loginProcessingUrl 需要放行，否则被拦截，否则无法进入loginPage！
                .loginPage("/myLogin")// loginPage 中的form 可以提交到任意的后端路径， 如果不是loginProcessingUrl 会什么效果？

                //必须是以/ 开头
                //loginPage的提交路径 loginProcessingUrl 无需要放行， 貌似因为它根本不会进入到controller或返回页面。
                .loginProcessingUrl("/doLogin")

                // 默认是返回登录前的一个页面, 不需要放行
                .defaultSuccessUrl("/defaultSuccessUrl", true)
                // successForwardUrl不需要放行
                .successForwardUrl("/successForwardUrl")

                // failureForwardUrl不需要放行
                .failureForwardUrl("/failureForwardUrl")

                // failureUrl 必须要放行
                .failureUrl("/loginFail") // default is /login?error

                //如果 login failure的url和Handler同时配置， 则使用 failureHandler， 忽略failure；
                // 但是浏览器地址不变，保持doLogin状态,即： http://192.168.1.103:8080/doLogin, 这跟successForwardUrl无关
                // .failureHandler(failureHandler)

                // 可以定制化
                // .usernameParameter("un")
                // .passwordParameter("pa")

                //login的url和Handler同时配置， 则使用successHandler， 忽略successForwardUrl及defaultSuccessUrl
                .successHandler(successHandler)

                .withObjectPostProcessor(myOPP)

                //这里的 permitAll 似乎可写可不行， 其实不然！不写(注释即可)也不影响程序启动运行，但是。需要手动放行loginPage、failureUrl
                .permitAll()

                .and()

                .authorizeRequests()
                // 这里必须要放行loginPage，否则无法进入loginPage，浏览器出现： 192.168.1.103 将您重定向的次数过多。
                .antMatchers("/myLogin","/loginFail","/failureForwardUrl2", "/myLogoutSuccessUrl", "/resources/**", "/static/**", "sdf*.html")
                .permitAll()

                // .anyRequest() 基本上等同于 .antMatchers("/**" )
                .anyRequest()
                .authenticated()

                .and()

                .logout()

                // The URL that triggers log out to occur (default is "/logout"). If CSRF protection is enabled (default), then the request must also be a POST. This means that by default POST "/logout" is required to trigger a log out. If CSRF protection is disabled, then any HTTP method is allowed.
                //  It is considered best practice to use an HTTP POST on any action that changes state (i.e. log out) to protect against CSRF attacks. If you really want to use an HTTP GET, you can use logoutRequestMatcher(new AntPathRequestMatcher(logoutUrl, "GET"));

                // logoutUrl好像没有登录之前也可以访问，感觉不好； —— 没有登录的话， 无须登出； 不过也不伤大雅。
                // 这里的.logoutUrl必须是以/ 开头， 否则无法logout，因为path永远都是/开头的！..
                // /myLogout 也无需要放行， 原因应该是同loginProcessingUrl
                // 以post方式访问myLogout， 不知道会不会到这里来？
                // logoutUrl 指定注销登录请求地址，默认GET请求，路径logout __ 但是 框架默认做了一个确认，
//                .logoutUrl("/myLogout")//默认logout 页面， 不是登出处理路径，其实也没有什么处理路径；没有logoutProcessingUrl

                // 同时配置时，logoutRequestMatcher会和 logoutUrl 冲突吗？ 测试发现是 替换！即使用logoutRequestMatcher， 忽略logoutUrl
                // 如果 logoutUrl、logoutRequestMatcher 都没有， 则使用 POST logout； 此时get /logout可以访问，并且被302到myLogoutSuccessUrl
//                 .logoutRequestMatcher(new AntPathRequestMatcher("/logout2", "GET"))

                // logoutSuccessUrl必须以/ 开头， 否则启动都启动不了： defaultTarget must start with '/' or with 'http(s)'
                // logoutSuccessUrl也无需要放行， 原因应该是同loginProcessingUrl
                .logoutSuccessUrl("/myLogoutSuccessUrl")

                // 可以任意添加； 而且，很明显 logoutHandler执行会在logoutSuccessHandler之前
                .addLogoutHandler(logoutHandler)

                // logout的url和Handler同时配置， 则使用logoutSuccessHandler， 忽略logoutSuccessUrl
                // 不能是 null， 否则启动不了： defaultTarget must start with '/' or with 'http(s)'； 它不像successHandler，如果successHandler为空，则报错是很准确的： successHandler cannot be null
                 .logoutSuccessHandler(tigerLogoutSuccessHandler)  //logout的url和Handler 最好只配置一个

                // 如果invalidateHttpSession 参数为false， 那么logout 的意义何在?.. TODO
                // 测试发现即使参数false， 其实也是被登出了的； 似乎不起作用， 并且跟 deleteCookies 无关！
                .invalidateHttpSession(false)

                // 测试发现即使参数false， 其实也是被登出了的； 似乎不起作用， 并且跟 deleteCookies 无关！
                .clearAuthentication(false)

                // 加上permitAll还是很有必要的（虽然不写也不会报错）， 否则需要手工放行logoutSuccessUrl中的/myLogoutSuccessUrl 等
                .permitAll()
                .deleteCookies("JSESSIONID")//清除cook键值; 不清除其实也不要紧， 因为它已经失效了，服务端不存在这个JSESSIONID的cookie，浏览器发过来也匹配不是
         ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser(username)
            .password(passwordEncoder().encode(password))
            .roles("USER")
        .and()
        .withUser("b")
                .password(passwordEncoder().encode("1"))
                .roles("USER").and()
        .withUser("admin")
                .password(passwordEncoder().encode("1"))
                .roles("ADMIN")
        ;
    }


    // springboot thymeleaf security 静态资源 302——thymeleaf的html 到底算不算是静态资源？
    /*
        注意观察日志：
        DefaultSecurityFilterChain     : Creating filter chain: Ant [pattern='/  ** /  *.html'], []
        DefaultSecurityFilterChain     : Creating filter chain: Ant [pattern='/assets/**'], []
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 允许所有的.html结尾的！不能是 *.html、 **.html 需要有 / 前缀
        // ！它匹配不到任何端点！ 而/*.html是可以的，但只能匹配 一级目录！
        web.ignoring().antMatchers("/**/*.html");

//        web.ignoring().mvcMatchers("**.html");
        // 静态资源方向发行，不起作用！ 依然被拦截，依然302到login page！ 因为静态资源的路径默认是不包括 /assets/  /js/， 必须要那边先放行！

        // 下面的 /templates /public/ /static 通常是默认静态资源的查找路径， 如果如下配置，则被当做url 匹配patter，通常是不对的！就是说，通常不应该这样配置！
        // web.ignoring().antMatchers("/mapper/**", "/public/**", "/templates/**", "/static/**", "/images/**");

        // 访问 http://192.168.1.103:8080/js/my.js 其实是从静态资源路径中寻找，访问 src\main\resources\static\js\my.js， 因为下面/js/放行了， 所以是可以直接访问！
        // 但是，如果不存在 src\main\resources\static\js\my.js， 那么就会被302到登录页面！
        // 但是，如果直接访问 http://192.168.1.103:8080/static/js/my.js， 那么就会被302到登录页面！因为不存在！就是说，访问的时候，不需要/static/！有的话，就是画蛇添足！
        // 通常，应该是把 所有静态资源文件放到/static/目录下！ 那 /public/ 目录呢？ 是那些没有任何限制的静态文件！
        web.ignoring().antMatchers("/js/**", "/assets/**");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

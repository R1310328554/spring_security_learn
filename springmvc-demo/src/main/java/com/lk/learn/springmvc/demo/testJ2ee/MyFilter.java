package com.lk.learn.springmvc.demo.testJ2ee;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


// 它的顺序是在 spring 之前， 还是之后？？
@WebFilter("/te/*") // 匹配 /te/.abd
public class MyFilter implements Filter {

    @Override
    public void destroy() {
        System.out.println("MyFilter.destroy");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filterConfig = [" + filterConfig + "]");
    }

    /*

    doFilter:26, MyFilter (com.lk.learn.springmvc.demo.testJ2ee)
        internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
        doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
        doFilterInternal:99, RequestContextFilter (org.springframework.web.filter)
        doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
        internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
        doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
        doFilterInternal:92, FormContentFilter (org.springframework.web.filter)
        doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
        internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
        doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
        doFilterInternal:93, HiddenHttpMethodFilter (org.springframework.web.filter)
        doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
        internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
        doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
        doFilterInternal:200, CharacterEncodingFilter (org.springframework.web.filter)
        doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
        internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
        doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
        invoke:200, StandardWrapperValve (org.apache.catalina.core)
        invoke:96, StandardContextValve (org.apache.catalina.core)
        invoke:490, AuthenticatorBase (org.apache.catalina.authenticator)
        invoke:139, StandardHostValve (org.apache.catalina.core)
        invoke:92, ErrorReportValve (org.apache.catalina.valves)
        invoke:74, StandardEngineValve (org.apache.catalina.core)
        service:343, CoyoteAdapter (org.apache.catalina.connector)
        service:408, Http11Processor (org.apache.coyote.http11)
        process:66, AbstractProcessorLight (org.apache.coyote)
        process:834, AbstractProtocol$ConnectionHandler (org.apache.coyote)
        doRun:1415, NioEndpoint$SocketProcessor (org.apache.tomcat.util.net)
        run:49, SocketProcessorBase (org.apache.tomcat.util.net)
        runWorker:1149, ThreadPoolExecutor (java.util.concurrent)
        run:624, ThreadPoolExecutor$Worker (java.util.concurrent)
        run:61, TaskThread$WrappingRunnable (org.apache.tomcat.util.threads)
        run:748, Thread (java.lang)

     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 这里的 servletRequest 其实是一个Facade： RequestFacade
        // servletRequest = [org.apache.catalina.connector.RequestFacade@228072ff], servletResponse = [org.apache.catalina.connector.ResponseFacade@3f2b0f75], filterChain = [org.apache.catalina.core.ApplicationFilterChain@7d2c5b51]
        System.out.println("servletRequest = [" + servletRequest + "], servletResponse = [" + servletResponse + "], filterChain = [" + filterChain + "]");

        servletRequest.getRequestDispatcher("a");
        filterChain.doFilter(servletRequest, servletResponse);

    }
}

package com.lk.learn.springmvc.demo.testJ2ee;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// SpringBoot-三种方式创建Servlet、Listener、Filter； 方式一
//@Configuration
public class MyJ2eeCfg {

    //@Bean是一个方法级别上的注解，主要用于配置类里 相当于<beans> <bean id="" class="" > </beans>
    @Bean
    public ServletRegistrationBean myServletRegistrationBean(){

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new MyServlet(), "/myserv123");
        return servletRegistrationBean;
    }// https://blog.csdn.net/liuhaiyang98/article/details/120581790


    @Bean
    public FilterRegistrationBean filter_twoBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new MyFilter());
        filterRegistrationBean.addUrlPatterns("/tes/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filter_oneBean(){
        return new FilterRegistrationBean(new filter_two(),myServletRegistrationBean());
    }


    @Bean
    public ServletListenerRegistrationBean listener_oneBean(){
        //// ServletListener 不需要参数，启动的时候就会执行！
        return new ServletListenerRegistrationBean(new listener_Session());
    }// https://blog.csdn.net/qq_41341288/article/details/96725281


}

package com.lk.learn.springmvc.demo.testJ2ee;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@Component // 这样也能够创建一个 Filter， 那么它名字是？ fqn ！ 它的url pattern 是？？ 是/ ， 拦截所有！
public class MyFilter222 implements Filter {

    @Override
    public void destroy() {
        System.out.println("Component.destroy");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Component filterConfig = [" + filterConfig + "]");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("MyFilter222.doFilter");
        filterChain.doFilter(servletRequest, servletResponse);
        // System.out.println("servletRequest = [" + servletRequest + "], servletResponse = [" + servletResponse + "], filterChain = [" + filterChain + "]");
    }

}

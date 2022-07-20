package com.lk.learn.springmvc.demo.testJ2ee;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter
public class MyFilter implements Filter {

    @Override
    public void destroy() {
        System.out.println("MyFilter.destroy");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filterConfig = [" + filterConfig + "]");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("servletRequest = [" + servletRequest + "], servletResponse = [" + servletResponse + "], filterChain = [" + filterChain + "]");

        filterChain.doFilter(servletRequest, servletResponse);

    }
}

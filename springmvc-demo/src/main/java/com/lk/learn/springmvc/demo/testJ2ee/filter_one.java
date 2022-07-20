package com.lk.learn.springmvc.demo.testJ2ee;

import javax.servlet.*;
import java.io.IOException;

public class filter_one implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter_one-init()");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter_one-doFilter()");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("filter_one-destroy()");
    }
}

  class filter_two implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter_two-init()");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter_two-doFilter()");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("filter_two-destroy()");
    }
}

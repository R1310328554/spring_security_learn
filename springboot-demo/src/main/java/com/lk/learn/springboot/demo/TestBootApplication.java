package com.lk.learn.springboot.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


/*
    war形式打包的时候， 可以继承 SpringBootServletInitializer
 */
@SpringBootApplication
@EnableConfigurationProperties(TestLkProperties.class)
public class TestBootApplication extends SpringBootServletInitializer implements ServletContextInitializer {

    @Autowired
    TestLkProperties lkProperties;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TestBootApplication.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        System.out.println("lkProperties = " + lkProperties);

    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestBootApplication.class, args);
    }
 }

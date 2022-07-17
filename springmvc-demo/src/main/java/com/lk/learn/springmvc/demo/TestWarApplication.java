package com.lk.learn.springmvc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/*
    war形式打包的时候， 可以继承 SpringBootServletInitializer
 */
//@SpringBootApplication
public class TestWarApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TestWarApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestWarApplication.class, args);
    }
 }

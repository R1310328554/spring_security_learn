package com.lk.learn.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/*
    war形式打包的时候， 可以继承 SpringBootServletInitializer
 */
@SpringBootApplication
public class TestBootApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TestBootApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestBootApplication.class, args);
    }
 }

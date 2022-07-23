package com.lk.learn.springboot.demo.domain;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class StartTask2 implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("-----ApplicationRunner:springboot start......");
    }
}

@Component
class StartTask implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("-----CommandLineRunner:springboot start......");
    }
}


@Component
class TestPostConstruct {
    // 当bean创建完成加载时执行
    @PostConstruct
    public String init(){
        System.out.println("-----@PostConstruct: method 【init】 is run.....");
        return "hello";
    }

    // 关闭前执行
    @PreDestroy
    public void destroyPro(){
        System.out.println("-----@PreDestroy: method 【destroyPro】 is run.....");
    }

}


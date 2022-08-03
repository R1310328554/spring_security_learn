package com.lk.learn.springboot.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/")
public class TestController {

    @RequestMapping("/test")
    public String test() {
        System.out.println("HomeController.test    " + System.currentTimeMillis());
        return "test " + System.currentTimeMillis();
    }

    @RequestMapping("/test2")
    public String test2() {
        System.out.println("HomeController.test2    " + System.currentTimeMillis());
        return "test2 " + System.currentTimeMillis();
    }
}

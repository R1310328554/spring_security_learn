package com.lk.learn.springboot.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/customer")
    public String customer() {
        System.out.println("HomeController.customer");
        return "only customer can see";
    }

    @RequestMapping("/admin")
    public String admin() {
        System.out.println("HomeController.admin");
        return "only admin cas see";
    }
}

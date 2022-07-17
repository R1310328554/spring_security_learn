package com.lk.learn.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
public class UserController {

    // 登录之后，Principal principal 就不会为null，否则就是null； 这个参数是security框架自动赋值的！
    @GetMapping("/user/me")
    public Principal user(Principal principal) {
        return principal;
    }


    @RequestMapping("/aa")
    public void aa( ) throws IOException {
        System.out.println("Controller层.aa");
    }


    @RequestMapping("/a")
    public String a( ) throws IOException {
        System.out.println("Controller层.a");
        return "a";
    }

    @RequestMapping("/test/aa")
    public void testaa( ) throws IOException {
        System.out.println("Controller层.testaa");
    }

    @RequestMapping("/test/aaaa")
    public void testaaaasdaa( ) throws IOException {
        System.out.println("Controller层.testaaa aaa");
    }

    @RequestMapping("/test/aa/aa")
    public void testaaaa( ) throws IOException {
        System.out.println("Controller层.testaaaaa");
    }

    @RequestMapping("/test/bb/aa")
    public void testaabbaa( ) throws IOException {
        System.out.println("Controller层.testabbbaaaa");
    }


    @RequestMapping("/test/bb")
    public void testbb( ) throws IOException {
        System.out.println("Controller层.testbb");
    }


}

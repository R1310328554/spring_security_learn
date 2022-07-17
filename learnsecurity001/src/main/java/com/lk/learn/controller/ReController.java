package com.lk.learn.controller;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;

@Controller
public class ReController {

    @RequestMapping("/js")
    public String  my( ) throws IOException {
        System.out.println("Controller层.my");
        return "my.html";
    }

    @RequestMapping("/login2")
    public String  login( Principal principal ) throws IOException {
        System.out.println("Controller层.login2"); // form 表单不会执行到这里， loginProcessingUrl 配置了也没有用； 除非把 loginProcessingUrl去掉。
//        String name = principal.getName();// npe
//        System.out.println("name = " + name);
        return "sec.html";
    }

}

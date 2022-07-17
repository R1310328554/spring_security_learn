package com.lk.learn.controller;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.security.Principal;


/*
三、Spring Security之方法调用级别的安全性Demo
Spring Security提供了三种不同的安全注解：

Spring Security自带的@Secured注解；
JSR-250的@RolesAllowed注解；
表达式驱动的注解，包括@PreAuthorize、@PostAuthorize、@PreFilter和@PostFilter。

注解	        描述
@PreAuthorize	在方法调用之前，基于表达式的计算结果来限制对方法的访问
@PostAuthorize	允许方法调用，但是如果表达式计算结果为false，将抛出一个安全性异常
@PostFilter	    允许方法调用，但必须按照表达式来过滤方法的结果
@PreFilter	    允许方法调用，但必须在进入方法之前过滤输入值

1、启用基于注解的方法安全性
     在Spring中，如果要启用基于注解的方法安全性，关键之处在于要在配置类上使用@EnableGlobalMethodSecurit
————————————————
版权声明：本文为CSDN博主「Gent_倪」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_37771475/article/details/86153799

 */
@Controller
public class TestAnnoController {


    @GetMapping(value = "/admin")
    @Secured("ROLE_ADMIN")
    public String admin(){
        return "admin";
    }

    @GetMapping(value = "/admin2")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin2(){
        return "admin";
    }

    @GetMapping(value = "/PostAuthorize")
    @PostAuthorize("hasRole('ROLE_ADMIN')")
    public String PostAuthorize(){
        return "admin";
    }

    @GetMapping(value = "/admin3")
    @PostFilter("hasAnyAuthority('Authority_ADMIN')")
    public String admin3(){
        return "admin";
    }

    @GetMapping(value = "/PreFilter")
    @PreFilter("hasPermission('Permi_ADMIN')")
    public String PreFilter(){
        return "admin";
    }


    @GetMapping(value = "/rolesAllowed")
    @RolesAllowed("ROLE_ADMIN")
    public String rolesAllowed(){
        return "admin";
    }


    @GetMapping(value = "/PermitAll")
    @PermitAll
    public String PermitAll(){
        return "admin";
    }

    @GetMapping(value = "/PostFilter")
    @PostFilter("authentication.principal.username=='tom'")
    public String PostFilter(){
        return "admin";
    }


    @GetMapping(value = "/DenyAll")
    @DenyAll
    public String DenyAll(){
        return "admin";
    }


}

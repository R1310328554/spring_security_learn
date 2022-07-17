package com.lk.learn.dynamicAuth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class HelloController {

    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user";
    }
    @GetMapping("/db/hello")
    public String db() {
        return "hello db";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "当前登录用户：" + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * 获取用户明细
     * @param principal
     * @return
     */
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    public Principal getUserDetails(Principal principal) {
        log.info("用户名:{}",principal.getName());
        return principal;
    }

    /**
     * 获取用户明细
     * @param authentication
     * @return
     */
    @RequestMapping(value = "getUserInfo2", method = RequestMethod.GET)
    public Authentication getUserInfo2(Authentication authentication) {
        log.info("用户名:{}", authentication);
        return authentication;
    }

    /**
     * 只获取用户信息
     * @param userDetails
     * @return
     */
    @RequestMapping(value = "getUser", method = RequestMethod.GET)
    public UserDetails getUser(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("用户名:{}",userDetails.getUsername());
        return userDetails;
    }


}

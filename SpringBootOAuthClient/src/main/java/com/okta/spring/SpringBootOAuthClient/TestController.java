
package com.okta.spring.SpringBootOAuthClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class TestController {


    @GetMapping("/my")
    public String my( ) throws IOException {
        System.out.println("WeiboController.my");
        return "my";
    }

    @PostMapping("/my")
    public String my2( ) throws IOException {
        System.out.println("WeiboController.my 2");
        return "my";
    }

    /*
         目前配置的微博的授权回调页：   http://192.168.1.103:8999/v1/weibo/user/login
     */
    @RequestMapping("/securedPage")
    public void securedPage( ) throws IOException {
        System.out.println("访问受保护页面securedPage，必须先登录");
    }


}

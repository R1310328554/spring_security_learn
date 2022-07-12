
package com.okta.spring.AuthorizationServerApplication.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;

@Controller
public class LoginController {


    // 500  Error resolving template [/], template might not exist or might not be accessible by any of the configured Template Resolvers
    @RequestMapping("/")
    public String idx( ) throws IOException {
        System.out.println("为什么这里/ 不能直接跳转到index、index.html?.  这个RequestMapping应该不用写才对..");
        return "/index";
    }


    @RequestMapping("/index")
    public String index( ) throws IOException {
        System.out.println("WeiboController. 首页 啊");
        return "/index";
    }


    @RequestMapping("/40x")
    public String status40x( ) throws IOException {
        System.out.println("不受 form登录保护，但是也没有放行，于是受到oauth2保护，一直返回401，无法访问！");
        return "40x";
    }

    @GetMapping("/myLogin")
    public String myLogin( ) throws IOException {
        System.out.println("访问自定义登录页.myLogin");
        return "myLogin";
    }

    @PostMapping("/myLogin")
    public String myLogin2() throws IOException {
        System.out.println("post 提交 访问自定义登录页.myLogin 2");
        return "myLogin";
    }

    @RequestMapping("/doLogin")
    public String doLogin( ) throws IOException {
        System.out.println("自定义登录-提交处理.doLogin");
        return "lo";
    }

    @RequestMapping("/tourist")
    public String tourist( ) throws IOException {
        System.out.println("游客访问无须权限！");
        return "tourist";
    }

    @RequestMapping("/securedPage")
    public void securedPage(Principal principal, HttpServletRequest request) throws IOException {
        System.out.println("访问受保护页面securedPage，必须先登录 -- " + principal);
        request.setAttribute("principal", principal);

        dumpReqAttributes(request);
    }


    @RequestMapping("/securedPage2")
    public void securedPage2(Principal principal) throws IOException {
        System.out.println("访问受保护页面securedPage 2，必须先登录 -- " + principal);
    }



    public static void dumpReqAttributes(HttpServletRequest request) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        int attributesCnt = 0;
        while (attributeNames.hasMoreElements()) {
            String s =  attributeNames.nextElement();
            System.out.println("请求属性： " + s + " = " + request.getAttribute(s));
            attributesCnt++;
        }
        System.out.println(" request attributesCnt = " + attributesCnt);
    }


    @RequestMapping("/myLogoutSuccessUrl")
    public String  myLogoutSuccessUrl(@CookieValue(value = "aimee-test-token", required = false)String access_token
            , @CookieValue(value = "thirdType", required = false)String thirdType, HttpServletResponse response) {
        System.out.println("myLogoutSuccessUrl      access_token = [" + access_token + "], thirdType = [" + thirdType + "], response = [" + response + "]");
        return "myLogoutSuccessUrl.html";
    }

}

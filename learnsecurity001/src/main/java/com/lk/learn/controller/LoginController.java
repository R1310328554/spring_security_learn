
package com.lk.learn.controller;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author luoaki
 */
@Controller
public class LoginController {


    @RequestMapping("/")
    public String idx(Model model, HttpServletRequest request) throws IOException {
        System.out.println("Controller层. 默认首页"); // 不要再 404 啊

        model.addAttribute("name","bigsai");

        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
        for (Iterator<Map.Entry<String, String[]>> iterator = entries.iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String[]> next =  iterator.next();
            System.out.println("请求参数： " + next.getKey() + "  = " + next.getValue()[0]);
        }

        dumpReqAttributes(request);

        return "index";
//        return "index.html";// 两个返回都可以，后缀 .html 不是必须的. why ？
    }

    private void dumpReqAttributes(HttpServletRequest request) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        int attributesCnt = 0;
        while (attributeNames.hasMoreElements()) {
            String s =  attributeNames.nextElement();
            System.out.println("请求属性： " + s + " = " + request.getAttribute(s));
            attributesCnt++;
        }
        System.out.println(" request attributesCnt = " + attributesCnt);
    }

    @RequestMapping("/test")
    public String test( ) throws IOException {
        System.out.println("Controller层.test");
        return "test";
    }

    @RequestMapping("/securedPage")
    public String securedPage( ) throws IOException {
        System.out.println("Controller层.securedPage， 已经登录，欢迎xx！（必须先登录！才能执行到这里来！）");
        return "securedPage";
    }

    @GetMapping("/myLogin")
    public String myLogin( ) throws IOException {
        System.out.println("Controller层.myLogin 先去登录吧！");
        return "myLogin";
    }

    @PostMapping("/myLogin")
    public String myLogin2() throws IOException {
        System.out.println("Controller层.myLogin 2");
        return "myLogin";
    }

    @RequestMapping("/doLogin") // 浏览器直接访问 http://192.168.1.103:8080/doLogin， 是get 方法， 是返回sec 页面！ post 才是loginProcessingUrl
    public String doLogin( ) throws IOException {
        System.out.println("Controller层.doLogin");// loginProcessingUrl 根本不会执行这个方法
        return "sec";
    }

    @RequestMapping(value = "/successForwardUrl", produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String  successForwardUrl( ) throws IOException {
        System.out.println("Controller层.successForwardUrl");
        return "successForwardUrl.html";
    }

    @RequestMapping("/successForwardUrl2")
    public String  successForwardUrl2( ) throws IOException {
        System.out.println("Controller层.successForwardUrl2");
        return "successForwardUrl.html";
    }

    @RequestMapping("/defaultSuccessUrl")
    public String  defaultSuccessUrl( ) throws IOException {
        System.out.println("Controller层.defaultSuccessUrl");
        return "defaultSuccessUrl.html";
    }

    @RequestMapping("/failureForwardUrl")
    public String  failureForwardUrl( ) throws IOException {
        System.out.println("Controller层.failureForwardUrl");
        return "failureForwardUrl.html";
    }


    @RequestMapping("/loginFail")
    public String  loginFail( ) throws IOException {
        System.out.println("Controller层.loginFail");
        return "loginFail.html";
    }

    @RequestMapping("/myLogout")
    public String  myLogout( ) throws IOException {
        System.out.println("Controller层.myLogout");
        return "myLogout.html";
    }

    @RequestMapping("/myLogoutSuccessUrl")
    public String  myLogoutSuccessUrl( ) throws IOException {
        System.out.println("Controller层.myLogoutSuccessUrl，成功登出， 准备返回myLogoutSuccessUrl页面！");
        return "myLogoutSuccessUrl";
    }

    // https://www.jianshu.com/p/3ce4504e4bc4
    @RequestMapping("/denied1")
    public String  denied1(Exception e ) throws IOException {
        System.out.println("Controller层.denied1，成功登出， 准备返回denied1页面！");
        System.out.println("e = " + e);
        return "denied1";
    }

}

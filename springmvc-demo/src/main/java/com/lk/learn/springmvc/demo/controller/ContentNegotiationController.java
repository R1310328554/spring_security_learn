package com.lk.learn.springmvc.demo.controller;

import com.lk.learn.springmvc.demo.domain.FileDomain;
import com.lk.learn.springmvc.demo.domain.UserForm;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/*

 */
@RestController
@RequestMapping("/nego/")
public class ContentNegotiationController {

    @RequestMapping(value = "/test", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserForm test() {
        UserForm userForm = new UserForm();
        userForm.setName("name1");
        return userForm;
    }

    public String login() {
        //转发到一个请求方法（同一个控制器类可以省略/index/）
        return "forward:/index/isLogin";
    }

    /*
        下面都是支持的：
        http://192.168.1.103:8080/nego/any?format=xxm
        http://192.168.1.103:8080/nego/any?format=json
        http://192.168.1.103:8080/nego/any?format=xml
        http://192.168.1.103:8080/nego/any?format=abc 不支持的，则以默认的xml 返回数据！


        http://192.168.1.103:8080/nego/any.xxm 因为默认不支持后缀形式，所以会匹配不上，然后尝试寻找静态资源文件——找不到静态资源文件则406， 找到了就变成了下载
        http://192.168.1.103:8080/nego/any.xml


     */
    @RequestMapping("/hi")
    public FileDomain hi(HttpServletRequest request) {
        //重定向到一个请求方法
        FileDomain domain = new FileDomain();
        System.out.println("request = [" + request + "]");
        String queryString = request.getQueryString();
        System.out.println("queryString = " + queryString);
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("requestURL = " + requestURL);

        domain.setDescription("Hi,Desc " + System.currentTimeMillis());
        return domain;
    }

    @RequestMapping("/any")
    public UserForm any() {
        //转发到一个视图
        UserForm userForm = new UserForm();
        return userForm;
    }

}

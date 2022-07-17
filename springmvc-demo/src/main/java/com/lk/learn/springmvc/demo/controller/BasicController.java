package com.lk.learn.springmvc.demo.controller;

import com.lk.learn.springmvc.demo.domain.UserForm;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/*
Spring MVC 请求方式分为转发、重定向 2 种，分别使用 forward 和 redirect 关键字在 controller 层进行处理。

重定向是将用户从当前处理请求定向到另一个视图（例如 JSP）或处理请求，以前的请求（request）中存放的信息全部失效，并进入一个新的 request 作用域；转发是将用户对当前处理的请求转发给另一个视图或处理请求，以前的 request 中存放的信息不会失效。

转发是服务器行为，重定向是客户端行为。
1）转发过程
客户浏览器发送 http 请求，Web 服务器接受此请求，调用内部的一个方法在容器内部完成请求处理和转发动作，将目标资源发送给客户；在这里转发的路径必须是同一个 Web 容器下的 URL，其不能转向到其他的 Web 路径上，中间传递的是自己的容器内的 request。

在客户浏览器的地址栏中显示的仍然是其第一次访问的路径，也就是说客户是感觉不到服务器做了转发的。转发行为是浏览器只做了一次访问请求。
2）重定向过程
客户浏览器发送 http 请求，Web 服务器接受后发送 302 状态码响应及对应新的 location 给客户浏览器，客户浏览器发现是 302 响应，则自动再发送一个新的 http 请求，请求 URL 是新的 location 地址，服务器根据此请求寻找资源并发送给客户。

在这里 location 可以重定向到任意 URL，既然是浏览器重新发出了请求，那么就没有什么 request 传递的概念了。在客户浏览器的地址栏中显示的是其重定向的路径，客户可以观察到地址的变化。重定向行为是浏览器做了至少两次的访问请求。


在 Spring MVC 框架中，不管是重定向或转发，都需要符合视图解析器的配置，如果直接转发到一个不需要 DispatcherServlet 的资源，例如：
return "forward:/html/my.html";

则需要使用 mvc:resources 配置：
<mvc:resources location="/html/" mapping="/html/**" />


默认情况下 RequestMapping 的consumes produces 是没有限制的！也就是 all ！

 */
@Controller
@RequestMapping("/basic/")
public class BasicController {

    /*
        不能  Could not find acceptable representation， application/xml
        HttpMediaTypeNotAcceptableException: Could not find acceptable representation

        调试发现是 AbstractMessageConverterMethodProcessor#writeWithMessageConverters 方法的 messageConverters 无法write text/plain
        0 = {ByteArrayHttpMessageConverter@7057}
        1 = {StringHttpMessageConverter@7058}
        2 = {StringHttpMessageConverter@7059}  支持 把String 输出为 text/plain
        3 = {ResourceHttpMessageConverter@7060}
        4 = {ResourceRegionHttpMessageConverter@7061}
        5 = {SourceHttpMessageConverter@7062}
        6 = {AllEncompassingFormHttpMessageConverter@7063}
        7 = {MappingJackson2HttpMessageConverter@7064}      支持将任意type的数据，转换为application/json （ supports 固定返回true ）
        8 = {MappingJackson2HttpMessageConverter@7065}
        9 = {Jaxb2RootElementHttpMessageConverter@7066}


解决方案3：

返回类型为JSONObject, 编写一个自己converter.-- JaksonConverter

     */
//    @RequestMapping(value = "/test", consumes = "application/json", produces = "application/xml")
    @RequestMapping(value = "/test", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
//    @RequestMapping(value = "/test", consumes = "application/json", produces = MediaType.APPLICATION_XML_VALUE)
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
    @RequestMapping("/isLogin")
    public String isLogin() {
        //重定向到一个请求方法
        return "redirect:/index/isRegister";
    }
    @RequestMapping("/isRegister")
    public String isRegister() {
        //转发到一个视图
        return "register";
    }

}

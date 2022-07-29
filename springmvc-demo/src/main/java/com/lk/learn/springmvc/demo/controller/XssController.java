package com.lk.learn.springmvc.demo.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/*
    Xss 最常见的几种分类：反射型（非持久型）XSS、存储型（持久型）XSS、DOM型XSS、通用型XSS、突变型XSS。


XSS（Cross Site Scripting）攻击全称跨站脚本攻击，是为不和层叠样式表(Cascading Style Sheets, CSS)的缩写混淆，故将跨站脚本攻击缩写为XSS，XSS是一种经常出现在web应用中的计算机安全漏洞，它允许恶意web用户将代码植入到提供给其它用户使用的页面中。比如这些代码包括HTML代码和客户端脚本。

XSS，两个层次是服务器端和浏览器端。协议就是HTML/CSS/JavaScript。对于服务器端来说，html是数据（字符串）；对于浏览器端来说，html是指令。XSS的原理，就是破坏html/css/js的构造。

主要危害

　　1、盗取各类用户帐号，如机器登录帐号、用户网银帐号、各类管理员帐号
　　2、控制企业数据，包括读取、篡改、添加、删除企业敏感数据的能力
　　3、盗窃企业重要的具有商业价值的资料
　　4、非法转账
　　5、强制发送电子邮件
　　6、网站挂马
　　7、控制受害者机器向其它网站发起攻击

XSS根据效果不同可以分为三类

反射型XSS(reflected)
存储型XSS(stored)
DOM Based XSS


1.反射型XSS：
    http://localhost:8080/helloController/search?name=<script>alert("hey!")</script>
    http://localhost:8080/helloController/search?name=<img src='w.123' onerror='alert("hey!")'>
    http://localhost:8080/helloController/search?name=<a onclick='alert("hey!")'>点我</a>
    // 有时攻击者会伪造一个图片，让你点击后链接跳转URL

2.存储型XSS
存储型XSS，也叫持久型XSS，主要是将XSS代码发送到服务器（不管是数据库、内存还是文件系统等。），然后在下次请求页面的时候就不用带上XSS代码了。最典型的就是留言板XSS。用户提交了一条包含XSS代码的留言到数据库。当目标用户查询留言时，那些留言的内容会从服务器解析之后加载出来。浏览器发现有XSS代码，就当做正常的HTML和JS解析执行。XSS攻击就发生了。
例如:该网页有一个发表评论的功能，该评论会写入后台数据库，其他用户访问留言板的时候，会从数据库中加载出所有的评论并执行了相应的 js 代码。


3.DOM型XSS
与前两者完全不同，DOM型XSS是纯前端的XSS漏洞，XSS直接通过浏览器进行解析，就完成攻击。
基于DOM的XSS有时也称为type0XSS。如果服务端对DOM中的数据没有经过严格确认，当用户能够通过交互动态地检查和修改浏览器页面中DOM(DocumentObjectModel)并显示在浏览器上时，就有可能产生这种漏洞，从效果上来说它也是反射型XSS。

 */
@Controller
@RequestMapping("/xss")
public class XssController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("applicationContext = [" + applicationContext + "]");
        this.applicationContext = applicationContext;
    }

    /*
        测试： http://192.168.1.103:8080/xss/xssTest1?param=%3Cscript%3Ewindow.location.href=%22https://qq.com%22%3C/script%3E
            http://192.168.1.103:8080/xss/xssTest1?param=%3Cscript%3Ealert(%22hey!%22)%3C/script%3E
     */
    @RequestMapping("/xssTest1")
    public String  xssTest1(Model model, String param) {
        System.out.println("XssController.xssTest1" + " model = [" + model + "], param = [" + param + "]");

        // param 是前端参数，如果不加处理，直接返回到前端html 展示， 可能引起xss 攻击！ 比如 param = abc; <script>alert('Bingo!')</script>
        //
        model.addAttribute("param2", param);

        return "xssTest1";
    }


    /*
    测试1, 直接访问：
        http://192.168.1.103:8080/xss/xssTest2?name=<script>alert('Bingo!')</script>
        http://192.168.1.103:8080/xss/xssTest2?name=<script>alert("hey!")</script>
    变成：
        http://192.168.1.103:8080/xss/xssTest2?name=%3Cscript%3Ealert(%22hey!%22)%3C/script%3E

    测试2
        http://192.168.1.103:8080/xss/xssTest2?name=<img src='w.123' onerror='alert("hey!")'
        http://192.168.1.103:8080/xss/xssTest2?name=%3Cimg%20src=%27w.123%27%20onerror=%27alert(%22hey!%22)%27

        测试xssTest2 有时候不会弹框，可能是因为点击了“不允许...再向你提示”； 但是改成xssTest3就好了！
     */
    @RequestMapping("/xssTest2")
    public String  xssTest2() {
        System.out.println("XssController.xssTest2");

        return "xssTest2";
    }


    @RequestMapping("/xssTest3")
    public String  xssTest3() {
        System.out.println("XssController.xssTest3");

        return "xssTest3";
    }

    @RequestMapping("/xssSubmit")
    public String  xssSubmit() {
        System.out.println("XssController.xssSubmit");

        return "xssSubmit";
    }


}

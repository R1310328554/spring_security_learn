package com.lk.learn.springmvc.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**

 必须要
 <dependency>
 <groupId>org.apache.tomcat</groupId>
 <artifactId>tomcat-jsp-api</artifactId>
 <version>9.0.16</version>
 </dependency>

 而且 其中version必须指定， 否则就相当于没有引入！


 也没有解决！ 原因其实是 pom 的 dependencies 出现了大片的红色！ mvn clean , package 报错了！

 https://developer.aliyun.com/article/763720 springboot整合jsp，必须是war工程

 */
@Controller // 不能是 @RestController 否则变成了下载！
@RequestMapping("/test/")
// 因为浏览器在 响应content-type为 application/octet-stream格式的时，无法渲染 jsp 文件
public class JspController {

    @RequestMapping(value = "")
    public ModelAndView default2() {
        ModelAndView mv=new ModelAndView();
        mv.addObject("msg","default2 ");
        System.out.println("JspController.default2");
        mv.setViewName("result");
        return mv;
    }

    @RequestMapping(value = "/say")
    public ModelAndView say() {
        ModelAndView mv=new ModelAndView();
        mv.addObject("msg","Hello , SpringBoot!!!");
        mv.setViewName("result");
        return mv;
    }

    @RequestMapping(value = "/speak")
    public String speak(Model model) {
        model.addAttribute("msg","Hello , World!!!");
        System.out.println("model = " + model);

        if (1 == 1) {
            throw new RuntimeException("eeerrr ");
        }

        return "result";
    }
}

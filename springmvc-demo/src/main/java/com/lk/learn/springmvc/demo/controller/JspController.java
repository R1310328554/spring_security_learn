package com.lk.learn.springmvc.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    // 代码也相应的改写下，返回视图时，不能使用@ResponseBody，也就是不能使用@RestController
    @RequestMapping(value = "/")
    public ModelAndView default2() {
        ModelAndView mv=new ModelAndView();
        mv.addObject("msg","msg default2 ");
        System.out.println("JspController.default2");
        mv.setViewName("aa");
        return mv;
    }

    @RequestMapping(value = "/cc")
    public String  cc(Model model) {
        model.addAttribute("msg","msg cc ");
        System.out.println("JspController.cc");
        // redirect 会改变浏览器地址栏的地址！ 前端响应码是 302， 浏览器会重新发起请求！
//        return "redirect:/v1";//  /v1是表示视图名称，还是@RequestMapping 名？ 是后者..
        return "redirect:say";//   相对路径是 相对当前 /test; 绝对路径是相对 /
    }

    @RequestMapping(value = "/dd")
    public String  dd(Model model) {
        model.addAttribute("name","name dd ");
        System.out.println("JspController.dd");
        return "forward:";    // redirect 转发，会仍然携带当前 model！
//        return "redirect:"; // redirect 重定向，不会携带当前 model！
    }

    @RequestMapping(value = "/bb")
    public ModelAndView bb() {
        ModelAndView mv=new ModelAndView();
        mv.addObject("msg","msg bb ");
        System.out.println("JspController.bb");
        mv.setViewName("bb");
        return mv;
    }

    /*
        为什么访问 http://192.168.1.103:8080/test/say 直接变成了下载，都没有进入 FrameworkServlet，
         而http://192.168.1.103:8080/test/bb 却能够进入上面bb方法进行调试？ 浏览器缓存的原因！
     */
    @RequestMapping(value = "/say")
    public ModelAndView say() {
        ModelAndView mv=new ModelAndView();
        mv.addObject("msg","msg Hello , SpringBoot!!!");
        mv.setViewName("result"); // 最终视图的结名称被拼接为 /test/result, 然后找不到，所以又变成了 /error
        return mv;
    }

    @RequestMapping(value = "/speak")
    public String speak(Model model) {
        model.addAttribute("msg","msg Hello , World!!!");
        System.out.println("model = " + model);

        if (1 == 1) {
            throw new RuntimeException("eeerrr ");
        }

        return "result";
    }

    //Servlet原生支持的重定向 - 请求转发处理器
    @RequestMapping("/redirectMVC")
    public void forwardView(HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendRedirect(request.getContextPath()+"/xxx/forwardView");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

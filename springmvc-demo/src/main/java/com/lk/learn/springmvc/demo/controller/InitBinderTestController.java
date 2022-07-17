package com.lk.learn.springmvc.demo.controller;

import com.lk.learn.springmvc.demo.domain.UserForm;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/*
https://blog.csdn.net/wang0907/article/details/108357696

 */
@RestController
public class InitBinderTestController {

    @InitBinder(value = "people") // 每次从 @ModelAttribute("people") 读取数据， 都会执行这个方法 ！！
    public void initBinderSetDefaultPreifixPeople(WebDataBinder dataBinder) {
        dataBinder.setFieldDefaultPrefix("p.");
    }

    @InitBinder(value = "address")
    public void initBinderSetDefaultPreifixAddress(WebDataBinder dataBinder) {
        dataBinder.setFieldDefaultPrefix("a.");// 每次！！
    }

    @RequestMapping("/testInitBinder")
    public String register(@ModelAttribute("person") UserForm person, Model model) {
        System.out.println("person = [" + person + "], model = [" + model + "]");
        return "asdf";
    }

    // dataBinder.setFieldDefaultPrefix("a.") 其实是为了区分 下面的两个相同的 UserForm参数！ 这种情况其实少见！
    @RequestMapping(value = "/test0942")
    @ResponseBody
    public String test0942(@ModelAttribute("people") UserForm people, @ModelAttribute("address") UserForm address) {
        StringBuffer sb = new StringBuffer();
        sb.append(people.toString());
        sb.append("---");
        sb.append(address.toString());
        return sb.toString();
    }


    @RequestMapping(value = "/testErr")
    public String speak(Model model) {
        System.out.println("model = " + model);
        throw new RuntimeException("eeerrr ");
    }

    /*
        只会在当前 controller中起作用； 不是全局的！
     */
    @ExceptionHandler({RuntimeException.class})
    public ModelAndView fix(Exception ex){
        System.out.println("do This");
        return new ModelAndView("error",new ModelMap("ex",ex.getMessage()));
    }



    // 404跳转
    @RequestMapping("404")
    public String notFound(){
        System.out.println("InitBinderTestController.notFound");
        return "404";
    }

    // 500跳转
    @RequestMapping("500")
    public String sysError(){
        System.out.println("InitBinderTestController.sysError");
        return "500";
    }// https://blog.csdn.net/millery22/article/details/123498962

}

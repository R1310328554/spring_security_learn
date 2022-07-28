package com.lk.learn.springmvc.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;



/*
    @ControllerAdvice中
    所有 方法上的 @ModelAttribute、@InitBinder 都会执行； @InitBinder本身不能有参数，否则它只会在对应的时刻执行

    @InitBinder 对方法的参数有什么要求吗
    不在 @ControllerAdvice中， 是否也是一样？

    原理是？
    @ModelAttribute、@InitBinder、@ExceptionHandler 会解析其对应的方法， 然后.

    在什么时候执行？ 有@RestController注解类里面的有@RequstMapping注解的方法


    补充：如果全部异常处理返回json，那么可以使用 @RestControllerAdvice 代替 @ControllerAdvice ，这样在方法上就可以不需要添加 @ResponseBody。


    // 原文链接：https://blog.csdn.net/qq_36829919/article/details/101210250

 */
@ControllerAdvice // 全局的！
// @RestControllerAdvice // 主要针对 异常处理？

public class MyControllerAdvice {

    // @ModelAttribute
    @Value("${user.oauth.clientUrl:a123}")
    private String oauthClientUrl;


    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     */
    @InitBinder
    private void init(@ModelAttribute String ddd) {
        System.out.println("MyControllerAdvice.init");
    }

    /*
     * @InitBinder("person") 对应找到@RequstMapping标识的方法参数 中 —— @RequstMapping的参数需要@ModelAttribute 修饰，否则不起作用
     * 找参数名为person的参数。
     * 在进行参数绑定的时候，以‘p.’开头的都绑定到名为person的参数中。
     */
    @InitBinder("person")
    public void BindPerson(WebDataBinder dataBinder){
        System.out.println("MyControllerAdvice.BindPerson   " + dataBinder);
        dataBinder.setFieldDefaultPrefix("p.");
    }

    @InitBinder("book")
    public void BindBook(WebDataBinder dataBinder){
        dataBinder.setFieldDefaultPrefix("b.");
    }

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器_ 每次http请求requestMapping 都会执行这个方法
     */
//    @InitBinder
    public void globalInitBinder(WebDataBinder binder) {
        System.out.println("MyControllerAdvice.globalInitBinder " + binder);

//        PropertyEditor ldC = new PropertyEditorSupport();
//        binder.registerCustomEditor(LocalDate.class, ldC);
//        binder.convertIfNecessary()
        // binder.addValidators();
//        binder.setValidator();
//        binder.setRequiredFields();
//        binder.setConversionService();
//        binder.validate();

        // 这个会覆盖 WebConversionService#registerJavaDate创建的 DateFormatter， 因为custom定制化的优先！
//        binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
        binder.addCustomFormatter(new MyDateFormatter("yyyy-MM-dd"));
//        binder.addValidators();
    }

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * 访问 任意的， 即使不存在的@RequestMapping 方法也会执行！！
     */
    @ModelAttribute
    private void init(Model model) {
        model.addAttribute("oauthClientUrl", oauthClientUrl);
        System.out.println("MyControllerAdvice.model " + model);
    }

    @ModelAttribute
    public void presetParam(Model model){
        System.out.println("MyControllerAdvice.presetParam");
        model.addAttribute("globalAttr","this is a global attribute");
    }


    // * 如果不需要返回json数据，而要渲染某个页面模板返回给浏览器，那么MyControllerAdvice中可以这么实现：
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleException(IllegalArgumentException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", "参数不符合规范!");
        return modelAndView;
    }

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public Map errorHandler(Exception ex) {
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
    }

    /*

    Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Sun Jul 17 10:39:46 CST 2022
There was an unexpected error (type=Not Found, status=404).
No message available


如果出现404 是不是会到这里来？

     */
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public Map errorHandler2(Throwable ex) {
        System.out.println("MyControllerAdvice.errorHandler2 " + ex);
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
    }


    /*
        如果出现404 是不是会到这里来？ no !
     */
    @ExceptionHandler(value = HttpStatusCodeException.class)
    public Map errorHandler2(HttpStatusCodeException ex) {
        System.out.println("MyControllerAdvice.HttpStatusCodeException " + ex);
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
    }



    // 专门用来捕获和处理Controller层的空指针异常
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView nullPointerExceptionHandler(NullPointerException e)
    {
        ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
        mv.addObject("success",false);
        mv.addObject("mesg","请求发生了空指针异常，请稍后再试");
        return mv;
    }
    /**
     * 处理与用户相关的业务异常 ResponseStatus
     * @return
     */
    /*
    @ExceptionHandler(CustomUserException.class)
    public BaseResult UserExceptionHandler(HttpServletRequest request, CustomUserException e){
        System.out.printf("用户信息异常：Host:{} invoke URL:{},错误信息：{}",request.getRemoteHost(),request.getRequestURL(),e.getMessage());
        return new BaseResult(e.getCode(),false,e.getMessage());
    }*/


}

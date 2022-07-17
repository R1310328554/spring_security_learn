package com.lk.learn.springmvc.demo;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
不同点
WebRequestInterceptor的入参WebRequest是包装了HttpServletRequest 和HttpServletResponse的，通过WebRequest获取Request中的信息更简便。
2.WebRequestInterceptor的preHandle是没有返回值的，说明该方法中的逻辑并不影响后续的方法执行，所以这个接口实现就是为了获取Request中的信息，或者预设一些参数供后续流程使用。
3.HandlerInterceptor的功能更强大也更基础，可以在preHandle方法中就直接拒绝请求进入controller方法。
使用场景
这个在上条已经说了，如果想更方便获取HttpServletRequest的信息就使用WebRequestInterceptor，当然这些HandlerInterceptor都能做，只不过要多写点代码


WebRequestInterceptor间接实现了HandlerInterceptor，只是他们之间使用 WebRequestHandlerInterceptorAdapter 适配器类联系。


重点： 更方便！！
 */
@Component
public class MyWebRequestInterceptor implements WebRequestInterceptor {


    @Override
    public void preHandle(WebRequest webRequest) throws Exception {
        System.out.println("MyWebRequestInterceptor.preHandle " + webRequest);
    }

    @Override
    public void postHandle(WebRequest webRequest, ModelMap modelMap) throws Exception {
        System.out.println("MyWebRequestInterceptor.postHandle  " + "   webRequest = [" + webRequest + "], modelMap = [" + modelMap + "]");
    }

    @Override
    public void afterCompletion(WebRequest webRequest, Exception e) throws Exception {
        System.out.println("MyWebRequestInterceptor.afterCompletion     " + "   webRequest = [" + webRequest + "], e = [" + e + "]");

    }
}

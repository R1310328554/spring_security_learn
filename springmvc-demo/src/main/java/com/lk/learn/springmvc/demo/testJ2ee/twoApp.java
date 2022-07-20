package com.lk.learn.springmvc.demo.testJ2ee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

// SpringBoot-三种方式创建Servlet、Listener、Filter
// 方式二 实现 ServletContextInitializer接口 重写onStartup()方法
@Configuration
public class twoApp implements ServletContextInitializer
{

    public static void main(String[] args) {
        SpringApplication.run(twoApp.class,args);
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 以/开头：/代表的是工程路径（/工程名称）必须要加/
        // 以*开头：必须要加后缀名（后缀名任意）后缀名不能用*

        // 1.【/】匹配带有/的请求。
        // 2.【/abc/bbb】 会匹配带有/abc/bbb结尾的请求，去交个定义的内处理。
        // 3.【*.do】所有以.do结尾的url都会被匹配到。

        // 不能是/test/*.aa  Invalid <url-pattern> [/test/*.aa] in servlet mapping
        // /test/*aa 没有报错，但是不能匹配任何
        // *.aa是后缀匹配，能匹配到 http://192.168.1.103:8080/tes/t123/test.aa.bb.cc./dd.aa.0as.aa
        servletContext.addServlet("servlet_one", MyServlet.class).addMapping("*.html");// servlet的url 不能以* 结尾！若包含*， 只能在开头！
        // 配置*.html之后， 直接访问 *.html 就不行了，会被 MyServlet拦截！

        // addMappingForServletNames的第三个参数是 servletNames，不能使用正则表达式 ！
        //   public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... servletNames) {
        servletContext.addFilter("filter_one", filter_one.class)
                .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "servlet_one");// 会 固定的匹配到servlet_one！

        /*
        <pre>
            1. Filter按照配置在web.xml中的先后顺序,每一个Filter都会进行url-pattern的匹配,匹配成功,则会执行对应的Filter方法

            2. 1个Filter可以设置多个url-pattern

            3. Filter匹配有3种模式

            1、精确匹配：
              /directory/file1.jsp
              /directory/file2.jsp
              /directory/file3.jsp

            2、目录匹配：
              /directory/*

            3、扩展匹配：
            *.jsp

            注意：下面的不支持：
            /direcotry/*.jsp

            /和/*之间的区别：
            <url-pattern>/</url-pattern>： 会匹配到/login这样的路径型url，不会匹配到模式为*.jsp这样的后缀型url
            <url-pattern>/*</url-pattern>：会匹配所有url：路径型的和后缀型的url(包括/login , *.jsp , *.js 和 *.html 等)
            <url-pattern>/</url-pattern>： 甚至会造成The requested resource () is not available.
        </pre>
         */
        // addMappingForUrlPatterns的第三个参数是 urlPatterns ， 可以多个！
        // /test* 和 /test/* 是很大不同的！ / 是目录分隔符！ /test* 匹配不到任何东西！
        // 能够拦截到 controller 请求！
        servletContext.addFilter("filter_two", MyFilter.class)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/test/*");//

        servletContext.addListener(listener_Session.class);
    }
}

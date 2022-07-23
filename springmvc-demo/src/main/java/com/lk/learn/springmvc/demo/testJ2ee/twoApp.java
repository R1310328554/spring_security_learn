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
        // 配置*.html之后， 直接访问 *.html 就不行了，会被 MyServlet拦截！但是不会影响视图的返回，因为那个是forward，而上面的过滤器默认是REQUEST，不经过servlet

        // addMappingForServletNames的第三个参数是 servletNames，不能使用正则表达式 ！
        //   public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... servletNames) {
        /*

        一个Filter拦截的资源可通过两种方式来指定：Servlet 名称和资源访问的请求路径: https://blog.csdn.net/reggergdsg/article/details/52821502

            <filter-mapping>元素用于设置一个 Filter 所负责拦截的资源。一个Filter拦截的资源可通过两种方式来指定：Servlet 名称和资源访问的请求路径
        　　<filter-name>子元素用于设置filter的注册名称。该值必须是在<filter>元素中声明过的过滤器的名字
        　　<url-pattern>设置 filter 所拦截的请求路径(过滤器关联的URL样式)
        　　<servlet-name>指定过滤器所拦截的Servlet名称。
        　　<dispatcher>指定过滤器所拦截的资源被 Servlet 容器调用的方式，可以是REQUEST,INCLUDE,FORWARD和ERROR之一，默认REQUEST。用户可以设置多个<dispatcher> 子元素用来指定 Filter 对资源的多种调用方式进行拦截。如下：

          REQUEST：当用户直接访问页面时，Web容器将会调用过滤器。如果目标资源是通过RequestDispatcher的include()或forward()方法访问
                时，那么该过滤器就不会被调用。
            INCLUDE：如果目标资源是通过RequestDispatcher的include()方法访问时，那么该过滤器将被调用。除此之外，该过滤器不会被调用。
            FORWARD：如果目标资源是通过RequestDispatcher的forward()方法访问时，那么该过滤器将被调用，除此之外，该过滤器不会被调用。
            ERROR：如果目标资源是通过声明式异常处理机制调用时，那么该过滤器将被调用。除此之外，过滤器不会被调用。
         */
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

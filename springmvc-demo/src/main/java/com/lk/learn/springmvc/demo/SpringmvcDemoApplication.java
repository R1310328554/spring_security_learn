package com.lk.learn.springmvc.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
@ServletComponentScan
public class SpringmvcDemoApplication implements CommandLineRunner, ServletContextInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringmvcDemoApplication.class, args);
    }

//    @Autowired
    public void init(DispatcherServlet config) throws ServletException {

        ServletConfig servletConfig = config.getServletConfig();// npe
    }

    public void init(ServletContext sc) throws ServletException
    {
        // 获得到ServletContext对象
        // 获得工程目录 webroot 下文件的绝对路径
        // getRealPath 的参数内容不会被校验，只有真正要用这个路径的时候才知道路径对不对

        //获得根目录
        String path = sc.getRealPath("/");
        System.out.println("ServletContext root path = " + path);// C:\Users\admin\AppData\Local\Temp\tomcat-docbase.2777152794702189536.8080\
        System.out.println(path);
        //获得根目录后追加 /upload，不关心有没有这个文件
        String path1 = sc.getRealPath("/upload");
        System.out.println(path1);

        path1 = sc.getRealPath("");
        System.out.println(path1);

        // 获得工程目录webroot下文件的流
        InputStream in = sc.getResourceAsStream("application.properties");
        Properties prop = new Properties();
        try
        {
            prop.load(in);
            System.out.println(prop.get("spring.thymeleaf.prefix"));
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("SpringmvcDemoApplication.run  测试 SpringFactoriesLoader .. ");

        /*
            SpringFactoriesLoader 测试
         */
        List<String> strings = SpringFactoriesLoader.loadFactoryNames(JaksonConverter.class, this.getClass().getClassLoader());
        System.out.println("strings = " + strings);

    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("SpringmvcDemoApplication.onStartup");
        init(servletContext);

    }
}

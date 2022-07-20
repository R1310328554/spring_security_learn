package com.lk.learn.springmvc.demo.testJ2ee;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//第三种方式 注解的方式@ServletComponentScan —— 1.启动类只需加上@ServletComponentScan注解；2.servlet、listener、filter也添加相应注解

// 当名称和 controller的requestMapping 同名的时候，会覆盖！ 这里的优先级更高, requestMapping就不会执行了！ why ？
@WebServlet("/MyServlet") // 默认参数就是 urlPatterns ， 可以通过 /MyServlet, 进行访问； 默认name 是？ fqn！
public class MyServlet extends HttpServlet
{

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		// 获得到ServletContext对象
		ServletContext sc = config.getServletContext();

		// 获得工程目录 webroot 下文件的绝对路径
		// getRealPath 的参数内容不会被校验，只有真正要用这个路径的时候才知道路径对不对

		//获得根目录
		String path = sc.getRealPath("/");
		System.out.println("ServletContext root path = " + path);
		System.out.println(path);
		//获得根目录后追加 /upload，不关心有没有这个文件
		String path1 = sc.getRealPath("/upload");
		System.out.println(path1);

		path1 = sc.getRealPath("");
		System.out.println(path1);

		// 获得工程目录webroot下文件的流
		InputStream in = sc.getResourceAsStream("/WEB-INF/test1.properties");
		Properties prop = new Properties();
		try
		{
			prop.load(in);
			System.out.println(prop.get("key"));
		// } catch (IOException e) // NullPointerException 导致当前servlet 无法完成初始化，然后就每次访问当前servlet都会执行当前init方法。不会再执行filter
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		System.out.println("MyServlet.doGet   " + "req = " + req);
        System.out.println("servlet_one_get");
        req.getSession().setAttribute("name","小明");
        resp.getWriter().append("servlet_one_get");
	}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        Object name = req.getSession().getAttribute("name");
        System.out.println("MyServlet - session-name - "+name);
        resp.getWriter().append(String.valueOf(name));
        // 原文链接：https://blog.csdn.net/qq_41341288/article/details/96725281
    }
} // https://blog.csdn.net/qq_36260974/article/details/90283466


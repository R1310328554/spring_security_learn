package com.lk.learn.springmvc.demo.controller;

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

@WebServlet("/MyServlet")
public class demo1 extends HttpServlet
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
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		System.out.println("MyServlet demo1.doGet   " + "req = " + req);
	}
} // https://blog.csdn.net/qq_36260974/article/details/90283466

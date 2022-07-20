package com.lk.learn.springmvc.demo.testJ2ee;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/*
http://c.biancheng.net/servlet2/listener.html
监听器的分类
Servlet 规范中定义了 8 个监听器接口，可以用于监听 ServletContext、HttpSession 和 ServletRequest 对象的生命周期和属性变化事件。开发 Servlet 监听器需要实现相应的监听器接口并重写接口中的方法。

监听器 Listener 按照监听的事件划分，可以分为 3 类：
监听对象创建和销毁的监听器
监听对象中属性变更的监听器
监听 HttpSession 中的对象状态改变的监听器
监听对象创建和销毁的监听器
Servlet 规范定义了监听 ServletContext、HttpSession、HttpServletRequest 这三个对象创建和销毁事件的监听器，如下表所示。

事件源	                监听器	                监听器描述	                                创建和销毁方法	                                    调用时机
ServletContext	        ServletContextListener	用于监听 ServletContext 对象的创建与销毁过程	void contextInitialized (ServletContextEvent sce)	当创建 ServletContext 对象时
                                                                                            void contextDestroyed (ServletContextEvent sce)	    当销毁 ServletContext 对象时
HttpSession	            HttpSessionListener	    用于监听 HttpSession 对象的创建和销毁过程	    void sessionCreated (HttpSessionEvent se)	        当创建 HttpSession 对象时
                                                                                            void sessionDestroyed (HttpSessionEvent se)	        当销毁 HttpSession 对象时
ServletRequest	        ServletRequestListener	用于监听 ServletRequest 对象的创建和销毁过程	void requestInitialized (ServletRequestEvent sre)	当创建 ServletRequest 对象时
                                                                                            void requestDestroyed (ServletRequestEvent sre)	    当销毁 ServletRequest 对象时

-- 方方面面，任何你能够想到的， 无数的细节，无数的分类，-- 只要是合理的，大佬们都已经想到了，都已经实现了！
 */
public class listener_request implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        System.out.println("listener_request.requestDestroyed");
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        System.out.println("listener_request.requestInitialized");

    }
}

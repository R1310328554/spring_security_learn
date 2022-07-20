package com.lk.learn.springmvc.demo.testJ2ee;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class listener_Session implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("listener_Session-sessionCreated()");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("listener_Session-sessionDestroyed()");
    }
}

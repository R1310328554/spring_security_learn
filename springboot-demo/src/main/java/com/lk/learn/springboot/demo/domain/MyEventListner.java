package com.lk.learn.springboot.demo.domain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/*
事件监听器注册方式
2、@Component + @EventListener
 */
@Component
public class MyEventListner {
    @EventListener
    public void evendListener(NotifyEvent event){
        if (event instanceof NotifyEvent) {
            System.out.println("EventListener 邮件地址：" + event.getAddress());
            System.out.println("EventListener 邮件内容：" + event.getText());
        } else {
            System.out.println("EventListener 容器本身事件：" + event);
        }
    }
}

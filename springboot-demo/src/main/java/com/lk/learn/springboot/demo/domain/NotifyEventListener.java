package com.lk.learn.springboot.demo.domain;

import org.springframework.context.ApplicationListener;

public class NotifyEventListener implements ApplicationListener<NotifyEvent> {
    @Override
    public void onApplicationEvent(NotifyEvent event) {
        if (event instanceof NotifyEvent) {
            System.out.println("邮件地址 11：" + event.getAddress());
            System.out.println("邮件内容 11 ：" + event.getText());
        } else {
            System.out.println("容器本身事件 11：" + event);
        }
    }
}

package com.lk.learn.springboot.demo.domain;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class SmartListenerTest implements SmartApplicationListener {
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType){
        //这里是类型判断，判断监听器感兴趣的事件
        //可以对多个事件感兴趣，这里就配置了两个事件
        return ApplicationStartedEvent.class.isAssignableFrom(eventType)
                || NotifyEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent appEvent) {
        if (appEvent instanceof NotifyEvent) {
            NotifyEvent event = (NotifyEvent) appEvent;
            System.out.println("SmartApplicationListener 邮件地址：" + event.getAddress());
            System.out.println("SmartApplicationListener 邮件内容：" + event.getText());
        } else {
            if (appEvent instanceof ApplicationStartedEvent){
                System.out.println("SmartApplicationListener 容器本身事件 applicationStartedEvent...");
            } else {
                System.out.println("SmartApplicationListener 容器本身事件：" + appEvent);
            }
        }
    }
}

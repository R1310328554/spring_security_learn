package com.lk.learn.springboot.demo.domain;

import org.springframework.boot.context.event.*;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.reactive.context.ReactiveWebServerInitializedEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/*
事件监听器注册方式
1、@Component + 实现ApplicationListener<>接口

 */
@Component
public class EnvironmentPreparedListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    public EnvironmentPreparedListener() {}

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        System.out.println("EnvironmentPreparedListener..." + event);
    }
}

@Component
class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {
    public ApplicationStartedEventListener() {}

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("ApplicationStartedEventListener..." + event);
    }
}

@Component
class ApplicationStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {
    public ApplicationStartingEventListener() {}

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        System.out.println("ApplicationStartingEventListener..." + event);
    }
}

@Component
class ApplicationContextInitializedEventListener implements ApplicationListener<ApplicationContextInitializedEvent> {
    public ApplicationContextInitializedEventListener() {}

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        System.out.println("ApplicationContextInitializedEventListener..." + event);
    }
}

@Component
class ApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {
    public ApplicationPreparedEventListener() {}

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        System.out.println("ApplicationPreparedEventListener..." + event);
    }
}

/*
class AvailabilityChangeEventListener implements ApplicationListener<AvailabilityChangeEvent> {
    public AvailabilityChangeEventListener() {}

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent event) {
        System.out.println("AvailabilityChangeEvent..." + event);
    }
}
*/

@Component
class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    public ApplicationReadyEventListener() {}

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("ApplicationReadyEventListener..." + event);
    }
}

@Component
class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {
    public ApplicationFailedEventListener() {}

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        System.out.println("ApplicationFailedEventListener， boot启动失败才会执行这个！..." + event);
    }
}

@Component
class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    public ContextRefreshedEventListener() {}

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("ContextRefreshedEventListener..." + event);
    }
}

@Component
class WebServerInitializedEventListener implements ApplicationListener<WebServerInitializedEvent> {
    public WebServerInitializedEventListener() {}

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        System.out.println("WebServerInitializedEventListener..." + event);
    }
}

@Component
class ServletWebServerInitializedEventListener implements ApplicationListener<ServletWebServerInitializedEvent> {
    public ServletWebServerInitializedEventListener() {}

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        System.out.println("ServletWebServerInitializedEventListener..." + event);
    }
}

@Component
class ReactiveWebServerInitializedEventListener implements ApplicationListener<ReactiveWebServerInitializedEvent> {
    public ReactiveWebServerInitializedEventListener() {}

    @Override
    public void onApplicationEvent(ReactiveWebServerInitializedEvent event) {
        System.out.println("ReactiveWebServerInitializedEventListener..." + event);
    }
}

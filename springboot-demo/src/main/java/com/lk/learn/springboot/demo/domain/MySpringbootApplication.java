package com.lk.learn.springboot.demo.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MySpringbootApplication {
    public static void main(String[] args) {
//        SpringApplication.run(MySpringbootApplication.class, args);
        SpringApplication sa = new SpringApplication(MySpringbootApplication.class);

        /*
            事件监听器注册方式
            4、启动类手动注册
         */
        // 添加事件监听
        sa.addListeners(new EnvironmentPreparedListener()); // 2
        sa.addListeners(new ApplicationStartingEventListener()); // 1
//        sa.addListeners(new ApplicationStartedEventListener()); // 5 不需要手动添加，它会自动触发
        sa.addListeners(new ApplicationContextInitializedEventListener()); // 3
        sa.addListeners(new ApplicationPreparedEventListener()); // 4
//        sa.addListeners(new ApplicationReadyEventListener()); // 6  不需要手动添加，它会自动触发
        sa.addListeners(new ApplicationFailedEventListener());  // 5

        sa.addListeners(new ContextRefreshedEventListener());  //

        ConfigurableApplicationContext context  = sa.run(args);

        // 发布 自定义 事件
        NotifyEvent notifyEvent = new NotifyEvent("nortifyEvent", "localhost", "hello lk");
        context.publishEvent(notifyEvent);

    }
}

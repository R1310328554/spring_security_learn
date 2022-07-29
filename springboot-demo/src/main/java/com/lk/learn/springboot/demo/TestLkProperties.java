package com.lk.learn.springboot.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.lk.test")
@Data
public class TestLkProperties {

    String name;
    String address;
    Integer age;
    AliyunAccount aliyunAccount;


    @Data
    static class AliyunAccount {
        String ip;
        String username;
        String password;
        String appid;
        String appSecret;

    }
}

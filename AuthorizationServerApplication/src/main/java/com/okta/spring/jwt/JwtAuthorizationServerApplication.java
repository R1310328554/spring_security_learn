package com.okta.spring.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;


/**
 *  全部json 格式认证、交换数据！
 */
@SpringBootApplication
//@EnableResourceServer
//@EnableAuthorizationServer // 必须要自定义
public class JwtAuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtAuthorizationServerApplication.class, args);
	}

}

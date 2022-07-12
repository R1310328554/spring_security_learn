package com.okta.spring.SpringBootOAuthClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;

@SpringBootApplication
public class SpringBootOAuthClientApplication {

	public static void main(String[] args) {
		OAuth2AccessTokenResponseHttpMessageConverter converter = new OAuth2AccessTokenResponseHttpMessageConverter();
		System.out.println("converter = " + converter);
		DefaultOAuth2UserService userService = new DefaultOAuth2UserService();
		System.out.println("userService = " + userService);

		SpringApplication.run(SpringBootOAuthClientApplication.class, args);
	}

}

package com.okta.spring.AuthorizationServerApplication;

import com.okta.spring.AuthorizationServerApplication.cfg.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer // 不能少，EnableResourceServer配置了一个资源服务器
// 否则客户端：DefaultOAuth2UserService.loadUser 在 Map<String, Object> userAttributes = response.getBody();获取的 userAttributes 为空！
// attributes cannot be empty

// 如果需要支持jwt格式token， 这里改成 JwtConfig
@Import(RedisConfig.class)
public class AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

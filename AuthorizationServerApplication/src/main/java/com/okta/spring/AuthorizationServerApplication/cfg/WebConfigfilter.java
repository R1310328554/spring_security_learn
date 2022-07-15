package com.okta.spring.AuthorizationServerApplication.cfg;

import com.okta.spring.AuthorizationServerApplication.cfg.refresh.OauthTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration // 配置
public class WebConfigfilter implements WebMvcConfigurer {

    @Autowired
    private OauthTokenInterceptor oauthTokenInterceptor; // 实例化拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // super.addInterceptors(registry);
        // 注册自定义的拦截器passwordStateInterceptor
        // 虽然看似拦截了所有请求，但是其顺序比较靠后， 并不是会真正的拦截所有请求， 因为可能就是前面的拦截器、过滤器已经处理完毕了！！todo
        registry.addInterceptor(oauthTokenInterceptor)
            .addPathPatterns("/**") //匹配要过滤的路径
//            .excludePathPatterns("/api/changePasswordByUser/*") //匹配不过滤的路径。密码还要修改呢，所以这个路径不能拦截
//            .excludePathPatterns("/api/passwordStateValid") //密码状态验证也不能拦截
//            .excludePathPatterns("/api/getManagerVersion")//版本信息同样不能拦截
        ;
    }
}

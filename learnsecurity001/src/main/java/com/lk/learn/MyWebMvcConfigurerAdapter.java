package com.lk.learn;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {
//public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.beanName();
//    }

    /**
     * ·
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/js/**")
                .addResourceLocations("classpath:/favicon.ico")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/templates/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("classpath:/jss/")
        ;

        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/templates/")
        ;
        // 在WebSecurityConfigurerAdapter中怎么配置，怎么放行都没有用，需要这里放行，而且需要同时添加 addResourceHandler/ addResourceLocations
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/") // 如果这里注释就不能访问/assets/** 了
                .addResourceLocations("classpath:/templates/")
        ;
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {

    }


}

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
     * 静态资源映射； 对于 Spring Security，静态资源映射是一码事，但如果不在自定义的WebSecurityConfigurerAdapter放行，仍然是无法直接访问的，出现302到登录页
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                // Add a resource handler for serving static resources based on the specified URL path patterns. The handler will be invoked for every incoming request that matches to one of the specified path patterns.
                //Patterns like "/static/**" or "/css/{filename:\\w+\\.css}" are allowed. See org.springframework.util.AntPathMatcher for more details on the syntax
                // 添加静态资源的访问路径， 千万要区分 pathPatterns 和 实际路径！ pathPatterns一般不要添加 /static/前缀！
                // 直接访问 http://192.168.1.103:8080/js/my.js 的时候， 前缀/js/会被博取，然后去/static/等目录查找my.js！！！ 找不到则..
                // 按照addResourceLocations 添加的顺序， 依次查找静态资源！！
                .addResourceHandler("/js/**")

                /*
                Add one or more resource locations from which to serve static content. Each location must point to a valid directory. Multiple locations may be specified as a comma-separated list, and the locations will be checked for a given resource in the order specified.
                    For example, {"/", "classpath:/META-INF/public-web-resources/"} allows resources to be served both from the web application root and from any JAR on the classpath that contains a /META-INF/public-web-resources/ directory, with resources in the web application root taking precedence.
                    For URL-based resources (e.g. files, HTTP URLs, etc) this method supports a special prefix to indicate the charset associated with the URL so that relative paths appended to it can be encoded correctly, e.g. [charset=Windows-31J]http://example.org/path

                    classpath 就指明了 可以搜索 jar包里面的文件！ 否则就是当前项目的内容！
                 */
                .addResourceLocations("classpath:/favicon.ico")// 应该是个路径， 不能是个具体文件名！
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")// 访问 /js/a.html, 会到 /resources/目录下查找/js/a.html，相当于是查找 /resources/js/a.html

//                .addResourceLocations("classpath:/templates/")
                .addResourceLocations("classpath:/static/")

                // 是否需要classpath？ 感觉一般是不需要的，因为，项目文件都存放在本地， 不是jar 里面， 所以..
                .addResourceLocations("classpath:/public/")

                // 是否一定要 / 结尾？ 是！ 试过 "classpath:/js" 就不行！
                // /jss/** 这样恐怕不行， 不需要*， 因为我们需要指定一个路径，不能是ant、正则表达式！
                .addResourceLocations("classpath:/js/")
        ;

        // 其实可以一次性添加多个， 这样就不需要多次调用addResourceHandler；  如果文件存放很有规律的话，可以这样
        // templates 目录的文件通常是不允许直接访问的，但是这里也可以放行出来！！
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/static/") // 这个可以去掉， 因为谁会直接访问templates路径吗？ 而且模板templates的请求会放到 static 目录下？
                .addResourceLocations("classpath:/templates/")
        ;
        // 在WebSecurityConfigurerAdapter中怎么配置，怎么放行都没有用，需要这里放行，而且需要同时添加 addResourceHandler/ addResourceLocations
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/") // 如果这里注释就不能访问/assets/** 了
                .addResourceLocations("classpath:/templates/")
        ;
        // 访问 http://192.168.1.103:8080/assets2/bd_145.png 变成 http://192.168.1.103:8080/assets/bd_145.png
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {

    }


}

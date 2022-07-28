package com.lk.learn.springmvc.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.PathMatcher;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.util.UrlPathHelper;

import java.util.List;

@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        PathMatcher pathMatcher = configurer.getPathMatcher();
        System.out.println("pathMatcher = " + pathMatcher);// 为空， 就使用默认的！

        UrlPathHelper urlPathHelper = configurer.getUrlPathHelper();
        System.out.println("urlPathHelper = " + urlPathHelper);

        Boolean useSuffixPatternMatch = configurer.isUseSuffixPatternMatch();
        System.out.println("useSuffixPatternMatch = " + useSuffixPatternMatch);

    }


    /*
        构建的过程是：
        ContentNegotiationManagerFactoryBean.build
     */
//    @Override
    public void configureContentNegotiation2(ContentNegotiationConfigurer configurer) {
//        List<ContentNegotiationStrategy> contentNegotiationStrategies = new ArrayList<>();// 不能添加一个空的 list ！框架不允许！
//        configurer.strategies(contentNegotiationStrategies);
        configurer.favorPathExtension(true); // 这里的配置和application.properties的配置会覆盖/叠加还是替换？
        configurer.favorParameter(true);
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
//        configurer.mediaType("a", "bb");

        // configurer.useJaf(); 过时
        configurer
                // .ignoreAcceptHeader(true)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("json", MediaType.APPLICATION_JSON)
        ;
//        configurer.useRegisteredExtensionsOnly(true)
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        System.out.println("MyWebMvcConfigurerAdapter.configureAsyncSupport");
//        configurer.setTaskExecutor()
    }

    /*
        Configures a request handler for serving static resources by forwarding the request to the Servlet container's "default" Servlet. This is intended to be used when the Spring MVC DispatcherServlet is mapped to "/" thus overriding the Servlet container's default handling of static resources.
        Since this handler is configured at the lowest precedence, effectively it allows all other handler mappings to handle the request, and if none of them do, this handler can forward it to the "default" Servlet.

        优先级是最低的！只有所有其他 handler mappings 处理不了，才轮到这个默认的处理器！
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();

        //  A RequestDispatcher could not be located for the default servlet 'asdf'
        //	at org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler.handleRequest(DefaultServletHttpRequestHandler.java:124) ~[spring-webmvc-5.1.5.RELEASE.jar:5.1.5.RELEASE]
        //	at org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter.handle(HttpRequestHandlerAdapter.java:53) ~[spring-webmvc-5.1.5.RELEASE.jar:5.1.5.RELEASE]
        //	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1038) ~[spring-webmvc-5.1.5.RELEASE.jar:5.1.5.RELEASE]
        //	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942) ~[spring-webmvc-5.1.5.RELEASE.jar:5.1.5.RELEASE]
        //	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005) ~[spring-webmvc-5.1.5.RELEASE.jar:5.1.5.RELEASE]
        //	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:897) ~[spring-webmvc-5.1.5.RELEASE.jar:5.1.5.RELEASE]

        configurer.enable("asdf");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        System.out.println("MyWebMvcConfigurerAdapter.addFormatters");

        // 如果存在自定义的 DateFormatter， 那么字段上的@DateTimeFormat(pattern = "yyyy.MM.dd")被忽略，不执行！ 因为只选取一个，一个不行就异常！
        //
        registry.addFormatter(new MyDateFormatter());// 这种方式添加的格式化器，会进入 ConversionService converters， 而不是 customEditor
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new MyHandlerInterceptor());
        interceptorRegistration.addPathPatterns("/test/**");

        registry.addWebRequestInterceptor(new MyWebRequestInterceptor())
            .addPathPatterns("/test/**")
            .excludePathPatterns("/static/**");
    }
    /**
     * ·
     * 静态资源映射，底层使用的是 SimpleUrlHandlerMapping
     *
     * 是否可以 通过指定后缀的方式？ 可以！
     *
     * 注意 默认的 CLASSPATH_RESOURCE_LOCATIONS 并不包含 templates， why，  templates 其实是 thymeleaf 的默认？ 是，从ThymeleafProperties#DEFAULT_PREFIX 可知
     * 也不单单是thymeleaf， 是其他所有的viewResolver 的默认视图文件目录！
     *
     * http://192.168.1.103:8080/templates/aa.html templates里面的视图文件是不能直接访问的，直接访问就是 404！
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 如果静态资源不在这里配置 addResourceHandler， 那么默认pathPatterns是 /**，然后 resource locations是CLASSPATH_RESOURCE_LOCATIONS； 是吧。
        // 这里的配置是 add， 如果匹配到则优先处理，是覆盖， 但不是替换！
        // 虽然/** 可以匹配所有，但是 在匹配静态资源的时候，优先级比较低！
        // 只要我们把静态资源全部放到CLASSPATH_RESOURCE_LOCATIONS，其实不用这里配置也是可以的； 这里的目的估计是在于 客制化！

        registry
                // Add a resource handler for serving static resources based on the specified URL path patterns. The handler will be invoked for every incoming request that matches to one of the specified path patterns.
                //Patterns like "/static/**" or "/css/{filename:\\w+\\.css}" are allowed. See org.springframework.util.AntPathMatcher for more details on the syntax
                // 添加静态资源的访问路径
                .addResourceHandler("/js/**")

                /*
                Add one or more resource locations from which to serve static content. Each location must point to a valid directory. Multiple locations may be specified as a comma-separated list, and the locations will be checked for a given resource in the order specified.
                    For example, {"/", "classpath:/META-INF/public-web-resources/"} allows resources to be served both from the web application root and from any JAR on the classpath that contains a /META-INF/public-web-resources/ directory, with resources in the web application root taking precedence.
                    For URL-based resources (e.g. files, HTTP URLs, etc) this method supports a special prefix to indicate the charset associated with the URL so that relative paths appended to it can be encoded correctly, e.g. [charset=Windows-31J]http://example.org/path

                    classpath 就指明了 可以搜索 jar包里面的文件！ 否则就是当前项目的内容！
                 */
                .addResourceLocations("classpath:/favicon.ico")// 应该是个路径， 不能是个具体文件名！
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")// 访问 /js/a.html, 会到 /resources/目录下查找/a.html，
                // 相当于是查找/resources/a.html, 而不是 /resources/js/a.html！ 因为 pathPattern 是有个/js/ 前缀， 实际查找的时候会去删除这个前缀！

//                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/templates/")

                // 是否需要classpath？ 感觉一般是不需要的，因为，项目文件都存放在本地， 不是jar 里面， 所以..
                .addResourceLocations("classpath:/public/")

                // /jss/** 这样恐怕不行， 不需要*， 因为我们需要指定一个路径，不能是ant、正则表达式！
                .addResourceLocations("classpath:/js/**")
        ;

        // 其实可以一次性添加多个， 这样就不需要多次调用addResourceHandler；  如果文件存放很有规律的话，可以这样
        //
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/") // 注意顺序！
                .addResourceLocations("classpath:/static/") // 这个可以去掉， 因为谁会直接访问templates路径吗？ 而且模板templates的请求会放到 static 目录下？
        ;
        // 在WebSecurityConfigurerAdapter中怎么配置，怎么放行都没有用，需要这里放行，而且需要同时添加 addResourceHandler/ addResourceLocations
        registry.addResourceHandler("/assets/**")
//                .addResourceLocations("classpath:/assets/") // 如果这里注释就不能访问/assets/** 了
                .addResourceLocations("classpath:/static/")
        ;

        registry.addResourceHandler("/stat") // 最好是 /** 结尾，否则是就全匹配！ 就是只能匹配单个端点！

                /*
                    "classpath:/META-INF/resources/", "classpath:/resources/",
                        "classpath:/static/", "classpath:/public/" };
                 */
                /**
                 * Locations of static resources. Defaults to classpath:[/META-INF/resources/,
                 * /resources/, /static/, /public/].
                 */
                //    private String[] staticLocations = CLASSPATH_RESOURCE_LOCATIONS;
                .addResourceLocations(); // 没有参数就是 空地址, 相当于不执行这一行！！
                // 这里没有指定资源路径，那么也不会使用默认的staticLocations， 而是 空路径， 所以说，必须要；
        // 如果指定了ResourceLocations， 那么就不会使用默认的静态资源路径

        boolean b = registry.hasMappingForPattern("/stat");
        System.out.println("b = " + b); // 刚刚添加了， 所以这里是 true！

        // 它使用默认的静态资源路径： staticLocations； 这样之后， 访问 /free 就不会走 Controller；因为它优先级比较低。
        registry.addResourceHandler("/test");// 并不会覆盖！

        // ResourceHttpRequestHandler ["/"] 意味着什么？ 意味着从 / 去查找资源..  注意/ 不是 classpath /, 不是 classes 目录！而是

        /* DispatcherServlet的处理器映射有7个 ， 即this.handlerMappings：
        0 = {SimpleUrlHandlerMapping@6856}
        1 = {RequestMappingHandlerMapping@6857}
        2 = {SimpleUrlHandlerMapping@6858}
        3 = {BeanNameUrlHandlerMapping@6859}
        4 = {WelcomePageHandlerMapping@6860}
        5 = {SimpleUrlHandlerMapping@6861}
        6 = {SimpleUrlHandlerMapping@6862}

         */
        // 优先级倒数第二：
        // 默认有两个：
        //1  /webjars/** -> {ResourceHttpRequestHandler@6961}  -- 资源路径是：classpath:/META-INF/resources/webjars/
        //2  /** -> {ResourceHttpRequestHandler@6519} ——  资源路径是：CLASSPATH_RESOURCE_LOCATIONS；  如果置空即spring.resources.static-locations=，则是根目录： /
        // 优先级最低的是：/** DefaultServletHttpRequestHandler

        // webjars 是由WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.addResourceHandlers
        //PathResourceResolver#getResource之中ServletContext resource [/assets/bd_145.png]  明明是存在， 为什么找不到？

        registry.addResourceHandler("*.png") // 不需要/ 开头也是可以的

                .addResourceLocations("classpath:/assets/") // 这种情况是 ClassPathResource： classpath:/img/
                .addResourceLocations("/img/") // 这种情况是ServletContextResource： /img/

                // Configure a chain of resource resolvers and transformers to use. This can be useful, for example, to apply a version strategy to resource URLs.
                //If this method is not invoked, by default only a simple PathResourceResolver is used in order to match URL paths to resources under the configured locations.
                .resourceChain(false)

//        .addResolver()

//        .addTransformer()

                ;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://domain2.com")
                .allowedMethods("PUT", "DELETE")
                .allowedHeaders("header1", "header2", "header3")
                .exposedHeaders("header1", "header2")
                .allowCredentials(false).maxAge(3600);
//        原文链接：https://blog.csdn.net/MobiusStrip/article/details/84849418
//        registry.addMapping("/cors").allowedMethods("get");
    }

    /*
        底层使用的仍然是SimpleUrlHandlerMapping， 而不是BeanNameUrlHandlerMapping
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/view").setViewName("v1");// 这样是直接把 http://192.168.1.103:8080/view 和 /v1.html 映射了起来!

//        registry.addRedirectViewController()

//        registry.addStatusController();

    }

    // ViewResolver 就是能够解析 前端视图的 渲染html 的页面引擎，比如 thymeleaf
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.beanName();
//        registry.jsp().viewNames("v2");// 就是说 访问 v2, 返回 v2.jsp

        // ContentNegotiation 含义是？
//         registry.enableContentNegotiation(true);// 参数的useNotAcceptableStatus是什么意思？

         // ContentNegotiation 需要经过controller吗？ 估计是！ 因为不是静态资源！
         // ContentNegotiation

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}

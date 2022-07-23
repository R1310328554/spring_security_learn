# @CrossOrigin
有2个参数：
 origins： 允许可访问的域列表， 默认使用
 maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。

如 @CrossOrigin(origins = "http://domain2.com", maxAge = 3600)

用法：
1 在@Controller类上，表示对当前controller的所有请求进行跨域配置
2 @RequestMapping方法上，表示对当前@RequestMapping方法的请求进行跨域配置

原理： 参考： https://blog.csdn.net/weixin_33691817/article/details/92392464?
使用CrossOrigin有什么条件要求吗？ 是不是需要.. ?  Spring MVC 提供的支持！！
RequestMappingHandlerMapping 的initCorsConfiguration；如果controller在类上标了@CrossOrigin或在方法上标了@CrossOrigin注解，则spring 在记录mapper映射时会记录对应跨域请求映射，代码如下
                                                    
当一个跨域请求过来时，spring在获取handler时会判断这个请求是否是一个跨域请求，如果是，则会返回一个可以处理跨域的handler




spring注解@CrossOrigin不起作用的原因

    1、是springMVC的版本要在4.2或以上版本才支持@CrossOrigin
    2、非@CrossOrigin没有解决跨域请求问题，而是不正确的请求导致无法得到预期的响应，导致浏览器端提示跨域问题。
    3、在Controller注解上方添加@CrossOrigin注解后，仍然出现跨域问题，解决方案之一就是：
    
    在@RequestMapping注解中没有指定Get、Post方式，具体指定后，问题解决。
    原文链接：https://blog.csdn.net/MobiusStrip/article/details/84849418 


全局的怎么办？
全局CORS配置，如下3个方案：

# 方案一
spring中可以采用的跨域配置方式如下：

RequestMapping
在一般性的配置中，在controller前添加@CrossOrigin即可使用spring的默认配置，允许跨域
该注解也可以配置一些设定，适合针对个别的controller



原文链接：https://blog.csdn.net/ZYC88888/article/details/86534515

# 方案二
webconfig的方式配置全局跨域
    
   @Configuration
   public class JxWebMvcConfiguration extends WebMvcConfigurerAdapter {
    
     /**
     * Cross Origin Resource Support(CORS) for the Spring MVC.
     * automatically.
     * https://my.oschina.net/wangnian/blog/689020
     * http://spring.io/guides/gs/rest-service-cors/
     */
     /* @Override
     public void addCorsMappings(CorsRegistry registry) {
     registry.addMapping("*")
     .allowedOrigins("*").exposedHeaders("x-total-count","x-auth-token")
     .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
     }*/
   }
   
这种方式的缺陷是，filter的顺序是固定的，在引入第三方组件的时候可能会因为filter滞后，导致出错

# 方案三
定制Filter
    
    @Bean
    public FilterRegistrationBean corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.addAllowedOrigin("*");
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");
      source.registerCorsConfiguration("/**", config);
      FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
      bean.setOrder(0);
      return bean;
    }
    
方案3缺陷
在3中，使用zuul的时候，的确解决了跨域问题，但是spring security的filter还是在其前边，引起登录的时候不能正常捕获401错误
    
    @Bean
        public Filter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            config.addExposedHeader("x-auth-token");
            config.addExposedHeader("x-total-count");
            source.registerCorsConfiguration("/**", config);
            return new CorsFilter(source);
        }
     
        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {       
            httpSecurity.addFilterBefore(corsFilter(), ChannelProcessingFilter.class);
        }


# CorsFilter

我们可能会去自己写一个CorsFilter， 但是实际上，spring 框架为我们想到了。伟大的作者们已经提供了相关配套设施！
 
它就是 org.springframework.web.filter.CorsFilter！

怎么去使用它呢？
我们需要提供一个 CorsConfigurationSource 给他

Spring Security框架已经为我们提供了这个：

org.springframework.security.config.annotation.web.configurers.CorsConfigurer.getCorsFilter

怎么使用？ 
其实很简单！

但 如果我们没有使用Spring Security框架， 而单就是普通的Spring boot项目呢？

这个就需要..

作用是..

# @CrossOrigin







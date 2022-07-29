# cache manager

@EnableCaching注解是spring framework中的注解驱动的缓存管理功能。自spring版本3.1起加入了该注解。如果你使用了这个注解，那么你就不需要在XML文件中配置cache manager了。

当你在配置类(@Configuration)上使用@EnableCaching注解时，会触发一个post processor，这会扫描每一个spring bean，查看是否已经存在注解对应的缓存。如果找到了，就会自动创建一个代理拦截方法调用，使用缓存的bean执行处理。



缓存的实现有多种实现,
ConcurentHashMapCache , GuavaCache, EnCacheCache， redis 等多种实现，spring boot 有默认的实现

那么我们如何配置，如何选择呢?
    
        @Cacheable    触发缓存填充
    　　@CacheEvict    触发缓存驱逐
    　　@CachePut    更新缓存而不会干扰方法执行
    　　@Caching    重新组合要在方法上应用的多个缓存操作
    　　@CacheConfig    在类级别共享一些常见的缓存相关设置


# CacheManager
@EnableCaching 会默认的启用基于内存的CacheManager， 但是我们可以对它进行定制。比如使用基于redis的 CacheManager！
怎么做？ 只要我们定义一个 自定义的CacheManager， 默认的那个就不会创建！

代码在哪里？
 
# redis 序列化


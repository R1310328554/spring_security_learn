# Servlet 


# Filter 过滤器
过滤器，Filter是j2ee的东西，它可以在Servlet执行前后做一些事情，比如拦截、日志、权限、异常处理等等

过滤器使用filter实现，拦截的是request请求，基于回调，基于servlect规范

依赖容器，有初始化方法和销毁方法，拦截的是地址，粒度很大



# FilterChain 
这个很重要，它配合Filter一起完成了j2ee的过滤机制，具体可以看源码：
org.apache.catalina.core.ApplicationFilterChain.doFilter 
FilterChain的doFilter方法 会调用下一个filter的 doFilter方法，filter的 doFilter中如果继续调用FilterChain的doFilter，
那么就形成了一个链条，这个链条往下传递，直到FilterChain中已经没有下一个Filter，这个时候就会调用 servlet的 doService方法

FilterChain的doFilter方法是固定的，不能修改的；而我们自定义的Filter的doFilter方法，是我们控制的， 如果我们在自定义的Filter的doFilter方法中
不继续调用 filterChain.doFilter， 那么链条提前中止，真正匹配的的servlet的 doService方法得不到执行！！
—— 这个其实是一个 责任链设计模式！

因此，在我们自定义的Filter的doFilter方法中，我们可以在 调用filterChain.doFilter前后做一些事情，这样就达到了在真正的servlet的 doService做一些事情的目的！
（也包括异常处理！）
  
如果，
ApplicationFilterChain 是关键，而ApplicationFilterChain的创建要看StandardWrapperValve和ApplicationFilterFactory，即这里：
org.apache.catalina.core.StandardWrapperValve.invoke -> org.apache.catalina.core.ApplicationFilterFactory.createFilterChain
这里从StandardContext中获取了filterMaps，然后分别通过matchFiltersURL, matchFiltersServlet 的方式添加Filter到filterChain，即
filterChain.addFilter(filterConfig);
—— 这个过程是每个请求每次都会执行的过程，也就是说，每次都会去按照上面的逻辑，创建一个ApplicationFilterChain！
—— 是否有缓存呢？ 应该也是有的吧！ 测试发现，并没有，难怪 Tomcat效率比不上 Undertow！
—— 如果对一个http request找不到对应的servlet呢？ 那么FilterChain还需要创建吗？Filter还会执行吗？ 测试是不会！
—— 为什么随意访问一个路径（非静态资源文件），SpringMVC全局的ModelAttribute 仍然会执行？ 因为DispatcherServlet的访问路径是/! 是所有！
—— 但是，即使如此，如果匹配到了 filter，而filter没有执行 filterChain.doFilter，则SpringMVC的DispatcherServlet 也不会其任何作用！
—— 为何访问 http://192.168.1.103:8080/ 返回 index.html， 没有执行SpringMVC全局的ModelAttribute？ 因为..

### filter执行顺序
按滤器的文件名首字母顺序执行。？？ 应该添加的顺序，也就是出现的顺序吧。
注：@Order无法对filter进行排序，因为@Order是spring的bean执行优先级的顺序，而filter并不属于spring的bean。

通过 web.xml 配置的 Filter 过滤器，执行顺序由 <filter-mapping> 标签的配置顺序决定。<filter-mapping> 靠前，则 Filter 先执行，靠后则后执行。通过修改 <filter-mapping> 的顺序便可以修改 Filter 的执行顺序。
通过 @WebFilter 注解配置的 Filter 过滤器，无法进行排序，若需要对 Filter 过滤器进行排序，建议使用 web.xml 进行配置。

可以使用 OrderedFilter 进行排序！

# Interceptor 拦截器
是基于Java的jdk动态代实现的，实现HandlerInterceptor接口。不依赖于servlet容器，

拦截器针对于contraller方法，并且能获取到所有的类，对类里面所有的方法实现拦截，粒度更小，拦截器中可以注入service，也可以调用业务逻辑

Interceptor不是j2ee的东西，它是Struts或SpringMVC框架提供的东西，它是在框架提供的Servlet执行前做一些事情

比如SpringMVC的 DispatcherServlet, 
	@Nullable
	private List<HandlerAdapter> handlerAdapters;
	
	其中HandlerAdapter用来处理 web请求(参数包括 req、resp、Handler)，
	HandlerAdapter 就是对几个做一个适配（对于RequestMappingHandlerAdapter，handle 就是那个注释了RequestMapping的方法，即HandlerMethod），然后返回ModelAndView
		
	HandlerInterceptor ， 其实现有 HandlerInterceptorAdapter， 提供了 pre、post、after 几个方法，
	就是在真正的处理web请求前后做一些事情！ 但这个整个过程都是在DispatcherServlet内部完成的！
	—— 这就是HandlerInterceptor 和 Filter的区别！
	 
	
比如 Spring框架的 MethodInterceptor， 是用来拦截.. aop 的  


# 那些重要的过滤器
### GenericFilterBean
抽象类GenericFilterBean实现了javax.servlet.Filter、org.springframework.beans.factory.BeanNameAware、org.springframework.context.EnvironmentAware、org.springframework.web.context.ServletContextAware、org.springframework.beans.factory.InitializingBean和org.springframework.beans.factory.DisposableBean五个接口，作用如下：

    (1) Filter，实现过滤器；
    (2) BeanNameAware，实现该接口的setBeanName方法，便于Bean管理器生成Bean；
    (3) EnvironmentAware，实现该接口的setEnvironment方法，指明该Bean运行的环境；
    (4) ServletContextAware，实现该接口的setServletContextAware方法，指明上下文；
    (5) InitializingBean，实现该接口的afterPropertiesSet方法，指明设置属性生的操作；
    (6) DisposableBean，实现该接口的destroy方法，用于回收资源。
    GenericFilterBean的工作流程是：init-doFilter-destory，其中的init和destory在该类中实现，doFilter在具体实现类中实现。

## OncePerRequestFilter
OncePerRequestFilter，顾名思义，它能够确保在一次请求中只通过一次filter，而需要重复的执行。大家常识上都认为，一次请求本来就只filter一次，为什么还要由此特别限定呢，往往我们的常识和实际的实现并不真的一样，经过一番资料的查阅，此方法是为了兼容不同的web container，也就是说并不是所有的container都入我们期望的只过滤一次，servlet版本不同，执行过程也不同。
    
如，servlet2.3与servlet2.4也有一定差异, 写道

    在servlet-2.3中，Filter会过滤一切请求，包括服务器内部使用forward转发请求和<%@ include file="/index.jsp"%>的情况。
    到了servlet-2.4中Filter默认下只拦截外部提交的请求，forward和include这些内部转发都不会被过滤，但是有时候我们需要 forward的时候也用到Filter。
    
因此，为了兼容各种不同的运行环境和版本，默认filter继承OncePerRequestFilter是一个比较稳妥的选择

如何确保一次请求中只通过一次filter？ 通过给request设置一个属性，即attribute，
没有执行就不会有这个属性，执行完，下次再执行，检查肯定是已经有这个属性，就跳过，不再执行！

OncePerRequestFilter是在一次外部请求中只过滤一次。对于服务器内部之间的forward等请求，不会再次执行过滤方法。

OncePerRequestFilter是采用的模板方法模式，子类需要实现父类定义的钩子方法(算法逻辑父类已经实现)，便可以进行过滤。


### CharacterEncodingFilter
可以设置HttpServletRequest, HttpServletResponse的编码格式，如果设置了强制，可以覆盖！

### RequestContextFilter
 主要 RequestContextHolder 
 把 请求的属性，即requestAttributes 放入到线程本地变量，以便在 不同方法的同个线程内共享，即 requestAttributesHolder(如果可以继承的话， 会xx )， why 
 这个是静态类完成的： 
 
 		RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);

处理完之后，要reset；

这是在Spring2.0时添加的类，通过LocaleContextHolder和RequestContextHolder把Http request对象基于LocalThread绑定到请求提供服务的线程上。
现在一般使用DispatcherServlet这个中央分发器。现在RequestContextFilter过滤器主要用于第三方的Servlet，如JSF的FacesServlet。在Spring2.5之前都是使用该过滤器配置。配置如下：

在Spring中可以使用很多种方式来实现request的转发，将页面提交的request转发到Controller中，而实现这一功能的原理即是设置LocaleContextHolder和RequestContextHolder，这些方式包括：

https://blog.csdn.net/geloin/article/details/7444718


最近遇到的问题是在service获取request和response,正常来说在service层是没有request的,然而直接从controlller传过来的话解决方法太粗暴,后来发现了SpringMVC提供的RequestContextHolder遂去分析一番,并借此对SpringMVC的结构深入了解一下,后面会再发文章详细分析源码



### AbstractRequestLoggingFilter
    在AbstractRequestLoggingFilter中，还包含很多其他方法：
    setIncludeQueryString：查询语句是否包含在日志文件中，true或false；
    setIncludeClientInfo：客户地址和session id是否包含在日志中，true或false；
    setIncludePayload：负载信息是否包含在日志信息中，true或false；
    setMaxPayloadLength：最大负载量，int值；
    setBeforeMessagePrefix：日志信息前的信息的前缀；
    setBeforeMessageSuffix：日志信息前的信息的后缀；
    setAfterMessagePrefix：日志信息后的信息的前缀；
    setAfterMessageSuffix：日志信息后的信息的后缀。
    以上这些信息均可以在init-param中进行设置。

主要有3个子类，分别是： 

- 其中CommonsRequestLoggingFilter将上下文信息直接打印；
- ServletContextRequestLoggingFilter将上下文信息写入日志文件；
- Log4jNestedDiagnosticContextFilter将上下文信息写入NDC中

### HiddenHttpMethodFilter 默认不开启
HiddenHttpMethodFilter的父类是OncePerRequestFilter，它继承了父类的doFilterInternal方法，工作原理是
    
    将jsp页面的form表单的method属性值在doFilterInternal方法中转化为标准的Http方法，即GET,、POST、 HEAD、OPTIONS、PUT、DELETE、TRACE，然后到Controller中找到对应的方法。

例如，在使用注解时我们可能会在Controller中用于@RequestMapping(value = "list", method = RequestMethod.PUT)，
所以如果你的表单中使用的是<form ...，那么这个表单会被提交到标了Controller的Method="PUT"的方法中

需要注意的是，由于doFilterInternal方法只对method为post的表单进行过滤，所以在页面中必须如下设置：
    
       <form action="..." method="<strong>post</strong>">
               <input type="hidden" name="_method" value="<strong>put</strong>" />
               ......
       </form>
       
       而不是
       
       <form action="..." method="put">
               ......
       </form>

 同时，HiddenHttpMethodFilter必须作用于dispatcher前，所以在web.xml中配置HiddenHttpMethodFilter时，需参照如下代码：

同样的，作为Filter，可以在web.xml中配置HiddenHttpMethodFilter的参数，可配置的参数为methodParam，
如果不在web.xml里重新配置的话，default的值为 “_method”,而他在hidden里对应的value为http method的任意一个即可


### HttpPutFormContentFilter 默认不开启
在Spring MVC过滤器-HiddenHttpMethodFilter中我们提到，**jsp或者说html中的form的method值只能为post或get**，我们可以通过HiddenHttpMethodFilter获取put表单中的参数-值
而在Spring3.0中获取put表单的参数-值还有另一种方法，即使用HttpPutFormContentFilter过滤器

 HttpPutFormContentFilter 过滤器的作为就是获取put表单的值，并将之传递到Controller中标注了method为RequestMethod.put的方法中。

    <form action="" method="put" enctype="<strong>application/x-www-form-urlencoded</strong>">
    ......
    </form>

### ShallowEtagHeaderFilter
ShallowEtagHeaderFilter是spring提供的支持ETag的一个过滤器，所谓ETag是指被请求变量的实体值，是一个可以与Web资源关联的记号，而Web资源可以是一个Web页，也可以是JSON或XML文档，服务器单独负责判断记号是什么及其含义，并在HTTP响应头中将其传送到客户端，以下是服务器端返回的格式：
ETag:"50b1c1d4f775c61:df3"  
客户端的查询更新格式是这样的：
If-None-Match : W / "50b1c1d4f775c61:df3"  
如果ETag没改变，则返回状态304然后不返回，这也和Last-Modified一样。

ShallowEtagHeaderFilter 会将JSP等的内容缓存，生成MD5的key，然后在response中作为Etage的header返回给客户端。下次客户端对相同的资源（或者说相同的url）发出请求时，客户端会将之前生成的key作为If-None-Match的值发送到server端。 Filter会客户端传来的值和服务器上的做比较，如果相同，则返回304；否则，将发送新的内容到客户端。

由源码可知，ShallowEtagHeaderFilter 只能根据结果判断是否重新向客户端发送数据，并不会不处理请求，因此节省带宽，而不能提高服务器性能。

304 表示，服务端告诉浏览器，你请求的数据、页面没有发生任何变化，因此我就不发给你了，你使用之前的缓存数据吧！

### FormContentFilter
把 queryString 转换为 form 提交.. 
把请求封装为 FormContentRequestWrapper ！

大意就是：默认情况下，只有POST请求的表单数据才会被解析，而PUT、PATCH和DELETE的表单数据则不会被解析。配置了FormContentFilter 后，后三种类型的表单数据也可以被解析。

Spring 5.1 之前是HttpPutFormContentFilter，Spring 5.1 之后FormContentFilter把前者取代了。

https://liuxingchang.blog.csdn.net/article/details/116601413

# CorsFilter
setLocale


# MultipartFilter
和MultipartResolver（有两个实现： StandardServletMultipartResolver和 CommonMultipartResolver ） 一起处理文件上传！ 把上传的数据 封装为 multipartFiles， 封装为 StandardMultipartHttpServletRequest
或 MultipartHttpServletRequest
然后赋值给后面的 controller 
中 request mapping 方法的 MultipartFile 参数

其中 CommonsMultipartResolver 使用 commons Fileupload 来处理 multipart 请求，所以在使用时，必须要引入相应的 jar 包；而 StandardServletMultipartResolver 是基于 Servlet 3.0来处理 multipart 请求的，所以不需要引用其他 jar 包，但是必须使用支持 Servlet 3.0的容器才可以，以tomcat为例，从 Tomcat 7.0.x的版本开始就支持 Servlet 3.0了。


# Locale 






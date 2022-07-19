# @ModelAttribute
@ModelAttribute主要的作用是将数据添加到模型对象中，用于视图页面显示。@ModelAttribute注释的位置不同,和其他注解一起使用时有很多种用法。

## @ModelAttribute注解在方法上。@ModelAttribute注解的方法会在Controller每个方法执行前被调用。这个有点类似于Junit的@Before注解。
    当被@ModelAttribute注解的方法的返回值类型不同时也有区分
    
    
### ①当注解void方法时，一般在方法的入参中使用Model参数,在方法体内用model.addAttribute(String name,Object object);将模型数据添加到模型对象中。

    @ModelAttribute 
    public void NoneReturn(@RequestParam String data, Model model) { 
             model.addAttribute("指定一个名称",data); 
      } 

### ②当注解返回具体类型的方法时，我们一般用@ModelAttribute的value属性指定model属性的名称。model属性对应的对象就是方法的返回值。

     @ModelAttribute("指定一个名称") 
     @ModelAttribute//如果这样使用，不指定名称，则model属性名就会默认是返回类型的首字母小写
     public String ReturnRealClass(@RequestParam String data) { 
             return data;     
      } 
      
### ③当@ModdelAttribute和@RequestMapping共同注解一个方法时。
    
    @RequestMapping(value = "指定一个访问路径") 
    @ModelAttribute("指定一个名称") 
    public String Fix() { 
             return "猛虎蔷薇"; 
      } 
  
  这时就比较特殊了，此时方法的返回值并不是表示一个视图名称，而是model属性的值，此时的视图名称就是@RequestMapping中指定的访问路径的最后一层去掉扩展名。
  这样说可能比较抽象，举个例子：
  	假设@RequestMapping("/MainPage.txt"),此时的视图名称就会被解析为MainPage，当然了具体处理的时候前后还会加上配置的suffix和prefix.
 
## @ModelAttribute注解在方法的参数上。

### ①从model中获取

        @RequestMapping(value = "指定访问路径") 
         public String AtAttribute(@ModelAttribute("user") User user) { 
            //SpringMVC首先会在 implicitModel（SpringMVC运行流程中的一个组件）中查找 key 对应的对象。
                若存在, 则作为入参传入。
                若不存在 key 对应的对象, 则检查当前的 Handler 是否使用 @SessionAttributes注解修饰, 
                    若使用了该注解, 且 @SessionAttributes 注解的 value 属性值中包含了 key, 则会从HttpSession 中来获取 key 所对应的 value 值,
                        若存在则直接传入到目标方法的入参中
                        若不存在则将抛出异常. 所以说使用@SessionAttributes注解一定要谨慎。在上一篇文章末尾，也提到了这点。
                    若 Handler 没有标识 @SessionAttributes 注解或 @SessionAttributes 注解的 value 值中不包含 key, 则会通过反射来创建 POJO 类型的参数, 传入为目标方法的参数
                SpringMVC 会把 key 和 POJO 类型的对象保存到 implicitModel 中, 进而会保存到 request 中. （跟SpringMVC的运行流程有关）
                        
                  return "轻嗅"; 
           }
    
### ②从Form表单或URL参数中获取（实际上，不加注解也可以拿到user对象）

        @RequestMapping(value = "指定一个路径")
        public String AtAttribute2(@ModelAttribute User user) {
                  return "水击";
          }
          
它的作用是将该绑定的命令对象以“user”为名称（类名首字母小写）添加到模型对象中供视图页面显示使用。
     
## @ModelAttribute注解方法的返回值上，添加方法返回值到模型对象中，用于视图页面展示时使用。

    @RequestMapping(value = "指定路径") 
    public @ModelAttribute("user") User helloWorld(User user) { 
             return new User(); 
     } 
     
此时会添加返回值到模型数据中供视图展示使用。 
————————————————
版权声明：本文为CSDN博主「指鹿为�ớ ₃ờ」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/yue_xx/article/details/105740360







@ModelAttribute最主要的作用是将数据添加到模型对象中，用于视图页面展示时使用。

被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，因此对于一个controller映射多个URL的用法来说，要谨慎使用。

@ModelAttribute等价于 model.addAttribute("attributeName", abc); 但是根据@ModelAttribute注释的位置不同，和其他注解组合使用，致使含义有所不同

     1 @Controller 
     2 public class HelloWorldController { 
     3     @ModelAttribute 
     4     public void populateModel(@RequestParam String abc, Model model) { 
     5          model.addAttribute("attributeName", abc); 
     6       } 
     7 
     8     @RequestMapping(value = "/helloWorld") 
     9     public String helloWorld() { 
    10        return "helloWorld"; 
    11         } 
    12  }
这个例子，在获得请求/helloWorld 后，populateModel方法在helloWorld方法之前先被调用，它把请求参数（/helloWorld?abc=text）加入到一个名为attributeName的model属性中，在它执行后 helloWorld被调用，返回视图名helloWorld和model已由@ModelAttribute方法生产好了。

    
    
    1 @ModelAttribute 
    2 public Account addAccount(@RequestParam String number) { 
    3     return accountManager.findAccount(number); 
    4 }
这种情况，model属性的名称没有指定，它由返回类型隐含表示，如这个方法返回Account类型，那么这个model属性的名称是account。
　　这个例子中model属性名称有返回对象类型隐含表示，model属性对象就是方法的返回值。它无须要特定的参数。




# @SessionAttributes原理
　　默认情况下Spring MVC将模型中的数据存储到request域中。当一个请求结束后，数据就失效了。如果要跨页面使用。那么需要使用到session。而@SessionAttributes注解就可以使得模型中的数据存储一份到session域中。

 @sessionattributes注解应用到Controller上面，可以将Model中的属性同步到session当中。


 
 @SessionAttributes参数

　　1、names：这是一个字符串数组。里面应写需要存储到session中数据的名称。

　　2、types：根据指定参数的类型，将模型中对应类型的参数存储到session中

 　 3、value：其实和names是一样的。

具体代码
Java代码
    
    1 @RequestMapping("/test")
    2 public String test(Map<String,Object> map){
    3     map.put("names", Arrays.asList("caoyc","zhh","cjx"));
    4     map.put("age", 18);
    5     return "hello";
    6 }

    
    @Controller
    @RequestMapping("/Demo.do")
    @SessionAttributes(value={"attr1","attr2"})
    public class Demo {
        
        @RequestMapping(params="method=index")
        public ModelAndView index() {
            ModelAndView mav = new ModelAndView("index.jsp");
            mav.addObject("attr1", "attr1Value");
            mav.addObject("attr2", "attr2Value");
            return mav;
        }
        
        @RequestMapping(params="method=index2")
        public ModelAndView index2(@ModelAttribute("attr1")String attr1, @ModelAttribute("attr2")String attr2) {
            ModelAndView mav = new ModelAndView("success.jsp");
            return mav;
        }}


当需要清除session当中的值得时候，我们只需要在controller的方法中传入一个SessionStatus的类型对象 通过调用setComplete方法就可以清除了。
    
    @RequestMapping(params="method=index3")
    　　public ModelAndView index4(SessionStatus status) {
    　　ModelAndView mav = new ModelAndView("success.jsp");
    　　status.setComplete();
    　　return mav;
    }



# @ControllerAdvice
首先，ControllerAdvice本质上是一个Component，因此也会被当成组建扫描，一视同仁，扫扫扫。

然后，我们来看一下此类的注释：

这个类是为那些声明了（@ExceptionHandler、@InitBinder 或 @ModelAttribute注解修饰的）方法的类而提供的专业化的@Component , 以供多个 Controller类所共享。

- @ControllerAdvice 配合 @ExceptionHandler 实现全局异常处理
- @ControllerAdvice 配合 @ModelAttribute 预设全局数据
- @ControllerAdvice 配合 @InitBinder 实现对请求参数的预处理


说白了，就是aop思想的一种实现，你告诉我需要拦截规则，我帮你把他们拦下来，具体你想做更细致的拦截筛选和拦截之后的处理，你自己通过@ExceptionHandler、@InitBinder 或 @ModelAttribute这三个注解以及被其注解的方法来自定义。

初定义拦截规则：
    
    ControllerAdvice 提供了多种指定Advice规则的定义方式，默认什么都不写，则是Advice所有Controller，当然你也可以通过下列的方式指定规则
    比如对于 String[] value() default {} , 写成@ControllerAdvice("org.my.pkg") 或者 @ControllerAdvice(basePackages="org.my.pkg"), 则匹配org.my.pkg包及其子包下的所有Controller，当然也可以用数组的形式指定，如：@ControllerAdvice(basePackages={"org.my.pkg", "org.my.other.pkg"}), 也可以通过指定注解来匹配，比如我自定了一个 @CustomAnnotation 注解，我想匹配所有被这个注解修饰的 Controller, 可以这么写：@ControllerAdvice（annotations={CustomAnnotation.class})
    
    还有很多用法，这里就不全部罗列了。


# @ExceptionHandler

1. 注解的参数  
@ExceptionHandler注解中可以添加参数，参数是某个异常类的class，代表这个方法专门处理该类异常
可以没有参数， 那么表示可以处理所有的异常

2. 方法的参数  
方法的参数， 通常就是Throwable、Exception、Error 或他们的子类
方法是否可以有多个参数？ 可以，比如 @ExceptionHandler的方法入参支持：Exception ；SessionAttribute 、 RequestAttribute注解 ； HttpServletRequest  、HttpServletResponse、HttpSession.
 但是 其他的不起作用

3. 就近原则  
当异常发生时，Spring会选择最接近抛出异常的处理方法。
比如之前提到的NumberFormatException，这个异常有父类RuntimeException，RuntimeException还有父类Exception，如果我们分别定义异常处理方法，@ExceptionHandler分别使用这三个异常作为参数
那么，当代码抛出NumberFormatException时，调用的方法将是注解参数NumberFormatException.class的方法，也就是handleExeption()，而当代码抛出IndexOutOfBoundsException时，调用的方法将是注解参数RuntimeException的方法，也就是handleExeption3()。

4. 注解方法的返回值  
标识了@ExceptionHandler注解的方法，返回值类型和标识了@RequestMapping的方法是统一的，可参见@RequestMapping的说明，比如默认返回Spring的ModelAndView对象，也可以返回String，这时的String是ModelAndView的路径，而不是字符串本身。
有些情况下我们会给标识了@RequestMapping的方法添加@ResponseBody，比如使用Ajax的场景，直接返回字符串，异常处理类也可以如此操作，添加@ResponseBody注解后，可以直接返回字符串， 这样的操作可以在执行完方法后直接返回字符串本身。

常见的ModelAndView，@ResponseBody注解标注，ResponseEntity等类型都OK.

                                                                                                             
5. 错误的操作  
使用@ExceptionHandler时尽量不要使用相同的注解参数。两个方法都处理NumberFormatException，这种定义方式编译可以通过，而当NumberFormatException真正被抛出时，Spring会给我们报错：
Ambiguous @ExceptionHandler method mapped for;


# @RequestAttribute @SessionAttribute

                  

@ResponseStatus



# 容器中初始化阶段：

先创建容器的工厂，再创建bean
先 BeanFactoryPostProcessor ，再 BeanPostProcessor
先创建，再实例化，再init
先 @Import ，再 @Configuration， 再创建、实例化Bean

 


# 容器中bean的实例化阶段：
  
  
  扩展点  
各种的Aware接口，比如 BeanFactoryAware，对于实现了这些Aware接口的bean，在实例化bean时Spring会帮我们注入对应的BeanFactory的实例。
BeanPostProcessor接口，实现了BeanPostProcessor接口的bean，在实例化bean时Spring会帮我们调用接口中的方法。
InitializingBean接口，实现了InitializingBean接口的bean，在实例化bean时Spring会帮我们调用接口中的方法。
DisposableBean接口，实现了BeanPostProcessor接口的bean，在该bean死亡时Spring会帮我们调用接口中的方法。
————————————————
版权声明：本文为CSDN博主「fei1234456」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/fei1234456/article/details/106693892



# Converter vs Formatter
两者其实非常的类似。

Converter 主要是两个方法：
    
    @FunctionalInterface
    public interface Converter<S, T> {
    
        /**
         * Convert the source object of type {@code S} to target type {@code T}.
         * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
         * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
         * @throws IllegalArgumentException if the source cannot be converted to the desired target type
         */
        @Nullable
        T convert(S source);
    
    }
    
Formatter 主要是两个方法：

    public interface Formatter<T> extends Printer<T>, Parser<T> {
    }

    分别有下面的接口方法：
    public T parse(String s, java.util.Locale locale)
    public String print(T object, java.util.Locale locale)

DateFormatter

顺序是怎么样的？

Converter 如何注册？ ConversionServiceFactoryBean，底层是ConversionService

Formatter 如何注册？ FormattingConversionServiceFactoryBean，底层是FormattingConversionService

如果两者都可以起作用， 顺序是怎么样的

Converter 是不同类型的转换，从输入S到输出T
Formatter 是String和某个类型的相互转换，parse就是输入字符串转换输出T类型；print就是反之

# json 格式
jackson 的注解， 

如何


# @EnableWebMvc
@EnableWebMvc没有任何属性，只是@Import了一个类叫 DelegatingWebMvcConfiguration 

2.注释中说明：将@EnableWebMvc添加给@Configuration类来导入SpringMvc的配置；

3.自定义MVC配置，实现接口WebMvcConfigurer或更可能继承 WebMvcConfigurerAdapter,并且使用@EnableWebMvc;

4.如果还想要自定义配置，移除@EnableWebMvc,并且继承WebMvcConfigurationSupport或DelegatingWebMvcConfiguration。

5.@EnableWebMvc出现自Spring3.1的版本


DelegatingWebMvcConfiguration很明显，只是一个代理； 它主要就是注入了一个List<WebMvcConfigurer>，然后放入到WebMvcConfigurerComposite

当然，直接继承 WebMvcConfigurationSupport 也是可以的！
 

# WebMvcAutoConfiguration
在 spring mvc的时代，我们是需要 @EnableWebMvc， 但是后面又出现了spring boot，这个时候就有了更先进的更完备的做法；
WebMvcAutoConfiguration 中的 WebMvcAutoConfigurationAdapter 就完成了包括@EnableWebMvc 的各种功能

而 WebMvcAutoConfiguration 又是自动的，所以在spring boot中 @EnableWebMvc 就不需要了！ 如果我们需要定制化，我们实现 WebMvcConfigurer即可！


所以有以下几种使用方式：

- @EnableWebMvc+extends WebMvcConfigurationAdapter，在扩展的类中重写父类的方法即可，这种方式会屏蔽springboot的@EnableAutoConfiguration中的设置
- extends WebMvcConfigurationSupport，在扩展的类中重写父类的方法即可，这种方式会屏蔽springboot的@EnableAutoConfiguration中的设置
- extends WebMvcConfigurationAdapter，在扩展的类中重写父类的方法即可，这种方式依旧使用springboot的@EnableAutoConfiguration中的设置
- extends WebMvcConfigurer，在扩展的类中重写父类的方法即可，这种方式依旧使用springboot的@EnableAutoConfiguration中的设置


- 使用 @EnableWebMvc 注解，需要以编程的方式指定视图文件相关配置；
- 使用 @EnableAutoConfiguration 注解，会读取 application.properties 或 application.yml 文件中的配置。

spring boot中的@SpringBootApplication 就做了相当多的事情！

哪些？



# WebMvcConfigurationSupport 

This is the main class providing the configuration behind the MVC Java config. It is typically imported by adding @EnableWebMvc to an application @Configuration class. An alternative more advanced option is to extend directly from this class and override methods as necessary, remembering to add @Configuration to the subclass and @Bean to overridden @Bean methods. For more details see the javadoc of @EnableWebMvc.

This class registers the following HandlerMappings:
    
    RequestMappingHandlerMapping ordered at 0 for mapping requests to annotated controller methods.
    HandlerMapping ordered at 1 to map URL paths directly to view names.
    BeanNameUrlHandlerMapping ordered at 2 to map URL paths to controller bean names.
    HandlerMapping ordered at Integer.MAX_VALUE-1 to serve static resource requests.
    HandlerMapping ordered at Integer.MAX_VALUE to forward requests to the default servlet.

从上可见Controller的RequestMapping 方法优先级比view names映射高！BeanNameUrlHandlerMapping 优先级更低， 静态资源更低，default servlet最低！

Registers these HandlerAdapters:
    
    RequestMappingHandlerAdapter for processing requests with annotated controller methods.
    HttpRequestHandlerAdapter for processing requests with HttpRequestHandlers.
    SimpleControllerHandlerAdapter for processing requests with interface-based Controllers.
    Registers a HandlerExceptionResolverComposite with this chain of exception resolvers:
    ExceptionHandlerExceptionResolver for handling exceptions through org.springframework.web.bind.annotation.ExceptionHandler methods.
    ResponseStatusExceptionResolver for exceptions annotated with org.springframework.web.bind.annotation.ResponseStatus.
    DefaultHandlerExceptionResolver for resolving known Spring exception types

Registers an AntPathMatcher and a UrlPathHelper to be used by:
    
    the RequestMappingHandlerMapping,
    the HandlerMapping for ViewControllers
    and the HandlerMapping for serving resources
    
Note that those beans can be configured with a PathMatchConfigurer.

Both the RequestMappingHandlerAdapter and the ExceptionHandlerExceptionResolver are configured with default instances of the following by default:
    
    a ContentNegotiationManager
    a DefaultFormattingConversionService
    an org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean if a JSR-303 implementation is available on the classpath
    a range of HttpMessageConverters depending on the third-party libraries available on the classpath.


其基本逻辑是
通过@Bean 创建一个mvc相关的实例，比如设名为Xyz，然后提供 configureXyz 的方法定制化，或提供AddXyz 的方法添加更多的定制化的处理器。

对于RequestMappingHandlerMapping ，其中的mappingRegistry保存了所有的 controller方法！

# viewControllerHandlerMapping
直接处理url，返回前端视图； 不进行任何其他逻辑处理。 当然 interceptor 之类的还是需要执行的，是少不了的！

ViewControllerRegistry 是注册什么？ 

BeanNameUrlHandlerMapping 直接把 Controller bean name 和 url 映射起来。

resourceHandlerMapping 又是什么？ 资源处理器映射？ 是不是和 viewControllerHandlerMapping 一样，都是处理静态资源的？ 

—— 不要猜测， 如果英语说明看起来吃力难懂，那么测试，验证一下就知道！

DefaultServletHandlerConfigurer 是？


#  静态资源
通过获取 服务器默认的servlet进行转发，因为/** 会匹配所有的URI，所以SimpleUrlHandlerMapping必须放在RequestMappingHandlerMapping之后；核心方式就是servletContext.getNamedDispatcher(“default”).forward(request,response);


 
# HandlerMethod
mvc中Handler 可以认为是@Controller ， HandlerMethod 可以认为是@Controller中的@RequestMapping 
 

# DispatcherServlet
DispatcherServlet 无疑是重中之重。

其中 mappedHandler = getHandler(processedRequest); 是获取处理器！

在Spring mvc中，其实Tomcat也是这样，所以访问都需要经过 servlet！ 在Spring mvc中则就是DispatcherServlet！


# InternalResourceViewResolver 
提供了访问 内部的能力： 前缀是 /WEB-INF/， 后缀默认是 jsp；
a  变成： /WEB-INF/a.jsp
默认是 jstlView， 当然可以设置其他的 view 

跟FreeMarkerViewResolver类似，可以解析 .ftl 文件；

UrlBasedViewResolver 通用的处理逻辑：
重定向： 视图名字需要前缀 redirect:
forward： 视图名字需要前缀 forward:

# AsyncSupportConfigurer


# PathResourceResolver

org.springframework.web.servlet.resource.ResourceHttpRequestHandler.handleRequest


# 视图
不能直接访问 http://192.168.1.103:8080/result.html 否则就被当成了静态资源，如果静态位置找不到result.html 那么就被重定向到 /error !
http://192.168.1.103:8080/templates/aa.html 也不行
那么，应该如何访问 模板资源呢？ 是不是只能经过 controller？ 估计是的！ controller 的@RequestMapping方法，可以返回任何值；如果是返回的视图(或者是能够解析成视图的 字符串)，那么就会调用对应的 viewResolver 进行解析
比如 thymeleaf 就会调用 ThymeleafViewResolver 进行解析！

ThymeleafViewResolver 的优先级是 优于 静态资源的！

如果 @RequestMapping方法返回了一个视图， ThymeleafViewResolver 解析却找不到对应的视图文件， 是不是会 变成静态资源？  测试并不会如此！ 找不到就报错： 
TemplateInputException: Error resolving template [fileUpload], template might not exist or might not be accessible by any of the configured Template Resolvers


为什么？ 是否可以直接 访问 模板视图资源？ 恐怕不行... 因为 它不是静态的， 一般情况下， 它都是可能存在视图参数的，需要通过 controller进行填充！！


# 首页
WelcomePageHandlerMapping    : Adding welcome page template: index

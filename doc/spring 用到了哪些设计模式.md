
## 观察者-监听模式
AuthorizationServerConfigurerAdapter

WebSecurityConfiguration#springSecurityFilterChain

一个小小知识点，小小问题， 搞半天， 甚至几天， 都还搞不定，或不能完全搞定，理解， 非常的考验人的耐心、智力...

事件、监听者

面试、谈话的时候，不要 挠头搔耳，于事无补，其实基本就是 浪费时间； 不会导致你思考难题，不要装模作样。

## 

1. 简单工厂(非23种设计模式中的一种)    


    实现方式：
    BeanFactory。Spring中的BeanFactory就是简单工厂模式的体现，根据传入beanName来获得Bean对象，先从缓存中取，缓存中没有再创建。
    
    实质：
    由一个工厂类根据传入的参数，动态决定应该创建哪一个产品类。
    实现原理：
    
    bean容器的启动阶段：
    
    读取bean的xml配置文件，将bean元素分别转换成一个个的BeanDefinition对象。
    然后通过BeanDefinitionRegistry将这些bean注册到beanFactory中，保存在它的一个ConcurrentHashMap中。
    将BeanDefinition注册到了beanFactory之后，在这里Spring为我们提供了一个扩展的接口，允许我们通过实现接口BeanFactoryPostProcessor 在此处来插入我们定义的代码。典型的例子就是：PropertyPlaceholderConfigurer，我们一般在配置数据库的dataSource时使用到的占位符的值，就是它注入进去的。
    容器中bean的实例化阶段：
    
    实例化阶段主要是通过反射或者CGLIB对bean进行实例化，在这个阶段Spring又给我们暴露了很多的扩展点：
    
    各种的Aware接口，比如 BeanFactoryAware，对于实现了这些Aware接口的bean，在实例化bean时Spring会帮我们注入对应的BeanFactory的实例。
    BeanPostProcessor接口，实现了BeanPostProcessor接口的bean，在实例化bean时Spring会帮我们调用接口中的方法。
    InitializingBean接口，实现了InitializingBean接口的bean，在实例化bean时Spring会帮我们调用接口中的方法。
    DisposableBean接口，实现了BeanPostProcessor接口的bean，在该bean死亡时Spring会帮我们调用接口中的方法。
    设计意义：
    
    松耦合。可以将原来硬编码的依赖，通过Spring这个beanFactory这个工厂来注入依赖，也就是说原来只有依赖方和被依赖方，现在我们引入了第三方——spring这个beanFactory，由它来解决bean之间的依赖问题，达到了松耦合的效果。
    bean的额外处理。通过Spring接口的暴露，在实例化bean的阶段我们可以进行一些额外的处理，这些额外的处理只需要让bean实现对应的接口即可，那么spring就会在bean的生命周期调用我们实现的接口来处理该bean。

2. 工厂方法  


    实现方式：
    
    FactoryBean接口。
    
    实现原理：
    
    实现了FactoryBean接口的bean是一类叫做factory的bean。其特点是spring会在使用getBean()调用获得该bean时，会自动调用该bean的getObject()方法，所以返回的不是factory这个bean，而是这个bean.getOjbect()方法的返回值。
    
    例子：
    
    典型的例子有spring与mybatis的结合。


3. 单例模式  


    Spring中依赖注入的Bean实例默认是单例的。
    
    Spring的依赖注入（包括lazy-init方式）都是发生在AbstractBeanFactory的getBean里。getBean的doGetBean方法调用getSingleton进行bean的创建。
    
    总结：
    
    单例模式定义：保证一个类仅有一个实例，并提供一个访问它的全局访问点。
    
    spring对单例的实现：spring中的单例模式完成了后半句话，即提供了全局的访问点BeanFactory。但没有从构造器级别去控制单例，这是因为spring管理的是任意的java对象。


4. 适配器模式  

    
    实现方式：
    
    SpringMVC中的适配器HandlerAdatper。
    
    实现原理：
    
    HandlerAdatper根据Handler规则执行不同的Handler。
    
    实现过程：
    
    DispatcherServlet根据HandlerMapping返回的handler，向HandlerAdatper发起请求，处理Handler。
    
    HandlerAdapter根据规则找到对应的Handler并让其执行，执行完毕后Handler会向HandlerAdapter返回一个ModelAndView，最后由HandlerAdapter向DispatchServelet返回一个ModelAndView。
    
    实现意义：
    
    HandlerAdatper使得Handler的扩展变得容易，只需要增加一个新的Handler和一个对应的HandlerAdapter即可。
    
    因此Spring定义了一个适配接口，使得每一种Controller有一种对应的适配器实现类，让适配器代替controller执行相应的方法。这样在扩展Controller时，只需要增加一个适配器类就完成了SpringMVC的扩展了。

5. 装饰器模式
    
    
    实现方式：
    
    Spring中用到的包装器模式在类名上有两种表现：一种是类名中含有Wrapper，另一种是类名中含有Decorator。
    
    实质：
    
    动态地给一个对象添加一些额外的职责。就增加功能来说，Decorator模式比生成子类更为灵活。

6. 代理模式

    
    实现方式：
    
    AOP底层，就是动态代理模式的实现。
    
    动态代理：
    
    在内存中构建的，不需要手动编写代理类。
    
    静态代理：
    
    需要手工编写代理类，代理类引用被代理对象。
    
    实现原理：
    
    切面在应用运行的时刻被织入。一般情况下，在织入切面时，AOP容器会为目标对象动态的创建一个代理对象。SpringAOP就是以这种方式织入切面的。
    
    织入：把切面应用到目标对象并创建新的代理对象的过程。


7. 观察者模式

    
    实现方式：
    
    spring的事件驱动模型使用的是 观察者模式 ，Spring中Observer模式常用的地方是listener的实现。
    
    具体实现：
    
    事件机制的实现需要三个部分：事件源、事件、事件监听器。
    
    ApplicationEvent抽象类(事件)
    
    继承自jdk的EventObject，所有的事件都需要继承ApplicationEvent，并且通过构造器参数source得到事件源。该类的实现类ApplicationContextEvent表示ApplicaitonContext的容器事件。
————————————————
版权声明：本文为CSDN博主「fei1234456」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/fei1234456/article/details/106693892



+++++++++++++

---

1）工厂设计模式（简单工厂和工厂方法）
Spring使用工厂模式可以通过BeanFactory或ApplicationContext创建bean对象。

两者对比：

 BeanFactory ：延迟注入(使用到某个 bean 的时候才会注入),相比于BeanFactory来说会占用更少的内存，程序启动速度更快。
 ApplicationContext ：容器启动的时候，不管你用没用到，一次性创建所有 bean 。BeanFactory 仅提供了最基本的依赖注入支持，ApplicationContext 扩展了 BeanFactory ,除了有BeanFactory的功能还有额外更多功能，所以一般开发人员使用ApplicationContext会更多。


2)单例设计模式
Spring中bean的默认作用域就是singleton。除了singleton作用域，Spring bean还有下面几种作用域：

 prototype : 每次请求都会创建一个新的 bean 实例。
 request : 每一次HTTP请求都会产生一个新的bean，该bean仅在当前HTTP request内有效。
 session : 每一次HTTP请求都会产生一个新的 bean，该bean仅在当前 HTTP session 内有效。
 global-session： 全局session作用域，仅仅在基于portlet的web应用中才有意义，Spring5已经没有了。Portlet是能够生成语义代码(例如：HTML)片段的小型Java Web插件。它们基于portlet容器，可以像servlet一样处理HTTP请求。但是，与 servlet 不同，每个 portlet 都有不同的会话。



3）代理设计模式
Spring AOP就是基于动态代理的，如果要代理的对象，实现了某个接口，那么Spring AOP会使用JDK Proxy，去创建代理对象，而对于没有实现接口的对象，就无法使用JDK Proxy去进行代理了，这时候Spring AOP会使用Cglib，这时候Spring AOP会使用Cglib生成一个被代理对象的子类来作为代理。如下图所示：

当然你也可以使用AspectJ，Spring AOP已经继承了AspectJ,AspectJ应该算的上是java生态系统中最完整的AOP框架了。

Spring AOP和AspectJ AOP有什么区别？
Spring AOP属于运行时增强，而AspectJ是编译时增强。Spring AOP基于代理，而AspectJ基于字节码操作。
Spring AOP已经集成了AspectJ，AsectJ应该算的上是Java生态系统中最完整的AOP框架了。AspectJ相比于Spring AOP功能更加强大，但是Spring AOP相对来说更简单，如果我们的切面比较少，那么两者的性能差异不大。但是当切面太多的话，最好选择AspectJ，它比Spring AOP快很多。


4）模板方法设计模式
模板方法模式是一种行为设计模式，它定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。模板方法使得子类可以在不改变一个算法的结构即可重定义该算法的默写特定步骤的实现方式

Spring中jdbcTemplate、hibernateTemplate等以Template结尾的对数据库操作的类，它们就使用到模板模式。一般情况下，我们都是使用继承的方式来实现模板模式，但是Spring并没有使用这种方式，而是使用Callback模式与模板方法配合，既达到了代码复用的效果，同时增加了灵活性。



5）观察者设计模式
观察者设计模式是一种对象行为模式。它表示的是一种对象与对象之间具有依赖关系，当一个对象发生改变时，这个对象锁依赖的对象也会做出反应。Spring事件驱动模型就是观察者模式很经典的应用。

事件角色：ApplicationEvent（org.springframework.context包下）充当事件的角色，这是一个抽象类。

事件监听者角色：ApplicationListener充当了事件监听者的角色，它是一个接口，里面只定义了一个onApplicationEvent（）方法来处理ApplicationEvent。

事件发布者角色：ApplicationEventPublisher充当了事件的发布者，它也是个接口。

Spring事件流程总结：

 定义一个事件: 实现一个继承自 ApplicationEvent，并且写相应的构造函数；
 定义一个事件监听者：实现 ApplicationListener 接口，重写 onApplicationEvent() 方法；
 使用事件发布者发布消息: 可以通过 ApplicationEventPublisher 的 publishEvent() 方法发布消息。


6）适配器设计模式
适配器设计模式将一个接口转换成客户希望的另一个接口，适配器模式使得接口不兼容的那些类可以一起工作，其别名为包装器。在Spring MVC中，DispatcherServlet根据请求信息调用HandlerMapping，解析请求对应的Handler，解析到对应的Handler（也就是我们常说的Controller控制器）后，开始由HandlerAdapter适配器处理。为什么要在Spring MVC中使用适配器模式？Spring MVC中的Controller种类众多不同类型的Controller通过不同的方法来对请求进行处理，有利于代码的维护拓展。

7）装饰者设计模式
装饰者设计模式可以动态地给对象增加些额外的属性或行为。相比于使用继承，装饰者模式更加灵活。Spring 中配置DataSource的时候，DataSource可能是不同的数据库和数据源。我们能否根据客户的需求在少修改原有类的代码下切换不同的数据源？这个时候据需要用到装饰者模式。

8）策略设计模式
Spring 框架的资源访问接口就是基于策略设计模式实现的。该接口提供了更强的资源访问能力，Spring框架本身大量使用了Resource接口来访问底层资源。Resource接口本身没有提供访问任何底层资源的实现逻辑，针对不同的额底层资源，Spring将会提供不同的Resource实现类，不同的实现类负责不同的资源访问类型。

Spring 为 Resource 接口提供了如下实现类： 
UrlResource：访问网络资源的实现类。
ClassPathResource：访问类加载路径里资源的实现类。
FileSystemResource：访问文件系统里资源的实现类。
ServletContextResource：访问相对于 ServletContext 路径里的资源的实现类.
InputStreamResource：访问输入流资源的实现类。
ByteArrayResource：访问字节数组资源的实现类。 
这些 Resource 实现类，针对不同的的底层资源，提供了相应的资源访问逻辑，并提供便捷的包装，以利于客户端程序的资源访问。
————————————————
版权声明：本文为CSDN博主「chao09_01」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/chao821/article/details/92400186




--- 
简单说，

# 单例模式 
**典型的有： 
Object BeanFactory.getBean(java.lang.String) 即 Object getBean(String beanName)**
通过这种方式获取单例，需要1 获取BeanFactory， 2 知道单例名称即beanName —— 其实里面是调用 getSingleton

单例模式 意味着整个jvm 范围有且只有一个实例； 当然，如果是饿汉模式，是最多只有一个实例。
比如，一些容器，通常就是全局只有一个，比如Tomcat容器，其体现是 ApplicationContext

再比如Spring 容器。 单例模式的实例，通常有一个静态的方法，可以直接调用获取实例，这也给我们的使用带来了方便！

SecurityContextHolder.getContext().getAuthentication()
SecurityContextHolder的方法基本上全部是静态方法，其setContext方法是在 AbstractSecurityInterceptor#beforeInvocation 被调用的

# 工厂方法
FactoryBean
SqlSessionFactoryBean 返回的不是 SqlSessionFactoryBean 的实例，而是它的 SqlSessionFactoryBean.getObject() 的返回值。


# 门面模式：
org.apache.catalina.core.ApplicationContextFacade
org.apache.catalina.connector.RequestFacade
com.alibaba.druid.stat.DruidStatManagerFacade


# 观察者模式 
ApplicationEvent ApplicationListener ApplicationEventPublisher
rxJava 框架、 reactive 

CallBack
SessionCallBack
SuccessCallBack
TransactionCallBack
RedisCallBack
RequestCallback

StatementCallback  用于jdbc回调！

# 适配器模式 Adapter
WebMvcConfigurerAdapter
WebSecurityConfigurerAdapter
WebRequestHandlerInterceptorAdapter
ServletWebArgumentResolverAdapter
AuthorizationServerConfigurerAdapter
ResourceServerConfigurerAdapter
SecurityConfigurerAdapter
MethodBeforeAdviceAdapter

HandlerInterceptor 一定要掌握！
MethodInterceptor
HandlerAdapter mvc处理@RequestMapping 的重要！
RequestMappingHandlerAdapter spring mvc 的重要类
RequestBodyAdviceAdapter 
HttpRequestHandlerAdapter  spring mvc 的重要类

# 装饰器 Decorator
org.springframework.http.server.reactive.ServerHttpRequestDecorator

HttpRequestWrapper
org.springframework.cglib.core.MethodWrapper
com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper


# 策略设计模式 Strategy
策略模式是一种行为的； 就是一个动作（通常体现为接口方法），有多个子类实现—— 返回值不重要，重要的是不同方法做不同事，实现不同策略
 比如电商的下单购买这个行为, 可能有国内、海外、特殊订单， 其支付方式是很大不同的。 
 
 那么看起来跟模板方法模式非常的类似， 是不是呢？ 其实确实类似，但模板方法是至少有个总的模板方法； 策略模式是没有的！
 
 策略模式， 更多用于 算法， 一个操作有不同的实现算法，就使用不同的策略！
 
ContentNegotiationStrategy
org.springframework.security.web.server.DefaultServerRedirectStrategy
org.springframework.data.jdbc.core.DefaultDataAccessStrategy
SimpleInstantiationStrategy


Resource 
org.springframework.core.io.Resource
—— 不仅仅是 接口、实现类， 而是用到了 很多设计模式！


# 代理模式 Proxy
com.alibaba.druid.proxy.jdbc.DataSourceProxy
AopProxy
SpringProxy
JdkDynamicAopProxy
CglibAopProxy
Delegate


# 命令模式 Command
spring-data-redis 用到大量 Command
RedisCommand


# 模板方法模式 Template

经典模板方法定义：

父类定义了骨架（调用哪些方法及顺序），某些特定方法由子类实现。

最大的好处：代码复用。除了子类要实现的特定方法，其他方法及方法调用顺序都在父类中预先写好了。

所以父类模板方法中有两类方法：

共同的方法：所有子类都会用到的代码
不同的方法：子类要覆盖的方法，分为两种：
抽象方法：父类中的是抽象方法，子类必须覆盖
钩子方法：父类中是一个空方法，子类继承了默认也是空的
注：为什么叫钩子，子类可以通过这个钩子（方法），控制父类，因为这个钩子实际是父类的方法（空方法）！
Spring模板方法模式实质：

是模板方法模式和回调模式的结合，是Template Method不需要继承的另一种实现方式。Spring几乎所有的外接扩展都采用这种模式。
————————————————
版权声明：本文为CSDN博主「fei1234456」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/fei1234456/article/details/106693892

redis Template
jdbc Template
jndi Template
jms
ldap
mq
rabbit
rocket
rest Template
MongoDB Template
elasticSearch Template
Session Template
Url Template
ThymeleafTemplate
Velocity Template

是模板方法模式和回调模式的结合，是Template Method不需要继承的另一种实现方式。Spring几乎所有的外接扩展都采用这种模式。

具体实现：

JDBC的抽象和对Hibernate的集成，都采用了一种理念或者处理方式，那就是模板方法模式与相应的Callback接口相结合。

采用模板方法模式是为了以一种统一而集中的方式来处理资源的获取和释放，以JdbcTempalte为例：

    public abstract class JdbcTemplate {  
         public final Object execute（String sql）{  
            Connection con=null;  
            Statement stmt=null;  
            try{  
                con=getConnection（）;  
                stmt=con.createStatement（）;  
                Object retValue=executeWithStatement（stmt,sql）;  
                return retValue;  
            }catch（SQLException e）{  
                 ...  
            }finally{  
                closeStatement（stmt）;  
                releaseConnection（con）;  
            }  
        }   
        protected abstract Object executeWithStatement（Statement   stmt, String sql）;  
    }  

query、 delete、 update、 insert 方法其实都是调用 execute 方法


为什么JdbcTemplate没有使用继承？

因为这个类的方法太多，但是我们还是想用到JdbcTemplate已有的稳定的、公用的数据库连接，那么我们怎么办呢？

我们可以把变化的东西抽出来作为一个参数传入JdbcTemplate的方法中。但是变化的东西是一段代码，而且这段代码会用到JdbcTemplate中的变量。怎么办？
那就用回调对象。在这个回调对象中定义一个操纵JdbcTemplate中变量的方法，我们去实现这个方法，就把变化的东西集中到这里了。然后我们再传入这个回调对象到JdbcTemplate，从而完成了调用。
————————————————
原文链接：https://blog.csdn.net/fei1234456/article/details/106693892


组合模式：


访问者模式

Chain
FilterChain

Visitor

Component
Children 
loop Children 


#  桥接模式：
可以根据客户的需求能够动态切换不同的数据源。比如我们的项目需要连接多个数据库，客户在每次访问中根据需要会去访问不同的数据库

# 责任链模式

ProxyMethodInvocation

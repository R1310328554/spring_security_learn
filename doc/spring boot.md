# springboot核心基础之spring.factories机制

在java spring cloud项目中，我们常常会在子模块中创建公共方法，那么在另外一个子模块中，需要加载配置文件的时候，往往Spring Boot 自动扫描包的时候，只会扫描自己模块下的类。这个是springboot约定俗成的内容。

抛出问题

如果想要被Spring容器管理的Bean的路径不再Spring Boot 的包扫描路径下，怎么办呢？也就是如何去加载第三方的Bean 呢？

目前较通用的方式有2种，一是使用注解进行实例化，而是使用spring.factories机制。
原文链接：https://blog.csdn.net/Javaesandyou/article/details/121789001

方法一：在Spring Boot Application 主类上使用@Import注解。

方法二：创建spring.factories文件

现在我们将其改造一下，采用spring.factories 的方式去加载SwaggerConfig类，在resources目录下新建一个META-INF 的目录，然后在

新建一个spring.factories 的文件，里面的内容为：
然后在把Spring Boot 启动类上的@Import注释掉，启动发现也可以把SwaggerConfig加载到Spring 容器中：


原文链接：https://blog.csdn.net/Javaesandyou/article/details/121789001

Spring.Factories这种机制实际上是仿照java中的SPI扩展机制实现的。

# 什么是SPI 机制
SPI 的全名为 Service Provider Interface.大多数开发人员可能不熟悉，因为这个是是针对厂商或者插件的。在java.util.ServiceLoader 的文档里有比较详细的介绍。

简单总结下Java SPI机制的思想。我们系统里抽象的各个模块，往往有很多不同的实现方案，比如 日志模块的方案，xml解析模块、jdbc模块的方案等。面向的对象设计里，我们一般推荐模块之间基于接口编程，模块之间不对实现类进行硬编码。一旦代码里涉及了具体的实现类，就违反了可插拔的原则，如果需要替换一种实现，就需要修改代码。为了实现在模块装配的时候能不在程序里动态指明，这就需要一种服务发现机制。

Java SPI 就是提供这样的一种机制：为某个接口寻找服务的实现的机制，有点类似IOC的思想，就是将装配的控制权移到程序之外，在模块化设计中这个机制很重要。

# Spring Boot 中的SPI 机制
在Spring boot 中也有一种类似的加载机制，它在
META-INFO/spring.factories文件中配置接口的实现类名称，然后在程序中读取这些配置文件并实例化。

这种自定义的SPI 机制就是Spring Boot Starter 实现的基础。

# Spring Factories实现原理
spring -core 包里定义了SpringFactoriesLoader 类，这个类实现了检索META-INF/spring.factories文件，并获取指定接口的配置的功能。在这个类中定义了两个对外的方法：

loadFactories 根据接口类获取其实现类的实例，这个方法返回的是对象列表
loadFactoryNames 根据接口获取其接口类的名称，这个方法返回的是类名的列表。
上面两个方法的关键都是从指定的ClassLoader中获取spring.factories文件，并解析得到类名列表，具体代码如下

从代码中可以看到，在这个方法中会遍历整个ClassLoader 中所有Jar包下的spring.factories文件，也就是我们可以在自己jar中配置spring.factories文件，不会影响到其他地方的配置，也不回被别人的配置覆盖。

spring.factories的是通过Properties解析得到的，所以我们在写文件中的内容都是按照下面这种方式配置的。

如果一个接口希望配置多个实现类，可以用","分割。

# spring-boot包中的spring.factories文件
在Spring Boot 的很多包中都能够找到spring.factories，下面就是spring-boot 包中的spring.factories文件。

spring.factories 一般放哪个工程下面？ 一般是放 autoconfigure 工程！ 

具体源码项目可以参考如下spring源码，源码从入口SpringApplication.run、SpringApplicationRunListeners listeners = getRunListeners(args);、getSpringFactoriesInstances、loadFactoryNames，如下图spring源码。

那么在该文件中能够定义哪些内容？

    ## Initializers
    ## ApplicationContextInitializer接口的作用是可以在ApplicationContext初始化之前，对Spring上下文属性进行修改，
    既refresh()前的一个钩子函数。
    org.springframework.context.ApplicationContextInitializer
     
    ## Application Listeners
    ## ApplicationListener 是Spring的监听器，可以通过对Spring上下文发送消息事件（由ApplicationContext. publishEvent进行消息发送），
    由对应的监听器进行捕获处理。 
    org.springframework.context.ApplicationListener
     
    ## Auto Configuration Import Listeners
    ## 当Spring使用ConfigurationClassParser加载完所有@Configuration后会再一次使用AutoConfigurationImportSelector排除所有不符合条件的@Configuration，
    排除完后则回调所有AutoConfigurationImportListener的监听器。可相当于加载并过滤完@Configuration后的钩子回调。
    org.springframework.boot.autoconfigure.AutoConfigurationImportListener
     
    # Auto Configuration Import Filters
    # 为 org.springframework.boot.autoconfigure.EnableAutoConfiguration定义的所有配置类增加ImportFilter来决定是否进行配置
    org.springframework.boot.autoconfigure.AutoConfigurationImportFilter
     
    # Auto Configure
    # 定义自动装配config类，当系统引入该jar包时， spring上下文将初始化这些config类
    org.springframework.boot.autoconfigure.EnableAutoConfiguration
     
    # Failure analyzers
    # 当spring bean的调用方法抛出特定异常时由自定义的特定FailureAnalyzer进行捕获并且进行处理。
    org.springframework.boot.diagnostics.FailureAnalyzer
     
    # Template availability providers
    # 定义jar内可用的模板转换器 在前后端分离场景下一般很少用的到。
    org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider
    
    除此之外，还有一些 不那么常用的：
    org.springframework.data.repository.core.support.RepositoryFactorySupport=org.springframework.data.redis.repository.support.RedisRepositoryFactory
    org.springframework.data.web.config.SpringDataJacksonModules=org.springframework.data.mongodb.config.GeoJsonConfiguration
    org.springframework.test.context.TestExecutionListener=
    org.springframework.test.context.ContextCustomizerFactory=

其中 最重要的，莫过于： org.springframework.boot.autoconfigure.EnableAutoConfiguration，它会把key对应的value当做一个 AutoConfiguration 来使用！
并且 自动的装配它！ Auto Configure 它！ 可以写比较复杂的东西。 Value上面一般需要 添加 @Configuration注解， xx 其他注解也可以！
value的命名通常是 XxxAutoConfiguration，主要是用来创建一些bean 实例！
怎么写？ 随便写就行！ 类上面甚至只需要一个@Component即可！
 

可以看到 基本上spring.factories文件中定义的东西，其key都是 Spring或SpringBoot框架的相关类； 一遍在SpringBoot启动过程中
动态的加载、启用它们！ 那如果随便自定义一个key， 可以吗？ 可以是可以，虽然不报错，但Spring不会拿他做任何事.. 因为SpringBoot 机制的原因！

org.springframework.core.io.support包，SpringFactoriesLoader类：
List<String> loadFactoryNames(Class<?> factoryClass, @Nullable ClassLoader classLoader)
Map<String, List<String>> loadFactories(Class<T> factoryClass, @Nullable ClassLoader classLoader)

loadFactoryNames 就是根据class 的名称，获取工厂的名称！
加载所需的类，并实例化它

loadFactories 是获取工厂, 并返回对应的类型，而不仅仅是字符串的名字！

两个方法的参数都是 class， 就是说，key 必须是一个类.. 

***需要特别注意的是， SpringFactoriesLoader会扫描每一个jar内部的 META-INF目录下的 spring.factories文件！！！***  
***需要特别注意的是， SpringFactoriesLoader会扫描每一个jar内部的 META-INF目录下的 spring.factories文件！！！***  
***需要特别注意的是， SpringFactoriesLoader会扫描每一个jar内部的 META-INF目录下的 spring.factories文件！！！***

—— 从哪里可以看到 扫描过程？？？ 

—— 所以，只要我们的maven dependencies中引入了某个jar， 其中的spring.factories文件就会起作用，会自动装配！！

# EnableAutoConfigurationImportSelector
1.3 才有， 后面2.x 之后，废弃了..

@SpringApplication注解标识在启动类上, 它上面会有一个注解@EnableAutoConfiguration也就是开启自动装配,
 继续跟踪@EnableAutoConfiguration注解, 它使用一个@Import 注解, 将一个类名为AutoConfigurationImportSelector注入到IOC容器中. 这个AutoConfigurationImportSelector类很重要, 因为springboot项目启动时, 会调用它里面一个方法loadFactoryNames, 这个方法会扫描pom文件中引入的其他starter中的spring.factories文件, 也就是说, 这个AutoConfigurationImportSelector类在springboot项目启动时会把第三方的jar包, 引入到IOC容器中,
  前提是jar里面resource目录要有META-INF/spring.factories文件

# starter
每个starter 都需要配置spring.factories！

官方命名空间

 前缀：spring-boot-starter-
 模式：spring-boot-starter-模块名
 举例：spring-boot-starter-web、spring-boot-starter-jdbc

自定义命名空间
后缀：-spring-boot-starter
 模式：模块-spring-boot-starter
 举例：mybatis-spring-boot-starter

怎么写一个starter ？

    1 继承spring-boot-starter-parent，比如
        
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.3.6.RELEASE</version>
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
    
    2 引入依赖：
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter</artifactId>
            </dependency>

    3 
    
        <!--‐导入配置文件处理器，配置文件进行绑定就会有提示-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
       
    4 CustomAutoConfiguration 自动配置类
    
    5 在META-INF/spring.factories文件配置自定义的 自动配置类 

spring-boot-configuration-processor 是什么？
关于自动提示的jar ,引入spring-boot-configuration-processor之后， 编译之后就会在META-INF文件夹下面生成一个spring-configuration-metadata.json的文件。

# 需要
在springboot官方文档中，特别提到，我们需要创建两个module ，其中一个是autoconfigure module  一个是 starter module ，其中 starter module 依赖 autoconfigure module

但是，网上仍然有很多blog在说这块的时候其实会发现他们其实只用了一个module，这当然并没有错，这点官方也有说明：

You may combine the auto-configuration code and the dependency management in a single module if you do not need to separate those two concerns

//如果不需要将自动配置代码和依赖项管理分离开来，则可以将它们组合到一个模块中。

# 看看官方的starter

了解了这两点之后，那么下面让我们一块去探索spingboot starter的奥秘吧。

按照springboot官方给的思路，starter的核心module应该是autoconfigure，所以我们直接去看spring-boot-autoconfigure里面的内容。

mybatis-spring-boot-starter 就是这样的, mybatis-spring-boot-starter 本身没有任何代码，它只是引入了依赖！
而 自动配置的代码在哪里？ 在 mybatis-spring-boot-autoconfigure ！

—— 注意， spring-boot-starter-web、spring-boot-starter-jdbc 等官方starter 有pom，但没有任何代码！统统如此！！
官方所有starter 详见： https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-starters?spm=a2c6h.12873639.article-detail.7.6d019c0evITbj5
它们统一使用一个 spring-boot-autoconfigure！
 

它的依赖是： 
    
      <dependencies>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
          <groupId>org.mybatis.spring.boot</groupId>
          <artifactId>mybatis-spring-boot-autoconfigure</artifactId>
        </dependency>
        <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis</artifactId>
        </dependency>
        <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis-spring</artifactId>
        </dependency>
      </dependencies>
  
可以看到其中有一个 mybatis-spring-boot-autoconfigure， 它是什么？
是专门用来，其中没有任何代码！
    
    <dependencies>
        <!-- Compile dependencies -->
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
    
        <!-- Optional dependencies -->
        <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis</artifactId>
          <optional>true</optional>
        </dependency>
        <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis-spring</artifactId>
          <optional>true</optional>
        </dependency>
        <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-api</artifactId>
          <optional>true</optional>
        </dependency>
        <dependency>
          <groupId>org.mybatis.scripting</groupId>
          <artifactId>mybatis-freemarker</artifactId>
          <optional>true</optional>
        </dependency>
        <dependency>
          <groupId>org.mybatis.scripting</groupId>
          <artifactId>mybatis-velocity</artifactId>
          <optional>true</optional>
        </dependency>
        <dependency>
          <groupId>org.mybatis.scripting</groupId>
          <artifactId>mybatis-thymeleaf</artifactId>
          <optional>true</optional>
        </dependency>
    
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-autoconfigure-processor</artifactId>
          <optional>true</optional>
        </dependency>
    
        <!-- @ConfigurationProperties annotation processing (metadata for IDEs) -->
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-configuration-processor</artifactId>
          <optional>true</optional>
        </dependency>
        
        <!-- Test dependencies -->
        <dependency>
          <groupId>com.h2database</groupId>
          <artifactId>h2</artifactId>
          <scope>test</scope>
        </dependency>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-jdbc</artifactId>
          <scope>test</scope>
        </dependency>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-test</artifactId>
          <scope>test</scope>
          <exclusions>
            <exclusion>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
            </exclusion>
          </exclusions>
        </dependency>
      </dependencies>

# 
spring-boot-starter-data-jdbc， 
    parent ： spring-boot-starters
        
          <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starters</artifactId>
            <version>2.1.3.RELEASE</version>
          </parent>
          
    依赖 ->  spring-boot-starter-jdbc 、 spring-data-jdbc 即：
    
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-jdbc</artifactId>
          <version>2.1.3.RELEASE</version>
          <scope>compile</scope>
        </dependency>
        <dependency>
          <groupId>org.springframework.data</groupId>
          <artifactId>spring-data-jdbc</artifactId>
          <version>1.0.5.RELEASE</version>
          <scope>compile</scope>
        </dependency>
      </dependencies>

而 spring-boot-starters 没有dependencies，即它不依赖于任何其他jar，而parent是：

      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-parent</artifactId>
        <version>2.1.3.RELEASE</version>
        <relativePath>../spring-boot-parent</relativePath>
      </parent>

而 spring-boot-starter-jdbc 依赖于
    
      <dependencies>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter</artifactId>
          <version>2.1.3.RELEASE</version>
          <scope>compile</scope>
        </dependency>
        <dependency>
          <groupId>com.zaxxer</groupId>
          <artifactId>HikariCP</artifactId>
          <version>3.2.0</version>
          <scope>compile</scope>
        </dependency>
        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-jdbc</artifactId>
          <version>5.1.5.RELEASE</version>
          <scope>compile</scope>
        </dependency>
      </dependencies>


spring-boot-starter 是重点：

      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starters</artifactId>
        <version>2.1.6.RELEASE</version>
      </parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      
      <dependencies>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot</artifactId>
          <version>2.1.6.RELEASE</version>
          <scope>compile</scope>
        </dependency>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-autoconfigure</artifactId>
          <version>2.1.6.RELEASE</version>
          <scope>compile</scope>
        </dependency>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-logging</artifactId>
          <version>2.1.6.RELEASE</version>
          <scope>compile</scope>
        </dependency>
        <dependency>
          <groupId>javax.annotation</groupId>
          <artifactId>javax.annotation-api</artifactId>
          <version>1.3.2</version>
          <scope>compile</scope>
        </dependency>
        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-core</artifactId>
          <version>5.1.8.RELEASE</version>
          <scope>compile</scope>
        </dependency>
        <dependency>
          <groupId>org.yaml</groupId>
          <artifactId>snakeyaml</artifactId>
          <version>1.23</version>
          <scope>runtime</scope>
        </dependency>
      </dependencies>
      

spring-boot-parent 本身没有依赖，其parent是 spring-boot-dependencies

      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>2.1.6.RELEASE</version>
        <relativePath>../spring-boot-dependencies</relativePath>
      </parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-parent</artifactId>
      <version>2.1.6.RELEASE</version>
      <packaging>pom</packaging>

spring-boot-dependencies 没有parent，没有 dependencies依赖，
而只是通过dependencyManagement 指定了一系列的dependency 的版本号！包括 spring-boot-xxx，而其他第三方的


# Springboot 自带有哪些 XxxAutoConfiguration ？



 
# @Enable注解
@Enable注解 通常用在某个 XxxAutoConfiguration 类之上，然后通过@Import 引入其他的 Configuration ！

为什么这样搞？ 目的在于手动的方式启动 某些XxxAutoConfiguration， 而不是自动的启用！

因为.. 某些XxxAutoConfiguration， 可能是你只要引入相应的jar就可以了，因为 你通过在pom中引入那个依赖，就明确表示了需要、想直接使用那个jar的相关bean等特性！

但是，有的jar可能功能比较多，我依赖了它，但是并不想使用其中的全部 XxxAutoConfiguration， 或者其中某个XxxAutoConfiguration 是很少用的，一般情况不会使用它。
这个时候，如果你自动就启用它， 是不太好的，因此，在XxxAutoConfiguration基础上提供一个，为其提供一个@EnableXxx 注解是非常恰当的！

比如 hutool的EnableSpringUtil，是手动方式启用 SpringUtil，为什么要手动，因为一般情况下，可能我们都有了自己的 SpringUtil, 不需要它来提供，所以，手动方式就比较合理。

—— XxxAutoConfiguration 其实也是可以设置条件的，是不是这样就可以替代@Enable注解呢？ 
—— 其实也差不多的，不过，@Enable注解含义更明确！

 Springboot 自带有哪些@Enable注解？
 
 EnableTransactionManagementConfiguration
 EnableTransactionManagement
 EnableWebMvc 是Spring mvc的东西！ 当然，它本质上也是启用了一些配置，实例化、装配了一些bean！
 EnableCaching 相当于是之前的<cache:annotation-driven/>，实际就是启用了对 @Cacheable等的支持（默认不支持）！
 EnableJdbcAudit 
EnableJdbcRepositories
EnableSwagger2



- oauth2 的几个： 
EnableOAuth2Client
EnableResourceServer
EnableAuthorizationServer

可以看到， Springboot中，这样的 @Enable注解并不多，而Spring Cloud中是比较多的！


# RepositoryFactoryBeanSupport

# starter


# auto configurer


# spring.factories

原理
SpringFactoriesLoader载荷从和实例化给定类型的工厂“META-INF / spring.factories”文件，其可存在于在类路径多个JAR文件。 该spring.factories文件必须为Properties格式，其中的关键是接口或抽象类的完全合格的名称和值是一个逗号分隔的实现类名的列表。 例如：
example.MyService=example.MyServiceImpl1,example.MyServiceImpl2
其中example.MyService是接口的名称，和MyServiceImpl1和MyServiceImpl2两种实现。

 SpringFactoriesLoader 是什么？ 是每一个SpringBoot都有的吗？ 是的，必须..！  
# 

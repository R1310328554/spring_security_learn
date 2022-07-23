# 类型转换
当我们使用Ioc的时候，我们需要先配置一个bean，其实就是给出一个 Bean Definition，然后Spring容器会根据配置创建一个bean实例，
通常需要的配置有：
bean 的name、class，各属性的值，但是呢，bean的各属性可能是各种各样的类型，而我们配置过程中，我们只能给出String或数值这样的字面值

因此，Spring容器需要进行类型转换，也就是常常遇见的 TypeConvert。 

Spring使用了 TypeConverter，即转换器， 同时有 Formatter即格式化器、 等概念。

Ioc 实例bean时需要 TypeConvert， 同样的道理，我们发起http请求到Spring MVC，也肯定需要涉及 TypeConvert， 因为http 发过来的原始数据，通常不会对每一个字段指定其 数据类型。

那么，这个过程是怎么完成的呢？ 其实一般就是通过 字段名进行匹配，Ioc配置bean或 http请求原始数据时, 如果有一个

发现bean class 对应字段是 其他类型， 比如 com.xxx.Abc，那就寻找 java.lang.String -> com.xxx.Abc的TypeConverter转换器

如果找到，那就直接使用它，如果没有找到，那么使用兼容的 转换器也可以！  当然， 所有的转换器，都应该提前实例化、注册好，后面就可以直接用了！

这个查找转换器的过程是：
org.springframework.beans.PropertyEditorRegistrySupport.findCustomEditor


转换器 要求是同名字段，不同名的可不可以？
其实也可以，但这就需要我们做更多的工作，需要把手动对不同字段做映射： 比如： f1 -> f2

当然，如果字段都是String， 那自然不需要 TypeConverter了！
org.springframework.beans.AbstractNestablePropertyAccessor.setPropertyValue(org.springframework.beans.AbstractNestablePropertyAccessor.PropertyTokenHolder, org.springframework.beans.PropertyValue)

关键方法是：
TypeConverterDelegate.convertIfNecessary(java.lang.String, java.lang.Object, java.lang.Object, java.lang.Class<T>, org.springframework.core.convert.TypeDescriptor)

可以看到，Spring是先查找custom ，如果没有找到 CustomEditor，那么

editor = this.findDefaultEditor(requiredType);



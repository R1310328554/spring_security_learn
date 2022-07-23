package com.lk.learn.springboot.demo.domain;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import java.lang.reflect.Constructor;
import java.util.Arrays;


@Configuration
public class MyPostProcessor {


}


/**
 * BeanPostProcessor 在所有其他 PostProcessor 之后执行！
 *
 * 这个做什么呢？ 就是在 bean 的初始化前、后，使用bean、beanName 做一些事情，比如统计什么的！
 */
@Component
class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 可以根据不同的bean执行不同的操作
//        System.out.println("postProcessBeforeInitialization()..." + beanName + "=>" + bean);
        if (beanName.equalsIgnoreCase("eventListner")){
            System.out.println("eventListner bean..." + beanName + "=>" + bean);
            System.out.println("eventListner is loading...");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equalsIgnoreCase("eventListner")){
            System.out.println("postProcessAfterInitialization()..." + beanName + "=>" + bean);
            System.out.println("eventListner is loaded...");
        }
        return bean;
    }
}


/*

通过beanFactory可以获取bean的示例或定义等。同时可以修改bean的属性，这是和BeanPostProcessor最大的区别。

bean工厂的bean属性处理容器，说通俗一些就是可以管理我们的bean工厂内所有的beandefinition（未实例化）数据，可以随心所欲的修改属性。
 */
@Component
class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    // Caused by: java.lang.NoSuchMethodException: com.lk.learn.springboot.demo.domain.MyPostProcessor$MyBeanFactoryPostProcessor.<init>()
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("MyBeanFactoryPostProcessor...postProcessBeanFactory...");
        int count = beanFactory.getBeanDefinitionCount();
        String[] names = beanFactory.getBeanDefinitionNames();
        System.out.println("当前BeanFactory中有"+count+" 个Bean");
        System.out.println(Arrays.asList(names));
    }
}


@Component
class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("environment = [" + environment + "], application = [" + application + "]");
    }
}

//@Component
//class MyEvaluationContextPostProcessor implements EvaluationContextPostProcessor {
//
//}

@Component
class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("bean = [" + bean + "], beanName = [" + beanName + "]");
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

}


@Component
class MySmartInstantiationAwareBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {
    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("beanClass = [" + beanClass + "], beanName = [" + beanName + "]");
        return beanClass;
    }

/*
    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
        try {
            return new Constructor[]{beanClass.getConstructor()};
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }
}

//@Component
//class MyObjectPostProcessor implements ObjectPostProcessor {
//
//}

@Component
class MyMergedBeanDefinitionPostProcessor implements MergedBeanDefinitionPostProcessor {

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> aClass, String s) {
//        System.out.println("MyMergedBeanDefinitionPostProcessor.postProcessMergedBeanDefinition");
    }

    @Override
    public void resetBeanDefinition(String beanName) {
        System.out.println("MyMergedBeanDefinitionPostProcessor.resetBeanDefinition");
    }
}


@Component
class MyDestructionAwareBeanPostProcessor implements DestructionAwareBeanPostProcessor {

    @Override
    public void postProcessBeforeDestruction(Object o, String s) throws BeansException {
        System.out.println("MyDestructionAwareBeanPostProcessor.postProcessBeforeDestruction");
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return true;
    }
}


@Component
class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        System.out.println("MyBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("MyBeanDefinitionRegistryPostProcessor.postProcessBeanFactory");
    }
}

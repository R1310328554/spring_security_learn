package com.lk.learn.springboot.demo.domain;

import org.springframework.aop.TargetClassAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.tags.ArgumentAware;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;


@Configuration
public class MyAware {

    @Component
    class MyApplicationContextAware implements ApplicationContextAware {

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            System.out.println("applicationContext = [" + applicationContext + "]");
        }
    }

    @Component
    class MyApplicationEventPublisherAware implements ApplicationEventPublisherAware {

        @Override
        public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
            System.out.println("applicationEventPublisher = [" + applicationEventPublisher + "]");
        }
    }


    @Component
    class MyBeanNameAware implements BeanNameAware {

        @Override
        public void setBeanName(String s) {
            System.out.println("s = [" + s + "]");// 全局就一个
        }
    }

    @Component
    class MyBeanFactoryAware implements BeanFactoryAware {

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            System.out.println("beanFactory = [" + beanFactory + "]");// 全局就一个
        }
    }

    @Component
    class MyBeanClassLoaderAware implements BeanClassLoaderAware {

        @Override
        public void setBeanClassLoader(ClassLoader classLoader) {
            // classLoader = [sun.misc.Launcher$AppClassLoader@18b4aac2] 全局就一个..
            System.out.println("classLoader = [" + classLoader + "]");
        }
    }

    @Component
    class MyEmbeddedValueResolverAware implements EmbeddedValueResolverAware {

        @Override
        public void setEmbeddedValueResolver(StringValueResolver resolver) {
            // resolver = [org.springframework.beans.factory.config.EmbeddedValueResolver@22367b8]
            System.out.println("resolver = [" + resolver + "]");//  全局就一个..
        }
    }

    @Component
    class MyEnvironmentAware implements EnvironmentAware {

        @Override
        public void setEnvironment(Environment environment) {
            System.out.println("environment = [" + environment + "]");// 全局就一个  StandardServletEnvironment
        }
    }


    @Component
    class MyImportAware implements ImportAware {

        @Override
        public void setImportMetadata(AnnotationMetadata importMetadata) {
            System.out.println("importMetadata = [" + importMetadata + "]");// 没有执行
        }
    }


    @Component
    class MyMessageSourceAware implements MessageSourceAware {

        @Override
        public void setMessageSource(MessageSource messageSource) {
            // 全局就一个
            System.out.println("messageSource = [" + messageSource + "]"); // AnnotationConfigServletWebServerApplicationContext
        }
    }


    @Component
    class MyServletConfigAware implements ServletConfigAware {

        @Override
        public void setServletConfig(ServletConfig servletConfig) {
            System.out.println("servletConfig = [" + servletConfig + "]");
        }
    }

    @Component
    class MyServletContextAware implements ServletContextAware {

        @Override
        public void setServletContext(ServletContext servletContext) {
            // 全局就一个
            System.out.println("servletContext = [" + servletContext + "]");// servletContext = [org.apache.catalina.core.ApplicationContextFacade@5ff2e84b]
        }
    }

/*
    @Component
    class MyArgumentAware implements ArgumentAware {
        @Override
        public void addArgument(Object argument) throws JspTagException {
            System.out.println("argument = [" + argument + "]");
        }
    }
    */

    @Component
    class MyResourceLoaderAware implements ResourceLoaderAware {

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            // 同， AnnotationConfigServletWebServerApplicationContext
            System.out.println("resourceLoader = [" + resourceLoader + "]");// 全局就一个
        }
    }

//    @Component
    class MyTargetClassAware implements TargetClassAware {

        @Override
        public Class<?> getTargetClass() {
            System.out.println("MyTargetClassAware.getTargetClass");
            return null;
        }
    }

}

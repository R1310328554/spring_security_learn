package com.lk.learn.springmvc.demo.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

//@Configuration
public class MyDateConvertCustoms implements Jackson2ObjectMapperBuilderCustomizer {

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        // 启动的时候会执行！ 但是.. deserializerByType方法 似乎没有作用， 可能是顺序问题，被Spring Boot 的auto config 覆盖了!!!
        System.out.println("jacksonObjectMapperBuilder = [" + jacksonObjectMapperBuilder + "]");
			// 覆盖默认的Date反序列化，第一个参数为需要反序列化的类，第二个为具体的序列化格式
      jacksonObjectMapperBuilder.deserializerByType(
                Date.class
                ,new DateDeserializers.DateDeserializer(
                      DateDeserializers.DateDeserializer.instance
                        , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")// 默认"yyyy-MM-dd'T'HH:mm:ss.SSSX")
                        , null));
    }


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    /*
        https://cloud.tencent.com/developer/article/1551379
     */
//    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                // 反序列化
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(FORMATTER))
                // 序列化
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(FORMATTER));
    }

//    @Bean
    public FormattingConversionService conversionService() {
        // 我们使用false参数创建DefaultFormattingConversionService，这意味着Spring默认情况下不会注册任何日期格式化程序。
        DefaultFormattingConversionService conversionService =
                new DefaultFormattingConversionService(false);
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        registrar.registerFormatters(conversionService);
        // other desired formatters
        return conversionService;
    }

//    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
        httpMessageConverter.setObjectMapper(objectMapper);
        return httpMessageConverter;
    }

}


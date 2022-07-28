package com.lk.learn.springmvc.demo.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/*
Spring 本身没有提供 JSR 303 的实现，Hibernate Validator 实现了 JSR 303，所以必须在项目中加入来自 Hibernate Validator 库的 jar 文件，下载地址为 http://hibernate.org/validator/。本节使用版本为 hibernate-validator-5.1.0.Final-dist.zip，复制其中的 3 个 jar 文件即可，Spring 将会自动加载并装配。

@DateTimeFormat 只用于输入参数的格式化
原因是Post请求参数在body中，需要反序列化成对象。默认是jackson类库来进行反序列化，并不触发`@DateTimeFormat`注解机制。

@JsonFormat 只用于输出格式化
@NumberFormat 也是，只用于输出格式化
 */
@Data
public class User {
    @NotNull(message = "用户id不能为空")
    private Integer id;

    @NotNull // NotNull的默认message 是{javax.validation.constraints.NotNull.message}， 即：不能为null
    @Length(min = 2, max = 8, message = "用户名不能少于2位大于8位")
    private String name;

    @NotNull
    private Date hiredate;

    //@Email 已过期 @Email(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+", message = "邮箱格式不正确")
    @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+", message = "邮箱格式不正确")
    private String email;

    /** 省略setter和getter方法*/

    private Number balance;

    private String  balance2;

    @NumberFormat(pattern = "###.##")
    private Number balance3;

    @NumberFormat(pattern = "#,###")
    private Double num4;


    // @DateTimeFormat // 默认是 yyyy-MM-dd'T'HH:mm:ss.SSSXXX
    // 优先级并 不高..
    @DateTimeFormat(pattern = "yyyy.MM.dd") // 如果存在自定义的 DateFormatter， 那么这里会被忽略，不执行！ 因为只选取一个，一个不行就异常！
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bornDate;

    @DateTimeFormat(pattern = "yyyy.MM.dd") // 日期 转为 long
    private Long bornDate2;

    @DateTimeFormat(pattern = "yyyy.MM.dd") // long转为 日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ) //只用于输出格式化！ //  局部的特点：灵活，但是配置繁琐，不统一（每个字段都要加）
    private LocalDateTime bornDate3; // 这里只能使用 LocalDateTime， 不能使用 LocalDate！ 大概是因为格式有HH:mm:ss

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bornDate4;

    // 如果再个性化一些`@JsonFormat` 可以被`@JsonDeserialize`和`@JsonSerialize` 代替。但是它们的`using`参数需要你自己实现为你对应的时间类型类型。
    // 如果`@JsonFormat`、`@JsonDeserialize`和`@JsonSerialize`同时存在`@JsonFormat`的优先级要更高。
//    @JsonSerialize(using = "yyyy-MM-dd")
//    @JsonDeserialize(using = "yyyy-MM-dd")
    private Date bornDate5;


    // 输入 1,123 ，转换为 1123； 格式要求，每三个数字需要一个逗号分隔
    @NumberFormat(style= NumberFormat.Style.NUMBER,pattern="#,###")
    private int total;

    // 输入 32%， 转换为 0.32， 也可以是 3.2 ， 那么就不变！
    @NumberFormat(style=NumberFormat.Style.PERCENT)
    private double discount;

    // 输入 ￥123456，转换为 123456.0 ，如果没有前缀￥，那么不转换..但是如果是 $123 就不行，因为默认locale 是china，所以$不认识
    @NumberFormat(style=NumberFormat.Style.CURRENCY)
    private double money;

}

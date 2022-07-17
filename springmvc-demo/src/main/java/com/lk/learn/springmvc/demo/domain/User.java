package com.lk.learn.springmvc.demo.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/*
Spring 本身没有提供 JSR 303 的实现，Hibernate Validator 实现了 JSR 303，所以必须在项目中加入来自 Hibernate Validator 库的 jar 文件，下载地址为 http://hibernate.org/validator/。本节使用版本为 hibernate-validator-5.1.0.Final-dist.zip，复制其中的 3 个 jar 文件即可，Spring 将会自动加载并装配。

 */
@Data
public class User {
    @NotNull(message = "用户id不能为空")
    private Integer id;
    @NotNull
    @Length(min = 2, max = 8, message = "用户名不能少于2位大于8位")
    private String name;

    //@Email 已过期 @Email(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+", message = "邮箱格式不正确")
    @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+", message = "邮箱格式不正确")
    private String email;

    /** 省略setter和getter方法*/
}

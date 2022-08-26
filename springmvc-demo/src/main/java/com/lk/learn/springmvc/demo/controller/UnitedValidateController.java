package com.lk.learn.springmvc.demo.controller;

import com.lk.learn.springmvc.demo.domain.User;
import com.lk.learn.springmvc.demo.domain.UserForm;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/valid2")
public class UnitedValidateController {

    @RequestMapping(value = { "/a" } )
    // 此处，如果每个方法校验参数都加上一个BindingResult 对象来接受校验后的结果，那就不能抛出MethodArgumentNotValidException，
    // 我们这里是要统一处理而不是单独处理每一个校验结果，所以不加BindingResult让它直接抛异常
    // public String aa(@Validated User user, BindingResult result) throws Exception {
    public String aa(@Validated User user) throws Exception {
        System.out.println("UnitedValidateController.aa " + "    user = [" + user + "]");// 参数校验失败， 不会执行方法体！
        return "initbinder/user.jsp";
    }

    @RequestMapping(value = { "/b" } )
    public String bb(@Valid User user) throws Exception {
        System.out.println("UnitedValidateController.bbb " + "    user = [" + user + "]");
        return "initbinder/user.jsp";
    }

}

package com.lk.learn.springmvc.demo.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CannotHaveBlankValidator implements ConstraintValidator<CannotHaveBlank, String> {

    @Override
    public void initialize(CannotHaveBlank constraintAnnotation) {
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context/* <2>*/) {
        //null时不进行校验
        if (value != null && value.contains(" ")) {
//            <3>
            //获取默认提示信息
            String defaultConstraintMessageTemplate = context.getDefaultConstraintMessageTemplate();
            System.out.println("default message :" + defaultConstraintMessageTemplate);
            //禁用默认提示信息
            context.disableDefaultConstraintViolation();
            //设置提示语
            context.buildConstraintViolationWithTemplate("can not contains blank").addConstraintViolation();
            return false;
        }
        return true;
    }
}




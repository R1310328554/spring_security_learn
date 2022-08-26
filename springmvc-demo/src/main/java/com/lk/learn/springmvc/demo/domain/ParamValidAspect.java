package com.lk.learn.springmvc.demo.domain;

import com.lk.learn.springmvc.demo.domain.BusinessCode;
import com.lk.learn.springmvc.demo.domain.Result;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数校验统一处理
 * 这种使用@Aspect的统一处理参数校验的方法需要 @RequestMapping方法的最后声明BindingResult 参数， 否则spring直接就将异常抛出
 *
 *
 *  使用 @Around 等 需要引入 aop 的相关jar ， 或 spring-boot-starter-aop， why ？
 *
 */
@Log4j
//@Aspect  // 使用切面进行 参数校验！
//@Component
public class ParamValidAspect {

    /**
     * 校验错误处理
     * @param joinPoint
     * @param bindingResult
     * @return
     * @throws Throwable
     */
     // 切入点表达式，指向要执行校验的方法
    @Around("execution(* com.**.controller.*.*(..)) && args(..,bindingResult)")
    public Object validateParam(ProceedingJoinPoint joinPoint, BindingResult bindingResult) throws Throwable {
        Object obj = null;
        if (bindingResult.hasErrors()) {
            // 有校验错误
            obj = getErrors(bindingResult);
        } else {
            // 没有错误方法继续执行
            obj = joinPoint.proceed();
        }
        return obj;
    }


    /**
     * 解析校验错误
     * @param bindingResult
     * @return
     */
    public ResponseEntity getErrors(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        allErrors.forEach(x -> {
            FieldError fieldError = (FieldError) x;
            String msg = x.getDefaultMessage();
            errors.add(fieldError.getField() + msg);
        });
        // 自己封装返回错误的格式
        Result<Object> responseData = Result.fail(BusinessCode.PARAM_TYPE_IS_INVALID , errors.toString());
        ResponseEntity responseEntity = new ResponseEntity<Result>(responseData, HttpStatus.OK);
        return responseEntity;
    }

}


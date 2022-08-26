package com.lk.learn.springmvc.demo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController("/validate3") // 这里的validate3不是 @RequestMapping("/validate3") ！ @RequestMapping 可以省去，所以下面的 /bar 直接访问就行；
@Validated
// <1> 对这个类里面的每个 @RequestMapping 方法进行校验！
public class ValidationConstraintsTestController {


    /**
     * http://localhost:8080/bar?age=21&name=ae323&asd=aaa
     * @return
     */
    @RequestMapping("/bar")
    public @NotBlank/* <2>*/ String bar(@Min(18) Integer age/* <3>*/, @NotBlank String name, String asd) {
        System.out.println("age = [" + age + "], name = [" + name + "], asd = [" + asd + "]");
        if (asd == null) {
            return "";
        }
        return "" + asd;
    }

    @ExceptionHandler(ConstraintViolationException.class) // 这里的ExceptionHandler 优先级高于全局的那个。
    public Map handleConstraintViolationException(ConstraintViolationException cve){
        System.out.println("BarController.handleConstraintViolationException    " + cve);
        Set<ConstraintViolation<?>> cves = cve.getConstraintViolations();// <4>
        for (ConstraintViolation<?> constraintViolation : cves) {
            System.out.println(constraintViolation.getMessage());
        }
        Map map = new HashMap();
        map.put("errorCode", 500);
        map.put("errorMsg", cve.getMessage());
        return map;
    }

}

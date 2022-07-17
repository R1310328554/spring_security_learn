package com.lk.learn.springmvc.demo;

import com.lk.learn.springmvc.demo.domain.BusinessCode;
import com.lk.learn.springmvc.demo.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

//@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
       return Result.fail(BusinessCode.PARAM_TYPE_IS_NOT_MATCH, msg);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        return Result.fail(BusinessCode.PARAM_TYPE_IS_NOT_MATCH, ex.getMessage());
    }

    @ExceptionHandler({NotReadablePropertyException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result handleNotReadablePropertyException(NotReadablePropertyException ex) {
        return Result.fail(BusinessCode.PARAM_TYPE_IS_NOT_MATCH, ex.getMessage());
    }


    /*
        同时需要满足 ExceptionHandler、 ResponseStatus
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result handleException(Exception ex) {
        log.error("未知系统错误", ex);
        return Result.fail(BusinessCode.SYSTEM_ERROR, ex.getMessage());
    }

    // HttpStatus.BAD_REQUEST
    //
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Result handle404Exception(Exception ex) {
        log.error(" NoHandlerFoundException 错误", ex);
        return Result.fail(BusinessCode.SYSTEM_ERROR, ex.getMessage());
    }


    /**
     * 404异常处理<br>
     * 返回ResponseEntity，保留网络请求404状态码
     *
     * @author yilabao
     * @date 2021年1月23日
     * @param e
     * @return ResponseEntity<ReturnVO<String>>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result<String>> noHandleFoundException(NoHandlerFoundException e) {
        String message = e.getMessage();
        System.out.println("message = " + message);
        Result<String> errorBody = Result.fail(BusinessCode.NOTFOUND_ERROR, message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    } // https://blog.csdn.net/u013727805/article/details/113056544


    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> factoryCustomizer() {
        return factory -> {
            // 出现404跳转到404页面
            ErrorPage notFound = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
            // 出现500跳转到500页面
            ErrorPage sysError = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
            factory.addErrorPages(notFound,sysError);
        };
    } // https://blog.csdn.net/millery22/article/details/123498962
}

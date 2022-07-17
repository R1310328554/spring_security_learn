package com.okta.spring.AuthorizationServerApplication.cfg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luokai 2022年7月9日
 */
@Component("failureAuthentication")
public class FailureAuthentication extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        Result result = new Result();
        result.setCode(1000);
        result.setMsg("登录失败: " + e.getMessage());
        response.setStatus(401);
        Map<String, Object> map = new HashMap<>();
        // 通过异常参数可以获取登录失败的原因，进而给用户一个明确的提示。
        map.put("status", 401);
        if (e instanceof LockedException) {
            map.put("msg", "账户被锁定，登录失败!");
        }else if(e instanceof BadCredentialsException){
            map.put("msg","账户名或密码输入错误，登录失败!");
        }else if(e instanceof DisabledException){
            map.put("msg","账户被禁用，登录失败!");
        }else if(e instanceof AccountExpiredException){
            map.put("msg","账户已过期，登录失败!");
        }else if(e instanceof CredentialsExpiredException){
            map.put("msg","密码已过期，登录失败!");
        }else{
            map.put("msg","登录失败!");
        }
        result.setData( map);
        ObjectMapper mapper = new ObjectMapper();
        writer.println(mapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}

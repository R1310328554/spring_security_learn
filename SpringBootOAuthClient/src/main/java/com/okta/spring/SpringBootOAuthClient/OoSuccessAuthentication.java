package com.okta.spring.SpringBootOAuthClient;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author luokai 2022年7月9日
 */
@Component("successAuthentication")
public class OoSuccessAuthentication extends SavedRequestAwareAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        System.out.println("到这里已经登录成功， 本应该返回页面，不过此处故意返回json数据， 后面可以正常访问受保护页面不受影响！！");
        System.out.println("onAuthenticationSuccess request = [" + request + "], response = [" + response + "], authentication = [" + authentication + "]");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        JSONObject result = new JSONObject();
        result.put("code",0);
        result.put("msg","success");
        result.put("data",authentication);
        result.put("msg2","成功");
        ObjectMapper mapper = new ObjectMapper();
        writer.println(mapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}

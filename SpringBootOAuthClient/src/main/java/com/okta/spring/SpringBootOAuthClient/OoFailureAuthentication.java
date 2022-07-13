package com.okta.spring.SpringBootOAuthClient;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author luokai 2022年7月9日
 */
@Component("failureAuthentication")
public class OoFailureAuthentication extends SimpleUrlAuthenticationFailureHandler {


    /**
     * 如果是 用户名密码错误，不会进这里来；
     *
     * 这里通常是 authorization_request_not_found 异常的时候才进来！
     *
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // authorization_request_not_found
        System.out.println("onAuthenticationFailure request = [" + request + "], response = [" + response + "], exception = [" + exception + "]");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        JSONObject result = new JSONObject();
        result.put("code",500);
        result.put("msg","fail");
        result.put("ex",exception.getMessage());
        result.put("msg2","登录失败");// 为什么出现问号乱码？todo
        ObjectMapper mapper = new ObjectMapper();

        // [invalid_user_info_response] An error occurred while attempting to retrieve the UserInfo Resource: 401 null]
        // InvalidDefinitionException: Direct self-reference leading to cycle (through reference chain:
        //  com.alibaba.fastjson.JSONObject["ex"]->org.springframework.security.oauth2.core.OAuth2AuthenticationException["cause"]->org.springframework.web.client.HttpClientErrorException$Unauthorized["mostSpecificCause"])
        writer.println(mapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}

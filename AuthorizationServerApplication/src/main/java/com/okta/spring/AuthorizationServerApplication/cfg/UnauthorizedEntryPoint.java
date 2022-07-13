package com.okta.spring.AuthorizationServerApplication.cfg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.spring.AuthorizationServerApplication.cfg.Result;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author luokai 2022年7月9日
 */
@Component("unauthorizedEntryPoint")
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Map<String, String[]> paramMap = request.getParameterMap();
        StringBuilder param = new StringBuilder();
        paramMap.forEach((k, v) -> {
            param.append("&").append(k).append("=").append(v[0]);
        });
        param.deleteCharAt(0);
        String isRedirectValue = request.getParameter("isRedirect");
//        if (!StringUtils.isEmpty(isRedirectValue) && Boolean.valueOf(isRedirectValue)) {
        if (StringUtils.isEmpty(isRedirectValue) || Boolean.valueOf(isRedirectValue)) {
            response.sendRedirect("http://192.168.1.103:8083/authPage/login?"+param.toString());
            // java.lang.IllegalStateException: Cannot call sendRedirect() after the response has been committed
            response.sendRedirect("http://192.168.1.103:8080/#/login");
            return;
        }

		// 如果前端ajax请求兼容处理了登录页面的响应（即text/html，而非application/json，前端处理见5、客户端前端配置），
		// 可以不需要返回json结果，直接response.sendRedirect到登录界面，
        // 建议前端ajax兼容处理sendRedirect方式返回的页面，方便非ajax请求直接重定向到登录页面，
        // 因为在这里很难判断最原始的请求（客户端的前端请求）是否为ajax请求
        String authUrl = "http://oauth.com/auth/oauth/authorize?"+param.toString()+"&isRedirect=true";
        authUrl = "http://192.168.1.103:8082/login/oauth2/code/?"+param.toString()+"&isRedirect=true";
        authUrl = "http://192.168.1.103:8081/auth/oauth/authorize?"+param.toString()+"&isRedirect=true";
        authUrl = "/auth/oauth/authorize?"+param.toString()+"&isRedirect=true";
        Result result = new Result();
        result.setCode(800);
        result.setData(authUrl);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        writer.print(mapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
